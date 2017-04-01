package com.splant.smartgarden.beanModel.Responses;

import com.google.gson.annotations.SerializedName;
import com.splant.smartgarden.baseModel.BaseRes;

import java.io.Serializable;
import java.util.List;

/**
 * 天气
 * Created by KeepCoding on 15/7/2.
 */
public class WeatherRes extends BaseRes implements Serializable {

    public WeatherRes(){}  //TODO 2016/7/1 添加

    @SerializedName("Weather")
    public List<WeatherInfo> weatherInfos;

    @SerializedName("Humidity")
    public List<Humidity> humidities;

    @SerializedName("Uv")
    public List<UV> uvs;

    @SerializedName("City")
    public String city;

    /**
     * 天气信息
     */
    public static class WeatherInfo implements Serializable {
        @SerializedName("Date")
        public long date;
        @SerializedName("Wind")
        public String wind;
        @SerializedName("Temp")
        public String temp;
        @SerializedName("Weather")
        public String weather;
        @SerializedName("Upper")
        public String upperTemp;
        @SerializedName("Lower")
        public String lowerTemp;
        @SerializedName("Ultraviolet")
        public String uv;

        /**
         * 风速
         */
        @SerializedName("Ws")
        public String ws;
        @SerializedName("Humidity")
        public String humidity;
        @SerializedName("Image")
        public String image;
        @SerializedName("Icon")
        public String icon;

    }

    /**
     * 湿度
     */
    public static class Humidity implements Serializable {
        @SerializedName("Value")
        public int value;
        @SerializedName("CreateTime")
        public long createTime;
    }

    /**
     * 紫外线
     */
    public static class UV implements Serializable {
        @SerializedName("Value")
        public int value;
        @SerializedName("ValueString")
        public String valueString;
        @SerializedName("CreateTime")
        public long createTime;
    }

}
