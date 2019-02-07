package com.ziffytech.booklab.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LabDetailListApi {

    @SerializedName("response")
    @Expose
    public Integer response;


    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    public ArrayList<Labdata> data;

    public class Labdata {
        @SerializedName("lab_id")
        @Expose
        public String lab_id;

        @SerializedName("lab_name")
        @Expose
        public String lab_name;


        @SerializedName("lab_address")
        @Expose
        public String lab_address;


        @SerializedName("lab_image")
        @Expose
        public String lab_photo;

        @SerializedName("lab_opening_time")
        @Expose
        public String lab_opening_time;

        @SerializedName("lab_closing_time")
        @Expose
        public String lab_closing_time;


    }

}
