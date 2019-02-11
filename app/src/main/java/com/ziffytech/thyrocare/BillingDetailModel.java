package com.ziffytech.thyrocare;

class BillingDetailModel {
    String user_id;
    String user_name;
    String user_phone;
    String location;
    String actual_total;
    String discount;
    String card_total;
    String thyro_profile_id;
    String test_code;
    String profile_name;
    String ziffy_profile_price;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() { return user_name; }

    public void setUser_name(String user_name) { this.user_name = user_name; }

    public String getUser_phone() { return user_phone; }

    public void setUser_phone(String user_phone) { this.user_phone = user_phone; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getActual_total() { return actual_total; }

    public void setActual_total(String actual_total) { this.actual_total = actual_total; }

    public String getDiscount() { return discount; }

    public void setDiscount(String discount) { this.discount = discount; }

    public String getCard_total() { return card_total; }

    public void setCard_total(String card_total) { this.card_total = card_total; }



    public String getThyro_profile_id() {
        return thyro_profile_id;
    }

    public void setThyro_profile_id(String thyro_profile_id) {
        this.thyro_profile_id = thyro_profile_id;
    }

    public String getName() {
        return profile_name;
    }

    public void setName(String profile_name) {
        this.profile_name = profile_name;
    }

    public String getTest_code() {
        return test_code;
    }

    public void setTest_code(String test_code) {
        this.test_code = test_code;
    }


    public String getZiffy_profile_price() {
        return ziffy_profile_price;
    }

    public void setZiffy_profile_price(String ziffy_profile_price) {
        this.ziffy_profile_price = ziffy_profile_price;
    }





}
