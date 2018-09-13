# dots

![](https://i.stack.imgur.com/xiuoj.jpg)


### Install

```groovy
implementation 'com.theah64.dots:dots:1.0.5'
```

### Usage

```xml
<com.theah64.dots.Dots
  android:id="@+id/dotsSignUp"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:layout_marginBottom="32dp"
  android:layout_marginTop="32dp"
  app:active_dot_color="@color/colorPrimaryDark"
  app:count="2"
  app:margin="3dp"
  app:radius="10dp" />
```

### Methods


```kotlin
// To set a dot active
fun setActive(activeIndex: Int)

// To set number of dots
fun setCount(count: Int)

// To move active dot to back
fun moveBack()

// To move active dot to forward 
fun moveForward()
```
