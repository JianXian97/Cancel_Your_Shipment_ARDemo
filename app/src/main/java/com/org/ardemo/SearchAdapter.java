package com.org.ardemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.Rating;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.idlestar.ratingstar.RatingStarView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    ArrayList<Product> productList;
    private Context context;

    public SearchAdapter(Context context, ArrayList<Product> productList) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_cell, viewGroup, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder viewHolder, int i) {
        Product product = productList.get(i);
        viewHolder.title.setText(product.getTitle());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        AssetManager assetManager = context.getAssets();
        try{
            String path = "product-images/"+product.getImg();
            InputStream is = assetManager.open(path);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            viewHolder.img.setImageBitmap(bitmap);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(product.oldPrice == 0){
            viewHolder.oldPrice.setVisibility(View.GONE);
        }
        else{
            viewHolder.oldPrice.setPaintFlags(viewHolder.oldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG); //strike through
            viewHolder.oldPrice.setText("$"+String.format("%.2f",product.getOldPrice()));
        }
        viewHolder.newPrice.setText("$"+String.format("%.2f",product.getNewPrice()));
        viewHolder.itemsSold.setText(format(product.getItemsSold()) + " sold");
        if(!product.supportingLocal()) viewHolder.supportLocal.setVisibility(View.GONE);
        if(!product.addingOn()) viewHolder.addOnDeal.setVisibility(View.GONE);
        if(!product.hasAR()) viewHolder.ar.setVisibility(View.GONE);

        viewHolder.shopRating.setRating(product.getShopRating());

//        viewHolder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(v.getContext(), ViewImageActivity.class);
//                int pos = viewHolder.getLayoutPosition();
//                myIntent.putExtra("imgID", uriList.get(pos).toString());
//                v.getContext().startActivity(myIntent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title, oldPrice, newPrice, itemsSold;
        private MaterialButton supportLocal, addOnDeal;
        private ImageView img,ar;
        private RatingStarView shopRating;
        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            img = view.findViewById(R.id.img);
            oldPrice = view.findViewById(R.id.oldPrice);
            newPrice = view.findViewById(R.id.newPrice);
            supportLocal = view.findViewById(R.id.supportLocalTag);
            addOnDeal = view.findViewById(R.id.addOnDealTag);
            shopRating = view.findViewById(R.id.shopRating);
            itemsSold = view.findViewById(R.id.itemsSold);
            ar = view.findViewById(R.id.arIcon);
        }
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }
}
