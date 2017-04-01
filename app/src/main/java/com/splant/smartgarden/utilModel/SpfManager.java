package com.splant.smartgarden.utilModel;

import android.content.Context;
import android.content.SharedPreferences;

import com.splant.smartgarden.SPlantApplication;
import com.splant.smartgarden.beanModel.config.Constant;

/**
 * Created by aifengbin on 2017/3/17.
 */

public class SpfManager {

    private SharedPreferences mSharedPreferences;
    private static SpfManager mInstance;


    /**
     * 使用单例模式得到SpfManager的引用
     */
    public static SpfManager getInstance() {
        if (mInstance == null) {
            mInstance = new SpfManager(SPlantApplication.getInstance());
        }
        return mInstance;
    }

    /**
     * 构造方法
     *
     * @param context
     */
    public SpfManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(Constant.Config.PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

    /**
     * 保存string值
     *
     * @param key
     * @param value
     */
    public void saveString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * 方法名: <deleteString/br>
     * 详述: <清除缓存/br>
     *
     * @param key 键值
     */
    public void deleteString(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    /**
     * 方法名: <deleteString/br>
     * 详述: <清除多个缓存/br>
     *
     * @param key
     */
    public void deleteString(String... key) {
        if (key.length > 0) {
            for (int i = 0; i < key.length; i++) {
                mSharedPreferences.edit().remove(key[i]).apply();
            }
        }
    }

    /**
     * 取string值
     *
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String... defValue) {
        if (defValue.length > 0)
            return mSharedPreferences.getString(key, defValue[0]);
        else
            return mSharedPreferences.getString(key, "");

    }

    /**
     * 保存int值
     */
    public void saveInt(String key, Integer value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    /**
     * 获取int值
     */
    public int getInt(String key, Integer... defValue) {
        if (defValue.length > 0) {
            return mSharedPreferences.getInt(key, defValue[0]);
        } else {
            return mSharedPreferences.getInt(key, -1);
        }
    }

    /**
     * 保存boolean值ֵ
     */
    public void saveBoolean(String key, Boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     * 取boolean类型的值ֵ
     */
    public Boolean getBoolean(String key, Boolean... defValue) {
        if (defValue.length > 0)
            return mSharedPreferences.getBoolean(key, defValue[0]);
        else
            return mSharedPreferences.getBoolean(key, false);
    }

    /**
     * 用户登录信息保存
     * @param username
     * @param password
     */
    public void setLoginInfo(String username, String password,String companyname) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("key_username", username).putString("key_password", password).putString("key_comcode",companyname).apply();
    }

    /**
     * 取用户信息
     * @return
     */
    public String[] getLoginInfo() {
        String[] loginInfo = new String[3];
        loginInfo[0] = mSharedPreferences.getString("key_username", "");
        loginInfo[1] = mSharedPreferences.getString("key_password", "");
        loginInfo[2] = mSharedPreferences.getString("key_comcode","");
        return loginInfo;
    }

    //清除密码
    public void clearPassword() {
        mSharedPreferences.edit().remove("key_password").apply();
    }

    //是否记住密码
    public boolean isRememberPassword() {
        return mSharedPreferences.getBoolean("key_remember_password", true);
    }

    //记住密码
    public void setRememberPassword(Context context, boolean remember) {
        mSharedPreferences.edit().putBoolean("key_remember_password", remember).apply();
    }

    //设置公司的logo
    public void setCompanyLogo(String logo) {
        mSharedPreferences.edit().putString("key_company_logo", logo).commit();
    }

    //获取公司的logo
    public String getCompanyLogo() {
        return mSharedPreferences.getString("key_company_logo", null);
    }

    /**
     * 获取语言类型
     * @param context
     * @return
     */
    public  String getLocale(Context context) {
        return mSharedPreferences.getString("key_locale", null);
    }

    public void setLocale(Context context, String locale) {
        mSharedPreferences.edit().putString("key_locale", locale).commit();
    }
}
