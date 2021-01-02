package com.org.ardemo;

import android.content.Context;

import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapted from com.github.adityagohad:HorizontalPicker:1.0.1
 **/

public class CustomLayoutManager extends LinearLayoutManager {

    private float scaleDownBy = 0.66f;
    private float scaleDownDistance = 0.9f;
    private boolean changeAlpha = true;

    private onScrollStopListener onScrollStopListener;

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scaleDownView();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            scaleDownView();
            return scrolled;
        } else return 0;
    }

    private void scaleDownView() {
        float mid = getWidth() / 2.0f;
        float scaleDownConst = 0.4f; // the ratio between the child's displacement from the screen centre and half of the screen width where the scale will become 0
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.0f;
//            float leftScale = 1.0f - (1/((scaleDownConst*mid)*(scaleDownConst*mid)))*(childMid-mid)*(childMid-mid);
//            float rightScale = 1.0f - (0.5f/getWidth())*(childMid - mid);
//            float finalScale = ( childMid < mid) ? Math.max(leftScale,0) : rightScale;
            float finalScale = 1.0f - Math.abs((0.5f/getWidth())*(childMid - mid));
            child.setScaleX(finalScale);
            child.setScaleY(finalScale);

        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == 0) {
            if (onScrollStopListener != null) {
                int selected = 0;
                float lastHeight = 0f;
                for (int i = 0; i < getChildCount(); i++) {
                    if (lastHeight < getChildAt(i).getScaleY()) {
                        lastHeight = getChildAt(i).getScaleY();
                        selected = i;
                    }
                }
                onScrollStopListener.selectedView(getChildAt(selected));
            }
        }
    }


    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
                return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
            }
        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public float getScaleDownBy() {
        return scaleDownBy;
    }

    public void setScaleDownBy(float scaleDownBy) {
        this.scaleDownBy = scaleDownBy;
    }

    public float getScaleDownDistance() {
        return scaleDownDistance;
    }

    public void setScaleDownDistance(float scaleDownDistance) {
        this.scaleDownDistance = scaleDownDistance;
    }

    public boolean isChangeAlpha() {
        return changeAlpha;
    }

    public void setChangeAlpha(boolean changeAlpha) {
        this.changeAlpha = changeAlpha;
    }

    public void setOnScrollStopListener(onScrollStopListener onScrollStopListener) {
        this.onScrollStopListener = onScrollStopListener;
    }

    public interface onScrollStopListener {
        public void selectedView(View view);
    }
}
