package org.qcode.fontchange;

/**
 * 支持字体变化的Activity应实现的接口
 *
 * qqliu
 * 2016/9/25.
 */
public interface IFontChangeActivity {
    /***
     * 确定是否支持切换字体大小
     *
     * @return
     */
    boolean isSupportFontChange();

    /***
     * 是否需要立刻刷新字体；默认不立刻字体
     *
     * @return
     */
    boolean isSwitchFontImmediately();

    /***
     * 刷新字体；
     * 此处刷新的是字体框架管理之外的界面
     */
    void handleFontChange();
}
