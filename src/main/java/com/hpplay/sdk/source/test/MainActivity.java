package com.hpplay.sdk.source.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.hpplay.sdk.source.test.fragment.BaseFragment;
import com.hpplay.sdk.source.test.fragment.MirrorFragment;

/**
 * author : DON
 * date   : 7/26/2110:14 AM
 * desc   :
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.fastPushBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Fragment mFastPushFragment = getSupportFragmentManager().findFragmentByTag(FastPushFragment.class.getName());
                    if (mFastPushFragment != null && mFastPushFragment.isAdded()) {
                        return;
                    }
                    if (mFastPushFragment == null) {
                        mFastPushFragment = new FastPushFragment();
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.mainContainer, mFastPushFragment, FastPushFragment.class.getName())
                            .disallowAddToBackStack()
                            .commitAllowingStateLoss();
                } catch (Exception e) {
                    Logger.w("MainActivity", e);
                }

            }
        });
        findViewById(R.id.fastMirrorBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Fragment mFastMirrorFragment = getSupportFragmentManager().findFragmentByTag(FastMirrorFragment.class.getName());
                    if (mFastMirrorFragment != null && mFastMirrorFragment.isAdded()) {
                        return;
                    }
                    if (mFastMirrorFragment == null) {
                        mFastMirrorFragment = new FastMirrorFragment();
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.mainContainer, mFastMirrorFragment, FastMirrorFragment.class.getName())
                            .disallowAddToBackStack()
                            .commitAllowingStateLoss();
                } catch (Exception e) {
                    Logger.w("MainActivity", e);
                }
            }
        });
        findViewById(R.id.apiCastBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Fragment mAPICastFragment = getSupportFragmentManager().findFragmentByTag(APICastFragment.class.getName());
                    if (mAPICastFragment != null && mAPICastFragment.isAdded()) {
                        return;
                    }
                    if (mAPICastFragment == null) {
                        mAPICastFragment = new APICastFragment();
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.mainContainer, mAPICastFragment, APICastFragment.class.getName())
                            .disallowAddToBackStack()
                            .commitAllowingStateLoss();
                } catch (Exception e) {
                    Logger.w("MainActivity", e);
                }
            }
        });
    }
}
