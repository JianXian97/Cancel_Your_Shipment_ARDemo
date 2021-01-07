package com.org.ardemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import androidx.recyclerview.widget.RecyclerView;

import com.org.ardemo.objs.GalleryImageObj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import com.org.ardemo.SquareImageView;

import static com.org.ardemo.DemoUtils.cropToSquare;
import static com.org.ardemo.SearchActivity.deviceWidth;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<GalleryImageObj> galleryList;
    private ArrayList<Uri> uriList;
    private Context context;
    private Activity activity;
    public static final int REQUESTCODE = 100;

    public GalleryAdapter(Context context, ArrayList<GalleryImageObj> galleryList, ArrayList<Uri> imgUriList, Activity activity) {
        this.galleryList = galleryList;
        this.context = context;
        this.uriList = imgUriList;
        this.activity = activity;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder viewHolder, int i) {

       // viewHolder.title.setText(galleryList.get(i).getImage_title());

        File path =new File(galleryList.get(i).getImage_URI().getPath());
        try {
            Bitmap image = BitmapFactory.decodeStream(new FileInputStream(path));
            Bitmap croppedImage = cropToSquare(image);
            viewHolder.img.setImageBitmap(croppedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        viewHolder.img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), ViewImageActivity.class);
                int pos = viewHolder.getLayoutPosition();
                myIntent.putExtra("imgID", uriList.get(pos).toString());
                myIntent.putExtra("position", i);
                activity.startActivityForResult(myIntent, REQUESTCODE);
            }
        });
    }

    public void removeAt(int pos){
        galleryList.remove(pos);
        uriList.remove(pos);
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
//        private TextView title;
        private SquareImageView img;
        public ViewHolder(View view) {
            super(view);
//            title = (TextView)view.findViewById(R.id.title);
            img =  view.findViewById(R.id.img);
        }
    }

}