package com.hpplay.sdk.source.test.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.hpplay.sdk.source.api.DeviceListenerConstant;
import com.hpplay.sdk.source.api.IHistoryDeviceListener;
import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.test.DemoApplication;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.adapter.DeviceAdapter;
import com.hpplay.sdk.source.test.manager.DeviceManager;

import java.util.List;

public class HistoryDeviceFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "HistoryDeviceFragment";

    private Spinner mSpinnerOnline, mSpinnerNetType;
    private Button mBtnRemove, mBtnRemoveAll, mBtnPush, mBtnMirror;
    private RecyclerView mRecyclerView;
    private DeviceAdapter mAdapter;

    private IHistoryDeviceListener mListener = new IHistoryDeviceListener() {
        @Override
        public void onRemoveDevice(int status, int errorCode) {
            Logger.i(TAG, "onRemoveDevice " + status + " / " + errorCode);
            if(status == DeviceListenerConstant.STATUS_SUCCESS) {
                DemoApplication.toast("删除历史设备成功");
                DemoApplication.runOnUI(() -> getDeviceList());
            } else {
                DemoApplication.toast("删除历史设备失败:" + errorCode + DeviceFragment.getDeviceErrorMsg(errorCode));
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
        return R.layout.f_history_device;
    }

    @Override
    public void init(View view) {
        mSpinnerOnline = view.findViewById(R.id.spinner);
        mSpinnerNetType = view.findViewById(R.id.spinnerNet);
        mBtnRemove = view.findViewById(R.id.btn_remove);
        mBtnRemove.setOnClickListener(this);
        mBtnRemoveAll = view.findViewById(R.id.btn_remove_all);
        mBtnRemoveAll.setOnClickListener(this);
        mBtnPush = view.findViewById(R.id.btn_push);
        mBtnPush.setOnClickListener(this);
        mBtnMirror = view.findViewById(R.id.btn_mirror);
        mBtnMirror.setOnClickListener(this);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
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
        LelinkSourceSDK.getInstance().setHistoryDeviceListener(mListener);
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
                deleteDevice(false);
                break;
            case R.id.btn_remove_all:
                if(mAdapter.getData().size() == 0) {
                    return;
                }
                deleteDevice(true);
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
        LelinkSourceSDK.getInstance().getHistoryDeviceList(onlineType, netType);
    }

    private void deleteDevice(boolean deleteAll) {
        if(deleteAll) {
            LelinkSourceSDK.getInstance().removeHistoryDevice(null, LelinkSourceSDK.REMOVE_HISTORY_ALL);
        } else {
            List<LelinkServiceInfo> selectedInfoList = mAdapter.getSelectedInfoList();
            LelinkSourceSDK.getInstance().removeHistoryDevice(selectedInfoList, LelinkSourceSDK.REMOVE_HISTORY_PART);
        }
        mAdapter.clearSelectedInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LelinkSourceSDK.getInstance().setHistoryDeviceListener(null);

        mSpinnerOnline = null;
        mSpinnerNetType = null;
        mBtnRemove = null;
        mBtnRemoveAll = null;
        mBtnPush = null;
        mBtnMirror = null;
        mRecyclerView = null;
        mAdapter = null;
    }
}
