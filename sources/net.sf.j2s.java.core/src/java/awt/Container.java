/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 1995, 2009, Oracle and/or its affiliates. All rights reserved.
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

import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.peer.ComponentPeer;
import java.awt.peer.ContainerPeer;
import java.awt.peer.LightweightPeer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.EventListener;
import java.util.Set;

import javax.swing.JInternalFrame;

import javajs.util.Lst;
import sun.awt.AppContext;
import sun.awt.SunGraphicsCallback;
import swingjs.JSFrameViewer;
import swingjs.JSMouse;
import swingjs.plaf.JSComponentUI;


/**
 * 
 * SwingJS note: THIS CLASS SHOULD NEVER BE DIRECTLY SUBCLASSED BY THE DEVELOPER IN SwingJS
 * 
 * A generic Abstract Window Toolkit(AWT) container object is a component
 * that can contain other AWT components.
 * <p>
 * Components added to a container are tracked in a list.  The order
 * of the list will define the components' front-to-back stacking order
 * within the container.  If no index is specified when adding a
 * component to a container, it will be added to the end of the list
 * (and hence to the bottom of the stacking order).
 * <p>
 * <b>Note</b>: For details on the focus subsystem, see
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/focus.html">
 * How to Use the Focus Subsystem</a>,
 * a section in <em>The Java Tutorial</em>, and the
 * <a href="../../java/awt/doc-files/FocusSpec.html">Focus Specification</a>
 * for more information.
 *
 * @author      Arthur van Hoff
 * @author      Sami Shaio
 * @see       #add(java.awt.Component, int)
 * @see       #getComponent(int)
 * @see       LayoutManager
 * @since     JDK1.0
 */
public class Container extends JSComponent {

//    private static final Logger log = Logger.getLogger("java.awt.Container");
//    private static final Logger eventLog = Logger.getLogger("java.awt.event.Container");
//
    public static final Component[] EMPTY_ARRAY = new Component[0];

    /**
     * The components in this container.
     * 
     * BH - sorry, for my sanity I changed this name from "component" to "children"
     * 
     * @see #add
     * @see #getComponents
     */
    private Lst<Component> component;
    
    private Component[] 秘childArray;
	
    private boolean 秘childTainted;


    /**
     * Fast access to stable array of children; last element is null
     * 
     * @return
     */
    Component[] getChildArray() {
		int n = component.size();
		if (n == 0)
			return EMPTY_ARRAY;
		if (秘childArray != null && !秘childTainted)
			return 秘childArray;
		秘childTainted = false;
		return component.toArray(秘childArray != null 
				&& 秘childArray.length > n ? 秘childArray : (秘childArray = new Component[n * 2]));
    }

    /**
     * Layout manager for this container.
     * @see #doLayout
     * @see #setLayout
     * @see #getLayout
     */
    LayoutManager layoutMgr;

    /**
     * Event router for lightweight components.  If this container
     * is native, this dispatcher takes care of forwarding and
     * retargeting the events to lightweight components contained
     * (if any).
     */
    protected LightweightDispatcher dispatcher;

    /**
     * The focus traversal policy that will manage keyboard traversal of this
     * Container's children, if this Container is a focus cycle root. If the
     * value is null, this Container inherits its policy from its focus-cycle-
     * root ancestor. If all such ancestors of this Container have null
     * policies, then the current KeyboardFocusManager's default policy is
     * used. If the value is non-null, this policy will be inherited by all
     * focus-cycle-root children that have no keyboard-traversal policy of
     * their own (as will, recursively, their focus-cycle-root children).
     * <p>
     * If this Container is not a focus cycle root, the value will be
     * remembered, but will not be used or inherited by this or any other
     * Containers until this Container is made a focus cycle root.
     *
     * @see #setFocusTraversalPolicy
     * @see #getFocusTraversalPolicy
     * @since 1.4
     */
    private transient FocusTraversalPolicy focusTraversalPolicy;

    /**
     * Indicates whether this Component is the root of a focus traversal cycle.
     * Once focus enters a traversal cycle, typically it cannot leave it via
     * focus traversal unless one of the up- or down-cycle keys is pressed.
     * Normal traversal is limited to this Container, and all of this
     * Container's descendants that are not descendants of inferior focus cycle
     * roots.
     *
     * @see #setFocusCycleRoot
     * @see #isFocusCycleRoot
     * @since 1.4
     */
    private boolean focusCycleRoot = false;


    /**
     * Stores the value of focusTraversalPolicyProvider property.
     * @since 1.5
     * @see #setFocusTraversalPolicyProvider
     */
    private boolean focusTraversalPolicyProvider;

    // keeps track of the threads that are printing this component
//    private transient Set printingThreads;
    // True if there is at least one thread that's printing this component
//    private transient boolean printing = false;

    transient ContainerListener containerListener;

    /* HierarchyListener and HierarchyBoundsListener support */
    transient int listeningChildren;
    transient int listeningBoundsChildren;
    transient int descendantsCount;

    /* Non-opaque window support -- see Window.setLayersOpaque */
    transient Color preserveBackgroundColor = null;

    /**
     * JDK 1.1 serialVersionUID
     */
    //private static final long serialVersionUID = 4613797578919906343L;

    /**
     * A constant which toggles one of the controllable behaviors
     * of <code>getMouseEventTarget</code>. It is used to specify whether
     * the method can return the Container on which it is originally called
     * in case if none of its children are the current mouse event targets.
     *
     * @see #getMouseEventTarget(int, int, boolean, boolean, boolean)
     */
    static final boolean INCLUDE_SELF = true;

    /**
     * A constant which toggles one of the controllable behaviors
     * of <code>getMouseEventTarget</code>. It is used to specify whether
     * the method should search only lightweight components.
     *
     * @see #getMouseEventTarget(int, int, boolean, boolean, boolean)
     */
    static final boolean SEARCH_HEAVYWEIGHTS = true;

    /*
     * Number of HW or LW components in this container (including
     * all descendant containers).
     */
    private transient int numOfHWComponents = 0;
    private transient int numOfLWComponents = 0;

		public final static Insets NULL_INSETS = new Insets(0, 0, 0, 0);

//    private static final Logger mixingLog = Logger.getLogger("java.awt.mixing.Container");
//
//    /**
//     * @serialField ncomponents                     int
//     *       The number of components in this container.
//     *       This value can be null.
//     * @serialField component                       Component[]
//     *       The components in this container.
//     * @serialField layoutMgr                       LayoutManager
//     *       Layout manager for this container.
//     * @serialField dispatcher                      LightweightDispatcher
//     *       Event router for lightweight components.  If this container
//     *       is native, this dispatcher takes care of forwarding and
//     *       retargeting the events to lightweight components contained
//     *       (if any).
//     * @serialField maxSize                         Dimension
//     *       Maximum size of this Container.
//     * @serialField focusCycleRoot                  boolean
//     *       Indicates whether this Component is the root of a focus traversal cycle.
//     *       Once focus enters a traversal cycle, typically it cannot leave it via
//     *       focus traversal unless one of the up- or down-cycle keys is pressed.
//     *       Normal traversal is limited to this Container, and all of this
//     *       Container's descendants that are not descendants of inferior focus cycle
//     *       roots.
//     * @serialField containerSerializedDataVersion  int
//     *       Container Serial Data Version.
//     * @serialField focusTraversalPolicyProvider    boolean
//     *       Stores the value of focusTraversalPolicyProvider property.
//     */
//    private static final ObjectStreamField[] serialPersistentFields = {
//        new ObjectStreamField("ncomponents", Integer.TYPE),
//        new ObjectStreamField("component", Component[].class),
//        new ObjectStreamField("layoutMgr", LayoutManager.class),
//        new ObjectStreamField("dispatcher", LightweightDispatcher.class),
//        new ObjectStreamField("maxSize", Dimension.class),
//        new ObjectStreamField("focusCycleRoot", Boolean.TYPE),
//        new ObjectStreamField("containerSerializedDataVersion", Integer.TYPE),
//        new ObjectStreamField("focusTraversalPolicyProvider", Boolean.TYPE),
//    };
//
//    static {
//        /* ensure that the necessary native libraries are loaded */
//        Toolkit.loadLibraries();
//        if (!GraphicsEnvironment.isHeadless()) {
//            initIDs();
//        }
//    }
//
//    /**
//     * Initialize JNI field and method IDs for fields that may be
//       called from C.
//     */
//    //private static native void initIDs();
//
    /**
     * The only constructor for container
     * Constructs a new Container. Containers can be extended directly,
     * but are lightweight in this case and must be contained by a parent
     * somewhere higher up in the component tree that is native.
     * (such as Frame for example).
     *
     */
    public Container() {
    	component = new Lst<Component>();
		 秘paintClass = 秘updateClass = /**@j2sNative C$ || */null;
    }

		@Override
		void initializeFocusTraversalKeys() {
        //focusTraversalKeys = new Set[4];
    }

    /**
     * Gets the number of components in this panel.
     * @return    the number of components in this panel.
     * @see       #getComponent
     * @since     JDK1.1
     */
    public int getComponentCount() {
        return countComponents();
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by getComponentCount().
     */
    @Deprecated
    public int countComponents() {
        //synchronized (getTreeLock()) {
            return component.size();
        //}
    }

    /**
     * Gets the nth component in this container.
     * @param      n   the index of the component to get.
     * @return     the n<sup>th</sup> component in this container.
     * @exception  ArrayIndexOutOfBoundsException
     *                 if the n<sup>th</sup> value does not exist.
     */
    public Component getComponent(int n) {
        //synchronized (getTreeLock()) {
            if ((n < 0) || (n >= component.size())) {
                throw new ArrayIndexOutOfBoundsException("No such child: " + n);
            }
            return component.get(n);
        //}
    }

    /**
     * Gets all the components in this container.
     * @return    an array of all the components in this container.
     */
    public Component[] getComponents() {
        return getComponents_NoClientCode();
    }
    // NOTE: This method may be called by privileged threads.
    //       This functionality is implemented in a package-private method
    //       to insure that it cannot be overridden by client subclasses.
    //       DO NOT INVOKE CLIENT CODE ON THIS THREAD!
    final Component[] getComponents_NoClientCode() {
      //  synchronized (getTreeLock()) {
            return component.toArray(EMPTY_ARRAY);
     //   }
    } // getComponents_NoClientCode()
    
    /**
     * Determines the insets of this container, which indicate the size
     * of the container's border.
     * <p>
     * A <code>Frame</code> object, for example, has a top inset that
     * corresponds to the height of the frame's title bar.
     * @return    the insets of this container.
     * @see       Insets
     * @see       LayoutManager
     * @since     JDK1.1
     */
    public Insets getInsets() {
    	// Panel, ScrollPane, and Window only
    	return (peer instanceof ContainerPeer ? this.秘getInsetsC() : NULL_INSETS);
    }

    public Insets 秘getInsetsC() {
    	// in SwingJS, we do not clone. Everything is a ContainerPeer.
    	// it is inconsistent with other classes that this would need cloning.
    	Insets i = (peer == null ? null : ((ContainerPeer) peer).getInsets());
    	return  (i == null ? NULL_INSETS : i);
    }

    @Deprecated
    public Insets insets() {
    	return getInsets();
    }
    
    /**
     * Appends the specified component to the end of this container.
     * This is a convenience method for {@link #addImpl}.
     * <p>
     * Note: If a component has been added to a container that
     * has been displayed, <code>validate</code> must be
     * called on that container to display the new component.
     * If multiple components are being added, you can improve
     * efficiency by calling <code>validate</code> only once,
     * after all the components have been added.
     *
     * @param     comp   the component to be added
     * @exception NullPointerException if {@code comp} is {@code null}
     * @see #addImpl
     * @see #validate
     * @see javax.swing.JComponent#revalidate()
     * @return    the component argument
     * 
     */
    public Component add(Component comp) {
        addImpl(comp, null, -1);
        return comp;
    }

    /**
     * Adds the specified component to this container.
     * This is a convenience method for {@link #addImpl}.
     * <p>
     * This method is obsolete as of 1.1.  Please use the
     * method <code>add(Component, Object)</code> instead.
     * @exception NullPointerException if {@code comp} is {@code null}
     * @see #add(Component, Object)
     * 
     */
    public Component add(String name, Component comp) {
        addImpl(comp, name, -1);
        return comp;
    }

    /**
     * Adds the specified component to this container at the given
     * position.
     * This is a convenience method for {@link #addImpl}.
     * <p>
     * Note: If a component has been added to a container that
     * has been displayed, <code>validate</code> must be
     * called on that container to display the new component.
     * If multiple components are being added, you can improve
     * efficiency by calling <code>validate</code> only once,
     * after all the components have been added.
     *
     * @param     comp   the component to be added
     * @param     index    the position at which to insert the component,
     *                   or <code>-1</code> to append the component to the end
     * @exception NullPointerException if {@code comp} is {@code null}
     * @exception IllegalArgumentException if {@code index} is invalid (see
     *            {@link #addImpl} for details)
     * @return    the component <code>comp</code>
     * @see #addImpl
     * @see #remove
     * @see #validate
     * @see javax.swing.JComponent#revalidate()
     * 
     */
    public Component add(Component comp, int index) {
        addImpl(comp, null, index);
        return comp;
    }

    /**
     * Checks that the component
     * isn't supposed to be added into itself.
     */
    private void checkAddToSelf(Component comp){
        if (comp instanceof Container) {
            for (Container cn = this; cn != null; cn=cn.parent) {
                if (cn == comp) {
                    throw new IllegalArgumentException("adding container's parent to itself");
                }
            }
        }
    }

//    /**
//     * Checks that the component is not a Window instance.
//     */
//    private void checkNotAWindow(Component comp){
//        if (comp instanceof Window && ((JSComponent) comp).getUIClassID() != "InternalFrameUI") {
//            throw new IllegalArgumentException("adding a window to a container");
//        }
//    }

//    /**
//     * Checks that the component comp can be added to this container
//     * Checks :  index in bounds of container's size,
//     * comp is not one of this container's parents,
//     * and comp is not a window.
//     * Comp and container must be on the same GraphicsDevice.
//     * if comp is container, all sub-components must be on
//     * same GraphicsDevice.
//     *
//     * @since 1.5
//     */
//    private void checkAdding(Component comp, int index) {
//        //checkTreeLock();
//
//        GraphicsConfiguration thisGC = getGraphicsConfiguration();
//
//        if (index > component.size() || index < 0) {
//            throw new IllegalArgumentException("illegal component position");
//        }
//        if (comp.parent == this) {
//            if (index == component.size()) {
//                throw new IllegalArgumentException("illegal component position " +
//                                                   index + " should be less then " + component.size());
//            }
//        }
//        checkAddToSelf(comp);
//        checkNotAWindow(comp);
//
//        Window thisTopLevel = getContainingWindow();
//        Window compTopLevel = comp.getContainingWindow();
//        if (thisTopLevel != compTopLevel) {
//            throw new IllegalArgumentException("component and container should be in the same top-level window");
//        }
//        if (thisGC != null) {
//            comp.checkGD(thisGC.getDevice().getIDstring());
//        }
//    }

    /**
     * Removes component comp from this container without making unneccessary changes
     * and generating unneccessary events. This function intended to perform optimized
     * remove, for example, if newParent and current parent are the same it just changes
     * index without calling removeNotify.
     * Note: Should be called while holding treeLock
     * Returns whether removeNotify was invoked
     * @since: 1.5
     */
    private boolean removeDelicately(Component comp, Container newParent, int newIndex) {
        //checkTreeLock();

        int index = getComponentZOrder(comp);
        boolean needRemoveNotify = isRemoveNotifyNeeded(comp, this, newParent);
        if (needRemoveNotify) {
            comp.removeNotify();
        }
        if (newParent != this) {
            if (layoutMgr != null) {
                layoutMgr.removeLayoutComponent(comp);
            }
            adjustListeningChildren(AWTEvent.HIERARCHY_EVENT_MASK,
                                    -comp.numListening(AWTEvent.HIERARCHY_EVENT_MASK));
            adjustListeningChildren(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK,
                                    -comp.numListening(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK));
            adjustDescendants(-(comp.countHierarchyMembers()));

            comp.parent = null;
            component.removeItemAt(index);
            秘childTainted = true;

            invalidateIfValid();
        } else {
            // We should remove component and then
            // add it by the newIndex without newIndex decrement if even we shift components to the left
            // after remove. Consult the rules below:
            // 2->4: 012345 -> 013425, 2->5: 012345 -> 013452
            // 4->2: 012345 -> 014235
            component.removeItemAt(index);
            component.add(newIndex, comp);
            秘childTainted = true;
        }
        if (comp.parent == null) { // was actually removed
            if (containerListener != null ||
                (eventMask & AWTEvent.CONTAINER_EVENT_MASK) != 0 ||
                Toolkit.enabledOnToolkit(AWTEvent.CONTAINER_EVENT_MASK)) {
                ContainerEvent e = new ContainerEvent(this,
                                                      ContainerEvent.COMPONENT_REMOVED,
                                                      comp);
                dispatchEvent(e);

            }
            comp.createHierarchyEvents(HierarchyEvent.HIERARCHY_CHANGED, comp,
                                       this, HierarchyEvent.PARENT_CHANGED,
                                       Toolkit.enabledOnToolkit(AWTEvent.HIERARCHY_EVENT_MASK));
            if (layoutMgr == null && isDisplayable() && isVisible()) {
                updateCursorImmediately();
            }
        }
        return needRemoveNotify;
    }

    /**
     * Checks whether this container can contain component which is focus owner.
     * Verifies that container is enable and showing, and if it is focus cycle root
     * its FTP allows component to be focus owner
     * @since 1.5
     */
    protected boolean canContainFocusOwner(Component focusOwnerCandidate) {
        if (!(isEnabled() && isDisplayable()
              && isVisible() && isFocusable()))
        {
            return false;
        }
        if (isFocusCycleRoot()) {
            FocusTraversalPolicy policy = getFocusTraversalPolicy();
            if (policy instanceof DefaultFocusTraversalPolicy) {
                if (!((DefaultFocusTraversalPolicy)policy).accept(focusOwnerCandidate)) {
                    return false;
                }
            }
        }
        synchronized(getTreeLock()) {
            if (parent != null) {
                return parent.canContainFocusOwner(focusOwnerCandidate);
            }
        }
        return true;
    }

    /**
     * Checks whether or not this container has heavyweight children.
     * Note: Should be called while holding tree lock
     * @return true if there is at least one heavyweight children in a container, false otherwise
     * @since 1.5
     */
    final boolean hasHeavyweightDescendants() {
        //checkTreeLock();
    	// SwingJS we do not care
        return numOfHWComponents > 0;
    }

    /**
     * Checks whether or not this container has lightweight children.
     * Note: Should be called while holding tree lock
     * @return true if there is at least one lightweight children in a container, false otherwise
     * @since 1.7
     */
    final boolean hasLightweightDescendants() {
        //checkTreeLock();
    	// SwingJS we do not care
        return numOfLWComponents > 0;
    }

    /**
     * Returns closest heavyweight component to this container. If this container is heavyweight
     * returns this.
     * @since 1.5
     */
    Container getHeavyweightContainer() {
        //checkTreeLock();
        if (peer != null && !(peer instanceof LightweightPeer)) {
            return this;
        } else {
            return getNativeContainer();
        }
    }

    /**
     * Detects whether or not remove from current parent and adding to new parent requires call of
     * removeNotify on the component. Since removeNotify destroys native window this might (not)
     * be required. For example, if new container and old containers are the same we don't need to
     * destroy native window.
     * @since: 1.5
     */
    private static boolean isRemoveNotifyNeeded(Component comp, Container oldContainer, Container newContainer) {
    	return false;
//        if (oldContainer == null) { // Component didn't have parent - no removeNotify
//            return false;
//        }
//        if (comp.peer == null) { // Component didn't have peer - no removeNotify
//            return false;
//        }
//        if (newContainer.peer == null) {
//            // Component has peer but new Container doesn't - call removeNotify
//            return true;
//        }
//
//        // If component is lightweight non-Container or lightweight Container with all but heavyweight
//        // children there is no need to call remove notify
//        if (comp.isLightweight()) {
//            if (comp instanceof Container) {
//                // If it has heavyweight children then removeNotify is required
//                return ((Container)comp).hasHeavyweightDescendants();
//            } else {
//                // Just a lightweight
//                return false;
//            }
//        }
//
//        // All three components have peers, check for peer change
//        Container newNativeContainer = oldContainer.getHeavyweightContainer();
//        Container oldNativeContainer = newContainer.getHeavyweightContainer();
//        if (newNativeContainer != oldNativeContainer) {
//            // Native containers change - check whether or not current platform supports
//            // changing of widget hierarchy on native level without recreation.
//            return !comp.peer.isReparentSupported();
//        } else {
//            // if container didn't change we still might need to recreate component's window as
//            // changes to zorder should be reflected in native window stacking order and it might
//            // not be supported by the platform. This is important only for heavyweight child
//            return !comp.isLightweight() &&
//                !((ContainerPeer)(newNativeContainer.peer)).isRestackSupported();
//        }
    }

    /**
     * Moves the specified component to the specified z-order index in
     * the container. The z-order determines the order that components
     * are painted; the component with the highest z-order paints first
     * and the component with the lowest z-order paints last.
     * Where components overlap, the component with the lower
     * z-order paints over the component with the higher z-order.
     * <p>
     * If the component is a child of some other container, it is
     * removed from that container before being added to this container.
     * The important difference between this method and
     * <code>java.awt.Container.add(Component, int)</code> is that this method
     * doesn't call <code>removeNotify</code> on the component while
     * removing it from its previous container unless necessary and when
     * allowed by the underlying native windowing system. This way, if the
     * component has the keyboard focus, it maintains the focus when
     * moved to the new position.
     * <p>
     * This property is guaranteed to apply only to lightweight
     * non-<code>Container</code> components.
     * <p>
     * <b>Note</b>: Not all platforms support changing the z-order of
     * heavyweight components from one container into another without
     * the call to <code>removeNotify</code>. There is no way to detect
     * whether a platform supports this, so developers shouldn't make
     * any assumptions.
     *
     * @param     comp the component to be moved
     * @param     index the position in the container's list to
     *            insert the component, where <code>getComponentCount()</code>
     *            appends to the end
     * @exception NullPointerException if <code>comp</code> is
     *            <code>null</code>
     * @exception IllegalArgumentException if <code>comp</code> is one of the
     *            container's parents
     * @exception IllegalArgumentException if <code>index</code> is not in
     *            the range <code>[0, getComponentCount()]</code> for moving
     *            between containers, or not in the range
     *            <code>[0, getComponentCount()-1]</code> for moving inside
     *            a container
     * @exception IllegalArgumentException if adding a container to itself
     * @exception IllegalArgumentException if adding a <code>Window</code>
     *            to a container
     * @see #getComponentZOrder(java.awt.Component)
     * @since 1.5
     */
    public void setComponentZOrder(Component comp, int index) {
         synchronized (getTreeLock()) {
             // Store parent because remove will clear it
             Container curParent = comp.parent;
             int oldZindex = getComponentZOrder(comp);

             if (curParent == this && index == oldZindex) {
                 return;
             }
             //checkAdding(comp, index);

             boolean peerRecreated = (curParent != null) ?
                 curParent.removeDelicately(comp, this, index) : false;

             addDelicately(comp, curParent, index);

             // If the oldZindex == -1, the component gets inserted,
             // rather than it changes its z-order.
             if (!peerRecreated && oldZindex != -1) {
                 // The new 'index' cannot be == -1.
                 // It gets checked at the checkAdding() method.
                 // Therefore both oldZIndex and index denote
                 // some existing positions at this point and
                 // this is actually a Z-order changing.
                 comp.mixOnZOrderChanging(oldZindex, index);
             }
             
             秘updateUIZOrder();
             
         }
    }

	/**
     * Traverses the tree of components and reparents children heavyweight component
     * to new heavyweight parent.
     * @since 1.5
     */
    @SuppressWarnings("deprecation")
	private void reparentTraverse(ContainerPeer parentPeer, Container child) {
        checkTreeLock();

        for (int i = 0; i < child.getComponentCount(); i++) {
            Component comp = child.getComponent(i);
            if (comp.isLightweight()) {
                // If components is lightweight check if it is container
                // If it is container it might contain heavyweight children we need to reparent
                if (comp instanceof Container) {
                    reparentTraverse(parentPeer, (Container)comp);
                }
            } else {
                // Q: Need to update NativeInLightFixer?
                comp.getPeer().reparent(parentPeer);
            }
        }
    }

    /**
     * Reparents child component peer to this container peer.
     * Container must be heavyweight.
     * @since 1.5
     */
    @SuppressWarnings("deprecation")
	private void reparentChild(Component comp) {
//        checkTreeLock();
        if (comp == null) {
            return;
        }
        if (comp.isLightweight()) {
        	
        	// never true in SwingJS
        	
            // If component is lightweight container we need to reparent all its explicit  heavyweight children
            if (comp instanceof Container) {
                // Traverse component's tree till depth-first until encountering heavyweight component
                reparentTraverse((ContainerPeer)getPeer(), (Container)comp);
            }
        } else {
            comp.getPeer().reparent((ContainerPeer)getPeer());
        }
    }

    /**
     * Adds component to this container. Tries to minimize side effects of this adding -
     * doesn't call remove notify if it is not required.
     * @since 1.5
     */
    private void addDelicately(Component comp, Container curParent, int index) {
        checkTreeLock();

        // Check if moving between containers
        if (curParent == this) {
            if (index < component.size()) {
                component.set(index, comp);
                秘childTainted = true;
            }
        } else {
            //index == -1 means add to the end.
            if (index == -1) {
                component.add(comp);
            } else {
                component.add(index, comp);
            }
            秘childTainted = true;
            comp.parent = this;

            adjustListeningChildren(AWTEvent.HIERARCHY_EVENT_MASK,
                                    comp.numListening(AWTEvent.HIERARCHY_EVENT_MASK));
            adjustListeningChildren(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK,
                                    comp.numListening(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK));
            adjustDescendants(comp.countHierarchyMembers());
        }

        invalidateIfValid();
        if (peer != null) {
            if (comp.peer == null) { 
            	// Remove notify was called or it didn't have peer - create new one
                comp.addNotify();
                // New created peer creates component on top of the stacking order
//                Container newNativeContainer = getHeavyweightContainer();
//                if (((ContainerPeer)newNativeContainer.getPeer()).isRestackSupported()) {
//                    ((ContainerPeer)newNativeContainer.getPeer()).restack();
//                }
            } else { // Both container and child have peers, it means child peer should be reparented.
                // In both cases we need to reparent native widgets.
                Container newNativeContainer = getHeavyweightContainer();
                Container oldNativeContainer = curParent.getHeavyweightContainer();
                if (oldNativeContainer != newNativeContainer) {
                    // Native container changed - need to reparent native widgets
                    newNativeContainer.reparentChild(comp);
                }
                // If component still has a peer and it is either container or heavyweight
                // and restack is supported we have to restack native windows since order might have changed
//                if ((!comp.isLightweight() || (comp instanceof Container))
//                    && ((ContainerPeer)newNativeContainer.getPeer()).isRestackSupported())
//                {
//                    ((ContainerPeer)newNativeContainer.getPeer()).restack();
//                }
                if (!comp.isLightweight() && isLightweight()) {
                	// SwingJS cannot reach this.
                    // If component is heavyweight and one of the containers is lightweight
                    // the location of the component should be fixed.
                    comp.relocateComponent();
                }
            }
        }
        if (curParent != this) {
            /* Notify the layout manager of the added component. */
            if (layoutMgr != null) {
                if (layoutMgr instanceof LayoutManager2) {
                    ((LayoutManager2)layoutMgr).addLayoutComponent(comp, null);
                } else {
                    layoutMgr.addLayoutComponent(null, comp);
                }
            }
            if (containerListener != null ||
                (eventMask & AWTEvent.CONTAINER_EVENT_MASK) != 0 ||
                Toolkit.enabledOnToolkit(AWTEvent.CONTAINER_EVENT_MASK)) {
                ContainerEvent e = new ContainerEvent(this,
                                                      ContainerEvent.COMPONENT_ADDED,
                                                      comp);
                dispatchEvent(e);
            }
            comp.createHierarchyEvents(HierarchyEvent.HIERARCHY_CHANGED, comp,
                                       this, HierarchyEvent.PARENT_CHANGED,
                                       Toolkit.enabledOnToolkit(AWTEvent.HIERARCHY_EVENT_MASK));

            // If component is focus owner or parent container of focus owner check that after reparenting
            // focus owner moved out if new container prohibit this kind of focus owner.
//            if (comp.isFocusOwner() && !comp.canBeFocusOwnerRecursively()) {
//                comp.transferFocus();
//            } else if (comp instanceof Container) {
//                Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
//                if (focusOwner != null && isParentOf(focusOwner) && !focusOwner.canBeFocusOwnerRecursively()) {
//                    focusOwner.transferFocus();
//                }
//            }
        } else {
            comp.createHierarchyEvents(HierarchyEvent.HIERARCHY_CHANGED, comp,
                                       this, HierarchyEvent.HIERARCHY_CHANGED,
                                       Toolkit.enabledOnToolkit(AWTEvent.HIERARCHY_EVENT_MASK));
        }

        if (peer != null && layoutMgr == null && isVisible()) {
            updateCursorImmediately();
        }
    }

    private void checkTreeLock() {
			// TODO Auto-generated method stub
			
		}

		/**
     * Returns the z-order index of the component inside the container.
     * The higher a component is in the z-order hierarchy, the lower
     * its index.  The component with the lowest z-order index is
     * painted last, above all other child components.
     *
     * @param comp the component being queried
     * @return  the z-order index of the component; otherwise
     *          returns -1 if the component is <code>null</code>
     *          or doesn't belong to the container
     * @see #setComponentZOrder(java.awt.Component, int)
     * @since 1.5
     */
    public int getComponentZOrder(Component comp) {
        if (comp == null) {
            return -1;
        }
        synchronized(getTreeLock()) {
            // Quick check - container should be immediate parent of the component
            if (comp.parent != this) {
                return -1;
            }
            return component.indexOf(comp);
        }
    }

    /**
     * Adds the specified component to the end of this container.
     * Also notifies the layout manager to add the component to
     * this container's layout using the specified constraints object.
     * This is a convenience method for {@link #addImpl}.
     * <p>
     * Note: If a component has been added to a container that
     * has been displayed, <code>validate</code> must be
     * called on that container to display the new component.
     * If multiple components are being added, you can improve
     * efficiency by calling <code>validate</code> only once,
     * after all the components have been added.
     *
     * @param     comp the component to be added
     * @param     constraints an object expressing
     *                  layout contraints for this component
     * @exception NullPointerException if {@code comp} is {@code null}
     * @see #addImpl
     * @see #validate
     * @see javax.swing.JComponent#revalidate()
     * @see       LayoutManager
     * @since     JDK1.1
     * 
     * 
     */
    public void add(Component comp, Object constraints) {
        addImpl(comp, constraints, -1);
    }

    /**
     * Adds the specified component to this container with the specified
     * constraints at the specified index.  Also notifies the layout
     * manager to add the component to the this container's layout using
     * the specified constraints object.
     * This is a convenience method for {@link #addImpl}.
     * <p>
     * Note: If a component has been added to a container that
     * has been displayed, <code>validate</code> must be
     * called on that container to display the new component.
     * If multiple components are being added, you can improve
     * efficiency by calling <code>validate</code> only once,
     * after all the components have been added.
     *
     * @param comp the component to be added
     * @param constraints an object expressing layout contraints for this
     * @param index the position in the container's list at which to insert
     * the component; <code>-1</code> means insert at the end
     * component
     * @exception NullPointerException if {@code comp} is {@code null}
     * @exception IllegalArgumentException if {@code index} is invalid (see
     *            {@link #addImpl} for details)
     * @see #addImpl
     * @see #validate
     * @see javax.swing.JComponent#revalidate()
     * @see #remove
     * @see LayoutManager
     */
    
    public Component add(Component comp, Object constraints, int index) {
       addImpl(comp, constraints, index);
       return comp;
    }

    /**
     * Adds the specified component to this container at the specified
     * index. This method also notifies the layout manager to add
     * the component to this container's layout using the specified
     * constraints object via the <code>addLayoutComponent</code>
     * method.
     * <p>
     * The constraints are
     * defined by the particular layout manager being used.  For
     * example, the <code>BorderLayout</code> class defines five
     * constraints: <code>BorderLayout.NORTH</code>,
     * <code>BorderLayout.SOUTH</code>, <code>BorderLayout.EAST</code>,
     * <code>BorderLayout.WEST</code>, and <code>BorderLayout.CENTER</code>.
     * <p>
     * The <code>GridBagLayout</code> class requires a
     * <code>GridBagConstraints</code> object.  Failure to pass
     * the correct type of constraints object results in an
     * <code>IllegalArgumentException</code>.
     * <p>
     * If the current layout manager implements {@code LayoutManager2}, then
     * {@link LayoutManager2#addLayoutComponent(Component,Object)} is invoked on
     * it. If the current layout manager does not implement
     * {@code LayoutManager2}, and constraints is a {@code String}, then
     * {@link LayoutManager#addLayoutComponent(String,Component)} is invoked on it.
     * <p>
     * If the component is not an ancestor of this container and has a non-null
     * parent, it is removed from its current parent before it is added to this
     * container.
     * <p>
     * This is the method to override if a program needs to track
     * every add request to a container as all other add methods defer
     * to this one. An overriding method should
     * usually include a call to the superclass's version of the method:
     * <p>
     * <blockquote>
     * <code>super.addImpl(comp, constraints, index)</code>
     * </blockquote>
     * <p>
     * @param     comp       the component to be added
     * @param     constraints an object expressing layout constraints
     *                 for this component
     * @param     index the position in the container's list at which to
     *                 insert the component, where <code>-1</code>
     *                 means append to the end
     * @exception IllegalArgumentException if {@code index} is invalid;
     *            if {@code comp} is a child of this container, the valid
     *            range is {@code [-1, getComponentCount()-1]}; if component is
     *            not a child of this container, the valid range is
     *            {@code [-1, getComponentCount()]}
     *
     * @exception IllegalArgumentException if {@code comp} is an ancestor of
     *                                     this container
     * @exception IllegalArgumentException if adding a window to a container
     * @exception NullPointerException if {@code comp} is {@code null}
     * @see       #add(Component)
     * @see       #add(Component, int)
     * @see       #add(Component, java.lang.Object)
     * @see       LayoutManager
     * @see       LayoutManager2
     * @since     JDK1.1
     */
    protected void addImpl(Component comp, Object constraints, int index) {
    	addImplCont(comp, constraints, index);
    }

	@SuppressWarnings("unused")
	protected void addImplCont(Component comp, Object constraints, int index) {
		synchronized (getTreeLock()) {
			
			if (/** @j2sNative comp.getWrap$  && !this.isWrapper$ || */ false) {
				comp = ((A2SWrappedComponent) comp).秘getWrap();
				comp.background = comp.foreground = null; // this parent should not set the background color				
			}
			// SwingJS used for all add methods

			/*
			 * Check for correct arguments: index in bounds, comp cannot be one of this
			 * container's parents, and comp cannot be a window. comp and container must be
			 * on the same GraphicsDevice. if comp is container, all sub-components must be
			 * on same GraphicsDevice.
			 */
//          GraphicsConfiguration thisGC = this.getGraphicsConfiguration();

			if (index > component.size() || (index < 0 && index != -1)) {
				throw new IllegalArgumentException("illegal component position");
			}
			checkAddToSelf(comp);
			// Here we do not allow JSApplet, but we do allow JInternalFrame, which is a
			// JFrame now
			if (comp.isJ2SWindowButNotJInternalFrame()) {
				throw new IllegalArgumentException("adding a window to a container");
			}

//          checkNotAWindow(comp);
//      if (thisGC != null) {
//          comp.checkGD(thisGC.getDevice().getIDstring());
//      }

			/* Reparent the component and tidy up the tree's state. */
			if (comp.parent != null) {
				comp.parent.remove(comp);
				if (index > component.size()) {
					throw new IllegalArgumentException("illegal component position");
				}
			}

			// index == -1 means add to the end.
			if (index == -1) {
				component.add(comp);
			} else {
				component.add(index, comp);
			}
			秘childTainted = true;
			comp.parent = this;

			adjustListeningChildren(AWTEvent.HIERARCHY_EVENT_MASK, comp.numListening(AWTEvent.HIERARCHY_EVENT_MASK));
			adjustListeningChildren(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK,
					comp.numListening(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK));
			adjustDescendants(comp.countHierarchyMembers());

			invalidateIfValid();
			if (peer != null) {
				comp.addNotify();
			}

			/* Notify the layout manager of the added component. */
			if (layoutMgr != null) {
				if (layoutMgr instanceof LayoutManager2) {
					((LayoutManager2) layoutMgr).addLayoutComponent(comp, constraints);
				} else if (constraints instanceof String) {
					layoutMgr.addLayoutComponent((String) constraints, comp);
				}
			}
			if (containerListener != null || (eventMask & AWTEvent.CONTAINER_EVENT_MASK) != 0
					|| Toolkit.enabledOnToolkit(AWTEvent.CONTAINER_EVENT_MASK)) {
				ContainerEvent e = new ContainerEvent(this, ContainerEvent.COMPONENT_ADDED, comp);
				dispatchEvent(e);
			}

			comp.createHierarchyEvents(HierarchyEvent.HIERARCHY_CHANGED, comp, this, HierarchyEvent.PARENT_CHANGED,
					Toolkit.enabledOnToolkit(AWTEvent.HIERARCHY_EVENT_MASK));
			if (peer != null && layoutMgr == null && isVisible()) {
				updateCursorImmediately();
			}
		}
	}

    

		/**
     * Checks that all Components that this Container contains are on
     * the same GraphicsDevice as this Container.  If not, throws an
     * IllegalArgumentException.
     */
    void checkGD(String stringID) {
//        for (Component comp : component) {
//            if (comp != null) {
//                comp.checkGD(stringID);
//            }
//        }
    }

    /**
     * Removes the component, specified by <code>index</code>,
     * from this container.
     * This method also notifies the layout manager to remove the
     * component from this container's layout via the
     * <code>removeLayoutComponent</code> method.
     *
     * <p>
     * Note: If a component has been removed from a container that
     * had been displayed, {@link #validate} must be
     * called on that container to reflect changes.
     * If multiple components are being removed, you can improve
     * efficiency by calling {@link #validate} only once,
     * after all the components have been removed.
     *
     * @param     index   the index of the component to be removed
     * @throws ArrayIndexOutOfBoundsException if {@code index} is not in
     *         range {@code [0, getComponentCount()-1]}
     * @see #add
     * @see #validate
     * @see #getComponentCount
     * @since JDK1.1
     * 
     */
    public void remove(int index) {
		synchronized (getTreeLock()) {
			if (index < 0 || index >= component.size()) {
				throw new ArrayIndexOutOfBoundsException(index);
			}
			Component comp = component.get(index);
			if (peer != null) {
				comp.removeNotify();
			}
			if (layoutMgr != null) {
				layoutMgr.removeLayoutComponent(comp);
			}

			adjustListeningChildren(AWTEvent.HIERARCHY_EVENT_MASK,
					-comp.numListening(AWTEvent.HIERARCHY_EVENT_MASK));
			adjustListeningChildren(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK,
					-comp.numListening(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK));
			adjustDescendants(-(comp.countHierarchyMembers()));

			comp.parent = null;
			component.removeItemAt(index);
            秘childTainted = true;

			invalidateIfValid();
			if (containerListener != null
					|| (eventMask & AWTEvent.CONTAINER_EVENT_MASK) != 0
					|| Toolkit.enabledOnToolkit(AWTEvent.CONTAINER_EVENT_MASK)) {
				ContainerEvent e = new ContainerEvent(this,
						ContainerEvent.COMPONENT_REMOVED, comp);
				dispatchEvent(e);
			}

			comp.createHierarchyEvents(HierarchyEvent.HIERARCHY_CHANGED, comp, this,
					HierarchyEvent.PARENT_CHANGED,
					Toolkit.enabledOnToolkit(AWTEvent.HIERARCHY_EVENT_MASK));
			if (peer != null && layoutMgr == null && isVisible()) {
				updateCursorImmediately();
			}
		}
	}

		/**
     * Removes the specified component from this container.
     * This method also notifies the layout manager to remove the
     * component from this container's layout via the
     * <code>removeLayoutComponent</code> method.
     *
     * <p>
     * Note: If a component has been removed from a container that
     * had been displayed, {@link #validate} must be
     * called on that container to reflect changes.
     * If multiple components are being removed, you can improve
     * efficiency by calling {@link #validate} only once,
     * after all the components have been removed.
     *
     * @param comp the component to be removed
     * @see #add
     * @see #validate
     * @see #remove(int)
     */
    @SuppressWarnings("unused")
	public void remove(Component comp) {
		synchronized (getTreeLock()) {

			if (/** @j2sNative comp.getWrap$ && !this.isWrapper$ || */ false) {
				comp = ((A2SWrappedComponent) comp).秘getWrap();
			}

			
			if (comp.parent == this) {
				int index = component.indexOf(comp);
				if (index >= 0) {
					remove(index);
				}
			}
		}
	}

		/**
     * Removes all the components from this container.
     * This method also notifies the layout manager to remove the
     * components from this container's layout via the
     * <code>removeLayoutComponent</code> method.
     * @see #add
     * @see #remove
     */
    @Override
	public void removeAll() { 
        synchronized (getTreeLock()) {
            adjustListeningChildren(AWTEvent.HIERARCHY_EVENT_MASK,
                                    -listeningChildren);
            adjustListeningChildren(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK,
                                    -listeningBoundsChildren);
            adjustDescendants(-descendantsCount);

            while (!component.isEmpty()) {
                Component comp = component.removeItemAt(component.size()-1);

                if (peer != null) {
                    comp.removeNotify();
                }
                if (layoutMgr != null) {
                    layoutMgr.removeLayoutComponent(comp);
                }
                comp.parent = null;
                if (containerListener != null ||
                   (eventMask & AWTEvent.CONTAINER_EVENT_MASK) != 0 ||
                    Toolkit.enabledOnToolkit(AWTEvent.CONTAINER_EVENT_MASK)) {
                    ContainerEvent e = new ContainerEvent(this,
                                     ContainerEvent.COMPONENT_REMOVED,
                                     comp);
                    dispatchEvent(e);
                }

                comp.createHierarchyEvents(HierarchyEvent.HIERARCHY_CHANGED,
                                           comp, this,
                                           HierarchyEvent.PARENT_CHANGED,
                                           Toolkit.enabledOnToolkit(AWTEvent.HIERARCHY_EVENT_MASK));
            }
            if (peer != null && layoutMgr == null && isVisible()) {
                updateCursorImmediately();
            }
            invalidateIfValid();
        }
        super.removeAll();
    }

    // Should only be called while holding tree lock
    @Override
		int numListening(long mask) {
        int superListening = numListeningMask(mask);

        if (mask == AWTEvent.HIERARCHY_EVENT_MASK) {
//            if (eventLog.isLoggable(Level.FINE)) {
//                // Verify listeningChildren is correct
//                int sum = 0;
//                for (Component comp : component) {
//                    sum += comp.numListening(mask);
//                }
//                if (listeningChildren != sum) {
//                    eventLog.log(Level.FINE, "Assertion (listeningChildren == sum) failed");
//                }
//            }
            return listeningChildren + superListening;
        } else if (mask == AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK) {
//            if (eventLog.isLoggable(Level.FINE)) {
//                // Verify listeningBoundsChildren is correct
//                int sum = 0;
//                for (Component comp : component) {
//                    sum += comp.numListening(mask);
//                }
//                if (listeningBoundsChildren != sum) {
//                    eventLog.log(Level.FINE, "Assertion (listeningBoundsChildren == sum) failed");
//                }
//            }
            return listeningBoundsChildren + superListening;
        } else {
//            // assert false;
//            if (eventLog.isLoggable(Level.FINE)) {
//                eventLog.log(Level.FINE, "This code must never be reached");
//            }
            return superListening;
        }
    }

    // Should only be called while holding tree lock
    void adjustListeningChildren(long mask, int num) {
//        if (eventLog.isLoggable(Level.FINE)) {
//            boolean toAssert = (mask == AWTEvent.HIERARCHY_EVENT_MASK ||
//                                mask == AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK ||
//                                mask == (AWTEvent.HIERARCHY_EVENT_MASK |
//                                         AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK));
//            if (!toAssert) {
//                eventLog.log(Level.FINE, "Assertion failed");
//            }
//        }
//
        if (num == 0)
            return;

        if ((mask & AWTEvent.HIERARCHY_EVENT_MASK) != 0) {
            listeningChildren += num;
        }
        if ((mask & AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK) != 0) {
            listeningBoundsChildren += num;
        }

        adjustListeningChildrenOnParent(mask, num);
    }

    // Should only be called while holding tree lock
    void adjustDescendants(int num) {
        if (num == 0)
            return;

        descendantsCount += num;
        adjustDecendantsOnParent(num);
    }

    // Should only be called while holding tree lock
    void adjustDecendantsOnParent(int num) {
        if (parent != null) {
            parent.adjustDescendants(num);
        }
    }

    // Should only be called while holding tree lock
    @Override
		int countHierarchyMembers() {
//        if (log.isLoggable(Level.FINE)) {
//            // Verify descendantsCount is correct
//            int sum = 0;
//            for (Component comp : component) {
//                sum += comp.countHierarchyMembers();
//            }
//            if (descendantsCount != sum) {
//                log.log(Level.FINE, "Assertion (descendantsCount == sum) failed");
//            }
//        }
        return descendantsCount + 1;
    }

    private int getListenersCount(int id, boolean enabledOnToolkit) {
        //assert Thread.holdsLock(getTreeLock());
        if (enabledOnToolkit) {
            return descendantsCount;
        }
        switch (id) {
          case HierarchyEvent.HIERARCHY_CHANGED:
            return listeningChildren;
          case HierarchyEvent.ANCESTOR_MOVED:
          case HierarchyEvent.ANCESTOR_RESIZED:
            return listeningBoundsChildren;
          default:
            return 0;
        }
    }

    @Override
	protected
		final int createHierarchyEvents(int id, Component changed,
        Container changedParent, long changeFlags, boolean enabledOnToolkit)
    {
        //assert Thread.holdsLock(getTreeLock());
        int listeners = getListenersCount(id, enabledOnToolkit);

        for (int count = listeners, i = 0; count > 0; i++) {
            count -= component.get(i).createHierarchyEvents(id, changed,
                changedParent, changeFlags, enabledOnToolkit);
        }
        return listeners +
            createHierEventsComp(id, changed, changedParent,
                                        changeFlags, enabledOnToolkit);
    }

    final void createChildHierarchyEvents(int id, long changeFlags,
        boolean enabledOnToolkit)
    {
        //assert Thread.holdsLock(getTreeLock());
        if (component.isEmpty()) {
            return;
        }
        int listeners = getListenersCount(id, enabledOnToolkit);

        for (int count = listeners, i = 0; count > 0; i++) {
            count -= component.get(i).createHierarchyEvents(id, this, parent,
                changeFlags, enabledOnToolkit);
        }
    }

    /**
     * Gets the layout manager for this container.
     * @see #doLayout
     * @see #setLayout
     */
    public LayoutManager getLayout() {
        return layoutMgr;
    }

    /**
     * Sets the layout manager for this container.
     * @param mgr the specified layout manager
     * @see #doLayout
     * @see #getLayout
     */
    public void setLayout(LayoutManager mgr) {
        layoutMgr = mgr;
        invalidateIfValid();
    }

    /**
     * Causes this container to lay out its components.  Most programs
     * should not call this method directly, but should invoke
     * the <code>validate</code> method instead.
     * @see LayoutManager#layoutContainer
     * @see #setLayout
     * @see #validate
     * @since JDK1.1
     */
    @Override
		public void doLayout() {
    	// called by Container.validateTree
        layout();
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by <code>doLayout()</code>.
     */
    @Override
		@Deprecated
    public void layout() {
    	// called by Component and Container
    	// The reason we don't care about w&h is that 
    	// we can be working just with preferences here. 
    	// This is when getPreferredSize() is copied to getSize()
        if (layoutMgr != null)
        	layoutMgr.layoutContainer(this);
    }

    /**
     * Invalidates the container.  The container and all parents
     * above it are marked as needing to be laid out.  This method can
     * be called often, so it needs to execute quickly.
     *
     * <p> If the {@code LayoutManager} installed on this container is
     * an instance of {@code LayoutManager2}, then
     * {@link LayoutManager2#invalidateLayout(Container)} is invoked on
     * it supplying this {@code Container} as the argument.
     *
     * @see #validate
     * @see #layout
     * @see LayoutManager
     * @see LayoutManager2#invalidateLayout(Container)
     */
    @Override
		public void invalidate() {
        LayoutManager layoutMgr = this.layoutMgr;
        if (layoutMgr instanceof LayoutManager2) {
            LayoutManager2 lm = (LayoutManager2) layoutMgr;
            lm.invalidateLayout(this);
        }
        invalidateComp();
    }

	/**
	 * Validates this container and all of its subcomponents.
	 * <p>
	 * The <code>validate</code> method is used to cause a container to lay out
	 * its subcomponents again. It should be invoked when this container's
	 * subcomponents are modified (added to or removed from the container, or
	 * layout-related information changed) after the container has been displayed.
	 * 
	 * <p>
	 * If this {@code Container} is not valid, this method invokes the
	 * {@code validateTree} method and marks this {@code Container} as valid.
	 * Otherwise, no action is performed.
	 * 
	 * @see #add(java.awt.Component)
	 * @see Component#invalidate
	 * @see javax.swing.JComponent#revalidate()
	 * @see #validateTree
	 * 
	 * 
	 */
	@Override
	public void validate() {
		/* Avoid grabbing lock unless really necessary. */
		if (!isValid() && peer != null) {
			synchronized (getTreeLock()) {

				// validation in AWT prior to addNotify will cause NPE for TextArea without font
				
				// for SwingJS ALL components must have peers. might as well do that
				// now.
				// I think there was a notification threading issue that the root pane
				// was not
				// getting its peer in time for validation.
//				if (peer == null)
//					peer = getToolkit().createComponent(this);
				int n = component.size();
				if (!isValid() && peer != null && n > 0) {
					ContainerPeer p = null;
					if (peer instanceof ContainerPeer)
						p = (ContainerPeer) peer;
					if (p != null)
						p.beginValidate();
					validateTree();
					if (p != null) {
						p.endValidate();
						if (isVisible())
							updateCursorImmediately();
					}
				}
			}
		}
	}

	// BH SwingJS These next two methods are moved from Window so that we can resize in-line applets 
	//
  public void repackContainer() {
    Dimension newSize = getPreferredSize();
    if (peer != null) {
        setClientSize(newSize.width, newSize.height);
    }
    validate();
	}

	void setClientSize(int w, int h) {
		synchronized (getTreeLock()) {
			setBoundsOp(ComponentPeer.SET_CLIENT_SIZE);
			setBounds(x, y, w, h);
		}
	}


    /**
     * Recursively descends the container tree and recomputes the
     * layout for any subtrees marked as needing it (those marked as
     * invalid).  Synchronization should be provided by the method
     * that calls this one:  <code>validate</code>.
     *
     * @see #doLayout
     * @see #validate
     */
    public void validateTree() {
        if (!isValid()) {
            if (peer instanceof ContainerPeer) {
                ((ContainerPeer)peer).beginLayout();
            }
            doLayout();
            for (int i = 0; i < component.size(); i++) {
                Component comp = component.get(i);
                if (   (comp instanceof Container)
    // SwingJS needs to create all DIV elements
    //               && !(comp instanceof Window)
                       && !comp.isValid()) {
                    ((Container)comp).validateTree();
                } else {
                    comp.validate();
                }
            }
            if (peer instanceof ContainerPeer) {
                ((ContainerPeer)peer).endLayout();
            }
        }
        super.validate();
    }

    /**
     * Recursively descends the container tree and invalidates all
     * contained components.
     */
    public void invalidateTree() { // SwingJS -- need this public for ToolTipManager PopupFactory
        synchronized (getTreeLock()) {
            for (int i = 0; i < component.size(); i++) {
                Component comp = component.get(i);
                if (comp instanceof Container) {
                    ((Container)comp).invalidateTree();
                }
                else {
                    comp.invalidateIfValid();
                }
            }
            invalidateIfValid();
        }
    }

	/**
	 * Sets the font of this container.
	 * 
	 * @param f
	 *          The font to become this container's font.
	 * @see Component#getFont
	 * @since JDK1.0
	 */
	@Override
	public void setFont(Font f) {
		Font oldfont = getFont();
		super.setFont(f);
		Font newfont = getFont();
		if (newfont != oldfont && (oldfont == null || !oldfont.equals(newfont))) {
			invalidateTree();
		}
	}

    /**
     * Returns the preferred size of this container.  If the preferred size has
     * not been set explicitly by {@link Component#setPreferredSize(Dimension)}
     * and this {@code Container} has a {@code non-null} {@link LayoutManager},
     * then {@link LayoutManager#preferredLayoutSize(Container)}
     * is used to calculate the preferred size.
     *
     * <p>Note: some implementations may cache the value returned from the
     * {@code LayoutManager}.  Implementations that cache need not invoke
     * {@code preferredLayoutSize} on the {@code LayoutManager} every time
     * this method is invoked, rather the {@code LayoutManager} will only
     * be queried after the {@code Container} becomes invalid.
     *
     * @return    an instance of <code>Dimension</code> that represents
     *                the preferred size of this container.
     * @see       #getMinimumSize
     * @see       #getMaximumSize
     * @see       #getLayout
     * @see       LayoutManager#preferredLayoutSize(Container)
     * @see       Component#getPreferredSize
     */
    @Override
		public Dimension getPreferredSize() {
        return preferredSize();
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by <code>getPreferredSize()</code>.
     */
    @Override
		@Deprecated
    public Dimension preferredSize() {
    	return preferredSizeContainer();
    }

    protected Dimension preferredSizeContainer() {
        /* Avoid grabbing the lock if a reasonable cached size value
         * is available.
         */
        Dimension dim = prefSize;
        if (dim == null || !(isPreferredSizeSet() || isValid())) {
            synchronized (getTreeLock()) {
                prefSize = (layoutMgr != null ?
                    layoutMgr.preferredLayoutSize(this) :
                    prefSizeComp());
                dim = prefSize;
            }
        }
        return (dim == null ? null : new Dimension(dim));
	}

	/**
     * Returns the minimum size of this container.  If the minimum size has
     * not been set explicitly by {@link Component#setMinimumSize(Dimension)}
     * and this {@code Container} has a {@code non-null} {@link LayoutManager},
     * then {@link LayoutManager#minimumLayoutSize(Container)}
     * is used to calculate the minimum size.
     *
     * <p>Note: some implementations may cache the value returned from the
     * {@code LayoutManager}.  Implementations that cache need not invoke
     * {@code minimumLayoutSize} on the {@code LayoutManager} every time
     * this method is invoked, rather the {@code LayoutManager} will only
     * be queried after the {@code Container} becomes invalid.
     *
     * @return    an instance of <code>Dimension</code> that represents
     *                the minimum size of this container.
     * @see       #getPreferredSize
     * @see       #getMaximumSize
     * @see       #getLayout
     * @see       LayoutManager#minimumLayoutSize(Container)
     * @see       Component#getMinimumSize
     * @since     JDK1.1
     */
    @Override
		public Dimension getMinimumSize() {
        /* Avoid grabbing the lock if a reasonable cached size value
         * is available.
         */
        Dimension dim = minSize;
        if (dim == null || !(isMinimumSizeSet() || isValid())) {
            synchronized (getTreeLock()) {
                minSize = (layoutMgr != null) ?
                    layoutMgr.minimumLayoutSize(this) :
                    minimumSize();
                dim = minSize;
            }
        }
        if (dim != null){
            return new Dimension(dim);
        }
        else{
            return dim;
        }
    }

    /**
     * Returns the maximum size of this container.  If the maximum size has
     * not been set explicitly by {@link Component#setMaximumSize(Dimension)}
     * and the {@link LayoutManager} installed on this {@code Container}
     * is an instance of {@link LayoutManager2}, then
     * {@link LayoutManager2#maximumLayoutSize(Container)}
     * is used to calculate the maximum size.
     *
     * <p>Note: some implementations may cache the value returned from the
     * {@code LayoutManager2}.  Implementations that cache need not invoke
     * {@code maximumLayoutSize} on the {@code LayoutManager2} every time
     * this method is invoked, rather the {@code LayoutManager2} will only
     * be queried after the {@code Container} becomes invalid.
     *
     * @return    an instance of <code>Dimension</code> that represents
     *                the maximum size of this container.
     * @see       #getPreferredSize
     * @see       #getMinimumSize
     * @see       #getLayout
     * @see       LayoutManager2#maximumLayoutSize(Container)
     * @see       Component#getMaximumSize
     */
    @Override
		public Dimension getMaximumSize() {
        /* Avoid grabbing the lock if a reasonable cached size value
         * is available.
         */
        Dimension dim = maxSize;
        if (dim == null || !(isMaximumSizeSet() || isValid())) {
            synchronized (getTreeLock()) {
               if (layoutMgr instanceof LayoutManager2) {
                    LayoutManager2 lm = (LayoutManager2) layoutMgr;
                    maxSize = lm.maximumLayoutSize(this);
               } else {
                    maxSize = getMaxSizeComp();
               }
               dim = maxSize;
            }
        }
        if (dim != null){
            return new Dimension(dim);
        }
        else{
            return dim;
        }
    }

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     */
    @Override
		public float getAlignmentX() {
        float xAlign;
        if (layoutMgr instanceof LayoutManager2) {
            synchronized (getTreeLock()) {
                LayoutManager2 lm = (LayoutManager2) layoutMgr;
                xAlign = lm.getLayoutAlignmentX(this);
            }
        } else {
            xAlign = getAlignmentXComp();
        }
        return xAlign;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     */
    @Override
		public float getAlignmentY() {
        float yAlign;
        if (layoutMgr instanceof LayoutManager2) {
            synchronized (getTreeLock()) {
                LayoutManager2 lm = (LayoutManager2) layoutMgr;
                yAlign = lm.getLayoutAlignmentY(this);
            }
        } else {
            yAlign = getAlignmentYComp();
        }
        return yAlign;
    }

		/**
     * Paints the container. This forwards the paint to any lightweight
     * components that are children of this container. If this method is
     * reimplemented, super.paint(g) should be called so that lightweight
     * components are properly rendered. If a child component is entirely
     * clipped by the current clipping setting in g, paint() will not be
     * forwarded to that child.
     *
     * @param g the specified Graphics window
     * @see   Component#update(Graphics)
     * 
     * 
     */
    @Override
	public void paint(Graphics g) {
      paintContainer(g);
    }

    public void paintContainer(Graphics g) {
    	
    	// !NOT!  -- 9/15/18
    	// SwingJS: split off here so that 
    	// the new JComponent-subclassed Window can 
    	// hit this one directly instead of JComponent.paint()
    	
    
    	// SwingJS : The developer should override paint() to draw;
    	//this method will take care of all buttons, in case the
    	//paintComponent(g) method for them has been overridden.
//    	
//        if (isShowing()) {
////            synchronized (this) {
////                if (printing) {
////                    if (printingThreads.contains(Thread.currentThread())) {
////                        return;
////                    }
////                }
////            }
//
//            // The container is showing on screen and
//            // this paint() is not called from print().
//            // Paint self and forward the paint to lightweight subcomponents.
//
//            // super.paint(); -- Don't bother, since it's a NOP.
//
            GraphicsCallback.PaintCallback.getInstance().
                runComponents(getComponentCount(), getChildArray(), g, SunGraphicsCallback.LIGHTWEIGHTS);
//        }
	}

    /**
     * Updates the container.  This forwards the update to any lightweight
     * components that are children of this container.  If this method is
     * reimplemented, super.update(g) should be called so that lightweight
     * components are properly rendered.  If a child component is entirely
     * clipped by the current clipping setting in g, update() will not be
     * forwarded to that child.
     *
     * @param g the specified Graphics window
     * @see   Component#update(Graphics)
     */
    @Override
		public void update(Graphics g) {
    		updateContainer(g);
    }

//    /**
//     * Prints the container. This forwards the print to any lightweight
//     * components that are children of this container. If this method is
//     * reimplemented, super.print(g) should be called so that lightweight
//     * components are properly rendered. If a child component is entirely
//     * clipped by the current clipping setting in g, print() will not be
//     * forwarded to that child.
//     *
//     * @param g the specified Graphics window
//     * @see   Component#update(Graphics)
//     */
//    public void print(Graphics g) {
//        if (isShowing()) {
//            Thread t = Thread.currentThread();
//            try {
//                synchronized (this) {
//                    if (printingThreads == null) {
//                        printingThreads = new HashSet();
//                    }
//                    printingThreads.add(t);
//                    printing = true;
//                }
//                super.print(g);  // By default, Component.print() calls paint()
//            } finally {
//                synchronized (this) {
//                    printingThreads.remove(t);
//                    printing = !printingThreads.isEmpty();
//                }
//            }
//
//            GraphicsCallback.PrintCallback.getInstance().
//                runComponents(component.toArray(EMPTY_ARRAY), g, GraphicsCallback.LIGHTWEIGHTS);
//        }
//    }

    protected void updateContainer(Graphics g) {
		
        if (isShowing()) {
//            if (! (peer instanceof LightweightPeer)) {
                g.clearRect(0, 0, width, height);
//            }
            paint(g);
        }
	}

	/**
     * Paints each of the components in this container.
     * @param     g   the graphics context.
     * @see       Component#paint
     * @see       Component#paintAll
     */
    public void paintComponents(Graphics g) {
        if (isShowing()) {
            GraphicsCallback.PaintAllCallback.getInstance().
                runComponents(getComponentCount(), getChildArray(), g, SunGraphicsCallback.TWO_PASSES);
        }
    }

    /**
     * Simulates the peer callbacks into java.awt for printing of
     * lightweight Containers.
     * @param     g   the graphics context to use for printing.
     * @see       Component#printAll
     * @see       #printComponents
     */
    @Override
		void lightweightPaint(Graphics g) {
        lwPaintComp(g);
        paintHeavyweightComponents(g);
    }

    /**
     * Prints all the heavyweight subcomponents.
     */
    @Override
		void paintHeavyweightComponents(Graphics g) {
        if (isShowing()) {
            GraphicsCallback.PaintHeavyweightComponentsCallback.getInstance().
                runComponents(getComponentCount(), getChildArray(), g, SunGraphicsCallback.LIGHTWEIGHTS |
                                            SunGraphicsCallback.HEAVYWEIGHTS);
        }
    }

//    /**
//     * Prints each of the components in this container.
//     * @param     g   the graphics context.
//     * @see       Component#print
//     * @see       Component#printAll
//     */
//    public void printComponents(Graphics g) {
//        if (isShowing()) {
//            GraphicsCallback.PrintAllCallback.getInstance().
//                runComponents(component.toArray(EMPTY_ARRAY), g, GraphicsCallback.TWO_PASSES);
//        }
//    }

//    /**
//     * Simulates the peer callbacks into java.awt for printing of
//     * lightweight Containers.
//     * @param     g   the graphics context to use for printing.
//     * @see       Component#printAll
//     * @see       #printComponents
//     */
//    void lightweightPrint(Graphics g) {
//        super.lightweightPrint(g);
//        printHeavyweightComponents(g);
//    }

//    /**
//     * Prints all the heavyweight subcomponents.
//     */
//    void printHeavyweightComponents(Graphics g) {
//        if (isShowing()) {
//            GraphicsCallback.PrintHeavyweightComponentsCallback.getInstance().
//                runComponents(component.toArray(EMPTY_ARRAY), g, GraphicsCallback.LIGHTWEIGHTS |
//                                            GraphicsCallback.HEAVYWEIGHTS);
//        }
//    }

    /**
     * Adds the specified container listener to receive container events
     * from this container.
     * If l is null, no exception is thrown and no action is performed.
     * <p>Refer to <a href="doc-files/AWTThreadIssues.html#ListenersThreads"
     * >AWT Threading Issues</a> for details on AWT's threading model.
     *
     * @param    l the container listener
     *
     * @see #removeContainerListener
     * @see #getContainerListeners
     */
    public synchronized void addContainerListener(ContainerListener l) {
        if (l == null) {
            return;
        }
        containerListener = AWTEventMulticaster.add(containerListener, l);
        newEventsOnly = true;
    }

    /**
     * Removes the specified container listener so it no longer receives
     * container events from this container.
     * If l is null, no exception is thrown and no action is performed.
     * <p>Refer to <a href="doc-files/AWTThreadIssues.html#ListenersThreads"
     * >AWT Threading Issues</a> for details on AWT's threading model.
     *
     * @param   l the container listener
     *
     * @see #addContainerListener
     * @see #getContainerListeners
     */
    public synchronized void removeContainerListener(ContainerListener l) {
        if (l == null) {
            return;
        }
        containerListener = AWTEventMulticaster.remove(containerListener, l);
    }

    /**
     * Returns an array of all the container listeners
     * registered on this container.
     *
     * @return all of this container's <code>ContainerListener</code>s
     *         or an empty array if no container
     *         listeners are currently registered
     *
     * @see #addContainerListener
     * @see #removeContainerListener
     * @since 1.4
     */
    public synchronized ContainerListener[] getContainerListeners() {
        return (ContainerListener[]) (getListeners(ContainerListener.class));
    }

    /**
     * Returns an array of all the objects currently registered
     * as <code><em>Foo</em>Listener</code>s
     * upon this <code>Container</code>.
     * <code><em>Foo</em>Listener</code>s are registered using the
     * <code>add<em>Foo</em>Listener</code> method.
     *
     * <p>
     * You can specify the <code>listenerType</code> argument
     * with a class literal, such as
     * <code><em>Foo</em>Listener.class</code>.
     * For example, you can query a
     * <code>Container</code> <code>c</code>
     * for its container listeners with the following code:
     *
     * <pre>ContainerListener[] cls = (ContainerListener[])(c.getListeners(ContainerListener.class));</pre>
     *
     * If no such listeners exist, this method returns an empty array.
     *
     * @param listenerType the type of listeners requested; this parameter
     *          should specify an interface that descends from
     *          <code>java.util.EventListener</code>
     * @return an array of all objects registered as
     *          <code><em>Foo</em>Listener</code>s on this container,
     *          or an empty array if no such listeners have been added
     * @exception ClassCastException if <code>listenerType</code>
     *          doesn't specify a class or interface that implements
     *          <code>java.util.EventListener</code>
     *
     * @see #getContainerListeners
     *
     * @since 1.3
     */
    @Override
		public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        EventListener l = null;
        if  (listenerType == ContainerListener.class) {
            l = containerListener;
        } else {
            return getListenersComp(listenerType);
        }
        return AWTEventMulticaster.getListeners(l, listenerType);
    }

    // REMIND: remove when filtering is done at lower level
    @Override
	protected
		boolean eventEnabled(AWTEvent e) {
        int id = e.getID();

        if (id == ContainerEvent.COMPONENT_ADDED ||
            id == ContainerEvent.COMPONENT_REMOVED) {
            if ((eventMask & AWTEvent.CONTAINER_EVENT_MASK) != 0 ||
                containerListener != null) {
                return true;
            }
            return false;
        }
        return eventTypeEnabled(e.id);
    }

    /**
     * Processes events on this container. If the event is a
     * <code>ContainerEvent</code>, it invokes the
     * <code>processContainerEvent</code> method, else it invokes
     * its superclass's <code>processEvent</code>.
     * <p>Note that if the event parameter is <code>null</code>
     * the behavior is unspecified and may result in an
     * exception.
     *
     * @param e the event
     */
    @Override
		protected void processEvent(AWTEvent e) {
    	processEventCont(e);
    }

	protected void processEventCont(AWTEvent e) {
		if (e instanceof ContainerEvent) {
			processContainerEvent((ContainerEvent) e);
			return;
		}
		processEventComp(e);
	}

    /**
     * Processes container events occurring on this container by
     * dispatching them to any registered ContainerListener objects.
     * NOTE: This method will not be called unless container events
     * are enabled for this component; this happens when one of the
     * following occurs:
     * <ul>
     * <li>A ContainerListener object is registered via
     *     <code>addContainerListener</code>
     * <li>Container events are enabled via <code>enableEvents</code>
     * </ul>
     * <p>Note that if the event parameter is <code>null</code>
     * the behavior is unspecified and may result in an
     * exception.
     *
     * @param e the container event
     * @see Component#enableEvents
     */
    protected void processContainerEvent(ContainerEvent e) {
        ContainerListener listener = containerListener;
        if (listener != null) {
            switch(e.getID()) {
              case ContainerEvent.COMPONENT_ADDED:
                listener.componentAdded(e);
                break;
              case ContainerEvent.COMPONENT_REMOVED:
                listener.componentRemoved(e);
                break;
            }
        }
    }

    /*
     * Dispatches an event to this component or one of its sub components.
     * Create ANCESTOR_RESIZED and ANCESTOR_MOVED events in response to
     * COMPONENT_RESIZED and COMPONENT_MOVED events. We have to do this
     * here instead of in processComponentEvent because ComponentEvents
     * may not be enabled for this Container.
     * @param e the event
     */
    @Override
	protected
		void dispatchEventImpl(AWTEvent e) {
        if ((dispatcher != null) && dispatcher.dispatchEvent(e)) {
            // event was sent to a lightweight component.  The
            // native-produced event sent to the native container
            // must be properly disposed of by the peer, so it
            // gets forwarded.  If the native host has been removed
            // as a result of the sending the lightweight event,
            // the peer reference will be null.
// SwingJS why this? true return indicates consumed            e.consume();
// SwingJS next is unnecessary for JavaScript
//            if (peer != null) {
//                peer.handleEvent(e);
//            }
            return;
        }

        dispatchEventImplComp(e);

        synchronized (getTreeLock()) {
            switch (e.getID()) {
              case ComponentEvent.COMPONENT_RESIZED:
//                createChildHierarchyEvents(HierarchyEvent.ANCESTOR_RESIZED, 0,
//                                           Toolkit.enabledOnToolkit(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK));
                break;
              case ComponentEvent.COMPONENT_MOVED:
//                createChildHierarchyEvents(HierarchyEvent.ANCESTOR_MOVED, 0,
//                                       Toolkit.enabledOnToolkit(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK));
                break;
              default:
                break;
            }
        }
    }

//  /**
//  * Fetches the top-most (deepest) component to receive SunDropTargetEvents.
//  */
// Component getDropTargetEventTarget(int x, int y, boolean includeSelf) {
//     return getMouseEventTarget(x, y, includeSelf,
//                                DropTargetEventTargetFilter.FILTER,
//                                SEARCH_HEAVYWEIGHTS);
// }


    /*
     * Dispatches an event to this component, without trying to forward
     * it to any subcomponents
     * @param e the event
     */
    void dispatchEventToSelf(AWTEvent e) {
        dispatchEventImplComp(e);
    }

    /**
     * Fetchs the top-most (deepest) lightweight component that is interested
     * in receiving mouse events.
     * 
     */
    Component getMouseEventTarget(int x, int y, boolean includeSelf) {
        return getMouseEventTarget(x, y, includeSelf,
                                   MouseEventTargetFilter.FILTER,
                                   !SEARCH_HEAVYWEIGHTS);
    }

    /**
     * A private version of getMouseEventTarget which has two additional
     * controllable behaviors. This method searches for the top-most
     * descendant of this container that contains the given coordinates
     * and is accepted by the given filter. The search will be constrained to
     * lightweight descendants if the last argument is <code>false</code>.
     *
     * @param filter EventTargetFilter instance to determine whether the
     *        given component is a valid target for this event.
     * @param searchHeavyweights if <code>false</code>, the method
     *        will bypass heavyweight components during the search.
     */
    public Component getMouseEventTarget(int x, int y, boolean includeSelf,
                                          EventTargetFilter filter,
                                          boolean searchHeavyweights) {
    	// was default package-specific
        Component comp = null;
//        if (searchHeavyweights) {
//            comp = getMouseEventTargetImpl(x, y, includeSelf, filter,
//                                           SEARCH_HEAVYWEIGHTS,
//                                           searchHeavyweights);
//        }
//
        if (comp == null || comp == this) {
            comp = getMouseEventTargetImpl(x, y, includeSelf, filter,
                                           !SEARCH_HEAVYWEIGHTS,
                                           searchHeavyweights);
        }

        return comp;
    }

    /**
     * A private version of getMouseEventTarget which has three additional
     * controllable behaviors. This method searches for the top-most
     * descendant of this container that contains the given coordinates
     * and is accepted by the given filter. The search will be constrained to
     * descendants of only lightweight children or only heavyweight children
     * of this container depending on searchHeavyweightChildren. The search will
     * be constrained to only lightweight descendants of the searched children
     * of this container if searchHeavyweightDescendants is <code>false</code>.
     *
     * @param filter EventTargetFilter instance to determine whether the
     *        selected component is a valid target for this event.
     * @param searchHeavyweightChildren if <code>true</code>, the method
     *        will bypass immediate lightweight children during the search.
     *        If <code>false</code>, the methods will bypass immediate
     *        heavyweight children during the search.
     * @param searchHeavyweightDescendants if <code>false</code>, the method
     *        will bypass heavyweight descendants which are not immediate
     *        children during the search. If <code>true</code>, the method
     *        will traverse both lightweight and heavyweight descendants during
     *        the search.
     */
    private Component getMouseEventTargetImpl(int x, int y, boolean includeSelf,
                                         EventTargetFilter filter,
                                         boolean searchHeavyweightChildren,
                                         boolean searchHeavyweightDescendants) {
        synchronized (getTreeLock()) {

            for (int i = 0; i < component.size(); i++) {
                Component comp = component.get(i);
                
                // comp != null && comp.visible && searchNeavyweightChildren != (comp.peer instanceof LightweightPeer)
                //  &&  comp.contains(x - comp.x, y - comp.y)
                
                if (comp != null && comp.visible &&
                    ((!searchHeavyweightChildren &&
                      comp.peer instanceof LightweightPeer) ||
                     (searchHeavyweightChildren &&
                      !(comp.peer instanceof LightweightPeer))) &&
                    comp.contains(x - comp.x, y - comp.y)) {

                    // found a component that intersects the point, see if there
                    // is a deeper possibility.
                    if (comp instanceof Container) {
                        Container child = (Container) comp;
                        Component deeper = child.getMouseEventTarget(
                                x - child.x,
                                y - child.y,
                                includeSelf,
                                filter,
                                searchHeavyweightDescendants);
                        if (deeper != null) {
                            return deeper;
                        }
                    } else {
                    	// SwingJS adding filter == null for AWT events for A2SListener
                        if (filter == null || filter.accept(comp)) {
                            // there isn't a deeper target, but this component
                            // is a target
                            return comp;
                        }
                    }
                }
            }

            boolean isPeerOK;
            boolean isMouseOverMe;

            isPeerOK = 
            		
            		//(peer instanceof LightweightPeer) || 
            		
            		includeSelf;
            isMouseOverMe = contains(x,y);

            // didn't find a child target, return this component if it's
            // a possible target
            if (isMouseOverMe && isPeerOK && (filter == null || filter.accept(this))) {
                return this;
            }
            // no possible target
            return null;
        }
    }

    static interface EventTargetFilter {
        boolean accept(final Component comp);
    }

    static class MouseEventTargetFilter implements EventTargetFilter {
        static final EventTargetFilter FILTER = new MouseEventTargetFilter();

        private MouseEventTargetFilter() {}

        @Override
				public boolean accept(final Component comp) {
            return (comp.eventMask & AWTEvent.MOUSE_MOTION_EVENT_MASK) != 0
                || (comp.eventMask & AWTEvent.MOUSE_EVENT_MASK) != 0
                || (comp.eventMask & AWTEvent.MOUSE_WHEEL_EVENT_MASK) != 0
                || comp.mouseListener != null
                || comp.mouseMotionListener != null
                || comp.mouseWheelListener != null;
        }
    }

//    static class DropTargetEventTargetFilter implements EventTargetFilter {
//        static final EventTargetFilter FILTER = new DropTargetEventTargetFilter();
//
//        private DropTargetEventTargetFilter() {}
//
//        public boolean accept(final Component comp) {
////            DropTarget dt = comp.getDropTarget();
////            return dt != null && dt.isActive();
//        }
//    }
//
    /**
     * This is called by lightweight components that want the containing
     * windowed parent to enable some kind of events on their behalf.
     * This is needed for events that are normally only dispatched to
     * windows to be accepted so that they can be forwarded downward to
     * the lightweight component that has enabled them.
     */
    void proxyEnableEvents(long events) {
//        if (peer instanceof LightweightPeer) {
            // this container is lightweight.... continue sending it
            // upward.
            if (parent != null) {
                parent.proxyEnableEvents(events);
            }
//        } else {
//            // This is a native container, so it needs to host
//            // one of it's children.  If this function is called before
//            // a peer has been created we don't yet have a dispatcher
//            // because it has not yet been determined if this instance
//            // is lightweight.
            if (dispatcher != null) {
                dispatcher.enableEvents(events);
            }
//        }
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by <code>dispatchEvent(AWTEvent e)</code>
     */
    @Override
		@Deprecated
    public void deliverEvent(Event e) {
        Component comp = getComponentAt(e.x, e.y);
        if ((comp != null) && (comp != this)) {
            e.translate(-comp.x, -comp.y);
            comp.deliverEvent(e);
        } else {
            postEvent(e);
        }
    }

    /**
     * Locates the component that contains the x,y position.  The
     * top-most child component is returned in the case where there
     * is overlap in the components.  This is determined by finding
     * the component closest to the index 0 that claims to contain
     * the given point via Component.contains(), except that Components
     * which have native peers take precedence over those which do not
     * (i.e., lightweight Components).
     *
     * @param x the <i>x</i> coordinate
     * @param y the <i>y</i> coordinate
     * @return null if the component does not contain the position.
     * If there is no child component at the requested point and the
     * point is within the bounds of the container the container itself
     * is returned; otherwise the top-most child is returned.
     * @see Component#contains
     * @since JDK1.1
     */
    @Override
		public Component getComponentAt(int x, int y) {
        return locate(x, y);
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by <code>getComponentAt(int, int)</code>.
     */
    @Override
		@Deprecated
    public Component locate(int x, int y) {
        if (!contains(x, y)) {
            return null;
        }
        synchronized (getTreeLock()) {
            // Two passes: see comment in sun.awt.SunGraphicsCallback
//SwingJS not relevant            for (int i = 0; i < component.size(); i++) {
//                Component comp = component.get(i);
//                if (comp != null &&
//                    !(comp.peer instanceof LightweightPeer)) {
//                    if (comp.contains(x - comp.x, y - comp.y)) {
//                        return comp;
//                    }
//                }
//            }
            for (int i = 0; i < component.size(); i++) {
                Component comp = component.get(i);
                if (comp != null &&
                    comp.peer instanceof LightweightPeer) {
                    if (comp.contains(x - comp.x, y - comp.y)) {
                        return comp;
                    }
                }
            }
        }
        return this;
    }

    /**
     * Gets the component that contains the specified point.
     * @param      p   the point.
     * @return     returns the component that contains the point,
     *                 or <code>null</code> if the component does
     *                 not contain the point.
     * @see        Component#contains
     * @since      JDK1.1
     */
    @Override
		public Component getComponentAt(Point p) {
        return getComponentAt(p.x, p.y);
    }

    /**
     * Returns the position of the mouse pointer in this <code>Container</code>'s
     * coordinate space if the <code>Container</code> is under the mouse pointer,
     * otherwise returns <code>null</code>.
     * This method is similar to {@link Component#getMousePosition()} with the exception
     * that it can take the <code>Container</code>'s children into account.
     * If <code>allowChildren</code> is <code>false</code>, this method will return
     * a non-null value only if the mouse pointer is above the <code>Container</code>
     * directly, not above the part obscured by children.
     * If <code>allowChildren</code> is <code>true</code>, this method returns
     * a non-null value if the mouse pointer is above <code>Container</code> or any
     * of its descendants.
     *
     * @exception HeadlessException if GraphicsEnvironment.isHeadless() returns true
     * @param     allowChildren true if children should be taken into account
     * @see       Component#getMousePosition
     * @return    mouse coordinates relative to this <code>Component</code>, or null
     * @since     1.5
     */
    public Point getMousePosition(boolean allowChildren) {
    	// TODO
//        PointerInfo pi = MouseInfo.getPointerInfo();
//        synchronized (getTreeLock()) {
//            Component inTheSameWindow = findUnderMouseInWindow(pi);
//            if (isSameOrAncestorOf(inTheSameWindow, allowChildren)) {
//                return  pointRelativeToComponent(pi.getLocation());
//            }
//            return null;
//        }
        return null;
    }

    @Override
		boolean isSameOrAncestorOf(Component comp, boolean allowChildren) {
        return this == comp || (allowChildren && isParentOf(comp));
    }

    /**
     * Locates the visible child component that contains the specified
     * position.  The top-most child component is returned in the case
     * where there is overlap in the components.  If the containing child
     * component is a Container, this method will continue searching for
     * the deepest nested child component.  Components which are not
     * visible are ignored during the search.<p>
     *
     * The findComponentAt method is different from getComponentAt in
     * that getComponentAt only searches the Container's immediate
     * children; if the containing component is a Container,
     * findComponentAt will search that child to find a nested component.
     *
     * @param x the <i>x</i> coordinate
     * @param y the <i>y</i> coordinate
     * @return null if the component does not contain the position.
     * If there is no child component at the requested point and the
     * point is within the bounds of the container the container itself
     * is returned.
     * @see Component#contains
     * @see #getComponentAt
     * @since 1.2
     */
    public Component findComponentAt(int x, int y) {
        synchronized (getTreeLock()) {
            return findComponentAt(x, y, true);
        }
    }

    /**
     * Private version of findComponentAt which has a controllable
     * behavior. Setting 'ignoreEnabled' to 'false' bypasses disabled
     * Components during the search. This behavior is used by the
     * lightweight cursor support in sun.awt.GlobalCursorManager.
     * The cursor code calls this function directly via native code.
     *
     * The addition of this feature is temporary, pending the
     * adoption of new, public API which exports this feature.
     */
    final Component findComponentAt(int x, int y, boolean ignoreEnabled)
    {
//        if (isRecursivelyVisible()){
//            return findComponentAtImpl(x, y, ignoreEnabled);
//        }
        return null;
    }

//    final Component findComponentAtImpl(int x, int y, boolean ignoreEnabled){
//        if (!(contains(x, y) && visible && (ignoreEnabled || enabled))) {
//            return null;
//        }
//
//        // Two passes: see comment in sun.awt.SunGraphicsCallback
//        synchronized (getTreeLock()) {
//            for (int i = 0; i < component.size(); i++) {
//                Component comp = component.get(i);
//                if (comp != null &&
//                    !(comp.peer instanceof LightweightPeer)) {
//                    if (comp instanceof Container) {
//                        comp = ((Container)comp).findComponentAtImpl(x - comp.x,
//                                                                     y - comp.y,
//                                                                     ignoreEnabled);
//                    } else {
//                        comp = comp.locate(x - comp.x, y - comp.y);
//                    }
//                    if (comp != null && comp.visible &&
//                        (ignoreEnabled || comp.enabled))
//                        {
//                            return comp;
//                        }
//                }
//            }
//            for (int i = 0; i < component.size(); i++) {
//                Component comp = component.get(i);
//                if (comp != null &&
//                    comp.peer instanceof LightweightPeer) {
//                    if (comp instanceof Container) {
//                        comp = ((Container)comp).findComponentAtImpl(x - comp.x,
//                                                                     y - comp.y,
//                                                                     ignoreEnabled);
//                    } else {
//                        comp = comp.locate(x - comp.x, y - comp.y);
//                    }
//                    if (comp != null && comp.visible &&
//                        (ignoreEnabled || comp.enabled))
//                        {
//                            return comp;
//                        }
//                }
//            }
//        }
//        return this;
//    }

    /**
     * Locates the visible child component that contains the specified
     * point.  The top-most child component is returned in the case
     * where there is overlap in the components.  If the containing child
     * component is a Container, this method will continue searching for
     * the deepest nested child component.  Components which are not
     * visible are ignored during the search.<p>
     *
     * The findComponentAt method is different from getComponentAt in
     * that getComponentAt only searches the Container's immediate
     * children; if the containing component is a Container,
     * findComponentAt will search that child to find a nested component.
     *
     * @param      p   the point.
     * @return null if the component does not contain the position.
     * If there is no child component at the requested point and the
     * point is within the bounds of the container the container itself
     * is returned.
     * @see Component#contains
     * @see #getComponentAt
     * @since 1.2
     */
    public Component findComponentAt(Point p) {
        return findComponentAt(p.x, p.y);
    }

    /**
     * Makes this Container displayable by connecting it to
     * a native screen resource.  Making a container displayable will
     * cause all of its children to be made displayable.
     * This method is called internally by the toolkit and should
     * not be called directly by programs.
     * @see Component#isDisplayable
     * @see #removeNotify
     */
    @Override
		public void addNotify() {
//        synchronized (getTreeLock()) {
            // addNotify() on the children may cause proxy event enabling
            // on this instance, so we first call addNotifyComp() and
            // possibly create an lightweight event dispatcher before calling
            // addNotify() on the children which may be lightweight.
            super.addNotify();
            if (! (peer instanceof LightweightPeer)) {
            	setDispatcher();
            }

            // We shouldn't use iterator because of the Swing menu
            // implementation specifics:
            // the menu is being assigned as a child to JLayeredPane
            // instead of particular component so always affect
            // collection of component if menu is becoming shown or hidden.
            for (int i = 0; i < component.size(); i++) {
                component.get(i).addNotify();
            }
// SwingJS             // Update stacking order if native platform allows
//            ContainerPeer cpeer = (ContainerPeer)peer;
//            if (cpeer.isRestackSupported()) {
//                cpeer.restack();
//            }


//        }
    }

    /**
     * SwingJS set by JSAppletViewer
     */
    public void setDispatcher() {
    	if (dispatcher != null)
    		return;
      dispatcher = new LightweightDispatcher(this);    	
    }
    /**
     * Makes this Container undisplayable by removing its connection
     * to its native screen resource.  Making a container undisplayable
     * will cause all of its children to be made undisplayable.
     * This method is called by the toolkit internally and should
     * not be called directly by programs.
     * @see Component#isDisplayable
     * @see #addNotify
     */
    @Override
		public void removeNotify() {
//        synchronized (getTreeLock()) {
            // We shouldn't use iterator because of the Swing menu
            // implementation specifics:
            // the menu is being assigned as a child to JLayeredPane
            // instead of particular component so always affect
            // collection of component if menu is becoming shown or hidden.
            for (int i = component.size(); --i >= 0;) {
                Component comp = component.get(i);
                if (comp != null) {
                    // Fix for 6607170.
                    // We want to suppress focus change on disposal
                    // of the focused component. But because of focus
                    // is asynchronous, we should suppress focus change
                    // on every component in case it receives native focus
                    // in the process of disposal.
                    comp.setAutoFocusTransferOnDisposal(false);
                    comp.removeNotify();
                    comp.setAutoFocusTransferOnDisposal(true);
                 }
             }
            // If some of the children had focus before disposal then it still has.
            // Auto-transfer focus to the next (or previous) component if auto-transfer
            // is enabled.
            if (containsFocus() && KeyboardFocusManager.isAutoFocusTransferEnabledFor(this)) {
                if (!transferFocus(false)) {
                    transferFocusBackward(true);
                }
            }
            if ( dispatcher != null ) {
                dispatcher.dispose();
                dispatcher = null;
            }
            removeNotifyComp();
//        }
    }

    /**
     * Checks if the component is contained in the component hierarchy of
     * this container.
     * @param c the component
     * @return     <code>true</code> if it is an ancestor;
     *             <code>false</code> otherwise.
     * @since      JDK1.1
     */
    public boolean isAncestorOf(Component c) {
        Container p;
        if (c == null || ((p = c.getParent()) == null)) {
            return false;
        }
        while (p != null) {
            if (p == this) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }

    /*
     * The following code was added to support modal JInternalFrames
     * Unfortunately this code has to be added here so that we can get access to
     * some private AWT classes like SequencedEvent.
     *
     * The native container of the LW component has this field set
     * to tell it that it should block Mouse events for all LW
     * children except for the modal component.
     *
     * In the case of nested Modal components, we store the previous
     * modal component in the new modal components value of modalComp;
     */

    transient Component modalComp;
    transient AppContext modalAppContext;

//    private void startLWModal() {
//        // Store the app context on which this component is being shown.
//        // Event dispatch thread of this app context will be sleeping until
//        // we wake it by any event from hideAndDisposeHandler().
//        modalAppContext = AppContext.getAppContext();
//
//        // keep the KeyEvents from being dispatched
//        // until the focus has been transfered
//        long time = Toolkit.getEventQueue().getMostRecentEventTime();
////        Component predictedFocusOwner = (Component.isInstanceOf(this, "javax.swing.JInternalFrame")) ? ((javax.swing.JInternalFrame)(this)).getMostRecentFocusOwner() : null;
////        if (predictedFocusOwner != null) {
////            KeyboardFocusManager.getCurrentKeyboardFocusManager().
////                enqueueKeyEvents(time, predictedFocusOwner);
////        }
//        // We have two mechanisms for blocking: 1. If we're on the
//        // EventDispatchThread, start a new event pump. 2. If we're
//        // on any other thread, call wait() on the treelock.
//        final Container nativeContainer;
//        synchronized (getTreeLock()) {
//            nativeContainer = getHeavyweightContainer();
//            if (nativeContainer.modalComp != null) {
//                this.modalComp =  nativeContainer.modalComp;
//                nativeContainer.modalComp = this;
//                return;
//            }
//            else {
//                nativeContainer.modalComp = this;
//            }
//        }
//
//        Runnable pumpEventsForHierarchy = new Runnable() {
//            public void run() {
//                EventDispatchThread dispatchThread =
//                    (EventDispatchThread)Thread.currentThread();
//                dispatchThread.pumpEventsForHierarchy(
//                        new Conditional() {
//                        public boolean evaluate() {
//                        return ((windowClosingException == null) && (nativeContainer.modalComp != null)) ;
//                        }
//                        }, Container.this);
//            }
//        };
//
//        if (EventQueue.isDispatchThread()) {
//            SequencedEvent currentSequencedEvent =
//                KeyboardFocusManager.getCurrentKeyboardFocusManager().
//                getCurrentSequencedEvent();
//            if (currentSequencedEvent != null) {
//                currentSequencedEvent.dispose();
//            }
//
//            pumpEventsForHierarchy.run();
//        } else {
//            synchronized (getTreeLock()) {
//                Toolkit.getEventQueue().
//                    postEvent(new PeerEvent(this,
//                                pumpEventsForHierarchy,
//                                PeerEvent.PRIORITY_EVENT));
//                while ((windowClosingException == null) &&
//                       (nativeContainer.modalComp != null))
//                {
//                    try {
//                        getTreeLock().wait();
//                    } catch (InterruptedException e) {
//                        break;
//                    }
//                }
//            }
//        }
//        if (windowClosingException != null) {
//            windowClosingException.fillInStackTrace();
//            throw windowClosingException;
//        }
//        if (predictedFocusOwner != null) {
//            KeyboardFocusManager.getCurrentKeyboardFocusManager().
//                dequeueKeyEvents(time, predictedFocusOwner);
//        }
//    }
//
//    private void stopLWModal() {
//        synchronized (getTreeLock()) {
//            if (modalAppContext != null) {
//                Container nativeContainer = getHeavyweightContainer();
//                if(nativeContainer != null) {
//                    if (this.modalComp !=  null) {
//                        nativeContainer.modalComp = this.modalComp;
//                        this.modalComp = null;
//                        return;
//                    }
//                    else {
//                        nativeContainer.modalComp = null;
//                    }
//                }
//                // Wake up event dispatch thread on which the dialog was
//                // initially shown
//                SunToolkit.postEvent(modalAppContext,
//                        new PeerEvent(this,
//                                new WakingRunnable(),
//                                PeerEvent.PRIORITY_EVENT));
//            }
//            EventQueue.invokeLater(new WakingRunnable());
//            getTreeLock().notifyAll();
//        }
//    }
//
//    final static class WakingRunnable implements Runnable {
//        public void run() {
//        }
//    }

    /* End of JOptionPane support code */
//
    /**
     * Returns a string representing the state of this <code>Container</code>.
     * This method is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not be
     * <code>null</code>.
     *
     * @return    the parameter string of this container
     */
    @Override
		protected String paramString() {
        String str = paramStringComp();
        LayoutManager layoutMgr = this.layoutMgr;
        if (layoutMgr != null) {
            str += ",layout=" + layoutMgr.getClass().getName();
        }
        return str;
    }
    //
//    /**
//     * Prints a listing of this container to the specified output
//     * stream. The listing starts at the specified indentation.
//     * <p>
//     * The immediate children of the container are printed with
//     * an indentation of <code>indent+1</code>.  The children
//     * of those children are printed at <code>indent+2</code>
//     * and so on.
//     *
//     * @param    out      a print stream
//     * @param    indent   the number of spaces to indent
//     * @see      Component#list(java.io.PrintStream, int)
//     * @since    JDK1.0
//     */
//    public void list(PrintStream out, int indent) {
//        super.list(out, indent);
//        synchronized(getTreeLock()) {
//            for (int i = 0; i < component.size(); i++) {
//                Component comp = component.get(i);
//                if (comp != null) {
//                    comp.list(out, indent+1);
//                }
//            }
//        }
//    }
//
//    /**
//     * Prints out a list, starting at the specified indentation,
//     * to the specified print writer.
//     * <p>
//     * The immediate children of the container are printed with
//     * an indentation of <code>indent+1</code>.  The children
//     * of those children are printed at <code>indent+2</code>
//     * and so on.
//     *
//     * @param    out      a print writer
//     * @param    indent   the number of spaces to indent
//     * @see      Component#list(java.io.PrintWriter, int)
//     * @since    JDK1.1
//     */
//    public void list(PrintWriter out, int indent) {
//        super.list(out, indent);
//        synchronized(getTreeLock()) {
//            for (int i = 0; i < component.size(); i++) {
//                Component comp = component.get(i);
//                if (comp != null) {
//                    comp.list(out, indent+1);
//                }
//            }
//        }
//    }

    /**
     * Sets the focus traversal keys for a given traversal operation for this
     * Container.
     * <p>
     * The default values for a Container's focus traversal keys are
     * implementation-dependent. Sun recommends that all implementations for a
     * particular native platform use the same default values. The
     * recommendations for Windows and Unix are listed below. These
     * recommendations are used in the Sun AWT implementations.
     *
     * <table border=1 summary="Recommended default values for a Container's focus traversal keys">
     * <tr>
     *    <th>Identifier</th>
     *    <th>Meaning</th>
     *    <th>Default</th>
     * </tr>
     * <tr>
     *    <td>KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS</td>
     *    <td>Normal forward keyboard traversal</td>
     *    <td>TAB on KEY_PRESSED, CTRL-TAB on KEY_PRESSED</td>
     * </tr>
     * <tr>
     *    <td>KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS</td>
     *    <td>Normal reverse keyboard traversal</td>
     *    <td>SHIFT-TAB on KEY_PRESSED, CTRL-SHIFT-TAB on KEY_PRESSED</td>
     * </tr>
     * <tr>
     *    <td>KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS</td>
     *    <td>Go up one focus traversal cycle</td>
     *    <td>none</td>
     * </tr>
     * <tr>
     *    <td>KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS<td>
     *    <td>Go down one focus traversal cycle</td>
     *    <td>none</td>
     * </tr>
     * </table>
     *
     * To disable a traversal key, use an empty Set; Collections.EMPTY_SET is
     * recommended.
     * <p>
     * Using the AWTKeyStroke API, client code can specify on which of two
     * specific KeyEvents, KEY_PRESSED or KEY_RELEASED, the focus traversal
     * operation will occur. Regardless of which KeyEvent is specified,
     * however, all KeyEvents related to the focus traversal key, including the
     * associated KEY_TYPED event, will be consumed, and will not be dispatched
     * to any Container. It is a runtime error to specify a KEY_TYPED event as
     * mapping to a focus traversal operation, or to map the same event to
     * multiple default focus traversal operations.
     * <p>
     * If a value of null is specified for the Set, this Container inherits the
     * Set from its parent. If all ancestors of this Container have null
     * specified for the Set, then the current KeyboardFocusManager's default
     * Set is used.
     *
     * @param id one of KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
     *        KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
     *        KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, or
     *        KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS
     * @param keystrokes the Set of AWTKeyStroke for the specified operation
     * @see #getFocusTraversalKeys
     * @see KeyboardFocusManager#FORWARD_TRAVERSAL_KEYS
     * @see KeyboardFocusManager#BACKWARD_TRAVERSAL_KEYS
     * @see KeyboardFocusManager#UP_CYCLE_TRAVERSAL_KEYS
     * @see KeyboardFocusManager#DOWN_CYCLE_TRAVERSAL_KEYS
     * @throws IllegalArgumentException if id is not one of
     *         KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
     *         KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
     *         KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, or
     *         KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS, or if keystrokes
     *         contains null, or if any Object in keystrokes is not an
     *         AWTKeyStroke, or if any keystroke represents a KEY_TYPED event,
     *         or if any keystroke already maps to another focus traversal
     *         operation for this Container
     * @since 1.4
     * @beaninfo
     *       bound: true
     */
    @Override
	public void setFocusTraversalKeys(int id,
                                      Set<? extends AWTKeyStroke> keystrokes)
    {
        if (id < 0 || id >= KeyboardFocusManager.TRAVERSAL_KEY_LENGTH) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }

        // Don't call super.setFocusTraversalKey. The Component parameter check
        // does not allow DOWN_CYCLE_TRAVERSAL_KEYS, but we do.
        setFocusTraversalKeys_NoIDCheck(id, keystrokes);
    }

    /**
     * Returns the Set of focus traversal keys for a given traversal operation
     * for this Container. (See
     * <code>setFocusTraversalKeys</code> for a full description of each key.)
     * <p>
     * If a Set of traversal keys has not been explicitly defined for this
     * Container, then this Container's parent's Set is returned. If no Set
     * has been explicitly defined for any of this Container's ancestors, then
     * the current KeyboardFocusManager's default Set is returned.
     *
     * @param id one of KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
     *        KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
     *        KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, or
     *        KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS
     * @return the Set of AWTKeyStrokes for the specified operation. The Set
     *         will be unmodifiable, and may be empty. null will never be
     *         returned.
     * @see #setFocusTraversalKeys
     * @see KeyboardFocusManager#FORWARD_TRAVERSAL_KEYS
     * @see KeyboardFocusManager#BACKWARD_TRAVERSAL_KEYS
     * @see KeyboardFocusManager#UP_CYCLE_TRAVERSAL_KEYS
     * @see KeyboardFocusManager#DOWN_CYCLE_TRAVERSAL_KEYS
     * @throws IllegalArgumentException if id is not one of
     *         KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
     *         KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
     *         KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, or
     *         KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS
     * @since 1.4
     */
    @Override
	public Set<AWTKeyStroke> getFocusTraversalKeys(int id) {
        if (id < 0 || id >= KeyboardFocusManager.TRAVERSAL_KEY_LENGTH) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }

        // Don't call super.getFocusTraversalKey. The Component parameter check
        // does not allow DOWN_CYCLE_TRAVERSAL_KEY, but we do.
        return getFocusTraversalKeys_NoIDCheck(id);
//    	return null;
    }

    /**
     * Returns whether the Set of focus traversal keys for the given focus
     * traversal operation has been explicitly defined for this Container. If
     * this method returns <code>false</code>, this Container is inheriting the
     * Set from an ancestor, or from the current KeyboardFocusManager.
     *
     * @param id one of KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
     *        KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
     *        KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, or
     *        KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS
     * @return <code>true</code> if the the Set of focus traversal keys for the
     *         given focus traversal operation has been explicitly defined for
     *         this Component; <code>false</code> otherwise.
     * @throws IllegalArgumentException if id is not one of
     *         KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
     *        KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
     *        KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, or
     *        KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS
     * @since 1.4
     */
    @Override
	public boolean areFocusTraversalKeysSet(int id) {
        if (id < 0 || id >= KeyboardFocusManager.TRAVERSAL_KEY_LENGTH) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }

        return (focusTraversalKeys != null && focusTraversalKeys[id] != null);
//    	return false;
    }

    /**
     * Returns whether the specified Container is the focus cycle root of this
     * Container's focus traversal cycle. Each focus traversal cycle has only
     * a single focus cycle root and each Container which is not a focus cycle
     * root belongs to only a single focus traversal cycle. Containers which
     * are focus cycle roots belong to two cycles: one rooted at the Container
     * itself, and one rooted at the Container's nearest focus-cycle-root
     * ancestor. This method will return <code>true</code> for both such
     * Containers in this case.
     *
     * @param container the Container to be tested
     * @return <code>true</code> if the specified Container is a focus-cycle-
     *         root of this Container; <code>false</code> otherwise
     * @see #isFocusCycleRoot()
     * @since 1.4
     */
    @Override
		public boolean isFocusCycleRoot(Container container) {
        if (isFocusCycleRoot() && container == this) {
            return true;
        } else {
            return isFocusCycleRootComp(container);
        }
    }

    private Container findTraversalRoot() {
        // I potentially have two roots, myself and my root parent
        // If I am the current root, then use me
        // If none of my parents are roots, then use me
        // If my root parent is the current root, then use my root parent
        // If neither I nor my root parent is the current root, then
        // use my root parent (a guess)

        Container currentFocusCycleRoot = KeyboardFocusManager.
            getCurrentKeyboardFocusManager().getCurrentFocusCycleRoot();
        Container root;

        if (currentFocusCycleRoot == this) {
            root = this;
        } else {
            root = getFocusCycleRootAncestor();
            if (root == null) {
                root = this;
            }
        }

        if (root != currentFocusCycleRoot) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().
                setGlobalCurrentFocusCycleRoot(root);
        }
        return root;
    }

    @Override
		final boolean containsFocus() {
        final Component focusOwner = KeyboardFocusManager.
            getCurrentKeyboardFocusManager().getFocusOwner();
        return isParentOf(focusOwner);
//    	return false;
    }

    /**
     * Check if this component is the child of this container or its children.
     * Note: this function acquires treeLock
     * Note: this function traverses children tree only in one Window.
     * @param comp a component in test, must not be null
     */
    private boolean isParentOf(Component comp) {
//        synchronized(getTreeLock()) {
            while (comp != null && comp != this && !comp.isWindowOrJSApplet()) {
                comp = comp.getParent();
            }
            return (comp == this);
//        }
    }

    @Override
	void clearMostRecentFocusOwnerOnHide() {
        boolean reset = false;
        Window window = null;

        synchronized (getTreeLock()) {
            window = getContainingWindow();
            if (window != null) {
                Component comp = KeyboardFocusManager.getMostRecentFocusOwner(window);
                reset = ((comp == this) || isParentOf(comp));
                // This synchronized should always be the second in a pair
                // (tree lock, KeyboardFocusManager.class)
                synchronized(KeyboardFocusManager.class) {
                    Component storedComp = window.getTemporaryLostComponent();
                    if (isParentOf(storedComp) || storedComp == this) {
                        window.setTemporaryLostComponent(null);
                    }
                }
            }
        }

        if (reset) {
            KeyboardFocusManager.setMostRecentFocusOwner(window, null);
        }
    }

    @Override
    protected void clearCurrentFocusCycleRootOnHide() {
        KeyboardFocusManager kfm =
            KeyboardFocusManager.getCurrentKeyboardFocusManager();
        Container cont = kfm.getCurrentFocusCycleRoot();

        if (cont == this || isParentOf(cont)) {
            kfm.setGlobalCurrentFocusCycleRoot(null);
        }
    }

    @Override
	final Container getTraversalRoot() {
        if (isFocusCycleRoot()) {
            return findTraversalRoot();
        }

        return super.getTraversalRoot();
    }

    /**
     * Sets the focus traversal policy that will manage keyboard traversal of
     * this Container's children, if this Container is a focus cycle root. If
     * the argument is null, this Container inherits its policy from its focus-
     * cycle-root ancestor. If the argument is non-null, this policy will be
     * inherited by all focus-cycle-root children that have no keyboard-
     * traversal policy of their own (as will, recursively, their focus-cycle-
     * root children).
     * <p>
     * If this Container is not a focus cycle root, the policy will be
     * remembered, but will not be used or inherited by this or any other
     * Containers until this Container is made a focus cycle root.
     *
     * @param policy the new focus traversal policy for this Container
     * @see #getFocusTraversalPolicy
     * @see #setFocusCycleRoot
     * @see #isFocusCycleRoot
     * @since 1.4
     * @beaninfo
     *       bound: true
     */
    public void setFocusTraversalPolicy(FocusTraversalPolicy policy) {
        FocusTraversalPolicy oldPolicy;
        synchronized (this) {
            oldPolicy = this.focusTraversalPolicy;
            this.focusTraversalPolicy = policy;
        }
        firePropertyChange("focusTraversalPolicy", oldPolicy, policy);
    }

    /**
     * Returns the focus traversal policy that will manage keyboard traversal
     * of this Container's children, or null if this Container is not a focus
     * cycle root. If no traversal policy has been explicitly set for this
     * Container, then this Container's focus-cycle-root ancestor's policy is
     * returned.
     *
     * @return this Container's focus traversal policy, or null if this
     *         Container is not a focus cycle root.
     * @see #setFocusTraversalPolicy
     * @see #setFocusCycleRoot
     * @see #isFocusCycleRoot
     * @since 1.4
     */
    public FocusTraversalPolicy getFocusTraversalPolicy() {
        if (!isFocusTraversalPolicyProvider() && !isFocusCycleRoot()) {
            return null;
        }

        FocusTraversalPolicy policy = this.focusTraversalPolicy;
        if (policy != null) {
            return policy;
        }

        Container rootAncestor = getFocusCycleRootAncestor();
        if (rootAncestor != null) {
            return rootAncestor.getFocusTraversalPolicy();
        } else if (秘isAWT()) {
            return KeyboardFocusManager.getCurrentKeyboardFocusManager().
                    getDefaultAWTFocusTraversalPolicy();
        } else {
            return KeyboardFocusManager.getCurrentKeyboardFocusManager().
                getDefaultFocusTraversalPolicy();
        }
    }

    /**
     * Returns whether the focus traversal policy has been explicitly set for
     * this Container. If this method returns <code>false</code>, this
     * Container will inherit its focus traversal policy from an ancestor.
     *
     * @return <code>true</code> if the focus traversal policy has been
     *         explicitly set for this Container; <code>false</code> otherwise.
     * @since 1.4
     */
    public boolean isFocusTraversalPolicySet() {
        return (focusTraversalPolicy != null);
    }

    /**
     * Sets whether this Container is the root of a focus traversal cycle. Once
     * focus enters a traversal cycle, typically it cannot leave it via focus
     * traversal unless one of the up- or down-cycle keys is pressed. Normal
     * traversal is limited to this Container, and all of this Container's
     * descendants that are not descendants of inferior focus cycle roots. Note
     * that a FocusTraversalPolicy may bend these restrictions, however. For
     * example, ContainerOrderFocusTraversalPolicy supports implicit down-cycle
     * traversal.
     * <p>
     * The alternative way to specify the traversal order of this Container's
     * children is to make this Container a
     * <a href="doc-files/FocusSpec.html#FocusTraversalPolicyProviders">focus traversal policy provider</a>.
     *
     * @param focusCycleRoot indicates whether this Container is the root of a
     *        focus traversal cycle
     * @see #isFocusCycleRoot()
     * @see #setFocusTraversalPolicy
     * @see #getFocusTraversalPolicy
     * @see ContainerOrderFocusTraversalPolicy
     * @see #setFocusTraversalPolicyProvider
     * @since 1.4
     * @beaninfo
     *       bound: true
     */
    public void setFocusCycleRoot(boolean focusCycleRoot) {
        boolean oldFocusCycleRoot;
        synchronized (this) {
            oldFocusCycleRoot = this.focusCycleRoot;
            this.focusCycleRoot = focusCycleRoot;
        }
        firePropertyChange("focusCycleRoot", oldFocusCycleRoot,
                           focusCycleRoot);
    }

    /**
     * Returns whether this Container is the root of a focus traversal cycle.
     * Once focus enters a traversal cycle, typically it cannot leave it via
     * focus traversal unless one of the up- or down-cycle keys is pressed.
     * Normal traversal is limited to this Container, and all of this
     * Container's descendants that are not descendants of inferior focus
     * cycle roots. Note that a FocusTraversalPolicy may bend these
     * restrictions, however. For example, ContainerOrderFocusTraversalPolicy
     * supports implicit down-cycle traversal.
     *
     * @return whether this Container is the root of a focus traversal cycle
     * @see #setFocusCycleRoot
     * @see #setFocusTraversalPolicy
     * @see #getFocusTraversalPolicy
     * @see ContainerOrderFocusTraversalPolicy
     * @since 1.4
     */
    public boolean isFocusCycleRoot() {
        return focusCycleRoot;
    }

    /**
     * Sets whether this container will be used to provide focus
     * traversal policy. Container with this property as
     * <code>true</code> will be used to acquire focus traversal policy
     * instead of closest focus cycle root ancestor.
     * @param provider indicates whether this container will be used to
     *                provide focus traversal policy
     * @see #setFocusTraversalPolicy
     * @see #getFocusTraversalPolicy
     * @see #isFocusTraversalPolicyProvider
     * @since 1.5
     * @beaninfo
     *        bound: true
     */
    public final void setFocusTraversalPolicyProvider(boolean provider) {
        boolean oldProvider;
        synchronized(this) {
            oldProvider = focusTraversalPolicyProvider;
            focusTraversalPolicyProvider = provider;
        }
        firePropertyChange("focusTraversalPolicyProvider", oldProvider, provider);
    }

    /**
     * Returns whether this container provides focus traversal
     * policy. If this property is set to <code>true</code> then when
     * keyboard focus manager searches container hierarchy for focus
     * traversal policy and encounters this container before any other
     * container with this property as true or focus cycle roots then
     * its focus traversal policy will be used instead of focus cycle
     * root's policy.
     * @see #setFocusTraversalPolicy
     * @see #getFocusTraversalPolicy
     * @see #setFocusCycleRoot
     * @see #setFocusTraversalPolicyProvider
     * @return <code>true</code> if this container provides focus traversal
     *         policy, <code>false</code> otherwise
     * @since 1.5
     * @beaninfo
     *        bound: true
     */
    public final boolean isFocusTraversalPolicyProvider() {
        return focusTraversalPolicyProvider;
    }

    /**
     * Transfers the focus down one focus traversal cycle. If this Container is
     * a focus cycle root, then the focus owner is set to this Container's
     * default Component to focus, and the current focus cycle root is set to
     * this Container. If this Container is not a focus cycle root, then no
     * focus traversal operation occurs.
     *
     * @see       Component#requestFocus()
     * @see       #isFocusCycleRoot
     * @see       #setFocusCycleRoot
     * @since     1.4
     */
    public void transferFocusDownCycle() {
//        if (isFocusCycleRoot()) {
//            KeyboardFocusManager.getCurrentKeyboardFocusManager().
//                setGlobalCurrentFocusCycleRoot(this);
//            Component toFocus = getFocusTraversalPolicy().
//                getDefaultComponent(this);
//            if (toFocus != null) {
//                toFocus.requestFocus(CausedFocusEvent.Cause.TRAVERSAL_DOWN);
//            }
//        }
    }

    void preProcessKeyEvent(KeyEvent e) {
        Container parent = this.parent;
        if (parent != null) {
            parent.preProcessKeyEvent(e);
        }
    }

    void postProcessKeyEvent(KeyEvent e) {
        Container parent = this.parent;
        if (parent != null) {
            parent.postProcessKeyEvent(e);
        }
    }

    @Override
		boolean postsOldMouseEvents() {
        return true;
    }

    /**
     * Sets the <code>ComponentOrientation</code> property of this container
     * and all components contained within it.
     *
     * @param o the new component orientation of this container and
     *        the components contained within it.
     * @exception NullPointerException if <code>orientation</code> is null.
     * @see Component#setComponentOrientation
     * @see Component#getComponentOrientation
     * @since 1.4
     */
    @Override
		public void applyComponentOrientation(ComponentOrientation o) {
        applyCompOrientComp(o);
        synchronized (getTreeLock()) {
            for (int i = 0; i < component.size(); i++) {
                Component comp = component.get(i);
                comp.applyComponentOrientation(o);
            }
        }
    }

    /**
     * Adds a PropertyChangeListener to the listener list. The listener is
     * registered for all bound properties of this class, including the
     * following:
     * <ul>
     *    <li>this Container's font ("font")</li>
     *    <li>this Container's background color ("background")</li>
     *    <li>this Container's foreground color ("foreground")</li>
     *    <li>this Container's focusability ("focusable")</li>
     *    <li>this Container's focus traversal keys enabled state
     *        ("focusTraversalKeysEnabled")</li>
     *    <li>this Container's Set of FORWARD_TRAVERSAL_KEYS
     *        ("forwardFocusTraversalKeys")</li>
     *    <li>this Container's Set of BACKWARD_TRAVERSAL_KEYS
     *        ("backwardFocusTraversalKeys")</li>
     *    <li>this Container's Set of UP_CYCLE_TRAVERSAL_KEYS
     *        ("upCycleFocusTraversalKeys")</li>
     *    <li>this Container's Set of DOWN_CYCLE_TRAVERSAL_KEYS
     *        ("downCycleFocusTraversalKeys")</li>
     *    <li>this Container's focus traversal policy ("focusTraversalPolicy")
     *        </li>
     *    <li>this Container's focus-cycle-root state ("focusCycleRoot")</li>
     * </ul>
     * Note that if this Container is inheriting a bound property, then no
     * event will be fired in response to a change in the inherited property.
     * <p>
     * If listener is null, no exception is thrown and no action is performed.
     *
     * @param    listener  the PropertyChangeListener to be added
     *
     * @see Component#removePropertyChangeListener
     * @see #addPropertyChangeListener(java.lang.String,java.beans.PropertyChangeListener)
     * 
     */
    @Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
    	super.addPropertyChangeListener(listener);
    }

    /**
     * Adds a PropertyChangeListener to the listener list for a specific
     * property. The specified property may be user-defined, or one of the
     * following defaults:
     * <ul>
     *    <li>this Container's font ("font")</li>
     *    <li>this Container's background color ("background")</li>
     *    <li>this Container's foreground color ("foreground")</li>
     *    <li>this Container's focusability ("focusable")</li>
     *    <li>this Container's focus traversal keys enabled state
     *        ("focusTraversalKeysEnabled")</li>
     *    <li>this Container's Set of FORWARD_TRAVERSAL_KEYS
     *        ("forwardFocusTraversalKeys")</li>
     *    <li>this Container's Set of BACKWARD_TRAVERSAL_KEYS
     *        ("backwardFocusTraversalKeys")</li>
     *    <li>this Container's Set of UP_CYCLE_TRAVERSAL_KEYS
     *        ("upCycleFocusTraversalKeys")</li>
     *    <li>this Container's Set of DOWN_CYCLE_TRAVERSAL_KEYS
     *        ("downCycleFocusTraversalKeys")</li>
     *    <li>this Container's focus traversal policy ("focusTraversalPolicy")
     *        </li>
     *    <li>this Container's focus-cycle-root state ("focusCycleRoot")</li>
     *    <li>this Container's focus-traversal-policy-provider state("focusTraversalPolicyProvider")</li>
     *    <li>this Container's focus-traversal-policy-provider state("focusTraversalPolicyProvider")</li>
     * </ul>
     * Note that if this Container is inheriting a bound property, then no
     * event will be fired in response to a change in the inherited property.
     * <p>
     * If listener is null, no exception is thrown and no action is performed.
     *
     * @param propertyName one of the property names listed above
     * @param listener the PropertyChangeListener to be added
     *
     * @see #addPropertyChangeListener(java.beans.PropertyChangeListener)
     * @see Component#removePropertyChangeListener
     */
    @Override
		public void addPropertyChangeListener(String propertyName,
                                          PropertyChangeListener listener) {
        super.addPropertyChangeListener(propertyName, listener);
    }


//     * --- Accessibility Support ---
//     */
//
//    /**
//     * Inner class of Container used to provide default support for
//     * accessibility.  This class is not meant to be used directly by
//     * application developers, but is instead meant only to be
//     * subclassed by container developers.
//     * <p>
//     * The class used to obtain the accessible role for this object,
//     * as well as implementing many of the methods in the
//     * AccessibleContainer interface.
//     * @since 1.3
//     */
//    protected class AccessibleAWTContainer extends AccessibleAWTComponent {
//
//        /**
//         * JDK1.3 serialVersionUID
//         */
//        //private static final long serialVersionUID = 5081320404842566097L;
//
//        /**
//         * Returns the number of accessible children in the object.  If all
//         * of the children of this object implement <code>Accessible</code>,
//         * then this method should return the number of children of this object.
//         *
//         * @return the number of accessible children in the object
//         */
//        public int getAccessibleChildrenCount() {
//            return Container.this.getAccessibleChildrenCount();
//        }
//
//        /**
//         * Returns the nth <code>Accessible</code> child of the object.
//         *
//         * @param i zero-based index of child
//         * @return the nth <code>Accessible</code> child of the object
//         */
//        public Accessible getAccessibleChild(int i) {
//            return Container.this.getAccessibleChild(i);
//        }
//
//        /**
//         * Returns the <code>Accessible</code> child, if one exists,
//         * contained at the local coordinate <code>Point</code>.
//         *
//         * @param p the point defining the top-left corner of the
//         *    <code>Accessible</code>, given in the coordinate space
//         *    of the object's parent
//         * @return the <code>Accessible</code>, if it exists,
//         *    at the specified location; else <code>null</code>
//         */
//        public Accessible getAccessibleAt(Point p) {
//            return Container.this.getAccessibleAt(p);
//        }
//
//        protected ContainerListener accessibleContainerHandler = null;
//
//        /**
//         * Fire <code>PropertyChange</code> listener, if one is registered,
//         * when children are added or removed.
//         * @since 1.3
//         */
//        protected class AccessibleContainerHandler
//            implements ContainerListener {
//            public void componentAdded(ContainerEvent e) {
//                Component c = e.getChild();
//                if (c != null && c instanceof Accessible) {
//                    AccessibleAWTContainer.this.firePropertyChange(
//                        AccessibleContext.ACCESSIBLE_CHILD_PROPERTY,
//                        null, ((Accessible) c).getAccessibleContext());
//                }
//            }
//            public void componentRemoved(ContainerEvent e) {
//                Component c = e.getChild();
//                if (c != null && c instanceof Accessible) {
//                    AccessibleAWTContainer.this.firePropertyChange(
//                        AccessibleContext.ACCESSIBLE_CHILD_PROPERTY,
//                        ((Accessible) c).getAccessibleContext(), null);
//                }
//            }
//        }
//
//        /**
//         * Adds a PropertyChangeListener to the listener list.
//         *
//         * @param listener  the PropertyChangeListener to be added
//         */
//        public void addPropertyChangeListener(PropertyChangeListener listener) {
//            if (accessibleContainerHandler == null) {
//                accessibleContainerHandler = new AccessibleContainerHandler();
//                Container.this.addContainerListener(accessibleContainerHandler);
//            }
//            super.addPropertyChangeListener(listener);
//        }
//
//    } // inner class AccessibleAWTContainer
//
//    /**
//     * Returns the <code>Accessible</code> child contained at the local
//     * coordinate <code>Point</code>, if one exists.  Otherwise
//     * returns <code>null</code>.
//     *
//     * @param p the point defining the top-left corner of the
//     *    <code>Accessible</code>, given in the coordinate space
//     *    of the object's parent
//     * @return the <code>Accessible</code> at the specified location,
//     *    if it exists; otherwise <code>null</code>
//     */
//    Accessible getAccessibleAt(Point p) {
//        synchronized (getTreeLock()) {
//            if (this instanceof Accessible) {
//                Accessible a = (Accessible)this;
//                AccessibleContext ac = a.getAccessibleContext();
//                if (ac != null) {
//                    AccessibleComponent acmp;
//                    Point location;
//                    int nchildren = ac.getAccessibleChildrenCount();
//                    for (int i=0; i < nchildren; i++) {
//                        a = ac.getAccessibleChild(i);
//                        if ((a != null)) {
//                            ac = a.getAccessibleContext();
//                            if (ac != null) {
//                                acmp = ac.getAccessibleComponent();
//                                if ((acmp != null) && (acmp.isShowing())) {
//                                    location = acmp.getLocation();
//                                    Point np = new Point(p.x-location.x,
//                                                         p.y-location.y);
//                                    if (acmp.contains(np)){
//                                        return a;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                return (Accessible)this;
//            } else {
//                Component ret = this;
//                if (!this.contains(p.x,p.y)) {
//                    ret = null;
//                } else {
//                    int ncomponents = this.getComponentCount();
//                    for (int i=0; i < ncomponents; i++) {
//                        Component comp = this.getComponent(i);
//                        if ((comp != null) && comp.isShowing()) {
//                            Point location = comp.getLocation();
//                            if (comp.contains(p.x-location.x,p.y-location.y)) {
//                                ret = comp;
//                            }
//                        }
//                    }
//                }
//                if (ret instanceof Accessible) {
//                    return (Accessible) ret;
//                }
//            }
//            return null;
//        }
//    }
//
//    /**
//     * Returns the number of accessible children in the object.  If all
//     * of the children of this object implement <code>Accessible</code>,
//     * then this method should return the number of children of this object.
//     *
//     * @return the number of accessible children in the object
//     */
//    int getAccessibleChildrenCount() {
//        synchronized (getTreeLock()) {
//            int count = 0;
//            Component[] children = this.getComponents();
//            for (int i = 0; i < children.length; i++) {
//                if (children[i] instanceof Accessible) {
//                    count++;
//                }
//            }
//            return count;
//        }
//    }
//
//    /**
//     * Returns the nth <code>Accessible</code> child of the object.
//     *
//     * @param i zero-based index of child
//     * @return the nth <code>Accessible</code> child of the object
//     */
//    Accessible getAccessibleChild(int i) {
//        synchronized (getTreeLock()) {
//            Component[] children = this.getComponents();
//            int count = 0;
//            for (int j = 0; j < children.length; j++) {
//                if (children[j] instanceof Accessible) {
//                    if (count == i) {
//                        return (Accessible) children[j];
//                    } else {
//                        count++;
//                    }
//                }
//            }
//            return null;
//        }
//    }

    // ************************** MIXING CODE *******************************

//    final void increaseComponentCount(Component c) {
////        synchronized (getTreeLock()) {
//            if (!c.isDisplayable()) {
//                throw new IllegalStateException(
//                    "Peer does not exist while invoking the increaseComponentCount() method"
//                );
//            }
//
//            int addHW = 0;
//            int addLW = 0;
//
//            if (c instanceof Container) {
//                addLW = ((Container)c).numOfLWComponents;
//                addHW = ((Container)c).numOfHWComponents;
//            }
//            if (c.isLightweight()) {
//                addLW++;
//            } else {
//                addHW++;
//            }
//
//            for (Container cont = this; cont != null; cont = cont.getContainer()) {
//                cont.numOfLWComponents += addLW;
//                cont.numOfHWComponents += addHW;
//            }
//  //      }
//    }

//    final void decreaseComponentCount(Component c) {
//    //    synchronized (getTreeLock()) {
//            if (!c.isDisplayable()) {
//                throw new IllegalStateException(
//                    "Peer does not exist while invoking the decreaseComponentCount() method"
//                );
//            }
//
//            int subHW = 0;
//            int subLW = 0;
//
//            if (c instanceof Container) {
//                subLW = ((Container)c).numOfLWComponents;
//                subHW = ((Container)c).numOfHWComponents;
//            }
//            if (c.isLightweight()) {
//                subLW++;
//            } else {
//                subHW++;
//            }
//
//            for (Container cont = this; cont != null; cont = cont.getContainer()) {
//                cont.numOfLWComponents -= subLW;
//                cont.numOfHWComponents -= subHW;
//            }
//      //  }
//   }
//
//    private int getTopmostComponentIndex() {
//        checkTreeLock();
//        if (getComponentCount() > 0) {
//            return 0;
//        }
//        return -1;
//    }
//
//    private int getBottommostComponentIndex() {
//        checkTreeLock();
//        if (getComponentCount() > 0) {
//            return getComponentCount() - 1;
//        }
//        return -1;
//    }
//
//    /*
//     * This method is overriden to handle opaque children in non-opaque
//     * containers.
//     */
//    @Override
//    final Region getOpaqueShape() {
//        checkTreeLock();
//        if (isLightweight() && isNonOpaqueForMixing()
//                && hasLightweightDescendants())
//        {
//            Region s = Region.EMPTY_REGION;
//            for (int index = 0; index < getComponentCount(); index++) {
//                Component c = getComponent(index);
//                if (c.isLightweight() && c.isShowing()) {
//                    s = s.getUnion(c.getOpaqueShape());
//                }
//            }
//            return s.getIntersection(getNormalShape());
//        }
//        return super.getOpaqueShape();
//    }


//    final void recursiveSubtractAndApplyShape(Region shape) {
//        recursiveSubtractAndApplyShape(shape, getTopmostComponentIndex(), getBottommostComponentIndex());
//    }
//
//    final void recursiveSubtractAndApplyShape(Region shape, int fromZorder) {
//        recursiveSubtractAndApplyShape(shape, fromZorder, getBottommostComponentIndex());
//    }
//
//    final void recursiveSubtractAndApplyShape(Region shape, int fromZorder, int toZorder) {
//        checkTreeLock();
//        if (mixingLog.isLoggable(Level.FINE)) {
//            mixingLog.fine("this = " + this +
//                "; shape=" + shape + "; fromZ=" + fromZorder + "; toZ=" + toZorder);
//        }
//        if (fromZorder == -1) {
//            return;
//        }
//        if (shape.isEmpty()) {
//            return;
//        }
//        // An invalid container with not-null layout should be ignored
//        // by the mixing code, the container will be validated later
//        // and the mixing code will be executed later.
//        if (getLayout() != null && !isValid()) {
//            return;
//        }
//        for (int index = fromZorder; index <= toZorder; index++) {
//            Component comp = getComponent(index);
//            if (!comp.isLightweight()) {
//                comp.subtractAndApplyShape(shape);
//            } else if (comp instanceof Container &&
//                    ((Container)comp).hasHeavyweightDescendants() && comp.isShowing()) {
//                ((Container)comp).recursiveSubtractAndApplyShape(shape);
//            }
//        }
//    }
//
//    final void recursiveApplyCurrentShape() {
//        recursiveApplyCurrentShape(getTopmostComponentIndex(), getBottommostComponentIndex());
//    }
//
//    final void recursiveApplyCurrentShape(int fromZorder) {
//        recursiveApplyCurrentShape(fromZorder, getBottommostComponentIndex());
//    }

//    final void recursiveApplyCurrentShape(int fromZorder, int toZorder) {
//        checkTreeLock();
//        if (mixingLog.isLoggable(Level.FINE)) {
//            mixingLog.fine("this = " + this +
//                "; fromZ=" + fromZorder + "; toZ=" + toZorder);
//        }
//        if (fromZorder == -1) {
//            return;
//        }
//        // An invalid container with not-null layout should be ignored
//        // by the mixing code, the container will be validated later
//        // and the mixing code will be executed later.
//        if (getLayout() != null && !isValid()) {
//            return;
//        }
//        for (int index = fromZorder; index <= toZorder; index++) {
//            Component comp = getComponent(index);
//            if (!comp.isLightweight()) {
//                comp.applyCurrentShape();
//                if (comp instanceof Container && ((Container)comp).getLayout() == null) {
//                    ((Container)comp).recursiveApplyCurrentShape();
//                }
//            } else if (comp instanceof Container &&
//                    ((Container)comp).hasHeavyweightDescendants()) {
//                ((Container)comp).recursiveApplyCurrentShape();
//            }
//        }
//    }
//
//    private void recursiveShowHeavyweightChildren() {
//        if (!hasHeavyweightDescendants() || !isVisible()) {
//            return;
//        }
//        for (int index = 0; index < getComponentCount(); index++) {
//            Component comp = getComponent(index);
//            if (comp.isLightweight()) {
//                if  (comp instanceof Container) {
//                    ((Container)comp).recursiveShowHeavyweightChildren();
//                }
//            } else {
//                if (comp.isVisible()) {
//                    ComponentPeer peer = comp.getPeer();
//                    if (peer != null) {
//                        peer.setVisible(true);// SwingJS  was show();
//                    }
//                }
//            }
//        }
//    }
//
//    private void recursiveHideHeavyweightChildren() {
//        if (!hasHeavyweightDescendants()) {
//            return;
//        }
//        for (int index = 0; index < getComponentCount(); index++) {
//            Component comp = getComponent(index);
//            if (comp.isLightweight()) {
//                if  (comp instanceof Container) {
//                    ((Container)comp).recursiveHideHeavyweightChildren();
//                }
//            } else {
//                if (comp.isVisible()) {
//                    ComponentPeer peer = comp.getPeer();
//                    if (peer != null) {
//                        peer.setVisible(false);// SwingJS  was hide();
//                    }
//                }
//            }
//        }
//    }
//
//    private void recursiveRelocateHeavyweightChildren(Point origin) {
//        for (int index = 0; index < getComponentCount(); index++) {
//            Component comp = getComponent(index);
//            if (comp.isLightweight()) {
//                if  (comp instanceof Container &&
//                        ((Container)comp).hasHeavyweightDescendants())
//                {
//                    final Point newOrigin = new Point(origin);
//                    newOrigin.translate(comp.getX(), comp.getY());
//                    ((Container)comp).recursiveRelocateHeavyweightChildren(newOrigin);
//                }
//            } else {
//                ComponentPeer peer = comp.getPeer();
//                if (peer != null) {
//                    peer.setBounds(origin.x + comp.getX(), origin.y + comp.getY(),
//                            comp.getWidth(), comp.getHeight(),
//                            ComponentPeer.SET_LOCATION);
//                }
//            }
//        }
//    }
//
//    /*
//     * Consider the heavyweight container hides or shows the HW descendants
//     * automatically. Therefore we care of LW containers' visibility only.
//     */
//    private boolean isRecursivelyVisibleUpToHeavyweightContainer() {
//        if (!isLightweight()) {
//            return true;
//        }
//        return isVisible() && (getContainer() == null ||
//             getContainer().isRecursivelyVisibleUpToHeavyweightContainer());
//    }
//
//    @Override
//    void mixOnShowing() {
//        synchronized (getTreeLock()) {
//            if (mixingLog.isLoggable(Level.FINE)) {
//                mixingLog.fine("this = " + this);
//            }
//
//            if (!isMixingNeeded()) {
//                return;
//            }
//
//            boolean isLightweight = isLightweight();
//
//            if (isLightweight && isRecursivelyVisibleUpToHeavyweightContainer()) {
//                recursiveShowHeavyweightChildren();
//            }
//
//            if (!isLightweight || (isLightweight && hasHeavyweightDescendants())) {
//                recursiveApplyCurrentShape();
//            }
//
//            super.mixOnShowing();
//        }
//    }

//    @Override
//    void mixOnHiding(boolean isLightweight) {
//        synchronized (getTreeLock()) {
//            if (mixingLog.isLoggable(Level.FINE)) {
//                mixingLog.fine("this = " + this +
//                        "; isLightweight=" + isLightweight);
//            }
//            if (isLightweight) {
//                recursiveHideHeavyweightChildren();
//            }
//            super.mixOnHiding(isLightweight);
//        }
//    }
//
//    @Override
//    void mixOnReshaping() {
//        synchronized (getTreeLock()) {
//            if (mixingLog.isLoggable(Level.FINE)) {
//                mixingLog.fine("this = " + this);
//            }
//
//            boolean isMixingNeeded = isMixingNeeded();
//
//            if (isLightweight() && hasHeavyweightDescendants()) {
//                final Point origin = new Point(getX(), getY());
//                for (Container cont = getContainer();
//                        cont != null && cont.isLightweight();
//                        cont = cont.getContainer())
//                {
//                    origin.translate(cont.getX(), cont.getY());
//                }
//
//                recursiveRelocateHeavyweightChildren(origin);
//
//                if (!isMixingNeeded) {
//                    return;
//                }
//
//                recursiveApplyCurrentShape();
//            }
//
//            if (!isMixingNeeded) {
//                return;
//            }
//
//            super.mixOnReshaping();
//        }
//    }
//
//    @Override
//    void mixOnZOrderChanging(int oldZorder, int newZorder) {
//        synchronized (getTreeLock()) {
//            if (mixingLog.isLoggable(Level.FINE)) {
//                mixingLog.fine("this = " + this +
//                    "; oldZ=" + oldZorder + "; newZ=" + newZorder);
//            }
//
//            if (!isMixingNeeded()) {
//                return;
//            }
//
//            boolean becameHigher = newZorder < oldZorder;
//
//            if (becameHigher && isLightweight() && hasHeavyweightDescendants()) {
//                recursiveApplyCurrentShape();
//            }
//            super.mixOnZOrderChanging(oldZorder, newZorder);
//        }
//    }
//
//    @Override
//    void mixOnValidating() {
//        synchronized (getTreeLock()) {
//            if (mixingLog.isLoggable(Level.FINE)) {
//                mixingLog.fine("this = " + this);
//            }
//
//            if (!isMixingNeeded()) {
//                return;
//            }
//
//            if (hasHeavyweightDescendants()) {
//                recursiveApplyCurrentShape();
//            }
//
//            if (isLightweight() && isNonOpaqueForMixing()) {
//                subtractAndApplyShapeBelowMe();
//            }
//
//            super.mixOnValidating();
//        }
//    }

    // ****************** END OF MIXING CODE ********************************
}


/**
 * Class to manage the dispatching of MouseEvents to the lightweight descendants
 * and SunDropTargetEvents to both lightweight and heavyweight descendants
 * contained by a native container.
 *
 * NOTE: the class name is not appropriate anymore, but we cannot change it
 * because we must keep serialization compatibility.
 *
 * @author Timothy Prinzing
 */
class LightweightDispatcher implements AWTEventListener {

    /*
     * JDK 1.1 serialVersionUID
     */
    //private static final long serialVersionUID = 5184291520170872969L;
    /*
     * Our own mouse event for when we're dragged over from another hw
     * container
     */
    private static final int  LWD_MOUSE_DRAGGED_OVER = 1500;

	private Component targetLastDown, targetLastKnown;

//    private static final Logger eventLog = Logger.getLogger("java.awt.event.LightweightDispatcher");

    LightweightDispatcher(Container nativeContainer) {
        this.nativeContainer = nativeContainer;
        mouseEventTarget = null;
        eventMask = 0;
    }

    /*
     * Clean up any resources allocated when dispatcher was created;
     * should be called from Container.removeNotify
     */
    void dispose() {
        stopListeningForOtherDrags();
        mouseEventTarget = null;
    }

    /**
     * Enables events to subcomponents.
     */
    void enableEvents(long events) {
        eventMask |= events;
    }

    /**
     * Dispatches an event to a sub-component if necessary, and
     * returns whether or not the event was forwarded to a
     * sub-component.
     *
     * @param e the event
     */
    boolean dispatchEvent(AWTEvent e) {
        boolean ret = false;

        /*
         * Fix for BugTraq Id 4389284.
         * Dispatch SunDropTargetEvents regardless of eventMask value.
         * Do not update cursor on dispatching SunDropTargetEvents.
         */
//        if (e instanceof SunDropTargetEvent) {
//
//            SunDropTargetEvent sdde = (SunDropTargetEvent) e;
//            ret = processDropTargetEvent(sdde);
//
//        } else {
            if (e instanceof MouseEvent && (eventMask & MOUSE_MASK) != 0) {
                MouseEvent me = (MouseEvent) e;
                ret = processMouseEvent(me);
            }

//            if (e.getID() == MouseEvent.MOUSE_MOVED) {
//                nativeContainer.updateCursorImmediately();
//            }
//        }

        return ret;
    }

    /* This method effectively returns whether or not a mouse button was down
     * just BEFORE the event happened.  A better method name might be
     * wasAMouseButtonDownBeforeThisEvent().
     */
    private boolean isMouseGrab(MouseEvent e) {
        int modifiers = e.getModifiersEx();

        if(e.getID() == MouseEvent.MOUSE_PRESSED
            || e.getID() == MouseEvent.MOUSE_RELEASED)
        {
            switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                modifiers ^= InputEvent.BUTTON1_DOWN_MASK;
                break;
            case MouseEvent.BUTTON2:
                modifiers ^= InputEvent.BUTTON2_DOWN_MASK;
                break;
            case MouseEvent.BUTTON3:
                modifiers ^= InputEvent.BUTTON3_DOWN_MASK;
                break;
            }
        }
        /* modifiers now as just before event */
        return ((modifiers & (InputEvent.BUTTON1_DOWN_MASK
                              | InputEvent.BUTTON2_DOWN_MASK
                              | InputEvent.BUTTON3_DOWN_MASK)) != 0);
    }

	/**
	 * This method attempts to distribute a mouse event to a lightweight component.
	 * It tries to avoid doing any unnecessary probes down into the component tree
	 * to minimize the overhead of determining where to route the event, since mouse
	 * movement events tend to come in large and frequent amounts.
	 */
	private boolean processMouseEvent(MouseEvent e) {

		int id = e.getID();

		// sensitive to mouse events

		Component mouseOver = nativeContainer.getMouseEventTarget(e.getX(), e.getY(), Container.INCLUDE_SELF);

		trackMouseEnterExit(mouseOver, e);

		Component actualTarget; 

		switch (id) {
		case MouseEvent.MOUSE_DRAGGED:
		case MouseEvent.MOUSE_RELEASED:
			actualTarget = targetLastDown;
			break;
		case MouseEvent.MOUSE_EXITED:
			actualTarget = targetLastKnown;
			break;
	    default:
	    	actualTarget = JSMouse.getJ2SEventTarget(e);
	    	break;
		}
		// SwingJS note: This was moved here 7/8/2019 from above. 
		targetLastKnown = (actualTarget == null ? mouseOver : actualTarget);
		if (id == MouseEvent.MOUSE_PRESSED)
			targetLastDown = targetLastKnown;

		// 4508327 : MOUSE_CLICKED should only go to the recipient of
		// the accompanying MOUSE_PRESSED, so don't reset mouseEventTarget on a
		// MOUSE_CLICKED.

		if (actualTarget != null)
			mouseEventTarget = actualTarget;
		else if (!isMouseGrab(e) && id != MouseEvent.MOUSE_CLICKED) {
			mouseEventTarget = (mouseOver != nativeContainer) ? mouseOver : null;
		}
		
		if (mouseEventTarget != null) {
			switch (id) {
			case MouseEvent.MOUSE_ENTERED:
			case MouseEvent.MOUSE_EXITED:
				if (JSMouse.getJ2SEventTarget(e) == mouseEventTarget)
					retargetMouseEvent(mouseEventTarget, id, e);
				break;
			case MouseEvent.MOUSE_PRESSED:
				checkInternalFrameMouseDown((JSComponent) e.getSource());
				retargetMouseEvent(mouseEventTarget, id, e);
				break;
			case MouseEvent.MOUSE_RELEASED:
				targetLastDown = null;
				retargetMouseEvent(mouseEventTarget, id, e);
				break;
			case MouseEvent.MOUSE_CLICKED:
				// 4508327: MOUSE_CLICKED should never be dispatched to a Component
				// other than that which received the MOUSE_PRESSED event. If the
				// mouse is now over a different Component, don't dispatch the event.
				// The previous fix for a similar problem was associated with bug
				// 4155217.
				targetLastDown = null;
				if (mouseOver == mouseEventTarget) {
					retargetMouseEvent(mouseOver, id, e);
				}
				break;
			case MouseEvent.MOUSE_MOVED:
				retargetMouseEvent(mouseEventTarget, id, e);
				break;
			case MouseEvent.MOUSE_DRAGGED:
				if (isMouseGrab(e)) {
					retargetMouseEvent(mouseEventTarget, id, e);
				}
				break;
			case MouseEvent.MOUSE_WHEEL:
				// This may send it somewhere that doesn't have MouseWheelEvents
				// enabled. In this case, Component.dispatchEventImpl() will
				// retarget the event to a parent that DOES have the events enabled.
//            if (eventLog.isLoggable(Level.FINEST) && (mouseOver != null)) {
//                eventLog.log(Level.FINEST, "retargeting mouse wheel to " +
//                             mouseOver.getName() + ", " +
//                             mouseOver.getClass());
//            }
				retargetMouseEvent(mouseOver, id, e);
				break;
			}
			e.consume();
		}
		return e.isConsumed();
	}

//    private boolean processDropTargetEvent(SunDropTargetEvent e) {
//        int id = e.getID();
//        int x = e.getX();
//        int y = e.getY();
//
//        /*
//         * Fix for BugTraq ID 4395290.
//         * It is possible that SunDropTargetEvent's Point is outside of the
//         * native container bounds. In this case we truncate coordinates.
//         */
//        if (!nativeContainer.contains(x, y)) {
//            final Dimension d = nativeContainer.getSize();
//            if (d.width <= x) {
//                x = d.width - 1;
//            } else if (x < 0) {
//                x = 0;
//            }
//            if (d.height <= y) {
//                y = d.height - 1;
//            } else if (y < 0) {
//                y = 0;
//            }
//        }
//        Component mouseOver =   // not necessarily sensitive to mouse events
//            nativeContainer.getDropTargetEventTarget(x, y,
//                                                     Container.INCLUDE_SELF);
//        trackMouseEnterExit(mouseOver, e);
//
//        if (mouseOver != nativeContainer && mouseOver != null) {
//            switch (id) {
//            case SunDropTargetEvent.MOUSE_ENTERED:
//            case SunDropTargetEvent.MOUSE_EXITED:
//                break;
//            default:
//                retargetMouseEvent(mouseOver, id, e);
//                e.consume();
//                break;
//            }
//        }
//        return e.isConsumed();
//    }

    public void checkInternalFrameMouseDown(JSComponent c) {
    	JSFrameViewer fv = c.getFrameViewer();
    	JSComponent top = fv.getTopComponent();
    	if (top.getUIClassID() == "InternalFrameUI")
			try {
				((JInternalFrame) top).setSelected(true);
			} catch (PropertyVetoException e) {
			}
	}

	/*
	 * Generates enter/exit events as mouse moves over lw components
	 * 
	 * @param targetOver Target mouse is over (including native container)
	 * 
	 * @param e Mouse event in native container
	 */
	private void trackMouseEnterExit(Component targetOver, MouseEvent e) {
		Component targetEnter = null;
		int id = e.getID();

//        if (e instanceof SunDropTargetEvent &&
//            id == MouseEvent.MOUSE_ENTERED &&
//            isMouseInNativeContainer == true) {
//            // This can happen if a lightweight component which initiated the
//            // drag has an associated drop target. MOUSE_ENTERED comes when the
//            // mouse is in the native container already. To propagate this event
//            // properly we should null out targetLastEntered.
//            targetLastEntered = null;
//        } else 
//        	
		if (e instanceof ActiveEvent) {
			targetLastEntered = null;
			return;
		} 
		if (id == MouseEvent.MOUSE_EXITED) {
			isMouseInNativeContainer = false;
			stopListeningForOtherDrags();
		} else if (id != MouseEvent.MOUSE_DRAGGED && id != LWD_MOUSE_DRAGGED_OVER
				&& isMouseInNativeContainer == false) {
			// any event but an exit or drag means we're in the native container
			isMouseInNativeContainer = true;
			startListeningForOtherDrags();
		}
		
		if (isMouseInNativeContainer) {
			targetEnter = targetOver;
		}

		if (targetLastEntered == targetEnter) {
			return;
		}

		if (targetLastEntered != null) {
			retargetMouseEvent(targetLastEntered, MouseEvent.MOUSE_EXITED, e);
		}
		if (id == MouseEvent.MOUSE_EXITED) {
			// consume native exit event if we generate one
			e.consume();
		}

		if (targetEnter != null) {
			retargetMouseEvent(targetEnter, MouseEvent.MOUSE_ENTERED, e);
		}
		if (id == MouseEvent.MOUSE_ENTERED) {
			// consume native enter event if we generate one
			e.consume();
		}

		targetLastEntered = targetEnter;
	}

    /*
     * Listens to global mouse drag events so even drags originating
     * from other heavyweight containers will generate enter/exit
     * events in this container
     */
    private void startListeningForOtherDrags() {
//        java.security.AccessController.doPrivileged(
//            new java.security.PrivilegedAction() {
//                public Object run() {
//                    nativeContainer.getToolkit().addAWTEventListener(
//                        LightweightDispatcher.this,
//                        AWTEvent.MOUSE_EVENT_MASK |
//                        AWTEvent.MOUSE_MOTION_EVENT_MASK);
//                    return null;
//                }
//            }
//        );
    }

    private void stopListeningForOtherDrags() {
//        java.security.AccessController.doPrivileged(
//            new java.security.PrivilegedAction() {
//                public Object run() {
//                    nativeContainer.getToolkit().removeAWTEventListener(LightweightDispatcher.this);
//                    return null;
//                }
//            }
//        );
    }

    /*
     * (Implementation of AWTEventListener)
     * Listen for drag events posted in other hw components so we can
     * track enter/exit regardless of where a drag originated
     */
    @Override
		public void eventDispatched(AWTEvent e) {
        boolean isForeignDrag = (e instanceof MouseEvent) &&
//                                !(e instanceof SunDropTargetEvent) &&
                                (e.id == MouseEvent.MOUSE_DRAGGED) &&
                                (e.getSource() != nativeContainer);

        if (!isForeignDrag) {
            // only interested in drags from other hw components
            return;
        }

        MouseEvent      srcEvent = (MouseEvent)e;
        MouseEvent      me;

        synchronized (nativeContainer.getTreeLock()) {
            Component srcComponent = srcEvent.getComponent();

            // component may have disappeared since drag event posted
            // (i.e. Swing hierarchical menus)
            if ( !srcComponent.isShowing() ) {
                return;
            }

            // see 5083555
            // check if srcComponent is in any modal blocked window
            Component c = nativeContainer;
            //SwingJS TODO Q: no check for applet here?
            while (c != null && !(c instanceof Window)) {
                c = c.getParent_NoClientCode();
            }
            if ((c == null) || ((Window)c).isModalBlocked()) {
                return;
            }

            //
            // create an internal 'dragged-over' event indicating
            // we are being dragged over from another hw component
            //
            me = new MouseEvent(nativeContainer,
                               LWD_MOUSE_DRAGGED_OVER,
                               srcEvent.getWhen(),
                               srcEvent.getModifiersEx() | srcEvent.getModifiers(),
                               srcEvent.getX(),
                               srcEvent.getY(),
                               srcEvent.getXOnScreen(),
                               srcEvent.getYOnScreen(),
                               srcEvent.getClickCount(),
                               srcEvent.isPopupTrigger(),
                               srcEvent.getButton());
            ((AWTEvent)srcEvent).copyPrivateDataInto(me);
            // translate coordinates to this native container
//            final Point ptSrcOrigin = srcComponent.getLocationOnScreen();

//            if (AppContext.getAppContext() != nativeContainer.appContext) {
//                final MouseEvent mouseEvent = me;
//                Runnable r = new Runnable() {
//                        public void run() {
//                            if (!nativeContainer.isShowing() ) {
//                                return;
//                            }
//
//                            Point       ptDstOrigin = nativeContainer.getLocationOnScreen();
//                            mouseEvent.translatePoint(ptSrcOrigin.x - ptDstOrigin.x,
//                                              ptSrcOrigin.y - ptDstOrigin.y );
//                            Component targetOver =
//                                nativeContainer.getMouseEventTarget(mouseEvent.getX(),
//                                                                    mouseEvent.getY(),
//                                                                    Container.INCLUDE_SELF);
//                            trackMouseEnterExit(targetOver, mouseEvent);
//                        }
//                    };
////                SunToolkit.executeOnEventHandlerThread(nativeContainer, r);
//                return;
//            } else {
//                if (!nativeContainer.isShowing() ) {
//                    return;
//                }
//
//                Point   ptDstOrigin = nativeContainer.getLocationOnScreen();
//                me.translatePoint( ptSrcOrigin.x - ptDstOrigin.x, ptSrcOrigin.y - ptDstOrigin.y );
//            }
        }
        // feed the 'dragged-over' event directly to the enter/exit
        // code (not a real event so don't pass it to dispatchEvent)
        Component targetOver =
            nativeContainer.getMouseEventTarget(me.getX(), me.getY(),
                                                Container.INCLUDE_SELF);
        trackMouseEnterExit(targetOver, me);
    }

	/**
     * Sends a mouse event to the current mouse event recipient using
     * the given event (sent to the windowed host) as a srcEvent.  If
     * the mouse event target is still in the component tree, the
     * coordinates of the event are translated to those of the target.
     * If the target has been removed, we don't bother to send the
     * message.
	 * 
     * Except for SwingJS we are using the parent frame as the native container,
     * and the PopupMenu does not have that as a parent. 
	 */
	void retargetMouseEvent(Component target, int id, MouseEvent e) {
		if (target == null) {
			return; // mouse is over another hw component or target is disabled
		}

		int x = e.getX(), y = e.getY();
		Component component = target;
		Component p = ((JSComponent) target).秘getUI().getTargetParent();
		if (p != null) {
			target = component = p;
		}
		for (; component != null && component != nativeContainer; component = component.getParent()) {
			x -= component.x;
			y -= component.y;
			if (((JSComponent) component).getUIClassID() == "PopupMenuUI")
				break; // SwingJS not to worry
		}
		MouseEvent retargeted;
		if (component != null) {
//            if (e instanceof SunDropTargetEvent) {
//                retargeted = new SunDropTargetEvent(target,
//                                                    id,
//                                                    x,
//                                                    y,
//                                                    ((SunDropTargetEvent)e).getDispatcher());
//            } else 
//            	
			if (id == MouseEvent.MOUSE_WHEEL) {
                retargeted = new MouseWheelEvent(target,
                                      id,
                                       e.getWhen(),
                                       e.getModifiersEx() | e.getModifiers(),
                                       x,
                                       y,
                                       e.getXOnScreen(),
                                       e.getYOnScreen(),
                                       e.getClickCount(),
                                       e.isPopupTrigger(),
                                       ((MouseWheelEvent)e).getScrollType(),
                                       ((MouseWheelEvent)e).getScrollAmount(),
                                       ((MouseWheelEvent)e).getWheelRotation(),
                                       ((MouseWheelEvent)e).getPreciseWheelRotation());
            }
            else {
                retargeted = new MouseEvent(target,
                                            id,
                                            e.getWhen(),
                                            e.getModifiersEx() | e.getModifiers(),
                                            x,
                                            y,
                                            e.getXOnScreen(),
                                            e.getYOnScreen(),
                                            e.getClickCount(),
                                            e.isPopupTrigger(),
                                            e.getButton());
			}

			((AWTEvent) e).copyPrivateDataInto(retargeted);

			if (target == nativeContainer) {
				// avoid recursively calling LightweightDispatcher...
				((Container) target).dispatchEventToSelf(retargeted);
			} else {
				// assert AppContext.getAppContext() == target.appContext;

				if (nativeContainer.modalComp != null) {
					if (((Container) nativeContainer.modalComp).isAncestorOf(target)) {
						target.dispatchEvent(retargeted);
					} else {
						e.consume();
					}
				} else {
					target.dispatchEvent(retargeted);
					JSMouse.setPropagation(target, e);
				}
			}
		}
	}

    // --- member variables -------------------------------

    /**
     * The windowed container that might be hosting events for
     * subcomponents.
     */
    private Container nativeContainer;

//    /**
//     * This variable is not used, but kept for serialization compatibility
//     */
//    private Component focus;

    /**
     * The current subcomponent being hosted by this windowed
     * component that has events being forwarded to it.  If this
     * is null, there are currently no events being forwarded to
     * a subcomponent.
     */
    private transient Component mouseEventTarget;

    /**
     * The last component entered
     */
    private transient Component targetLastEntered;

    /**
     * Is the mouse over the native container
     */
    private transient boolean isMouseInNativeContainer = false;

//    /**
//     * This variable is not used, but kept for serialization compatibility
//     */
//    private Cursor nativeCursor;

    /**
     * The event mask for contained lightweight components.  Lightweight
     * components need a windowed container to host window-related
     * events.  This separate mask indicates events that have been
     * requested by contained lightweight components without effecting
     * the mask of the windowed component itself.
     */
    private long eventMask;

//    /**
//     * The kind of events routed to lightweight components from windowed
//     * hosts.
//     */
//    private static final long PROXY_EVENT_MASK =
//        AWTEvent.FOCUS_EVENT_MASK |
//        AWTEvent.KEY_EVENT_MASK |
//        AWTEvent.MOUSE_EVENT_MASK |
//        AWTEvent.MOUSE_MOTION_EVENT_MASK |
//        AWTEvent.MOUSE_WHEEL_EVENT_MASK;

    private static final long MOUSE_MASK =
        AWTEvent.MOUSE_EVENT_MASK |
        AWTEvent.MOUSE_MOTION_EVENT_MASK |
        AWTEvent.MOUSE_WHEEL_EVENT_MASK;
}
