package com.hpplay.sdk.source.test.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hpplay.sdk.source.test.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by duyifeng on 2017/11/24.
 * Device工具类
 */
public class Utils {

    private static final String TAG = "Utils";

    public static String getIP(Context context) {
        String ip = "";
        try {

            // 判断是否是有线网络
            boolean eth0 = false;
            boolean wifi = false;
            boolean mobile = false;
            ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                String type = networkInfo.getTypeName();
                if (type.equalsIgnoreCase("Ethernet")) {
                    eth0 = true;
                } else if (type.equalsIgnoreCase("WIFI")) {
                    wifi = true;
                } else if (type.equalsIgnoreCase("MOBILE")) {
                    mobile = true;
                }
            }

            // 在有些设备上wifi和有线同时存在，获得的ip会有两个
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            if (en == null) {
                return ip;
            }
            while (en.hasMoreElements()) {
                NetworkInterface element = en.nextElement();
                Enumeration<InetAddress> inetAddresses = element.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        ip = inetAddress.getHostAddress().toString();
                        Logger.i(TAG, "getIPAddress: " + ip);
                        if (eth0) {
                            if (element.getDisplayName().equals("eth0")) {
                                return ip;
                            }
                        } else if (wifi) {
                            if (element.getDisplayName().equals("wlan0")) {
                                return ip;
                            }
                        } else if (mobile) {
                            return ip;
                        }
                        break;
                    }
                }
            }
        } catch (SocketException ex) {
            Logger.w(TAG, ex);
        }
        return ip;
    }

    /* 获取WiFi的SSID */
    /* 判断当前网路有线还是WiFi */
    public static String getNetWorkName(Context context) {
        String wired_network = "有线网络";
        String wireless_network = "无线网络";
        String mobile_network = "移动网络";
        String net_error = "网络错误";

        try {
            ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                String type = networkInfo.getTypeName();
                if (type.equalsIgnoreCase("Ethernet")) {
                    return wired_network;
                } else if (type.equalsIgnoreCase("WIFI")) {
                    String tmpssid = getWifiSSID(context);
                    if (tmpssid.contains("unknown") || tmpssid.contains("0x")) {
                        tmpssid = wireless_network;
                    }
                    return tmpssid;
                } else if (type.equalsIgnoreCase("MOBILE")) {
                    return mobile_network;
                } else {
                    return wired_network;
                }
            } else {
                String apName = getAPName(context);
                if (!TextUtils.isEmpty(apName)) {
                    return apName;
                }
                return net_error;
            }
        } catch (Exception e) {
            Logger.w(TAG, e);
            return net_error;
        }
    }

    private static String getAPName(Context context) {
        if (!isWifiApOpen(context)) {
            return "";
        }
        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Method method = manager.getClass().getDeclaredMethod("getWifiApConfiguration");
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(manager);
            return configuration.SSID;
        } catch (Exception e) {
            Logger.w(TAG, e);
        }
        return "";
    }

    public static boolean isWifiApOpen(Context context) {
        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Method method = manager.getClass().getDeclaredMethod("getWifiApState");
            int state = (int) method.invoke(manager);
            Field field = manager.getClass().getDeclaredField("WIFI_AP_STATE_ENABLED");
            int value = (int) field.get(manager);
            if (state == value) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Logger.w(TAG, e);
        }
        return false;
    }

    private static String getWifiSSID(Context context) {
        String ssid = "unknown id";
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O || Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return info.getSSID();
            } else {
                return info.getSSID().replace("\"", "");
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
            ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo() != null) {
                    return networkInfo.getExtraInfo().replace("\"", "");
                }
            }
        }
        return ssid;
    }
}
