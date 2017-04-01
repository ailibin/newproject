package com.splant.smartgarden.beanModel.Responses;


import java.util.List;

/**
 * Created by cyx on 2016/12/13.
 */
public class GatewayRes {

    /**
     * isSuccessed : 1
     * message : 获取网关资料成功
     * data : [{"id":"50ac308b-b6c3-11e6-bfbd-930b2abbed0f","clientId":"01583a13-b6b1-11e6-bfbd-930b2abbed0f","name":"网关B724B729","hardwareId":"ACCF23DE7E90","pan_id":null},{"id":"1ac14930-bb92-11e6-a3bb-f52357173307","clientId":"01583a13-b6b1-11e6-bfbd-930b2abbed0f","name":"网关_手机","hardwareId":"ACCF23DF1CF8","pan_id":null},{"id":"9a07de64-be9b-11e6-a3bb-f52357173307","clientId":"01583a13-b6b1-11e6-bfbd-930b2abbed0f","name":"网关B705","hardwareId":"ACCF23DF1D3A","pan_id":null}]
     */

    private int isSuccessed;
    private String message;
    private List<DataBean> data;

    public int getIsSuccessed() {
        return isSuccessed;
    }

    public void setIsSuccessed(int isSuccessed) {
        this.isSuccessed = isSuccessed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 50ac308b-b6c3-11e6-bfbd-930b2abbed0f
         * clientId : 01583a13-b6b1-11e6-bfbd-930b2abbed0f
         * name : 网关B724B729
         * hardwareId : ACCF23DE7E90
         * pan_id : null
         */

        private String id;
        private String clientId;
        private String name;
        private String hardwareId;
        private Object pan_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHardwareId() {
            return hardwareId;
        }

        public void setHardwareId(String hardwareId) {
            this.hardwareId = hardwareId;
        }

        public Object getPan_id() {
            return pan_id;
        }

        public void setPan_id(Object pan_id) {
            this.pan_id = pan_id;
        }
    }
}

