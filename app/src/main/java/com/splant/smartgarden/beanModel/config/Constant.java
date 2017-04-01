package com.splant.smartgarden.beanModel.config;

/**
 * 常量
 */
public class Constant {

    public static  String TOKEN ="";
    public static final String APP_ROOT_PATH = "/SmartPlant/";

    public static final String IMAGE_DIR_PATH = APP_ROOT_PATH + "image/";

    public static class Config {

        public static final String PREFERENCE_NAME = "sPlantBusiness";

    }

    /**
     * 本地缓存参数
     */
    public static class SpfManagerParams {

        public static final String DEVICE_TOKEN = "devToken";
        public static final String LOCAL_LANGUAGE = "localLanguage";
        public static final String Longitude = "longitude";
        public static final String Latitude = "latitude";
        public static final String CUR_ADDRESS = "curAddr";
        public static final String TOKEN = "token";
        public static final String UNIQUEID = "uniqueId";
        //是否有权限编辑节点
        public static final String IsSupperMaster = "master";
        //客户端IP
        public static final String LocalServerIP = "localServerIP";
        //用户ID
        public static final String UserID = "userId";
        //客户端ID
        public static final String ClientID = "clientId";
        //公司LOGO
        public static final String CompanyLogoUrl = "companyLogo";
        public static final String CompanyName = "companyName";
        public static final String CityName = "cityName";

        public static final String ScreenW = "screenW";
        public static final String ScreenH = "screenH";
        //客户端是否能链接并通信
        public static final String isLocalInternet = "local";
        public static final String isLocal = "islocal";


    }

    public static class ResultCode {
        public static final int SelectWaterUnit = 0x0080;
        public static final int SelectArea = 0x0010;
        public static final int SelectGateway = 0x0013;
        public static final int SelectPlantType = 0x0011;
        public static final int SelectPlant = 0x0012;
    }

}
