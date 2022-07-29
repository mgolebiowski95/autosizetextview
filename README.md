# autosizetextview
**AutoSizeTextView** is a better version of auto size implemented in framework TextView. Size of text can be measured only via height of the view.

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

## Usage:

### Attributes
| Attribute | Format | Default |
|:---|:---:|:---:|
| AutoSizeTextView_measureTextSizeEnabled | boolean | true
| AutoSizeTextView_minTextSizeFactor | float | 0
| AutoSizeTextView_maxTextSizeFactor | float | 1
| AutoSizeTextView_skipLayoutWidth | boolean | false
| AutoSizeTextView_referenceText | string |