package com.splant.smartgarden.uiModel.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.splant.smartgarden.R;
import com.splant.smartgarden.baseModel.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aifengbin on 2017/3/8.
 * 植物类别界面(资料界面)
 */

public class PlantTypeFragment extends BaseFragment {

    @Bind(R.id.search_bar)
    LinearLayout searchBar;
    @Bind(R.id.list_recycleview)
    RecyclerView listRecycleview;
    @Bind(R.id.list_refreshlayout)
    SwipeRefreshLayout listRefreshlayout;

    private static final String METIERA = "metiera";
    private String mParam;
    private final int REFRESH_DELAY_TIME = 2000;

    public static PlantTypeFragment newInstance(String param) {
        PlantTypeFragment fragment = new PlantTypeFragment();
        Bundle args = new Bundle();
        args.putString(METIERA, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(METIERA);
        }
    }

    @Override
    protected void Refresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gaiaa_fragment_material, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设置下拉刷新颜色
        listRefreshlayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);

        listRefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //延时处理数据刷新
                        listRefreshlayout.setRefreshing(false);
                    }
                }, REFRESH_DELAY_TIME);

            }
        });

    }
}
