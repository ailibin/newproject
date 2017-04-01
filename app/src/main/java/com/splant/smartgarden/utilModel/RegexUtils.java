package com.splant.smartgarden.utilModel;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.splant.smartgarden.listenerModel.CallBack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具
 * Created by aifengbin on 2017/3/10.
 */
public final class RegexUtils {

    private RegexUtils() {
    }

    /**
     * 纯数字
     *
     * @param input 输入
     * @return true 纯数字
     */
    public static boolean isDigitsOnly(String input) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+");
        return pattern.matcher(input).matches();
    }

    /**
     * 本地校验手机号码段 正则表达式
     *
     * @param mobiles
     * @return
     */
    public static final boolean isMobile(String mobiles) {
        Pattern p = Pattern
                .compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static final boolean isEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(check);
        Matcher m = p.matcher(email);
        return m.matches();
    }
    public static final boolean isPassWord(String passWord) {
        String check = "^[a-zA-Z0-9_]{6,15}$";
        Pattern p = Pattern.compile(check);
        Matcher m = p.matcher(passWord);
        return m.matches();
    }

    public static final boolean isNet(String net) {
        Pattern p = Pattern
                .compile("http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
        Matcher m = p.matcher(net);
        return m.matches();
    }
    public static final boolean isEnOrNumber(String s) {
        Pattern p = Pattern
                .compile("[A-Za-z0-9\\-]*");
        Matcher m = p.matcher(s);
        return m.matches();
    }
    public static final void passWord(final EditText editText, final ImageView correct, final CallBack<Boolean> callBack) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().trim().length() >= 6) {
                    correct.setVisibility(View.VISIBLE);
                    callBack.onSuccess(true);
                } else {
                    correct.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}
