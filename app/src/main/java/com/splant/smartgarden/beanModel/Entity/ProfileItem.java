package com.splant.smartgarden.beanModel.Entity;

/**
 *  Created by aifengbin on 2017/3/9.
 */
public class ProfileItem {

    private int mId;
    private int mIconResId;
    private String mTitle;
    private String mDescript;

    public ProfileItem(int id, int iconResId, String title, String descript) {
        mId = id;
        mIconResId = iconResId;
        mTitle = title;
        mDescript = descript;
    }

    public int getIconResId() {
        return mIconResId;
    }

    public void setIconResId(int iconResId) {
        mIconResId = iconResId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescript() {
        return mDescript;
    }

    public void setDescript(String descript) {
        mDescript = descript;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
