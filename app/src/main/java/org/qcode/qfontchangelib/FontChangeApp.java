package org.qcode.qfontchangelib;

import android.app.Application;

import org.qcode.fontchange.FontManager;

/**
 * qqliu
 * 2016/11/16.
 */

public class FontChangeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //加载字体变化框架
        FontManager.getInstance().init(this);
    }
}
