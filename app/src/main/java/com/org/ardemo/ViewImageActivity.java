package com.org.ardemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.org.ardemo.R;

import java.io.File;
import java.util.ArrayList;

public class ViewImageActivity extends AppCompatActivity {
    ImageButton deleteButton, shareButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imgID"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);
        ImageView imgView = findViewById(R.id.imageView);
        imgView.setImageURI(imageUri);
        deleteButton = findViewById(R.id.deleteButton);
        shareButton = findViewById(R.id.shareButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(imageUri.getPath());
                file.delete();
                ArrayList<Uri> imgUriList = getImgUriList();
                Intent myIntent = new Intent(v.getContext(), GalleryActivity.class);
                myIntent.putParcelableArrayListExtra("imgUriList", imgUriList); //Optional parameters
                startActivity(myIntent);
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public ArrayList<Uri> getImgUriList(){
        ArrayList<Uri> list = new ArrayList<>();
        File dir = new File(getFilesDir().getAbsolutePath() + File.separator + "/images");
        for(File f : dir.listFiles()) {
            if (f.isFile()) {
                list.add(Uri.fromFile(f));
            }
        }
        return list;
    }
}
