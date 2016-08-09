package com.tobiasschuerg.fitted.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Super class for fitted drawables which fit their content into a predefined shape.
 * <p>
 * Created by Tobias Schürg on 02.08.2016.
 */

public abstract class FittedDrawable extends Drawable {

	final static String LOG_TAG = FittedDrawable.class.getSimpleName();
	private final SHAPE shape;
	private final int fillColor;
	private final Paint fillPaint;
	private final Paint foregroundPaint;
	static boolean debug = false;
	private int additionalPadding = 0;
	boolean isAlphaEnabled;

	FittedDrawable(SHAPE shape, int backgroundColor) {
		this.shape = shape;
		this.fillColor = backgroundColor;

		fillPaint = new Paint();
		fillPaint.setColor(fillColor);
		fillPaint.setStyle(Paint.Style.FILL);
		fillPaint.setAntiAlias(true);
		fillPaint.setDither(true);

		foregroundPaint = new Paint();
		foregroundPaint.setAntiAlias(true);
		foregroundPaint.setTextAlign(Paint.Align.CENTER);
		foregroundPaint.setFilterBitmap(true);
		foregroundPaint.setDither(true);
		foregroundPaint.setStyle(Paint.Style.STROKE);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public int getAdditionalPadding() {
		if (debug) {
			Log.d("FittedDrawable", "Padding is " + additionalPadding);
		}
		return additionalPadding;
	}

	/**
	 * Expects dp!
	 *
	 * @param padding padding in dp
	 */
	public void setAdditionalPadding(int padding) {
		Log.d("FittedDrawable", "Setting additionalPadding " + padding);
		DisplayMetrics displaymetrics = Resources.getSystem().getDisplayMetrics();
		this.additionalPadding = (int) (padding * displaymetrics.density);
	}

	/**
	 * Shortcut for {@link #toBitmap(int, int)} with intrinsic parameters.
	 */
	public Bitmap toBitmap() {
		return toBitmap(getIntrinsicWidth(), getIntrinsicHeight());
	}

	/**
	 * Based on http://stackoverflow.com/a/10600736
	 *
	 * @return bitmap from this drawable.
	 */
	public Bitmap toBitmap(int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		draw(canvas);
		return bitmap;
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		// draw the background for the selected shape
		switch (getShape()) {
			case RECTANGLE:
				canvas.drawColor(getFillColor());
				break;
			case ROUND:
				int radius = getInnerCircleRadius();
				canvas.drawCircle(getCenterX(), getCenterY(), radius, fillPaint);
				break;
		}
	}

	public SHAPE getShape() {
		return shape;
	}

	public int getFillColor() {
		return fillColor;
	}

	int getInnerCircleRadius() {
		int w = getWidth();
		int h = getHeight();
		int radius = Math.min(w, h) / 2;
		if (debug) {
			Log.d(LOG_TAG, "Width: " + w + ", height: " + h);
			Log.d(LOG_TAG, "Radius: " + radius);
		}
		return radius;
	}

	int getCenterX() {
		return getBounds().centerX();
	}

	int getCenterY() {
		return getBounds().centerY();
	}

	int getWidth() {
		return getBounds().width();
	}

	int getHeight() {
		return getBounds().height();

	}

	@Override
	public void setAlpha(int alpha) {
		Log.d(LOG_TAG, "setAlpha(" + alpha + ")");
		foregroundPaint.setAlpha(alpha);
		fillPaint.setAlpha(alpha);
		if (alpha == 255) {
			isAlphaEnabled = false;
		} else {
			isAlphaEnabled = true;
		}
	}

	public Paint foregroundPaint() {
		return foregroundPaint;
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		Log.d(LOG_TAG, "color filter set");
		foregroundPaint.setColorFilter(cf);
		fillPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	Paint getFillPaint() {
		return fillPaint;
	}

	public enum SHAPE {ROUND, RECTANGLE}
}
