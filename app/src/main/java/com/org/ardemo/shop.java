package com.org.ardemo;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

public class shop implements Parcelable{
    String name, picture;
    int lastSeen, productCount, chatResponse;
    double rating;

    public shop(String picture, String name, int lastSeen, int productCount, int chatResponse, double rating){
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

    public String getName() {return name;}
    public int getLastSeen() {return lastSeen;}
    public int getProductCount() {return productCount;}
    public int getChatResponse() {return chatResponse;}
    public double getRating() {return rating;}
    public String getPicture(){return picture;}

    @Override
    public int describeContents() {return 0;}

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(picture);
        out.writeString(name);
        out.writeInt(lastSeen);
        out.writeInt(productCount);
        out.writeInt(chatResponse);
        out.writeDouble(rating);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<shop> CREATOR = new Parcelable.Creator<shop>() {
        public shop createFromParcel(Parcel in) {
            return new shop(in);
        }

        public shop[] newArray(int size) {
            return new shop[size];
        }
    };

    // constructor that takes a Parcel and gives you an object populated with it's values
    private shop(Parcel in) {
        picture= in.readString();
        name= in.readString();
        lastSeen= in.readInt();
        productCount= in.readInt();
        chatResponse= in.readInt();
        rating= in.readDouble();
    }

}
