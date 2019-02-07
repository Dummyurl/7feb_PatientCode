package com.ziffytech.booklab.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LabListApi {

    @SerializedName("response")
    @Expose
    public Integer response;


    @SerializedName("message")
    @Expose
    public String message;


    @SerializedName("data")
    public ArrayList<LabDetailListApi.Labdata> data;

    public class Labdata {

        @SerializedName("lab_name")
        @Expose
        public String lab_name;

    }
}
