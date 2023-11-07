package com.hpplay.sdk.source.test.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.hpplay.sdk.source.api.IDaPlayerListener;
import com.hpplay.sdk.source.api.INewPlayerListener;
import com.hpplay.sdk.source.api.LelinkPlayerInfo;
import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.browse.api.ReceiverPropertyAction;
import com.hpplay.sdk.source.browse.api.ReceiverPropertyValue;
import com.hpplay.sdk.source.bean.CastBean;
import com.hpplay.sdk.source.bean.DaCastBean;
import com.hpplay.sdk.source.bean.MediaAssetBean;
import com.hpplay.sdk.source.bean.MicroAppInfoBean;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.log.SourceLog;
import com.hpplay.sdk.source.test.DemoApplication;
import com.hpplay.sdk.source.test.DialogFactory;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.config.PushConfig;
import com.hpplay.sdk.source.test.manager.CastManager;
import com.hpplay.sdk.source.test.manager.DeviceManager;
import com.hpplay.sdk.source.bean.ReceiverProperties;
import com.hpplay.sdk.source.api.IReceiverPropertiesCallback;


public class PushFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {
    private final static String TAG = "PushFragment";
    private final SeekBar.OnSeekBarChangeListener mProgressChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // ignore
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // ignore
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            if (seekBar.getId() == R.id.seekbar_progress) {
                LelinkSourceSDK.getInstance().seekTo(progress);
            }
        }
    };
    private ImageView mBackIv;
    private TextView mTitleView;
    private RadioGroup mRadioGroup;
    private EditText mUrlEditText;
    private EditText mEtRate;
    private EditText mEtTrack;
    private SeekBar mProgressBar;
    private Switch mSwTempRestrict;
    private LelinkServiceInfo mSelectInfo;
    private String mPassword;
    private RadioGroup rgPlayer, rgPlayMode, rgRotate;

    private IDaPlayerListener mDaListener = new IDaPlayerListener() {
        @Override
        public void onResult(DaCastBean bean, boolean hasDa) {
            Logger.i(TAG, "mDaListener onResult :" + hasDa);
        }

        @Override
        public void onLoading(DaCastBean bean) {
            Logger.i(TAG, "mDaListener onLoading");
            DemoApplication.toast("广告开始加载");
        }

        @Override
        public void onStart(DaCastBean bean) {
            Logger.i(TAG, "mDaListener onStart");
            DemoApplication.toast("广告开始播放");
        }

        @Override
        public void onStop(DaCastBean bean) {
            Logger.i(TAG, "mDaListener onStop");
            DemoApplication.toast("广告播放停止");
        }
    };

    private final INewPlayerListener mPushListener = new INewPlayerListener() {
        @Override
        public void onLoading(CastBean bean) {
            Logger.i(TAG, "mPushListener onLoading");
            DemoApplication.toast("开始加载");
        }

        @Override
        public void onStart(CastBean bean) {
            Logger.i(TAG, "mPushListener onStart");
            DemoApplication.toast("开始播放");
        }

        @Override
        public void onPause(CastBean bean) {
            Logger.i(TAG, "mPushListener onPause");
            DemoApplication.toast("暂停播放");
        }

        @Override
        public void onCompletion(CastBean bean, int type) {
            Logger.i(TAG, "mPushListener onCompletion");
            DemoApplication.toast("播放完成");
            resetTempRestrictState();
        }

        @Override
        public void onStop(CastBean bean) {
            Logger.i(TAG, "mPushListener onStop");
            DemoApplication.toast("播放停止");
            resetTempRestrictState();
        }

        @Override
        public void onSeekComplete(CastBean bean, int position) {

        }

        @Override
        public void onInfo(CastBean bean, int what, int extra) {
            Logger.i(TAG, "mPushListener onInfo:" + what + "/" + extra);
        }

        @Override
        public void onInfo(CastBean bean, int what, String data) {
            Logger.i(TAG, "mPushListener onInfo:" + what + "/" + data);
            if (what == RELEVANCE_RATE_QUERY_RESPONSE || what == RELEVANCE_RATE_UPDATE) {
                DemoApplication.toast("当前倍率是:" + data);
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
                case EXTRA_ERROR_PUSH_IO:
                    text = "网络通讯异常";
                    break;
            }
            if (TextUtils.isEmpty(text)) {
                text = "推送 onError " + what + "/" + extra;
            }
            if (bean != null) {
                Logger.i(TAG, "mPushListener onError:" + text + " errorInfo: " + bean.errorInfo);
            }
            DemoApplication.toast(text);
            resetTempRestrictState();
        }

        @Override
        public void onVolumeChanged(CastBean bean, float percent) {

        }

        @Override
        public void onPositionUpdate(CastBean bean, long duration, long position) {
            //Logger.i(TAG, "mPushListener onPositionUpdate:" + duration + "/" + position);
            if (mProgressBar != null) {
                mProgressBar.setMax((int) duration);
                mProgressBar.setProgress((int) position);
            }
        }
    };


    @Override
    public int getLayoutID() {
        return R.layout.f_push;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CastManager.getInstance().addPlayerListener(mPushListener);
        CastManager.getInstance().addDaPlayerListener(mDaListener);
        setSelectInfo(DeviceManager.getInstance().getSelectInfo());
    }

    @Override
    public void init(View view) {
        initData();
        initView(view);
    }

    private void initView(View view) {
        mTitleView = view.findViewById(R.id.title);
        mBackIv = view.findViewById(R.id.iv_back);
        mBackIv.setOnClickListener(this);
        mUrlEditText = view.findViewById(R.id.edit_url);
        mRadioGroup = view.findViewById(R.id.radio_group);
        mProgressBar = view.findViewById(R.id.seekbar_progress);
        mEtRate = view.findViewById(R.id.ed_rate);
        mEtTrack = view.findViewById(R.id.edtTrack);
        view.findViewById(R.id.btn_play).setOnClickListener(this);
        view.findViewById(R.id.btn_pause).setOnClickListener(this);
        view.findViewById(R.id.btn_resume).setOnClickListener(this);
        view.findViewById(R.id.btn_stop).setOnClickListener(this);
        view.findViewById(R.id.btn_volume_up).setOnClickListener(this);
        view.findViewById(R.id.btn_volume_down).setOnClickListener(this);
        view.findViewById(R.id.btn_send_mediaasset_info).setOnClickListener(this);
        view.findViewById(R.id.btn_send_header_info).setOnClickListener(this);
        view.findViewById(R.id.btn_loop_mode).setOnClickListener(this);
        view.findViewById(R.id.btn_send_rate_info).setOnClickListener(this);
        view.findViewById(R.id.btn_get_rate_info).setOnClickListener(this);
        view.findViewById(R.id.btnSelectTrack).setOnClickListener(this);
        rgPlayer = view.findViewById(R.id.radio_set_receiver_player);
        rgPlayer.setOnCheckedChangeListener(this);
        rgPlayMode = view.findViewById(R.id.radio_set_receiver_play_mode);
        rgPlayMode.setOnCheckedChangeListener(this);
        rgRotate = view.findViewById(R.id.radio_set_receiver_angle_rotate);
        rgRotate.setOnCheckedChangeListener(this);
        mSwTempRestrict = view.findViewById(R.id.switch_temp_restrict);
        mSwTempRestrict.setOnCheckedChangeListener(this);
        view.findViewById(R.id.send_danmaku).setOnClickListener(this);
        view.findViewById(R.id.danmaku_settings).setOnClickListener(this);
        view.findViewById(R.id.btn_sync_receiver_properties).setOnClickListener(this);
        mUrlEditText.setText(PushConfig.getInstance().getNetVideo());
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PushConfig pushConfig = PushConfig.getInstance();
                switch (checkedId) {
                    case R.id.rb_net_video:
                        mUrlEditText.setText(pushConfig.getNetVideo());
                        break;
                    case R.id.rb_net_music:
                        mUrlEditText.setText(pushConfig.getNetMusic());
                        break;
                    case R.id.rb_net_picture:
                        mUrlEditText.setText(pushConfig.getNetPhoto());
                        break;
                    case R.id.rb_local_video:
                        mUrlEditText.setText(pushConfig.getLocalVideo());
                        break;
                    case R.id.rb_local_music:
                        mUrlEditText.setText(pushConfig.getLocalMusic());
                        break;
                    case R.id.rb_local_picture:
                        mUrlEditText.setText(pushConfig.getLocalPhoto());
                        break;
                }
            }
        });
        mProgressBar.setOnSeekBarChangeListener(mProgressChangeListener);
        if (mSelectInfo != null) {
            mTitleView.setText(mSelectInfo.getName());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                startPlayMedia();
                break;
            case R.id.btn_pause:
                LelinkSourceSDK.getInstance().pause();
                break;
            case R.id.btn_resume:
                LelinkSourceSDK.getInstance().resume();
                break;
            case R.id.btn_stop:
                LelinkSourceSDK.getInstance().stopPlay();
                resetTempRestrictState();
                break;
            case R.id.btn_volume_up:
                LelinkSourceSDK.getInstance().addVolume();
                break;
            case R.id.btn_volume_down:
                LelinkSourceSDK.getInstance().subVolume();
                break;
            case R.id.btn_send_rate_info:
                sendRate();
                break;
            case R.id.btn_get_rate_info:
                if (!LelinkSourceSDK.getInstance().isSupportQueryRate(mSelectInfo)) {
                    DemoApplication.toast("设备不支持查询倍率");
                    return;
                }
                LelinkSourceSDK.getInstance().queryRate();
                break;
            case R.id.send_danmaku:
                if (!LelinkSourceSDK.getInstance().isSupportDanmaku(mSelectInfo)) {
                    DemoApplication.toast("设备不支持弹幕");
                    return;
                }
                DialogFactory.showDanmukuSendDialog(getActivity());
                break;
            case R.id.danmaku_settings:
                DialogFactory.showDanmakuSettingDailog(getActivity());
                break;
            case R.id.iv_back:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this)
                        .commitAllowingStateLoss();
                break;
            case R.id.btnSelectTrack:
                if (!LelinkSourceSDK.getInstance().isSupportTrack(mSelectInfo)) {
                    DemoApplication.toast("设备不支持音轨");
                    return;
                }
                String trackIndex = mEtTrack.getText().toString().trim();
                try {
                    int index = Integer.parseInt(trackIndex);
                    LelinkSourceSDK.getInstance().selectTrack(index);
                } catch (NumberFormatException e) {
                    DemoApplication.toast("请输入正确的音轨下标");
                }
                break;
            case R.id.btn_sync_receiver_properties:
                if (!LelinkSourceSDK.getInstance().isSupportGetReceiverProperties(mSelectInfo)) {
                    DemoApplication.toast("设备不支持获取播放特性");
                    return;
                }
                syncReceiverProperties();
                break;
        }
    }

    private void initData() {
    }

    public void setSelectInfo(LelinkServiceInfo info) {
        mSelectInfo = info;
        if (mTitleView != null && mSelectInfo != null) {
            mTitleView.setText(mSelectInfo.getName());
        }
    }

    private void startPlayMedia() {
        if (null == mSelectInfo) {
            DemoApplication.toast("请选择接设备");
            return;
        }
        String url = mUrlEditText.getText().toString();
        if (TextUtils.isEmpty(url)) {
            DemoApplication.toast("请输入投屏地址");
            return;
        }
        int checkedId = mRadioGroup.getCheckedRadioButtonId();
        int mediaType = 0;
        String mediaTypeStr = null;
        boolean isLocalFile = false;
        switch (checkedId) {
            case R.id.rb_net_video:
                mediaType = LelinkSourceSDK.MEDIA_TYPE_VIDEO;
                mediaTypeStr = "NetVideo";
                PushConfig.getInstance().setNetVideo(url);
                break;
            case R.id.rb_net_music:
                mediaType = LelinkSourceSDK.MEDIA_TYPE_AUDIO;
                PushConfig.getInstance().setNetMusic(url);
                mediaTypeStr = "NetMusic";
                break;
            case R.id.rb_net_picture:
                mediaType = LelinkSourceSDK.MEDIA_TYPE_IMAGE;
                PushConfig.getInstance().setNetPhoto(url);
                mediaTypeStr = "NetPicture";
                break;
            case R.id.rb_local_video:
                mediaType = LelinkSourceSDK.MEDIA_TYPE_VIDEO;
                PushConfig.getInstance().setLocalVideo(url);
                isLocalFile = true;
                mediaTypeStr = "LocalVideo";
                break;
            case R.id.rb_local_music:
                mediaType = LelinkSourceSDK.MEDIA_TYPE_AUDIO;
                PushConfig.getInstance().setLocalMusic(url);
                isLocalFile = true;
                mediaTypeStr = "LocalMusic";
                break;
            case R.id.rb_local_picture:
                mediaType = LelinkSourceSDK.MEDIA_TYPE_IMAGE;
                PushConfig.getInstance().setLocalPhoto(url);
                isLocalFile = true;
                mediaTypeStr = "LocalPicture";
                break;
        }
        Logger.i(TAG, "start play url:" + url + " type:" + mediaTypeStr);
        startPlay(url, mediaType, isLocalFile);
    }

    private void startPlay(String url, int mediaType, boolean isLocalFile) {
        LelinkPlayerInfo lelinkPlayerInfo = new LelinkPlayerInfo();
        if (isLocalFile) {
            lelinkPlayerInfo.setLocalPath(url);
        } else {
            lelinkPlayerInfo.setUrl(url);
        }
        lelinkPlayerInfo.setType(mediaType);
        if (!TextUtils.isEmpty(mPassword)) {
            lelinkPlayerInfo.setCastPwd(mPassword);
        }
        lelinkPlayerInfo.setUrl(url);
        MediaAssetBean mediaAssetBean = new MediaAssetBean();
        mediaAssetBean.setName("测试资源");
        if (mediaType == LelinkSourceSDK.MEDIA_TYPE_AUDIO) {
            // 音乐媒资信息，可不设置
            mediaAssetBean.setAlbum("专辑-xxx");
            mediaAssetBean.setAlbumArtURI("https://img2.baidu.com/it/u=2192265457,2884791613&fm=26&fmt=auto");
            mediaAssetBean.setActor("艺术家-xxx");
            mediaAssetBean.setDirector("创造者-xxx");
        }
        // 非必要参数，部分dlna不返回视频总长度，实现下一集时若需要兼容这种dlna接收端，需设置媒资总长度，单位秒
        mediaAssetBean.setDuration(PushConfig.getInstance().getDuration(url));
        Logger.i(TAG, "startPlay duration:" + mediaAssetBean.getDuration());
        lelinkPlayerInfo.setMediaAsset(mediaAssetBean);
        lelinkPlayerInfo.setLelinkServiceInfo(mSelectInfo);
        Logger.i(TAG, "startPlay deviceName:" + mSelectInfo.getName());
        LelinkSourceSDK.getInstance().startPlayMedia(lelinkPlayerInfo);
        Logger.i(TAG, "startPlay startPlayMedia end:");
    }

    public void showPWDDialog() {
        final EditText editText = new EditText(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle("请输入密码")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface diaLoggerInterface, int i) {
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            DemoApplication.toast("请输入密码");
                            return;
                        }
                        mPassword = editText.getText().toString();
                        startPlayMedia();
                    }
                }).setNegativeButton("取消", null).show();
    }


    /**
     * 设置播放倍率
     */
    private void sendRate() {
        if (!LelinkSourceSDK.getInstance().isSupportRate(mSelectInfo)) {
            DemoApplication.toast("设备不支持设置倍速");
            return;
        }
        String value = mEtRate.getText().toString();
        if (!TextUtils.isEmpty(value)) {
            try {
                float rate = Float.parseFloat(value);
                Logger.i(TAG, "onClick: rate -" + rate);
                if (rate == LelinkSourceSDK.PLAYBACK_SPEED1
                        || rate == LelinkSourceSDK.PLAYBACK_SPEED2
                        || rate == LelinkSourceSDK.PLAYBACK_SPEED3
                        || rate == LelinkSourceSDK.PLAYBACK_SPEED4
                        || rate == LelinkSourceSDK.PLAYBACK_SPEED5
                        || rate == LelinkSourceSDK.PLAYBACK_SPEED6) {
                    LelinkSourceSDK.getInstance().setRate(rate);
                } else {
                    DemoApplication.toast("倍速值无效");
                }

            } catch (Exception e) {
                DemoApplication.toast("倍速值非法");
            }

        } else {
            DemoApplication.toast("请输入倍速值");
        }
    }

    public void resetTempRestrictState() {
        DemoApplication.runOnUI(new Runnable() {
            @Override
            public void run() {
                mSwTempRestrict.setOnCheckedChangeListener(null);
                mSwTempRestrict.setChecked(false);
                mSwTempRestrict.setOnCheckedChangeListener(PushFragment.this);
            }
        });
    }

    private void syncReceiverProperties() {
        LelinkSourceSDK.getInstance().getReceiverProperties(new IReceiverPropertiesCallback() {
            @Override
            public void callback(ReceiverProperties bean) {
                SourceLog.i(TAG, "syncReceiverProperties: " + bean);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bean.isSupport == 1) {
                            rgPlayer.setOnCheckedChangeListener(null);
                            rgPlayMode.setOnCheckedChangeListener(null);
                            rgRotate.setOnCheckedChangeListener(null);
                            if (bean.player < rgPlayer.getChildCount()) {
                                ((RadioButton) rgPlayer.getChildAt(bean.player)).setChecked(true);
                            }
                            if (bean.playMode < rgPlayMode.getChildCount()) {
                                ((RadioButton) rgPlayMode.getChildAt(bean.playMode)).setChecked(true);
                            }
                            if (bean.rotateAngle < rgRotate.getChildCount()) {
                                ((RadioButton) rgRotate.getChildAt(bean.rotateAngle)).setChecked(true);
                            }
                            rgPlayer.setOnCheckedChangeListener(PushFragment.this);
                            rgPlayMode.setOnCheckedChangeListener(PushFragment.this);
                            rgRotate.setOnCheckedChangeListener(PushFragment.this);
                        } else {
                            DemoApplication.toast("接收端不支持该功能");
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.switch_temp_restrict) {
            if (!LelinkSourceSDK.getInstance().isSupportTempRestrict(mSelectInfo)) {
                DemoApplication.toast("设备不支持设置临时独占");
            } else {
                LelinkSourceSDK.getInstance().enableTempRestrict(isChecked);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (!LelinkSourceSDK.getInstance().isSupportSetReceiverProperties(mSelectInfo)) {
            RadioButton rb = (RadioButton) group.getChildAt(0);
            rb.setChecked(true);
            DemoApplication.toast("设置不支持设置接收端播放特性");
            return;
        }
        switch (checkedId) {
            case R.id.player_0:
                LelinkSourceSDK.getInstance().setReceiverProperty(ReceiverPropertyAction.ACTION_RECEIVER_PROPERTY_PLAYER, ReceiverPropertyValue.VALUE_AUTO_PLAYER);
                break;
            case R.id.player_1:
                LelinkSourceSDK.getInstance().setReceiverProperty(ReceiverPropertyAction.ACTION_RECEIVER_PROPERTY_PLAYER, ReceiverPropertyValue.VALUE_SYSTEM_PLAYER);
                break;
            case R.id.player_2:
                LelinkSourceSDK.getInstance().setReceiverProperty(ReceiverPropertyAction.ACTION_RECEIVER_PROPERTY_PLAYER, ReceiverPropertyValue.VALUE_SOFT_DECODE_PLAYER);
                break;
            case R.id.player_3:
                LelinkSourceSDK.getInstance().setReceiverProperty(ReceiverPropertyAction.ACTION_RECEIVER_PROPERTY_PLAYER, ReceiverPropertyValue.VALUE_HARDWARE_DECODE_PLAYER);
                break;
            case R.id.play_mode_0:
                LelinkSourceSDK.getInstance().setReceiverProperty(ReceiverPropertyAction.ACTION_RECEIVER_PROPERTY_PLAY_MODE, ReceiverPropertyValue.VALUE_PLAY_MODE_AUTO);
                break;
            case R.id.play_mode_1:
                LelinkSourceSDK.getInstance().setReceiverProperty(ReceiverPropertyAction.ACTION_RECEIVER_PROPERTY_PLAY_MODE, ReceiverPropertyValue.VALUE_PLAY_MODE_STANDARD);
                break;
            case R.id.play_mode_2:
                LelinkSourceSDK.getInstance().setReceiverProperty(ReceiverPropertyAction.ACTION_RECEIVER_PROPERTY_PLAY_MODE, ReceiverPropertyValue.VALUE__PLAY_MODE_COMPATIBLE);
                break;
            case R.id.rotate_0:
                LelinkSourceSDK.getInstance().setReceiverProperty(ReceiverPropertyAction.ACTION_RECEIVER_PROPERTY_ANGLE, ReceiverPropertyValue.VALUE_ANGLE_DISABLE);
                break;
            case R.id.rotate_1:
                LelinkSourceSDK.getInstance().setReceiverProperty(ReceiverPropertyAction.ACTION_RECEIVER_PROPERTY_ANGLE, ReceiverPropertyValue.VALUE_ANGLE_ENABLE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        CastManager.getInstance().removeListener(mPushListener);
        CastManager.getInstance().removeDaListener(mDaListener);
        super.onDestroyView();

        mBackIv = null;
        mTitleView = null;
        mRadioGroup = null;
        mUrlEditText = null;
        mEtRate = null;
        mEtTrack = null;
        mProgressBar = null;
        mSwTempRestrict = null;
    }
}
