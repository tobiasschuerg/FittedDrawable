package com.tobiasschuerg.fitted.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tobiasschuerg.fitted.R;
import com.tobiasschuerg.fitted.drawable.FittedBitmapDrawable;
import com.tobiasschuerg.fitted.drawable.FittedTextDrawable;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sample, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		topBitmapView(view);
		// bottomTextView(view);
	}

	private void topBitmapView(View view) {
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout_top);
		ImageView imageR = (ImageView) view.findViewById(R.id.iv_top);
		FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(getActivity(), R.drawable.andfoo2, ROUND);
		// bitmapDrawable.setDebug(true);

		bitmapDrawable.setBorderColor(Color.BLUE);
		bitmapDrawable.setBorderWidthDp(5);

		imageR.setImageDrawable(bitmapDrawable);
	}


	private void bottomTextView(View view) {
		ImageView imageRT = (ImageView) view.findViewById(R.id.iv_bottom);
		FittedTextDrawable textDrawable = new FittedTextDrawable("I'm a text", Color.WHITE, Color.RED, ROUND);

		textDrawable.debug = true;
		textDrawable.setLongSidePaddingDp(1);
		textDrawable.setCornerRadiusDp(8);
		textDrawable.setDrawBorder(true);
		imageRT.setImageDrawable(textDrawable);
	}
}

