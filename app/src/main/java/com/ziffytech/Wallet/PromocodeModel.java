package com.ziffytech.Wallet;

public class PromocodeModel {

    String promo_code_id;
    String promo_code_name;
    String promo_code_details;
    String max_wallet_usage;
    String min_trans_amt;
    String cashback;
    String discount;

    public String getPromo_code_id() {
        return promo_code_id;
    }

    public void setPromo_code_id(String promo_code_id) {
        this.promo_code_id = promo_code_id;
    }

    public String getPromo_code_name() {
        return promo_code_name;
    }

    public void setPromo_code_name(String promo_code_name) {
        this.promo_code_name = promo_code_name;
    }

    public String getPromo_code_details() {
        return promo_code_details;
    }

    public void setPromo_code_details(String promo_code_details) {
        this.promo_code_details = promo_code_details;
    }

    public String getMax_wallet_usage() {
        return max_wallet_usage;
    }

    public void setMax_wallet_usage(String max_wallet_usage) {
        this.max_wallet_usage = max_wallet_usage;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getMin_trans_amt() {
        return min_trans_amt;
    }

    public void setMin_trans_amt(String min_trans_amt) {
        this.min_trans_amt = min_trans_amt;
    }
}
