package com.tobiasschuerg.fitted.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Fits a bitmap inside a drawable which can bei either a circle or a rectangle and fills the background.
 * Right now the color of the pixel at (1,1) is taken as background color.
 * <p>
 * Created by Tobias Schürg on 28.07.2016.
 */

public class FittedBitmapDrawable extends FittedDrawable {

	private static final String TAG = FittedBitmapDrawable.class.getSimpleName();

	private final Bitmap bitmap;
	private final float aspectRatio;


	@Nullable
	private Shader.TileMode tileMode;

	public FittedBitmapDrawable(Context context, int resource, SHAPE SHAPE) {
		this(BitmapFactory.decodeResource(context.getResources(), resource), SHAPE);
	}

	public FittedBitmapDrawable(Bitmap bitmap, SHAPE SHAPE) {
		this(bitmap, SHAPE, getFillColorFromBitmap(bitmap));
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

	private static int getFillColorFromBitmap(Bitmap bitmap) {
		int tl = bitmap.getPixel(1, 1);
		int tr = bitmap.getPixel(1, bitmap.getHeight() - 1);
		int bl = bitmap.getPixel(bitmap.getWidth() - 1, 1);
		int br = bitmap.getPixel(bitmap.getWidth() - 1, bitmap.getHeight() - 1);
		if (tl == tr && tl == bl && tl == br) {
			return tl;
		} else {
			Log.w("FillColor", "Bitmap has no monochrome border! Taking (1, 1)");
			return tl; // (tl + tr + bl + br) / 4;
		}
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		super.draw(canvas);

		final RectF clipBounds = new RectF(canvas.getClipBounds());
		int radius = getInnerCircleRadius();
		int cx = getCenterX();
		int cy = getCenterY();

		if (debug) {
			drawBorder = true;
			foregroundPaint().setColor(Color.RED);
		}

		if (drawBorder) {
			switch (getShape()) {
				case ROUND:
					getFillPaint().setColor(Color.LTGRAY);
					canvas.drawCircle(cx, cy, radius, getFillPaint());
					getFillPaint().setColor(getFillColor());
					break;
				case RECTANGLE:
					// not needed:
					// canvas.drawColor(getFillColor());
					break;
			}
		}

		Bitmap scaledBitmap;
		switch (getShape()) {
			case ROUND:
				canvas.drawCircle(cx, cy, --radius, getFillPaint());
				scaledBitmap = fitBitmapInCircle(radius - getAdditionalPaddingPX());
				break;
			case RECTANGLE:
				if (getWidth() > getHeight()) {
					scaledBitmap = fitBitmapInRectangle(getWidth() - (2 * getAdditionalPaddingPX()), getHeight());
				} else {
					scaledBitmap = fitBitmapInRectangle(getWidth(), getHeight() - (2 * getAdditionalPaddingPX()));
				}
				break;
			default:
				// must not happen
				return;
		}

		float hOff = cx - scaledBitmap.getWidth() / 2;
		float vOff = cy - scaledBitmap.getHeight() / 2;

		if (isAlphaEnabled) {
			// clear background behind image
			Paint clearPaint = new Paint();
			clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			canvas.drawRect(hOff, vOff, hOff + scaledBitmap.getWidth(), vOff + scaledBitmap.getHeight(), clearPaint);
		}


		switch (getShape()) {

			case ROUND:
				if (tileMode != null) {
					int offLeft = radius - (scaledBitmap.getWidth() / 2) + getAdditionalPaddingPX();
					int offTop = radius - (scaledBitmap.getHeight() / 2) + getAdditionalPaddingPX();

					RectF inRect = new RectF(0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());
					RectF outRect = new RectF(
							offLeft, offTop,
							radius * 2 - offLeft,
							radius * 2 - offTop);

					Paint shaderPaint = getShaderPaint(scaledBitmap, inRect, outRect);
					canvas.drawCircle(cx, cy, radius, shaderPaint);
				} else {
					canvas.drawBitmap(scaledBitmap, hOff, vOff, foregroundPaint());
				}
				break;

			case RECTANGLE:

				if (tileMode != null) {

					canvas.drawColor(Color.WHITE);

					RectF sourceRect = new RectF(0f, 0f, getWidth(), getHeight());
					RectF targetRect = new RectF(
							getAdditionalPaddingPX(),
							getAdditionalPaddingPX(),
							getWidth() - getAdditionalPaddingPX(),
							getHeight() - getAdditionalPaddingPX());

					Paint sp = getShaderPaint(scaledBitmap, sourceRect, targetRect);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						canvas.drawRoundRect(clipBounds, borderRadiusPx, borderRadiusPx, sp);
					} else {
						canvas.drawRect(sourceRect, sp);
					}
				} else {
					canvas.drawColor(getFillColor());
					canvas.drawBitmap(scaledBitmap, hOff, vOff, foregroundPaint());
				}

				if (drawBorder) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						canvas.drawRoundRect(clipBounds, borderRadiusPx, borderRadiusPx, borderPaint);
					} else {
						canvas.drawRect(clipBounds, borderPaint);
					}
				}
				break;
		}


		if (debug && (getShape() == SHAPE.RECTANGLE)) {
			Log.d(TAG, "Green: bitmap border");
			debugPaint.setColor(Color.GREEN);
			debugPaint.setStyle(Paint.Style.STROKE);
			canvas.drawRect(getAdditionalPaddingPX(), getAdditionalPaddingPX(),
					scaledBitmap.getWidth(),
					scaledBitmap.getHeight(), debugPaint);

			Log.d(TAG, "Yellow: intrinsic border");
			debugPaint.setColor(Color.YELLOW);
			canvas.drawRect(0, 0, getIntrinsicWidth(), getIntrinsicHeight(), debugPaint);

			Log.d(TAG, "RED: outer border");
			debugPaint.setColor(Color.RED);
			canvas.drawRect(clipBounds, debugPaint);
		}
	}

	private Paint getShaderPaint(Bitmap bm, RectF bitmapBoudns, RectF targetBounds) {
		Matrix shaderMatrix = new Matrix();
		shaderMatrix.setRectToRect(bitmapBoudns, targetBounds, Matrix.ScaleToFit.CENTER);

		BitmapShader shader = new BitmapShader(bm, tileMode, tileMode);
		shader.setLocalMatrix(shaderMatrix);

		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setShader(shader);

		return p;
	}

	/**
	 * Looks like magic but is only pythagoras.
	 *
	 * @param radius radius of the circle.
	 * @return fitted bitmap
	 */
	private Bitmap fitBitmapInCircle(int radius) {
		Double root = Math.sqrt(aspectRatio * aspectRatio + 1f);

		Float bmWidth = (2 * radius) / root.floatValue();
		Float bmHeight = bmWidth * aspectRatio;

		return Bitmap.createScaledBitmap(bitmap, bmWidth.intValue(), bmHeight.intValue(), true);
	}

	private Bitmap fitBitmapInRectangle(int width, int height) {
		if (debug) {
			Log.d(TAG, "Rectangle width: " + width);
			Log.d(TAG, "Rectangle height: " + height);
		}
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

		if (debug) {
			Log.d(TAG, "Scaled rectangle width: " + scaleWidth);
			Log.d(TAG, "Scaled rectangle height: " + scaleHeight);
		}

		scaledBm = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, true);
		return scaledBm;
	}

	@Override
	public int getIntrinsicHeight() {
		switch (getShape()) {
			case ROUND:
				return Math.max(bitmap.getHeight(), bitmap.getWidth());

			case RECTANGLE:
				return bitmap.getHeight();

			default:
				throw new IllegalStateException();
		}
	}

	@Override
	public int getIntrinsicWidth() {
		switch (getShape()) {
			case ROUND:
				return Math.max(bitmap.getHeight(), bitmap.getWidth());

			case RECTANGLE:
				return bitmap.getWidth();

			default:
				throw new IllegalStateException();
		}
	}

	public void setTileMode(@Nullable Shader.TileMode tileMode) {
		this.tileMode = tileMode;
	}
}
