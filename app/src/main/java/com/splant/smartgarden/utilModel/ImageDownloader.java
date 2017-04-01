package com.splant.smartgarden.utilModel;

import android.content.Context;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 图片下载器
 * Created by KeepCoding on 15/9/26.
 */
public class ImageDownloader extends Thread {

    public interface OnImageDownloadListener {
        /**
         * 下载结果
         *
         * @param imageFilePath   下载后文件路径，下载失败时为 null
         * @param imageDownloader
         */
        public void onResult(String imageFilePath, ImageDownloader imageDownloader);
    }

    private String mUrl;//图片地址
    private String mImageFilePath;
    private OnImageDownloadListener mOnImageDownloadListener;
    private Context mContext;

    //本地地图名称
    public static final String LocalMapFileName = "sPlantMap.png";

    /**
     * @param localPath 文件夹路径
     * @param url       图片url
     */
    public ImageDownloader(Context context, String localPath, String url) {
        mContext = context;
        mUrl = url;
        mImageFilePath = localPath + File.separator + LocalMapFileName;
    }

    public void setOnImageDownloadListener(OnImageDownloadListener onImageDownloadListener) {
        mOnImageDownloadListener = onImageDownloadListener;
    }

    @Override
    public void run() {
        super.run();
        //原图存在不需要重新下载
        for (int i = 0; i < 3; i++) {
            try {
                URL url = new URL(mUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(20000);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BitmapUtils.saveToSD(mImageFilePath, inputStream);
                inputStream.close();
                connection.disconnect();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mOnImageDownloadListener != null) {
            mOnImageDownloadListener.onResult(mImageFilePath, this);
        }

    }

    private String getOriFilePath(String filePath) {
        return FilenameUtils.getPath(filePath) + "ori_" + FilenameUtils.getName(filePath);
    }
}
