package com.splant.smartgarden.uiModel.fragment;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.splant.smartgarden.R;
import com.splant.smartgarden.baseModel.BaseFragment;
import com.splant.smartgarden.beanModel.Entity.ProfileItem;
import com.splant.smartgarden.customModel.RecycleViewDivider;
import com.splant.smartgarden.listenerModel.OnHeaderClickListener;
import com.splant.smartgarden.listenerModel.OnMyItemClickListener;
import com.splant.smartgarden.profileModel.Activity.AboutCompanyActivity;
import com.splant.smartgarden.profileModel.Activity.ChangeLanguageActivity;
import com.splant.smartgarden.profileModel.Activity.ChangePasswordActivity;
import com.splant.smartgarden.profileModel.Activity.UserManualActivity;
import com.splant.smartgarden.profileModel.Adapter.ProfileAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aifengbin on 2017/3/8.
 * 个人中心界面
 */

public class ProfileFragment extends BaseFragment implements OnMyItemClickListener, OnHeaderClickListener {

    @Bind(R.id.profile_recycleview)
    RecyclerView mRecycleView;

    private static final String INFO = "info";
    private String mParam;
    private List<ProfileItem> mProfileItems;
    private View rootView;
    private View headerView;
    private ProfileAdapter profileAdapter;

    public static ProfileFragment newInstance(String param) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(INFO, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(INFO);
        }
    }

    @Override
    protected void Refresh() {
        //使用EventBus来通知界面刷新
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.gaiaa_fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        initData();
        initView();
        return rootView;
    }

    private void initView() {
        profileAdapter = new ProfileAdapter(getActivity(), mProfileItems);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL ));
        mRecycleView.setAdapter(profileAdapter);
        profileAdapter.setOnHeaderClickListener(this);
        profileAdapter.setOnItemClickListener(this);
    }

    private void initData() {
        Resources resources = getResources();
        TypedArray idTypedArray = resources.obtainTypedArray(R.array.profile_id_array);
        TypedArray iconTypedArray = resources.obtainTypedArray(R.array.profile_icon_array);
        String[] titleArray = resources.getStringArray(R.array.profile_title_array);
        String[] descriptArray = resources.getStringArray(R.array.profile_descript_array);
        int length = idTypedArray.length();
        mProfileItems = new ArrayList<>(length);
        ProfileItem item = null;
        for (int i = 0; i < length; i++) {
            int id = idTypedArray.getResourceId(i, 0);
            item = new ProfileItem(id, iconTypedArray.getResourceId(i, 0), titleArray[i], descriptArray[i]);
            mProfileItems.add(item);
        }
        idTypedArray.recycle();
        iconTypedArray.recycle();
    }

    //列表项点击监听
    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case 0:
                //修改密码
                Jump2Activity(ChangePasswordActivity.class);
                break;
            case 1:
                //用户手册
                Jump2Activity(UserManualActivity.class);
                break;
            case 2:
                //切换语言
                Jump2Activity(ChangeLanguageActivity.class);
                break;
            case 3:
                //关于sPlant
                break;
            case 4:
                //定时浇水
                break;
            case 5:
                //退出
                break;
        }
    }

    /**
     * 头部点击事件
     * @param view
     */
    @Override
    public void onRecycleHeadClick(View view) {
        Jump2Activity(AboutCompanyActivity.class);
    }
}
