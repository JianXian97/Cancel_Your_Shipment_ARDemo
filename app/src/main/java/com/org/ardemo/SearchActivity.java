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

    private boolean isFilterFragmentShown = false;
    Fragment filterFragment;
    FragmentManager fragmentManager;
    int width;
    View backgroundBlur;
    private String[] filelist;
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


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        backgroundBlur = findViewById(R.id.filterFragmentLayoutBlur);
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
                isFilterFragmentShown = true;
                backgroundBlur.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onTouchEvent ( MotionEvent event ){
        // I only care if the event is an UP action
        if ( event.getAction () == MotionEvent.ACTION_UP )
        {
            Log.d("TEST", "X " + event.getX() + " Y" + event.getY());
            Log.d("TEST 2", "X " + ((event.getX() + width/2) % width) + " Y" + event.getY());
            //and only is the ListFragment shown.
            if (isFilterFragmentShown)
            {
                // create a rect for storing the fragment window rect
                Rect r = new Rect( 0, 0, 0, 0 );
                // retrieve the fragment's windows rect
                filterFragment.getView().getHitRect(r);
                // check if the event position is inside the window rect
//                boolean intersects = r.contains ((int)(event.getX()), (int) event.getY());
                boolean intersects = r.contains ((int)(width - event.getX()), (int) event.getY());

                // if the event is not inside then we can close the fragment
                if (!intersects) {
                    fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(filterFragment).commit();
                    backgroundBlur.setVisibility(View.INVISIBLE);
                    isFilterFragmentShown = false;

                    // notify that we consumed this event
                    return true;
                }
            }
        }
        //let the system handle the event
        return super.onTouchEvent ( event );
    }

    private ArrayList<Product> prepareData(){
        ArrayList<Product> productLists = new ArrayList<>();
        for(int i = 0; i< filelist.length; i++){
            Product product = new Product("HOUZE - Diato", filelist[i], 11.80, 5.90, true, false, 4.0f,1800, true);
            productLists.add(product);
        }
        return productLists;
    }
}
