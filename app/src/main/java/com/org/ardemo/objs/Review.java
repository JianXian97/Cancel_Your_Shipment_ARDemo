package com.org.ardemo.objs;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Review implements Parcelable{
    private String comment, userName, profilePicture, variation;
    private String[]mediaNames;
    private Double rating;
    private Date dateTime;

    public Review(String comment, String userName, String profilePicture, String variation, String[] mediaNames, Double rating, Date dateTime) {
        this.comment = comment;
        this.userName = userName;
        this.profilePicture = profilePicture;
        this.variation = variation;
        this.mediaNames = mediaNames;
        this.rating = rating;
        this.dateTime = dateTime;
    }

    public String getComment() {return comment;}
    public void setComment(String comment) {this.comment = comment;}
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}
    public String getProfilePicture() {return profilePicture;}
    public void setProfilePicture(String profilePicture) {this.profilePicture = profilePicture;}
    public String getVariation() {return variation;}
    public void setVariation(String variation) {this.variation = variation;}
    public String[] getMediaNames() {return mediaNames;}
    public void setMediaNames(String[] mediaNames) {this.mediaNames = mediaNames;}
    public Double getRating() {return rating;}
    public void setRating(Double rating) {this.rating = rating;}
    public Date getDateTime() {return dateTime;}
    public void setDateTime(Date dateTime) {this.dateTime = dateTime;}

    @Override
    public int describeContents() {return 0;}

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(comment);
        out.writeString(userName);
        out.writeString(profilePicture);
        out.writeString(variation);
        out.writeStringArray(mediaNames);
        out.writeDouble(rating);
        out.writeLong(dateTime.getTime());
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    // constructor that takes a Parcel and gives you an object populated with it's values
    private Review(Parcel in) {
        comment = in.readString();
        userName = in.readString();
        profilePicture = in.readString();
        variation = in.readString();
        mediaNames = in.createStringArray();
        rating = in.readDouble();
        dateTime = new Date(in.readLong());
    }

}
