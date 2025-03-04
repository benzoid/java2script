/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 1995, 2007, Oracle and/or its affiliates. All rights reserved.
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

import java.awt.peer.ComponentPeer;

/**
 * <code>Panel</code> is the simplest container class. A panel
 * provides space in which an application can attach any other
 * component, including other panels.
 * <p>
 * The default layout manager for a panel is the
 * <code>FlowLayout</code> layout manager.
 *
 * @author      Sami Shaio
 * @see     java.awt.FlowLayout
 * @since   JDK1.0
 */
public class JSPanel extends Container {
    private static final String base = "panel";
    private static int nameCounter = 0;

    /*
     * JDK 1.1 serialVersionUID
     */
     //private static final long serialVersionUID = -2728009084054400034L;

    /**
     * Creates a new panel using the default layout manager.
     * The default layout manager for all panels is the
     * <code>FlowLayout</code> class.
     * 
     */
    public JSPanel() {
    	this(new FlowLayout());
    }


	/**
	 * Creates a new panel with the specified layout manager.
	 * 
	 * @param layout the layout manager for this panel.
	 * @since JDK1.1
	 */
	public JSPanel(LayoutManager layout) {
		setAppContext();
		setLayout(layout);
	}
// JSPanel is just for JSApplet, which is the superclass of JApplet and, from that, Applet
//    @Override
//		protected boolean canPaint() {
//    	//return this.isContentPane || this.rootPane; ??
//			return 秘isContentPane;
//    }

    /**
     * Construct a name for this component.  Called by getName() when the
     * name is null.
     */
    @Override
		protected String constructComponentName() {
 //       synchronized (JSPanel.class) {
            return base + nameCounter++;
 //       }
    }

	/**
	 * Creates the Panel's peer. The peer allows you to modify the appearance of
	 * the panel without changing its functionality.
	 */

	@Override
	public void addNotify() {
		// synchronized (getTreeLock()) {
		getOrCreatePeer();
		super.addNotify();
		// }
	}
    
  	@Override
  	protected ComponentPeer getOrCreatePeer() {
  		return (ui == null ? null : peer == null ? (peer = getToolkit().createPanel((Panel) (Object) this)) : peer);
  	}

  	/**
  	 * SwingJS added for focus management
  	 */
    @Override
		public boolean isFocusCycleRoot() {
        return true;
    }


/////////////////
// Accessibility support
////////////////
//
//    /**
//     * Gets the AccessibleContext associated with this Panel.
//     * For panels, the AccessibleContext takes the form of an
//     * AccessibleAWTPanel.
//     * A new AccessibleAWTPanel instance is created if necessary.
//     *
//     * @return an AccessibleAWTPanel that serves as the
//     *         AccessibleContext of this Panel
//     * @since 1.3
//     */
//    public AccessibleContext getAccessibleContext() {
//        if (accessibleContext == null) {
//            accessibleContext = new AccessibleAWTPanel();
//        }
//        return accessibleContext;
//    }
//
//    /**
//     * This class implements accessibility support for the
//     * <code>Panel</code> class.  It provides an implementation of the
//     * Java Accessibility API appropriate to panel user-interface elements.
//     * @since 1.3
//     */
//    protected class AccessibleAWTPanel extends AccessibleAWTContainer {
//
//        //private static final long serialVersionUID = -6409552226660031050L;
//
//        /**
//         * Get the role of this object.
//         *
//         * @return an instance of AccessibleRole describing the role of the
//         * object
//         */
//        public AccessibleRole getAccessibleRole() {
//            return AccessibleRole.PANEL;
//        }
//    }

}
