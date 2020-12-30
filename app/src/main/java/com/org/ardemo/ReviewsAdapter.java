package com.org.ardemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.idlestar.ratingstar.RatingStarView;
import com.org.ardemo.objs.Review;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import static com.org.ardemo.DemoUtils.format;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    Review[] reviewList;
    private Context context;
    private ConstraintLayout reviewCell;

//    public ReviewsAdapter(Context context, Review[] reviewList, Context activityContext) {
    public ReviewsAdapter(Context context, Review[] reviewList) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_review_cell, viewGroup, false);
        return new ReviewsAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder viewHolder, int i) {
        Review review = reviewList[i];
        viewHolder.comment.setText(review.getComment());
        viewHolder.profilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        AssetManager assetManager = context.getAssets();
        ImageView[] mediaLayoutList = new ImageView[]{viewHolder.media1, viewHolder.media2, viewHolder.media3};

        try{
            String path = "profile-picture/"+review.getProfilePicture();
            InputStream is = assetManager.open(path);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            viewHolder.profilePicture.setImageBitmap(bitmap);

            for(int j = 0; j < Math.min(review.getMediaNames().length,3); j++){
                path = "review-images/"+review.getMediaNames()[j];
                is = assetManager.open(path);
                bitmap = BitmapFactory.decodeStream(is);
                mediaLayoutList[j].setImageBitmap(bitmap);
                Log.d("CHECK", ""+j);
            }

            if(review.getComment().isEmpty()){
                viewHolder.comment.setVisibility(View.GONE);
            }
            else {
                path = "loremipsum.txt"; //comment stores the path to the asset
                is = assetManager.open(path);
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                String comment = new String(buffer, StandardCharsets.UTF_8);
                //Log.d("CHECK123", comment);
                viewHolder.comment.setText(comment);
            }

            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        viewHolder.userName.setText(review.getUserName());
        viewHolder.variation.setText("Variation: " +review.getVariation());
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(review.getDateTime());
        viewHolder.dateTime.setText(formattedDate);

        viewHolder.rating.setRating((int)Math.round(review.getRating()));
    }

    @Override
    public int getItemCount() {
        return reviewList.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView comment, userName, dateTime, variation;
        private ImageView profilePicture, media1, media2, media3;
        private RatingStarView rating;

        public ViewHolder(View view) {
            super(view);
            comment = view.findViewById(R.id.comment);
            userName = view.findViewById(R.id.userName);
            profilePicture = view.findViewById(R.id.profilePic);
            dateTime = view.findViewById(R.id.dateTime);
            variation = view.findViewById(R.id.variation);
            media1 = view.findViewById(R.id.media1);
            media2 = view.findViewById(R.id.media2);
            media3 = view.findViewById(R.id.media3);
            rating = view.findViewById(R.id.reviewRating);
            reviewCell = view.findViewById(R.id.reviewCell);
        }
    }


}
