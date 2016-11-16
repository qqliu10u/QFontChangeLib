package org.qcode.fontchange;

import android.widget.TextView;

/**
 * qqliu
 * 2016/10/17.
 */

public class FontSizeAttr {
    int textSize;

    public FontSizeAttr(int textSize) {
        this.textSize = textSize;
    }

    public void apply(TextView textView, float scale) {
        if (null == textView) {
            return;
        }

        textView.setTextSize(textSize * scale);
    }

    @Override
    public String toString() {
        return "FontSizeAttr{" +
                "textSize=" + textSize +
                '}';
    }
}
