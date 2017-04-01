package com.splant.smartgarden.utilModel;

import android.app.Activity;
import android.app.ProgressDialog;

import com.splant.smartgarden.R;


/**
 * {@link ProgressDialog} util
 */
public class ProgressDialogUtil {
    private static ProgressDialog sDialog;

    public static void show(Activity activity, String message, boolean cancelable) {
        dismiss();
        if (sDialog == null) {
            sDialog = new ProgressDialog(activity);
            sDialog.setMessage(message);
//            sDialog.setCancelable(cancelable);
            sDialog.setCancelable(true);// notice 2016.9.8 点击返回键就可以取消ProgressDialog  //设置进度条是否可以按退回键取消
//            sDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // 设置为矩形进度条
//            sDialog.setIndeterminate(true); // 设置进度条是否为不明确
//            sDialog.setMax(100); // 设置进度条的最大值
//            sDialog.setProgress(0); // 设置当前默认进度为 0
//            sDialog.setSecondaryProgress(1000); // 设置第二条进度值为100
            sDialog.setTitle(R.string.warm_prompt); // 设置进度条的标题信息

            sDialog.setCanceledOnTouchOutside(false);// notice 2016.9.8  设置点击进度对话框外的区域对话框不消失
            if (!activity.isFinishing()) {
                sDialog.show();
            }
        }
    }

    public static void show(Activity activity, int resId, boolean cancelable) {
        show(activity, activity.getString(resId), cancelable);
    }

    public static void dismiss() {
        if (sDialog != null) {
            if (sDialog.isShowing()) {
                sDialog.dismiss();
            }
            sDialog = null;
        }
    }

}
