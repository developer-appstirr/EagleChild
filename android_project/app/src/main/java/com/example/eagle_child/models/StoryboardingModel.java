package com.example.eagle_child.models;

public class StoryboardingModel {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getHeadingText() {
        return headingText;
    }

    public void setHeadingText(String headingText) {
        this.headingText = headingText;
    }

    public String getParaText() {
        return paraText;
    }

    public void setParaText(String paraText) {
        this.paraText = paraText;
    }

    private String id;

    public int getBgImage() {
        return bgImage;
    }

    public void setBgImage(int bgImage) {
        this.bgImage = bgImage;
    }

    private int bgImage;
    private String headingText;
    private String paraText;


}