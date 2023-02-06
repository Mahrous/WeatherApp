package com.graduation.mah.weather.model.googleModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GeneralStatusModel implements Serializable {


    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("message")
    String message;

    public GeneralStatusModel(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
