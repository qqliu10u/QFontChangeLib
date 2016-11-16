package org.qcode.fontchange.base.utils;

import android.content.Context;

/**
 * qqliu
 * 2016/10/17.
 */

public class DimenUtils {
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, double dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dip
     */
    public static int px2dip(Context context, double pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     */
    public static int sp2px(Context context, double spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(Context context, double pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5);
    }

    /**
     * 根据手机的分辨率从 point 的单位 转成为 px(像素)
     */
    public static int pt2px(Context context, double ptValue) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        return (int) (ptValue * scale * (1.0f / 72) + 0.5);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 point
     */
    public static int px2pt(Context context, double pxValue) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        return (int) (pxValue / scale / (1.0f / 72) + 0.5);
    }

    /**
     * 根据手机的分辨率从 inches 的单位 转成为 px(像素)
     */
    public static int in2px(Context context, double inValue) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        return (int) (inValue * scale + 0.5);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 inches
     */
    public static int px2in(Context context, double pxValue) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        return (int) (pxValue / scale + 0.5);
    }

    /**
     * 根据手机的分辨率从 millimeters 的单位 转成为 px(像素)
     */
    public static int mm2px(Context context, double mmValue) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        return (int) (mmValue * scale * (1.0f / 25.4f) + 0.5);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 millimeters
     */
    public static int px2mm(Context context, double pxValue) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        return (int) (pxValue / scale / (1.0f / 25.4f) + 0.5);
    }
}
