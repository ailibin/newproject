package com.splant.smartgarden.baseModel;

/**
 * Created by aifengbin on 2017/3/10.
 */

public class BasePresenterImpl <T>{
    protected T view;
    public void attachView(T view) {
        this.view = view;
    }
}
