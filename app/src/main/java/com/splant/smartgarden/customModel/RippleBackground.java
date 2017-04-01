package com.splant.smartgarden.customModel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.splant.smartgarden.R;
import com.splant.smartgarden.utilModel.DisplayUtils;

import java.util.ArrayList;

/**
 * https://github.com/skyfishjy/android-ripple-background
 * Created by fyu on 11/3/14.
 */
public class RippleBackground extends RelativeLayout {

    private static final int DEFAULT_RIPPLE_COUNT = 6;
    private static final int DEFAULT_DURATION_TIME = 3000;
    private static final float DEFAULT_SCALE = 6.0f;
    private static final int DEFAULT_FILL_TYPE = 0;

    private int rippleColor;
    private float rippleStrokeWidth;
    private float rippleRadius;
    private int rippleDurationTime;
    private int rippleAmount;
    private int rippleDelay;
    private float rippleScale;
    private int rippleType;
    private boolean animationRunning = false;
    private ArrayList<RippleView> rippleViewList = new ArrayList<RippleView>();

    private BitmapFactory.Options mOptions = new BitmapFactory.Options();

    private int mV;//方形对角线

    private SplashAnimLogo mSplashAnimLogo;

    public RippleBackground(Context context) {
        super(context);
    }

    public RippleBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RippleBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode())
            return;

        if (null == attrs) {
            throw new IllegalArgumentException("Attributes should be provided to this view,");
        }

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleBackground);
        rippleColor = typedArray.getColor(R.styleable.RippleBackground_rb_color, getResources().getColor(R.color.rippelColor));
        rippleStrokeWidth = typedArray.getDimension(R.styleable.RippleBackground_rb_strokeWidth, getResources().getDimension(R.dimen.rippleStrokeWidth));
        rippleRadius = typedArray.getDimension(R.styleable.RippleBackground_rb_radius, getResources().getDimension(R.dimen.rippleRadius));
        rippleDurationTime = typedArray.getInt(R.styleable.RippleBackground_rb_duration, DEFAULT_DURATION_TIME);
        rippleAmount = typedArray.getInt(R.styleable.RippleBackground_rb_rippleAmount, DEFAULT_RIPPLE_COUNT);
        rippleScale = typedArray.getFloat(R.styleable.RippleBackground_rb_scale, DEFAULT_SCALE);
        rippleType = typedArray.getInt(R.styleable.RippleBackground_rb_type, DEFAULT_FILL_TYPE);
        typedArray.recycle();

        rippleDelay = rippleDurationTime / rippleAmount;

        mV = Math.min(DisplayUtils.getDisplayHeight(getResources()), DisplayUtils.getDisplayWidth(getResources()));
        LayoutParams rippleParams = new LayoutParams(mV, mV);
        rippleParams.addRule(CENTER_IN_PARENT, TRUE);
        mOptions.inJustDecodeBounds = false;
        BitmapFactory.decodeResource(getResources(), R.mipmap.logo0001, mOptions);
        for (int i = 0; i < rippleAmount; i++) {
            RippleView rippleView = new RippleView(getContext());
            addView(rippleView, rippleParams);
            rippleView.setVisibility(VISIBLE);
            rippleViewList.add(rippleView);
        }

        LayoutParams logoParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        logoParams.addRule(CENTER_IN_PARENT, TRUE);
        mSplashAnimLogo = new SplashAnimLogo(getContext());
        mSplashAnimLogo.setImageResource(R.mipmap.logo0001);
        addView(mSplashAnimLogo, logoParams);

    }

    public interface OnAnimationListener {
        public void onStart();

        public void onStop();
    }

    private OnAnimationListener mOnAnimationListener;

    public void setOnAnimationListener(OnAnimationListener onAnimationListener) {
        mOnAnimationListener = onAnimationListener;
    }

    /**
     * 闪屏动画logo
     * Created by KeepCoding on 15/9/19.
     */
    private class SplashAnimLogo extends android.support.v7.widget.AppCompatImageView {

        private Paint mPaint = new Paint();
        private int a;
        private RectF mRectF = new RectF();
        private float mStep;
        private RectF mClipRectF;
        private boolean isStart;

        public SplashAnimLogo(Context context) {
            this(context, null);
        }

        public SplashAnimLogo(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public SplashAnimLogo(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            a = (int) (mOptions.outWidth / Math.sqrt(2));
            float top = ((mOptions.outWidth - a) * 0.5f);
            LinearGradient linearGradient = new LinearGradient(0, a * 0.5f, 3 * a, a * 0.5f,
                    new int[]{Color.parseColor("#00FFFFFF"), Color.WHITE, Color.parseColor("#00FFFFFF")}, new float[]{0, 0.5f, 1}, Shader.TileMode.MIRROR);
            mPaint.setShader(linearGradient);

            mStep = DisplayUtils.dp2px(getResources(), 4);// (3 * a * 10) / 2000;
            mClipRectF = new RectF((mOptions.outWidth - a) * 0.5f, (mOptions.outHeight - a) * 0.5f, (mOptions.outWidth - a) * 0.5f + a, (mOptions.outHeight - a) * 0.5f + a);
            mRectF.set(-(3 * a) + mClipRectF.left, top, 0, top + a);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!isStart) {
                return;
            }
            mRectF.offset(mStep, 0);
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.rotate(45, getWidth() / 2, getHeight() / 2);
            canvas.clipRect(mClipRectF);
            canvas.drawRect(mRectF, mPaint);
            canvas.restore();
            if (mRectF.left < mClipRectF.right) {
                postInvalidateDelayed(16);
            } else {
                //stop
                isStart = false;
                for (RippleView rippleView : rippleViewList) {
                    rippleView.setVisibility(GONE);
                }
                RippleBackground.this.setVisibility(GONE);
                if (mOnAnimationListener != null) {
                    mOnAnimationListener.onStop();
                }
            }
        }

        public void start(long delay) {
            isStart = true;
            mRectF.offsetTo(-(3 * a) + mClipRectF.left, mRectF.top);
            postInvalidateDelayed(delay);
        }
    }

    private class RippleView extends View {

        private final static float MAX_ALPHA = 160;
        private float mStep = 20.0f;
        private float mRectFa;
        private float mAlpha = MAX_ALPHA;
        private Paint mPaint;
        private boolean isStart = false;
        private RectF mRectF = new RectF();

        private float a;//边长
        private float clipA;
        private float clipLeft;
        private float clipTop;

        private long mDelay;

        public RippleView(Context context) {
            super(context);
            this.setVisibility(View.INVISIBLE);
            reset();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(rippleColor);
            a = getA(mV * 0.5f);
            clipA = getA(mOptions.outWidth);
            clipLeft = ((mV - clipA) * 0.5f);
            clipTop = ((mV - clipA) * 0.5f);
            mDelay = (long) (700 / ((a - clipA) / mStep));
        }

        public void reset() {
            mRectFa = getA(mOptions.outWidth);
            mAlpha = MAX_ALPHA;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (!isStart) {
                return;
            }
            mRectFa += mStep;
            mAlpha = mAlpha - (MAX_ALPHA / ((a - clipA) / mStep));
            if (mRectFa > a || mAlpha < 0) {
                return;
            }
            mPaint.setAlpha((int) mAlpha);
            mRectF.set(0, 0, mRectFa, mRectFa);
            mRectF.offset((mV - mRectFa) / 2, (mV - mRectFa) / 2);
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.rotate(45, mV / 2, mV / 2);
            canvas.clipRect(clipLeft, clipTop, clipLeft + clipA, clipTop + clipA, Region.Op.DIFFERENCE);
            canvas.drawRoundRect(mRectF, 20, 20, mPaint);
            canvas.restore();
            postInvalidateDelayed(20);
        }

        /**
         * 正方形对角线求边长
         *
         * @param v 对角线
         * @return 边长长度
         */
        private float getA(float v) {
            return (float) (v / Math.sqrt(2));
        }

        private void startRipple(long delay) {
            isStart = true;
            postInvalidateDelayed(delay);
        }

    }

    public void startRippleAnimation() {
        if (!isRippleAnimationRunning()) {
            if (mOnAnimationListener != null) {
                mOnAnimationListener.onStart();
            }
            int i = 0;
            mSplashAnimLogo.start(0);
            for (RippleView rippleView : rippleViewList) {
                rippleView.reset();
                rippleView.startRipple(i * rippleDelay);
                i++;
            }
            animationRunning = true;
        }
    }

    public void stopRippleAnimation() {
        if (isRippleAnimationRunning()) {
            animationRunning = false;
        }
    }

    public boolean isRippleAnimationRunning() {
        return animationRunning;
    }
}
