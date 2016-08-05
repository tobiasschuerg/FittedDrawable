package com.tobiasschuerg.fitted.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Fits a bitmap inside a drawable which can bei either a circle or a rectangle and fills the background.
 * Right now the color of the pixel at (1,1) is taken as background color.
 * <p>
 * Created by Tobias Schürg on 28.07.2016.
 */

public class FittedBitmapDrawable extends FittedDrawable {

	private static final boolean DEBUG = false;
	private static final String TAG = FittedBitmapDrawable.class.getSimpleName();
	private final Bitmap bitmap;
	private final float aspectRatio;
	private boolean drawBorder = false;

	public FittedBitmapDrawable(Context context, int resource, SHAPE SHAPE) {
		this(BitmapFactory.decodeResource(context.getResources(), resource), SHAPE);
	}

	public FittedBitmapDrawable(Bitmap bitmap, SHAPE SHAPE) {
		this(bitmap, SHAPE, getFillColorFromBitmap(bitmap));
	}

	private static int getFillColorFromBitmap(Bitmap bitmap) {
		int tl = bitmap.getPixel(1, 1);
		int tr = bitmap.getPixel(1, bitmap.getHeight() - 1);
		int bl = bitmap.getPixel(bitmap.getWidth() - 1, 1);
		int br = bitmap.getPixel(bitmap.getWidth() - 1, bitmap.getHeight() - 1);
		if (tl == tr && tl == bl && tl == br) {
			return tl;
		} else {
			Log.e("FillColor", "Bitmap has no monochrome border!");
			if (DEBUG) {
				throw new IllegalArgumentException();
			} else {
				return tl; // (tl + tr + bl + br) / 4;
			}
		}
	}

	public FittedBitmapDrawable(Bitmap bitmap, SHAPE shape, int color) {
		super(shape, color);
		this.bitmap = bitmap;
		aspectRatio = (float) bitmap.getHeight() / bitmap.getWidth();
		Log.d(TAG, "Ratio: " + aspectRatio);
	}

	public FittedBitmapDrawable(Context context, int resource, SHAPE SHAPE, int color) {
		this(BitmapFactory.decodeResource(context.getResources(), resource), SHAPE, color);
	}

	public void setDrawBorder(boolean drawBorder) {
		this.drawBorder = drawBorder;
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		super.draw(canvas);

		int radius = getInnerCircleRadius();
		int cx = getCenterX();
		int cy = getCenterY();

		if (DEBUG) {
			drawBorder = true;
			foregroundPaint().setColor(Color.RED);
		}

//		if (drawBorder) {
//			switch (getShape()) {
//				case ROUND:
//					canvas.drawCircle(cx, cy, radius, paint);
//					break;
//				case RECTANGLE:
//					canvas.drawColor(getFillColor());
//					break;
//			}
//		}

		Bitmap scaledBitmap;
		switch (getShape()) {
			case ROUND:
				canvas.drawCircle(cx, cy, --radius, getFillPaint());
				scaledBitmap = fitBitmapInCircle(radius);
				break;
			case RECTANGLE:
				scaledBitmap = fitBitmapInRectangle(getWidth(), getHeight());
				break;
			default:
				// must not happen
				return;
		}

		float hOff = cx - scaledBitmap.getWidth() / 2;
		float vOff = cy - scaledBitmap.getHeight() / 2;
		canvas.drawBitmap(scaledBitmap, hOff, vOff, foregroundPaint());

		if (DEBUG || (getShape() == SHAPE.RECTANGLE && drawBorder)) {
			foregroundPaint().setColor(Color.RED);
			foregroundPaint().setStyle(Paint.Style.STROKE);
			canvas.drawRect(hOff, vOff, hOff + scaledBitmap.getWidth(), vOff + scaledBitmap.getHeight(), foregroundPaint());
		}
	}

	/**
	 * Looks like magic but is only pythagoras.
	 *
	 * @param radius radius of the circle.
	 * @return fitted bitmap
	 */
	private Bitmap fitBitmapInCircle(int radius) {
		Bitmap scaledBm;
		//if (aspectRatio < 0) {

		Double root = Math.sqrt(aspectRatio * aspectRatio + 1f);
		Log.d(TAG, "Root: " + root);

		Float bmWidth = (2 * radius) / root.floatValue();
		Float bmHeigth = bmWidth * aspectRatio;

		Log.d(TAG, "Radius * 2: " + 2 * radius);
		Log.d(TAG, "Width: " + bmWidth);
		Log.d(TAG, "Heigth: " + bmHeigth);

		scaledBm = Bitmap.createScaledBitmap(bitmap, bmWidth.intValue(), bmHeigth.intValue(), true);
		return scaledBm;
	}

	private Bitmap fitBitmapInRectangle(int width, int height) {
		Bitmap scaledBm;
		int scaleWidth;
		int scaleHeight;
		float aspectRatioCanvas = (float) height / width;
		if (aspectRatioCanvas > aspectRatio) {
			scaleWidth = width;
			scaleHeight = Math.round(width * aspectRatio);
		} else {
			scaleHeight = height;
			scaleWidth = Math.round(height / aspectRatio);
		}

		scaledBm = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, true);
		return scaledBm;
	}

}
