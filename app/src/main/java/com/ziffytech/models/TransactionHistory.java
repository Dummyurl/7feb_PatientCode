package com.ziffytech.models;

public class TransactionHistory {
    String trans_mode;
    String amount;
    String payment_status;
    String PaidTo;
    String date_time;
    String bookedFor;
    String test;

    String txn_id;
    boolean success;
    boolean fail;
    boolean all;
    boolean cancel;

    public String getTrans_mode() {
        return trans_mode;
    }

    public void setTrans_mode(String trans_mode) {
        this.trans_mode = trans_mode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPaidTo() {
        return PaidTo;
    }

    public void setPaidTo(String paidTo) {
        PaidTo = paidTo;
    }

    public String getBookedFor() {
        return bookedFor;
    }

    public void setBookedFor(String bookedFor) {
        this.bookedFor = bookedFor;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isFail() {
        return fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }


    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
