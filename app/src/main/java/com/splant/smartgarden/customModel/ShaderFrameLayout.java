package com.splant.smartgarden.customModel;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;

import com.splant.smartgarden.R;
import com.zhy.autolayout.AutoFrameLayout;


/**
 * Created by aifengbin on 2017/3/14.
 */
public class ShaderFrameLayout extends AutoFrameLayout {


    private Paint mPaint;

    public ShaderFrameLayout(Context context) {
        this(context, null);
    }

    public ShaderFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShaderFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.black_40));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShaderFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
    }
}
