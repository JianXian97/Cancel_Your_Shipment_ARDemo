package com.org.ardemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.org.ardemo.R;
import com.org.ardemo.objs.Product;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewImageActivity extends AppCompatActivity {
    ImageButton deleteButton, shareButton, backButton, openGalleryButton;
    ArrayList<Uri> imgUriList;
    Product product;
    int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imgID"));
        product = (Product) getIntent().getExtras().getParcelable("product");
        int position = getIntent().getIntExtra("position",0);
        Log.e("Checking",imageUri.toString());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);
        ImageView imgView = findViewById(R.id.imageView);
        imgView.setImageURI(imageUri);


        deleteButton = findViewById(R.id.deleteButton);
        shareButton = findViewById(R.id.shareButton);
        backButton = findViewById(R.id.backButton);
        openGalleryButton = findViewById(R.id.openGallery);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(imageUri.getPath());
                file.delete();
                finish();
//                ArrayList<Uri> imgUriList = getImgUriList();
//                Intent myIntent = new Intent(v.getContext(), GalleryActivity.class);
//                myIntent.putParcelableArrayListExtra("imgUriList", imgUriList); //Optional parameters
//                startActivity(myIntent);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ShareButtonFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgUriList = getImgUriList();
                Intent myIntent = new Intent(ViewImageActivity.this, GalleryActivity.class);
                myIntent.putParcelableArrayListExtra("imgUriList", imgUriList); //Optional parameters
                myIntent.putExtra("product", product);
                startActivity(myIntent);
            }
        });

    }

    public ArrayList<Uri> getImgUriList(){
        ArrayList<Uri> list = new ArrayList<>();
        File dir = new File(getFilesDir().getAbsolutePath() + File.separator + "/images");

        if(!dir.exists()) dir.mkdirs();

        for(File f : dir.listFiles()) {
            if (f.isFile()) {
                list.add(Uri.fromFile(f));
            }
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("deletedPosition", position);
        // setResult(RESULT_OK);
        setResult(RESULT_OK, returnIntent); //By not passing the intent in the result, the calling activity will get null data.
        super.finish();
    }
}
