package com.splant.smartgarden.weatherModel.Utils;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * 缓存控制
 */
public class DiskCacheController {

    private SimpleDiskCache mSimpleDiskCache;

    private static class SingletonHolder {
        public static DiskCacheController sController = new DiskCacheController();
    }

    public static DiskCacheController getInstance() {
        return SingletonHolder.sController;
    }

    private DiskCacheController() {
    }

    public void init(Context context) {
        try {
            mSimpleDiskCache = SimpleDiskCache.open(context.getCacheDir(), AppUtils.getVersionCode(context), Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, String value) {
        if (mSimpleDiskCache != null) {
            try {
                mSimpleDiskCache.put(key, value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public <T extends Serializable> void put(String key, T object) {
        if (mSimpleDiskCache != null && object != null) {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(mSimpleDiskCache.openStream(key));
                outputStream.writeObject(object);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public <T extends Serializable> T get(String key) {
        ObjectInputStream inputStream = null;
        try {
            SimpleDiskCache.InputStreamEntry entry = mSimpleDiskCache.getInputStream(key);
            if (entry == null) {
                return null;
            }
            inputStream = new ObjectInputStream(entry.getInputStream());
            return (T) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
