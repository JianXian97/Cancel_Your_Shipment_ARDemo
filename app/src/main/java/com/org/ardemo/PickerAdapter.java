package com.org.ardemo;

import android.content.Context;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.List;

import static com.org.ardemo.DemoUtils.convertDpToPixel;

public class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.ImageHolder> {

    private Context context;
    private String[] dataList;
    private RecyclerView recyclerView;

    public PickerAdapter(Context context, String[] dataList, RecyclerView recyclerView) {
        this.context = context;
        this.dataList = dataList;
        this.recyclerView = recyclerView;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.picker_item_layout, parent, false);
        return new PickerAdapter.ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, final int position) {

        if(position == 0){ //set a circle
            holder.pickerImg.setImageResource(R.drawable.bg_circle);
            holder.pickerImg.getLayoutParams().height = (int)convertDpToPixel(60,context);
            holder.pickerImg.setColorFilter(0xffffffff);
        }
        else{
            AssetManager assetManager = context.getAssets();
            try{
                int height = (int)convertDpToPixel(60,context);
                String path = "ar-images/"+dataList[position];
                InputStream is = assetManager.open(path);
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                holder.pickerImg.setImageBitmap(bitmap);
                holder.pickerImg.getLayoutParams().height = height;
                holder.pickerImg.getLayoutParams().width = height;

                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(0xDD898989);

                Bitmap bg = Bitmap.createBitmap(height,height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bg);
                canvas.drawCircle(height/2, height/2, height/2, paint);
                BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bg);

                holder.pickerImg.setBackground(drawable);

                is.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        holder.pickerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView != null) {
//                    recyclerView.smoothScrollToPosition(position);
                    Log.e("POSITION", ""+position);
                    CustomLayoutManager layoutManager = (CustomLayoutManager) recyclerView.getLayoutManager();
                    layoutManager.scrollToPosition(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.length;
    }

    public void swapData(String[] newData) {
        dataList = newData;
        notifyDataSetChanged();
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        ImageView pickerImg;

        public ImageHolder(View itemView) {
            super(itemView);
            pickerImg = (ImageView) itemView.findViewById(R.id.arImage);
        }
    }
}