package com.zzti.lsy.ninetingapp.utils;

import com.zzti.lsy.ninetingapp.App;


//常用单位转换的辅助类

public class DensityUtils {

    private DensityUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");

    }


    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue //     * @param scale
     *                （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dp(float pxValue) {
        final float scale = App.get().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue //     * @param scale
     *                 （DisplayMetrics类中属性density）
     * @return
     */
    public static int dp2px(float dipValue) {
        final float scale = App.get().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue //     * @param fontScale
     *                （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = App.get().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue //     * @param fontScale
     *                （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = App.get().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}