package com.splant.smartgarden.utilModel;

import android.content.res.Resources;

/**
 * 屏幕相关工具
 * Created by aifengbin on 17/3/13.
 */
public class DisplayUtils {


    public static float dp2px(Resources resources, float dp) {
        float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5F;
    }

    public static float px2dp(Resources resources, float px) {
        float scale = resources.getDisplayMetrics().density;
        return px / scale + 0.5F;
    }

    public static float sp2px(Resources resources, float sp) {
        float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale + 0.5F;
    }

    public static int getDisplayWidth(Resources resources) {

        return resources.getDisplayMetrics().widthPixels;
    }

    public static int getDisplayHeight(Resources resources) {
        return resources.getDisplayMetrics().heightPixels;
    }
}
