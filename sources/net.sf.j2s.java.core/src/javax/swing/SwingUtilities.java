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

import java.applet.JSApplet;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.JSFrame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.JSComponent;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.event.MenuDragMouseEvent;
import javax.swing.plaf.UIResource;
import javax.swing.text.View;

import sun.awt.AppContext;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;
import swingjs.JSAppletViewer;
import swingjs.JSToolkit;
import swingjs.api.Interface;
import swingjs.api.js.DOMNode;
import swingjs.plaf.JSComponentUI;

/**
 * A collection of utility methods for Swing.
 *
 * @author unknown
 */
@SuppressWarnings({"rawtypes", "deprecation"})
public class SwingUtilities implements SwingConstants
{
    // These states are system-wide, rather than AppContext wide.
//    private static boolean canAccessEventQueue = false;
//    private static boolean eventQueueTested = false;

//    /**
//     * Indicates if we should change the drop target when a
//     * {@code TransferHandler} is set.
//     */
//    private static boolean suppressDropSupport;
//
//    /**
//     * Indiciates if we've checked the system property for suppressing
//     * drop support.
//     */
//    private static boolean checkedSuppressDropSupport;
//
    /**
     * Installs a {@code DropTarget} on the component as necessary for a
     * {@code TransferHandler} change.
     */
    static void installSwingDropTargetAsNecessary(Component c,
                                                         TransferHandler t) {

  //      if (!getSuppressDropTarget()) {
            DropTarget dropHandler = c.getDropTarget();
            if ((dropHandler == null) || (dropHandler instanceof UIResource)) {
                if (t == null) {
                    c.setDropTarget(null);
                } else if (!GraphicsEnvironment.isHeadless()) {
                    c.setDropTarget(new TransferHandler.SwingDropTarget(c));
                }
            }
//        }
    }

    /**
     * Return true if <code>a</code> contains <code>b</code>
     */
    public static final boolean isRectangleContainingRectangle(Rectangle a,Rectangle b) {
        if (b.x >= a.x && (b.x + b.width) <= (a.x + a.width) &&
            b.y >= a.y && (b.y + b.height) <= (a.y + a.height)) {
            return true;
        }
        return false;
    }

    /**
     * Return the rectangle (0,0,bounds.width,bounds.height) for the component <code>aComponent</code>
     */
    public static Rectangle getLocalBounds(Component aComponent) {
        Rectangle b = new Rectangle(aComponent.getBounds());
        b.x = b.y = 0;
        return b;
    }


    /**
     * Returns the first <code>Window </code> ancestor of <code>c</code>, or
     * {@code null} if <code>c</code> is not contained inside a <code>Window</code>.
     *
     * @param c <code>Component</code> to get <code>Window</code> ancestor
     *        of.
     * @return the first <code>Window </code> ancestor of <code>c</code>, or
     *         {@code null} if <code>c</code> is not contained inside a
     *         <code>Window</code>.
     * @since 1.3
     */
    public static Window getWindowAncestor(Component c) {
        for(Container p = c.getParent(); p != null; p = p.getParent()) {
            if (p.isWindowOrJSApplet()) {
                return (Window)p;
            }
        }
        return null;
    }

    /**
     * Converts the location <code>x</code> <code>y</code> to the
     * parents coordinate system, returning the location.
     */
    static Point convertScreenLocationToParent(Container parent,int x, int y) {
        for (Container p = parent; p != null; p = p.getParent()) {
            if (p != null && p.isWindowOrJSApplet()) {
                Point point = new Point(x, y);

                SwingUtilities.convertPointFromScreen(point, parent);
                return point;
            }
        }
        throw new Error("convertScreenLocationToParent: no window ancestor");
    }

    /**
     * Convert a <code>aPoint</code> in <code>source</code> coordinate system to
     * <code>destination</code> coordinate system.
     * If <code>source</code> is {@code null}, <code>aPoint</code> is assumed to be in <code>destination</code>'s
     * root component coordinate system.
     * If <code>destination</code> is {@code null}, <code>aPoint</code> will be converted to <code>source</code>'s
     * root component coordinate system.
     * If both <code>source</code> and <code>destination</code> are {@code null}, return <code>aPoint</code>
     * without any conversion.
     */
    public static Point convertPoint(Component source,Point aPoint,Component destination) {
        Point p;

        if(source == null && destination == null)
            return aPoint;
        if(source == null) {
            source = getWindowAncestor(destination);
            if(source == null)
                throw new Error("Source component not connected to component tree hierarchy");
        }
        p = new Point(aPoint);
        convertPointToScreen(p,source);
        if(destination == null) {
            destination = getWindowAncestor(source);
            if(destination == null)
                throw new Error("Destination component not connected to component tree hierarchy");
        }
        convertPointFromScreen(p,destination);
        return p;
    }

    /**
     * Convert the point <code>(x,y)</code> in <code>source</code> coordinate system to
     * <code>destination</code> coordinate system.
     * If <code>source</code> is {@code null}, <code>(x,y)</code> is assumed to be in <code>destination</code>'s
     * root component coordinate system.
     * If <code>destination</code> is {@code null}, <code>(x,y)</code> will be converted to <code>source</code>'s
     * root component coordinate system.
     * If both <code>source</code> and <code>destination</code> are {@code null}, return <code>(x,y)</code>
     * without any conversion.
     */
    public static Point convertPoint(Component source,int x, int y,Component destination) {
        Point point = new Point(x,y);
        return convertPoint(source,point,destination);
    }

    /**
     * Convert the rectangle <code>aRectangle</code> in <code>source</code> coordinate system to
     * <code>destination</code> coordinate system.
     * If <code>source</code> is {@code null}, <code>aRectangle</code> is assumed to be in <code>destination</code>'s
     * root component coordinate system.
     * If <code>destination</code> is {@code null}, <code>aRectangle</code> will be converted to <code>source</code>'s
     * root component coordinate system.
     * If both <code>source</code> and <code>destination</code> are {@code null}, return <code>aRectangle</code>
     * without any conversion.
     */
    public static Rectangle convertRectangle(Component source,Rectangle aRectangle,Component destination) {
        Point point = new Point(aRectangle.x,aRectangle.y);
        point =  convertPoint(source,point,destination);
        return new Rectangle(point.x,point.y,aRectangle.width,aRectangle.height);
    }

    /**
     * Convenience method for searching above <code>comp</code> in the
     * component hierarchy and returns the first object of class <code>c</code> it
     * finds. Can return {@code null}, if a class <code>c</code> cannot be found.
     */
    public static Container getAncestorOfClass(Class<?> c, Component comp)
    {
        if(comp == null || c == null)
            return null;

        Container parent = comp.getParent();
        while(parent != null && !(c.isInstance(parent)))
            parent = parent.getParent();
        return parent;
    }

    /**
     * Convenience method for searching above <code>comp</code> in the
     * component hierarchy and returns the first object of <code>name</code> it
     * finds. Can return {@code null}, if <code>name</code> cannot be found.
     */
    public static Container getAncestorNamed(String name, Component comp) {
        if(comp == null || name == null)
            return null;

        Container parent = comp.getParent();
        while(parent != null && !(name.equals(parent.getName())))
            parent = parent.getParent();
        return parent;
    }

    /**
     * Returns the deepest visible descendent Component of <code>parent</code>
     * that contains the location <code>x</code>, <code>y</code>.
     * If <code>parent</code> does not contain the specified location,
     * then <code>null</code> is returned.  If <code>parent</code> is not a
     * container, or none of <code>parent</code>'s visible descendents
     * contain the specified location, <code>parent</code> is returned.
     *
     * @param parent the root component to begin the search
     * @param x the x target location
     * @param y the y target location
     */
    public static Component getDeepestComponentAt(Component parent, int x, int y) {
        if (!parent.contains(x, y)) {
            return null;
        }
        if (parent instanceof Container) {
            Component components[] = JSComponent.秘getChildArray((Container)parent);
            for (int i = 0, n = ((Container) parent).getComponentCount() ; i < n ; i++) {
                Component comp = components[i];
                if (comp != null && comp.isVisible()) {
                    Point loc = comp.getLocation();
                    if (comp instanceof Container) {
                        comp = getDeepestComponentAt(comp, x - loc.x, y - loc.y);
                    } else {
                        comp = comp.getComponentAt(x - loc.x, y - loc.y);
                    }
                    if (comp != null && comp.isVisible()) {
                        return comp;
                    }
                }
            }
        }
        return parent;
    }


    /**
     * Returns a MouseEvent similar to <code>sourceEvent</code> except that its x
     * and y members have been converted to <code>destination</code>'s coordinate
     * system.  If <code>source</code> is {@code null}, <code>sourceEvent</code> x and y members
     * are assumed to be into <code>destination</code>'s root component coordinate system.
     * If <code>destination</code> is <code>null</code>, the
     * returned MouseEvent will be in <code>source</code>'s coordinate system.
     * <code>sourceEvent</code> will not be changed. A new event is returned.
     * the <code>source</code> field of the returned event will be set
     * to <code>destination</code> if destination is non-{@code null}
     * use the translateMouseEvent() method to translate a mouse event from
     * one component to another without changing the source.
     */
    public static MouseEvent convertMouseEvent(Component source,
                                               MouseEvent sourceEvent,
                                               Component destination) {
        Point p = convertPoint(source,new Point(sourceEvent.getX(),
                                                sourceEvent.getY()),
                               destination);
        Component newSource;

        if(destination != null)
            newSource = destination;
        else
            newSource = source;

        MouseEvent newEvent;
        if (sourceEvent instanceof MouseWheelEvent) {
            MouseWheelEvent sourceWheelEvent = (MouseWheelEvent)sourceEvent;
            newEvent = new MouseWheelEvent(newSource,
                                           sourceWheelEvent.getID(),
                                           sourceWheelEvent.getWhen(),
                                           sourceWheelEvent.getModifiers(),
                                           p.x,p.y,
                                           sourceWheelEvent.getXOnScreen(),
                                           sourceWheelEvent.getYOnScreen(),
                                           sourceWheelEvent.getClickCount(),
                                           sourceWheelEvent.isPopupTrigger(),
                                           sourceWheelEvent.getScrollType(),
                                           sourceWheelEvent.getScrollAmount(),
                                           sourceWheelEvent.getWheelRotation());
        }
        else if (sourceEvent instanceof MenuDragMouseEvent) {
            MenuDragMouseEvent sourceMenuDragEvent = (MenuDragMouseEvent)sourceEvent;
            newEvent = new MenuDragMouseEvent(newSource,
                                              sourceMenuDragEvent.getID(),
                                              sourceMenuDragEvent.getWhen(),
                                              sourceMenuDragEvent.getModifiers(),
                                              p.x,p.y,
                                              sourceMenuDragEvent.getXOnScreen(),
                                              sourceMenuDragEvent.getYOnScreen(),
                                              sourceMenuDragEvent.getClickCount(),
                                              sourceMenuDragEvent.isPopupTrigger(),
                                              sourceMenuDragEvent.getPath(),
                                              sourceMenuDragEvent.getMenuSelectionManager());
        }
        else {
            newEvent = new MouseEvent(newSource,
                                      sourceEvent.getID(),
                                      sourceEvent.getWhen(),
                                      sourceEvent.getModifiers(),
                                      p.x,p.y,
                                      sourceEvent.getXOnScreen(),
                                      sourceEvent.getYOnScreen(),
                                      sourceEvent.getClickCount(),
                                      sourceEvent.isPopupTrigger(),
                                      MouseEvent.NOBUTTON );
        }
        return newEvent;
    }


    /**
     * Convert a point from a component's coordinate system to
     * screen coordinates.
     *
     * @param p  a Point object (converted to the new coordinate system)
     * @param c  a Component object
     */
    public static void convertPointToScreen(Point p,Component c) {
//            Rectangle b;
            int x,y;
            boolean done = false;
            do {
                if((done = c.isWindowOrJSApplet())) {
                    try {
                        Point pp = c.getLocationOnScreen();
                        x = pp.x;
                        y = pp.y;
                    } catch (IllegalComponentStateException icse) {
                        x = c.getX();
                        y = c.getY();
                    }
                } else {
                    x = c.getX();
                    y = c.getY();
                }

                p.x += x;
                p.y += y;
                c = c.getParent();
            } while(c != null && !done);
        }

    /**
     * Convert a point from a screen coordinates to a component's
     * coordinate system
     *
     * @param p  a Point object (converted to the new coordinate system)
     * @param c  a Component object
     */
    public static void convertPointFromScreen(Point p,Component c) {
//        Rectangle b;
        int x,y;

        do {
            if(c.isWindowOrJSApplet()) {
                try {
                    Point pp = c.getLocationOnScreen();
                    x = pp.x;
                    y = pp.y;
                } catch (IllegalComponentStateException icse) {
                    x = c.getX();
                    y = c.getY();
                }
            }  else {
                x = c.getX();
                y = c.getY();
            }

            p.x -= x;
            p.y -= y;

            if(c.isWindowOrJSApplet())
                break;
            c = c.getParent();
        } while(c != null);
    }

    /**
     * Returns the first <code>Window </code> ancestor of <code>c</code>, or
     * {@code null} if <code>c</code> is not contained inside a <code>Window</code>.
     * <p>
     * Note: This method provides the same functionality as
     * <code>getWindowAncestor</code>.
     *
     * @param c <code>Component</code> to get <code>Window</code> ancestor
     *        of.
     * @return the first <code>Window </code> ancestor of <code>c</code>, or
     *         {@code null} if <code>c</code> is not contained inside a
     *         <code>Window</code>.
     */
    public static Window windowForComponent(Component c) {
        return getWindowAncestor(c);
    }

    /**
     * Return <code>true</code> if a component <code>a</code> descends from a component <code>b</code>
     */
    public static boolean isDescendingFrom(Component a,Component b) {
        if(a == b)
            return true;
        for(Container p = a.getParent();p!=null;p=p.getParent())
            if(p == b)
                return true;
        return false;
    }


    /**
     * Convenience to calculate the intersection of two rectangles
     * without allocating a new rectangle.
     * If the two rectangles don't intersect,
     * then the returned rectangle begins at (0,0)
     * and has zero width and height.
     *
     * @param x       the X coordinate of the first rectangle's top-left point
     * @param y       the Y coordinate of the first rectangle's top-left point
     * @param width   the width of the first rectangle
     * @param height  the height of the first rectangle
     * @param dest    the second rectangle
     *
     * @return <code>dest</code>, modified to specify the intersection
     */
    public static Rectangle computeIntersection(int x,int y,int width,int height,Rectangle dest) {
        int x1 = (x > dest.x) ? x : dest.x;
        int x2 = ((x+width) < (dest.x + dest.width)) ? (x+width) : (dest.x + dest.width);
        int y1 = (y > dest.y) ? y : dest.y;
        int y2 = ((y + height) < (dest.y + dest.height) ? (y+height) : (dest.y + dest.height));

        dest.x = x1;
        dest.y = y1;
        dest.width = x2 - x1;
        dest.height = y2 - y1;

        // If rectangles don't intersect, return zero'd intersection.
        if (dest.width < 0 || dest.height < 0) {
            dest.x = dest.y = dest.width = dest.height = 0;
        }

        return dest;
    }

    /**
     * Convenience method that calculates the union of two rectangles
     * without allocating a new rectangle.
     *
     * @param x the x-coordinate of the first rectangle
     * @param y the y-coordinate of the first rectangle
     * @param width the width of the first rectangle
     * @param height the height of the first rectangle
     * @param dest  the coordinates of the second rectangle; the union
     *    of the two rectangles is returned in this rectangle
     * @return the <code>dest</code> <code>Rectangle</code>
     */
    public static Rectangle computeUnion(int x,int y,int width,int height,Rectangle dest) {
        int x1 = (x < dest.x) ? x : dest.x;
        int x2 = ((x+width) > (dest.x + dest.width)) ? (x+width) : (dest.x + dest.width);
        int y1 = (y < dest.y) ? y : dest.y;
        int y2 = ((y+height) > (dest.y + dest.height)) ? (y+height) : (dest.y + dest.height);

        dest.x = x1;
        dest.y = y1;
        dest.width = (x2 - x1);
        dest.height= (y2 - y1);
        return dest;
    }

    /**
     * Convenience returning an array of rect representing the regions within
     * <code>rectA</code> that do not overlap with <code>rectB</code>. If the
     * two Rects do not overlap, returns an empty array
     */
    public static Rectangle[] computeDifference(Rectangle rectA,Rectangle rectB) {
        if (rectB == null || !rectA.intersects(rectB) || isRectangleContainingRectangle(rectB,rectA)) {
            return new Rectangle[0];
        }

        Rectangle t = new Rectangle();
        Rectangle a=null,b=null,c=null,d=null;
        Rectangle result[];
        int rectCount = 0;

        
        /* rectA contains rectB */
        if (isRectangleContainingRectangle(rectA,rectB)) {
            t.x = rectA.x; t.y = rectA.y; t.width = rectB.x - rectA.x; t.height = rectA.height;
            if(t.width > 0 && t.height > 0) {
                a = new Rectangle(t);
                rectCount++;
            }

            t.x = rectB.x; t.y = rectA.y; t.width = rectB.width; t.height = rectB.y - rectA.y;
            if(t.width > 0 && t.height > 0) {
                b = new Rectangle(t);
                rectCount++;
            }

            t.x = rectB.x; t.y = rectB.y + rectB.height; t.width = rectB.width;
            t.height = rectA.y + rectA.height - (rectB.y + rectB.height);
            if(t.width > 0 && t.height > 0) {
                c = new Rectangle(t);
                rectCount++;
            }

            t.x = rectB.x + rectB.width; t.y = rectA.y; t.width = rectA.x + rectA.width - (rectB.x + rectB.width);
            t.height = rectA.height;
            if(t.width > 0 && t.height > 0) {
                d = new Rectangle(t);
                rectCount++;
            }
        } else {
            /* 1 */
            if (rectB.x <= rectA.x && rectB.y <= rectA.y) {
                if ((rectB.x + rectB.width) > (rectA.x + rectA.width)) {

                    t.x = rectA.x; t.y = rectB.y + rectB.height;
                    t.width = rectA.width; t.height = rectA.y + rectA.height - (rectB.y + rectB.height);
                    if(t.width > 0 && t.height > 0) {
                        a = t;
                        rectCount++;
                    }
                } else if ((rectB.y + rectB.height) > (rectA.y + rectA.height)) {
                    t.setBounds((rectB.x + rectB.width), rectA.y,
                                (rectA.x + rectA.width) - (rectB.x + rectB.width), rectA.height);
                    if(t.width > 0 && t.height > 0) {
                        a = t;
                        rectCount++;
                    }
                } else {
                    t.setBounds((rectB.x + rectB.width), rectA.y,
                                (rectA.x + rectA.width) - (rectB.x + rectB.width),
                                (rectB.y + rectB.height) - rectA.y);
                    if(t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }

                    t.setBounds(rectA.x, (rectB.y + rectB.height), rectA.width,
                                (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if(t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                }
            } else if (rectB.x <= rectA.x && (rectB.y + rectB.height) >= (rectA.y + rectA.height)) {
                if ((rectB.x + rectB.width) > (rectA.x + rectA.width)) {
                    t.setBounds(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if(t.width > 0 && t.height > 0) {
                        a = t;
                        rectCount++;
                    }
                } else {
                    t.setBounds(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if(t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.reshape((rectB.x + rectB.width), rectB.y,
                                (rectA.x + rectA.width) - (rectB.x + rectB.width),
                                (rectA.y + rectA.height) - rectB.y);
                    if(t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                }
            } else if (rectB.x <= rectA.x) {
                if ((rectB.x + rectB.width) >= (rectA.x + rectA.width)) {
                    t.reshape(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if(t.width>0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }

                    t.reshape(rectA.x, (rectB.y + rectB.height), rectA.width,
                                (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if(t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                } else {
                    t.reshape(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if(t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }

                    t.reshape((rectB.x + rectB.width), rectB.y,
                                (rectA.x + rectA.width) - (rectB.x + rectB.width),
                                rectB.height);
                    if(t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }

                    t.reshape(rectA.x, (rectB.y + rectB.height), rectA.width,
                                (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if(t.width > 0 && t.height > 0) {
                        c = new Rectangle(t);
                        rectCount++;
                    }
                }
            } else if (rectB.x <= (rectA.x + rectA.width) && (rectB.x + rectB.width) > (rectA.x + rectA.width)) {
                if (rectB.y <= rectA.y && (rectB.y + rectB.height) > (rectA.y + rectA.height)) {
                    t.reshape(rectA.x, rectA.y, rectB.x - rectA.x, rectA.height);
                    if(t.width > 0 && t.height > 0) {
                        a = t;
                        rectCount++;
                    }
                } else if (rectB.y <= rectA.y) {
                    t.reshape(rectA.x, rectA.y, rectB.x - rectA.x,
                                (rectB.y + rectB.height) - rectA.y);
                    if(t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }

                    t.reshape(rectA.x, (rectB.y + rectB.height), rectA.width,
                                (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if(t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                } else if ((rectB.y + rectB.height) > (rectA.y + rectA.height)) {
                    t.reshape(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if(t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }

                    t.reshape(rectA.x, rectB.y, rectB.x - rectA.x,
                                (rectA.y + rectA.height) - rectB.y);
                    if(t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                } else {
                    t.reshape(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if(t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }

                    t.reshape(rectA.x, rectB.y, rectB.x - rectA.x,
                                rectB.height);
                    if(t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }

                    t.reshape(rectA.x, (rectB.y + rectB.height), rectA.width,
                                (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if(t.width > 0 && t.height > 0) {
                        c = new Rectangle(t);
                        rectCount++;
                    }
                }
            } else if (rectB.x >= rectA.x && (rectB.x + rectB.width) <= (rectA.x + rectA.width)) {
                if (rectB.y <= rectA.y && (rectB.y + rectB.height) > (rectA.y + rectA.height)) {
                    t.reshape(rectA.x, rectA.y, rectB.x - rectA.x, rectA.height);
                    if(t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.reshape((rectB.x + rectB.width), rectA.y,
                                (rectA.x + rectA.width) - (rectB.x + rectB.width), rectA.height);
                    if(t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                } else if (rectB.y <= rectA.y) {
                    t.reshape(rectA.x, rectA.y, rectB.x - rectA.x, rectA.height);
                    if(t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }

                    t.reshape(rectB.x, (rectB.y + rectB.height),
                                rectB.width,
                                (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if(t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }

                    t.reshape((rectB.x + rectB.width), rectA.y,
                                (rectA.x + rectA.width) - (rectB.x + rectB.width), rectA.height);
                    if(t.width > 0 && t.height > 0) {
                        c = new Rectangle(t);
                        rectCount++;
                    }
                } else {
                    t.reshape(rectA.x, rectA.y, rectB.x - rectA.x, rectA.height);
                    if(t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }

                    t.reshape(rectB.x, rectA.y, rectB.width,
                                rectB.y - rectA.y);
                    if(t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }

                    t.reshape((rectB.x + rectB.width), rectA.y,
                                (rectA.x + rectA.width) - (rectB.x + rectB.width), rectA.height);
                    if(t.width > 0 && t.height > 0) {
                        c = new Rectangle(t);
                        rectCount++;
                    }
                }
            }
        }

        result = new Rectangle[rectCount];
        rectCount = 0;
        if(a != null)
            result[rectCount++] = a;
        if(b != null)
            result[rectCount++] = b;
        if(c != null)
            result[rectCount++] = c;
        if(d != null)
            result[rectCount++] = d;
        return result;
    }

    /**
     * Returns true if the mouse event specifies the left mouse button.
     *
     * @param anEvent  a MouseEvent object
     * @return true if the left mouse button was active
     */
    public static boolean isLeftMouseButton(MouseEvent anEvent) {
         return ((anEvent.getModifiers() & InputEvent.BUTTON1_MASK) != 0);
    }

    /**
     * Returns true if the mouse event specifies the middle mouse button.
     *
     * @param anEvent  a MouseEvent object
     * @return true if the middle mouse button was active
     */
    public static boolean isMiddleMouseButton(MouseEvent anEvent) {
        return ((anEvent.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK);
    }

    /**
     * Returns true if the mouse event specifies the right mouse button.
     *
     * @param anEvent  a MouseEvent object
     * @return true if the right mouse button was active
     */
    public static boolean isRightMouseButton(MouseEvent anEvent) {
        return ((anEvent.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK);
    }

//    /**
//     * Compute the width of the string using a font with the specified
//     * "metrics" (sizes).
//     *
//     * @param fm   a FontMetrics object to compute with
//     * @param str  the String to compute
//     * @return an int containing the string width
//     */
//    public static int computeStringWidth(FontMetrics fm,String str) {
//        // You can't assume that a string's width is the sum of its
//        // characters' widths in Java2D -- it may be smaller due to
//        // kerning, etc.
//        return SwingUtilities2.stringWidth(null, fm, str);
//    }
//
    /**
     * Compute and return the location of the icons origin, the
     * location of origin of the text baseline, and a possibly clipped
     * version of the compound labels string.  Locations are computed
     * relative to the viewR rectangle.
     * The JComponents orientation (LEADING/TRAILING) will also be taken
     * into account and translated into LEFT/RIGHT values accordingly.
     */
    public static String layoutCompoundLabel(JComponent c,
                                             FontMetrics fm,
                                             String text,
                                             Icon icon,
                                             int verticalAlignment,
                                             int horizontalAlignment,
                                             int verticalTextPosition,
                                             int horizontalTextPosition,
                                             Rectangle viewR,
                                             Rectangle iconR,
                                             Rectangle textR,
                                             int textIconGap)
    {
    	
    	JSComponentUI ui = c.秘getUI();

        boolean ltr = (c == null || c.getComponentOrientation().isLeftToRight());
        int hAlign, hTextPos;

		if (ui.menuAnchorNode == null) {
	        // Translate LEADING/TRAILING values in horizontalAlignment
	        // to LEFT/RIGHT values depending on the components orientation
	        switch (horizontalAlignment) {
	        case LEADING:
	            hAlign = (ltr) ? LEFT : RIGHT;
	            break;
	        case TRAILING:
	            hAlign = (ltr) ? RIGHT : LEFT;
	            break;
	        default:
				hAlign = horizontalAlignment;
	        	break;
	        }
	        switch (horizontalTextPosition) {
	        case LEADING:
	            hTextPos = (ltr) ? LEFT : RIGHT;
	            break;
	        case TRAILING:
	            hTextPos = (ltr) ? RIGHT : LEFT;
	            break;
	        default:
		        hTextPos = horizontalTextPosition;
	        	break;
	        }
		} else {
			// menus are far simpler!
			hAlign = hTextPos = (ltr ? LEFT : RIGHT);
		}
    	
        return layoutCompoundLabelImpl(c, ui,
                                       fm,
                                       text,
                                       icon,
                                       verticalAlignment,
                                       hAlign,
                                       verticalTextPosition,
                                       hTextPos,
                                       viewR,
                                       iconR,
                                       textR,
                                       textIconGap);
    }

    /**
     * Compute and return the location of the icons origin, the
     * location of origin of the text baseline, and a possibly clipped
     * version of the compound labels string.  Locations are computed
     * relative to the viewR rectangle.
     * This layoutCompoundLabel() does not know how to handle LEADING/TRAILING
     * values in horizontalTextPosition (they will default to RIGHT) and in
     * horizontalAlignment (they will default to CENTER).
     * Use the other version of layoutCompoundLabel() instead.
     */
    public static String layoutCompoundLabel(
        FontMetrics fm,
        String text,
        Icon icon,
        int verticalAlignment,
        int horizontalAlignment,
        int verticalTextPosition,
        int horizontalTextPosition,
        Rectangle viewR,
        Rectangle iconR,
        Rectangle textR,
        int textIconGap)
    {
        return layoutCompoundLabelImpl(null, null, fm, text, icon,
                                       verticalAlignment,
                                       horizontalAlignment,
                                       verticalTextPosition,
                                       horizontalTextPosition,
                                       viewR, iconR, textR, textIconGap);
    }

    /**
     * Compute and return the location of the icons origin, the
     * location of origin of the text baseline, and a possibly clipped
     * version of the compound labels string.  Locations are computed
     * relative to the viewR rectangle.
     * This layoutCompoundLabel() does not know how to handle LEADING/TRAILING
     * values in horizontalTextPosition (they will default to RIGHT) and in
     * horizontalAlignment (they will default to CENTER).
     * Use the other version of layoutCompoundLabel() instead.
     */
    @SuppressWarnings("unused")
	private static String layoutCompoundLabelImpl(
        JComponent c,
        JSComponentUI ui,
        FontMetrics fm,
        String text,
        Icon icon,
        int verticalAlignment,
        int horizontalAlignment,
        int verticalTextPosition,
        int horizontalTextPosition,
        Rectangle viewR,
        Rectangle iconR,
        Rectangle textR,
        int textIconGap)
    {
        /* Initialize the icon bounds rectangle iconR.
         */

        if (icon != null) {
            iconR.width = icon.getIconWidth();
            iconR.height = icon.getIconHeight();
//        }
//        else {
           // SwingJS -- we allow passing in the radio button 
//            iconR.width = iconR.height = 0;
        }

        /* Initialize the text bounds rectangle textR.  If a null
         * or and empty String was specified we substitute "" here
         * and use 0,0,0,0 for textR.
         */

        boolean textIsEmpty = (text == null) || text.equals("");
        int lsb = 0;
        int rsb = 0;
        /* Unless both text and icon are non-null, we effectively ignore
         * the value of textIconGap.
         */
        int gap;

        View v = null;
        if (textIsEmpty) {
            textR.width = textR.height = 0;
            text = "";
            gap = 0;
        }
        else {
            int availTextWidth;
            gap = (iconR.width == 0) ? 0 : textIconGap;

            if (horizontalTextPosition == CENTER) {
                availTextWidth = viewR.width;
            }
            else {
                availTextWidth = viewR.width - (iconR.width + gap);
            }
            v = (c != null ? (View) c.getClientProperty("html") : null);
            if (v != null) {
                textR.width = Math.min(availTextWidth,
                                       (int) v.getPreferredSpan(View.X_AXIS));
                textR.height = (int) v.getPreferredSpan(View.Y_AXIS);
            } else {
            	if (ui.textNode == null) {
            		textR.width = fm.stringWidth(text);
            		textR.height = fm.getHeight();
            	} else if (text.length() == 0) {
            		textR.width = 1;
            		textR.height = fm.getHeight();
            	} else {
            		String t = text.replace(' ', '\u00A0'); // no-break space
            		if (t != text && /** @j2sNative ui.textNode.innerHTML != t || */false)
            			DOMNode.setAttr(ui.textNode, "innerHTML", t);
            		Dimension d = ui.getHTMLSize(ui.textNode);
//            		if (t != text)
//            			DOMNode.setAttr(ui.textNode, "innerHTML", text);            			
            		textR.width = (d.width == 0 ? 1 : d.width);
            		textR.height = (d.height == 0 ? fm.getHeight() : d.height);
            	}
            	// Take into account the left and right side bearings.
                // This gives more space than it is actually needed,
                // but there are two reasons:
                // 1. If we set the width to the actual bounds,
                //    all callers would have to account for the bearings
                //    themselves. NOTE: all pref size calculations don't do it.
                // 2. You can do a drawString at the returned location
                //    and the text won't be clipped.
// SwingJS                lsb = SwingUtilities2.getLeftSideBearing(c, fm, text);
//                if (lsb < 0) {
//                    textR.width -= lsb;
//                }
//                rsb = SwingUtilities2.getRightSideBearing(c, fm, text);
//                if (rsb > 0) {
//                    textR.width += rsb;
//                }

                if (textR.width > availTextWidth) {
                    text = SwingUtilities2.clipString(c, fm, text,
                                                      availTextWidth);
                    textR.width = SwingUtilities2.stringWidth(c, fm, text);
                }
//                textR.height = fm.getHeight();
            }
        }


        /* Compute textR.x,y given the verticalTextPosition and
         * horizontalTextPosition properties
         */

        if (verticalTextPosition == TOP) {
            if (horizontalTextPosition != CENTER) {
                textR.y = 0;
//                textR.y = iconR.height  - textR.height;//(int) (4 * textR.height/54f);//- textR.height;
            }
            else {
                textR.y = -(textR.height + gap);
            }
        }
        else if (verticalTextPosition == CENTER) { // 16 pt max; height is 18, ascent is 12, actually
            textR.y = (iconR.height/2) - (textR.height / 2);
        }
        else { // (verticalTextPosition == BOTTOM)
            if (horizontalTextPosition != CENTER) {
                textR.y = iconR.height - textR.height;
            }
            else {
                textR.y = (iconR.height + gap);
            }
        }

        if (horizontalTextPosition == LEFT) {
            textR.x = -(textR.width + gap);
        }
        else if (horizontalTextPosition == CENTER) {
            textR.x = (iconR.width / 2) - (textR.width / 2);
        }
        else { // (horizontalTextPosition == RIGHT)
            textR.x = (iconR.width + gap);
        }

        // WARNING: DefaultTreeCellEditor uses a shortened version of
        // this algorithm to position it's Icon. If you change how this
        // is calculated, be sure and update DefaultTreeCellEditor too.

        /* labelR is the rectangle that contains iconR and textR.
         * Move it to its proper position given the labelAlignment
         * properties.
         *
         * To avoid actually allocating a Rectangle, Rectangle.union
         * has been inlined below.
         */
        int labelR_x = Math.min(iconR.x, textR.x);
        int labelR_width = Math.max(iconR.x + iconR.width,
                                    textR.x + textR.width) - labelR_x;
        int labelR_y = Math.min(iconR.y, textR.y);
        int labelR_height = Math.max(iconR.y + iconR.height,
                                     textR.y + textR.height) - labelR_y;

        int dx, dy;

        if (verticalAlignment == TOP) {
            dy = viewR.y - labelR_y;
        }
        else if (verticalAlignment == CENTER) {
            dy = (viewR.y + (viewR.height / 2)) - (labelR_y + (labelR_height / 2));
        }
        else { // (verticalAlignment == BOTTOM)
            dy = (viewR.y + viewR.height) - (labelR_y + labelR_height);
        }

        if (horizontalAlignment == LEFT) {
            dx = viewR.x - labelR_x;
        }
        else if (horizontalAlignment == RIGHT) {
            dx = (viewR.x + viewR.width) - (labelR_x + labelR_width);
        }
        else { // (horizontalAlignment == CENTER)
            dx = (viewR.x + (viewR.width / 2)) -
                 (labelR_x + (labelR_width / 2));
        }

        /* Translate textR and glypyR by dx,dy.
         */

        textR.x += dx;
        textR.y += dy;

        iconR.x += dx;
        iconR.y += dy;

//SwingJS        if (lsb < 0) {
//            // lsb is negative. Shift the x location so that the text is
//            // visually drawn at the right location.
//            textR.x -= lsb;
//
//            textR.width += lsb;
//        }
//        if (rsb > 0) {
//            textR.width -= rsb;
//        }

        if (viewR.width == Short.MAX_VALUE) {
        	// SwingJS, for JSGraphicsUtil setting preferred button size;
        	viewR.width = labelR_width;
        	viewR.height = labelR_height;
        }        
        return text;
    }


    /**
     * Paints a component to the specified <code>Graphics</code>.
     * This method is primarily useful to render
     * <code>Component</code>s that don't exist as part of the visible
     * containment hierarchy, but are used for rendering.  For
     * example, if you are doing your own rendering and want to render
     * some text (or even HTML), you could make use of
     * <code>JLabel</code>'s text rendering support and have it paint
     * directly by way of this method, without adding the label to the
     * visible containment hierarchy.
     * <p>
     * This method makes use of <code>CellRendererPane</code> to handle
     * the actual painting, and is only recommended if you use one
     * component for rendering.  If you make use of multiple components
     * to handle the rendering, as <code>JTable</code> does, use
     * <code>CellRendererPane</code> directly.  Otherwise, as described
     * below, you could end up with a <code>CellRendererPane</code>
     * per <code>Component</code>.
     * <p>
     * If <code>c</code>'s parent is not a <code>CellRendererPane</code>,
     * a new <code>CellRendererPane</code> is created, <code>c</code> is
     * added to it, and the <code>CellRendererPane</code> is added to
     * <code>p</code>.  If <code>c</code>'s parent is a
     * <code>CellRendererPane</code> and the <code>CellRendererPane</code>s
     * parent is not <code>p</code>, it is added to <code>p</code>.
     * <p>
     * The component should either descend from <code>JComponent</code>
     * or be another kind of lightweight component.
     * A lightweight component is one whose "lightweight" property
     * (returned by the <code>Component</code>
     * <code>isLightweight</code> method)
     * is true. If the Component is not lightweight, bad things map happen:
     * crashes, exceptions, painting problems...
     *
     * @param g  the <code>Graphics</code> object to draw on
     * @param c  the <code>Component</code> to draw
     * @param p  the intermediate <code>Container</code>
     * @param x  an int specifying the left side of the area draw in, in pixels,
     *           measured from the left edge of the graphics context
     * @param y  an int specifying the top of the area to draw in, in pixels
     *           measured down from the top edge of the graphics context
     * @param w  an int specifying the width of the area draw in, in pixels
     * @param h  an int specifying the height of the area draw in, in pixels
     *
     * @see CellRendererPane
     * @see java.awt.Component#isLightweight
     */
    public static void paintComponent(Graphics g, Component c, Container p, int x, int y, int w, int h) {
        getCellRendererPane(c, p).paintComponent(g, c, p, x, y, w, h,false);
    }

    /**
     * Paints a component to the specified <code>Graphics</code>.  This
     * is a cover method for
     * {@link #paintComponent(Graphics,Component,Container,int,int,int,int)}.
     * Refer to it for more information.
     *
     * @param g  the <code>Graphics</code> object to draw on
     * @param c  the <code>Component</code> to draw
     * @param p  the intermediate <code>Container</code>
     * @param r  the <code>Rectangle</code> to draw in
     *
     * @see #paintComponent(Graphics,Component,Container,int,int,int,int)
     * @see CellRendererPane
     */
    public static void paintComponent(Graphics g, Component c, Container p, Rectangle r) {
        paintComponent(g, c, p, r.x, r.y, r.width, r.height);
    }


    /*
     * Ensures that cell renderer <code>c</code> has a
     * <code>ComponentShell</code> parent and that
     * the shell's parent is p.
     */
    private static CellRendererPane getCellRendererPane(Component c, Container p) {
        Container shell = c.getParent();
        if (shell instanceof CellRendererPane) {
            if (shell.getParent() != p) {
                p.add(shell);
            }
        } else {
            shell = (Container) Interface.getInstance("javax.swing.CellRendererPane", false);
            shell.add(c);
            p.add(shell);
        }
        return (CellRendererPane)shell;
    }

    /**
     * A simple minded look and feel change: ask each node in the tree
     * to <code>updateUI()</code> -- that is, to initialize its UI property
     * with the current look and feel.
     */
    public static void updateComponentTreeUI(Component c) {
        updateComponentTreeUI0(c);
        c.invalidate();
        c.validate();
        ((JSComponent)c).秘repaint();
    }

    private static void updateComponentTreeUI0(Component c) {
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;	
            jc.updateUI();
            JPopupMenu jpm =jc.getComponentPopupMenu();
            if(jpm != null) {
                updateComponentTreeUI(jpm);
            }
        }
        Component[] children = null;
        int n = 0;
        if (c instanceof JMenu) {
            children = ((JMenu)c).getMenuArray();
            n = ((JMenu) c).getMenuComponentCount();
        }
        else if (c instanceof Container) {
            children = JSComponent.秘getChildArray((Container)c);
            n = ((Container) c).getComponentCount();
        }
        for(int i = 0; i < n; i++) {
            updateComponentTreeUI0(children[i]);
        }
    }


    /**
     * Causes <i>doRun.run()</i> to be executed asynchronously on the
     * AWT event dispatching thread.  This will happen after all
     * pending AWT events have been processed.  This method should
     * be used when an application thread needs to update the GUI.
     * In the following example the <code>invokeLater</code> call queues
     * the <code>Runnable</code> object <code>doHelloWorld</code>
     * on the event dispatching thread and
     * then prints a message.
     * <pre>
     * Runnable doHelloWorld = new Runnable() {
     *     public void run() {
     *         System.out.println("Hello World on " + Thread.currentThread());
     *     }
     * };
     *
     * SwingUtilities.invokeLater(doHelloWorld);
     * System.out.println("This might well be displayed before the other message.");
     * </pre>
     * If invokeLater is called from the event dispatching thread --
     * for example, from a JButton's ActionListener -- the <i>doRun.run()</i> will
     * still be deferred until all pending events have been processed.
     * Note that if the <i>doRun.run()</i> throws an uncaught exception
     * the event dispatching thread will unwind (not the current thread).
     * <p>
     * Additional documentation and examples for this method can be
     * found in
     * <A HREF="http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html">How to Use Threads</a>,
     * in <em>The Java Tutorial</em>.
     * <p>
     * As of 1.3 this method is just a cover for <code>java.awt.EventQueue.invokeLater()</code>.
     * <p>
     * Unlike the rest of Swing, this method can be invoked from any thread.
     *
     * @see #invokeAndWait
     */
    public static void invokeLater(Runnable doRun) {
        EventQueue.invokeLater(doRun);
    }


    /**
     * Causes <code>doRun.run()</code> to be executed synchronously on the
     * AWT event dispatching thread.  This call blocks until
     * all pending AWT events have been processed and (then)
     * <code>doRun.run()</code> returns. This method should
     * be used when an application thread needs to update the GUI.
     * It shouldn't be called from the event dispatching thread.
     * Here's an example that creates a new application thread
     * that uses <code>invokeAndWait</code> to print a string from the event
     * dispatching thread and then, when that's finished, print
     * a string from the application thread.
     * <pre>
     * final Runnable doHelloWorld = new Runnable() {
     *     public void run() {
     *         System.out.println("Hello World on " + Thread.currentThread());
     *     }
     * };
     *
     * Thread appThread = new Thread() {
     *     public void run() {
     *         try {
     *             SwingUtilities.invokeAndWait(doHelloWorld);
     *         }
     *         catch (Exception e) {
     *             e.printStackTrace();
     *         }
     *         System.out.println("Finished on " + Thread.currentThread());
     *     }
     * };
     * appThread.start();
     * </pre>
     * Note that if the <code>Runnable.run</code> method throws an
     * uncaught exception
     * (on the event dispatching thread) it's caught and rethrown, as
     * an <code>InvocationTargetException</code>, on the caller's thread.
     * <p>
     * Additional documentation and examples for this method can be
     * found in
     * <A HREF="http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html">How to Use Threads</a>,
     * in <em>The Java Tutorial</em>.
     * <p>
     * As of 1.3 this method is just a cover for
     * <code>java.awt.EventQueue.invokeAndWait()</code>.
     *
     * @exception  InterruptedException if we're interrupted while waiting for
     *             the event dispatching thread to finish excecuting
     *             <code>doRun.run()</code>
     * @exception  InvocationTargetException  if an exception is thrown
     *             while running <code>doRun</code>
     *
     * @see #invokeLater
     */
    public static void invokeAndWait(final Runnable doRun)
        throws InterruptedException, InvocationTargetException
    {
        EventQueue.invokeAndWait(doRun);
    }

    /**
     * Returns true if the current thread is an AWT event dispatching thread.
     * <p>
     * As of 1.3 this method is just a cover for
     * <code>java.awt.EventQueue.isDispatchThread()</code>.
     *
     * @return true if the current thread is an AWT event dispatching thread
     */
    public static boolean isEventDispatchThread()
    {
        return EventQueue.isDispatchThread();
    }


//    /*
//     * --- Accessibility Support ---
//     *
//     */
//
//    /**
//     * Get the index of this object in its accessible parent.<p>
//     *
//     * Note: as of the Java 2 platform v1.3, it is recommended that developers call
//     * Component.AccessibleAWTComponent.getAccessibleIndexInParent() instead
//     * of using this method.
//     *
//     * @return -1 of this object does not have an accessible parent.
//     * Otherwise, the index of the child in its accessible parent.
//     */
//    public static int getAccessibleIndexInParent(Component c) {
//        return c.getAccessibleContext().getAccessibleIndexInParent();
//    }
//
//    /**
//     * Returns the <code>Accessible</code> child contained at the
//     * local coordinate <code>Point</code>, if one exists.
//     * Otherwise returns <code>null</code>.
//     *
//     * @return the <code>Accessible</code> at the specified location,
//     *    if it exists; otherwise <code>null</code>
//     */
//    public static Accessible getAccessibleAt(Component c, Point p) {
//        if (c instanceof Container) {
//            return c.getAccessibleContext().getAccessibleComponent().getAccessibleAt(p);
//        } else if (c instanceof Accessible) {
//            Accessible a = (Accessible) c;
//            if (a != null) {
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
//            }
//            return (Accessible) c;
//        }
//        return null;
//    }
//
//    /**
//     * Get the state of this object. <p>
//     *
//     * Note: as of the Java 2 platform v1.3, it is recommended that developers call
//     * Component.AccessibleAWTComponent.getAccessibleIndexInParent() instead
//     * of using this method.
//     *
//     * @return an instance of AccessibleStateSet containing the current state
//     * set of the object
//     * @see AccessibleState
//     */
//    public static AccessibleStateSet getAccessibleStateSet(Component c) {
//        return c.getAccessibleContext().getAccessibleStateSet();
//    }
//
//    /**
//     * Returns the number of accessible children in the object.  If all
//     * of the children of this object implement Accessible, than this
//     * method should return the number of children of this object. <p>
//     *
//     * Note: as of the Java 2 platform v1.3, it is recommended that developers call
//     * Component.AccessibleAWTComponent.getAccessibleIndexInParent() instead
//     * of using this method.
//     *
//     * @return the number of accessible children in the object.
//     */
//    public static int getAccessibleChildrenCount(Component c) {
//        return c.getAccessibleContext().getAccessibleChildrenCount();
//    }
//
//    /**
//     * Return the nth Accessible child of the object. <p>
//     *
//     * Note: as of the Java 2 platform v1.3, it is recommended that developers call
//     * Component.AccessibleAWTComponent.getAccessibleIndexInParent() instead
//     * of using this method.
//     *
//     * @param i zero-based index of child
//     * @return the nth Accessible child of the object
//     */
//    public static Accessible getAccessibleChild(Component c, int i) {
//        return c.getAccessibleContext().getAccessibleChild(i);
//    }
//
    /**
     * Return the child <code>Component</code> of the specified
     * <code>Component</code> that is the focus owner, if any.
     *
     * @param c the root of the <code>Component</code> hierarchy to
     *        search for the focus owner
     * @return the focus owner, or <code>null</code> if there is no focus
     *         owner, or if the focus owner is not <code>comp</code>, or a
     *         descendant of <code>comp</code>
     *
     * @see java.awt.KeyboardFocusManager#getFocusOwner
     * @deprecated As of 1.4, replaced by
     *   <code>KeyboardFocusManager.getFocusOwner()</code>.
     */
    @Deprecated
    public static Component findFocusOwner(Component c) {
        Component focusOwner = KeyboardFocusManager.
            getCurrentKeyboardFocusManager().getFocusOwner();

        // verify focusOwner is a descendant of c
        for (Component temp = focusOwner; temp != null;
             temp = (temp.isWindowOrJSApplet() ? null : temp.getParent()))
        {
            if (temp == c) {
                return focusOwner;
            }
        }

        return null;
    }

    /**
     * If c is a JRootPane descendant return its JRootPane ancestor.
     * If c is a RootPaneContainer then return its JRootPane.
     * @return the JRootPane for Component c or {@code null}.
     */
    public static JRootPane getRootPane(Component c) {
        if (c instanceof RootPaneContainer) {
            return ((RootPaneContainer)c).getRootPane();
        }
        for( ; c != null; c = c.getParent()) {
            if (c instanceof JRootPane) {
                return (JRootPane)c;
            }
        }
        return null;
    }


    /**
     * Returns the root component for the current component tree.
     * @return the first ancestor of c that's a Window or the last Applet ancestor
     */
    public static Component getRoot(Component c) {
        for(Component p = c; p != null; p = p.getParent()) {
            if (p.isWindowOrJSApplet()) {
                return p;
            }
        }
        return null;
    }

 // SwingJS never used - see JComponent   
//    /**
//     * Process the key bindings for the <code>Component</code> associated with
//     * <code>event</code>. This method is only useful if
//     * <code>event.getComponent()</code> does not descend from
//     * <code>JComponent</code>, or your are not invoking
//     * <code>super.processKeyEvent</code> from within your
//     * <code>JComponent</code> subclass. <code>JComponent</code>
//     * automatically processes bindings from within its
//     * <code>processKeyEvent</code> method, hence you rarely need
//     * to directly invoke this method.
//     *
//     * @param event KeyEvent used to identify which bindings to process, as
//     *              well as which Component has focus.
//     * @return true if a binding has found and processed
//     * @since 1.4
//     */
//    public static boolean processKeyBindings(KeyEvent event) {
//        if (event != null) {
//            if (event.isConsumed()) {
//                return false;
//            }
//
//            Component component = event.getComponent();
//            boolean pressed = (event.getID() == KeyEvent.KEY_PRESSED);
//
//            if (!isValidKeyEventForKeyBindings(event)) {
//                return false;
//            }
//            // Find the first JComponent in the ancestor hierarchy, and
//            // invoke processKeyBindings on it
//            while (component != null) {
//                if (component instanceof JComponent) {
//                    return ((JComponent)component).processKeyBindings(
//                                                   event, pressed);
//                }
//                if ((component instanceof JSApplet) ||
//                    (component instanceof Window)) {
//                    // No JComponents, if Window or Applet parent, process
//                    // WHEN_IN_FOCUSED_WINDOW bindings.
//                    return JComponent.processKeyBindingsForAllComponents(
//                                  event, (Container)component, pressed);
//                }
//                component = component.getParent();
//            }
//        }
//        return false;
//    }

    /**
     * Returns true if the <code>e</code> is a valid KeyEvent to use in
     * processing the key bindings associated with JComponents.
     */
    static boolean isValidKeyEventForKeyBindings(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_TYPED) {
            int mod = e.getModifiers();
            if (((mod & ActionEvent.ALT_MASK) != 0) &&
                ((mod & ActionEvent.CTRL_MASK) == 0)) {
                // filter out typed "alt-?" keys, but not those created
                // with AltGr, and not control characters
                return false;
            }
        } else {
        	switch (e.getKeyCode()) {
        	case KeyEvent.VK_SHIFT:
        	case KeyEvent.VK_CONTROL:
        	case KeyEvent.VK_ALT:
        	case KeyEvent.VK_ALT_GRAPH:
        	case KeyEvent.VK_META:
        		return false;
        	}
        }
        return true;
    }

    /**
     * Invokes <code>actionPerformed</code> on <code>action</code> if
     * <code>action</code> is enabled (and non-{@code null}). The command for the
     * ActionEvent is determined by:
     * <ol>
     *   <li>If the action was registered via
     *       <code>registerKeyboardAction</code>, then the command string
     *       passed in ({@code null} will be used if {@code null} was passed in).
     *   <li>Action value with name Action.ACTION_COMMAND_KEY, unless {@code null}.
     *   <li>String value of the KeyEvent, unless <code>getKeyChar</code>
     *       returns KeyEvent.CHAR_UNDEFINED..
     * </ol>
     * This will return true if <code>action</code> is non-{@code null} and
     * actionPerformed is invoked on it.
     *
     * @since 1.3
     */
    public static boolean notifyAction(Action action, KeyStroke ks,
                                       KeyEvent event, Object sender,
                                       int modifiers) {
        if (action == null) {
            return false;
        }
        if (action instanceof UIAction) {
            if (!((UIAction)action).isEnabled(sender)) {
                return false;
            }
        }
        else if (!action.isEnabled()) {
            return false;
        }
        Object commandO;
        boolean stayNull;

        // Get the command object.
        commandO = action.getValue(Action.ACTION_COMMAND_KEY);
        if (commandO == null && JComponent.isActionStandin(action)) {
            // ActionStandin is used for historical reasons to support
            // registerKeyboardAction with a null value.
            stayNull = true;
        }
        else {
            stayNull = false;
        }

        // Convert it to a string.
        String command;

        if (commandO != null) {
            command = commandO.toString();
        }
        else if (!stayNull && event.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
            command = String.valueOf(event.getKeyChar());
        }
        else {
            // Do null for undefined chars, or if registerKeyboardAction
            // was called with a null.
            command = null;
        }
        action.actionPerformed(new ActionEvent(sender,
                        ActionEvent.ACTION_PERFORMED, command, event.getWhen(),
                        modifiers));
        return true;
    }


    /**
     * Convenience method to change the UI InputMap for <code>component</code>
     * to <code>uiInputMap</code>. If <code>uiInputMap</code> is {@code null},
     * this removes any previously installed UI InputMap.
     *
     * @since 1.3
     */
    public static void replaceUIInputMap(JComponent component, int type,
                                         InputMap uiInputMap) {
        InputMap map = component.getInputMap(type, (uiInputMap != null));

        while (map != null) {
            InputMap parent = map.getParent();
            if (parent == null || (parent instanceof UIResource)) {
                map.setParent(uiInputMap);
                return;
            }
            map = parent;
        }
    }


    /**
     * Convenience method to change the UI ActionMap for <code>component</code>
     * to <code>uiActionMap</code>. If <code>uiActionMap</code> is {@code null},
     * this removes any previously installed UI ActionMap.
     *
     * @since 1.3
     */
    public static void replaceUIActionMap(JComponent component,
                                          ActionMap uiActionMap) {
        ActionMap map = component.getActionMap((uiActionMap != null));

        while (map != null) {
            ActionMap parent = map.getParent();
            if (parent == null || (parent instanceof UIResource) && (!(uiActionMap instanceof UIResource) || 
            		/** @j2sNative uiActionMap._key == parent._key || */false)) {
            	// SwingJS change to allow both ButtonListener and JMenuItemUI to both create UIResources for the same component
                map.setParent(uiActionMap);
                return;
            }
            map = parent;
        }
    }


    /**
     * Returns the InputMap provided by the UI for condition
     * <code>condition</code> in component <code>component</code>.
     * <p>This will return {@code null} if the UI has not installed a InputMap
     * of the specified type.
     *
     * @since 1.3
     */
    public static InputMap getUIInputMap(JComponent component, int condition) {
        InputMap map = component.getInputMap(condition, false);
        while (map != null) {
            InputMap parent = map.getParent();
            if (parent instanceof UIResource) {
                return parent;
            }
            map = parent;
        }
        return null;
    }

    /**
     * Returns the ActionMap provided by the UI
     * in component <code>component</code>.
     * <p>This will return {@code null} if the UI has not installed an ActionMap.
     *
     * @since 1.3
     */
    public static ActionMap getUIActionMap(JComponent component) {
        ActionMap map = component.getActionMap(false);
        while (map != null) {
            ActionMap parent = map.getParent();
            if (parent instanceof UIResource) {
                return parent;
            }
            map = parent;
        }
        return null;
    }


    // Don't use String, as it's not guaranteed to be unique in a Hashtable.
    //private static final Object sharedOwnerFrameKey = new Object(); // SwingUtilities.sharedOwnerFrame

    // SwingJS -- note that we extend JFrame not Frame here so that we have a UI peer
    
    static class SharedOwnerFrame extends JFrame implements WindowListener {
    	
    	SharedOwnerFrame() {
    		super(null, null, null, null); // required by SwingJS
    	}
    	
        @Override
				public void addNotify() {
        	  updateUI();
        		getOrCreatePeer();
            super.addNotify();
            installListeners();
        }

        /**
         * Install window listeners on owned windows to watch for displayability changes
         */
        void installListeners() {
            Window[] windows = getOwnedWindows();
            for (int ind = 0; ind < windows.length; ind++){
                Window window = windows[ind];
                if (window != null) {
                    window.removeWindowListener(this);
                    window.addWindowListener(this);
                }
            }
        }

        /**
         * Watches for displayability changes and disposes shared instance if there are no
         * displayable children left.
         */
        @Override
				public void windowClosed(WindowEvent e) {
//            synchronized(getTreeLock()) {
                Window[] windows = getOwnedWindows();
                for (int ind = 0; ind < windows.length; ind++) {
                    Window window = windows[ind];
                    if (window != null) {
                        if (window.isDisplayable()) {
                            return;
                        }
                        window.removeWindowListener(this);
                    }
 //               }
                dispose();
            }
        }
        @Override
				public void windowOpened(WindowEvent e) {
        }
        @Override
				public void windowClosing(WindowEvent e) {
        }
        @Override
				public void windowIconified(WindowEvent e) {
        }
        @Override
				public void windowDeiconified(WindowEvent e) {
        }
        @Override
				public void windowActivated(WindowEvent e) {
        }
        @Override
				public void windowDeactivated(WindowEvent e) {
        }

        @Override
				public void show() {
            // This frame can never be shown
        }
        @Override
				public void dispose() {
//            try {
//                getToolkit().getSystemEventQueue();
//                super.dispose();
//            } catch (Exception e) {
//                // untrusted code not allowed to dispose
//            }
        }
    }

	/**
	 * Returns a toolkit-private, shared, invisible Frame to be the owner for
	 * JDialogs and JWindows created with {@code null} owners.
	 * 
	 * Note that in SwingJS this is not fully static -- it is associated with this
	 * applet only
	 * 
	 * @exception HeadlessException
	 *              if GraphicsEnvironment.isHeadless() returns true.
	 * @see java.awt.GraphicsEnvironment#isHeadless
	 */
	public static JSFrame getSharedOwnerFrame() {
		JSAppletViewer p = JSToolkit.getAppletViewer();
		JSFrame f = p.sharedOwnerFrame;
		return (f == null ? (p.sharedOwnerFrame = new SharedOwnerFrame()) : f);
	}

    /**
     * Returns a SharedOwnerFrame's shutdown listener to dispose the SharedOwnerFrame
     * if it has no more displayable children.
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    static WindowListener getSharedOwnerFrameShutdownListener() {
        JSFrame sharedOwnerFrame = getSharedOwnerFrame();
        return (WindowListener)sharedOwnerFrame;
    }

    /* Don't make these AppContext accessors public or protected --
     * since AppContext is in sun.awt in 1.2, we shouldn't expose it
     * even indirectly with a public API.
     */
    // REMIND(aim): phase out use of 4 methods below since they
    // are just private covers for AWT methods (?)

    public static Object appContextGet(Object key) {
        return AppContext.getAppContext().get(key);
    }

    public static void appContextPut(Object key, Object value) {
        AppContext.getAppContext().put(key, value);
    }

    public static void appContextRemove(Object key) {
        AppContext.getAppContext().remove(key);
    }


    static Class loadSystemClass(String className) throws ClassNotFoundException {
        //ReflectUtil.checkPackageAccess(className);
        return Class.forName(className, true, Thread.currentThread().
                             getContextClassLoader());
    }


   /*
     * Convenience function for determining ComponentOrientation.  Helps us
     * avoid having Munge directives throughout the code.
     */
    static boolean isLeftToRight( Component c ) {
        return c.getComponentOrientation().isLeftToRight();
    }
    private SwingUtilities() {
        throw new Error("SwingUtilities is just a container for static methods");
    }

    /**
     * Returns true if the Icon <code>icon</code> is an instance of
     * ImageIcon, and the image it contains is the same as <code>image</code>.
     */
    static boolean doesIconReferenceImage(Icon icon, Image image) {
        Image iconImage = (icon != null && (icon instanceof ImageIcon)) ?
                           ((ImageIcon)icon).getImage() : null;
        return (iconImage == image);
    }

    /**
     * Returns index of the first occurrence of <code>mnemonic</code>
     * within string <code>text</code>. Matching algorithm is not
     * case-sensitive.
     *
     * @param text The text to search through, may be {@code null}
     * @param mnemonic The mnemonic to find the character for.
     * @return index into the string if exists, otherwise -1
     */
    static int findDisplayedMnemonicIndex(String text, int mnemonic) {
        if (text == null || mnemonic == '\0') {
            return -1;
        }

        char uc = Character.toUpperCase((char)mnemonic);
        char lc = Character.toLowerCase((char)mnemonic);

        int uci = text.indexOf(uc);
        int lci = text.indexOf(lc);

        if (uci == -1) {
            return lci;
        } else if(lci == -1) {
            return uci;
        } else {
            return (lci < uci) ? lci : uci;
        }
    }

    /**
     * Stores the position and size of
     * the inner painting area of the specified component
     * in <code>r</code> and returns <code>r</code>.
     * The position and size specify the bounds of the component,
     * adjusted so as not to include the border area (the insets).
     * This method is useful for classes
     * that implement painting code.
     *
     * @param c  the JComponent in question; if {@code null}, this method returns {@code null}
     * @param r  the Rectangle instance to be modified;
     *           may be {@code null}
     * @return {@code null} if the Component is {@code null};
     *         otherwise, returns the passed-in rectangle (if non-{@code null})
     *         or a new rectangle specifying position and size information
     *
     * @since 1.4
     */
    public static Rectangle calculateInnerArea(JComponent c, Rectangle r) {
        if (c == null) {
            return null;
        }
        Rectangle rect = r;
        Insets insets = c.getInsets();

        if (rect == null) {
            rect = new Rectangle();
        }

        rect.x = insets.left;
        rect.y = insets.top;
        rect.width = c.getWidth() - insets.left - insets.right;
        rect.height = c.getHeight() - insets.top - insets.bottom;

        return rect;
    }

    static void updateRendererOrEditorUI(Object rendererOrEditor) {
        if (rendererOrEditor == null) {
            return;
        }

        Component component = null;

        if (rendererOrEditor instanceof Component) {
            component = (Component)rendererOrEditor;
        }
        if (rendererOrEditor instanceof DefaultCellEditor) {
            component = ((DefaultCellEditor)rendererOrEditor).getComponent();
        }

        if (component != null) {
            SwingUtilities.updateComponentTreeUI(component);
        }
    }

    /**
     * Java 1.7 
     * @param component
     * @return
     */
    public static Container getUnwrappedParent(Component component) {
        Container parent = component.getParent();
        while(parent instanceof JLayer) {
            parent = parent.getParent();
        }
        return parent;
    }

}
