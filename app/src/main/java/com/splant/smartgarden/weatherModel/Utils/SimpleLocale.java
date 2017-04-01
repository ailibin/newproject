package com.splant.smartgarden.weatherModel.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aifengbin on 17/3/18.
 */
public class SimpleLocale {
    private static SimpleLocale ourInstance = new SimpleLocale();

    public static SimpleLocale getInstance() {
        return ourInstance;
    }

    private SimpleLocale() {
    }

    private Locale mLocale;

    public void setLocale(Locale locale) {
        mLocale = locale;
    }

    public Locale getLocale() {
        if (mLocale == null) {
            mLocale = Locale.getDefault();
        }
        return mLocale;
    }

    private static final SimpleDateFormat FORMAT_CN = new SimpleDateFormat("yyyy年MM月", Locale.getDefault());
    private static final SimpleDateFormat FORMAT_EN = new SimpleDateFormat("LLLL yyyy", Locale.getDefault());

    public String getMonthAndYearString(Date date) {
        if (Locale.CHINESE.equals(mLocale)) {
            return FORMAT_CN.format(date);
        } else {
            return FORMAT_EN.format(date);
        }
    }

    /**
     * @author:aifengbin
     *
     * @return
     */
    public String getTimeZoneID() {
        return "GMT-8";
    }
}
