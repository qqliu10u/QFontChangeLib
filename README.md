# FontChangeLib
内容阅读类客户端通常都有个功能——调整字体大小，这种功能说复杂也不复杂，但做起来还是费些时间的。本框架是基于代理View创建来支持多Activity调节字体大小的一个小功能，支持应用内多TextView的字体大小调节（**注意：不负责分辨率适配，调大了可能影响布局，不过好在一般这类需求都出现在列表之类可扩张的View内**）。整体设计逻辑与[QSkinLoader](https://github.com/qqliu10u/QSkinLoader)换肤框架相同，此处不再赘述。

----
#一、效果图
![字体调节框架效果图](https://github.com/qqliu10u/FontChangeLib/blob/master/demo.gif)

#二、使用方法
##2.1 XML集成
在XML文件根布局中增加命名空间
```xml
xmlns:skin="http://schemas.android.com/android/skin"
```
对需要支持字体大小调节的TextView（仅支持TextView）设置：
```xml
skin:enableFontChange="true"
```

##2.2 Java代码集成
在Application初始化时对框架进行初始化：
```Java
//加载字体变化框架
FontManager.getInstance().init(this);
```

建立一个BaseActivity，支持字体大小调节
```
package org.qcode.qfontchangelib;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import org.qcode.fontchange.FontManager;
import org.qcode.fontchange.IActivityFontEventHandler;
import org.qcode.fontchange.IFontChangeActivity;


/**
 * 所有Activity的父类
 */
public abstract class BaseActivity extends FragmentActivity
        implements IFontChangeActivity {

    private static final String TAG = "BaseActivity";

    private IActivityFontEventHandler mFontEventHandler;
    private boolean mFirstTimeApplyFont = true;

    private boolean mIsDestroyingFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉标题栏，自定义
        getWindow().setFormat(PixelFormat.RGBA_8888);

        initFontHandler();
    }

    private void initFontHandler() {
        mFontEventHandler = FontManager.newActivityFontEventHandler()
                .setSupportFontChange(isSupportFontChange())
                .setSwitchFontImmediately(isSwitchFontImmediately())
                .setNeedDelegateViewCreate(true);
        mFontEventHandler.onCreate(this);
    }


    @Override
    public boolean isSupportFontChange() {
        return true;
    }

    @Override
    public boolean isSwitchFontImmediately() {
        return true;
    }

    @Override
    public void handleFontChange() {
    }

    @Override
    protected void onDestroy() {
        mIsDestroyingFlag = true;
        super.onDestroy();

        mFontEventHandler.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ////此通知放在此处，尽量让子类的view都添加到view树内
        if(mFirstTimeApplyFont) {
            mFontEventHandler.onViewCreated();
            mFirstTimeApplyFont = false;
        }

        mFontEventHandler.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        mFontEventHandler.onWindowFocusChanged(hasFocus);
    }
}
```

如果不使用XML内的自定义属性，可以在Java代码内设置：
```Java
//代码内设置TextView的文字大小
FontManager
        .with(mMainTextView)
        .setFontSize(20)
        .applyFont();
```

切换字体大小时，调用接口如下，此时所有支持字体大小变化的TextView文字大小都是标准值的0.8倍（**注意是标准大小不是当前大小**）：
```
FontManager.getInstance().changeFontSize(0.8f, mListener);
```

对于动态创建的View，如果不想对所有TextView动态设置属性，可以考虑用如下接口：
```Java
//对convertView的所有子元素应用字体大小属性
FontManager.getInstance().applyFont(view, true);
```
此时框架会对view及其子View内所有支持字体大小调节的TextView应用当前设置的字体大小。

#三、总结
好了，框架使用就讲完了，这个框架是QSkinLoader的一个简化版本的设计，本来想在QSkinLoader内直接支持此功能，但觉得两者虽做法类似，但目标不同，所以单独抽出了这个小框架，大家如果有需要可以下一个试试。