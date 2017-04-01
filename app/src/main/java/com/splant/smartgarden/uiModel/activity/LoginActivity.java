package com.splant.smartgarden.uiModel.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.splant.smartgarden.ApiModel.ServerAPI;
import com.splant.smartgarden.BuildConfig;
import com.splant.smartgarden.R;
import com.splant.smartgarden.SPlantApplication;
import com.splant.smartgarden.baseModel.BaseActivity;
import com.splant.smartgarden.beanModel.Responses.ClientInfoRes;
import com.splant.smartgarden.beanModel.Responses.LoginRes;
import com.splant.smartgarden.beanModel.config.Config;
import com.splant.smartgarden.beanModel.config.Constant;
import com.splant.smartgarden.customModel.RippleBackground;
import com.splant.smartgarden.listenerModel.MyAnimatorListener;
import com.splant.smartgarden.uiModel.Mvp.LoginPresenterImpl;
import com.splant.smartgarden.uiModel.Mvp.LoginView;
import com.splant.smartgarden.uiModel.broadcast.NetworkChangedReceiver;
import com.splant.smartgarden.utilModel.CheckIsLANUtils;
import com.splant.smartgarden.utilModel.DisplayUtils;
import com.splant.smartgarden.utilModel.ImageDownloader;
import com.splant.smartgarden.utilModel.NetWorkUtils;
import com.splant.smartgarden.utilModel.ProgressDialogUtil;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by aifengbin on 2017/3/9.
 */

public class LoginActivity extends BaseActivity implements LoginView {

    @Bind(R.id.username)
    EditText mUsernameEt;
    @Bind(R.id.password)
    EditText mPasswordEt;
    @Bind(R.id.company_code)
    EditText mCompanyCodeEt;
    @Bind(R.id.btn_login)
    Button loginBtn;
    @Bind(R.id.save_password)
    CheckBox mSavePasswordCb;
    @Bind(R.id.splash_image)
    ImageView mSplashImageIv;
    @Bind(R.id.blur_image)
    ImageView mBlurImageIv;
    @Bind(R.id.login_content)
    RippleBackground rippleBackground;
    @Bind(R.id.top_logo)
    ImageView topLogo;
    @Bind(R.id.circle_top_logo)
    ImageView circleTopLogo;
    @Bind(R.id.splant)
    View logoText;
    @Bind(R.id.login_panel)
    View loginPanel;


    //网络端的参数
    public static final String USERLOGIN_API = "/API/JsonLogin.aspx";
    private boolean usedWifiNet = false;
    private RequestParams userParams;
    private String username;
    private String password;
    private String clientCode;
    private String loginUrl = "";
    private String user_Id;
    //其他的参数
    private NetworkChangedReceiver networkChangedReceiver;
    private static final int MSG_HIDE_SPLASH = 1;
    private static final int MSG_BLUR = 2;
    private UIHandler mHandler;

    @Inject
    AsyncHttpClient client;
    @Inject
    LoginPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);
        //注解器的注入
        getActivityComponent().inject(this);
        //设置TitleBar不可见
        titlebarView.setTitleBarDismiss(true);
        presenter.attachView(this);
        initUserData();
        initReceiver();
        initHandler();
    }

    private void initHandler() {
        mHandler = new UIHandler(this);
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                loginBtn.setEnabled(true);
            }
        };
    }

    //消息的处理
    @Override
    protected void onHandleMessage(BaseActivity baseActivity, Message message) {
        super.onHandleMessage(baseActivity, message);
        LoginActivity activity = (LoginActivity) baseActivity;
        switch (message.what) {
            case MSG_HIDE_SPLASH:
                activity.onHideSplash();
                break;
            case MSG_BLUR:
                activity.onBlur();
                activity.mHandler.sendEmptyMessageDelayed(MSG_HIDE_SPLASH, 500);
                break;
        }
    }

    //动画开始
    private void onBlur() {
        try {
            Blurry.with(getApplicationContext())
                    .radius(25)
                    .sampling(1)
                    .color(getResources().getColor(R.color.black_20))
                    .capture(mBlurImageIv).into(mBlurImageIv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //隐藏动画
    private void onHideSplash() {
        mSplashImageIv.animate().alpha(0).setDuration(1000).setListener(new MyAnimatorListener(mSplashImageIv) {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startRipple();
            }
        }).start();
    }

    private void startRipple() {
        rippleBackground.setOnAnimationListener(new RippleBackground.OnAnimationListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {
                setupTopPanel();
            }
        });
        rippleBackground.startRippleAnimation();
        if (BuildConfig.DEBUG) {
            rippleBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rippleBackground.isRippleAnimationRunning()) {
                        rippleBackground.stopRippleAnimation();
                    } else {
                        rippleBackground.startRippleAnimation();
                    }
                }
            });
        }
    }

    /**
     * 动画结束后,显示公司logo
     */
    private void setupTopPanel() {
        String companyLogo = spUtils.getString(Constant.SpfManagerParams.CompanyLogoUrl, "");
        View animLogo;
        boolean isCompanyLogo = false;
        if (!TextUtils.isEmpty(companyLogo)) {
            topLogo.setVisibility(View.GONE);
            circleTopLogo.setVisibility(View.VISIBLE);
            // 放到动画结束后
            Glide.with(this).load(companyLogo)
                    .placeholder(R.mipmap.logo0001)
                    .override(200, 200).into(circleTopLogo);
            animLogo = circleTopLogo;
            isCompanyLogo = true;
        } else {
            topLogo.setVisibility(View.VISIBLE);
            circleTopLogo.setVisibility(View.GONE);
            animLogo = topLogo;
            isCompanyLogo = false;
        }
        startAnimations(animLogo, isCompanyLogo);
    }

    /**
     * 开启动画
     *
     * @param logoImage
     * @param isCompanyLogo
     */
    private void startAnimations(View logoImage, boolean isCompanyLogo) {
        AnimatorSet animatorSet = new AnimatorSet();
        final ObjectAnimator logoAlpha = ObjectAnimator.ofFloat(logoImage, "alpha", 0f, 1.0f);
        logoAlpha.setDuration(500);

        float ty = DisplayUtils.dp2px(getResources(), 25);
        final ObjectAnimator logoTranslationY = ObjectAnimator.ofFloat(logoImage, "translationY", -ty);
        logoTranslationY.setDuration(1000);
        final ObjectAnimator logoTextTranslationY = ObjectAnimator.ofFloat(logoText, "translationY", ty);
        final ObjectAnimator logoTextAlpha = ObjectAnimator.ofFloat(logoText, "alpha", 0f, 1.0f);
        logoTextTranslationY.setDuration(1000);
        logoTextAlpha.setDuration(1000);

        //向上移动动画
        AnimatorSet ySet = new AnimatorSet();
        ySet.setDuration(1000);
        final ObjectAnimator logoY = ObjectAnimator.ofFloat(logoImage, "y", DisplayUtils.dp2px(getResources(), 60));
        final ObjectAnimator logoTextY = ObjectAnimator.ofFloat(logoText, "y", DisplayUtils.dp2px(getResources(), 120));
        final ObjectAnimator logoScaleX = ObjectAnimator.ofFloat(logoImage, "scaleX", 1.0f, 1.2f);
        final ObjectAnimator logoTextScaleX = ObjectAnimator.ofFloat(logoText, "scaleX", 1.0f, 1.2f);
        final ObjectAnimator logoScaleY = ObjectAnimator.ofFloat(logoImage, "scaleY", 1.0f, 1.2f);
        final ObjectAnimator logoTextScaleY = ObjectAnimator.ofFloat(logoText, "scaleY", 1.0f, 1.2f);
        logoY.setInterpolator(new AccelerateDecelerateInterpolator());
        logoScaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        logoTextScaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        logoScaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        logoTextScaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        logoTextY.setInterpolator(new AccelerateDecelerateInterpolator());
        final ObjectAnimator loginPanelY = ObjectAnimator.ofFloat(loginPanel, "y", DisplayUtils.dp2px(getResources(), 210));
        loginPanelY.setStartDelay(200);
        loginPanelY.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator blurImageAlpha = ObjectAnimator.ofFloat(mBlurImageIv, "alpha", 1.0f, 0f);
        ySet.play(logoY).with(logoTextY).with(loginPanelY).with(logoScaleX).with(logoTextScaleX).with(logoScaleY).with(logoTextScaleY).with(blurImageAlpha);

        if (!isCompanyLogo) {
            logoText.setAlpha(0);
            logoText.setVisibility(View.VISIBLE);
            animatorSet.play(logoTranslationY).with(logoTextTranslationY).with(logoTextAlpha).before(ySet);
        } else {
            animatorSet.play(logoAlpha).before(ySet);
        }
        loginPanel.setVisibility(View.VISIBLE);
        loginPanel.setY(DisplayUtils.getDisplayHeight(getResources()));
        animatorSet.start();
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkChangedReceiver = new NetworkChangedReceiver();
        registerReceiver(networkChangedReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(MSG_BLUR, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangedReceiver);
//        sPlantGaiaaApplication.getInstance().exit();
    }

    //初始化用户数据,从缓存中取
    private void initUserData() {
        String[] loginInfo = spUtils.getLoginInfo();
        mUsernameEt.setText(loginInfo[0]);
        mPasswordEt.setText(loginInfo[1]);
        mCompanyCodeEt.setText(loginInfo[2]);
//        checkVersionUpdate();
        //是否记住密码
        mSavePasswordCb.setChecked(spUtils.isRememberPassword());
        mSavePasswordCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spUtils.setRememberPassword(getApplicationContext(), isChecked);
            }
        });
    }

    /*检查版本更新*/
//    private void checkVersionUpdate() {
//        SoftVersionManager softVersionManager = new SoftVersionManager(this);
//        softVersionManager.checkVersion(false);
//    }

    @OnClick({R.id.btn_login})
    void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //登录按钮
                login();
                break;
        }
    }

    /**
     * 登录过程....
     */
    private void login() {
        if (!checkInfo()) return;
        if (NetWorkUtils.isNetworkConnected(this)) {
            userParams = new RequestParams();
            userParams.put("username", username);
            userParams.put("password", password);
            userParams.put("clientCode", clientCode);
            loginUrl = Config.REMOTE_SERVER + USERLOGIN_API;
            client.setTimeout(3000);
            client.setConnectTimeout(3000);
            usedWifiNet = true;
            ProgressDialogUtil.show(LoginActivity.this, R.string.waiting, false);
            presenter.userlogin(loginUrl, userParams, username, password,clientCode);
        } else {
            ShowToast(R.string.NoNetwork);
        }

    }

    //检测用户输入参数
    private boolean checkInfo() {
        clientCode = mCompanyCodeEt.getText().toString().trim();
        if (TextUtils.isEmpty(clientCode)) {
            mCompanyCodeEt.setError(getString(R.string.companycode_requires));
            mCompanyCodeEt.requestFocus();
            return false;
        }
        username = mUsernameEt.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            mUsernameEt.setError(getString(R.string.username_requires));
            mUsernameEt.requestFocus();
            return false;
        }
        password = mPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            mPasswordEt.setError(getString(R.string.password_requires));
            mPasswordEt.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 登录成功的回调方法
     *
     * @param client
     * @param loginRes
     * @param username
     * @param password
     */
    @Override
    public void loginSuccess(AsyncHttpClient client, LoginRes loginRes, String username, String password,String comCode) {
        user_Id = loginRes.userId;
        if (loginRes != null && !TextUtils.isEmpty(user_Id) && loginRes.isSuccessed == 1) {
            //根据用户ID获取客户端信息
            presenter.getClientInfo(client, user_Id, username, password,comCode);
            Log.i("aaa", "loginSuccess");
        } else {
            ProgressDialogUtil.dismiss();
            ShowToast(R.string.login_failure);
        }
    }

    /**
     * 获取客户资料成功的回调
     *
     * @param clientInfoRes
     */
    @Override
    public void getClientInfoSuccess(ClientInfoRes clientInfoRes) {
        boolean isGetUserInfoSuccess = false;
        if (clientInfoRes != null) {
            Log.i("aaa", "获取用户信息成功");
            spUtils.setCompanyLogo(clientInfoRes.logoUrl);
            SPlantApplication.sClientInfo = clientInfoRes;
            SPlantApplication.sUserId = user_Id;
            //保存到缓存中
            saveToSpUtils(clientInfoRes);
            DownloadMapImage(clientInfoRes.mapUrl);
            Log.i("aaa", "保存用户信息成功");
            isGetUserInfoSuccess = true;
        }
        if (isGetUserInfoSuccess) {
            if (mSavePasswordCb.isChecked()) {
                spUtils.setLoginInfo(username, password, clientCode);
            }
            initLocalServerConfig();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userName", username);
            intent.putExtra("password", password);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
            finish();
        } else {
            ShowToast(R.string.login_failure);
        }
    }

    /**
     * 下载地图图片
     */
    private void DownloadMapImage(String mapUrl) {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            filesDir = getExternalFilesDir(null);
        } else {
            filesDir = getFilesDir();
        }
        File fileDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            ImageDownloader imageDownloader = new ImageDownloader(this,
                    fileDir.getAbsolutePath(), mapUrl);
            imageDownloader.setOnImageDownloadListener(
                    new ImageDownloader.OnImageDownloadListener() {
                        @Override
                        public void onResult(final String imageFilePath, ImageDownloader imageDownloader) {
                            //  Log.d("vincent", "保存地图成功");
                        }
                    });
            imageDownloader.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveToSpUtils(ClientInfoRes clientInfoRes) {
        spUtils.saveInt(Constant.SpfManagerParams.IsSupperMaster, clientInfoRes.isAuthUnit);
        spUtils.saveString(Constant.SpfManagerParams.LocalServerIP, clientInfoRes.ip);
        spUtils.saveString(Constant.SpfManagerParams.UserID, user_Id);
        spUtils.saveString(Constant.SpfManagerParams.ClientID, clientInfoRes.clientId);
        spUtils.saveString(Constant.SpfManagerParams.CompanyLogoUrl, clientInfoRes.logoUrl);
        spUtils.saveString(Constant.SpfManagerParams.CompanyName, clientInfoRes.title);
        spUtils.saveString(Constant.SpfManagerParams.CityName, clientInfoRes.city);
    }

    /**
     * 调用局域网内的接口
     */
    private void initLocalServerConfig() {
        CheckIsLANUtils checkIsLANUtils = new CheckIsLANUtils();
        checkIsLANUtils.CheckIsLANUtils(this);
        if (SPlantApplication.sClientInfo != null && !TextUtils.isEmpty(SPlantApplication.sClientInfo.ip)) {
            if (SPlantApplication.sClientInfo.ip.startsWith("http://")) {
                ServerAPI.getInstance().initLocalServer(SPlantApplication.sClientInfo.ip.trim());
            }
        }
    }

}
