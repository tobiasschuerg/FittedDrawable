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

import static com.tobiasschuerg.fitted.drawable.FittedDrawable.SHAPE.ROUND_RECTANGLE;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sample, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		float radius = 25f;
		int padding = 16;

		ImageView imageR = (ImageView) view.findViewById(R.id.iv_top);
		FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(getActivity(), R.drawable.andfoo2, ROUND_RECTANGLE);
		bitmapDrawable.debug = true;
		bitmapDrawable.setAdditionalPaddingDp(padding);
		bitmapDrawable.setBorderRadiusDp(radius);
		bitmapDrawable.setTileMode(Shader.TileMode.CLAMP);
		bitmapDrawable.setBorderColor(Color.parseColor("#cccccc"));
		imageR.setImageDrawable(bitmapDrawable);

		ImageView imageRT = (ImageView) view.findViewById(R.id.iv_bottom);
		FittedTextDrawable textDrawable = new FittedTextDrawable("I'm a text", Color.WHITE, Color.RED, ROUND_RECTANGLE);
		textDrawable.debug = true;
		textDrawable.setAdditionalPaddingDp(padding);
		textDrawable.setBorderRadiusDp(radius);
		imageRT.setImageDrawable(textDrawable);
	}
}
