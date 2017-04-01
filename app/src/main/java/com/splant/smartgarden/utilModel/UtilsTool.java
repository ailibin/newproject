package com.splant.smartgarden.utilModel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.text.TextUtils;

import com.splant.smartgarden.R;
import com.splant.smartgarden.beanModel.Responses.UnitListRes;


/**
 * Created by aifengbin on 2017/3/14.
 */

public class UtilsTool {
    /**
     * 获取警告颜色
     *
     * @param alarm
     * @return 颜色资源 id
     */
    @ColorRes
    public static int getAlarmColorResId(int alarm) {
        switch (alarm) {
            case UnitListRes.UnitInfo.ALARM_GOOD:
                return R.color.alarm_good;
            case UnitListRes.UnitInfo.ALARM_MIDDLE:
                return R.color.alarm_middle;
            case UnitListRes.UnitInfo.ALARM_BAD:
                return R.color.alarm_bad;
            default:
                return R.color.alarm_good;
        }
    }

    /**
     * 设置电池电量的图形
     * @param powerValue
     * @param deviceType
     * @param context
     * @return
     */
    public static Drawable setPowerEnery(String powerValue, String deviceType, Context context)
    {

        if (powerValue==null||deviceType==null|| TextUtils.isEmpty(powerValue)||TextUtils.isEmpty(deviceType))
        {
            return context.getResources().getDrawable(R.mipmap.icon_electricity_unknow);
        }
        else
        {
            float power=0;
            try
            {
                power=Float.parseFloat(powerValue);
            }
            catch (Exception e)
            {
                return context.getResources().getDrawable(R.mipmap.icon_electricity_unknow);
            }
            if (deviceType.startsWith("B1"))
            {
                if (power>0)
                    return context.getResources().getDrawable(R.mipmap.icon_electricity_100);
                else
                    return context.getResources().getDrawable(R.mipmap.icon_electricity_unknow);
            }
            else if (deviceType.startsWith("B2")||deviceType.startsWith("B3"))
            {
                if (power<=30)
                {
                    return context.getResources().getDrawable(R.mipmap.icon_electricity_0);
                }
                else if (power>=40)
                {
                    return context.getResources().getDrawable(R.mipmap.icon_electricity_100);
                }
                else
                {
                    float total=40-30;
                    float value=power-30;
                    float pro=value/total;

                    if (pro<=12.5f)
                        return context.getResources().getDrawable(R.mipmap.icon_electricity_0);
                    else if (pro>12.5f&&pro<=37.5)
                    {
                        return context.getResources().getDrawable(R.mipmap.icon_electricity_25);
                    }
                    else if (pro>37.5f&&pro<=62.5f)
                    {
                        return context.getResources().getDrawable(R.mipmap.icon_electricity_50);
                    }
                    else if (pro>62.5f&&pro<=87.5f)
                    {
                        return context.getResources().getDrawable(R.mipmap.icon_electricity_75);
                    }
                    else
                    {
                        return context.getResources().getDrawable(R.mipmap.icon_electricity_100);
                    }

                }
            }
            else if (deviceType.startsWith("B7"))
            {
                if (power>0)
                {
                    return context.getResources().getDrawable(R.mipmap.icon_electricity_100);
                }
                else
                {
                    return context.getResources().getDrawable(R.mipmap.icon_electricity_0);
                }
            }
            else
            {
                return context.getResources().getDrawable(R.mipmap.icon_electricity_unknow);
            }

        }
    }

    /**
     * 设置语言
     *
     * @param locale
     */
    public static String changeLanguage(Context context, String locale) {
        String result = "1";
        if (("zh_CN").equals(locale)) {
            result = "1";
        }  else if (("en_US").equals(locale)) {
            result = "2";
        }
        return result;
    }
}

