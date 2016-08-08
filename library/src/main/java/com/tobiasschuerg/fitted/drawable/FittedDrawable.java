package com.tobiasschuerg.fitted.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Super class for fitted drawables which fit their content into a predefined shape.
 * <p>
 * Created by Tobias Schürg on 02.08.2016.
 */

public abstract class FittedDrawable extends Drawable {

	protected static final boolean DEBUG = true;

	private final SHAPE shape;
	private final int fillColor;
	private final Paint fillPaint;
	private final Paint foregroundPaint;

	FittedDrawable(SHAPE shape, int backgroundColor) {
		this.shape = shape;
		this.fillColor = backgroundColor;

		fillPaint = new Paint();
		fillPaint.setColor(fillColor);
		fillPaint.setStyle(Paint.Style.FILL);

		foregroundPaint = new Paint();
		foregroundPaint.setAntiAlias(true);
		foregroundPaint.setTextAlign(Paint.Align.CENTER);
		foregroundPaint.setFilterBitmap(true);
		foregroundPaint.setDither(true);
		foregroundPaint.setStyle(Paint.Style.STROKE);
	}

	Paint getFillPaint() {
		return fillPaint;
	}

	public Paint foregroundPaint() {
		return foregroundPaint;
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
		if (DEBUG) {
			Log.d("FittedDrawable", "Width: " + w + ", height: " + h);
			Log.d("FittedDrawable", "Radius: " + radius);
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
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	public enum SHAPE {ROUND, RECTANGLE}

	@Override
	public void setAlpha(int alpha) {
		foregroundPaint().setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		foregroundPaint().setColorFilter(cf);
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

	/**
	 * Shortcut for {@link #toBitmap(int, int)} with intrinsic parameters.
	 */
	public Bitmap toBitmap() {
		return toBitmap(getIntrinsicWidth(), getIntrinsicHeight());
	}
}
