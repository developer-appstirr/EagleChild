package com.example.eagle_child.models;

import java.io.File;

public class FileTypeModel {

    private File ImgUrl = null;
    private String fileUrl = null;
    private String type;
    private String webImgUrl = null;
    private boolean webUrl = false;

    public boolean getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(boolean webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebImgUrl() {
        return webImgUrl;
    }

    public void setWebImgUrl(String webImgUrl) {
        this.webImgUrl = webImgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(File imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

}
