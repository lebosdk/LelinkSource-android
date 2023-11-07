package com.hpplay.sdk.source.test.view;

import java.lang.reflect.Field;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.hpplay.sdk.source.test.Logger;

/**
 * Created by jasinCao
 * <p>
 * 仿IOS相册功能图片滑动切换View
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class LinkageTranslateViewPager extends ViewPager {

    private final static String TAG = "LinkageTranslateViewPager";

    private LinkageTranslateViewPager mControlViewPager;
    private LinkageZoomView mPictureZoomView;
    private int mSelectedPosition;
    private boolean isFirst = true;
    private int mLastIndex = 0;

    public LinkageTranslateViewPager(Context context) {
        super(context);
        initScollSpeed();
    }

    public LinkageTranslateViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScollSpeed();
    }

    private void initScollSpeed() {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(
                    this.getContext(), new DecelerateInterpolator());
            scroller.setDuration(500);
            mScroller.set(this, scroller);
        } catch (Exception e) {
            Logger.w(TAG, e);
        }

    }


    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 1000;

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,
                                int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setDuration(int time) {
            mDuration = time;
        }

    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        mLastIndex = mSelectedPosition;
        this.setCurrentItem(mSelectedPosition);
        //  registerCurrentItemListener();
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);

        if (isFirst && mControlViewPager != null) {
            isFirst = false;
            this.postDelayed(mRegisterCurrentItemRunable, 300);
        }

    }

    Runnable mRegisterCurrentItemRunable = new Runnable() {
        @Override
        public void run() {
            registerCurrentItemListener();
        }
    };

    public void setControlViewPager(LinkageTranslateViewPager controlViewPager) {
        mControlViewPager = controlViewPager;
        mControlViewPager.addOnPageChangeListener(onControlViewPageChangeListener);
    }


    OnPageChangeListener onControlViewPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            scrollOfOffset(position - mSelectedPosition, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Logger.i(TAG, "onPageScrollStateChanged " + state);
            if (state == 0 && mControlViewPager != null) {
                registerCurrentItemListener();
                resetLastChildSize();
            }

        }
    };


    /**
     * 还原非当前选择view的尺寸
     */
    private void resetLastChildSize() {
        if (mLastIndex != mControlViewPager.getCurrentItem()) {
            LinkageZoomView controlViewChild = (LinkageZoomView) mControlViewPager.findViewById(mLastIndex);
            LinkageZoomView currentViewChild = (LinkageZoomView) this.findViewById(mLastIndex);
            if (controlViewChild != null && currentViewChild != null) {
                controlViewChild.resetToSourceSize(false);
                currentViewChild.resetToSourceSize(false);
            }
            mLastIndex = mControlViewPager.getCurrentItem();
        }
    }

    public void registerCurrentItemListener() {
        Logger.i(TAG, mControlViewPager.getCurrentItem() + "");
        this.setCurrentItem(mControlViewPager.getCurrentItem(), false);
        mPictureZoomView = (LinkageZoomView) this.findViewById(mControlViewPager.getCurrentItem());

        LinkageZoomView pictureZoomView = (LinkageZoomView) mControlViewPager.findViewById(mControlViewPager.getCurrentItem());
        if (pictureZoomView != null) {
            pictureZoomView.setOnLinkControlChangeListener(mScaleChangeListener);
        }

    }

    LinkageZoomView.LinkControlChangeListener mScaleChangeListener = new LinkageZoomView.LinkControlChangeListener() {
        @Override
        public void onScaleChange(float scale, float centerX, float centerY) {
            Logger.i(TAG, "onScaleChange");
            if (mPictureZoomView != null) {
                mPictureZoomView.startZoomImage(scale);
            }
        }

        @Override
        public void onDrag(float controlViewMaxWidthMargin, float controlViewMaxHeightMargin, float dx, float dy) {
            if (mPictureZoomView != null) {
                mPictureZoomView.setControlDragTranslate(controlViewMaxWidthMargin, controlViewMaxHeightMargin, dx, dy);
            }
        }

        @Override
        public void onControlStop() {
            if (mPictureZoomView != null) {
                mPictureZoomView.resetToSourceSize(true);
            }
        }


        @Override
        public void onControlStart() {
            if (mPictureZoomView != null) {
                mPictureZoomView.isMatrixScaleType();
            }
        }

        @Override
        public void onDuobleTap(float current, float target) {
            if (mPictureZoomView != null) {
                mPictureZoomView.onDoubleTapScale(current, target);
            }
        }
    };


    /**
     * 页面滑动扩展屏跟随移动
     *
     * @param position
     * @param value
     */
    public void scrollOfOffset(int position, float value) {
        if (value > 0f) {
            int width = getWidth() + this.getPageMargin();
            int socrollValue = (int) (width * (value + (double) position));
            setScrollX(socrollValue);
        }
    }


}
