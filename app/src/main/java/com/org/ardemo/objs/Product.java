package com.org.ardemo.objs;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Product implements Parcelable{
    private String title, img, brand;
    private Double oldPrice, newPrice;
    private float productRating;
    private int itemsSold, stocks;
    private Boolean supportLocal, addOnDeal, ar;
    private Shop shop;
    private String[] variations;
    private Product[] suggestedProducts;
    private Review[] reviewList;



    public Product(String title, String img, String brand, Double oldPrice, Double newPrice, Boolean supportLocal, Boolean addOnDeal, float productRating, int itemsSold, int stocks, Boolean ar, Product[] suggestedProducts, String[] variations, Review[] reviewList, Shop shop){
        this.title = title;
        this.img = img;
        this.brand = brand;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.supportLocal = supportLocal;
        this.addOnDeal = addOnDeal;
        this.productRating = productRating;
        this.itemsSold = itemsSold;
        this.stocks = stocks;
        this.ar = ar;
        this.suggestedProducts = suggestedProducts;
        this.reviewList = reviewList;
        this.shop = shop;
        this.variations = variations;
    }
    public String getTitle() {return title;}
    public String getImg() {return img;}
    public String getBrand() {return brand;}
    public Boolean supportingLocal() {return supportLocal;}
    public Boolean addingOn() {return addOnDeal;}
    public Boolean hasAR() {return ar;}
    public Double getOldPrice() {return oldPrice;}
    public Double getNewPrice() {return newPrice;}
    public float getProductRating() {return productRating;}
    public int getItemsSold() {return itemsSold;}
    public int getStocks() {return stocks;}
    public Double getDiscountAmount(){
        return (oldPrice - newPrice) / oldPrice;
    }
    public void setShop(Shop shop){this.shop = shop;}
    public Shop getShop(){return this.shop;}
    public Review[] getReviews(){return this.reviewList;}
    public Product[] getsuggestedProducts() {return suggestedProducts;}
    public void setSuggestedProducts(Product[] suggestedProducts) {this.suggestedProducts = suggestedProducts;}
    public String[] getVariations() {return variations;}
    public void setVariations(String[] variations) {this.variations = variations;}
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedArray(reviewList,0);
        out.writeTypedArray(suggestedProducts,1);
        out.writeParcelable(shop,flags);
        out.writeStringArray(variations);
        out.writeString(title);
        out.writeString(img);
        out.writeString(brand);
        out.writeDouble(oldPrice);
        out.writeDouble(newPrice);
        out.writeFloat(productRating);
        out.writeInt(itemsSold);
        out.writeInt(stocks);
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
        this.reviewList = in.createTypedArray(Review.CREATOR);
        this.suggestedProducts = in.createTypedArray(Product.CREATOR);
        this.shop = in.readParcelable(Shop.class.getClassLoader());
        this.variations = in.createStringArray();
        title = in.readString();
        img = in.readString();
        brand = in.readString();
        oldPrice = in.readDouble();
        newPrice = in.readDouble();
        productRating = in.readFloat();
        itemsSold = in.readInt();
        stocks = in.readInt();
        supportLocal = in.readInt() == 1;
        addOnDeal = in.readInt() == 1;
        ar = in.readInt() == 1;
    }



}
