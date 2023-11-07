package com.hpplay.sdk.source.test.media.decode;

import android.view.Surface;

import com.hpplay.sdk.source.test.media.extractor.IExtractor;
import com.hpplay.sdk.source.test.media.extractor.VideoExtractor;

public class VideoDecoder extends BaseDecoder {
    private Surface mSurface;

    public VideoDecoder(Surface surface, String filePath) {
        super(filePath);

        this.mSurface = surface;
    }

    @Override
    protected void configCodec() {
        mCodec.configure(getMediaFormat(), mSurface, null, 0);
    }

    @Override
    protected IExtractor initExtractor(String filePath) {
        return new VideoExtractor(filePath);
    }
}
