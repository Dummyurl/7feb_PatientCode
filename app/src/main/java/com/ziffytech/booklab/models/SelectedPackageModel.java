package com.ziffytech.booklab.models;

public class SelectedPackageModel {
    String test_code;
    String price;
    String names;
    String ISTestProfile;
    String test_names;
    String lab_test_id;

    public String getTest_code() {
        return test_code;
    }

    public void setTest_code(String id) {
        this.test_code = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getISTestProfile() {
        return ISTestProfile;
    }

    public void setISTestProfile(String ISTestProfile) {
        this.ISTestProfile = ISTestProfile;
    }

    public String getTest_names() {
        return test_names;
    }

    public void setTest_names(String test_names) {
        this.test_names = test_names;

    }

    public String getLab_test_id() {
        return lab_test_id;
    }

    public void setLab_test_id(String lab_test_id) {
        this.lab_test_id = lab_test_id;
    }
}
