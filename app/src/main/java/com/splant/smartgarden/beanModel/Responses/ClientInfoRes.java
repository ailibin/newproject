package com.splant.smartgarden.beanModel.Responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.splant.smartgarden.baseModel.BaseRes;

/**
 * 客户资料
 * Created by KeepCoding on 15/7/2.
 */
public class ClientInfoRes extends BaseRes implements Parcelable {

    @SerializedName("ClientId")
    public String clientId;
    @SerializedName("Title")
    public String title;
    @SerializedName("Descript")
    public String descript;
    @SerializedName("Logo")
    public String logoUrl;
    @SerializedName("Map")
    public String mapUrl;
    @SerializedName("City")
    public String city;
    @SerializedName("IP")
    public String ip;

    public int isAuthUnit = 0;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.clientId);
        dest.writeString(this.title);
        dest.writeString(this.descript);
        dest.writeString(this.logoUrl);
        dest.writeString(this.mapUrl);
        dest.writeString(this.city);
        dest.writeString(this.ip);
        dest.writeInt(this.isAuthUnit);
    }

    public ClientInfoRes() {
    }

    protected ClientInfoRes(Parcel in) {
        this.clientId = in.readString();
        this.title = in.readString();
        this.descript = in.readString();
        this.logoUrl = in.readString();
        this.mapUrl = in.readString();
        this.city = in.readString();
        this.ip = in.readString();
        this.isAuthUnit = in.readInt();
    }

    public static final Creator<ClientInfoRes> CREATOR = new Creator<ClientInfoRes>() {
        public ClientInfoRes createFromParcel(Parcel source) {
            return new ClientInfoRes(source);
        }

        public ClientInfoRes[] newArray(int size) {
            return new ClientInfoRes[size];
        }
    };
}
