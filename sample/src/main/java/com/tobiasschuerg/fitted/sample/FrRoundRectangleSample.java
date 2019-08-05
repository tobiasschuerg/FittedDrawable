package com.tobiasschuerg.fitted.sample;

import android.graphics.Color;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tobiasschuerg.fitted.R;
import com.tobiasschuerg.fitted.drawable.FittedBitmapDrawable;
import com.tobiasschuerg.fitted.drawable.FittedTextDrawable;

import static com.tobiasschuerg.fitted.drawable.DrawableShape.ROUND_RECTANGLE;

public class FrRoundRectangleSample extends BaseFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public FrRoundRectangleSample() {
    }

    public static FrRoundRectangleSample newInstance(int sectionNumber) {
        FrRoundRectangleSample fragment = new FrRoundRectangleSample();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        float radius = 25f;
        int padding = 4;

        FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(getActivity(), R.drawable.android, ROUND_RECTANGLE);

        bitmapDrawable.setLongSidePaddingDp(padding);
        bitmapDrawable.setCornerRadiusDp(radius);
        bitmapDrawable.setTileMode(Shader.TileMode.CLAMP);
        bitmapDrawable.setBorderColor(getBorderColor());
        bitmapDrawable.setBorderWidthDp(getBorderWidth());
        getFittedImageView().setImageDrawable(bitmapDrawable);


        FittedTextDrawable textDrawable = new FittedTextDrawable("I'm a text", Color.WHITE, Color.RED, ROUND_RECTANGLE);
        textDrawable.setBorderColor(getBorderColor());
        textDrawable.setBorderWidthDp(getBorderWidth());
        textDrawable.setLongSidePaddingDp(padding);
        textDrawable.setCornerRadiusDp(radius);
        getFittedTextView().setImageDrawable(textDrawable);
    }
}
