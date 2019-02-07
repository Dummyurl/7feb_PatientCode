package com.ziffytech.booklab.models;

public class LabDetailListModel {
    private String lab_id;

    private String lab_name;


    private String lab_address;


    private String lab_photo;

    private String lab_opening_time;

    private String lab_closing_time;


    public String getLab_id() {
        return lab_id;
    }

    public void setLab_id(String lab_id) {
        this.lab_id = lab_id;
    }

    public String getLab_name() {
        return lab_name;
    }

    public void setLab_name(String lab_name) {
        this.lab_name = lab_name;
    }

    public String getLab_address() {
        return lab_address;
    }

    public void setLab_address(String lab_address) {
        this.lab_address = lab_address;
    }

    public String getLab_photo() {
        return lab_photo;
    }

    public void setLab_photo(String lab_photo) {
        this.lab_photo = lab_photo;
    }

    public String getLab_opening_time() {
        return lab_opening_time;
    }

    public void setLab_opening_time(String lab_opening_time) {
        this.lab_opening_time = lab_opening_time;
    }

    public String getLab_closing_time() {
        return lab_closing_time;
    }

    public void setLab_closing_time(String lab_closing_time) {
        this.lab_closing_time = lab_closing_time;
    }
}
