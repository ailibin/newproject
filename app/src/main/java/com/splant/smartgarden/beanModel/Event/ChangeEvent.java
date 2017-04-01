package com.splant.smartgarden.beanModel.Event;

/**
 * Created by aifengbin on 2017/3/9.
 */

public class ChangeEvent {

    private boolean isInfoChange;
    private String msg;
    private String type;

    public ChangeEvent() {
    }

    public ChangeEvent(boolean isInfoChange) {
        this.isInfoChange = isInfoChange;
    }

    public boolean isInfoChange() {
        return isInfoChange;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
