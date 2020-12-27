package com.org.ardemo;

import android.net.Uri;

public class Product {
    private String title;
    private String img;
    Double oldPrice, newPrice;
    float shopRating;
    int itemsSold;
    Boolean supportLocal, addOnDeal, ar;

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


}
