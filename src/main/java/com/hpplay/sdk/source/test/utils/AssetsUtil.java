package com.hpplay.sdk.source.test.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.test.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by Zippo on 2018/6/9.
 * Date: 2018/6/9
 * Time: 16:35:15
 */
public class AssetsUtil {

    private static final String TAG = "AssetsUtil";
    private static AssetsUtil instance;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private Context context;
    private FileOperateCallback callback;
    private volatile boolean isSuccess;
    private String errorStr;

    public static AssetsUtil getInstance(Context context) {
        if (instance == null)
            instance = new AssetsUtil(context);
        return instance;
    }

    private AssetsUtil(Context context) {
        this.context = context;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (callback != null) {
                if (msg.what == SUCCESS) {
                    callback.onSuccess();
                }
                if (msg.what == FAILED) {
                    callback.onFailed(msg.obj.toString());
                }
            }
        }
    };

    public AssetsUtil copyAssetsToSD(final String srcPath, final String sdPath) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                copyAssetsToDst(context, srcPath, sdPath);
                if (isSuccess) {
                    handler.obtainMessage(SUCCESS).sendToTarget();
                } else {
                    handler.obtainMessage(FAILED, errorStr).sendToTarget();
                }
            }

        }).start();
        return this;
    }

    public void setFileOperateCallback(FileOperateCallback callback) {
        this.callback = callback;
    }

    private void copyAssetsToDst(Context context, String srcPath, String dstPath) {
        FileOutputStream fos = null;
        try {
            String fileNames[] = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
                File file = new File(Environment.getExternalStorageDirectory(), dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!srcPath.equals("")) { // assets 文件夹下的目录
                        copyAssetsToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsToDst(context, fileName, dstPath + File.separator + fileName);
                    }
                }
            } else {
                File outFile = new File(Environment.getExternalStorageDirectory(), dstPath);
                InputStream is = context.getAssets().open(srcPath);
                fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
            }
            isSuccess = true;
        } catch (Exception e) {
            Logger.w(TAG, e);
            errorStr = e.getMessage();
            isSuccess = false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    Logger.w(TAG, e);
                }
            }
        }
    }


    public static boolean isContains(LelinkServiceInfo selectInfo, LelinkServiceInfo info) {

        try {
            if (selectInfo == null || info == null) {
                return false;
            }
            if (info.getUid() != null && selectInfo.getUid() != null && TextUtils.equals(info.getUid(), selectInfo.getUid())) {
                return true;
            } else if (TextUtils.equals(info.getIp(), selectInfo.getIp()) && TextUtils.equals(info.getName(), selectInfo.getName())) {
                return true;
            }
        } catch (Exception e) {
            Logger.w(TAG, e);
            return false;
        }

        return false;
    }

    public static String getRandomColor() {
        String red;
        String green;
        String blue;
        Random random = new Random();
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();
        red = red.length() == 1 ? "0" + red : red;
        green = green.length() == 1 ? "0" + green : green;
        blue = blue.length() == 1 ? "0" + blue : blue;
        String color = "#" + red + green + blue;
        return color;
    }


    public interface FileOperateCallback {
        void onSuccess();

        void onFailed(String error);
    }

}
