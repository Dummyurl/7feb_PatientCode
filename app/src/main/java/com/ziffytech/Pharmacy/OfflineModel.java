package com.ziffytech.Pharmacy;

import java.io.Serializable;

public class OfflineModel implements Serializable {

    String pres_img;
    String status;
    String date_time;
    String prescriptionoff_id;

    public String getPres_img() {
        return pres_img;
    }

    public void setPres_img(String pres_img) {
        this.pres_img = pres_img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getPrescriptionoff_id() {
        return prescriptionoff_id;
    }

    public void setPrescriptionoff_id(String prescriptionoff_id) {
        this.prescriptionoff_id = prescriptionoff_id;
    }
}
