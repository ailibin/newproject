package com.splant.smartgarden.daoModel.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by aifengbin on 2017/3/13.
 */

@Entity
public class UnitSingleModel implements Serializable {
    private String DbId;
    private String UnitId;
    private String UnitName;
    private String AreaId;
    private String AreaName;
    private String PlantId;
    private String PlantName;
    private String PlantType;
    private Integer PlantCount;
    private Long LastWaterTime;
    private String Descript;
    private String WaterId;
    private String WaterName;
    private String WaterSwitch;
    private String ImageUrl;
    private Integer State;
    private String PowerEmery;
    private String DeviceType;
    private Integer Alarm;
    private String MapCoord;
    private String MapX;
    private String MapY;
    private String ClientId;
    private String GatewayId;
    private String GatewayName;

    public String getGatewayName() {
        return this.GatewayName;
    }

    public void setGatewayName(String GatewayName) {
        this.GatewayName = GatewayName;
    }

    public String getGatewayId() {
        return this.GatewayId;
    }

    public void setGatewayId(String GatewayId) {
        this.GatewayId = GatewayId;
    }

    public String getClientId() {
        return this.ClientId;
    }

    public void setClientId(String ClientId) {
        this.ClientId = ClientId;
    }

    public String getMapY() {
        return this.MapY;
    }

    public void setMapY(String MapY) {
        this.MapY = MapY;
    }

    public String getMapX() {
        return this.MapX;
    }

    public void setMapX(String MapX) {
        this.MapX = MapX;
    }

    public String getMapCoord() {
        return this.MapCoord;
    }

    public void setMapCoord(String MapCoord) {
        this.MapCoord = MapCoord;
    }

    public Integer getAlarm() {
        return this.Alarm;
    }

    public void setAlarm(Integer Alarm) {
        this.Alarm = Alarm;
    }

    public String getDeviceType() {
        return this.DeviceType;
    }

    public void setDeviceType(String DeviceType) {
        this.DeviceType = DeviceType;
    }

    public String getPowerEmery() {
        return this.PowerEmery;
    }

    public void setPowerEmery(String PowerEmery) {
        this.PowerEmery = PowerEmery;
    }

    public Integer getState() {
        return this.State;
    }

    public void setState(Integer State) {
        this.State = State;
    }

    public String getImageUrl() {
        return this.ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getWaterSwitch() {
        return this.WaterSwitch;
    }

    public void setWaterSwitch(String WaterSwitch) {
        this.WaterSwitch = WaterSwitch;
    }

    public String getWaterName() {
        return this.WaterName;
    }

    public void setWaterName(String WaterName) {
        this.WaterName = WaterName;
    }

    public String getWaterId() {
        return this.WaterId;
    }

    public void setWaterId(String WaterId) {
        this.WaterId = WaterId;
    }

    public String getDescript() {
        return this.Descript;
    }

    public void setDescript(String Descript) {
        this.Descript = Descript;
    }

    public Long getLastWaterTime() {
        return this.LastWaterTime;
    }

    public void setLastWaterTime(Long LastWaterTime) {
        this.LastWaterTime = LastWaterTime;
    }

    public Integer getPlantCount() {
        return this.PlantCount;
    }

    public void setPlantCount(Integer PlantCount) {
        this.PlantCount = PlantCount;
    }

    public String getPlantType() {
        return this.PlantType;
    }

    public void setPlantType(String PlantType) {
        this.PlantType = PlantType;
    }

    public String getPlantName() {
        return this.PlantName;
    }

    public void setPlantName(String PlantName) {
        this.PlantName = PlantName;
    }

    public String getPlantId() {
        return this.PlantId;
    }

    public void setPlantId(String PlantId) {
        this.PlantId = PlantId;
    }

    public String getAreaName() {
        return this.AreaName;
    }

    public void setAreaName(String AreaName) {
        this.AreaName = AreaName;
    }

    public String getAreaId() {
        return this.AreaId;
    }

    public void setAreaId(String AreaId) {
        this.AreaId = AreaId;
    }

    public String getUnitName() {
        return this.UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    public String getUnitId() {
        return this.UnitId;
    }

    public void setUnitId(String UnitId) {
        this.UnitId = UnitId;
    }

    public String getDbId() {
        return this.DbId;
    }

    public void setDbId(String DbId) {
        this.DbId = DbId;
    }

    @Generated(hash = 1502774873)
    public UnitSingleModel(String DbId, String UnitId, String UnitName,
                           String AreaId, String AreaName, String PlantId, String PlantName,
                           String PlantType, Integer PlantCount, Long LastWaterTime,
                           String Descript, String WaterId, String WaterName, String WaterSwitch,
                           String ImageUrl, Integer State, String PowerEmery, String DeviceType,
                           Integer Alarm, String MapCoord, String MapX, String MapY,
                           String ClientId, String GatewayId, String GatewayName) {
        this.DbId = DbId;
        this.UnitId = UnitId;
        this.UnitName = UnitName;
        this.AreaId = AreaId;
        this.AreaName = AreaName;
        this.PlantId = PlantId;
        this.PlantName = PlantName;
        this.PlantType = PlantType;
        this.PlantCount = PlantCount;
        this.LastWaterTime = LastWaterTime;
        this.Descript = Descript;
        this.WaterId = WaterId;
        this.WaterName = WaterName;
        this.WaterSwitch = WaterSwitch;
        this.ImageUrl = ImageUrl;
        this.State = State;
        this.PowerEmery = PowerEmery;
        this.DeviceType = DeviceType;
        this.Alarm = Alarm;
        this.MapCoord = MapCoord;
        this.MapX = MapX;
        this.MapY = MapY;
        this.ClientId = ClientId;
        this.GatewayId = GatewayId;
        this.GatewayName = GatewayName;
    }

    @Generated(hash = 402500729)
    public UnitSingleModel() {
    }
}
