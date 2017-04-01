package com.splant.smartgarden.beanModel.Responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 区域信息
 * Created by KeepCoding on 15/7/2.
 */
public class AreaInfo implements Parcelable {
    @SerializedName("Title")
    public String title;
    @SerializedName("Descript")
    public String descript;
    @SerializedName("AreaId")
    public String areaId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.descript);
        dest.writeString(this.areaId);
    }

    public AreaInfo() {
    }

    protected AreaInfo(Parcel in) {
        this.title = in.readString();
        this.descript = in.readString();
        this.areaId = in.readString();
    }

    public static final Creator<AreaInfo> CREATOR = new Creator<AreaInfo>() {
        public AreaInfo createFromParcel(Parcel source) {
            return new AreaInfo(source);
        }

        public AreaInfo[] newArray(int size) {
            return new AreaInfo[size];
        }
    };
}
