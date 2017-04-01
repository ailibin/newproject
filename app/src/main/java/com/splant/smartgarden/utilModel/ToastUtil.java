package com.splant.smartgarden.utilModel;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by aifengbin on 2017/3/8.
 */
public class ToastUtil {
    private  static Toast mToast;

    public static void toastLong(Context ct, String msg) {
        toastNoRepeat(ct,msg, Toast.LENGTH_LONG);
    }

    public static void toastShort(Context ct, String msg) {
        toastNoRepeat(ct,msg, Toast.LENGTH_SHORT);

    }

    public static void toastLong(Context ct, int StringId) {
        toastNoRepeat(ct,StringId, Toast.LENGTH_LONG);

    }

    public static void toastShort(Context ct, int StringId) {
        toastNoRepeat(ct,StringId, Toast.LENGTH_SHORT);

    }

    public static void toastNoRepeat(Context context, String str, int type){
        if (mToast == null) {
            mToast = Toast.makeText(context, str, type);
        } else {
            mToast.setText(str);
            mToast.setDuration(type);
        }
        mToast.show();
    }

    public static void toastNoRepeat(Context context, int id, int type){
        if (mToast == null) {
            mToast = Toast.makeText(context, id, type);
        } else {
            mToast.setText(id);
            mToast.setDuration(type);
        }
        mToast.show();
    }
}
