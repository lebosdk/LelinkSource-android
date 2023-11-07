package com.hpplay.sdk.source.test.view.mediacontroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * author : DON
 * date   : 5/20/2111:10 AM
 * desc   :
 */
public class CastController extends AbsMediaController {
    private static final int RENDER_LOCAL = 1;
    private static final int RENDER_REMOTE = 2;
    private int mRenderType = RENDER_LOCAL;
    private LocalController mLocalController;
    private RemoteController mRemoteController;
    private AbsMediaController mMediaController;

    public CastController(Context context) {
        super(context);
        init(context);
    }

    public CastController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CastController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mLocalController = new LocalController(context);
        mRemoteController = new RemoteController(context);
        initController();
    }

    public void renderRemote() {
        if (mRenderType == RENDER_REMOTE) {
            return;
        }
        mRenderType = RENDER_REMOTE;
        initController();
    }

    public void renderLocal() {
        if (mRenderType == RENDER_LOCAL) {
            return;
        }
        mRenderType = RENDER_LOCAL;
        initController();
    }

    private void initController() {
        if (mMediaController != null && mMediaController.getParent() != null) {
            this.removeView(mMediaController);
        }
        switch (mRenderType) {
            case RENDER_LOCAL:
                mMediaController = mLocalController;
                break;
            case RENDER_REMOTE:
                mMediaController = mRemoteController;
                break;
        }
        this.addView(mMediaController, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mMediaController.setOnUserStopCastListener(mStopListener);
    }

    @Override
    public void setOnUserStopCastListener(OnUserStopCastListener listener) {
        super.setOnUserStopCastListener(listener);
    }

    @Override
    public void prepare() {
        mMediaController.prepare();
    }

    @Override
    public void onPrepared() {
        mMediaController.onPrepared();
    }

    @Override
    public void onError(int what, int extra) {
        mMediaController.onError(what, extra);
    }

    @Override
    public void start() {
        mMediaController.start();
    }

    @Override
    public void pause() {
        mMediaController.pause();
    }

    @Override
    public void stop() {
        mMediaController.stop();
    }

    @Override
    public void release() {
        mMediaController.release();
    }
}
