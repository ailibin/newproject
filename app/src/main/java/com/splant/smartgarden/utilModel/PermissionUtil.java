package com.splant.smartgarden.utilModel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by aifengbin on 2017/3/9.
 * 运行时权限的封装,必须先检查应用是否有这个权限
 */
public class PermissionUtil {
    public static boolean IsBiggerAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    //相机权限
    public static boolean CameraPermission(Context context, int requestCode) {
        if (IsBiggerAndroidM() && ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CAMERA}, requestCode);
            return false;
        }
        return true;
    }

    //文件读
    public static boolean ReadStoragePermission(Context context, int requestCode) {
        if (IsBiggerAndroidM() && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
            return false;
        }
        return true;
    }

    //文件写
    public static boolean WriteStoragePermission(Context context, int requestCode) {
        if (IsBiggerAndroidM() && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            return false;
        }
        return true;
    }

    //录音权限
    public static boolean RecordAudioPermission(Context context, int requestCode) {
        if (IsBiggerAndroidM() && ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.RECORD_AUDIO}, requestCode);
            return false;
        }
        return true;
    }

    //联系人权限
    public static boolean ReadContactsPermission(Context context, int requestCode) {
        if (IsBiggerAndroidM() && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_CONTACTS}, requestCode);
            return false;
        }
        return true;
    }

    //打电话权限
    public static boolean CallPhonePermission(Context context, int requestCode) {
        if (IsBiggerAndroidM() && ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CALL_PHONE}, requestCode);
            return false;
        }
        return true;
    }

    //读取电话状态信息
    public static boolean readPhoneStatePermission(Context context,int requestCode){
        if (IsBiggerAndroidM() && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, requestCode);
            return false;
        }
        return true;
    }
    public static boolean PermissionRequest(Context context, int requestCode, String[] permissions) {
        boolean flag = true;
        if (IsBiggerAndroidM()) {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    flag = false;
                }
            }
            if (!flag) {
                ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
            }
        }
        return flag;
    }
}
