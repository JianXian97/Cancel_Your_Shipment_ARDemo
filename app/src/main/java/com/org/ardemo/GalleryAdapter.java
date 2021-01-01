package com.org.ardemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import androidx.recyclerview.widget.RecyclerView;

import com.org.ardemo.objs.CreateList;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<CreateList> galleryList;
    private ArrayList<Uri> uriList;
    private Context context;
    private Activity activity;
    public static final int REQUESTCODE = 100;

    public GalleryAdapter(Context context, ArrayList<CreateList> galleryList, ArrayList<Uri> imgUriList, Activity activity) {
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

        viewHolder.title.setText(galleryList.get(i).getImage_title());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setImageURI(galleryList.get(i).getImage_URI());
//        viewHolder.img.setImageResource((galleryList.get(i).getImage_ID()));
        //Picasso.with(context).load(galleryList.get(i).getImage_ID()).resize(240, 120).into(viewHolder.img);
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
        private TextView title;
        private ImageView img;
        public ViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }

}