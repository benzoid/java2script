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

package java.awt.image;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.util.Hashtable;
import java.util.Vector;

import sun.awt.image.OffScreenImageSource;
import swingjs.JSGraphics2D;
import swingjs.api.js.DOMNode;
import swingjs.api.js.HTML5Canvas;

/**
 * 
 * The <code>BufferedImage</code> subclass describes an {@link java.awt.Image
 * Image} with an accessible buffer of image data. A <code>BufferedImage</code>
 * is comprised of a {@link ColorModel} and a {@link Raster} of image data. The
 * number and types of bands in the {@link SampleModel} of the
 * <code>Raster</code> must match the number and types required by the
 * <code>ColorModel</code> to represent its color and alpha components. All
 * <code>BufferedImage</code> objects have an upper left corner coordinate of
 * (0,&nbsp;0). Any <code>Raster</code> used to construct a
 * <code>BufferedImage</code> must therefore have minX=0 and minY=0.
 * 
 * <p>
 * This class relies on the data fetching and setting methods of
 * <code>Raster</code>, and on the color characterization methods of
 * <code>ColorModel</code>.
 * 
 * @see ColorModel
 * @see Raster
 * @see WritableRaster
 */

public class BufferedImage extends Image implements RenderedImage, Transparency // , WritableRenderedImage
{
	int imageType = TYPE_CUSTOM;
	ColorModel colorModel; 
	protected WritableRaster raster;
	OffScreenImageSource osis;
	@SuppressWarnings("rawtypes")
	Hashtable properties;

	boolean isAlphaPremultiplied;// If true, alpha has been premultiplied in
	// color channels
	protected int[] 秘pix;
	protected Object 秘imgNode; // used by JSGraphics2D directly
	protected int width, height;
	private boolean 秘havePix;
	protected Object 秘canvas; // created in setRGB
	public Component 秘component; // for context from component.createImage()
	private int[] 秘pixSaved;
    JSGraphics2D 秘g; // a JSGraphics2D instance
	//private static int rangeIndex;

	/**
	 * Image Type Constants
	 */

	/**
	 * Image type is not recognized so it must be a customized image. This type is
	 * only used as a return value for the getType() method.
	 */
	public static final int TYPE_CUSTOM = 0;

	/**
	 * Represents an image with 8-bit RGB color components packed into integer
	 * pixels. The image has a {@link DirectColorModel} without alpha. When data
	 * with non-opaque alpha is stored in an image of this type, the color data
	 * must be adjusted to a non-premultiplied form and the alpha discarded, as
	 * described in the {@link java.awt.AlphaComposite} documentation.
	 */
	public static final int TYPE_INT_RGB = 1;

	/**
	 * Represents an image with 8-bit RGBA color components packed into integer
	 * pixels. The image has a <code>DirectColorModel</code> with alpha. The color
	 * data in this image is considered not to be premultiplied with alpha. When
	 * this type is used as the <code>imageType</code> argument to a
	 * <code>BufferedImage</code> constructor, the created image is consistent
	 * with images created in the JDK1.1 and earlier releases.
	 */
	public static final int TYPE_INT_ARGB = 2;

	 /**
	 * Represents an image with 8-bit RGBA color components packed into
	 * integer pixels. The image has a <code>DirectColorModel</code>
	 * with alpha. The color data in this image is considered to be
	 * premultiplied with alpha.
	 */
	 public static final int TYPE_INT_ARGB_PRE = 3;
	//
	 /**
	 * Represents an image with 8-bit RGB color components, corresponding
	 * to a Windows- or Solaris- style BGR color model, with the colors
	 * Blue, Green, and Red packed into integer pixels. There is no alpha.
	 * The image has a {@link DirectColorModel}.
	 * When data with non-opaque alpha is stored
	 * in an image of this type,
	 * the color data must be adjusted to a non-premultiplied form
	 * and the alpha discarded,
	 * as described in the
	 * {@link java.awt.AlphaComposite} documentation.
	 */
	 public static final int TYPE_INT_BGR = 4;
	
	 /**
	 * Represents an image with 8-bit RGB color components, corresponding
	 * to a Windows-style BGR color model) with the colors Blue, Green,
	 * and Red stored in 3 bytes. There is no alpha. The image has a
	 * <code>ComponentColorModel</code>.
	 * When data with non-opaque alpha is stored
	 * in an image of this type,
	 * the color data must be adjusted to a non-premultiplied form
	 * and the alpha discarded,
	 * as described in the
	 * {@link java.awt.AlphaComposite} documentation.
	 */
	 public static final int TYPE_3BYTE_BGR = 5;
	
	 /**
	 * Represents an image with 8-bit RGBA color components with the colors
	 * Blue, Green, and Red stored in 3 bytes and 1 byte of alpha. The
	 * image has a <code>ComponentColorModel</code> with alpha. The
	 * color data in this image is considered not to be premultiplied with
	 * alpha. The byte data is interleaved in a single
	 * byte array in the order A, B, G, R
	 * from lower to higher byte addresses within each pixel.
	 */
	 public static final int TYPE_4BYTE_ABGR = 6;
	
	 /**
	 * Represents an image with 8-bit RGBA color components with the colors
	 * Blue, Green, and Red stored in 3 bytes and 1 byte of alpha. The
	 * image has a <code>ComponentColorModel</code> with alpha. The color
	 * data in this image is considered to be premultiplied with alpha.
	 * The byte data is interleaved in a single byte array in the order
	 * A, B, G, R from lower to higher byte addresses within each pixel.
	 */
	 public static final int TYPE_4BYTE_ABGR_PRE = 7;
	
	 /**
	 * Represents an image with 5-6-5 RGB color components (5-bits red,
	 * 6-bits green, 5-bits blue) with no alpha. This image has
	 * a <code>DirectColorModel</code>.
	 * When data with non-opaque alpha is stored
	 * in an image of this type,
	 * the color data must be adjusted to a non-premultiplied form
	 * and the alpha discarded,
	 * as described in the
	 * {@link java.awt.AlphaComposite} documentation.
	 */
	 public static final int TYPE_USHORT_565_RGB = 8;
	
	 /**
	 * Represents an image with 5-5-5 RGB color components (5-bits red,
	 * 5-bits green, 5-bits blue) with no alpha. This image has
	 * a <code>DirectColorModel</code>.
	 * When data with non-opaque alpha is stored
	 * in an image of this type,
	 * the color data must be adjusted to a non-premultiplied form
	 * and the alpha discarded,
	 * as described in the
	 * {@link java.awt.AlphaComposite} documentation.
	 */
	 public static final int TYPE_USHORT_555_RGB = 9;
	
	 /**
	 * Represents a unsigned byte grayscale image, non-indexed. This
	 * image has a <code>ComponentColorModel</code> with a CS_GRAY
	 * {@link ColorSpace}.
	 * When data with non-opaque alpha is stored
	 * in an image of this type,
	 * the color data must be adjusted to a non-premultiplied form
	 * and the alpha discarded,
	 * as described in the
	 * {@link java.awt.AlphaComposite} documentation.
	 */
	 public static final int TYPE_BYTE_GRAY = 10;
	
	 /**
	 * Represents an unsigned short grayscale image, non-indexed). This
	 * image has a <code>ComponentColorModel</code> with a CS_GRAY
	 * <code>ColorSpace</code>.
	 * When data with non-opaque alpha is stored
	 * in an image of this type,
	 * the color data must be adjusted to a non-premultiplied form
	 * and the alpha discarded,
	 * as described in the
	 * {@link java.awt.AlphaComposite} documentation.
	 */
	 public static final int TYPE_USHORT_GRAY = 11;
	
	 /**
	 * Represents an opaque byte-packed 1, 2, or 4 bit image. The
	 * image has an {@link IndexColorModel} without alpha. When this
	 * type is used as the <code>imageType</code> argument to the
	 * <code>BufferedImage</code> constructor that takes an
	 * <code>imageType</code> argument but no <code>ColorModel</code>
	 * argument, a 1-bit image is created with an
	 * <code>IndexColorModel</code> with two colors in the default
	 * sRGB <code>ColorSpace</code>: {0,&nbsp;0,&nbsp;0} and
	 * {255,&nbsp;255,&nbsp;255}.
	 *
	 * <p> Images with 2 or 4 bits per pixel may be constructed via
	 * the <code>BufferedImage</code> constructor that takes a
	 * <code>ColorModel</code> argument by supplying a
	 * <code>ColorModel</code> with an appropriate map size.
	 *
	 * <p> Images with 8 bits per pixel should use the image types
	 * <code>TYPE_BYTE_INDEXED</code> or <code>TYPE_BYTE_GRAY</code>
	 * depending on their <code>ColorModel</code>.
	
	 * <p> When color data is stored in an image of this type,
	 * the closest color in the colormap is determined
	 * by the <code>IndexColorModel</code> and the resulting index is stored.
	 * Approximation and loss of alpha or color components
	 * can result, depending on the colors in the
	 * <code>IndexColorModel</code> colormap.
	 */
	 public static final int TYPE_BYTE_BINARY = 12;
	
	 /**
	 * Represents an indexed byte image. When this type is used as the
	 * <code>imageType</code> argument to the <code>BufferedImage</code>
	 * constructor that takes an <code>imageType</code> argument
	 * but no <code>ColorModel</code> argument, an
	 * <code>IndexColorModel</code> is created with
	 * a 256-color 6/6/6 color cube palette with the rest of the colors
	 * from 216-255 populated by grayscale values in the
	 * default sRGB ColorSpace.
	 *
	 * <p> When color data is stored in an image of this type,
	 * the closest color in the colormap is determined
	 * by the <code>IndexColorModel</code> and the resulting index is stored.
	 * Approximation and loss of alpha or color components
	 * can result, depending on the colors in the
	 * <code>IndexColorModel</code> colormap.
	 */
	 public static final int TYPE_BYTE_INDEXED = 13;
	
	// private static final int DCM_RED_MASK = 0x00ff0000;
	// private static final int DCM_GREEN_MASK = 0x0000ff00;
	// private static final int DCM_BLUE_MASK = 0x000000ff;
	// private static final int DCM_ALPHA_MASK = 0xff000000;
	// private static final int DCM_565_RED_MASK = 0xf800;
	// private static final int DCM_565_GRN_MASK = 0x07E0;
	// private static final int DCM_565_BLU_MASK = 0x001F;
	// private static final int DCM_555_RED_MASK = 0x7C00;
	// private static final int DCM_555_GRN_MASK = 0x03E0;
	// private static final int DCM_555_BLU_MASK = 0x001F;
	// private static final int DCM_BGR_RED_MASK = 0x0000ff;
	// private static final int DCM_BGR_GRN_MASK = 0x00ff00;
	// private static final int DCM_BGR_BLU_MASK = 0xff0000;

	/**
	 * Constructs a <code>BufferedImage</code> of one of the predefined image
	 * types. The <code>ColorSpace</code> for the image is the default sRGB space.
	 * 
	 * @param width
	 *          width of the created image
	 * @param height
	 *          height of the created image
	 * @param imageType
	 *          type of the created image
	 * @see ColorSpace
	 * @see #TYPE_INT_RGB
	 * @see #TYPE_INT_ARGB
	 * @see #TYPE_INT_ARGB_PRE
	 * @see #TYPE_INT_BGR
	 * @see #TYPE_3BYTE_BGR
	 * @see #TYPE_4BYTE_ABGR
	 * @see #TYPE_4BYTE_ABGR_PRE
	 * @see #TYPE_BYTE_GRAY
	 * @see #TYPE_USHORT_GRAY
	 * @see #TYPE_BYTE_BINARY
	 * @see #TYPE_BYTE_INDEXED
	 * @see #TYPE_USHORT_565_RGB
	 * @see #TYPE_USHORT_555_RGB
	 */
	public BufferedImage(int width, int height, int imageType) {
		this.width = width;
		this.height = height;
		switch (imageType) {
		case TYPE_INT_RGB: {
			colorModel = new DirectColorModel(24, 0x00ff0000, // Red
					0x0000ff00, // Green
					0x000000ff, // Blue
					0x0 // Alpha
			);
			raster = colorModel.createCompatibleWritableRaster(width, height);
			raster.setImage(this);
			秘pix = ((DataBufferInt) raster.getDataBuffer()).data;
		}
			break;

		case TYPE_INT_ARGB_PRE:
		case TYPE_INT_ARGB: {
			colorModel = ColorModel.getRGBdefault();
			raster = colorModel.createCompatibleWritableRaster(width, height);
			raster.setImage(this);
			秘pix = ((DataBufferInt) raster.getDataBuffer()).data;
		}
			break;

		// case TYPE_INT_ARGB_PRE:
		// {
		// colorModel = new
		// DirectColorModel(
		// ColorSpace.getInstance(ColorSpace.CS_sRGB),
		// 32,
		// 0x00ff0000,// Red
		// 0x0000ff00,// Green
		// 0x000000ff,// Blue
		// 0xff000000,// Alpha
		// true, // Alpha Premultiplied
		// DataBuffer.TYPE_INT
		// );
		//
		// raster = colorModel.createCompatibleWritableRaster(width,
		// height);
		// }
		// break;
		//
		// case TYPE_INT_BGR:
		// {
		// colorModel = new DirectColorModel(24,
		// 0x000000ff, // Red
		// 0x0000ff00, // Green
		// 0x00ff0000 // Blue
		// );
		// raster = colorModel.createCompatibleWritableRaster(width,
		// height);
		// }
		// break;
		//
		// case TYPE_3BYTE_BGR:
		// {
		// ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		// int[] nBits = {8, 8, 8};
		// int[] bOffs = {2, 1, 0};
		// colorModel = new ComponentColorModel(cs, nBits, false, false,
		// Transparency.OPAQUE,
		// DataBuffer.TYPE_BYTE);
		// raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
		// width, height,
		// width*3, 3,
		// bOffs, null);
		// }
		// break;
		//
		// case TYPE_4BYTE_ABGR:
		// {
		// ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		// int[] nBits = {8, 8, 8, 8};
		// int[] bOffs = {3, 2, 1, 0};
		// colorModel = new ComponentColorModel(cs, nBits, true, false,
		// Transparency.TRANSLUCENT,
		// DataBuffer.TYPE_BYTE);
		// raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
		// width, height,
		// width*4, 4,
		// bOffs, null);
		// }
		// break;
		//
		// case TYPE_4BYTE_ABGR_PRE:
		// {
		// ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		// int[] nBits = {8, 8, 8, 8};
		// int[] bOffs = {3, 2, 1, 0};
		// colorModel = new ComponentColorModel(cs, nBits, true, true,
		// Transparency.TRANSLUCENT,
		// DataBuffer.TYPE_BYTE);
		// raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
		// width, height,
		// width*4, 4,
		// bOffs, null);
		// }
		// break;
		//
		// case TYPE_BYTE_GRAY:
		// {
		// ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		// int[] nBits = {8};
		// colorModel = new ComponentColorModel(cs, nBits, false, true,
		// Transparency.OPAQUE,
		// DataBuffer.TYPE_BYTE);
		// raster = colorModel.createCompatibleWritableRaster(width,
		// height);
		// }
		// break;
		//
		// case TYPE_USHORT_GRAY:
		// {
		// ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		// int[] nBits = {16};
		// colorModel = new ComponentColorModel(cs, nBits, false, true,
		// Transparency.OPAQUE,
		// DataBuffer.TYPE_USHORT);
		// raster = colorModel.createCompatibleWritableRaster(width,
		// height);
		// }
		// break;
		//
		// case TYPE_BYTE_BINARY:
		// {
		// byte[] arr = {(byte)0, (byte)0xff};
		//
		// colorModel = new IndexColorModel(1, 2, arr, arr, arr);
		// raster = Raster.createPackedRaster(DataBuffer.TYPE_BYTE,
		// width, height, 1, 1, null);
		// }
		// break;
		//
		// case TYPE_BYTE_INDEXED:
		// {
		// // Create a 6x6x6 color cube
		// int[] cmap = new int[256];
		// int i=0;
		// for (int r=0; r < 256; r += 51) {
		// for (int g=0; g < 256; g += 51) {
		// for (int b=0; b < 256; b += 51) {
		// cmap[i++] = (r<<16)|(g<<8)|b;
		// }
		// }
		// }
		// // And populate the rest of the cmap with gray values
		// int grayIncr = 256/(256-i);
		//
		// // The gray ramp will be between 18 and 252
		// int gray = grayIncr*3;
		// for (; i < 256; i++) {
		// cmap[i] = (gray<<16)|(gray<<8)|gray;
		// gray += grayIncr;
		// }
		//
		// colorModel = new IndexColorModel(8, 256, cmap, 0, false, -1,
		// DataBuffer.TYPE_BYTE);
		// raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
		// width, height, 1, null);
		// }
		// break;
		//
		// case TYPE_USHORT_565_RGB:
		// {
		// colorModel = new DirectColorModel(16,
		// DCM_565_RED_MASK,
		// DCM_565_GRN_MASK,
		// DCM_565_BLU_MASK
		// );
		// raster = colorModel.createCompatibleWritableRaster(width,
		// height);
		// }
		// break;
		//
		// case TYPE_USHORT_555_RGB:
		// {
		// colorModel = new DirectColorModel(15,
		// DCM_555_RED_MASK,
		// DCM_555_GRN_MASK,
		// DCM_555_BLU_MASK
		// );
		// raster = colorModel.createCompatibleWritableRaster(width,
		// height);
		// }
		// break;
		//
		default:
			throw new IllegalArgumentException("Unknown image type " + imageType);
		}
		this.imageType = imageType;
	}

	// /**
	// * Constructs a <code>BufferedImage</code> of one of the predefined
	// * image types:
	// * TYPE_BYTE_BINARY or TYPE_BYTE_INDEXED.
	// *
	// * <p> If the image type is TYPE_BYTE_BINARY, the number of
	// * entries in the color model is used to determine whether the
	// * image should have 1, 2, or 4 bits per pixel. If the color model
	// * has 1 or 2 entries, the image will have 1 bit per pixel. If it
	// * has 3 or 4 entries, the image with have 2 bits per pixel. If
	// * it has between 5 and 16 entries, the image will have 4 bits per
	// * pixel. Otherwise, an IllegalArgumentException will be thrown.
	// *
	// * @param width width of the created image
	// * @param height height of the created image
	// * @param imageType type of the created image
	// * @param cm <code>IndexColorModel</code> of the created image
	// * @throws IllegalArgumentException if the imageType is not
	// * TYPE_BYTE_BINARY or TYPE_BYTE_INDEXED or if the imageType is
	// * TYPE_BYTE_BINARY and the color map has more than 16 entries.
	// * @see #TYPE_BYTE_BINARY
	// * @see #TYPE_BYTE_INDEXED
	// */
	// public BufferedImage (int width,
	// int height,
	// int imageType,
	// IndexColorModel cm) {
	// if (cm.hasAlpha() && cm.isAlphaPremultiplied()) {
	// throw new IllegalArgumentException("This image types do not have "+
	// "premultiplied alpha.");
	// }
	//
	// switch(imageType) {
	// case TYPE_BYTE_BINARY:
	// int bits; // Will be set below
	// int mapSize = cm.getMapSize();
	// if (mapSize <= 2) {
	// bits = 1;
	// } else if (mapSize <= 4) {
	// bits = 2;
	// } else if (mapSize <= 16) {
	// bits = 4;
	// } else {
	// throw new IllegalArgumentException
	// ("Color map for TYPE_BYTE_BINARY " +
	// "must have no more than 16 entries");
	// }
	// raster = Raster.createPackedRaster(DataBuffer.TYPE_BYTE,
	// width, height, 1, bits, null);
	// break;
	//
	// case TYPE_BYTE_INDEXED:
	// raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
	// width, height, 1, null);
	// break;
	// default:
	// throw new IllegalArgumentException("Invalid image type (" +
	// imageType+").  Image type must"+
	// " be either TYPE_BYTE_BINARY or "+
	// " TYPE_BYTE_INDEXED");
	// }
	//
	// if (!cm.isCompatibleRaster(raster)) {
	// throw new
	// IllegalArgumentException("Incompatible image type and IndexColorModel");
	// }
	//
	// colorModel = cm;
	// this.imageType = imageType;
	// }

	/**
	 * Constructs a new <code>BufferedImage</code> with a specified
	 * <code>ColorModel</code> and <code>Raster</code>. If the number and types of
	 * bands in the <code>SampleModel</code> of the <code>Raster</code> do not
	 * match the number and types required by the <code>ColorModel</code> to
	 * represent its color and alpha components, a {@link RasterFormatException}
	 * is thrown. This method can multiply or divide the color <code>Raster</code>
	 * data by alpha to match the <code>alphaPremultiplied</code> state in the
	 * <code>ColorModel</code>. Properties for this <code>BufferedImage</code> can
	 * be established by passing in a {@link Hashtable} of <code>String</code>/
	 * <code>Object</code> pairs.
	 * 
	 * @param cm
	 *          <code>ColorModel</code> for the new image
	 * @param raster
	 *          <code>Raster</code> for the image data
	 * @param isRasterPremultiplied
	 *          if <code>true</code>, the data in the raster has been
	 *          premultiplied with alpha.
	 * @param properties
	 *          <code>Hashtable</code> of <code>String</code>/<code>Object</code>
	 *          pairs.
	 * @exception <code>RasterFormatException</code> if the number and types of
	 *            bands in the <code>SampleModel</code> of the <code>Raster</code>
	 *            do not match the number and types required by the
	 *            <code>ColorModel</code> to represent its color and alpha
	 *            components.
	 * @exception <code>IllegalArgumentException</code> if <code>raster</code> is
	 *            incompatible with <code>cm</code>
	 * @see ColorModel
	 * @see Raster
	 * @see WritableRaster
	 */

	/*
	 * 
	 * FOR NOW THE CODE WHICH DEFINES THE RASTER TYPE IS DUPLICATED BY DVF SEE THE
	 * METHOD DEFINERASTERTYPE @ RASTEROUTPUTMANAGER
	 */
	public BufferedImage(ColorModel cm, WritableRaster raster,
			boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
		//
		// if (!cm.isCompatibleRaster(raster)) {
		// throw new
		// IllegalArgumentException("Raster "+raster+
		// " is incompatible with ColorModel "+
		// cm);
		// }
		//
		if ((raster.minX != 0) || (raster.minY != 0)) {
			throw new IllegalArgumentException("Raster " + raster
					+ " has minX or minY not equal to zero: " + raster.minX + " "
					+ raster.minY);
		}

		colorModel = cm;
		this.raster = raster;
		raster.setImage(this);
		秘pix = ((DataBufferInt) raster.getDataBuffer()).data;
		this.properties = properties;
		// int numBands = raster.getNumBands();
		// boolean isAlphaPre = cm.isAlphaPremultiplied();
		//ColorSpace cs;

		// Force the raster data alpha state to match the premultiplied
		// state in the color model
		// coerceData(isRasterPremultiplied);

		// SampleModel sm = raster.getSampleModel();
		// cs = cm.getColorSpace();
		// int csType = cs.getType();
		// if (csType != ColorSpace.TYPE_RGB) {
		// if (csType == ColorSpace.TYPE_GRAY
		// && ComponentColorModel.class.equals(cm.getClass())) {
		// // Check if this might be a child raster (fix for bug 4240596)
		// if (sm instanceof ComponentSampleModel &&
		// ((ComponentSampleModel)sm).getPixelStride() != numBands) {
		// imageType = TYPE_CUSTOM;
		// } else if (raster instanceof ByteComponentRaster &&
		// PixelInterleavedSampleModel.class.equals(sm.getClass()) &&
		// raster.getNumBands() == 1 &&
		// cm.getComponentSize(0) == 8 &&
		// ((ByteComponentRaster)raster).getPixelStride() == 1) {
		// imageType = TYPE_BYTE_GRAY;
		// } else if (raster instanceof ShortComponentRaster &&
		// PixelInterleavedSampleModel.class.equals(sm.getClass()) &&
		// raster.getNumBands() == 1 &&
		// cm.getComponentSize(0) == 16 &&
		// ((ShortComponentRaster)raster).getPixelStride() == 1) {
		// imageType = TYPE_USHORT_GRAY;
		// }
		// } else {
		// imageType = TYPE_CUSTOM;
		// }
		// return;
		// }

		// if ((raster instanceof IntegerComponentRaster) &&
		// (numBands == 3 || numBands == 4)) {
		// IntegerComponentRaster iraster =
		// (IntegerComponentRaster) raster;
		// // Check if the raster params and the color model
		// // are correct
		// int pixSize = cm.getPixelSize();
		// if (iraster.getPixelStride() == 1 &&
		// DirectColorModel.class.equals(cm.getClass()) &&
		// SinglePixelPackedSampleModel.class.equals(sm.getClass()) &&
		// (pixSize == 32 || pixSize == 24))
		// {
		// // Now check on the DirectColorModel params
		// DirectColorModel dcm = (DirectColorModel) cm;
		// int rmask = dcm.getRedMask();
		// int gmask = dcm.getGreenMask();
		// int bmask = dcm.getBlueMask();
		// if (rmask == DCM_RED_MASK && gmask == DCM_GREEN_MASK &&
		// bmask == DCM_BLUE_MASK)
		// {
		// if (dcm.getAlphaMask() == DCM_ALPHA_MASK) {
		// imageType = (isAlphaPre
		// ? TYPE_INT_ARGB_PRE
		// : TYPE_INT_ARGB);
		// }
		// else {
		// // No Alpha
		// if (!dcm.hasAlpha()) {
		// imageType = TYPE_INT_RGB;
		// }
		// }
		// } // if (dcm.getRedMask() == DCM_RED_MASK &&
		// else if (rmask == DCM_BGR_RED_MASK && gmask == DCM_BGR_GRN_MASK
		// && bmask == DCM_BGR_BLU_MASK) {
		// if (!dcm.hasAlpha()) {
		// imageType = TYPE_INT_BGR;
		// }
		// } // if (rmask == DCM_BGR_RED_MASK &&
		// } // if (iraster.getPixelStride() == 1
		// } // ((raster instanceof IntegerComponentRaster) &&
		// else if ((IndexColorModel.class.equals(cm.getClass())) &&
		// (numBands == 1) &&
		// (!cm.hasAlpha() || !isAlphaPre))
		// {
		// IndexColorModel icm = (IndexColorModel) cm;
		// int pixSize = icm.getPixelSize();
		//
		// if (raster instanceof BytePackedRaster &&
		// MultiPixelPackedSampleModel.class.equals(sm.getClass()))
		// {
		// imageType = TYPE_BYTE_BINARY;
		// } // if (raster instanceof BytePackedRaster)
		// else if (raster instanceof ByteComponentRaster &&
		// PixelInterleavedSampleModel.class.equals(sm.getClass()))
		// {
		// ByteComponentRaster braster = (ByteComponentRaster) raster;
		// if (braster.getPixelStride() == 1 && pixSize <= 8) {
		// imageType = TYPE_BYTE_INDEXED;
		// }
		// }
		// } // else if (cm instanceof IndexColorModel) && (numBands == 1))
		// else if ((raster instanceof ShortComponentRaster)
		// && (DirectColorModel.class.equals(cm.getClass()))
		// && (SinglePixelPackedSampleModel.class.equals(sm.getClass()))
		// && (numBands == 3)
		// && !cm.hasAlpha())
		// {
		// DirectColorModel dcm = (DirectColorModel) cm;
		// if (dcm.getRedMask() == DCM_565_RED_MASK) {
		// if (dcm.getGreenMask() == DCM_565_GRN_MASK &&
		// dcm.getBlueMask() == DCM_565_BLU_MASK) {
		// imageType = TYPE_USHORT_565_RGB;
		// }
		// }
		// else if (dcm.getRedMask() == DCM_555_RED_MASK) {
		// if (dcm.getGreenMask() == DCM_555_GRN_MASK &&
		// dcm.getBlueMask() == DCM_555_BLU_MASK) {
		// imageType = TYPE_USHORT_555_RGB;
		// }
		// }
		// } // else if ((cm instanceof IndexColorModel) && (numBands == 1))
		// else if ((raster instanceof ByteComponentRaster)
		// && (cm instanceof ComponentColorModel)
		// && (raster.getSampleModel() instanceof PixelInterleavedSampleModel)
		// && (numBands == 3 || numBands == 4))
		// {
		// ComponentColorModel ccm = (ComponentColorModel) cm;
		// PixelInterleavedSampleModel csm =
		// (PixelInterleavedSampleModel)raster.getSampleModel();
		// ByteComponentRaster braster = (ByteComponentRaster) raster;
		// int[] offs = csm.getBandOffsets();
		// if (ccm.getNumComponents() != numBands) {
		// throw new RasterFormatException("Number of components in "+
		// "ColorModel ("+
		// ccm.getNumComponents()+
		// ") does not match # in "+
		// " Raster ("+numBands+")");
		// }
		// int[] nBits = ccm.getComponentSize();
		// boolean is8bit = true;
		// for (int i=0; i < numBands; i++) {
		// if (nBits[i] != 8) {
		// is8bit = false;
		// break;
		// }
		// }
		// if (is8bit &&
		// braster.getPixelStride() == numBands &&
		// offs[0] == numBands-1 &&
		// offs[1] == numBands-2 &&
		// offs[2] == numBands-3 &&
		// ComponentColorModel.class.equals(ccm.getClass()) &&
		// PixelInterleavedSampleModel.class.equals(csm.getClass()))
		// {
		// if (numBands == 3 && !ccm.hasAlpha()) {
		// imageType = TYPE_3BYTE_BGR;
		// }
		// else if (offs[3] == 0 && ccm.hasAlpha()) {
		// imageType = (isAlphaPre
		// ? TYPE_4BYTE_ABGR_PRE
		// : TYPE_4BYTE_ABGR);
		// }
		// }
		// } // else if ((raster instanceof ByteComponentRaster) &&
	}

	/**
	 * Returns the image type. If it is not one of the known types, TYPE_CUSTOM is
	 * returned.
	 * 
	 * @return the image type of this <code>BufferedImage</code>.
	 * @see #TYPE_INT_RGB
	 * @see #TYPE_INT_ARGB
	 * @see #TYPE_INT_ARGB_PRE
	 * @see #TYPE_INT_BGR
	 * @see #TYPE_3BYTE_BGR
	 * @see #TYPE_4BYTE_ABGR
	 * @see #TYPE_4BYTE_ABGR_PRE
	 * @see #TYPE_BYTE_GRAY
	 * @see #TYPE_BYTE_BINARY
	 * @see #TYPE_BYTE_INDEXED
	 * @see #TYPE_USHORT_GRAY
	 * @see #TYPE_USHORT_565_RGB
	 * @see #TYPE_USHORT_555_RGB
	 * @see #TYPE_CUSTOM
	 */
	public int getType() {
		return imageType;
	}

	/**
	 * Returns the <code>ColorModel</code>.
	 * 
	 * @return the <code>ColorModel</code> of this <code>BufferedImage</code>.
	 */
	@Override
	public ColorModel getColorModel() {
		return colorModel;
	}

	/**
	 * Returns the {@link WritableRaster}.
	 * 
	 * @return the <code>WriteableRaster</code> of this <code>BufferedImage</code>
	 *         .
	 */
	public WritableRaster getRaster() {
		return raster;
	}

	// /**
	// * Returns a <code>WritableRaster</code> representing the alpha
	// * channel for <code>BufferedImage</code> objects
	// * with <code>ColorModel</code> objects that support a separate
	// * spatial alpha channel, such as <code>ComponentColorModel</code> and
	// * <code>DirectColorModel</code>. Returns <code>null</code> if there
	// * is no alpha channel associated with the <code>ColorModel</code> in
	// * this image. This method assumes that for all
	// * <code>ColorModel</code> objects other than
	// * <code>IndexColorModel</code>, if the <code>ColorModel</code>
	// * supports alpha, there is a separate alpha channel
	// * which is stored as the last band of image data.
	// * If the image uses an <code>IndexColorModel</code> that
	// * has alpha in the lookup table, this method returns
	// * <code>null</code> since there is no spatially discrete alpha
	// * channel. This method creates a new
	// * <code>WritableRaster</code>, but shares the data array.
	// * @return a <code>WritableRaster</code> or <code>null</code> if this
	// * <code>BufferedImage</code> has no alpha channel associated
	// * with its <code>ColorModel</code>.
	// */
	// public WritableRaster getAlphaRaster() {
	// return colorModel.getAlphaRaster(raster);
	// }


	
	/**
	 * Returns an integer pixel in the default RGB color model (TYPE_INT_ARGB) and
	 * default sRGB colorspace. Color conversion takes place if this default model
	 * does not match the image <code>ColorModel</code>. There are only 8-bits of
	 * precision for each color component in the returned data when using this
	 * method.
	 * 
	 * <p>
	 * 
	 * An <code>ArrayOutOfBoundsException</code> may be thrown if the coordinates
	 * are not in bounds. However, explicit bounds checking is not guaranteed.
	 * 
	 * @param x
	 *          the X coordinate of the pixel from which to get the pixel in the
	 *          default RGB color model and sRGB color space
	 * @param y
	 *          the Y coordinate of the pixel from which to get the pixel in the
	 *          default RGB color model and sRGB color space
	 * @return an integer pixel in the default RGB color model and default sRGB
	 *         colorspace.
	 * @see #setRGB(int, int, int)
	 * @see #setRGB(int, int, int, int, int[], int, int)
	 */
	public int getRGB(int x, int y) {
		checkHavePixels();
		if (秘pix == null)
			秘pix = 秘pixSaved;
		return 秘pix[y * this.width + x];
	}

	/**
	 * Returns an array of integer pixels in the default RGB color model
	 * (TYPE_INT_ARGB) and default sRGB color space, from a portion of the image
	 * data. Color conversion takes place if the default model does not match
	 * the image <code>ColorModel</code>. There are only 8-bits of precision for
	 * each color component in the returned data when using this method. With a
	 * specified coordinate (x,&nbsp;y) in the image, the ARGB pixel can be
	 * accessed in this way:
	 * </p>
	 * 
	 * <pre>
	 * pixel = rgbArray[offset + (y - startY) * scansize + (x - startX)];
	 * </pre>
	 * 
	 * <p>
	 * 
	 * An <code>ArrayOutOfBoundsException</code> may be thrown if the region is
	 * not in bounds. However, explicit bounds checking is not guaranteed.
	 * 
	 * @param startX
	 *            the starting X coordinate
	 * @param startY
	 *            the starting Y coordinate
	 * @param w
	 *            width of region
	 * @param h
	 *            height of region
	 * @param rgbArray
	 *            if not <code>null</code>, the rgb pixels are written here
	 * @param offset
	 *            offset into the <code>rgbArray</code>
	 * @param scansize
	 *            scanline stride for the <code>rgbArray</code>
	 * @return array of RGB pixels.
	 * @see #setRGB(int, int, int)
	 * @see #setRGB(int, int, int, int, int[], int, int)
	 */
	public int[] getRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize) {
		checkHavePixels();
		if (秘pix == null)
			秘pix = 秘pixSaved;
		return getRangeRGB(startX, startY, w, h, rgbArray, offset, scansize);
	}

	/**
	 * 
	 * @return true if pixels had to be set
	 */
	public boolean checkHavePixels() {
		if ((秘imgNode != null || 秘g != null) && !秘havePix) {
			setPixels();
			return true;
		}
		return false;
	}

	public int[] getRangeRGB(int startX, int startY, int w, int h,
			int[] rgbArray, int offset, int scansize) {
		if (秘pix == null && 秘pixSaved == null)
			checkHavePixels();
		int[] pixels = (秘pix == null ? 秘pixSaved : 秘pix);
		for (int y = startY, yoff=offset; y < startY + h; y++, yoff += scansize)
			for (int off = yoff, x = startX; x < startX + w; x++)
				rgbArray[off++] = pixels[y * this.width + x];
		return rgbArray;
	}
	
	/**
	 * Sets a pixel in this <code>BufferedImage</code> to the specified RGB value.
	 * The pixel is assumed to be in the default RGB color model, TYPE_INT_ARGB,
	 * and default sRGB color space. For images with an
	 * <code>IndexColorModel</code>, the index with the nearest color is chosen.
	 * 
	 * <p>
	 * 
	 * An <code>ArrayOutOfBoundsException</code> may be thrown if the coordinates
	 * are not in bounds. However, explicit bounds checking is not guaranteed.
	 * 
	 * @param x
	 *          the X coordinate of the pixel to set
	 * @param y
	 *          the Y coordinate of the pixel to set
	 * @param rgb
	 *          the RGB value
	 * @see #getRGB(int, int)
	 * @see #getRGB(int, int, int, int, int[], int, int)
	 */
	public synchronized void setRGB(int x, int y, int rgb) {
		if (checkHavePixels())
		  秘imgNode = null;
		int[] pixels = (秘pix == null ? 秘pixSaved : 秘pix);
		pixels[y * this.width + x] = rgb;
	}

	/**
	 * 
	 * Sets an array of integer pixels in the default RGB color model
	 * (TYPE_INT_ARGB) and default sRGB color space, into a portion of the image
	 * data. Color conversion takes place if the default model does not match the
	 * image <code>ColorModel</code>. There are only 8-bits of precision for each
	 * color component in the returned data when using this method. With a
	 * specified coordinate (x,&nbsp;y) in the this image, the ARGB pixel can be
	 * accessed in this way:
	 * 
	 * <pre>
	 * pixel = rgbArray[offset + (y - startY) * scansize + (x - startX)];
	 * </pre>
	 * 
	 * WARNING: No dithering takes place.
	 * 
	 * <p>
	 * 
	 * An <code>ArrayOutOfBoundsException</code> may be thrown if the region is
	 * not in bounds. However, explicit bounds checking is not guaranteed.
	 * 
	 * @param startX
	 *          the starting X coordinate
	 * @param startY
	 *          the starting Y coordinate
	 * @param w
	 *          width of the region
	 * @param h
	 *          height of the region
	 * @param rgbArray
	 *          the rgb pixels
	 * @param offset
	 *          offset into the <code>rgbArray</code>
	 * @param scansize
	 *          scanline stride for the <code>rgbArray</code>
	 * @see #getRGB(int, int)
	 * @see #getRGB(int, int, int, int, int[], int, int)
	 */
	public void setRGB(int startX, int startY, int w, int h, int[] rgbArray,
			int offset, int scansize) {
		if (checkHavePixels())
			  秘imgNode = null;
		int[] pixels = (秘pix == null ? 秘pixSaved : 秘pix);
		int width = this.width;
		for (int y = startY, yoff = offset; y < startY + h; y++, yoff += scansize) 
			for (int x = startX, off = yoff; x < startX + w; x++) 
				pixels[y * width + x] = rgbArray[off++];
		秘pix = 秘pixSaved = pixels;
		// 秘pix is used by getGraphics()
		// 秘pixSaved is kept in case we need to do this again
		秘g = null; // forces new this.秘canvas to be created in getGraphics()
		getImageGraphic(); // sets 秘pix = null and creates 秘canvas 
		
	}

	/**
	 * Returns the width of the <code>BufferedImage</code>.
	 * 
	 * @return the width of this <code>BufferedImage</code>
	 */
	@Override
	public int getWidth() {
		return raster.getWidth();
	}

	/**
	 * Returns the height of the <code>BufferedImage</code>.
	 * 
	 * @return the height of this <code>BufferedImage</code>
	 */
	@Override
	public int getHeight() {
		return raster.getHeight();
	}

	/**
	 * 
	 * Returns the width of the <code>BufferedImage</code>.
	 * 
	 * @param observer
	 *          ignored
	 * @return the width of this <code>BufferedImage</code>
	 */
	@Override
	public int getWidth(ImageObserver observer) {
		return raster.getWidth();
	}

	/**
	 * Returns the height of the <code>BufferedImage</code>.
	 * 
	 * @param observer
	 *          ignored
	 * @return the height of this <code>BufferedImage</code>
	 */
	@Override
	public int getHeight(ImageObserver observer) {
		return raster.getHeight();
	}

	/**
	 * Returns the object that produces the pixels for the image.
	 * 
	 * @return the {@link ImageProducer} that is used to produce the pixels for
	 *         this image.
	 * @see ImageProducer
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ImageProducer getSource() {
		if (osis == null) {
			if (properties == null) {
				properties = new Hashtable();
			}
			osis = new OffScreenImageSource(this, properties);
		}
		return osis;
	}

	/**
	 * Returns a property of the image by name. Individual property names are
	 * defined by the various image formats. If a property is not defined for a
	 * particular image, this method returns the <code>UndefinedProperty</code>
	 * field. If the properties for this image are not yet known, then this method
	 * returns <code>null</code> and the <code>ImageObserver</code> object is
	 * notified later. The property name "comment" should be used to store an
	 * optional comment that can be presented to the user as a description of the
	 * image, its source, or its author.
	 * 
	 * @param name
	 *          the property name
	 * @param observer
	 *          the <code>ImageObserver</code> that receives notification
	 *          regarding image information
	 * @return an {@link Object} that is the property referred to by the specified
	 *         <code>name</code> or <code>null</code> if the properties of this
	 *         image are not yet known.
	 * @throws <code>NullPointerException</code> if the property name is null.
	 * @see ImageObserver
	 * @see java.awt.Image#UndefinedProperty
	 */
	@Override
	public Object getProperty(String name, ImageObserver observer) {
		return getProperty(name);
	}

	/**
	 * Returns a property of the image by name.
	 * 
	 * @param name
	 *          the property name
	 * @return an <code>Object</code> that is the property referred to by the
	 *         specified <code>name</code>.
	 * @throws <code>NullPointerException</code> if the property name is null.
	 */
	@Override
	public Object getProperty(String name) {
		if (name == null) {
			throw new NullPointerException("null property name is not allowed");
		}
		if (properties == null) {
			return java.awt.Image.UndefinedProperty;
		}
		Object o = properties.get(name);
		if (o == null) {
			o = java.awt.Image.UndefinedProperty;
		}
		return o;
	}

	/**
	 * This method returns a {@link Graphics2D}, but is here for backwards
	 * compatibility. {@link #createGraphics() createGraphics} is more convenient,
	 * since it is declared to return a <code>Graphics2D</code>.
	 * 
	 * @return a <code>Graphics2D</code>, which can be used to draw into this
	 *         image.
	 */
	@Override
	public Graphics getGraphics() {
		return createGraphics();
	}

	/**
	 * Creates a <code>Graphics2D</code>, which can be used to draw into this
	 * <code>BufferedImage</code>.
	 * 
	 * @return a <code>Graphics2D</code>, used for drawing into this image.
	 */
	public Graphics2D createGraphics() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		return (Graphics2D) env.createGraphics(this);
	}

	/**
	 * Returns a subimage defined by a specified rectangular region. The returned
	 * <code>BufferedImage</code> shares the same data array as the original
	 * image.
	 * 
	 * @param x
	 *          the X coordinate of the upper-left corner of the specified
	 *          rectangular region
	 * @param y
	 *          the Y coordinate of the upper-left corner of the specified
	 *          rectangular region
	 * @param w
	 *          the width of the specified rectangular region
	 * @param h
	 *          the height of the specified rectangular region
	 * @return a <code>BufferedImage</code> that is the subimage of this
	 *         <code>BufferedImage</code>.
	 * @exception <code>RasterFormatException</code> if the specified area is not
	 *            contained within this <code>BufferedImage</code>.
	 */
	public BufferedImage getSubimage(int x, int y, int w, int h) {
		return new BufferedImage(colorModel, raster.createWritableChild(x, y, w, h,
				0, 0, null), colorModel.isAlphaPremultiplied(), properties);
	}

	/**
	 * Returns whether or not the alpha has been premultiplied. It returns
	 * <code>false</code> if there is no alpha.
	 * 
	 * @return <code>true</code> if the alpha has been premultiplied;
	 *         <code>false</code> otherwise.
	 */
	public boolean isAlphaPremultiplied() {
		return false;
		// return colorModel.isAlphaPremultiplied();
	}

	// /**
	// * Forces the data to match the state specified in the
	// * <code>isAlphaPremultiplied</code> variable. It may multiply or
	// * divide the color raster data by alpha, or do nothing if the data is
	// * in the correct state.
	// * @param isAlphaPremultiplied <code>true</code> if the alpha has been
	// * premultiplied; <code>false</code> otherwise.
	// */
	// public void coerceData (boolean isAlphaPremultiplied) {
	// //
	// // if (colorModel.hasAlpha() &&
	// // colorModel.isAlphaPremultiplied() != isAlphaPremultiplied) {
	// // // Make the color model do the conversion
	// // colorModel = colorModel.coerceData (raster, isAlphaPremultiplied);
	// // }
	// }

	/**
	 * Returns a <code>String</code> representation of this
	 * <code>BufferedImage</code> object and its values.
	 * 
	 * @return a <code>String</code> representing this <code>BufferedImage</code>.
	 */
	@Override
	public String toString() {
		return new String("BufferedImage@" + Integer.toHexString(hashCode())
				+ ": type = " + imageType + " " + colorModel.toString() + " " + raster.toString());
	}

	/**
	 * Returns a {@link Vector} of {@link RenderedImage} objects that are the
	 * immediate sources, not the sources of these immediate sources, of image
	 * data for this <code>BufferedImage</code>. This method returns
	 * <code>null</code> if the <code>BufferedImage</code> has no information
	 * about its immediate sources. It returns an empty <code>Vector</code> if the
	 * <code>BufferedImage</code> has no immediate sources.
	 * 
	 * @return a <code>Vector</code> containing immediate sources of this
	 *         <code>BufferedImage</code> object's image date, or
	 *         <code>null</code> if this <code>BufferedImage</code> has no
	 *         information about its immediate sources, or an empty
	 *         <code>Vector</code> if this <code>BufferedImage</code> has no
	 *         immediate sources.
	 */
	@Override
	public Vector<RenderedImage> getSources() {
		return null;
	}

	/**
	 * Returns an array of names recognized by {@link #getProperty(String)
	 * getProperty(String)} or <code>null</code>, if no property names are
	 * recognized.
	 * 
	 * @return a <code>String</code> array containing all of the property names
	 *         that <code>getProperty(String)</code> recognizes; or
	 *         <code>null</code> if no property names are recognized.
	 */
	@Override
	public String[] getPropertyNames() {
		return null;
	}

	/**
	 * Returns the minimum x coordinate of this <code>BufferedImage</code>. This
	 * is always zero.
	 * 
	 * @return the minimum x coordinate of this <code>BufferedImage</code>.
	 */
	@Override
	public int getMinX() {
		return raster.getMinX();
	}

	/**
	 * Returns the minimum y coordinate of this <code>BufferedImage</code>. This
	 * is always zero.
	 * 
	 * @return the minimum y coordinate of this <code>BufferedImage</code>.
	 */
	@Override
	public int getMinY() {
		return raster.getMinY();
	}

	/**
	 * Returns the <code>SampleModel</code> associated with this
	 * <code>BufferedImage</code>.
	 * 
	 * @return the <code>SampleModel</code> of this <code>BufferedImage</code>.
	 */
	@Override
	public SampleModel getSampleModel() {
		return raster.getSampleModel();
	}

	/**
	 * Returns the number of tiles in the x direction. This is always one.
	 * 
	 * @return the number of tiles in the x direction.
	 */
	@Override
	public int getNumXTiles() {
		return 1;
	}

	/**
	 * Returns the number of tiles in the y direction. This is always one.
	 * 
	 * @return the number of tiles in the y direction.
	 */
	@Override
	public int getNumYTiles() {
		return 1;
	}

	/**
	 * Returns the minimum tile index in the x direction. This is always zero.
	 * 
	 * @return the minimum tile index in the x direction.
	 */
	@Override
	public int getMinTileX() {
		return 0;
	}

	/**
	 * Returns the minimum tile index in the y direction. This is always zero.
	 * 
	 * @return the mininum tile index in the y direction.
	 */
	@Override
	public int getMinTileY() {
		return 0;
	}

	/**
	 * Returns the tile width in pixels.
	 * 
	 * @return the tile width in pixels.
	 */
	@Override
	public int getTileWidth() {
		return raster.getWidth();
	}

	/**
	 * Returns the tile height in pixels.
	 * 
	 * @return the tile height in pixels.
	 */
	@Override
	public int getTileHeight() {
		return raster.getHeight();
	}

	/**
	 * Returns the x offset of the tile grid relative to the origin, For example,
	 * the x coordinate of the location of tile (0,&nbsp;0). This is always zero.
	 * 
	 * @return the x offset of the tile grid.
	 */
	@Override
	public int getTileGridXOffset() {
		return 0;// raster.getSampleModelTranslateX();
	}

	/**
	 * Returns the y offset of the tile grid relative to the origin, For example,
	 * the y coordinate of the location of tile (0,&nbsp;0). This is always zero.
	 * 
	 * @return the y offset of the tile grid.
	 */
	@Override
	public int getTileGridYOffset() {
		return 0;// raster.getSampleModelTranslateY();
	}

	/**
	 * Returns tile (<code>tileX</code>,&nbsp;<code>tileY</code>). Note that
	 * <code>tileX</code> and <code>tileY</code> are indices into the tile array,
	 * not pixel locations. The <code>Raster</code> that is returned is live,
	 * which means that it is updated if the image is changed.
	 * 
	 * @param tileX
	 *          the x index of the requested tile in the tile array
	 * @param tileY
	 *          the y index of the requested tile in the tile array
	 * @return a <code>Raster</code> that is the tile defined by the arguments
	 *         <code>tileX</code> and <code>tileY</code>.
	 * @exception <code>ArrayIndexOutOfBoundsException</code> if both
	 *            <code>tileX</code> and <code>tileY</code> are not equal to 0
	 */
	@Override
	public Raster getTile(int tileX, int tileY) {
		// SwingJS not implemented
		// if (tileX == 0 && tileY == 0) {
		return raster;
		// }
		// throw new ArrayIndexOutOfBoundsException("BufferedImages only have"+
		// " one tile with index 0,0");
	}

	/**
	 * Returns the image as one large tile. The <code>Raster</code> returned is a
	 * copy of the image data is not updated if the image is changed.
	 * 
	 * @return a <code>Raster</code> that is a copy of the image data.
	 * @see #setData(Raster)
	 */
	@Override
	public Raster getData() {

		// REMIND : this allocates a whole new tile if raster is a
		// subtile. (It only copies in the requested area)
		// We should do something smarter.
		int width = raster.getWidth();
		int height = raster.getHeight();
		int startX = raster.getMinX();
		int startY = raster.getMinY();
		WritableRaster wr = Raster.createWritableRaster(
				raster.getSampleModel(),
				new Point(raster.getSampleModelTranslateX(), raster
						.getSampleModelTranslateY()));

		Object tdata = null;
		checkHavePixels();
		for (int i = startY; i < startY + height; i++) {
			tdata = raster.getDataElements(startX, i, width, 1, tdata);
			wr.setDataElements(startX, i, width, 1, tdata);
		}
		return wr;
	}

	/**
	 * Computes and returns an arbitrary region of the <code>BufferedImage</code>.
	 * The <code>Raster</code> returned is a copy of the image data and is not
	 * updated if the image is changed.
	 * 
	 * @param rect
	 *          the region of the <code>BufferedImage</code> to be returned.
	 * @return a <code>Raster</code> that is a copy of the image data of the
	 *         specified region of the <code>BufferedImage</code>
	 * @see #setData(Raster)
	 */
	@Override
	public Raster getData(Rectangle rect) {
		SampleModel sm = raster.getSampleModel();
		SampleModel nsm = sm.createCompatibleSampleModel(rect.width, rect.height);
		WritableRaster wr = Raster.createWritableRaster(nsm, rect.getLocation());
		int width = rect.width;
		int height = rect.height;
		int startX = rect.x;
		int startY = rect.y;

		Object tdata = null;

		for (int i = startY; i < startY + height; i++) {
			tdata = raster.getDataElements(startX, i, width, 1, tdata);
			wr.setDataElements(startX, i, width, 1, tdata);
		}
		return wr;
	}

	/**
	 * Computes an arbitrary rectangular region of the <code>BufferedImage</code>
	 * and copies it into a specified <code>WritableRaster</code>. The region to
	 * be computed is determined from the bounds of the specified
	 * <code>WritableRaster</code>. The specified <code>WritableRaster</code> must
	 * have a <code>SampleModel</code> that is compatible with this image. If
	 * <code>outRaster</code> is <code>null</code>, an appropriate
	 * <code>WritableRaster</code> is created.
	 * 
	 * @param outRaster
	 *          a <code>WritableRaster</code> to hold the returned part of the
	 *          image, or <code>null</code>
	 * @return a reference to the supplied or created <code>WritableRaster</code>.
	 */
	@Override
	public WritableRaster copyData(WritableRaster outRaster) {
		if (outRaster == null) {
			return (WritableRaster) getData();
		}
		int width = outRaster.getWidth();
		int height = outRaster.getHeight();
		int startX = outRaster.getMinX();
		int startY = outRaster.getMinY();

		Object tdata = null;

		for (int i = startY; i < startY + height; i++) {
			tdata = raster.getDataElements(startX, i, width, 1, tdata);
			outRaster.setDataElements(startX, i, width, 1, tdata);
		}

		return outRaster;
	}

	/**
	 * Sets a rectangular region of the image to the contents of the specified
	 * <code>Raster</code> <code>r</code>, which is assumed to be in the same
	 * coordinate space as the <code>BufferedImage</code>. The operation is
	 * clipped to the bounds of the <code>BufferedImage</code>.
	 * 
	 * @param r
	 *          the specified <code>Raster</code>
	 * @see #getData
	 * @see #getData(Rectangle)
	 */
	public void setData(Raster r) {
		int width = r.getWidth();
		int height = r.getHeight();
		int startX = r.getMinX();
		int startY = r.getMinY();

		int[] tdata = null;

		// Clip to the current Raster
		Rectangle rclip = new Rectangle(startX, startY, width, height);
		Rectangle bclip = new Rectangle(0, 0, raster.width, raster.height);
		Rectangle intersect = rclip.intersection(bclip);
		if (intersect.isEmpty()) {
			return;
		}
		width = intersect.width;
		height = intersect.height;
		startX = intersect.x;
		startY = intersect.y;

		// remind use get/setDataElements for speed if Rasters are
		// compatible
		for (int i = startY; i < startY + height; i++) {
			tdata = r.getPixels(startX, i, width, 1, tdata);
			raster.setPixels(startX, i, width, 1, tdata);
		}
	}

	//
	//
	// /**
	// * Adds a tile observer. If the observer is already present,
	// * it receives multiple notifications.
	// * @param to the specified {@link TileObserver}
	// */
	// public void addTileObserver (TileObserver to) {
	// }
	//
	// /**
	// * Removes a tile observer. If the observer was not registered,
	// * nothing happens. If the observer was registered for multiple
	// * notifications, it is now registered for one fewer notification.
	// * @param to the specified <code>TileObserver</code>.
	// */
	// public void removeTileObserver (TileObserver to) {
	// }
	//
	// /**
	// * Returns whether or not a tile is currently checked out for writing.
	// * @param tileX the x index of the tile.
	// * @param tileY the y index of the tile.
	// * @return <code>true</code> if the tile specified by the specified
	// * indices is checked out for writing; <code>false</code>
	// * otherwise.
	// * @exception <code>ArrayIndexOutOfBoundsException</code> if both
	// * <code>tileX</code> and <code>tileY</code> are not equal
	// * to 0
	// */
	// public boolean isTileWritable (int tileX, int tileY) {
	// if (tileX == 0 && tileY == 0) {
	// return true;
	// }
	// throw new IllegalArgumentException("Only 1 tile in image");
	// }
	//
	// /**
	// * Returns an array of {@link Point} objects indicating which tiles
	// * are checked out for writing. Returns <code>null</code> if none are
	// * checked out.
	// * @return a <code>Point</code> array that indicates the tiles that
	// * are checked out for writing, or <code>null</code> if no
	// * tiles are checked out for writing.
	// */
	// public Point[] getWritableTileIndices() {
	// Point[] p = new Point[1];
	// p[0] = new Point(0, 0);
	//
	// return p;
	// }
	//
	// /**
	// * Returns whether or not any tile is checked out for writing.
	// * Semantically equivalent to
	// * <pre>
	// * (getWritableTileIndices() != null).
	// * </pre>
	// * @return <code>true</code> if any tile is checked out for writing;
	// * <code>false</code> otherwise.
	// */
	// public boolean hasTileWriters () {
	// return true;
	// }
	//
	// // /**
	// // * Checks out a tile for writing. All registered
	// // * <code>TileObservers</code> are notified when a tile goes from having
	// // * no writers to having one writer.
	// // * @param tileX the x index of the tile
	// // * @param tileY the y index of the tile
	// // * @return a <code>WritableRaster</code> that is the tile, indicated by
	// // * the specified indices, to be checked out for writing.
	// // */
	// // public WritableRaster getWritableTile (int tileX, int tileY) {
	// // return raster;
	// // }
	//
	// /**
	// * Relinquishes permission to write to a tile. If the caller
	// * continues to write to the tile, the results are undefined.
	// * Calls to this method should only appear in matching pairs
	// * with calls to {@link #getWritableTile(int, int) getWritableTile(int,
	// int)}. Any other leads
	// * to undefined results. All registered <code>TileObservers</code>
	// * are notified when a tile goes from having one writer to having no
	// * writers.
	// * @param tileX the x index of the tile
	// * @param tileY the y index of the tile
	// */
	// public void releaseWritableTile (int tileX, int tileY) {
	// }

	/**
	 * Returns the transparency. Returns either OPAQUE, BITMASK, or TRANSLUCENT.
	 * 
	 * @return the transparency of this <code>BufferedImage</code>.
	 * @see Transparency#OPAQUE
	 * @see Transparency#BITMASK
	 * @see Transparency#TRANSLUCENT
	 * @since 1.5
	 */
	@Override
	public int getTransparency() {
		return colorModel.getTransparency();
	}

	
	/**
	 * Set the underlying graphics object coming from painting this image
	 * 
	 * 
	 */
	
	public void setImageFromHTML5Canvas(JSGraphics2D g) {
		this.秘g = g;
		width = raster.width;
		height = raster.height;
		setPixels();
	}
	
	/**
	 * Extract the int[] data from this image by installing it in a canvas.
	 * Note that if if img.complete == false, then this will result in a
	 * black rectangle.
	 * 
	 */
	@SuppressWarnings("unused")
	public void setPixels() {
		DOMNode canvas = (秘g == null ? null : /** @j2sNative this.秘g.canvas || */null);
		if (canvas == null)
			canvas = DOMNode.createElement("canvas", null);
		int w = width;
		int h = height;
		int[] data = null;
		/**
		 * note that setting canvas.width clears it
		 * @j2sNative
		 * 
		 * if (!this.秘g) {
		 *   canvas.width = w;
		 *   canvas.height = h;
		 * }
		 * var ctx = canvas.getContext("2d");
		 * if (!this.秘g)
		 *   ctx.drawImage(this.秘imgNode, 0, 0, w, h);
		 * data = ctx.getImageData(0, 0, w, h).data;
		 * 
		 */
		{
		}
		DataBufferInt buffer = (DataBufferInt) raster.getDataBuffer();
	    toIntARGB(data, 秘pix = buffer.data);
		秘imgNode = canvas;
		秘havePix = true;
	}

	
	@Override
	public void flush() {
		// call this method after drawing to ensure that
		// pixels are recreated from the HTML5 canvas
	    秘pix = null;
		秘havePix = false;
		// was for surfaceManager only super.flush();
	}
	/**
	 * convert [r g b a  r g b a ...] into [argb argb argb ...]
	 * 
	 * currently does not respect transparency
	 * 
	 * @param imgData HTML5 canvas.context.imageData.data
	 * @return array of ARGB values
	 * 
	 */
  void toIntARGB(int[] imgData, int[] iData) {
    /*
     * red=imgData.data[0];
     * green=imgData.data[1];
     * blue=imgData.data[2];
     * alpha=imgData.data[3];
     */
    int n = imgData.length / 4;
    int a;
    for (int i = 0, j = 0; i < n;) {
		  int argb = (imgData[j++] << 16) | (imgData[j++] << 8) | imgData[j++] | 0xFF000000;
	      iData[i++] =  (imgData[j++] == 0 ? 0 : argb);   	
    }
  }

  /**
   * 
   * Get a JSGraphics2D for this image, but don't initialize it with a state save
   * the way g.create() or image.getGraphics() or image.createGraphics() do.
   * So do NOT execute g.dispose() on the returned object. 
   * 
   * @author Bob Hanson
   * @return a JSGraphics2D object
   */
	@SuppressWarnings("unused")
	public Graphics2D getImageGraphic() {
		if (秘g == null) {
			HTML5Canvas canvas = (HTML5Canvas) DOMNode.createElement("canvas", "img" + System.currentTimeMillis());
			int w = getWidth();
			int h = getHeight();
			/**
			 * @j2sNative
			 * 
			 * canvas.width = w;
			 * canvas.height = h;
			 * 	
			 */
			 秘canvas = canvas;
			Object pix =  秘pix;
			秘g = new JSGraphics2D(canvas);
			// we need to draw the image now, because it might
			// have pixels. Note that Java actually does not
			// allow creating a Graphics from MemoryImageSource
			// so pixels would never be there. 
			if (pix != null)
				秘g.drawImagePriv(this, 0, 0, null);
			/**
			 * @j2sNative
			 * if (pix)
			 *   pix.img = this;
			 * 
			 */
			
			// 秘pix = null;
			flush(); // also setting 秘havePix false
			
		}
		Graphics2D g2d = (Graphics2D) (Object)秘g;
		if (秘component != null) {
			g2d.setFont(秘component.getFont());
			g2d.setBackground(秘component.getBackground());
			g2d.setColor(秘component.getForeground());
		}
		return g2d;
	}


}
