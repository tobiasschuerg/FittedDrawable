package com.tobiasschuerg.fitted.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tobiasschuerg.fitted.drawable.FittedBitmapDrawable
import com.tobiasschuerg.fitted.drawable.FittedDrawable

/**
 * An [AppCompatImageView] which uses a [FittedDrawable] internally.
 */
class FittedImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    // TODO, add attributes for setting the shape in xml

    override fun setImageResource(resId: Int) {
        val drawable = FittedBitmapDrawable(context, resId, FittedDrawable.SHAPE.RECTANGLE)
        super.setImageDrawable(drawable)
    }
}