package com.hpplay.sdk.source.test.fragment;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.test.DemoApplication;
import com.hpplay.sdk.source.test.IUIUpdateListener;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.manager.DeviceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * author : DON
 * date   : 5/19/218:59 PM
 * desc   :
 */
public class BrowseListFragment extends BaseFragment {
    private final static String TAG = "BrowseListFragment";
    private ListView mListView;
    private ImageButton mBrowseBtn;
    private BrowseAdapter mBrowseAdapter;
    private List<LelinkServiceInfo> mServiceList = new ArrayList<>();
    private OnPlayListener mPlayListener;
    private IUIUpdateListener mUIListener = new IUIUpdateListener() {
        @Override
        public void onUpdateDevices(List<LelinkServiceInfo> list) {
            if (mListView == null) {
                return;
            }
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    mServiceList.clear();
                    mServiceList.addAll(list);
                    mBrowseAdapter.notifyDataSetChanged();
                    if (mServiceList.size() > 0) {
                        animateBrowse(false);
                    }
                }
            });

        }

        @Override
        public void onConnect(LelinkServiceInfo info) {
        }

        @Override
        public void onDisconnect(LelinkServiceInfo info) {

        }

        @Override
        public void onNetChanged() {

        }

        @Override
        public void onBindSuccess() {

        }
    };

    @Override
    public int getLayoutID() {
        return R.layout.f_browse_list;
    }

    @Override
    public void init(View view) {
        DeviceManager.getInstance().addUIListener(mUIListener);
        mListView = view.findViewById(R.id.browseList);
        mBrowseBtn = view.findViewById(R.id.browserBtn);
        mBrowseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LelinkSourceSDK.getInstance().startBrowse();
                animateBrowse(true);
            }
        });
        if (DeviceManager.getInstance().getBrowseList() != null) {
            mServiceList.clear();
            mServiceList.addAll(DeviceManager.getInstance().getBrowseList());
        } else {
            //当前无可用无设备，则开始搜索设备
            LelinkSourceSDK.getInstance().startBrowse();
            animateBrowse(true);
        }
        mBrowseAdapter = new BrowseAdapter(getActivity(), mServiceList);
        mListView.setAdapter(mBrowseAdapter);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPlayListener != null) {
                    mPlayListener.onPlay(mServiceList.get(position));
                }
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        mPlayListener = null;
        DeviceManager.getInstance().removeUIListener(mUIListener);
        animateBrowse(false);
        super.onDestroyView();

        mListView = null;
        mBrowseBtn = null;
        mBrowseAdapter = null;
    }

    private void animateBrowse(boolean animate) {
        if (mBrowseBtn == null) {
            return;
        }
        mBrowseBtn.clearAnimation();
        if (!animate) {
            return;
        }
        Animation animation = new RotateAnimation(0, 359
                , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setRepeatCount(10000);
        animation.setFillAfter(true);
        mBrowseBtn.startAnimation(animation);
    }

    public void setOnPlayListener(OnPlayListener listener) {
        mPlayListener = listener;
    }


    public interface OnPlayListener {
        void onPlay(LelinkServiceInfo info);
    }
}
