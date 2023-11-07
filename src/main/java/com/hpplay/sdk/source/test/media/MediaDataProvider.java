package com.hpplay.sdk.source.test.media;

import android.media.AudioFormat;
import android.media.MediaFormat;
import android.view.Surface;

import com.hpplay.sdk.source.test.DemoApplication;
import com.hpplay.sdk.source.test.media.decode.AudioDecoder;
import com.hpplay.sdk.source.test.media.decode.Frame;
import com.hpplay.sdk.source.test.media.decode.IDecoder;
import com.hpplay.sdk.source.test.media.decode.SampleDecoderStateListener;
import com.hpplay.sdk.source.test.media.decode.VideoDecoder;

import java.nio.ByteBuffer;

public class MediaDataProvider {
    private static final String TAG = "MediaDataProvider";

    public interface Callback {
        void onAudioData(int sampleRate, int channel, int audioFormat, byte[] data);
    }

    private Callback mCallback;
    private VideoDecoder mVideoDecoder;
    private AudioDecoder mAudioDecoder;

    private boolean isStart = false;

    private static MediaDataProvider sInstance;

    private MediaDataProvider() {

    }

    public synchronized static MediaDataProvider getInstance() {
        if (sInstance == null) {
            synchronized (MediaDataProvider.class) {
                if (sInstance == null) {
                    sInstance = new MediaDataProvider();
                }
            }
        }
        return sInstance;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void start(Surface surface, String filePath) {
        if (isStart) {
            stop();
            return;
        }
        isStart = true;

        if (mVideoDecoder != null) {
            mVideoDecoder.release();
        }
        mVideoDecoder = new VideoDecoder(surface, filePath);
        DemoApplication.runOnAsync(mVideoDecoder);

        if (mAudioDecoder != null) {
            mAudioDecoder.release();
        }
        mAudioDecoder = new AudioDecoder(filePath);
        mAudioDecoder.setStateListener(new SampleDecoderStateListener() {

            @Override
            public void decodeOneFrame(IDecoder decoder, Frame frame) {
                if (mCallback != null) {
                    ByteBuffer byteBuffer = frame.buffer;
                    byte[] data = new byte[byteBuffer.remaining()];
                    byteBuffer.get(data);
                    MediaFormat mediaFormat = decoder.getMediaFormat();
                    int sampleRate = mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                    int channelMode = mediaFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT) == 1 ?
                            AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO;
                    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        try {
                            audioFormat = mediaFormat.getInteger(MediaFormat.KEY_PCM_ENCODING);
                        } catch (Exception ignore) {
                        }
                    }
                    mCallback.onAudioData(sampleRate, channelMode, audioFormat, data);
                }
            }
        });
        DemoApplication.runOnAsync(mAudioDecoder);
    }

    public void stop() {
        isStart = false;
        if (mVideoDecoder != null) {
            mVideoDecoder.stop();
            mVideoDecoder.release();
            mVideoDecoder = null;
        }
        if (mAudioDecoder != null) {
            mAudioDecoder.stop();
            mAudioDecoder.release();
            mAudioDecoder = null;
        }
    }

}
