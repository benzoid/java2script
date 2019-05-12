package test;

//web_Ready
//web_AppletName= MyTest1
//web_Description= A test
//web_JavaVersion= http://www.dmitry
//web_AppletImage= dddd
//web_Category= test
//web_Date= $Date$
//web_Features= graphics, AWT-to-Swing

import java.awt.Adjustable;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.util.Dictionary;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ViewportUI;
import javax.swing.plaf.basic.BasicSliderUI;

public class Test_Applet_Scroll extends JApplet implements ChangeListener {

	static {MouseEvent m;
		/**
		 * @j2sNative
		 * 
		 * 	J2S.thisApplet.__Info.width = 500;
		 *  J2S.thisApplet.__Info.height = 400;
		 *  J2S.thisApplet.__Info.isResizable = true;
		 */
	}
	static DecimalFormat df = new DecimalFormat("0.00");
	boolean preferred = true;

	private JScrollBar hsb;
	
	void setSize(JComponent c, int x, int y) {
		if (preferred)
			c.setPreferredSize(new Dimension(x, y));
		else
			c.setSize(x, y);
	}

	@Override
	public void layout() {
		super.layout();
		
	}
		
	public void test(Iterable i) {
		
	}

	
	@Override
	public void init() {
		
		final JLabel label = new JLabel("hello");
		// label.setBounds(0, 60, 200, 60);
		setSize(label, 80, 50);
		label.setBackground(Color.yellow);
		label.setForeground(Color.BLUE);
		label.setOpaque(true);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setVerticalAlignment(SwingConstants.CENTER);

		final JTextField tf = new JTextField("12.5", 8);
//		tf.setBackground(Color.black);
//		tf.setForeground(Color.yellow);
		tf.setOpaque(true);
		setSize(tf, 80, 40);
		tf.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				label.setBackground(Color.white);
				label.setText(tf.getText());
			}
		});
		tf.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				tf.setBackground(Color.WHITE);
			}

			@Override
			public void focusLost(FocusEvent e) {
				tf.setBackground(Color.yellow);
			}

		});
		tf.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				System.out.println(e);
			}
			
		});
		tf.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int n = e.getWheelRotation();
				tf.setText("" + (Float.parseFloat(tf.getText()) + n));
				// e.consume(); not necessary for scrollbars
			}
		});
		final JToggleButton button = new JToggleButton("test");
		setSize(button, 80, 20);
		button.setBackground(Color.orange);
		button.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				label.setBackground(button.isSelected() ? Color.green : Color.yellow);
				tf.setBackground(Color.black);
				label.setText("test");
				// repaint();
			}
		});
		final JToggleButton button2 = new JToggleButton("btn2");
		button2.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				System.out.println("BTN2 clicked " + e.getClickCount());


			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("BTN2 released");
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				System.out.println("in button2");
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				System.out.println("out button2");
				
			}
			
		});
		// BasicToggleButtonUI us; just using this to get access to code for
		// BasicToggleButtonUI
		setSize(button2, 80, 20);
		button2.setBackground(Color.orange);
		button2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				label.setBackground(button2.isSelected() ? Color.green : Color.yellow);
				tf.setBackground(Color.green);
				label.setText("btn2");
				// repaint();
			}
		});
		
		button2.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				System.out.println("btn2 DRAG " + e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

		JPanel p = new JPanel();
		JTextArea area = new JTextArea("testing", 10,20) {
			protected int getColumnWidth() {
				int i = super.getColumnWidth();
				System.out.println("colwidth is " + i + " " + p.getFontMetrics(p.getFont()).stringWidth("m"));
				return i;
			}
			protected int getRowHeight() {
				int i = super.getRowHeight();
				System.out.println("rowHeight is " + i + " " + p.getFontMetrics(p.getFont()).getHeight());
				return i;
			}
		};
		area.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				System.out.println(e);
			}
			
		});

		p.setBackground(Color.yellow);
		p.add(area);
		

		// the first two buttons act like radio buttons; only one is ever ON
		JScrollBar hbar = mkBar(p, tf, Adjustable.VERTICAL, 20, 200);

		ButtonGroup bg = new ButtonGroup();
		bg.add(button);
		bg.add(button2);

		// the third button is not part of the group
		// note that JButtonUI does not need to know anything about the groups

		final JToggleButton button3 = new JToggleButton("btn3");
		// BasicToggleButtonUI us; just using this to get access to code for
		// BasicToggleButtonUI
		setSize(button3, 80, 20);
		button3.setBackground(Color.red);
		button3.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				label.setBackground(button3.isSelected() ? Color.green : Color.yellow);
				tf.setBackground(Color.black);
				label.setText("btn3");
				System.out.println("val0="+ hbar.getValue());
				hbar.setValue(hbar.getValue() + 20);
				System.out.println("val1="+ hbar.getValue());
				// repaint();
			}
		});

		p.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				System.out.println("PANEL clicked " + e.getClickCount());
				

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
				System.out.println("in panel");
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
				System.out.println("out panel");
			}
		
			
		});
		
		
		p.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				System.out.println("panel DRAG " + e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				System.out.println("panel Move " + e);
				
			}
			
		});

		p.setToolTipText("this is the panel");
		// p.setLayout(new GridLayout(2, 2, 2, 2));
		JScrollPane sp = new JScrollPane();
		sp.setSize(500, 250);
		sp.getViewport().add(p);
		getContentPane().add(sp);
		sp.getViewport().addChangeListener(this);
		hsb = sp.getHorizontalScrollBar();
		hsb.setUnitIncrement(100);
		hsb.setSize(new Dimension(100,20));
		button2.setToolTipText("this is hsb");

		hbar.setToolTipText("this is scrollbar 1");
		mkSlider(p, tf, Adjustable.VERTICAL, 20, 200).setToolTipText("this is slider 2");

	    JSlider s = mkSlider(p, tf, Adjustable.VERTICAL, 20, 200);
		s.setInverted(true);
		s.setSnapToTicks(true);
		p.add(label);
		label.setToolTipText("this is label");
		p.add(tf);
		tf.setToolTipText("this is tf");
		p.add(button);
		p.add(button2);
		p.add(button3);
		p.setBackground(Color.blue);
		button2.setToolTipText("this is Button 2");
		button3.setToolTipText("this is Button 3");
		mkBar(p, tf, Adjustable.HORIZONTAL, 100, 40);
		JSlider framesPerSecond = mkSlider(p, tf, Adjustable.HORIZONTAL, 300, 40);
		framesPerSecond.setForeground(Color.BLACK);
		framesPerSecond.setMajorTickSpacing(500);
		framesPerSecond.setMinorTickSpacing(100);
		framesPerSecond.setPaintTicks(true);
		framesPerSecond.setPaintLabels(true);
//		framesPerSecond.setLabelTable(labels);
		
		mkSlider(p, tf, Adjustable.HORIZONTAL, 100, 20).setInverted(true);
		repaint();
	}

	JScrollBar mkBar(JPanel p, final JTextField tf, int orient, int x, int y) {
		final JScrollBar bar = new JScrollBar(orient, 500, 10, 300, 10000);
		bar.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				tf.setText(df.format(e.getValue() / 100.0));
				System.out.println("adjusting " + bar.getValueIsAdjusting() + " " + bar.getValue());
			}

		});
		bar.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int n = e.getWheelRotation();
				bar.setValue(bar.getValue() + n * 5);
				// e.consume(); not necessary for scrollbars
			}
		});
		setSize(bar, x, y);
		bar.setBackground(Color.red);
		bar.setForeground(Color.green);
		bar.setBlockIncrement(10);
		bar.setUnitIncrement(100);
		bar.setOpaque(true);
		p.add(bar);
		bar.setVisibleAmount(100);
		return bar;
	}

	JSlider mkSlider(JPanel p, final JTextField tf, int orient, int w, int h) {
		final JSlider bar = new JSlider(orient, 300, 1000, 500);
		bar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				tf.setText(df.format(((JSlider) e.getSource()).getValue() / 100.0));
			}
		});
		bar.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int n = e.getWheelRotation();
				bar.setValue(bar.getValue() + n * 5);
				// e.consume(); not necessary for sliders
			}
		});
		setSize(bar, w, h);
		bar.setBackground(Color.orange);
		bar.setForeground(Color.green);
		bar.setOpaque(true);
		p.add(bar);
		return bar;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// Viewport has scrolled
//		JViewport v = (JViewport) e.getSource();
//		System.out.println("extent " +v.getExtentSize() + " " + v.getViewPosition());
//		if (v.getViewRect().x > 0)
//			System.out.println("view change: " + v.getViewRect());
//		System.out.println(v.getWidth() + " " + v.getHeight() + " " + v.getView().getBounds());
//		System.out.println(sbar.getValue() + "  "+ sbar.getVisibleAmount() + " " + sbar.getMaximum());

	}

}
