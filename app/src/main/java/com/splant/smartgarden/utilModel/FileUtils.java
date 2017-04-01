package com.splant.smartgarden.utilModel;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件工具
 * Created by Hal on 15/9/27.
 */
public class FileUtils {

    private static final String LOG_TAG = FileUtils.class.getSimpleName();

    public static boolean exists(@NonNull String filePath) {
        return !TextUtils.isEmpty(filePath) && new File(filePath).exists();
    }

    public static void deleteFile(@NonNull String filePath) {
        if (exists(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                file.deleteOnExit();
            }
        }
    }

    public static boolean writeInputStreamToFile(InputStream inputStream, File file) {
        if (file == null || inputStream == null) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            } else if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            int readLength = 0;
            byte[] buffer = new byte[2048];
            while ((readLength = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, readLength);
            }
            fos.close();
            return true;

        } catch (IOException e) {
            Log.e(LOG_TAG, "", e);
        }
        return false;

    }

}
