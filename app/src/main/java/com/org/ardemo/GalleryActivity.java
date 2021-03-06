package com.org.ardemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.org.ardemo.objs.GalleryImageObj;
import com.org.ardemo.objs.Product;

import java.io.File;
import java.util.ArrayList;


public class GalleryActivity  extends AppCompatActivity {

    private ArrayList<Uri> imgUriList;
    private Product product;
    RecyclerView recyclerView;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imgUriList = (ArrayList<Uri>) getIntent().getSerializableExtra("imgUriList");
        product = (Product) getIntent().getExtras().getParcelable("product");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);

        recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<GalleryImageObj> createLists = prepareData();
        GalleryAdapter adapter = new GalleryAdapter(getApplicationContext(), createLists, imgUriList,GalleryActivity.this);
        recyclerView.setAdapter(adapter);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }
    private ArrayList<GalleryImageObj> prepareData(){

        ArrayList<GalleryImageObj> theimage = new ArrayList<>();
        for(int i = 0; i< imgUriList.size(); i++){
            GalleryImageObj createList = new GalleryImageObj();
            //createList.setImage_title("Img " + (i+1));
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
        Intent i= new Intent(GalleryActivity.this, ARActivity.class);
        i.putExtra("product", product);
        startActivity(i);
        finish();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            int deletedPosition = (int)data.getExtras().get("deletedPosition");
            if(deletedPosition > -1) {
                Log.d("GALLERY", "DELETED " + deletedPosition);
                GalleryAdapter adapter = (GalleryAdapter) recyclerView.getAdapter();
                adapter.removeAt(deletedPosition);
                adapter.notifyItemRemoved(deletedPosition);
                adapter.notifyItemRangeChanged(deletedPosition, adapter.getItemCount() - 1);
            }
        }
    }


}