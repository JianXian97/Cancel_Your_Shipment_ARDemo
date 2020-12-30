package com.org.ardemo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.org.ardemo.ViewProductActivity;
import com.org.ardemo.objs.Product;
import com.org.ardemo.R;
import com.org.ardemo.objs.Review;
import com.org.ardemo.objs.Shop;

import java.util.ArrayList;

import static com.org.ardemo.SearchActivity.deviceWidth;


public class ProductReviewFragment extends Fragment {
    private Integer pos;
    Product product;
    ConstraintLayout overall;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("PRODUCT REVIEWs","STARTINGGGGG!!!");
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
        String allButtonText = "<font color=#000000>All<br/></font> <font size = 10pt color=#BE2827>("+product.getReviews().length +")</font>";
        allReviews.setText(Html.fromHtml(allButtonText));
        String withCommentButtonText = "<font color=#000000>With Comment<br/></font> <font size = 10pt color=#BE2827>("+product.getReviews().length +")</font>";
        withComment.setText(Html.fromHtml(withCommentButtonText));
        String withMediaButtonText = "<font color=#000000>With Media<br/></font> <font size = 10pt color=#BE2827>("+product.getReviews().length +")</font>";
        withMedia.setText(Html.fromHtml(withMediaButtonText));
        int[] ratingCount = getRatingCount(product);
        String fiveStarsText = "<font color=#FFB81D>★★★★★<br/></font> <font size = 10pt color=#545454>("+ratingCount[4] +")</font>";
        fiveStars.setText(Html.fromHtml(fiveStarsText));
        String fourStarsText = "<font color=#FFB81D>★★★★<br/></font> <font size = 10pt color=#545454>("+ratingCount[3] +")</font>";
        fourStars.setText(Html.fromHtml(fourStarsText));
        String threeStarsText = "<font color=#FFB81D>★★★<br/></font> <font size = 10pt color=#545454>("+ratingCount[2] +")</font>";
        threeStars.setText(Html.fromHtml(threeStarsText));
        String twoStarsText = "<font color=#FFB81D>★★<br/></font> <font size = 10pt color=#545454>("+ratingCount[1] +")</font>";
        twoStars.setText(Html.fromHtml(twoStarsText));
        String oneStarText = "<font color=#FFB81D>★<br/></font> <font size = 1000pt color=#545454>("+ratingCount[0] +")</font>";
        oneStar.setText(Html.fromHtml(oneStarText));


        View viewPager = ((ViewProductActivity)getActivity()).findViewById(R.id.productInfo);
        View tabLayout = ((ViewProductActivity)getActivity()).findViewById(R.id.tab_layout);

        recyclerView = (RecyclerView)view.findViewById(R.id.reviews);
        recyclerView.setHasFixedSize(true);

        CustomGridLayoutManager layoutManager = new CustomGridLayoutManager(getActivity().getApplicationContext(),1);
        layoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
 
        ReviewsAdapter adapter = new ReviewsAdapter(getActivity().getApplicationContext(), product.getReviews());
        recyclerView.setAdapter(adapter);
        Log.e("PRODUCT REVIEWs","Product Review Fragment CREATED!");
        return view;
    }

    public int getHeight(){
        overall.measure(
                View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return overall.getMeasuredHeight();
    }

    private int[] getRatingCount(Product product){
        int[] result = new int[5];
        for(Review review: product.getReviews()){
            int rating = (int)Math.round(review.getRating());
            result[rating-1]++;
        }
        return result;
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
        Log.e("PRODUCT REVIEWs","0th STEP!!");
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().get("data");
            pos = getArguments().getInt("pageNum");
        }
        Log.e("PRODUCT REVIEWs","1st STEP!!");
    }

    public class CustomGridLayoutManager extends GridLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context,int span) {
            super(context,span);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }
}
