package com.hpplay.sdk.source.test.media.extractor;

import android.media.MediaFormat;

import java.nio.ByteBuffer;

/**
 * 音视频数据抽象提取接口
 *
 * <p>下面是示例用法
 *
 * <pre>
 *      IExtractor extractor = ...;
 *      MediaFormat format = extractor.getFormat();
 *      int readCount;
 *      ByteBuffer inputBuffer = ByteBuffer.allocate(...)
 *      while(extractor.readBuffer(inputBuffer) &gt;= -1) {
 *          ...
 *      }
 *
 *      extractor.stop()
 * <pre/>
 */
public interface IExtractor {

    /**
     * 获取媒体参数
     */
    MediaFormat getFormat();

    /**
     * 读取音视频数据
     */
    int readBuffer(ByteBuffer byteBuffer);

    /**
     * 读取当前帧实际
     */
    long getCurrentTimestamp();

    /**
     * 获取当前采样Flag
     */
    int getCurrentSampleFlags();

    /**
     * seek到指定位置，并返回实际帧的时间戳
     */
    long seek(long pos);

    /**
     * 停止读取数据
     */
    void stop();

}
