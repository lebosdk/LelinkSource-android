package com.hpplay.sdk.source.test;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.hpplay.sdk.source.test.constants.Constant;
import com.hpplay.sdk.source.test.utils.AssetsUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * jasinCao
 * 19-7-26 下午2:05
 */
public class DemoApplication extends Application {
    private final static String TAG = "DemoApplication";
    private static DemoApplication sApplication;

    private static Handler sHandler;
    private static Executor sExecutor;


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i(TAG, "onCreate");
        sHandler = new Handler(Looper.getMainLooper());
        sExecutor = Executors.newFixedThreadPool(4);
        sApplication = this;
        copyMediaToSDCard();
    }

    private void copyMediaToSDCard() {
        AssetsUtil.getInstance(getApplicationContext())
                .copyAssetsToSD("local_media", Constant.LOCAL_MEDIA_PATH)
                .setFileOperateCallback(new AssetsUtil.FileOperateCallback() {

                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailed(String error) {
                        Logger.e(TAG, error);
                        toast("本地文件拷贝失败");
                    }
                });
    }

    public static DemoApplication getApplication() {
        return sApplication;
    }


    public static void toast(String toast) {
        sHandler.post(() -> Toast.makeText(sApplication, toast, Toast.LENGTH_SHORT).show());
    }

    public static void runOnUI(Runnable task) {
        sHandler.post(task);
    }

    public static void delayOnUI(Runnable task, long delay) {
        sHandler.postDelayed(task, delay);
    }

    public static void runOnAsync(Runnable task) {
        sExecutor.execute(task);
    }
}
