package com.hpplay.sdk.source.test.exscreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;

import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.adapter.GridViewAdapter;

import java.util.ArrayList;

/**
 * Created by jasinCao
 */
public class ImageBrowseActivity extends Activity {

    private static final String TAG = "ImageBrowseActivity";

    private ArrayList<String> mUrls = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private GridViewAdapter mViewGridAdapter;
    private LelinkServiceInfo lelinkServiceInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_layout);
        mUrls.add("http://img.redocn.com/sheying/20161021/xiaohefengjing_7311709.jpg");
        mUrls.add("http://pic17.nipic.com/20111010/4317361_163247363000_2.jpg");
        mUrls.add("http://img.pconline.com.cn/images/upload/upc/tx/softbbs/1202/28/c0/10644987_1330417008638.jpg");
        mUrls.add("http://pic27.nipic.com/20130304/7368717_232014122100_2.jpg");
        mUrls.add("http://www.hubei.gov.cn/mlhb/lyms/xyjq/201205/W020120531559128275377.jpg");
        mUrls.add("http://img17.3lian.com/d/file/201702/21/9167add2223eabf3543d4288b74cc231.jpg");
        mUrls.add("http://pic163.nipic.com/file/20180421/7092831_140036752037_2.jpg");
        mUrls.add("http://pic14.nipic.com/20110529/7570613_004640647181_2.jpg");
        mUrls.add("http://img17.3lian.com/d/file/201702/15/43ee293f41a790802fc2489186959dac.jpg");
        mUrls.add("http://pic59.nipic.com/file/20150122/4329275_100353572000_2.jpg");
        mUrls.add("http://pic1.win4000.com/wallpaper/5/574575e72598a.jpg");
        lelinkServiceInfo = getIntent().getParcelableExtra("serviceinfo");
        initViewRecycler();
    }


    private void initViewRecycler() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayout.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(20));
        mViewGridAdapter = new GridViewAdapter(getApplicationContext(), mUrls, onRecyclerviewItemClickListener);
        mRecyclerView.setAdapter(mViewGridAdapter);
    }

    GridViewAdapter.OnRecyclerviewItemClickListener onRecyclerviewItemClickListener = new GridViewAdapter.OnRecyclerviewItemClickListener() {
        @Override
        public void onItemClickListener(View v, int position) {
            Intent intent = new Intent(ImageBrowseActivity.this, ExternalScreenMirrorActivity.class);
            intent.putExtra("urls", mUrls);
            intent.putExtra("position", position);
            intent.putExtra("serviceinfo", lelinkServiceInfo);
            startActivity(intent);
        }
    };

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildPosition(view) == 0)
                outRect.top = space;
        }
    }

}
