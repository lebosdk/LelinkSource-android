package com.hpplay.sdk.source.test.media.extractor;

import android.media.MediaFormat;

/**
 * 音频数据提取器
 */
public class AudioExtractor extends BaseExtractor {
    public AudioExtractor(String filePath) {
        super(filePath);
    }

    @Override
    public MediaFormat getFormat() {
        return mRealExtractor.getAudioFormat();
    }
}
