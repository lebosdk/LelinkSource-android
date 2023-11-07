package com.hpplay.sdk.source.test;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.easycast.IEasyCastListener;
import com.hpplay.sdk.source.easycast.bean.EasyCastBean;
import com.hpplay.sdk.source.test.config.PushConfig;
import com.hpplay.sdk.source.test.fragment.BaseFragment;
import com.hpplay.sdk.source.test.view.mediacontroller.CastController;
import com.hpplay.sdk.source.test.view.mediacontroller.OnUserStopCastListener;

/**
 * author : DON
 * date   : 7/26/2110:12 AM
 * desc   :
 */
public class FastPushFragment extends BaseFragment {
    private final static String TAG = "FastPushActivity";
    private RelativeLayout mSurfaceContainer;
    private SurfaceView mSurfaceView;
    private MediaPlayer mMediaPlayer;
    private CastController mMediaController;
    private String[] mCurrentUrls;
    private int mCurrentIndex = 0;
    private ViewGroup mBrowseContainer;
    private EditText mUrlEdit;
    private Button button1, button2, button3, button4, button5, button6;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mUrlEdit.setText(mCurrentUrls[mCurrentIndex]);
            if (!TextUtils.isEmpty(mUrlEdit.getText().toString())
                    && !mUrlEdit.getText().toString().contains("推送的url")) {
                Logger.i(TAG, "surfaceCreated url:" + mUrlEdit.getText().toString());
                openVideo(mUrlEdit.getText().toString(), holder.getSurface());
                return;
            }
            openVideo(mCurrentUrls[mCurrentIndex], holder.getSurface());
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };
    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            mMediaController.onPrepared();
        }
    };
    private MediaPlayer.OnVideoSizeChangedListener mVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            setDisplay(width, height);
        }
    };
    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            mMediaController.onError(what, extra);
            stopVideo();
            return false;
        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            stopVideo();
            mCurrentIndex = (++mCurrentIndex) % mCurrentUrls.length;
            mUrlEdit.setText(mCurrentUrls[mCurrentIndex]);
            initVideoView();
        }
    };

    private IEasyCastListener mCastListener = new IEasyCastListener() {

        /**
         * 如果没有触发此回调，则表示此次未搜索到设备或用户未投屏
         * @param lelinkServiceInfo
         * @return
         */
        @Override
        public EasyCastBean onCast(LelinkServiceInfo lelinkServiceInfo) {
            Logger.w(TAG, "onCast " + mCurrentIndex);
            EasyCastBean bean = new EasyCastBean();
            if (!TextUtils.isEmpty(mUrlEdit.getText().toString())
                    & !mUrlEdit.getText().toString().contains("推送的url")) {
                bean.url = mUrlEdit.getText().toString();
            } else {
                bean.url = mCurrentUrls[mCurrentIndex];
            }
            Logger.w(TAG, "onCast url:" + bean.url);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mMediaController.renderRemote();
                    mMediaController.setClickable(false);
                    stopVideo();
                }
            });
            return bean;
        }

        /**
         * 如果投屏失败，在搜索页面关闭的时候，会触发回调
         * @param lelinkServiceInfo
         * @param easyCastBean
         * @param what
         * @param extra
         */
        @Override
        public void onCastError(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean, int what, int extra) {
            Logger.w(TAG, "onCastError " + what + " / " + extra);
            mMediaController.onError(what, extra);
        }

        /**
         * 推送成功
         * @param lelinkServiceInfo
         * @param easyCastBean
         */
        @Override
        public void onCastLoading(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean) {
            Logger.w(TAG, "onCastLoading");
            mMediaController.prepare();
        }

        /**
         * 暂停
         * @param lelinkServiceInfo
         * @param easyCastBean
         */
        @Override
        public void onCastPause(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean) {
            Logger.w(TAG, "onCastPause");
            mMediaController.pause();
        }

        /**
         * 起播 或 收端暂停之后恢复播放
         * @param lelinkServiceInfo
         * @param easyCastBean
         */
        @Override
        public void onCastStart(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean) {
            Logger.w(TAG, "onCastStart");
            mMediaController.onPrepared();
        }

        @Override
        public void onCastPositionUpdate(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean, long duration, long position) {
            Logger.w(TAG, "onCastPositionUpdate " + duration + " / " + position);
        }

        /**
         * 收端播放结束，如果需要实现下一集，可在之后的onCast回调中提供播放信息
         * @param lelinkServiceInfo
         * @param easyCastBean
         */
        @Override
        public void onCastCompletion(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean) {
            Logger.w(TAG, "onCastCompletion " + mCurrentIndex);
            mCurrentIndex = (++mCurrentIndex) % mCurrentUrls.length;
        }

        /**
         * 搜索退出播放，不同于 onCastCompletion，此时需要恢复APP视频播放
         * @param lelinkServiceInfo
         * @param easyCastBean
         */
        @Override
        public void onCastStop(LelinkServiceInfo lelinkServiceInfo, EasyCastBean easyCastBean) {
            Logger.w(TAG, "onCastStop");
            mMediaController.renderLocal();
            initVideoView();
        }

        @Override
        public void onDismiss() {
            Logger.w(TAG, "onDismiss");
        }
    };

    @Override
    public int getLayoutID() {
        return R.layout.f_fast_push;
    }

    @Override
    public void init(View view) {
        mCurrentUrls = PushConfig.getInstance().getDrama_1080();
        mCurrentIndex = 0;
        mSurfaceContainer = view.findViewById(R.id.surface_container);
        view.findViewById(R.id.castBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseAndCast();
            }
        });
        mBrowseContainer = view.findViewById(R.id.browseContainer);
        mUrlEdit = view.findViewById(R.id.url_container);
        button1 = view.findViewById(R.id.video_1);
        button1.setOnClickListener(listener);
        button2 = view.findViewById(R.id.video_2);
        button2.setOnClickListener(listener);
        button3 = view.findViewById(R.id.video_3);
        button3.setOnClickListener(listener);
        button4 = view.findViewById(R.id.video_4);
        button4.setOnClickListener(listener);
        button5 = view.findViewById(R.id.video_5);
        button5.setOnClickListener(listener);
        button6 = view.findViewById(R.id.video_6);
        button6.setOnClickListener(listener);
        initVideoView();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.video_1:
                    mUrlEdit.setText(mCurrentUrls[0]);
                    break;
                case R.id.video_2:
                    mUrlEdit.setText(mCurrentUrls[1]);
                    break;
                case R.id.video_3:
                    mUrlEdit.setText(mCurrentUrls[2]);
                    break;
                case R.id.video_4:
                    mUrlEdit.setText(mCurrentUrls[3]);
                    break;
                case R.id.video_5:
                    mUrlEdit.setText(mCurrentUrls[4]);
                    break;
                case R.id.video_6:
                    mUrlEdit.setText(mCurrentUrls[5]);
                    break;
                default:
                    mUrlEdit.setText(mCurrentUrls[0]);
                    break;
            }
        }
    };

    private void initVideoView() {
        mSurfaceContainer.removeAllViews();
        SurfaceView surfaceView = new SurfaceView(getActivity());
        surfaceView.getHolder().addCallback(mSurfaceCallback);
        mSurfaceContainer.addView(surfaceView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mSurfaceView = surfaceView;
        mMediaController = new CastController(getActivity());
        mSurfaceContainer.addView(mMediaController, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mMediaController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVideo();
            }
        });
        mMediaController.setOnUserStopCastListener(new OnUserStopCastListener() {
            @Override
            public void onStopCast() {
                mMediaController.renderLocal();
                mMediaController.setClickable(true);
                LelinkSourceSDK.getInstance().stopPlay();
                initVideoView();
            }
        });
    }

    private void toggleVideo() {
        if (mMediaPlayer == null) {
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mMediaController.pause();
        } else {
            mMediaPlayer.start();
            mMediaController.start();
        }
    }

    private void openVideo(String url, Surface surface) {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
            mMediaPlayer = null;
        }
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mVideoSizeChangedListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
            mMediaController.prepare();
        } catch (Exception e) {
            Logger.w(TAG, e);
            mMediaPlayer = null;
        }
    }

    private void setDisplay(int width, int height) {
        if (mSurfaceView == null) {
            return;
        }
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(point);
        int screenWidth = point.x;
        int videoHeight = (int) (screenWidth * (height / (double) width));
        ViewGroup.LayoutParams params = mSurfaceView.getLayoutParams();
        params.width = screenWidth;
        params.height = videoHeight;
        mSurfaceView.setLayoutParams(params);
        ViewGroup.LayoutParams params1 = mMediaController.getLayoutParams();
        params1.width = screenWidth;
        params1.height = videoHeight;
        mMediaController.setLayoutParams(params1);
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
            mMediaController.stop();
        }
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
                .easyPush(mBrowseContainer);
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
