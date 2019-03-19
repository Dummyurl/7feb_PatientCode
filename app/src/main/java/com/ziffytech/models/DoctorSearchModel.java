package com.ziffytech.models;

import android.util.Log;

/**
 * Created by Mahesh on 29/11/17.
 */

public class DoctorSearchModel {

    public String getDoct_name() {
        return doct_name;
    }

    public void setDoct_name(String doct_name) {
        this.doct_name = doct_name;
    }

    private String doct_name;

    public String getBus_title() {
        return bus_title;
    }

    public void setBus_title(String bus_title) {
        this.bus_title = bus_title;
    }

    private String bus_title;

    private String doct_email;

    public String getDoct_email() {
        return doct_email;
    }

    public void setDoct_email(String doct_email) {
        this.doct_email = doct_email;
    }

    public String getDoct_id() {
        return doct_id;
    }

    public void setDoct_id(String doct_id) {
        this.doct_id = doct_id;
    }

    private String doct_id;

    private String doct_speciality;

    public String get_doc_spe() {
        return doct_speciality;
    }

    public void set_doc_spe(String doct_speciality) {
        this.doct_speciality = doct_speciality;
    }

    String doct_city;
    String doct_multi_speciality;


    public String getDoct_multi_speciality() {
        return doct_multi_speciality;
    }

    public void setDoct_multi_speciality(String doct_multi_speciality) {
        this.doct_multi_speciality = doct_multi_speciality;
    }

    public String getDoct_city() {
        return doct_city;
    }

    public void setDoct_city(String doct_city) {
        this.doct_city = doct_city;
    }
}
