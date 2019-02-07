package com.ziffytech.models;

/**
 * Created by Ziffy on 11/16/2018.
 */

public class Medication_History_Model
{
    private String patient_id;
    private String app_id;
    private String ziffy_code;
    private String doctor_name;
    private String token;
    private String mobile_number;
    private String created;
    private String modified;
    private String final_note;
    private String doctor_note;
    private String id;
    private String category;
    private String test_name;


    public String getPatient_id() {
        return patient_id;
    }
    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getTest_App_id() {return app_id;}
    public void setTest_App_id(String app_id){this.app_id = app_id;}

    public String getZiffy_code() {
        return ziffy_code;
    }

    public void setZiffy_code(String ziffy_code) {
        this.ziffy_code = ziffy_code;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getCreated_date() {
        return created;
    }

    public void setCreated_date(String created) {
        this.created = created;
    }

    public String getModified_date() {
        return modified;
    }

    public void setModified_date(String modified) {
        this.modified = modified;
    }

    public String getFinal_note() {
        return final_note;
    }

    public void setFinal_note(String final_note) {
        this.final_note = final_note;
    }

    public String getDoctor_note() {
        return doctor_note;
    }

    public void setDoctor_note(String doctor_note) {
        this.doctor_note = doctor_note;
    }

    public String getTest_History_Id() {
        return id;
    }

    public void setTest_History_Id(String id) {
        this.id = id;
    }

    public String getTest_History_Category() {
        return category;
    }

    public void setTest_History_Category(String category) {
        this.category = category;
    }

    public String getTest_History_name() {
        return test_name;
    }

    public void setTest_History_name(String test_name) {
        this.test_name = test_name;
    }
}
