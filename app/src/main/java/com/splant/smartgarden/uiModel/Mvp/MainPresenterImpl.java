package com.splant.smartgarden.uiModel.Mvp;

import com.loopj.android.http.AsyncHttpClient;
import com.splant.smartgarden.baseModel.BasePresenterImpl;

/**
 * Created by aifengbin on 2017/3/10.
 */

public class MainPresenterImpl extends BasePresenterImpl<MainView> {
    //这里主要处理网络数据
    private AsyncHttpClient client;

    public MainPresenterImpl(AsyncHttpClient client) {
        this.client = client;
    }


}
