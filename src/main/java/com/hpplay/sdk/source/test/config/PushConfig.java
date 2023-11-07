package com.hpplay.sdk.source.test.config;

import com.hpplay.sdk.source.test.constants.Constant;

import java.util.List;

public class PushConfig {
    private final static String TAG = "PushConfig";
    private static PushConfig sInstance;

    private static String DEFAULT_URL_VIDEO = "http://cdn.hpplay.com.cn/demo/lbtp.mp4";
    private static String DEFAULT_URL_VIDEO1 = "http://cdn.hpplay.com.cn/videos/lb/NFC.mp4";
    private static String DEFAULT_URL_VIDEO2 = "http://video.hpplay.cn/demo/aom.mp4";
    private static String DEFAULT_URL_MUSIC = "http://cdn.hpplay.com.cn/demo/bedroom.mp3";
    private static String DEFAULT_URL_PHOTO = "http://cdn.hpplay.com.cn/demo/1080P.png";
    private static String DEFAULT_LOCAL_VIDEO = Constant.SDCARD_LOCAL_MEDIA_PATH + "test_video.ts";
    private static String DEFAULT_LOCAL_MUSIC = Constant.SDCARD_LOCAL_MEDIA_PATH + "EDC - I Never Told You.mp3";
    private static String DEFAULT_LOCAL_PHOTO = Constant.SDCARD_LOCAL_MEDIA_PATH + "I01027343.png";
    private static String DEFAULT_EXTERNAL_VIDEO = DEFAULT_LOCAL_VIDEO;
    private static String DEFAULT_URL_DRAMA_1080_01 = "http://cdn.hpplay.com.cn/demo/demo_01_1080.mp4";
    private static String DEFAULT_URL_DRAMA_1080_02 = "http://cdn.hpplay.com.cn/demo/demo_02_1080.mp4";
    private static String DEFAULT_URL_DRAMA_1080_03 = "http://cdn.hpplay.com.cn/demo/demo_03_1080.mp4";
    private static String DEFAULT_URL_DRAMA_1080_04 = "http://cdn.hpplay.com.cn/demo/demo_04_1080.mp4";
    private static String DEFAULT_URL_DRAMA_1080_05 = "http://cdn.hpplay.com.cn/demo/demo_05_1080.mp4";
    private static String DEFAULT_URL_DRAMA_1080_06 = "http://cdn.hpplay.com.cn/demo/demo_06_1080.mp4";
    private static String DEFAULT_URL_DRAMA_720_01 = "http://cdn.hpplay.com.cn/demo/demo_01_720.mp4";
    private static String DEFAULT_URL_DRAMA_720_02 = "http://cdn.hpplay.com.cn/demo/demo_02_720.mp4";
    private static String DEFAULT_URL_DRAMA_720_03 = "http://cdn.hpplay.com.cn/demo/demo_03_720.mp4";
    private static String DEFAULT_URL_DRAMA_720_04 = "http://cdn.hpplay.com.cn/demo/demo_04_720.mp4";
    private static String DEFAULT_URL_DRAMA_720_05 = "http://cdn.hpplay.com.cn/demo/demo_05_720.mp4";
    private static String DEFAULT_URL_DRAMA_720_06 = "http://cdn.hpplay.com.cn/demo/demo_06_720.mp4";

    private String mNetVideo = DEFAULT_URL_VIDEO2;
    private String mNetMusic = DEFAULT_URL_MUSIC;
    private String mNetPhoto = DEFAULT_URL_PHOTO;
    private String mLocalVideo = DEFAULT_LOCAL_VIDEO;
    private String mLocalMusic = DEFAULT_LOCAL_MUSIC;
    private String mLocalPhoto = DEFAULT_LOCAL_PHOTO;
    private String mExternalVideo = DEFAULT_EXTERNAL_VIDEO;

    public static synchronized PushConfig getInstance() {
        synchronized (PushConfig.class) {
            if (sInstance == null) {
                sInstance = new PushConfig();
            }
            return sInstance;
        }
    }

    private PushConfig() {

    }

    public void setNetVideo(String url) {
        mNetVideo = url;
    }

    public String getNetVideo() {
        return mNetVideo;
    }

    public void setNetMusic(String url) {
        mNetMusic = url;
    }

    public String getNetMusic() {
        return mNetMusic;
    }

    public void setNetPhoto(String url) {
        mNetPhoto = url;
    }

    public String getNetPhoto() {
        return mNetPhoto;
    }

    public void setLocalVideo(String uri) {
        mLocalVideo = uri;
    }

    public String getLocalVideo() {
        return mLocalVideo;
    }

    public void setLocalMusic(String uri) {
        mLocalMusic = uri;
    }

    public String getLocalMusic() {
        return mLocalMusic;
    }

    public void setLocalPhoto(String uri) {
        mLocalPhoto = uri;
    }

    public String getLocalPhoto() {
        return mLocalPhoto;
    }

    public void setExternalVideo(String url) {
        this.mExternalVideo = url;
    }

    public String getExternalVideo() {
        return mExternalVideo;
    }

    public int getDuration(String url) {
        if (DEFAULT_URL_VIDEO.equals(url)) {
            return 61;
        }
        if (DEFAULT_URL_VIDEO1.equals(url)) {
            return 33;
        }
        if (DEFAULT_URL_MUSIC.equals(url)) {
            return 187;
        }
        if (DEFAULT_LOCAL_VIDEO.equals(url)) {
            return 16;
        }
        if (DEFAULT_LOCAL_MUSIC.equals(url)) {
            return 252;
        }
        return 0;
    }

    public String[] getDrama_1080() {
        return new String[]{DEFAULT_URL_DRAMA_1080_01
                , DEFAULT_URL_DRAMA_1080_02
                , DEFAULT_URL_DRAMA_1080_03
                , DEFAULT_URL_DRAMA_1080_04
                , DEFAULT_URL_DRAMA_1080_05
                , DEFAULT_URL_DRAMA_1080_06};
    }

    public String[] getDrama_720() {
        return new String[]{DEFAULT_URL_DRAMA_720_01
                , DEFAULT_URL_DRAMA_720_02
                , DEFAULT_URL_DRAMA_720_03
                , DEFAULT_URL_DRAMA_720_04
                , DEFAULT_URL_DRAMA_720_05
                , DEFAULT_URL_DRAMA_720_06};
    }

}
