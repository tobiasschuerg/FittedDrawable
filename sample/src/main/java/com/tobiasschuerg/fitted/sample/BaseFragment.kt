package com.tobiasschuerg.fitted.sample

import android.graphics.Color
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tobiasschuerg.fitted.R
import com.tobiasschuerg.fitted.view.FittedImageView

abstract class BaseFragment : Fragment(R.layout.fragment_sample) {

    val fittedImageView: FittedImageView
        get() = view!!.findViewById(R.id.iv_top)

    val fittedTextView: ImageView
        get() = view!!.findViewById(R.id.iv_bottom)

    val primaryColor = Color.RED
    val borderColor by lazy { ContextCompat.getColor(context!!, R.color.colorPrimary) }

    // set border with to 0 to remove border
    val borderWidth = 0

}