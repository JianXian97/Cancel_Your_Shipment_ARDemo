package com.org.ardemo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.ardemo.fragments.ProductReviewFragment;
import com.org.ardemo.objs.Product;
import com.org.ardemo.objs.Review;
import com.org.ardemo.objs.Shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.org.ardemo.DemoUtils.convertDpToPixel;

public class SearchActivity extends AppCompatActivity{

    Fragment filterFragment;
    FragmentManager fragmentManager;
    public static int deviceWidth, deviceHeight;
    private String[] filelist;
    ImageView instructions, instructions2;
    ConstraintLayout searchBar, instructionPanel, instructionPanel2;

    Paint paint = new Paint();
    Paint transparentPaint = new Paint();

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

        Review reviewA = new Review("loremipsum.txt","Jane Doe", "reviewerA.png", "Blue", new String[]{"reviewA_1.png","reviewA_2.png"},4.5,new GregorianCalendar(2020, 12, 11).getTime());
        Review reviewB = new Review("loremipsum.txt","Alice Tan", "reviewerB.png", "Red", new String[]{"reviewB_1.png"},4.8,new GregorianCalendar(2020, 11, 12).getTime());
        Review reviewC = new Review("loremipsum.txt","Bob Lim", "reviewerC.png", "Shiny", new String[]{"reviewC_1.png","reviewC_2.png","reviewC_3.png"},4.6,new GregorianCalendar(2020, 12, 28).getTime());
        Review reviewD = new Review("loremipsum.txt","John Doe", "reviewerD.png", "Cool", new String[]{"reviewD_1.png"},2.5,new GregorianCalendar(2020, 12, 2).getTime());


        Product productD = new Product("CHAHIN_WOODEN_CHAIR", filelist[3], "HOUSE",342.00, 184.50, true, false, 3.0f,100, 25,true, new Product[]{}, new String[]{"Brown","Blue"},new Review[]{}, shopA);
        Product productE = new Product("High Top Chair", filelist[4], "HOUSE",392.00, 300.00, true, false, 3.0f,850, 35,true, new Product[]{}, new String[]{"Black","White"},new Review[]{}, shopA);
        Product productF = new Product("Chair", filelist[5], "HOUSE",602.00, 568.30, true, false, 5.0f,760, 205,true, new Product[]{}, new String[]{"Green","White","Purple"},new Review[]{}, shopA);
        Product productA = new Product("Earth", filelist[1], "Citylife",211.60, 65.70, true, false, 4.0f,2930, 406,true, new Product[]{}, new String[]{"Green","Red"}, new Review[]{reviewB, reviewC}, shopB);
        Product productB = new Product("efit 10", filelist[0], "HOUSE",86.80, 5.90, true, false, 3.0f,360, 65,true, new Product[]{productD,productE,productF}, new String[]{"Silver","Blue"},new Review[]{reviewA}, shopA);
        Product productC = new Product("Paper Airplane", filelist[2], "Best Shop",2.00, 0.99, false, true, 5.0f,1999, 23,true, new Product[]{}, new String[]{"White","Black"}, new Review[]{reviewD}, shopC);


        ArrayList<Product> productList = new ArrayList<>();
        productList.add(productA);
        productList.add(productB);
        productList.add(productC);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;
        deviceHeight = displayMetrics.heightPixels;

        paint.setColor(0xcc000000);
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
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

        searchBar = findViewById(R.id.search_bar);

        RecyclerView recyclerView = findViewById(R.id.searchResult);
        recyclerView.setHasFixedSize(true);

        instructions = findViewById(R.id.instructions);
        instructions2 = findViewById(R.id.instructions2);
        instructionPanel = findViewById(R.id.instructionPanel);
        instructionPanel2 = findViewById(R.id.instructionPanel2);

        instructionPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instructionPanel.setVisibility(View.INVISIBLE);
                instructionPanel2.setVisibility(View.VISIBLE);
                displayInstructions2(recyclerView);
            }
        });

        instructionPanel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instructionPanel2.setVisibility(View.INVISIBLE);
            }
        });
        //recyclerView.setLayoutManager(layoutManager);

        SearchAdapter searchResultAdapter = new SearchAdapter(getApplicationContext(), productList);
        recyclerView.setAdapter(searchResultAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);

        recyclerView.setLayoutManager(layoutManager);


        instructions.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                displayInstructions1(recyclerView);
            }
        });

    }


    public void displayInstructions1(RecyclerView recyclerView){
        Bitmap bg = Bitmap.createBitmap((int)deviceWidth, (int)deviceHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        SearchAdapter.ViewHolder child1 = (SearchAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(0);
        SearchAdapter.ViewHolder child2 = (SearchAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(1);
        int[] pos1 = new int[2];
        int[] pos2 = new int[2];
        child1.ar.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        child1.ar.getLocationInWindow(pos1);
        child2.ar.getLocationInWindow(pos2);

        searchBar.measure(
                View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


        int radius = (int)convertDpToPixel(20,SearchActivity.this);
        canvas.drawColor(0x80000000);
        float hBias = (pos1[0] + child1.ar.getMeasuredWidth()/2.0f);
        float vBias = (pos1[1] - child1.ar.getMeasuredHeight()/2.0f);
        canvas.drawCircle(hBias, vBias, radius, transparentPaint);
        hBias = (pos2[0] + child1.ar.getMeasuredWidth()/2.0f);
        canvas.drawCircle(hBias, vBias, radius, transparentPaint);
        canvas.drawRect(0,0,deviceWidth,searchBar.getMeasuredHeight(),transparentPaint);
        canvas.drawBitmap(bg, 0, 0, paint);
        instructions.setImageBitmap(bg);
    }

    public void displayInstructions2(RecyclerView recyclerView){
        Bitmap bg = Bitmap.createBitmap((int)deviceWidth, (int)deviceHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bg);
        SearchAdapter.ViewHolder child1 = (SearchAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(1);
        int[] pos = new int[2];

        child1.panel.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        child1.panel.getLocationOnScreen(pos);

        searchBar.measure(
                View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        canvas.drawColor(0x80000000);
        canvas.drawRect(0,0,deviceWidth,searchBar.getMeasuredHeight(),transparentPaint);
        canvas.drawRect(pos[0],pos[1] - 75,pos[0] + child1.panel.getMeasuredWidth() - 25,pos[1] + child1.panel.getMeasuredHeight() - 80,transparentPaint); //manually adjusted
        canvas.drawBitmap(bg, 0, 0, paint);
        instructions2.setImageBitmap(bg);
        Log.d("TEST","TESTing123");
    }

}
