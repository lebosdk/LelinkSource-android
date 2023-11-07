package com.hpplay.sdk.source.test.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.display.VirtualDisplay;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hpplay.sdk.source.api.IConnectListener;
import com.hpplay.sdk.source.api.INewPlayerListener;
import com.hpplay.sdk.source.api.LelinkPlayerInfo;
import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.api.PlayerListenerConstant;
import com.hpplay.sdk.source.bean.CastBean;
import com.hpplay.sdk.source.browse.api.IAPI;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.test.DemoApplication;
import com.hpplay.sdk.source.test.IUIUpdateListener;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.activity.ReverseControlActivity;
import com.hpplay.sdk.source.test.config.PushConfig;
import com.hpplay.sdk.source.test.exscreen.ImageBrowseActivity;
import com.hpplay.sdk.source.test.manager.CastManager;
import com.hpplay.sdk.source.test.manager.DeviceManager;
import com.hpplay.sdk.source.test.media.MediaDataProvider;
import com.hpplay.sdk.source.bean.WatermarkBean;

import java.util.List;
import java.util.Objects;

public class MirrorFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = "MirrorFragment";

    public static final String IMG_LOCAL_MEDIA_PIC = "file:///android_asset/local_media/I01027343.png";

    private TextView mTitleView;
    private RadioGroup mRgResolution, mRgBitRate, mRgMirrorAudio, mRgAutoBitrate, mMirrorFullScreen, mRgMirrorAudioFocus;
    private SwitchCompat mMirrorSwitch, mWatermarkSwitch, mMirrorsecretSwitch, mCustomAudioSwitch;
    private SurfaceView mSurfaceView;
    private Surface mSurface;
    private EditText mExternalVideoEt;
    private boolean isAutoBitrate;
    private Button mExpandBtn;
    private String mPassword;
    private LelinkServiceInfo mSelectInfo;
    private EditText mMirrorTimeoutEdit, mFpsEdit;
    private Button mLocalMirroBtn;
    private Button mCloudMirroBtn;
    private Button mPauseBtn;
    private Button mResumeBtn;
    private Button mDisconnectBtn;

    private IUIUpdateListener mUIListener = new IUIUpdateListener() {
        @Override
        public void onUpdateDevices(List<LelinkServiceInfo> list) {

        }

        @Override
        public void onConnect(LelinkServiceInfo info) {
        }

        @Override
        public void onDisconnect(LelinkServiceInfo info) {
            DemoApplication.runOnUI(new Runnable() {
                @Override
                public void run() {
                    updateMirrorState();
                }
            });
        }

        @Override
        public void onNetChanged() {

        }

        @Override
        public void onBindSuccess() {

        }
    };

    private final RadioGroup.OnCheckedChangeListener mRadioChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (group.getId() == R.id.rg_auto_bitrate) {
                if (checkedId == R.id.rb_mirror_auto_bitrate_on) {
                    isAutoBitrate = true;
                    mRgResolution.getChildAt(0).setEnabled(false);
                    mRgResolution.getChildAt(1).setEnabled(false);
                    mRgResolution.getChildAt(2).setEnabled(false);
                    mRgBitRate.getChildAt(0).setEnabled(false);
                    mRgBitRate.getChildAt(1).setEnabled(false);
                    mRgBitRate.getChildAt(2).setEnabled(false);
                } else {
                    isAutoBitrate = false;
                    mRgResolution.getChildAt(0).setEnabled(true);
                    mRgResolution.getChildAt(1).setEnabled(true);
                    mRgResolution.getChildAt(2).setEnabled(true);
                    mRgBitRate.getChildAt(0).setEnabled(true);
                    mRgBitRate.getChildAt(1).setEnabled(true);
                    mRgBitRate.getChildAt(2).setEnabled(true);
                }
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener onAudioCheckedChangeListener = (buttonView, isChecked) -> {
        mSurfaceView.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        mExternalVideoEt.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        if (!isChecked) {
            MediaDataProvider.getInstance().stop();
            LelinkSourceSDK.getInstance().disableExternalAudio();
        } else {
            LelinkSourceSDK.getInstance().enableExternalAudio();
            MediaDataProvider.getInstance().setCallback((sampleRate, channel, audioFormat, data) ->
                    LelinkSourceSDK.getInstance().updatePCMData(sampleRate, channel, audioFormat, data, 0, data.length));
        }
    };

    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int id = compoundButton.getId();
            if (id == R.id.mirror_switch) {
                if (b) {
                    if (mSelectInfo != null) {
                        startMirror();
                    } else {
                        DemoApplication.toast("没有可镜像设备");
                    }
                } else {
                    LelinkSourceSDK.getInstance().stopPlay();
                    resetWatermarkStatus();
                    resetMirrorsecretStatus();
                }
            }

        }
    };

    private final CompoundButton.OnCheckedChangeListener onWatermarkCheckedChangeListener = (buttonView, isChecked) -> {
        LelinkSourceSDK.getInstance().setWatermarkVisible(isChecked);
    };

    private final CompoundButton.OnCheckedChangeListener onMirrorSecretCheckedChangeListener = (buttonView, isChecked) -> {
        LelinkSourceSDK.getInstance().setMirrorScreenSecret(isChecked);
    };

    private final INewPlayerListener mMirrorListener = new INewPlayerListener() {

        @Override
        public void onLoading(CastBean bean) {
        }

        @Override
        public void onStart(CastBean bean) {
            DemoApplication.toast("开始镜像");
            updateMirrorState();
        }

        @Override
        public void onPause(CastBean bean) {
            DemoApplication.toast("镜像暂停");
        }

        @Override
        public void onCompletion(CastBean bean, int type) {
        }

        @Override
        public void onStop(CastBean bean) {
            DemoApplication.toast("镜像停止");
            updateMirrorState();
        }

        @Override
        public void onSeekComplete(CastBean bean, int position) {

        }

        @Override
        public void onInfo(CastBean bean, int what, int extra) {


        }

        @Override
        public void onInfo(CastBean bean, int what, String data) {
            Logger.i(TAG, "onInfo, what = " + what + ", data = " + data);
            // 防骚扰支持输入验证码时，请提示用户输入密码以及超时时间，data 为超时时间，单位为 秒
            if (what == PlayerListenerConstant.HARASS_RECEIVE_CODE
                    || what == PlayerListenerConstant.HARASS_SEND_CODE_WRONG) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showHarassCodeDialog();
                    }
                });
            }
        }

        @Override
        public void onError(CastBean bean, int what, int extra) {
            String text = "未知异常";
            switch (extra) {
                case EXTRA_ERROR_AUTH:
                case EXTRA_ERROR_AUTH_TIME_DONE:
                    text = "SDK认证失败";
                    break;
                case EXTRA_NEED_SCREEN_CODE:
                    text = "请输入密码";
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showPWDDialog();
                        }
                    });
                    break;
                case EXTRA_DEVICE_OFFLINE:
                    text = "接收端不在线";
                    break;
                case EXTRA_ERROR_MIRROR_REQUEST_PERMISSION:
                    text = "申请录屏权限发生异常";
                    break;
                case EXTRA_ERROR_MIRROR_REJECT_PERMISSION:
                    text = "申请录屏权限被拒绝";
                    break;
                case EXTRA_ERROR_MIRROR_DEVICE_UNSUPPORTED:
                    text = "此设备不支持镜像";
                    break;
                case EXTRA_ERROR_MIRROR_IO:
                    text = "网络通讯异常";
                    break;
                case EXTRA_ERROR_MIRROR_UNSUPPORTED:
                    text = "SDK不支持镜像";
                    break;
                case EXTRA_ERROR_MIRROR_SINK_UNSUPPORTED:
                    text = "接收端不支持此镜像";
                    break;
                case EXTRA_ERROR_MIRROR_CLOUD_NO_RIGHT:
                    text = "没有云镜像权益";
                    break;

                case EXTRA_ERROR_MIRROR_INVALID_INPUT:
                    text = "输入异常";
                    break;
            }
            if (TextUtils.isEmpty(text)) {
                text = "镜像 onError " + what + "/" + extra;
            }
            DemoApplication.toast(text);
            updateMirrorState();
        }

        @Override
        public void onVolumeChanged(CastBean bean, float percent) {

        }

        @Override
        public void onPositionUpdate(CastBean bean, long duration, long position) {

        }
    };

    @Override
    public int getLayoutID() {
        return R.layout.f_mirror;
    }

    @Override
    public void init(View view) {
        initView(view);
    }

    private void initView(View view) {
        Logger.i(TAG, "initView");
        mTitleView = view.findViewById(R.id.title);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        mRgResolution = view.findViewById(R.id.rg_resolution);
        mRgBitRate = view.findViewById(R.id.rg_bitrate);
        mRgMirrorAudio = view.findViewById(R.id.rg_mirror_audio);
        mRgAutoBitrate = view.findViewById(R.id.rg_auto_bitrate);
//        mMirrorFullScreen = view.findViewById(R.id.rg_full_screen);
//        mMirrorFullScreen.check(R.id.rb_mirror_full_screen_auto);
        mMirrorSwitch = view.findViewById(R.id.mirror_switch);
        mWatermarkSwitch = view.findViewById(R.id.switch_watermark);
        mWatermarkSwitch.setOnCheckedChangeListener(onWatermarkCheckedChangeListener);
        mMirrorsecretSwitch = view.findViewById(R.id.switch_mirrorsecret);
        mMirrorsecretSwitch.setOnCheckedChangeListener(onMirrorSecretCheckedChangeListener);
        mMirrorTimeoutEdit = view.findViewById(R.id.timeout_edit);
        mCustomAudioSwitch = view.findViewById(R.id.custom_audio_switch);
        mCustomAudioSwitch.setChecked(false);
        mCustomAudioSwitch.setOnCheckedChangeListener(onAudioCheckedChangeListener);

//        mCloudMirroBtn = view.findViewById(R.id.btn_multi_mirror_public);
//        mLocalMirroBtn = view.findViewById(R.id.btn_multi_mirror_local);
        mPauseBtn = view.findViewById(R.id.btn_pause_mirror);
        mResumeBtn = view.findViewById(R.id.btn_resume_mirror);
        mDisconnectBtn = view.findViewById(R.id.btn_disconnect);
        mFpsEdit = view.findViewById(R.id.edit_fps);

        view.findViewById(R.id.btn_set_fps).setOnClickListener(this);
        mDisconnectBtn.setOnClickListener(this);
//        mLocalMirroBtn.setOnClickListener(this);
//        mCloudMirroBtn.setOnClickListener(this);
        mPauseBtn.setOnClickListener(this);
        mResumeBtn.setOnClickListener(this);

        view.findViewById(R.id.btn_reverse_control).setOnClickListener(this);
        view.findViewById(R.id.btn_watermark_setting).setOnClickListener(this);
        mExpandBtn = view.findViewById(R.id.btn_expan);
        mExpandBtn.setOnClickListener(this);
        mRgAutoBitrate.setOnCheckedChangeListener(mRadioChangeListener);
        if (mSelectInfo != null) {
            mTitleView.setText(mSelectInfo.getName());
        }
        mExternalVideoEt = view.findViewById(R.id.et_external_video_path);
        mExternalVideoEt.setText(PushConfig.getInstance().getExternalVideo());
        initSurface(view);
    }

    private void initSurface(View view) {
        mSurfaceView = view.findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(callback2);
        mSurfaceView.setOnClickListener(v -> {
            MediaDataProvider.getInstance().start(mSurface, PushConfig.getInstance().getExternalVideo());
        });
    }

    SurfaceHolder.Callback2 callback2 = new SurfaceHolder.Callback2() {
        @Override
        public void surfaceRedrawNeeded(SurfaceHolder holder) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Logger.i(TAG, "surfaceCreated: ");
            mSurface = holder.getSurface();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateMirrorState();
        CastManager.getInstance().addPlayerListener(mMirrorListener);
        DeviceManager.getInstance().addUIListener(mUIListener);
        setSelectInfo(DeviceManager.getInstance().getSelectInfo());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pause_mirror:
                LelinkSourceSDK.getInstance().pause();
                break;
            case R.id.btn_resume_mirror:
                LelinkSourceSDK.getInstance().resume();
                break;
//            case R.id.btn_multi_mirror_public:
//                //WARING 多通道切换涉及到收费功能云镜像，目前仅在乐播APP生效
//                LelinkSourceSDK.getInstance().switchMirror(IConnectListener.TYPE_IM);
//                break;
//            case R.id.btn_multi_mirror_local:
//                //WARING 多通道切换涉及到收费功能云镜像，目前仅在乐播APP生效
//                LelinkSourceSDK.getInstance().switchMirror(IConnectListener.TYPE_LELINK);
//                break;
            case R.id.btn_expan:
                Intent intent = new Intent(getActivity(), ImageBrowseActivity.class);
                intent.putExtra("serviceinfo", mSelectInfo);
                startActivity(intent);
                break;
            case R.id.btn_reverse_control:
                ReverseControlActivity.show(Objects.requireNonNull(getContext()));
                break;
            case R.id.btn_watermark_setting:
                showWatermarkDialog();
                break;
            case R.id.iv_back:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this)
                        .commitAllowingStateLoss();
                break;
            case R.id.btn_get_virtual_display:
                VirtualDisplay vd = LelinkSourceSDK.getInstance().getVirtualDisplay();
                Logger.i(TAG, "vd = " + vd);
                break;
            case R.id.btn_disconnect:
                if (mSelectInfo == null) {
                    DemoApplication.toast("请先选择接收端设备");
                    return;
                }
                LelinkSourceSDK.getInstance().disconnect(mSelectInfo);
                break;
            case R.id.btn_set_fps:
                if (!TextUtils.isEmpty(mFpsEdit.getText().toString())) {
                    Logger.i(TAG, "set fps size = " + mFpsEdit.getText().toString());
                    LelinkSourceSDK.getInstance().setOption(IAPI.OPTION_SET_FRAME_RATE,
                            Integer.parseInt(mFpsEdit.getText().toString()));
                }
                break;
        }
    }

    private void sendHarassCode(String harassCode) {
        if (TextUtils.isEmpty(harassCode)) {
            Toast.makeText(getContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        LelinkSourceSDK.getInstance().setOption(IAPI.OPTION_SEND_HARASS_CODE, harassCode);
    }

    public void setSelectInfo(LelinkServiceInfo info) {
        mSelectInfo = info;
        if (mTitleView != null && mSelectInfo != null) {
            mTitleView.setText(mSelectInfo.getName());
        }
    }

    public void updateMirrorState() {
        DemoApplication.runOnUI(() -> {
            mMirrorSwitch.setOnCheckedChangeListener(null);
            mMirrorSwitch.setChecked(isMirroring());
            mMirrorSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        });
    }

    private void setWatermarkVisible(boolean status) {
        LelinkSourceSDK.getInstance().setWatermarkVisible(status);
    }

    private void resetWatermarkStatus() {
        DemoApplication.runOnUI(new Runnable() {
            @Override
            public void run() {
                mWatermarkSwitch.setChecked(false);
            }
        });
    }

    private void resetMirrorsecretStatus() {
        DemoApplication.runOnUI(new Runnable() {
            @Override
            public void run() {
                mMirrorsecretSwitch.setChecked(false);
            }
        });
    }

    private boolean isMirroring() {
        Object mirrorState = LelinkSourceSDK.getInstance().getOption(IAPI.OPTION_32);
        return mirrorState instanceof Integer
                && ((int) mirrorState == LelinkSourceSDK.MIRROR_PLAYING
                || (int) mirrorState == LelinkSourceSDK.MIRROR_PAUSE);
    }

    private void startMirror() {
        if (mSelectInfo == null) {
            DemoApplication.toast("请选择设备!");
            return;
        }
        // 分辨率
        int resolutionLevel = 0;
        int resolutionCheckId = mRgResolution.getCheckedRadioButtonId();
        switch (resolutionCheckId) {
            case R.id.rb_resolution_height:
                resolutionLevel = LelinkSourceSDK.RESOLUTION_HIGH;
                break;
            case R.id.rb_resolution_middle:
                resolutionLevel = LelinkSourceSDK.RESOLUTION_MIDDLE;
                break;
            case R.id.rb_resolution_low:
                resolutionLevel = LelinkSourceSDK.RESOLUTION_AUTO;
                break;
        }

        // 比特率
        int bitrateLevel = 0;
        int bitrateCheckId = mRgBitRate.getCheckedRadioButtonId();
        switch (bitrateCheckId) {
            case R.id.rb_bitrate_height:
                bitrateLevel = LelinkSourceSDK.BITRATE_HIGH;
                break;
            case R.id.rb_bitrate_middle:
                bitrateLevel = LelinkSourceSDK.BITRATE_MIDDLE;
                break;
            case R.id.rb_bitrate_low:
                bitrateLevel = LelinkSourceSDK.BITRATE_LOW;
                break;
        }
        // 音频
        int audioType = LelinkPlayerInfo.CAPTURE_AUDIO_AUTO;
        int audioCheckId = mRgMirrorAudio.getCheckedRadioButtonId();
        switch (audioCheckId) {
            case R.id.rb_mirror_audio_on:
                audioType = LelinkPlayerInfo.CAPTURE_AUDIO_AUTO;
                break;
            case R.id.rb_mirror_audio_off:
                audioType = LelinkPlayerInfo.CAPTURE_AUDIO_CLOSE;
                break;
        }
        // 全屏镜像
        int fullScreen = LelinkPlayerInfo.FULLSCREEN_AUTO;
//        switch (mMirrorFullScreen.getCheckedRadioButtonId()) {
//            case R.id.rb_mirror_full_screen_auto:
//                fullScreen = LelinkPlayerInfo.FULLSCREEN_AUTO;
//                break;
//            case R.id.rb_mirror_full_screen_on:
//                fullScreen = LelinkPlayerInfo.FULLSCREEN_ON;
//                break;
//            case R.id.rb_mirror_full_screen_off:
//                fullScreen = LelinkPlayerInfo.FULLSCREEN_OFF;
//                break;
//        }
        //两种方法都可镜像
        LelinkPlayerInfo lelinkPlayerInfo = new LelinkPlayerInfo();
        lelinkPlayerInfo.setAutoBitrate(isAutoBitrate);
        lelinkPlayerInfo.setLelinkServiceInfo(mSelectInfo);
        lelinkPlayerInfo.setBitRateLevel(bitrateLevel);
        lelinkPlayerInfo.setResolutionLevel(resolutionLevel);
        int timeout = 0;
        if (!TextUtils.isEmpty(mMirrorTimeoutEdit.getText().toString())) {
            timeout = Integer.parseInt(mMirrorTimeoutEdit.getText().toString());
        }
        lelinkPlayerInfo.setMirrorSendTimeout(timeout);
        if (!TextUtils.isEmpty(mPassword)) {
            lelinkPlayerInfo.setCastPwd(mPassword);
        }
        lelinkPlayerInfo.setMirrorAudioType(audioType);
        lelinkPlayerInfo.setFullScreen(fullScreen);
        if (mSurfaceView.getVisibility() == View.VISIBLE) {
            LelinkSourceSDK.getInstance().enableExternalAudio();
        } else {
            LelinkSourceSDK.getInstance().disableExternalAudio();
        }
        LelinkSourceSDK.getInstance().startMirror(lelinkPlayerInfo);
        setWatermarkVisible(mWatermarkSwitch.isChecked());
    }

    public void showHarassCodeDialog() {
        final EditText editText = new EditText(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle("请输入验证码")
                .setView(editText)
                .setPositiveButton("确定", (diaLoggerInterface, i) -> {
                    String code = editText.getText().toString();
                    if (TextUtils.isEmpty(code)) {
                        DemoApplication.toast("请输入验证码");
                        return;
                    }
                    sendHarassCode(code);
                }).setNegativeButton("取消", null).show();
    }

    public void showPWDDialog() {
        final EditText editText = new EditText(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle("请输入密码")
                .setView(editText)
                .setPositiveButton("确定", (diaLoggerInterface, i) -> {
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        DemoApplication.toast("请输入密码");
                        return;
                    }
                    mPassword = editText.getText().toString();
                    if (!mMirrorSwitch.isChecked()) {
                        mMirrorSwitch.setChecked(true);
                    } else {
                        startMirror();
                    }
                }).setNegativeButton("取消", null).show();
    }

    public void showWatermarkDialog() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editId = new EditText(getActivity());
        editId.setLayoutParams(params);
        final EditText editPath = new EditText(getActivity());
        editPath.setLayoutParams(params);
        final EditText editXpoint = new EditText(getActivity());
        editXpoint.setLayoutParams(params);
        final EditText editYpoint = new EditText(getActivity());
        editYpoint.setLayoutParams(params);
        final TextView textId = new TextView(getActivity());
        final TextView textPath = new TextView(getActivity());
        final TextView textXpoint = new TextView(getActivity());
        final TextView textYpoint = new TextView(getActivity());
        textId.setText("sourceId:");
        textPath.setText("sourcePath:");
        textXpoint.setText("xpoint:");
        textYpoint.setText("ypoint:");
        editId.setHint("" + R.drawable.image_scale);
//        editXpoint.setHint("0.5");
//        editYpoint.setHint("0.5");
        // 设置水印图片路径
        // editPath.setHint(IMG_LOCAL_MEDIA_PIC);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layoutId = new LinearLayout(getActivity());
        layoutId.setOrientation(LinearLayout.HORIZONTAL);
        layoutId.addView(textId);
        layoutId.addView(editId);
        layout.addView(layoutId);

        LinearLayout layoutPath = new LinearLayout(getActivity());
        layoutPath.addView(textPath);
        layoutPath.addView(editPath);
        layout.addView(layoutPath);

        LinearLayout layoutXpoint = new LinearLayout(getActivity());
        layoutXpoint.addView(textXpoint);
        layoutXpoint.addView(editXpoint);
        layout.addView(layoutXpoint);

        LinearLayout layoutYpoint = new LinearLayout(getActivity());
        layoutYpoint.addView(textYpoint);
        layoutYpoint.addView(editYpoint);
        layout.addView(layoutYpoint);

        Logger.i(TAG, "showWatermarkDialog asserts测试路径:" + IMG_LOCAL_MEDIA_PIC + ", 测试资源id（R.drawable.image_scale）:" + R.drawable.image_scale);
        new AlertDialog.Builder(getActivity()).setTitle("镜像水印配置信息")
                .setView(layout)
                .setPositiveButton("确定", (diaLoggerInterface, i) -> {
                    try {
                        String id = editId.getText().toString();
                        String path = editPath.getText().toString();
                        String xpoint = editXpoint.getText().toString();
                        String ypoint = editYpoint.getText().toString();
                        Logger.i(TAG, "镜像水印 id:" + id + ", xpoint:" + xpoint + ", ypoint:" + xpoint
                                + "\npath:" + path);
                        WatermarkBean watermarkBean = new WatermarkBean();
                        if (!TextUtils.isEmpty(id)) {
                            watermarkBean.sourceId = Integer.parseInt(id);
                        } else if (editId.getHint() != null && editId.getHint().toString() != null) {
                            watermarkBean.sourceId = Integer.parseInt(editId.getHint().toString());
                        }

                        if (!TextUtils.isEmpty(path)) {
                            watermarkBean.sourcePath = path;
                        } else if (editPath.getHint() != null && editPath.getHint().toString() != null) {
                            watermarkBean.sourcePath = editPath.getHint().toString();
                        }

                        if (!TextUtils.isEmpty(xpoint)) {
                            watermarkBean.xPositionRatio = Float.parseFloat(xpoint);
                        }

                        if (!TextUtils.isEmpty(ypoint)) {
                            watermarkBean.yPositionRatio = Float.parseFloat(ypoint);
                        }
                        Logger.i(TAG, "镜像水印 id:" + watermarkBean.sourceId + ", xPositionRatio:" + watermarkBean.xPositionRatio + ", yPositionRatio:" + watermarkBean.yPositionRatio
                                + "\npath:" + watermarkBean.sourcePath);
                        LelinkSourceSDK.getInstance().setWatermarkInfo(watermarkBean);
                    } catch (Exception e) {

                    }
                }).setNegativeButton("取消", null).show();
    }

    @Override
    public void onDestroyView() {
        CastManager.getInstance().removeListener(mMirrorListener);
        DeviceManager.getInstance().removeUIListener(mUIListener);
        mSurfaceView.getHolder().removeCallback(callback2);
        MediaDataProvider.getInstance().stop();
        super.onDestroyView();

        mMirrorSwitch.setOnCheckedChangeListener(null);
        mWatermarkSwitch.setOnCheckedChangeListener(null);
        mMirrorsecretSwitch.setOnCheckedChangeListener(null);
        mCustomAudioSwitch.setOnCheckedChangeListener(null);
        mRgAutoBitrate.setOnCheckedChangeListener(null);

        mTitleView = null;
        mRgResolution = null;
        mRgBitRate = null;
        mRgMirrorAudio = null;
        mRgAutoBitrate = null;
        mMirrorFullScreen = null;
        mRgMirrorAudioFocus = null;
        mMirrorSwitch = null;
        mWatermarkSwitch = null;
        mMirrorsecretSwitch = null;
        mCustomAudioSwitch = null;
        mSurfaceView = null;
        mExternalVideoEt = null;
        mExpandBtn = null;
        mMirrorTimeoutEdit = null;
        mLocalMirroBtn = null;
        mCloudMirroBtn = null;
        mPauseBtn = null;
        mResumeBtn = null;
        mDisconnectBtn = null;
    }
}
