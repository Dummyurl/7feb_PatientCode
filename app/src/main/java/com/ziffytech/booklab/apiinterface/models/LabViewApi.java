package com.ziffytech.booklab.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LabViewApi {

    @SerializedName("response")
    @Expose
    public Integer response;


    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    public ArrayList<Labviewdata> data;

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Labviewdata {
        @SerializedName("lab_id")
        @Expose
        public String lab_id;

        @SerializedName("lab_name")
        @Expose
        public String lab_name;


        @SerializedName("lab_address")
        @Expose
        public String lab_address;

        @SerializedName("lab_details")
        @Expose
        public String lab_details;


        @SerializedName("lab_image")
        @Expose
        public String lab_photo;

        @SerializedName("lab_opening_time")
        @Expose
        public String lab_opening_time;

        @SerializedName("lab_long")
        @Expose
        public String lab_long;


        @SerializedName("lab_lat")
        @Expose
        public String lab_lat;

        @SerializedName("lab_closing_time")
        @Expose
        public String lab_closing_time;

        @SerializedName("working_days")
        @Expose
        public String working_days;


    }

}
