package com.splant.smartgarden.uiModel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.splant.smartgarden.R;
import com.splant.smartgarden.SPlantApplication;
import com.splant.smartgarden.baseModel.BaseFragment;
import com.splant.smartgarden.beanModel.Responses.WeatherRes;
import com.splant.smartgarden.utilModel.ProgressDialogUtil;
import com.splant.smartgarden.weatherModel.Adapter.TabAdapter;
import com.splant.smartgarden.weatherModel.Bean.TabData;
import com.splant.smartgarden.weatherModel.Bean.WeatherDataWrapper;
import com.splant.smartgarden.weatherModel.Loader.BaseLoader;
import com.splant.smartgarden.weatherModel.Loader.WeatherLoader;
import com.splant.smartgarden.weatherModel.Utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aifengbin on 2017/3/8.
 * 天气界面
 */

public class WeatherFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<WeatherRes> {

    private static final String WEATHER = "weather";
    private String mParam;

    @Bind(R.id.temperature)
    TextView mCurrentTempText;//当前温度
    @Bind(R.id.temp_range)
    TextView mTempRangeText;//温度范围
    @Bind(R.id.city_name)
    TextView mCityNameText;//所在城市
    @Bind(R.id.weather_desc)
    TextView mWeatherDescText;//天气描述，多云，
    @Bind(R.id.uv)
    TextView mUVText;
    @Bind(R.id.wind)
    TextView mWindText;//风级
    @Bind(R.id.humidity)
    TextView mHumidityText;//湿度
    @Bind(R.id.bg_weather)
    ImageView mWeatherBg;//背景
    @Bind(R.id.image_weather)
    ImageView mWeatherIcon;//图标
    @Bind(R.id.tabs)
    Gallery mGalleryTab;
    @Bind(R.id.line_chart)
    LineChart mLineChart;
    @Bind(R.id.weather_info_panel)
    View mWeatherInfoPanel;
    @Bind(R.id.refresh_view)
    View mRefreshView;

    private ArrayList<WeatherDataWrapper> mWeatherDataWrappers = new ArrayList<>(7);
    private ArrayList<TabData> mTabDatas = new ArrayList<>(0);
    private TabAdapter mTabAdapter;
    private Loader weatherLoader = null;

    public static WeatherFragment newInstance(String param) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(WEATHER, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(WEATHER);
        }
    }

    @Override
    protected void Refresh() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gaiaa_fragment_weather, container, false);
        ButterKnife.bind(this, view);
        initData();
//        WeatherMarkerView mv = new WeatherMarkerView(getContext(), R.layout.marker_view_layout);
//        mLineChart.setMarkerView(mv);
//        mWeatherInfoPanel.setVisibility(View.GONE);
        mRefreshView.setVisibility(View.GONE);
        mRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogUtil.show(getActivity(), R.string.waiting, true);
                getLoaderManager().restartLoader(WeatherLoader.LOAD_ID, null, WeatherFragment.this);
            }
        });
        return view;
    }

    /**
     * 仅仅为测试用
     */
    private void initData() {
        TabData tabData;
        for (int i = 1; i <=7; i++) {
            tabData = new TabData();
            tabData.setmWeek("周"+i);
            tabData.setmDate("2017.3.0"+i);
            mTabDatas.add(tabData);
        }
        initGallery();
    }

    private void initGallery() {
        mTabAdapter = new TabAdapter(mTabDatas);
        mGalleryTab.setAdapter(mTabAdapter);
        mGalleryTab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSelectedTab != null) {
                    mSelectedTab.animate().scaleX(1.0f).scaleY(1.0f).start();
                }
                view.animate().scaleX(1.5f).scaleY(1.5f).start();
                mSelectedTab = view;
//                setupWeatherInfo(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (weatherLoader == null) {
//            weatherLoader = getLoaderManager().initLoader(WeatherLoader.LOAD_ID, null, this);
//            weatherLoader.forceLoad();
//        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        getLoaderManager().destroyLoader(WeatherLoader.LOAD_ID);
        super.onDestroy();
    }

    /**
     * 设置天气信息
     *
     * @param dayIndex 第几天
     */
    private void setupWeatherInfo(int dayIndex) {
        if (dayIndex > -1 && dayIndex < mWeatherDataWrappers.size()) {
            WeatherRes.WeatherInfo info = mWeatherDataWrappers.get(dayIndex).mWeatherInfo;
            Glide.with(this).load(info.image).into(mWeatherBg);
            Glide.with(this).load(info.icon).placeholder(R.mipmap.ic_weather_cloudy).into(mWeatherIcon);
            mWeatherDescText.setText(info.weather);
            mTempRangeText.setText(getString(R.string.temp_range, info.lowerTemp, info.upperTemp));
            mUVText.setText(info.uv);
            mHumidityText.setText(TextUtils.isEmpty(info.humidity) ? null : (info.humidity + "%"));
            mWindText.setText(info.wind);
            mCurrentTempText.setText(info.temp);
        }
    }

    private View mSelectedTab;
    /**
     * 生成 WeatherWrapper
     *
     * @return 返回当天序号
     */
    private int wrapperWeatherData(WeatherRes weatherRes) {
        mCityNameText.setText(weatherRes.city);//2016.12.7
        int todayIndex = 0;
        if (weatherRes != null && weatherRes.weatherInfos != null) {
            int size = weatherRes.weatherInfos.size();
            if (size > 0) {
                SimpleDateFormat weekFormat = new SimpleDateFormat("EEE", SPlantApplication.getAppLocale());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.M.dd", SPlantApplication.getAppLocale());
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                Calendar today = Calendar.getInstance();
                today.setTime(new Date());
                int i = 0;
                WeatherDataWrapper weatherDataWrapper = null;
                while (i < weatherRes.weatherInfos.size()) {
                    WeatherRes.WeatherInfo info = weatherRes.weatherInfos.get(i);
                    if (info.date == 0) {
                        weatherRes.weatherInfos.remove(i);
                        continue;
                    }
                    date.setTime(info.date * 1000);
                    String mWeek = weekFormat.format(date).toUpperCase();
                    String mDate = dateFormat.format(date);
                    mTabDatas.add(new TabData(mWeek, mDate));
                    weatherDataWrapper = new WeatherDataWrapper();
                    weatherDataWrapper.mWeatherInfo = info;
                    calendar.setTime(date);
                    if (DateUtils.isSameDay(today, calendar)) {
                        todayIndex = i;
                    }
                    if (weatherRes.humidities != null && weatherRes.uvs != null) {
                        Calendar another = Calendar.getInstance();
                        for (int j = 0; j < weatherRes.humidities.size(); j++) {
                            WeatherRes.Humidity humidity = weatherRes.humidities.get(j);
                            another.setTimeInMillis(humidity.createTime * 1000);
                            if (DateUtils.isSameDay(calendar, another)) {
                                weatherDataWrapper.mHumidities.add(humidity);
                                weatherDataWrapper.mUVs.add(weatherRes.uvs.get(j));
                            }
                        }

                    }
                    mWeatherDataWrappers.add(weatherDataWrapper);
                    i++;
                }
                if (mTabDatas.size() > 0) {
                    mTabAdapter = new TabAdapter(mTabDatas);
                    mGalleryTab.setAdapter(mTabAdapter);
                    mGalleryTab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (mSelectedTab != null) {
                                mSelectedTab.animate().scaleX(1.0f).scaleY(1.0f).start();
                            }
                            view.animate().scaleX(1.5f).scaleY(1.5f).start();
                            mSelectedTab = view;
                            setupWeatherInfo(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        }
        return todayIndex;
    }


    @Override
    public Loader<WeatherRes> onCreateLoader(int id, Bundle args) {
        if (id == WeatherLoader.LOAD_ID) {
            WeatherLoader loader = new WeatherLoader(getContext(),
                    SPlantApplication.getClientId(), SPlantApplication.sLocale);
            loader.setOnLoaderListener(new BaseLoader.OnSimpleLoaderListener() {
                @Override
                public void onCanceled(Loader loader, Object data) {
                    super.onCanceled(loader, data);
                    ProgressDialogUtil.dismiss();
                }

                @Override
                public void onResult(Loader loader, Object data) {
                    super.onResult(loader, data);
                    ProgressDialogUtil.dismiss();
                }

                @Override
                public void onStopLoading(Loader loader) {
                    super.onStopLoading(loader);
                    ProgressDialogUtil.dismiss();
                }
            });
            return loader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<WeatherRes> loader, WeatherRes data) {
        if (data != null && data.isSuccessed == 1 && data.weatherInfos != null && data.weatherInfos.size() > 0) {
            mRefreshView.setVisibility(View.GONE);
            mWeatherInfoPanel.setVisibility(View.VISIBLE);
            int todayIndex = wrapperWeatherData(data);
            mGalleryTab.setSelection(todayIndex, false);
        } else {
            mRefreshView.setVisibility(View.VISIBLE);
            mWeatherInfoPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<WeatherRes> loader) {
    }

}
