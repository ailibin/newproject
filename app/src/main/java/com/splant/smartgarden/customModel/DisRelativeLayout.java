package com.splant.smartgarden.customModel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zhy.autolayout.AutoRelativeLayout;


/**
 * Created by aifengbin on 2017/3/12.
 * 解决事件冲突
 */

public class DisRelativeLayout extends AutoRelativeLayout {
    public DisRelativeLayout(Context context) {
        super(context);
    }

    public DisRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
