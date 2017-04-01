package com.splant.smartgarden.beanModel.Responses;

import com.google.gson.annotations.SerializedName;
import com.splant.smartgarden.baseModel.BaseRes;

import java.util.List;

/**
 * 监控器指标数据
 * 图表数据
 * Created by KeepCoding on 15/7/3.
 */
public class ParaListRes extends BaseRes {

    @SerializedName("ParaList")
    public List<Para> paras;
    @SerializedName("State")
    public int state;//给水状态，1 不需要给水，0 需要给水

    public static class Para {
        @SerializedName("ParaInfo")
        public ParaInfo paraInfo;
        @SerializedName("ValueInfo")
        public List<ValueInfo> valueInfos;
    }

    public static class ParaInfo {
        @SerializedName("ParaName")
        public String paraName;
        @SerializedName("DataName")
        public String dataName;
        @SerializedName("Upper")
        public String upper;
        @SerializedName("Lower")
        public String lower;
        @SerializedName("Plant")
        public String plant;
        @SerializedName("UnitTitle")
        public String unitTitle;
        @SerializedName("UnitId")
        public String unitId;
        @SerializedName("Alarm")
        public int alarm;

    }

    public static class ValueInfo {

        @SerializedName("Value")
        public String valueStr;
        @SerializedName("NumValue")
        public float numValue;
        @SerializedName("CreateTime")
        public long createTime;
        @SerializedName("Alarm")
        public int alarm;
    }
}
