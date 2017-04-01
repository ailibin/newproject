package com.splant.smartgarden.ApiModel;


import com.splant.smartgarden.baseModel.BaseRes;
import com.splant.smartgarden.beanModel.Responses.AreaListRes;
import com.splant.smartgarden.beanModel.Responses.ClientInfoRes;
import com.splant.smartgarden.beanModel.Responses.ContentRes;
import com.splant.smartgarden.beanModel.Responses.ExplainRes;
import com.splant.smartgarden.beanModel.Responses.LoginRes;
import com.splant.smartgarden.beanModel.Responses.ParaListRes;
import com.splant.smartgarden.beanModel.Responses.PlantListRes;
import com.splant.smartgarden.beanModel.Responses.PlantTypeRes;
import com.splant.smartgarden.beanModel.Responses.UnitListRes;
import com.splant.smartgarden.beanModel.Responses.WeatherRes;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 本地局域网,给水和修改密码只能在本地局域网操作
 */
public interface IServerAPI {

    /**
     * 给水
     * @param clientId
     * @param deviceType
     * @param gateNo
     * @param unitId
     * @param time
     * @return {@link BaseRes}
     */
    @FormUrlEncoded
    @POST("/API/JsonWatering.aspx")
    public BaseRes feedWater(@Field("ClientId") String clientId,
                             @Field("DeviceType") String deviceType,
                             @Field("SwitchNo") String gateNo,
                             @Field("UnitId") String unitId,
                             @Field("Time") String time);

    @FormUrlEncoded
    @POST("/API/JsonRePassword.aspx")
    public BaseRes modifyPassword(@Field("userid") String userId, @Field("old_password") String oldPassword, @Field("new_password") String newPassword);


    @FormUrlEncoded
    @POST("/API/JsonLogin.aspx")
    public LoginRes login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/API/JsonClientId.aspx")
    public ClientInfoRes getClientInfo(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("/API/JsonClient.aspx")
    public AreaListRes getAreaList(@Field("ClientId") String clientId);

    /**
     * 区域管理
     * @param clientId
     * @param action
     * @param areaId
     * @return
     */
    @FormUrlEncoded
    @POST("/API/JsonAreaManager.aspx")
    public AreaListRes getAreaManager(@Field("ClientId") String clientId,
                                      @Field("action") String action,
                                      @Field("areaId") String areaId,
                                      @Field("name") String name,
                                      @Field("descript") String descript);

    @FormUrlEncoded
    @POST("/API/JsonWeather.aspx")
    public WeatherRes getWeather(@Field("clientId") String clientId, @Field("locale") String locale);

    @FormUrlEncoded
    @POST("/API/JsonPlantType.aspx")
    public PlantTypeRes getPlantType(@Field("ClientId") String clientId, @Field("locale") String locale);

    @FormUrlEncoded
    @POST("/API/JsonPlant.aspx")
    public PlantListRes getPlantList(@Field("ClientId") String clientId,
                                     @Field("locale") String locale,
                                     @Field("TypeId") String typeId,
                                     @Field("PinYin") String pinyin,
                                     @Field("Page") int page);

    @FormUrlEncoded
    @POST("/API/JsonCuveApps.aspx")
    public ParaListRes getParaList(@Field("begin") long begin,
                                   @Field("end") long end,
                                   @Field("clientId") String clientId,
                                   @Field("unitid") String unitId,
                                   @Field("locale") String locale);

    @FormUrlEncoded
    @POST("/API/JsonCuveApps.aspx")
    public ParaListRes getParaList(@Field("clientId") String clientId,
                                   @Field("unitid") String unitId,
                                   @Field("locale") String locale);

    @FormUrlEncoded
    @POST("/API/JsonUnit.aspx")
    public UnitListRes getUnitList(@Field("clientid") String clientId,
                                   @Field("AreaId") String areaId,
                                   @Field("PlantTypeId") String plantTypeId,
                                   @Field("Title") String key,
                                   @Field("locale") String locale);

    @FormUrlEncoded
    @POST("/API/JsonExplain.aspx")
    public ExplainRes getExplain(@Field("locale") String locale);

    @FormUrlEncoded
    @POST("/API/JsonContent.aspx")
    public ContentRes getContent(@Field("locale") String locale);

    @FormUrlEncoded
    @POST("/API/JsonUnitOper.aspx")
    public BaseRes unitOp(@Field("action") String action,
                          @Field("deviceType") String deviceType,
                          @Field("user") String userId,
                          @Field("id") String id,
                          @Field("name") String name,
                          @Field("title") String title,
                          @Field("plantId") String plantId,
                          @Field("areaid") String areaId,
                          @Field("amount") int amount,
                          @Field("mapCoord") String mapCoord,
                          @Field("descript") String descript,
                          @Field("waterId") String waterId,
                          @Field("switchNo") String switchNo,
                          @Field("ip") String clientIP);
}
