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

package swingjs.plaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
//import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.CellRendererPane;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.UIResource;
import javax.swing.text.Position;

import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;
import swingjs.JSUtil;
//import java.awt.datatransfer.Transferable;
//import javax.swing.BorderFactory;
//import javax.swing.ImageIcon;
//import javax.swing.TransferHandler;
//import javax.swing.plaf.ComponentUI;
import swingjs.api.js.DOMNode;
import swingjs.api.js.JQueryObject;

/**
 * An extensible implementation of {@code ListUI}.
 * <p>
 * {@code JSListUI} instances cannot be shared between multiple lists.
 * 
 * @author Hans Muller
 * @author Philip Milne
 * @author Shannon Hickey (drag and drop)
 * 
 * 
 *         SwingJS notes:
 * 
 *         Current implementation does not include support for drag/drop.
 * 
 *         Only changes to Java 6.27 BasicListUI involve cell painting, which is
 *         replaced by DOM div insertion (see updateItemHTML), and setting up
 *         the domNode so that it can accept focus and handle mouse and keyboard
 *         clicks
 * 
 * 
 * 
 * 
 * 
 */
public class JSListUI extends JSLightweightUI //true, but unnecessary implements ListPeer
{

	protected boolean needFilling = true;

	JSListUI() {
		super();
		me = this;
	}

	String itemHTML = null;

	@Override
	public DOMNode updateDOMNode() {
		list = (JList) jc;
		if (domNode == null) {
			
			domNode = focusNode = enableNode = newDOMObject("div", id);
			// maybe DOMNode.setAttrInt(domNode, "tabIndex", 1);
			innerNode = newDOMObject("div", id + "_inner");
			addFocusHandler();

			domNode.appendChild(innerNode);
			// tell j2sApplet.js that we will handle all the mouse clicks here
//			setDataComponent(focusNode);
//			bindJSKeyEvents(focusNode, false);
		}
	    setBackgroundImpl(jc.getBackground());
		if (needFilling) {
			fillDOM();
		}
		
		return updateDOMNodeCUI();
	}

	@Override
	protected void undisposeUI(DOMNode node) {
		super.undisposeUI(node);
		bindJSKeyEvents(focusNode, true);		
	}


//	@Override
//	public boolean handleJSEvent(Object target, int eventType, Object jQueryEvent) {
//		switch (eventType) {
//		case SOME_KEY_EVENT:
//			JSKeyEvent keyEvent = JSKeyEvent.newJSKeyEvent(jc, jQueryEvent, 0, true);
//			if (keyEvent != null)
//				jc.dispatchEvent(keyEvent);
//			break;
//		}
//		return true;
//	}
//
	/**
	 * Each time there is a change in components - a scroll, for example, we will
	 * need to fill domNode with a new set of children
	 */
	protected void fillDOM() {
//		
//		DOMNode.removeAllChildren(listNode);
//		String s = "";
//		if (itemHTML != null)
//			// for (int n = itemHTML.length, i = 0; i < n; i++)
//			// s += itemHTML[i];
//			DOMNode.setAttr(listNode, "innerHTML", itemHTML);
	}

	private static final Object BASELINE_COMPONENT_KEY = new Object(); // List.baselineComponent

	protected JSListUI me;
	protected JList list = null;
	protected CellRendererPane rendererPane;

	// Listeners that this UI attaches to the JList
	protected FocusListener focusListener;
	protected MouseInputListener mouseInputListener;
	protected ListSelectionListener listSelectionListener;
	protected ListDataListener listDataListener;
	protected PropertyChangeListener propertyChangeListener;
	private Handler handler;

	protected int[] cellHeights = null;
	protected int cellHeight = -1;
	protected int cellWidth = -1;
	protected int updateLayoutStateNeeded = modelChanged;
	/**
	 * Height of the list. When asked to paint, if the current size of the list
	 * differs, this will update the layout state.
	 */
	private int listHeight;

	/**
	 * Width of the list. When asked to paint, if the current size of the list
	 * differs, this will update the layout state.
	 */
	private int listWidth;

	/**
	 * The layout orientation of the list.
	 */
	int layoutOrientation;

	// Following ivars are used if the list is laying out horizontally

	/**
	 * Number of columns to create.
	 */
	int columnCount;
	/**
	 * Preferred height to make the list, this is only used if the the list is
	 * layed out horizontally.
	 */
	private int preferredHeight;
	/**
	 * Number of rows per column. This is only used if the row height is fixed.
	 */
	private int rowsPerColumn;

	/**
	 * The time factor to trea the series of typed alphanumeric key as prefix for
	 * first letter navigation.
	 */
	long timeFactor = 1000L;

	/**
	 * Local cache of JList's client property "List.isFileList"
	 */
	boolean isFileList = false;

	/**
	 * Local cache of JList's component orientation property
	 */
	boolean isLeftToRight = true;

	/*
	 * The bits below define JList property changes that affect layout. When one
	 * of these properties changes we set a bit in updateLayoutStateNeeded. The
	 * change is dealt with lazily, see maybeUpdateLayoutState. Changes to the
	 * JLists model, e.g. the models length changed, are handled similarly, see
	 * DataListener.
	 */

	protected final static int modelChanged = 1 << 0;
	protected final static int selectionModelChanged = 1 << 1;
	protected final static int fontChanged = 1 << 2;
	protected final static int fixedCellWidthChanged = 1 << 3;
	protected final static int fixedCellHeightChanged = 1 << 4;
	protected final static int prototypeCellValueChanged = 1 << 5;
	protected final static int cellRendererChanged = 1 << 6;
	private final static int layoutOrientationChanged = 1 << 7;
	private final static int heightChanged = 1 << 8;
	private final static int widthChanged = 1 << 9;
	private final static int componentOrientationChanged = 1 << 10;

//	private static final int DROP_LINE_THICKNESS = 2;

	static void loadActionMap(LazyActionMap map) {
		map.put(new Actions(Actions.SELECT_PREVIOUS_COLUMN));
		map.put(new Actions(Actions.SELECT_PREVIOUS_COLUMN_EXTEND));
		map.put(new Actions(Actions.SELECT_PREVIOUS_COLUMN_CHANGE_LEAD));
		map.put(new Actions(Actions.SELECT_NEXT_COLUMN));
		map.put(new Actions(Actions.SELECT_NEXT_COLUMN_EXTEND));
		map.put(new Actions(Actions.SELECT_NEXT_COLUMN_CHANGE_LEAD));
		map.put(new Actions(Actions.SELECT_PREVIOUS_ROW));
		map.put(new Actions(Actions.SELECT_PREVIOUS_ROW_EXTEND));
		map.put(new Actions(Actions.SELECT_PREVIOUS_ROW_CHANGE_LEAD));
		map.put(new Actions(Actions.SELECT_NEXT_ROW));
		map.put(new Actions(Actions.SELECT_NEXT_ROW_EXTEND));
		map.put(new Actions(Actions.SELECT_NEXT_ROW_CHANGE_LEAD));
		map.put(new Actions(Actions.SELECT_FIRST_ROW));
		map.put(new Actions(Actions.SELECT_FIRST_ROW_EXTEND));
		map.put(new Actions(Actions.SELECT_FIRST_ROW_CHANGE_LEAD));
		map.put(new Actions(Actions.SELECT_LAST_ROW));
		map.put(new Actions(Actions.SELECT_LAST_ROW_EXTEND));
		map.put(new Actions(Actions.SELECT_LAST_ROW_CHANGE_LEAD));
		map.put(new Actions(Actions.SCROLL_UP));
		map.put(new Actions(Actions.SCROLL_UP_EXTEND));
		map.put(new Actions(Actions.SCROLL_UP_CHANGE_LEAD));
		map.put(new Actions(Actions.SCROLL_DOWN));
		map.put(new Actions(Actions.SCROLL_DOWN_EXTEND));
		map.put(new Actions(Actions.SCROLL_DOWN_CHANGE_LEAD));
		map.put(new Actions(Actions.SELECT_ALL));
		map.put(new Actions(Actions.CLEAR_SELECTION));
		map.put(new Actions(Actions.ADD_TO_SELECTION));
		map.put(new Actions(Actions.TOGGLE_AND_ANCHOR));
		map.put(new Actions(Actions.EXTEND_TO));
		map.put(new Actions(Actions.MOVE_SELECTION_TO));

		// map.put(TransferHandler.getCutAction().getValue(Action.NAME),
		// TransferHandler.getCutAction());
		// map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
		// TransferHandler.getCopyAction());
		// map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
		// TransferHandler.getPasteAction());
	}

	/**
	 * Paint one List cell: compute the relevant state, get the "rubber stamp"
	 * cell renderer component, and then use the CellRendererPane to paint it.
	 * Subclasses may want to override this method rather than paint().
	 * 
	 * @see #paint
	 */
	protected void paintCell(Graphics g, int index, Rectangle rowBounds,
			ListCellRenderer cellRenderer, ListModel dataModel,
			ListSelectionModel selModel, int leadIndex) {
		Object value = dataModel.getElementAt(index);
		boolean cellHasFocus = list.hasFocus() && (index == leadIndex);
		boolean isSelected = selModel.isSelectedIndex(index);
		Component rendererComponent = cellRenderer
				.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
		/**
		 * @j2sNative
		 * 
		 *            if (!rendererComponent.getUI$) { alert(
		 *            "swingjs.JListUI -- Developer! All list cell renderers in SwingJS must be Swing components"
		 *            ); } 
		 *            
		 */
		{}
		int cx = rowBounds.x;
		int cy = rowBounds.y;
		int cw = rowBounds.width;
		int ch = rowBounds.height;

		if (isFileList) {
			// Shrink renderer to preferred size. This is mostly used on Windows
			// where selection is only shown around the file name, instead of
			// across the whole list cell.
			int w = Math.min(cw, rendererComponent.getPreferredSize().width + 4);
			if (!isLeftToRight) {
				cx += (cw - w);
			}
			cw = w;
		}

		JComponent r = (JComponent) rendererComponent;
		rendererPane.paintComponent(g, r, list, cx, cy, cw, ch,
				true);
		updateItemHTML((JComponent) r, index, cx, cy, cw, getRowHeight(index));
	}

	private void updateItemHTML(JComponent c, int index, int left, int top, int width, int height) {
	  DOMNode node = null;
		if (c != null) {
			c.setSize(width, height);
	    node = c.秘getUI().getListNode();
	  }
		String myid = id + "_" + index;
		JQueryObject jnode = $((DOMNode) (Object) ("#" + myid));
		if (((DOMNode[]) (Object) jnode)[0] == null) {
			if (node != null) {
				DOMNode div = newDOMObject("div", myid);
				DOMNode.setTopLeftAbsolute(div, top, left);
				div.appendChild(node);
				innerNode.appendChild(div);
			}
		} else {
			jnode.empty();
			if (node != null)
				jnode.append(node);
		}
		//Rectangle r = getCellBounds1(list, index);
		DOMNode.setSize(node, width, height);
//		DOMNode.setTopLeftAbsolute(node, r.y, r.x);
	}

	protected void removeItemHTML(int i0, int i1) {
		int n = list.getModel().getSize();
		for (int i = i0; i <= i1; i++)
			updateItemHTML(null, n++, 0, 0, 0, 0);
	}

	/**
	 * Paint the rows that intersect the Graphics objects clipRect. This method
	 * calls paintCell as necessary. Subclasses may want to override these
	 * methods.
	 * 
	 * @see #paintCell
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
	  if (isTainted)
	    updateDOMNode();
		super.paint(g, c);
		Shape clip = g.getClip();
		paintImpl(g, c);
		g.setClip(clip);
//		paintDropLine(g);
	}

	private void paintImpl(Graphics g, JComponent c) {

		// It is the responsibility of the JScrollPane scrollbar will move the JList
		// to new x,y coordinates.

		needFilling = false;
		itemHTML = "";
		switch (layoutOrientation) {
		case JList.VERTICAL_WRAP:
			if (list.getHeight() != listHeight) {
				updateLayoutStateNeeded |= heightChanged;
				redrawList();
			}
			break;
		case JList.HORIZONTAL_WRAP:
			if (list.getWidth() != listWidth) {
				updateLayoutStateNeeded |= widthChanged;
				redrawList();
			}
			break;
		default:
			break;
		}
		maybeUpdateLayoutState();

		ListCellRenderer renderer = list.getCellRenderer();
		ListModel dataModel = list.getModel();
		ListSelectionModel selModel = list.getSelectionModel();
		int size;

		if ((renderer == null) || (size = dataModel.getSize()) == 0) {
			return;
		}

		// Determine how many columns we need to paint
		Rectangle paintBounds = g.getClipBounds();
		int startColumn, endColumn;
		if (c.getComponentOrientation().isLeftToRight()) {
			startColumn = convertLocationToColumn(paintBounds.x, paintBounds.y);
			endColumn = convertLocationToColumn(paintBounds.x + paintBounds.width,
					paintBounds.y);
		} else {
			startColumn = convertLocationToColumn(paintBounds.x + paintBounds.width,
					paintBounds.y);
			endColumn = convertLocationToColumn(paintBounds.x, paintBounds.y);
		}
		int maxY = paintBounds.y + paintBounds.height;
		int leadIndex = adjustIndex(list.getLeadSelectionIndex(), list);
		int rowIncrement = (layoutOrientation == JList.HORIZONTAL_WRAP) ? columnCount
				: 1;

		for (int colCounter = startColumn; colCounter <= endColumn; colCounter++) {
			// And then how many rows in this columnn
			int row = convertLocationToRowInColumn(paintBounds.y, colCounter);
			int rowCount = getRowCount(colCounter);
			int index = getModelIndex(colCounter, row);
			Rectangle rowBounds = getCellBounds(list, index, index);

			if (rowBounds == null) {
				// Not valid, bail!
				return;
			}
			while (row < rowCount && rowBounds.y < maxY && index < size) {
				rowBounds.height = getHeight(colCounter, row);
				// SwingJS unnecessary
				// g.setClip(rowBounds.x, rowBounds.y, rowBounds.width,
				// rowBounds.height);
				// g.clipRect(paintBounds.x, paintBounds.y, paintBounds.width,
				// paintBounds.height);
				paintCell(g, index, rowBounds, renderer, dataModel, selModel, leadIndex);
				rowBounds.y += rowBounds.height;
				index += rowIncrement;
				row++;
			}
		}
		// Empty out the renderer pane, allowing renderers to be gc'ed.
		rendererPane.removeAll();
		updateDOMNode();
	}

//	private void paintDropLine(Graphics g) {
//		JList.DropLocation loc = null;// list.getDropLocation();
//		if (loc == null || !loc.isInsert()) {
//			return;
//		}
//
//		Color c = DefaultLookup.getColor(list, this, "List.dropLineColor", null);
//		if (c != null) {
//			g.setColor(c);
//			Rectangle rect = getDropLineRect(loc);
//			g.fillRect(rect.x, rect.y, rect.width, rect.height);
//		}
//	}

//	private Rectangle getDropLineRect(JList.DropLocation loc) {
//		int size = list.getModel().getSize();
//
//		if (size == 0) {
//			Insets insets = list.getInsets();
//			if (layoutOrientation == JList.HORIZONTAL_WRAP) {
//				if (isLeftToRight) {
//					return new Rectangle(insets.left, insets.top, DROP_LINE_THICKNESS, 20);
//				} else {
//					return new Rectangle(list.getWidth() - DROP_LINE_THICKNESS
//							- insets.right, insets.top, DROP_LINE_THICKNESS, 20);
//				}
//			} else {
//				return new Rectangle(insets.left, insets.top, list.getWidth()
//						- insets.left - insets.right, DROP_LINE_THICKNESS);
//			}
//		}
//
//		Rectangle rect = null;
//		int index = loc.getIndex();
//		boolean decr = false;
//
//		if (layoutOrientation == JList.HORIZONTAL_WRAP) {
//			if (index == size) {
//				decr = true;
//			} else if (index != 0
//					&& convertModelToRow(index) != convertModelToRow(index - 1)) {
//
//				Rectangle prev = getCellBounds1(list, index - 1);
//				Rectangle me = getCellBounds1(list, index);
//				Point p = loc.getDropPoint();
//
//				if (isLeftToRight) {
//					decr = Point2D.distance(prev.x + prev.width, prev.y
//							+ (int) (prev.height / 2.0), p.x, p.y) < Point2D.distance(me.x,
//							me.y + (int) (me.height / 2.0), p.x, p.y);
//				} else {
//					decr = Point2D.distance(prev.x, prev.y + (int) (prev.height / 2.0),
//							p.x, p.y) < Point2D.distance(me.x + me.width, me.y
//							+ (int) (prev.height / 2.0), p.x, p.y);
//				}
//			}
//
//			if (decr) {
//				index--;
//				rect = getCellBounds1(list, index);
//				if (isLeftToRight) {
//					rect.x += rect.width;
//				} else {
//					rect.x -= DROP_LINE_THICKNESS;
//				}
//			} else {
//				rect = getCellBounds1(list, index);
//				if (!isLeftToRight) {
//					rect.x += rect.width - DROP_LINE_THICKNESS;
//				}
//			}
//
//			if (rect.x >= list.getWidth()) {
//				rect.x = list.getWidth() - DROP_LINE_THICKNESS;
//			} else if (rect.x < 0) {
//				rect.x = 0;
//			}
//
//			rect.width = DROP_LINE_THICKNESS;
//		} else if (layoutOrientation == JList.VERTICAL_WRAP) {
//			if (index == size) {
//				index--;
//				rect = getCellBounds1(list, index);
//				rect.y += rect.height;
//			} else if (index != 0
//					&& convertModelToColumn(index) != convertModelToColumn(index - 1)) {
//
//				Rectangle prev = getCellBounds1(list, index - 1);
//				Rectangle me = getCellBounds1(list, index);
//				Point p = loc.getDropPoint();
//				if (Point2D.distance(prev.x + (int) (prev.width / 2.0), prev.y
//						+ prev.height, p.x, p.y) < Point2D.distance(me.x
//						+ (int) (me.width / 2.0), me.y, p.x, p.y)) {
//
//					index--;
//					rect = getCellBounds1(list, index);
//					rect.y += rect.height;
//				} else {
//					rect = getCellBounds1(list, index);
//				}
//			} else {
//				rect = getCellBounds1(list, index);
//			}
//
//			if (rect.y >= list.getHeight()) {
//				rect.y = list.getHeight() - DROP_LINE_THICKNESS;
//			}
//
//			rect.height = DROP_LINE_THICKNESS;
//		} else {
//			if (index == size) {
//				index--;
//				rect = getCellBounds1(list, index);
//				rect.y += rect.height;
//			} else {
//				rect = getCellBounds1(list, index);
//			}
//
//			if (rect.y >= list.getHeight()) {
//				rect.y = list.getHeight() - DROP_LINE_THICKNESS;
//			}
//
//			rect.height = DROP_LINE_THICKNESS;
//		}
//
//		return rect;
//	}

	/**
	 * Returns the baseline.
	 * 
	 * @throws NullPointerException
	 *           {@inheritDoc}
	 * @throws IllegalArgumentException
	 *           {@inheritDoc}
	 * @see javax.swing.JComponent#getBaseline(int, int)
	 * @since 1.6
	 */
	@Override
	public int getBaseline(JComponent c, int width, int height) {
		super.getBaseline(c, width, height);
		int rowHeight = list.getFixedCellHeight();
		UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
		Component baselineComponent = (Component) lafDefaults
				.get(BASELINE_COMPONENT_KEY);
		if (baselineComponent == null) {
			ListCellRenderer lcr = (ListCellRenderer) UIManager
					.get("List.cellRenderer");

			// fix for 6711072 some LAFs like Nimbus do not provide this
			// UIManager key and we should not through a NPE here because of it
			if (lcr == null) {
				lcr = new DefaultListCellRenderer();
			}
			baselineComponent = lcr.getListCellRendererComponent(list, "a", -1,
					false, false);
			lafDefaults.put(BASELINE_COMPONENT_KEY, baselineComponent);
		}
		baselineComponent.setFont(getFont());
		// JList actually has much more complex behavior here.
		// If rowHeight != -1 the rowHeight is either the max of all cell
		// heights (layout orientation != VERTICAL), or is variable depending
		// upon the cell. We assume a default size.
		// We could theoretically query the real renderer, but that would
		// not work for an empty model and the results may vary with
		// the content.
		if (rowHeight == -1) {
			rowHeight = baselineComponent.getPreferredSize().height;
		}
		return baselineComponent.getBaseline(Integer.MAX_VALUE, rowHeight)
				+ list.getInsets().top;
	}

	/**
	 * Returns an enum indicating how the baseline of the component changes as the
	 * size changes.
	 * 
	 * @throws NullPointerException
	 *           {@inheritDoc}
	 * @see javax.swing.JComponent#getBaseline(int, int)
	 * @since 1.6
	 */
	@Override
	public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent c) {
		super.getBaselineResizeBehavior(c);
		return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
	}

	/**
	 * The preferredSize of the list depends upon the layout orientation.
	 * <table summary="Describes the preferred size for each layout orientation">
	 * <tr>
	 * <th>Layout Orientation</th>
	 * <th>Preferred Size</th>
	 * </tr>
	 * <tr>
	 * <td>JList.VERTICAL
	 * <td>The preferredSize of the list is total height of the rows and the
	 * maximum width of the cells. If JList.fixedCellHeight is specified then the
	 * total height of the rows is just (cellVerticalMargins + fixedCellHeight) *
	 * model.getSize() where rowVerticalMargins is the space we allocate for
	 * drawing the yellow focus outline. Similarly if fixedCellWidth is specified
	 * then we just use that.</td>
	 * <tr>
	 * <td>JList.VERTICAL_WRAP
	 * <td>If the visible row count is greater than zero, the preferredHeight is
	 * the maximum cell height * visibleRowCount. If the visible row count is <=
	 * 0, the preferred height is either the current height of the list, or the
	 * maximum cell height, whichever is bigger. The preferred width is than the
	 * maximum cell width * number of columns needed. Where the number of columns
	 * needs is list.height / max cell height. Max cell height is either the fixed
	 * cell height, or is determined by iterating through all the cells to find
	 * the maximum height from the ListCellRenderer.
	 * <tr>
	 * <td>JList.HORIZONTAL_WRAP
	 * <td>If the visible row count is greater than zero, the preferredHeight is
	 * the maximum cell height * adjustedRowCount. Where visibleRowCount is used
	 * to determine the number of columns. Because this lays out horizontally the
	 * number of rows is then determined from the column count. For example, lets
	 * say you have a model with 10 items and the visible row count is 8. The
	 * number of columns needed to display this is 2, but you no longer need 8
	 * rows to display this, you only need 5, thus the adjustedRowCount is 5.
	 * <p>
	 * If the visible row count is <= 0, the preferred height is dictated by the
	 * number of columns, which will be as many as can fit in the width of the
	 * <code>JList</code> (width / max cell width), with at least one column. The
	 * preferred height then becomes the model size / number of columns * maximum
	 * cell height. Max cell height is either the fixed cell height, or is
	 * determined by iterating through all the cells to find the maximum height
	 * from the ListCellRenderer.
	 * </table>
	 * The above specifies the raw preferred width and height. The resulting
	 * preferred width is the above width + insets.left + insets.right and the
	 * resulting preferred height is the above height + insets.top +
	 * insets.bottom. Where the <code>Insets</code> are determined from
	 * <code>list.getInsets()</code>.
	 * 
	 * @param c
	 *          The JList component.
	 * @return The total size of the list.
	 */
	@Override
	public Dimension getPreferredSize(JComponent jc) {
		maybeUpdateLayoutState();
		return getListDimensions();
	}

	private Dimension getListDimensions() {
		int lastRow = list.getModel().getSize() - 1;
		if (lastRow < 0) {
			return new Dimension(0, 0);
		}
		Insets insets = list.getInsets();
		int width = cellWidth * columnCount + insets.left + insets.right;
		int height;

		if (layoutOrientation != JList.VERTICAL) {
			height = preferredHeight;
		} else {
			Rectangle bounds = getCellBounds1(list, lastRow);

			if (bounds != null) {
				height = bounds.y + bounds.height + insets.bottom;
			} else {
				height = 0;
			}
		}
		return new Dimension(width, height);
	}

	/**
	 * Selected the previous row and force it to be visible.
	 * 
	 * @see JList#ensureIndexIsVisible
	 */
	protected void selectPreviousIndex() {
		int s = list.getSelectedIndex();
		if (s > 0) {
			s -= 1;
			list.setSelectedIndex(s);
			list.ensureIndexIsVisible(s);
		}
	}

	/**
	 * Selected the previous row and force it to be visible.
	 * 
	 * @see JList#ensureIndexIsVisible
	 */
	protected void selectNextIndex() {
		int s = list.getSelectedIndex();
		if ((s + 1) < list.getModel().getSize()) {
			s += 1;
			list.setSelectedIndex(s);
			list.ensureIndexIsVisible(s);
		}
	}

	/**
	 * Registers the keyboard bindings on the <code>JList</code> that the
	 * <code>JSListUI</code> is associated with. This method is called at
	 * installUI() time.
	 * 
	 * @see #installUI
	 */
	protected void installKeyboardActions() {
		InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);

		SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED, inputMap);

		LazyActionMap.installLazyActionMap(list, JSListUI.class, "List.actionMap");
	}

	InputMap getInputMap(int condition) {
		if (condition == JComponent.WHEN_FOCUSED) {
			InputMap keyMap = (InputMap) DefaultLookup.get(list, this,
					"List.focusInputMap");
			InputMap rtlKeyMap;

			if (isLeftToRight
					|| ((rtlKeyMap = (InputMap) DefaultLookup.get(list, this,
							"List.focusInputMap.RightToLeft")) == null)) {
				return keyMap;
			} else {
				rtlKeyMap.setParent(keyMap);
				return rtlKeyMap;
			}
		}
		return null;
	}

	/**
	 * Unregisters keyboard actions installed from
	 * <code>installKeyboardActions</code>. This method is called at uninstallUI()
	 * time - subclassess should ensure that all of the keyboard actions
	 * registered at installUI time are removed here.
	 * 
	 * @see #installUI
	 */
	protected void uninstallKeyboardActions() {
		SwingUtilities.replaceUIActionMap(list, null);
		SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED, null);
	}

	/**
	 * Create and install the listeners for the JList, its model, and its
	 * selectionModel. This method is called at installUI() time.
	 * 
	 * @see #installUI
	 * @see #uninstallListeners
	 */
	protected void installListeners() {
		// TransferHandler th = list.getTransferHandler();
		// if (th == null || th instanceof UIResource) {
		// list.setTransferHandler(defaultTransferHandler);
		// // default TransferHandler doesn't support drop
		// // so we don't want drop handling
		// if (list.getDropTarget() instanceof UIResource) {
		list.setDropTarget(null);
		// }
		// }

		focusListener = createFocusListener();
		mouseInputListener = createMouseInputListener();
		propertyChangeListener = createPropertyChangeListener();
		listSelectionListener = createListSelectionListener();
		listDataListener = createListDataListener();

		list.addFocusListener(focusListener);
		list.addMouseListener(mouseInputListener);
		list.addMouseMotionListener(mouseInputListener);
		list.addPropertyChangeListener(propertyChangeListener);
		list.addKeyListener(getHandler());

		ListModel model = list.getModel();
		if (model != null) {
			model.addListDataListener(listDataListener);
		}

		ListSelectionModel selectionModel = list.getSelectionModel();
		if (selectionModel != null) {
			selectionModel.addListSelectionListener(listSelectionListener);
		}
	}

	/**
	 * Remove the listeners for the JList, its model, and its selectionModel. All
	 * of the listener fields, are reset to null here. This method is called at
	 * uninstallUI() time, it should be kept in sync with installListeners.
	 * 
	 * @see #uninstallUI
	 * @see #installListeners
	 */
	protected void uninstallListeners() {
		list.removeFocusListener(focusListener);
		list.removeMouseListener(mouseInputListener);
		list.removeMouseMotionListener(mouseInputListener);
		list.removePropertyChangeListener(propertyChangeListener);
		list.removeKeyListener(getHandler());

		ListModel model = list.getModel();
		if (model != null) {
			model.removeListDataListener(listDataListener);
		}

		ListSelectionModel selectionModel = list.getSelectionModel();
		if (selectionModel != null) {
			selectionModel.removeListSelectionListener(listSelectionListener);
		}

		focusListener = null;
		mouseInputListener = null;
		listSelectionListener = null;
		listDataListener = null;
		propertyChangeListener = null;
		handler = null;
	}

	/**
	 * Initialize JList properties, e.g. font, foreground, and background, and add
	 * the CellRendererPane. The font, foreground, and background properties are
	 * only set if their current value is either null or a UIResource, other
	 * properties are set if the current value is null.
	 * 
	 * @see #uninstallDefaults
	 * @see #installUI
	 * @see CellRendererPane
	 */
	protected void installDefaults() {
		list.setLayout(null);

		LookAndFeel.installBorder(list, "List.border");

		LookAndFeel.installColorsAndFont(list, "List.background",
				"List.foreground", "List.font");

		LookAndFeel.installProperty(list, "opaque", Boolean.TRUE);

		if (list.getCellRenderer() == null) {
			list.setCellRenderer((ListCellRenderer) (UIManager
					.get("List.cellRenderer")));
		}

		Color sbg = list.getSelectionBackground();
		if (sbg == null || sbg instanceof UIResource) {
			list.setSelectionBackground(UIManager
					.getColor("List.selectionBackground"));
		}

		Color sfg = list.getSelectionForeground();
		if (sfg == null || sfg instanceof UIResource) {
			list.setSelectionForeground(UIManager
					.getColor("List.selectionForeground"));
		}

		Long l = (Long) UIManager.get("List.timeFactor");
		timeFactor = (l != null) ? l.longValue() : 1000L;

		updateIsFileList();
	}

	void updateIsFileList() {
		boolean b = Boolean.TRUE.equals(list.getClientProperty("List.isFileList"));
		if (b != isFileList) {
			isFileList = b;
			Font oldFont = getFont();
			if (oldFont == null || oldFont instanceof UIResource) {
				Font newFont = UIManager.getFont(b ? "FileChooser.listFont"
						: "List.font");
				if (newFont != null && newFont != oldFont) {
					list.setFont(newFont);
				}
			}
		}
	}

	/**
	 * Set the JList properties that haven't been explicitly overridden to null. A
	 * property is considered overridden if its current value is not a UIResource.
	 * 
	 * @see #installDefaults
	 * @see #uninstallUI
	 * @see CellRendererPane
	 */
	protected void uninstallDefaults() {
		LookAndFeel.uninstallBorder(list);
		if (getFont() instanceof UIResource) {
			list.setFont(null);
		}
		if (list.getForeground() instanceof UIResource) {
			list.setForeground(null);
		}
		if (list.getBackground() instanceof UIResource) {
			list.setBackground(null);
		}
		if (list.getSelectionBackground() instanceof UIResource) {
			list.setSelectionBackground(null);
		}
		if (list.getSelectionForeground() instanceof UIResource) {
			list.setSelectionForeground(null);
		}
		if (list.getCellRenderer() instanceof UIResource) {
			list.setCellRenderer(null);
		}
		// if (list.getTransferHandler() instanceof UIResource) {
		// list.setTransferHandler(null);
		// }
	}

	/**
	 * Initializes <code>this.list</code> by calling
	 * <code>installDefaults()</code>, <code>installListeners()</code>, and
	 * <code>installKeyboardActions()</code> in order.
	 * 
	 * @see #installDefaults
	 * @see #installListeners
	 * @see #installKeyboardActions
	 */
	@Override
	public void installUI(JComponent c) {
		list = (JList) jc;

		layoutOrientation = list.getLayoutOrientation();

		rendererPane = new CellRendererPane();
		list.add(rendererPane);

		columnCount = 1;

		updateLayoutStateNeeded = modelChanged;
		isLeftToRight = list.getComponentOrientation().isLeftToRight();

		installDefaults();
		installListeners();
		installKeyboardActions();
	}

	/**
	 * Uninitializes <code>this.list</code> by calling
	 * <code>uninstallListeners()</code>, <code>uninstallKeyboardActions()</code>,
	 * and <code>uninstallDefaults()</code> in order. Sets this.list to null.
	 * 
	 * @see #uninstallListeners
	 * @see #uninstallKeyboardActions
	 * @see #uninstallDefaults
	 */
	@Override
	public void uninstallUI(JComponent c) {
		uninstallListeners();
		uninstallDefaults();
		uninstallKeyboardActions();

		cellWidth = cellHeight = -1;
		cellHeights = null;

		listWidth = listHeight = -1;

		list.remove(rendererPane);
		rendererPane = null;
		list = null;
	}

	// /**
	// * Returns a new instance of JSListUI. JSListUI delegates are allocated
	// * one per JList.
	// *
	// * @return A new ListUI implementation for the Windows look and feel.
	// */
	// public static ComponentUI createUI(JComponent list) {
	// return new JSListUI();
	// }

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException
	 *           {@inheritDoc}
	 */
	public int locationToIndex(JList list, Point location) {
		maybeUpdateLayoutState();
		return convertLocationToModel(location.x, location.y);
	}

	/**
	 * {@inheritDoc}
	 */
	public Point indexToLocation(JList list, int index) {
		maybeUpdateLayoutState();
		Rectangle rect = getCellBounds(list, index, index);

		if (rect != null) {
			return new Point(rect.x, rect.y);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Rectangle getCellBounds(JList list, int index1, int index2) {
		maybeUpdateLayoutState();

		int minIndex = Math.min(index1, index2);
		int maxIndex = Math.max(index1, index2);

		if (minIndex >= list.getModel().getSize()) {
			return null;
		}

		Rectangle minBounds = getCellBounds1(list, minIndex);

		if (minBounds == null) {
			return null;
		}
		if (minIndex == maxIndex) {
			return minBounds;
		}
		Rectangle maxBounds = getCellBounds1(list, maxIndex);

		if (maxBounds != null) {
			if (layoutOrientation == JList.HORIZONTAL_WRAP) {
				int minRow = convertModelToRow(minIndex);
				int maxRow = convertModelToRow(maxIndex);

				if (minRow != maxRow) {
					minBounds.x = 0;
					minBounds.width = list.getWidth();
				}
			} else if (minBounds.x != maxBounds.x) {
				// Different columns
				minBounds.y = 0;
				minBounds.height = list.getHeight();
			}
			minBounds.add(maxBounds);
		}
		return minBounds;
	}

	/**
	 * Gets the bounds of the specified model index, returning the resulting
	 * bounds, or null if <code>index</code> is not valid.
	 */
	private Rectangle getCellBounds1(JList list, int index) {
		maybeUpdateLayoutState();

		int row = convertModelToRow(index);
		int column = convertModelToColumn(index);

		if (row == -1 || column == -1) {
			return null;
		}

		Insets insets = list.getInsets();
		int x;
		int w = cellWidth;
		int y = insets.top;
		int h;
		switch (layoutOrientation) {
		case JList.VERTICAL_WRAP:
		case JList.HORIZONTAL_WRAP:
			if (isLeftToRight) {
				x = insets.left + column * cellWidth;
			} else {
				x = list.getWidth() - insets.right - (column + 1) * cellWidth;
			}
			y += cellHeight * row;
			h = cellHeight;
			break;
		default:
			x = insets.left;
			if (cellHeights == null) {
				y += (cellHeight * row);
			} else if (row >= cellHeights.length) {
				y = 0;
			} else {
				for (int i = 0; i < row; i++) {
					y += cellHeights[i];
				}
			}
			w = list.getWidth() - (insets.left + insets.right);
			h = getRowHeight(index);
			break;
		}
		return new Rectangle(x, y, w, h);
	}

	/**
	 * Returns the height of the specified row based on the current layout.
	 * 
	 * @return The specified row height or -1 if row isn't valid.
	 * @see #convertYToRow
	 * @see #convertRowToY
	 * @see #updateLayoutState
	 */
	protected int getRowHeight(int row) {
		return getHeight(0, row);
	}

	/**
	 * Convert the JList relative coordinate to the row that contains it, based on
	 * the current layout. If y0 doesn't fall within any row, return -1.
	 * 
	 * @return The row that contains y0, or -1.
	 * @see #getRowHeight
	 * @see #updateLayoutState
	 */
	protected int convertYToRow(int y0) {
		return convertLocationToRow(0, y0, false);
	}

	/**
	 * Return the JList relative Y coordinate of the origin of the specified row
	 * or -1 if row isn't valid.
	 * 
	 * @return The Y coordinate of the origin of row, or -1.
	 * @see #getRowHeight
	 * @see #updateLayoutState
	 */
	protected int convertRowToY(int row) {
		if (row >= getRowCount(0) || row < 0) {
			return -1;
		}
		Rectangle bounds = getCellBounds(list, row, row);
		return bounds.y;
	}

	/**
	 * Returns the height of the cell at the passed in location.
	 */
	private int getHeight(int column, int row) {
		if (column < 0 || column > columnCount || row < 0) {
			return -1;
		}
		if (layoutOrientation != JList.VERTICAL) {
			return cellHeight;
		}
		if (row >= list.getModel().getSize()) {
			return -1;
		}
		return (cellHeights == null) ? cellHeight
				: ((row < cellHeights.length) ? cellHeights[row] : -1);
	}

	/**
	 * Returns the row at location x/y.
	 * 
	 * @param closest
	 *          If true and the location doesn't exactly match a particular
	 *          location, this will return the closest row.
	 */
	private int convertLocationToRow(int x, int y0, boolean closest) {
		int size = list.getModel().getSize();

		if (size <= 0) {
			return -1;
		}
		Insets insets = list.getInsets();
		if (cellHeights == null) {
			int row = (cellHeight == 0) ? 0 : ((y0 - insets.top) / cellHeight);
			if (closest) {
				if (row < 0) {
					row = 0;
				} else if (row >= size) {
					row = size - 1;
				}
			}
			return row;
		} else if (size > cellHeights.length) {
			return -1;
		} else {
			int y = insets.top;
			int row = 0;

			if (closest && y0 < y) {
				return 0;
			}
			int i;
			for (i = 0; i < size; i++) {
				if ((y0 >= y) && (y0 < y + cellHeights[i])) {
					return row;
				}
				y += cellHeights[i];
				row += 1;
			}
			return i - 1;
		}
	}

	/**
	 * Returns the closest row that starts at the specified y-location in the
	 * passed in column.
	 */
	private int convertLocationToRowInColumn(int y, int column) {
		int x = 0;

		if (layoutOrientation != JList.VERTICAL) {
			if (isLeftToRight) {
				x = column * cellWidth;
			} else {
				x = list.getWidth() - (column + 1) * cellWidth - list.getInsets().right;
			}
		}
		return convertLocationToRow(x, y, true);
	}

	/**
	 * Returns the closest location to the model index of the passed in location.
	 */
	private int convertLocationToModel(int x, int y) {
		int row = convertLocationToRow(x, y, true);
		int column = convertLocationToColumn(x, y);

		if (row >= 0 && column >= 0) {
			return getModelIndex(column, row);
		}
		return -1;
	}

	/**
	 * Returns the number of rows in the given column.
	 */
	int getRowCount(int column) {
		if (column < 0 || column >= columnCount) {
			return -1;
		}
		if (layoutOrientation == JList.VERTICAL
				|| (column == 0 && columnCount == 1)) {
			return list.getModel().getSize();
		}
		if (column >= columnCount) {
			return -1;
		}
		if (layoutOrientation == JList.VERTICAL_WRAP) {
			if (column < (columnCount - 1)) {
				return rowsPerColumn;
			}
			return list.getModel().getSize() - (columnCount - 1) * rowsPerColumn;
		}
		// JList.HORIZONTAL_WRAP
		int diff = columnCount
				- (columnCount * rowsPerColumn - list.getModel().getSize());

		if (column >= diff) {
			return Math.max(0, rowsPerColumn - 1);
		}
		return rowsPerColumn;
	}

	/**
	 * Returns the model index for the specified display location. If
	 * <code>column</code>x<code>row</code> is beyond the length of the model,
	 * this will return the model size - 1.
	 */
	int getModelIndex(int column, int row) {
		switch (layoutOrientation) {
		case JList.VERTICAL_WRAP:
			return Math.min(list.getModel().getSize() - 1, rowsPerColumn * column
					+ Math.min(row, rowsPerColumn - 1));
		case JList.HORIZONTAL_WRAP:
			return Math
					.min(list.getModel().getSize() - 1, row * columnCount + column);
		default:
			return row;
		}
	}

	/**
	 * Returns the closest column to the passed in location.
	 */
	private int convertLocationToColumn(int x, int y) {
		if (cellWidth > 0) {
			if (layoutOrientation == JList.VERTICAL) {
				return 0;
			}
			Insets insets = list.getInsets();
			int col;
			if (isLeftToRight) {
				col = (x - insets.left) / cellWidth;
			} else {
				col = (list.getWidth() - x - insets.right - 1) / cellWidth;
			}
			if (col < 0) {
				return 0;
			} else if (col >= columnCount) {
				return columnCount - 1;
			}
			return col;
		}
		return 0;
	}

	/**
	 * Returns the row that the model index <code>index</code> will be displayed
	 * in..
	 */
	int convertModelToRow(int index) {
		int size = list.getModel().getSize();

		if ((index < 0) || (index >= size)) {
			return -1;
		}

		if (layoutOrientation != JList.VERTICAL && columnCount > 1
				&& rowsPerColumn > 0) {
			if (layoutOrientation == JList.VERTICAL_WRAP) {
				return index % rowsPerColumn;
			}
			return index / columnCount;
		}
		return index;
	}

	/**
	 * Returns the column that the model index <code>index</code> will be
	 * displayed in.
	 */
	int convertModelToColumn(int index) {
		int size = list.getModel().getSize();

		if ((index < 0) || (index >= size)) {
			return -1;
		}

		if (layoutOrientation != JList.VERTICAL && rowsPerColumn > 0
				&& columnCount > 1) {
			if (layoutOrientation == JList.VERTICAL_WRAP) {
				return index / rowsPerColumn;
			}
			return index % columnCount;
		}
		return 0;
	}

	/**
	 * If updateLayoutStateNeeded is non zero, call updateLayoutState() and reset
	 * updateLayoutStateNeeded. This method should be called by methods before
	 * doing any computation based on the geometry of the list. For example it's
	 * the first call in paint() and getPreferredSize().
	 * 
	 * @see #updateLayoutState
	 */
	protected void maybeUpdateLayoutState() {
		if (updateLayoutStateNeeded != 0) {
			updateLayoutStateNeeded = 0; // SwingJS switch of order here for getting
																		// actual size
			updateLayoutState();
		}
	}

	/**
	 * Recompute the value of cellHeight or cellHeights based and cellWidth, based
	 * on the current font and the current values of fixedCellWidth,
	 * fixedCellHeight, and prototypeCellValue.
	 * 
	 * @see #maybeUpdateLayoutState
	 */
	protected void updateLayoutState() {
		/*
		 * If both JList fixedCellWidth and fixedCellHeight have been set, then
		 * initialize cellWidth and cellHeight, and set cellHeights to null.
		 */

		int fixedCellHeight = list.getFixedCellHeight();
		int fixedCellWidth = list.getFixedCellWidth();

		cellWidth = (fixedCellWidth != -1) ? fixedCellWidth : -1;

		if (fixedCellHeight != -1) {
			cellHeight = fixedCellHeight;
			cellHeights = null;
		} else {
			cellHeight = -1;
			cellHeights = new int[list.getModel().getSize()];
		}

		/*
		 * If either of JList fixedCellWidth and fixedCellHeight haven't been set,
		 * then initialize cellWidth and cellHeights by scanning through the entire
		 * model. Note: if the renderer is null, we just set cellWidth and
		 * cellHeights[*] to zero, if they're not set already.
		 */

		if ((fixedCellWidth == -1) || (fixedCellHeight == -1)) {

			ListModel dataModel = list.getModel();
			int dataModelSize = dataModel.getSize();
			ListCellRenderer renderer = list.getCellRenderer();

			if (renderer != null) {
				for (int index = 0; index < dataModelSize; index++) {
					Object value = dataModel.getElementAt(index);
					Component c = renderer.getListCellRendererComponent(list, value,
							index, false, false);
					rendererPane.add(c);
					c.setSize(c.getPreferredSize());
					((JComponent) c).秘getUI().updateDOMNode();
					((JComponent) c).getInsets();
					Dimension cellSize = c.getPreferredSize();
					if (fixedCellWidth == -1) {
						cellWidth = Math.max(cellSize.width, cellWidth);
					}
					if (fixedCellHeight == -1) {
						cellHeights[index] = cellSize.height;
					}
				}
			} else {
				if (cellWidth == -1) {
					cellWidth = 0;
				}
				if (cellHeights == null) {
					cellHeights = new int[dataModelSize];
				}
				for (int index = 0; index < dataModelSize; index++) {
					cellHeights[index] = 0;
				}
			}
		}

		columnCount = 1;
		getSwingJSListActualSize(fixedCellWidth, fixedCellHeight);
		if (layoutOrientation != JList.VERTICAL) {
			updateHorizontalLayoutState(fixedCellWidth, fixedCellHeight);
		}
	}

	/**
	 * Invoked when the list is laid out horizontally to determine how many
	 * columns to create.
	 * <p>
	 * This updates the <code>rowsPerColumn, </code><code>columnCount</code>,
	 * <code>preferredHeight</code> and potentially <code>cellHeight</code>
	 * instance variables.
	 */
	private void updateHorizontalLayoutState(int fixedCellWidth,
			int fixedCellHeight) {
		getWrappedListDimensions(list.getVisibleRowCount(), fixedCellWidth,
				fixedCellHeight);
	}

	private void getSwingJSListActualSize(int fixedCellWidth, int fixedCellHeight) {
		if (layoutOrientation != JList.VERTICAL) {
			getWrappedListDimensions(list.getModel().getSize(), fixedCellWidth,
					fixedCellHeight);
		}
		Dimension d = getListDimensions();
		jsActualWidth = d.width;
		jsActualHeight = d.height;
	}

	private void getWrappedListDimensions(int visRows, int fixedCellWidth,
			int fixedCellHeight) {
		int dataModelSize = list.getModel().getSize();
		Insets insets = list.getInsets();

		listHeight = list.getHeight();
		listWidth = list.getWidth();

		if (dataModelSize == 0) {
			rowsPerColumn = columnCount = 0;
			preferredHeight = insets.top + insets.bottom;
			return;
		}

		int height;

		if (fixedCellHeight != -1) {
			height = fixedCellHeight;
		} else {
			// Determine the max of the renderer heights.
			int maxHeight = 0;
			if (cellHeights.length > 0) {
				maxHeight = cellHeights[cellHeights.length - 1];
				for (int counter = cellHeights.length - 2; counter >= 0; counter--) {
					maxHeight = Math.max(maxHeight, cellHeights[counter]);
				}
			}
			height = cellHeight = maxHeight;
			cellHeights = null;
		}
		// The number of rows is either determined by the visible row
		// count, or by the height of the list.
		rowsPerColumn = dataModelSize;
		if (visRows > 0) {
			rowsPerColumn = visRows;
			columnCount = Math.max(1, dataModelSize / rowsPerColumn);
			if (dataModelSize > 0 && dataModelSize > rowsPerColumn
					&& dataModelSize % rowsPerColumn != 0) {
				columnCount++;
			}
			if (layoutOrientation == JList.HORIZONTAL_WRAP) {
				// Because HORIZONTAL_WRAP flows differently, the
				// rowsPerColumn needs to be adjusted.
				rowsPerColumn = (dataModelSize / columnCount);
				if (dataModelSize % columnCount > 0) {
					rowsPerColumn++;
				}
			}
		} else if (layoutOrientation == JList.VERTICAL_WRAP && height != 0) {
			rowsPerColumn = Math.max(1, (listHeight - insets.top - insets.bottom)
					/ height);
			columnCount = Math.max(1, dataModelSize / rowsPerColumn);
			if (dataModelSize > 0 && dataModelSize > rowsPerColumn
					&& dataModelSize % rowsPerColumn != 0) {
				columnCount++;
			}
		} else if (layoutOrientation == JList.HORIZONTAL_WRAP && cellWidth > 0
				&& listWidth > 0) {
			columnCount = Math.max(1, (listWidth - insets.left - insets.right)
					/ cellWidth);
			rowsPerColumn = dataModelSize / columnCount;
			if (dataModelSize % columnCount > 0) {
				rowsPerColumn++;
			}
		}
		preferredHeight = rowsPerColumn * cellHeight + insets.top + insets.bottom;
	}

	Handler getHandler() {
		if (handler == null) {
			handler = new Handler(jc);
		}
		return handler;
	}

	/**
	 * Mouse input, and focus handling for JList. An instance of this class is
	 * added to the appropriate java.awt.Component lists at installUI() time. Note
	 * keyboard input is handled with JComponent KeyboardActions, see
	 * installKeyboardActions().
	 * <p>
	 * <strong>Warning:</strong> Serialized objects of this class will not be
	 * compatible with future Swing releases. The current serialization support is
	 * appropriate for short term storage or RMI between applications running the
	 * same version of Swing. As of 1.4, support for long term storage of all
	 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
	 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
	 * 
	 * @see #createMouseInputListener
	 * @see #installKeyboardActions
	 * @see #installUI
	 */
	public class MouseInputHandler implements MouseInputListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			getHandler().mouseClicked(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			getHandler().mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			getHandler().mouseExited(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			getHandler().mousePressed(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			getHandler().mouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			getHandler().mouseMoved(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			getHandler().mouseReleased(e);
		}
	}

	/**
	 * Creates a delegate that implements MouseInputListener. The delegate is
	 * added to the corresponding java.awt.Component listener lists at installUI()
	 * time. Subclasses can override this method to return a custom
	 * MouseInputListener, e.g.
	 * 
	 * <pre>
	 * class MyListUI extends JSListUI {
	 *    protected MouseInputListener <b>createMouseInputListener</b>() {
	 *        return new MyMouseInputHandler();
	 *    }
	 *    public class MyMouseInputHandler extends MouseInputHandler {
	 *        public void mouseMoved(MouseEvent e) {
	 *            // do some extra work when the mouse moves
	 *            super.mouseMoved(e);
	 *        }
	 *    }
	 * }
	 * </pre>
	 * 
	 * @see MouseInputHandler
	 * @see #installUI
	 */
	protected MouseInputListener createMouseInputListener() {
		return getHandler();
	}

	/**
	 * This inner class is marked &quot;public&quot; due to a compiler bug. This
	 * class should be treated as a &quot;protected&quot; inner class. Instantiate
	 * it only within subclasses of BasicTableUI.
	 */
	public class FocusHandler implements FocusListener {
		protected void repaintCellFocus() {
			getHandler().repaintCellFocus();
		}

		/*
		 * The focusGained() focusLost() methods run when the JList focus changes.
		 */

		@Override
		public void focusGained(FocusEvent e) {
			getHandler().focusGained(e);
		}

		@Override
		public void focusLost(FocusEvent e) {
			getHandler().focusLost(e);
		}
	}

	protected FocusListener createFocusListener() {
		return getHandler();
	}

	/**
	 * The ListSelectionListener that's added to the JLists selection model at
	 * installUI time, and whenever the JList.selectionModel property changes.
	 * When the selection changes we repaint the affected rows.
	 * <p>
	 * <strong>Warning:</strong> Serialized objects of this class will not be
	 * compatible with future Swing releases. The current serialization support is
	 * appropriate for short term storage or RMI between applications running the
	 * same version of Swing. As of 1.4, support for long term storage of all
	 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
	 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
	 * 
	 * @see #createListSelectionListener
	 * @see #getCellBounds
	 * @see #installUI
	 */
	public class ListSelectionHandler implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			getHandler().valueChanged(e);
		}
	}

	/**
	 * Creates an instance of ListSelectionHandler that's added to the JLists by
	 * selectionModel as needed. Subclasses can override this method to return a
	 * custom ListSelectionListener, e.g.
	 * 
	 * <pre>
	 * class MyListUI extends JSListUI {
	 *    protected ListSelectionListener <b>createListSelectionListener</b>() {
	 *        return new MySelectionListener();
	 *    }
	 *    public class MySelectionListener extends ListSelectionHandler {
	 *        public void valueChanged(ListSelectionEvent e) {
	 *            // do some extra work when the selection changes
	 *            super.valueChange(e);
	 *        }
	 *    }
	 * }
	 * </pre>
	 * 
	 * @see ListSelectionHandler
	 * @see #installUI
	 */
	protected ListSelectionListener createListSelectionListener() {
		return getHandler();
	}

	void redrawList() {
		needFilling = true;
		setTainted(true);
		list.revalidate();
		list.秘repaint();
	}

	/**
	 * The ListDataListener that's added to the JLists model at installUI time,
	 * and whenever the JList.model property changes.
	 * <p>
	 * <strong>Warning:</strong> Serialized objects of this class will not be
	 * compatible with future Swing releases. The current serialization support is
	 * appropriate for short term storage or RMI between applications running the
	 * same version of Swing. As of 1.4, support for long term storage of all
	 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
	 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
	 * 
	 * @see JList#getModel
	 * @see #maybeUpdateLayoutState
	 * @see #createListDataListener
	 * @see #installUI
	 */
	public class ListDataHandler implements ListDataListener {
		@Override
		public void intervalAdded(ListDataEvent e) {
			getHandler().intervalAdded(e);
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			getHandler().intervalRemoved(e);
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			getHandler().contentsChanged(e);
		}
	}

	/**
	 * Creates an instance of ListDataListener that's added to the JLists by model
	 * as needed. Subclasses can override this method to return a custom
	 * ListDataListener, e.g.
	 * 
	 * <pre>
	 * class MyListUI extends JSListUI {
	 *    protected ListDataListener <b>createListDataListener</b>() {
	 *        return new MyListDataListener();
	 *    }
	 *    public class MyListDataListener extends ListDataHandler {
	 *        public void contentsChanged(ListDataEvent e) {
	 *            // do some extra work when the models contents change
	 *            super.contentsChange(e);
	 *        }
	 *    }
	 * }
	 * </pre>
	 * 
	 * @see ListDataListener
	 * @see JList#getModel
	 * @see #installUI
	 */
	protected ListDataListener createListDataListener() {
		return getHandler();
	}

	/**
	 * The PropertyChangeListener that's added to the JList at installUI time.
	 * When the value of a JList property that affects layout changes, we set a
	 * bit in updateLayoutStateNeeded. If the JLists model changes we additionally
	 * remove our listeners from the old model. Likewise for the JList
	 * selectionModel.
	 * <p>
	 * <strong>Warning:</strong> Serialized objects of this class will not be
	 * compatible with future Swing releases. The current serialization support is
	 * appropriate for short term storage or RMI between applications running the
	 * same version of Swing. As of 1.4, support for long term storage of all
	 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
	 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
	 * 
	 * @see #maybeUpdateLayoutState
	 * @see #createPropertyChangeListener
	 * @see #installUI
	 */
	public class PropertyChangeHandler implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			getHandler().propertyChange(e);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);
	}

	/**
	 * Creates an instance of PropertyChangeHandler that's added to the JList by
	 * installUI(). Subclasses can override this method to return a custom
	 * PropertyChangeListener, e.g.
	 * 
	 * <pre>
	 * class MyListUI extends JSListUI {
	 *    protected PropertyChangeListener <b>createPropertyChangeListener</b>() {
	 *        return new MyPropertyChangeListener();
	 *    }
	 *    public class MyPropertyChangeListener extends PropertyChangeHandler {
	 *        public void propertyChange(PropertyChangeEvent e) {
	 *            if (e.getPropertyName().equals("model")) {
	 *                // do some extra work when the model changes
	 *            }
	 *            super.propertyChange(e);
	 *        }
	 *    }
	 * }
	 * </pre>
	 * 
	 * @see PropertyChangeListener
	 * @see #installUI
	 */
	protected PropertyChangeListener createPropertyChangeListener() {
		return getHandler();
	}

	/**
	 * Used by IncrementLeadSelectionAction. Indicates the action should change
	 * the lead, and not select it.
	 */
	private static final int CHANGE_LEAD = 0;
	/**
	 * Used by IncrementLeadSelectionAction. Indicates the action should change
	 * the selection and lead.
	 */
	private static final int CHANGE_SELECTION = 1;
	/**
	 * Used by IncrementLeadSelectionAction. Indicates the action should extend
	 * the selection from the anchor to the next index.
	 */
	private static final int EXTEND_SELECTION = 2;

	private static class Actions extends UIAction {
		private static final String SELECT_PREVIOUS_COLUMN = "selectPreviousColumn";
		private static final String SELECT_PREVIOUS_COLUMN_EXTEND = "selectPreviousColumnExtendSelection";
		private static final String SELECT_PREVIOUS_COLUMN_CHANGE_LEAD = "selectPreviousColumnChangeLead";
		private static final String SELECT_NEXT_COLUMN = "selectNextColumn";
		private static final String SELECT_NEXT_COLUMN_EXTEND = "selectNextColumnExtendSelection";
		private static final String SELECT_NEXT_COLUMN_CHANGE_LEAD = "selectNextColumnChangeLead";
		private static final String SELECT_PREVIOUS_ROW = "selectPreviousRow";
		private static final String SELECT_PREVIOUS_ROW_EXTEND = "selectPreviousRowExtendSelection";
		private static final String SELECT_PREVIOUS_ROW_CHANGE_LEAD = "selectPreviousRowChangeLead";
		private static final String SELECT_NEXT_ROW = "selectNextRow";
		private static final String SELECT_NEXT_ROW_EXTEND = "selectNextRowExtendSelection";
		private static final String SELECT_NEXT_ROW_CHANGE_LEAD = "selectNextRowChangeLead";
		private static final String SELECT_FIRST_ROW = "selectFirstRow";
		private static final String SELECT_FIRST_ROW_EXTEND = "selectFirstRowExtendSelection";
		private static final String SELECT_FIRST_ROW_CHANGE_LEAD = "selectFirstRowChangeLead";
		private static final String SELECT_LAST_ROW = "selectLastRow";
		private static final String SELECT_LAST_ROW_EXTEND = "selectLastRowExtendSelection";
		private static final String SELECT_LAST_ROW_CHANGE_LEAD = "selectLastRowChangeLead";
		private static final String SCROLL_UP = "scrollUp";
		private static final String SCROLL_UP_EXTEND = "scrollUpExtendSelection";
		private static final String SCROLL_UP_CHANGE_LEAD = "scrollUpChangeLead";
		private static final String SCROLL_DOWN = "scrollDown";
		private static final String SCROLL_DOWN_EXTEND = "scrollDownExtendSelection";
		private static final String SCROLL_DOWN_CHANGE_LEAD = "scrollDownChangeLead";
		private static final String SELECT_ALL = "selectAll";
		private static final String CLEAR_SELECTION = "clearSelection";

		// add the lead item to the selection without changing lead or anchor
		private static final String ADD_TO_SELECTION = "addToSelection";

		// toggle the selected state of the lead item and move the anchor to it
		private static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";

		// extend the selection to the lead item
		private static final String EXTEND_TO = "extendTo";

		// move the anchor to the lead and ensure only that item is selected
		private static final String MOVE_SELECTION_TO = "moveSelectionTo";

		Actions(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String name = getName();
			JList list = (JList) e.getSource();
			JSListUI ui = (JSListUI) list.getUI();
			// JSListUI ui = (JSListUI) BasicLookAndFeel.getUIOfType(list.getUI(),
			// JSListUI.class);

			int index;
			switch (name) {
			case SELECT_PREVIOUS_COLUMN:
				changeSelection(list, CHANGE_SELECTION, getNextColumnIndex(list, ui, -1), -1);
				break;
			case SELECT_PREVIOUS_COLUMN_EXTEND:
				changeSelection(list, EXTEND_SELECTION, getNextColumnIndex(list, ui, -1), -1);
				break;
			case SELECT_PREVIOUS_COLUMN_CHANGE_LEAD:
				changeSelection(list, CHANGE_LEAD, getNextColumnIndex(list, ui, -1), -1);
				break;
			case SELECT_NEXT_COLUMN:
				changeSelection(list, CHANGE_SELECTION, getNextColumnIndex(list, ui, 1), 1);
				break;
			case SELECT_NEXT_COLUMN_EXTEND:
				changeSelection(list, EXTEND_SELECTION, getNextColumnIndex(list, ui, 1), 1);
				break;
			case SELECT_NEXT_COLUMN_CHANGE_LEAD:
				changeSelection(list, CHANGE_LEAD, getNextColumnIndex(list, ui, 1), 1);
				break;
			case SELECT_PREVIOUS_ROW:
				changeSelection(list, CHANGE_SELECTION, getNextIndex(list, ui, -1), -1);
				break;
			case SELECT_PREVIOUS_ROW_EXTEND:
				changeSelection(list, EXTEND_SELECTION, getNextIndex(list, ui, -1), -1);
				break;
			case SELECT_PREVIOUS_ROW_CHANGE_LEAD:
				changeSelection(list, CHANGE_LEAD, getNextIndex(list, ui, -1), -1);
				break;
			case SELECT_NEXT_ROW:
				changeSelection(list, CHANGE_SELECTION, getNextIndex(list, ui, 1), 1);
				break;
			case SELECT_NEXT_ROW_EXTEND:
				changeSelection(list, EXTEND_SELECTION, getNextIndex(list, ui, 1), 1);
				break;
			case SELECT_NEXT_ROW_CHANGE_LEAD:
				changeSelection(list, CHANGE_LEAD, getNextIndex(list, ui, 1), 1);
				break;
			case SELECT_FIRST_ROW:
				changeSelection(list, CHANGE_SELECTION, 0, -1);
				break;
			case SELECT_FIRST_ROW_EXTEND:
				changeSelection(list, EXTEND_SELECTION, 0, -1);
				break;
			case SELECT_FIRST_ROW_CHANGE_LEAD:
				changeSelection(list, CHANGE_LEAD, 0, -1);
				break;
			case SELECT_LAST_ROW:
				changeSelection(list, CHANGE_SELECTION, list.getModel().getSize() - 1, 1);
				break;
			case SELECT_LAST_ROW_EXTEND:
				changeSelection(list, EXTEND_SELECTION, list.getModel().getSize() - 1, 1);
				break;
			case SELECT_LAST_ROW_CHANGE_LEAD:
				changeSelection(list, CHANGE_LEAD, list.getModel().getSize() - 1, 1);
				break;
			case SCROLL_UP:
				changeSelection(list, CHANGE_SELECTION, getNextPageIndex(list, -1), -1);
				break;
			case SCROLL_UP_EXTEND:
				changeSelection(list, EXTEND_SELECTION, getNextPageIndex(list, -1), -1);
				break;
			case SCROLL_UP_CHANGE_LEAD:
				changeSelection(list, CHANGE_LEAD, getNextPageIndex(list, -1), -1);
				break;
			case SCROLL_DOWN:
				changeSelection(list, CHANGE_SELECTION, getNextPageIndex(list, 1), 1);
				break;
			case SCROLL_DOWN_EXTEND:
				changeSelection(list, EXTEND_SELECTION, getNextPageIndex(list, 1), 1);
				break;
			case SCROLL_DOWN_CHANGE_LEAD:
				changeSelection(list, CHANGE_LEAD, getNextPageIndex(list, 1), 1);
				break;
			case SELECT_ALL:
				selectAll(list);
				break;
			case CLEAR_SELECTION:
				clearSelection(list);
				break;
			case ADD_TO_SELECTION:
				index = adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list);

				if (!list.isSelectedIndex(index)) {
					int oldAnchor = list.getSelectionModel().getAnchorSelectionIndex();
					list.setValueIsAdjusting(true);
					list.addSelectionInterval(index, index);
					list.getSelectionModel().setAnchorSelectionIndex(oldAnchor);
					list.setValueIsAdjusting(false);
				}
				break;
			case TOGGLE_AND_ANCHOR:
				index = adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list);

				if (list.isSelectedIndex(index)) {
					list.removeSelectionInterval(index, index);
				} else {
					list.addSelectionInterval(index, index);
				}
				break;
			case EXTEND_TO:
				changeSelection(list, EXTEND_SELECTION,
						adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list), 0);
				break;
			case MOVE_SELECTION_TO:
				changeSelection(list, CHANGE_SELECTION,
						adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list), 0);
			}
		}

		@Override
		public boolean isEnabled(Object c) {
			Object name = getName();
			if (name == SELECT_PREVIOUS_COLUMN_CHANGE_LEAD
					|| name == SELECT_NEXT_COLUMN_CHANGE_LEAD
					|| name == SELECT_PREVIOUS_ROW_CHANGE_LEAD
					|| name == SELECT_NEXT_ROW_CHANGE_LEAD
					|| name == SELECT_FIRST_ROW_CHANGE_LEAD
					|| name == SELECT_LAST_ROW_CHANGE_LEAD
					|| name == SCROLL_UP_CHANGE_LEAD || name == SCROLL_DOWN_CHANGE_LEAD) {

				// discontinuous selection actions are only enabled for
				// DefaultListSelectionModel
				return c != null
						&& ((JList) c).getSelectionModel() instanceof DefaultListSelectionModel;
			}

			return true;
		}

		private void clearSelection(JList list) {
			list.clearSelection();
		}

		private void selectAll(JList list) {
			int size = list.getModel().getSize();
			if (size > 0) {
				ListSelectionModel lsm = list.getSelectionModel();
				int lead = adjustIndex(lsm.getLeadSelectionIndex(), list);

				if (lsm.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
					if (lead == -1) {
						int min = adjustIndex(list.getMinSelectionIndex(), list);
						lead = (min == -1 ? 0 : min);
					}

					list.setSelectionInterval(lead, lead);
					list.ensureIndexIsVisible(lead);
				} else {
					list.setValueIsAdjusting(true);

					int anchor = adjustIndex(lsm.getAnchorSelectionIndex(), list);

					list.setSelectionInterval(0, size - 1);

					// this is done to restore the anchor and lead
					SwingUtilities2.setLeadAnchorWithoutSelection(lsm, anchor, lead);

					list.setValueIsAdjusting(false);
				}
			}
		}

		private int getNextPageIndex(JList list, int direction) {
			if (list.getModel().getSize() == 0) {
				return -1;
			}

			int index = -1;
			Rectangle visRect = list.getVisibleRect();
			ListSelectionModel lsm = list.getSelectionModel();
			int lead = adjustIndex(lsm.getLeadSelectionIndex(), list);
			Rectangle leadRect = (lead == -1) ? new Rectangle() : list.getCellBounds(
					lead, lead);

			if (list.getLayoutOrientation() == JList.VERTICAL_WRAP
					&& list.getVisibleRowCount() <= 0) {
				if (!list.getComponentOrientation().isLeftToRight()) {
					direction = -direction;
				}
				// apply for horizontal scrolling: the step for next
				// page index is number of visible columns
				if (direction < 0) {
					// left
					visRect.x = leadRect.x + leadRect.width - visRect.width;
					Point p = new Point(visRect.x - 1, leadRect.y);
					index = list.locationToIndex(p);
					Rectangle cellBounds = list.getCellBounds(index, index);
					if (visRect.intersects(cellBounds)) {
						p.x = cellBounds.x - 1;
						index = list.locationToIndex(p);
						cellBounds = list.getCellBounds(index, index);
					}
					// this is necessary for right-to-left orientation only
					if (cellBounds.y != leadRect.y) {
						p.x = cellBounds.x + cellBounds.width;
						index = list.locationToIndex(p);
					}
				} else {
					// right
					visRect.x = leadRect.x;
					Point p = new Point(visRect.x + visRect.width, leadRect.y);
					index = list.locationToIndex(p);
					Rectangle cellBounds = list.getCellBounds(index, index);
					if (visRect.intersects(cellBounds)) {
						p.x = cellBounds.x + cellBounds.width;
						index = list.locationToIndex(p);
						cellBounds = list.getCellBounds(index, index);
					}
					if (cellBounds.y != leadRect.y) {
						p.x = cellBounds.x - 1;
						index = list.locationToIndex(p);
					}
				}
			} else {
				if (direction < 0) {
					// up
					// go to the first visible cell
					Point p = new Point(leadRect.x, visRect.y);
					index = list.locationToIndex(p);
					if (lead <= index) {
						// if lead is the first visible cell (or above it)
						// adjust the visible rect up
						visRect.y = leadRect.y + leadRect.height - visRect.height;
						p.y = visRect.y;
						index = list.locationToIndex(p);
						Rectangle cellBounds = list.getCellBounds(index, index);
						// go one cell down if first visible cell doesn't fit
						// into adjasted visible rectangle
						if (cellBounds.y < visRect.y) {
							p.y = cellBounds.y + cellBounds.height;
							index = list.locationToIndex(p);
							cellBounds = list.getCellBounds(index, index);
						}
						// if index isn't less then lead
						// try to go to cell previous to lead
						if (cellBounds.y >= leadRect.y) {
							p.y = leadRect.y - 1;
							index = list.locationToIndex(p);
						}
					}
				} else {
					// down
					// go to the last completely visible cell
					Point p = new Point(leadRect.x, visRect.y + visRect.height - 1);
					index = list.locationToIndex(p);
					Rectangle cellBounds = list.getCellBounds(index, index);
					// go up one cell if last visible cell doesn't fit
					// into visible rectangle
					if (cellBounds.y + cellBounds.height > visRect.y + visRect.height) {
						p.y = cellBounds.y - 1;
						index = list.locationToIndex(p);
						cellBounds = list.getCellBounds(index, index);
						index = Math.max(index, lead);
					}

					if (lead >= index) {
						// if lead is the last completely visible index
						// (or below it) adjust the visible rect down
						visRect.y = leadRect.y;
						p.y = visRect.y + visRect.height - 1;
						index = list.locationToIndex(p);
						cellBounds = list.getCellBounds(index, index);
						// go one cell up if last visible cell doesn't fit
						// into adjasted visible rectangle
						if (cellBounds.y + cellBounds.height > visRect.y + visRect.height) {
							p.y = cellBounds.y - 1;
							index = list.locationToIndex(p);
							cellBounds = list.getCellBounds(index, index);
						}
						// if index isn't greater then lead
						// try to go to cell next after lead
						if (cellBounds.y <= leadRect.y) {
							p.y = leadRect.y + leadRect.height;
							index = list.locationToIndex(p);
						}
					}
				}
			}
			return index;
		}

		private void changeSelection(JList list, int type, int index, int direction) {
			if (index >= 0 && index < list.getModel().getSize()) {
				ListSelectionModel lsm = list.getSelectionModel();

				// CHANGE_LEAD is only valid with multiple interval selection
				if (type == CHANGE_LEAD
						&& list.getSelectionMode() != ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {

					type = CHANGE_SELECTION;
				}

				// importANT - This needs to happen before the index is changed.
				// This is because JFileChooser, which uses JList, also scrolls
				// the selected item into view. If that happens first, then
				// this method becomes a no-op.
				adjustScrollPositionIfNecessary(list, index, direction);

				if (type == EXTEND_SELECTION) {
					int anchor = adjustIndex(lsm.getAnchorSelectionIndex(), list);
					if (anchor == -1) {
						anchor = 0;
					}

					list.setSelectionInterval(anchor, index);
				} else if (type == CHANGE_SELECTION) {
					list.setSelectedIndex(index);
				} else {
					// casting should be safe since the action is only enabled
					// for DefaultListSelectionModel
					((DefaultListSelectionModel) lsm).moveLeadSelectionIndex(index);
				}
			}
		}

		/**
		 * When scroll down makes selected index the last completely visible index.
		 * When scroll up makes selected index the first visible index. Adjust
		 * visible rectangle respect to list's component orientation.
		 */
		private void adjustScrollPositionIfNecessary(JList list, int index,
				int direction) {
			if (direction == 0) {
				return;
			}
			Rectangle cellBounds = list.getCellBounds(index, index);
			Rectangle visRect = list.getVisibleRect();
			if (cellBounds != null && !visRect.contains(cellBounds)) {
				if (list.getLayoutOrientation() == JList.VERTICAL_WRAP
						&& list.getVisibleRowCount() <= 0) {
					// horizontal
					if (list.getComponentOrientation().isLeftToRight()) {
						if (direction > 0) {
							// right for left-to-right
							int x = Math.max(0, cellBounds.x + cellBounds.width
									- visRect.width);
							int startIndex = list.locationToIndex(new Point(x, cellBounds.y));
							Rectangle startRect = list.getCellBounds(startIndex, startIndex);
							if (startRect.x < x && startRect.x < cellBounds.x) {
								startRect.x += startRect.width;
								startIndex = list.locationToIndex(startRect.getLocation());
								startRect = list.getCellBounds(startIndex, startIndex);
							}
							cellBounds = startRect;
						}
						cellBounds.width = visRect.width;
					} else {
						if (direction > 0) {
							// left for right-to-left
							int x = cellBounds.x + visRect.width;
							int rightIndex = list.locationToIndex(new Point(x, cellBounds.y));
							Rectangle rightRect = list.getCellBounds(rightIndex, rightIndex);
							if (rightRect.x + rightRect.width > x
									&& rightRect.x > cellBounds.x) {
								rightRect.width = 0;
							}
							cellBounds.x = Math.max(0, rightRect.x + rightRect.width
									- visRect.width);
							cellBounds.width = visRect.width;
						} else {
							cellBounds.x += Math.max(0, cellBounds.width - visRect.width);
							// adjust width to fit into visible rectangle
							cellBounds.width = Math.min(cellBounds.width, visRect.width);
						}
					}
				} else {
					// vertical
					if (direction > 0) {
						// down
						int y = Math.max(0, cellBounds.y + cellBounds.height
								- visRect.height);
						int startIndex = list.locationToIndex(new Point(cellBounds.x, y));
						Rectangle startRect = list.getCellBounds(startIndex, startIndex);
						if (startRect.y < y && startRect.y < cellBounds.y) {
							startRect.y += startRect.height;
							startIndex = list.locationToIndex(startRect.getLocation());
							startRect = list.getCellBounds(startIndex, startIndex);
						}
						cellBounds = startRect;
						cellBounds.height = visRect.height;
					} else {
						// adjust height to fit into visible rectangle
						cellBounds.height = Math.min(cellBounds.height, visRect.height);
					}
				}
				list.scrollRectToVisible(cellBounds);
			}
		}

		private int getNextColumnIndex(JList list, JSListUI ui, int amount) {
			if (list.getLayoutOrientation() != JList.VERTICAL) {
				int index = adjustIndex(list.getLeadSelectionIndex(), list);
				int size = list.getModel().getSize();

				if (index == -1) {
					return 0;
				} else if (size == 1) {
					// there's only one item so we should select it
					return 0;
				} else if (ui == null || ui.columnCount <= 1) {
					return -1;
				}

				int column = ui.convertModelToColumn(index);
				int row = ui.convertModelToRow(index);

				column += amount;
				if (column >= ui.columnCount || column < 0) {
					// No wrapping.
					return -1;
				}
				int maxRowCount = ui.getRowCount(column);
				if (row >= maxRowCount) {
					return -1;
				}
				return ui.getModelIndex(column, row);
			}
			// Won't change the selection.
			return -1;
		}

		private int getNextIndex(JList list, JSListUI ui, int amount) {
			int index = adjustIndex(list.getLeadSelectionIndex(), list);
			int size = list.getModel().getSize();

			if (index == -1) {
				if (size > 0) {
					if (amount > 0) {
						index = 0;
					} else {
						index = size - 1;
					}
				}
			} else if (size == 1) {
				// there's only one item so we should select it
				index = 0;
			} else if (list.getLayoutOrientation() == JList.HORIZONTAL_WRAP) {
				if (ui != null) {
					index += ui.columnCount * amount;
				}
			} else {
				index += amount;
			}

			return index;
		}
	}

	private class Handler implements FocusListener, KeyListener,
			ListDataListener, ListSelectionListener, MouseInputListener,
			PropertyChangeListener {// /, BeforeDrag {
		//
		// KeyListener
		//
		private String prefix = "";
		private String typedString = "";
		private long lastTime = 0L;

		public Handler(JComponent jc) {
		}

		/**
		 * Invoked when a key has been typed.
		 * 
		 * Moves the keyboard focus to the first element whose prefix matches the
		 * sequence of alphanumeric keys pressed by the user with delay less than
		 * value of <code>timeFactor</code> property (or 1000 milliseconds if it is
		 * not defined). Subsequent same key presses move the keyboard focus to the
		 * next object that starts with the same letter until another key is
		 * pressed, then it is treated as the prefix with appropriate number of the
		 * same letters followed by first typed another letter.
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			JList src = (JList) e.getSource();
			ListModel model = src.getModel();

			if (model.getSize() == 0 || e.isAltDown() || e.isControlDown()
					|| e.isMetaDown() || isNavigationKey(e)) {
				// Nothing to select
				return;
			}
			boolean startingFromSelection = true;

			char c = e.getKeyChar();

			long time = e.getWhen();
			int startIndex = adjustIndex(src.getLeadSelectionIndex(), list);
			if (time - lastTime < timeFactor) {
				typedString += c;
				if ((prefix.length() == 1) && (c == prefix.charAt(0))) {
					// Subsequent same key presses move the keyboard focus to the next
					// object that starts with the same letter.
					startIndex++;
				} else {
					prefix = typedString;
				}
			} else {
				startIndex++;
				typedString = "" + c;
				prefix = typedString;
			}
			lastTime = time;

			if (startIndex < 0 || startIndex >= model.getSize()) {
				startingFromSelection = false;
				startIndex = 0;
			}
			int index = src.getNextMatch(prefix, startIndex, Position.Bias.Forward);
			if (index >= 0) {
				src.setSelectedIndex(index);
				src.ensureIndexIsVisible(index);
			} else if (startingFromSelection) { // wrap
				index = src.getNextMatch(prefix, 0, Position.Bias.Forward);
				if (index >= 0) {
					src.setSelectedIndex(index);
					src.ensureIndexIsVisible(index);
				}
			}
		}

		/**
		 * Invoked when a key has been pressed.
		 * 
		 * Checks to see if the key event is a navigation key to prevent dispatching
		 * these keys for the first letter navigation.
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if (isNavigationKey(e)) {
				prefix = "";
				typedString = "";
				lastTime = 0L;
			}
		}

		/**
		 * Invoked when a key has been released. See the class description for
		 * {@link KeyEvent} for a definition of a key released event.
		 */
		@Override
		public void keyReleased(KeyEvent e) {
		}

		/**
		 * Returns whether or not the supplied key event maps to a key that is used
		 * for navigation. This is used for optimizing key input by only passing
		 * non- navigation keys to the first letter navigation mechanism.
		 */
		private boolean isNavigationKey(KeyEvent event) {
			InputMap inputMap = list
					.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			KeyStroke key = KeyStroke.getKeyStrokeForEvent(event);

			if (inputMap != null && inputMap.get(key) != null) {
				return true;
			}
			return false;
		}

		//
		// PropertyChangeListener
		//
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			String propertyName = e.getPropertyName();
			/*
			 * If the JList.model property changes, remove our listener, listDataListener
			 * from the old model and add it to the new one.
			 */
			switch (propertyName) {
			case "model":
				ListModel oldModel = (ListModel) e.getOldValue();
				ListModel newModel = (ListModel) e.getNewValue();
				if (oldModel != null) {
					oldModel.removeListDataListener(listDataListener);
				}
				if (newModel != null) {
					newModel.addListDataListener(listDataListener);
				}
				updateLayoutStateNeeded |= modelChanged;
				redrawList();

				/*
				 * If the JList.selectionModel property changes, remove our listener,
				 * listSelectionListener from the old selectionModel and add it to the new one.
				 */
				break;
			case "selectionModel":
				ListSelectionModel oldModel2 = (ListSelectionModel) e.getOldValue();
				ListSelectionModel newModel2 = (ListSelectionModel) e.getNewValue();
				if (oldModel2 != null) {
					oldModel2.removeListSelectionListener(listSelectionListener);
				}
				if (newModel2 != null) {
					newModel2.addListSelectionListener(listSelectionListener);
				}
				updateLayoutStateNeeded |= modelChanged;
				redrawList();
				break;
			case "cellRenderer":
				updateLayoutStateNeeded |= cellRendererChanged;
				redrawList();
				break;
			case "font":
				updateLayoutStateNeeded |= fontChanged;
				redrawList();
				break;
			case "prototypeCellValue":
				updateLayoutStateNeeded |= prototypeCellValueChanged;
				redrawList();
				break;
			case "fixedCellHeight":
				updateLayoutStateNeeded |= fixedCellHeightChanged;
				redrawList();
				break;
			case "fixedCellWidth":
				updateLayoutStateNeeded |= fixedCellWidthChanged;
				redrawList();
				break;
			case "selectionForeground":
				list.秘repaint();
				break;
			case "selectionBackground":
				list.秘repaint();
				break;
			case "layoutOrientation":
				updateLayoutStateNeeded |= layoutOrientationChanged;
				layoutOrientation = list.getLayoutOrientation();
				redrawList();
				break;
			case "visibleRowCount":
				if (layoutOrientation != JList.VERTICAL) {
					updateLayoutStateNeeded |= layoutOrientationChanged;
					redrawList();
				}
				break;
			case "componentOrientation":
				isLeftToRight = list.getComponentOrientation().isLeftToRight();
				updateLayoutStateNeeded |= componentOrientationChanged;
				redrawList();
				InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
				SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED, inputMap);
				break;
			case "List.isFileList":
				updateIsFileList();
				redrawList();
				break;
			case "dropLocation":
				JSUtil.notImplemented("dropLocation");
				// JList.DropLocation oldValue = (JList.DropLocation) e.getOldValue();
				// repaintDropLocation(oldValue);
				// repaintDropLocation(list.getDropLocation());
				break;
			}
		}

		// private void repaintDropLocation(JList.DropLocation loc) {
		// if (loc == null) {
		// return;
		// }
		//
		// Rectangle r;
		//
		// if (loc.isInsert()) {
		// r = getDropLineRect(loc);
		// } else {
		// r = getCellBounds(list, loc.getIndex());
		// }
		//
		// if (r != null) {
		// list.repaint(r);
		// }
		// }

		//
		// ListDataListener
		//
		@Override
		public void intervalAdded(ListDataEvent e) {
			updateLayoutStateNeeded = modelChanged;

			int minIndex = Math.min(e.getIndex0(), e.getIndex1());
			int maxIndex = Math.max(e.getIndex0(), e.getIndex1());
			/*
			 * Sync the SelectionModel with the DataModel.
			 */

			ListSelectionModel sm = list.getSelectionModel();
			if (sm != null) {
				sm.insertIndexInterval(minIndex, maxIndex - minIndex + 1, true);
			}

			/*
			 * Repaint the entire list, from the origin of the first added cell, to
			 * the bottom of the component.
			 */
			redrawList();
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			updateLayoutStateNeeded = modelChanged;
			/*
			 * Sync the SelectionModel with the DataModel.
			 */

			ListSelectionModel sm = list.getSelectionModel();
			if (sm != null) {
				int minIndex = Math.min(e.getIndex0(), e.getIndex1());
				int maxIndex = Math.max(e.getIndex0(), e.getIndex1());
				sm.removeIndexInterval(minIndex, maxIndex);
				removeItemHTML(minIndex, maxIndex);
			}

			/*
			 * Repaint the entire list, from the origin of the first removed cell, to
			 * the bottom of the component.
			 */

			redrawList();
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			updateLayoutStateNeeded = modelChanged;
			redrawList();
		}

		//
		// ListSelectionListener
		//
		@Override
		public void valueChanged(ListSelectionEvent e) {
			maybeUpdateLayoutState();

			int size = list.getModel().getSize();
			int firstIndex = Math.min(size - 1, Math.max(e.getFirstIndex(), 0));
			int lastIndex = Math.min(size - 1, Math.max(e.getLastIndex(), 0));

			Rectangle bounds = getCellBounds(list, firstIndex, lastIndex);
			list.ensureIndexIsVisible(firstIndex);
			if (lastIndex != firstIndex)
				list.ensureIndexIsVisible(lastIndex);
			if (bounds != null) {
				list.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
			}
		}

		//
		// MouseListener
		//
		@Override
		public void mouseClicked(MouseEvent e) {
			list.秘processUIEvent(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			list.秘processUIEvent(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			list.秘processUIEvent(e);
		}

		// Whether or not the mouse press (which is being considered as part
		// of a drag sequence) also caused the selection change to be fully
		// processed.
		@SuppressWarnings("unused")
		private boolean dragPressDidSelection;

		@Override
		public void mousePressed(MouseEvent e) {
			if (list.秘processUIEvent(e)) {
				return;
			}

			boolean dragEnabled = list.getDragEnabled();
			boolean grabFocus = true;

			// different behavior if drag is enabled
			if (dragEnabled) {
				int row = SwingUtilities2.loc2IndexFileList(list, e.getPoint());
				// if we have a valid row and this is a drag initiating event
				if (row != -1) {// && DragRecognitionSupport.mousePressed(e)) {
					dragPressDidSelection = false;

					if (e.isControlDown()) {
						// do nothing for control - will be handled on release
						// or when drag starts
						return;
					} else if (!e.isShiftDown() && list.isSelectedIndex(row)) {
						// clicking on something that's already selected
						// and need to make it the lead now
						list.addSelectionInterval(row, row);
						return;
					}

					// could be a drag initiating event - don't grab focus
					grabFocus = false;

					dragPressDidSelection = true;
				}
			} else {
				// When drag is enabled mouse drags won't change the selection
				// in the list, so we only set the isAdjusting flag when it's
				// not enabled
				list.setValueIsAdjusting(true);
			}

			if (grabFocus) {
				SwingUtilities2.adjustFocus(list);
			}

			adjustSelection(e);
		}

		private void adjustSelection(MouseEvent e) {
			int row = SwingUtilities2.loc2IndexFileList(list, e.getPoint());
			if (row < 0) {
				// If shift is down in multi-select, we should do nothing.
				// For single select or non-shift-click, clear the selection
				if (isFileList
						&& e.getID() == MouseEvent.MOUSE_PRESSED
						&& (!e.isShiftDown() || list.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION)) {
					list.clearSelection();
				}
			} else {
				int anchorIndex = adjustIndex(list.getAnchorSelectionIndex(), list);
				boolean anchorSelected;
				if (anchorIndex == -1) {
					anchorIndex = 0;
					anchorSelected = false;
				} else {
					anchorSelected = list.isSelectedIndex(anchorIndex);
				}

				if (e.isControlDown()) {
					if (e.isShiftDown()) {
						if (anchorSelected) {
							list.addSelectionInterval(anchorIndex, row);
						} else {
							list.removeSelectionInterval(anchorIndex, row);
							if (isFileList) {
								list.addSelectionInterval(row, row);
								list.getSelectionModel().setAnchorSelectionIndex(anchorIndex);
							}
						}
					} else if (list.isSelectedIndex(row)) {
						list.removeSelectionInterval(row, row);
					} else {
						list.addSelectionInterval(row, row);
					}
				} else if (e.isShiftDown()) {
					list.setSelectionInterval(anchorIndex, row);
				} else {
					list.setSelectionInterval(row, row);
				}
			}
		}

		// public void dragStarting(MouseEvent me) {
		// if (me.isControlDown()) {
		// int row = SwingUtilities2.loc2IndexFileList(list, me.getPoint());
		// list.addSelectionInterval(row, row);
		// }
		// }

		@Override
		public void mouseDragged(MouseEvent e) {
			if (list.秘processUIEvent(e)) {
				return;
			}

			// if (list.getDragEnabled()) {
			// DragRecognitionSupport.mouseDragged(e, this);
			// return;
			// }

			if (e.isShiftDown() || e.isControlDown()) {
				return;
			}

			int row = locationToIndex(list, e.getPoint());
			if (row != -1) {
				// 4835633. Dragging onto a File should not select it.
				if (isFileList) {
					return;
				}
				Rectangle cellBounds = getCellBounds(list, row, row);
				if (cellBounds != null) {
					list.scrollRectToVisible(cellBounds);
					list.setSelectionInterval(row, row);
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			list.秘processUIEvent(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (list.秘processUIEvent(e)) {
				return;
			}

			if (list.getDragEnabled()) {
				// MouseEvent me = DragRecognitionSupport.mouseReleased(e);
				// if (me != null) {
				// SwingUtilities2.adjustFocus(list);
				// if (!dragPressDidSelection) {
				// adjustSelection(me);
				// }
				// }
			} else {
				list.setValueIsAdjusting(false);
			}
		}

		//
		// FocusListener
		//
		protected void repaintCellFocus() {
			int leadIndex = adjustIndex(list.getLeadSelectionIndex(), list);
			if (leadIndex != -1) {
				Rectangle r = getCellBounds(list, leadIndex, leadIndex);
				if (r != null) {
					list.repaint(r.x, r.y, r.width, r.height);
				}
			}
		}

		/*
		 * The focusGained() focusLost() methods run when the JList focus changes.
		 */

		@Override
		public void focusGained(FocusEvent e) {
			repaintCellFocus();
		}

		@Override
		public void focusLost(FocusEvent e) {
			repaintCellFocus();
		}
	}

	static int adjustIndex(int index, JList list) {
		return index < list.getModel().getSize() ? index : -1;
	}

	// private static final TransferHandler defaultTransferHandler = new
	// ListTransferHandler();

	// static class ListTransferHandler extends TransferHandler implements
	// UIResource {
	//
	// /**
	// * Create a Transferable to use as the source for a data transfer.
	// *
	// * @param c
	// * The component holding the data to be transfered. This argument
	// * is provided to enable sharing of TransferHandlers by multiple
	// * components.
	// * @return The representation of the data to be transfered.
	// *
	// */
	// @Override
	// protected Transferable createTransferable(JComponent c) {
	// if (c instanceof JList) {
	// JList list = (JList) jc;
	// Object[] values = list.getSelectedValues();
	//
	// if (values == null || values.length == 0) {
	// return null;
	// }
	//
	// StringBuffer plainBuf = new StringBuffer();
	// StringBuffer htmlBuf = new StringBuffer();
	//
	// htmlBuf.append("<html>\n<body>\n<ul>\n");
	//
	// for (int i = 0; i < values.length; i++) {
	// Object obj = values[i];
	// String val = ((obj == null) ? "" : obj.toString());
	// plainBuf.append(val + "\n");
	// htmlBuf.append("  <li>" + val + "\n");
	// }
	//
	// // remove the last newline
	// plainBuf.deleteCharAt(plainBuf.length() - 1);
	// htmlBuf.append("</ul>\n</body>\n</html>");
	//
	// return new BasicTransferable(plainBuf.toString(), htmlBuf.toString());
	// }
	//
	// return null;
	// }
	//
	// @Override
	// public int getSourceActions(JComponent c) {
	// return COPY;
	// }
	//
	// }

	// ListPeer 
	
    public void makeVisible(int index) {
    }

	public int[] getSelectedIndexes() {
		return new int[] {};
	}

	public void add(String item, int index) {
	}

	public void delItems(int start, int end) {
	}

	public void removeAll() {
	}

	public void select(int index) {
	}

	public void deselect(int index) {
	}

	public void setMultipleMode(boolean m) {
	}

	/**
	 *
	 * AWT list only
	 * 
	 * @param rows
	 * @return
	 */
	public Dimension getPreferredSize(int rows) {
		int h = 4, w = 0;
		for (int i = 0; i < rows; i++)
			h += getRowHeight(i);
		ListModel m = list.getModel();
		for (int i = m.getSize(); --i >= 0; ) {
			Object o = m.getElementAt(i);
			int d = 0;
			if (o instanceof Component) {
				// was w +=... so these would be for left to right
				d = ((Component) o).getPreferredSize().width;
			} else if (o != null) {
				// and this would not?
				d = list.getFontMetrics(getFont()).stringWidth(o.toString());
			}
			// was included in o != null
			if (d > w)
				w = d;
		}
		return new Dimension(w + 24, h); 
	}

	/**
	 * AWT list only
	 * @param rows
	 * @return
	 */
	public Dimension getMinimumSize(int rows) {
		int h = 4;
		for (int i = 0; i < rows; i++)
			h += getRowHeight(i);
		return new Dimension(getFont().getSize() * 10, h); 
	}


}
