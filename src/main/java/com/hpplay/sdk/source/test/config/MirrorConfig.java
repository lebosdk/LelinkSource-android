package com.hpplay.sdk.source.test.config;

public class MirrorConfig {
    private final static String TAG = "MirrorConfig";

    private static MirrorConfig sInstance = null;

    public static synchronized MirrorConfig getInstance() {
        synchronized (MirrorConfig.class) {
            sInstance = new MirrorConfig();
        }
        return sInstance;
    }

    private MirrorConfig() {

    }
}
