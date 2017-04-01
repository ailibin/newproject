package com.splant.smartgarden.beanModel.Entity;

/**
 * 多语言地区
 * Created by aifengbin on 17/3/12.
 */
public enum LocaleEnum {
    CN("zh-CN"), EN("en-US");

    private String value;

    LocaleEnum(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
