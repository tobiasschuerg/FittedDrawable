package com.tobiasschuerg.fitted.sample;

import android.graphics.Color;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tobiasschuerg.fitted.R;
import com.tobiasschuerg.fitted.drawable.FittedBitmapDrawable;
import com.tobiasschuerg.fitted.drawable.FittedTextDrawable;

import static com.tobiasschuerg.fitted.drawable.DrawableShape.ROUND_RECTANGLE;

public class FrRoundRectangleSample extends Fragment {

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        float radius = 25f;
        int padding = 4;
        int borderWidth = 4;

        ImageView imageR = view.findViewById(R.id.iv_top);
        FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(getActivity(), R.drawable.android, ROUND_RECTANGLE);

        bitmapDrawable.setLongSidePaddingDp(padding);
        bitmapDrawable.setCornerRadiusDp(radius);
        bitmapDrawable.setTileMode(Shader.TileMode.CLAMP);
        bitmapDrawable.setBorderColor(Color.parseColor("#cccccc"));
        bitmapDrawable.setBorderWidthDp(borderWidth);
        imageR.setImageDrawable(bitmapDrawable);


        ImageView imageRT = view.findViewById(R.id.iv_bottom);
        FittedTextDrawable textDrawable = new FittedTextDrawable("I'm a text", Color.WHITE, Color.RED, ROUND_RECTANGLE);

        textDrawable.setLongSidePaddingDp(padding);
        textDrawable.setCornerRadiusDp(radius);
        imageRT.setImageDrawable(textDrawable);
    }
}
