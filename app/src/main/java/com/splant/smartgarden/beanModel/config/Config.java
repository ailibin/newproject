package com.splant.smartgarden.beanModel.config;


import com.splant.smartgarden.baseModel.BaseActivity;

/**
 * 配置
 * Created by aifengbin on 17/3/10.
 */
public class Config {

    /**
     * 测试服务器地址
     */
//    public static final String REMOTE_SERVER = "http://192.168.1.104:4380";
    /**
     * 正式服务器地址
     * http://splant.sunrace-landscape.com（就是http://120.77.53.132）
     */
    public static final String REMOTE_SERVER = "http://120.77.53.132:3001";
    /**
     * 本地IP地址
     */
      public static final String LOCAL_SERVER = BaseActivity.getClientIPs();


}
