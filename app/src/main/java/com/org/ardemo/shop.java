package com.org.ardemo;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

public class shop {
    int picture;
    String name;
    int lastSeen, productCount, chatResponse;
    double rating;

    public shop(int picture, String name, int lastSeen, int productCount, int chatResponse, double rating){
        this.picture = picture;
        this.name = name;
        this.lastSeen = lastSeen;
        this.productCount = productCount;
        this.chatResponse = chatResponse;
        this.rating = rating;
    }

    public void updateData(int lastSeen, int productCount, int chatResponse, double rating){
        this.lastSeen = lastSeen;
        this.productCount = productCount;
        this.chatResponse = chatResponse;
        this.rating = rating;
    }

    public void displayInformation(ImageView shopPicture, TextView shopName, TextView shopLastSeen, TextView shopProductCount, TextView shopRating, TextView shopChatResponse){
        shopPicture.setImageResource(picture);
        shopName.setText(name);
        shopLastSeen.setText("Active " + Integer.toString(lastSeen) + " minutes ago");
        shopProductCount.setText(Integer.toString(productCount));
        shopRating.setText(Integer.toString(chatResponse)+"%");
        shopChatResponse.setText(Double.toString(rating));
    }

}
