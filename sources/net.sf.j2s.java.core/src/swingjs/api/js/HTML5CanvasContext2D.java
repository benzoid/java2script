package swingjs.api.js;

import java.awt.geom.AffineTransform;

public abstract class HTML5CanvasContext2D {

	public class ImageData {
		public int[] data; 
	}

	public ImageData imageData;
	
	public Object[][] _aSaved;
	
	public double lineWidth;

	public String font, fillStyle, strokeStyle;

	public float globalAlpha;

	public abstract void drawImage(DOMNode img, int sx,
			int sy, int swidth, int sheight, int dx, int dy, int dwidth, int dheight);

	public abstract ImageData getImageData(int x, int y, int width, int height);

	public abstract void beginPath();

	public abstract void moveTo(double x0, double y0);

	public abstract void lineTo(double x1, double y1);

	public abstract void stroke();

	public abstract void save();

	public abstract void scale(double f, double g);

	public abstract void arc(double centerX, double centerY, double radius, double startAngle, double  endAngle, boolean counterclockwise);

	public abstract void closePath();

	public abstract void restore();

	public abstract void translate(double x, double y);
	
	public abstract void rotate(double radians);

	public abstract void fill();

	public abstract void rect(double x, double y, double width, double height);

	public abstract void fillText(String s, double x, double y);

	public abstract void fillRect(double x, double y, double width, double height);

	public abstract void clearRect(int i, int j, int windowWidth, int windowHeight);

	public abstract void setLineDash(int[] dash);

	public abstract void clip();

	public abstract void quadraticCurveTo(double d, double e, double f, double g);

	public abstract void bezierCurveTo(double d, double e, double f, double g, double h, double i);

	public abstract void drawImage(DOMNode img, int x, int y, int width, int height);

	public abstract void putImageData(Object imageData, int x, int y);

	/**
	 * pull one save structure onto the stack array ctx._aSaved
	 * 
	 * @param ctx
	 * @return the length of the stack array after the push
	 */
	public static int push(HTML5CanvasContext2D ctx, Object[] map) {
		/**
		 * @j2sNative
		 * 
		 * (ctx._aSaved || (ctx._aSaved = [])).push(map); 
		 * return ctx._aSaved.length;
		 */
		{
			return 0;
		}
	}

	/**
	 * pull one save structure off the stack array ctx._aSaved
	 * 
	 * @param ctx
	 * @return
	 */
	public static Object[] pop(HTML5CanvasContext2D ctx) {
		/**
		 * @j2sNative
		 * 
		 * return (ctx._aSaved && ctx._aSaved.length > 0 ? ctx._aSaved.pop() : null); 
		 */
		{
			return null;
		}
	}

	public static int getSavedLevel(HTML5CanvasContext2D ctx) {
		/**
		 * @j2sNative
		 * 
		 * return (ctx._aSaved ? ctx._aSaved.length : 0); 
		 */
		{
			return 0;
		}
	}

	public static double[] getMatrix(HTML5CanvasContext2D ctx, AffineTransform transform) {
		double[] m = /**  @j2sNative ctx._m || */ null;
		if (m == null) {
			m = new double[6];
			/**
			 * @j2sNative
			 * ctx._m = m;
			 */
			transform.getMatrix(m);
		}
		return m;
	}


}
