[![](https://jitpack.io/v/tobiasschuerg/FittedDrawable.svg)](https://jitpack.io/#tobiasschuerg/FittedDrawable)

# FittedDrawable
This is a library which creates Drawables for Android that perfectly fits its content - text or an image - into a rectangle, rounded rectangle or circle. For image the background gets cleverly continued (for details see `android.graphics.Shader.TileMode`). 
This library was first created to serve our needs at [Stocard](http://stocard.de) but in the meantime also found its way into other project.
 
 There is still a lot room for improvements, PRs are welcome ;-)

### Source image:
<img src="https://github.com/tobiasschuerg/FittedDrawable/raw/master/sample/src/main/res/drawable/android.png" width="200">

### Outcome
|  round, no border  |  rounded rect with border  |  round with border  |
|-----|-----|-----|
|<img src="https://github.com/tobiasschuerg/FittedDrawable/raw/master/previews/round.png"> | <img src="https://github.com/tobiasschuerg/FittedDrawable/raw/master/previews/rect_border.png"> | <img src="https://github.com/tobiasschuerg/FittedDrawable/raw/master/previews/42.png">


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

```kotlin
FittedBitmapDrawable bitmapDrawable = new FittedBitmapDrawable(context, R.drawable.android, ROUND_RECTANGLE)
bitmapDrawable.setTileMode(Shader.TileMode.CLAMP)
imageView.setImageDrawable(bitmapDrawable)

```

### Get it via Jitpack
Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency
```groovy
dependencies {
        implementation 'com.github.tobiasschuerg:FittedDrawable:Tag'
}
```

For details see: https://jitpack.io/#tobiasschuerg/FittedDrawable


### license

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

