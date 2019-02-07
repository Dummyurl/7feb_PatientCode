package com.ziffytech.booklab.models;

public class TestModel {

    private String id;

    private String app_id;
    private String lab_test_id;

    private String doct_id;

    private String user_id;

    private String sub_user_id;

    private String test_code;
    private String price;
    private String ISTestProfile;
    private String Activity;

    public String getISTestProfile() {
        return ISTestProfile;
    }

    public void setISTestProfile(String ISTestProfile) {
        this.ISTestProfile = ISTestProfile;
    }



    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private boolean isChecked;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSub_user_id() {
        return sub_user_id;
    }

    public void setSub_user_id(String sub_user_id) {
        this.sub_user_id = sub_user_id;
    }

    public String getTest() {
        return test_code;
    }

    public void setTest(String test) {
        this.test_code = test;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getDoct_id() {
        return doct_id;
    }

    public void setDoct_id(String doct_id) {
        this.doct_id = doct_id;
    }


    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    private String test_name;

    public String getLab_test_id() {
        return lab_test_id;
    }

    public void setLab_test_id(String lab_test_id) {
        this.lab_test_id = lab_test_id;
    }

    public String getTest_code() {
        return test_code;
    }

    public void setTest_code(String test_code) {
        this.test_code = test_code;
    }
}
