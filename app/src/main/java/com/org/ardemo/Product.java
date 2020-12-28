package com.org.ardemo;

import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

public class Product implements Parcelable{
    private String title;
    private String img;
    private Double oldPrice, newPrice;
    private float shopRating;
    private int itemsSold;
    private Boolean supportLocal, addOnDeal, ar;
    private shop shop;

    public Product(String title, String img, Double oldPrice, Double newPrice, Boolean supportLocal, Boolean addOnDeal, float shopRating, int itemsSold, Boolean ar){
        this.title = title;
        this.img = img;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.supportLocal = supportLocal;
        this.addOnDeal = addOnDeal;
        this.shopRating = shopRating;
        this.itemsSold = itemsSold;
        this.ar = ar;
    }
    public String getTitle() {return title;}
    public String getImg() {return img;}
    public Boolean supportingLocal() {return supportLocal;}
    public Boolean addingOn() {return addOnDeal;}
    public Boolean hasAR() {return ar;}
    public Double getOldPrice() {return oldPrice;}
    public Double getNewPrice() {return newPrice;}
    public float getShopRating() {return shopRating;}
    public int getItemsSold() {return itemsSold;}
    public Double getDiscountAmount(){
        return (oldPrice - newPrice) / oldPrice;
    }
    public void setShop(shop shop){this.shop = shop;}
    public shop getShop(){return this.shop;}


    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(shop,flags);
        out.writeString(title);
        out.writeString(img);
        out.writeDouble(oldPrice);
        out.writeDouble(newPrice);
        out.writeFloat(shopRating);
        out.writeInt(itemsSold);
        out.writeInt((supportLocal)?1:0);
        out.writeInt((addOnDeal)?1:0);
        out.writeInt((ar)?1:0);

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // constructor that takes a Parcel and gives you an object populated with it's values
    private Product(Parcel in) {
        this.shop = (shop)in.readParcelable(shop.class.getClassLoader());
        title = in.readString();
        img = in.readString();
        oldPrice = in.readDouble();
        newPrice = in.readDouble();
        shopRating = in.readFloat();
        itemsSold = in.readInt();
        supportLocal = in.readInt() == 1;
        addOnDeal = in.readInt() == 1;
        ar = in.readInt() == 1;
    }



}
