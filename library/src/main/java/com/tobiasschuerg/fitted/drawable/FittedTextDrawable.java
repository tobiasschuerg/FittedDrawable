package com.tobiasschuerg.fitted.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Tobias Schürg on 09.05.2016.
 * Based on http://stackoverflow.com/a/8831182
 */
public class FittedTextDrawable extends FittedDrawable {

	private static final String TAG = FittedTextDrawable.class.getSimpleName();
	// Pick a reasonably large value for the test. Larger values produce
	// more accurate results, but may cause problems with hardware
	// acceleration. But there are workarounds for that, too; refer to
	// http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
	private static final float DEFAULT_TEXT_SIZE = 48f;
	@NonNull
	private final String text;

	public FittedTextDrawable(@NonNull String text, int textColor, int backgroundColor, SHAPE shape) {
		super(shape, backgroundColor);
		if (text.isEmpty()) {
			throw new IllegalArgumentException("Text must not be empty");
		}
		this.text = text;
		foregroundPaint().setColor(textColor);
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		super.draw(canvas);

		float textWidth = 0;
		float textHeight = 0;

		switch (getShape()) {
			case ROUND_RECTANGLE:
			case RECTANGLE:
				float textPercentage = 0.75f;
				textWidth = getWidth() * textPercentage;
				textHeight = getHeight() * textPercentage;
				break;
			case ROUND:
				int radius = getInnerCircleRadius();
				textWidth = (float) (Math.sqrt(2 * radius * radius));
				textHeight = textWidth;
				break;
		}

		setTextSizeForWidthHeight(foregroundPaint(), textWidth, textHeight);

		int xPos = getWidth() / 2;
		int yPos = (int) ((getHeight() / 2) - ((foregroundPaint().descent() + foregroundPaint().ascent()) / 2));
		//((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

		canvas.drawText(text, xPos, yPos, foregroundPaint());

		if (drawBorder) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				canvas.drawRoundRect(getClipBounds().left + 1, getClipBounds().top + 1, getClipBounds().right -1, getClipBounds().bottom-1,
						borderRadiusPx, borderRadiusPx, borderPaint);
			} else {
				canvas.drawRect(getClipBounds().left + 1, getClipBounds().top + 1, getClipBounds().right -1, getClipBounds().bottom-1, borderPaint);
			}
		}
	}

	/**
	 * Sets the text size for a Paint object so a given string of text will be a
	 * given width. http://stackoverflow.com/a/21895626/570168
	 *
	 * @param paint        the Paint to set the text size for
	 * @param desiredWidth the desired width
	 */
	private void setTextSizeForWidthHeight(Paint paint, float desiredWidth, float desiredHeight) {
		Rect bounds = getDefaultTextBounds(paint);

		// Calculate the desired size as a proportion of our testTextSize.
		float textSizeWidth = DEFAULT_TEXT_SIZE * desiredWidth / bounds.width();
		float textSizeHeight = DEFAULT_TEXT_SIZE * desiredHeight / bounds.height();

		float textSize = Math.min(textSizeWidth, textSizeHeight);
		Log.d("Fitted text size", "size: " + textSize);

		// Set the paint for that size.
		paint.setTextSize(textSize);
	}

	/**
	 * Get the bounds of the text, using our testTextSize.
	 */
	@NonNull
	private Rect getDefaultTextBounds(Paint paint) {
		paint.setTextSize(DEFAULT_TEXT_SIZE);
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		return bounds;
	}

	@Override
	public int getIntrinsicWidth() {
		int width = getWidth();
		Log.d("FittedTextDrawable", "Width: " + width);
		if (width <= 1) {
			width = Math.max(getDefaultTextBounds().width(), getDefaultTextBounds().height());
		}
		Log.d("FittedTextDrawable", "Width after: " + width);
		return width;
	}

	@Override
	public int getIntrinsicHeight() {
		int height = getHeight();
		Log.d("FittedTextDrawable", "Height: " + height);
		if (height <= 1) {
			height = Math.max(getDefaultTextBounds().width(), getDefaultTextBounds().height());
		}
		Log.d("FittedTextDrawable", "Height after: " + height);
		return height;
	}

	private Rect getDefaultTextBounds() {
		return getDefaultTextBounds(new Paint());
	}
}
