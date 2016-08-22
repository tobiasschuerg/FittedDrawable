package com.tobiasschuerg.fitted.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

/**
 * Super class for fitted drawables which fit their content into a predefined shape.
 * <p>
 * Created by Tobias Schürg on 02.08.2016.
 */

public abstract class FittedDrawable extends Drawable {

	private static String LOG_TAG = FittedDrawable.class.getSimpleName();
	final Paint debugPaint;
	protected final Paint borderPaint;
	private final DisplayMetrics displaymetrics;
	public boolean debug = false;
	private final SHAPE shape;
	private final int fillColor;
	private final Paint fillPaint;
	private final Paint foregroundPaint;
	boolean isAlphaEnabled;
	private float additionalPaddingPX = 0;
	private float cornerRadiusPx = 0;

	public void setBorderWidthPx(int borderWidth) {
		borderPaint.setStrokeWidth(borderWidth);
	}

	public void setBorderWidthDp(int dp) {
		setBorderWidthPx((int) (dp * Resources.getSystem().getDisplayMetrics().density));
	}

	FittedDrawable(SHAPE shape, int backgroundColor) {
		displaymetrics = Resources.getSystem().getDisplayMetrics();
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
		foregroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);

		debugPaint = new Paint();
		debugPaint.setColor(Color.CYAN);
		debugPaint.setStyle(Paint.Style.STROKE);
		debugPaint.setAntiAlias(true);
		debugPaint.setStrokeWidth(8);

		// TODO: set when border is actually set as enabled
		borderPaint = new Paint();
		setBorderColor(Color.LTGRAY);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setAntiAlias(true);
		borderPaint.setDither(true);
	}

	public void setBorderColor(int color) {
		borderPaint.setColor(color);
		setDrawBorder(true);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public float getAdditionalPaddingPX() {
		if (debug) {
			Log.d("FittedDrawable", "InnerPadding is " + additionalPaddingPX);
		}
		return additionalPaddingPX + borderPaint.getStrokeWidth();
	}

	/**
	 * Expects dp!
	 *
	 * @param padding padding in dp
	 */
	public void setAdditionalPaddingDp(int padding) {
		additionalPaddingPX = padding * Resources.getSystem().getDisplayMetrics().density;
		if (debug) {
			Log.d("FittedDrawable", "Set additionalPadding " + padding + "dp -> " + additionalPaddingPX + "px");
		}
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

		if (debug) {
			Log.d(LOG_TAG, "Going to draw " + getShape().name());
			Log.d(LOG_TAG, "-- INITIAL canvas width: " + canvas.getWidth() + " height: " + canvas.getHeight());
			// Log.d(LOG_TAG, "-- INITIAL bounds width: " + getBounds().width() + " height: " + getBounds().height());
		}

		// draw the background for the selected shape
		switch (getShape()) {
			case RECTANGLE:
				canvas.drawColor(getFillColor());
				break;
			case ROUND_RECTANGLE:
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					float borderWidth = borderPaint.getStrokeWidth() * 0.8f;
					canvas.drawRoundRect(
							getBounds().left + borderWidth,
							getBounds().top + borderWidth,
							getBounds().right - borderWidth,
							getBounds().bottom - borderWidth,
							getCornerRadiusPx(), getCornerRadiusPx(), getFillPaint());
				} else {
					canvas.drawRect(getBounds(), getFillPaint());
				}
				break;
		}
	}

	public SHAPE getShape() {
		return shape;
	}

	public int getFillColor() {
		return fillColor;
	}

	public void setCornerRadiusDp(float radius) {
		cornerRadiusPx = radius * displaymetrics.density;
	}

	public void setCornerRadiusPx(float radius) {
		cornerRadiusPx = radius;
	}

	int getInnerCircleRadius() {
		int w = getBounds().width();
		int h = getBounds().height();
		int radius = Math.min(w, h) / 2;
		if (debug) {
			Log.d(LOG_TAG, "Radius is " + radius + " for width: " + w + " and height: " + h);
		}
		return radius;
	}

	int getCenterX() {
		// return getWidth() / 2;
		return getBounds().centerX();
	}

	int getCenterY() {
		// return getHeight() / 2;
		return getBounds().centerY();
	}

	protected int getWidth() {
		int width = getBounds().width();
		if (width <= 0) {
			throw new IllegalStateException("width is " + width + ". Call setBounds() first!");
		}
		return width;
	}

	protected int getHeight() {
		int heigth = getBounds().height();
		if (heigth <= 0) {
			throw new IllegalStateException("width is " + heigth + ". Call setBounds() first!");
		}
		return heigth;

	}

	@Override
	public void setAlpha(int alpha) {
		Log.d(LOG_TAG, "setAlpha(" + alpha + ")");
		foregroundPaint.setAlpha(alpha);
		fillPaint.setAlpha(alpha);
		isAlphaEnabled = alpha != 255;
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

	public Paint foregroundPaint() {
		return foregroundPaint;
	}

	Paint getFillPaint() {
		return fillPaint;
	}

	public enum SHAPE {ROUND, ROUND_RECTANGLE, RECTANGLE}

	protected boolean drawBorder = false;

	public void setDrawBorder(boolean drawBorder) {
		this.drawBorder = drawBorder;
	}

	public float getCornerRadiusPx() {
		return cornerRadiusPx;
	}

}
