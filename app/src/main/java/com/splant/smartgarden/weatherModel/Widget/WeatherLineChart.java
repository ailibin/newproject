package com.splant.smartgarden.weatherModel.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Highlight;

import java.util.List;

/**
 * Custom LineChart
 * Created by KeepCoding on 2015/9/8.
 */
public class WeatherLineChart extends LineChart {

    public WeatherLineChart(Context context) {
        this(context, null);
    }

    public WeatherLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setNoDataText("");
    }

    @Override
    protected void drawMarkers(Canvas canvas) {

        // if there is no marker view or drawing marker is disabled
        if (mMarkerView == null || !mDrawMarkerViews || !valuesToHighlight())
            return;

        int xIndex = -1;
        int dataSetIndex = -1;
        for (Highlight highlight : mIndicesToHightlight) {
            xIndex = highlight.getXIndex();
            dataSetIndex = highlight.getDataSetIndex();
        }
        if (xIndex == -1 || dataSetIndex == -1 || xIndex == 0/*第一点，占位点不显示*/) {
            return;
        }

        if (xIndex <= mDeltaX && xIndex <= mDeltaX * mAnimator.getPhaseX()) {

            List<LineDataSet> dataSets = getData().getDataSets();
            int size = dataSets.size();
            // TODO: 15/9/8 优化判断，增强健壮性
            if (xIndex == dataSets.get(0).getEntryCount() - 1) {
                //最后一点，占位点不显示
                return;
            }
            DataSetInfo[] dataSetInfos = new DataSetInfo[size];
            for (int i = 0; i < size; i++) {
                DataSetInfo info = new DataSetInfo();
                info.mEntry = dataSets.get(i).getEntryForXIndex(xIndex);
                info.mDataSetIndex = getData().getIndexOfDataSet(dataSets.get(i));
                dataSetInfos[i] = info;
            }

            // make sure entry not null
            if (dataSetInfos.length <= 0)
                return;

            float[] pos = getMaxYPos(dataSetInfos);

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                return;

            // callbacks to update the content
            if (mMarkerView instanceof WeatherMarkerView) {
                WeatherMarkerView weatherMarkerView = (WeatherMarkerView) mMarkerView;
                weatherMarkerView.refreshContent(dataSetInfos);
                weatherMarkerView.setArrowPosition(WeatherMarkerView.MIDDLE);
                mMarkerView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                mMarkerView.layout(0, 0, mMarkerView.getMeasuredWidth(),
                        mMarkerView.getMeasuredHeight());

                if (weatherMarkerView.isOutLeft(pos[0])) {
                    weatherMarkerView.setArrowPosition(WeatherMarkerView.LEFT);
                    mMarkerView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    mMarkerView.layout(0, 0, mMarkerView.getMeasuredWidth(),
                            mMarkerView.getMeasuredHeight());
                } else if (weatherMarkerView.isOutRight(pos[0])) {
                    weatherMarkerView.setArrowPosition(WeatherMarkerView.RIGHT);
                    mMarkerView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    mMarkerView.layout(0, 0, mMarkerView.getMeasuredWidth(),
                            mMarkerView.getMeasuredHeight());
                }

                if (pos[1] - mMarkerView.getHeight() <= 0) {
                    float y = mMarkerView.getHeight() - pos[1];
                    mMarkerView.draw(canvas, pos[0], pos[1] + y);
                } else {
                    mMarkerView.draw(canvas, pos[0], pos[1]);
                }
            } else {
                throw new IllegalArgumentException("mMarkerView must be instanceof " + WeatherMarkerView.class.getSimpleName());
            }
        }

    }

    /**
     * 获取Y最高的点
     *
     * @param infos {@link DataSetInfo}
     */
    private float[] getMaxYPos(DataSetInfo[] infos) {
        float[] pos = new float[]{0, 0};
        if (infos == null || infos.length < 1) {
            return pos;
        }
        for (DataSetInfo info : infos) {
            float[] tPos = getMarkerPosition(info.mEntry, info.mDataSetIndex);
            if (pos[1] == 0 || tPos[1] < pos[1]) {
                pos[0] = tPos[0];
                pos[1] = tPos[1];
            }
        }
        return pos;
    }
}
