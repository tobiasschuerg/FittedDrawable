package com.tobiasschuerg.fitted.sample;

import android.graphics.Color;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tobiasschuerg.fitted.R;
import com.tobiasschuerg.fitted.drawable.FittedBitmapDrawable;
import com.tobiasschuerg.fitted.drawable.FittedTextDrawable;

import static com.tobiasschuerg.fitted.drawable.FittedDrawable.SHAPE.ROUND;
import static com.tobiasschuerg.fitted.drawable.FittedDrawable.SHAPE.ROUND_RECTANGLE;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sample, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ImageView imageR = (ImageView) view.findViewById(R.id.iv_top);
		FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(getActivity(), R.drawable.andfoo2, ROUND);
		bitmapDrawable.debug = true;
		// bitmapDrawable.setAdditionalPaddingDp(16);
		// bitmapDrawable.setBorderColor(Color.parseColor("#cccccc"));
		imageR.setImageDrawable(bitmapDrawable);

		ImageView imageRT = (ImageView) view.findViewById(R.id.iv_bottom);
		FittedTextDrawable textDrawable = new FittedTextDrawable("I'm a text", Color.WHITE, Color.RED, ROUND);
		textDrawable.debug = true;
		textDrawable.setAdditionalPaddingDp(16);
		textDrawable.setBorderRadiusDp(8);
		imageRT.setImageDrawable(textDrawable);
	}
}
