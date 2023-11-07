package com.hpplay.sdk.source.test.view.mediacontroller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hpplay.sdk.source.test.R;

/**
 * author : DON
 * date   : 5/20/2111:11 AM
 * desc   :
 */
public class LocalController extends AbsMediaController {
    private ImageView mPlayBtn;

    public LocalController(Context context) {
        super(context);
        View view = View.inflate(context, R.layout.view_local_meidia, null);
        this.addView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPlayBtn = view.findViewById(R.id.controlIcon);
    }

    @Override
    public void prepare() {
        mPlayBtn.setVisibility(GONE);
    }

    @Override
    public void onPrepared() {
        mPlayBtn.setVisibility(GONE);
    }

    @Override
    public void onError(int what, int extra) {
        mPlayBtn.setVisibility(VISIBLE);
    }

    @Override
    public void start() {
        mPlayBtn.setVisibility(GONE);
    }

    @Override
    public void pause() {
        mPlayBtn.setVisibility(VISIBLE);
    }

    @Override
    public void stop() {
        mPlayBtn.setVisibility(VISIBLE);
    }

    @Override
    public void release() {
        mPlayBtn.setVisibility(VISIBLE);
    }
}
