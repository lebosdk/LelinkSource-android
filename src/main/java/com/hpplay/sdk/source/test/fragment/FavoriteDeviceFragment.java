package com.hpplay.sdk.source.test.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.hpplay.sdk.source.api.DeviceListenerConstant;
import com.hpplay.sdk.source.api.IFavoriteDeviceListener;
import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.test.DemoApplication;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.adapter.BrowseAdapter;
import com.hpplay.sdk.source.test.adapter.DeviceAdapter;
import com.hpplay.sdk.source.test.manager.DeviceManager;

import java.util.List;

public class FavoriteDeviceFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "FavoriteDeviceFragment";

    private Spinner mSpinnerOnline, mSpinnerNetType;
    private Button mBtnRemove, mBtnSetAlias, mBtnPush, mBtnMirror;
    private RecyclerView mRecyclerView;
    private DeviceAdapter mAdapter;

    private IFavoriteDeviceListener mListener = new IFavoriteDeviceListener() {
        @Override
        public void onAddDevice(int status, int errorCode) {
            Logger.i(TAG, "onAddDevice " + status + " / " + errorCode);
            if(status == DeviceListenerConstant.STATUS_SUCCESS) {
                DemoApplication.toast("收藏设备成功");
                DemoApplication.runOnUI(() -> getDeviceList());
            } else {
                DemoApplication.toast("收藏设备失败:" + errorCode + DeviceFragment.getDeviceErrorMsg(errorCode));
            }
        }

        @Override
        public void onRemoveDevice(int status, int errorCode) {
            Logger.i(TAG, "onRemoveDevice " + status + " / " + errorCode);
            if(status == DeviceListenerConstant.STATUS_SUCCESS) {
                DemoApplication.toast("删除设备成功");
                DemoApplication.runOnUI(() -> getDeviceList());
            } else {
                DemoApplication.toast("删除设备失败:" + errorCode + DeviceFragment.getDeviceErrorMsg(errorCode));
            }
        }

        @Override
        public void onSetDeviceAlias(int status, int errorCode) {
            Logger.i(TAG, "onSetDeviceAlias " + status + " / " + errorCode);
            if(status == DeviceListenerConstant.STATUS_SUCCESS) {
                DemoApplication.toast("设置设备别名成功");
                DemoApplication.runOnUI(() -> getDeviceList());
            } else {
                DemoApplication.toast("设置设备别名失败:" + errorCode + DeviceFragment.getDeviceErrorMsg(errorCode));
            }
        }

        @Override
        public void onGetDeviceList(int status, int errorCode, List<LelinkServiceInfo> list) {
            Logger.i(TAG, "onGetDeviceList " + status + " / " + errorCode);
            if(status == DeviceListenerConstant.STATUS_SUCCESS) {
                DemoApplication.toast("查询设备成功");
                DemoApplication.runOnUI(() -> mAdapter.updateDatas(list));
            } else {
                DemoApplication.toast("查询设备失败:" + errorCode + DeviceFragment.getDeviceErrorMsg(errorCode));
            }
        }
    };

    @Override
    public int getLayoutID() {
        return R.layout.f_favorite_device;
    }

    @Override
    public void init(View view) {
        mSpinnerOnline = view.findViewById(R.id.spinner);
        mSpinnerNetType = view.findViewById(R.id.spinnerNet);
        mBtnRemove = view.findViewById(R.id.btn_remove);
        mBtnRemove.setOnClickListener(this);
        mBtnSetAlias = view.findViewById(R.id.btn_set_alias);
        mBtnSetAlias.setOnClickListener(this);
        mBtnPush = view.findViewById(R.id.btn_push);
        mBtnPush.setOnClickListener(this);
        mBtnMirror = view.findViewById(R.id.btn_mirror);
        mBtnMirror.setOnClickListener(this);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        view.findViewById(R.id.btn_add).setOnClickListener(this);
        view.findViewById(R.id.btn_get).setOnClickListener(this);

        if (mRecyclerView == null) {
            mAdapter = new DeviceAdapter(getContext());
            mRecyclerView = view.findViewById(R.id.recyclerView);
            mAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position, LelinkServiceInfo pInfo) {
                    mAdapter.selectItem(pInfo);
                }
            });
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LelinkSourceSDK.getInstance().setFavoriteDeviceListener(mListener);
        getDeviceList();
    }

    @Override
    public void onClick(View v) {
        List<LelinkServiceInfo> selectedInfoList = mAdapter.getSelectedInfoList();
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this)
                        .commitAllowingStateLoss();
                break;
            case R.id.btn_remove:
                if (selectedInfoList == null || selectedInfoList.size() < 1) {
                    DemoApplication.toast("请先选择接收端设备");
                    return;
                }
                removeDevice();
                break;
            case R.id.btn_set_alias:
                if (selectedInfoList == null || selectedInfoList.size() != 1) {
                    DemoApplication.toast("请先选择一个接收端设备");
                    return;
                }
                showDeviceAliasDialog();
                break;
            case R.id.btn_add:
                LelinkServiceInfo selectServiceInfo = DeviceManager.getInstance().getConnectInfo();
                if (selectServiceInfo != null) {
                    DemoApplication.toast("收藏 " + selectServiceInfo.getName());
                    LelinkSourceSDK.getInstance().addFavoriteDevice(selectServiceInfo);
                } else {
                    DemoApplication.toast("请先连接设备");
                }
                break;
            case R.id.btn_get:
                getDeviceList();
                break;
            case R.id.btn_push:
                if (selectedInfoList == null || selectedInfoList.size() != 1) {
                    DemoApplication.toast("请先选择一个接收端设备");
                    return;
                }
                enterPushFragment(selectedInfoList.get(0));
                break;
            case R.id.btn_mirror:
                if (selectedInfoList == null || selectedInfoList.size() != 1) {
                    DemoApplication.toast("请先选择一个接收端设备");
                    return;
                }
                enterMirrorFragment(selectedInfoList.get(0));
                break;
        }
    }

    private void getDeviceList() {
        Logger.i(TAG, "getDeviceList ");
        mAdapter.clearData();
        int position = mSpinnerOnline.getSelectedItemPosition();
        int netPosition = mSpinnerNetType.getSelectedItemPosition();
        int onlineType = LelinkSourceSDK.ALL_DEVICE;
        int netType = LelinkSourceSDK.ALL_DEVICE;
        if(position == 0) {
            onlineType = LelinkSourceSDK.ALL_DEVICE;
        } else if(position == 1) {
            onlineType = LelinkSourceSDK.ONLINE_DEVICE;
        }
        if(netPosition == 0) {
            netType = LelinkSourceSDK.ALL_DEVICE;
        } else if(netPosition == 1) {
            netType = LelinkSourceSDK.LOCAL_DEVICE;
        }
        LelinkSourceSDK.getInstance().getFavoriteDeviceList(onlineType, netType);
    }

    private void removeDevice() {
        List<LelinkServiceInfo> selectedInfoList = mAdapter.getSelectedInfoList();
        LelinkSourceSDK.getInstance().removeFavoriteDevice(selectedInfoList);
        mAdapter.clearSelectedInfo();
    }

    private void showDeviceAliasDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_dev_alias, null);
        EditText editAlias = view.findViewById(R.id.et_alias);
        new AlertDialog.Builder(getContext())
                .setTitle("请输入设备别名")
                .setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String alias = editAlias.getText().toString().trim();
                        LelinkServiceInfo lelinkServiceInfo = mAdapter.getSelectedInfoList().get(0);
                        DemoApplication.toast("修改别名：" + lelinkServiceInfo.getName());
                        LelinkSourceSDK.getInstance().setFavoriteDeviceAlias(lelinkServiceInfo, alias);
                    }
                }).setNegativeButton("取消", null).setCancelable(false).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LelinkSourceSDK.getInstance().setFavoriteDeviceListener(null);
        mSpinnerOnline = null;
        mSpinnerNetType = null;
        mBtnRemove = null;
        mBtnSetAlias = null;
        mBtnPush = null;
        mBtnMirror = null;
        mRecyclerView = null;
        mAdapter = null;
    }
}
