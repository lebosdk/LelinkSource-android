package com.hpplay.sdk.source.test.media.decode;

public interface IDecoderStateListener {
    void decoderPrepare(IDecoder decoder);

    void decoderInitSuccess(IDecoder decoder);

    void decoderFinish(IDecoder decoder);

    void decoderPause(IDecoder decoder);

    void decoderRunning(IDecoder decoder);

    void decoderError(IDecoder decoder, String errorMsg);

    void decoderDestroy(IDecoder decoder);

    void decodeOneFrame(IDecoder decoder, Frame frame);
}
