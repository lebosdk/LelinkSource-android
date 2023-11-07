package com.hpplay.sdk.source.test.media.extractor;

import android.annotation.SuppressLint;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import com.hpplay.sdk.source.test.Logger;

import java.nio.ByteBuffer;

@SuppressLint("NewApi")
public class RealExtractor {
    private static final String TAG = "RealExtractor";

    private MediaExtractor mExtractor;

    private int mAudioTrack = -1;

    private int mVideoTrack = -1;

    private long mCurSampleTime = 0;

    private int mCurSampleFlags = 0;

    public RealExtractor(String filePath) {
        mExtractor = new MediaExtractor();
        try {
            mExtractor.setDataSource(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取视频格式参数
     */
    public MediaFormat getVideoFormat() {
        for (int i = 0; i < mExtractor.getTrackCount(); i++) {
            MediaFormat mediaFormat = mExtractor.getTrackFormat(i);
            String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
            if (mime != null && mime.startsWith("video/")) {
                Logger.i(TAG, "getVideoFormat: mime: " + mime);
                mVideoTrack = i;
                break;
            }
        }
        if (mVideoTrack >= 0) {
            return mExtractor.getTrackFormat(mVideoTrack);
        }
        return null;
    }

    /**
     * 获取音频格式参数
     */
    public MediaFormat getAudioFormat() {
        for (int i = 0; i < mExtractor.getTrackCount(); i++) {
            MediaFormat mediaFormat = mExtractor.getTrackFormat(i);
            String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
            if (mime != null && mime.startsWith("audio/")) {
                Logger.i(TAG, "getAudioFormat: mime: " + mime);
                mAudioTrack = i;
                break;
            }
        }
        if (mAudioTrack >= 0) {
            return mExtractor.getTrackFormat(mAudioTrack);
        }
        return null;
    }

    /**
     * 读取视频数据
     */
    public int readBuffer(ByteBuffer byteBuffer) {
        byteBuffer.clear();
        selectSourceTrack();
        int readSampleCount = mExtractor.readSampleData(byteBuffer, 0);
        Logger.d(TAG, "readBuffer: readSampleCount: " + readSampleCount);
        if (readSampleCount < 0) {
            return -1;
        }
        mCurSampleTime = mExtractor.getSampleTime();
        mCurSampleFlags = mExtractor.getSampleFlags();
        mExtractor.advance();
        return readSampleCount;
    }

    /**
     * 选择通道
     */
    private void selectSourceTrack() {
        if (mVideoTrack >= 0) {
            mExtractor.selectTrack(mVideoTrack);
        } else if (mAudioTrack >= 0) {
            mExtractor.selectTrack(mAudioTrack);
        }
    }

    /**
     * seek到指定位置，并返回实际帧的时间戳
     */
    public long seek(long pos) {
        mExtractor.seekTo(pos, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
        return mExtractor.getSampleTime();
    }

    public void stop() {
        mExtractor.release();
        mExtractor = null;
    }

    public int getVideoTrack() {
        return mVideoTrack;
    }

    public int getAudioTrack() {
        return mAudioTrack;
    }

    public long getCurrentTimestamp() {
        return mCurSampleTime;
    }

    public int getCurrentSampleFlags() {
        return mCurSampleFlags;
    }

}
