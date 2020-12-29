package com.org.ardemo.objs;

import android.os.Parcel;
import android.os.Parcelable;

public class Shop implements Parcelable{
    String name, picture, address;
    int lastSeen, productCount, chatResponse;
    double rating;

    public Shop(String picture, String name, String address, int lastSeen, int productCount, int chatResponse, double rating){
        this.picture = picture;
        this.name = name;
        this.address = address;
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
    public String getaddress() {return address;}
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
        out.writeString(address);
        out.writeString(name);
        out.writeInt(lastSeen);
        out.writeInt(productCount);
        out.writeInt(chatResponse);
        out.writeDouble(rating);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop>() {
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    // constructor that takes a Parcel and gives you an object populated with it's values
    private Shop(Parcel in) {
        picture= in.readString();
        address= in.readString();
        name= in.readString();
        lastSeen= in.readInt();
        productCount= in.readInt();
        chatResponse= in.readInt();
        rating= in.readDouble();
    }

}
