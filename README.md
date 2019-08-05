[![](https://jitpack.io/v/tobiasschuerg/FittedDrawable.svg)](https://jitpack.io/#tobiasschuerg/FittedDrawable)

# FittedDrawable
This is a library which creates Drawables for Android that perfectly fits its content - text or an image - into a rectangle, rounded rectangle or circle. For image the background gets cleverly continued (for details see `android.graphics.Shader.TileMode`). 
This library was created to serve our needs at [Stocard](http://stocard.de) in the first place but pull requests are welcome ;-)

### Source image:
<img src="https://github.com/tobiasschuerg/FittedDrawable/raw/master/sample/src/main/res/drawable/android.png" width="200">

### Outcome
<img src="https://github.com/tobiasschuerg/FittedDrawable/raw/master/previews/round.png" width="300">
<img src="https://github.com/tobiasschuerg/FittedDrawable/raw/master/previews/rect_border.png" width="300">
<img src="https://github.com/tobiasschuerg/FittedDrawable/raw/master/previews/42.png" width="300">


#### Simple usage:

Rectangle
```kotlin
FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(context, R.drawable.android, ROUND_RECTANGLE)
imageView.setImageDrawable(bitmapDrawable)
```
or circle
```kotlin
FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(context, R.drawable.android, ROUND)
imageView.setImageDrawable(bitmapDrawable)
```

#### For fancier results set the tile mode `.setTileMode(Shader.TileMode.CLAMP);` 
Source image:
![source image2](https://github.com/tobiasschuerg/FittedDrawable/raw/master/sample/src/main/res/drawable/andfoo.png)

```kotlin
FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(context, R.drawable.android, ROUND_RECTANGLE)
bitmapDrawable.setTileMode(Shader.TileMode.CLAMP)
imageView.setImageDrawable(bitmapDrawable)

```




```
Copyright [2015-2019] [Tobias Schürg]

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

