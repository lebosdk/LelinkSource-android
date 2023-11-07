package com.hpplay.sdk.source.test.exscreen;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.process.LelinkSdkManager;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.adapter.MyPagerAdapter;
import com.hpplay.sdk.source.test.view.LinkageTranslateViewPager;

import java.util.ArrayList;

/**
 * Created by jasinCao
 * <p>
 * 然后activity 在manifest中配置 android:process=":lelinkps"属性
 */
public class ExternalScreenMirrorActivity extends Activity {
    private static final String TAG = "ExternalScreenMirrorActivity";
    private LinkageTranslateViewPager mViewPager;
    private ArrayList<String> mUrls = new ArrayList<>();
    private int mPosition = 0;
    private LelinkServiceInfo lelinkServiceInfo;
    private MyExpandScreen mExpandScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_browser_layout);
        mUrls = getIntent().getStringArrayListExtra("urls");
        mPosition = getIntent().getIntExtra("position", 0);
        mViewPager = (LinkageTranslateViewPager) findViewById(R.id.view_page);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(mUrls, getApplicationContext());
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setPageMargin(300);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setCurrentItem(mPosition);
        Logger.i(TAG, "onCreate");
        lelinkServiceInfo = getIntent().getParcelableExtra("serviceinfo");

        mExpandScreen = new MyExpandScreen(ExternalScreenMirrorActivity.this);
        mExpandScreen.setPictureUrls(mUrls);
        mExpandScreen.setSelectPosition(mPosition);
        mExpandScreen.setViewPager(mViewPager);
        mExpandScreen.startExpansionScreen(lelinkServiceInfo);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        Logger.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        Logger.i(TAG, "onStop");
        super.onStop();
//        mExpandScreen.stopExpandScreen();
        LelinkSdkManager.getInstance().switchExpansionScreen(false);
    }

    @Override
    protected void onDestroy() {
        Logger.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
