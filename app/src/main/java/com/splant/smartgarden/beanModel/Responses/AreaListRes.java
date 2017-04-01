package com.splant.smartgarden.beanModel.Responses;

import com.google.gson.annotations.SerializedName;
import com.splant.smartgarden.baseModel.BaseRes;

import java.util.List;

/**
 * 区域列表
 * Created by KeepCoding on 15/7/2.
 */
public class AreaListRes extends BaseRes {

    @SerializedName("AreaInfo")
    public List<AreaInfo> areaInfos;
}
