package com.hpplay.sdk.source.test.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.hpplay.sdk.source.test.Logger;

import java.lang.reflect.Field;

/**
 * Created by Zippo on 2018/3/19.
 * Date: 2018/3/19
 * Time: 9:56:11
 */

public class ScreenUtil {
    private static final String TAG = "ScreenUtil";

    private ScreenUtil() {

    }

    /**
     * return screen width
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        if (null == context) {
            //bugly:https://bugly.qq.com/v2/crash-reporting/crashes/820cbca48f/200430?pid=1#
            Logger.i(TAG, "getScreenWidth fail  context is:" + context);
            return -1;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /***
     * return screen height
     *
     * @param context 上下文
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        if (null == context) {
            //bugly:https://bugly.qq.com/v2/crash-reporting/crashes/820cbca48f/200430?pid=1#
            Logger.i(TAG, "getScreenWidth fail  context is:" + context);
            return -1;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕密度dpi（120 / 160 / 240）
     *
     * @param pContext pContext
     * @return 屏幕密度dpi
     */
    public static int getDensityDpi(Context pContext) {
        if (null == pContext) {
            Logger.i(TAG, "getScreenWidth fail  context is:" + pContext);
            return -1;
        }
        DisplayMetrics displayMetrics = pContext.getResources().getDisplayMetrics();
        return displayMetrics.densityDpi;
    }

    /**
     * 获取屏幕密度（0.75 / 1.0 / 1.5）
     *
     * @param pContext 上下文
     * @return 屏幕密度
     */
    public static float getDensity(Context pContext) {
        if (null == pContext) {
            Logger.i(TAG, "getScreenWidth fail  context is:" + pContext);
            return -1;
        }
        DisplayMetrics displayMetrics = pContext.getResources().getDisplayMetrics();
        return displayMetrics.density;
    }

    /**
     * 获取屏幕英寸
     *
     * @param pContext 上下文
     * @return double类型的屏幕尺寸
     */
    private static double getScreenInches(Context pContext) {
        if (null == pContext) {
            Logger.i(TAG, "getScreenWidth fail  context is:" + pContext);
            return -1;
        }
        int width = getScreenWidth(pContext);
        int height = getScreenHeight(pContext);
        double x = Math.pow(width, 2);
        double y = Math.pow(height, 2);
        double diagonal = Math.sqrt(x + y);
        int dens = getDensityDpi(pContext);
        return diagonal / (double) dens;
    }


    /**
     * return status bar height
     *
     * @param pActivity pActivity
     * @return status bar height
     */
    public static int getStatusBarHeight(Activity pActivity) {
        Rect frame = new Rect();
        pActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        if (statusBarHeight <= 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = pActivity.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return statusBarHeight;
    }


    /**
     * 通过反射获取手机的真实分辨率
     *
     * @param context
     * @return
     */
    public static int[] getRelScreenSize(Context context) {
        int[] size = new int[2];
        WindowManager w = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        //  包含statusbar bar/menu bar
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth")
                        .invoke(d);
                heightPixels = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(d);
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
        }
        // 包含statusbar bar/menu bar
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d,
                        realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
        }
        size[0] = widthPixels;
        size[1] = heightPixels;
        Logger.d("ScreenUtils",
                " widthPixels  " + widthPixels + " heightPixels "
                        + heightPixels);
        return size;
    }

    public static float dp2px(Context context, float dp){
        return dp * context.getResources().getDisplayMetrics().density + 0.5f;
    }

}
