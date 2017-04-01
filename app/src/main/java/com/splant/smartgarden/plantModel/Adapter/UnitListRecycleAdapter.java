package com.splant.smartgarden.plantModel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.splant.smartgarden.R;
import com.splant.smartgarden.baseModel.BaseRecyclerAdapter;
import com.splant.smartgarden.baseModel.BaseViewHolder;
import com.splant.smartgarden.daoModel.Entity.UnitSingleModel;
import com.splant.smartgarden.daoModel.Entity.UnitWaterModel;
import com.splant.smartgarden.listenerModel.UnitOnClickListener;
import com.splant.smartgarden.listenerModel.UnitWaterOnClickListener;

import java.util.List;

import butterknife.Bind;

/**
 * 首页节点列表 适配器
 * Created by aifengbin on 17/3/15.
 */
public class UnitListRecycleAdapter extends BaseRecyclerAdapter<Object, BaseViewHolder> {

    private Context mContext;
    private List<UnitSingleModel> unitList = null;
    private List<UnitWaterModel> waterList = null;

    private int unitCount = 0;
    private int waterCount = 0;

    public UnitListRecycleAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * 清空数据
     */
    public void Clear() {
        if (unitList != null) {
            this.unitList.clear();
            this.unitCount = 0;
        }
        if (waterList != null) {
            this.waterList.clear();
            this.waterCount = 0;
        }
    }

    /**
     * 设置数据源
     *
     * @param list
     * @param
     */
    public void setUnitListData(List<UnitSingleModel> list) {
        this.unitList = list;
        unitCount = unitList.size();
    }

    //节点列表点击
    private UnitOnClickListener mOnClickListener;

    public void setOnClickListener(UnitOnClickListener listener) {
        this.mOnClickListener = listener;
    }

    //浇灌列表点击
    private UnitWaterOnClickListener mWaterOnClickListener;

    public void setOnClickListener(UnitWaterOnClickListener listener) {
        this.mWaterOnClickListener = listener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.plant_listitem_unit, parent, false);
        return new UnitViewHolder(view);
    }

    // 检测器:绑定ViewHolder(onBindViewHolder方法负责将数据与ViewHolder绑定)
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        UnitSingleModel unitInfo = unitList.get(position);
        ((UnitViewHolder) holder).listItemUnitPlantName.setText(unitInfo.getPlantName());
        ((UnitViewHolder) holder).listItemUnitName.setText(unitInfo.getUnitName());
        ((UnitViewHolder) holder).listItemUnitArea.setText(unitInfo.getAreaName());
    }

    @Override
    public int getItemCount() {
        return unitCount;
    }

    /**
     * 浇灌设备
     */
    public class WaterViewHolder extends BaseViewHolder implements View.OnClickListener {

        @Bind(R.id.list_item_unit_status)
        View waterStatus;
        @Bind(R.id.list_item_unit_plant_image)
        ImageView waterPlantImage;
        @Bind(R.id.list_item_unit_area)
        TextView waterArea;
        @Bind(R.id.list_item_unit_plant_name)
        TextView waterPlantName;
        @Bind(R.id.list_item_unit_name)
        TextView listItemUnitAddress;

        @Bind(R.id.list_item_unit_water_name)
        TextView waterName;

        @Bind(R.id.unit_detail_electricity)
        ImageView electricity;

        public WaterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);//TODO 2016.7.20 为浇灌设备添加详情页面添加的
        }

        @Override
        public void onClick(View v) {
            if (mWaterOnClickListener != null) {
                mWaterOnClickListener.onItemClick(getAdapterPosition(), waterList.get(getAdapterPosition() - unitList.size()));
            }
        }
    }

    /**
     * 检测器
     */
    public class UnitViewHolder extends BaseViewHolder implements View.OnClickListener {
        @Bind(R.id.list_item_unit_status)
        View listItemUnitStatus;
        @Bind(R.id.list_item_unit_plant_image)
        ImageView listItemUnitPlantImage;
        @Bind(R.id.list_item_unit_area)
        TextView listItemUnitArea;
        @Bind(R.id.list_item_unit_plant_name)
        TextView listItemUnitPlantName;
        @Bind(R.id.list_item_unit_name)
        TextView listItemUnitName;
        @Bind(R.id.list_item_unit_water_name)
        TextView listItemUnitWaterName;

        @Bind(R.id.unit_detail_electricity)
        ImageView unitDetailElectricity;

        public UnitViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(getAdapterPosition(), unitList.get(getAdapterPosition()));
            }
        }
    }

}

