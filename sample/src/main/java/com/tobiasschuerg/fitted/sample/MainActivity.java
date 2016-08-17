package com.tobiasschuerg.fitted.sample;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.tobiasschuerg.fitted.R;
import com.tobiasschuerg.fitted.drawable.FittedBitmapDrawable;
import com.tobiasschuerg.fitted.drawable.FittedDrawable;
import com.tobiasschuerg.fitted.drawable.FittedTextDrawable;

import static com.tobiasschuerg.fitted.drawable.FittedDrawable.SHAPE.RECTANGLE;
import static com.tobiasschuerg.fitted.drawable.FittedDrawable.SHAPE.ROUND;
import static com.tobiasschuerg.fitted.drawable.FittedDrawable.SHAPE.ROUND_RECTANGLE;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		int color1 = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
		int color2 = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);

		int src = R.drawable.andfoo2;

		// ### first

		ImageView imageR = (ImageView) findViewById(R.id.image_rect_bitmap);
		FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(this, src, ROUND_RECTANGLE);
		bitmapDrawable.debug = true;
		bitmapDrawable.setAdditionalPaddingDp(16);
		bitmapDrawable.setBorderRadiusDp(16);
		bitmapDrawable.setTileMode(Shader.TileMode.CLAMP);
		bitmapDrawable.setBorderColor(Color.parseColor("#cccccc"));


		// Draw the drawable on a bitmap first and then attach that bitmap to the ImageView
		// Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
		//Canvas canvas = new Canvas(bitmap);
		//bitmapDrawable.draw(canvas);







		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		int iconSize = am.getLauncherLargeIconSize();
		Log.d("ShortCutHelper", "icon size " + iconSize);

		int logoSize = Math.round(iconSize / 192.0f * 154.0f);
		Log.d("ShortCutHelper", "logo size " + logoSize);

		float border = (iconSize - logoSize) / 2.0f;
		Log.d("ShortCutHelper", "border " + border);

		RectF logoRect = new RectF(border, border, iconSize - border, iconSize - border);
		Log.d("ShortCutHelper", "logo rect " + logoRect);

		Rect iconRect = new Rect(0, 0, iconSize, iconSize);
		float cornerSize = 3.0f / 38.0f * logoSize;

		FittedBitmapDrawable drawable = new FittedBitmapDrawable(this, src, ROUND_RECTANGLE);
		drawable.setBorderRadiusDp(cornerSize);

		Bitmap launcherIcon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(launcherIcon);

		drawable.draw(canvas);






		imageR.setImageBitmap(launcherIcon);

		// imageR.setImageDrawable(bitmapDrawable);


		ImageView imageRT = (ImageView) findViewById(R.id.image_rect_text);
		FittedTextDrawable textDrawable = new FittedTextDrawable("I'm a text", Color.WHITE, Color.RED, ROUND_RECTANGLE);
		textDrawable.debug = true;
		textDrawable.setAdditionalPaddingDp(16);
		textDrawable.setBorderRadiusDp(8);
		imageRT.setImageDrawable(textDrawable);


		// ...

//		ImageView image12 = (ImageView) findViewById(R.id.image_l2);
//		final FittedBitmapDrawable fd2 = new FittedBitmapDrawable(this, src, ROUND);
//		image12.setImageDrawable(fd2);
//
//		src = R.drawable.andfoo;
//
//		ImageView image13 = (ImageView) findViewById(R.id.image_l3);
//		final FittedBitmapDrawable fd3 = new FittedBitmapDrawable(this, src, RECTANGLE);
//		fd3.setTileMode(Shader.TileMode.CLAMP);
////		fd3.setDebug(true);
////		fd3.setAdditionalPaddingPX(50);
//		image13.setImageDrawable(fd3);
//
//		ImageView image14 = (ImageView) findViewById(R.id.image_l4);
////		final FittedBitmapDrawable fd4 = new FittedBitmapDrawable(this, src, ROUND);
////		fd4.setTileMode(Shader.TileMode.CLAMP);
////		fd4.setAdditionalPaddingPX(2);
////		image14.setImageDrawable(fd4);

//		final FittedTextDrawable fd4 = new FittedTextDrawable("Android", Color.BLUE, Color.MAGENTA, RECTANGLE);
//		// fd4.setAdditionalPaddingPX(2);
//		image14.setImageDrawable(fd4);


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
