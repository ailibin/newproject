package com.splant.smartgarden.utilModel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Bitmap工具
 * Created by Hal on 2015/8/14.
 */
public class BitmapUtils {

    private static final String LOG = BitmapUtils.class.getSimpleName();

    /**
     * 保存bitmap到文件，格式 JPG
     *
     * @param dstPath 目标文件路径
     * @param bitmap  位图
     * @return true 保存成功
     */
    public static boolean saveToSD(String dstPath, Bitmap bitmap) {
        if (TextUtils.isEmpty(dstPath) || bitmap == null) {
            return false;
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        FileOutputStream fos = null;
        File file = new File(dstPath);
        try {
            if (!file.isFile()) {
                Log.w(LOG, "dstPath is not a file Path");
                return false;
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            } else if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            fos.write(bytes.toByteArray());
            fos.close();
            return true;

        } catch (IOException e) {
            Log.e(LOG, "", e);
        }
        return false;
    }

    /**
     * 保存图片
     *
     * @param dstPath     图片文件路径
     * @param inputStream 输入流
     * @return true 成功，false 失败
     */
    public static boolean saveToSD(String dstPath, InputStream inputStream) {
        if (TextUtils.isEmpty(dstPath) || inputStream == null) {
            return false;
        }
        File file = new File(dstPath);
        return FileUtils.writeInputStreamToFile(inputStream, file);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 圆角
     *
     * @param source
     * @param maxValue
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap source, long maxValue) {
        // TODO: 15/10/24 实现图片圆角
        return null;
    }

    /**
     * 通过降低图片的质量来压缩图片
     *
     * @param bitmap  要压缩的图片位图对象
     * @param maxSize 压缩后图片大小的最大值,单位KB
     * @return 压缩后的图片位图对象
     */
    public static Bitmap compressByQuality(Bitmap bitmap, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        System.out.println("图片压缩前大小：" + baos.toByteArray().length + "byte");
        boolean isCompressed = false;
        while (baos.toByteArray().length / 1024 > maxSize) {
            quality -= 10;
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            System.out.println("质量压缩到原来的" + quality + "%时大小为："
                    + baos.toByteArray().length + "byte");
            isCompressed = true;
        }
        System.out.println("图片压缩后大小：" + baos.toByteArray().length + "byte");
        if (isCompressed) {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(
                    baos.toByteArray(), 0, baos.toByteArray().length);
            recycleBitmap(bitmap);
            return compressedBitmap;
        } else {
            return bitmap;
        }
    }

    /**
     * 压缩
     *
     * @param oriFilePath  原图片路径
     * @param maxSize      图片大小
     * @param destFilePath 压缩后图片路径
     * @return true 压缩成功
     */
    public static boolean compressByQuality(String oriFilePath, int maxSize, String destFilePath) {
        if (!FileUtils.exists(oriFilePath)) {
            return false;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(oriFilePath);
        if (bitmap == null) {
            return false;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean isCompressed = false;
        int quality = 60;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        isCompressed = true;
//        System.out.println("图片压缩前大小：" + baos.toByteArray().length + "byte");
//        while (baos.toByteArray().length / 1024 > maxSize) {
//            quality -= 10;
//            baos.reset();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//            System.out.println("质量压缩到原来的" + quality + "%时大小为："
//                    + baos.toByteArray().length + "byte");
//            isCompressed = true;
//        }
        recycleBitmap(bitmap);
        System.out.println("图片压缩后大小：" + baos.toByteArray().length + "byte");
        if (isCompressed) {
            File destFile = new File(destFilePath);
            try {
                org.apache.commons.io.FileUtils.writeByteArrayToFile(destFile, baos.toByteArray());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 回收位图对象
     *
     * @param bitmap
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }


}
