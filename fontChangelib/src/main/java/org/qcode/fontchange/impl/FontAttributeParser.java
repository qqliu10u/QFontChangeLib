package org.qcode.fontchange.impl;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import org.qcode.fontchange.FontConstant;
import org.qcode.fontchange.FontSizeAttr;
import org.qcode.fontchange.IFontAttributeParser;
import org.qcode.fontchange.base.utils.DimenUtils;
import org.qcode.fontchange.base.utils.Logging;
import org.qcode.fontchange.base.utils.StringUtils;

/***
 * 解析与字体切换相关的属性的帮助类
 */
class FontAttributeParser implements IFontAttributeParser {

    private static final String TAG = "FontAttributeParser";

    @Override
    public boolean isSupportFontChange(String name, Context context, AttributeSet attrs) {
        //只有View设置了skin:enableFontChange，才解析属性
        boolean isFontEnable = attrs.getAttributeBooleanValue(
                FontConstant.XML_NAMESPACE,
                FontConstant.ATTR_FONT_CHANGE_ENABLE,
                false);
        return isFontEnable;
    }

    @Override
    public void parseAttribute(TextView textView, String name, Context context, AttributeSet attrs) {
        if (textView == null) {
            return;
        }

        FontSizeAttr fontSizeAttr = parseFontAttr(context, attrs);

        if (null != fontSizeAttr) {
            ViewFontTagHelper.setFontAttr(textView, fontSizeAttr);
        }
    }

    /***
     * 收集与字体相关的属性
     *
     * @param context
     * @param attrs
     */
    public static FontSizeAttr parseFontAttr(Context context,
                                             AttributeSet attrs) {
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);

            if (!FontConstant.ATTR_TEXT_SIZE.equals(attrName)) {
                continue;
            }

            FontSizeAttr fontSizeAttr = null;
            if (!attrValue.startsWith("@")) {
                try {
                    fontSizeAttr = getFontAttrByValueSplit(context, attrValue);
                } catch (Exception ex) {
                    Logging.d(TAG, "parseFontAttr()| parse font size failed");
                }
            } else {
                try {
                    fontSizeAttr = getFontAttrFromId(context, attrName, attrValue);
                } catch (NumberFormatException ex) {
                    fontSizeAttr = getFontAttrByIdSplit(context, attrName, attrValue);
                } catch (NotFoundException ex) {
                    Logging.d(TAG, "parseFontAttr()| error happened", ex);
                }
            }

            return fontSizeAttr;
        }

        return null;
    }

    private static FontSizeAttr getFontAttrByValueSplit(Context context, String attrValue) throws NumberFormatException {
        if (StringUtils.isEmpty(attrValue)) {
            return null;
        }

        int textValue = 0;
        if (attrValue.endsWith(FontConstant.UNIT_SP)) {
            int value = (int) Float.parseFloat(attrValue.substring(0, attrValue.length() - 2));
            textValue = value;
        } else if (attrValue.endsWith(FontConstant.UNIT_PX)) {
            int value = (int) Float.parseFloat(attrValue.substring(0, attrValue.length() - 2));
            textValue = DimenUtils.px2sp(context, value);
        } else if (attrValue.endsWith(FontConstant.UNIT_DIP)) {
            int value = (int) Float.parseFloat(attrValue.substring(0, attrValue.length() - 3));
            textValue = value;
        } else if (attrValue.endsWith(FontConstant.UNIT_DP)) {
            int value = (int) Float.parseFloat(attrValue.substring(0, attrValue.length() - 2));
            textValue = value;
        } else if (attrValue.endsWith(FontConstant.UNIT_PT)) {
            int value = (int) Float.parseFloat(attrValue.substring(0, attrValue.length() - 2));
            textValue = DimenUtils.px2sp(context, DimenUtils.pt2px(context, value));
        } else if (attrValue.endsWith(FontConstant.UNIT_IN)) {
            int value = (int) Float.parseFloat(attrValue.substring(0, attrValue.length() - 2));
            textValue = DimenUtils.px2sp(context, DimenUtils.in2px(context, value));
        } else if (attrValue.endsWith(FontConstant.UNIT_MM)) {
            int value = (int) Float.parseFloat(attrValue.substring(0, attrValue.length() - 2));
            textValue = DimenUtils.px2sp(context, DimenUtils.mm2px(context, value));
        } else {
            throw new RuntimeException("unit not found");
        }

        return new FontSizeAttr(textValue);
    }

    private static FontSizeAttr getFontAttrByIdSplit(Context context, String attrName, String attrValue) {
        try {
            int dividerIndex = attrValue.indexOf("/");
            String entryName = attrValue.substring(dividerIndex + 1, attrValue.length());
            String typeName = attrValue.substring(1, dividerIndex);
            int id = context.getResources().getIdentifier(entryName, typeName, context.getPackageName());
            int textSize = getTextSizeFromId(context, id);
            return new FontSizeAttr(textSize);
        } catch (NotFoundException e) {
            Logging.d(TAG, "getFontAttrByIdSplit()| error happened", e);
        }
        return null;
    }

    private static FontSizeAttr getFontAttrFromId(Context context, String attrName, String attrValue) {
        int id = Integer.parseInt(attrValue.substring(1));
        int textSize = getTextSizeFromId(context, id);
        return new FontSizeAttr(textSize);
    }

    private static int getTextSizeFromId(Context context, int dimenId) {
        TypedValue value = new TypedValue();
        Resources resources = context.getResources();
        resources.getValue(dimenId, value, true);
        if (value.type == TypedValue.TYPE_DIMENSION) {
            int unit = (value.data >> TypedValue.COMPLEX_UNIT_SHIFT) & TypedValue.COMPLEX_UNIT_MASK;
            float textValue = TypedValue.complexToFloat(value.data);
            DisplayMetrics metrics = resources.getDisplayMetrics();
            switch (unit) {
                case TypedValue.COMPLEX_UNIT_PX:
                    return DimenUtils.px2sp(context, textValue);
                case TypedValue.COMPLEX_UNIT_DIP:
                    //NOTICE:此处将dp与sp等同，意味着我们支持系统未设置系统字体调节
                    return (int) (textValue + 0.5);
                case TypedValue.COMPLEX_UNIT_SP:
                    return (int) (textValue + 0.5);
                case TypedValue.COMPLEX_UNIT_PT:
                    return (int) (textValue * metrics.xdpi * (1.0f / 72) / metrics.scaledDensity + 0.5);
                case TypedValue.COMPLEX_UNIT_IN:
                    return (int) (textValue * metrics.xdpi / metrics.scaledDensity + 0.5);
                case TypedValue.COMPLEX_UNIT_MM:
                    return (int) (textValue * metrics.xdpi * (1.0f / 25.4f) / metrics.scaledDensity + 0.5);
            }
            return 0;
        }
        throw new NotFoundException(
                "Resource ID #0x" + Integer.toHexString(dimenId) + " type #0x"
                        + Integer.toHexString(value.type) + " is not valid");
    }
}
