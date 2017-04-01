package com.splant.smartgarden.beanModel.Responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.splant.smartgarden.baseModel.BaseRes;

import java.util.List;

/**
 * 植物列表
 * Created by KeepCoding on 15/7/3.
 */
public class PlantListRes extends BaseRes {

    @SerializedName("PlantInfo")
    public List<PlantInfo> plantInfos;

    public static class PlantInfo implements Parcelable {

        @Expose(serialize = false, deserialize = false)
        public long dbId;
        @SerializedName("Title")
        public String title;//TODO 2016/7/1 去掉 final 原： public final String title;
        @SerializedName("PlantType")
        public String plantType;
        @SerializedName("Descript")
        public String descript;
        @SerializedName("SoilTemp")
        public String soilTemp;//土壤温度
        @SerializedName("SoilHumid")
        public String soilHumid;//土壤湿度
        @SerializedName("SoilNutr")
        public String soilNutr;//土壤养分
        @SerializedName("SoilPH")
        public String soilPH;
        @SerializedName("EnviTemp")
        public String enviTemp;//环境温度
        @SerializedName("EnviHumid")
        public String enviHumid;//环境湿度
        @SerializedName("EnviShine")
        public String enviShine;//环境光照
        public String image;
        @SerializedName("Pinyin")
        public String pinyin;
        @SerializedName("Latin")
        public String latin;
        @SerializedName("PlantId")
        public String plantId;

        public PlantInfo(String title, String plantType, String descript, String soilTemp, String soilHumid, String soilNutr,
                         String soilPH, String enviTemp, String enviHumid, String enviShine, String image, String pinyin, String latin, String plantId) {
            this.title = title;
            this.plantType = plantType;
            this.descript = descript;
            this.soilTemp = soilTemp;
            this.soilHumid = soilHumid;
            this.soilNutr = soilNutr;
            this.soilPH = soilPH;
            this.enviTemp = enviTemp;
            this.enviHumid = enviHumid;
            this.enviShine = enviShine;
            this.image = image;
            this.pinyin = pinyin;
            this.latin = latin;
            this.plantId = plantId;
        }
        public PlantInfo(){
            this.title = "";
            this.plantType = "";
            this.descript = "";
            this.soilTemp = "";
            this.soilHumid = "";
            this.soilNutr = "";
            this.soilPH = "";
            this.enviTemp = "";
            this.enviHumid = "";
            this.enviShine = "";
            this.image = "";
            this.pinyin = "";
            this.latin = "";
            this.plantId = "";
        }

        public PlantInfo(long dbId, String title, String plantType, String descript, String soilTemp, String soilHumid,
                         String soilNutr, String soilPH, String enviTemp, String enviHumid, String enviShine, String image, String pinyin, String latin, String plantId) {
            this.dbId = dbId;
            this.title = title;
            this.plantType = plantType;
            this.descript = descript;
            this.soilTemp = soilTemp;
            this.soilHumid = soilHumid;
            this.soilNutr = soilNutr;
            this.soilPH = soilPH;
            this.enviTemp = enviTemp;
            this.enviHumid = enviHumid;
            this.enviShine = enviShine;
            this.image = image;
            this.pinyin = pinyin;
            this.latin = latin;
            this.plantId = plantId;
        }
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.dbId);
            dest.writeString(this.title);
            dest.writeString(this.plantType);
            dest.writeString(this.descript);
            dest.writeString(this.soilTemp);
            dest.writeString(this.soilHumid);
            dest.writeString(this.soilNutr);
            dest.writeString(this.soilPH);
            dest.writeString(this.enviTemp);
            dest.writeString(this.enviHumid);
            dest.writeString(this.enviShine);
            dest.writeString(this.image);
            dest.writeString(this.pinyin);
            dest.writeString(this.latin);
            dest.writeString(this.plantId);
        }
        protected PlantInfo(Parcel in) {
            this.dbId = in.readLong();
            this.title = in.readString();
            this.plantType = in.readString();
            this.descript = in.readString();
            this.soilTemp = in.readString();
            this.soilHumid = in.readString();
            this.soilNutr = in.readString();
            this.soilPH = in.readString();
            this.enviTemp = in.readString();
            this.enviHumid = in.readString();
            this.enviShine = in.readString();
            this.image = in.readString();
            this.pinyin = in.readString();
            this.latin = in.readString();
            this.plantId = in.readString();
        }

        public static final Creator<PlantInfo> CREATOR = new Creator<PlantInfo>() {
            public PlantInfo createFromParcel(Parcel source) {
                return new PlantInfo(source);
            }
            public PlantInfo[] newArray(int size) {
                return new PlantInfo[size];
            }
        };

    }


}
