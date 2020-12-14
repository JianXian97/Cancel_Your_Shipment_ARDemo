package com.org.ardemo;

import android.net.Uri;


public class CreateList {

    private String image_title;
    private Integer image_id;
    private Uri image_uri;

    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String android_version_name) {
        this.image_title = android_version_name;
    }

    public Integer getImage_ID() {
        return image_id;
    }

    public Uri getImage_URI() {
        return image_uri;
    }

    public void setImage_ID(Integer android_image_url) {
        this.image_id = android_image_url;
    }

    public void setImage_URI(Uri android_image_uri) {
        this.image_uri = android_image_uri;
    }
}

