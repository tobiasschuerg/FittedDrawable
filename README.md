[![](https://jitpack.io/v/tobiasschuerg/FittedDrawable.svg)](https://jitpack.io/#tobiasschuerg/FittedDrawable)

# FittedDrawable
Drawable that perfectly fits text or an image into a rectangle or circle and automatically sets the backgroudn color.

### Source image:
![source image1](https://github.com/tobiasschuerg/FittedDrawable/raw/master/sample/src/main/res/drawable/andfoo2.png)

#### Simple usage:

Rectangle
```java
FittedBitmapDrawable fbd = new FittedBitmapDrawable(getApplicationContext(), R.drawable.logo, RECTANGLE);
imageView.setImageDrawable(fbd);
```
or circle
```java
FittedBitmapDrawable fbd = new FittedBitmapDrawable(getApplicationContext(), R.drawable.logo, ROUND);
imageView.setImageDrawable(fbd);
```

![result1](https://github.com/tobiasschuerg/FittedDrawable/raw/master/previews/sample1.png)


#### For fancier results set the tile mode `.setTileMode(Shader.TileMode.CLAMP);` 
Source image:
![source image2](https://github.com/tobiasschuerg/FittedDrawable/raw/master/sample/src/main/res/drawable/andfoo.png)

```java
ImageView image14 = (ImageView) findViewById(R.id.image_l4);
		final FittedBitmapDrawable fd4 = new FittedBitmapDrawable(this, src, ROUND);
		fd4.setTileMode(Shader.TileMode.CLAMP);
		fd4.setAdditionalPadding(2);
		image14.setImageDrawable(fd4);
```

![result1](https://github.com/tobiasschuerg/FittedDrawable/raw/master/previews/sample2.png)





