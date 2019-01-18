package com.tobiasschuerg.fitted.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tobiasschuerg.fitted.R;
import com.tobiasschuerg.fitted.drawable.FittedTextDrawable;
import com.tobiasschuerg.fitted.view.FittedImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.tobiasschuerg.fitted.drawable.DrawableShape.RECTANGLE;

public class FrRectangleSample extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public FrRectangleSample() {
    }

    public static FrRectangleSample newInstance(int sectionNumber) {
        FrRectangleSample fragment = new FrRectangleSample();
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

        FittedImageView imageR = view.findViewById(R.id.iv_top);
        imageR.setImageResource(R.drawable.andfoo2);

        ImageView imageRT = view.findViewById(R.id.iv_bottom);
        FittedTextDrawable textDrawable = new FittedTextDrawable("I'm a text", Color.WHITE, Color.RED, RECTANGLE);
        textDrawable.debug = true;
        imageRT.setImageDrawable(textDrawable);
    }
}
