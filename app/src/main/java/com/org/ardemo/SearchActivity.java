package com.org.ardemo;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.ardemo.objs.Product;
import com.org.ardemo.objs.Review;
import com.org.ardemo.objs.Shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SearchActivity extends AppCompatActivity{

    Fragment filterFragment;
    FragmentManager fragmentManager;
    int width;
    private String[] filelist;
    ArrayList<Shop> ShopList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_layout);
        AssetManager aMan = this.getAssets();
        try {
            filelist = aMan.list("product-images");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Fake data to populate the various activities*/
        Shop shopA = new Shop("company_logo_1.png", "Houze", "4 Changi South Lane", 17, 420,73,4.8);
        Shop shopB = new Shop("company_logo_2.png", "Repoe", "54 Bayfront Avenue Block 12", 23, 982,99,4.7);
        Shop shopC = new Shop("company_logo_3.jpg", "Origami", "Prince Edward Avenue ",10, 77,89,4.5);

        Review reviewA = new Review("loremipsum.txt","Jane Doe", "reviewerA.png", "None", new String[]{"reviewA_1.png","reviewA_2.png"},4.5,new GregorianCalendar(2020, 12, 11).getTime());
        Review reviewB = new Review("loremipsum.txt","Alice Tan", "reviewerB.png", "Red", new String[]{"reviewB_1.png"},4.8,new GregorianCalendar(2020, 11, 12).getTime());
        Review reviewC = new Review("loremipsum.txt","Bob Lim", "reviewerC.png", "Shiny", new String[]{"reviewC_1.png","reviewC_2.png","reviewC_2.png"},4.6,new GregorianCalendar(2020, 12, 28).getTime());
        Review reviewD = new Review("loremipsum.txt","John Doe", "reviewerD.png", "Cool", new String[]{"reviewD_1.png"},2.5,new GregorianCalendar(2020, 12, 2).getTime());

        Product productA = new Product("HOUZE - Diato", filelist[0], "HOUSE",86.80, 5.90, true, false, 3.0f,360, 65,false, new Review[]{reviewA}, shopA);
        Product productB = new Product("Citylife Chait", filelist[1], "Citylife",211.60, 65.70, true, false, 4.0f,2930, 406,true, new Review[]{reviewB, reviewC}, shopB);
        Product productC = new Product("Best Shop Plane", filelist[2], "Best Shop",2.00, 0.99, false, true, 5.0f,1999, 23,true, new Review[]{reviewD}, shopC);

        ArrayList<Product> productList = new ArrayList<>();
        productList.add(productA);
        productList.add(productB);
        productList.add(productC);

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

        SearchAdapter searchResultAdapter = new SearchAdapter(getApplicationContext(), productList);
        recyclerView.setAdapter(searchResultAdapter);
    }


}
