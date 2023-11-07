package com.hpplay.sdk.source.test.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.view.LinkageZoomView;

import java.util.ArrayList;

/**
 * Created by jasinCao
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class MyPagerAdapter extends PagerAdapter {
    private ArrayList<String> mUrls;
    private Context mContext;

    public MyPagerAdapter(ArrayList<String> urls, Context context) {
        this.mUrls = urls;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinkageZoomView imageView = new LinkageZoomView(mContext, position);
        Glide.with(mContext).load(mUrls.get(position)).into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Logger.i("pagerAdapter", "postition " + position + "  child count " + container.getChildCount());
        LinkageZoomView zoomView = (LinkageZoomView) object;
        zoomView.clear();
        container.removeView(zoomView);
    }

}
