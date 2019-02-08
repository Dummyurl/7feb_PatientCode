package com.ziffytech.thyrocare;

public class TopfivepickModel
{

    String thyro_profile_id;
    String name;
    String test_code;
    String actual_amount;
    String ziffy_profile_price;
    String test_count;

    public String getThyro_profile_id() {
        return thyro_profile_id;
    }

    public void setThyro_profile_id(String thyro_profile_id) {
        this.thyro_profile_id = thyro_profile_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTest_code() {
        return test_code;
    }

    public void setTest_code(String test_code) {
        this.test_code = test_code;
    }

    public String getActual_amount() {
        return actual_amount;
    }

    public void setActual_amount(String actual_amount) {
        this.actual_amount = actual_amount;
    }

    public String getZiffy_profile_price() {
        return ziffy_profile_price;
    }

    public void setZiffy_profile_price(String ziffy_profile_price) {
        this.ziffy_profile_price = ziffy_profile_price;
    }

    public int getTest_count() {
        return Integer.parseInt(test_count);
    }

    public void setTest_count(int test_count) {
        this.test_count = String.valueOf(test_count);
    }
}
