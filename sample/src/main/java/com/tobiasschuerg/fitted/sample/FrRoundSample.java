package com.tobiasschuerg.fitted.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tobiasschuerg.fitted.R;
import com.tobiasschuerg.fitted.drawable.FittedBitmapDrawable;

import static com.tobiasschuerg.fitted.drawable.FittedDrawable.SHAPE.ROUND;

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
    }

    private void topBitmapView(View view) {
        ImageView imageR = view.findViewById(R.id.iv_top);
        FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(getActivity(), R.drawable.andfoo2, ROUND);

        bitmapDrawable.setBorderColor(Color.BLUE);
        bitmapDrawable.setBorderWidthDp(5);

        imageR.setImageDrawable(bitmapDrawable);
    }

}

