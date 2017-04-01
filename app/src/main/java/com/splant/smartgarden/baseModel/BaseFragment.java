package com.splant.smartgarden.baseModel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.splant.smartgarden.R;
import com.splant.smartgarden.SPlantApplication;
import com.splant.smartgarden.daggerModel.Component.DaggerFragmentComponent;
import com.splant.smartgarden.daggerModel.Component.FragmentComponent;
import com.splant.smartgarden.daggerModel.Module.FragmentModel;
import com.splant.smartgarden.utilModel.ToastUtil;

import butterknife.ButterKnife;

/**
 * Created by aifengbin on 2017/3/8.
 */

public abstract class BaseFragment extends Fragment implements BaseView {

    public Context mContext;
    protected boolean NeedRefresh = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NeedRefresh) {
            Refresh();
            NeedRefresh = false;
        }
    }

    public FragmentComponent getFragmentComponent(){
        return  DaggerFragmentComponent.builder()
                .appComponent(SPlantApplication.getInstance().getAppComponent())
                .fragmentModel(getFragmentModule())
                .build();
    }
    public FragmentModel getFragmentModule(){
        return new FragmentModel(this);
    }


    @Override
    public void ShowToast(int msgId) {
        ToastUtil.toastShort(mContext, msgId);
    }

    @Override
    public void Jump2Activity(Class ActivityClass) {
        Intent intent = new Intent(mContext, ActivityClass);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setNeedRefresh() {
        NeedRefresh = true;
    }

    //通知子类去刷新数据
    protected abstract void Refresh();
}
