package com.ziffytech.booklab.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TestApi {


    @SerializedName("response")
    @Expose
    public Integer response;


    @SerializedName("message")
    @Expose
    public String message;


    @SerializedName("data")
    public ArrayList<Labdata> data;

    public class Labdata {

        @SerializedName("id")
        @Expose
        public String id;

        @SerializedName("user_id")
        @Expose
        public String user_id;

        @SerializedName("sub_user_id")
        @Expose
        public String sub_user_id;

        @SerializedName("app_id")
        @Expose
        public String app_id;

        @SerializedName("doct_id")
        @Expose
        public String doct_id;

        @SerializedName("test")
        @Expose
        public String test;

    @SerializedName("test_price")
        @Expose
        public String price;



    }
}
