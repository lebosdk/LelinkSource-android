package com.hpplay.sdk.source.test.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.hpplay.sdk.source.test.Logger;


/**
 * Created by jasinCao
 * <p>
 * 仿IOS相册功能图片控制View
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class LinkageZoomView extends ImageView {

    private static final String TAG = "LinkageZoomView";
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mDetector;
    private Matrix mSourceMatrix = new Matrix();//初始化矩阵
    private Matrix mControlMatrix = new Matrix();//控制矩阵包括缩放和移动

    private double mImageWidth;
    private double mImageHeight;
    private double mMaxScale = 6;
    private double mDobleTapScale = 3;
    private double lastX, lastY;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;
    boolean isDragMode = false;
    private LinkControlChangeListener mOnControlChangeListener;
    private final RectF mDisplayRect = new RectF();

    public LinkageZoomView(Context context) {
        super(context);
        init();
    }

    public LinkageZoomView(Context context, int id) {
        super(context);
        init();
        this.setId(id);
    }

    public LinkageZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public interface LinkControlChangeListener {

        void onScaleChange(float scale, float centerX, float centerY);

        void onDrag(float controlViewMaxWidthMargin, float controlViewMaxHeightMargin, float dx, float dy);

        void onDuobleTap(float current, float target);

        void onControlStart();

        void onControlStop();
    }


    public void setOnLinkControlChangeListener(LinkControlChangeListener onScaleChangeListener) {
        this.mOnControlChangeListener = onScaleChangeListener;
    }


    public void init() {
        setScaleType(ScaleType.FIT_CENTER);
        mDetector = new ScaleGestureDetector(getContext(), mScaleListener);
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener());
        mGestureDetector.setOnDoubleTapListener(mOnDoubleTapListener);

    }


    ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor))
                return false;
//            Logger.i("ScaleGestureDetector", scaleFactor + "    " + detector.getFocusX() + "    " + detector.getFocusY());
            float centerX = detector.getFocusX();
            float centerY = detector.getFocusY();
            startZoomImage(scaleFactor,
                    centerX, centerY);
            if (mOnControlChangeListener != null) {
                mOnControlChangeListener.onScaleChange(scaleFactor, centerX, centerY);
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            // NO-OP
        }
    };

    GestureDetector.OnDoubleTapListener mOnDoubleTapListener = new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            double target = isUseZoom() ? 1 : mDobleTapScale;
            double current = !isUseZoom() ? 1 : mDobleTapScale;
            if (mOnControlChangeListener != null) {
                mOnControlChangeListener.onDuobleTap((float) current, (float) target);
            }
            onDoubleTapScale((float) current, (float) target);
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    };

    private void getSourceSize() {
        Drawable drawable = this.getDrawable();
        if (drawable != null) {
            mSourceMatrix.set(getImageMatrix());
            float[] values = new float[9];
            mSourceMatrix.getValues(values);
            mImageWidth = this.getDrawable().getIntrinsicWidth();
            mImageHeight = this.getDrawable().getIntrinsicHeight();
            Logger.i(TAG, "  getSourceSize  -------------------->" + mImageWidth + "   mImageHeight " + mImageHeight);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector != null) {
            mDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);
        }
        if (mImageHeight == 0) {
            getSourceSize();
            if (mImageHeight == 0) {
                return super.onTouchEvent(event);
            }
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isDragMode = true;
                isInterceptParentEvent(isUseZoom());
                lastX = event.getX();
                lastY = event.getY();
                isMatrixScaleType();
                if (mOnControlChangeListener != null) {
                    mOnControlChangeListener.onControlStart();
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isInterceptParentEvent(isUseZoom());
                resetToSourceSize(true);
                if (mOnControlChangeListener != null) {
                    mOnControlChangeListener.onControlStop();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDragMode) {
                    double dx = event.getX() - lastX;
                    double dy = event.getY() - lastY;
                    if (mOnControlChangeListener != null) {
                        RectF rectF = getDisplayRect(mControlMatrix);
                        if(rectF != null) {
                            mOnControlChangeListener.onDrag(rectF.width() - getWidth(), rectF.height() - getHeight(), (float) dx, (float) dy);
                        }
                    }
//                    Logger.i(TAG, "setDragTranslate-  dx " + dx + " dy  " + dy);
                    if (isUseZoom() && Math.sqrt(dx * dx + dy * dy) > 10f) {
                        mControlMatrix.set(getImageMatrix());
                        float[] values = new float[9];
                        mControlMatrix.getValues(values);
                        dx = checkDxBound(values, dx);
                        dy = checkDyBound(values, dy);
                        setDragTranslate((float) dx, (float) dy);
                    }
                    lastX = event.getX();
                    lastY = event.getY();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                isDragMode = false;
                break;
            default:
                break;
        }

        return true;
    }


    public void isInterceptParentEvent(boolean isInterept) {
        ViewParent parent = this.getParent();
        if (parent != null) {
            Logger.i(TAG, "isInterceptParentEvent " + isInterept);
            parent.requestDisallowInterceptTouchEvent(isInterept);
        }
    }

    public void setDragTranslate(float dx, float dy) {
        if (isUseZoom()) {
            //确保事件不和父容器冲突
            if (dx == 0) {
                isInterceptParentEvent(false);
            } else {
                isInterceptParentEvent(true);
            }
            mControlMatrix.postTranslate(dx, dy);
            setImageMatrix(mControlMatrix);
        }
    }


    /**
     * 扩展屏滑动控制
     *
     * @param controlViewMaxWidthMargin  主屏的图片放大后的当移动到最边界处， 超出部分的宽度
     * @param controlViewMaxHeightMargin 主屏的图片放大后的当移动到最边界处， 超出部分的高度
     * @param dx                         x滑动值 by
     * @param dy                         y滑动之 by
     */
    public void setControlDragTranslate(double controlViewMaxWidthMargin, double controlViewMaxHeightMargin, double dx, double dy) {
        if (isUseZoom()) {
            if (mImageWidth == 0) {
                getSourceSize();
            }

            //计算滑动宽高比
            RectF rectF = getDisplayRect(mControlMatrix);
            if(rectF == null) {
                return;
            }
            double maxWidthMargin = rectF.width() - getWidth();
            double maxHeightMagin = rectF.height() - getHeight();
            double widthPercent = 1.d;
            double heightPercent = 1.d;
            if (controlViewMaxWidthMargin > 0 && maxWidthMargin > 0) {
                widthPercent = maxWidthMargin / controlViewMaxWidthMargin;
            }
            if (controlViewMaxHeightMargin > 0 && maxHeightMagin > 0) {
                heightPercent = maxHeightMagin / controlViewMaxHeightMargin;
            }
//            Logger.i(TAG, "maxLeft " + maxWidthMargin + "  widthPercent " + controlWidth + "  " + widthPercent);
            mControlMatrix.set(getImageMatrix());
            float[] values = new float[9];
            mControlMatrix.getValues(values);
            //检查滑动边界
            double transX = checkDxBound(values, dx * widthPercent);
            double transY = checkDyBound(values, dy * heightPercent);
            mControlMatrix.postTranslate((float) transX, (float) transY);
            setImageMatrix(mControlMatrix);
        }
    }

    private boolean isUseZoom() {
        float[] values = new float[9];
        getImageMatrix().getValues(values);
        float sourceScale = values[Matrix.MSCALE_X];
        mSourceMatrix.getValues(values);
        return sourceScale != values[Matrix.MSCALE_X];
    }


    private double checkDyBound(float[] values, double dy) {
        double height = getHeight();
        if (mImageHeight * values[Matrix.MSCALE_Y] < height) {
            return 0;
        }
//        Logger.i("checkdy", "source " + mImageHeight + "   " + values[Matrix.MTRANS_Y] + "  width  " + height);
        if (values[Matrix.MTRANS_Y] + dy > 0) {
            dy = -values[Matrix.MTRANS_Y];
        } else if (values[Matrix.MTRANS_Y] + dy < -(mImageHeight * values[Matrix.MSCALE_Y] - height)) {
            dy = -(mImageHeight * values[Matrix.MSCALE_Y] - height) - values[Matrix.MTRANS_Y];
        }
        return dy;
    }


    private double checkDxBound(float[] values, double dx) {
        double width = getWidth();
        if (mImageWidth * values[Matrix.MSCALE_X] < width) {
            return 0;
        }
        if (values[Matrix.MTRANS_X] + dx > 0) {
            dx = -values[Matrix.MTRANS_X];
        } else if (values[Matrix.MTRANS_X] + dx < -(mImageWidth * values[Matrix.MSCALE_X] - width)) {
            dx = -(mImageWidth * values[Matrix.MSCALE_X] - width) - values[Matrix.MTRANS_X];
        }
        return dx;
    }


    public void startZoomImage(double scale, double centerX, double centerY) {
        mControlMatrix.set(getImageMatrix());
        float[] values = new float[9];
        mControlMatrix.getValues(values);
        if (scale * values[Matrix.MSCALE_X] > mMaxScale) {
            scale = mMaxScale / values[Matrix.MSCALE_X];
        }
        mControlMatrix.postScale((float) scale, (float) scale, (float) centerX, (float) centerY);
        checkMatrixBounds();
        setImageMatrix(mControlMatrix);
    }


    public void startZoomImage(double scale) {
        if (mImageWidth == 0) {
            getSourceSize();
        }
        mControlMatrix.set(getImageMatrix());
        float[] values = new float[9];
        mControlMatrix.getValues(values);
        if (scale * values[Matrix.MSCALE_X] > mMaxScale) {
            scale = mMaxScale / values[Matrix.MSCALE_X];
        }
        mControlMatrix.postScale((float) scale, (float) scale, this.getWidth() / 2, this.getHeight() / 2);
        checkMatrixBounds();
        setImageMatrix(mControlMatrix);
    }


    private RectF getDisplayRect(Matrix matrix) {
        Drawable d = this.getDrawable();
        if (d != null) {
            mDisplayRect.set(0, 0, d.getIntrinsicWidth(),
                    d.getIntrinsicHeight());
            matrix.mapRect(mDisplayRect);
            return mDisplayRect;
        }
        return null;
    }


    /**
     * 计算移动边界， 主要是在移动后缩小的时候保持往view中心点缩放
     *
     * @return
     */
    private boolean checkMatrixBounds() {
        final RectF rect = getDisplayRect(mControlMatrix);
        if (rect == null) {
            return false;
        }
        final double height = rect.height(), width = rect.width();
        double deltaX = 0, deltaY = 0;
        final int viewHeight = this.getHeight();
        if (height <= viewHeight) {
            switch (mScaleType) {
                case FIT_START:
                    deltaY = -rect.top;
                    break;
                case FIT_END:
                    deltaY = viewHeight - height - rect.top;
                    break;
                default:
                    deltaY = (viewHeight - height) / 2 - rect.top;
                    break;
            }
        } else if (rect.top > 0) {
            deltaY = -rect.top;
        } else if (rect.bottom < viewHeight) {
            deltaY = viewHeight - rect.bottom;
        }
        final int viewWidth = this.getWidth();
        if (width <= viewWidth) {
            switch (mScaleType) {
                case FIT_START:
                    deltaX = -rect.left;
                    break;
                case FIT_END:
                    deltaX = viewWidth - width - rect.left;
                    break;
                default:
                    deltaX = (viewWidth - width) / 2 - rect.left;
                    break;
            }
        } else if (rect.left > 0) {
            deltaX = -rect.left;
        } else if (rect.right < viewWidth) {
            deltaX = viewWidth - rect.right;
        }
        mControlMatrix.postTranslate((float) deltaX, (float) deltaY);
        return true;
    }


    /**
     * @param isCheck 是否检测drawable大小< view大小进行还原最初尺寸的操作, false 表示不检测直接还原
     */
    public void resetToSourceSize(boolean isCheck) {
        if (isNeedReset() || (!isCheck && isUseZoom())) {
            mControlMatrix.set(mSourceMatrix);
            setImageMatrix(mControlMatrix);
        }
    }


    private boolean isNeedReset() {
        float[] values = new float[9];
        getImageMatrix().getValues(values);
        float scale = values[Matrix.MSCALE_X];
        mSourceMatrix.getValues(values);
        return scale < values[Matrix.MSCALE_X];
    }

    /**
     * 修改imageview的Matrix属性
     */
    public void isMatrixScaleType() {
        if (getScaleType() != ScaleType.CENTER) {
            setScaleType(ScaleType.MATRIX);
        }
    }


    public void onDoubleTapScale(float current, float target) {
        mControlMatrix.set(mSourceMatrix);
        this.post(new AnimatedZoomRunnable(current, target,
                getWidth() / 2, getHeight() / 2));
        setImageMatrix(mControlMatrix);
    }

    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    /**
     * 双击缩放动画
     */
    private class AnimatedZoomRunnable implements Runnable {
        int mZoomDuration = 200;
        private final double mFocalX, mFocalY;
        private final long mStartTime;
        private final double mZoomStart, mZoomEnd;

        public AnimatedZoomRunnable(final float currentZoom, final float targetZoom,
                                    final float focalX, final float focalY) {
            mFocalX = focalX;
            mFocalY = focalY;
            mStartTime = System.currentTimeMillis();
            mZoomStart = currentZoom;
            mZoomEnd = targetZoom;
        }

        @Override
        public void run() {
            double t = interpolate();
            double scale = mZoomStart + t * (mZoomEnd - mZoomStart);
            double deltaScale = scale / getScale();
            startZoomImage(deltaScale, mFocalX, mFocalY);
            if (t < 1.d) {
                LinkageZoomView.this.postOnAnimation(this);
            }
        }

        private double interpolate() {
            double t = 1.d * (System.currentTimeMillis() - mStartTime) / mZoomDuration;
            t = Math.min(1.d, t);
            t = mInterpolator.getInterpolation((float) t);
            return t;
        }
    }

    public double getScale() {
        return Math.sqrt(Math.pow(getValue(mControlMatrix, Matrix.MSCALE_X), 2) + Math.pow(getValue(mControlMatrix, Matrix.MSKEW_Y), 2));
    }

    private final float[] mMatrixValues = new float[9];

    private double getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    public void clear() {
        mOnControlChangeListener = null;
        mDetector = null;
        mGestureDetector = null;
        mOnDoubleTapListener = null;
        mScaleListener = null;
        setImageBitmap(null);
        setBackground(null);
    }
}