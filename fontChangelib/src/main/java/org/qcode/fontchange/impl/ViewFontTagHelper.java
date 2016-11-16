package org.qcode.fontchange.impl;

import android.widget.TextView;

import com.iflytek.android_font_loader_lib.R;
import org.qcode.fontchange.FontSizeAttr;

/**
 * 存取View的字体属性的帮助类
 * qqliu
 * 2016/10/8.
 */

class ViewFontTagHelper {

    /***
     * 设置View的字体属性
     * @param view
     * @param fontSizeAttr
     */
    public static void setFontAttr(TextView view, FontSizeAttr fontSizeAttr) {
        if(null == view) {
            return;
        }

        view.setTag(R.id.tag_font_attr, fontSizeAttr);
    }

    /***
     * 获取View的字体属性
     * @param view
     * @return  FontSizeAttr
     */
    public static FontSizeAttr getFontAttr(TextView view) {
        if(null == view) {
            return null;
        }

        return (FontSizeAttr) view.getTag(R.id.tag_font_attr);
    }
}
