package org.qcode.fontchange;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.qcode.fontchange.base.observable.IObservable;

/**
 * 字体变化框架管理类接口
 *
 * qqliu
 * 2016/9/24.
 */
public interface IFontManager extends IObservable<IActivityFontEventHandler> {
    /**初始化*/
    void init(Context context);

    /***
     * 调整字体大小
     * @param scale 字体相对标准大小的放大缩小比例
     * @param fontChangeListener 切换完成的回调
     */
    void changeFontSize(float scale, IFontChangeListener fontChangeListener);

    /***
     * 对TextView应用字体大小
     * @param textView
     */
    void applyFont(TextView textView);

    /***
     * 对View及其子View内所有支持字体大小调节的TextView应用字体大小调节
     * @param view
     * @param applyChild
     */
    void applyFont(View view, boolean applyChild);
}
