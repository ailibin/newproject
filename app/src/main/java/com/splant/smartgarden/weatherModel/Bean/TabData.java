package com.splant.smartgarden.weatherModel.Bean;

/**
 * Created by aifengbin on 2017/3/15.
 */

public class TabData {

    private String mWeek;
    private String mDate;

    public TabData(String week, String date) {
        mWeek = week;
        mDate = date;
    }
    public TabData(){}

    public String getmWeek() {
        return mWeek;
    }

    public void setmWeek(String mWeek) {
        this.mWeek = mWeek;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
