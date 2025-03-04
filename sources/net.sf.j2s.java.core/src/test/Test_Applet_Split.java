package test;

import java.awt.Color;
import java.awt.Label;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Test_Applet_Split extends JApplet {

	static {
		
		/**
		 * @j2sNative
		 * 
		 * 	thisApplet.__Info.width = 500;
		 *  thisApplet.__Info.height = 400;
		 *  thisApplet.__Info.isResizable = true;
		 */
	}

	@Override
	public void init() {
		JPanel top = new JPanel();
		top.setBackground(Color.green);
		top.add(new Label("testing"));
		JPanel bottom = new JPanel();
		Label lblBottom = new Label("enabled");
		bottom.add(lblBottom);
		bottom.setBackground(Color.yellow);

		JSplitPane pleft = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
		pleft.setDividerSize(3);

		JPanel right = new JPanel();
		right.add(new Label("here"));
		right.setBackground(Color.red);
		JSplitPane p = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pleft, right);
		p.setSize(500, 400);
		p.setDividerLocation(0.24);
		p.setDividerSize(3);
		JComboBox cb = new JComboBox();
		cb.addItem("test1");
		cb.addItem("test2");
		cb.addItem("test3");
		right.add(cb);
		p.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				switch (evt.getPropertyName()) {
				case "dividerLocation":
					pleft.setEnabled(false);
					lblBottom.setText("disabled");
					cb.setEnabled(false);
					if (p.getDividerLocation() > p.getWidth() * 0.75) {
						//p.setDividerLocation(0.75);
						p.setDividerLocation((int)(p.getWidth() * 0.45));
					}
					break;
				case "lastDividerLocation":
					break;
				}
			}
			
		});
		add(p);
		resize(500,400);		
	}
}
