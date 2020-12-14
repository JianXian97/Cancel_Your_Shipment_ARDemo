package com.org.ardemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.org.ardemo.R;

import java.util.ArrayList;


public class GalleryActivity  extends AppCompatActivity {

    private ArrayList<Uri> imgUriList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imgUriList = (ArrayList<Uri>) getIntent().getSerializableExtra("imgUriList");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<CreateList> createLists = prepareData();
        GalleryAdapter adapter = new GalleryAdapter(getApplicationContext(), createLists, imgUriList);
        recyclerView.setAdapter(adapter);


    }
    private ArrayList<CreateList> prepareData(){

        ArrayList<CreateList> theimage = new ArrayList<>();
        for(int i = 0; i< imgUriList.size(); i++){
            CreateList createList = new CreateList();
            createList.setImage_title("Img " + (i+1));
            //createList.setImage_ID(image_ids[i]);
            createList.setImage_URI(imgUriList.get(i));
            theimage.add(createList);
        }
        return theimage;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent i= new Intent(GalleryActivity.this,MainActivity.class);
        startActivity(i);
        finish();

    }
}