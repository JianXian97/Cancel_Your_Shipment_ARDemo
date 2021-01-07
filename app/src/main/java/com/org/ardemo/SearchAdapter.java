package com.org.ardemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Parcelable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.idlestar.ratingstar.RatingStarView;
import com.org.ardemo.objs.Product;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

import static com.org.ardemo.DemoUtils.format;

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
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        if(product.getOldPrice() == 0){
            viewHolder.oldPrice.setVisibility(View.GONE);
            viewHolder.discountValue.setVisibility(View.GONE);
        }
        else{
            viewHolder.oldPrice.setPaintFlags(viewHolder.oldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG); //strike through
            viewHolder.oldPrice.setText("$"+String.format("%.2f",product.getOldPrice()));

            double discount =  product.getDiscountAmount() * 100; // in %
            String text = "<font color=#DC593B>"+(int)discount+"%<br/></font> <font color=#ffffff>OFF</font>";
            viewHolder.discountValue.setText(Html.fromHtml(text));
            viewHolder.discountValue.setElevation(2);
            //viewHolder.discountValue.setBackgroundColor(0xFFFAD923);
            viewHolder.discountValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            viewHolder.discountValue.setBackgroundResource(R.drawable.ic_discount_label);
        }
        viewHolder.newPrice.setText("$"+String.format("%.2f",product.getNewPrice()));
        viewHolder.itemsSold.setText(format(product.getItemsSold()) + " sold");
        if(!product.supportingLocal()) viewHolder.supportLocal.setVisibility(View.GONE);
        if(!product.addingOn()) viewHolder.addOnDeal.setVisibility(View.GONE);
        if(!product.hasAR()) viewHolder.ar.setVisibility(View.GONE);

        viewHolder.shopRating.setRating(product.getProductRating());

        viewHolder.panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), ViewProductActivity.class);
                int pos = viewHolder.getLayoutPosition();
                myIntent.putExtra("product", (Parcelable) productList.get(pos));
                v.getContext().startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title, oldPrice, newPrice, itemsSold, discountValue;
        private MaterialButton supportLocal, addOnDeal;
        public ImageView img,ar;
        private RatingStarView shopRating;
        ConstraintLayout panel, labelsPanel;
        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            img = view.findViewById(R.id.img);
            discountValue = view.findViewById(R.id.discountValue);
            oldPrice = view.findViewById(R.id.oldPrice);
            newPrice = view.findViewById(R.id.newPrice);
            supportLocal = view.findViewById(R.id.supportLocalTag);
            addOnDeal = view.findViewById(R.id.addOnDealTag);
            shopRating = view.findViewById(R.id.shopRating);
            itemsSold = view.findViewById(R.id.itemsSold);
            ar = view.findViewById(R.id.arIcon);
            panel = view.findViewById(R.id.panel);
            labelsPanel = view.findViewById(R.id.labelsPanel);
        }
    }


}
