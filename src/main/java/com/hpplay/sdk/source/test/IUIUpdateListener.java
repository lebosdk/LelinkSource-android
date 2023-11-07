package com.hpplay.sdk.source.test;

import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;

import java.util.List;

public interface IUIUpdateListener {

    void onUpdateDevices(List<LelinkServiceInfo> list);

    void onConnect(LelinkServiceInfo info);

    void onDisconnect(LelinkServiceInfo info);

    void onNetChanged();

    void onBindSuccess();

}
