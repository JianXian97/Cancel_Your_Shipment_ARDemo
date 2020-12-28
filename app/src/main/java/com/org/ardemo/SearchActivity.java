package com.org.ardemo;

import android.content.res.AssetManager;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity{

    Fragment filterFragment;
    FragmentManager fragmentManager;
    int width;
    private String[] filelist;
    ArrayList<shop> ShopList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_layout);

        shop shopA = new shop("company_logo_1.png", "Houze", 17, 420,73,4.8);
        shop shopB = new shop("company_logo_2.png", "Repoe", 23, 982,99,4.7);
        shop shopC = new shop("company_logo_3.png", "Origami", 10, 77,89,4.5);

        ShopList.add(shopA);
        ShopList.add(shopB);
        ShopList.add(shopC);
        AssetManager aMan = this.getAssets();
        try {
            filelist = aMan.list("product-images");
        } catch (IOException e) {
            e.printStackTrace();
        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        Spinner dropdown = findViewById(R.id.sortDropdown);
        //create a list of items for the spinner.
        String[] items = new String[]{"Sort By", "Relevance", "Latest","Top Sales","Price"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        Button filterButton = findViewById(R.id.filterButton);
        FrameLayout fragmentFrame = findViewById(R.id.filterFragmentLayout);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterFragment = new FilterFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.filterFragmentLayout, filterFragment, filterFragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.searchResult);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Product> productList = prepareData();
        SearchAdapter searchResultAdapter = new SearchAdapter(getApplicationContext(), productList);
        recyclerView.setAdapter(searchResultAdapter);
    }

    private ArrayList<Product> prepareData(){
        ArrayList<Product> productLists = new ArrayList<>();
        for(int i = 0; i< filelist.length; i++){
            Product product = new Product("HOUZE - Diato", filelist[i], 11.80, 5.90, true, false, 4.0f,1800, true);
            product.setShop(ShopList.get(i));
            productLists.add(product);
        }
        return productLists;
    }
}
