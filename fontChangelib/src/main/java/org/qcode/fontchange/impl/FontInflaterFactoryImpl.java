package org.qcode.fontchange.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.qcode.fontchange.IFontAttributeParser;
import org.qcode.fontchange.IViewCreateListener;
import org.qcode.fontchange.base.utils.Logging;

/***
 * 代理View的创建，解析与字体相关的属性
 */
class FontInflaterFactoryImpl implements LayoutInflater.Factory {

    private static final String TAG = "FontInflaterFactoryImpl";

    private IViewCreateListener mViewCreateListener;

    private IFontAttributeParser mFontAttributeParser;

    public FontInflaterFactoryImpl(IFontAttributeParser parser) {
        this.mFontAttributeParser = parser;
    }

    public void setViewCreateListener(IViewCreateListener viewCreateListener) {
        this.mViewCreateListener = viewCreateListener;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;

        if (null != mViewCreateListener) {
            view = mViewCreateListener.beforeCreate(name, context, attrs);
        }

        //只有View设置了skin:enableFontChange，才解析属性
        if (!mFontAttributeParser.isSupportFontChange(name, context, attrs)) {
            return null;
        }

        //代理创建View
        TextView textView;
        if(view instanceof TextView) {
            textView = (TextView) view;
        } else {
            textView = createTextView(context, name, attrs);
        }

        if (textView == null) {
            return null;
        }

        mFontAttributeParser.parseAttribute(textView, name, context, attrs);

        if (null != mViewCreateListener) {
            mViewCreateListener.afterCreated(textView, name, context, attrs);
        }

        return textView;
    }

    /***
     * 根据名称创建view
     *
     * @param context
     * @param name
     * @param attrs
     * @return
     */
    private TextView createTextView(Context context,
                                    String name,
                                    AttributeSet attrs) {
        View view = null;
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            if (-1 == name.indexOf('.')) {
                if ("TextView".equals(name)) {
                    view = inflater.createView(
                            name, "android.widget.", attrs);
                }
            } else {
                view = inflater.createView(name, null, attrs);
            }

        } catch (Exception ex) {
            Logging.d(TAG, "createView()| create view failed", ex);
            view = null;
        }

        if (view instanceof TextView) {
            return (TextView) view;
        }

        return null;
    }
}
