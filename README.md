# autosizetextview
**AutoSizeTextView** has a better version of auto size implemented in framework TextView.

## Installation

Library is installed by putting aar file into libs folder:

```
module/libs (ex. app/libs)
```

and adding the aar dependency to your `build.gradle` file:
```groovy
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.0-alpha05'
    implementation files("libs/autosizetextview-1.0.0.aar")
}
```

## Screenshots
![](https://github.com/mgolebiowski95/autosizetextview/blob/master/screenshots/Screenshot_1659113492.png)

## Usage:
```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    tools:ignore="HardcodedText">

    <LinearLayout
        android:id="@+id/my_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:divider="@drawable/space_divider_8dp"
        android:gravity="center"
        android:orientation="vertical"
        android:showDividers="middle">

        <com.mgsoftware.autosizetextview.AutoSizeTextView
            style="@style/DefaultTextView"
            android:layout_width="80dp"
            android:layout_height="32dp"
            android:text="A B C D E F G H I K L M N O P Q R S T V X Y Z"
            app:AutoSizeTextView_maxTextSizeFactor="0.75"
            app:AutoSizeTextView_minTextSizeFactor="0.25" />

        <com.mgsoftware.autosizetextview.AutoSizeTextView
            style="@style/DefaultTextView"
            android:layout_width="80dp"
            android:layout_height="32dp"
            android:text="A B C D E F G H I K L M N O P Q R S T V X Y Z"
            app:AutoSizeTextView_maxTextSizeFactor="0.75"
            app:AutoSizeTextView_minTextSizeFactor="0.25"
            app:AutoSizeTextView_skipLayoutWidth="true" />

        <com.mgsoftware.autosizetextview.AutoSizeTextView
            style="@style/DefaultTextView"
            android:layout_width="80dp"
            android:layout_height="32dp"
            android:text="A B C D E F G H I K L M N O P Q R S T V X Y Z"
            app:AutoSizeTextView_maxTextSizeFactor="0.75"
            app:AutoSizeTextView_minTextSizeFactor="0.25"
            app:AutoSizeTextView_referenceText="A\nA" />

        <com.mgsoftware.autosizetextview.AutoSizeTextView
            style="@style/DefaultTextView"
            android:layout_width="80dp"
            android:layout_height="32dp"
            android:ellipsize="end"
            android:lines="2"
            android:text="A B C D E F G H I K L M N O P Q R S T V X Y Z"
            app:AutoSizeTextView_maxTextSizeFactor="0.75"
            app:AutoSizeTextView_minTextSizeFactor="0.25"
            app:AutoSizeTextView_referenceText="A\nA" />

        <com.mgsoftware.autosizetextview.AutoSizeTextView
            style="@style/DefaultTextView"
            android:layout_width="80dp"
            android:layout_height="32dp"
            android:text="A B C D E F G H I K L M N O P Q R S T V X Y Z"
            app:AutoSizeTextView_maxTextSizeFactor="0.75"
            app:AutoSizeTextView_minTextSizeFactor="0.25"
            app:AutoSizeTextView_referenceText="A" />

        <com.mgsoftware.autosizetextview.AutoSizeTextView
            style="@style/DefaultTextView"
            android:layout_width="80dp"
            android:layout_height="32dp"
            android:singleLine="true"
            android:text="A B C D E F G H I K L M N O P Q R S T V X Y Z"
            app:AutoSizeTextView_maxTextSizeFactor="0.75"
            app:AutoSizeTextView_minTextSizeFactor="0.25"
            app:AutoSizeTextView_referenceText="A" />

    </LinearLayout>
    
</layout>
```

### Attributes
| Attribute | Format | Default |
|:---|:---:|:---:|
| AutoSizeTextView_measureTextSizeEnabled | boolean | true
| AutoSizeTextView_minTextSizeFactor | float | 0
| AutoSizeTextView_maxTextSizeFactor | float | 1
| AutoSizeTextView_skipLayoutWidth | boolean | false
| AutoSizeTextView_referenceText | string |

### Attributes description
**measureTextSizeEnabled** - enable / disable auto size\
**minTextSizeFactor / maxTextSizeFactor** - size of text is measured of the width and height, by setting these attributes you can set up down and upper limit (depends of height), f.ex. TextView has 100px, then minTextSize = minTextSizeFactor * 100 and maxTextSize = maxTextSizeFactor * 100\
**skipLayoutWidth** - during measuring, width of the view is ignored and measuring are based only of the height\
**referenceText** - TextView will perform measuring for the referenceText, and will apply it to the original text
