package com.hpplay.sdk.source.test.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.hpplay.sdk.source.api.INewPlayerListener;
import com.hpplay.sdk.source.api.LelinkPlayerInfo;
import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.bean.CastBean;
import com.hpplay.sdk.source.bean.DramaInfoBean;
import com.hpplay.sdk.source.browse.api.IAPI;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.test.DemoApplication;
import com.hpplay.sdk.source.test.IUIUpdateListener;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.config.PushConfig;
import com.hpplay.sdk.source.test.view.mediacontroller.CastController;
import com.hpplay.sdk.source.test.view.mediacontroller.OnUserStopCastListener;
import com.hpplay.sdk.source.test.manager.CastManager;
import com.hpplay.sdk.source.test.manager.DeviceManager;
import com.hpplay.sdk.source.test.view.DemoListView;

import java.util.ArrayList;
import java.util.List;

/**
 * author : DON
 * date   : 5/18/215:23 PM
 * desc   :
 */
public class DramaFragment extends BaseFragment implements View.OnClickListener{
    private final static String TAG = "DramaFragment";
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private static final int SETTING_RATE = 1;
    private static final int SETTING_CATEGORY = 2;

    private Spinner mSpinner;
    private SeekBar mProgressBar;
    private EditText mEdtVideoBegin, mEdtVideoEnd, mEdtPeriod;
    private ImageView mBackIv;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;
    private CastController mMediaController;
    private DemoListView mSettingListView;
    private TextView mRateBtn, mCategoryBtn;
    private SettingAdapter mSettingAdapter;
    private List<String> mRateSettings = new ArrayList<>();
    private List<String> mCategorySettings = new ArrayList<>();
    private List<String> mSettings = new ArrayList<>();

    private int mMediaState = STATE_IDLE;
    private LelinkServiceInfo mSelectServiceInfo;
    private DramaInfoBean[] mDramaInfoArr;
    private DramaInfoBean[] mAppendData;
    private int mPlayIndex = 0;
    private int mCategoryIndex = 0;
    private int mSettingType = 0;
    private BrowseListFragment.OnPlayListener mPlayListener = new BrowseListFragment.OnPlayListener() {
        @Override
        public void onPlay(LelinkServiceInfo info) {
            Logger.w(TAG, "onPlay " + info);
            LelinkSourceSDK.getInstance().connect(info);
        }
    };
    private OnUserStopCastListener mUserStopListener = new OnUserStopCastListener() {
        @Override
        public void onStopCast() {
            LelinkSourceSDK.getInstance().stopPlay();
            updateState(false);
        }
    };

    private IUIUpdateListener mUIListener = new IUIUpdateListener() {
        @Override
        public void onUpdateDevices(List<LelinkServiceInfo> list) {

        }

        @Override
        public void onConnect(LelinkServiceInfo info) {
            DemoApplication.runOnUI(new Runnable() {
                @Override
                public void run() {
                    mSelectServiceInfo = info;
                    cast();
                }
            });

        }

        @Override
        public void onDisconnect(LelinkServiceInfo info) {

        }

        @Override
        public void onNetChanged() {

        }

        @Override
        public void onBindSuccess() {

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
            Logger.i(TAG, "mPushListener onStart:" + bean.dramaID);
            DemoApplication.toast("开始播放");
            updatePlayIndex(bean.dramaID);
        }

        @Override
        public void onPause(CastBean bean) {
            Logger.i(TAG, "mPushListener onPause:" + bean.dramaID);
            DemoApplication.toast("暂停播放");
            updatePlayIndex(bean.dramaID);
        }

        @Override
        public void onCompletion(CastBean bean, int type) {
            Logger.i(TAG, "mPushListener onCompletion:" + bean.dramaID + "/" + type);
            DemoApplication.toast("播放完成");
            updatePlayIndex(bean.dramaID);
            if (type == 0) {
                //最后一集播放完了，退出投屏
                updateState(false);
            } else {
                //type == 1非最后一集播放完
            }
        }

        @Override
        public void onStop(CastBean bean) {
            Logger.i(TAG, "mPushListener onStop:" + bean.dramaID);
            DemoApplication.toast("播放停止");
            updateState(false);
            updatePlayIndex(bean.dramaID);
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
            updateState(false);
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
                case EXTRA_ERROR_PUSH_INVALID_INPUT:
                    text = "无效推送";
                    break;
            }
            if (TextUtils.isEmpty(text)) {
                text = "推送 onError " + what + "/" + extra;
            }
            Logger.i(TAG, "mPushListener onError:" + text);
            DemoApplication.toast(text);
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

    @Override
    public int getLayoutID() {
        return R.layout.f_drama;
    }

    @Override
    public void init(View view) {
        DeviceManager.getInstance().addUIListener(mUIListener);
        CastManager.getInstance().addPlayerListener(mPushListener);
        initView(view);
        initData();
        initMediaPlayer();
        updateVideoState();
    }

    private void updateVideoState() {
        Object videoState = LelinkSourceSDK.getInstance().getOption(IAPI.OPTION_32);
        boolean isVideoCasting = videoState instanceof Integer
                && ((int) videoState == LelinkSourceSDK.VIDEO_LOADING
                || (int) videoState == LelinkSourceSDK.VIDEO_PAUSE
                || (int) videoState == LelinkSourceSDK.VIDEO_PLAYING);
        if (!isVideoCasting) {
            mMediaController.renderLocal();
        }
    }

    private void initView(View view) {
        mBackIv = view.findViewById(R.id.iv_back);
        mBackIv.setOnClickListener(this);
        mSurfaceView = view.findViewById(R.id.drama_surface);
        mEdtVideoBegin = view.findViewById(R.id.edtVideoBegin);
        mEdtVideoEnd = view.findViewById(R.id.edtVideoEnd);
        mEdtPeriod = view.findViewById(R.id.edtPeriod);
        mSurfaceView.setOnClickListener(this);
        mMediaController = view.findViewById(R.id.media_controller);
        mMediaController.setOnUserStopCastListener(mUserStopListener);
        mSettingListView = view.findViewById(R.id.settingListView);
        mSettingAdapter = new SettingAdapter(getActivity(), mSettings);
        mSettingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSettingType == SETTING_RATE) {
                    if (mSelectServiceInfo == null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            PlaybackParams params = mMediaPlayer.getPlaybackParams();
                            params.setSpeed(Float.parseFloat(mRateSettings.get(position)));
                            mMediaPlayer.setPlaybackParams(params);
                        } else {
                            //do nothing
                        }
                    } else {
                        if (LelinkSourceSDK.getInstance().isSupportRate(mSelectServiceInfo)) {
                            LelinkSourceSDK.getInstance().setRate(Float.parseFloat(mRateSettings.get(position)));
                        } else {
                            DemoApplication.toast(mSelectServiceInfo.getName() + "不支持设置倍速，请在电视下载乐播投屏");
                        }
                    }
                } else if (mSettingType == SETTING_CATEGORY) {
                    mCategoryIndex = position;
                    if (mSelectServiceInfo == null) {
                        reopen();
                    } else {
                        if (LelinkSourceSDK.getInstance().isSupportPlayList(mSelectServiceInfo)) {
                            // 播放指定分辨率的视频
                            LelinkSourceSDK.getInstance().playDrama(mDramaInfoArr[mPlayIndex].urls[mCategoryIndex].id);
                        } else {
                            DemoApplication.toast(mSelectServiceInfo.getName() + "不支持剧集推送，请在电视下载乐播投屏");
                        }
                    }
                }
                mSettingListView.setVisibility(View.GONE);
            }
        });
        mSettingListView.setAdapter(mSettingAdapter);
        view.findViewById(R.id.castBtn).setOnClickListener(this);
        view.findViewById(R.id.drama_pre).setOnClickListener(this);
        view.findViewById(R.id.drama_next).setOnClickListener(this);
        view.findViewById(R.id.btn_pause).setOnClickListener(this);
        view.findViewById(R.id.btn_resume).setOnClickListener(this);
        view.findViewById(R.id.btn_volume_up).setOnClickListener(this);
        view.findViewById(R.id.btn_volume_down).setOnClickListener(this);
        view.findViewById(R.id.append_list).setOnClickListener(this);
        view.findViewById(R.id.clear_list).setOnClickListener(this);
        mSpinner = view.findViewById(R.id.spinner);
        mProgressBar = view.findViewById(R.id.seekbar_progress);
        mRateBtn = view.findViewById(R.id.rateBtn);
        mRateBtn.setOnClickListener(this);
        mCategoryBtn = view.findViewById(R.id.categoryBtn);
        mCategoryBtn.setOnClickListener(this);
        mProgressBar.setOnSeekBarChangeListener(mProgressChangeListener);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeDrama(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData() {
        mRateSettings.clear();
        mRateSettings.add("0.5");
        mRateSettings.add("0.75");
        mRateSettings.add("1.0");
        mRateSettings.add("1.25");
        mRateSettings.add("1.5");
        mRateSettings.add("2.0");
        mCategorySettings.clear();
        mCategorySettings.add("高清");
        mCategorySettings.add("标清");

        String[] drama_1080 = PushConfig.getInstance().getDrama_1080();
        String[] drama_720 = PushConfig.getInstance().getDrama_720();
        DramaInfoBean[] initDramaArr = new DramaInfoBean[drama_1080.length];
        for (int i = 0; i < drama_1080.length; i++) {
            DramaInfoBean dramaInfo = new DramaInfoBean();
            dramaInfo.name = "第" + (i + 1) + "集";

            DramaInfoBean.UrlBean url_1080 = new DramaInfoBean.UrlBean();
            url_1080.category = DramaInfoBean.CATEGORY_HIGH;
            url_1080.url = drama_1080[i];
            url_1080.id = "1080_" + (i + 1);
            url_1080.width = 1920;
            url_1080.height = 1080;

            DramaInfoBean.UrlBean url_720 = new DramaInfoBean.UrlBean();
            url_720.category = DramaInfoBean.CATEGORY_STD;
            url_720.url = drama_720[i];
            url_720.id = "720_" + (i + 1);
            url_720.width = 1280;
            url_720.height = 720;

            DramaInfoBean.UrlBean[] beans = new DramaInfoBean.UrlBean[2];
            beans[0] = url_1080;
            beans[1] = url_720;
            dramaInfo.urls = beans;
            initDramaArr[i] = dramaInfo;
        }
        mDramaInfoArr = initDramaArr;
        mPlayIndex = 0;
        updateSpinner();
    }

    private void updateSpinner() {
        ArrayList<String> list = new ArrayList<>();
        for (DramaInfoBean dramaInfoBean : mDramaInfoArr) {
            list.add(dramaInfoBean.name);
        }
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(R.layout.item_dropdown);
        mSpinner.setPrompt("本地剧集列表(共" + mDramaInfoArr.length + "集)");
        mSpinner.setAdapter(adapter);
        updateSpinnerSelect();
    }

    private void updateSpinnerSelect() {
        DemoApplication.runOnUI(() -> {
            int selectIndex = mPlayIndex;
            if (selectIndex > mDramaInfoArr.length - 1 || selectIndex < 0) {
                selectIndex = 0;
            }
            mSpinner.setSelection(selectIndex);
        });
    }

    private void updateState(boolean isCasting) {
        DemoApplication.runOnUI(() -> {
            if (isCasting) {
                try {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                } catch (Exception e) {
                    Logger.w(TAG, e);
                }
                mMediaPlayer = null;
                mMediaState = STATE_IDLE;
                mMediaController.renderRemote();
                mMediaController.prepare();
            } else {
                mSelectServiceInfo = null;
                toggleVideo();
            }
        });
    }

    private void updatePlayIndex(String dramaId) {
        Logger.i(TAG, "updatePlayIndex dramaId:" + dramaId);
        if (TextUtils.isEmpty(dramaId)) {
            return;
        }
        for (int i = 0; i < mDramaInfoArr.length; i++) {
            DramaInfoBean.UrlBean[] urls = mDramaInfoArr[i].urls;
            for (int j = 0; j < urls.length; j++) {
                if (dramaId.equals(urls[j].id)) {
                    mPlayIndex = i;
                    Logger.i(TAG, "updatePlayIndex mPlayIndex:" + mPlayIndex);
                    updateSpinnerSelect();
                    break;
                }
            }
        }
    }

    public void setSelectInfo(LelinkServiceInfo info) {
        mSelectServiceInfo = info;
        if (info != null) {
            LelinkSourceSDK.getInstance().connect(mSelectServiceInfo);
        }
    }

    private void reopen() {
        stop();
        toggleVideo();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaState = STATE_PREPARED;
                start();
            }
        });
        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        try {
            mMediaPlayer.setDataSource(mDramaInfoArr[mPlayIndex].urls[mCategoryIndex].url);
        } catch (Exception e) {
            Logger.w(TAG, e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this)
                        .commitAllowingStateLoss();
                break;
            case R.id.drama_surface:
                Logger.w(TAG, "click drama_surface");
                toggleVideo();
                break;
            case R.id.castBtn:
                Logger.w(TAG, "click castBtn");
                try {
                    Fragment browseFragment = getActivity().getSupportFragmentManager().findFragmentByTag("browser_fm");
                    if (browseFragment != null && browseFragment.isAdded()) {
                        return;
                    }
                    if (browseFragment == null) {
                        browseFragment = new BrowseListFragment();
                        ((BrowseListFragment) browseFragment).setOnPlayListener(mPlayListener);
                    }
                    getChildFragmentManager().beginTransaction()
                            .add(R.id.drama_container, browseFragment, "browser_fm")
                            .addToBackStack("browser_fm")
                            .commitAllowingStateLoss();
                } catch (Exception e) {
                    Logger.w(TAG, e);
                }

                break;
            case R.id.drama_pre:
                if (mPlayIndex == 0) {
                    DemoApplication.toast("已经没有上一集啦");
                    return;
                }
                mPlayIndex--;
                updateSpinnerSelect();
                if (mSelectServiceInfo != null) {
                    if (checkSinkSupport()) {
                        LelinkSourceSDK.getInstance().playPreDrama();
                    }
                } else {
                    reopen();
                }
                break;
            case R.id.drama_next:
                if (mDramaInfoArr != null && mPlayIndex == mDramaInfoArr.length - 1) {
                    DemoApplication.toast("已经没有下一集啦");
                    return;
                }
                mPlayIndex++;
                updateSpinnerSelect();
                if (mSelectServiceInfo != null) {
                    if (checkSinkSupport()) {
                        LelinkSourceSDK.getInstance().playNextDrama();
                    }
                } else {
                    reopen();
                }
                break;
            case R.id.rateBtn:
                if (mSettingListView.getVisibility() == View.VISIBLE && mSettingType == SETTING_RATE) {
                    mSettingListView.setVisibility(View.GONE);
                } else {
                    mSettingType = SETTING_RATE;
                    mSettings.clear();
                    mSettings.addAll(mRateSettings);
                    Logger.w(TAG, "mSettings:" + mSettings.size());
                    mSettingAdapter.notifyDataSetChanged();
                    mSettingListView.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSettingListView.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.ALIGN_LEFT, mRateBtn.getId());
                    mSettingListView.setLayoutParams(layoutParams);
                }
                break;
            case R.id.categoryBtn:
                if (mSettingListView.getVisibility() == View.VISIBLE && mSettingType == SETTING_CATEGORY) {
                    mSettingListView.setVisibility(View.GONE);
                } else {
                    mSettingType = SETTING_CATEGORY;
                    mSettings.clear();
                    mSettings.addAll(mCategorySettings);
                    mSettingAdapter.notifyDataSetChanged();
                    mSettingListView.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) mSettingListView.getLayoutParams();
                    layoutParams1.addRule(RelativeLayout.ALIGN_LEFT, mCategoryBtn.getId());
                    mSettingListView.setLayoutParams(layoutParams1);
                }
                break;
            case R.id.btn_pause:
                LelinkSourceSDK.getInstance().pause();
                break;
            case R.id.btn_resume:
                LelinkSourceSDK.getInstance().resume();
                break;
            case R.id.btn_volume_up:
                LelinkSourceSDK.getInstance().addVolume();
                break;
            case R.id.btn_volume_down:
                LelinkSourceSDK.getInstance().subVolume();
                break;
            case R.id.append_list:
                if (checkSinkSupport()) {
                    showAppendListDialog();
                }
                break;
            case R.id.clear_list:
                if (checkSinkSupport()) {
                    LelinkSourceSDK.getInstance().clearPlayList();
                }
                break;
        }
    }

    private boolean checkSinkSupport() {
        if (mSelectServiceInfo == null) {
            DemoApplication.toast("请先选择接收端设备");
            return false;
        }
        if (!LelinkSourceSDK.getInstance().isSupportPlayList(mSelectServiceInfo)) {
            // 播放指定分辨率的视频
            DemoApplication.toast(mSelectServiceInfo.getName() + "不支持剧集推送，请在电视下载乐播投屏");
            return false;
        }
        return true;
    }

    private void showAppendListDialog() {
        mAppendData = new DramaInfoBean[0];

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_append_play_list, null, false);
        dialog.setView(view);
        TextView tvAddedNum = view.findViewById(R.id.tvAddedNum);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtHighUrl = view.findViewById(R.id.edtHighUrl);
        EditText edtStdUrl = view.findViewById(R.id.edtStdUrl);
        EditText edtNum = view.findViewById(R.id.edtNum);
        AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        view.findViewById(R.id.cancle).setOnClickListener(v -> alertDialog.dismiss());
        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInput(edtName, edtHighUrl, edtStdUrl, edtNum)) {
                    return;
                }
                addDramaInfo(edtName, edtHighUrl, edtStdUrl, edtNum);
                tvAddedNum.setText(String.valueOf(mAppendData.length));
                updateSpinner();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInput(edtName, edtHighUrl, edtStdUrl, edtNum)) {
                    return;
                }
                alertDialog.dismiss();
                addDramaInfo(edtName, edtHighUrl, edtStdUrl, edtNum);

                //追加
                int periodAppend = getPeriod();
                int[] durationArr = skipVideoBeginAndEnd();
                LelinkSourceSDK.getInstance().appendPlayList(mAppendData, periodAppend, durationArr[0], durationArr[1]);
                updateSpinner();
            }
        });
        alertDialog.show();
    }

    private void addDramaInfo(EditText edtName, EditText edtHighUrl, EditText edtStdUrl, EditText edtNum) {
        String name = edtName.getText().toString();
        String highUrl = edtHighUrl.getText().toString();
        String stdUrl = edtStdUrl.getText().toString();
        int num = Integer.parseInt(edtNum.getText().toString());

        DramaInfoBean[] resultData = new DramaInfoBean[num];
        int currentId = mDramaInfoArr.length;
        for (int i = 0; i < num; i++) {
            DramaInfoBean dramaInfo = new DramaInfoBean();
            dramaInfo.name = name;
            if (num > 1) {
                dramaInfo.name = name + i;
            }

            int beanSize = 0;
            DramaInfoBean.UrlBean url_1080 = null;
            DramaInfoBean.UrlBean url_720 = null;
            if (!TextUtils.isEmpty(highUrl)) {
                beanSize++;
                url_1080 = new DramaInfoBean.UrlBean();
                url_1080.category = DramaInfoBean.CATEGORY_HIGH;
                url_1080.url = highUrl;
                url_1080.id = "1080_" + (currentId + i + 1);
                url_1080.width = 1920;
                url_1080.height = 1080;
            }

            if (!TextUtils.isEmpty(stdUrl)) {
                beanSize++;
                url_720 = new DramaInfoBean.UrlBean();
                url_720.category = DramaInfoBean.CATEGORY_STD;
                url_720.url = stdUrl;
                url_720.id = "720_" + (currentId + i + 1);
                url_720.width = 1280;
                url_720.height = 720;
            }

            DramaInfoBean.UrlBean[] beans = new DramaInfoBean.UrlBean[beanSize];
            int index = 0;
            if (url_1080 != null) {
                beans[index++] = url_1080;
            }
            if (url_720 != null) {
                beans[index++] = url_720;
            }
            dramaInfo.urls = beans;
            resultData[i] = dramaInfo;
        }
        DramaInfoBean[] newList = new DramaInfoBean[resultData.length + mDramaInfoArr.length];
        System.arraycopy(mDramaInfoArr, 0, newList, 0, mDramaInfoArr.length);
        System.arraycopy(resultData, 0, newList, mDramaInfoArr.length, resultData.length);
        mDramaInfoArr = newList;

        DramaInfoBean[] newAppendList = new DramaInfoBean[resultData.length + mAppendData.length];
        System.arraycopy(mAppendData, 0, newAppendList, 0, mAppendData.length);
        System.arraycopy(resultData, 0, newAppendList, mAppendData.length, resultData.length);
        mAppendData = newAppendList;
    }

    private boolean checkInput(EditText edtName, EditText edtHighUrl, EditText edtStdUrl, EditText edtNum) {
//        if (TextUtils.isEmpty(edtName.getText().toString())) {
//            DemoApplication.toast("请输入名字");
//            return false;
//        }
//
        if (TextUtils.isEmpty(edtHighUrl.getText().toString())
                && TextUtils.isEmpty(edtStdUrl.getText().toString())) {
            DemoApplication.toast("请至少输入一个url");
            return false;
        }

        if (TextUtils.isEmpty(edtNum.getText().toString())) {
            DemoApplication.toast("请输入剧集数量");
            return false;
        }

        return true;
    }

    private int getPeriod() {
        String str = mEdtPeriod.getText().toString();
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int period = 0;
        try {
            period = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            Logger.w(TAG, e.getMessage());
        }
        return period;
    }

    private void toggleVideo() {
        Logger.w(TAG, "toggleVideo mMediaState：" + mMediaState);

        if (mMediaPlayer == null) {
            initMediaPlayer();
        }
        switch (mMediaState) {
            case STATE_IDLE:
                if (mSelectServiceInfo != null) {
                    cast();
                    break;
                }
                try {
                    mMediaPlayer.setSurface(mSurfaceView.getHolder().getSurface());
                    mMediaPlayer.prepareAsync();
                    mMediaState = STATE_PREPARING;
                    mMediaController.renderLocal();
                    mMediaController.prepare();
                } catch (Exception e) {
                    Logger.w(TAG, "toggleVideo " + e.getMessage());
                }
                break;
            case STATE_PLAYING:
                pause();
                break;
            case STATE_PREPARED:
            case STATE_PAUSED:
                start();
                break;
            default:
                break;
        }
    }

    private void start() {
        mMediaPlayer.start();
        mMediaState = STATE_PLAYING;
        mMediaController.start();
    }

    private void pause() {
        mMediaPlayer.pause();
        mMediaState = STATE_PAUSED;
        mMediaController.pause();
    }

    private void stop() {
        try {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        } catch (Exception e) {
            Logger.w(TAG, e);
        }
        mMediaPlayer = null;
        mMediaState = STATE_IDLE;
        mMediaController.stop();
    }

    private void cast() {
        Logger.w(TAG, "cast :" + mPlayIndex);
        if (mDramaInfoArr == null || mDramaInfoArr.length <= 0) {
            DemoApplication.runOnUI(() -> DemoApplication.toast("列表为空"));
            return;
        }
        if (!checkSinkSupport()) {
            return;
        }
        updateState(true);
        int period = getPeriod();
        int[] durationArr = skipVideoBeginAndEnd();
        LelinkPlayerInfo playerInfo = new LelinkPlayerInfo();
        playerInfo.setType(LelinkSourceSDK.MEDIA_TYPE_VIDEO);
        playerInfo.setLelinkServiceInfo(mSelectServiceInfo);
        playerInfo.setPlayList(mDramaInfoArr, mDramaInfoArr[mPlayIndex].urls[0].id,
                period, durationArr[0], durationArr[1]);
        Logger.w(TAG, "cast id:" + playerInfo.getDramaID());
        LelinkSourceSDK.getInstance().startPlayMedia(playerInfo);
    }

    private void changeDrama(int index) {
        if (mPlayIndex == index) {
            return;
        }
        if (mSelectServiceInfo == null) {
            mPlayIndex = index;
            reopen();
            return;
        }
        if (mDramaInfoArr == null || index < 0 || index >= mDramaInfoArr.length) {
            return;
        }
        if (!checkSinkSupport()) {
            return;
        }
        mPlayIndex = index;
        String dramaID = mDramaInfoArr[index].urls[mCategoryIndex].id;
        Logger.i(TAG, "changeDrama:" + dramaID);
        LelinkSourceSDK.getInstance().playDrama(dramaID);
    }

    private int[] skipVideoBeginAndEnd() {
        try {
            String beginStr = mEdtVideoBegin.getText().toString();
            String endStr = mEdtVideoEnd.getText().toString();
            int begin = -1;
            int end = -1;
            if (!TextUtils.isEmpty(beginStr)) {
                begin = Integer.parseInt(beginStr);
            }
            if (!TextUtils.isEmpty(endStr)) {
                end = Integer.parseInt(endStr);
            }
            Logger.i(TAG, "setSkipVideoBeginAndEnd:" + begin + "/" + end);
            return new int[]{begin, end};
        } catch (NumberFormatException e) {
            Logger.w(TAG, "setSkipVideoBeginAndEnd:" + e.getMessage());
        }
        return new int[]{-1, -1};
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
                        //TODO impl here, cast with psd again
                        //mPassword = editText.getText().toString();
                        //startPlayMedia();
                    }
                }).setNegativeButton("取消", null).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.w(TAG, "onStop");
        stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.w(TAG, "onDestroyView");
        CastManager.getInstance().removeListener(mPushListener);
        DeviceManager.getInstance().removeUIListener(mUIListener);

        mSpinner = null;
        mProgressBar = null;
        mEdtVideoBegin = null;
        mEdtVideoEnd = null;
        mEdtPeriod = null;
        mBackIv = null;
        mSurfaceView = null;
        mMediaController = null;
        mSettingListView = null;
        mRateBtn = null;
        mCategoryBtn = null;
        mSettingAdapter = null;
    }
}
