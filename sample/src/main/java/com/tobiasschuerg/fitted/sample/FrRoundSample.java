package com.tobiasschuerg.fitted.sample;

import static com.tobiasschuerg.fitted.drawable.DrawableShape.ROUND;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tobiasschuerg.fitted.R;
import com.tobiasschuerg.fitted.drawable.FittedBitmapDrawable;
import com.tobiasschuerg.fitted.drawable.FittedTextDrawable;

public class FrRoundSample extends BaseFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public FrRoundSample() {
    }

    public static FrRoundSample newInstance(int sectionNumber) {
        FrRoundSample fragment = new FrRoundSample();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topBitmapView();
        bottomSample();
    }

    private void topBitmapView() {

        FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(requireContext(), R.drawable.android, ROUND);

        bitmapDrawable.setBorderColor(getBorderColor());
        bitmapDrawable.setBorderWidthDp(getBorderWidth());

        getFittedImageView().setImageDrawable(bitmapDrawable);
    }

    private void bottomSample() {
        FittedTextDrawable textDrawable = new FittedTextDrawable("42", Color.WHITE, getPrimaryColor(), ROUND);

        textDrawable.setBorderColor(getBorderColor());
        textDrawable.setBorderWidthDp(getBorderWidth());

        getFittedTextView().setImageDrawable(textDrawable);
    }

}

