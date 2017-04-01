package com.splant.smartgarden.weatherModel.Widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.splant.smartgarden.R;
import com.splant.smartgarden.utilModel.DisplayUtils;

/**
 * 天气tab
 * Created by aifengbin on 2017/3/16.
 */
public class WeatherTab extends LinearLayout implements View.OnFocusChangeListener {

    public static final int STATE_NORMAL = 1;
    public static final int STATE_SELECTED = 2;

    private int mState = STATE_NORMAL;

    /**
     * 周
     */
    private TextView mWeekText;
    /**
     * 日期
     */
    private TextView mDateText;

    private OnFocusChangeListener mOnFocusChangeListener;

    public WeatherTab(Context context) {
        this(context, null);
    }

    public WeatherTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        mWeekText = new TextView(context);
        mWeekText.setLines(1);
        mWeekText.setGravity(Gravity.CENTER);
        mWeekText.setPadding(0, (int) DisplayUtils.dp2px(getResources(), 10), 0, 0);
        addView(mWeekText, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mDateText = new TextView(context);
        mDateText.setLines(1);
        mDateText.setGravity(Gravity.CENTER);
        addView(mDateText, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        updateStyle();
        super.setOnFocusChangeListener(this);
    }

    public void setWeekValue(String weekValue) {
        mWeekText.setText(weekValue);
    }

    public void setDateValue(String dateValue) {
        mDateText.setText(dateValue);
    }

    private void updateStyle() {

        mWeekText.setTextSize(getResources().getDimension(R.dimen.weather_tab_normal_week_text_size));
        mDateText.setTextSize(getResources().getDimension(R.dimen.weather_tab_normal_date_text_size));
        mWeekText.setTextColor(Color.WHITE);
        mDateText.setTextColor(Color.WHITE);
    }

    /**
     * 设置状态
     *
     * @param state {@link #STATE_NORMAL} or {@link #STATE_SELECTED}
     */
    public void setState(int state) {
        if (mState != state) {
            mState = state;
            updateStyle();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        float scale = 1.0f;
        if (hasFocus) {
            scale = 1.5f;
        }
        v.animate().scaleX(scale).scaleY(scale).start();
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    public TextView getWeekText() {
        return mWeekText;
    }

    public TextView getDateText() {
        return mDateText;
    }
}
