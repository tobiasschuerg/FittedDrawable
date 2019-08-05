package com.tobiasschuerg.fitted.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

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

    public FittedTextDrawable(@NonNull String text, int textColor, int backgroundColor, DrawableShape shape) {
        super(shape, backgroundColor);
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Text must not be empty");
        }
        this.text = text;
        getForegroundPaint().setColor(textColor);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        // As long as the drawable is not drawn, getBounds of this drawable might be 0.
        // Using the canvas sizes as fallback
        int canvasWidth = canvas.getClipBounds().width();
        int canvasHeight = canvas.getClipBounds().height();

        float textWidth = 0;
        float textHeight = 0;

        switch (getShape()) {
            case ROUND_RECTANGLE:
            case RECTANGLE:
                float textPercentage = 0.75f;
                textWidth = getWidth(canvasWidth) * textPercentage;
                textHeight = getHeight(canvasHeight) * textPercentage;
                break;
            case ROUND:
                float radius = getInnerCircleRadius();
                float sqrt = (float) (Math.sqrt(2f * radius * radius));
                textWidth = sqrt;
                textHeight = sqrt;
                canvas.drawCircle(getCenterX(), getCenterY(), radius, getFillPaint());
                break;
        }

        setTextSizeForWidthHeight(getForegroundPaint(), textWidth, textHeight);

        int xPos = getWidth(canvasWidth) / 2;
        int yPos = (int) ((getHeight(canvasHeight) / 2) - ((getForegroundPaint().descent() + getForegroundPaint().ascent()) / 2));
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

        if (getDebug()) {
            Log.d(TAG, "Drawing text at " + xPos + ", " + yPos);
        }
        canvas.drawText(text, xPos, yPos, getForegroundPaint());

        drawBorder(canvas);
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
        if (getDebug()) {
            Log.d("Fitted text size", "size: " + textSize);
        }

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

}
