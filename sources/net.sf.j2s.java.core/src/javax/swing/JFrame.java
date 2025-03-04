/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 1997, 2009, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package javax.swing;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.JSFrame;
import java.awt.LayoutManager;
import java.awt.event.WindowEvent;
//

//BH: Added rootPane.addNotify(); // builds a peer for the root pane
//BH: Added ability to embed the frame in a div of the web page.

/**
 * An extended version of <code>java.awt.Frame</code> that adds support for
 * the JFC/Swing component architecture. You can find task-oriented
 * documentation about using <code>JFrame</code> in <em>The Java Tutorial</em>,
 * in the section <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/frame.html">How
 * to Make Frames</a>.
 * 
 * <p>
 * The <code>JFrame</code> class is slightly incompatible with
 * <code>Frame</code>. Like all other JFC/Swing top-level containers, a
 * <code>JFrame</code> contains a <code>JRootPane</code> as its only child. The
 * <b>content pane</b> provided by the root pane should, as a rule, contain all
 * the non-menu components displayed by the <code>JFrame</code>. This is
 * different from the AWT <code>Frame</code> case. As a conveniance
 * <code>add</code> and its variants, <code>remove</code> and
 * <code>setLayout</code> have been overridden to forward to the
 * <code>contentPane</code> as necessary. This means you can write:
 * 
 * <pre>
 * frame.add(child);
 * </pre>
 * 
 * And the child will be added to the contentPane. The content pane will always
 * be non-null. Attempting to set it to null will cause the JFrame to throw an
 * exception. The default content pane will have a BorderLayout manager set on
 * it. Refer to {@link javax.swing.RootPaneContainer} for details on adding,
 * removing and setting the <code>LayoutManager</code> of a <code>JFrame</code>.
 * <p>
 * Unlike a <code>Frame</code>, a <code>JFrame</code> has some notion of how to
 * respond when the user attempts to close the window. The default behavior is
 * to simply hide the JFrame when the user closes the window. To change the
 * default behavior, you invoke the method {@link #setDefaultCloseOperation}. To
 * make the <code>JFrame</code> behave the same as a <code>Frame</code>
 * instance, use
 * <code>setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)</code>.
 * <p>
 * For more information on content panes and other features that root panes
 * provide, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/toplevel.html"
 * >Using Top-Level Containers</a> in <em>The Java Tutorial</em>.
 * <p>
 * In a multi-screen environment, you can create a <code>JFrame</code> on a
 * different screen device. See {@link java.awt.JSFrame} for more information.
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more information see
 * <a href="package-summary.html#threading">Swing's Threading Policy</a>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 * 
 * @see JRootPane
 * @see #setDefaultCloseOperation
 * @see java.awt.event.WindowListener#windowClosing
 * @see javax.swing.RootPaneContainer
 * 
 * @beaninfo attribute: isContainer true attribute: containerDelegate
 *           getContentPane description: A toplevel window which can be
 *           minimized to an icon.
 * 
 * @author Jeff Dinkins
 * @author Georges Saab
 * @author David Kloba
 */
public class JFrame extends JSFrame implements WindowConstants,

RootPaneContainer// TransferHandler.HasGetTransferHandler
{
	/**
	 * The exit application default window close operation. If a window has this
	 * set as the close operation and is closed in an applet, a
	 * <code>SecurityException</code> may be thrown. It is recommended you only
	 * use this in an application.
	 * <p>
	 * 
	 * @since 1.3
	 */
	public static final int EXIT_ON_CLOSE = 3;

	/**
	 * Key into the AppContext, used to check if should provide decorations by
	 * default.
	 */
	private static final Object defaultLookAndFeelDecoratedKey = new Object(); // JFrame.defaultLookAndFeelDecorated

	protected int defaultCloseOperation = HIDE_ON_CLOSE;

	/**
	 * The <code>TransferHandler</code> for this frame.
	 */
	private TransferHandler transferHandler;

	/**
	 * The <code>JRootPane</code> instance that manages the
	 * <code>contentPane</code> and optional <code>menuBar</code> for this frame,
	 * as well as the <code>glassPane</code>.
	 * 
	 * @see JRootPane
	 * @see RootPaneContainer
	 */
	protected JRootPane rootPane;

    @Override
	public void add(Component comp, Object constraints) {
    	if (comp instanceof JApplet) {
    		秘isAppletFrame = true;
    		((JApplet) comp).getLayeredPane().秘isFramedApplet = true;
    	}
    	super.add(comp, constraints);
     }

	/**
	 * If true then calls to <code>add</code> and <code>setLayout</code> will be
	 * forwarded to the <code>contentPane</code>. This is initially false, but is
	 * set to true when the <code>JFrame</code> is constructed.
	 * 
	 * @see #isRootPaneCheckingEnabled
	 * @see #setRootPaneCheckingEnabled
	 * @see javax.swing.RootPaneContainer
	 */
	protected boolean rootPaneCheckingEnabled = false;

	private boolean 秘boundsFrozen;

	/**
	 * Constructs a new frame that is initially invisible.
	 * <p>
	 * This constructor sets the component's locale property to the value returned
	 * by <code>JComponent.getDefaultLocale</code>.
	 * 
	 * @exception HeadlessException
	 *              if GraphicsEnvironment.isHeadless() returns true.
	 * @see java.awt.GraphicsEnvironment#isHeadless
	 * @see Component#setSize
	 * @see Component#setVisible
	 * @see JComponent#getDefaultLocale
	 * 
	 */
	public JFrame() {
		this(null, null);
	}

	/**
	 * Creates a <code>Frame</code> in the specified
	 * <code>GraphicsConfiguration</code> of a screen device and a blank title.
	 * <p>
	 * This constructor sets the component's locale property to the value returned
	 * by <code>JComponent.getDefaultLocale</code>.
	 * 
	 * @param gc
	 *          the <code>GraphicsConfiguration</code> that is used to construct
	 *          the new <code>Frame</code>; if <code>gc</code> is
	 *          <code>null</code>, the system default
	 *          <code>GraphicsConfiguration</code> is assumed
	 * @exception IllegalArgumentException
	 *              if <code>gc</code> is not from a screen device. This exception
	 *              is always thrown when GraphicsEnvironment.isHeadless() returns
	 *              true.
	 * @see java.awt.GraphicsEnvironment#isHeadless
	 * @see JComponent#getDefaultLocale
	 * @since 1.3
	 * 
	 */
	public JFrame(GraphicsConfiguration gc) {
		this(null, gc);
	}

	/**
	 * Creates a new, initially invisible <code>Frame</code> with the specified
	 * title.
	 * <p>
	 * This constructor sets the component's locale property to the value returned
	 * by <code>JComponent.getDefaultLocale</code>.
	 * 
	 * @param title
	 *          the title for the frame
	 * @exception HeadlessException
	 *              if GraphicsEnvironment.isHeadless() returns true.
	 * @see java.awt.GraphicsEnvironment#isHeadless
	 * @see Component#setSize
	 * @see Component#setVisible
	 * @see JComponent#getDefaultLocale
	 * 
	 */
	public JFrame(String title) {
		this(title, null);
	}

	/**
	 * The only one we use in SwingJS
	 * 
	 * Creates a <code>JFrame</code> with the specified title and the specified
	 * <code>GraphicsConfiguration</code> of a screen device.
	 * <p>
	 * This constructor sets the component's locale property to the value returned
	 * by <code>JComponent.getDefaultLocale</code>.
	 * 
	 * @param title the title to be displayed in the frame's border. A
	 *              <code>null</code> value is treated as an empty string, "".
	 * @param gc    the <code>GraphicsConfiguration</code> that is used to construct
	 *              the new <code>JFrame</code> with; if <code>gc</code> is
	 *              <code>null</code>, the system default
	 *              <code>GraphicsConfiguration</code> is assumed
	 * @exception IllegalArgumentException if <code>gc</code> is not from a screen
	 *                                     device. This exception is always thrown
	 *                                     when GraphicsEnvironment.isHeadless()
	 *                                     returns true.
	 * @see java.awt.GraphicsEnvironment#isHeadless
	 * @see JComponent#getDefaultLocale
	 * @since 1.3
	 * 
	 */
	public JFrame(String title, GraphicsConfiguration gc) {
		秘paintClass = 秘updateClass = /** @j2sNative C$ || */
				null;
		initTitleGC(title, gc);
		enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK);
		setLocale(JComponent.getDefaultLocale());
		setRootPane(createRootPane());
		rootPane.setFrameViewer(setFrameViewer(null));
		setBackground(UIManager.getColor("control"));
		setRootPaneCheckingEnabled(true);
		if (JFrame.isDefaultLookAndFeelDecorated()) {
			boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
			if (supportsWindowDecorations) {
				setUndecorated(true);
				getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
			}
		}
		updateUI();
		秘frameAddNodify(rootPane);
	}	

	public JFrame(Object object, Object object2, Object object3, Object object4) {
		// For SwingJS JInternalFrame constructor
	}

	@Override
	public String getUIClassID() {
		return "FrameUI";
	}


	private static int frameCount;

	/**
	 * Called by the constructor methods to create the default
	 * <code>rootPane</code>.
	 */
	protected JRootPane createRootPane() {
		JRootPane rp = new JRootPane("_Frame" + (++frameCount), false, this);
		// NOTE: this uses setOpaque vs LookAndFeel.installProperty as there
		// is NO reason for the RootPane not to be opaque. For painting to
		// work the contentPane must be opaque, therefor the RootPane can
		// also be opaque.
		rp.setOpaque(true);
		return rp;
	}

	/**
	 * Processes window events occurring on this component. Hides the window or
	 * disposes of it, as specified by the setting of the
	 * <code>defaultCloseOperation</code> property.
	 * 
	 * @param e
	 *          the window event
	 * @see #setDefaultCloseOperation
	 * @see java.awt.Window#processWindowEvent
	 */
	@Override
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);

		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			switch (defaultCloseOperation) {
			case HIDE_ON_CLOSE:
				setVisible(false);
				break;
			case DISPOSE_ON_CLOSE:
				dispose();
				break;
			case DO_NOTHING_ON_CLOSE:
			default:
				break;
			case EXIT_ON_CLOSE:
				// This needs to match the checkExit call in
				// setDefaultCloseOperation
				System.exit(0);
				break;
			}
		}
	}

//	public void setMenuBar(MenuBar menu) {
//		throw new IllegalComponentStateException(
//				"Please use setJMenuBar() with JFrame.");
//	}
//
	/**
	 * Sets the operation that will happen by default when the user initiates a
	 * "close" on this frame. You must specify one of the following choices:
	 * <p>
	 * <ul>
	 * <li><code>DO_NOTHING_ON_CLOSE</code> (defined in
	 * <code>WindowConstants</code>): Don't do anything; require the program to
	 * handle the operation in the <code>windowClosing</code> method of a
	 * registered <code>WindowListener</code> object.
	 * 
	 * <li><code>HIDE_ON_CLOSE</code> (defined in <code>WindowConstants</code>):
	 * Automatically hide the frame after invoking any registered
	 * <code>WindowListener</code> objects.
	 * 
	 * <li><code>DISPOSE_ON_CLOSE</code> (defined in <code>WindowConstants</code>
	 * ): Automatically hide and dispose the frame after invoking any registered
	 * <code>WindowListener</code> objects.
	 * 
	 * <li><code>EXIT_ON_CLOSE</code> (defined in <code>JFrame</code>): Exit the
	 * application using the <code>System</code> <code>exit</code> method. Use
	 * this only in applications.
	 * </ul>
	 * <p>
	 * The value is set to <code>HIDE_ON_CLOSE</code> by default. Changes to the
	 * value of this property cause the firing of a property change event, with
	 * property name "defaultCloseOperation".
	 * <p>
	 * <b>Note</b>: When the last displayable window within the Java virtual
	 * machine (VM) is disposed of, the VM may terminate. See <a
	 * href="../../java/awt/doc-files/AWTThreadIssues.html"> AWT Threading
	 * Issues</a> for more information.
	 * 
	 * @param operation
	 *          the operation which should be performed when the user closes the
	 *          frame
	 * @exception IllegalArgumentException
	 *              if defaultCloseOperation value isn't one of the above valid
	 *              values
	 * @see #addWindowListener
	 * @see #getDefaultCloseOperation
	 * @see WindowConstants
	 * @throws SecurityException
	 *           if <code>EXIT_ON_CLOSE</code> has been specified and the
	 *           <code>SecurityManager</code> will not allow the caller to invoke
	 *           <code>System.exit</code>
	 * @see java.lang.Runtime#exit(int)
	 * 
	 * @beaninfo preferred: true bound: true enum: DO_NOTHING_ON_CLOSE
	 *           WindowConstants.DO_NOTHING_ON_CLOSE HIDE_ON_CLOSE
	 *           WindowConstants.HIDE_ON_CLOSE DISPOSE_ON_CLOSE
	 *           WindowConstants.DISPOSE_ON_CLOSE EXIT_ON_CLOSE
	 *           WindowConstants.EXIT_ON_CLOSE description: The frame's default
	 *           close operation.
	 */
	public void setDefaultCloseOperation(int operation) {
		if (operation != DO_NOTHING_ON_CLOSE && operation != HIDE_ON_CLOSE
				&& operation != DISPOSE_ON_CLOSE && operation != EXIT_ON_CLOSE) {
			throw new IllegalArgumentException(
					"defaultCloseOperation must be one of: DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, DISPOSE_ON_CLOSE, or EXIT_ON_CLOSE");
		}
		if (this.defaultCloseOperation != operation) {
			if (operation == EXIT_ON_CLOSE) {
				SecurityManager security = System.getSecurityManager();
				if (security != null) {
					security.checkExit(0);
				}
			}
			int oldValue = this.defaultCloseOperation;
			this.defaultCloseOperation = operation;
			firePropertyChange("defaultCloseOperation", oldValue, operation);
		}
	}

	/**
	 * Returns the operation that occurs when the user initiates a "close" on this
	 * frame.
	 * 
	 * @return an integer indicating the window-close operation
	 * @see #setDefaultCloseOperation
	 */
	public int getDefaultCloseOperation() {
		return defaultCloseOperation;
	}

	/**
	 * Sets the {@code transferHandler} property, which is a mechanism to support
	 * transfer of data into this component. Use {@code null} if the component
	 * does not support data transfer operations.
	 * <p>
	 * If the system property {@code suppressSwingDropSupport} is {@code false}
	 * (the default) and the current drop target on this component is either
	 * {@code null} or not a user-set drop target, this method will change the
	 * drop target as follows: If {@code newHandler} is {@code null} it will clear
	 * the drop target. If not {@code null} it will install a new
	 * {@code DropTarget}.
	 * <p>
	 * Note: When used with {@code JFrame}, {@code TransferHandler} only provides
	 * data import capability, as the data export related methods are currently
	 * typed to {@code JComponent}.
	 * <p>
	 * Please see <a
	 * href="http://java.sun.com/docs/books/tutorial/uiswing/misc/dnd.html"> How
	 * to Use Drag and Drop and Data Transfer</a>, a section in
	 * <em>The Java Tutorial</em>, for more information.
	 * 
	 * @param newHandler
	 *          the new {@code TransferHandler}
	 * 
	 * @see TransferHandler
	 * @see #getTransferHandler
	 * @see java.awt.Component#setDropTarget
	 * @since 1.6
	 * 
	 * @beaninfo bound: true hidden: true description: Mechanism for transfer of
	 *           data into the component
	 */
	@Override
	public void setTransferHandler(TransferHandler newHandler) {
		TransferHandler oldHandler = transferHandler;
		transferHandler = newHandler;
		SwingUtilities.installSwingDropTargetAsNecessary(this, transferHandler);
		firePropertyChange("transferHandler", oldHandler, newHandler);
	}

	/**
	 * Gets the <code>transferHandler</code> property.
	 * 
	 * @return the value of the <code>transferHandler</code> property
	 * 
	 * @see TransferHandler
	 * @see #setTransferHandler
	 * @since 1.6
	 */
	@Override
	public TransferHandler getTransferHandler() {
		return transferHandler;
	}

	/**
	 * Just calls <code>paint(g)</code>. This method was overridden to prevent an
	 * unnecessary call to clear the background.
	 * 
	 * @param g
	 *          the Graphics context in which to paint
	 */
	@Override
	public void update(Graphics g) {
		paint(g);
	}
		
	@Override
	public void paint(Graphics g) {
		((Graphics2D) g).setBackground(getBackground());
		((Graphics2D) g).setColor(getForeground());
		super.paint(g);
	}

	/**
	 * Sets the menubar for this frame.
	 * 
	 * @param menubar
	 *          the menubar being placed in the frame
	 * 
	 * @see #getJMenuBar
	 * 
	 * @beaninfo hidden: true description: The menubar for accessing pulldown
	 *           menus from this frame.
	 */
	@SuppressWarnings("deprecation")
	public void setJMenuBar(JMenuBar menubar) {
		getRootPane().setMenuBar(menubar);
	}

	/**
	 * Returns the menubar set on this frame.
	 * 
	 * @return the menubar for this frame
	 * 
	 * @see #setJMenuBar
	 */
	@SuppressWarnings("deprecation")
	public JMenuBar getJMenuBar() {
		return getRootPane().getMenuBar();
	}

	/**
	 * Returns whether calls to <code>add</code> and <code>setLayout</code> are
	 * forwarded to the <code>contentPane</code>.
	 * 
	 * @return true if <code>add</code> and <code>setLayout</code> are fowarded;
	 *         false otherwise
	 * 
	 * @see #addImpl
	 * @see #setLayout
	 * @see #setRootPaneCheckingEnabled
	 * @see javax.swing.RootPaneContainer
	 */
	protected boolean isRootPaneCheckingEnabled() {
		return rootPaneCheckingEnabled;
	}

	/**
	 * Sets whether calls to <code>add</code> and <code>setLayout</code> are
	 * forwarded to the <code>contentPane</code>.
	 * 
	 * @param enabled
	 *          true if <code>add</code> and <code>setLayout</code> are forwarded,
	 *          false if they should operate directly on the <code>JFrame</code>.
	 * 
	 * @see #addImpl
	 * @see #setLayout
	 * @see #isRootPaneCheckingEnabled
	 * @see javax.swing.RootPaneContainer
	 * @beaninfo hidden: true description: Whether the add and setLayout methods
	 *           are forwarded
	 */
	protected void setRootPaneCheckingEnabled(boolean enabled) {
		rootPaneCheckingEnabled = enabled;
	}

	/**
	 * Adds the specified child <code>Component</code>. This method is overridden
	 * to conditionally forward calls to the <code>contentPane</code>. By default,
	 * children are added to the <code>contentPane</code> instead of the frame,
	 * refer to {@link javax.swing.RootPaneContainer} for details.
	 * 
	 * @param comp
	 *          the component to be enhanced
	 * @param constraints
	 *          the constraints to be respected
	 * @param index
	 *          the index
	 * @exception IllegalArgumentException
	 *              if <code>index</code> is invalid
	 * @exception IllegalArgumentException
	 *              if adding the container's parent to itself
	 * @exception IllegalArgumentException
	 *              if adding a window to a container
	 * 
	 * @see #setRootPaneCheckingEnabled
	 * @see javax.swing.RootPaneContainer
	 */
	@Override
	protected void addImpl(Component comp, Object constraints, int index) {
		if (isRootPaneCheckingEnabled()) {
			getContentPane().add(comp, constraints, index);
			return;
		}
		addImplCont(comp, constraints, index);
	}

	/**
	 * Removes the specified component from the container. If <code>comp</code> is
	 * not the <code>rootPane</code>, this will forward the call to the
	 * <code>contentPane</code>. This will do nothing if <code>comp</code> is not
	 * a child of the <code>JFrame</code> or <code>contentPane</code>.
	 * 
	 * @param comp
	 *          the component to be removed
	 * @throws NullPointerException
	 *           if <code>comp</code> is null
	 * @see #add
	 * @see javax.swing.RootPaneContainer
	 */
	@Override
	public void remove(Component comp) {
		if (comp == rootPane) {
			super.remove(comp);
		} else {
			getContentPane().remove(comp);
		}
	}

	/**
	 * Sets the <code>LayoutManager</code>. Overridden to conditionally forward
	 * the call to the <code>contentPane</code>. Refer to
	 * {@link javax.swing.RootPaneContainer} for more information.
	 * 
	 * @param manager
	 *          the <code>LayoutManager</code>
	 * @see #setRootPaneCheckingEnabled
	 * @see javax.swing.RootPaneContainer
	 */
	@Override
	public void setLayout(LayoutManager manager) {
		if (isRootPaneCheckingEnabled()) {
			getContentPane().setLayout(manager);
		} else {
			super.setLayout(manager);
		}
	}

	/**
	 * Returns the <code>rootPane</code> object for this frame.
	 * 
	 * @return the <code>rootPane</code> property
	 * 
	 * @see #setRootPane
	 * @see RootPaneContainer#getRootPane
	 */
	@Override
	public JRootPane getRootPane() {
		return rootPane;
	}

	/**
	 * Sets the <code>rootPane</code> property. This method is called by the
	 * constructor.
	 * 
	 * @param root
	 *          the <code>rootPane</code> object for this frame
	 * 
	 * @see #getRootPane
	 * 
	 * @beaninfo hidden: true description: the RootPane object for this frame.
	 */
	protected void setRootPane(JRootPane root) {
		if (rootPane != null) {
			remove(rootPane);
		}
		rootPane = root;
		if (rootPane != null) {
			boolean checkingEnabled = isRootPaneCheckingEnabled();
			try {
				setRootPaneCheckingEnabled(false);
				add(rootPane, BorderLayout.CENTER);
			} finally {
				setRootPaneCheckingEnabled(checkingEnabled);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIconImage(Image image) {
		super.setIconImage(image);
	}

	/**
	 * Returns the <code>contentPane</code> object for this frame.
	 * 
	 * @return the <code>contentPane</code> property
	 * 
	 * @see #setContentPane
	 * @see RootPaneContainer#getContentPane
	 */
	@Override
	public Container getContentPane() {
		return getRootPane().getContentPane();
	}

	/**
	 * Sets the <code>contentPane</code> property. This method is called by the
	 * constructor.
	 * <p>
	 * Swing's painting architecture requires an opaque <code>JComponent</code> in
	 * the containment hiearchy. This is typically provided by the content pane.
	 * If you replace the content pane it is recommended you replace it with an
	 * opaque <code>JComponent</code>.
	 * 
	 * @param contentPane
	 *          the <code>contentPane</code> object for this frame
	 * 
	 * @exception java.awt.IllegalComponentStateException
	 *              (a runtime exception) if the content pane parameter is
	 *              <code>null</code>
	 * @see #getContentPane
	 * @see RootPaneContainer#setContentPane
	 * @see JRootPane
	 * 
	 * @beaninfo hidden: true description: The client area of the frame where
	 *           child components are normally inserted.
	 */
	@Override
	public void setContentPane(Container contentPane) {
		getRootPane().setContentPane(contentPane);

	}

	/**
	 * Returns the <code>layeredPane</code> object for this frame.
	 * 
	 * @return the <code>layeredPane</code> property
	 * 
	 * @see #setLayeredPane
	 * @see RootPaneContainer#getLayeredPane
	 */
	@Override
	public JLayeredPane getLayeredPane() {
		return getRootPane().getLayeredPane();
	}

	/**
	 * Sets the <code>layeredPane</code> property. This method is called by the
	 * constructor.
	 * 
	 * @param layeredPane
	 *          the <code>layeredPane</code> object for this frame
	 * 
	 * @exception java.awt.IllegalComponentStateException
	 *              (a runtime exception) if the layered pane parameter is
	 *              <code>null</code>
	 * @see #getLayeredPane
	 * @see RootPaneContainer#setLayeredPane
	 * 
	 * @beaninfo hidden: true description: The pane that holds the various frame
	 *           layers.
	 */
	@Override
	public void setLayeredPane(JLayeredPane layeredPane) {
		getRootPane().setLayeredPane(layeredPane);
	}

	/**
	 * Returns the <code>glassPane</code> object for this frame.
	 * 
	 * @return the <code>glassPane</code> property
	 * 
	 * @see #setGlassPane
	 * @see RootPaneContainer#getGlassPane
	 */
	@Override
	public Component getGlassPane() {
		return getRootPane().getGlassPane();
	}

	/**
	 * Sets the <code>glassPane</code> property. This method is called by the
	 * constructor.
	 * 
	 * @param glassPane
	 *          the <code>glassPane</code> object for this frame
	 * 
	 * @see #getGlassPane
	 * @see RootPaneContainer#setGlassPane
	 * 
	 * @beaninfo hidden: true description: A transparent pane used for menu
	 *           rendering.
	 */
	@Override
	public void setGlassPane(Component glassPane) {
		getRootPane().setGlassPane(glassPane);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.6
	 */
	@Override
	public Graphics getGraphics() {
		JComponent.getGraphicsInvoked(this);
		return super.getGraphics();
	}

	/**
	 * Repaints the specified rectangle of this component within <code>time</code>
	 * milliseconds. Refer to <code>RepaintManager</code> for details on how the
	 * repaint is handled.
	 * 
	 * @param time
	 *          maximum time in milliseconds before update
	 * @param x
	 *          the <i>x</i> coordinate
	 * @param y
	 *          the <i>y</i> coordinate
	 * @param width
	 *          the width
	 * @param height
	 *          the height
	 * @see RepaintManager
	 * @since 1.6
	 */
	@Override
	public void repaint(long time, int x, int y, int width, int height) {
		if (RepaintManager.HANDLE_TOP_LEVEL_PAINT) {
			RepaintManager.currentManager(this).addDirtyRegion(this, x, y, width,
					height);
		} else {
			super.repaint(time, x, y, width, height);
		}
	}

	/**
	 * Provides a hint as to whether or not newly created <code>JFrame</code>s
	 * should have their Window decorations (such as borders, widgets to close the
	 * window, title...) provided by the current look and feel. If
	 * <code>defaultLookAndFeelDecorated</code> is true, the current
	 * <code>LookAndFeel</code> supports providing window decorations, and the
	 * current window manager supports undecorated windows, then newly created
	 * <code>JFrame</code>s will have their Window decorations provided by the
	 * current <code>LookAndFeel</code>. Otherwise, newly created
	 * <code>JFrame</code>s will have their Window decorations provided by the
	 * current window manager.
	 * <p>
	 * You can get the same effect on a single JFrame by doing the following:
	 * 
	 * <pre>
	 * JFrame frame = new JFrame();
	 * frame.setUndecorated(true);
	 * frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
	 * </pre>
	 * 
	 * @param defaultLookAndFeelDecorated
	 *          A hint as to whether or not current look and feel should provide
	 *          window decorations
	 * @see javax.swing.LookAndFeel#getSupportsWindowDecorations
	 * @since 1.4
	 */
	public static void setDefaultLookAndFeelDecorated(
			boolean defaultLookAndFeelDecorated) {
		if (defaultLookAndFeelDecorated) {
			SwingUtilities
					.appContextPut(defaultLookAndFeelDecoratedKey, Boolean.TRUE);
		} else {
			SwingUtilities.appContextPut(defaultLookAndFeelDecoratedKey,
					Boolean.FALSE);
		}
	}

	/**
	 * Returns true if newly created <code>JFrame</code>s should have their Window
	 * decorations provided by the current look and feel. This is only a hint, as
	 * certain look and feels may not support this feature.
	 * 
	 * @return true if look and feel should provide Window decorations.
	 * @since 1.4
	 */
	public static boolean isDefaultLookAndFeelDecorated() {
		Boolean defaultLookAndFeelDecorated = (Boolean) SwingUtilities
				.appContextGet(defaultLookAndFeelDecoratedKey);
		if (defaultLookAndFeelDecorated == null) {
			defaultLookAndFeelDecorated = Boolean.FALSE;
		}
		return defaultLookAndFeelDecorated.booleanValue();
	}

	/**
	 * Returns a string representation of this <code>JFrame</code>. This method is
	 * intended to be used only for debugging purposes, and the content and format
	 * of the returned string may vary between implementations. The returned
	 * string may be empty but may not be <code>null</code>.
	 * 
	 * @return a string representation of this <code>JFrame</code>
	 */
	@Override
	protected String paramString() {
		String defaultCloseOperationString;
		if (defaultCloseOperation == HIDE_ON_CLOSE) {
			defaultCloseOperationString = "HIDE_ON_CLOSE";
		} else if (defaultCloseOperation == DISPOSE_ON_CLOSE) {
			defaultCloseOperationString = "DISPOSE_ON_CLOSE";
		} else if (defaultCloseOperation == DO_NOTHING_ON_CLOSE) {
			defaultCloseOperationString = "DO_NOTHING_ON_CLOSE";
		} else if (defaultCloseOperation == 3) {
			defaultCloseOperationString = "EXIT_ON_CLOSE";
		} else
			defaultCloseOperationString = "";
		String rootPaneString = (rootPane != null ? rootPane.toString() : "");
		String rootPaneCheckingEnabledString = (rootPaneCheckingEnabled ? "true"
				: "false");

		return super.paramString() + ",defaultCloseOperation="
				+ defaultCloseOperationString + ",rootPane=" + rootPaneString
				+ ",rootPaneCheckingEnabled=" + rootPaneCheckingEnabledString;
	}

	public void 秘freezeBounds(int w, int h) {
		setSize(w, h);
		秘boundsFrozen = true;
		resizable = false;
	}
	
	@Override
	public void reshape(int x, int y, int width, int height) {
		if (!秘boundsFrozen)
			super.reshape(x, y, width, height);
	}

	
}
