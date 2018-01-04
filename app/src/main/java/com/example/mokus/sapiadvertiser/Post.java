package com.example.mokus.sapiadvertiser;

import java.io.Serializable;

/**
 * Created by Mokus on 12/31/2017.
 */

public class Post implements Serializable {

    public String title;
    public String shortDesc;
    public String longDesc;
    private String postImage;

    public Post() {
    }

    public Post(String title) {
        this.title = title;
    }

    public Post(String title, String shortDesc) {
        this.title = title;
        this.shortDesc = shortDesc;
    }

    public Post(String title, String shortDesc, String longDesc) {
        this.title = title;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    public Post(String title, String shortDesc, String longDesc, String postImage) {
        this.title = title;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.postImage = postImage;
    }

    public String getTitle() { return title; }

    public String getShortDesc() { return shortDesc; }

    public String getLongDesc() {
        return longDesc;
    }

    public String getPostImage() {
        return postImage;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public void setPostImages(String postImages) {
        this.postImage = postImages;
    }
}
