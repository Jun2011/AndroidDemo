package jun.viewdraghelper;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Jun on 2016/8/3.
 */
public class CustomViewGroup extends LinearLayout {

    private static final String TAG = "CustomViewGroup";

    private View view_one, view_two;
    private ViewDragHelper mDragHelper;

    public CustomViewGroup(Context context) {
        this(context, null);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 创建ViewDragHelper实例
        // 第1个参数传入当前我们自定义的ViewGroup
        // 第2个参数传入敏感度的值
        // 第3个参数传入Callback
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
        // 设置触摸屏幕边界的哪条边，这里是左边。
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    class DragHelperCallback extends ViewDragHelper.Callback {
        /**
         * 捕获子View
         *
         * @param child
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            // 可以根据child判断是否执行捕获
            return true;
        }

        /**
         * 决定子View水平方向的位置
         *
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.d(TAG, "clampViewPositionHorizontal: " + left + "," + dx);
            // 防止子View滑出屏幕边界
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - view_one.getWidth();
            final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            return newLeft;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.d(TAG, "clampViewPositionVertical: " + top + "," + dy);
            // 子防止View滑出屏幕边界
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - view_one.getHeight();
            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;
        }

        /**
         * 触摸屏幕边界时回调
         *
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            Toast.makeText(getContext(), "left edge touched", Toast.LENGTH_SHORT).show();
        }

        /**
         * 触摸屏幕边界拖拽时调用
         *
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            // 设置触摸屏幕边界滑动时捕获子View
            mDragHelper.captureChildView(view_two, pointerId);
        }

        /**
         * 当手指松开的时候回调
         *
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.d(TAG, "onViewReleased: ");
        }
    }

    /**
     * 拦截事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 设置由ViewDragHelper来拦截事件
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    /**
     * 处理事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 设置由ViewDragHelper来消费事件
        mDragHelper.processTouchEvent(event);
        return true;
    }

    /**
     * 当布局加载完成时回调
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 获取子View
        view_one = getChildAt(0);
        view_two = getChildAt(1);
    }
}
