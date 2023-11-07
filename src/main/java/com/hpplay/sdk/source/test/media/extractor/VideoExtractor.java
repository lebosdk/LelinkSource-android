package com.hpplay.sdk.source.test.media.extractor;

import android.media.MediaFormat;

/**
 * 视频数据提取器
 */
public class VideoExtractor extends BaseExtractor {
    public VideoExtractor(String filePath) {
        super(filePath);
    }

    @Override
    public MediaFormat getFormat() {
        return mRealExtractor.getVideoFormat();
    }
}
