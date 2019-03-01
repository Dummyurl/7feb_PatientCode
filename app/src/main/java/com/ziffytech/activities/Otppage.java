package com.ziffytech.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.models.Medication_History_Model;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.SaveSharedPreference;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Otppage extends CommonActivity implements View.OnClickListener {

    EditText edt_mobilenumber, edt_otp;
    TextView txtv_getotp, txtv_verifyotp, txtv_resend;
    String otpmsg, usermobile;
    String ServerOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otppage);
        getSupportActionBar().hide();

        edt_otp = (EditText) findViewById(R.id.edt_otp);
        txtv_verifyotp = (TextView) findViewById(R.id.txtv_verifyotp);
        txtv_verifyotp.setOnClickListener(this);
        txtv_resend = (TextView) findViewById(R.id.txtv_resend);
        txtv_resend.setOnClickListener(this);
//        common.setSession(ApiParams.OTP,"true");

        try {

            SendOtp();
            edt_otp.setText("");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text", messageText);
                String[] splited = messageText.split("\\s+");
                Toast.makeText(Otppage.this, "Message: " + messageText, Toast.LENGTH_LONG).show();
                edt_otp.setText("" + splited[0]);
            }
        });*/

    }

    private void SendOtp() throws JSONException {

        Log.e("SEND OTP", "TRUE");

        int randomPIN = (int) (Math.random() * 9000) + 1000;
        //  JSONObject data = new JSONObject(common.getSession(ApiParams.USER_JSON_DATA));
        otpmsg = ("") + String.valueOf(randomPIN).toString();
        usermobile = getIntent().getStringExtra("user_phone");

        Log.e("usermobile", getIntent().getStringExtra("user_phone"));

        //  Toast.makeText(this, usermobile, Toast.LENGTH_SHORT).show();


       /* String base="http://login.wishbysms.com/api/sendhttp.php?authkey=223369ArknFeJ7GNB5b372f1a&mobiles="+usermobile+"&message="+otpmsg+"%20is%20Your%20OTP.%20Welcome%20To%20Ziffytech%20Digital%20Healthacare%20Pvt.%20Ltd.&sender=ZIFFYT&route=4&country=91";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.post(base, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String items) {
                pd.dismiss();
            }

            public void onFailure(int statusCode, Throwable error, String content)
            {
                pd.dismiss();
                Toast.makeText(Otppage.this, "Sending fail", Toast.LENGTH_LONG).show();
            }
        }); */

        HashMap<String, String> params = new HashMap<>();
        params.put("otpmsg", otpmsg);
        params.put("usermobile", usermobile);
        Log.e("PARAMS", params.toString());
        String url = ApiParams.GET_OTP_SMS;
        showPrgressBar();
        VJsonRequest vJsonRequest = new VJsonRequest(this, url, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) throws JSONException {
                        Log.e("GET_OTP_RES", responce);
                        hideProgressBar();
                        JSONObject jsonObject = new JSONObject(responce);
                        ServerOtp = jsonObject.getString("OTP");
                    }

                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                    }
                });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtv_verifyotp) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


            if (edt_otp.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
            } else if (edt_otp.getText().toString().length() < 4) {
                Toast.makeText(this, "Enter valid OTP", Toast.LENGTH_SHORT).show();
            } else {
                if (networkInfo != null && networkInfo.isConnected()) {
                    String otp = edt_otp.getText().toString();
                    verifyOtp(otp);

                } else {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setCancelable(false);
                    alert.setTitle("Network info").setMessage("No Internet Available").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alert.show();
                }
            }


        }

        if (v.getId() == R.id.txtv_resend) {
            try {
                SendOtp();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

   /* public void readMessage()
    {
        Log.e("####","TRUE");
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText, String sendtext)
             {
                Log.e("Text",messageText);
                edt_otp.setText(messageText);
            }
        });
    }*/


    private void userRegister(HashMap<String, String> params) {


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.REGISTER_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);


                            if (userdata.getInt("responce") == 1) {
                                JSONObject data = userdata.getJSONObject("data");
                                common.setSession(ApiParams.COMMON_KEY, data.getString("user_id"));

                                common.setSession(ApiParams.USER_FULLNAME, data.getString("user_fullname"));
                                common.setSession(ApiParams.USER_PHONE, data.getString("user_phone"));
                                common.setSession(ApiParams.USER_JSON_DATA, data.toString());

                                Intent intent = new Intent(Otppage.this, ehrdump2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);


                                //intent.putExtra("new","new");


                            } else {
                                MyUtility.showAlertMessage(Otppage.this, "Provided email or mobile number is already Registered with us,If you forgotten your password then reset your password.");
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                    }
                });

    }


    private void verifyOtp(String otp) {
        if (otp.equals(ServerOtp)) {
            Log.e("verifyOtp", "true");

            HashMap<String, String> params = new HashMap<>();
            params.put("user_fullname", getIntent().getStringExtra("user_fullname"));
            params.put("user_phone", getIntent().getStringExtra("user_phone"));
            if (getIntent().hasExtra("user_email")) {
                params.put("user_email", getIntent().getStringExtra("user_email"));
            }

            if (!SaveSharedPreference.getPrefRefCode(Otppage.this).equals("")) {

                params.put("referral_code", SaveSharedPreference.getPrefRefCode(Otppage.this));
            } else {
                params.put("referral_code", "");
            }

            Log.e("PARAMS", params.toString());
            userRegister(params);

        } else {
            Toast.makeText(this, "Your number is not verified", Toast.LENGTH_SHORT).show();
        }
    }
}
