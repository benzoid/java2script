/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 1997, 2006, Oracle and/or its affiliates. All rights reserved.
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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.TextComponent;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/**
 * A <code>JTextArea</code> is a multi-line area that displays plain text.
 * It is intended to be a lightweight component that provides source
 * compatibility with the <code>java.awt.TextArea</code> class where it can
 * reasonably do so.
 * You can find information and examples of using all the text components in
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/text.html">Using Text Components</a>,
 * a section in <em>The Java Tutorial.</em>
 *
 * <p>
 * This component has capabilities not found in the
 * <code>java.awt.TextArea</code> class.  The superclass should be
 * consulted for additional capabilities.
 * Alternative multi-line text classes with
 * more capabilities are <code>JTextPane</code> and <code>JEditorPane</code>.
 * <p>
 * The <code>java.awt.TextArea</code> internally handles scrolling.
 * <code>JTextArea</code> is different in that it doesn't manage scrolling,
 * but implements the swing <code>Scrollable</code> interface.  This allows it
 * to be placed inside a <code>JScrollPane</code> if scrolling
 * behavior is desired, and used directly if scrolling is not desired.
 * <p>
 * The <code>java.awt.TextArea</code> has the ability to do line wrapping.
 * This was controlled by the horizontal scrolling policy.  Since
 * scrolling is not done by <code>JTextArea</code> directly, backward
 * compatibility must be provided another way.  <code>JTextArea</code> has
 * a bound property for line wrapping that controls whether or
 * not it will wrap lines.  By default, the line wrapping property
 * is set to false (not wrapped).
 * <p>
 * <code>java.awt.TextArea</code> has two properties <code>rows</code>
 * and <code>columns</code> that are used to determine the preferred size.
 * <code>JTextArea</code> uses these properties to indicate the
 * preferred size of the viewport when placed inside a <code>JScrollPane</code>
 * to match the functionality provided by <code>java.awt.TextArea</code>.
 * <code>JTextArea</code> has a preferred size of what is needed to
 * display all of the text, so that it functions properly inside of
 * a <code>JScrollPane</code>.  If the value for <code>rows</code>
 * or <code>columns</code> is equal to zero,
 * the preferred size along that axis is used for
 * the viewport preferred size along the same axis.
 * <p>
 * The <code>java.awt.TextArea</code> could be monitored for changes by adding
 * a <code>TextListener</code> for <code>TextEvent</code>s.
 * In the <code>JTextComponent</code> based
 * components, changes are broadcasted from the model via a
 * <code>DocumentEvent</code> to <code>DocumentListeners</code>.
 * The <code>DocumentEvent</code> gives
 * the location of the change and the kind of change if desired.
 * The code fragment might look something like:
 * <pre>
 *    DocumentListener myListener = ??;
 *    JTextArea myArea = ??;
 *    myArea.getDocument().addDocumentListener(myListener);
 * </pre>
 * <p>
 * <dl>
 * <dt><b><font size=+1>Newlines</font></b>
 * <dd>
 * For a discussion on how newlines are handled, see
 * <a href="text/DefaultEditorKit.html">DefaultEditorKit</a>.
 * </dl>
 *
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more
 * information see <a
 * href="package-summary.html#threading">Swing's Threading
 * Policy</a>.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @beaninfo
 *   attribute: isContainer false
 * description: A multi-line area that displays plain text.
 *
 * @author  Timothy Prinzing
 * @see JTextPane
 * @see JEditorPane
 */
public class JTextArea extends JTextComponent {



    /**
     * Constructs a new TextArea.  A default model is set, the initial string
     * is null, and rows/columns are set to 0.
     */
    public JTextArea() {
        this(null, null, 0, 0);
    }

    /**
     * Constructs a new TextArea with the specified text displayed.
     * A default model is created and rows/columns are set to 0.
     *
     * @param text the text to be displayed, or null
     */
    public JTextArea(String text) {
        this(null, text, 0, 0);
    }

    /**
     * Constructs a new empty TextArea with the specified number of
     * rows and columns.  A default model is created, and the initial
     * string is null.
     *
     * @param rows the number of rows >= 0
     * @param columns the number of columns >= 0
     * @exception IllegalArgumentException if the rows or columns
     *  arguments are negative.
     */
    public JTextArea(int rows, int columns) {
        this(null, null, rows, columns);
    }

    /**
     * Constructs a new TextArea with the specified text and number
     * of rows and columns.  A default model is created.
     *
     * @param text the text to be displayed, or null
     * @param rows the number of rows >= 0
     * @param columns the number of columns >= 0
     * @exception IllegalArgumentException if the rows or columns
     *  arguments are negative.
     */
    public JTextArea(String text, int rows, int columns) {
        this(null, text, rows, columns);
    }

    /**
     * Constructs a new JTextArea with the given document model, and defaults
     * for all of the other arguments (null, 0, 0).
     *
     * @param doc  the model to use
     */
    public JTextArea(Document doc) {
        this(doc, null, 0, 0);
    }

    /**
     * Constructs a new JTextArea with the specified number of rows
     * and columns, and the given model.  All of the constructors
     * feed through this constructor.
     *
     * @param doc the model to use, or create a default one if null
     * @param text the text to be displayed, null if none
     * @param rows the number of rows >= 0
     * @param columns the number of columns >= 0
     * @exception IllegalArgumentException if the rows or columns
     *  arguments are negative.
     */
    public JTextArea(Document doc, String text, int rows, int columns) {
    	
    	setFocusTraversalKeysEnabled(false); // can't handle CTRL-ENTER in JavaScript
        this.rows = rows;
        this.columns = columns;
        if (doc == null) {
            doc = createDefaultModel();
        }
        setDocument(doc);
        if (text != null) {
            setText(text);
            select(0, 0);
        }
        if (rows < 0) {
            throw new IllegalArgumentException("rows: " + rows);
        }
        if (columns < 0) {
            throw new IllegalArgumentException("columns: " + columns);
        }
		// unfortunately, we cannot trap CTRL-TAB in a browser
//
//        LookAndFeel.installProperty(this,
//                                    "focusTraversalKeysForward",
//                                    JComponent.
//                                    getManagingFocusForwardTraversalKeys());
//        LookAndFeel.installProperty(this,
//                                    "focusTraversalKeysBackward",
//                                    JComponent.
//                                    getManagingFocusBackwardTraversalKeys());
    }

	@Override
	public String getUIClassID() {
		return "TextAreaUI";
	}

    /**
     * Creates the default implementation of the model
     * to be used at construction if one isn't explicitly
     * given.  A new instance of PlainDocument is returned.
     *
     * @return the default document model
     */
    protected Document createDefaultModel() {
        return new PlainDocument();
    }

    /**
     * Sets the number of characters to expand tabs to.
     * This will be multiplied by the maximum advance for
     * variable width fonts.  A PropertyChange event ("tabSize") is fired
     * when the tab size changes.
     *
     * @param size number of characters to expand to
     * @see #getTabSize
     * @beaninfo
     *   preferred: true
     *       bound: true
     * description: the number of characters to expand tabs to
     */
    public void setTabSize(int size) {
        Document doc = getDocument();
        if (doc != null) {
            int old = getTabSize();
            doc.putProperty(PlainDocument.tabSizeAttribute, new Integer(size));
            firePropertyChange("tabSize", old, size);
        }
    }

    /**
     * Gets the number of characters used to expand tabs.  If the document is
     * null or doesn't have a tab setting, return a default of 8.
     *
     * @return the number of characters
     */
    public int getTabSize() {
        int size = 8;
        Document doc = getDocument();
        if (doc != null) {
            Integer i = (Integer) doc.getProperty(PlainDocument.tabSizeAttribute);
            if (i != null) {
                size = i.intValue();
            }
        }
        return size;
    }

    /**
     * Sets the line-wrapping policy of the text area.  If set
     * to true the lines will be wrapped if they are too long
     * to fit within the allocated width.  If set to false,
     * the lines will always be unwrapped.  A <code>PropertyChange</code>
     * event ("lineWrap") is fired when the policy is changed.
     * By default this property is false.
     *
     * @param wrap indicates if lines should be wrapped
     * @see #getLineWrap
     * @beaninfo
     *   preferred: true
     *       bound: true
     * description: should lines be wrapped
     */
    public void setLineWrap(boolean wrap) {
        boolean old = this.wrap;
        this.wrap = wrap;
        firePropertyChange("lineWrap", old, wrap);
    }

    /**
     * Gets the line-wrapping policy of the text area.  If set
     * to true the lines will be wrapped if they are too long
     * to fit within the allocated width.  If set to false,
     * the lines will always be unwrapped.
     *
     * @return if lines will be wrapped
     */
    public boolean getLineWrap() {
        return wrap;
    }

    /**
     * Sets the style of wrapping used if the text area is wrapping
     * lines.  If set to true the lines will be wrapped at word
     * boundaries (whitespace) if they are too long
     * to fit within the allocated width.  If set to false,
     * the lines will be wrapped at character boundaries.
     * By default this property is false.
     *
     * @param word indicates if word boundaries should be used
     *   for line wrapping
     * @see #getWrapStyleWord
     * @beaninfo
     *   preferred: false
     *       bound: true
     * description: should wrapping occur at word boundaries
     */
    public void setWrapStyleWord(boolean word) {
        boolean old = this.word;
        this.word = word;
        firePropertyChange("wrapStyleWord", old, word);
    }

    /**
     * Gets the style of wrapping used if the text area is wrapping
     * lines.  If set to true the lines will be wrapped at word
     * boundaries (ie whitespace) if they are too long
     * to fit within the allocated width.  If set to false,
     * the lines will be wrapped at character boundaries.
     *
     * @return if the wrap style should be word boundaries
     *  instead of character boundaries
     * @see #setWrapStyleWord
     */
    public boolean getWrapStyleWord() {
        return word;
    }

    /**
     * Translates an offset into the components text to a
     * line number.
     *
     * @param offset the offset >= 0
     * @return the line number >= 0
     * @exception BadLocationException thrown if the offset is
     *   less than zero or greater than the document length.
     */
    public int getLineOfOffset(int offset) throws BadLocationException {
        Document doc = getDocument();
        if (offset < 0) {
            throw new BadLocationException("Can't translate offset to line", -1);
        } else if (offset > doc.getLength()) {
            throw new BadLocationException("Can't translate offset to line", doc.getLength()+1);
        } else {
            Element map = getDocument().getDefaultRootElement();
            return map.getElementIndex(offset);
        }
    }

    /**
     * Determines the number of lines contained in the area.
     *
     * @return the number of lines > 0
     */
    public int getLineCount() {
        Element map = getDocument().getDefaultRootElement();
        return map.getElementCount();
    }

    /**
     * Determines the offset of the start of the given line.
     *
     * @param line  the line number to translate >= 0
     * @return the offset >= 0
     * @exception BadLocationException thrown if the line is
     * less than zero or greater or equal to the number of
     * lines contained in the document (as reported by
     * getLineCount).
     */
    public int getLineStartOffset(int line) throws BadLocationException {
        int lineCount = getLineCount();
        if (line < 0) {
            throw new BadLocationException("Negative line", -1);
        } else if (line >= lineCount) {
            throw new BadLocationException("No such line", getDocument().getLength()+1);
        } else {
            Element map = getDocument().getDefaultRootElement();
            Element lineElem = map.getElement(line);
            return lineElem.getStartOffset();
        }
    }

    /**
     * Determines the offset of the end of the given line.
     *
     * @param line  the line >= 0
     * @return the offset >= 0
     * @exception BadLocationException Thrown if the line is
     * less than zero or greater or equal to the number of
     * lines contained in the document (as reported by
     * getLineCount).
     */
    public int getLineEndOffset(int line) throws BadLocationException {
        int lineCount = getLineCount();
        if (line < 0) {
            throw new BadLocationException("Negative line", -1);
        } else if (line >= lineCount) {
            throw new BadLocationException("No such line", getDocument().getLength()+1);
        } else {
            Element map = getDocument().getDefaultRootElement();
            Element lineElem = map.getElement(line);
            int endOffset = lineElem.getEndOffset();
            // hide the implicit break at the end of the document
            return ((line == lineCount - 1) ? (endOffset - 1) : endOffset);
        }
    }

    // --- java.awt.TextArea methods ---------------------------------

    /**
     * Inserts the specified text at the specified position.  Does nothing
     * if the model is null or if the text is null or empty.
     * <p>
     * This method is thread safe, although most Swing methods
     * are not. Please see
     * <A HREF="http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html">How
     * to Use Threads</A> for more information.
     *
     * @param str the text to insert
     * @param pos the position at which to insert >= 0
     * @exception IllegalArgumentException  if pos is an
     *  invalid position in the model
     * @see TextComponent#setText
     * @see #replaceRange
     */
    public void insert(String str, int pos) {
        Document doc = getDocument();
        if (doc != null) {
            try {
                doc.insertString(pos, str, null);
            } catch (BadLocationException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    /**
     * Appends the given text to the end of the document.  Does nothing if
     * the model is null or the string is null or empty.
     * <p>
     * This method is thread safe, although most Swing methods
     * are not. Please see
     * <A HREF="http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html">How
     * to Use Threads</A> for more information.
     *
     * @param str the text to insert
     * @see #insert
     */
    public void append(String str) {
        Document doc = getDocument();
        if (doc != null) {
            try {
                doc.insertString(doc.getLength(), str, null);
            } catch (BadLocationException e) {
            }
        }
    }

    /**
     * Replaces text from the indicated start to end position with the
     * new text specified.  Does nothing if the model is null.  Simply
     * does a delete if the new string is null or empty.
     * <p>
     * This method is thread safe, although most Swing methods
     * are not. Please see
     * <A HREF="http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html">How
     * to Use Threads</A> for more information.
     *
     * @param str the text to use as the replacement
     * @param start the start position >= 0
     * @param end the end position >= start
     * @exception IllegalArgumentException  if part of the range is an
     *  invalid position in the model
     * @see #insert
     * @see #replaceRange
     */
    public void replaceRange(String str, int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("end before start");
        }
        Document doc = getDocument();
        if (doc != null) {
            try {
                if (doc instanceof AbstractDocument) {
                    ((AbstractDocument)doc).replace(start, end - start, str,
                                                    null, this);
                }
                else {
                    doc.remove(start, end - start);
                    doc.insertString(start, str, null);
                }
            } catch (BadLocationException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    /**
     * Returns the number of rows in the TextArea.
     *
     * @return the number of rows >= 0
     */
    public int getRows() {
        return rows;
    }

    /**
     * Sets the number of rows for this TextArea.  Calls invalidate() after
     * setting the new value.
     *
     * @param rows the number of rows >= 0
     * @exception IllegalArgumentException if rows is less than 0
     * @see #getRows
     * @beaninfo
     * description: the number of rows preferred for display
     */
    public void setRows(int rows) {
        int oldVal = this.rows;
        if (rows < 0) {
            throw new IllegalArgumentException("rows less than zero.");
        }
        if (rows != oldVal) {
            this.rows = rows;
            invalidate();
        }
    }

    /**
     * Defines the meaning of the height of a row.  This defaults to
     * the height of the font.
     *
     * @return the height >= 1
     */
    protected int getRowHeight() {
        if (rowHeight == 0) {
            FontMetrics metrics = getFontMetrics(getFont());
            rowHeight = metrics.getHeight();// + 2; // SwingJS adds +2 here
        }
        return rowHeight;
    }

    /**
     * Returns the number of columns in the TextArea.
     *
     * @return number of columns >= 0
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Sets the number of columns for this TextArea.  Does an invalidate()
     * after setting the new value.
     *
     * @param columns the number of columns >= 0
     * @exception IllegalArgumentException if columns is less than 0
     * @see #getColumns
     * @beaninfo
     * description: the number of columns preferred for display
     */
    public void setColumns(int columns) {
        int oldVal = this.columns;
        if (columns < 0) {
            throw new IllegalArgumentException("columns less than zero.");
        }
        if (columns != oldVal) {
            this.columns = columns;
            invalidate();
        }
    }

    /**
     * Gets column width.
     * The meaning of what a column is can be considered a fairly weak
     * notion for some fonts.  This method is used to define the width
     * of a column.  By default this is defined to be the width of the
     * character <em>m</em> for the font used.  This method can be
     * redefined to be some alternative amount.
     *
     * @return the column width >= 1
     */
    protected int getColumnWidth() {
        if (columnWidth == 0) {
            FontMetrics metrics = getFontMetrics(getFont());
            columnWidth = metrics.charWidth('m');
        }
        return columnWidth;
    }

    // --- Component methods -----------------------------------------

    /**
     * Returns the preferred size of the TextArea.  This is the
     * maximum of the size needed to display the text and the
     * size requested for the viewport.
     *
     * @return the size
     */
    @Override
		public Dimension getPreferredSize() {
    	return getSizeJS(getPrefSizeJComp(), 400, rows, columns);
    }

	public Dimension getSizeJS(Dimension d, int n, int rows, int columns) {
		int w = 10, h = 10;
		if (d == null) {
			d = new Dimension(n, n);
		} else {
			w = d.width;
			h = d.height;
		}
		if (columns != 0) {
			d.width = Math.max(w, getJ2SWidth(columns));
		}
		if (rows != 0) {
			Insets insets = getInsets();
			d.height = Math.max(h, rows * getRowHeight() + insets.top + insets.bottom);
		}
		return d;
	}

    protected int getJ2SWidth(int columns) {
		Insets insets = getInsets();
		return  columns * getColumnWidth() + insets.left + insets.right;
	}

	/**
     * Sets the current font.  This removes cached row height and column
     * width so the new font will be reflected, and calls revalidate().
     *
     * @param f the font to use as the current font
     */
    @Override
		public void setFont(Font f) {
        super.setFont(f);
        rowHeight = 0;
        columnWidth = 0;
    }


    /**
     * Returns a string representation of this JTextArea. This method
     * is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not
     * be <code>null</code>.
     *
     * @return  a string representation of this JTextArea.
     */
    @Override
		protected String paramString() {
        String wrapString = (wrap ?
                             "true" : "false");
        String wordString = (word ?
                             "true" : "false");

        return super.paramString() +
        ",colums=" + columns +
        ",columWidth=" + columnWidth +
        ",rows=" + rows +
        ",rowHeight=" + rowHeight +
        ",word=" + wordString +
        ",wrap=" + wrapString;
    }

    // --- Scrollable methods ----------------------------------------

    /**
     * Returns true if a viewport should always force the width of this
     * Scrollable to match the width of the viewport.  This is implemented
     * to return true if the line wrapping policy is true, and false
     * if lines are not being wrapped.
     *
     * @return true if a viewport should force the Scrollables width
     * to match its own.
     */
    @Override
		public boolean getScrollableTracksViewportWidth() {
        return (wrap) ? true : super.getScrollableTracksViewportWidth();
    }

    /**
     * Returns the preferred size of the viewport if this component
     * is embedded in a JScrollPane.  This uses the desired column
     * and row settings if they have been set, otherwise the superclass
     * behavior is used.
     *
     * @return The preferredSize of a JViewport whose view is this Scrollable.
     * @see JViewport#getPreferredSize
     */
    @Override
		public Dimension getPreferredScrollableViewportSize() {
        Dimension size = super.getPreferredScrollableViewportSize();
        size = (size == null) ? new Dimension(400,400) : size;
        Insets insets = getInsets();

        size.width = (columns == 0) ? size.width :
                columns * getColumnWidth() + insets.left + insets.right;
        size.height = (rows == 0) ? size.height :
                rows * getRowHeight() + insets.top + insets.bottom;
        return size;
    }

    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one new row
     * or column, depending on the value of orientation.  This is implemented
     * to use the values returned by the <code>getRowHeight</code> and
     * <code>getColumnWidth</code> methods.
     * <p>
     * Scrolling containers, like JScrollPane, will use this method
     * each time the user requests a unit scroll.
     *
     * @param visibleRect the view area visible within the viewport
     * @param orientation Either SwingConstants.VERTICAL or
     *   SwingConstants.HORIZONTAL.
     * @param direction Less than zero to scroll up/left,
     *   greater than zero for down/right.
     * @return The "unit" increment for scrolling in the specified direction
     * @exception IllegalArgumentException for an invalid orientation
     * @see JScrollBar#setUnitIncrement
     * @see #getRowHeight
     * @see #getColumnWidth
     */
    @Override
		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
    	// SwingJS: Note that we are just using the HTML5 narrow-width scrollbar here. 
        switch (orientation) {
        case SwingConstants.VERTICAL:
            return getRowHeight();
        case SwingConstants.HORIZONTAL:
            return getColumnWidth();
        default:
            throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
    }


    // --- variables -------------------------------------------------

    public int rows;
    public int columns;
    protected int columnWidth;
    protected int rowHeight;
    protected boolean wrap;
    protected boolean word;

}
