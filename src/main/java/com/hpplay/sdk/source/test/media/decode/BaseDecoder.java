package com.hpplay.sdk.source.test.media.decode;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.text.TextUtils;

import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.media.extractor.IExtractor;

import java.io.File;
import java.nio.ByteBuffer;

abstract class BaseDecoder implements IDecoder {
    private static final String TAG = "BaseDecoder";

    private volatile boolean mIsRunning = true;

    private final Object mLock = new Object();

    protected MediaCodec mCodec;

    protected IExtractor mExtractor;

    protected ByteBuffer[] mInputBuffers;
    protected ByteBuffer[] mOutputBuffers;

    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    private DecodeState mState = DecodeState.STOP;

    private IDecoderStateListener mStateListener;

    private boolean mIsEOS = false;

    private final String mFilePath;

    private long mEndPos = 0L;

    private MediaFormat mMediaFormat;

    private long mStartTimeForSync = -1L;

    public BaseDecoder(String filePath) {
        this.mFilePath = filePath;
    }

    @Override
    public void run() {
        if (mState == DecodeState.STOP) {
            mState = DecodeState.START;
        }
        if (mStateListener != null) {
            mStateListener.decoderPrepare(this);
        }

        if (!init()) return;

        if (mStateListener != null) {
            mStateListener.decoderInitSuccess(this);
        }

        Logger.i(TAG, "run: 开始解码");
        try {
            while (mIsRunning) {
                if (mState != DecodeState.START &&
                        mState != DecodeState.DECODING) {
                    Logger.i(TAG, "run: 进入等待");

                    waitDecode();

                    mStartTimeForSync = System.currentTimeMillis() - mExtractor.getCurrentTimestamp() / 1000;
                }

                if (!mIsRunning ||
                        mState == DecodeState.STOP) {
                    mIsRunning = false;
                    break;
                }

                if (mStartTimeForSync == -1L) {
                    mStartTimeForSync = System.currentTimeMillis();
                }

                if (!mIsEOS) {
                    mIsEOS = pushBufferToDecoder();
                }

                int index = pullBufferFromDecoder();
                if (index >= 0) {
                    if (mState == DecodeState.DECODING) {
                        sleepForSync();
                    }

                    Frame frame = new Frame(mOutputBuffers[index], mBufferInfo);
                    Logger.i(TAG, "run: size: " + mBufferInfo.size + " offset: " + mBufferInfo.offset);

                    if (mStateListener != null) {
                        mStateListener.decodeOneFrame(this, frame);
                    }
                    mCodec.releaseOutputBuffer(index, true);
                    if (mState == DecodeState.START) {
                        mState = DecodeState.DECODING;
                    }
                }
                if (mBufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                    Logger.i(TAG, "run: 解码结束");
                    mState = DecodeState.FINISH;
                    if (mStateListener != null) {
                        mStateListener.decoderFinish(this);
                    }
                    break;
                }
            }
        } catch (Exception ignore) {

        } finally {
            release();
        }
    }

    private void sleepForSync() {
        long passTime = System.currentTimeMillis() - mStartTimeForSync;
        long curTime = mExtractor.getCurrentTimestamp() / 1000;
        if (curTime > passTime) {
            try {
                Thread.sleep(curTime - passTime);
            } catch (InterruptedException ignore) {
            }
        }
    }

    private boolean init() {
        if (TextUtils.isEmpty(mFilePath) || !new File(mFilePath).exists()) {
            Logger.w(TAG, "init: 文件路径为空或不存在");
            if (mStateListener != null) {
                mStateListener.decoderError(this, "文件路径为空或不存在");
            }
            return false;
        }

        mExtractor = initExtractor(mFilePath);
        mMediaFormat = mExtractor.getFormat();
        if (mExtractor == null || mMediaFormat == null) {
            return false;
        }

        if (!initParams()) {
            return false;
        }

        return initCodec();
    }

    private boolean initParams() {
        try {
            long duration = mMediaFormat.getLong(MediaFormat.KEY_DURATION) / 1000;
            if (mEndPos == 0L) {
                mEndPos = duration;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean initCodec() {
        try {
            String type = mMediaFormat.getString(MediaFormat.KEY_MIME);
            assert type != null;
            Logger.i(TAG, "initCodec: type: " + type);
            mCodec = MediaCodec.createDecoderByType(type);
            configCodec();
            mCodec.start();

            mInputBuffers = mCodec.getInputBuffers();
            mOutputBuffers = mCodec.getOutputBuffers();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected void configCodec() {
        mCodec.configure(mMediaFormat, null, null, 0);
    }

    private boolean pushBufferToDecoder() {
        int inputBufferIndex = mCodec.dequeueInputBuffer(2000);
        boolean isEndOfStream = false;

        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = mInputBuffers[inputBufferIndex];
            int sampleSize = mExtractor.readBuffer(inputBuffer);
            if (sampleSize < 0) {
                mCodec.queueInputBuffer(inputBufferIndex, 0, 0, 0,
                        MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                isEndOfStream = true;
            } else {
                mCodec.queueInputBuffer(inputBufferIndex, 0,
                        sampleSize, mExtractor.getCurrentTimestamp(), mExtractor.getCurrentSampleFlags());
            }
        }
        return isEndOfStream;
    }

    private int pullBufferFromDecoder() {
        int index = mCodec.dequeueOutputBuffer(mBufferInfo, 1000);
        if (index >= 0) {
            return index;
        }
        if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            mOutputBuffers = mCodec.getOutputBuffers();
        }
        return -1;
    }

    protected abstract IExtractor initExtractor(String filePath);

    private void waitDecode() {
        if (mState == DecodeState.PAUSE && mStateListener != null) {
            mStateListener.decoderPause(this);
        }
        try {
            synchronized (mLock) {
                mLock.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void notifyDecode() {
        synchronized (mLock) {
            mLock.notifyAll();
        }

        if (mState == DecodeState.DECODING && mStateListener != null) {
            mStateListener.decoderRunning(this);
        }
    }

    @Override
    public void pause() {
        mState = DecodeState.PAUSE;
    }

    @Override
    public void resume() {
        if (mState == DecodeState.PAUSE) {
            mState = DecodeState.DECODING;
            notifyDecode();
        }
    }

    @Override
    public void stop() {
        mState = DecodeState.STOP;
        mIsRunning = false;
        notifyDecode();
    }

    @Override
    public boolean isPause() {
        return mState == DecodeState.PAUSE;
    }

    @Override
    public boolean isDecoding() {
        return mState == DecodeState.DECODING;
    }

    @Override
    public boolean isStop() {
        return mState == DecodeState.STOP;
    }

    @Override
    public void setStateListener(IDecoderStateListener l) {
        this.mStateListener = l;
    }

    @Override
    public MediaFormat getMediaFormat() {
        return mMediaFormat;
    }

    @Override
    public void release() {
        Logger.i(TAG, "release: ");
        try {
            mState = DecodeState.STOP;
            mIsEOS = false;
            if (mExtractor != null) {
                mExtractor.stop();
            }
            if (mCodec != null) {
                mCodec.stop();
                mCodec.release();
            }
            if (mStateListener != null) {
                mStateListener.decoderDestroy(this);
            }
        } catch (Exception ignore) {
        }
    }
}
