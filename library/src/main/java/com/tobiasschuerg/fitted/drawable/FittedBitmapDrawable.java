package com.tobiasschuerg.fitted.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Fits a bitmap inside a drawable which can bei either a circle or a rectangle and fills the background.
 * Right now the color of the pixel at (1,1) is taken as background color.
 * <p>
 * Created by Tobias Schürg on 28.07.2016.
 */

public class FittedBitmapDrawable extends FittedDrawable {

	private static String LOG_TAG = FittedBitmapDrawable.class.getSimpleName();
	private final Bitmap bitmap;
	private final float aspectRatio;

	@NonNull
	private Shader.TileMode tileMode = Shader.TileMode.CLAMP;

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
		if (debug) {
			Log.d(LOG_TAG, "Ratio: " + aspectRatio);
		}
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

		if (debug) {
			drawBorder = true;
			foregroundPaint().setColor(Color.RED);
		}


//		if (isAlphaEnabled) {
//			// clear background behind image
//			Paint clearPaint = new Paint();
//			clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//			canvas.drawRect(hOff, vOff, hOff + scaledBitmap.getWidth(), vOff + scaledBitmap.getHeight(), clearPaint);
//		}

		RectF targetRect = null;
		Bitmap scaledBitmap = getScaledBitmap(canvas);
		final float horizontalOffset = getCenterX() - scaledBitmap.getWidth() / 2 + getBounds().left;
		final float verticalOffset = getCenterY() - scaledBitmap.getHeight() / 2 + getBounds().top;

		switch (getShape()) {

			case ROUND:
				if (tileMode != null) {
					double borderCenterRadius = Math.floor(getInnerCircleRadius() - 0.5 * borderPaint.getStrokeWidth());
					canvas.drawCircle(getCenterX(), getCenterY(), (float) borderCenterRadius, borderPaint);
					float radius = Math.nextUp(getInnerCircleRadius() - borderPaint.getStrokeWidth());

					RectF inRect = new RectF(
							0,
							0,
							scaledBitmap.getWidth(),
							scaledBitmap.getHeight());

					RectF outRect = new RectF(
							getBounds().left + getAdditionalPaddingPX(),
							getBounds().top + getAdditionalPaddingPX(),
							getBounds().right - getAdditionalPaddingPX(),
							getBounds().bottom - getAdditionalPaddingPX()
					);


					Paint shaderPaint = createShaderPaint(scaledBitmap, inRect, outRect, tileMode);

					canvas.drawCircle(getCenterX(), getCenterY(), radius, shaderPaint);
				} else {
					canvas.drawBitmap(scaledBitmap, horizontalOffset, verticalOffset, foregroundPaint());
				}
				break;

			case ROUND_RECTANGLE:
				if (tileMode != null) {
					RectF sourceRect = new RectF(0f, 0f, scaledBitmap.getWidth(), scaledBitmap.getHeight());
					targetRect = new RectF(
							getClipBounds().left + getAdditionalPaddingPX(),
							getClipBounds().top + getAdditionalPaddingPX(),
							getClipBounds().right - getAdditionalPaddingPX(),
							getClipBounds().bottom - getAdditionalPaddingPX());

					Paint sp = createShaderPaint(scaledBitmap, sourceRect, targetRect, tileMode);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						if (drawBorder) {
							canvas.drawRoundRect(getClipBounds().left + 1, getClipBounds().top + 1, getClipBounds().right - 1, getClipBounds().bottom - 1, getCornerRadiusPx(), getCornerRadiusPx(), sp);
						} else {
							canvas.drawRoundRect(new RectF(canvas.getClipBounds()), getCornerRadiusPx(), getCornerRadiusPx(), sp);
						}
					} else {
						canvas.drawRect(sourceRect, sp);
					}
				} else {
					throw new IllegalArgumentException("Tile mode not set");
				}

				if (drawBorder) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						canvas.drawRoundRect(getClipBounds().left + 1, getClipBounds().top + 1, getClipBounds().right - 1, getClipBounds().bottom - 1,
								getCornerRadiusPx(), getCornerRadiusPx(), borderPaint);
					} else {
						canvas.drawRect(getClipBounds().left + 1, getClipBounds().top + 1, getClipBounds().right - 1, getClipBounds().bottom - 1, borderPaint);
					}
				}
				break;

			case RECTANGLE:
				// Simple, should result in the same as #ROUND_RECTANGLE with #setBorderRadius(0)
				canvas.drawColor(getFillColor());
				canvas.drawBitmap(scaledBitmap, horizontalOffset, verticalOffset, foregroundPaint());
				if (drawBorder) {
					float borderWidth = borderPaint.getStrokeWidth();
					canvas.drawRect(
							getBounds().left + borderWidth,
							getBounds().top + borderWidth,
							getBounds().right - borderWidth,
							getBounds().bottom - borderWidth,
							borderPaint);
				}
				break;
		}


		if (debug) {
			if (targetRect != null) {
				Log.d(LOG_TAG, "Green: bitmap border");
				debugPaint.setColor(Color.GREEN);
				debugPaint.setStyle(Paint.Style.STROKE);
				canvas.drawRect(targetRect, debugPaint);
			}

			Log.d(LOG_TAG, "Yellow: intrinsic border");
			debugPaint.setColor(Color.YELLOW);
			canvas.drawRect(0, 0, getIntrinsicWidth(), getIntrinsicHeight(), debugPaint);

			Log.d(LOG_TAG, "RED: outer border");
			debugPaint.setColor(Color.RED);
			canvas.drawRect(getClipBounds(), debugPaint);

			Log.d(LOG_TAG, "BLUE: bounds");
			debugPaint.setColor(Color.BLUE);
			canvas.drawRect(getBounds(), debugPaint);
		}
	}

	private static Paint createShaderPaint(Bitmap bm, RectF bitmapBoudns, RectF targetBounds, Shader.TileMode tileMode) {
		Matrix shaderMatrix = new Matrix();
		shaderMatrix.setRectToRect(bitmapBoudns, targetBounds, Matrix.ScaleToFit.CENTER);

		BitmapShader shader = new BitmapShader(bm, tileMode, tileMode);
		shader.setLocalMatrix(shaderMatrix);

		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setDither(true);
		p.setShader(shader);
		return p;
	}

	/**
	 * Looks like magic but is only pythagoras.
	 *
	 * @param radius radius of the circle.
	 * @return fitted bitmap
	 */
	private Bitmap fitBitmapInCircle(float radius) {
		Double root = Math.sqrt(aspectRatio * aspectRatio + 1f);

		Float bmWidth = (2 * radius) / root.floatValue();
		Float bmHeight = bmWidth * aspectRatio;

		if (debug) {
			Log.d(LOG_TAG, "Scaled round width: " + bmWidth);
			Log.d(LOG_TAG, "Scaled round height: " + bmHeight);
		}

		return Bitmap.createScaledBitmap(bitmap, bmWidth.intValue(), bmHeight.intValue(), true);
	}

	private Bitmap fitBitmapInRectangle(int width, int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("width(" + width + ") and height(" + height + ") must be > 0");
		}
		if (debug) {
			Log.d(LOG_TAG, "Rectangle scale to width: " + width);
			Log.d(LOG_TAG, "Rectangle scale to height: " + height);
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
			Log.d(LOG_TAG, "Scaled rectangle width: " + scaleWidth);
			Log.d(LOG_TAG, "Scaled rectangle height: " + scaleHeight);
		}

		scaledBm = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, true);
		return scaledBm;
	}

	@Override
	public int getIntrinsicHeight() {
		switch (getShape()) {
			case ROUND:
				return Math.max(bitmap.getHeight(), bitmap.getWidth());

			case ROUND_RECTANGLE:
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

			case ROUND_RECTANGLE:
			case RECTANGLE:
				return bitmap.getWidth();

			default:
				throw new IllegalStateException();
		}
	}

	public void setTileMode(@NonNull Shader.TileMode tileMode) {
		this.tileMode = tileMode;
	}

	private Bitmap getScaledBitmap(Canvas canvas) {
		Bitmap scaledBitmap;
		switch (getShape()) {

			case ROUND:
				float radius = getInnerCircleRadius() - borderPaint.getStrokeWidth();
				scaledBitmap = fitBitmapInCircle(radius - getAdditionalPaddingPX());
				break;

			case ROUND_RECTANGLE:
			case RECTANGLE:

				if (getWidth() > getHeight()) {
					final int adjustedWidth = (int) (getWidth() - (2 * getAdditionalPaddingPX()));
					scaledBitmap = fitBitmapInRectangle(adjustedWidth, Math.min(getHeight(), adjustedWidth));
				} else {
					final int adjustedHeight = (int) (getHeight() - (2 * getAdditionalPaddingPX()));
					scaledBitmap = fitBitmapInRectangle(Math.min(getWidth(), adjustedHeight), adjustedHeight);
				}
				break;

			default:
				throw new IllegalStateException();
		}
		return scaledBitmap;
	}
}
