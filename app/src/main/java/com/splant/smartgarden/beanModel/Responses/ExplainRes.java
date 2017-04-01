package com.splant.smartgarden.beanModel.Responses;

import com.google.gson.annotations.SerializedName;
import com.splant.smartgarden.baseModel.BaseRes;

/**
 * 使用说明
 * Created by KeepCoding on 15/7/5.
 */
public class ExplainRes extends BaseRes {

    @SerializedName("Content")
    public String content;
}
