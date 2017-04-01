package com.splant.smartgarden.beanModel.Responses;

import com.google.gson.annotations.SerializedName;
import com.splant.smartgarden.baseModel.BaseRes;

/**
 * app 介绍内容
 * Created by KeepCoding on 15/9/10.
 */
public class ContentRes extends BaseRes {

    @SerializedName("Content")
    public String text;
}
