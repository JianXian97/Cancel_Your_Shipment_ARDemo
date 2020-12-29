package com.org.ardemo.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.org.ardemo.CreateList;
import com.org.ardemo.CustomToggle;
import com.org.ardemo.ReviewsAdapter;
import com.org.ardemo.objs.Product;
import com.org.ardemo.R;
import com.org.ardemo.objs.Review;
import com.org.ardemo.objs.Shop;

import java.util.ArrayList;


public class ProductReviewFragment extends Fragment {
    private Integer pos;
    Product product;
    ConstraintLayout overall;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_review_fragment, container, false);

        //Product product = (Product) getArguments().get("data");
        Shop shop = product.getShop();
        overall = view.findViewById(R.id.overall);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

//        ThemedToggleButtonGroup buttonGroup1 = view.findViewById(R.id.topPanel);
        CustomToggle allReviews = view.findViewById(R.id.allReviews);
        CustomToggle withComment = view.findViewById(R.id.withComment);
        CustomToggle withMedia = view.findViewById(R.id.withMedia);
        CustomToggle fiveStars = view.findViewById(R.id.fiveStars);
        CustomToggle fourStars = view.findViewById(R.id.fourStars);
        CustomToggle threeStars = view.findViewById(R.id.threeStars);
        CustomToggle twoStars = view.findViewById(R.id.twoStars);
        CustomToggle oneStar = view.findViewById(R.id.oneStar);

        int minWidth =(int)((width*0.9)/3.0f);
        allReviews.setMinimumWidth(minWidth);
        withMedia.setMinimumWidth(minWidth);
        withComment.setMinimumWidth(minWidth);
        minWidth =(int)((width*0.9-20)/5.0f);
        fiveStars.setMinimumWidth(minWidth);
        fourStars.setMinimumWidth(minWidth);
        threeStars.setMinimumWidth(minWidth);
        twoStars.setMinimumWidth(minWidth);
        oneStar.setMinimumWidth(minWidth);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.reviews);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
 
        ReviewsAdapter adapter = new ReviewsAdapter(getActivity().getApplicationContext(), product.getReviews());
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutCompleted(final RecyclerView.State state) {
                super.onLayoutCompleted(state);
                recyclerView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
            }
        });

        return view;
    }

  

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public ProductReviewFragment(){}

    public static ProductReviewFragment newInstance(int pos, Product product) {
        ProductReviewFragment fragment = new ProductReviewFragment();
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
