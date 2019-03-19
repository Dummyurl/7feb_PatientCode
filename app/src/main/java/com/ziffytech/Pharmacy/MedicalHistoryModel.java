package com.ziffytech.Pharmacy;

public class MedicalHistoryModel {

    String clinic_name;
    String doctor_name;
    String date;
    String pres_id;

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPres_id() {
        return pres_id;
    }

    public void setPres_id(String app_id) {
        this.pres_id = app_id;
    }
}
