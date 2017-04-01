package com.splant.smartgarden.uiModel.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.splant.smartgarden.R;
import com.splant.smartgarden.baseModel.BaseFragment;
import com.splant.smartgarden.customModel.RecycleViewDivider;
import com.splant.smartgarden.daoModel.Entity.UnitSingleModel;
import com.splant.smartgarden.listenerModel.UnitOnClickListener;
import com.splant.smartgarden.plantModel.Adapter.UnitListRecycleAdapter;
import com.splant.smartgarden.utilModel.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by aifengbin on 2017/3/8.
 * 植物页面
 */

public class PlantFragment extends BaseFragment {

    @Bind(R.id.ib_list)
    TextView radioList;
    @Bind(R.id.btn_filter)
    ImageButton btnFilter;

    @Bind(R.id.top_bar)
    LinearLayout topBar;
    @Bind(R.id.all_plants)
    TextView allPlants;
    @Bind(R.id.unit_list_total_count)
    TextView mTotalText;

    @Bind(R.id.unit_list_layout)
    LinearLayout unitListLayout;
    @Bind(R.id.list_refreshlayout)
    SwipeRefreshLayout listRefreshlayout;
    @Bind(R.id.list_recycleview)
    RecyclerView recyclerView;
    @Bind(R.id.list_multistateview)
    FrameLayout listMultistateview;



    private static final String PLANT = "plant";
    private String mParam;
    private LinearLayoutManager layoutManager;
    private boolean isUnitListView = false;
    private UnitListRecycleAdapter recycleAdapter;
    private List<UnitSingleModel> datas;
    private final int REFRESH_TIME = 2000;

    public static PlantFragment newInstance(String param) {
        PlantFragment fragment = new PlantFragment();
        Bundle args = new Bundle();
        args.putString(PLANT, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(PLANT);
        }
    }

    /**
     * 页面刷新调用
     */
    @Override
    protected void Refresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gaiaa_fragment_plant, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        datas = new ArrayList<>();
        initRecycleData();
        initRecycleList();
        initOnClickListener();
    }

    /**
     * 模拟展示一下数据
     */
    private void initRecycleData() {
        for (int i = 0; i < 5; i++) {
            UnitSingleModel model = new UnitSingleModel();
            model.setAreaName("江苏" + i);
            model.setPlantName("仙人掌" + i);
            model.setUnitName("开关" + i);
            datas.add(model);
        }
    }

    //按钮的点击事件
    @OnClick({R.id.donut_progress_good, R.id.donut_progress_middle, R.id.donut_progress_bad})
    void clickEvent(View v) {
        switch (v.getId()) {
            //良好
            case R.id.donut_progress_good:
                break;
            //一般
            case R.id.donut_progress_middle:
                break;
            //异常
            case R.id.donut_progress_bad:
                break;
        }
    }


    /**
     * 展示节点列表
     */
    private void initRecycleList() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        recycleAdapter = new UnitListRecycleAdapter(getActivity());
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.setUnitListData(datas);
        setRefresh();
    }

    /**
     * 设置listItem的监听
     */
    private void initOnClickListener() {
        recycleAdapter.setOnClickListener(new UnitOnClickListener() {
            @Override
            public void onItemClick(int position, UnitSingleModel unitInfo) {
                ToastUtil.toastShort(getActivity(),"当前点击的是UnitSingleModel:"+position);
            }
        });
    }

    private void setRefresh() {
        //刷新
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
                        listRefreshlayout.setRefreshing(false);
                    }
                }, REFRESH_TIME);
            }
        });
    }
}
