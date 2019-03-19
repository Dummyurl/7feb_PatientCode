package com.ziffytech.Pharmacy;

public class MedicineOrderModel {

    String id;
    String drug_name;
    String medicine_price;
    String quantity;
    boolean isChecked;
    String availability;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String medicine_name) {
        this.drug_name = medicine_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String qty) {
        this.quantity = qty;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getMedicine_price() {
        return medicine_price;
    }

    public void setMedicine_price(String medicine_price) {
        this.medicine_price = medicine_price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
