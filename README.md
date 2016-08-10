[![](https://jitpack.io/v/tobiasschuerg/FittedDrawable.svg)](https://jitpack.io/#tobiasschuerg/FittedDrawable)

# FittedDrawable
Drawable that perfectly fits its content - text or an image - into a rectangle or circle and automatically sets the backgroudn color. This library was created to serve our needs at [Stocard](http://stocard.de) in the first place but pull requests are welcome ;-)

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



```
Copyright [yyyy] [name of copyright owner]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

