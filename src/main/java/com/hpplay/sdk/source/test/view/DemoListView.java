package com.hpplay.sdk.source.test.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class DemoListView extends ListView {

    public DemoListView(Context context) {
        super(context);
    }

    public DemoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DemoListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //点击listview里面滚动停止时，scrollview拦截listview的触屏事件，就是scrollview该滚动了
            requestDisallowInterceptTouchEvent(false);
        } else {
            //当listview在滚动时，不拦截listview的滚动事件；就是listview可以滚动，
            requestDisallowInterceptTouchEvent(true);
        }

        return super.onTouchEvent(event);
    }
}