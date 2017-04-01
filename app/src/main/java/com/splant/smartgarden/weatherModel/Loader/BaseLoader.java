package com.splant.smartgarden.weatherModel.Loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

/**
 * Base
 * Created by KeepCoding on 2015/8/19.
 */
public abstract class BaseLoader<D> extends AsyncTaskLoader<D> {

    public static final int APP_CONTENT_LOAD_ID = 0x10;
    public static final int UNIT_OP_LOAD_ID = 0x11;

    public interface OnLoaderListener {

        public void onStart(Loader loader);

        public void onStopLoading(Loader loader);

        public void onResult(Loader loader, Object data);

        public void onCanceled(Loader loader, Object data);
    }

    public static class OnSimpleLoaderListener implements OnLoaderListener {


        @Override
        public void onStart(Loader loader) {

        }

        @Override
        public void onStopLoading(Loader loader) {

        }

        @Override
        public void onResult(Loader loader, Object data) {

        }

        @Override
        public void onCanceled(Loader loader, Object data) {

        }
    }

    private OnLoaderListener mOnLoaderListener;

    public BaseLoader(Context context) {
        super(context);
    }

    public void setOnLoaderListener(OnLoaderListener onLoaderListener) {
        mOnLoaderListener = onLoaderListener;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        if (mOnLoaderListener != null) {
            mOnLoaderListener.onStart(this);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        if (mOnLoaderListener != null) {
            mOnLoaderListener.onStopLoading(this);
        }
    }

    @Override
    public void deliverResult(D data) {
        super.deliverResult(data);
            if (mOnLoaderListener != null) {
                mOnLoaderListener.onResult(this, data);
            }
        }

    @Override
    public void onCanceled(D data) {
        super.onCanceled(data);
        if (mOnLoaderListener != null) {
            mOnLoaderListener.onCanceled(this, data);
        }
    }
}
