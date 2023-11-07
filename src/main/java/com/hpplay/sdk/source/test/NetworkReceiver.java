package com.hpplay.sdk.source.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.hpplay.sdk.source.test.constants.Constant;

class NetworkReceiver extends BroadcastReceiver {

    private IUIUpdateListener mUIListener = null;

    public NetworkReceiver() {

    }

    public void setUIListener(IUIUpdateListener listener) {
        mUIListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equalsIgnoreCase(action) ||
                Constant.WIFI_AP_STATE_CHANGED_ACTION.equalsIgnoreCase(action)) {
            if (mUIListener != null) {
                mUIListener.onNetChanged();
            }
        }
    }
}