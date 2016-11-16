package org.qcode.fontchange;

import android.widget.TextView;

import org.qcode.fontchange.impl.ActivityFontEventHandlerImpl;
import org.qcode.fontchange.impl.FontManagerImpl;
import org.qcode.fontchange.impl.FontViewHelperImpl;

/**
 * 字体大小调节框架对外接口
 * qqliu
 * 2016/10/8.
 */

public class FontManager {
    /***
     * 获取字体管理类实例
     * @return
     */
    public static IFontManager getInstance() {
        return FontManagerImpl.getInstance();
    }

    /***
     * 获取View的字体属性管理类
     * @param view
     * @return
     */
    public static IFontViewHelper with(TextView view) {
        return new FontViewHelperImpl(view);
    }

    /***
     * 创建一个新的Activity的字体事件处理器
     * @return
     */
    public static IActivityFontEventHandler newActivityFontEventHandler() {
        return new ActivityFontEventHandlerImpl();
    }
}
