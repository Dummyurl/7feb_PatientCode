package com.ziffytech.models;



public class Testappointment
{

    String lab_app_id;
    String lab_appointment_date;
    String start_time;
    String lab_appointment_test;
    String lab_name;
    String test_count;
    String status;

    public String getLab_appointment_count() {
        return test_count;
    }

    public void setLab_appointment_count(String lab_appointment_count) {
        this.test_count = lab_appointment_count;
    }

    public String getLab_appointment_date() {
        return lab_appointment_date;
    }

    public void setLab_appointment_date(String lab_appointment_date) {
        this.lab_appointment_date = lab_appointment_date;
    }

    public String getLab_appointment_id() {
        return lab_app_id;
    }

    public void setLab_appointment_id(String lab_appointment_id) {
        this.lab_app_id = lab_appointment_id;
    }

    public String getLab_appointment_name() {
        return lab_name;
    }

    public void setLab_appointment_name(String lab_appointment_name) {
        this.lab_name = lab_appointment_name;
    }

    public String getLab_appointment_test() {
        return lab_appointment_test;
    }

    public void setLab_appointment_test(String lab_appointment_test) {
        this.lab_appointment_test = lab_appointment_test;
    }

    public String getLab_start_time() {
        return start_time;
    }

    public void setLab_start_time(String lab_start_time) {
        this.start_time = lab_start_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
