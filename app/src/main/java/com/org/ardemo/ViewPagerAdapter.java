package com.org.ardemo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.org.ardemo.fragments.ProductDetailsFragment;
import com.org.ardemo.fragments.ProductDiscountsFragment;
import com.org.ardemo.fragments.ProductReviewFragment;
import com.org.ardemo.objs.Product;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 3;
    Product product;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Product product) {
        super(fragmentActivity);
        this.product = product;
    }
    @NonNull
    @Override
    public Fragment createFragment(int Position) {
        switch(Position){
            case 0:
                Log.e("PAGER ADAPTER", "position " + Position + " is called");
                return ProductDetailsFragment.newInstance(Position, product);
            case 1:
                Log.e("PAGER ADAPTER", "position " + Position + " is called");
                return ProductReviewFragment.newInstance(Position, product);
            case 2:
                Log.e("PAGER ADAPTER", "position " + Position + " is called");
                return ProductDiscountsFragment.newInstance(Position, product);
        }
        return ProductReviewFragment.newInstance(Position, product);
    }

    @Override public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}