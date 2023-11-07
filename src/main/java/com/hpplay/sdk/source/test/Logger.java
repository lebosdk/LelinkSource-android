package com.hpplay.sdk.source.test;

import android.util.Log;

public class Logger {

    private static final String TAG = "hpplay-demo";

    public static void v(String tag, String msg) {
        String message = formatMessage(tag, msg);
        Log.v(TAG, message);
    }

    public static void d(String tag, String msg) {
        String message = formatMessage(tag, msg);
        Log.d(TAG, message);
    }

    public static void i(String tag, String msg) {
        String message = formatMessage(tag, msg);
        Log.i(TAG, message);
    }

    public static void w(String tag, String msg) {
        String message = formatMessage(tag, msg);
        Log.w(TAG, message);
    }

    public static void w(String tag, String msg, Throwable tr) {
        String message = formatMessage(tag, msg);
        Log.w(TAG, message, tr);
    }

    public static void w(String tag, Throwable tr) {
        String message = formatMessage(tag, null);
        Log.w(TAG, message, tr);
    }

    public static void e(String tag, String msg) {
        String message = formatMessage(tag, msg);
        Log.e(TAG, message);
    }

    public static void e(String tag, String msg, Throwable tr) {
        String message = formatMessage(tag, msg);
        Log.e(TAG, message, tr);
    }

    private static String formatMessage(String tag, String msg) {
        if (tag == null) {
            tag = "";
        }
        if (msg == null) {
            msg = "";
        }
        String ret = tag + ":" + msg;
        ret = "[" + Thread.currentThread().getName() + "]:" + ret;
        return ret;
    }
}
