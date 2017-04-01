package com.splant.smartgarden.beanModel.Responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.splant.smartgarden.baseModel.BaseRes;

import java.util.List;

/**
 * 植物种类
 * Created by KeepCoding on 15/7/3.
 */
public class PlantTypeRes extends BaseRes {

    @SerializedName("PlantType")
    public List<PlantType> plantTypes;

    public static class PlantType implements Parcelable {
        @SerializedName("Title")
        public String title;
        @SerializedName("TypeId")
        public String typeId;
        @SerializedName("Img")
        public String img;
        @SerializedName("NameEn")
        public String nameEn;
        @SerializedName("plantCount")
        public String mPlantCount;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.typeId);
            dest.writeString(this.img);
            dest.writeString(this.nameEn);
            dest.writeString(this.mPlantCount);
        }

        public PlantType() {}

        protected PlantType(Parcel in) {
            this.title = in.readString();
            this.typeId = in.readString();
            this.img = in.readString();
            this.nameEn = in.readString();
            this.mPlantCount = in.readString();
        }

        public static final Creator<PlantType> CREATOR = new Creator<PlantType>() {
            public PlantType createFromParcel(Parcel source) {
                return new PlantType(source);
            }

            public PlantType[] newArray(int size) {
                return new PlantType[size];
            }
        };
    }

}
