package com.org.ardemo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.org.ardemo.objs.Product;
import com.org.ardemo.R;
import com.org.ardemo.objs.Shop;

import static com.org.ardemo.SearchActivity.deviceWidth;


public class ProductDiscountsFragment extends Fragment {
    private Integer pos;
    Product product;
    ConstraintLayout overall;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_discounts_fragment, container, false);
        //Product product = (Product) getArguments().get("data");
        Shop shop = product.getShop();
        overall = view.findViewById(R.id.overall);

        return view;
    }

    public int getHeight(){
        overall.measure(
                View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return overall.getMeasuredHeight();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public ProductDiscountsFragment(){}

    public static ProductDiscountsFragment newInstance(int pos, Product product) {
        ProductDiscountsFragment fragment = new ProductDiscountsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",product);
        bundle.putInt("pageNum", pos);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().get("data");
            pos = getArguments().getInt("pageNum");
        }
    }



}
