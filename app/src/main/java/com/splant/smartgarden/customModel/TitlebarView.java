package com.splant.smartgarden.customModel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.splant.smartgarden.R;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoLayoutHelper;

/**
 * Created by aifengbin on 2017/3/08.
 */
public class TitlebarView extends RelativeLayout implements View.OnClickListener {

    private ImageView imgLeft;     //左侧按钮
    private ImageView imgRight;
    private TextView tvRight;    //右侧按钮
    private TextView tvTitle;   //标题文本
    private FrameLayout btnLeft;
    private FrameLayout btnRight;
    private TextView tvLeft;
    private BtnClickListener listener;
    private Context context;
    private View view;
    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);


    public TitlebarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TitlebarView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    /**
     * 屏幕适配需要使用的两种方法
     */
    @Override
    public AutoRelativeLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AutoRelativeLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!isInEditMode()){
            mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 初始化组件
     */
    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.gaiaa_custom_titlebar, this);
        imgLeft = (ImageView) findViewById(R.id.img_titlebar_left);
        imgRight = (ImageView) findViewById(R.id.img_titlebar_right);
        btnLeft = (FrameLayout) findViewById(R.id.btn_titlebar_left);
        btnRight = (FrameLayout) findViewById(R.id.btn_titlebar_right);
        tvRight = (TextView) findViewById(R.id.tv_titlebar_right);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_name);
        tvLeft = (TextView) findViewById(R.id.tv_titlebar_left);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
    }

    public void setTitleBarClickListener(BtnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 按钮点击接口
     */
    public interface BtnClickListener {
        void leftClick();

        void rightClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_titlebar_left:
                if (listener != null)
                    listener.leftClick();
                break;
            case R.id.btn_titlebar_right:
                if (listener != null)
                    listener.rightClick();
                break;
            default:
                break;
        }
    }


    /**
     * 设置左侧控件是否可见
     *
     * @param flag 是否可见
     */
    public void setBtnLeftVisable(boolean flag) {
        if (flag) {
            btnLeft.setVisibility(VISIBLE);
        } else {
            btnLeft.setVisibility(GONE);
        }
    }

    public void setLeftBtnImage(int ResId) {
        btnLeft.setVisibility(VISIBLE);
        imgLeft.setVisibility(VISIBLE);
        tvLeft.setVisibility(GONE);
        imgLeft.setImageResource(ResId);
    }

    public void setLeftBtnTv(int ResId) {
        btnLeft.setVisibility(VISIBLE);
        tvLeft.setVisibility(VISIBLE);
        imgLeft.setVisibility(GONE);
        tvLeft.setText(ResId);
    }


    /**
     * 设置右侧按钮是否可见
     *
     * @param flag 是否可见
     */
    public void setRightBtnText(boolean flag, int msgId) {
        if (flag) {
            btnRight.setVisibility(VISIBLE);
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(msgId);
        } else {
            tvRight.setVisibility(GONE);
        }
    }

    /**
     *右侧图片
     * @param ResId
     */
    public void setRightBtnImage(int ResId) {
        btnRight.setVisibility(VISIBLE);
        imgRight.setVisibility(VISIBLE);
        tvRight.setVisibility(GONE);
        imgRight.setImageResource(ResId);
    }

    /**
     * 多用于设置返回
     *
     * @param listener
     */
    public void setBtnLeftClickListener(OnClickListener listener) {
        btnLeft.setVisibility(VISIBLE);
        btnLeft.setOnClickListener(listener);
    }

    /**
     * 标题点击事件
     *
     * @param listener
     */
    public void setTitleClickListener(OnClickListener listener) {
        tvTitle.setClickable(true);
        tvTitle.setOnClickListener(listener);
    }

    public void setTitleClickEnable(boolean enable) {
        tvTitle.setClickable(enable);
    }

    /**
     * 设置标题名称
     *
     * @param title
     */
    public void setTitle(int title) {
        tvTitle.setText(title);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }


    /**
     * 设置标题栏消失
     */
    public void setTitleBarDismiss(boolean dismiss) {
        if (dismiss) {
            view.setVisibility(GONE);
        } else {
            view.setVisibility(VISIBLE);
        }
    }


}
