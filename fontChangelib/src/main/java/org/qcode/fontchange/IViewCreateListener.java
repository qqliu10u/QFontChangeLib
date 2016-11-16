package org.qcode.fontchange;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * 代理View创建时的回调
 * qqliu
 * 2016/10/17.
 */

public interface IViewCreateListener {
    View beforeCreate(String name, Context context, AttributeSet attrs);

    void afterCreated(TextView textView, String name, Context context, AttributeSet attrs);
}
