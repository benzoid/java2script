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

package swingjs.plaf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.text.DefaultEditorKit;

//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.Point;
//import java.awt.Toolkit;
//import java.awt.Window;
//import java.awt.event.AWTEventListener;
//import java.awt.event.ActionEvent;
//import java.awt.event.KeyEvent;
//import java.awt.event.MouseEvent;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import java.beans.PropertyVetoException;
//import java.io.BufferedInputStream;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
////import java.io.Serializable;
//import java.security.AccessController;
//import java.security.PrivilegedAction;
//import java.util.HashSet;
//import java.util.Locale;
//
////import javax.sound.sampled.AudioInputStream;
////import javax.sound.sampled.AudioSystem;
////import javax.sound.sampled.Clip;
////import javax.sound.sampled.DataLine;
////import javax.sound.sampled.LineEvent;
////import javax.sound.sampled.LineListener;
//import javax.swing.AbstractAction;
//import javax.swing.Action;
//import javax.swing.ActionMap;
//import javax.swing.DefaultListCellRenderer;
//import javax.swing.JComponent;
////import javax.swing.JInternalFrame;
//import javax.swing.JTextField;
//import javax.swing.LookAndFeel;
//import javax.swing.MenuElement;
//import javax.swing.MenuSelectionManager;
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
//import javax.swing.border.BevelBorder;
//import javax.swing.plaf.ActionMapUIResource;
//import javax.swing.plaf.BorderUIResource;
//import javax.swing.plaf.ComponentUI;
//import javax.swing.plaf.DimensionUIResource;
//import javax.swing.plaf.InsetsUIResource;
////import javax.swing.plaf.basic.popupMenuUI;
////import javax.swing.plaf.basic.BasicSplitPaneDivider;
//import javax.swing.text.DefaultEditorKit;
//
//import sun.awt.AppContext;
//import sun.swing.SwingUtilities2;
////import sun.swing.SwingLazyValue;
//
import sun.swing.SwingLazyValue;

/*
 * A base class to use in creating a look and feel for Swing.
 * <p>
 * Each of the {@code ComponentUI}s provided by {@code BasicLookAndFeel} derives
 * its behavior from the defaults table. Unless otherwise noted each of the
 * {@code ComponentUI} implementations in this package document the set of
 * defaults they use. Unless otherwise noted the defaults are installed at the
 * time {@code installUI} is invoked, and follow the recommendations outlined in
 * {@code LookAndFeel} for installing defaults.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 * 
 * @author unattributed
 */
public class HTML5LookAndFeel extends LookAndFeel {
	
	public HTML5LookAndFeel() {
		// for reflection
	}
	
	// /**
	// * Whether or not the developer has created a JPopupMenu.
	// */
	// static boolean needsEventHelper;
	//
	// /**
	// * Lock used when manipulating clipPlaying.
	// */
	// private transient Object audioLock = new Object();
	// /**
	// * The Clip that is currently playing (set in AudioAction).
	// */
	// private Clip clipPlaying;
	//
	// AWTEventHelper invocator = null;
	//
	// /*
	// * Listen for our AppContext being disposed
	// */
	// private PropertyChangeListener disposer = null;
	//
	/**
	 * Returns the look and feel defaults. The returned {@code UIDefaults} is
	 * populated by invoking, in order, {@code initClassDefaults},
	 * {@code initSystemColorDefaults} and {@code initComponentDefaults}.
	 * <p>
	 * While this method is public, it should only be invoked by the
	 * {@code UIManager} when the look and feel is set as the current look and
	 * feel and after {@code initialize} has been invoked.
	 * 
	 * @return the look and feel defaults
	 * 
	 * @see #initClassDefaults
	 * @see #initSystemColorDefaults
	 * @see #initComponentDefaults
	 */
	@Override
	public UIDefaults getDefaults() {
		UIDefaults table = new UIDefaults(610, 0.75f);

		initClassDefaults(table);
		initSystemColorDefaults(table);
		initComponentDefaults(table);

		return table;
	}

//    static int getFocusAcceleratorKeyMask() {
//        Toolkit tk = Toolkit.getDefaultToolkit();
//        if (tk instanceof SunToolkit) {
//            return ((SunToolkit)tk).getFocusAcceleratorKeyMask();
//        }
//        return ActionEvent.ALT_MASK;
//    }
//
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		// if (needsEventHelper) {
		// installAWTEventListener();
		// }
	}

	void installAWTEventListener() {
		// if (invocator == null) {
		// invocator = new AWTEventHelper();
		// needsEventHelper = true;
		//
		// // Add a PropertyChangeListener to our AppContext so we're alerted
		// // when the AppContext is disposed(), at which time this laf should
		// // be uninitialize()d.
		// disposer = new PropertyChangeListener() {
		// public void propertyChange(PropertyChangeEvent prpChg) {
		// uninitialize();
		// }
		// };
		// AppContext.getAppContext().addPropertyChangeListener(
		// AppContext.GUI_DISPOSED,
		// disposer);
		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void uninitialize() {
		// AppContext context = AppContext.getAppContext();
		// synchronized (popupMenuUI.MOUSE_GRABBER_KEY) {
		// Object grabber = context.get(popupMenuUI.MOUSE_GRABBER_KEY);
		// if (grabber != null) {
		// ((popupMenuUI.MouseGrabber)grabber).uninstall();
		// }
		// }
		// synchronized (popupMenuUI.MENU_KEYBOARD_HELPER_KEY) {
		// Object helper =
		// context.get(popupMenuUI.MENU_KEYBOARD_HELPER_KEY);
		// if (helper != null) {
		// ((popupMenuUI.MenuKeyboardHelper)helper).uninstall();
		// }
		// }
		//
		// if(invocator != null) {
		// AccessController.doPrivileged(invocator);
		// invocator = null;
		// }
		//
		// if (disposer != null) {
		// // Note that we're likely calling removePropertyChangeListener()
		// // during the course of AppContext.firePropertyChange().
		// // However, EventListenerAggreggate has code to safely modify
		// // the list under such circumstances.
		// context.removePropertyChangeListener(AppContext.GUI_DISPOSED,
		// disposer);
		// disposer = null;
		// }
	}

	/**
	 * Populates {@code table} with mappings from {@code uiClassID} to the fully
	 * qualified name of the ui class. The value for a particular
	 * {@code uiClassID} is {@code "javax.swing.plaf.basic.Basic + uiClassID"}.
	 * For example, the value for the {@code uiClassID} {@code TreeUI} is
	 * {@code "javax.swing.plaf.basic.BasicTreeUI"}.
	 * 
	 * @param table
	 *          the {@code UIDefaults} instance the entries are added to
	 * @throws NullPointerException
	 *           if {@code table} is {@code null}
	 * 
	 * @see javax.swing.LookAndFeel
	 * @see #getDefaults
	 */
	protected void initClassDefaults(UIDefaults table) {
		final String packageName = "swingjs.plaf.";
		Object[] uiDefaults = { 
		/**/		"ButtonUI", 						packageName + "JSButtonUI",
		/**/		"CheckBoxUI", 					packageName + "JSCheckBoxUI", 
		    		"ColorChooserUI",				packageName + "JSColorChooserUI", 
	  /**/		"FormattedTextFieldUI", packageName + "JSFormattedTextFieldUI", 
	  /**/		"MenuBarUI", 						packageName + "JSMenuBarUI", 
	  /**/		"MenuUI", 							packageName + "JSMenuUI",
	  /**/		"MenuItemUI", 					packageName + "JSMenuItemUI", 
	  /**/		"CheckBoxMenuItemUI", 	packageName + "JSCheckBoxMenuItemUI", 
	  /**/		"RadioButtonMenuItemUI", packageName + "JSRadioButtonMenuItemUI", 
	  /**/		"RadioButtonUI", 				packageName + "JSRadioButtonUI", 
	  /**/		"ToggleButtonUI", 			packageName + "JSToggleButtonUI", 
	  /**/		"PopupMenuUI", 					packageName + "JSPopupMenuUI", 
		    		"ProgressBarUI", 				packageName + "JSProgressBarUI", 
		/**/		"ScrollBarUI", 					packageName + "JSScrollBarUI", 
		/**/		"ScrollPaneUI", 				packageName + "JSScrollPaneUI", 
				"SplitPaneUI", 					packageName + "JSSplitPaneUI", 
	  /**/		"SliderUI", 						packageName + "JSSliderUI",
		/**/		"SeparatorUI", 					packageName + "JSSeparatorUI", 
				"SpinnerUI", 						packageName + "JSSpinnerUI", 
		/**/	 "ToolBarSeparatorUI", 	packageName + "JSToolBarSeparatorUI", 
		/**/   "PopupMenuSeparatorUI", packageName + "JSPopupMenuSeparatorUI", 
				"TabbedPaneUI", 				packageName + "JSTabbedPaneUI", 
	  /**/	 "TextAreaUI", 					packageName + "JSTextAreaUI", 
	  /**/	 "TextFieldUI", 					packageName + "JSTextFieldUI", 
	  /**/	 "PasswordFieldUI", 			packageName + "JSPasswordFieldUI", 
        "TextPaneUI", 					packageName + "JSTextPaneUI", 
				"EditorPaneUI", 				packageName + "JSEditorPaneUI", 
				"TreeUI",	 							packageName + "JSTreeUI",
		/**/	 "LabelUI", 							packageName + "JSLabelUI", 
				"ListUI", 							packageName + "JSListUI", 
				"ToolBarUI", 						packageName + "JSToolBarUI",
				"ToolTipUI", 						packageName + "JSToolTipUI", 
		/**/	 "ComboBoxUI", 					packageName + "JSComboBoxUI", 
				"TableUI", 							packageName + "JSTableUI",
				"TableHeaderUI", 				packageName + "JSTableHeaderUI", 
				"InternalFrameUI", 			packageName + "JSInternalFrameUI", 
				"DesktopPaneUI", 				packageName + "JSDesktopPaneUI", 
				"DesktopIconUI", 				packageName + "JSDesktopIconUI", 
				"OptionPaneUI", 				packageName + "JSOptionPaneUI", 
	  /**/		"PanelUI", 							packageName + "JSPanelUI",
		/**/		"ViewportUI", 					packageName + "JSViewportUI", 
	  /**/    "RootPaneUI", 					packageName + "JSRootPaneUI"
				};

		table.putDefaults(uiDefaults);
	}

	/**
	 * Populates {@code table} with system colors. This creates an array of
	 * {@code name-color} pairs and invokes {@code loadSystemColors}.
	 * <p>
	 * The name is a {@code String} that corresponds to the name of one of the
	 * static {@code SystemColor} fields in the {@code SystemColor} class. A
	 * name-color pair is created for every such {@code SystemColor} field.
	 * <p>
	 * The {@code color} corresponds to a hex {@code String} as understood by
	 * {@code Color.decode}. For example, one of the {@code name-color} pairs is
	 * {@code "desktop"-"#005C5C"}. This corresponds to the {@code SystemColor}
	 * field {@code desktop}, with a color value of {@code new Color(0x005C5C)}.
	 * <p>
	 * The following shows two of the {@code name-color} pairs:
	 * 
	 * <pre>
	 * String[] nameColorPairs = new String[] { &quot;desktop&quot;, &quot;#005C5C&quot;, &quot;activeCaption&quot;,
	 * 		&quot;#000080&quot; };
	 * loadSystemColors(table, nameColorPairs, isNativeLookAndFeel());
	 * </pre>
	 * 
	 * As previously stated, this invokes {@code loadSystemColors} with the
	 * supplied {@code table} and {@code name-color} pair array. The last argument
	 * to {@code loadSystemColors} indicates whether the value of the field in
	 * {@code SystemColor} should be used. This method passes the value of
	 * {@code isNativeLookAndFeel()} as the last argument to
	 * {@code loadSystemColors}.
	 * 
	 * @param table
	 *          the {@code UIDefaults} object the values are added to
	 * @throws NullPointerException
	 *           if {@code table} is {@code null}
	 * 
	 * @see java.awt.SystemColor
	 * @see #getDefaults
	 * @see #loadSystemColors
	 */
	protected void initSystemColorDefaults(UIDefaults table) {
		String[] defaultSystemColors = { 
				
				
//		"desktop", "#005C5C", /*
//																														 * Color of the
//																														 * desktop
//																														 * background
//																														 */
//		"activeCaption", "#000080", /*
//																 * Color for captions (title bars) when they are
//																 * active.
//																 */
//		"activeCaptionText", "#FFFFFF", /*
//																		 * Text color for text in captions (title
//																		 * bars).
//																		 */
//		"activeCaptionBorder", "#C0C0C0", /*
//																			 * Border color for caption (title bar)
//																			 * window borders.
//																			 */
//		"inactiveCaption", "#808080", /*
//																	 * Color for captions (title bars) when not
//																	 * active.
//																	 */
//		"inactiveCaptionText", "#C0C0C0", /*
//																			 * Text color for text in inactive
//																			 * captions (title bars).
//																			 */
//		"inactiveCaptionBorder", "#C0C0C0", /*
//																				 * Border color for inactive caption
//																				 * (title bar) window borders.
//																				 */
				// plaf.metal.OceanTheme
		"window", "#FFFFFF", /* Default color for the interior of windows */
		"windowBorder", "#000000", /* ??? */
		"windowText", "#333333", /* ??? */
		"menu", "#EEEEEE",//"#C0C0C0", /* Background color for menus */
		"menuText", "#333333", /* Text color for menus */
		"text", "#C0C0C0", /* Text background color */
		"textText", "#333333", /* Text foreground color */
		"textHighlight", "#B8CFE5", /* Text color when selected */
		"textHighlightText", "#000000", /* Text color when selected */
		"textInactiveText", "#808080", /* Text color when disabled */
		"control", "#EEEEEE", // SECONDARY3 Default color for controls (buttons, sliders, etc)
		"controlText", "#333333", //  Default color for text in controls 
		"controlHighlight", "#C0C0C0", /*
//																		 * Specular highlight (opposite of the
//																		 * shadow)
//																		 */
		"controlLtHighlight", "#B8CFE5", /* Highlight color for controls */
		"controlShadow", "#808080", /* Shadow color for controls */
		"controlDkShadow", "#000000", /* Dark shadow color for controls */
		"scrollbar", "#E0E0E0", /* Scrollbar background (usually the "track") */
		"info", "#FFFFE1", /* ToolTip -- light yellow */
		"infoText", "#000000" /* ToolTip */
		};

		loadSystemColors(table, defaultSystemColors, isNativeLookAndFeel());
	}

	/**
	 * Populates {@code table} with the {@code name-color} pairs in
	 * {@code systemColors}. Refer to {@link #initSystemColorDefaults(UIDefaults)}
	 * for details on the format of {@code systemColors}.
	 * <p>
	 * An entry is added to {@code table} for each of the {@code name-color} pairs
	 * in {@code systemColors}. The entry key is the {@code name} of the
	 * {@code name-color} pair.
	 * <p>
	 * The value of the entry corresponds to the {@code color} of the
	 * {@code name-color} pair. The value of the entry is calculated in one of two
	 * ways. With either approach the value is always a {@code ColorUIResource}.
	 * <p>
	 * If {@code useNative} is {@code false}, the {@code color} is created by
	 * using {@code Color.decode} to convert the {@code String} into a
	 * {@code Color}. If {@code decode} can not convert the {@code String} into a
	 * {@code Color} ({@code NumberFormatException} is thrown) then a
	 * {@code ColorUIResource} of black is used.
	 * <p>
	 * If {@code useNative} is {@code true}, the {@code color} is the value of the
	 * field in {@code SystemColor} with the same name as the {@code name} of the
	 * {@code name-color} pair. If the field is not valid, a
	 * {@code ColorUIResource} of black is used.
	 * 
	 * @param table
	 *          the {@code UIDefaults} object the values are added to
	 * @param systemColors
	 *          array of {@code name-color} pairs as described in
	 *          {@link #initSystemColorDefaults(UIDefaults)}
	 * @param useNative
	 *          whether the color is obtained from {@code SystemColor} or
	 *          {@code Color.decode}
	 * @throws NullPointerException
	 *           if {@code systemColors} is {@code null}; or {@code systemColors}
	 *           is not empty, and {@code table} is {@code null}; or one of the
	 *           names of the {@code name-color} pairs is {@code null}; or
	 *           {@code useNative} is {@code false} and one of the {@code colors}
	 *           of the {@code name-color} pairs is {@code null}
	 * @throws ArrayIndexOutOfBoundsException
	 *           if {@code useNative} is {@code false} and
	 *           {@code systemColors.length} is odd
	 * 
	 * @see #initSystemColorDefaults(javax.swing.UIDefaults)
	 * @see java.awt.SystemColor
	 * @see java.awt.Color#decode(String)
	 */
	protected void loadSystemColors(UIDefaults table, String[] systemColors,
			boolean useNative) {
		/*
		 * PENDING(hmuller) We don't load the system colors below because they're
		 * not reliable. Hopefully we'll be able to do better in a future version of
		 * AWT.
		 */
//		if (useNative) {
//			for (int i = 0; i < systemColors.length; i += 2) {
//				Color color = Color.black;
//				// try {
//				// String name = systemColors[i];
//				// color = (Color)(SystemColor.class.getField(name).get(null));
//				// } catch (Exception e) {
//				// }
//				table.put(systemColors[i], new ColorUIResource(color));
//			}
//		} else {
			for (int i = 0; i < systemColors.length; i += 2) {
				Color color = Color.black;
				try {
					color = Color.decode(systemColors[i + 1]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				table.put(systemColors[i], new ColorUIResource(color));
			}
//		}
	}

	/**
	 * Initialize the defaults table with the name of the ResourceBundle used for
	 * getting localized defaults. Also initialize the default locale used when no
	 * locale is passed into UIDefaults.get(). The default locale should generally
	 * not be relied upon. It is here for compatability with releases prior to
	 * 1.4.
	 */
	private void initResourceBundle(UIDefaults table) {
		// table.setDefaultLocale( Locale.getDefault() );
		// table.addResourceBundle(
		// "com.sun.swing.internal.plaf.basic.resources.basic" );
	}

	static final Object dialogPlain12 = new FontUIResource(Font.DIALOG, Font.PLAIN, 12); 
	static final Object dialogBold12 = new FontUIResource(Font.DIALOG, Font.BOLD, 12); 

	/**
	 * Populates {@code table} with the defaults for the basic look and feel.
	 * 
	 * @param table
	 *          the {@code UIDefaults} to add the values to
	 * @throws NullPointerException
	 *           if {@code table} is {@code null}
	 */
	@SuppressWarnings("unused")
	protected void initComponentDefaults(UIDefaults table) {

		initResourceBundle(table);

		// // *** Shared Integers
		// Integer fiveHundred = new Integer(500);

		// *** Shared Longs
		Long oneThousand = new Long(1000);

		// *** Shared Fonts
		Integer twelve = new Integer(12);
		Integer fontPlain = new Integer(Font.PLAIN);
		// Integer fontBold = new Integer(Font.BOLD);
		// Object dialogPlain12 = new SwingLazyValue(
		// "javax.swing.plaf.FontUIResource",
		// null,
		// new Object[] {Font.DIALOG, fontPlain, twelve});
		Object serifPlain12 = new FontUIResource(Font.SERIF, Font.PLAIN, 12);
		Object sansSerifPlain12 = new FontUIResource(Font.SANS_SERIF, Font.PLAIN,
				12);
		Object monospacedPlain12 = new FontUIResource(Font.MONOSPACED, Font.PLAIN, 12);
		Object sansSerifPlain18 = new SwingLazyValue(
		 "javax.swing.plaf.FontUIResource",
		 null,
		 new Object[] {Font.SANS_SERIF, Font.PLAIN, Integer.valueOf(18)});
	
		// *** Shared Colors
		ColorUIResource red = new ColorUIResource(Color.red);
		ColorUIResource black = new ColorUIResource(Color.black);
		ColorUIResource white = new ColorUIResource(Color.white);
		// ColorUIResource yellow = new ColorUIResource(Color.yellow);
		ColorUIResource gray = new ColorUIResource(Color.gray);
		// ColorUIResource lightGray = new ColorUIResource(Color.lightGray);
		ColorUIResource darkGray = new ColorUIResource(Color.darkGray);
		ColorUIResource scrollBarTrack = new ColorUIResource(224, 224, 224);

		Color control = table.getColor("control");
		Color controlDkShadow = table.getColor("controlDkShadow");
		Color controlHighlight = table.getColor("controlHighlight");
	  Color controlLtHighlight = table.getColor("controlLtHighlight");
		Color controlShadow = table.getColor("controlShadow");
		Color controlText = table.getColor("controlText");
		Color menu = table.getColor("menu");
		Color menuText = table.getColor("menuText");
		Color textHighlight = table.getColor("textHighlight");
		Color textHighlightText = table.getColor("textHighlightText");
	  Color disabledTextColor = table.getColor("textInactiveText");
		Color textText = table.getColor("textText");
		Color window = table.getColor("window");

		// *** Shared Insets
		InsetsUIResource zeroInsets = new InsetsUIResource(0, 0, 0, 0);
		InsetsUIResource twoInsets = new InsetsUIResource(2, 2, 2, 2);
		InsetsUIResource threeInsets = new InsetsUIResource(3, 3, 3, 3);

		// // *** Shared Borders
	Object marginBorder = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders$MarginBorder");
	Object etchedBorder = new BorderUIResource.EtchedBorderUIResource();
		Object loweredBevelBorder = new SwingLazyValue(
		"javax.swing.plaf.BorderUIResource",
		"getLoweredBevelBorderUIResource");
		//
		// Object popupMenuBorder = new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicBorders",
		// "getInternalFrameBorder");
		//
		 Object blackLineBorder = new SwingLazyValue(
		 "javax.swing.plaf.BorderUIResource",
		 "getBlackLineBorderUIResource");
		// Object focusCellHighlightBorder = new SwingLazyValue(
		// "javax.swing.plaf.BorderUIResource$LineBorderUIResource",
		// null,
		// new Object[] {yellow});
		//
		// Object noFocusBorder = new
		// BorderUIResource.EmptyBorderUIResource(1,1,1,1);
		//
		// Object tableHeaderBorder = new SwingLazyValue(
		// "javax.swing.plaf.BorderUIResource$BevelBorderUIResource",
		// null,
		// new Object[] { new Integer(BevelBorder.RAISED),
		// controlLtHighlight,
		// control,
		// controlDkShadow,
		// controlShadow });
		//
		//
		// // *** Button value objects
		//
//		Object buttonBorder = BorderFactory.createHTML5Border(); // an empty border, but really just the browser's border
	        Object buttonBorder =
	                new SwingLazyValue(
	                                "javax.swing.plaf.basic.BasicBorders",
	                                "getButtonBorder");

	            Object buttonToggleBorder =
	                new SwingLazyValue(
	                                "javax.swing.plaf.basic.BasicBorders",
	                                "getToggleButtonBorder");

	            Object radioButtonBorder =
	                new SwingLazyValue(
	                                "javax.swing.plaf.basic.BasicBorders",
	                                "getRadioButtonBorder");

		//
//		Object buttonToggleBorder = buttonBorder;//new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getToggleButtonBorder");
		//
		// Object radioButtonBorder =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicBorders",
		// "getRadioButtonBorder");
		//
		// // *** FileChooser / FileView value objects
		//
		// Object newFolderIcon = SwingUtilities2.makeIcon(getClass(),
		// HTML5LookAndFeel.class,
		// "icons/NewFolder.gif");
		// Object upFolderIcon = SwingUtilities2.makeIcon(getClass(),
		// HTML5LookAndFeel.class,
		// "icons/UpFolder.gif");
		// Object homeFolderIcon = SwingUtilities2.makeIcon(getClass(),
		// HTML5LookAndFeel.class,
		// "icons/HomeFolder.gif");
		// Object detailsViewIcon = SwingUtilities2.makeIcon(getClass(),
		// HTML5LookAndFeel.class,
		// "icons/DetailsView.gif");
		// Object listViewIcon = SwingUtilities2.makeIcon(getClass(),
		// HTML5LookAndFeel.class,
		// "icons/ListView.gif");
		// Object directoryIcon = SwingUtilities2.makeIcon(getClass(),
		// HTML5LookAndFeel.class,
		// "icons/Directory.gif");
		// Object fileIcon = SwingUtilities2.makeIcon(getClass(),
		// HTML5LookAndFeel.class,
		// "icons/File.gif");
		// Object computerIcon = SwingUtilities2.makeIcon(getClass(),
		// HTML5LookAndFeel.class,
		// "icons/Computer.gif");
		// Object hardDriveIcon = SwingUtilities2.makeIcon(getClass(),
		// HTML5LookAndFeel.class,
		// "icons/HardDrive.gif");
		// Object floppyDriveIcon = SwingUtilities2.makeIcon(getClass(),
		// HTML5LookAndFeel.class,
		// "icons/FloppyDrive.gif");
		//
		//
		// // *** InternalFrame value objects
		//
		// Object internalFrameBorder = new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicBorders",
		// "getInternalFrameBorder");
		//
		// // *** List value objects
		//
		Object listCellRendererActiveValue = new UIDefaults.ActiveValue() {
			@Override
			public Object createValue(UIDefaults table) {
				return new DefaultListCellRenderer.UIResource();
			}
		};

		
        Object multilineInputMap = new UIDefaults.LazyInputMap(new Object[] {
//                "control C", DefaultEditorKit.copyAction,
                "control V", DefaultEditorKit.pasteAction,
                "control X", DefaultEditorKit.cutAction,
//                     "COPY", DefaultEditorKit.copyAction,
                    "PASTE", DefaultEditorKit.pasteAction,
                      "CUT", DefaultEditorKit.cutAction,
//           "control INSERT", DefaultEditorKit.copyAction,
             "shift INSERT", DefaultEditorKit.pasteAction,
             "shift DELETE", DefaultEditorKit.cutAction,
               "shift LEFT", DefaultEditorKit.selectionBackwardAction,
              "shift RIGHT", DefaultEditorKit.selectionForwardAction,
             "control LEFT", DefaultEditorKit.previousWordAction,
            "control RIGHT", DefaultEditorKit.nextWordAction,
       "control shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
      "control shift RIGHT", DefaultEditorKit.selectionNextWordAction,
                "control A", DefaultEditorKit.selectAllAction,
//       "control BACK_SLASH", "unselect"/*DefaultEditorKit.unselectAction*/,
                     "HOME", DefaultEditorKit.beginLineAction,
                      "END", DefaultEditorKit.endLineAction,
               "shift HOME", DefaultEditorKit.selectionBeginLineAction,
                "shift END", DefaultEditorKit.selectionEndLineAction,
             "control HOME", DefaultEditorKit.beginAction,
              "control END", DefaultEditorKit.endAction,
       "control shift HOME", DefaultEditorKit.selectionBeginAction,
        "control shift END", DefaultEditorKit.selectionEndAction,
                       "UP", DefaultEditorKit.upAction,
                     "DOWN", DefaultEditorKit.downAction,
               "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
         "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
                   "ctrl H", DefaultEditorKit.deletePrevCharAction,
                   "DELETE", DefaultEditorKit.deleteNextCharAction,
              "ctrl DELETE", DefaultEditorKit.deleteNextWordAction,
          "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction,
                    "RIGHT", DefaultEditorKit.forwardAction,
                     "LEFT", DefaultEditorKit.backwardAction,
                 "KP_RIGHT", DefaultEditorKit.forwardAction,
                  "KP_LEFT", DefaultEditorKit.backwardAction,
                  "PAGE_UP", DefaultEditorKit.pageUpAction,
                "PAGE_DOWN", DefaultEditorKit.pageDownAction,
//            "shift PAGE_UP", "selection-page-up",
//          "shift PAGE_DOWN", "selection-page-down",
//       "ctrl shift PAGE_UP", "selection-page-left",
//     "ctrl shift PAGE_DOWN", "selection-page-right",
                 "shift UP", DefaultEditorKit.selectionUpAction,
               "shift DOWN", DefaultEditorKit.selectionDownAction,
                    "ENTER", DefaultEditorKit.insertBreakAction,
                      "TAB", DefaultEditorKit.insertTabAction,
//                "control T", "next-link-action",
//          "control shift T", "previous-link-action",
//            "control SPACE", "activate-link-action",
//          "control shift O", "toggle-componentOrientation"/*DefaultEditorKit.toggleComponentOrientation*/
  });

		//
		// // *** Menus value objects
		//
		// Object menuBarBorder =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicBorders",
		// "getMenuBarBorder");
		//
		// Object menuItemCheckIcon =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicIconFactory",
		// "getMenuItemCheckIcon");
		//
		// Object menuItemArrowIcon =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicIconFactory",
		// "getMenuItemArrowIcon");
		//
		//
		// Object menuArrowIcon =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicIconFactory",
		// "getMenuArrowIcon");
		//
		// Object checkBoxIcon =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicIconFactory",
		// "getCheckBoxIcon");
		//
		// Object radioButtonIcon =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicIconFactory",
		// "getRadioButtonIcon");
		//
		// Object checkBoxMenuItemIcon =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicIconFactory",
		// "getCheckBoxMenuItemIcon");
		//
		// Object radioButtonMenuItemIcon =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicIconFactory",
		// "getRadioButtonMenuItemIcon");
		//
		// Object menuItemAcceleratorDelimiter = new String("+");
		//
		// // *** OptionPane value objects
		//
		Object optionPaneMinimumSize = new DimensionUIResource(262, 90);
		//
		Integer zero = new Integer(0);
		 Object zeroBorder = new SwingLazyValue(
		 "javax.swing.plaf.BorderUIResource$EmptyBorderUIResource",
		 new Object[] {zero, zero, zero, zero});
		//
		Object optionPaneBorder = new javax.swing.plaf.BorderUIResource.EmptyBorderUIResource(10, 10, 12, 10);
		//
		 Object optionPaneButtonAreaBorder = new SwingLazyValue(
		 "javax.swing.plaf.BorderUIResource$EmptyBorderUIResource",
		 new Object[] {new Integer(6), zero, zero, zero});
		//
		//
		// // *** ProgessBar value objects
		//
		// Object progressBarBorder =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicBorders",
		// "getProgressBarBorder");
		//
		// // ** ScrollBar value objects
		//
		// Object minimumThumbSize = new DimensionUIResource(8,8);
		// Object maximumThumbSize = new DimensionUIResource(4096,4096);
		//
		// // ** Slider value objects
		//
		// Object sliderFocusInsets = twoInsets;
		//
		Object toolBarSeparatorSize = new DimensionUIResource( 10, 10 );
		//
		//
		// // *** SplitPane value objects
		//
		// Object splitPaneBorder =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicBorders",
		// "getSplitPaneBorder");
		// Object splitPaneDividerBorder =
		// new SwingLazyValue(
		// "javax.swing.plaf.basic.BasicBorders",
		// "getSplitPaneDividerBorder");
		//
		// ** TabbedBane value objects

		Object tabbedPaneTabInsets = new InsetsUIResource(2, 4, 2, 4);

		Object tabbedPaneTabPadInsets = new InsetsUIResource(2, 2, 2, 1);// selected tab size

		Object tabbedPaneTabAreaInsets = new InsetsUIResource(3, 2, 0, 2);

		Object tabbedPaneContentBorderInsets = new InsetsUIResource(2, 2, 3, 3);

		// *** Text value objects

		 Object textFieldBorder = // used for scrollpane
		 new SwingLazyValue(
		 "javax.swing.plaf.basic.BasicBorders",
		 "getTextFieldBorder");
		
		Object editorMargin = threeInsets;

		// Object caretBlinkRate = fiveHundred;
		Integer four = new Integer(4);
		//
		// Object[] allAuditoryCues = new Object[] {
		// "CheckBoxMenuItem.commandSound",
		// "InternalFrame.closeSound",
		// "InternalFrame.maximizeSound",
		// "InternalFrame.minimizeSound",
		// "InternalFrame.restoreDownSound",
		// "InternalFrame.restoreUpSound",
		// "MenuItem.commandSound",
		// "OptionPane.errorSound",
		// "OptionPane.informationSound",
		// "OptionPane.questionSound",
		// "OptionPane.warningSound",
		// "PopupMenu.popupSound",
		// "RadioButtonMenuItem.commandSound"};
		//
		// Object[] noAuditoryCues = new Object[] {"mute"};
		//
		// // *** Component Defaults
		//
		

// from MetalTheme, which is the Windows default:
//
//	    static final int CONTROL_TEXT_FONT = 0;
//	    static final int SYSTEM_TEXT_FONT = 1;
//	    static final int USER_TEXT_FONT = 2;
//	    static final int MENU_TEXT_FONT = 3;
//	    static final int WINDOW_TITLE_FONT = 4;
//	    static final int SUB_TEXT_FONT = 5;
//	    private static final int[] fontStyles = {
//	            Font.BOLD, Font.PLAIN, Font.PLAIN, Font.BOLD, Font.BOLD, Font.PLAIN
//	        };
//	        /**
//	         * Sizes for the fonts.
//	         */
//	        private static final int[] fontSizes = {
//	            12, 12, 12, 12, 12, 10
//	        };
//
		
		Object[] defaults = {
				"*.font",
				dialogPlain12,
				"*.background",
				control,
				"*.foreground",
				controlText,
				// *** Auditory Feedback
				// "AuditoryCues.cueList", allAuditoryCues,
				// "AuditoryCues.allAuditoryCues", allAuditoryCues,
				// "AuditoryCues.noAuditoryCues", noAuditoryCues,
				// // this key defines which of the various cues to render.
				// // L&Fs that want auditory feedback NEED to override playList.
				// "AuditoryCues.playList", null,

				// *** Buttons
				// "Button.defaultButtonFollowsFocus", Boolean.TRUE,
				// "Button.font", dialogPlain12,
			 "Button.background", control,
			 "Button.foreground", controlText,
			 "Button.inactiveForeground", disabledTextColor,
				// "Button.shadow", controlShadow,
				// "Button.darkShadow", controlDkShadow,
				// "Button.light", controlHighlight,
				// "Button.highlight", controlLtHighlight,
			  "Button.border", buttonBorder,
				"Button.margin",
				new InsetsUIResource(2, 14, 2, 14),
				"Button.textIconGap", four,
				// "Button.textShiftOffset", zero,
				 "Button.focusInputMap", new UIDefaults.LazyInputMap(new Object[] {
				 "SPACE", "pressed",
				 "released SPACE", "released",
				 "ENTER", "pressed",
				 "released ENTER", "released"
				 }),
				//
				// "ToggleButton.font", dialogPlain12,
				"ToggleButton.background", control,
				"ToggleButton.foreground", controlText,
				// "ToggleButton.shadow", controlShadow,
				// "ToggleButton.darkShadow", controlDkShadow,
				// "ToggleButton.light", controlHighlight,
			  "ToggleButton.highlight", controlLtHighlight,
				"ToggleButton.border", buttonBorder,
				"ToggleButton.margin",
				new InsetsUIResource(2, 14, 2, 14),
				"ToggleButton.textIconGap",
				four,
				"ToggleButton.textShiftOffset",
				zero,
				 "ToggleButton.focusInputMap",
				 new UIDefaults.LazyInputMap(new Object[] {
				 "SPACE", "pressed",
				 "released SPACE", "released"
				 }),
				
				// "RadioButton.font", dialogPlain12,
				"RadioButton.background", control,
				"RadioButton.foreground", controlText,
				// "RadioButton.shadow", controlShadow,
				// "RadioButton.darkShadow", controlDkShadow,
				// "RadioButton.light", controlHighlight,
				// "RadioButton.highlight", controlLtHighlight,
				"RadioButton.border", buttonBorder,
				"RadioButton.margin",
				twoInsets,
				"RadioButton.textIconGap",
				four,
				"RadioButton.textShiftOffset",
				zero,
//				 "RadioButton.icon", radioButtonIcon,
				 "RadioButton.focusInputMap",
				 new UIDefaults.LazyInputMap(new Object[] {
				 "SPACE", "pressed",
				 "released SPACE", "released",
				 "ENTER", "pressed" // Was mistakenly "RETURN" in Java
				 }),
				//
				// "CheckBox.font", dialogPlain12,
				"CheckBox.background", control,
			  "CheckBox.foreground", controlText,
				"CheckBox.border", buttonBorder,
				"CheckBox.margin",
				twoInsets,
				"CheckBox.textIconGap",
				four,
				"CheckBox.textShiftOffset",
				zero,
				// "CheckBox.icon", checkBoxIcon,
				 "CheckBox.focusInputMap",
				 new UIDefaults.LazyInputMap(new Object[] {
				 "SPACE", "pressed",
				 "released SPACE", "released"
				 }),
				// "FileChooser.useSystemExtensionHiding", Boolean.FALSE,

				// *** ColorChooser
				// "ColorChooser.font", dialogPlain12,
				"ColorChooser.background", control,
				"ColorChooser.foreground", controlText,
				//
				"ColorChooser.swatchesSwatchSize",
				new Dimension(10, 10),
				"ColorChooser.swatchesRecentSwatchSize",
				new Dimension(10, 10),
				"ColorChooser.swatchesDefaultRecentColor",
				control,

				// *** ComboBox
				"ComboBox.font",
				sansSerifPlain12,
				"ComboBox.background",
				window,
				"ComboBox.foreground",
				textText,
				// "ComboBox.buttonBackground", control,
				// "ComboBox.buttonShadow", controlShadow,
				// "ComboBox.buttonDarkShadow", controlDkShadow,
				// "ComboBox.buttonHighlight", controlLtHighlight,
				"ComboBox.selectionBackground", textHighlight,
				"ComboBox.selectionForeground", textHighlightText,
				 "ComboBox.disabledBackground", control,
				 "ComboBox.disabledForeground", disabledTextColor,
				"ComboBox.timeFactor",
				oneThousand,
				"ComboBox.isEnterSelectablePopup",
				Boolean.FALSE,
				 "ComboBox.ancestorInputMap",
				 new UIDefaults.LazyInputMap(new Object[] {
				 "ESCAPE", "hidePopup",
				 "PAGE_UP", "pageUpPassThrough",
				 "PAGE_DOWN", "pageDownPassThrough",
				 "HOME", "homePassThrough",
				 "END", "endPassThrough",
				 "ENTER", "enterPressed"
				 }),
				//
				// *** FileChooser

				// "FileChooser.newFolderIcon", newFolderIcon,
				// "FileChooser.upFolderIcon", upFolderIcon,
				// "FileChooser.homeFolderIcon", homeFolderIcon,
				// "FileChooser.detailsViewIcon", detailsViewIcon,
				// "FileChooser.listViewIcon", listViewIcon,
				"FileChooser.readOnly",
				Boolean.FALSE,
				// "FileChooser.usesSingleFilePane", Boolean.FALSE,
				// "FileChooser.ancestorInputMap",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "ESCAPE", "cancelSelection",
				// "F5", "refresh",
				// }),
				//
				// "FileView.directoryIcon", directoryIcon,
				// "FileView.fileIcon", fileIcon,
				// "FileView.computerIcon", computerIcon,
				// "FileView.hardDriveIcon", hardDriveIcon,
				// "FileView.floppyDriveIcon", floppyDriveIcon,

				// *** InternalFrame
				// "InternalFrame.titleFont", dialogBold12,
				// "InternalFrame.borderColor", control,
				// "InternalFrame.borderShadow", controlShadow,
				// "InternalFrame.borderDarkShadow", controlDkShadow,
				// "InternalFrame.borderHighlight", controlLtHighlight,
				// "InternalFrame.borderLight", controlHighlight,
				// "InternalFrame.border", internalFrameBorder,
				// "InternalFrame.icon", SwingUtilities2.makeIcon(getClass(),
				// HTML5LookAndFeel.class,
				// "icons/JavaCup16.png"),
				//
				// /* Default frame icons are undefined for Basic. */
				// "InternalFrame.maximizeIcon",
				// new SwingLazyValue(
				// "javax.swing.plaf.basic.BasicIconFactory",
				// "createEmptyFrameIcon"),
				// "InternalFrame.minimizeIcon",
				// new SwingLazyValue(
				// "javax.swing.plaf.basic.BasicIconFactory",
				// "createEmptyFrameIcon"),
				// "InternalFrame.iconifyIcon",
				// new SwingLazyValue(
				// "javax.swing.plaf.basic.BasicIconFactory",
				// "createEmptyFrameIcon"),
				// "InternalFrame.closeIcon",
				// new SwingLazyValue(
				// "javax.swing.plaf.basic.BasicIconFactory",
				// "createEmptyFrameIcon"),
				// // InternalFrame Auditory Cue Mappings
				// "InternalFrame.closeSound", null,
				// "InternalFrame.maximizeSound", null,
				// "InternalFrame.minimizeSound", null,
				// "InternalFrame.restoreDownSound", null,
				// "InternalFrame.restoreUpSound", null,
				//
				// "InternalFrame.activeTitleBackground", table.get("activeCaption"),
				// "InternalFrame.activeTitleForeground",
				// table.get("activeCaptionText"),
				// "InternalFrame.inactiveTitleBackground",
				// table.get("inactiveCaption"),
				// "InternalFrame.inactiveTitleForeground",
				// table.get("inactiveCaptionText"),
				// "InternalFrame.windowBindings", new Object[] {
				// "shift ESCAPE", "showSystemMenu",
				// "ctrl SPACE", "showSystemMenu",
				// "ESCAPE", "hideSystemMenu"},
				//
				"InternalFrameTitlePane.iconifyButtonOpacity", Boolean.TRUE,
				"InternalFrameTitlePane.maximizeButtonOpacity", Boolean.TRUE,
				"InternalFrameTitlePane.closeButtonOpacity", Boolean.TRUE,

				//"DesktopIcon.border", internalFrameBorder,
				
				"Desktop.minOnScreenInsets", threeInsets,
				"Desktop.background", table.get("desktop"),
				//"Desktop.ancestorInputMap",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "ctrl F5", "restore",
				// "ctrl F4", "close",
				// "ctrl F7", "move",
				// "ctrl F8", "resize",
				// "RIGHT", "right",
				// "KP_RIGHT", "right",
				// "shift RIGHT", "shrinkRight",
				// "shift KP_RIGHT", "shrinkRight",
				// "LEFT", "left",
				// "KP_LEFT", "left",
				// "shift LEFT", "shrinkLeft",
				// "shift KP_LEFT", "shrinkLeft",
				// "UP", "up",
				// "KP_UP", "up",
				// "shift UP", "shrinkUp",
				// "shift KP_UP", "shrinkUp",
				// "DOWN", "down",
				// "KP_DOWN", "down",
				// "shift DOWN", "shrinkDown",
				// "shift KP_DOWN", "shrinkDown",
				// "ESCAPE", "escape",
				// "ctrl F9", "minimize",
				// "ctrl F10", "maximize",
				// "ctrl F6", "selectNextFrame",
				// "ctrl TAB", "selectNextFrame",
				// "ctrl alt F6", "selectNextFrame",
				// "shift ctrl alt F6", "selectPreviousFrame",
				// "ctrl F12", "navigateNext",
				// "shift ctrl F12", "navigatePrevious"
				// }),

				// *** Label
				"Label.font", dialogBold12,
				"Label.background", control,
				"Label.foreground", controlText,
			    "Label.inactiveForeground", disabledTextColor,
				// "Label.disabledShadow", controlShadow,
				"Label.border",
				null,

				// *** List
				// "List.font", dialogPlain12,
				"List.background",
				window,
				"List.foreground",
				textText,
			  "List.selectionBackground", textHighlight,
				"List.selectionForeground", textHighlightText,
				// "List.noFocusBorder", noFocusBorder,
				// "List.focusCellHighlightBorder", focusCellHighlightBorder,
				// "List.dropLineColor", controlShadow,
				// "List.border", null,
				"List.cellRenderer",
				listCellRendererActiveValue,
				"List.timeFactor",
				oneThousand,
				 "List.focusInputMap",
				 new UIDefaults.LazyInputMap(new Object[] {
				 "ctrl C", "copy",
				 "ctrl V", "paste",
				 "ctrl X", "cut",
				 "COPY", "copy",
				 "PASTE", "paste",
				 "CUT", "cut",
				 "control INSERT", "copy",
				 "shift INSERT", "paste",
				 "shift DELETE", "cut",
				 "UP", "selectPreviousRow",
				 "KP_UP", "selectPreviousRow",
				 "shift UP", "selectPreviousRowExtendSelection",
				 "shift KP_UP", "selectPreviousRowExtendSelection",
				 "ctrl shift UP", "selectPreviousRowExtendSelection",
				 "ctrl shift KP_UP", "selectPreviousRowExtendSelection",
				 "ctrl UP", "selectPreviousRowChangeLead",
				 "ctrl KP_UP", "selectPreviousRowChangeLead",
				 "DOWN", "selectNextRow",
				 "KP_DOWN", "selectNextRow",
				 "shift DOWN", "selectNextRowExtendSelection",
				 "shift KP_DOWN", "selectNextRowExtendSelection",
				 "ctrl shift DOWN", "selectNextRowExtendSelection",
				 "ctrl shift KP_DOWN", "selectNextRowExtendSelection",
				 "ctrl DOWN", "selectNextRowChangeLead",
				 "ctrl KP_DOWN", "selectNextRowChangeLead",
				 "LEFT", "selectPreviousColumn",
				 "KP_LEFT", "selectPreviousColumn",
				 "shift LEFT", "selectPreviousColumnExtendSelection",
				 "shift KP_LEFT", "selectPreviousColumnExtendSelection",
				 "ctrl shift LEFT", "selectPreviousColumnExtendSelection",
				 "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection",
				 "ctrl LEFT", "selectPreviousColumnChangeLead",
				 "ctrl KP_LEFT", "selectPreviousColumnChangeLead",
				 "RIGHT", "selectNextColumn",
				 "KP_RIGHT", "selectNextColumn",
				 "shift RIGHT", "selectNextColumnExtendSelection",
				 "shift KP_RIGHT", "selectNextColumnExtendSelection",
				 "ctrl shift RIGHT", "selectNextColumnExtendSelection",
				 "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection",
				 "ctrl RIGHT", "selectNextColumnChangeLead",
				 "ctrl KP_RIGHT", "selectNextColumnChangeLead",
				 "HOME", "selectFirstRow",
				 "shift HOME", "selectFirstRowExtendSelection",
				 "ctrl shift HOME", "selectFirstRowExtendSelection",
				 "ctrl HOME", "selectFirstRowChangeLead",
				 "END", "selectLastRow",
				 "shift END", "selectLastRowExtendSelection",
				 "ctrl shift END", "selectLastRowExtendSelection",
				 "ctrl END", "selectLastRowChangeLead",
				 "PAGE_UP", "scrollUp",
				 "shift PAGE_UP", "scrollUpExtendSelection",
				 "ctrl shift PAGE_UP", "scrollUpExtendSelection",
				 "ctrl PAGE_UP", "scrollUpChangeLead",
				 "PAGE_DOWN", "scrollDown",
				 "shift PAGE_DOWN", "scrollDownExtendSelection",
				 "ctrl shift PAGE_DOWN", "scrollDownExtendSelection",
				 "ctrl PAGE_DOWN", "scrollDownChangeLead",
				 "ctrl A", "selectAll",
				 "ctrl SLASH", "selectAll",
				 "ctrl BACK_SLASH", "clearSelection",
				 "SPACE", "addToSelection",
				 "ctrl SPACE", "toggleAndAnchor",
				 "shift SPACE", "extendTo",
				 "ctrl shift SPACE", "moveSelectionTo"
				 }),
				 "List.focusInputMap.RightToLeft",
				 new UIDefaults.LazyInputMap(new Object[] {
				 "LEFT", "selectNextColumn",
				 "KP_LEFT", "selectNextColumn",
				 "shift LEFT", "selectNextColumnExtendSelection",
				 "shift KP_LEFT", "selectNextColumnExtendSelection",
				 "ctrl shift LEFT", "selectNextColumnExtendSelection",
				 "ctrl shift KP_LEFT", "selectNextColumnExtendSelection",
				 "ctrl LEFT", "selectNextColumnChangeLead",
				 "ctrl KP_LEFT", "selectNextColumnChangeLead",
				 "RIGHT", "selectPreviousColumn",
				 "KP_RIGHT", "selectPreviousColumn",
				 "shift RIGHT", "selectPreviousColumnExtendSelection",
				 "shift KP_RIGHT", "selectPreviousColumnExtendSelection",
				 "ctrl shift RIGHT", "selectPreviousColumnExtendSelection",
				 "ctrl shift KP_RIGHT", "selectPreviousColumnExtendSelection",
				 "ctrl RIGHT", "selectPreviousColumnChangeLead",
				 "ctrl KP_RIGHT", "selectPreviousColumnChangeLead",
				 }),
				//
				// *** Menus
			  "MenuBar.font", dialogPlain12,
				"MenuBar.background", menu,
				"MenuBar.foreground", menuText,
				// "MenuBar.shadow", controlShadow,
				// "MenuBar.highlight", controlLtHighlight,
				// "MenuBar.border", menuBarBorder,
				// "MenuBar.windowBindings", new Object[] {
				// "F10", "takeFocus" },
				//
				"MenuItem.font", dialogPlain12,
				//"MenuItem.acceleratorFont", dialogPlain12,
				"MenuItem.background",
				menu,
				"MenuItem.foreground",
				menuText,
				// "MenuItem.selectionForeground", textHighlightText,
				// "MenuItem.selectionBackground", textHighlight,
				// "MenuItem.disabledForeground", null,
				// "MenuItem.acceleratorForeground", menuText,
				// "MenuItem.acceleratorSelectionForeground", textHighlightText,
				// "MenuItem.acceleratorDelimiter", menuItemAcceleratorDelimiter,
				"MenuItem.border", marginBorder,
				"MenuItem.borderPainted", Boolean.FALSE,
				"MenuItem.margin",
				twoInsets,
				// "MenuItem.checkIcon", menuItemCheckIcon,
				// "MenuItem.arrowIcon", menuItemArrowIcon,
				// "MenuItem.commandSound", null,
				//
			  "RadioButtonMenuItem.font", dialogPlain12,
				// "RadioButtonMenuItem.acceleratorFont", dialogPlain12,
				"RadioButtonMenuItem.background",
				menu,
				"RadioButtonMenuItem.foreground",
				menuText,
				// "RadioButtonMenuItem.selectionForeground", textHighlightText,
				// "RadioButtonMenuItem.selectionBackground", textHighlight,
				// "RadioButtonMenuItem.disabledForeground", null,
				// "RadioButtonMenuItem.acceleratorForeground", menuText,
				// "RadioButtonMenuItem.acceleratorSelectionForeground",
				// textHighlightText,
				"RadioButtonMenuItem.border", marginBorder,
				// "RadioButtonMenuItem.borderPainted", Boolean.FALSE,
				"RadioButtonMenuItem.margin",
				twoInsets,
				// "RadioButtonMenuItem.checkIcon", radioButtonMenuItemIcon,
				// "RadioButtonMenuItem.arrowIcon", menuItemArrowIcon,
				// "RadioButtonMenuItem.commandSound", null,

			 "CheckBoxMenuItem.font", dialogPlain12,
				// "CheckBoxMenuItem.acceleratorFont", dialogPlain12,
				"CheckBoxMenuItem.background",
				menu,
				"CheckBoxMenuItem.foreground",
				menuText,
				// "CheckBoxMenuItem.selectionForeground", textHighlightText,
				// "CheckBoxMenuItem.selectionBackground", textHighlight,
				// "CheckBoxMenuItem.disabledForeground", null,
				// "CheckBoxMenuItem.acceleratorForeground", menuText,
				// "CheckBoxMenuItem.acceleratorSelectionForeground", textHighlightText,
				"CheckBoxMenuItem.border", marginBorder,
				"CheckBoxMenuItem.borderPainted", Boolean.FALSE,
				"CheckBoxMenuItem.margin",
				twoInsets,
				// "CheckBoxMenuItem.checkIcon", checkBoxMenuItemIcon,
				// "CheckBoxMenuItem.arrowIcon", menuItemArrowIcon,
				// "CheckBoxMenuItem.commandSound", null,

				// "Menu.font", dialogPlain12,
				// "Menu.acceleratorFont", dialogPlain12,
				"Menu.background",
				menu,
				"Menu.foreground",
				menuText,
				// "Menu.selectionForeground", textHighlightText,
				// "Menu.selectionBackground", textHighlight,
				// "Menu.disabledForeground", null,
				// "Menu.acceleratorForeground", menuText,
				// "Menu.acceleratorSelectionForeground", textHighlightText,
				"Menu.border", marginBorder,
				"Menu.borderPainted", Boolean.FALSE,
				"Menu.margin",
				twoInsets,
				// "Menu.checkIcon", menuItemCheckIcon,
				// "Menu.arrowIcon", menuArrowIcon,
				// "Menu.menuPopupOffsetX", new Integer(0),
				// "Menu.menuPopupOffsetY", new Integer(0),
				// "Menu.submenuPopupOffsetX", new Integer(0),
				// "Menu.submenuPopupOffsetY", new Integer(0),
				// "Menu.shortcutKeys", new int[] {KeyEvent.ALT_MASK},
				 "Menu.crossMenuMnemonic", Boolean.FALSE,//SwingJS using Windows version // Boolean.TRUE,
				// Menu.cancelMode affects the cancel menu action behaviour;
				// currently supports:
				// "hideLastSubmenu" (default)
				// hides the last open submenu,
				// and move selection one step back
				// "hideMenuTree"
				// resets selection and
				// hide the entire structure of open menu and its submenus
				// "Menu.cancelMode", "hideLastSubmenu",

				// Menu.preserveTopLevelSelection affects
				// the cancel menu action behaviour
				// if set to true then top level menu selection
				// will be preserved when the last popup was cancelled;
				// the menu itself will be unselect with the next cancel action
				// "Menu.preserveTopLevelSelection", Boolean.FALSE,

				// PopupMenu
				// "PopupMenu.font", dialogPlain12,
				"PopupMenu.background",
				menu,
				"PopupMenu.foreground",
				menuText,
				// "PopupMenu.border", popupMenuBorder,
				// Internal Frame Auditory Cue Mappings
				// "PopupMenu.popupSound", null,
				 // These window InputMap bindings are used when the Menu is
				 // selected.
				 "PopupMenu.selectedWindowInputMapBindings", new Object[] {
				 "ESCAPE", "cancel",
				 "DOWN", "selectNext",
				 "KP_DOWN", "selectNext",
				 "UP", "selectPrevious",
				 "KP_UP", "selectPrevious",
				 "LEFT", "selectParent",
				 "KP_LEFT", "selectParent",
				 "RIGHT", "selectChild",
				 "KP_RIGHT", "selectChild",
				 "ENTER", "return",
				 "ctrl ENTER", "return",
				 "SPACE", "return"
				 },
				// "PopupMenu.selectedWindowInputMapBindings.RightToLeft", new Object[]
				// {
				// "LEFT", "selectChild",
				// "KP_LEFT", "selectChild",
				// "RIGHT", "selectParent",
				// "KP_RIGHT", "selectParent",
				// },
				"PopupMenu.consumeEventOnClose",
				Boolean.FALSE,

				// *** OptionPane
				// You can additionaly define OptionPane.messageFont which will
				// dictate the fonts used for the message, and
				// OptionPane.buttonFont, which defines the font for the buttons.
				"OptionPane.yesButtonText", "Yes",
				"OptionPane.noButtonText", "No",
				"OptionPane.cancelButtonText", "Cancel",
				"OptionPane.okButtonText", "OK",
				 "OptionPane.font", dialogPlain12,
			  "OptionPane.background", control,
			  "OptionPane.foreground", controlText,
				"OptionPane.messageForeground",
				controlText,
				 "OptionPane.border", optionPaneBorder,
				 "OptionPane.messageAreaBorder", zeroBorder,
				 "OptionPane.buttonAreaBorder", optionPaneButtonAreaBorder,
				 "OptionPane.minimumSize", optionPaneMinimumSize,
				// "OptionPane.errorIcon", SwingUtilities2.makeIcon(getClass(),
				// HTML5LookAndFeel.class,
				// "icons/Error.gif"),
				// "OptionPane.informationIcon", SwingUtilities2.makeIcon(getClass(),
				// HTML5LookAndFeel.class,
				// "icons/Inform.gif"),
				// "OptionPane.warningIcon", SwingUtilities2.makeIcon(getClass(),
				// HTML5LookAndFeel.class,
				// "icons/Warn.gif"),
				// "OptionPane.questionIcon", SwingUtilities2.makeIcon(getClass(),
				// HTML5LookAndFeel.class,
				// "icons/Question.gif"),
				// "OptionPane.windowBindings", new Object[] {
				// "ESCAPE", "close" },
				// // OptionPane Auditory Cue Mappings
				// "OptionPane.errorSound", null,
				// "OptionPane.informationSound", null, // Info and Plain
				// "OptionPane.questionSound", null,
				// "OptionPane.warningSound", null,
				// "OptionPane.buttonClickThreshhold", fiveHundred,

				// *** Panel
				 "Panel.font", 
				 dialogPlain12,
				 "Panel.background", 
				 control,
				 "Panel.foreground",
	       textText,

				// *** ProgressBar
				// "ProgressBar.font", dialogPlain12,
				// "ProgressBar.foreground", textHighlight,
				// "ProgressBar.background", control,
				// "ProgressBar.selectionForeground", control,
				// "ProgressBar.selectionBackground", textHighlight,
				// "ProgressBar.border", progressBarBorder,
				"ProgressBar.cellLength",
				new Integer(1),
				"ProgressBar.cellSpacing",
				zero,
				"ProgressBar.repaintInterval",
				new Integer(50),
				"ProgressBar.cycleTime",
				new Integer(3000),
				"ProgressBar.horizontalSize",
				new DimensionUIResource(146, 12),
				"ProgressBar.verticalSize",
				new DimensionUIResource(12, 146),

				// *** Separator
				// "Separator.shadow", controlShadow, // DEPRECATED - DO NOT USE!
				// "Separator.highlight", controlLtHighlight, // DEPRECATED - DO NOT
				// USE!
				//
				// "Separator.background", controlLtHighlight,
				// "Separator.foreground", controlShadow,

				// *** ScrollBar/ScrollPane/Viewport
				"ScrollBar.background", scrollBarTrack,
				"ScrollBar.foreground",
				control,
				// "ScrollBar.track", table.get("scrollbar"),
				// "ScrollBar.trackHighlight", controlDkShadow,
				// "ScrollBar.thumb", control,
				// "ScrollBar.thumbHighlight", controlLtHighlight,
				// "ScrollBar.thumbDarkShadow", controlDkShadow,
				// "ScrollBar.thumbShadow", controlShadow,
				// "ScrollBar.border", null,
				// "ScrollBar.minimumThumbSize", minimumThumbSize,
				// "ScrollBar.maximumThumbSize", maximumThumbSize,
				// "ScrollBar.ancestorInputMap",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "RIGHT", "positiveUnitIncrement",
				// "KP_RIGHT", "positiveUnitIncrement",
				// "DOWN", "positiveUnitIncrement",
				// "KP_DOWN", "positiveUnitIncrement",
				// "PAGE_DOWN", "positiveBlockIncrement",
				// "LEFT", "negativeUnitIncrement",
				// "KP_LEFT", "negativeUnitIncrement",
				// "UP", "negativeUnitIncrement",
				// "KP_UP", "negativeUnitIncrement",
				// "PAGE_UP", "negativeBlockIncrement",
				// "HOME", "minScroll",
				// "END", "maxScroll"
				// }),
				// "ScrollBar.ancestorInputMap.RightToLeft",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "RIGHT", "negativeUnitIncrement",
				// "KP_RIGHT", "negativeUnitIncrement",
				// "LEFT", "positiveUnitIncrement",
				// "KP_LEFT", "positiveUnitIncrement",
				// }),
				"ScrollBar.width",
				new Integer(16),

				// "ScrollPane.font", dialogPlain12,
				"ScrollPane.background", control,
				"ScrollPane.foreground", controlText,
				"ScrollPane.border", textFieldBorder,
				// "ScrollPane.viewportBorder", null,
				// "ScrollPane.ancestorInputMap",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "RIGHT", "unitScrollRight",
				// "KP_RIGHT", "unitScrollRight",
				// "DOWN", "unitScrollDown",
				// "KP_DOWN", "unitScrollDown",
				// "LEFT", "unitScrollLeft",
				// "KP_LEFT", "unitScrollLeft",
				// "UP", "unitScrollUp",
				// "KP_UP", "unitScrollUp",
				// "PAGE_UP", "scrollUp",
				// "PAGE_DOWN", "scrollDown",
				// "ctrl PAGE_UP", "scrollLeft",
				// "ctrl PAGE_DOWN", "scrollRight",
				// "ctrl HOME", "scrollHome",
				// "ctrl END", "scrollEnd"
				// }),
				// "ScrollPane.ancestorInputMap.RightToLeft",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "ctrl PAGE_UP", "scrollRight",
				// "ctrl PAGE_DOWN", "scrollLeft",
				// }),
				//
				// "Viewport.font", dialogPlain12,
				"Viewport.background", control,
				"Viewport.foreground",
				textText,

				// *** Slider
				"Slider.font", dialogPlain12,
				"Slider.foreground", control,
				"Slider.background", control,
				// "Slider.highlight", controlLtHighlight,
				// "Slider.tickColor", Color.black,
				// "Slider.shadow", controlShadow,
				// "Slider.focus", controlDkShadow,
				// "Slider.border", null,
				"Slider.horizontalSize",
				new Dimension(200, 21),
				"Slider.verticalSize",
				new Dimension(21, 200),
				"Slider.minimumHorizontalSize",
				new Dimension(36, 21),
				"Slider.minimumVerticalSize",
				new Dimension(21, 36),
				// "Slider.focusInsets", sliderFocusInsets,
				 "Slider.focusInputMap",
				 new UIDefaults.LazyInputMap(new Object[] {
				 "RIGHT", "positiveUnitIncrement",
				 "KP_RIGHT", "positiveUnitIncrement",
				 "DOWN", "negativeUnitIncrement",
				 "KP_DOWN", "negativeUnitIncrement",
				 "PAGE_DOWN", "negativeBlockIncrement",
				 "LEFT", "negativeUnitIncrement",
				 "KP_LEFT", "negativeUnitIncrement",
				 "UP", "positiveUnitIncrement",
				 "KP_UP", "positiveUnitIncrement",
				 "PAGE_UP", "positiveBlockIncrement",
				 "HOME", "minScroll",
				 "END", "maxScroll"
				 }),
				 "Slider.focusInputMap.RightToLeft",
				 new UIDefaults.LazyInputMap(new Object[] {
				 "RIGHT", "negativeUnitIncrement",
				 "KP_RIGHT", "negativeUnitIncrement",
				 "LEFT", "positiveUnitIncrement",
				 "KP_LEFT", "positiveUnitIncrement",
				 }),
				//
				// *** Spinner
				"Spinner.font",
				sansSerifPlain12,
				"Spinner.background", control,
				"Spinner.foreground", control,
				// "Spinner.border", textFieldBorder,
				// "Spinner.arrowButtonBorder", null,
				// "Spinner.arrowButtonInsets", null,
				"Spinner.arrowButtonSize",
				new Dimension(16, 5),
				// "Spinner.ancestorInputMap",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "UP", "increment",
				// "KP_UP", "increment",
				// "DOWN", "decrement",
				// "KP_DOWN", "decrement",
				// }),
				// "Spinner.editorBorderPainted", Boolean.FALSE,
				//"Spinner.editorAlignment",
				//JTextField.TRAILING,

				// *** SplitPane
				"SplitPane.background",
				control,
				// "SplitPane.highlight", controlLtHighlight,
				// "SplitPane.shadow", controlShadow,
				// "SplitPane.darkShadow", controlDkShadow,
				// "SplitPane.border", splitPaneBorder,
				"SplitPane.dividerSize",
				new Integer(7),
				// "SplitPaneDivider.border", splitPaneDividerBorder,
				"SplitPaneDivider.draggingColor",
				darkGray,
				// "SplitPane.ancestorInputMap",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "UP", "negativeIncrement",
				// "DOWN", "positiveIncrement",
				// "LEFT", "negativeIncrement",
				// "RIGHT", "positiveIncrement",
				// "KP_UP", "negativeIncrement",
				// "KP_DOWN", "positiveIncrement",
				// "KP_LEFT", "negativeIncrement",
				// "KP_RIGHT", "positiveIncrement",
				// "HOME", "selectMin",
				// "END", "selectMax",
				// "F8", "startResize",
				// "F6", "toggleFocus",
				// "ctrl TAB", "focusOutForward",
				// "ctrl shift TAB", "focusOutBackward"
				// }),
				//
				// *** TabbedPane
				"TabbedPane.font", dialogPlain12,
				"TabbedPane.background", control,
				"TabbedPane.foreground", controlText,
				"TabbedPane.highlight", controlLtHighlight,
				"TabbedPane.light", controlHighlight,
				"TabbedPane.shadow", controlShadow,
				"TabbedPane.darkShadow", controlDkShadow,
				"TabbedPane.selected",
				null,
				// "TabbedPane.focus", controlText,
				"TabbedPane.textIconGap",
				four,

				// Causes tabs to be painted on top of the content area border.
				// The amount of overlap is then controlled by tabAreaInsets.bottom,
				// which is zero by default
				"TabbedPane.tabsOverlapBorder",
				Boolean.FALSE,
				// "TabbedPane.selectionFollowsFocus", Boolean.TRUE,

				"TabbedPane.labelShift",
				1,
				"TabbedPane.selectedLabelShift",
				-1,
				"TabbedPane.tabInsets",
				tabbedPaneTabInsets,
				"TabbedPane.selectedTabPadInsets",
				tabbedPaneTabPadInsets,
				"TabbedPane.tabAreaInsets",
				tabbedPaneTabAreaInsets,
				"TabbedPane.contentBorderInsets",
				tabbedPaneContentBorderInsets,
				"TabbedPane.tabRunOverlay",
				new Integer(2),
				// "TabbedPane.tabsOpaque", Boolean.TRUE,
				// "TabbedPane.contentOpaque", Boolean.TRUE,
				 "TabbedPane.focusInputMap",
				 new UIDefaults.LazyInputMap(new Object[] {
				 "RIGHT", "navigateRight",
				 "KP_RIGHT", "navigateRight",
				 "LEFT", "navigateLeft",
				 "KP_LEFT", "navigateLeft",
				 "UP", "navigateUp",
				 "KP_UP", "navigateUp",
				 "DOWN", "navigateDown",
				 "KP_DOWN", "navigateDown",
				 "ctrl DOWN", "requestFocusForVisibleComponent",
				 "ctrl KP_DOWN", "requestFocusForVisibleComponent",
				 }),
				// "TabbedPane.ancestorInputMap",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "ctrl PAGE_DOWN", "navigatePageDown",
				// "ctrl PAGE_UP", "navigatePageUp",
				// "ctrl UP", "requestFocus",
				// "ctrl KP_UP", "requestFocus",
				// }),
				//
				//
				// *** Table
				"Table.font", dialogPlain12,
				"Table.foreground", controlText, // cell text color
				"Table.background", white, // cell background color
				"Table.selectionForeground", textHighlightText,
				"Table.selectionBackground", textHighlight,
			   "Table.dropLineColor", controlShadow,
				"Table.dropLineShortColor",
				black,
				"Table.gridColor",
				controlShadow,//gray, // grid line color
				"Table.focusCellBackground",
				window,
				"Table.focusCellForeground",
				controlText,
				//"Table.focusCellHighlightBorder", focusCellHighlightBorder,
				"Table.scrollPaneBorder", loweredBevelBorder,
				 "Table.ancestorInputMap",
				 new UIDefaults.LazyInputMap(new Object[] {
				// "ctrl C", "copy",
				// "ctrl V", "paste",
				// "ctrl X", "cut",
				// "COPY", "copy",
				// "PASTE", "paste",
				// "CUT", "cut",
				// "control INSERT", "copy",
				// "shift INSERT", "paste",
				// "shift DELETE", "cut",
				 "RIGHT", "selectNextColumn",
				 "KP_RIGHT", "selectNextColumn",
				 "shift RIGHT", "selectNextColumnExtendSelection",
				 "shift KP_RIGHT", "selectNextColumnExtendSelection",
				// "ctrl shift RIGHT", "selectNextColumnExtendSelection",
				// "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection",
				// "ctrl RIGHT", "selectNextColumnChangeLead",
				// "ctrl KP_RIGHT", "selectNextColumnChangeLead",
				 "LEFT", "selectPreviousColumn",
				 "KP_LEFT", "selectPreviousColumn",
				 "shift LEFT", "selectPreviousColumnExtendSelection",
				 "shift KP_LEFT", "selectPreviousColumnExtendSelection",
				// "ctrl shift LEFT", "selectPreviousColumnExtendSelection",
				// "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection",
				// "ctrl LEFT", "selectPreviousColumnChangeLead",
				// "ctrl KP_LEFT", "selectPreviousColumnChangeLead",
				 "DOWN", "selectNextRow",
				 "KP_DOWN", "selectNextRow",
				 "shift DOWN", "selectNextRowExtendSelection",
				 "shift KP_DOWN", "selectNextRowExtendSelection",
				// "ctrl shift DOWN", "selectNextRowExtendSelection",
				// "ctrl shift KP_DOWN", "selectNextRowExtendSelection",
				// "ctrl DOWN", "selectNextRowChangeLead",
				// "ctrl KP_DOWN", "selectNextRowChangeLead",
				 "UP", "selectPreviousRow",
				 "KP_UP", "selectPreviousRow",
				 "shift UP", "selectPreviousRowExtendSelection",
				 "shift KP_UP", "selectPreviousRowExtendSelection",
				// "ctrl shift UP", "selectPreviousRowExtendSelection",
				// "ctrl shift KP_UP", "selectPreviousRowExtendSelection",
				// "ctrl UP", "selectPreviousRowChangeLead",
				// "ctrl KP_UP", "selectPreviousRowChangeLead",
				 "HOME", "selectFirstColumn",
				 "shift HOME", "selectFirstColumnExtendSelection",
				// "ctrl shift HOME", "selectFirstRowExtendSelection",
				// "ctrl HOME", "selectFirstRow",
				 "END", "selectLastColumn",
				 "shift END", "selectLastColumnExtendSelection",
				// "ctrl shift END", "selectLastRowExtendSelection",
				// "ctrl END", "selectLastRow",
				 "PAGE_UP", "scrollUpChangeSelection",
				 "shift PAGE_UP", "scrollUpExtendSelection",
				// "ctrl shift PAGE_UP", "scrollLeftExtendSelection",
				// "ctrl PAGE_UP", "scrollLeftChangeSelection",
				 "PAGE_DOWN", "scrollDownChangeSelection",
				 "shift PAGE_DOWN", "scrollDownExtendSelection",
				// "ctrl shift PAGE_DOWN", "scrollRightExtendSelection",
				// "ctrl PAGE_DOWN", "scrollRightChangeSelection",
				// "TAB", "selectNextColumnCell",
				// "shift TAB", "selectPreviousColumnCell",
				 "ENTER", "selectNextRowCell",
				 "shift ENTER", "selectPreviousRowCell",
				 "ctrl A", "selectAll",
				// "ctrl SLASH", "selectAll",
				// "ctrl BACK_SLASH", "clearSelection",
				 "ESCAPE", "cancel",
				 "F2", "startEditing",
				// "SPACE", "addToSelection",
				// "ctrl SPACE", "toggleAndAnchor",
				// "shift SPACE", "extendTo",
				// "ctrl shift SPACE", "moveSelectionTo",
				// "F8", "focusHeader"
				 }),
				// "Table.ancestorInputMap.RightToLeft",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "RIGHT", "selectPreviousColumn",
				// "KP_RIGHT", "selectPreviousColumn",
				// "shift RIGHT", "selectPreviousColumnExtendSelection",
				// "shift KP_RIGHT", "selectPreviousColumnExtendSelection",
				// "ctrl shift RIGHT", "selectPreviousColumnExtendSelection",
				// "ctrl shift KP_RIGHT", "selectPreviousColumnExtendSelection",
				// "ctrl RIGHT", "selectPreviousColumnChangeLead",
				// "ctrl KP_RIGHT", "selectPreviousColumnChangeLead",
				// "LEFT", "selectNextColumn",
				// "KP_LEFT", "selectNextColumn",
				// "shift LEFT", "selectNextColumnExtendSelection",
				// "shift KP_LEFT", "selectNextColumnExtendSelection",
				// "ctrl shift LEFT", "selectNextColumnExtendSelection",
				// "ctrl shift KP_LEFT", "selectNextColumnExtendSelection",
				// "ctrl LEFT", "selectNextColumnChangeLead",
				// "ctrl KP_LEFT", "selectNextColumnChangeLead",
				// "ctrl PAGE_UP", "scrollRightChangeSelection",
				// "ctrl PAGE_DOWN", "scrollLeftChangeSelection",
				// "ctrl shift PAGE_UP", "scrollRightExtendSelection",
				// "ctrl shift PAGE_DOWN", "scrollLeftExtendSelection",
				// }),
				// "Table.ascendingSortIcon", new SwingLazyValue(
				// "sun.swing.icon.SortArrowIcon",
				// null, new Object[] { Boolean.TRUE,
				// "Table.sortIconColor" }),
				// "Table.descendingSortIcon", new SwingLazyValue(
				// "sun.swing.icon.SortArrowIcon",
				// null, new Object[] { Boolean.FALSE,
				// "Table.sortIconColor" }),
			  "Table.sortIconColor", controlShadow,
				//
				"TableHeader.font", dialogPlain12,
				"TableHeader.foreground", controlText, // header text color
				"TableHeader.background", control, // header background
				//"TableHeader.cellBorder", tableHeaderBorder,

				// Support for changing the background/border of the currently
				// selected header column when the header has the keyboard focus.
				// "TableHeader.focusCellBackground", table.getColor("text"), // like
				// text component bg
				// "TableHeader.focusCellForeground", null,
				// "TableHeader.focusCellBorder", null,
				// "TableHeader.ancestorInputMap",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "SPACE", "toggleSortOrder",
				// "LEFT", "selectColumnToLeft",
				// "KP_LEFT", "selectColumnToLeft",
				// "RIGHT", "selectColumnToRight",
				// "KP_RIGHT", "selectColumnToRight",
				// "alt LEFT", "moveColumnLeft",
				// "alt KP_LEFT", "moveColumnLeft",
				// "alt RIGHT", "moveColumnRight",
				// "alt KP_RIGHT", "moveColumnRight",
				// "alt shift LEFT", "resizeLeft",
				// "alt shift KP_LEFT", "resizeLeft",
				// "alt shift RIGHT", "resizeRight",
				// "alt shift KP_RIGHT", "resizeRight",
				// "ESCAPE", "focusTable",
				// }),
				//
				// *** Text
				"TextField.font",
				sansSerifPlain12,
				"TextField.background",
				window,
				"TextField.foreground",
				textText,
				 "TextField.shadow", controlShadow,
				 "TextField.darkShadow", controlDkShadow,
				 "TextField.light", controlHighlight,
				 "TextField.highlight", controlLtHighlight,
				"TextField.inactiveForeground", disabledTextColor,
				"TextField.inactiveBackground", control,
				// "TextField.selectionBackground", textHighlight,
				// "TextField.selectionForeground", textHighlightText,
				// "TextField.caretForeground", textText,
				// "TextField.caretBlinkRate", caretBlinkRate,
				// "TextField.border", textFieldBorder,
				"TextField.margin",
				zeroInsets,

				"FormattedTextField.font",
				sansSerifPlain12,
				"FormattedTextField.background",
				window,
				"FormattedTextField.foreground",
				textText,
				"FormattedTextField.inactiveForeground", disabledTextColor,
				"FormattedTextField.inactiveBackground", control,
				// "FormattedTextField.selectionBackground", textHighlight,
				// "FormattedTextField.selectionForeground", textHighlightText,
				"FormattedTextField.caretForeground",
				textText,
				// "FormattedTextField.caretBlinkRate", caretBlinkRate,
				// "FormattedTextField.border", textFieldBorder,
				"FormattedTextField.margin",
				zeroInsets,
				// "FormattedTextField.focusInputMap",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "ctrl C", DefaultEditorKit.copyAction,
				// "ctrl V", DefaultEditorKit.pasteAction,
				// "ctrl X", DefaultEditorKit.cutAction,
				// "COPY", DefaultEditorKit.copyAction,
				// "PASTE", DefaultEditorKit.pasteAction,
				// "CUT", DefaultEditorKit.cutAction,
				// "control INSERT", DefaultEditorKit.copyAction,
				// "shift INSERT", DefaultEditorKit.pasteAction,
				// "shift DELETE", DefaultEditorKit.cutAction,
				// "shift LEFT", DefaultEditorKit.selectionBackwardAction,
				// "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
				// "shift RIGHT", DefaultEditorKit.selectionForwardAction,
				// "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,
				// "ctrl LEFT", DefaultEditorKit.previousWordAction,
				// "ctrl KP_LEFT", DefaultEditorKit.previousWordAction,
				// "ctrl RIGHT", DefaultEditorKit.nextWordAction,
				// "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction,
				// "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
				// "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction,
				// "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction,
				// "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction,
				// "ctrl A", DefaultEditorKit.selectAllAction,
				// "HOME", DefaultEditorKit.beginLineAction,
				// "END", DefaultEditorKit.endLineAction,
				// "shift HOME", DefaultEditorKit.selectionBeginLineAction,
				// "shift END", DefaultEditorKit.selectionEndLineAction,
				// "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
				// "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
				// "ctrl H", DefaultEditorKit.deletePrevCharAction,
				// "DELETE", DefaultEditorKit.deleteNextCharAction,
				// "ctrl DELETE", DefaultEditorKit.deleteNextWordAction,
				// "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction,
				// "RIGHT", DefaultEditorKit.forwardAction,
				// "LEFT", DefaultEditorKit.backwardAction,
				// "KP_RIGHT", DefaultEditorKit.forwardAction,
				// "KP_LEFT", DefaultEditorKit.backwardAction,
				// "ENTER", JTextField.notifyAction,
				// "ctrl BACK_SLASH", "unselect",
				// "control shift O", "toggle-componentOrientation",
				// "ESCAPE", "reset-field-edit",
				// "UP", "increment",
				// "KP_UP", "increment",
				// "DOWN", "decrement",
				// "KP_DOWN", "decrement",
				// }),
				//
				"PasswordField.font",
				monospacedPlain12,
				"PasswordField.background",
				window,
				"PasswordField.foreground",
				textText,
				"PasswordField.inactiveForeground", disabledTextColor,
				"PasswordField.inactiveBackground", control,
				// "PasswordField.selectionBackground", textHighlight,
				// "PasswordField.selectionForeground", textHighlightText,
				// "PasswordField.caretForeground", textText,
				// "PasswordField.caretBlinkRate", caretBlinkRate,
				// "PasswordField.border", textFieldBorder,
				"PasswordField.margin",
				zeroInsets,
				"PasswordField.echoChar",
				'*',

				"TextArea.font",
				sansSerifPlain12,
				"TextArea.background",
				window,
				"TextArea.foreground",
				textText,
				"TextArea.inactiveForeground", disabledTextColor,
				// "TextArea.selectionBackground", textHighlight,
				// "TextArea.selectionForeground", textHighlightText,
				// "TextArea.caretForeground", textText,
				// "TextArea.caretBlinkRate", caretBlinkRate,
				// "TextArea.border", marginBorder,
				"TextArea.margin",
				zeroInsets,

				"TextPane.font",
				serifPlain12,
				"TextPane.background",
				white,
				"TextPane.foreground",
				textText,
				// "TextPane.selectionBackground", textHighlight,
				// "TextPane.selectionForeground", textHighlightText,
				// "TextPane.caretForeground", textText,
				// "TextPane.caretBlinkRate", caretBlinkRate,
				"TextPane.inactiveForeground", disabledTextColor,
				// "TextPane.border", marginBorder,
				"TextPane.margin",
				editorMargin,
	            "TextPane.focusInputMap", multilineInputMap,

				"EditorPane.font",
				serifPlain12,
				"EditorPane.background",
				white,
				"EditorPane.foreground",
				textText,
				// "EditorPane.selectionBackground", textHighlight,
				// "EditorPane.selectionForeground", textHighlightText,
				// "EditorPane.caretForeground", textText,
				// "EditorPane.caretBlinkRate", caretBlinkRate,
				"EditorPane.inactiveForeground", disabledTextColor,
				// "EditorPane.border", marginBorder,
				"EditorPane.margin",
				editorMargin,
	            "EditorPane.focusInputMap", multilineInputMap,
				//
				// "html.pendingImage", SwingUtilities2.makeIcon(getClass(),
				// HTML5LookAndFeel.class,
				// "icons/image-delayed.png"),
				// "html.missingImage", SwingUtilities2.makeIcon(getClass(),
				// HTML5LookAndFeel.class,
				// "icons/image-failed.png"),
				// *** TitledBorder
				// "TitledBorder.font", dialogPlain12,
				"TitledBorder.titleColor",
				controlText,
			  "TitledBorder.border", etchedBorder,

				// *** ToolBar
				"ToolBar.font", dialogPlain12,
				"ToolBar.background", control,
				"ToolBar.foreground", controlText,
				"ToolBar.shadow", controlShadow,
				"ToolBar.darkShadow", controlDkShadow,
				"ToolBar.light", controlHighlight,
				"ToolBar.highlight", controlLtHighlight,
				"ToolBar.dockingBackground", control,
				"ToolBar.dockingForeground", red,
				"ToolBar.floatingBackground", control,
				"ToolBar.floatingForeground", darkGray,
				"ToolBar.border", etchedBorder,
				"ToolBar.separatorSize", toolBarSeparatorSize,
				// "ToolBar.ancestorInputMap",
				// new UIDefaults.LazyInputMap(new Object[] {
				// "UP", "navigateUp",
				// "KP_UP", "navigateUp",
				// "DOWN", "navigateDown",
				// "KP_DOWN", "navigateDown",
				// "LEFT", "navigateLeft",
				// "KP_LEFT", "navigateLeft",
				// "RIGHT", "navigateRight",
				// "KP_RIGHT", "navigateRight"
				// }),

				// *** ToolTips
				 "ToolTip.font", sansSerifPlain12,
				 "ToolTip.background", table.get("info"),
				 "ToolTip.foreground", table.get("infoText"),
				 "ToolTip.border", blackLineBorder,
				// ToolTips also support backgroundInactive, borderInactive,
				// and foregroundInactive

				// *** ToolTipManager
				// ToolTipManager.enableToolTipMode currently supports:
				// "allWindows" (default):
				// enables tool tips for all windows of all java applications,
				// whether the windows are active or inactive
				// "activeApplication"
				// enables tool tips for windows of an application only when
				// the application has an active window
				// "ToolTipManager.enableToolTipMode", "allWindows",

				// *** Tree
				"Tree.paintLines",
				Boolean.TRUE,
				"Tree.lineTypeDashed",
				Boolean.FALSE,
				// "Tree.font", dialogPlain12,
				"Tree.background",
				window,
				"Tree.foreground",
				textText,
				"Tree.hash",
				gray,
				"Tree.textForeground",
				textText,
				"Tree.textBackground",
				table.get("text"),
				"Tree.selectionForeground", textHighlightText,
				"Tree.selectionBackground", textHighlight,
				// "Tree.selectionBorderColor", black,
				// "Tree.dropLineColor", controlShadow,
				// "Tree.editorBorder", blackLineBorder,
				"Tree.leftChildIndent", new Integer(7), "Tree.rightChildIndent",
				new Integer(13), "Tree.rowHeight", new Integer(16),
				"Tree.scrollsOnExpand", Boolean.TRUE,
				// "Tree.openIcon", SwingUtilities2.makeIcon(getClass(),
				// HTML5LookAndFeel.class,
				// "icons/TreeOpen.gif"),
				// "Tree.closedIcon", SwingUtilities2.makeIcon(getClass(),
				// HTML5LookAndFeel.class,
				// "icons/TreeClosed.gif"),
				// "Tree.leafIcon", SwingUtilities2.makeIcon(getClass(),
				// HTML5LookAndFeel.class,
				// "icons/TreeLeaf.gif"),
				// "Tree.expandedIcon", null,
				// "Tree.collapsedIcon", null,
				// "Tree.changeSelectionWithFocus", Boolean.TRUE,
				// "Tree.drawsFocusBorderAroundIcon", Boolean.FALSE,
				"Tree.timeFactor", oneThousand,
		// "Tree.focusInputMap",
		// new UIDefaults.LazyInputMap(new Object[] {
		// "ctrl C", "copy",
		// "ctrl V", "paste",
		// "ctrl X", "cut",
		// "COPY", "copy",
		// "PASTE", "paste",
		// "CUT", "cut",
		// "control INSERT", "copy",
		// "shift INSERT", "paste",
		// "shift DELETE", "cut",
		// "UP", "selectPrevious",
		// "KP_UP", "selectPrevious",
		// "shift UP", "selectPreviousExtendSelection",
		// "shift KP_UP", "selectPreviousExtendSelection",
		// "ctrl shift UP", "selectPreviousExtendSelection",
		// "ctrl shift KP_UP", "selectPreviousExtendSelection",
		// "ctrl UP", "selectPreviousChangeLead",
		// "ctrl KP_UP", "selectPreviousChangeLead",
		// "DOWN", "selectNext",
		// "KP_DOWN", "selectNext",
		// "shift DOWN", "selectNextExtendSelection",
		// "shift KP_DOWN", "selectNextExtendSelection",
		// "ctrl shift DOWN", "selectNextExtendSelection",
		// "ctrl shift KP_DOWN", "selectNextExtendSelection",
		// "ctrl DOWN", "selectNextChangeLead",
		// "ctrl KP_DOWN", "selectNextChangeLead",
		// "RIGHT", "selectChild",
		// "KP_RIGHT", "selectChild",
		// "LEFT", "selectParent",
		// "KP_LEFT", "selectParent",
		// "PAGE_UP", "scrollUpChangeSelection",
		// "shift PAGE_UP", "scrollUpExtendSelection",
		// "ctrl shift PAGE_UP", "scrollUpExtendSelection",
		// "ctrl PAGE_UP", "scrollUpChangeLead",
		// "PAGE_DOWN", "scrollDownChangeSelection",
		// "shift PAGE_DOWN", "scrollDownExtendSelection",
		// "ctrl shift PAGE_DOWN", "scrollDownExtendSelection",
		// "ctrl PAGE_DOWN", "scrollDownChangeLead",
		// "HOME", "selectFirst",
		// "shift HOME", "selectFirstExtendSelection",
		// "ctrl shift HOME", "selectFirstExtendSelection",
		// "ctrl HOME", "selectFirstChangeLead",
		// "END", "selectLast",
		// "shift END", "selectLastExtendSelection",
		// "ctrl shift END", "selectLastExtendSelection",
		// "ctrl END", "selectLastChangeLead",
		// "F2", "startEditing",
		// "ctrl A", "selectAll",
		// "ctrl SLASH", "selectAll",
		// "ctrl BACK_SLASH", "clearSelection",
		// "ctrl LEFT", "scrollLeft",
		// "ctrl KP_LEFT", "scrollLeft",
		// "ctrl RIGHT", "scrollRight",
		// "ctrl KP_RIGHT", "scrollRight",
		// "SPACE", "addToSelection",
		// "ctrl SPACE", "toggleAndAnchor",
		// "shift SPACE", "extendTo",
		// "ctrl shift SPACE", "moveSelectionTo"
		// }),
		 "Tree.focusInputMap.RightToLeft",
		 new UIDefaults.LazyInputMap(new Object[] {
		 "RIGHT", "selectParent",
		 "KP_RIGHT", "selectParent",
		 "LEFT", "selectChild",
		 "KP_LEFT", "selectChild",
		 }),
		 "Tree.ancestorInputMap",
		 new UIDefaults.LazyInputMap(new Object[] {
		 "ESCAPE", "cancel"
		 }),
		// // Bind specific keys that can invoke popup on currently
		// // focused JComponent
		// "RootPane.ancestorInputMap",
		// new UIDefaults.LazyInputMap(new Object[] {
		// "shift F10", "postPopup",
		// "CONTEXT_MENU", "postPopup"
		// }),
		//
		// // These bindings are only enabled when there is a default
		// // button set on the rootpane.
		// "RootPane.defaultButtonWindowKeyBindings", new Object[] {
		// "ENTER", "press",
		// "released ENTER", "release",
		// "ctrl ENTER", "press",
		// "ctrl released ENTER", "release"
		// },
		};

		table.putDefaults(defaults);
	}

	// /**
	// * Returns the ui that is of type <code>klass</code>, or null if
	// * one can not be found.
	// */
	// static Object getUIOfType(ComponentUI ui, Class klass) {
	// if (klass.isInstance(ui)) {
	// return ui;
	// }
	// return null;
	// }
	//
	// // ********* Auditory Cue support methods and objects *********
	// // also see the "AuditoryCues" section of the defaults table
	//
	// /**
	// * Returns an <code>ActionMap</code> containing the audio actions
	// * for this look and feel.
	// * <P>
	// * The returned <code>ActionMap</code> contains <code>Actions</code> that
	// * embody the ability to render an auditory cue. These auditory
	// * cues map onto user and system activities that may be useful
	// * for an end user to know about (such as a dialog box appearing).
	// * <P>
	// * At the appropriate time,
	// * the {@code ComponentUI} is responsible for obtaining an
	// * <code>Action</code> out of the <code>ActionMap</code> and passing
	// * it to <code>playSound</code>.
	// * <P>
	// * This method first looks up the {@code ActionMap} from the
	// * defaults using the key {@code "AuditoryCues.actionMap"}.
	// * <p>
	// * If the value is {@code non-null}, it is returned. If the value
	// * of the default {@code "AuditoryCues.actionMap"} is {@code null}
	// * and the value of the default {@code "AuditoryCues.cueList"} is
	// * {@code non-null}, an {@code ActionMapUIResource} is created and
	// * populated. Population is done by iterating over each of the
	// * elements of the {@code "AuditoryCues.cueList"} array, and
	// * invoking {@code createAudioAction()} to create an {@code
	// * Action} for each element. The resulting {@code Action} is
	// * placed in the {@code ActionMapUIResource}, using the array
	// * element as the key. For example, if the {@code
	// * "AuditoryCues.cueList"} array contains a single-element, {@code
	// * "audioKey"}, the {@code ActionMapUIResource} is created, then
	// * populated by way of {@code actionMap.put(cueList[0],
	// * createAudioAction(cueList[0]))}.
	// * <p>
	// * If the value of the default {@code "AuditoryCues.actionMap"} is
	// * {@code null} and the value of the default
	// * {@code "AuditoryCues.cueList"} is {@code null}, an empty
	// * {@code ActionMapUIResource} is created.
	// *
	// *
	// * @return an ActionMap containing {@code Actions}
	// * responsible for playing auditory cues
	// * @throws ClassCastException if the value of the
	// * default {@code "AuditoryCues.actionMap"} is not an
	// * {@code ActionMap}, or the value of the default
	// * {@code "AuditoryCues.cueList"} is not an {@code Object[]}
	// * @see #createAudioAction
	// * @see #playSound(Action)
	// * @since 1.4
	// */
	// protected ActionMap getAudioActionMap() {
	// ActionMap audioActionMap = (ActionMap)UIManager.get(
	// "AuditoryCues.actionMap");
	// if (audioActionMap == null) {
	// Object[] acList = (Object[])UIManager.get("AuditoryCues.cueList");
	// if (acList != null) {
	// audioActionMap = new ActionMapUIResource();
	// for(int counter = acList.length-1; counter >= 0; counter--) {
	// audioActionMap.put(acList[counter],
	// createAudioAction(acList[counter]));
	// }
	// }
	// UIManager.getLookAndFeelDefaults().put("AuditoryCues.actionMap",
	// audioActionMap);
	// }
	// return audioActionMap;
	// }
	//
	// /**
	// * Creates and returns an {@code Action} used to play a sound.
	// * <p>
	// * If {@code key} is {@code non-null}, an {@code Action} is created
	// * using the value from the defaults with key {@code key}. The value
	// * identifies the sound resource to load when
	// * {@code actionPerformed} is invoked on the {@code Action}. The
	// * sound resource is loaded into a {@code byte[]} by way of
	// * {@code getClass().getResourceAsStream()}.
	// *
	// * @param key the key identifying the audio action
	// * @return an {@code Action} used to play the source, or {@code null}
	// * if {@code key} is {@code null}
	// * @see #playSound(Action)
	// * @since 1.4
	// */
	// protected Action createAudioAction(Object key) {
	// if (key != null) {
	// String audioKey = (String)key;
	// String audioValue = (String)UIManager.get(key);
	// return new AudioAction(audioKey, audioValue);
	// } else {
	// return null;
	// }
	// }
	//
	// /**
	// * Pass the name String to the super constructor. This is used
	// * later to identify the Action and decide whether to play it or
	// * not. Store the resource String. I is used to get the audio
	// * resource. In this case, the resource is an audio file.
	// *
	// * @since 1.4
	// */
	// private class AudioAction extends AbstractAction implements LineListener {
	// // We strive to only play one sound at a time (other platforms
	// // appear to do this). This is done by maintaining the field
	// // clipPlaying. Every time a sound is to be played,
	// // cancelCurrentSound is invoked to cancel any sound that may be
	// // playing.
	// private String audioResource;
	// private byte[] audioBuffer;
	//
	// /**
	// * The String is the name of the Action and
	// * points to the audio resource.
	// * The byte[] is a buffer of the audio bits.
	// */
	// public AudioAction(String name, String resource) {
	// super(name);
	// audioResource = resource;
	// }
	//
	// public void actionPerformed(ActionEvent e) {
	// if (audioBuffer == null) {
	// audioBuffer = loadAudioData(audioResource);
	// }
	// if (audioBuffer != null) {
	// cancelCurrentSound(null);
	// try {
	// AudioInputStream soundStream =
	// AudioSystem.getAudioInputStream(
	// new ByteArrayInputStream(audioBuffer));
	// DataLine.Info info =
	// new DataLine.Info(Clip.class, soundStream.getFormat());
	// Clip clip = (Clip) AudioSystem.getLine(info);
	// clip.open(soundStream);
	// clip.addLineListener(this);
	//
	// synchronized(audioLock) {
	// clipPlaying = clip;
	// }
	//
	// clip.start();
	// } catch (Exception ex) {}
	// }
	// }
	//
	// public void update(LineEvent event) {
	// if (event.getType() == LineEvent.Type.STOP) {
	// cancelCurrentSound((Clip)event.getLine());
	// }
	// }
	//
	// /**
	// * If the parameter is null, or equal to the currently
	// * playing sound, then cancel the currently playing sound.
	// */
	// private void cancelCurrentSound(Clip clip) {
	// Clip lastClip = null;
	//
	// synchronized(audioLock) {
	// if (clip == null || clip == clipPlaying) {
	// lastClip = clipPlaying;
	// clipPlaying = null;
	// }
	// }
	//
	// if (lastClip != null) {
	// lastClip.removeLineListener(this);
	// lastClip.close();
	// }
	// }
	// }
	//
	// /**
	// * Utility method that loads audio bits for the specified
	// * <code>soundFile</code> filename. If this method is unable to
	// * build a viable path name from the <code>baseClass</code> and
	// * <code>soundFile</code> passed into this method, it will
	// * return <code>null</code>.
	// *
	// * @param baseClass used as the root class/location to get the
	// * soundFile from
	// * @param soundFile the name of the audio file to be retrieved
	// * from disk
	// * @return A byte[] with audio data or null
	// * @since 1.4
	// */
	// private byte[] loadAudioData(final String soundFile){
	// if (soundFile == null) {
	// return null;
	// }
	// /* Copy resource into a byte array. This is
	// * necessary because several browsers consider
	// * Class.getResource a security risk since it
	// * can be used to load additional classes.
	// * Class.getResourceAsStream just returns raw
	// * bytes, which we can convert to a sound.
	// */
	// byte[] buffer = (byte[])AccessController.doPrivileged(
	// new PrivilegedAction() {
	// public Object run() {
	// try {
	// InputStream resource = HTML5LookAndFeel.this.
	// getClass().getResourceAsStream(soundFile);
	// if (resource == null) {
	// return null;
	// }
	// BufferedInputStream in =
	// new BufferedInputStream(resource);
	// ByteArrayOutputStream out =
	// new ByteArrayOutputStream(1024);
	// byte[] buffer = new byte[1024];
	// int n;
	// while ((n = in.read(buffer)) > 0) {
	// out.write(buffer, 0, n);
	// }
	// in.close();
	// out.flush();
	// buffer = out.toByteArray();
	// return buffer;
	// } catch (IOException ioe) {
	// System.err.println(ioe.toString());
	// return null;
	// }
	// }
	// });
	// if (buffer == null) {
	// System.err.println(getClass().getName() + "/" +
	// soundFile + " not found.");
	// return null;
	// }
	// if (buffer.length == 0) {
	// System.err.println("warning: " + soundFile +
	// " is zero-length");
	// return null;
	// }
	// return buffer;
	// }
	//
	// /**
	// * If necessary, invokes {@code actionPerformed} on
	// * {@code audioAction} to play a sound.
	// * The {@code actionPerformed} method is invoked if the value of
	// * the {@code "AuditoryCues.playList"} default is a {@code
	// * non-null} {@code Object[]} containing a {@code String} entry
	// * equal to the name of the {@code audioAction}.
	// *
	// * @param audioAction an Action that knows how to render the audio
	// * associated with the system or user activity
	// * that is occurring; a value of {@code null}, is
	// * ignored
	// * @throws ClassCastException if {@code audioAction} is {@code non-null}
	// * and the value of the default {@code "AuditoryCues.playList"}
	// * is not an {@code Object[]}
	// * @since 1.4
	// */
	// protected void playSound(Action audioAction) {
	// if (audioAction != null) {
	// Object[] audioStrings = (Object[])
	// UIManager.get("AuditoryCues.playList");
	// if (audioStrings != null) {
	// // create a HashSet to help us decide to play or not
	// HashSet audioCues = new HashSet();
	// for (int i = 0; i < audioStrings.length; i++) {
	// audioCues.add(audioStrings[i]);
	// }
	// // get the name of the Action
	// String actionName = (String)audioAction.getValue(Action.NAME);
	// // if the actionName is in the audioCues HashSet, play it.
	// if (audioCues.contains(actionName)) {
	// audioAction.actionPerformed(new
	// ActionEvent(this, ActionEvent.ACTION_PERFORMED,
	// actionName));
	// }
	// }
	// }
	// }
	//
	//
	// /**
	// * Sets the parent of the passed in ActionMap to be the audio action
	// * map.
	// */
	// static void installAudioActionMap(ActionMap map) {
	// LookAndFeel laf = UIManager.getLookAndFeel();
	// if (laf instanceof HTML5LookAndFeel) {
	// map.setParent(((HTML5LookAndFeel)laf).getAudioActionMap());
	// }
	// }
	//
	//
	// /**
	// * Helper method to play a named sound.
	// *
	// * @param c JComponent to play the sound for.
	// * @param actionKey Key for the sound.
	// */
	// static void playSound(JComponent c, Object actionKey) {
	// LookAndFeel laf = UIManager.getLookAndFeel();
	// if (laf instanceof HTML5LookAndFeel) {
	// ActionMap map = c.getActionMap();
	// if (map != null) {
	// Action audioAction = map.get(actionKey);
	// if (audioAction != null) {
	// // pass off firing the Action to a utility method
	// ((BasicLookAndFeel)laf).playSound(audioAction);
	// }
	// }
	// }
	// }
	//
	// /**
	// * This class contains listener that watches for all the mouse
	// * events that can possibly invoke popup on the component
	// */
	// class AWTEventHelper implements AWTEventListener,PrivilegedAction {
	// AWTEventHelper() {
	// super();
	// AccessController.doPrivileged(this);
	// }
	//
	// public Object run() {
	// Toolkit tk = Toolkit.getDefaultToolkit();
	// if(invocator == null) {
	// tk.addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK);
	// } else {
	// tk.removeAWTEventListener(invocator);
	// }
	// // Return value not used.
	// return null;
	// }
	//
	// public void eventDispatched(AWTEvent ev) {
	// int eventID = ev.getID();
	// if((eventID & AWTEvent.MOUSE_EVENT_MASK) != 0) {
	// MouseEvent me = (MouseEvent) ev;
	// if(me.isPopupTrigger()) {
	// MenuElement[] elems = MenuSelectionManager
	// .defaultManager()
	// .getSelectedPath();
	// if(elems != null && elems.length != 0) {
	// return;
	// // We shall not interfere with already opened menu
	// }
	// Object c = me.getSource();
	// JComponent src = null;
	// if(c instanceof JComponent) {
	// src = (JComponent) jc;
	// } else if(c instanceof BasicSplitPaneDivider) {
	// // Special case - if user clicks on divider we must
	// // invoke popup from the SplitPane
	// src = (JComponent)
	// ((BasicSplitPaneDivider)c).getParent();
	// }
	// if(src != null) {
	// if(src.getComponentPopupMenu() != null) {
	// Point pt = src.getPopupLocation(me);
	// if(pt == null) {
	// pt = me.getPoint();
	// pt = SwingUtilities.convertPoint((Component)c,
	// pt, src);
	// }
	// src.getComponentPopupMenu().show(src, pt.x, pt.y);
	// me.consume();
	// }
	// }
	// }
	// }
	// /* Activate a JInternalFrame if necessary. */
	// if (eventID == MouseEvent.MOUSE_PRESSED) {
	// Object object = ev.getSource();
	// if (!(object instanceof Component)) {
	// return;
	// }
	// Component component = (Component)object;
	// if (component != null) {
	// Component parent = component;
	// while (parent != null && !(parent instanceof Window)) {
	// if (parent instanceof JInternalFrame) {
	// // Activate the frame.
	// try { ((JInternalFrame)parent).setSelected(true); }
	// catch (PropertyVetoException e1) { }
	// }
	// parent = parent.getParent();
	// }
	// }
	// }
	// }
	// }
	//
	@Override
	public String getName() {
		return "SwingJS";
	}

	@Override
	public String getID() {
		return "SwingJS";
	}

	@Override
	public String getDescription() {
		return "SwingJS L&F";
	}

	@Override
	public boolean isNativeLookAndFeel() {
		return true;
	}

	@Override
	public boolean isSupportedLookAndFeel() {
		return true;
	}
}
