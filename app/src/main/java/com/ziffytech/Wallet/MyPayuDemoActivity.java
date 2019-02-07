package com.ziffytech.Wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;

import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.BillingActivity;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.CustomRequestForString;
import com.ziffytech.activities.LoginActivity;
import com.ziffytech.activities.MainActivity;
import com.ziffytech.util.CommonClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MyPayuDemoActivity extends CommonActivity {
    String cust_id;
    Button btn_pay;
    CommonClass common;
    TextView txtFor, textTo, txt_amt_pay;
    String merchant_key = "sMp8wubk";
    String merchant_id = "6358042";
    String merchant_salt = "pJa4LBJWMg";
    String wallet_amt,is_use_wallet,add_to_wallet;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private WebView mWebView;
    MaterialDialog dialog1;


    public static String hashCal(String type, String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_payu_demo);

        setHeaderTitle("PayuMoney");
        allowBack();

        common = new CommonClass(MyPayuDemoActivity.this);


        wallet_amt=getIntent().getStringExtra("wallet_amt");
        is_use_wallet=getIntent().getStringExtra("is_use_wallet");
        String addwallet=getIntent().getStringExtra("add_to_wallet");
        if (addwallet.equals("")){
            add_to_wallet="0";
        }else {
            add_to_wallet=addwallet;
        }

        Log.e("wallet_amt",wallet_amt);
        Log.e("is_use_wallet",is_use_wallet);


        btn_pay = (Button) findViewById(R.id.btn_pay);
        textTo = findViewById(R.id.txt_paid_to);
        txtFor = findViewById(R.id.txt_paid_for);
        txt_amt_pay = findViewById(R.id.txt_amt_pay);

        if (getIntent().getStringExtra("status").equals("1")) {

            txtFor.setText("Doctor Appointment");
            textTo.setText(getIntent().getStringExtra("doct_name"));
            txt_amt_pay.setText(ConstValue.CURRENCY+(getIntent().getStringExtra("total_price")));
        } else {


            txtFor.setText("Lab Appointment");
            textTo.setText(getIntent().getStringExtra("lab_name"));
            txt_amt_pay.setText((getIntent().getStringExtra("total_price")));
        }




        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPayUMoneyFlow(getIntent().getStringExtra("txn_id"));
                Log.e("txn_id",getIntent().getStringExtra("txn_id"));
            }
        });


       /* mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        WebSettings ws = mWebView.getSettings();
        mWebView.addJavascriptInterface(new Object()
        {
            @JavascriptInterface
            public void performClick(String status)
            {
                // Deal with a click on the OK button
                //    Toast.makeText(mContext,"Payment Success",2000).show();
                //  Log.e("status payment-------->",status);
                finish();

            }
        }, "ok");*/

    }
    private void launchPayUMoneyFlow(String cust_id) {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        // //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("Done");



        // //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("PayUMoney");


        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(getIntent().getStringExtra("total_price"));
            Log.e("amount", String.valueOf(amount));
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*Random rand = new Random();
        Log.e("%04d%n", String.valueOf(rand.nextInt(10000)));
*/
        String txnId = cust_id;
        String phone = common.getSession("user_phone");
        String productInfo = getString(R.string.app_name);
        String firstName  = common.getSession("user_fullname");
        String email = common.getSession("user_email");
        String udf1 =wallet_amt;
        String udf2 = is_use_wallet;
        String udf3 = add_to_wallet;
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        builder.setAmount(String.valueOf(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productInfo)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(ApiParams.SURL)
                .setfUrl(ApiParams.FURL)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(false)
                .setKey(merchant_key)
                .setMerchantId(merchant_id);

        try {
            mPaymentParams = builder.build();

            /* Hash should always be generated from your server side.*/

            Log.e("mPaymentParams","" + mPaymentParams);


            HashMap<String, String> params = mPaymentParams.getParams();

            StringBuffer postParamsBuffer = new StringBuffer();
            postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
            postParamsBuffer.append(concatParams("productInfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
            postParamsBuffer.append(concatParams("firstName ", params.get(PayUmoneyConstants.FIRSTNAME)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));


            String postParams = params.get(PayUmoneyConstants.TXNID) + "|"
                    + params.get(PayUmoneyConstants.AMOUNT) + "|" + params.get(PayUmoneyConstants.PRODUCT_INFO) +
                    "|" + params.get(PayUmoneyConstants.FIRSTNAME) + "|" + params.get(PayUmoneyConstants.EMAIL) +
                    "|" + params.get(PayUmoneyConstants.UDF1) + "|" + params.get(PayUmoneyConstants.UDF2) +
                    "|" + params.get(PayUmoneyConstants.UDF3) + "|" + params.get(PayUmoneyConstants.UDF4) +
                    "|" + params.get(PayUmoneyConstants.UDF5) + "||||||";


            String postParams1 =merchant_key+"|"+params.get(PayUmoneyConstants.TXNID) + "|"
                    + params.get(PayUmoneyConstants.AMOUNT) + "|" + params.get(PayUmoneyConstants.PRODUCT_INFO) +
                    "|" + params.get(PayUmoneyConstants.FIRSTNAME) + "|" + params.get(PayUmoneyConstants.EMAIL) +
                    "|" + params.get(PayUmoneyConstants.UDF1) + "|" + params.get(PayUmoneyConstants.UDF2) +
                    "|" + params.get(PayUmoneyConstants.UDF3) + "|" + params.get(PayUmoneyConstants.UDF4) +
                    "|" + params.get(PayUmoneyConstants.UDF5) + "||||||"+merchant_salt;

            String hash = hashCal("SHA-512", postParams1);
            Log.e("HASH", hash);
            Log.e("post params",postParams);
            Log.e("post params1",postParams1);



            mPaymentParams.setMerchantHash(hash);
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, MyPayuDemoActivity.this, R.style.Mypayu, true);





            // generateHashFromServer(postParams,cust_id,common.getSession("user_email"),common.getSession("user_phone"),getIntent().getStringExtra("total_price"),common.getSession("user_fullname"),getString(R.string.app_name));




        } catch (Exception e) {
            Toast.makeText(MyPayuDemoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void generateHashFromServer(String postparams,String txn_id, String email,String phone,String amount,String firstname,String productinfo) {

        HashMap<String, String> params1 = new HashMap<String, String>();
     /* params1.put("txnid",cust_id);
        params1.put("amount",amount);
        params1.put("firstname",firstname);
        params1.put("email",email);
        params1.put("phone",phone);
        params1.put("productinfo",productinfo);*/
        //params1.put("customer_id",cust_id);
        params1.put("hash",postparams);
        params1.put("customer_id",cust_id);
        Log.e("params", String.valueOf(params1));

        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_HASH, params1, this.createRequestSuccessListener(), this.createRequestErrorListener());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);

    }




    private Response.Listener<String> createRequestSuccessListener() {
        return  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("RESPONSE", response.toString());


                try {
                    JSONObject jsonObject=new JSONObject(response);

                    String hash="";
                    String result=jsonObject.getString("status");
                    if (result.equals("1")){


                        hash=jsonObject.getString("data");
                        Log.e("HASH FROM SERVER",hash);
                    }





                    mPaymentParams.setMerchantHash(hash.trim());
                    // PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MyPayuDemoActivity.this, R.style.Mypayu, true);







                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##","##" + error.toString());
            }
        };
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.e("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra( PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE );

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if(transactionResponse.getTransactionStatus().equals( TransactionResponse.TransactionStatus.SUCCESSFUL )){


                    //Success Transaction
                    // Toast.makeText(this,"Success",Toast.LENGTH_LONG).show();
                   // showTransactionSuccessFailureAlert(this,"Success");

                    dialog1 = new MaterialDialog.Builder(MyPayuDemoActivity.this)
                            .customView(R.layout.dilog_order_success, false)
                            .show();
                    dialog1.setCancelable(false);

                    View view = dialog1.getView();
                    TextView text_ok = view.findViewById(R.id.text_ok);
                    text_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            Intent intent = new Intent(MyPayuDemoActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    });


                } else{



                      //showTransactionSuccessFailureAlert(this,"Fail");
                     //Toast.makeText(this,"Failure",Toast.LENGTH_LONG).show();
                    //Failure Transaction



                    MaterialDialog dialog1 = new MaterialDialog.Builder(MyPayuDemoActivity.this)
                            .customView(R.layout.dilog_order_success, false)
                            .show();


                    View view = dialog1.getView();
                    TextView text_ok = view.findViewById(R.id.text_ok);
                    TextView txt_info = view.findViewById(R.id.txt_info);
                    TextView txt1 = view.findViewById(R.id.txt1);
                    txt1.setVisibility(View.GONE);
                    txt_info.setText("Sorry!!!!\n Your transaction with Ziffytech is failed.Please Try Again");
                    ImageView imageView = view.findViewById(R.id.imageView4);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.fail));


                    text_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            Intent intent = new Intent(MyPayuDemoActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    });

                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
            }
// else if (resultModel != null && resultModel.getError() != null) {
//                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
//            } else {
//                Log.d(TAG, "Both objects are null!");
//            }
        }
    }
    void showTransactionSuccessFailureAlert(final Activity activity, String msg){

        new MaterialDialog.Builder(activity)
                .title(R.string.app_name)
                .content("Your Transaction is "+msg)
                .positiveText("Ok")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        Intent intent = new Intent(activity, MainActivity.class);
                        startActivity(intent);
                        dialog.dismiss();


                    }
                })
                .show();

    }
    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    @Override
     public void onBackPressed() {

        startActivity(new Intent(MyPayuDemoActivity.this,MainActivity.class));
    }
}
