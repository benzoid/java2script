/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 1997, 2007, Oracle and/or its affiliates. All rights reserved.
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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeCellEditor;

import swingjs.plaf.CellHolder;

/**
 * The default editor for table and tree cells.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Alan Chung
 * @author Philip Milne
 */

public class DefaultCellEditor extends AbstractCellEditor implements TableCellEditor, TreeCellEditor {

//
//  Instance Variables
//

	/** The Swing component being edited. */
	protected JComponent editorComponent;
	/**
	 * The delegate class which handles all methods sent from the
	 * <code>CellEditor</code>.
	 */
	protected EditorDelegate delegate;
	/**
	 * An integer specifying the number of clicks needed to start editing. Even if
	 * <code>clickCountToStart</code> is defined as zero, it will not initiate until
	 * a click occurs.
	 */
	protected int clickCountToStart = 1;

//
//  Constructors
//

	/**
	 * Constructs a <code>DefaultCellEditor</code> that uses a text field.
	 *
	 * @param textField a <code>JTextField</code> object
	 */
	public DefaultCellEditor(final JTextField textField) {
		setComponent(textField);
		this.clickCountToStart = 2;
		delegate = new EditorDelegate() {
			@Override
			public void setValue(Object value) {
				textField.setText((value != null) ? value.toString() : "");
			}

			@Override
			public Object getCellEditorValue() {
				return textField.getText();
			}
		};
		textField.addActionListener(delegate);
	}

	/**
	 * Constructs a <code>DefaultCellEditor</code> object that uses a check box.
	 *
	 * @param checkBox a <code>JCheckBox</code> object
	 */
	public DefaultCellEditor(final JCheckBox checkBox) {
		setComponent(checkBox);

		delegate = new EditorDelegate() {
			@Override
			public void setValue(Object value) {
				boolean selected = false;
				if (value instanceof Boolean) {
					selected = ((Boolean) value).booleanValue();
				} else if (value instanceof String) {
					selected = value.equals("true");
				}
				checkBox.setSelected(selected);
			}

			@Override
			public Object getCellEditorValue() {
				return Boolean.valueOf(checkBox.isSelected());
			}
		};
		checkBox.addActionListener(delegate);
		checkBox.setRequestFocusEnabled(false);
	}

	/**
	 * Constructs a <code>DefaultCellEditor</code> object that uses a combo box.
	 *
	 * @param comboBox a <code>JComboBox</code> object
	 */
	public DefaultCellEditor(final JComboBox comboBox) {
		setComponent(comboBox);
		comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
		delegate = new EditorDelegate() {
			@Override
			public void setValue(Object value) {
				comboBox.setSelectedItem(value);
			}

			@Override
			public Object getCellEditorValue() {
				return comboBox.getSelectedItem();
			}

			@Override
			public boolean shouldSelectCell(EventObject anEvent) {
				if (anEvent instanceof MouseEvent) {
					MouseEvent e = (MouseEvent) anEvent;
					return e.getID() != MouseEvent.MOUSE_DRAGGED;
				}
				return true;
			}

			@Override
			public boolean stopCellEditing() {
				if (comboBox.isEditable()) {
					// Commit edited value.
					comboBox.actionPerformed(new ActionEvent(DefaultCellEditor.this, 0, ""));
				}
				return super.stopCellEditing();
			}
		};
		comboBox.addActionListener(delegate);
	}

	/**
	 * Returns a reference to the editor component.
	 *
	 * @return the editor <code>Component</code>
	 */
	public Component getComponent() {
		return editorComponent;
	}

//
//  Modifying
//

	/**
	 * Specifies the number of clicks needed to start editing.
	 *
	 * @param count an int specifying the number of clicks needed to start editing
	 * @see #getClickCountToStart
	 */
	public void setClickCountToStart(int count) {
		clickCountToStart = count;
	}

	/**
	 * Returns the number of clicks needed to start editing.
	 * 
	 * @return the number of clicks needed to start editing
	 */
	public int getClickCountToStart() {
		return clickCountToStart;
	}

//
//  Override the implementations of the superclass, forwarding all methods
//  from the CellEditor interface to our delegate.
//

	/**
	 * Forwards the message from the <code>CellEditor</code> to the
	 * <code>delegate</code>.
	 * 
	 * @see EditorDelegate#getCellEditorValue
	 */
	@Override
	public Object getCellEditorValue() {
		return delegate.getCellEditorValue();
	}

	/**
	 * Forwards the message from the <code>CellEditor</code> to the
	 * <code>delegate</code>.
	 * 
	 * @see EditorDelegate#isCellEditable(EventObject)
	 */
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return delegate.isCellEditable(anEvent);
	}

	/**
	 * Forwards the message from the <code>CellEditor</code> to the
	 * <code>delegate</code>.
	 * 
	 * @see EditorDelegate#shouldSelectCell(EventObject)
	 */
	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return delegate.shouldSelectCell(anEvent);
	}

	/**
	 * Forwards the message from the <code>CellEditor</code> to the
	 * <code>delegate</code>.
	 * 
	 * @see EditorDelegate#stopCellEditing
	 */
	@Override
	public boolean stopCellEditing() {
		return delegate.stopCellEditing();
	}

	/**
	 * Forwards the message from the <code>CellEditor</code> to the
	 * <code>delegate</code>.
	 * 
	 * @see EditorDelegate#cancelCellEditing
	 */
	@Override
	public void cancelCellEditing() {
		delegate.cancelCellEditing();
	}

//
//  Implementing the TreeCellEditor Interface
//

	/** Implements the <code>TreeCellEditor</code> interface. */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
			boolean leaf, int row) {
		String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, false);

		delegate.setValue(stringValue);
		return editorComponent;
	}

//
//  Implementing the CellEditor Interface
//
	/** Implements the <code>TableCellEditor</code> interface. */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		delegate.setValue(value);
		if (editorComponent instanceof JCheckBox) {
			// in order to avoid a "flashing" effect when clicking a checkbox
			// in a table, it is important for the editor to have as a border
			// the same border that the renderer has, and have as the background
			// the same color as the renderer has. This is primarily only
			// needed for JCheckBox since this editor doesn't fill all the
			// visual space of the table cell, unlike a text field.
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component c = renderer.getTableCellRendererComponent(table, value, isSelected, true, row, column);
			if (c != null) {
				editorComponent.setOpaque(true);
				editorComponent.setBackground(c.getBackground());
				if (c instanceof JComponent) {
					editorComponent.setBorder(((JComponent) c).getBorder());
				}
			} else {
				editorComponent.setOpaque(false);
			}
		}
		return editorComponent;
	}

//
//  Protected EditorDelegate class
//

	/**
	 * The protected <code>EditorDelegate</code> class.
	 */
	protected class EditorDelegate implements ActionListener, ItemListener {

		/** The value of this cell. */
		protected Object value;

		/**
		 * Returns the value of this cell.
		 * 
		 * @return the value of this cell
		 */
		public Object getCellEditorValue() {
			return value;
		}

		/**
		 * Sets the value of this cell.
		 * 
		 * @param value the new value of this cell
		 */
		public void setValue(Object value) {
			this.value = value;
		}

		/**
		 * Returns true if <code>anEvent</code> is <b>not</b> a <code>MouseEvent</code>.
		 * Otherwise, it returns true if the necessary number of clicks have occurred,
		 * and returns false otherwise.
		 *
		 * @param anEvent the event
		 * @return true if cell is ready for editing, false otherwise
		 * @see #setClickCountToStart
		 * @see #shouldSelectCell
		 */
		public boolean isCellEditable(EventObject anEvent) {
			if (anEvent instanceof MouseEvent) {
				return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
			}
			return true;
		}

		/**
		 * Returns true to indicate that the editing cell may be selected.
		 *
		 * @param anEvent the event
		 * @return true
		 * @see #isCellEditable
		 */
		public boolean shouldSelectCell(EventObject anEvent) {
			return true;
		}

		/**
		 * Returns true to indicate that editing has begun.
		 *
		 * @param anEvent the event
		 */
		public boolean startCellEditing(EventObject anEvent) {
			return true;
		}

		/**
		 * Stops editing and returns true to indicate that editing has stopped. This
		 * method calls <code>fireEditingStopped</code>.
		 *
		 * @return true
		 */
		public boolean stopCellEditing() {
			//System.out.println("DefaultCellEditor stop");
			fireEditingStopped();
			return true;
		}

		/**
		 * Cancels editing. This method calls <code>fireEditingCanceled</code>.
		 */
		public void cancelCellEditing() {
			//System.out.println("DefaultCellEditor cancel");
			fireEditingCanceled();
		}

		/**
		 * When an action is performed, editing is ended.
		 * 
		 * @param e the action event
		 * @see #stopCellEditing
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.println("DefaultCellEditor action Performed");
			DefaultCellEditor.this.stopCellEditing();
		}

		/**
		 * When an item's state changes, editing is ended.
		 * 
		 * @param e the action event
		 * @see #stopCellEditing
		 */
		@Override
		public void itemStateChanged(ItemEvent e) {
			DefaultCellEditor.this.stopCellEditing();
		}
	}

	private void setComponent(JComponent comp) {
		CellHolder.setJ2SRendererComponent(editorComponent = comp);
	}

} // End of class JCellEditor
