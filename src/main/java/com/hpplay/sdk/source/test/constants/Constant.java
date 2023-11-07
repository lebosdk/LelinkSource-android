package com.hpplay.sdk.source.test.constants;

import android.os.Environment;

public class Constant {

    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";

    public static final String LOCAL_MEDIA_PATH = "/hpplay_demo/local_media/";
    public static final String SDCARD_LOCAL_MEDIA_PATH = Environment.getExternalStorageDirectory()
            + LOCAL_MEDIA_PATH;
}
