package com.hpplay.sdk.source.test;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.hpplay.sdk.source.api.BleState;
import com.hpplay.sdk.source.api.IBleBrowseStateListener;
import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.bean.BrowserConfigBean;
import com.hpplay.sdk.source.bean.HistoryConfigBean;
import com.hpplay.sdk.source.browse.api.IAPI;
import com.hpplay.sdk.source.browse.api.IServiceInfoParseListener;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.log.SourceLog;
import com.hpplay.sdk.source.test.adapter.BrowseAdapter;
import com.hpplay.sdk.source.test.adapter.MyDiffCallback;
import com.hpplay.sdk.source.test.constants.Constant;
import com.hpplay.sdk.source.test.fragment.BaseFragment;
import com.hpplay.sdk.source.test.fragment.DeviceFragment;
import com.hpplay.sdk.source.test.fragment.DramaFragment;
import com.hpplay.sdk.source.test.fragment.MirrorFragment;
import com.hpplay.sdk.source.test.fragment.PushFragment;
import com.hpplay.sdk.source.test.manager.CastManager;
import com.hpplay.sdk.source.test.manager.DeviceManager;
import com.hpplay.sdk.source.test.utils.CameraPermissionCompat;
import com.hpplay.sdk.source.test.utils.Utils;
import com.hpplay.sdk.source.utils.LeboUtil;

import java.util.List;

public class APICastFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "APICastActivity";
    private static final int REQUEST_MUST_PERMISSION = 100;
    private static final int REQUEST_CAMERA_PERMISSION = 2;
    private static String sPhone = "";
    boolean flag = false;
    boolean isReport = false;
    private View mRootView;
    private TextView mSDKVersion, mPhoneWifi, mPhoneIP;
    private RecyclerView mBrowseRecyclerView;
    private BrowseAdapter mBrowseAdapter;

    private NetworkReceiver mNetworkReceiver;
    private Button mPushBtn, mMirrorBtn;
    private Button mConnectBtn, mDisconnectBtn;
    private CheckBox mDlnaCheckBox;
    private CheckBox mHisCheckBox;
    private CheckBox mReportCheckBox;
    private CheckBox mBleCheckBox;

    private enum TypeEnum {
        BROWSE_INTRANET,
        BROWSE_HISTORY,
        DEVICE_REPORT
    }

    private final IUIUpdateListener mUIListener = new IUIUpdateListener() {
        @Override
        public void onUpdateDevices(List<LelinkServiceInfo> list) {
            updateBrowseAdapter(list);
        }

        @Override
        public void onConnect(LelinkServiceInfo info) {
        }

        @Override
        public void onDisconnect(LelinkServiceInfo info) {
        }

        @Override
        public void onNetChanged() {
            refreshWifiName();
        }

        @Override
        public void onBindSuccess() {
        }
    };


    private final BrowseAdapter.OnSelectListener mOnSelectListener = hasSelected -> {
        mPushBtn.setEnabled(hasSelected);
        mMirrorBtn.setEnabled(hasSelected);
        mConnectBtn.setEnabled(hasSelected);
        mDisconnectBtn.setEnabled(hasSelected);
    };

    private static IBleBrowseStateListener mBleBrowseStateListener = new IBleBrowseStateListener() {
        @Override
        public void onBrowseResult(BleState bleState) {
            if (bleState == BleState.BLE_ADVERTISE_BT_TURNED_OFF) {
                DemoApplication.toast("蓝牙未开启");
            }
        }
    };

    @Override
    public int getLayoutID() {
        return R.layout.f_api_cast;
    }

    @Override
    public void init(View view) {
        mRootView = view;
        initSDK();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Logger.w(TAG, "onRequestPermissionsResult ignore ");
            getActivity().finish();
            return;
        }
        if (requestCode == CameraPermissionCompat.REQUEST_CODE_CAMERA) {
            Logger.w(TAG, "onRequestPermissionsResult ignore camera");
            startCaptureActivity();
            return;
        }
        initSDK();
    }

    private void initSDK() {
        DeviceManager connectManager = DeviceManager.getInstance();
        connectManager.addUIListener(mUIListener);
        //sdk初始化
        LelinkSourceSDK.getInstance()
                .setBindSdkListener(connectManager.getBindListener())
                .setBrowseResultListener(connectManager.getBrowseListener())
                .setConnectListener(connectManager.getConnectListener())
                .setNewPlayListener(CastManager.getInstance().getPlayerListener())
                .setDaPlayListener(CastManager.getInstance().getDaPlayerListener())
                //FIXME WARN: 这里替换为您申请的AppID & AppSecret，build.gradle替换为您的应用包名
                .setSdkInitInfo(getActivity().getApplicationContext(), getString(R.string.app_id), getString(R.string.app_secret))
                .bindSdk();
        LelinkSourceSDK.getInstance().setOption(IAPI.OPTION_4, connectManager.getAuthListener());
        initView();
        mNetworkReceiver = new NetworkReceiver();
        mNetworkReceiver.setUIListener(mUIListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.WIFI_AP_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mNetworkReceiver, intentFilter);
        LelinkSourceSDK.getInstance().setBleBrowseStateListener(mBleBrowseStateListener);
    }

    private void initView() {
        mDlnaCheckBox = mRootView.findViewById(R.id.browse_dlna);
        mHisCheckBox = mRootView.findViewById(R.id.browse_history);
        mReportCheckBox = mRootView.findViewById(R.id.report_data);
        mBleCheckBox = mRootView.findViewById(R.id.browse_ble);
        mReportCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isReport = isChecked;
                if (isChecked) {
                    showEnterOperationType(TypeEnum.DEVICE_REPORT);
                    return;
                }
                LelinkSourceSDK.getInstance().setConnectDeviceReport(false);
            }
        });
        mPhoneWifi = mRootView.findViewById(R.id.tv_wifi);
        mPhoneIP = mRootView.findViewById(R.id.tv_ip);
        refreshWifiName();
        mPushBtn = mRootView.findViewById(R.id.btn_push);
        mPushBtn.setEnabled(false);
        mMirrorBtn = mRootView.findViewById(R.id.btn_mirror);
        mMirrorBtn.setEnabled(false);
        mConnectBtn = mRootView.findViewById(R.id.btn_connect);
        mConnectBtn.setEnabled(false);
        mDisconnectBtn = mRootView.findViewById(R.id.btn_disconnect);
        mDisconnectBtn.setEnabled(false);
        mBrowseRecyclerView = mRootView.findViewById(R.id.recycler_browse);
        mBrowseAdapter = new BrowseAdapter(getActivity());
        mBrowseAdapter.setOnSelectListener(mOnSelectListener);
        mBrowseRecyclerView.setAdapter(mBrowseAdapter);
        mBrowseAdapter.setOnItemClickListener((position, info) -> {
            mBrowseAdapter.setSelectServiceInfo(info);
        });
        mSDKVersion = mRootView.findViewById(R.id.tv_version);
        mSDKVersion.setText("SDK:"
                + com.hpplay.sdk.source.api.BuildConfig.BUILD_TYPE
                + "-" + com.hpplay.sdk.source.api.BuildConfig.VERSION_NAME
                + "    " + com.hpplay.sdk.source.api.BuildConfig.BUILD_TIME);
        mRootView.findViewById(R.id.btn_browse).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_stop_browse).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_qrcode).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_pincode_connect).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_connect).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_disconnect).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_push).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_drama).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_mirror).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_history).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_verifySHA256).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_device).setOnClickListener(this);
    }

    public void showScreenCodeDialog() {
        final EditText editText = new EditText(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle("请输入投屏码")
                .setView(editText)
                .setPositiveButton("确定", (diaLoggerInterface, i) -> {
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        DemoApplication.toast("请输入屏幕码");
                        return;
                    }
                    getPinCodeDevice(editText.getText().toString());
                }).setNegativeButton("取消", null).show();
    }

    public void showVerifySHA256Dialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_verify, null);
        EditText requestEt = view.findViewById(R.id.et_phone);
        EditText resultEt = view.findViewById(R.id.et_encrptid);
        view.findViewById(R.id.btn_H256).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(requestEt.getText().toString())) {
                    DemoApplication.toast("请输入正确手机号");
                    return;
                }
                String result = LeboUtil.anonymizeBySHA256For60Bits(requestEt.getText().toString());
                SourceLog.i(TAG, "SHA256: " + result);
                resultEt.setText(result);
                resultEt.setSelection(result.length());
            }
        });
        view.findViewById(R.id.btn_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Label", resultEt.getText().toString());
                cm.setPrimaryClip(clipData);
                DemoApplication.toast("已复制到粘贴板");
            }
        });
        new AlertDialog.Builder(getActivity()).setTitle("请输入明文手机号")
                .setView(view)
                .setPositiveButton("关闭", null).show();
    }

    public void getPinCodeDevice(String castCode) {
        LelinkSourceSDK.getInstance().addPinCodeToLelinkServiceInfo(castCode, new IServiceInfoParseListener() {
            @Override
            public void onParseResult(int resultCode, LelinkServiceInfo lelinkServiceInfo) {
                if (resultCode == IServiceInfoParseListener.PARSE_SUCCESS) {
                    DemoApplication.toast("pin码解析成功");
                } else {
                    DemoApplication.toast("pin码解析失败");
                }
            }
        });
    }

    public void getQRCodeDevice(String qrUrl) {
        Logger.i(TAG, "getQRCodeDevice: " + qrUrl);
        LelinkSourceSDK.getInstance().addQRCodeToLelinkServiceInfo(qrUrl, new IServiceInfoParseListener() {
            @Override
            public void onParseResult(int resultCode, LelinkServiceInfo lelinkServiceInfo) {
                if (resultCode == IServiceInfoParseListener.PARSE_SUCCESS) {
                    DemoApplication.toast("扫码成功");
                } else {
                    DemoApplication.toast("扫码失败");
                }
            }
        });
    }

    private void startCaptureActivity() {
        CameraPermissionCompat.checkCameraPermission(getActivity(), granted -> {
            Logger.i(TAG, "checkCameraPermission:" + granted);
            if (granted) {
                // 允许，打开二维码
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CAMERA_PERMISSION);
            } else {
                // 若没有授权，会弹出一个对话框（这个对话框是系统的，开发者不能自己定制），用户选择是否授权应用使用系统权限
            }
        });
    }

    @Override
    public void onClick(View v) {
        LelinkServiceInfo selectInfo = mBrowseAdapter.getSelectServiceInfo();
        switch (v.getId()) {
            case R.id.btn_browse:
                if (mHisCheckBox.isChecked()) {
                    showEnterOperationType(TypeEnum.BROWSE_INTRANET);
                    return;
                }

                mBrowseAdapter.clearDatas();
                BrowserConfigBean bean = new BrowserConfigBean();
                bean.useLelink = true;
                bean.useDlna = mDlnaCheckBox.isChecked();
                bean.useBLE = mBleCheckBox.isChecked();
                LelinkSourceSDK.getInstance().startBrowse(bean);
                break;
            case R.id.btn_stop_browse:
                LelinkSourceSDK.getInstance().stopBrowse();
                break;
            case R.id.btn_history:
                showEnterOperationType(TypeEnum.BROWSE_HISTORY);
                break;
            case R.id.btn_verifySHA256:
                showVerifySHA256Dialog();
                break;
            case R.id.btn_qrcode:
                startCaptureActivity();
                break;
            case R.id.btn_pincode_connect:
                showScreenCodeDialog();
                break;
            case R.id.btn_push:
                enterPushFragment(selectInfo);
                break;
            case R.id.btn_drama:
                try {
                    Fragment dramaFragment = getActivity().getSupportFragmentManager().findFragmentByTag("drama_fm");
                    if (dramaFragment != null && dramaFragment.isAdded()) {
                        ((DramaFragment) dramaFragment).setSelectInfo(selectInfo);
                        return;
                    }
                    if (dramaFragment == null) {
                        dramaFragment = new DramaFragment();
                    }
                    ((DramaFragment) dramaFragment).setSelectInfo(selectInfo);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, dramaFragment, "drama_fm")
                            .addToBackStack("drama")
                            .commitAllowingStateLoss();
                } catch (Exception e) {
                    Logger.w(TAG, e);
                }
                break;
            case R.id.btn_mirror:
                enterMirrorFragment(selectInfo);
                break;
            case R.id.btn_connect:
                if (selectInfo == null) {
                    DemoApplication.toast("请先选择接收端设备");
                    return;
                }
                LelinkSourceSDK.getInstance().connect(selectInfo);
                break;
            case R.id.btn_disconnect:
                if (selectInfo == null) {
                    DemoApplication.toast("请先选择接收端设备");
                    return;
                }
                LelinkSourceSDK.getInstance().disconnect(selectInfo);
                break;
            case R.id.btn_device:
                try {
                    Fragment deviceFragment = getActivity().getSupportFragmentManager().findFragmentByTag("device_fm");
                    if (deviceFragment != null && deviceFragment.isAdded()) {
                        return;
                    }
                    if (deviceFragment == null) {
                        deviceFragment = new DeviceFragment();
                    }
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, deviceFragment, "device_fm")
                            .addToBackStack("device")
                            .commitAllowingStateLoss();
                } catch (Exception e) {
                    Logger.w(TAG, e);
                }
                break;
        }
    }

    private void showEnterOperationType(TypeEnum type) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_browse, null);
        EditText phoneEt = view.findViewById(R.id.et_phone);
        EditText encryptEt = view.findViewById(R.id.et_encrptid);
        new AlertDialog.Builder(getContext())
                .setTitle("请输入手机号")
                .setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String encrytId = encryptEt.getText().toString();
                        String phoneId = phoneEt.getText().toString();
                        if (TextUtils.isEmpty(encrytId.trim())
                                && TextUtils.isEmpty(phoneId.trim())) {
                            DemoApplication.toast("请输入手机号");
                            if (TypeEnum.DEVICE_REPORT.equals(type)) {
                                mReportCheckBox.setChecked(false);
                            }
                            return;
                        }

                        if (TypeEnum.BROWSE_INTRANET.equals(type)) {
                            mBrowseAdapter.clearDatas();
                            // browse device contains history
                            BrowserConfigBean bean = new BrowserConfigBean();
                            bean.useLelink = true;
                            bean.useDlna = mDlnaCheckBox.isChecked();
                            bean.useHistory = mHisCheckBox.isChecked();
                            bean.encryptNumberId = encrytId;
                            bean.numberId = phoneId;
                            LelinkSourceSDK.getInstance().startBrowse(bean);
                        } else if (TypeEnum.BROWSE_HISTORY.equals(type)) {
                            // only browse history
                            mBrowseAdapter.clearDatas();

                            BrowserConfigBean bean = new BrowserConfigBean();
                            bean.encryptNumberId = encrytId;
                            bean.numberId = phoneId;
                            LelinkSourceSDK.getInstance().startBrowseHistory(bean);
                        } else {
                            HistoryConfigBean bean = new HistoryConfigBean();
                            bean.isReport = isReport;
                            bean.numberId = phoneId;
                            bean.encryptNumberId = encrytId;
                            LelinkSourceSDK.getInstance().setConnectDeviceReport(bean);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TypeEnum.DEVICE_REPORT.equals(type)) {
                    mReportCheckBox.setChecked(false);
                }
            }
        }).setCancelable(false).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (REQUEST_CAMERA_PERMISSION == requestCode) {
            String scanResult = data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
            Logger.i(TAG, "onActivityResult url:" + scanResult);
            getQRCodeDevice(scanResult);
        }
    }

    private void updateBrowseAdapter(final List<LelinkServiceInfo> infos) {

        DemoApplication.runOnUI(new Runnable() {
            @Override
            public void run() {
                List<LelinkServiceInfo> oldData = mBrowseAdapter.getDatas();
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffCallback(oldData, infos));
                mBrowseAdapter.updateDatas(infos);
                diffResult.dispatchUpdatesTo(mBrowseAdapter);
            }
        });
    }

    public void refreshWifiName() {
        mPhoneWifi.setText("WiFi:" + Utils.getNetWorkName(getActivity()));
        mPhoneIP.setText(Utils.getIP(getActivity()));
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.w(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.w(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.w(TAG, "onDestroy");
        LelinkSourceSDK.getInstance().stopBrowse();
        if (mNetworkReceiver != null) {
            getActivity().unregisterReceiver(mNetworkReceiver);
            mNetworkReceiver = null;
        }
    }

}
