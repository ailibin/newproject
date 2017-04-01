package com.splant.smartgarden.beanModel.Responses;

import com.google.gson.annotations.SerializedName;
import com.splant.smartgarden.baseModel.BaseRes;

/**
 * Created by aifengbin on 2017/3/10.
 */

public class LoginRes extends BaseRes {
    @SerializedName("UserId")
    public String userId;
}
