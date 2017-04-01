package com.splant.smartgarden.weatherModel.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.splant.smartgarden.weatherModel.Bean.TabData;
import com.splant.smartgarden.weatherModel.Widget.WeatherTab;

import java.util.ArrayList;

/**
 * Created by aifengbin on 2017/3/15.
 */

public class TabAdapter extends BaseAdapter {

    private ArrayList<TabData> mTabDatas;

    public TabAdapter(ArrayList<TabData> tabDatas) {
        this.mTabDatas = tabDatas;
    }

    @Override
    public int getCount() {
        return mTabDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mTabDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = new WeatherTab(parent.getContext());
            holder = new ViewHolder();
            holder.mWeekText = ((WeatherTab) convertView).getWeekText();
            holder.mDateText = ((WeatherTab) convertView).getDateText();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TabData tabData = mTabDatas.get(position);
        holder.mWeekText.setText(tabData.getmWeek());
        holder.mDateText.setText(tabData.getmDate());
        return convertView;
    }

    class ViewHolder {
        TextView mWeekText;
        TextView mDateText;
    }
}

