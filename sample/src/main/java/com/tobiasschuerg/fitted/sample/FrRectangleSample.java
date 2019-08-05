package com.tobiasschuerg.fitted.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tobiasschuerg.fitted.R;
import com.tobiasschuerg.fitted.drawable.FittedTextDrawable;

import static com.tobiasschuerg.fitted.drawable.DrawableShape.RECTANGLE;

public class FrRectangleSample extends BaseFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public FrRectangleSample() {
    }

    static FrRectangleSample newInstance(int sectionNumber) {
        FrRectangleSample fragment = new FrRectangleSample();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getFittedImageView().setImageResource(R.drawable.android);
        getFittedImageView().drawable.setBorderColor(getBorderColor());
        getFittedImageView().drawable.setBorderWidthDp(getBorderWidth());

        FittedTextDrawable textDrawable = new FittedTextDrawable("I'm a text", Color.WHITE, getPrimaryColor(), RECTANGLE);
        textDrawable.setBorderColor(getBorderColor());
        textDrawable.setBorderWidthDp(getBorderWidth());

        getFittedTextView().setImageDrawable(textDrawable);
    }
}
