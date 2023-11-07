package com.hpplay.sdk.source.test.media.decode;

import android.media.MediaCodec;

import java.nio.ByteBuffer;

public class Frame {
    public final ByteBuffer buffer;
    public final MediaCodec.BufferInfo bufferInfo;

    public Frame(ByteBuffer buffer, MediaCodec.BufferInfo bufferInfo) {
        this.buffer = buffer;
        this.bufferInfo = bufferInfo;
    }
}
