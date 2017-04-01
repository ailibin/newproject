
package com.splant.smartgarden.weatherModel.Widget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.Utils;
import com.splant.smartgarden.R;
import com.splant.smartgarden.SPlantApplication;
import com.splant.smartgarden.beanModel.Responses.WeatherRes;
import com.splant.smartgarden.utilModel.DisplayUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom implementation of the MarkerView.
 *
 * @author KeepCoding
 */
public class WeatherMarkerView extends MarkerView {

    public static final int LEFT = 1;
    public static final int MIDDLE = 2;
    public static final int RIGHT = 3;

    private TextView mWeekText;
    private TextView mTimeText;
    private TextView mUVText;
    private TextView mHumidText;
    private View mContentView;

    private int mArrowPosition = MIDDLE;

    @IntDef({LEFT, MIDDLE, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ArrowPosition {

    }

    public WeatherMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        mWeekText = (TextView) findViewById(R.id.week);
        mTimeText = (TextView) findViewById(R.id.time);
        mUVText = (TextView) findViewById(R.id.uv);
        mHumidText = (TextView) findViewById(R.id.humidity);
        mContentView = findViewById(R.id.content_view);
    }

    public boolean isOutLeft(float middleX) {
        return (middleX - getWidth() / 2) < 0;
    }

    public boolean isOutRight(float middleX) {
        return (middleX + getWidth() / 2) > DisplayUtils.getDisplayWidth(getResources());
    }

    public void setArrowPosition(@ArrowPosition int arrowPosition) {
        mArrowPosition = arrowPosition;
        switch (mArrowPosition) {
            case MIDDLE:
                mContentView.setBackgroundResource(R.drawable.bg_marker);
                break;
            case LEFT:
                mContentView.setBackgroundResource(R.drawable.bg_marker_left);
                break;
            case RIGHT:
                mContentView.setBackgroundResource(R.drawable.bg_marker_right);
                break;
        }
    }

    @Override
    public void refreshContent(Entry e, int dataSetIndex) {
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
        } else {
        }
    }

    @Override
    public int getXOffset() {
        switch (mArrowPosition) {
            case MIDDLE:
                return -(getWidth() / 2);
            case LEFT:
                return (int) (0 - DisplayUtils.dp2px(getResources(), 10));
            case RIGHT:
                return (int) -(getWidth() - DisplayUtils.dp2px(getResources(), 10));
            default:
                return -(getWidth() / 2);
        }
    }

    @Override
    public int getYOffset() {
        return -getHeight();
    }

    public void refreshContent(DataSetInfo[] dataSetInfos) {
        if (dataSetInfos == null || dataSetInfos.length < 1) {
            return;
        }
        SimpleDateFormat WEEK = new SimpleDateFormat("EEEE", SPlantApplication.getAppLocale());
        SimpleDateFormat TIME = new SimpleDateFormat("a hh:mm", SPlantApplication.getAppLocale());
        Date date = new Date();
        for (DataSetInfo info : dataSetInfos) {
            Object data = info.mEntry.getData();
            if (data instanceof WeatherRes.UV) {
                WeatherRes.UV uv = (WeatherRes.UV) data;
                date.setTime(uv.createTime * 1000);
                mWeekText.setText(WEEK.format(date));
                mTimeText.setText(TIME.format(date));
                mUVText.setText(uv.valueString);
            } else if (data instanceof WeatherRes.Humidity) {
                mHumidText.setText(Utils.formatNumber(info.mEntry.getVal(), 0, true) + "%");
            }
        }
    }

}
