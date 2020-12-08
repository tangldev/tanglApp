package com.tangl.tanglapp.DataModels;

import android.widget.ImageView;

public class DiscussionDataModel {
    private String mTitle;
    private String mContent;
    private ImageView mImg;
    public DiscussionDataModel(){

    }

    public void setTitle(String title){
        this.mTitle = title;
    }

    public void setContent(String content){
        this.mContent = content;
    }

    public String getContent(){
        return this.mContent;
    }

    public String getTitle(){
        return this.mTitle;
    }

    public void setImg(ImageView img){
        this.mImg = img;
    }

    public ImageView getImg(){
        return this.mImg;
    }
}
