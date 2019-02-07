package com.ziffytech.models;

public class VersionModel
{
       String patient_ver_code;
       String doctor_ver_code;
       String delivery_ver_code;
       String doc_dashboard_ver_code;
       String version_app_id;

    public String getPatient_ver_code()
    {
        return patient_ver_code;
    }

    public void setPatient_ver_code(String patient_ver_code) {
        this.patient_ver_code = patient_ver_code;
    }

    public String getDoctor_ver_code() {
        return doctor_ver_code;
    }

    public void setDoctor_ver_code(String doctor_ver_code) {
        this.doctor_ver_code = doctor_ver_code;
    }

    public String getDelivery_ver_code() {
        return delivery_ver_code;
    }

    public void setDelivery_ver_code(String delivery_ver_code) {
        this.delivery_ver_code = delivery_ver_code;
    }

    public String getDoc_dashboard_ver_code() {
        return doc_dashboard_ver_code;
    }

    public void setDoc_dashboard_ver_code(String doc_dashboard_ver_code) {
        this.doc_dashboard_ver_code = doc_dashboard_ver_code;
    }

    public String getVersion_app_id() {
        return version_app_id;
    }

    public void setVersion_app_id(String version_app_id) {
        this.version_app_id = version_app_id;
    }
}
