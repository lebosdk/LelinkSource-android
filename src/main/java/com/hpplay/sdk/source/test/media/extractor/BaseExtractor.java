package com.hpplay.sdk.source.test.media.extractor;

import java.nio.ByteBuffer;

abstract class BaseExtractor implements IExtractor {
    protected final RealExtractor mRealExtractor;

    public BaseExtractor(String filePath) {
        mRealExtractor = new RealExtractor(filePath);
    }

    @Override
    public int readBuffer(ByteBuffer byteBuffer) {
        return mRealExtractor.readBuffer(byteBuffer);
    }

    @Override
    public long getCurrentTimestamp() {
        return mRealExtractor.getCurrentTimestamp();
    }

    @Override
    public int getCurrentSampleFlags() {
        return mRealExtractor.getCurrentSampleFlags();
    }

    @Override
    public long seek(long pos) {
        return mRealExtractor.seek(pos);
    }

    @Override
    public void stop() {
        mRealExtractor.stop();
    }
}
