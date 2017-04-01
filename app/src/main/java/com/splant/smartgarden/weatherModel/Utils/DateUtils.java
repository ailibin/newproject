package com.splant.smartgarden.weatherModel.Utils;

import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * 日期
 * Created by aifengbin on 17/3/12.
 */
public class DateUtils {

    /**
     * 判断是否同一天
     *
     * @param one
     * @param another
     * @return true 同一天
     */
    public static boolean isSameDay(@NonNull Calendar one, @NonNull Calendar another) {
        return one.get(Calendar.YEAR) == another.get(Calendar.YEAR) && one.get(Calendar.MONTH) == another.get(Calendar.MONTH)
                && one.get(Calendar.DAY_OF_MONTH) == another.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isToday(@NonNull Calendar calendar) {
        Calendar today = Calendar.getInstance();
        return isSameDay(today, calendar);
    }

}
