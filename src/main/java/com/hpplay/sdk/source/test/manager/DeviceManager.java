package com.hpplay.sdk.source.test.manager;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.hpplay.sdk.source.api.IBindSdkListener;
import com.hpplay.sdk.source.api.IConnectListener;
import com.hpplay.sdk.source.api.IRelevantInfoListener;
import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.browse.api.AuthListener;
import com.hpplay.sdk.source.browse.api.IAPI;
import com.hpplay.sdk.source.browse.api.IBrowseListener;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.test.DemoApplication;
import com.hpplay.sdk.source.test.IUIUpdateListener;
import com.hpplay.sdk.source.test.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 连接
 */
public class DeviceManager {
    private final static String TAG = "DemoDeviceManager";

    private static DeviceManager sInstance = null;
    private LelinkServiceInfo mSelectInfo;
    private LelinkServiceInfo mConnectInfo;
    private List<IUIUpdateListener> mUIListeners = new ArrayList<>();
    private List<LelinkServiceInfo> mBrowseList;
    private boolean firstAuthSuccess = true;

    private IBindSdkListener mBindSdkListener = new IBindSdkListener() {
        @Override
        public void onBindCallback(boolean success) {
            Logger.i(TAG, "onBindCallback " + success);
            setPassThroughListener();
            if (success) {
                for (IUIUpdateListener listener : mUIListeners) {
                    listener.onBindSuccess();
                }
            }
        }
    };

    private static String sAndroidID = "";

    public static String getAndroidID(Context context) {
        if (!TextUtils.isEmpty(sAndroidID)) {
            return sAndroidID;
        }
        try {
            sAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return sAndroidID;
        } catch (Exception e) {
            Logger.w(TAG, "getAndroidID Settings.Secure can not get androidID");
        } catch (Error r) {
            Logger.w(TAG, "getAndroidID Settings.Secure can not get androidID");
        }
        try {
            sAndroidID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
            return sAndroidID;
        } catch (Exception e) {
            Logger.w(TAG, "getAndroidID Settings.System can not get androidID");
        } catch (Error e) {
            Logger.w(TAG, "getAndroidID Settings.System can not get androidID");
        }
        return "";
    }

    private AuthListener mAuthListener = new AuthListener() {
        @Override
        public void onAuthSuccess(String s, String s1) {
            Logger.i(TAG, "onAuthSuccess");
            if (firstAuthSuccess) {
                firstAuthSuccess = false;
                LelinkSourceSDK.getInstance().setPermissionMode(IAPI.PERMISSION_MODE_CLOUD_LICENSE,
                        getAndroidID(DemoApplication.getApplication()));
            }
        }

        @Override
        public void onAuthFailed(int i) {

        }
    };

    private IBrowseListener mBrowseListener = new IBrowseListener() {

        @Override
        public void onBrowse(int i, List<LelinkServiceInfo> list) {
            if (i == IBrowseListener.BROWSE_ERROR_AUTH) {
                Logger.e(TAG, "授权失败");
                DemoApplication.toast("授权失败");
                return;
            }
            if (i == IBrowseListener.BROWSE_STOP) {
                Logger.i(TAG, "搜索停止");
                return;
            } else if (i == IBrowseListener.BROWSE_TIMEOUT) {
                Logger.i(TAG, "搜索超时");
            }
            mBrowseList = list;
            for (IUIUpdateListener listener : mUIListeners) {
                listener.onUpdateDevices(list);
            }
        }
    };

    private IConnectListener mConnectListener = new IConnectListener() {
        @Override
        public void onConnect(LelinkServiceInfo lelinkServiceInfo, int protocol) {
            Logger.i(TAG, "onConnect:" + lelinkServiceInfo.getName());
            String type = protocol == IConnectListener.TYPE_LELINK ? "Lelink"
                    : protocol == IConnectListener.TYPE_DLNA ? "DLNA"
                    : protocol == IConnectListener.TYPE_IM ? "IM" : ("协议:" + protocol);
            DemoApplication.toast(type + "  " + lelinkServiceInfo.getName() + "连接成功");
            setSelectInfo(lelinkServiceInfo);
            DeviceManager.getInstance().setConnectInfo(lelinkServiceInfo);
            for (IUIUpdateListener listener : mUIListeners) {
                listener.onConnect(lelinkServiceInfo);
            }
        }

        @Override
        public void onDisconnect(LelinkServiceInfo lelinkServiceInfo, int what, int extra) {
            if (lelinkServiceInfo == null) {
                return;
            }
            Logger.i(TAG, "onDisconnect:" + lelinkServiceInfo.getName() + " disConnectType:" + what + " extra:" + extra);
            DeviceManager.getInstance().setConnectInfo(null);
            String text = null;
            if (what == IConnectListener.WHAT_HARASS_WAITING) {// 防骚扰，等待用户确认
                // 乐播投屏防骚扰等待消息，请开发者务必处理该消息
                text = lelinkServiceInfo.getName() + "等待用户确认";
            } else if (what == IConnectListener.WHAT_DISCONNECT) {
                switch (extra) {
                    case IConnectListener.EXTRA_HARASS_REJECT:// 防骚扰，用户拒绝投屏
                        text = lelinkServiceInfo.getName() + "连接被拒绝";
                        break;
                    case IConnectListener.EXTRA_HARASS_TIMEOUT:// 防骚扰，用户响应超时
                        text = lelinkServiceInfo.getName() + "防骚扰响应超时";
                        break;
                    case IConnectListener.EXTRA_HARASS_BLACKLIST:// 防骚扰，该用户被加入黑名单
                        text = lelinkServiceInfo.getName() + "已被加入投屏黑名单";
                        break;
                    case IConnectListener.EXTRA_CONNECT_DEVICE_OFFLINE:
                        text = lelinkServiceInfo.getName() + "不在线";
                        break;
                    default:
                        text = lelinkServiceInfo.getName() + "连接断开";
                        break;
                }
            } else if (what == IConnectListener.WHAT_CONNECT_FAILED) {
                switch (extra) {
                    case IConnectListener.EXTRA_CONNECT_DEVICE_OFFLINE:
                        text = lelinkServiceInfo.getName() + "不在线";
                        break;
                    default:
                        text = lelinkServiceInfo.getName() + "连接失败";
                        break;
                }
            }
            if (TextUtils.isEmpty(text)) {
                text = "onDisconnect " + what + "/" + extra;
            }
            DemoApplication.toast(text);
            for (IUIUpdateListener listener : mUIListeners) {
                listener.onDisconnect(lelinkServiceInfo);
            }
        }
    };

    public static synchronized DeviceManager getInstance() {
        synchronized (DeviceManager.class) {
            if (sInstance == null) {
                sInstance = new DeviceManager();
            }
        }
        return sInstance;
    }

    private DeviceManager() {

    }

    public void setSelectInfo(LelinkServiceInfo serviceInfo) {
        mSelectInfo = serviceInfo;
    }

    public void addUIListener(IUIUpdateListener listener) {
        mUIListeners.add(listener);
    }

    public void removeUIListener(IUIUpdateListener listener) {
        mUIListeners.remove(listener);
    }

    public LelinkServiceInfo getSelectInfo() {
        return mSelectInfo;
    }

    public IBindSdkListener getBindListener() {
        return mBindSdkListener;
    }

    public IBrowseListener getBrowseListener() {
        return mBrowseListener;
    }

    public IConnectListener getConnectListener() {
        return mConnectListener;
    }

    public List<LelinkServiceInfo> getBrowseList() {
        return mBrowseList;
    }

    public AuthListener getAuthListener() {
        return mAuthListener;
    }

    public void setConnectInfo(LelinkServiceInfo serviceInfo) {
        mConnectInfo = serviceInfo;
    }

    public LelinkServiceInfo getConnectInfo() {
        return  mConnectInfo;
    }


    /**
     * 设置透传监听
     */
    private void setPassThroughListener() {
        LelinkSourceSDK.getInstance().setPassThroughListener(new IRelevantInfoListener() {
            @Override
            public void onSendRelevantInfoResult(int option, String result) {

            }

            @Override
            public void onReverseInfoResult(int option, final String result) {
                Logger.i(TAG, "onReverseInfoResult option = " + option + ", result = " + result);
            }
        });
    }

}
