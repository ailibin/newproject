package com.splant.smartgarden.beanModel.Responses;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.splant.smartgarden.baseModel.BaseRes;

import java.util.List;

/**
 * 监测器列表
 * Created by KeepCoding on 15/7/3.
 */
public class UnitListRes extends BaseRes {

    @SerializedName("UnitInfo")
    public List<UnitInfo> unitInfos;

    @SerializedName("UnitWater")
    public List<UnitWater> unitWaters;

    public static class UnitWater implements Parcelable {

        @SerializedName("UnitId")
        public String dbId;//sqlserver 本地主键ID
        @SerializedName("DeviceId")
        public String deviceId;//设备编号
        @SerializedName("DeviceName")
        public String deviceName;//设备名称
        @SerializedName("CreateTime")
        public String createTime;
        @SerializedName("UserId")
        public String userId;
        @SerializedName("ClientId")
        public String clientId;
        @SerializedName("MapCoord")
        public String mapCoord;//设备坐标
        @SerializedName("Remarks")
        public String remarks;
        @SerializedName("PlantId")
        public String plantId;//植物主键
        @SerializedName("PlantName")
        public String plantName;
        @SerializedName("PlantCount")
        public int plantCount;//植物数量
        @SerializedName("PlantTypeId")
        public String plantTypeId;
        @SerializedName("PlantTypeName")
        public String plantTypeName;
        @SerializedName("AreaId")
        public String areaId;//区域主键
        @SerializedName("AreaName")
        public String areaName;//区域名称
        @SerializedName("PowerEmery")
        public String powerEmery;
        @SerializedName("DeviceType")
        public String deviceType;//节点类型

        public PointF mCoord;

        @SerializedName("GatewayId")
        public String gatewayId;//网关ID                //2016.12.22添加
        @SerializedName("GatewayName")
        public String gatewayName;//网关名称            //2016.12.22添加


        public void checkCoord() {
            if (!TextUtils.isEmpty(mapCoord) && mCoord == null) {
                String[] mapCoords = mapCoord.split(",");
                if (mapCoords.length != 2) {
                    return;
                }
                try {
                    mCoord = new PointF(Float.valueOf(mapCoords[0]), Float.valueOf(mapCoords[1]));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        public UnitWater() {}

        protected UnitWater(Parcel in) {
            this.dbId = in.readString();
            this.deviceId = in.readString();
            this.deviceName = in.readString();
            this.createTime = in.readString();
            this.userId = in.readString();
            this.clientId = in.readString();
            this.mapCoord = in.readString();
            this.remarks = in.readString();
            this.plantId = in.readString();
            this.plantName = in.readString();
            this.plantCount = in.readInt();
            this.plantTypeId = in.readString();
            this.plantTypeName = in.readString();
            this.areaId = in.readString();
            this.areaName = in.readString();
            this.powerEmery = in.readString();
            this.deviceType = in.readString();
            this.mCoord = in.readParcelable(PointF.class.getClassLoader());
            this.gatewayId = in.readString();//2016.12.22添加
            this.gatewayName = in.readString();//2016.12.22添加
        }

        public static final Creator<UnitWater> CREATOR = new Creator<UnitWater>() {
            @Override
            public UnitWater createFromParcel(Parcel in) {
                return new UnitWater(in);
            }

            @Override
            public UnitWater[] newArray(int size) {
                return new UnitWater[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(this.dbId);
            parcel.writeString(this.deviceId);
            parcel.writeString(this.deviceName);
            parcel.writeString(this.createTime);
            parcel.writeString(this.userId);
            parcel.writeString(this.clientId);
            parcel.writeString(this.mapCoord);
            parcel.writeString(this.remarks);
            parcel.writeString(this.plantId);
            parcel.writeString(this.plantName);
            parcel.writeInt(this.plantCount);
            parcel.writeString(this.plantTypeId);
            parcel.writeString(this.plantTypeName);
            parcel.writeString(this.areaId);
            parcel.writeString(this.areaName);
            parcel.writeString(this.powerEmery);
            parcel.writeString(this.deviceType);
            parcel.writeParcelable(this.mCoord, 0);
            parcel.writeString(this.gatewayId);//2016.12.22添加
            parcel.writeString(this.gatewayName);//2016.12.22添加
        }
    }

    public static class UnitInfo implements Parcelable {

        public static final int ALARM_GOOD = Alarm.ALARM_GOOD;
        public static final int ALARM_MIDDLE = Alarm.ALARM_NORMAL;
        public static final int ALARM_BAD = Alarm.ALARM_BAD;

        @Expose(serialize = false, deserialize = false)
        public String dbId;//sqlserver 的主键ID

        @SerializedName("deviceType")
        public String devType;//节点类型

        @SerializedName("Title")
        public String title;//节点ID编号
        @SerializedName("Area")
        public String area;//区域名称
        @SerializedName("Deacrtip")
        public String descript;//备注
        @SerializedName("MapCoord")
        public String mapCoord;//坐标
        @SerializedName("GatewayId")
        public String gatewayId;//网关ID                //2016.12.22添加
        @SerializedName("GatewayName")
        public String gatewayName;//网关名称            //2016.12.22添加
        @SerializedName("LastWarter")
        public long lastWater;//最后浇水时间
        /**
         * 状态 0正常,1提醒 2恶劣
         */
        @SerializedName("Alarm")
        public int alarm;
        @SerializedName("Plant")
        public String plant;//植物名称
        @SerializedName("PlantType")
        public String plantType;//植物类型 名称
        @SerializedName("UnitId")
        public String unitId;//sql server 的主键ID
        @SerializedName("Img")
        public String img;//植物图片
        @SerializedName("Name")
        public String name;//
        @SerializedName("State")
        public int state;//浇水状态 0:未浇水  1:已浇水
        @SerializedName("Amount")
        public int amount;//植物数量
        @SerializedName("PlantId")
        public String plantId;//植物主键
        @SerializedName("AreaId")
        public String areaId;//区域主键

        public PointF mCoord;

        //new add
        @SerializedName("DumoEnergy")
        public String powerEnery;//剩余电量
        @SerializedName("WaterUnitID")
        public String waterUnitId;//浇灌设备主键
        @SerializedName("WaterUnitName")
        public String waterUnitName;//浇灌名称
        @SerializedName("WaterGateNo")
        public String waterGateNo;//浇灌开关编号

        public void checkCoord() {
            if (!TextUtils.isEmpty(mapCoord) && mCoord == null) {
                String[] mapCoords = mapCoord.split(",");
                if (mapCoords.length != 2) {
                    return;
                }
                try {
                    mCoord = new PointF(Float.valueOf(mapCoords[0]), Float.valueOf(mapCoords[1]));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(this.dbId);
            parcel.writeString(this.title);
            parcel.writeString(this.area);
            parcel.writeString(this.descript);
            parcel.writeString(this.mapCoord);
            parcel.writeLong(this.lastWater);
            parcel.writeInt(this.alarm);
            parcel.writeString(this.plant);
            parcel.writeString(this.plantType);
            parcel.writeString(this.unitId);
            parcel.writeString(this.img);
            parcel.writeString(this.name);
            parcel.writeInt(this.state);
            parcel.writeInt(this.amount);
            parcel.writeString(this.plantId);
            parcel.writeString(this.areaId);
            parcel.writeParcelable(this.mCoord, 0);
            parcel.writeString(this.devType);
            parcel.writeString(this.powerEnery);
            parcel.writeString(this.waterUnitId);
            parcel.writeString(this.waterUnitName);
            parcel.writeString(this.waterGateNo);
            parcel.writeString(this.gatewayId);//2016.12.22添加
            parcel.writeString(this.gatewayName);//2016.12.22添加
        }

        public UnitInfo () {}

        public UnitInfo(String title, String areaId, String descript, String mapCoord, Long lastWaterTime,
                        int alarm, String plant, String plantType, String plantId, String unitId, String img,
                        String name, String devType, String gatewayId , String gatewayName) {
            this.title = title;
            this.areaId = areaId;
            this.descript = descript;
            this.mapCoord = mapCoord;
            this.alarm = alarm;
            this.lastWater = lastWaterTime;
            this.plant = plant;
            this.plantType = plantType;
            this.plantId = plantId;
            this.unitId = unitId;
            this.img = img;
            this.name = name;
            this.devType = devType;
            this.gatewayId = gatewayId;//2016.12.22添加
            this.gatewayName = gatewayName;//2016.12.22添加
        }

        protected UnitInfo(Parcel in) {
            this.dbId = in.readString();
            this.title = in.readString();
            this.area = in.readString();
            this.descript = in.readString();
            this.mapCoord = in.readString();
            this.lastWater = in.readLong();
            this.alarm = in.readInt();
            this.plant = in.readString();
            this.plantType = in.readString();
            this.unitId = in.readString();
            this.img = in.readString();
            this.name = in.readString();
            this.state = in.readInt();
            this.amount = in.readInt();
            this.plantId = in.readString();
            this.areaId = in.readString();
            this.mCoord = in.readParcelable(PointF.class.getClassLoader());
            this.devType = in.readString();
            this.powerEnery = in.readString();
            this.waterUnitId = in.readString();
            this.waterUnitName = in.readString();
            this.waterGateNo = in.readString();
            this.gatewayId = in.readString();//2016.12.22添加
            this.gatewayName = in.readString();//2016.12.22添加
        }

        public static final Creator<UnitInfo> CREATOR = new Creator<UnitInfo>() {
            public UnitInfo createFromParcel(Parcel source) {
                return new UnitInfo(source);
            }

            public UnitInfo[] newArray(int size) {
                return new UnitInfo[size];
            }
        };
    }

}
