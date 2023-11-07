package com.hpplay.sdk.source.test.media.decode;

import android.media.MediaFormat;

public interface IDecoder extends Runnable {

    void pause();

    void resume();

    void stop();

    boolean isDecoding();

    boolean isPause();

    boolean isStop();

    void setStateListener(IDecoderStateListener l);

    MediaFormat getMediaFormat();

    void release();
}
