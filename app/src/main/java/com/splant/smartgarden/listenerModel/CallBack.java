package com.splant.smartgarden.listenerModel;


public interface CallBack<T> {
    void onError(String s);

    void onSuccess(T t);

}
