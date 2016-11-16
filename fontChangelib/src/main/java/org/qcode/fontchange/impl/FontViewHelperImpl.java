package org.qcode.fontchange.impl;

import android.widget.TextView;

import org.qcode.fontchange.FontSizeAttr;
import org.qcode.fontchange.IFontViewHelper;

/**
 * View设置管理
 * qqliu
 * 2016/10/8.
 */

public class FontViewHelperImpl implements IFontViewHelper {

    private TextView mTxtView;

    public FontViewHelperImpl(TextView view) {
        mTxtView = view;
    }

    //=========================interfaces================================//

    @Override
    public IFontViewHelper setFontSize(int fontSize) {
        if(fontSize <= 0) {
            return this;
        }

        FontSizeAttr attr = new FontSizeAttr(fontSize);
        ViewFontTagHelper.setFontAttr(mTxtView, attr);
        return this;
    }

    @Override
    public void applyFont() {
        FontManagerImpl.getInstance().applyFont(mTxtView);
    }
}
