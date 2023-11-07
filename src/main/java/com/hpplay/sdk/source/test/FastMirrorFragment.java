package com.hpplay.sdk.source.test;

import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.easycast.IEasyCastListener;
import com.hpplay.sdk.source.easycast.bean.EasyCastBean;
import com.hpplay.sdk.source.test.config.PushConfig;
import com.hpplay.sdk.source.test.fragment.BaseFragment;

/**
 * author : DON
 * date   : 7/26/214:37 PM
 * desc   :
 */
public class FastMirrorFragment extends BaseFragment {
    private final static String TAG = "FastMirrorActivity";
    private MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;
    private int mVideoWidth, mVideoHeight;
    private ViewGroup mBrowseContainer;

    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
        }
    };
    private MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            mVideoWidth = width;
            mVideoHeight = height;
            setDisplay();
        }
    };

    private IEasyCastListener mCastListener = new IEasyCastListener() {
        @Override
        public EasyCastBean onCast(LelinkServiceInfo lelinkServiceInfo) {
            EasyCastBean castBean = new EasyCastBean();
            castBean.mirrorAudioEnable = false;
            return castBean;
        }

        @Override
        public void onCastError(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean, int what, int extra) {
            Logger.w(TAG, "onCastError " + what + " / " + extra);
        }

        @Override
        public void onCastLoading(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean) {
            Logger.w(TAG, "onCastLoading");
        }

        @Override
        public void onCastPause(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean) {
        }

        @Override
        public void onCastStart(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean) {
            Logger.w(TAG, "onCastStart");
        }

        @Override
        public void onCastPositionUpdate(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean, long duration, long position) {
        }

        @Override
        public void onCastCompletion(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean) {
        }

        @Override
        public void onCastStop(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean) {
            Logger.w(TAG, "onCastStop");
        }

        @Override
        public void onDismiss() {
            Logger.w(TAG, "onDismiss");
        }
    };


    @Override
    public int getLayoutID() {
        return R.layout.f_fast_mirror;
    }

    @Override
    public void init(View view) {

        mBrowseContainer = view.findViewById(R.id.browseContainer);
        mSurfaceView = view.findViewById(R.id.mirror_surface);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                openVideo(holder.getSurface());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
        view.findViewById(R.id.castBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseAndCast();
            }
        });
        view.findViewById(R.id.stopCastBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LelinkSourceSDK.getInstance().stopPlay();
            }
        });
    }

    private void browseAndCast() {
        int isSuccess = LelinkSourceSDK.getInstance().setEasyCastListener(mCastListener);
        if (isSuccess != 0) {
            DemoApplication.toast("此sdk包不支持该接入模式");
            return;
        }
        LelinkSourceSDK.getInstance()
                //FIXME WARN: 这里替换为您申请的AppID & AppSecret，build.gradle替换为您的应用包名
                .setSdkInitInfo(getActivity(), getString(R.string.app_id), getString(R.string.app_secret))
                .easyMirror(mBrowseContainer);
    }

    private void setDisplay() {
        if (mSurfaceView == null) {
            return;
        }
        if (mVideoWidth == 0 || mVideoHeight == 0) {
            return;
        }
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(point);
        int screenWidth = point.x;
        int videoHeight = (int) (screenWidth * (mVideoHeight / (double) mVideoWidth));
        ViewGroup.LayoutParams params = mSurfaceView.getLayoutParams();
        params.width = screenWidth;
        params.height = videoHeight;
        mSurfaceView.setLayoutParams(params);
    }

    private void openVideo(Surface surface) {
        Logger.w(TAG, "openVideo");
        if (mMediaPlayer != null) {
            stopVideo();
        }
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setDataSource(PushConfig.getInstance().getNetVideo());
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            Logger.w(TAG, e);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setDisplay();
    }

    private void stopVideo() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
            mMediaPlayer = null;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (LelinkSourceSDK.getInstance().isBrowseShowing()) {
                LelinkSourceSDK.getInstance().dismissBrowserUI();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onStop() {
        super.onStop();
        stopVideo();
    }
}
