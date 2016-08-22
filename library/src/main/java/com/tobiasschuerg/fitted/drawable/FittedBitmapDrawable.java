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

		Bitmap scaledBitmap = getScaledBitmap(canvas);
		final float horizontalOffset = getCenterX() - scaledBitmap.getWidth() / 2 + getBounds().left;
		final float verticalOffset = getCenterY() - scaledBitmap.getHeight() / 2 + getBounds().top;

		RectF inRect = new RectF(
				0,
				0,
				scaledBitmap.getWidth(),
				scaledBitmap.getHeight());

		RectF outRect = new RectF(
				getBounds().centerX() - scaledBitmap.getWidth() / 2f,
				getBounds().centerY() - scaledBitmap.getHeight() / 2f,
				getBounds().centerX() + scaledBitmap.getWidth() / 2f,
				getBounds().centerY() + scaledBitmap.getHeight() / 2f
		);

		switch (getShape()) {

			case ROUND:
				if (tileMode != null) {
					double borderCenterRadius = Math.floor(getInnerCircleRadius() - 0.5 * borderPaint.getStrokeWidth());
					canvas.drawCircle(getCenterX(), getCenterY(), (float) borderCenterRadius, borderPaint);
					float radius = Math.nextUp(getInnerCircleRadius() - borderPaint.getStrokeWidth());

					Paint shaderPaint = createShaderPaint(scaledBitmap, inRect, outRect, tileMode);

					canvas.drawCircle(getCenterX(), getCenterY(), radius, getFillPaint()); // needed if bitmap has alpha
					canvas.drawCircle(getCenterX(), getCenterY(), radius, shaderPaint);
				} else {
					canvas.drawBitmap(scaledBitmap, horizontalOffset, verticalOffset, foregroundPaint());
				}
				break;

			case ROUND_RECTANGLE:

				// Draw the bitmap with the right size
				if (tileMode != null) {
					Paint sp = createShaderPaint(scaledBitmap, inRect, outRect, tileMode);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						canvas.drawRoundRect(outRect,
								getCornerRadiusPx(),
								getCornerRadiusPx(), sp);
					} else {
						canvas.drawRect(outRect, sp);
					}
				} else {
					throw new IllegalArgumentException("Tile mode not set");
				}

				// draw border if wanted
				if (drawBorder) {
					float halfBoderWidth = borderPaint.getStrokeWidth() / 2;
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						canvas.drawRoundRect(
								getBounds().left + halfBoderWidth,
								getBounds().top + halfBoderWidth,
								getBounds().right - halfBoderWidth,
								getBounds().bottom - halfBoderWidth,
								getCornerRadiusPx(), getCornerRadiusPx(), borderPaint);
					} else {
						canvas.drawRect(
								getBounds().left + halfBoderWidth,
								getBounds().top + halfBoderWidth,
								getBounds().right - halfBoderWidth,
								getBounds().bottom - halfBoderWidth,
								borderPaint);
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
			if (outRect != null) {
				Log.d(LOG_TAG, "Green: bitmap border");
				debugPaint.setColor(Color.GREEN);
				debugPaint.setStyle(Paint.Style.STROKE);
				canvas.drawRect(outRect, debugPaint);
			}

			Log.d(LOG_TAG, "Yellow: intrinsic border");
			debugPaint.setColor(Color.YELLOW);
			canvas.drawRect(0, 0, getIntrinsicWidth(), getIntrinsicHeight(), debugPaint);

			//Log.d(LOG_TAG, "RED: outer border");
			//debugPaint.setColor(Color.RED);
			//canvas.drawRect(getClipBounds(), debugPaint);

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

	private static Bitmap fitBitmapInRectangle(int destWidth, int destHeight, Bitmap bitmap, boolean debug) {

		debug = true;

		if (destWidth <= 0 || destHeight <= 0) {
			throw new IllegalArgumentException("width(" + destWidth + ") and height(" + destHeight + ") must be > 0");
		}


		final float widthRatio = (float) destWidth / bitmap.getWidth();
		final float heightRatio = (float) destHeight / bitmap.getHeight();
		float scale = Math.min(widthRatio, heightRatio);

		if (debug) {
			Log.d(LOG_TAG, "Width ratio: " + widthRatio + ", height ratio: " + heightRatio + ", scale: " + scale);
		}

		Bitmap scaledBm = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale), (int) (bitmap.getHeight() * scale), true);
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

				final int adjustedWidth = (int) ((getWidth() - (2 * getAdditionalPaddingPX())));
				final int adjustedHeight = (int) (getHeight() - (2 * getAdditionalPaddingPX()));
				scaledBitmap = fitBitmapInRectangle(adjustedWidth, adjustedHeight, bitmap, debug);
				break;

			default:
				throw new IllegalStateException();
		}
		return scaledBitmap;
	}
}
