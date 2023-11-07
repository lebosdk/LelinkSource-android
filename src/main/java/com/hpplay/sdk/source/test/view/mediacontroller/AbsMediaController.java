package com.hpplay.sdk.source.test.view.mediacontroller;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * author : DON
 * date   : 5/20/2111:16 AM
 * desc   :
 */
public abstract class AbsMediaController extends RelativeLayout {
    protected OnUserStopCastListener mStopListener;

    public AbsMediaController(Context context) {
        super(context);
    }

    public AbsMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnUserStopCastListener(OnUserStopCastListener listener) {
        mStopListener = listener;
    }

    public abstract void prepare();

    public abstract void onPrepared();

    public abstract void onError(int what, int extra);

    public abstract void start();

    public abstract void pause();

    public abstract void stop();

    public abstract void release();

}
