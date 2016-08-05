package com.tobiasschuerg.fitted.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Tobias Schürg on 09.05.2016.
 * Based on http://stackoverflow.com/a/8831182
 */
public class FittedTextDrawable extends FittedDrawable {

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

		setTextSizeForWidthHeight(foregroundPaint(), textWidth, textHeight, text);

		int xPos = getWidth() / 2;
		int yPos = (int) ((getHeight() / 2) - ((foregroundPaint().descent() + foregroundPaint().ascent()) / 2));
		//((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

		canvas.drawText(text, xPos, yPos, foregroundPaint());
	}

	/**
	 * Based on http://stackoverflow.com/a/10600736
	 */
	public Bitmap toBitmap() {
		Bitmap bitmap;

		if (getIntrinsicWidth() <= 0 || getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
		} else {
			bitmap = Bitmap.createBitmap(getIntrinsicWidth(), getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(bitmap);
		setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		draw(canvas);
		return bitmap;
	}

	/**
	 * Sets the text size for a Paint object so a given string of text will be a
	 * given width. http://stackoverflow.com/a/21895626/570168
	 *
	 * @param paint        the Paint to set the text size for
	 * @param desiredWidth the desired width
	 * @param text         the text that should be that width
	 */
	private static void setTextSizeForWidthHeight(Paint paint, float desiredWidth, float desiredHeight, String text) {

		// Pick a reasonably large value for the test. Larger values produce
		// more accurate results, but may cause problems with hardware
		// acceleration. But there are workarounds for that, too; refer to
		// http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
		final float testTextSize = 48f;

		// Get the bounds of the text, using our testTextSize.
		paint.setTextSize(testTextSize);
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);

		// Calculate the desired size as a proportion of our testTextSize.
		float textSizeWidth = testTextSize * desiredWidth / bounds.width();
		float textSizeHeight = testTextSize * desiredHeight / bounds.height();

		float textSize = Math.min(textSizeWidth, textSizeHeight);
		Log.d("Fitted text size", "size: " + textSize);

		// Set the paint for that size.
		paint.setTextSize(textSize);
	}
}
