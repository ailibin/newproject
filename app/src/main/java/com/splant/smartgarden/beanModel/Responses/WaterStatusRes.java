package com.splant.smartgarden.beanModel.Responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 浇水状态信息类
 * Created by cyx on 2016/7/27.
 */
public class WaterStatusRes implements Serializable {
    /**
     * 是否成功，1 成功 0 失败
     */
    public int isSuccessed;
    public String Index;
    public String message;

    @SerializedName("WaterStatus")
    public List<WaterStatus> waterStatusList;

    public static class WaterStatus implements Parcelable {
        @SerializedName("Id")
        public String Id ;//记录ID
        @SerializedName("SiteId")
        public String SiteId;
        @SerializedName("CreateTime")
        public String CreateTime;//创建时间
        @SerializedName("ClientId")
        public String ClientId;//客户ID
        @SerializedName("DeviceType")
        public String DeviceType;//设备类型
        @SerializedName("DeviceIdentifierId")
        public String DeviceIdentifierId;//设备ID（GUID）
        @SerializedName("DeviceId")
        public String DeviceId;//设备硬件ID
        @SerializedName("WaterDeviceIdentifierId")
        public String WaterDeviceIdentifierId;//浇灌ID（GUID）
        @SerializedName("WaterDeviceId")
        public String WaterDeviceId;//浇灌硬件ID
        @SerializedName("GateNo")
        public String GateNo;//开关号
        @SerializedName("StartTime")
        public String StartTime;//开始时间
        @SerializedName("SupplyTime")
        public String SupplyTime;//给水时长（秒）
        @SerializedName("UpdateStatusTime")
        public String UpdateStatusTime;//状态更新时间
        @SerializedName("RestTime")
        public String RestTime;//剩余时间（秒）
        @SerializedName("Status")
        public String Status;//状态


        protected WaterStatus(Parcel in) {
            this.Id = in.readString();
            this.SiteId = in.readString();
            this.CreateTime = in.readString();
            this.ClientId = in.readString();
            this.DeviceType = in.readString();
            this.DeviceIdentifierId = in.readString();
            this.DeviceId = in.readString();
            this.WaterDeviceIdentifierId = in.readString();
            this.WaterDeviceId = in.readString();
            this.GateNo = in.readString();
            this.StartTime = in.readString();
            this.SupplyTime = in.readString();
            this.UpdateStatusTime = in.readString();
            this.RestTime = in.readString();
            this.Status = in.readString();

        }
        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.Id);
            parcel.writeString(this.SiteId);
            parcel.writeString(this.CreateTime);
            parcel.writeString(this.ClientId);
            parcel.writeString(this.DeviceType);
            parcel.writeString(this.DeviceIdentifierId);
            parcel.writeString(this.DeviceId);
            parcel.writeString(this.WaterDeviceIdentifierId);
            parcel.writeString(this.WaterDeviceId);
            parcel.writeString(this.GateNo);
            parcel.writeString(this.StartTime);
            parcel.writeString(this.SupplyTime);
            parcel.writeString(this.UpdateStatusTime);
            parcel.writeString(this.RestTime);
            parcel.writeString(this.Status);
        }

        public static final Creator<WaterStatus> CREATOR = new Creator<WaterStatus>() {
            @Override
            public WaterStatus createFromParcel(Parcel in) {
                return new WaterStatus(in);
            }

            @Override
            public WaterStatus[] newArray(int size) {
                return new WaterStatus[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        public WaterStatus(){}
        public WaterStatus(String id , String siteId , String createTime , String clientId , String deviceType  , String deviceIdentifierId , String deviceId , String waterDeviceIdentifierId , String waterDeviceId , String gateNo , String startTime , String supplyTime , String updateStatusTime , String restTime , String status){
            this.Id = id;
            this.SiteId = siteId;
            this.CreateTime = createTime;
            this.ClientId = clientId;
            this.DeviceType = deviceType;
            this.DeviceIdentifierId = deviceIdentifierId;
            this.DeviceId = deviceId;
            this.WaterDeviceIdentifierId = waterDeviceIdentifierId;
            this.WaterDeviceId = waterDeviceId;
            this.GateNo = gateNo;
            this.StartTime = startTime;
            this.SupplyTime = supplyTime;
            this.UpdateStatusTime = updateStatusTime;
            this.RestTime = restTime;
            this.Status = status;
        }
    }
}
