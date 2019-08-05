package com.tobiasschuerg.fitted.sample;

import android.graphics.Color;
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

import static com.tobiasschuerg.fitted.drawable.DrawableShape.ROUND;
import static com.tobiasschuerg.fitted.drawable.DrawableShape.ROUND_RECTANGLE;

public class FrRoundSample extends Fragment {

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topBitmapView(view);
        bottomSample(view);
    }

    private void topBitmapView(View view) {
        ImageView imageR = view.findViewById(R.id.iv_top);
        FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(getActivity(), R.drawable.android, ROUND);

        bitmapDrawable.setBorderColor(Color.BLUE);
        bitmapDrawable.setBorderWidthDp(5);

        imageR.setImageDrawable(bitmapDrawable);
    }

    private void bottomSample(View view) {
        ImageView imageRT = view.findViewById(R.id.iv_bottom);
        FittedTextDrawable textDrawable = new FittedTextDrawable("I'm a text", Color.WHITE, Color.RED, ROUND);

        textDrawable.setCornerRadiusDp(4);

        textDrawable.setBorderColor(Color.BLUE);
        textDrawable.setBorderWidthDp(5);

        imageRT.setImageDrawable(textDrawable);
    }

}

