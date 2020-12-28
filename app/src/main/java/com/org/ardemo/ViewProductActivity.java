package com.org.ardemo;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.idlestar.ratingstar.RatingStarView;
import com.org.ardemo.fragments.ProductDetailsFragment;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import static com.org.ardemo.DemoUtils.format;

public class ViewProductActivity extends AppCompatActivity {
    MaterialButton chatNow, ar, addToCart, buyNow, returnPolicy, freeShipping, authentic;
    ImageView img, arIcon;
    TextView title, discountValue, oldPrice, newPrice, itemsSold, fixedShopName;
    ToggleButton toggleDetails, toggleReviews, toggleDiscounts;
    ScrollView scrollView;
    ConstraintLayout fixedTopPanel;
    View fixedTopPanelBackground;
    RatingStarView shopRating;
    ImageButton backTopButton, shareTopButton, moreTopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_page_layout);

        Product product = (Product) getIntent().getParcelableExtra("product");
        chatNow = findViewById(R.id.chatNow);
        ar = findViewById(R.id.ar);
        addToCart = findViewById(R.id.addToCart);
        buyNow = findViewById(R.id.buyNow);
        returnPolicy = findViewById(R.id.returnPolicy);
        freeShipping = findViewById(R.id.freeShipping);
        authentic = findViewById(R.id.authentic);
        title = findViewById(R.id.title);
        oldPrice = findViewById(R.id.oldPrice);
        newPrice = findViewById(R.id.newPrice);
        discountValue = findViewById(R.id.discountValue);
        img = findViewById(R.id.image);
        arIcon = findViewById(R.id.arIcon);
        itemsSold = findViewById(R.id.itemsSold);
        //shopRating = (RatingStarView) findViewById(R.id.shopRating);
        toggleDetails = findViewById(R.id.toggleDetails);
        toggleReviews = findViewById(R.id.toggleReviews);
        toggleDiscounts = findViewById(R.id.toggleDiscounts);
        fixedTopPanel = findViewById(R.id.fixedTopPanel);
        fixedTopPanelBackground = findViewById(R.id.fixedTopPanelBackground);
        fixedShopName = findViewById(R.id.fixedShopName);
        scrollView = findViewById(R.id.scrollView);
        backTopButton = findViewById(R.id.backTopButton);
        shareTopButton = findViewById(R.id.shareTopButton);
        moreTopButton = findViewById(R.id.moreTopButton);

        displayDetails(product);
        toggleDetails.setChecked(true);

        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        AssetManager assetManager = getAssets();
        try{
            String path = "product-images/"+product.getImg();
            InputStream is = assetManager.open(path);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            img.setImageBitmap(bitmap);
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        title.setText(product.getTitle());
        fixedShopName.setText(product.getTitle());
        if(product.getOldPrice() == 0){
            oldPrice.setVisibility(View.GONE);
            discountValue.setVisibility(View.GONE);
        }
        else{
            oldPrice.setPaintFlags(oldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG); //strike through
            oldPrice.setText("$"+String.format("%.2f",product.getOldPrice()));
            double discount =  product.getDiscountAmount() * 100; // in %
            String text = "<font color=#DC593B>"+(int)discount+"%<br/></font> <font color=#ffffff>OFF</font>";
            discountValue.setText(Html.fromHtml(text));
            discountValue.setElevation(2);
            discountValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            discountValue.setBackgroundResource(R.drawable.ic_discount_label);
        }
        newPrice.setText("$"+String.format("%.2f",product.getNewPrice()));
        itemsSold.setText(format(product.getItemsSold()) + " sold");
        if(!product.hasAR()) ar.setVisibility(View.GONE);
        //shopRating.setRating(product.getShopRating());

        toggleDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleDetails.isChecked()) displayDetails(product);
            }
        });
        toggleReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleReviews.isChecked()){
                    toggleDetails.setChecked(false);
                    toggleDiscounts.setChecked(false);
                }
            }
        });
        toggleDiscounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleDiscounts.isChecked()) {
                    toggleDetails.setChecked(false);
                    toggleReviews.setChecked(false);
                    Log.d("TEST","TEST");
                }
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = Math.max(Math.min(scrollView.getScrollY(), 500),0); // For ScrollView
                float percentage = (scrollY/500.0 < 1)?(scrollY/500.0f):1.0f;
                fixedTopPanelBackground.setAlpha(percentage);
                fixedShopName.setAlpha(percentage);
                double redGrad = (255 - 190)/500.0;
                double greenGrad = (255 - 40)/500.0;
                double blueGrad = (255 - 30)/500.0;
                int newColor = Color.rgb((int)(255 - redGrad*scrollY), (int)(255 - greenGrad*scrollY), (int)(255 - blueGrad*scrollY));
                backTopButton.getDrawable().setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                shareTopButton.getDrawable().setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                moreTopButton.getDrawable().setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                double gradient = 38.25/500;
                newColor = Color.argb((int)(38.25 - gradient*scrollY),0,0,0);
                backTopButton.setBackground(drawCircle(newColor));
                shareTopButton.setBackground(drawCircle(newColor));
                moreTopButton.setBackground(drawCircle(newColor));


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    public void displayDetails(Product product){
        toggleReviews.setChecked(false);
        toggleDiscounts.setChecked(false);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",product);
        Fragment fragment = new ProductDetailsFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.productInfo, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    public static GradientDrawable drawCircle(int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        shape.setColor(backgroundColor);
        return shape;
    }
}
