package com.hpplay.sdk.source.test.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.test.DemoApplication;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.manager.DeviceManager;


public abstract class BaseFragment extends Fragment {
    private View mRootView;
    private boolean isInit = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutID(), null);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        init(mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isInit) {
            return;
        }
        isInit = true;
    }

    public abstract int getLayoutID();

    public abstract void init(View view);

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
    }


    protected void enterPushFragment(LelinkServiceInfo serviceInfo) {
        if (serviceInfo == null) {
            DemoApplication.toast("请先选择接收端设备");
            return;
        }
        DeviceManager.getInstance().setSelectInfo(serviceInfo);
        try {
            Fragment pushFragment = getActivity().getSupportFragmentManager().findFragmentByTag("push_fm");
            if (pushFragment != null && pushFragment.isAdded()) {
                return;
            }
            if (pushFragment == null) {
                pushFragment = new PushFragment();
            }
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, pushFragment, "push_fm")
                    .addToBackStack("push")
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            Logger.w("enterPushFragment", e);
        }
    }

    protected void enterMirrorFragment(LelinkServiceInfo serviceInfo) {
        if (serviceInfo == null) {
            DemoApplication.toast("请先选择接收端设备");
            return;
        }
        DeviceManager.getInstance().setSelectInfo(serviceInfo);
        try {
            Fragment mirrorFragment = getActivity().getSupportFragmentManager().findFragmentByTag("mirror_fm");
            if (mirrorFragment != null && mirrorFragment.isAdded()) {
                return;
            }
            if (mirrorFragment == null) {
                mirrorFragment = new MirrorFragment();
            }
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mirrorFragment, "mirror_fm")
                    .addToBackStack("mirror")
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            Logger.w("enterMirrorFragment", e);
        }
    }
}
