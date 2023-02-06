package com.graduation.mah.weather.model;

public class ModelMore {

    String desc,url;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ModelMore(String desc, String url) {
        this.desc = desc;
        this.url = url;
    }
}
