package org.qcode.fontchange;

import android.app.Activity;

/**
 * 与Activity相关的字体调节框架逻辑处理抽象接口
 *
 * qqliu
 * 2016/9/25.
 */
public interface IActivityFontEventHandler {
    /***
     * 应该在setContentView之前调用
     */
    void onCreate(Activity activity);

    /***
     * 设置View创建的监听器
     * @param viewCreateListener
     */
    void setViewCreateListener(IViewCreateListener viewCreateListener);

    /**在setContentView之后调用*/
    void onViewCreated();

    void onResume();

    void onWindowFocusChanged(boolean hasFocus);

    void onDestroy();

    /**onCreate之前调用*/
    IActivityFontEventHandler setNeedDelegateViewCreate(boolean needDelegateViewCreate);

    /**onCreate之前调用*/
    IActivityFontEventHandler setSupportFontChange(boolean isSupportFontChange);

    /**onCreate之前调用*/
    IActivityFontEventHandler setSwitchFontImmediately(boolean isImmediate);

    void handleFontUpdate();

    /***
     * 获取字体属性的解析器
     * @return
     */
    IFontAttributeParser getFontAttributeParser();
}
