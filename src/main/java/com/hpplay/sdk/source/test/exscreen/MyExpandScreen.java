package com.hpplay.sdk.source.test.exscreen;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.hpplay.sdk.source.api.LelinkPlayerInfo;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.process.LelinkSdkManager;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.adapter.MyPagerAdapter;
import com.hpplay.sdk.source.test.view.LinkageTranslateViewPager;

import java.util.ArrayList;

/**
 * jasinCao
 * 20-9-9 下午8:18
 */
public class MyExpandScreen {

    private static final String TAG = "MyExternalScreen";
    private ViewGroup viewGroup;
    private LinkageTranslateViewPager mViewPager;
    private ArrayList<String> pictureUrls = new ArrayList<>();
    private int mSelectPosition;
    private LinkageTranslateViewPager mParentPager;
    private Activity mActivity;
    private View mRoot;

    public MyExpandScreen(Activity activity) {
        this.mActivity = activity;
        mRoot = activity.getLayoutInflater().inflate(R.layout.external_screen_layout, null);
        viewGroup = (ViewGroup) mRoot.findViewById(R.id.parent);
        mViewPager = (LinkageTranslateViewPager) mRoot.findViewById(R.id.view_pager);
    }

    public void startExpansionScreen(LelinkServiceInfo serviceInfo) {
        Logger.i(TAG, "startExpansionScreen " + serviceInfo.getName());
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(pictureUrls, mActivity.getApplicationContext());
        mViewPager.setControlViewPager(mParentPager);
        mViewPager.setPageMargin(300);
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setSelectedPosition(mSelectPosition);

        LelinkSdkManager.getInstance().setExpansionScreenInfo(mActivity, mRoot);
        //启用分屏镜像时，若未开启主屏镜像，直接调用startExpandMirror
        LelinkPlayerInfo playerInfo = new LelinkPlayerInfo();
        playerInfo.setLelinkServiceInfo(serviceInfo);
        LelinkSdkManager.getInstance().startExpandMirror(playerInfo);
        //若已开启主屏镜像，调用switchExpansionScreen
        //LelinkSdkManager.getInstance().switchExpansionScreen(true);
    }

    public void stopExpandScreen() {
        //启用分屏镜像时，若未开启主屏镜像，停止时调用stopPlay
        LelinkSdkManager.getInstance().setExpansionScreenInfo(null, null);
        LelinkSdkManager.getInstance().stopPlay();
        //若已开启主屏镜像，停止时调用switchExpansionScreen
        //LelinkSdkManager.getInstance().switchExpansionScreen(false);
    }

    public void setPictureUrls(ArrayList<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public void setSelectPosition(int position) {
        this.mSelectPosition = position;
    }

    public void setViewPager(LinkageTranslateViewPager viewPager) {
        mParentPager = viewPager;
    }

    public void addView(View v, ViewGroup.LayoutParams layoutParams) {
        viewGroup.removeAllViews();
        viewGroup.addView(v, layoutParams);
    }

}
