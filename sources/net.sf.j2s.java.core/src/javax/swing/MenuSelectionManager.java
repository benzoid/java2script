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

import java.util.ArrayList;
import java.util.List;
//import java.util.Vector;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import sun.awt.AppContext;
import swingjs.JSMenuManager;

/**
 * A MenuSelectionManager owns the selection in menu hierarchy.
 *
 * @author Arnaud Weber
 */
public class MenuSelectionManager {
    private List<MenuElement> selection = new ArrayList<MenuElement>();//Vector();

//    /* diagnostic aids -- should be false for production builds. */
//    private static final boolean TRACE =   false; // trace creates and disposes
//    private static final boolean VERBOSE = false; // show reuse hits/misses
//    private static final boolean DEBUG =   false;  // show bad params, misc.

    private static final Object MENU_SELECTION_MANAGER_KEY = new Object(); // javax.swing.MenuSelectionManager

    /**
     * Returns the default menu selection manager.
     *
     * @return a MenuSelectionManager object
     */
    public static MenuSelectionManager defaultManager() {
        synchronized (MENU_SELECTION_MANAGER_KEY) {
            AppContext context = AppContext.getAppContext();
            MenuSelectionManager msm = (MenuSelectionManager)context.get(
                                                 MENU_SELECTION_MANAGER_KEY);
            if (msm == null) {
                msm = new JSMenuManager();
                context.put(MENU_SELECTION_MANAGER_KEY, msm);
            }

            return msm;
        }
    }

    /**
     * Only one ChangeEvent is needed per button model instance since the
     * event's only state is the source property.  The source of events
     * generated is always "this".
     */
    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Changes the selection in the menu hierarchy.  The elements
     * in the array are sorted in order from the root menu
     * element to the currently selected menu element.
     * <p>
     * Note that this method is public but is used by the look and
     * feel engine and should not be called by client applications.
     *
     * @param path  an array of <code>MenuElement</code> objects specifying
     *        the selected path
     */
    public void setSelectedPath(MenuElement[] path) {
        int i,c;
        int currentSelectionCount = selection.size();
        int firstDifference = 0;

        if(path == null) {
            path = new MenuElement[0];
        }

//        if (DEBUG) {
//            System.out.print("Previous:  "); printMenuElementArray(getSelectedPath());
//            System.out.print("New:  "); printMenuElementArray(path);
//        }
//
        for(i=0,c=path.length;i<c;i++) {
            if(i < currentSelectionCount && selection.get(i) == path[i])
                firstDifference++;
            else
                break;
        }

        for(i=currentSelectionCount - 1 ; i >= firstDifference ; i--) {
            MenuElement me = selection.get(i);
            selection.remove(i);
            me.menuSelectionChanged(false);
        }

        for(i = firstDifference, c = path.length ; i < c ; i++) {
            if (path[i] != null) {
                selection.add(path[i]);
                path[i].menuSelectionChanged(true);
            }
        }

        fireStateChanged();
    }

    /**
     * Returns the path to the currently selected menu item
     *
     * @return an array of MenuElement objects representing the selected path
     */
    public MenuElement[] getSelectedPath() {
        MenuElement res[] = new MenuElement[selection.size()];
        int i,c;
        for(i=0,c=selection.size();i<c;i++)
            res[i] = selection.get(i);
        return res;
    }

    /**
     * Tell the menu selection to close and unselect all the menu components. Call this method
     * when a choice has been made
     */
    public void clearSelectedPath() {
        if (selection.size() > 0) {
            setSelectedPath(null);
        }
    }

    /**
     * Adds a ChangeListener to the button.
     *
     * @param l the listener to add
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener from the button.
     *
     * @param l the listener to remove
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * Returns an array of all the <code>ChangeListener</code>s added
     * to this MenuSelectionManager with addChangeListener().
     *
     * @return all of the <code>ChangeListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[])listenerList.getListeners(
                ChangeListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is created lazily.
     *
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }

    /**
     * When a MenuElement receives an event from a MouseListener, it should never process the event
     * directly. Instead all MenuElements should call this method with the event.
     *
     * @param event  a MouseEvent object
     */
    public void processMouseEvent(MouseEvent event) {
//        int screenX,screenY;
//        Point p;
//        int i,j,d;
//        Component mc;
//        Rectangle r2;
//        int cWidth,cHeight;
//        MenuElement menuElement;
//        MenuElement subElements[];
//        MenuElement path[];
//        //List tmp;
//        int selectionSize;
//        p = event.getPoint();
//
//        Component source = (Component)event.getSource();
//
//        if (!source.isShowing()) {
//            // This can happen if a mouseReleased removes the
//            // containing component -- bug 4146684
//            return;
//        }
//
//        int type = event.getID();
//        int modifiers = event.getModifiers();
//        // 4188027: drag enter/exit added in JDK 1.1.7A, JDK1.2
//        if ((type==MouseEvent.MOUSE_ENTERED||
//             type==MouseEvent.MOUSE_EXITED)
//            && ((modifiers & (InputEvent.BUTTON1_MASK |
//                              InputEvent.BUTTON2_MASK | InputEvent.BUTTON3_MASK)) !=0 )) {
//            return;
//        }
//
//        SwingUtilities.convertPointToScreen(p,source);
//
//        screenX = p.x;
//        screenY = p.y;
//
//        //tmp = (Vector)selection.clone();
//        selectionSize = selection.size();//tmp.size();
//        boolean success = false;
//        for (i=selectionSize - 1;i >= 0 && success == false; i--) {
//            menuElement = (MenuElement) selection.get(i);
//            subElements = menuElement.getSubElements();
//
//            path = null;
//            for (j = 0, d = subElements.length;j < d && success == false; j++) {
//                if (subElements[j] == null)
//                    continue;
//                mc = subElements[j].getComponent();
//                if(!mc.isShowing())
//                    continue;
//                if(mc instanceof JComponent) {
//                    cWidth  = ((JComponent)mc).getWidth();
//                    cHeight = ((JComponent)mc).getHeight();
//                } else {
//                    r2 = mc.getBounds();
//                    cWidth  = r2.width;
//                    cHeight = r2.height;
//                }
//                p.x = screenX;
//                p.y = screenY;
//                SwingUtilities.convertPointFromScreen(p,mc);
//
//                /** Send the event to visible menu element if menu element currently in
//                 *  the selected path or contains the event location
//                 */
//                if(
//                   (p.x >= 0 && p.x < cWidth && p.y >= 0 && p.y < cHeight)) {
//                    int k;
//                    if(path == null) {
//                        path = new MenuElement[i+2];
//                        for(k=0;k<=i;k++)
//                            path[k] = (MenuElement)selection.get(k);
//                    }
//                    path[i+1] = subElements[j];
//                    MenuElement currentSelection[] = getSelectedPath();
//
//                    // Enter/exit detection -- needs tuning...
//                    if (currentSelection[currentSelection.length-1] !=
//                        path[i+1] &&
//                        (currentSelection.length < 2 ||
//                         currentSelection[currentSelection.length-2] !=
//                         path[i+1])) {
//                        Component oldMC = currentSelection[currentSelection.length-1].getComponent();
//
//                        MouseEvent exitEvent = new MouseEvent(oldMC, MouseEvent.MOUSE_EXITED,
//                                                              event.getWhen(),
//                                                              event.getModifiers(), p.x, p.y,
//                                                              event.getXOnScreen(),
//                                                              event.getYOnScreen(),
//                                                              event.getClickCount(),
//                                                              event.isPopupTrigger(),
//                                                              MouseEvent.NOBUTTON);
//                        currentSelection[currentSelection.length-1].
//                            processMouseEvent(exitEvent, path, this);
//
//                        MouseEvent enterEvent = new MouseEvent(mc,
//                                                               MouseEvent.MOUSE_ENTERED,
//                                                               event.getWhen(),
//                                                               event.getModifiers(), p.x, p.y,
//                                                               event.getXOnScreen(),
//                                                               event.getYOnScreen(),
//                                                               event.getClickCount(),
//                                                               event.isPopupTrigger(),
//                                                               MouseEvent.NOBUTTON);
//                        subElements[j].processMouseEvent(enterEvent, path, this);
//                    }
//                    MouseEvent mouseEvent = new MouseEvent(mc, event.getID(),event. getWhen(),
//                                                           event.getModifiers(), p.x, p.y,
//                                                           event.getXOnScreen(),
//                                                           event.getYOnScreen(),
//                                                           event.getClickCount(),
//                                                           event.isPopupTrigger(),
//                                                           MouseEvent.NOBUTTON);
//                    subElements[j].processMouseEvent(mouseEvent, path, this);
//                    success = true;
//                    event.consume();
//                }
//            }
//        }
    }

//    private void printMenuElementArray(MenuElement path[]) {
//        printMenuElementArray(path, false);
//    }
//
//    private void printMenuElementArray(MenuElement path[], boolean dumpStack) {
//        System.out.println("Path is(");
//        int i, j;
//        for(i=0,j=path.length; i<j ;i++){
//            for (int k=0; k<=i; k++)
//                System.out.print("  ");
//            MenuElement me = (MenuElement) path[i];
//            if(me instanceof JMenuItem) {
//                System.out.println(((JMenuItem)me).getText() + ", ");
//            } else if (me instanceof JMenuBar) {
//                System.out.println("JMenuBar, ");
//            } else if(me instanceof JPopupMenu) {
//                System.out.println("JPopupMenu, ");
//            } else if (me == null) {
//                System.out.println("NULL , ");
//            } else {
//                System.out.println("" + me + ", ");
//            }
//        }
//        System.out.println(")");
//
//        if (dumpStack == true)
//            Thread.dumpStack();
//    }
//
    /**
     * Returns the component in the currently selected path
     * which contains sourcePoint.
     *
     * @param source The component in whose coordinate space sourcePoint
     *        is given
     * @param sourcePoint The point which is being tested
     * @return The component in the currently selected path which
     *         contains sourcePoint (relative to the source component's
     *         coordinate space.  If sourcePoint is not inside a component
     *         on the currently selected path, null is returned.
     */
    public Component componentForPoint(Component source, Point sourcePoint) {
//        int screenX,screenY;
//        Point p = sourcePoint;
//        int i,j,d;
//        Component mc;
//        Rectangle r2;
//        int cWidth,cHeight;
//        MenuElement menuElement;
//        MenuElement subElements[];
//        //Vector tmp;
//        int selectionSize;
//
//        SwingUtilities.convertPointToScreen(p,source);
//
//        screenX = p.x;
//        screenY = p.y;
//
//        //tmp = (Vector)selection.clone();
//        selectionSize = selection.size();
//        for(i=selectionSize - 1 ; i >= 0 ; i--) {
//            menuElement = (MenuElement) selection.get(i);
//            subElements = menuElement.getSubElements();
//
//            for(j = 0, d = subElements.length ; j < d ; j++) {
//                if (subElements[j] == null)
//                    continue;
//                mc = subElements[j].getComponent();
//                if(!mc.isShowing())
//                    continue;
//                if(mc instanceof JComponent) {
//                    cWidth  = ((JComponent)mc).getWidth();
//                    cHeight = ((JComponent)mc).getHeight();
//                } else {
//                    r2 = mc.getBounds();
//                    cWidth  = r2.width;
//                    cHeight = r2.height;
//                }
//                p.x = screenX;
//                p.y = screenY;
//                SwingUtilities.convertPointFromScreen(p,mc);
//
//                /** Return the deepest component on the selection
//                 *  path in whose bounds the event's point occurs
//                 */
//                if (p.x >= 0 && p.x < cWidth && p.y >= 0 && p.y < cHeight) {
//                    return mc;
//                }
//            }
//        }
        return null;
    }

    /**
     * When a MenuElement receives an event from a KeyListener, it should never process the event
     * directly. Instead all MenuElements should call this method with the event.
     *
     * @param e  a KeyEvent object
     */
    public void processKeyEvent(KeyEvent e) {
//        MenuElement[] sel2 = new MenuElement[0];
//        sel2 = (MenuElement[])selection.toArray(sel2);
//        int selSize = sel2.length;
//        MenuElement[] path;
//
//        if (selSize < 1) {
//            return;
//        }
//
//        for (int i=selSize-1; i>=0; i--) {
//            MenuElement elem = sel2[i];
//            MenuElement[] subs = elem.getSubElements();
//            path = null;
//
//            for (int j=0; j<subs.length; j++) {
//                if (subs[j] == null || !subs[j].getComponent().isShowing()
//                    || !subs[j].getComponent().isEnabled()) {
//                    continue;
//                }
//
//                if(path == null) {
//                    path = new MenuElement[i+2];
//                    System.arraycopy(sel2, 0, path, 0, i+1);
//                    }
//                path[i+1] = subs[j];
//                subs[j].processKeyEvent(e, path, this);
//                if (e.isConsumed()) {
//                    return;
//            }
//        }
//    }
//
//        // finally dispatch event to the first component in path
//        path = new MenuElement[1];
//        path[0] = sel2[0];
//        path[0].processKeyEvent(e, path, this);
//        if (e.isConsumed()) {
//            return;
//        }
    }

    /**
     * Return true if c is part of the currently used menu
     */
    public boolean isComponentPartOfCurrentMenu(Component c) {
        if(selection.size() > 0) {
            MenuElement me = (MenuElement)selection.get(0);
            return isComponentPartOfCurrentMenu(me,c);
        } else
            return false;
    }

    private boolean isComponentPartOfCurrentMenu(MenuElement root,Component c) {
        MenuElement children[];
        int i,d;

        if (root == null)
            return false;

        if(root.getComponent() == c)
            return true;
        else {
            children = root.getSubElements();
            for(i=0,d=children.length;i<d;i++) {
                if(isComponentPartOfCurrentMenu(children[i],c))
                    return true;
            }
        }
        return false;
    }
    
    
    
}
