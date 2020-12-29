package com.org.ardemo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.idlestar.ratingstar.RatingStarView;
import com.org.ardemo.objs.Product;

import java.io.InputStream;

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
    ViewPager2 viewPager;
    String[] nameList = new String[]{"Details","Reviews","Discounts"};

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
        shopRating = (RatingStarView) findViewById(R.id.shopRatingLarge);
        fixedTopPanel = findViewById(R.id.fixedTopPanel);
        fixedTopPanelBackground = findViewById(R.id.fixedTopPanelBackground);
        fixedShopName = findViewById(R.id.fixedShopName);
        scrollView = findViewById(R.id.scrollView);
        backTopButton = findViewById(R.id.backTopButton);
        shareTopButton = findViewById(R.id.shareTopButton);
        moreTopButton = findViewById(R.id.moreTopButton);

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
        if(!product.hasAR()) arIcon.setVisibility(View.GONE);
        shopRating.setRating(product.getShopRating());



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
        viewPager = findViewById(R.id.productInfo);

        viewPager.setAdapter(createCardAdapter(product));
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(nameList[position])
        ).attach();


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected (int position){
                super.onPageSelected(position);
                //getAdapter().notifyDataSetChanged();
                //ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
                scrollView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
//                getSupportFragmentManager().executePendingTransactions();
//                getFragmentManager().executePendingTransactions();
//                Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("f"+position);
//
//                if(currentFragment!=null){
//                    View view = currentFragment.getView();
//                    Log.e("TEST","234");
//                    //View view = adapter.list.get(position).getView();
//                    if(view!=null && viewPager.getLayoutParams().height != view.getMeasuredHeight()){
//                        viewPager.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
//                    }
//                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }




    public static GradientDrawable drawCircle(int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        shape.setColor(backgroundColor);
        return shape;
    }

    private ViewPagerAdapter createCardAdapter(Product product) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this,product);
        return adapter;
    }

}
