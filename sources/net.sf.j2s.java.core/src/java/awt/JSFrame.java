/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
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
package java.awt;

import java.awt.HeadlessException;
import java.util.Vector;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.peer.ComponentPeer;
import java.awt.peer.FramePeer;

/**
 * A <code>Frame</code> is a top-level window with a title and a border.
 * <p>
 * The size of the frame includes any area designated for the
 * border.  The dimensions of the border area may be obtained
 * using the <code>getInsets</code> method, however, since
 * these dimensions are platform-dependent, a valid insets
 * value cannot be obtained until the frame is made displayable
 * by either calling <code>pack</code> or <code>show</code>.
 * Since the border area is included in the overall size of the
 * frame, the border effectively obscures a portion of the frame,
 * constraining the area available for rendering and/or displaying
 * subcomponents to the rectangle which has an upper-left corner
 * location of <code>(insets.left, insets.top)</code>, and has a size of
 * <code>width - (insets.left + insets.right)</code> by
 * <code>height - (insets.top + insets.bottom)</code>.
 * <p>
 * The default layout for a frame is <code>BorderLayout</code>.
 * <p>
 * A frame may have its native decorations (i.e. <code>Frame</code>
 * and <code>Titlebar</code>) turned off
 * with <code>setUndecorated</code>. This can only be done while the frame
 * is not {@link Component#isDisplayable() displayable}.
 * <p>
 * In a multi-screen environment, you can create a <code>Frame</code>
 * on a different screen device by constructing the <code>Frame</code>
 * with {@link #Frame(GraphicsConfiguration)} or
 * {@link #Frame(String title, GraphicsConfiguration)}.  The
 * <code>GraphicsConfiguration</code> object is one of the
 * <code>GraphicsConfiguration</code> objects of the target screen
 * device.
 * <p>
 * In a virtual device multi-screen environment in which the desktop
 * area could span multiple physical screen devices, the bounds of all
 * configurations are relative to the virtual-coordinate system.  The
 * origin of the virtual-coordinate system is at the upper left-hand
 * corner of the primary physical screen.  Depending on the location
 * of the primary screen in the virtual device, negative coordinates
 * are possible, as shown in the following figure.
 * <p>
 * <img src="doc-files/MultiScreen.gif"
 * alt="Diagram of virtual device encompassing three physical screens and one primary physical screen. The primary physical screen
 * shows (0,0) coords while a different physical screen shows (-80,-100) coords."
 * ALIGN=center HSPACE=10 VSPACE=7>
 * <p>
 * In such an environment, when calling <code>setLocation</code>,
 * you must pass a virtual coordinate to this method.  Similarly,
 * calling <code>getLocationOnScreen</code> on a <code>Frame</code>
 * returns virtual device coordinates.  Call the <code>getBounds</code>
 * method of a <code>GraphicsConfiguration</code> to find its origin in
 * the virtual coordinate system.
 * <p>
 * The following code sets the
 * location of the <code>Frame</code> at (10, 10) relative
 * to the origin of the physical screen of the corresponding
 * <code>GraphicsConfiguration</code>.  If the bounds of the
 * <code>GraphicsConfiguration</code> is not taken into account, the
 * <code>Frame</code> location would be set at (10, 10) relative to the
 * virtual-coordinate system and would appear on the primary physical
 * screen, which might be different from the physical screen of the
 * specified <code>GraphicsConfiguration</code>.
 *
 * <pre>
 *      Frame f = new Frame(GraphicsConfiguration gc);
 *      Rectangle bounds = gc.getBounds();
 *      f.setLocation(10 + bounds.x, 10 + bounds.y);
 * </pre>
 *
 * <p>
 * Frames are capable of generating the following types of
 * <code>WindowEvent</code>s:
 * <ul>
 * <li><code>WINDOW_OPENED</code>
 * <li><code>WINDOW_CLOSING</code>:
 *     <br>If the program doesn't
 *     explicitly hide or dispose the window while processing
 *     this event, the window close operation is canceled.
 * <li><code>WINDOW_CLOSED</code>
 * <li><code>WINDOW_ICONIFIED</code>
 * <li><code>WINDOW_DEICONIFIED</code>
 * <li><code>WINDOW_ACTIVATED</code>
 * <li><code>WINDOW_DEACTIVATED</code>
 * <li><code>WINDOW_GAINED_FOCUS</code>
 * <li><code>WINDOW_LOST_FOCUS</code>
 * <li><code>WINDOW_STATE_CHANGED</code>
 * </ul>
 *
 * @author      Sami Shaio
 * @see WindowEvent
 * @see Window#addWindowListener
 * @since       JDK1.0
 */
@SuppressWarnings({"rawtypes"})
public class JSFrame extends Window {

    /* Note: These are being obsoleted;  programs should use the Cursor class
     * variables going forward. See Cursor and Component.setCursor.
     */

   /**
    * @deprecated   replaced by <code>Cursor.DEFAULT_CURSOR</code>.
    */
    @Deprecated
    public static final int     DEFAULT_CURSOR                  = Cursor.DEFAULT_CURSOR;


   /**
    * @deprecated   replaced by <code>Cursor.CROSSHAIR_CURSOR</code>.
    */
    @Deprecated
    public static final int     CROSSHAIR_CURSOR                = Cursor.CROSSHAIR_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.TEXT_CURSOR</code>.
    */
    @Deprecated
    public static final int     TEXT_CURSOR                     = Cursor.TEXT_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.WAIT_CURSOR</code>.
    */
    @Deprecated
    public static final int     WAIT_CURSOR                     = Cursor.WAIT_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.SW_RESIZE_CURSOR</code>.
    */
    @Deprecated
    public static final int     SW_RESIZE_CURSOR                = Cursor.SW_RESIZE_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.SE_RESIZE_CURSOR</code>.
    */
    @Deprecated
    public static final int     SE_RESIZE_CURSOR                = Cursor.SE_RESIZE_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.NW_RESIZE_CURSOR</code>.
    */
    @Deprecated
    public static final int     NW_RESIZE_CURSOR                = Cursor.NW_RESIZE_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.NE_RESIZE_CURSOR</code>.
    */
    @Deprecated
    public static final int     NE_RESIZE_CURSOR                = Cursor.NE_RESIZE_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.N_RESIZE_CURSOR</code>.
    */
    @Deprecated
    public static final int     N_RESIZE_CURSOR                 = Cursor.N_RESIZE_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.S_RESIZE_CURSOR</code>.
    */
    @Deprecated
    public static final int     S_RESIZE_CURSOR                 = Cursor.S_RESIZE_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.W_RESIZE_CURSOR</code>.
    */
    @Deprecated
    public static final int     W_RESIZE_CURSOR                 = Cursor.W_RESIZE_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.E_RESIZE_CURSOR</code>.
    */
    @Deprecated
    public static final int     E_RESIZE_CURSOR                 = Cursor.E_RESIZE_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.HAND_CURSOR</code>.
    */
    @Deprecated
    public static final int     HAND_CURSOR                     = Cursor.HAND_CURSOR;

   /**
    * @deprecated   replaced by <code>Cursor.MOVE_CURSOR</code>.
    */
    @Deprecated
    public static final int     MOVE_CURSOR                     = Cursor.MOVE_CURSOR;


    /**
     * Frame is in the "normal" state.  This symbolic constant names a
     * frame state with all state bits cleared.
     * @see #setExtendedState(int)
     * @see #getExtendedState
     */
    public static final int NORMAL = 0;

    /**
     * This state bit indicates that frame is iconified.
     * @see #setExtendedState(int)
     * @see #getExtendedState
     */
    public static final int ICONIFIED = 1;

    /**
     * This state bit indicates that frame is maximized in the
     * horizontal direction.
     * @see #setExtendedState(int)
     * @see #getExtendedState
     * @since 1.4
     */
    public static final int MAXIMIZED_HORIZ = 2;

    /**
     * This state bit indicates that frame is maximized in the
     * vertical direction.
     * @see #setExtendedState(int)
     * @see #getExtendedState
     * @since 1.4
     */
    public static final int MAXIMIZED_VERT = 4;

    /**
     * This state bit mask indicates that frame is fully maximized
     * (that is both horizontally and vertically).  It is just a
     * convenience alias for
     * <code>MAXIMIZED_VERT&nbsp;|&nbsp;MAXIMIZED_HORIZ</code>.
     *
     * <p>Note that the correct test for frame being fully maximized is
     * <pre>
     *     (state & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH
     * </pre>
     *
     * <p>To test is frame is maximized in <em>some</em> direction use
     * <pre>
     *     (state & Frame.MAXIMIZED_BOTH) != 0
     * </pre>
     *
     * @see #setExtendedState(int)
     * @see #getExtendedState
     * @since 1.4
     */
    public static final int MAXIMIZED_BOTH = MAXIMIZED_VERT | MAXIMIZED_HORIZ;

    /**
     * Maximized bounds for this frame.
     * @see     #setMaximizedBounds(Rectangle)
     * @see     #getMaximizedBounds
     * @serial
     * @since 1.4
     */
    Rectangle maximizedBounds;


    /**
     * This is the title of the frame.  It can be changed
     * at any time.  <code>title</code> can be null and if
     * this is the case the <code>title</code> = "".
     *
     * @serial
     * @see #getTitle
     * @see #setTitle(String)
     */
    String      title = "Untitled";

    /**
     * The frames menubar.  If <code>menuBar</code> = null
     * the frame will not have a menubar.
     *
     * @serial
     * @see #getMenuBar
     * @see #setMenuBar(MenuBar)
     */
     //MenuBar     menuBar;

    /**
     * This field indicates whether the frame is resizable.
     * This property can be changed at any time.
     * <code>resizable</code> will be true if the frame is
     * resizable, otherwise it will be false.
     *
     * @serial
     * @see #isResizable()
     */
    protected boolean     resizable = true;

    /**
     * This field indicates whether the frame is undecorated.
     * This property can only be changed while the frame is not displayable.
     * <code>undecorated</code> will be true if the frame is
     * undecorated, otherwise it will be false.
     *
     * @serial
     * @see #setUndecorated(boolean)
     * @see #isUndecorated()
     * @see Component#isDisplayable()
     * @since 1.4
     */
    boolean undecorated = false;

    /**
     * <code>mbManagement</code> is only used by the Motif implementation.
     *
     * @serial
     */
    boolean     mbManagement = false;   /* used only by the Motif impl. */

    // XXX: uwe: abuse old field for now
    // will need to take care of serialization
    private int state = NORMAL;

    /*
     * The Windows owned by the Frame.
     * Note: in 1.2 this has been superceded by Window.ownedWindowList
     *
     * @serial
     * @see java.awt.Window#ownedWindowList
     */
    Vector ownedWindows;


		//private Object menuBar; // JSSwing -- no real menubar here

    private static final String base = "frame";
    private static int nameCounter = 0;

    /*
     * JDK 1.1 serialVersionUID
     */
     //private static final long serialVersionUID = 2673458971256075116L;

//    static {
//        /* ensure that the necessary native libraries are loaded */
//        Toolkit.loadLibraries();
//        if (!GraphicsEnvironment.isHeadless()) {
//            initIDs();
//        }
//    }

    /**
     * Constructs a new instance of <code>Frame</code> that is
     * initially invisible.  The title of the <code>Frame</code>
     * is empty.
     * @exception HeadlessException when
     *     <code>GraphicsEnvironment.isHeadless()</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless()
     * @see Component#setSize
     * @see Component#setVisible(boolean)
     * 
     * 
     */
    public JSFrame() {
    	initTitleGC(null, null);
    }

    /**
     * 
     * Constructs a new, initially invisible {@code Frame} with the
     * specified {@code GraphicsConfiguration}.
     *
     * @param gc the <code>GraphicsConfiguration</code>
     * of the target screen device. If <code>gc</code>
     * is <code>null</code>, the system default
     * <code>GraphicsConfiguration</code> is assumed.
     * @exception IllegalArgumentException if
     * <code>gc</code> is not from a screen device.
     * @exception HeadlessException when
     *     <code>GraphicsEnvironment.isHeadless()</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless()
     * @since     1.3
     * 
     */
    public JSFrame(GraphicsConfiguration gc) {
    	initTitleGC(null, gc);
    }

    /**
     * Constructs a new, initially invisible <code>Frame</code> object
     * with the specified title.
     * @param title the title to be displayed in the frame's border.
     *              A <code>null</code> value
     *              is treated as an empty string, "".
     * @see java.awt.GraphicsEnvironment#isHeadless()
     * @see java.awt.Component#setSize
     * @see java.awt.Component#setVisible(boolean)
     * @see java.awt.GraphicsConfiguration#getBounds
     * 
     */
    public JSFrame(String title) {
    	initTitleGC(title, null);
    }

    /**
     * the only one we use in SwingJS
     * 
     * Constructs a new, initially invisible <code>Frame</code> object
     * with the specified title and a
     * <code>GraphicsConfiguration</code>.
     * @param title the title to be displayed in the frame's border.
     *              A <code>null</code> value
     *              is treated as an empty string, "".
     * @param gc the <code>GraphicsConfiguration</code>
     * of the target screen device.  If <code>gc</code> is
     * <code>null</code>, the system default
     * <code>GraphicsConfiguration</code> is assumed.
     * @exception IllegalArgumentException if <code>gc</code>
     * is not from a screen device.
     * @exception HeadlessException when
     *     <code>GraphicsEnvironment.isHeadless()</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless()
     * @see java.awt.Component#setSize
     * @see java.awt.Component#setVisible(boolean)
     * @see java.awt.GraphicsConfiguration#getBounds
     * @since 1.3
     * 
     * 
     */
    public JSFrame(String title, GraphicsConfiguration gc) {
    	initTitleGC(title, gc);
    }

    protected void initTitleGC(String title, GraphicsConfiguration gc) {
      this.title = (title == null  ? "" : title);
  	  initWinGC(null, gc);   // Window
		}

		/**
     * Construct a name for this component.  Called by getName() when the
     * name is null.
     */
    @Override
		protected String constructComponentName() {
//        synchronized (JSFrame.class) {
            return base + nameCounter++;
//        }
    }

	/**
	 * Makes this Frame displayable by connecting it to a native screen resource.
	 * Making a frame displayable will cause any of its children to be made
	 * displayable. This method is called internally by the toolkit and should not
	 * be called directly by programs.
	 * 
	 * @see Component#isDisplayable
	 * @see #removeNotify
	 */
	@Override
	public void addNotify() {
		// synchronized (getTreeLock()) {
		getOrCreatePeer();
		FramePeer p = (FramePeer) peer;
		// MenuBar menuBar = this.menuBar;
		// if (menuBar != null) {
		// mbManagement = true;
		// menuBar.addNotify();
		// p.setMenuBar(menuBar);
		// }
		if (p != null)
			p.setMaximizedBounds(maximizedBounds);
		super.addNotify();
		// }
	}

  	@Override
  	protected ComponentPeer getOrCreatePeer() {
  		return (ui == null ? null : peer == null ? (peer = getToolkit().createFrame(this)) : peer);
  	}

    /**
     * Gets the title of the frame.  The title is displayed in the
     * frame's border.
     * @return    the title of this frame, or an empty string ("")
     *                if this frame doesn't have a title.
     * @see       #setTitle(String)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title for this frame to the specified string.
     * @param title the title to be displayed in the frame's border.
     *              A <code>null</code> value
     *              is treated as an empty string, "".
     * @see      #getTitle
     */
    public void setTitle(String title) {
        String oldTitle = this.title;
        if (title == null) {
            title = "";
        }


        synchronized(this) {
            this.title = title;
            FramePeer peer = (FramePeer)this.peer;
            if (peer != null) {
                peer.setTitle(title);
            }
        }
        firePropertyChange("title", oldTitle, title);
    }

    /**
     * Returns the image to be displayed as the icon for this frame.
     * <p>
     * This method is obsolete and kept for backward compatibility
     * only. Use {@link Window#getIconImages Window.getIconImages()} instead.
     * <p>
     * If a list of several images was specified as a Window's icon,
     * this method will return the first item of the list.
     *
     * @return    the icon image for this frame, or <code>null</code>
     *                    if this frame doesn't have an icon image.
     * @see       #setIconImage(Image)
     * @see       Window#getIconImages()
     * @see       Window#setIconImages
     */
    public Image getIconImage() {
        java.util.List<Image> icons = this.icons;
        if (icons != null) {
            if (icons.size() > 0) {
                return icons.get(0);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
		public void setIconImage(Image image) {
        super.setIconImage(image);
    }

//    /**
//     * Gets the menu bar for this frame.
//     * @return    the menu bar for this frame, or <code>null</code>
//     *                   if this frame doesn't have a menu bar.
//     * @see       #setMenuBar(MenuBar)
//     */
//    public MenuBar getMenuBar() {
//        return menuBar;
//    }
//
//    /**
//     * Sets the menu bar for this frame to the specified menu bar.
//     * @param     mb the menu bar being set.
//     *            If this parameter is <code>null</code> then any
//     *            existing menu bar on this frame is removed.
//     * @see       #getMenuBar
//     */
//    public void setMenuBar(MenuBar mb) {
//        synchronized (getTreeLock()) {
//            if (menuBar == mb) {
//                return;
//            }
//            if ((mb != null) && (mb.parent != null)) {
//                mb.parent.remove(mb);
//            }
//            if (menuBar != null) {
//                remove(menuBar);
//            }
//            menuBar = mb;
//            if (menuBar != null) {
//                menuBar.parent = this;
//
//                FramePeer peer = (FramePeer)this.peer;
//                if (peer != null) {
//                    mbManagement = true;
//                    menuBar.addNotify();
//                    invalidateIfValid();
//                    peer.setMenuBar(menuBar);
//                }
//            }
//        }
//    }

    /**
     * Indicates whether this frame is resizable by the user.
     * By default, all frames are initially resizable.
     * @return    <code>true</code> if the user can resize this frame;
     *                        <code>false</code> otherwise.
     * @see       java.awt.JSFrame#setResizable(boolean)
     */
    public boolean isResizable() {
        return resizable;
    }

    /**
     * Sets whether this frame is resizable by the user.
     * @param    resizable   <code>true</code> if this frame is resizable;
     *                       <code>false</code> otherwise.
     * @see      java.awt.JSFrame#isResizable
     */
    public void setResizable(boolean resizable) {
        boolean oldResizable = this.resizable;
//        boolean testvalid = false;

        synchronized (this) {
            this.resizable = resizable;
//            FramePeer peer = (FramePeer)this.peer;
//            if (peer != null) {
//                peer.setResizable(resizable);
//                testvalid = true;
//            }
        }

//        // On some platforms, changing the resizable state affects
//        // the insets of the Frame. If we could, we'd call invalidate()
//        // from the peer, but we need to guarantee that we're not holding
//        // the Frame lock when we call invalidate().
//        if (testvalid) {
//            invalidateIfValid();
//        }
        firePropertyChange("resizable", oldResizable, resizable);
    }


    /**
     * Sets the state of this frame (obsolete).
     * <p>
     * In older versions of JDK a frame state could only be NORMAL or
     * ICONIFIED.  Since JDK 1.4 set of supported frame states is
     * expanded and frame state is represented as a bitwise mask.
     * <p>
     * For compatibility with old programs this method still accepts
     * <code>Frame.NORMAL</code> and <code>Frame.ICONIFIED</code> but
     * it only changes the iconic state of the frame, other aspects of
     * frame state are not affected by this method.
     *
     * @param state either <code>Frame.NORMAL</code> or
     *     <code>Frame.ICONIFIED</code>.
     * @see #getState
     * @see #setExtendedState(int)
     */
    public synchronized void setState(int state) {
        int current = getExtendedState();
        if (state == ICONIFIED && (current & ICONIFIED) == 0) {
            setExtendedState(current | ICONIFIED);
        }
        else if (state == NORMAL && (current & ICONIFIED) != 0) {
            setExtendedState(current & ~ICONIFIED);
        }
    }

    /**
     * Sets the state of this frame. The state is
     * represented as a bitwise mask.
     * <ul>
     * <li><code>NORMAL</code>
     * <br>Indicates that no state bits are set.
     * <li><code>ICONIFIED</code>
     * <li><code>MAXIMIZED_HORIZ</code>
     * <li><code>MAXIMIZED_VERT</code>
     * <li><code>MAXIMIZED_BOTH</code>
     * <br>Concatenates <code>MAXIMIZED_HORIZ</code>
     * and <code>MAXIMIZED_VERT</code>.
     * </ul>
     * <p>Note that if the state is not supported on a
     * given platform, nothing will happen. The application
     * may determine if a specific state is available via
     * the <code>java.awt.Toolkit.isFrameStateSupported(int state)</code>
     * method.
     *
     * @param state a bitwise mask of frame state constants
     * @see     #getExtendedState
     * @see     java.awt.Toolkit#isFrameStateSupported(int)
     * @since   1.4
     */
    public synchronized void setExtendedState(int state) {
        if ( !isFrameStateSupported( state ) ) {
            return;
        }
        this.state = state;
//        FramePeer peer = (FramePeer)this.peer;
//        if (peer != null) {
//            peer.setState(state);
//        }
    }
    private boolean isFrameStateSupported(int state) {
        if( !getToolkit().isFrameStateSupported( state ) ) {
            // * Toolkit.isFrameStateSupported returns always false
            // on compound state even if all parts are supported;
            // * if part of state is not supported, state is not supported;
            // * MAXIMIZED_BOTH is not a compound state.
            if( ((state & ICONIFIED) != 0) &&
                !getToolkit().isFrameStateSupported( ICONIFIED )) {
                return false;
            }else {
                state &= ~ICONIFIED;
            }
            return getToolkit().isFrameStateSupported( state );
        }
        return true;
    }

    /**
     * Gets the state of this frame (obsolete).
     * <p>
     * In older versions of JDK a frame state could only be NORMAL or
     * ICONIFIED.  Since JDK 1.4 set of supported frame states is
     * expanded and frame state is represented as a bitwise mask.
     * <p>
     * For compatibility with old programs this method still returns
     * <code>Frame.NORMAL</code> and <code>Frame.ICONIFIED</code> but
     * it only reports the iconic state of the frame, other aspects of
     * frame state are not reported by this method.
     *
     * @return  <code>Frame.NORMAL</code> or <code>Frame.ICONIFIED</code>.
     * @see     #setState(int)
     * @see     #getExtendedState
     */
    public synchronized int getState() {
        return (getExtendedState() & ICONIFIED) != 0 ? ICONIFIED : NORMAL;
    }


    /**
     * Gets the state of this frame. The state is
     * represented as a bitwise mask.
     * <ul>
     * <li><code>NORMAL</code>
     * <br>Indicates that no state bits are set.
     * <li><code>ICONIFIED</code>
     * <li><code>MAXIMIZED_HORIZ</code>
     * <li><code>MAXIMIZED_VERT</code>
     * <li><code>MAXIMIZED_BOTH</code>
     * <br>Concatenates <code>MAXIMIZED_HORIZ</code>
     * and <code>MAXIMIZED_VERT</code>.
     * </ul>
     *
     * @return  a bitwise mask of frame state constants
     * @see     #setExtendedState(int)
     * @since 1.4
     */
    public synchronized int getExtendedState() {
//        FramePeer peer = (FramePeer)this.peer;
//        if (peer != null) {
//            state = peer.getState();
//        }
        return state;
    }

    /**
     * Sets the maximized bounds for this frame.
     * <p>
     * When a frame is in maximized state the system supplies some
     * defaults bounds.  This method allows some or all of those
     * system supplied values to be overridden.
     * <p>
     * If <code>bounds</code> is <code>null</code>, accept bounds
     * supplied by the system.  If non-<code>null</code> you can
     * override some of the system supplied values while accepting
     * others by setting those fields you want to accept from system
     * to <code>Integer.MAX_VALUE</code>.
     * <p>
     * On some systems only the size portion of the bounds is taken
     * into account.
     *
     * @param bounds  bounds for the maximized state
     * @see #getMaximizedBounds()
     * @since 1.4
     */
    public synchronized void setMaximizedBounds(Rectangle bounds) {
        this.maximizedBounds = bounds;
//        FramePeer peer = (FramePeer)this.peer;
//        if (peer != null) {
//            peer.setMaximizedBounds(bounds);
//        }
    }

    /**
     * Gets maximized bounds for this frame.
     * Some fields may contain <code>Integer.MAX_VALUE</code> to indicate
     * that system supplied values for this field must be used.
     *
     * @return  maximized bounds for this frame;  may be <code>null</code>
     * @see     #setMaximizedBounds(Rectangle)
     * @since   1.4
     */
    public Rectangle getMaximizedBounds() {
        return maximizedBounds;
    }


    /**
     * Disables or enables decorations for this frame.
     * This method can only be called while the frame is not displayable.
     * @param  undecorated <code>true</code> if no frame decorations are
     *         to be enabled;
     *         <code>false</code> if frame decorations are to be enabled.
     * @throws <code>IllegalComponentStateException</code> if the frame
     *         is displayable.
     * @see    #isUndecorated
     * @see    Component#isDisplayable
     * @see    javax.swing.JFrame#setDefaultLookAndFeelDecorated(boolean)
     * @since 1.4
     */
    public void setUndecorated(boolean undecorated) {
        /* Make sure we don't run in the middle of peer creation.*/
//        synchronized (getTreeLock()) {
//            if (isDisplayable()) {
//                throw new IllegalComponentStateException("The frame is displayable.");
//            }
            this.undecorated = undecorated;
//        }
    }

    /**
     * Indicates whether this frame is undecorated.
     * By default, all frames are initially decorated.
     * @return    <code>true</code> if frame is undecorated;
     *                        <code>false</code> otherwise.
     * @see       java.awt.JSFrame#setUndecorated(boolean)
     * @since 1.4
     */
    public boolean isUndecorated() {
        return undecorated;
    }

//    /**
//     * Removes the specified menu bar from this frame.
//     * @param    m   the menu component to remove.
//     *           If <code>m</code> is <code>null</code>, then
//     *           no action is taken
//     */
//    public void remove(MenuComponent m) {
//        if (m == null) {
//            return;
//        }
//        synchronized (getTreeLock()) {
//            if (m == menuBar) {
//                menuBar = null;
//                FramePeer peer = (FramePeer)this.peer;
//                if (peer != null) {
//                    mbManagement = true;
//                    invalidateIfValid();
//                    peer.setMenuBar(null);
//                    m.removeNotify();
//                }
//                m.parent = null;
//            } else {
//                super.remove(m);
//            }
//        }
//    }

    /**
     * Makes this Frame undisplayable by removing its connection
     * to its native screen resource. Making a Frame undisplayable
     * will cause any of its children to be made undisplayable.
     * This method is called by the toolkit internally and should
     * not be called directly by programs.
     * @see Component#isDisplayable
     * @see #addNotify
     */
    @Override
		public void removeNotify() {
//        synchronized (getTreeLock()) {
//            FramePeer peer = (FramePeer)this.peer;
//            if (peer != null) {
//                // get the latest Frame state before disposing
//                getState();
//
//                if (menuBar != null) {
//                    mbManagement = true;
//                    peer.setMenuBar(null);
//                    menuBar.removeNotify();
//                }
//            }
            super.removeNotify();
//        }
    }

    @Override
		void postProcessKeyEvent(KeyEvent e) {
//        if (menuBar != null && menuBar.handleShortcut(e)) {
//            e.consume();
//            return;
//        }
        super.postProcessKeyEvent(e);
    }

    /**
     * Returns a string representing the state of this <code>Frame</code>.
     * This method is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not be
     * <code>null</code>.
     *
     * @return the parameter string of this frame
     */
    @Override
		protected String paramString() {
        String str = super.paramString();
        if (title != null) {
            str += ",title=" + title;
        }
        if (resizable) {
            str += ",resizable";
        }
        getExtendedState();     // sync with peer
        if (state == NORMAL) {
            str += ",normal";
        }
        else {
            if ((state & ICONIFIED) != 0) {
                str += ",iconified";
            }
            if ((state & MAXIMIZED_BOTH) == MAXIMIZED_BOTH) {
                str += ",maximized";
            }
            else if ((state & MAXIMIZED_HORIZ) != 0) {
                str += ",maximized_horiz";
            }
            else if ((state & MAXIMIZED_VERT) != 0) {
                str += ",maximized_vert";
            }
        }
        return str;
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by <code>Component.setCursor(Cursor)</code>.
     */
    @Deprecated
    public void setCursor(int cursorType) {
        if (cursorType < DEFAULT_CURSOR || cursorType > MOVE_CURSOR) {
            throw new IllegalArgumentException("illegal cursor type");
        }
        setCursor(Cursor.getPredefinedCursor(cursorType));
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by <code>Component.getCursor()</code>.
     */
    @Deprecated
    public int getCursorType() {
        return (getCursor().getType());
    }

    /**
     * Returns an array of all {@code Frame}s created by this application.
     * If called from an applet, the array includes only the {@code Frame}s
     * accessible by that applet.
     * <p>
     * <b>Warning:</b> this method may return system created frames, such
     * as a shared, hidden frame which is used by Swing. Applications
     * should not assume the existence of these frames, nor should an
     * application assume anything about these frames such as component
     * positions, <code>LayoutManager</code>s or serialization.
     * <p>
     * <b>Note</b>: To obtain a list of all ownerless windows, including
     * ownerless {@code Dialog}s (introduced in release 1.6), use {@link
     * Window#getOwnerlessWindows Window.getOwnerlessWindows}.
     *
     * @see Window#getWindows()
     * @see Window#getOwnerlessWindows
     *
     * @since 1.2
     */
    public static JSFrame[] getFrames() {
        Window[] allWindows = Window.getWindows();

        int frameCount = 0;
        for (Window w : allWindows) {
            if (w instanceof JSFrame) {
                frameCount++;
            }
        }

        JSFrame[] frames = new JSFrame[frameCount];
        int c = 0;
        for (Window w : allWindows) {
            if (w instanceof JSFrame) {
                frames[c++] = (JSFrame)w;
            }
        }

        return frames;
    }


}
