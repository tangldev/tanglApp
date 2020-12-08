package com.tangl.tanglapp.DataModels;

public class MenuPreviewDataModel {
    private String title;
    private String previewImg;
    private String subtitle;
    private String shortDesc;
    public MenuPreviewDataModel(){}
    public MenuPreviewDataModel(String title, String previewImg, String subtitle){
        this.title = title;
        this.previewImg = previewImg;
        this.subtitle = subtitle;
    }

    public String getTitle(){
        return this.title;
    }

    public String getSubtitle(){
        return this.subtitle;
    }

    public String getPreviewImg(){
        return this.previewImg;
    }

}
