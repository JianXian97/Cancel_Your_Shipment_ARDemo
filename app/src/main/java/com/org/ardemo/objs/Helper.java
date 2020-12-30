package com.org.ardemo.objs;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class Helper {

    public static void updatePagerHeightForChild(View view , ViewPager2 pager) {

        int wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
        int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(wMeasureSpec, hMeasureSpec);

        if (pager.getLayoutParams().height != view.getMeasuredHeight()) {
            ViewGroup.LayoutParams layout = pager.getLayoutParams();
            layout.height = view.getMeasuredHeight();
            pager.setLayoutParams(layout);
        }


    }
}
