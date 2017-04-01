package com.splant.smartgarden.listenerModel;

import android.animation.Animator;
import android.view.View;

/**
 * Created by aifengbin on 2017/3/10.
 */

public class MyAnimatorListener implements Animator.AnimatorListener {

    private View mView;

    public MyAnimatorListener(View view) {
        mView = view;
    }
    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mView.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
