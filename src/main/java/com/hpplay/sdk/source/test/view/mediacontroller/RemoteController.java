package com.hpplay.sdk.source.test.view.mediacontroller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.test.R;

/**
 * author : DON
 * date   : 5/20/2111:12 AM
 * desc   :
 */
public class RemoteController extends AbsMediaController {
    private TextView mStatusTxt;
    private Button mStopBtn;

    public RemoteController(Context context) {
        super(context);
        View view = View.inflate(context, R.layout.view_remote_media, null);
        this.addView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mStatusTxt = view.findViewById(R.id.statusTxt);
        mStopBtn = view.findViewById(R.id.stopCast);
        mStopBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LelinkSourceSDK.getInstance().stopPlay();
                if (mStopListener != null) {
                    mStopListener.onStopCast();
                }
            }
        });
    }

    @Override
    public void prepare() {
        mStatusTxt.setText("投屏中...");
    }

    @Override
    public void onPrepared() {
        mStatusTxt.setText("投屏中...");
    }

    @Override
    public void onError(int what, int extra) {
        mStatusTxt.setText("投屏失败");
    }

    @Override
    public void start() {
        mStatusTxt.setText("投屏中...");
    }

    @Override
    public void pause() {
        mStatusTxt.setText("暂停");
    }

    @Override
    public void stop() {

    }

    @Override
    public void release() {

    }
}
