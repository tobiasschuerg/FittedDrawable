package com.tobiasschuerg.fitted.sample;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.tobiasschuerg.fitted.R;
import com.tobiasschuerg.fitted.drawable.FittedBitmapDrawable;
import com.tobiasschuerg.fitted.drawable.FittedDrawable;
import com.tobiasschuerg.fitted.drawable.FittedTextDrawable;

import static com.tobiasschuerg.fitted.drawable.FittedDrawable.SHAPE.RECTANGLE;
import static com.tobiasschuerg.fitted.drawable.FittedDrawable.SHAPE.ROUND;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		int color1 = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
		int color2 = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);

		// ### left

		ImageView image11 = (ImageView) findViewById(R.id.image_l1);
		FittedBitmapDrawable fd1 = new FittedBitmapDrawable(this, R.drawable.logobahnbonus, RECTANGLE);
		fd1.debug = true;
		fd1.setAdditionalPadding(50);
		image11.setImageDrawable(fd1);

		ImageView image12 = (ImageView) findViewById(R.id.image_l2);
		final FittedBitmapDrawable fd2 = new FittedBitmapDrawable(this, R.drawable.logobahnbonus, ROUND);
		image12.setImageDrawable(fd2);

		ImageView image13 = (ImageView) findViewById(R.id.image_l3);
		final FittedBitmapDrawable fd3 = new FittedBitmapDrawable(this, R.drawable.logobahnbonus, RECTANGLE);
		fd3.setTileMode(Shader.TileMode.CLAMP);
		fd3.setDebug(true);
		fd3.setAdditionalPadding(50);
		image13.setImageDrawable(fd3);

		ImageView image14 = (ImageView) findViewById(R.id.image_l4);
		final FittedBitmapDrawable fd4 = new FittedBitmapDrawable(this, R.drawable.logobahnbonus, ROUND);
		fd4.setTileMode(Shader.TileMode.CLAMP);
		image14.setImageDrawable(fd4);


		// ### right

//		final ImageView image2 = (ImageView) findViewById(R.id.image_r1);
//		final FittedTextDrawable ft1 = new FittedTextDrawable("Android", color2, color1, RECTANGLE);
//		image2.setImageDrawable(ft1);
//
//		final ImageView image22 = (ImageView) findViewById(R.id.image_r2);
//		image22.setImageDrawable(new FittedTextDrawable("Android", Color.WHITE, color1, ROUND));
//
//		final ImageView image23 = (ImageView) findViewById(R.id.image_r3);
//		image23.setImageDrawable(new FittedTextDrawable("And", Color.WHITE, color1, ROUND));
//
//		final ImageView image24 = (ImageView) findViewById(R.id.image_r4);
//		image24.setImageDrawable(new FittedTextDrawable("DROID", Color.WHITE, color1, ROUND));

	}
}
