package com.tobiasschuerg.fitted.drawable

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.nextUp

/**
 * Super class for fitted drawables which fit their content into a predefined shape.
 *
 *
 * Created by Tobias Schürg on 02.08.2016.
 */

internal abstract class FittedDrawable internal constructor(
        val shape: DrawableShape,
        val fillColor: Int
) : Drawable() {
    private val displayMetrics: DisplayMetrics by lazy { Resources.getSystem().displayMetrics }

    val borderPaint: Paint
    val debugPaint: Paint

    val fillPaint: Paint = Paint().apply {
        color = fillColor
        style = Paint.Style.FILL
        isAntiAlias = true
        isDither = true
    }

    val foregroundPaint: Paint = Paint()
    var debug = false
    var drawBorder = false
    private var longSidePaddingPx = 0f
    var cornerRadiusPx = 0f

    // return getWidth() / 2;
    val centerX: Int
        get() = bounds.centerX()

    // return getHeight() / 2;
    val centerY: Int
        get() = bounds.centerY()

    val innerCircleRadius: Float
        get() {
            val w = bounds.width()
            val h = bounds.height()
            // +1 to make sure we round up, not down!
            val radius = ((min(w, h)) / 2f)
            if (debug) {
                Log.d(LOG_TAG, "Radius is $radius for width: $w and height: $h")
            }
            return radius
        }

    init {


        foregroundPaint.isAntiAlias = true
        foregroundPaint.textAlign = Paint.Align.CENTER
        foregroundPaint.isFilterBitmap = true
        foregroundPaint.isDither = true
        foregroundPaint.style = Paint.Style.FILL_AND_STROKE

        debugPaint = Paint()
        debugPaint.color = Color.CYAN
        debugPaint.style = Paint.Style.STROKE
        debugPaint.isAntiAlias = true
        debugPaint.strokeWidth = 8f

        // TODO: set when border is actually set as enabled
        borderPaint = Paint()
        setBorderColor(Color.LTGRAY)
        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        borderPaint.isDither = true
    }

    fun setBorderColor(color: Int) {
        borderPaint.color = color
    }

    fun setBorderWidthDp(dp: Int) {
        setBorderWidthPx((dp * Resources.getSystem().displayMetrics.density).toInt())
    }

    fun setBorderWidthPx(borderWidth: Int) {
        borderPaint.strokeWidth = borderWidth.toFloat()
        drawBorder = borderWidth > 0
    }

    fun getLongSidePaddingPx(): Float {
        if (debug) {
            Log.d("FittedDrawable", "InnerPadding is $longSidePaddingPx")
        }
        return longSidePaddingPx + borderPaint.strokeWidth
    }

    /**
     * Padding to apply to both sides of the longer side.
     *
     *
     * Mst not be greater 2 * height.
     *
     * @param padding padding in DP(!)
     */
    fun setLongSidePaddingDp(padding: Int) {
        longSidePaddingPx = padding * Resources.getSystem().displayMetrics.density
        if (debug) {
            Log.d("FittedDrawable", "Set additionalPadding " + padding + "dp -> " + longSidePaddingPx + "px")
        }
    }

    /**
     * Based on http://stackoverflow.com/a/10600736
     *
     * @param width  desired with of returned bitmap
     * @param height desired height of returned bitmap
     * @return bitmap from this drawable.
     */
    @JvmOverloads
    fun toBitmap(width: Int = intrinsicWidth, height: Int = intrinsicHeight): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        return bitmap
    }

    override fun draw(canvas: Canvas) {

        if (debug) {
            Log.d(LOG_TAG, "Going to draw " + shape.name)
            Log.d(LOG_TAG, "-- INITIAL bounds width: " + bounds.width() + " height: " + bounds.height())
        }

        // draw the background for the selected shape
        when (shape) {
            DrawableShape.RECTANGLE -> canvas.drawColor(fillColor)
            DrawableShape.ROUND_RECTANGLE -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val borderWidth = borderPaint.strokeWidth * 0.8f
                canvas.drawRoundRect(
                        bounds.left + borderWidth,
                        bounds.top + borderWidth,
                        bounds.right - borderWidth,
                        bounds.bottom - borderWidth,
                        cornerRadiusPx, cornerRadiusPx, fillPaint)
            } else {
                canvas.drawRect(bounds, fillPaint)
            }
            DrawableShape.ROUND -> {
                // TODO
            }
        }
    }

    fun drawBorder(canvas: Canvas) {
        if (!drawBorder) return

        val halfBorderWidth = 0.5f * borderPaint.strokeWidth

        when (shape) {
            DrawableShape.ROUND -> {
                val radius = innerCircleRadius.nextUp()
                val borderCenterRadius = ceil(radius - halfBorderWidth)
                canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), borderCenterRadius, borderPaint)
            }
            DrawableShape.ROUND_RECTANGLE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawRoundRect(
                            bounds.left + halfBorderWidth,
                            bounds.top + halfBorderWidth,
                            bounds.right - halfBorderWidth,
                            bounds.bottom - halfBorderWidth,
                            cornerRadiusPx, cornerRadiusPx, borderPaint)
                } else {
                    canvas.drawRect(
                            bounds.left + halfBorderWidth,
                            bounds.top + halfBorderWidth,
                            bounds.right - halfBorderWidth,
                            bounds.bottom - halfBorderWidth,
                            borderPaint)
                }
            }
            DrawableShape.RECTANGLE -> {
                canvas.drawRect(
                        bounds.left + halfBorderWidth,
                        bounds.top + halfBorderWidth,
                        bounds.right - halfBorderWidth,
                        bounds.bottom - halfBorderWidth,
                        borderPaint
                )
            }
        }
    }

    override fun setAlpha(alpha: Int) {
        Log.d(LOG_TAG, "setAlpha($alpha)")
        foregroundPaint.alpha = alpha
        fillPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        Log.d(LOG_TAG, "color filter set")
        foregroundPaint.colorFilter = cf
        fillPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    fun setCornerRadiusDp(radius: Float) {
        cornerRadiusPx = radius * displayMetrics.density
    }

    fun getWidth(fallback: Int?): Int {
        val width = bounds.width()
        if (width <= 0) {
            if (fallback != null) {
                return fallback
            }
            throw IllegalStateException("width is $width. Call setBounds() first!")
        }
        return width
    }

    fun getHeight(fallback: Int?): Int {
        val height = bounds.height()
        if (height <= 0) {
            if (fallback != null) {
                return fallback
            }
            throw IllegalStateException("width is $height. Call setBounds() first!")
        }
        return height

    }

    companion object {
        private const val LOG_TAG = "FittedDrawable"
    }

}
/**
 * Shortcut for [.toBitmap] with intrinsic parameters.
 *
 * @return this drawable as bitmap
 */
