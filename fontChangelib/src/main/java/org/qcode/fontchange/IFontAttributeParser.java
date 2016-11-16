package org.qcode.fontchange;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 框架解析字体属性的解析帮助类
 *
 * the font attribute parser defines
 * how to parse attributes when view is created.
 *
 * qqliu
 * 2016/11/8.
 */

public interface IFontAttributeParser {
    /***
     * 是否支持字体大小调节
     *
     * return the parse result whether the view supports font-size-change
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    boolean isSupportFontChange(String name, Context context, AttributeSet attrs);

    /***
     * 解析View的字体属性
     *
     * parse font attributes from view-creating process
     *
     * @param view
     * @param name
     * @param context
     * @param attrs
     */
    void parseAttribute(TextView view, String name, Context context, AttributeSet attrs);
}
