package com.splant.smartgarden.weatherModel.Widget;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.CircleBuffer;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.LineDataProvider;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

/**
 * Custom Renderer
 */
public class WeatherLineChartRenderer extends LineChartRenderer {

    public WeatherLineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    private void drawHighlightedCircles(Canvas c, int xIndex) {
        mRenderPaint.setStyle(Paint.Style.FILL);
        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        List<LineDataSet> dataSets = mChart.getLineData().getDataSets();

        for (LineDataSet set : dataSets) {
            int index = mChart.getLineData().getIndexOfDataSet(set);

            Transformer trans = mChart.getTransformer(set.getAxisDependency());
            List<Entry> entries = set.getYVals();
            Entry entryFrom = set.getEntryForXIndex((mMinX < 0) ? 0 : mMinX);
            Entry entryTo = set.getEntryForXIndex(mMaxX);
            int minx = Math.max(set.getEntryPosition(entryFrom), 0);
            int maxx = Math.min(set.getEntryPosition(entryTo) + 1, entries.size());
            CircleBuffer buffer = mCircleBuffers[index];
            buffer.setPhases(phaseX, phaseY);
            buffer.limitFrom(minx);
            buffer.limitTo(maxx);
            buffer.feed(entries);
            trans.pointValuesToPixel(buffer.buffer);

            float halfsize = set.getCircleSize() / 2f;

            int xIndexBuffer = xIndex * 2;
            float x = buffer.buffer[xIndexBuffer];
            float y = buffer.buffer[xIndexBuffer + 1];

            if (!mViewPortHandler.isInBoundsRight(x))
                return;

            // make sure the circles don't do shitty things outside
            // bounds
            if (!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y))
                return;

            int circleColor = set.getCircleColor(xIndexBuffer / 2 + minx);

            mRenderPaint.setColor(circleColor);

            c.drawCircle(x, y, set.getCircleSize(),
                    mRenderPaint);
            mCirclePaintInner.setColor(set.getCircleHoleColor());
            if (set.isDrawCircleHoleEnabled()
                    && circleColor != mCirclePaintInner.getColor())
                c.drawCircle(x, y,
                        halfsize,
                        mCirclePaintInner);
        }
    }

    private class MaxInfo {
        LineDataSet mDataSet;
        float y;
    }

    /**
     * 根据 xIndex，获取对应的 Y value，然后算出 Y 最高点(Y 的 pixel 最小)
     *
     * @param xIndex x 轴序列
     * @return Y 最高点对应的 Y value
     */
    private MaxInfo getMaxY(int xIndex) {
        MaxInfo maxInfo = new MaxInfo();
        List<LineDataSet> dataSets = mChart.getLineData().getDataSets();
        float[] pts = new float[]{xIndex, 0};
        float pixelY = 0;
        for (LineDataSet set : dataSets) {
            float y = set.getYValForXIndex(xIndex) * mAnimator.getPhaseX();
            pts[1] = y;
            mChart.getTransformer(set.getAxisDependency()).pointValuesToPixel(pts);
            if (pts[1] < pixelY || pixelY == 0) {
                maxInfo.y = y;
                maxInfo.mDataSet = set;
                pixelY = pts[1];
            }
        }
        return maxInfo;
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        for (int i = 0; i < indices.length; i++) {

            LineDataSet set = mChart.getLineData().getDataSetByIndex(indices[i]
                    .getDataSetIndex());
            if (set == null)
                continue;

            int xIndex = indices[i].getXIndex(); // get the x-position

            if (xIndex == 0 || xIndex == set.getEntryCount() - 1) {
                //第一点和最后一点不显示，占位点
                continue;
            }

            mHighlightPaint.setColor(set.getHighLightColor());

            if (xIndex > mChart.getXChartMax() * mAnimator.getPhaseX())
                continue;

//            float y = set.getYValForXIndex(xIndex) * mAnimator.getPhaseY(); // get the y-position

            MaxInfo maxInfo = getMaxY(xIndex);// get the max y-position

            float[] pts = new float[]{
                    xIndex, maxInfo.y, xIndex, mChart.getYChartMin(), /*mChart.getXChartMin(), y,
                    mChart.getXChartMax(), y*/
            };

            mChart.getTransformer(maxInfo.mDataSet.getAxisDependency()).pointValuesToPixel(pts);
            // draw the highlight lines
            c.drawLines(pts, mHighlightPaint);

            drawHighlightedCircles(c, xIndex);
        }
    }
}
