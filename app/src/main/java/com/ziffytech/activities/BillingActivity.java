package com.ziffytech.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.Wallet.MyPayuDemoActivity;
import com.ziffytech.Wallet.OfferScreen;
import com.ziffytech.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;


public class BillingActivity extends CommonActivity {

    private static final int MY_SOCKET_TIMEOUT_MS = 5000;
    private static final String CASHBACK = "cash_back";
    private static final String DISCOUNT = "discount";
    static String user_id;
    TextView tax, total_price, final_total, text_details, text_address, tv_apply, txt_current_wallet_balance,
            txtv_total_discount;
    Button btn_details, btn_proceed;
    String lab_name, date, time;
    boolean isPaymentOptionChecked = false;
    MaterialDialog dialog1;
    String fAmt;
    String wallet_amt = "";
    String add_to_wallet = "";
    int billing_amt = 0, discount_amt = 0;
    String txn_id;
    String PromoCodeName = "";
    String promoCodeId = "";
    int is_use_wallet = 0;
    double final_price;
    // boolean isWalletP = false;
    // double wallet;
    LinearLayout layout_offers, layout_wallet_check, layout_offers_applied;
    ImageView img_offers;
    String tAmt;
    CheckBox checkWallet;
    boolean isOfferApplied = false;
    //  boolean isCashback = false;
    boolean isZiffy = false;
    boolean isCash = false, isPayUmoney = false, isBhimUPI = false, isWalletUse = false, isProfile = false, isDialog = false;
    double walletAmt;
    RadioGroup radio_payment;
    boolean isAmt50 = false;
    String discountType = "";
    LinearLayout ll_note;
    String s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        setHeaderTitle("Billing");
        allowBack();

        if (getIntent().hasExtra("status")) {

            if (getIntent().getStringExtra("status").equals("0")) {//
                fAmt = getIntent().getStringExtra("total_price");
              s1=getIntent().getStringExtra("total_price").replace(ConstValue.CURRENCY,"");
                final_price = Double.parseDouble(s1.replace("()",""));
                Log.e("final_price", String.valueOf(final_price));

            } else if (getIntent().getStringExtra("status").equals("1")) {//appti
                fAmt = getIntent().getStringExtra("amount");
                final_price = Double.parseDouble(getIntent().getStringExtra("amount"));
                Log.e("final_price", String.valueOf(final_price));

            } else if (getIntent().getStringExtra("status").equals("2")) {
                fAmt = getIntent().getStringExtra("total");
                final_price = Double.parseDouble(getIntent().getStringExtra("total"));
                s1=getIntent().getStringExtra("total").replace(ConstValue.CURRENCY,"");

                Log.e("final_price", String.valueOf(final_price));
            }else if (getIntent().getStringExtra("status").equals("3")) {
                fAmt = getIntent().getStringExtra("total_price");
                final_price = Double.parseDouble(getIntent().getStringExtra("total_price"));
                Log.e("final_price", String.valueOf(final_price));
            }
        }


        user_id = common.get_user_id();

        //  wallet = Double.parseDouble(common.getSession("ziffy_wallet_amt"));


        Log.e("WALLET AMOUNT", String.valueOf(ApiParams.ZIFFY_WALLET_AMT));

        Random rand = new Random();
        Log.e("%05d%n", String.valueOf(rand.nextInt(100000)));
        txn_id = common.getSession("user_id") + String.valueOf(rand.nextInt(10000));

        Log.e("Transaction_ID", txn_id);

        tax = (TextView) findViewById(R.id.text_tax);
        text_details = (TextView) findViewById(R.id.Tvdetails);
        text_address = (TextView) findViewById(R.id.text_address);
        //  Log.e("TAX", getIntent().getStringExtra("tax"));
        total_price = (TextView) findViewById(R.id.total_price);
        final_total = (TextView) findViewById(R.id.final_total);

        btn_details = (Button) findViewById(R.id.btn_details);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        radio_payment = (RadioGroup) findViewById(R.id.radio_payment);
        lab_name = getIntent().getStringExtra("lab_name");
        date = getIntent().getStringExtra("appointment_date");
        time = getIntent().getStringExtra("start_time");
        checkWallet = findViewById(R.id.cb_wallet);
        layout_wallet_check = findViewById(R.id.layout_wallet_check);
        txtv_total_discount = findViewById(R.id.txtv_total_discount);

        layout_offers = findViewById(R.id.layout_offers);
        layout_offers_applied = findViewById(R.id.layout_offer_applied);
        img_offers = findViewById(R.id.img_offers);
        tv_apply = findViewById(R.id.tv_apply);
        txt_current_wallet_balance = (TextView) findViewById(R.id.txt_current_wallet_balance);

        walletAmt = Double.parseDouble(common.getSession(ApiParams.ZIFFY_WALLET_AMT));
        txt_current_wallet_balance.setVisibility(View.GONE);
        txt_current_wallet_balance.setText("Wallet Balance:- " + ConstValue.CURRENCY + String.valueOf(walletAmt));
        layout_wallet_check.setVisibility(View.GONE);
        checkWallet.setVisibility(View.GONE);


        if (getIntent().hasExtra("status")) {

            if (getIntent().getStringExtra("status").equalsIgnoreCase("0")) {
                layout_offers.setVisibility(View.VISIBLE);

                Log.e("####", "SELF TEST");

                total_price.setText(fAmt);
                final_total.setText(ConstValue.CURRENCY+" "+String.valueOf(final_price));
                text_address.setText(getIntent().getStringExtra("address"));


                text_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BillingActivity.this, AppointmentDetailsActivity.class);
                        intent.putExtra("test_names", getIntent().getStringExtra("test_names"));
                        Log.e("TEST TEST", getIntent().getStringExtra("test_names"));
                        intent.putExtra("lab_name", lab_name);
                        intent.putExtra("start_time", time);
                        intent.putExtra("status", "0");
                        intent.putExtra("appointment_date", date);
                        intent.putExtra("tax", getIntent().getStringExtra("tax"));
                        intent.putExtra("total_price", fAmt);
                        intent.putExtra("final_total", String.valueOf(final_price));
                        startActivity(intent);
                    }
                });


            } else if (getIntent().getStringExtra("status").equalsIgnoreCase("1")) {

                Log.e("####", "Doctor Booking");


                if (getIntent().getStringExtra("is_ziffydoc").equals("0")) {

                    isZiffy = false;


                    if (layout_offers.getVisibility() == View.VISIBLE) {
                        layout_offers.setVisibility(View.GONE);
                        layout_wallet_check.setVisibility(View.GONE);
                    }

                } else if (getIntent().getStringExtra("is_ziffydoc").equals("1")) {
                    isZiffy = true;

                    layout_offers.setVisibility(View.VISIBLE);
                    // layout_wallet_check.setVisibility(View.VISIBLE);


                }


                Log.e("AMT##", fAmt);
                total_price.setText(ConstValue.CURRENCY + " " + fAmt);
                final_total.setText(ConstValue.CURRENCY + " " + String.valueOf(final_price));
                text_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BillingActivity.this, AppointmentDetailsActivity.class);
                        intent.putExtra("start_time", getIntent().getStringExtra("start_time"));
                        intent.putExtra("appointment_date", getIntent().getStringExtra("appointment_date"));
                        int e = Log.e("DATE-TIME", getIntent().getStringExtra("appointment_date") + getIntent().getStringExtra("start_time"));
                        intent.putExtra("clinic_name", getIntent().getStringExtra("clinic_name"));
                        intent.putExtra("doct_name", getIntent().getStringExtra("doctor_name"));
                        // intent.putExtra("tax", String.valueOf(taxAmt));
                        intent.putExtra("total_price", fAmt);
                        intent.putExtra("final_total", String.valueOf(final_price));
                        intent.putExtra("status", "1");
                        startActivity(intent);
                    }
                });


            } else if (getIntent().getStringExtra("status").equalsIgnoreCase("2")) {
                Log.e("###", "RECOMMENDED TESTS");
                layout_offers.setVisibility(View.VISIBLE);

                final double price = Double.parseDouble(getIntent().getStringExtra("total").replace(ConstValue.CURRENCY, " "));
                Log.e("###", String.valueOf(price));
                final double taxamt = (price * 10) / 100;
                Log.e("###", String.valueOf(taxamt));
                final_price = price;
                Log.e("###", String.valueOf(final_price));
                // tax.setText(ConstValue.CURRENCY + taxamt);
                fAmt = getIntent().getStringExtra("total");
                total_price.setText(ConstValue.CURRENCY+fAmt);
                final_total.setText(ConstValue.CURRENCY+String.valueOf(final_price));
                text_address.setText(getIntent().getStringExtra("address"));


                text_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(BillingActivity.this, AppointmentDetailsActivity.class);
                        intent.putExtra("test_names", getIntent().getStringExtra("test_names"));

                        Log.e("TEST TEST", getIntent().getStringExtra("test_names"));
                        intent.putExtra("lab_name", lab_name);
                        intent.putExtra("start_time", getIntent().getStringExtra("finalTime"));
                        intent.putExtra("appointment_date", getIntent().getStringExtra("finalDate"));
                        intent.putExtra("tax", "0");
                        intent.putExtra("total_price", fAmt);
                        intent.putExtra("final_total", String.valueOf(final_price));
                        intent.putExtra("status", "0");
                        startActivity(intent);

                    }
                });

            }else if (getIntent().getStringExtra("status").equalsIgnoreCase("3")){

                layout_offers.setVisibility(View.VISIBLE);

                //     final double price = Double.parseDouble(getIntent().getStringExtra("total").replace(ConstValue.CURRENCY, " "));
               /* Log.e("###", String.valueOf(price));
                final double taxamt = (price * 10) / 100;
                Log.e("###", String.valueOf(taxamt));
                final_price = price;
                Log.e("###", String.valueOf(final_price));*/
                // tax.setText(ConstValue.CURRENCY + taxamt);
                //    fAmt = getIntent().getStringExtra("total").replace(ConstValue.CURRENCY, " ");
                  total_price.setText(ConstValue.CURRENCY+" "+fAmt);
                  final_total.setText(String.valueOf(ConstValue.CURRENCY+" "+final_price));
                //text_address.setText(getIntent().getStringExtra("address"));


                text_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(BillingActivity.this, AppointmentDetailsActivity.class);
                        intent.putExtra("test_names", getIntent().getStringExtra("test_names"));

                        Log.e("TEST TEST", getIntent().getStringExtra("test_names"));
                        intent.putExtra("lab_name", getIntent().getStringExtra("lab_name"));
                        intent.putExtra("start_time",time);
                        intent.putExtra("appointment_date",date);
                        intent.putExtra("tax", "0");
                        intent.putExtra("total_price", fAmt);
                        intent.putExtra("final_total", String.valueOf(final_price));
                        intent.putExtra("status", "3");
                        startActivity(intent);

                    }
                });

            }
        }

        layout_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BillingActivity.this, OfferScreen.class);

                Log.e("trans_amt", fAmt);
                Log.e("trans_amt", String.valueOf(fAmt));

                if (getIntent().getStringExtra("status").equalsIgnoreCase("0")) {


                    intent.putExtra("Activity", "0");
                    intent.putExtra("trans_amt", fAmt);
                    Log.e("Activity", "0");
                    intent.putExtra("isPaymentOptionChecked", isPaymentOptionChecked);

                } else if (getIntent().getStringExtra("status").equalsIgnoreCase("1")) {

                    intent.putExtra("Activity", "1");
                    intent.putExtra("trans_amt", fAmt);
                    intent.putExtra("isPaymentOptionChecked", isPaymentOptionChecked);

                } else if (getIntent().getStringExtra("status").equalsIgnoreCase("2")) {

                    intent.putExtra("Activity", "2");
                    intent.putExtra("trans_amt", fAmt);
                    intent.putExtra("isPaymentOptionChecked", isPaymentOptionChecked);

                }
                startActivityForResult(intent, 178);
            }
        });






        /*            FOR EVERY TRANSACTION  USER CAN USE 50 WALLET BALANCE ONLY    */

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean isProfile = checkProfile();


                if (isProfile) {
                    if (isPaymentOptionChecked) {
                        if (isCash) {

                            checkWallet.setVisibility(View.GONE);

                            if (isOfferApplied) {
                                isCash = false;
                                isPayUmoney = true;
                                MyUtility.showAlertMessage(BillingActivity.this, "This promo code is not applicable for pay by cash option");
                            } else {

                                if (getIntent().hasExtra("status")) {

                                    Log.e("Pay_by_cash", "true");
                                    if (getIntent().getStringExtra("status").equalsIgnoreCase("0")) {
                                        Log.e("########", "0");
                                        bookLab();
                                    } else if (getIntent().getStringExtra("status").equalsIgnoreCase("1")) {
                                        Log.e("########", "1");
                                        confirmBooking();
                                    } else if (getIntent().getStringExtra("status").equalsIgnoreCase("2")) {
                                        Log.e("#####", "2");
                                        bookLab();
                                    } else if (getIntent().getStringExtra("status").equalsIgnoreCase("3")) {
                                        Log.e("#####", "3");
                                        bookLab();
                                    }
                                }


                            }
                        } else if (isPayUmoney) {

                            AlertDialog.Builder ad = new AlertDialog.Builder(BillingActivity.this);
                            ad.setMessage("Proceed further with PayUMoney?");

                            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if (getIntent().getStringExtra("status").equalsIgnoreCase("0")) {
                                        Log.e("########", "0");

                                        bookLab();
                                    } else if (getIntent().getStringExtra("status").equalsIgnoreCase("1")) {
                                        Log.e("########", "1");


                                        confirmBooking();
                                    } else if (getIntent().getStringExtra("status").equalsIgnoreCase("2")) {
                                        Log.e("#####", "2");
                                        bookLab();
                                    }
                                }
                            });


                            ad.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();


                                }
                            });
                            AlertDialog dialog = ad.create();
                            dialog.show();
                            dialog.setCancelable(false);

                        }


                    } else {
                        MyUtility.showAlertMessage(BillingActivity.this, "Please Select Payment Option");
                    }
                } else {

                    AlertDialog.Builder ad = new AlertDialog.Builder(BillingActivity.this);
                    ad.setMessage("Please update your profile");

                    ad.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            startActivity(new Intent(BillingActivity.this, PersonalDetailsActivity.class));
                        }
                    });
                    AlertDialog dialog = ad.create();
                    dialog.show();



                }
            }

        });


        if (walletAmt >= 50) {
            isAmt50 = true;
            checkWallet.setText("Use Wallet Balance" + " " + "(" + ConstValue.CURRENCY + "50" + ")");
            //checkWallet.setChecked(true);
            // calculateWalletAmt(true);
            checkWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    calculateWalletAmt(isChecked);
                }
            });
        } else {
            isAmt50 = false;
            checkWallet.setVisibility(View.GONE);
            isWalletUse = false;
            //   is_use_wallet = 0;
        }


        RadioButton radioButton = radio_payment.findViewById(R.id.payUmoney);


        if (getIntent().getStringExtra("status").equals("1")) {

            if (isZiffy) {
                radioButton.setVisibility(View.VISIBLE);
                // radioButton.setChecked(true);
            } else {
                radioButton.setVisibility(View.GONE);
                layout_offers.setVisibility(View.GONE);
            }
        }


        radio_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isPaymentOptionChecked = true;

                if (checkedId == R.id.cash) {


                    if (checkWallet.isChecked()) {
                        checkWallet.setChecked(false);
                        calculateWalletAmt(false);
                    }

                    if (isWalletUse && isOfferApplied) {

                        isWalletUse = false;
                        isCash = true;
                        isPayUmoney = false;
                        isOfferApplied = false;
                        promoCodeId = "";
                        PromoCodeName = "";

                        final_price = Double.parseDouble(fAmt);
                        final_price = Double.parseDouble(fAmt);
                        final_total.setText(String.valueOf(final_price));
                        walletAmt = Double.parseDouble(common.getSession(ApiParams.ZIFFY_WALLET_AMT));


                        if (layout_offers.getVisibility() == View.VISIBLE) {
                            layout_offers.setVisibility(View.GONE);
                        }

                        if (layout_wallet_check.getVisibility() == View.VISIBLE) {
                            layout_wallet_check.setVisibility(View.GONE);
                        }

                        if (layout_offers_applied.getVisibility() == View.VISIBLE) {
                            layout_offers_applied.setVisibility(View.GONE);
                        }

                        txt_current_wallet_balance.setText("Wallet Balance:- " + ConstValue.CURRENCY + common.getSession("ziffy_wallet_amt"));

                        if (discountType.equals(CASHBACK)) {

                        } else if (discountType.equals(DISCOUNT)) {
                            txtv_total_discount.setText(ConstValue.CURRENCY + "0.00");
                            billing_amt = 0;
                            discount_amt = 0;
                        }

                        discountType = "";
                        //   isCashback = false;

                    } else if (isWalletUse) {

                        isWalletUse = false;
                        isCash = true;
                        isPayUmoney = false;
                        discountType = "";
                        final_price = Double.parseDouble(fAmt);
                        final_total.setText(String.valueOf(final_price));
                        walletAmt = Double.parseDouble(common.getSession(ApiParams.ZIFFY_WALLET_AMT));

                        if (layout_offers.getVisibility() == View.VISIBLE) {
                            layout_offers.setVisibility(View.GONE);
                        }

                        if (layout_wallet_check.getVisibility() == View.VISIBLE) {
                            layout_wallet_check.setVisibility(View.GONE);
                        }
                        txt_current_wallet_balance.setText("Wallet Balance:- " + ConstValue.CURRENCY + common.getSession(ApiParams.ZIFFY_WALLET_AMT));


                    } else {
                        isWalletUse = false;
                        isCash = true;
                        isPayUmoney = false;
                        isOfferApplied = false;
                        checkWallet.setVisibility(View.GONE);
                        layout_wallet_check.setVisibility(View.GONE);

                        final_price = Double.parseDouble(s1);
                        //total_price.setText(ConstValue.CURRENCY + fAmt);
                        final_total.setText(ConstValue.CURRENCY + final_price);
                        txtv_total_discount.setText(ConstValue.CURRENCY + "0.00");

                        promoCodeId = "";
                        PromoCodeName = "";
                        discountType = "";

                        layout_offers.setVisibility(View.GONE);
                        layout_offers_applied.setVisibility(View.GONE);

                        txt_current_wallet_balance.setText("Wallet Balance:- " + ConstValue.CURRENCY + common.getSession("ziffy_wallet_amt"));


                    }
                } else if (checkedId == R.id.payUmoney) {

                    isPayUmoney = true;
                    isCash = false;
                    isWalletUse = false;
                    if (isAmt50) {
                        checkWallet.setVisibility(View.VISIBLE);
                    } else {
                        checkWallet.setVisibility(View.GONE);
                    }
                    txt_current_wallet_balance.setVisibility(View.VISIBLE);

                    if (layout_wallet_check.getVisibility() == View.GONE) {
                        layout_wallet_check.setVisibility(View.VISIBLE);
                    }

                    if (layout_offers.getVisibility() == View.GONE) {
                        layout_offers.setVisibility(View.VISIBLE);
                    }

                    if (layout_offers_applied.getVisibility() == View.VISIBLE) {
                        layout_offers_applied.setVisibility(View.GONE);
                    }

                }
            }
        });
        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOfferApplied) {

                    if (discountType.equals(DISCOUNT)) {
                        final_price = final_price + discount_amt;
                        final_total.setText(String.valueOf(final_price));
                        txtv_total_discount.setText("00");
                        tv_apply.setVisibility(View.GONE);
                        layout_offers.setVisibility(View.VISIBLE);
                        discount_amt = 0;
                        discountType = "";
                        //  isCashback = false;
                        isOfferApplied = false;
                    } else if (discountType.equals(CASHBACK)) {
                        tv_apply.setVisibility(View.GONE);
                        layout_offers.setVisibility(View.VISIBLE);
                        discountType = "";
                        //  isCashback = false;
                        isOfferApplied = false;
                    }
                }

            }
        });

    }

    private void calculateWalletAmt(boolean isChecked) {

        if (isChecked) {
            isWalletUse = true;
            //  isWalletP = true;
            is_use_wallet = 1;

            final_price = final_price - 50;
            final_total.setText(String.valueOf(final_price));
            walletAmt = walletAmt - 50;
            txt_current_wallet_balance.setText("Wallet Balance:- " + ConstValue.CURRENCY + String.valueOf(walletAmt));


        } else {
            isWalletUse = false;
            is_use_wallet = 0;
            /// isWalletP = false;

            if (discountType.equals(CASHBACK)) {
                final_price = Double.parseDouble(fAmt);
            } else if (discountType.equals(DISCOUNT)) {
                final_price = final_price + 50;
            } else {
                final_price = Double.parseDouble(fAmt);
            }
            final_total.setText(String.valueOf(final_price));
            walletAmt = Double.parseDouble(common.getSession(ApiParams.ZIFFY_WALLET_AMT));
            txt_current_wallet_balance.setText("Wallet Balance:- " + ConstValue.CURRENCY + String.valueOf(walletAmt));
        }

    }

    private void confirmBooking() {

        /*txn_id
          amount
          payment_mode*/



        /*        $is_use_wallet =  $this->input->post("is_use_wallet");
        $wallet_amt =  $this->input->post("wallet_amt");
        $promo_id =  $this->input->post("promo_id");*/

        if (MyUtility.isConnected(BillingActivity.this)) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("bus_id", getIntent().getStringExtra("bus_id"));
            params.put("user_id", getIntent().getStringExtra("user_id"));
            params.put("sub_user_id", getIntent().getStringExtra("sub_user_id"));
            params.put("appointment_date", getIntent().getStringExtra("appointment_date"));
            params.put("start_time", getIntent().getStringExtra("start_time"));
            params.put("doct_id", getIntent().getStringExtra("doct_id"));
            params.put("time_token", getIntent().getStringExtra("time_token"));
            params.put("payment_status", "0");
            params.put("add_to_wallet", add_to_wallet);
            params.put("is_use_wallet", String.valueOf(is_use_wallet));
           /* params.put("is_use_wallet", String.valueOf(is_use_wallet));
           params.put("amount", String.valueOf(final_price));*/
            Log.e("is_use_wallet", String.valueOf(is_use_wallet));


            if (isPayUmoney) {
                params.put("payment_mode", "online");
                params.put("txn_id", txn_id);
                Log.e("Payu" + "txn_id", txn_id);
                params.put("is_use_wallet", String.valueOf(is_use_wallet));


                if (isOfferApplied) {

                    params.put("promo_id", promoCodeId);

                    if (is_use_wallet == 1) {
                        params.put("is_use_wallet", "1");
                        params.put("wallet_amt", String.valueOf(walletAmt));
                        params.put("amount", String.valueOf(final_price));
                    } else {
                        params.put("is_use_wallet", "0");
                        params.put("wallet_amt", String.valueOf(walletAmt));
                        params.put("amount", String.valueOf(final_price));
                    }
                } else {
                    params.put("promo_id", "");
                    params.put("amount", String.valueOf(final_price));
                    params.put("wallet_amt", String.valueOf(walletAmt));
                    params.put("is_use_wallet", String.valueOf(is_use_wallet));

                }


            } else if (isCash) {


                params.put("payment_mode", "cash");
                params.put("wallet_amt", wallet_amt);
                params.put("amount", String.valueOf(final_price));
                params.put("txn_id", "0");
                params.put("is_use_wallet", "0");


            }


            Log.e("PARAMS", params.toString());

            CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.BOOKING_URL, params, this.createRequestSuccessListenerBookAppointment(), this.createRequestErrorListenerBookAppointment());
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(customRequestForString);

            customRequestForString.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            startProgressDialog(BillingActivity.this);
        } else {
            MyUtility.showAlertMessage(this, MyUtility.INTERNET_ERROR);
            return;
        }


    }


    private void bookLab() {
        HashMap<String, String> params = new HashMap<String, String>();
        if (getIntent().getStringExtra("status").equalsIgnoreCase("2")) {
            /* Call<JsonObject> call3 = apiInterface.bookLab(getIntent().getStringExtra("lab_id"), getIntent().getStringExtra("finalDate"), getIntent().getStringExtra("finalTime"), getIntent().getStringExtra("app_id"), getIntent().getStringExtra("ishome"), getIntent().getStringExtra("test"), common.get_user_id(), txnid, getIntent().getStringExtra("total"), pay_mode,"0");*/


            params.put("lab_id", getIntent().getStringExtra("lab_id"));
            params.put("appointment_date", getIntent().getStringExtra("finalDate"));
            params.put("start_time", getIntent().getStringExtra("finalTime"));
            params.put("home_collection", getIntent().getStringExtra("ishome"));
            params.put("user_id", common.get_user_id());
            params.put("test", getIntent().getStringExtra("test"));
            params.put("appointment_id", getIntent().getStringExtra("app_id"));
            params.put("payment_status", "0");
            params.put("add_to_wallet", add_to_wallet);
            params.put("is_use_wallet", String.valueOf(is_use_wallet));
          /*  params.put("amount", String.valueOf(final_price));
            params.put("is_use_wallet", String.valueOf(is_use_wallet));*/
            if (isPayUmoney) {
                params.put("payment_mode", "online");
                params.put("txn_id", txn_id);
                params.put("wallet_amt", String.valueOf(walletAmt));
                params.put("is_use_wallet", String.valueOf(is_use_wallet));


                if (isOfferApplied) {

                    params.put("promo_id", promoCodeId);

                    if (is_use_wallet == 1) {
                        params.put("is_use_wallet", "1");
                        params.put("wallet_amt", String.valueOf(walletAmt));
                        params.put("amount", String.valueOf(final_price));
                    } else {
                        params.put("is_use_wallet", "0");
                        params.put("wallet_amt", String.valueOf(walletAmt));
                        params.put("amount", String.valueOf(final_price));
                    }
                } else {
                    params.put("promo_id", "");
                    params.put("amount", String.valueOf(final_price));
                    params.put("wallet_amt", String.valueOf(walletAmt));
                    params.put("is_use_wallet", String.valueOf(is_use_wallet));
                }


            } else if (isCash) {
                params.put("payment_mode", "cash");
                params.put("txn_id", "0");
                params.put("amount", String.valueOf(final_price));
                params.put("is_use_wallet", "0");
            }

            Log.e("PARAMS", params.toString());


            CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.BOOK_LAB, params, this.createRequestSuccessListenerBookLab(), this.createRequestErrorListenerBookLab());
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(customRequestForString);
            startProgressDialog(BillingActivity.this);

        } else if (getIntent().getStringExtra("status").equalsIgnoreCase("3")){


            /* Call<JsonObject> call3 = apiInterface.bookLab(getIntent().getStringExtra("lab_id"), getIntent().getStringExtra("finalDate"), getIntent().getStringExtra("finalTime"), getIntent().getStringExtra("app_id"), getIntent().getStringExtra("ishome"), getIntent().getStringExtra("test"), common.get_user_id(), txnid, getIntent().getStringExtra("total"), pay_mode,"0");*/


            params.put("lab_id", getIntent().getStringExtra("lab_id"));
            params.put("appointment_date", getIntent().getStringExtra("finalDate"));
            params.put("start_time", getIntent().getStringExtra("start_time"));
            params.put("home_collection","1");
            params.put("user_id", common.get_user_id());
            params.put("test", getIntent().getStringExtra("test"));
            params.put("appointment_id","965");
            params.put("payment_status", "0");
            params.put("add_to_wallet", add_to_wallet);
            params.put("is_use_wallet", String.valueOf(is_use_wallet));
          /*  params.put("amount", String.valueOf(final_price));
            params.put("is_use_wallet", String.valueOf(is_use_wallet));*/
            if (isPayUmoney) {
                params.put("payment_mode", "online");
                params.put("txn_id", txn_id);
                params.put("wallet_amt", String.valueOf(walletAmt));
                params.put("is_use_wallet", String.valueOf(is_use_wallet));


                if (isOfferApplied) {

                    params.put("promo_id", promoCodeId);

                    if (is_use_wallet == 1) {
                        params.put("is_use_wallet", "1");
                        params.put("wallet_amt", String.valueOf(walletAmt));
                        params.put("amount", String.valueOf(final_price));
                    } else {
                        params.put("is_use_wallet", "0");
                        params.put("wallet_amt", String.valueOf(walletAmt));
                        params.put("amount", String.valueOf(final_price));
                    }
                } else {
                    params.put("promo_id", "");
                    params.put("amount", String.valueOf(final_price));
                    params.put("wallet_amt", String.valueOf(walletAmt));
                    params.put("is_use_wallet", String.valueOf(is_use_wallet));
                }


            } else if (isCash) {
                params.put("payment_mode", "cash");
                params.put("txn_id", "0");
                params.put("amount", String.valueOf(final_price));
                params.put("is_use_wallet", "0");
            }

            Log.e("PARAMS", params.toString());


            CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.BOOK_LAB, params, this.createRequestSuccessListenerBookLab(), this.createRequestErrorListenerBookLab());
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(customRequestForString);
            startProgressDialog(BillingActivity.this);


        }else {


            if (MyUtility.isConnected(BillingActivity.this)) {

                params.put("lab_id", getIntent().getStringExtra("lab_id"));
                params.put("appointment_date", getIntent().getStringExtra("appointment_date"));
                params.put("start_time", getIntent().getStringExtra("start_time"));
                params.put("home_collection", getIntent().getStringExtra("home_collection"));
                params.put("user_id", common.get_user_id());
                params.put("test", getIntent().getStringExtra("test"));
                params.put("appointment_id", "965");
                params.put("payment_status", "0");
                params.put("amount", String.valueOf(final_price));
                params.put("is_use_wallet", String.valueOf(is_use_wallet));
                params.put("add_to_wallet", add_to_wallet);
                if (isPayUmoney) {
                    params.put("payment_mode", "online");
                    params.put("txn_id", txn_id);
                    params.put("wallet_amt", String.valueOf(walletAmt));
                    params.put("is_use_wallet", String.valueOf(is_use_wallet));


                    if (isOfferApplied) {

                        params.put("promo_id", promoCodeId);

                        if (is_use_wallet == 1) {
                            params.put("is_use_wallet", "1");
                            params.put("wallet_amt", String.valueOf(walletAmt));
                            params.put("amount", String.valueOf(final_price));
                        } else {
                            params.put("is_use_wallet", "0");
                            params.put("wallet_amt", String.valueOf(walletAmt));
                            params.put("amount", String.valueOf(final_price));
                        }
                    } else {
                        params.put("promo_id", "");
                        params.put("amount", String.valueOf(final_price));
                        params.put("wallet_amt", String.valueOf(walletAmt));
                        params.put("is_use_wallet", String.valueOf(is_use_wallet));
                    }


                } else if (isCash) {
                    params.put("payment_mode", "cash");
                    params.put("txn_id", "0");
                    params.put("is_use_wallet", "0");
                }

                Log.e("PARAMS", params.toString());
            }

            CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.BOOK_LAB, params, this.createRequestSuccessListenerBookLab(), this.createRequestErrorListenerBookLab());
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(customRequestForString);
            startProgressDialog(BillingActivity.this);
        }
       /* else {
            MyUtility.showAlertMessage(this, MyUtility.INTERNET_ERROR);
            return;
        }*/


    }


    private com.android.volley.Response.Listener<String> createRequestSuccessListenerBookLab() {
        return new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LAB_BOOK_RESPONSE", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("responce");


                    /*      if (result.equalsIgnoreCase("1")) {*/

                    if (result.equalsIgnoreCase("1")) {

                        if (isPayUmoney) {
                            isPayUmoney = false;

                            Log.e("is_use_wallet", String.valueOf(is_use_wallet));

                            Intent intent = new Intent(BillingActivity.this, MyPayuDemoActivity.class);
                            intent.putExtra("total_price", final_total.getText().toString().replace(ConstValue.CURRENCY, " "));
                            intent.putExtra("lab_name", lab_name);
                            intent.putExtra("status", "0");
                            intent.putExtra("txn_id", txn_id);
                            if (add_to_wallet.equals("")) {
                                intent.putExtra("add_to_wallet", "0");
                            } else {
                                intent.putExtra("add_to_wallet", add_to_wallet);
                            }
                            if (isWalletUse) {
                                intent.putExtra("is_use_wallet", "1");
                            } else {
                                intent.putExtra("is_use_wallet", "0");
                            }

                            intent.putExtra("wallet_amt", String.valueOf(walletAmt));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        } else if (isWalletUse) {

                            dialog1 = new MaterialDialog.Builder(BillingActivity.this)
                                    .customView(R.layout.dilog_order_success, false)
                                    .show();
                            dialog1.setCancelable(false);

                            View view = dialog1.getView();

                            TextView text_ok = view.findViewById(R.id.text_ok);
                            text_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                    Intent intent = new Intent(BillingActivity.this, MainActivity.class);
                                    //intent.putExtra("new","new");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });

                        } else if (isCash) {


                            dialog1 = new MaterialDialog.Builder(BillingActivity.this)
                                    .customView(R.layout.dilog_order_success, false)
                                    .show();
                            dialog1.setCancelable(false);

                            View view = dialog1.getView();
                            TextView text_ok = view.findViewById(R.id.text_ok);
                            text_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                    Intent intent = new Intent(BillingActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }
                            });
                        }


                    } else {

                        Toast.makeText(BillingActivity.this, "This time slot is recently booked.Please select other time slot.", Toast.LENGTH_SHORT).show();
                        finish();
                    }


                } catch (JSONException e) {
                    stopProgressDialog();
                    e.printStackTrace();
                }
            }
        };

    }


    private com.android.volley.Response.ErrorListener createRequestErrorListenerBookLab() {
        return new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialog();
                if (error instanceof TimeoutError) {
                    MyUtility.showAlertMessage(BillingActivity.this, "Server is busy.Please try again");
                }
                Log.i("##", "##" + error.toString());
                Toast.makeText(BillingActivity.this, "Lab is not available", Toast.LENGTH_SHORT).show();
                //App.showAlert("Something Went Wrong, Please Try again",MultiTestSearchActivity.this);

            }
        };
    }

    private com.android.volley.Response.Listener<String> createRequestSuccessListenerBookAppointment() {
        return new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                stopProgressDialog();
                Log.e("#####  " + "DOCTOR_BOOKING", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("responce");

                    if (result.equalsIgnoreCase("1")) {

                        if (isPayUmoney) {


                            Intent intent = new Intent(BillingActivity.this, MyPayuDemoActivity.class);
                            intent.putExtra("total_price", final_total.getText().toString().replace(ConstValue.CURRENCY, " "));
                            intent.putExtra("status", "1");
                            intent.putExtra("doct_name", getIntent().getStringExtra("doctor_name"));
                            intent.putExtra("txn_id", txn_id);

                            if (add_to_wallet.equals("")) {
                                intent.putExtra("add_to_wallet", "0");
                            } else {
                                intent.putExtra("add_to_wallet", add_to_wallet);
                            }

                            if (isWalletUse) {
                                intent.putExtra("is_use_wallet", "1");
                            } else {
                                intent.putExtra("is_use_wallet", "0");
                            }
                            intent.putExtra("wallet_amt", String.valueOf(walletAmt));
                            intent.putExtra("txn_id", txn_id);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else if (isWalletUse) {


                            dialog1 = new MaterialDialog.Builder(BillingActivity.this)
                                    .customView(R.layout.dilog_order_success, false)
                                    .show();
                            dialog1.setCancelable(false);

                            isDialog = true;

                            View view = dialog1.getView();
                            TextView text_ok = view.findViewById(R.id.text_ok);
                            text_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                    Intent intent = new Intent(BillingActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }
                            });

                        } else if (isCash) {

                            isDialog = true;

                            dialog1 = new MaterialDialog.Builder(BillingActivity.this)
                                    .customView(R.layout.dilog_order_success, false)
                                    .show();
                            dialog1.setCancelable(false);

                            View view = dialog1.getView();
                            TextView text_ok = view.findViewById(R.id.text_ok);
                            text_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                    Intent intent = new Intent(BillingActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }
                            });


                        }


                    } else {

                        MyUtility.showAlertMessage(BillingActivity.this, "This time slot is recently booked.Please select other time slot.");
                        finish();
                        Intent intent = new Intent(BillingActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                    stopProgressDialog();
                    e.printStackTrace();
                }


            }
        };
    }


    private Response.ErrorListener createRequestErrorListenerOffers() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    MyUtility.showAlertMessage(BillingActivity.this, "Server is busy.Please try again");
                }
                Log.i("##", "##" + error.toString());
                hideProgressBar();
            }
        };
    }

    private com.android.volley.Response.ErrorListener createRequestErrorListenerBookAppointment() {
        return new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialog();
                if (error instanceof TimeoutError) {
                    MyUtility.showAlertMessage(BillingActivity.this, "Server is busy.Please try again");
                }
                Log.i("##", "##" + error.toString());


            }
        };


    }

    private boolean checkProfile() {


        if (!common.getSession("salutation").equals("")) {
            if (!common.getSession("gender").equals("")) {
                if (!common.getSession("marital_status").equals("")) {
                    if (!common.getSession("dob").equals("")) {
                        if (!common.getSession("height").equals("")) {
                            if (!common.getSession("weight").equals("")) {
                                isProfile = true;
                            }
                        }
                    }
                }
            }
        }


        return isProfile;
    }

    @Override
    public void onBackPressed() {

        if (isDialog) {
            dialog1.setCancelable(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 178) {

            if (resultCode == Activity.RESULT_OK) {

//                if (walletAmt >= 50) {
//
//                    layout_wallet_check.setVisibility(View.VISIBLE);
//                    checkWallet.setText("Use Wallet Balance" + " " + "(" + ConstValue.CURRENCY + ".50" + ")");
//
//                    checkWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                            calculateWalletAmt(isChecked);
//                        }
//                    });
//                } else {
//                    checkWallet.setVisibility(View.GONE);
//                    isWalletUse = false;
//                    is_use_wallet = 0;
//                }

                PromoCodeName = data.getStringExtra("promo_name");
                promoCodeId = data.getStringExtra("promo_id");
                Log.e("promoCodeId", promoCodeId);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("promo_id", promoCodeId);
                //params.put("promo_id", "5");
                params.put("transaction_amt", fAmt);
                CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.APPLY_PROMO_CODE, params, this.createRequestSuccessListenerOffers(), this.createRequestErrorListenerOffers());
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(customRequestForString);
                showPrgressBar();

            }
        }

    }


    private com.android.volley.Response.Listener<String> createRequestSuccessListenerOffers() {
        return new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgressBar();
                Log.e("#####  " + "OFFER_APPLIED", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt("status") == 1) {

                        Log.e("status", "1");

                        layout_offers.setVisibility(View.GONE);
                        layout_offers_applied.setVisibility(View.VISIBLE);
                        tv_apply.setText("Promo code " + PromoCodeName + " is applied");
                        isOfferApplied = true;

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            Log.e("###", "true");

                            JSONObject object = jsonArray.getJSONObject(i);


                            if (object.getInt("cashback") == 0) {

                                // isCashback = false;
                                discountType = DISCOUNT;
                                isPayUmoney = true;

                                Log.e("cashback", String.valueOf(object.getInt("cashback")));
                                // total_price.setText(ConstValue.CURRENCY + object.getString("billing_amt"));
                                discount_amt = object.getInt("discount_amt");
                                Log.e("discount_amt", String.valueOf(discount_amt));
                                //  add_to_wallet=object.getString("add_to_wallet");


                                Log.e("discount_amt", "" + discount_amt);

                                calculateAmt();


                            } else {

                                Log.e("isCashback", "1");
                                discountType = CASHBACK;
                                //  isCashback = true;
                                // add_to_wallet=object.getString("add_to_wallet");
                                add_to_wallet = object.getString("add_to_wallet");


                                wallet_amt = object.getString("wallet_amt");
                                Log.e("###add_to_wallet", add_to_wallet);


                            }


                            Log.e("status", "1");
                            add_to_wallet = object.getString("add_to_wallet");

                            billing_amt = object.getInt("billing_amt");
                            wallet_amt = object.getString("wallet_amt");

                            Log.e("###add_to_wallet", add_to_wallet);
                            Log.e("wallet_amt", wallet_amt);


                        }

                        calculateAmt();

                    } else {
                        Log.e("status", "0");
                        isOfferApplied = false;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
    }

    private void calculateAmt() {


        if (discountType.equals(DISCOUNT)) {

            Log.e("###", "true");
            final_price = final_price - discount_amt;
            final_total.setText(String.valueOf(final_price));
            txtv_total_discount.setText(ConstValue.CURRENCY + String.valueOf(discount_amt));
            Log.e("Final_price", String.valueOf(final_price));


        } else if (discountType.equals(CASHBACK)) {

            final_total.setText(String.valueOf(final_price));

        }


    }




}



