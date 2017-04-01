package com.splant.smartgarden.ApiModel;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.splant.smartgarden.BuildConfig;
import com.splant.smartgarden.R;
import com.splant.smartgarden.SPlantApplication;
import com.splant.smartgarden.baseModel.BaseActivity;
import com.splant.smartgarden.baseModel.BaseRes;
import com.splant.smartgarden.beanModel.Responses.AreaInfo;
import com.splant.smartgarden.beanModel.Responses.AreaListRes;
import com.splant.smartgarden.beanModel.Responses.ClientInfoRes;
import com.splant.smartgarden.beanModel.Responses.ContentRes;
import com.splant.smartgarden.beanModel.Responses.ExplainRes;
import com.splant.smartgarden.beanModel.Responses.GatewayListRes;
import com.splant.smartgarden.beanModel.Responses.IntervalWateringSettingRes;
import com.splant.smartgarden.beanModel.Responses.LoginRes;
import com.splant.smartgarden.beanModel.Responses.ParaListRes;
import com.splant.smartgarden.beanModel.Responses.PlantListRes;
import com.splant.smartgarden.beanModel.Responses.PlantTypeRes;
import com.splant.smartgarden.beanModel.Responses.UnitListRes;
import com.splant.smartgarden.beanModel.Responses.WaterStatusRes;
import com.splant.smartgarden.beanModel.Responses.WeatherRes;
import com.splant.smartgarden.beanModel.config.Config;
import com.splant.smartgarden.beanModel.config.Constant;
import com.splant.smartgarden.utilModel.NetWorkUtils;
import com.splant.smartgarden.utilModel.SpfManager;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * 控制切换服务器
 */
public class ServerAPI {

    private static final long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MB
    private static final int CONNECTION_IME_OUT = 30000;  //网络连接超时3秒

    private ServerAPI() {

    }

    private static class SingletonHolder {
        private static final ServerAPI INSTANCE = new ServerAPI();
    }

    public static ServerAPI getInstance() {

        return SingletonHolder.INSTANCE;
    }

    private IServerAPI remoteServerAPI;
    private IServerAPI localServerAPI;

    private PingResult mPingResult;


    private boolean isInited = false;

    private Context mContext;
    private SpfManager spUtils;
    private String mLocalIP;
    private int mLocalPort;

    public void init(@NonNull final Context context) {
        if (!isInited) {
            spUtils = new SpfManager(context);
            mContext = context.getApplicationContext();
            Interceptor mCacheControlInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();

                    // Add Cache Control only for GET methods
                    if (request.method().equals("GET")) {
                        if (NetWorkUtils.isNetworkAvailable(mContext)) {
                            // 1 day
                            request.newBuilder()
                                    .header("Cache-Control", "only-if-cached")
                                    .build();
                        } else {
                            // 4 weeks stale
                            request.newBuilder()
                                    .header("Cache-Control", "public, max-stale=2419200")
                                    .build();
                        }
                    }

                    Response response = chain.proceed(request);

                    // Re-write response CC header to force use of cache
                    return response.newBuilder()
                            .header("Cache-Control", "public, max-age=86400") // 1 day
                            .build();
                }
            };

            Cache cache = new Cache(new File(context.getCacheDir(), "http"), SIZE_OF_CACHE);

            // Create OkHttpClient
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setCache(cache);
            okHttpClient.setConnectTimeout(300, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(300, TimeUnit.SECONDS);
            okHttpClient.setWriteTimeout(300, TimeUnit.SECONDS);

            // Add Cache-Control Interceptor
            okHttpClient.networkInterceptors().add(mCacheControlInterceptor);

            // Create Executor
            Executor executor = Executors.newCachedThreadPool();

            //云端地址
            RestAdapter restAdapter = new RestAdapter.Builder().setClient(
                    new OkClient(okHttpClient)).setExecutors(executor, executor)
                    .setEndpoint(Config.REMOTE_SERVER)
                    .setLogLevel(BuildConfig.LOG_DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                    .build();
            remoteServerAPI = restAdapter.create(IServerAPI.class);

            isInited = true;
            mPingResult = new PingResult();

        }
    }
//    public void initLocalServer(String ip, int port) {
//        mLocalIP = ip;
//        mLocalPort = port;
//        String url = "http://" + mLocalIP + ":" + mLocalPort;
//        initLocalServer(url);
//    }

    /**
     * 初始化本地服务器接口
     * 登录后查看是否在本地 Wi-Fi范围内
     *
     * @param url 服务器地址
     */
    public void initLocalServer(String url) {
        // TODO: 15/10/27 校验 url
        if (TextUtils.isEmpty(url)) {
            return;
        }
        String thisUrl = url;
        if (!thisUrl.startsWith("http://")) {
            thisUrl = "http://" + thisUrl;
        }
        if (TextUtils.isEmpty(mLocalIP) || mLocalPort < 1) {
            Uri uri = Uri.parse(thisUrl);
            mLocalIP = uri.getHost();
            mLocalPort = uri.getPort();
            if (mLocalPort == -1) {
                mLocalPort = 80;
            }
        }
        Interceptor mCacheControlInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                // Add Cache Control only for GET methods
                if (request.method().equals("GET")) {
                    if (NetWorkUtils.isNetworkAvailable(mContext)) {
                        // 1 day
                        request.newBuilder()
                                .header("Cache-Control", "only-if-cached")
                                .build();
                    } else {
                        // 4 weeks stale
                        request.newBuilder()
                                .header("Cache-Control", "public, max-stale=2419200")
                                .build();
                    }
                }

                Response response = chain.proceed(request);

                // Re-write response CC header to force use of cache
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age=86400") // 1 day
                        .build();
            }
        };
        Cache cache = new Cache(new File(mContext.getCacheDir(), "http"), SIZE_OF_CACHE);
        // Create OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        okHttpClient.setConnectTimeout(300, TimeUnit.SECONDS);//2016.9.19添加，为了解决超时问题
        // Add Cache-Control Interceptor
        okHttpClient.networkInterceptors().add((com.squareup.okhttp.Interceptor) mCacheControlInterceptor);
        // Create Executor
        Executor executor = Executors.newCachedThreadPool();
        RestAdapter restAdapter = new RestAdapter.Builder().setClient(
                new OkClient(okHttpClient)).setExecutors(executor, executor)
                .setEndpoint(url)
                .setLogLevel(BuildConfig.LOG_DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();
        localServerAPI = restAdapter.create(IServerAPI.class);

    }

    /**
     * app 退出的时候调用
     */
    public void unInit(@NonNull Context context) {
        if (isInited) {
            isInited = false;
        }
    }



    public class PingResult {
        public boolean locale;
        public boolean remote;
    }

    /**
     * ping 本地客户端地址,查看是否联通
     */


    public interface PingCallBack {
        public void CallBakc(Object response);
    }

    /**
     * 判断请求地址
     * 若本地客户端存在, 连接本地
     * 若本地客户端不存在, 连接云端
     *
     * @return
     */
    public IServerAPI getServerAPI() {
//        SpfManager SpfManager = new SpfManager(SPlantApplication.mApplication);
        if (mPingResult.locale == true && localServerAPI != null) {
            Log.d("vincent", "数据从客户端获取");

//            SpfManager.saveBoolean(Constant.SpfManagerParams.isLocal,true);

            return localServerAPI;
        }
        Log.d("vincent", "数据从云端获取");
//        SpfManager.saveBoolean(Constant.SpfManagerParams.isLocal,false);
        return remoteServerAPI;
    }

    public String getRemoteServerUrl() {
        return Config.REMOTE_SERVER;
    }

    //2016.9.19 获取登录的信息的URL
    public URL getLoginurl() throws MalformedURLException {
        URL url;
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonLogin.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonLogin.aspx?");
        }
        return url;
    }

    //2016.9.19 获取用户的信息的URL
    public URL getClientInfourl() throws MalformedURLException {
        URL url;
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonClientId.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonClientId.aspx?");
        }
        return url;
    }

    //2016.9.19 修改密码的URL
    private URL getModifyPasswordurl() throws MalformedURLException {
        URL url;
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonRePassword.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonRePassword.aspx?");
        }
        return url;
    }
    //2016.10.9 获取检测器列表的URL
    private URL getUnitListUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonUnit.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonUnit.aspx?");
        }
        return url;
    }
    //NOTICE 2016.10.10 获取浇灌定时浇水状态信息的URL
    private URL getIntervalWateringSettingUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonIntervalWateringSetting.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonIntervalWateringSetting.aspx?");
        }
        return url;
    }
    //NOTICE 2016.12.16 获取植物操作信息的URL
    private URL getUnitOpUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonUnitOper.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonUnitOper.aspx?");
        }
        return url;
    }
    //NOTICE 2016.12.13 获取网关信息的URL
    private URL getUserManualUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonExplain.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonExplain.aspx?");
        }
        return url;
    }

    //NOTICE 2017.1.3 获取用户手册信息的URL
    private URL getGatewayUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/api/Gateway?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/api/Gateway?");
        }
        return url;
    }

    //NOTICE 2016.10.10 获取浇灌开关给水状态信息的URL
    private URL getWaterStatusResUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonWateringStatus.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonWateringStatus.aspx?");
        }
        return url;
    }
    //NOTICE 2016.10.10 获取分类信息的URL
    private URL getPlantListResUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonPlant.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonPlant.aspx?");
        }
        return url;
    }

    //NOTICE 2017.1.23 获取app 说明的URL
    private URL getAppContentUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/ JsonContent.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/ JsonContent.aspx?");
        }
        return url;
    }

    //NOTICE 2016.10.13 获取区域列表的URL
    private URL getAreaListResUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonClient.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonClient.aspx?");
        }
        return url;
    }
    //NOTICE 2017.1.6 获取定时浇水编辑的URL
    private URL getWateringSettingEditUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonIntervalWateringSettingOP.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonIntervalWateringSettingOP.aspx?");
        }
        return url;
    }
    //NOTICE 2016.10.13 获取天气的URL
    private URL getWeatherResUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonWeather.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonWeather.aspx?");
        }
        return url;
    }
    //NOTICE 2017.2.13 获取植物种类的URL
    private URL getPlantTypeUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonPlantType.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonPlantType.aspx?");
        }
        return url;
    }

    //NOTICE 2016.12.7 的URL
    private URL getFeedWaterUrl() throws MalformedURLException {
        URL url;
//        SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
        Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
        if (isLocalInternet) {
            url = new URL(BaseActivity.getClientIPs() + "/API/JsonWatering.aspx?");
        } else {
            url = new URL(Config.REMOTE_SERVER + "/API/JsonWatering.aspx?");
        }
        return url;
    }

    /**
     * 登录接口，只读云端
     *
     * @param username 用户名
     * @param password 密码
     */
//    public LoginRes login(String username, String password) {
//        return getServerAPI().login(username, password);
//    }
//
    private String TAG = "login";
    private String loginResString = "";

    public LoginRes parseJsonMulti8(String strResult) {
        LoginRes loginRes = new LoginRes();
        String userId = "";
        int token = 0;
        try {
            if (strResult == null|| strResult== "") {
                loginRes.isSuccessed = 0;
                loginRes.userId = "";
                return loginRes;
            }
            JSONTokener jsonParser = new JSONTokener(strResult);
            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
            token = jsonObject.optInt("isSuccessed");
            if (token == 0) {
                loginRes.isSuccessed = 0;
                loginRes.userId = "";
                return loginRes;
            }
            userId = jsonObject.optString("UserId");

            Constant.TOKEN = jsonObject.optString("Token");//2016.12.5 添加Token值

        } catch (Exception e) {
            System.out.println("Jsons parse error !!!!");
            e.printStackTrace();
        }
        loginRes.userId = userId;
        loginRes.isSuccessed = token;
        return loginRes;
    }


    /**
     * 读取客户信息，只读云端
     */
//    public ClientInfoRes getClientInfo(String userId) {
//        return getServerAPI().getClientInfo(userId);
//    }
    public ClientInfoRes parseJsonMulti14(String strResult) {
        if (strResult == null || strResult== "") {
            return null;
        }

        return new Gson().fromJson(strResult,ClientInfoRes.class);
    }


    //NOTICE 2016.10.13 获取区域列表 修改为SyncHttpClient网络访问方式
    private String areaListTAG = "areaList";
    private String areaListString;
    public AreaListRes getAreaList(String clientId){
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("clientId", clientId);
        areaListString = "" ;
        String url = "";
        try {
            url = getAreaListResUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization", Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(areaListTAG, "onSuccess: ");
                        areaListString = response.toString();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(areaListTAG, "onFailure: ");
                    }
                }
        );
        Log.i(areaListTAG, "获取区域列表解析成功");
        return parseJsonMulti6(areaListString);
    }


    private AreaListRes parseJsonMulti6(String strResult) {
//        AreaListRes areaListRes = new AreaListRes();
//        int token = 0;
//        try {
            if (strResult == null|| strResult== "") {
                return null;
            }
        return new Gson().fromJson(strResult,AreaListRes.class);
    }

    public AreaListRes getAreaManager(String clientId, String action, String areaId, String areaName, String des) {
        String resultData = "";
        try {
            URL url;
            String urltemp = "";

            if (clientId != null && clientId != "") {
                urltemp = "ClientId=" + clientId;
            }

            if (areaId != null && areaId != "") {
                if (urltemp != "") {
                    urltemp += "&areaId=" + areaId;
                } else {
                    urltemp += "areaId=" + areaId;
                }
            }
            if (action != null && action != "") {
                if (urltemp != "") {
                    urltemp += "&action=" + action;
                } else {
                    urltemp += "action=" + action;
                }
            }
            if (areaName != null && areaName != "") {
                if (urltemp != "") {
                    urltemp += "&name=" + areaName;
                } else {
                    urltemp += "name=" + areaName;
                }
            }
            if (des != null && des != "") {
                if (urltemp != "") {
                    urltemp += "&descript=" + des;
                } else {
                    urltemp += "descript=" + des;
                }
            }
//            url = new URL(Config.LOCAL_SERVER);
//            mLocalIP = url.getHost();
//            mLocalPort = url.getPort();
//            PingUtils pingUtils = new PingUtils();
//            String  token = pingUtils.Ping(mLocalIP);
//            SpfManager SpfManager = new SpfManager(sPlantGaiaaApplication.getInstance());
            Boolean isLocalInternet = spUtils.getBoolean(Constant.SpfManagerParams.isLocalInternet);
            if (isLocalInternet) {
                url = new URL(BaseActivity.getClientIPs() + "/API/JsonAreaManager.aspx?" + urltemp);
            } else {
                url = new URL(Config.REMOTE_SERVER + "/API/JsonAreaManager.aspx?" + urltemp);
            }

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            urlConn.setDoOutput(true);

            // 设置是否从httpUrlConnection读入，默认情况下是true;
            urlConn.setDoInput(true);

            // Post 请求不能使用缓存
            urlConn.setUseCaches(false);

            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            urlConn.setRequestProperty("Content-type", "application/x-java-serialized-object");

            // 设定请求的方法为"POST"，默认是GET
            urlConn.setRequestMethod("POST");

            // 连接，上面对urlConn的所有配置必须要在connect之前完成，
            urlConn.connect();

            // 此处getOutputStream会隐含的进行connect (即：如同调用上面的connect()方法，
            // 所以在开发中不调用上述的connect()也可以)。
            OutputStream outStrm = urlConn.getOutputStream();

            // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
            ObjectOutputStream out = new ObjectOutputStream(outStrm);

//            String content = "clientId=" + URLEncoder.encode("D5139C15-0425-410A-8EE4-36409A1EA420", "gb2312") +
//                    "&locale=" + URLEncoder.encode("zh-CN", "gb2312");
            //将要上传的内容写入流中
//            out.writeBytes(content);

            //刷新、关闭
            out.flush();
            out.close();
            //获取数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine = null;
            //使用循环来读取获得的数据
            while (((inputLine = reader.readLine()) != null)) {
                //我们在每一行后面加上一个"\n"来换行
                resultData += inputLine + "\n";
            }
            reader.close();
            //关闭http连接
            urlConn.disconnect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseJsonMulti5(resultData);
    }

    private AreaListRes parseJsonMulti5(String strResult) {
        AreaListRes areaListRes = new AreaListRes();
        int token = 0;
        try {
            if (strResult == null|| strResult== "") {
                return null;
            }
            JSONTokener jsonParser = new JSONTokener(strResult);
            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
            token = jsonObject.getInt("isSuccessed");
            JSONArray jsonObjs = jsonObject.getJSONArray("AreaInfo");
            areaListRes.areaInfos = new ArrayList<AreaInfo>();

            for (int i = 0; i < jsonObjs.length(); i++) {
                JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
                AreaInfo areaInfo = new AreaInfo();
                areaInfo.title = jsonObj.getString("Title");
                areaInfo.descript = jsonObj.getString("Descript");
                areaInfo.areaId = jsonObj.getString("AreaId");
                areaListRes.areaInfos.add(areaInfo);
            }
        } catch (Exception e) {
            System.out.println("Jsons parse error !!!!");
            e.printStackTrace();
        }
        areaListRes.isSuccessed = token;
        return areaListRes;
    }



    //notice 2016.10.13 天气 修改为用SyncHttpClient网络访问方式
    private String weatherResTAG = "weatherRes";
    private String weatherResString;
    public WeatherRes getWeather(String clientId, String locale){
        SyncHttpClient client = new SyncHttpClient();
        client.setTimeout(20000);
        RequestParams params = new RequestParams();
        params.put("clientId", clientId);
        params.put("locale", locale);
        weatherResString = "" ;
        String url = "";
        try {
            url = getWeatherResUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization", Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(weatherResTAG, "onSuccess: ");
                        weatherResString = response.toString();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(weatherResTAG, "onFailure: ");
                    }
                }
        );

        Log.i(weatherResTAG, "获取天气解析成功");
        return parseJsonMulti3(weatherResString);
    }

    public WeatherRes parseJsonMulti3(String strResult) {
//        WeatherRes weatherRes = new WeatherRes();
//        int token = 0;
//        try {
            if (strResult == null|| strResult== "") {
                return null;
            }

        return new Gson().fromJson(strResult,WeatherRes.class);
    }


    /**
     * 植物种类（植物页面）
     *
     * @param clientId
     * @param locale
     * @return
     */
    private String PlantTypeResTAG = "PlantTypeRes";
    private String plantTypeResString = "";

    public PlantTypeRes getPlantType(String clientId, String locale) {
//        AsyncHttpClient client = new AsyncHttpClient();
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("clientId", clientId);
        params.put("locale", locale);
        plantTypeResString = "";
//        String url = "http://splant.sunrace-landscape.com:8080/API/JsonPlantType.aspx?";
        String url = "";
        try {
            url = getPlantTypeUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization", Constant.TOKEN);
//        client.post(url, params, new JsonHttpResponseHandler() {
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        plantTypeResString = response.toString();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(PlantTypeResTAG, "onFailure: ");
                    }
                }
        );
        PlantTypeRes plantTypeRes = parseJsonMulti7(plantTypeResString);
      //  Log.i(PlantTypeResTAG, "植物资料解析成功");
        return plantTypeRes;
    }

    private PlantTypeRes parseJsonMulti7(String strResult) {
        PlantTypeRes plantTypeRes = new PlantTypeRes();
        int token = 0;
        try {
            if (strResult == null|| strResult== "") {
                return null;
            }

            JSONTokener jsonParser = new JSONTokener(strResult);
            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
            token = jsonObject.getInt("isSuccessed");
            if (token == 0) {
                return null;
            }
            JSONArray jsonObjs = jsonObject.getJSONArray("PlantType");
            PlantTypeRes.PlantType plantType;
            plantTypeRes.plantTypes = new ArrayList<PlantTypeRes.PlantType>();
            for (int i = 0; i < jsonObjs.length(); i++) {
                JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
                plantType = new PlantTypeRes.PlantType();
                plantType.title = jsonObj.getString("Title");
                plantType.typeId = jsonObj.getString("TypeId");
                plantType.img = jsonObj.getString("Img");

                plantTypeRes.plantTypes.add(plantType);
            }
        } catch (Exception e) {
            System.out.println("Jsons parse error !!!!");
            e.printStackTrace();
        }
        plantTypeRes.isSuccessed = token;
        return plantTypeRes;
    }

    /**
     * 分类
     */


//    public PlantListRes getPlantList(@NonNull String clientId, @NonNull String locale, @NonNull String typeId, String pinyin, int page) {
//        try {
//            return getServerAPI().getPlantList(clientId, locale, typeId, pinyin, page);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    public PlantListRes getPlantList(@NonNull String clientId, @NonNull String locale, @NonNull String typeId, String pinyin, int page, boolean isLocalOrSvr) {
//        String resultData = "";
//        try {
//            URL url;
//            String urltemp = "";
////
//            if (isLocalOrSvr)
//                clientId = "";
//
//            if (clientId != null && clientId != "") {
//                urltemp = "ClientId=" + clientId;
//            }
//
//            if (locale != null && locale != "") {
//                if (urltemp != "") {
//                    urltemp += "&locale=" + locale;
//                } else {
//                    urltemp += "locale=" + locale;
//                }
//            }
//
//            if (typeId != null && typeId != "") {
//                if (urltemp != "") {
//                    urltemp += "&TypeId=" + typeId;
//                } else {
//                    urltemp += "TypeId=" + typeId;
//                }
//            }
//            if (pinyin != null && pinyin != "") {
//                if (urltemp != "") {
//                    urltemp += "&PingYin=" + pinyin;
//                } else {
//                    urltemp += "PingYin=" + pinyin;
//                }
//            }
//
//            if (page != 0) {
//                if (urltemp != "") {
//                    urltemp += "&Page=" + page;
//                } else {
//                    urltemp += "Page=" + page;
//                }
//            }
////            url = new URL(Config.LOCAL_SERVER);
////            mLocalIP = url.getHost();
////            mLocalPort = url.getPort();
////            PingUtils pingUtils = new PingUtils();
////            String  token = pingUtils.Ping(mLocalIP);
////            if (token=="faild") {
//
//            if (isLocalOrSvr == false) {
//                SpfManager SpfManager = new SpfManager(SPlantApplication.mApplication);
//                Boolean isLocalInternet = SpfManager.getBoolean(Constant.SpfManagerParams.isLocalInternet);
//                if (isLocalInternet) {//2016.8.30开启注释
//                    url = new URL(Config.LOCAL_SERVER + "/API/JsonPlant.aspx?" + urltemp);//2016.8.30开启注释
//                } else {//2016.8.30开启注释
//                    url = new URL(Config.REMOTE_SERVER + "/API/JsonPlant.aspx?" + urltemp);
//                }//2016.8.30开启注释
//            } else {
//                url = new URL(Config.REMOTE_SERVER + "/API/JsonPlant.aspx?" + urltemp);
//            }
//
//
//            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
//            // http正文内，因此需要设为true, 默认情况下是false;
//            urlConn.setDoOutput(true);
//
//            // 设置是否从httpUrlConnection读入，默认情况下是true;
//            urlConn.setDoInput(true);
//
//            // Post 请求不能使用缓存
//            urlConn.setUseCaches(false);
//
//            // 设定传送的内容类型是可序列化的java对象
//            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
//            urlConn.setRequestProperty("Content-type", "application/x-java-serialized-object");
//
//            // 设定请求的方法为"POST"，默认是GET
//            urlConn.setRequestMethod("POST");
//
//            // 连接，上面对urlConn的所有配置必须要在connect之前完成，
//            urlConn.connect();
//
//            // 此处getOutputStream会隐含的进行connect (即：如同调用上面的connect()方法，
//            // 所以在开发中不调用上述的connect()也可以)。
//            OutputStream outStrm = urlConn.getOutputStream();
//
//            // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
//            ObjectOutputStream out = new ObjectOutputStream(outStrm);
//
////            String content = "clientId=" + URLEncoder.encode("D5139C15-0425-410A-8EE4-36409A1EA420", "gb2312") +
////                    "&locale=" + URLEncoder.encode("zh-CN", "gb2312");
//            //将要上传的内容写入流中
////            out.writeBytes(content);
//
//            //刷新、关闭
//            out.flush();
//            out.close();
//            //获取数据
//            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
//            String inputLine = null;
//            //使用循环来读取获得的数据
//            while (((inputLine = reader.readLine()) != null)) {
//                //我们在每一行后面加上一个"\n"来换行
//                resultData += inputLine + "\n";
//            }
//            reader.close();
//            //关闭http连接
//            urlConn.disconnect();
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return parseJsonMulti2(resultData);
//    }


    //notice 2016.10.10 分类 修改为用SyncHttpClient网络访问方式
    private String plantListTAG = "plantList";
    private String plantListString;

    public PlantListRes getPlantList(@NonNull String clientId, @NonNull String locale, @NonNull String typeId, String pinyin, int page, boolean isLocalOrSvr) {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("clientId", clientId);
        params.put("locale", locale);
        params.put("typeId", typeId);
        params.put("pinyin", pinyin);
        params.put("page", page);
        params.put("isLocalOrSvr", isLocalOrSvr);
        plantListString = "" ;
        String url = "";
        try {
            url = getPlantListResUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization", Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(plantListTAG, "onSuccess: ");
                        plantListString = response.toString();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(plantListTAG, "onFailure: ");
                    }
                }
        );

        Log.i(plantListTAG, "获取分类解析成功");
        return parseJsonMulti2(plantListString);
    }



    public PlantListRes parseJsonMulti2(String strResult) {
//        PlantListRes plantListRes = new PlantListRes();
//        int token = 0;
//        try {
            if (strResult == null || strResult== "") {
                return null;
            }
//            JSONTokener jsonParser = new JSONTokener(strResult);
//            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
//            token = jsonObject.getInt("isSuccessed");
//            if (token == 0) {
//                return null;
//            }
//            JSONArray jsonObjs = jsonObject.getJSONArray("PlantInfo");
//
//
//            PlantListRes.PlantInfo plantInfo;
//
//            plantListRes.plantInfos = new ArrayList<PlantListRes.PlantInfo>();
//            for (int i = 0; i < jsonObjs.length(); i++) {
//                JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
//                plantInfo = new PlantListRes.PlantInfo();
//                plantInfo.title = jsonObj.getString("Title");
//                plantInfo.plantType = jsonObj.getString("PlantType");
//                plantInfo.descript = jsonObj.getString("Descript");
//                plantInfo.plantId = jsonObj.getString("PlantId");
//                plantInfo.pinyin = jsonObj.getString("Pinyin");
//                plantInfo.latin = jsonObj.getString("Latin");
//                plantInfo.soilTemp = jsonObj.getString("SoilTemp");
//                plantInfo.soilHumid = jsonObj.getString("SoilHumid");
//                plantInfo.soilNutr = jsonObj.getString("SoilNutr");
//                plantInfo.soilPH = jsonObj.getString("SoilPH");
//                plantInfo.enviTemp = jsonObj.getString("EnviTemp");
//                plantInfo.enviHumid = jsonObj.getString("EnviHumid");
//                plantInfo.enviShine = jsonObj.getString("EnviShine");
//                plantInfo.image = jsonObj.getString("image");
//                plantListRes.plantInfos.add(plantInfo);
//            }
//
//        } catch (Exception e) {
//            System.out.println("Jsons parse error !!!!");
//            e.printStackTrace();
//        }
//        plantListRes.isSuccessed = token;
//        return plantListRes;

            return new Gson().fromJson(strResult,PlantListRes.class);
    }


//    public ParaListRes getParaList(long begin, long end, String clientId, String unitId, String locale) {
//        try {
//            if (begin == 0 || end == 0) {
//                return getServerAPI().getParaList(clientId, unitId, locale);
//            }
//            return getServerAPI().getParaList(begin, end, clientId, unitId, locale);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 监控器指标数据
     * 图表数据
     *
     * @param begin
     * @param end
     * @param clientId
     * @param unitId
     * @param locale
     * @return
     */
    public ParaListRes getParaList(long begin, long end, String clientId, String unitId, String locale) {
        String resultData = "";
        try {
            URL url;
            String urltemp = "";
            if (begin != 0) {
                urltemp = "begin=" + begin;
            }
            if (end != 0) {
                if (urltemp != "") {
                    urltemp += "&end=" + end;
                } else {
                    urltemp += "end=" + end;
                }
            }
            if (clientId != null && clientId != "") {
                if (urltemp != "") {
                    urltemp += "&clientid=" + clientId;
                } else {
                    urltemp += "clientid=" + clientId;
                }
            }
            if (unitId != null && unitId != "") {
                if (urltemp != "") {
                    urltemp += "&unitid=" + unitId;
                } else {
                    urltemp += "unitid=" + unitId;
                }
            }
            if (locale != null && locale != "") {
                if (urltemp != "") {
                    urltemp += "&locale=" + locale;
                } else {
                    urltemp += "locale=" + locale;
                }
            }
//            url = new URL(Config.LOCAL_SERVER);
//            mLocalIP = url.getHost();
//            mLocalPort = url.getPort();
//            PingUtils pingUtils = new PingUtils();
//            String  token = pingUtils.Ping(mLocalIP);
//            if (token=="faild") {
            SpfManager SpfManager = new SpfManager(SPlantApplication.getInstance());
            Boolean isLocalInternet = SpfManager.getBoolean(Constant.SpfManagerParams.isLocalInternet);
            if (isLocalInternet) {
                url = new URL(BaseActivity.getClientIPs() + "/API/JsonCuveApps.aspx?" + urltemp);
            } else {
                url = new URL(Config.REMOTE_SERVER + "/API/JsonCuveApps.aspx?" + urltemp);

            }

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.addRequestProperty("authorization", Constant.TOKEN);
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            urlConn.setDoOutput(true);

            // 设置是否从httpUrlConnection读入，默认情况下是true;
            urlConn.setDoInput(true);

            // Post 请求不能使用缓存
            urlConn.setUseCaches(false);

            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            urlConn.setRequestProperty("Content-type", "application/x-java-serialized-object");

            // 设定请求的方法为"POST"，默认是GET
            urlConn.setRequestMethod("POST");

            // 连接，上面对urlConn的所有配置必须要在connect之前完成，
            urlConn.connect();

            // 此处getOutputStream会隐含的进行connect (即：如同调用上面的connect()方法，
            // 所以在开发中不调用上述的connect()也可以)。
            OutputStream outStrm = urlConn.getOutputStream();

            // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
            ObjectOutputStream out = new ObjectOutputStream(outStrm);

//            String content = "clientId=" + URLEncoder.encode("D5139C15-0425-410A-8EE4-36409A1EA420", "gb2312") +
//                    "&locale=" + URLEncoder.encode("zh-CN", "gb2312");
            //将要上传的内容写入流中
//            out.writeBytes(content);

            //刷新、关闭
            out.flush();
            out.close();
            //获取数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine = "";
            //使用循环来读取获得的数据
            while (((inputLine = reader.readLine()) != null)) {
                //我们在每一行后面加上一个"\n"来换行
                resultData += inputLine + "\n";
            }
            reader.close();
            //关闭http连接
            urlConn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseJsonMulti1(resultData);
    }

    public ParaListRes parseJsonMulti1(String strResult) {

        ParaListRes paraListRes = new ParaListRes();
        int token = 0;
        try {
            if (strResult == null|| strResult== "") {
                return null;
            }
            JSONTokener jsonParser = new JSONTokener(strResult);
            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
            token = jsonObject.getInt("isSuccessed");
            JSONArray jsonObjs = jsonObject.getJSONArray("ParaList");
            ParaListRes.Para para;
            ParaListRes.ParaInfo paraInfo;
            ParaListRes.ValueInfo valueInfo;
            paraListRes.paras = new ArrayList<ParaListRes.Para>();
            for (int i = 0; i < jsonObjs.length(); i++) {
                para = new ParaListRes.Para();
                JSONObject jsonObj;
                jsonObject = ((JSONObject) jsonObjs.opt(i));
                jsonObj = (JSONObject) jsonObject.getJSONObject("ParaInfo");
                paraInfo = new ParaListRes.ParaInfo();
                paraInfo.paraName = jsonObj.optString("ParaName");
                paraInfo.dataName = jsonObj.optString("DataName");
                paraInfo.upper = jsonObj.optString("Upper");
                paraInfo.lower = jsonObj.optString("Lower");
                paraInfo.plant = jsonObj.optString("Plant");
                paraInfo.unitTitle = jsonObj.optString("UnitTitle");
                paraInfo.unitId = jsonObj.optString("UnitID");
                para.paraInfo = paraInfo;
                jsonObj = ((JSONObject) jsonObjs.opt(i));
                JSONArray ValueInfojsonObjs;
                ValueInfojsonObjs = (JSONArray) jsonObj.getJSONArray("ValueInfo");
                para.valueInfos = new ArrayList<ParaListRes.ValueInfo>();
                for (int j = 0; j < ValueInfojsonObjs.length(); j++) {
                    jsonObj = ((JSONObject) ValueInfojsonObjs.opt(j));
                    valueInfo = new ParaListRes.ValueInfo();
                    valueInfo.valueStr = jsonObj.optString("Value");
                    valueInfo.numValue = jsonObj.optInt("NumValue");
                    valueInfo.createTime = jsonObj.optLong("CreateTime");
                    valueInfo.alarm = jsonObj.optInt("Alarm");
                    para.valueInfos.add(valueInfo);
                }
                paraListRes.paras.add(para);
            }
        } catch (Exception e) {
            System.out.println("Jsons parse error !!!!");
            e.printStackTrace();
        }
        paraListRes.isSuccessed = token;
        return paraListRes;
    }


//    public UnitListRes getUnitList(String clientId, @Nullable String areaId, @Nullable String plantTypeId, @Nullable String key, String locale) {
//        try {
//            return getServerAPI().getUnitList(clientId, areaId, plantTypeId, key, locale);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 检测器（节点）列表
     *
     * @param clientId
     * @param areaId
     * @param plantTypeId
     * @param key
     * @param locale
     * @return
     */

    private String unitListTAG = "unitList";
    private String unitListString;
    public UnitListRes getUnitList(String clientId, @org.jetbrains.annotations.Nullable String areaId, @Nullable String plantTypeId, @Nullable String key, String locale){


        SyncHttpClient client = new SyncHttpClient();
        client.setTimeout(20000);
        client.setConnectTimeout(20000);

        RequestParams params = new RequestParams();
        params.put("clientId", clientId);
        params.put("areaId", areaId);
        params.put("plantTypeId", plantTypeId);
        params.put("key", key);
        params.put("locale", locale);
        unitListString = "" ;
        String url = "";
        try {
            url = getUnitListUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization", Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(unitListTAG, "onSuccess: ");
                        unitListString = response.toString();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(unitListTAG, "onFailure: ");
                    }
                }
        );

        Log.i(unitListTAG, "获取检测器列表解析成功");
        return parseJsonMulti(unitListString);
    }

    public UnitListRes parseJsonMulti(String strResult) {
        UnitListRes unitListRes = new UnitListRes();
        int token = 0;
        try {
            if (strResult == null || strResult== "") {
                return null;
            }
            JSONTokener jsonParser = new JSONTokener(strResult);
            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
            token = jsonObject.optInt("isSuccessed");
            JSONArray jsonObjs = jsonObject.optJSONArray("UnitInfo");
            UnitListRes.UnitInfo unitInfo;
            UnitListRes.UnitWater unitWater;
            unitListRes.unitInfos = new ArrayList<UnitListRes.UnitInfo>();
            for (int i = 0; i < jsonObjs.length(); i++) {
                JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
                unitInfo = new UnitListRes.UnitInfo();
                unitInfo.dbId = jsonObj.optString("UnitId");
                unitInfo.title = jsonObj.optString("Title");
                unitInfo.areaId = jsonObj.optString("AreaId");
                unitInfo.area = jsonObj.optString("Area");
                unitInfo.descript = jsonObj.optString("Deacrtip");
                unitInfo.mapCoord = jsonObj.optString("MapCoord");
                unitInfo.lastWater = jsonObj.optInt("LastWarter");
                unitInfo.gatewayId = jsonObj.optString("GatewayId"); //2016.12.22 添加
                unitInfo.gatewayName = jsonObj.optString("GatewayName");//2016.12.22 添加
                unitInfo.state = jsonObj.optInt("State");
                unitInfo.alarm = jsonObj.optInt("Alarm");
                if (jsonObj.optString("Plant").toString().equals("null")) {
                    unitInfo.plant = "";
                } else {
                    unitInfo.plant = jsonObj.optString("Plant");
                }
                unitInfo.plantId = jsonObj.optString("PlantId");
                unitInfo.plantType = jsonObj.optString("PlantType");
                unitInfo.amount = jsonObj.optInt("Amount");
                unitInfo.unitId = jsonObj.optString("UnitId");
                unitInfo.img = jsonObj.optString("Img");
                if (jsonObj.optString("Name").toString().equals("null")) {
                    unitInfo.name = "";
                } else {
                    unitInfo.name = jsonObj.optString("Name");
                }
                unitInfo.powerEnery = jsonObj.optString("DumoEnergy");
                unitInfo.waterUnitId = jsonObj.optString("WaterUnitID");

                if (jsonObj.optString("WaterGateNo").toString().equals("")) {
                    unitInfo.waterGateNo = SPlantApplication.getInstance().getString(R.string.none);
                } else {
                    unitInfo.waterGateNo = jsonObj.optString("WaterGateNo");
                }

                if (jsonObj.optString("WaterUnitName").toString().equals("null")) {
                    unitInfo.waterUnitName = SPlantApplication.getInstance().getString(R.string.none);
                } else {
                    unitInfo.waterUnitName = jsonObj.optString("WaterUnitName");
                }
                unitInfo.devType = jsonObj.optString("deviceType");
                unitListRes.unitInfos.add(unitInfo);

            }

            jsonObjs = jsonObject.optJSONArray("UnitWater");
            unitListRes.unitWaters = new ArrayList<UnitListRes.UnitWater>();
            for (int i = 0; i < jsonObjs.length(); i++) {
                JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
                unitWater = new UnitListRes.UnitWater();
                unitWater.dbId = jsonObj.optString("UnitId");
                unitWater.deviceId = jsonObj.optString("DeviceId");
                unitWater.deviceName = jsonObj.optString("Name");
                unitWater.mapCoord = jsonObj.optString("MapCoord");
                unitWater.remarks = jsonObj.optString("remarks");
                unitWater.gatewayId = jsonObj.optString("GatewayId");//2016.12.22 添加
                unitWater.gatewayName = jsonObj.optString("GatewayName");//2016.12.22 添加
                unitListRes.unitWaters.add(unitWater);
            }

        } catch (Exception e) {
            System.out.println("Jsons parse error !!!!");
            e.printStackTrace();
        }
        unitListRes.isSuccessed = token;
        return unitListRes;
//        return new Gson().fromJson(strResult,UnitListRes.class);
    }


    /**
     * 用户手册接口，读云端
     */
    public ExplainRes getExplain(String locale) {
        try {
            return remoteServerAPI.getExplain(locale);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取浇灌开关给水状态信息
     */
    //NOTICE 2016.7.26 添加
//    public WaterStatusRes wateringStatus(String deviceType, String waterId, String gateNo, String status, String index) {
//        String resultData = "";
//        WaterStatusRes waterStatusRes=null;
//        try
//        {
//            URL url;
//            String urltemp = "";
//            if (deviceType != null && deviceType != "") {
//                urltemp = "deviceType=" + deviceType;
//            }
//            if (waterId != null && waterId != "") {
//                if (urltemp != "") {
//                    urltemp += "&waterId=" + waterId;
//                } else {
//                    urltemp += "waterId=" + waterId;
//                }
//            }
//            urltemp += "&gateNo=" + gateNo;//因为（该参数）它可以为空
//            if (status != null && status != "") {
//                if (urltemp != "") {
//                    urltemp += "&status=" + status;
//                } else {
//                    urltemp += "status=" + status;
//                }
//            }
//            if (index != null && index != "") {
//                if (urltemp != "") {
//                    urltemp += "&index=" + index;
//                } else {
//                    urltemp += "index=" + index;
//                }
//            }
//
//            SpfManager SpfManager = new SpfManager(SPlantApplication.mApplication);
//            Boolean isLocalInternet = SpfManager.getBoolean(Constant.SpfManagerParams.isLocalInternet);
//            if (isLocalInternet) {
//                url = new URL(BaseActivity.getClientIPs() + "/API/JsonWateringStatus.aspx?" + urltemp);
//            } else {
//                url = new URL(Config.REMOTE_SERVER + "/API/JsonWateringStatus.aspx?" + urltemp);
//            }
//
//            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//            urlConn.setConnectTimeout(20000);//连接云端长时间等待
//            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
//            // http正文内，因此需要设为true, 默认情况下是false;
//            urlConn.setDoOutput(true);
//            // 设置是否从httpUrlConnection读入，默认情况下是true;
//            urlConn.setDoInput(true);
//            // Post 请求不能使用缓存
//            urlConn.setUseCaches(false);
//            // 设定传送的内容类型是可序列化的java对象
//            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
//            urlConn.setRequestProperty("Content-type", "application/x-java-serialized-object");
//
//            // 设定请求的方法为"POST"，默认是GET
//            urlConn.setRequestMethod("POST");
//
//            // 连接，上面对urlConn的所有配置必须要在connect之前完成，
//            urlConn.connect();
//
//            //获取数据
//            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
//            String inputLine = null;
//            //使用循环来读取获得的数据
//            while (((inputLine = reader.readLine()) != null)) {
//                //我们在每一行后面加上一个"\n"来换行
//                resultData += inputLine + "\n";
//            }
//            reader.close();
//            //关闭http连接
//            urlConn.disconnect();
//            waterStatusRes=parseJsonMulti10(resultData);
//        }
//        catch (MalformedURLException e)
//        {
//            return null;
//        } catch (IOException e) {
//            return null;
//        } catch (Exception e) {
//            return null;
//        }
//        return waterStatusRes;
//    }

    //NOTICE 2016.10.10 获取浇灌开关给水状态信息 修改为SyncHttpClient网络请求方式
    private String waterStatusResTAG = "waterStatusRes";
    private String waterStatusResString;

    public WaterStatusRes wateringStatus(String deviceType, String waterId, String gateNo, String status, String index) {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("deviceType", deviceType);
        params.put("waterId", waterId);
        params.put("gateNo", gateNo);
        params.put("status", status);
        params.put("index", index);
        waterStatusResString = "" ;
        String url = "";
        try {
            url = getWaterStatusResUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization",Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(waterStatusResTAG, "onSuccess: ");
                        waterStatusResString = response.toString();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(waterStatusResTAG, "onFailure: ");
                    }
                }
        );

        Log.i(waterStatusResTAG, "获取浇灌开关给水状态信息解析成功");
        return parseJsonMulti10(waterStatusResString);
    }

    private WaterStatusRes parseJsonMulti10(String strResult) {
        if (strResult.equals("")|| strResult== ""|| strResult == null) {
            return null;
        }
//        WaterStatusRes waterStatusRes = null;
//        String msg = "";
//        int token = 0;
//        String index = "";
//        try {
//            waterStatusRes = new WaterStatusRes();
//            JSONTokener jsonParser = new JSONTokener(strResult);
//            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
//            msg = jsonObject.optString("message");
//            token = jsonObject.optInt("isSuccessed");
//            index = jsonObject.optString("Index");//暂时APP端用不到这个属性
//            JSONArray jsonObjs = jsonObject.getJSONArray("WaterStatus");
//            WaterStatusRes.WaterStatus waterStatus;
//            waterStatusRes.waterStatusList = new ArrayList<WaterStatusRes.WaterStatus>();
//            for (int i = 0; i < jsonObjs.length(); i++) {
//                JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
//                waterStatus = new WaterStatusRes.WaterStatus();
//                waterStatus.Id = jsonObj.optString("Id");
//                waterStatus.SiteId = jsonObj.optString("SiteId");
//                waterStatus.CreateTime = jsonObj.optString("CreateTime");
//                waterStatus.ClientId = jsonObj.optString("ClientId");
//                waterStatus.DeviceType = jsonObj.optString("DeviceType");
//                waterStatus.DeviceIdentifierId = jsonObj.optString("DeviceIdentifierId");
//                waterStatus.DeviceId = jsonObj.optString("DeviceId");
//                waterStatus.WaterDeviceIdentifierId = jsonObj.optString("WaterDeviceIdentifierId");
//                waterStatus.WaterDeviceId = jsonObj.optString("WaterDeviceId");
//                waterStatus.GateNo = jsonObj.optString("GateNo");
//                waterStatus.StartTime = jsonObj.optString("StartTime");
//                waterStatus.SupplyTime = jsonObj.optString("SupplyTime");
//                waterStatus.UpdateStatusTime = jsonObj.optString("UpdateStatusTime");
//                waterStatus.RestTime = jsonObj.optString("RestTime");
//                waterStatus.Status = jsonObj.optString("Status");
//                waterStatusRes.waterStatusList.add(waterStatus);
//            }
//        } catch (Exception e) {
//            System.out.println("Jsons parse error !!!!");
//            e.printStackTrace();
//        }
//        waterStatusRes.isSuccessed = token;
//        waterStatusRes.message = msg;
//        waterStatusRes.Index = index;
//        return waterStatusRes;
        return new Gson().fromJson(strResult, WaterStatusRes.class);
    }


    /**
     * 获取浇灌定时浇水状态信息
     */

    //notice 2016.10.10 获取浇灌定时浇水状态信息 修改为SyncHttpClient网络请求方式
    private String intervalWateringSettingTAG = "intervalWateringSetting";
    private String intervalWateringSettingString;
    public IntervalWateringSettingRes intervalWateringSetting(String clientId, String locale, String rows, String page, String sort, String order) {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("clientId", clientId);
        params.put("locale", locale);
        params.put("rows", rows);
        params.put("page", page);
        params.put("sort", sort);
        params.put("order", order);
        intervalWateringSettingString = "" ;
        String url = "";
        try {
            url = getIntervalWateringSettingUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization",Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(intervalWateringSettingTAG, "onSuccess: ");
                        intervalWateringSettingString = response.toString();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(intervalWateringSettingTAG, "onFailure: ");
                    }
                }
        );
        Log.i(intervalWateringSettingTAG, "获取浇灌定时浇水状态信息解析成功");
        return parseJsonMulti11(intervalWateringSettingString);
    }


    //NOtice LianXi
    private IntervalWateringSettingRes parseJsonMulti11(String strResult) {
        return new Gson().fromJson(strResult, IntervalWateringSettingRes.class);
    }

    /**
     * 定时浇水编辑
     */
    //NOTICE 2017.1.6  获取定时浇水编辑 修改为SyncHttpClient网络访问方式
    private String wateringSettingEditTAG = "wateringSettingEdit";
    private String wateringSettingEditString;

    public BaseRes wateringSettingEdit(String action, String intervalWateringSettingId, String classifyName, String periodList, String startTime, String waterTimeLength) {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("action", action);
        params.put("intervalWateringSettingId", intervalWateringSettingId);
        params.put("classifyName", classifyName);
        params.put("periodList", periodList);
        params.put("startTime", startTime);
        params.put("waterTimeLength", waterTimeLength);
        wateringSettingEditString = "" ;
        String url = "";
        try {
            url = getWateringSettingEditUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization", Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(wateringSettingEditTAG, "onSuccess: ");
                        wateringSettingEditString = response.toString();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(wateringSettingEditTAG, "onFailure: ");
                    }
                }
        );
        Log.i(wateringSettingEditTAG, "获取定时浇水编辑解析成功");
        return parseJsonMulti12(wateringSettingEditString);
    }

    private BaseRes parseJsonMulti12(String wateringSettingEditString) {
        return new Gson().fromJson(wateringSettingEditString,BaseRes.class);
    }


//    /**
//     * 定时浇水编辑
//     */
//    //NOTICE 2016.8.24 添加
//    public BaseRes wateringSettingEdit(String action, String intervalWateringSettingId, String classifyName, String periodList, String startTime, String waterTimeLength) {
//        String resultData = "";
//        try {
//            URL url;
//            String urltemp = "";
//
//            if (action != null && action != "") {
//                urltemp = "action=" + action;
//            }
//
//            if (intervalWateringSettingId != null && intervalWateringSettingId != "") {
//                if (urltemp != "") {
//                    urltemp += "&intervalWateringSettingId=" + intervalWateringSettingId;
//                } else {
//                    urltemp += "intervalWateringSettingId=" + intervalWateringSettingId;
//                }
//            }
//            if (classifyName != null && classifyName != "") {
//                if (urltemp != "") {
//                    urltemp += "&classifyName=" + classifyName;
//                } else {
//                    urltemp += "classifyName=" + classifyName;
//                }
//            }
//
//
//            if (periodList != null && periodList != "") {
//                if (urltemp != "") {
//                    urltemp += "&periodList=" + periodList;
//                } else {
//                    urltemp += "periodList=" + periodList;
//                }
//            }
//
//            if (startTime != null && startTime != "") {
//                if (urltemp != "") {
//                    urltemp += "&startTime=" + startTime;
//                } else {
//                    urltemp += "startTime=" + startTime;
//                }
//            }
//            if (waterTimeLength != null && waterTimeLength != "") {
//                if (urltemp != "") {
//                    urltemp += "&waterTimeLength=" + waterTimeLength;
//                } else {
//                    urltemp += "waterTimeLength=" + waterTimeLength;
//                }
//            }
//
//            SpfManager SpfManager = new SpfManager(SPlantApplication.mApplication);
//            Boolean isLocalInternet = SpfManager.getBoolean(Constant.SpfManagerParams.isLocalInternet);
//            if (isLocalInternet) {
//                url = new URL(BaseActivity.getClientIPs() + "/API/JsonIntervalWateringSettingOP.aspx?" + urltemp);
//            } else {
//                url = new URL(Config.REMOTE_SERVER + "/API/JsonIntervalWateringSettingOP.aspx?" + urltemp);
//            }
//
//            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//
//            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
//            // http正文内，因此需要设为true, 默认情况下是false;
//            urlConn.setDoOutput(true);
//
//            // 设置是否从httpUrlConnection读入，默认情况下是true;
//            urlConn.setDoInput(true);
//
//            // Post 请求不能使用缓存
//            urlConn.setUseCaches(false);
//
//            // 设定传送的内容类型是可序列化的java对象
//            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
//            urlConn.setRequestProperty("Content-type", "application/x-java-serialized-object");
//
//            // 设定请求的方法为"POST"，默认是GET
//            urlConn.setRequestMethod("POST");
//
//            // 连接，上面对urlConn的所有配置必须要在connect之前完成，
//            urlConn.connect();
//
//            // 此处getOutputStream会隐含的进行connect (即：如同调用上面的connect()方法，
//            // 所以在开发中不调用上述的connect()也可以)。
////            OutputStream outStrm = urlConn.getOutputStream();
//
//            // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
////            ObjectOutputStream out = new ObjectOutputStream(outStrm);
//
//
//            //刷新、关闭
////            out.flush();
////            out.close();
//            //获取数据
//            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
//            String inputLine = null;
//            //使用循环来读取获得的数据
//            while (((inputLine = reader.readLine()) != null)) {
//                //我们在每一行后面加上一个"\n"来换行
//                resultData += inputLine + "\n";
//            }
//            reader.close();
//            //关闭http连接
//            urlConn.disconnect();
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return parseJsonMulti12(resultData);
//    }
//
//    private BaseRes parseJsonMulti12(String strResult) {
//        BaseRes baseRes = new BaseRes();
//        String msg = "";
//        int token = 0;
//        try {
//            if (strResult == null|| strResult== "") {
//                return null;
//            }
//            JSONTokener jsonParser = new JSONTokener(strResult);
//            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
//            msg = jsonObject.optString("message");
//            token = jsonObject.optInt("isSuccessed");
//        } catch (Exception e) {
//            System.out.println("Jsons parse error !!!!");
//            e.printStackTrace();
//        }
////        baseRes.isSuccessed = 1;
//        baseRes.isSuccessed = token;
//        baseRes.message = msg;
//        return baseRes;
//    }

    /**
     * 定时浇水增加
     */
    //Notice 2016.8.24添加
    public BaseRes wateringSettingAdd(String action, String clientId, String classifyName, String deviceList, String periodList, String startTime, String waterTimeLength) {
        String resultData = "";
        try {
            URL url;
            String urltemp = "";

            if (action != null && action != "") {
                urltemp = "action=" + action;
            }

            if (clientId != null && clientId != "") {
                if (urltemp != "") {
                    urltemp += "&clientId=" + clientId;
                } else {
                    urltemp += "clientId=" + clientId;
                }
            }
            if (classifyName != null && classifyName != "") {
                if (urltemp != "") {
                    urltemp += "&classifyName=" + classifyName;
                } else {
                    urltemp += "classifyName=" + classifyName;
                }
            }

            if (deviceList != null && deviceList != "") {
                if (urltemp != "") {
                    urltemp += "&deviceList=" + deviceList;
                } else {
                    urltemp += "deviceList=" + deviceList;
                }
            }
            if (periodList != null && periodList != "") {
                if (urltemp != "") {
                    urltemp += "&periodList=" + periodList;
                } else {
                    urltemp += "periodList=" + periodList;
                }
            }


            if (startTime != null && startTime != "") {
                if (urltemp != "") {
                    urltemp += "&startTime=" + startTime;
                } else {
                    urltemp += "startTime=" + startTime;
                }
            }
            if (waterTimeLength != null && waterTimeLength != "") {
                if (urltemp != "") {
                    urltemp += "&waterTimeLength=" + waterTimeLength;
                } else {
                    urltemp += "waterTimeLength=" + waterTimeLength;
                }
            }

            SpfManager SpfManager = new SpfManager(SPlantApplication.getInstance());
            Boolean isLocalInternet = SpfManager.getBoolean(Constant.SpfManagerParams.isLocalInternet);
            if (isLocalInternet) {
                url = new URL(BaseActivity.getClientIPs() + "/API/JsonIntervalWateringSettingOP.aspx?" + urltemp);
            } else {
                url = new URL(Config.REMOTE_SERVER + "/API/JsonIntervalWateringSettingOP.aspx?" + urltemp);
            }

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            urlConn.setDoOutput(true);

            // 设置是否从httpUrlConnection读入，默认情况下是true;
            urlConn.setDoInput(true);

            // Post 请求不能使用缓存
            urlConn.setUseCaches(false);

            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            urlConn.setRequestProperty("Content-type", "application/x-java-serialized-object");

            // 设定请求的方法为"POST"，默认是GET
            urlConn.setRequestMethod("POST");

            // 连接，上面对urlConn的所有配置必须要在connect之前完成，
            urlConn.connect();

            // 此处getOutputStream会隐含的进行connect (即：如同调用上面的connect()方法，
            // 所以在开发中不调用上述的connect()也可以)。
//            OutputStream outStrm = urlConn.getOutputStream();

            // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
//            ObjectOutputStream out = new ObjectOutputStream(outStrm);


            //刷新、关闭
//            out.flush();
//            out.close();
            //获取数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine = null;
            //使用循环来读取获得的数据
            while (((inputLine = reader.readLine()) != null)) {
                //我们在每一行后面加上一个"\n"来换行
                resultData += inputLine + "\n";
            }
            reader.close();
            //关闭http连接
            urlConn.disconnect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseJsonMulti13(resultData);
    }

    private BaseRes parseJsonMulti13(String strResult) {
        BaseRes baseRes = new BaseRes();
        String msg = "";
        int token = 0;
        try {
            if (strResult == null|| strResult== "") {
                return null;
            }
            JSONTokener jsonParser = new JSONTokener(strResult);
            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
            msg = jsonObject.optString("message");
            token = jsonObject.optInt("isSuccessed");
        } catch (Exception e) {
            System.out.println("Jsons parse error !!!!");
            e.printStackTrace();
        }
//        baseRes.isSuccessed = 1;
        baseRes.isSuccessed = token;
        baseRes.message = msg;
        return baseRes;
    }


    /**
     * 浇水 控制
     *
     * @param clientId
     * @param type
     * @param switchNo
     * @param unitId
     * @param waterTime
     * @return
     */
//    public BaseRes feedWater(String clientId, String type, String switchNo, String unitId, String waterTime) {
//        if (mPingResult.locale && localServerAPI != null) {
//            try {
//                return localServerAPI.feedWater(clientId, type, switchNo, unitId, waterTime);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//        return null;
//    }

//    public BaseRes feedWater(String clientId, String type, String switchNo, String unitId, String waterTime) {
//        String resultData = "";
//        try {
//            URL url;
//            String urltemp = "";
//
//            if (clientId != null && clientId != "") {

//                urltemp = "ClientId=" + clientId;
//            }
//
//            if (type != null && type != "") {
//                if (urltemp != "") {
//                    urltemp += "&DeviceType=" + type;
//                } else {
//                    urltemp += "DeviceType=" + type;
//                }
//            }
//            if (switchNo != null && switchNo != "") {
//                if (urltemp != "") {
//                    urltemp += "&SwitchNo=" + switchNo;
//                } else {
//                    urltemp += "SwitchNo=" + switchNo;
//                }
//            }
//            if (unitId != null && unitId != "") {
//                if (urltemp != "") {
//                    urltemp += "&UnitId=" + unitId;
//                } else {
//                    urltemp += "UnitId=" + unitId;
//                }
//            }
//            if (waterTime != null && waterTime != "") {
//                if (urltemp != "") {
//                    urltemp += "&Time=" + waterTime;
//                } else {
//                    urltemp += "Time=" + waterTime;
//                }
//            }
//
//            SpfManager SpfManager = new SpfManager(SPlantApplication.mApplication);
//            Boolean isLocalInternet = SpfManager.getBoolean(Constant.SpfManagerParams.isLocalInternet);
//            if (isLocalInternet) {
//                url = new URL(BaseActivity.getClientIPs() + "/API/JsonWatering.aspx?" + urltemp);
//            } else {
//                url = new URL(Config.REMOTE_SERVER + "/API/JsonWatering.aspx?" + urltemp);
//            }
//
//            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//            urlConn.setConnectTimeout(20000);
//
//            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
//            // http正文内，因此需要设为true, 默认情况下是false;
//            urlConn.setDoOutput(true);
//
//            // 设置是否从httpUrlConnection读入，默认情况下是true;
//            urlConn.setDoInput(true);
//
//            // Post 请求不能使用缓存
//            urlConn.setUseCaches(false);
//
//            // 设定传送的内容类型是可序列化的java对象
//            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
////            urlConn.setRequestProperty("Content-type", "application/x-java-serialized-object");
//            urlConn.setRequestProperty("authorization",Constant.TOKEN);
//            // 设定请求的方法为"POST"，默认是GET
//            urlConn.setRequestMethod("GET");
//
//            // 连接，上面对urlConn的所有配置必须要在connect之前完成，
//            urlConn.connect();
//
//            // 此处getOutputStream会隐含的进行connect (即：如同调用上面的connect()方法，
//            // 所以在开发中不调用上述的connect()也可以)。
////            OutputStream outStrm = urlConn.getOutputStream();
//
//            // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
////            ObjectOutputStream out = new ObjectOutputStream(outStrm);
//
//
//            //刷新、关闭
////            out.flush();
////            out.close();
//            //获取数据
//            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
//            String inputLine = null;
//            //使用循环来读取获得的数据
//            while (((inputLine = reader.readLine()) != null)) {
//                //我们在每一行后面加上一个"\n"来换行
//                resultData += inputLine + "\n";
//            }
//            reader.close();
//            //关闭http连接
//            urlConn.disconnect();
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return parseJsonMulti9(resultData);
//    }

    //notice 2016.12.7 浇水 修改为SyncHttpClient网络请求方式
    private String feedWaterTAG = "feedWater";
    private String feedWaterString;

    public BaseRes feedWater(String clientId, String type, String switchNo, String unitId, String waterTime) {
        SyncHttpClient client = new SyncHttpClient();
        client.setTimeout(20000);
        RequestParams params = new RequestParams();
        params.put("clientId", clientId);
        params.put("type", type);
        params.put("switchNo", switchNo);
        params.put("unitId", unitId);
        params.put("waterTime", waterTime);
        feedWaterString = "";
        String url = "";
        try {
            url = getFeedWaterUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization",Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(feedWaterTAG, "onSuccess: ");
                        feedWaterString = response.toString();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(feedWaterTAG, "onFailure: ");

                    }
                }
        );
        Log.i(intervalWateringSettingTAG, "获取浇水信息解析成功");
        if (feedWaterString == null||feedWaterString.equals("") || feedWaterString== "") {
                return null;
            }else {
            return parseJsonMulti9(feedWaterString);
        }
    }
    private BaseRes parseJsonMulti9(String strResult) {
        return new Gson().fromJson(strResult, BaseRes.class);
    }

//    private BaseRes parseJsonMulti9(String strResult) {
//        BaseRes baseRes = new BaseRes();
//        String msg = "";
//        int token = 0;
//        try {
//            if (strResult == null||strResult.equals("") || strResult== "") {
//                return null;
//            }
//            JSONTokener jsonParser = new JSONTokener(strResult);
//            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
//            msg = jsonObject.optString("message");
//            token = jsonObject.optInt("isSuccessed");
//        } catch (Exception e) {
//            System.out.println("Jsons parse error !!!!");
//            e.printStackTrace();
//        }
////        baseRes.isSuccessed = 1;
//        baseRes.isSuccessed = token;
//        baseRes.message = msg;
//        return baseRes;
//    }

    /**
     * 修改密码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     */
//    public BaseRes modifyPassword(@NonNull String userId, @NonNull String oldPassword, @NonNull String newPassword) {
//        try {
//            return getServerAPI().modifyPassword(userId, oldPassword, newPassword);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private String modifyPasswordTAG = "modifyPassword";
    private String modifyPasswordString = "";

    public BaseRes modifyPassword(String userId, String oldPassword, String newPassword) {
//        AsyncHttpClient client = new AsyncHttpClient();
        SyncHttpClient client = new SyncHttpClient();
        client.setTimeout(20000);
        RequestParams params = new RequestParams();
        params.put("userid", userId);
        params.put("old_password", oldPassword);
        params.put("new_password", newPassword);
        modifyPasswordString = "";
        String url = "";
        try {
            url = getModifyPasswordurl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization",Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(modifyPasswordTAG, "onSuccess: ");
                        modifyPasswordString = response.toString();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(modifyPasswordTAG, "onFailure: ");
                    }
                }
        );
        BaseRes baseRes = parseJsonMulti15(modifyPasswordString);
        Log.i(modifyPasswordTAG, "修改密码解析成功");
        return baseRes;
    }

    private BaseRes parseJsonMulti15(String modifyPasswordString) {
        BaseRes baseres = new BaseRes();
        String msg = "";
        int token = 0;
        try {
            if (modifyPasswordString == null) {
                return null;
            }
            JSONTokener jsonParser = new JSONTokener(modifyPasswordString);
            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
            msg = jsonObject.getString("msg");
            token = jsonObject.getInt("isSuccessed");
        } catch (Exception e) {
            System.out.println("Jsons parse error !!!!");
            e.printStackTrace();
        }
        baseres.isSuccessed = token;
        baseres.message = msg;
        return baseres;
    }

    /**
     * app 说明接口，只读云端
     */
//    public ContentRes getAppContent(@NonNull String locale) {
//        try {
//            return remoteServerAPI.getContent(locale);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    //notice 2017.1.23 app 说明接口修改为用SyncHttpClient网络访问方式
    private String appContentTAG = "appContent";
    private String appContentString;

    public ContentRes getAppContent(@NonNull String locale) {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("locale", locale);
        appContentString = "" ;
        String url = "";
        try {
            url = getAppContentUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization", Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(appContentTAG, "onSuccess: ");
                        appContentString = response.toString();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(appContentTAG, "onFailure: ");
                    }
                }
        );

        Log.i(appContentTAG, "获取pp 说明解析成功");
        return parseJsonMulti18(appContentString);
    }



    public ContentRes parseJsonMulti18(String strResult) {
        if (strResult == null || strResult== "") {
            return null;
        }
        return new Gson().fromJson(strResult,ContentRes.class);
    }



    /**
     * 植物操作，只能在本地服务器
     */

// public BaseRes unitOp(@NonNull String action,String deviceType,@NonNull String userId,String id,String name,String title,String plantId,String areaId,int amount,String mapCoord,String descript,String waterId,String switchNo,String clientIP) {
//        try {
//            return getServerAPI().unitOp(action, deviceType, userId, id, name,title, plantId, areaId, amount, mapCoord, descript, waterId, switchNo, clientIP);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//    }
// }

//    public BaseRes unitOp(@NonNull String action, String deviceType, @NonNull String userId, String id, String name, String title, String plantId, String areaId, int amount, String mapCoord, String descript, String waterId, String switchNo, String clientIP, String gatewayId, String PAN_ID) {
//        String resultData = "";
//        try {
//            URL url;
//            String urltemp = "";
//
//            if (action != null && action != "") {
//                urltemp = "action=" + action;
//            }
//
//            if (deviceType != null && deviceType != "") {
//                if (urltemp != "") {
//                    urltemp += "&deviceType=" + deviceType;
//                } else {
//                    urltemp += "deviceType=" + deviceType;
//                }
//            }
//            if (userId != null && userId != "") {
//                if (urltemp != "") {
//                    urltemp += "&user=" + userId;
//                } else {
//                    urltemp += "user=" + userId;
//                }
//            }
//            if (id != null && id != "") {
//                if (urltemp != "") {
//                    urltemp += "&id=" + id;
//                } else {
//                    urltemp += "id=" + id;
//                }
//            }
//            if (name != null && name != "") {
//                if (urltemp != "") {
//                    urltemp += "&name=" + name;
//                } else {
//                    urltemp += "name=" + name;
//                }
//            }
//            if (title != null && title != "") {
//                if (urltemp != "") {
//                    urltemp += "&title=" + title;
//                } else {
//                    urltemp += "title=" + title;
//                }
//            }
//            if (plantId != null && plantId != "") {
//                if (urltemp != "") {
//                    urltemp += "&plantId=" + plantId;
//                } else {
//                    urltemp += "plantId=" + plantId;
//                }
//            }
//            if (areaId != null && areaId != "") {
//                if (urltemp != "") {
//                    urltemp += "&areaid=" + areaId;
//                } else {
//                    urltemp += "areaid=" + areaId;
//                }
//            }
//            if (amount != 0) {
//                if (urltemp != "") {
//                    urltemp += "&amount=" + amount;
//                } else {
//                    urltemp += "amount=" + amount;
//                }
//            }
//            if (mapCoord != null && mapCoord != "") {
//                if (urltemp != "") {
//                    urltemp += "&mapCoord=" + mapCoord;
//                } else {
//                    urltemp += "mapCoord=" + mapCoord;
//                }
//            }
//            if (descript != null && descript != "") {
//                if (urltemp != "") {
//                    urltemp += "&descript=" + descript;
//                } else {
//                    urltemp += "descript=" + descript;
//                }
//            }
//            if (waterId != null && waterId != "") {
//                if (urltemp != "") {
//                    urltemp += "&waterId=" + waterId;
//                } else {
//                    urltemp += "waterId=" + waterId;
//                }
//            }
//            if (switchNo != null && switchNo != "") {
//                if (urltemp != "") {
//                    urltemp += "&switchNo=" + switchNo;
//                } else {
//                    urltemp += "switchNo=" + switchNo;
//                }
//            }
//            if (clientIP != null && clientIP != "") {
//                if (urltemp != "") {
//                    urltemp += "&ip=" + clientIP;
//                } else {
//                    urltemp += "ip=" + clientIP;
//                }
//            }
//            if (gatewayId != null && gatewayId != "") {//网关id   例如：1ac14930-bb92-11e6-a3bb-f52357173307
//                if (urltemp != "") {
//                    urltemp += "&gatewayId=" + gatewayId;
//                } else {
//                    urltemp += "gatewayId=" + gatewayId;
//                }
//            }
//            if (PAN_ID != null && PAN_ID != "") {//网关备注，可以传空
//                if (urltemp != "") {
//                    urltemp += "&PAN_ID=" + PAN_ID;
//                } else {
//                    urltemp += "PAN_ID=" + PAN_ID;
//                }
//            }
////            url = new URL(Config.LOCAL_SERVER);
////            mLocalIP = url.getHost();
////            mLocalPort = url.getPort();
////            PingUtils pingUtils = new PingUtils();
////            String  token = pingUtils.Ping(mLocalIP);
////            if (token=="faild") {
//            SpfManager SpfManager = new SpfManager(SPlantApplication.mApplication);
//            Boolean isLocalInternet = SpfManager.getBoolean(Constant.SpfManagerParams.isLocalInternet);
//            if (isLocalInternet) {
//                url = new URL(BaseActivity.getClientIPs() + "/API/JsonUnitOper.aspx?" + urltemp);
//            } else {
//                url = new URL(Config.REMOTE_SERVER + "/API/JsonUnitOper.aspx?" + urltemp);
//            }
//
//            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//
//            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
//            // http正文内，因此需要设为true, 默认情况下是false;
//            urlConn.setDoOutput(true);
//
//            // 设置是否从httpUrlConnection读入，默认情况下是true;
//            urlConn.setDoInput(true);
//
//            // Post 请求不能使用缓存
//            urlConn.setUseCaches(false);
//
//            // 设定传送的内容类型是可序列化的java对象
//            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
//            urlConn.setRequestProperty("Content-type", "application/x-java-serialized-object");
//            urlConn.addRequestProperty("authorization", Constant.TOKEN);
//
//            // 设定请求的方法为"POST"，默认是GET
//            urlConn.setRequestMethod("GET");
//
//            // 连接，上面对urlConn的所有配置必须要在connect之前完成，
//            urlConn.connect();
//
//            // 此处getOutputStream会隐含的进行connect (即：如同调用上面的connect()方法，
//            // 所以在开发中不调用上述的connect()也可以)。
//            OutputStream outStrm = urlConn.getOutputStream();
//
//            // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
//            ObjectOutputStream out = new ObjectOutputStream(outStrm);
//
////            String content = "clientId=" + URLEncoder.encode("D5139C15-0425-410A-8EE4-36409A1EA420", "gb2312") +
////                    "&locale=" + URLEncoder.encode("zh-CN", "gb2312");
//            //将要上传的内容写入流中
////            out.writeBytes(content);
//
//            //刷新、关闭
//            out.flush();
//            out.close();
//            //获取数据
//            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
//            String inputLine = null;
//            //使用循环来读取获得的数据
//            while (((inputLine = reader.readLine()) != null)) {
//                //我们在每一行后面加上一个"\n"来换行
//                resultData += inputLine + "\n";
//            }
//            reader.close();
//            //关闭http连接
//            urlConn.disconnect();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return parseJsonMulti4(resultData);
//    }

//    private BaseRes parseJsonMulti4(String strResult) {
//        BaseRes baseRes = new BaseRes();
//        String msg = "";
//        int token = 0;
//        try {
//            if (strResult == null|| strResult== "") {
//                return null;
//            }
//            JSONTokener jsonParser = new JSONTokener(strResult);
//            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
//            msg = jsonObject.getString("msg");
//            token = jsonObject.getInt("isSuccessed");
//        } catch (Exception e) {
//            System.out.println("Jsons parse error !!!!");
//            e.printStackTrace();
//        }
//        baseRes.isSuccessed = token;
//        baseRes.message = msg;
//        return baseRes;
//    }
    //notice 2016.12.16 获取植物操作信息 修改为SyncHttpClient网络请求方式
    private String unitOpTAG = "unitOp";
    private String unitOpString;
    public BaseRes unitOp(String action, String deviceType, String userId, String id, String name, String title, String plantId, String areaId, int amount, String mapCoord, String descript, String waterId, String switchNo, String clientIP, String gatewayId, String PAN_ID){
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("action", action);
        params.put("deviceType", deviceType);
        params.put("userId", userId);
        params.put("id", id);
        params.put("name", name);
        params.put("title", title);
        params.put("plantId", plantId);
        params.put("areaId", areaId);
        params.put("amount", amount);
        params.put("mapCoord", mapCoord);
        params.put("descript", descript);
        params.put("waterId", waterId);
        params.put("switchNo", switchNo);
        params.put("clientIP", clientIP);
        params.put("gatewayId", gatewayId);
        params.put("PAN_ID", PAN_ID);
        unitOpString = "" ;
        String url = "";
        try {
            url = getUnitOpUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization",Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(unitOpTAG, "onSuccess: ");
                        unitOpString = response.toString();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(unitOpTAG, "onFailure: ");
                    }
                }
        );
        Log.i(unitOpTAG, "获取植物操作信息解析成功");
        return parseJsonMulti4(unitOpString);
    }

    private BaseRes parseJsonMulti4(String strResult) {
        return new Gson().fromJson(strResult, BaseRes.class);
    }


    //notice 2016.12.13 获取网关信息 修改为SyncHttpClient网络请求方式（按照原来“定时浇水”的方式来写）
//    private String gatewayTAG = "gateway";
//    private String gatewayString;
//    public GatewayRes getGatewayList(String id) {
//        SyncHttpClient client = new SyncHttpClient();
//        RequestParams params = new RequestParams();
//        params.put("id", id);
//        gatewayString = "" ;
//        String url = "";
//        try {
//            url = getGatewayUrl().toString();
//        } catch (MalformedURLException e) {
//            url = Config.REMOTE_SERVER;
//        }
//        client.addHeader("authorization",Constant.TOKEN);
//        client.get(url, params, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        Log.i(gatewayTAG, "onSuccess: ");
//                        gatewayString = response.toString();
//                    }
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        Log.i(gatewayTAG, "onFailure: ");
//                    }
//                }
//        );
//        Log.i(gatewayTAG, "获取网关信息解析成功");
//        return parseJsonMulti16(gatewayString);
//    }
//
//    private GatewayRes parseJsonMulti16(String strResult) {
//        return new Gson().fromJson(strResult, GatewayRes.class);
//    }

    //notice 2016.12.14 获取网关信息 修改为SyncHttpClient网络请求方式（按照原来“选择区域”的方式来写）
    private String gatewayTAG = "gateway";
    private String gatewayString;
    public GatewayListRes getGatewayList() {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
//        params.put("id", id);
        gatewayString = "" ;
        String url = "";
        try {
            url = getGatewayUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization",Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(gatewayTAG, "onSuccess: ");
                        gatewayString = response.toString();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(gatewayTAG, "onFailure: ");
                    }
                }
        );
        Log.i(gatewayTAG, "获取网关信息解析成功");
        return parseJsonMulti16(gatewayString);
    }

    private GatewayListRes parseJsonMulti16(String strResult) {
        return new Gson().fromJson(strResult, GatewayListRes.class);
    }

    //notice 2016.12.14 获取用户手册信息 修改为SyncHttpClient网络请求方式
    private String userManualTAG = "userManual";
    private String userManualString;
    public ExplainRes getUserManual(String locale) {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("locale", locale);
        userManualString = "" ;
        String url = "";
        try {
            url = getUserManualUrl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER;
        }
        client.addHeader("authorization",Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i(userManualTAG, "onSuccess: ");
                        userManualString = response.toString();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i(userManualTAG, "onFailure: ");
                    }
                }
        );
        Log.i(userManualTAG, "获取用户手册信息解析成功");
        return parseJsonMulti17(userManualString);
    }

    private ExplainRes parseJsonMulti17(String strResult) {
        return new Gson().fromJson(strResult, ExplainRes.class);
    }
}