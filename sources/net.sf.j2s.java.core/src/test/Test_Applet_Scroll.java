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
import java.awt.Frame;
import java.awt.Graphics;
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

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Test_Applet_Scroll extends JApplet implements ChangeListener, MouseListener, MouseMotionListener {

	static {
		/**
		 * @j2sNative
		 * 
		 * 	thisApplet.__Info.width = 500;
		 *  thisApplet.__Info.height = 400;
		 *  thisApplet.__Info.isResizable = true;
		 */
	}
	static DecimalFormat df = new DecimalFormat("0.00");

	boolean preferred = true;

//	private JScrollBar hsb;
//
//	private JScrollBar sbar;

	private JPanel panel2;

	void setSize(JComponent c, int x, int y) {
		if (preferred)
			c.setPreferredSize(new Dimension(x, y));
		else
			c.setSize(x, y);
	}

	@Override 
	public void paint(Graphics g) {
		System.out.println("appletPaint");
		super.paint(g);
	}
	
	@Override 
	public void update(Graphics g) {
		System.out.println("appletUpdate");
		super.update(g);
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
		tf.setBackground(Color.black);
		tf.setForeground(Color.yellow);
		tf.setOpaque(true);
		setSize(tf, 80, 40);
		tf.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				label.setBackground(Color.white);
				label.setText(tf.getText());
				panel2.removeAll();
			   repaint();
			}
		});
		tf.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				tf.setBackground(Color.BLUE);
			}

			@Override
			public void focusLost(FocusEvent e) {
				tf.setBackground(Color.BLACK);
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
		final JToggleButton button = new JToggleButton("ytesty");
		// BasicToggleButtonUI us; just using this to get access to code for
		// BasicToggleButtonUI
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
		// BasicToggleButtonUI us; just using this to get access to code for
		// BasicToggleButtonUI
		setSize(button2, 80, 20);
		button2.setBackground(Color.orange);
		Test_Applet_Scroll me = this;
		button2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				label.setBackground(button2.isSelected() ? Color.green : Color.yellow);
				tf.setBackground(Color.green);
				label.setText("btn2");
				JFrame frame1 = new JFrame();
				frame1.setLocationRelativeTo(button2);
				JPanel jp = new JPanel();
				jp.setPreferredSize(new Dimension(150,150));
				frame1.add(jp);
				frame1.pack();
				frame1.setVisible(true);
			}
		});

		// the first two buttons act like radio buttons; only one is ever ON
		
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
				// repaint();
			}
		});

		
		JPanel p = new JPanel();

		// p.setLayout(new GridLayout(2, 2, 2, 2));
		JScrollPane sp = new JScrollPane();
		
		sp.addMouseMotionListener(this);
		sp.addMouseListener(this);


		panel2 = new JPanel();
		panel2.add(new JTextArea(10,10));
		panel2.setSize(100,100);
		p.add(panel2);

		sp.getViewport().add(p);
		getContentPane().add(sp);
		sp.getViewport().addChangeListener(this);
//		hsb = sp.getHorizontalScrollBar();

		sp.getViewport().setBackground(Color.blue);
		p.setOpaque(false);
		mkBar(p, tf, Adjustable.VERTICAL, 20, 200);
		mkSlider(p, tf, Adjustable.VERTICAL, 20, 200);
		mkSlider(p, tf, Adjustable.VERTICAL, 20, 200).setInverted(true);
		p.add(label);
		p.add(tf);
		p.add(button);
		p.add(button2);
		p.add(button3);
	    JComboBox formatChoice = new JComboBox();
	    formatChoice.insertItemAt("bin\u00e4r", 0);
	    formatChoice.insertItemAt("hex", 1);
	    p.add(formatChoice);
		mkBar(p, tf, Adjustable.HORIZONTAL, 100, 20);
		JSlider slider = mkSlider(p, tf, Adjustable.HORIZONTAL, 100, 20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPreferredSize(new Dimension(100, 100));
        slider.setMajorTickSpacing(200);
        slider.setBackground(Color.WHITE);
        slider.setForeground(Color.BLUE);

		mkSlider(p, tf, Adjustable.HORIZONTAL, 100, 20).setInverted(true);
	}

	JScrollBar mkBar(JPanel p, final JTextField tf, int orient, int x, int y) {
		final JScrollBar bar = new JScrollBar(orient, 500, 10, 300, 1000);
		bar.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				tf.setText(df.format(e.getValue() / 100.0));
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
		bar.setBackground(Color.orange);
		bar.setForeground(Color.green);
		bar.setOpaque(true);
		p.add(bar);
//		sbar = bar;
		bar.setVisibleAmount(80);
		return bar;
	}

	JSlider mkSlider(JPanel p, final JTextField tf, int orient, int x, int y) {
		final JSlider bar = new JSlider(orient, 300, 1000, 500) 
//		{
//		    @SuppressWarnings("deprecation")
//			@Override
//				public void reshape(int x, int y, int w, int h) {
//				System.out.println("??reshape " + x + " " + y + " " + w + " " + h);
//				super.reshape(x,  y, w, h);
// 				
//			}
//		}
		;
		bar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				tf.setText(df.format(((JSlider) e.getSource()).getValue() / 100.0));
				doRepaint();

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
		setSize(bar, x, y);
		bar.setBackground(Color.orange);
//		bar.setForeground(Color.green);
		bar.setOpaque(true);
		p.add(bar);
		return bar;
	}

	protected void doRepaint() {
		repaint();
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

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("Test_Applet_Scroll mouseDragged " + e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		System.out.println("Test_Applet_Scroll mouseEntered " + e);
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("Test_Applet_Scroll mouseExited " + e);
		
	}

}
