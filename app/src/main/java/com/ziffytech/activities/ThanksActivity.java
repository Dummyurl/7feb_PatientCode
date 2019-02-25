package com.ziffytech.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.Wallet.MyPayuDemoActivity;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ThanksActivity extends CommonActivity {
    TextView txtPatientName, txtAge, txtGender;
    TextView txtBusinessName, txtDoctorName, txtDate, txtTimeSlot;
    DoctorModel selected_business;
    ImageView doctorImage;
    JSONObject object = null;
    MaterialDialog dialog1;
    private String userId = "0";
    private int MY_SOCKET_TIMEOUT_MS=5000;
     boolean isProfile=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        allowBack();
        setHeaderTitle("Confirm Appointment");


        txtDoctorName = (TextView) findViewById(R.id.doctorName);
        txtDate = (TextView) findViewById(R.id.bookedDate);
        txtTimeSlot = (TextView) findViewById(R.id.bookedTime);
        txtBusinessName = (TextView) findViewById(R.id.clinicName);
        txtPatientName = (TextView) findViewById(R.id.patientName);
        txtAge = (TextView) findViewById(R.id.age);
        txtGender = (TextView) findViewById(R.id.gender);
        doctorImage = (ImageView) findViewById(R.id.doctorImage);


        if (getIntent().getStringExtra("appointment_details").equals("")) {
            Log.e("RESCHEDULE", "false");
            selected_business = ActiveModels.DOCTOR_MODEL;

            Log.e("SELECTED BUSINESS", selected_business.toString());


            txtDoctorName.setText(selected_business.getDoct_name() + " , " + selected_business.getBus_title());
            txtBusinessName.setText(selected_business.getBus_title());
            txtDate.setText("Booking Date & Time :" + getIntent().getExtras().getString("date") + " , " + getIntent().getExtras().getString("timeslot"));
            txtTimeSlot.setText("Booking Time" + " : " + getIntent().getExtras().getString("timeslot"));

            Picasso.with(this).load(ConstValue.BASE_URL + "/uploads/profile/" + selected_business.getDoct_photo()).into(doctorImage);


            try {

                JSONObject data = new JSONObject(common.getSession(ApiParams.USER_JSON_DATA));

                txtPatientName.setText(common.getSession(ApiParams.USER_FULLNAME));
                txtAge.setText(common.getSession(ApiParams.USER_EMAIL));
                txtGender.setText("Mob. : " + common.getSession(ApiParams.USER_PHONE));

            } catch (Exception e) {

                e.printStackTrace();
            }


            Button confirm = (Button) findViewById(R.id.confirm);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                  isProfile=checkProfile();

                  if (isProfile){
                      confirm();
                  }else {


                      AlertDialog.Builder ad = new AlertDialog.Builder(ThanksActivity.this);
                      ad.setMessage("Please update your profile");

                      ad.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {

                              startActivity(new Intent(ThanksActivity.this, PersonalDetailsActivity.class));
                          }
                      });
                      AlertDialog dialog = ad.create();
                      dialog.show();


                  }

                }

            });


        } else {

            Log.e("RESCHEDULE", "true");


            txtPatientName.setText(common.getSession(ApiParams.USER_FULLNAME));
            txtAge.setText(common.getSession(ApiParams.USER_EMAIL));
            txtGender.setText("Mob. : " + common.getSession(ApiParams.USER_PHONE));

            String appointmentDetails = getIntent().getStringExtra("appointment_details");
            Log.e("appointmentDetails", appointmentDetails);

            try {

                JSONArray jsonArray = new JSONArray(appointmentDetails);

                for (int i = 0; i < jsonArray.length(); i++) {
                    object = jsonArray.getJSONObject(i);

                    // object.getString("doct_name")

                    txtDoctorName.setText(object.getString("doct_name") + " , " + object.getString("bus_title"));
                    txtBusinessName.setText(object.getString("bus_title"));
                    txtDate.setText("Booking Date & Time :" + getIntent().getExtras().getString("date") + " , " + getIntent().getExtras().getString("timeslot"));
                    txtTimeSlot.setText("Booking Time" + " : " + getIntent().getExtras().getString("timeslot"));


                    Picasso.with(this)
                            .load(ConstValue.BASE_URL + "/uploads/profile/" + object.getString("doct_photo"))
                            .into(doctorImage);


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            Button confirm = (Button) findViewById(R.id.confirm);
            confirm.setText("RESCHEDULE APPOINTMENT");
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        updateAppointment(object.getString("bus_id"), common.getSession("user_id"), object.getString("doct_id"));
                        Log.e("doct_id", object.getString("doct_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }


        Button member = (Button) findViewById(R.id.addMember);
        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent intent = new Intent(ThanksActivity.this,RelativeProfile.class);
                intent.putExtra("result","result");
                startActivityForResult(intent,1);  */

                Toast.makeText(ThanksActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateAppointment(String bus_id, String userId, String doctid) {


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("bus_id", bus_id);
        params.put("appointment_id", getIntent().getStringExtra("app_id"));
        params.put("appointment_date", getIntent().getStringExtra("date"));
        params.put("start_time", getIntent().getStringExtra("timeslot"));
        params.put("time_token", getIntent().getStringExtra("time_token"));
        params.put("doct_id", doctid);
        params.put("user_id", userId);
        params.put("sub_user_id", "0");

        Log.e("PARAMS", params.toString());

        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.BOOKING_URL, params, this.createRequestSuccessListenerReschedule(), this.createRequestErrorListenerReschedule());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);
        showPrgressBar();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            txtPatientName.setText(data.getExtras().getString("name"));
            txtAge.setText("Age:" + data.getExtras().getString("age"));

            if (data.getExtras().getString("gender").equalsIgnoreCase("0")) {

                txtGender.setText("Gender:" + " Male");

            } else if (data.getExtras().getString("gender").equalsIgnoreCase("1")) {

                txtGender.setText("Gender:" + " Female");

            }


            userId = data.getExtras().getString("user_id");


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void confirm() {
        String date = getIntent().getStringExtra("date");
        String doct_id = selected_business.getDoct_id();

        String time_token = getIntent().getStringExtra("time_token");

        Log.e("Token", getIntent().getExtras().getString("time_token"));



/*

        Intent intent = new Intent(ThanksActivity.this, BillingActivity.class);
        intent.putExtra("bus_id", selected_business.getBus_id());
        intent.putExtra("user_id", common.get_user_id());
        intent.putExtra("sub_user_id", userId);
        intent.putExtra("appointment_date", date);
        Log.e("DATE", date);
        intent.putExtra("start_time", getIntent().getExtras().getString("timeslot"));
        intent.putExtra("time_token", time_token);
        intent.putExtra("doct_id", doct_id);
        intent.putExtra("doctor_name", selected_business.getDoct_name());
        Log.e("DOCT_NAME", selected_business.getDoct_name());
        intent.putExtra("clinic_name", selected_business.getBus_title());
        Log.e("DOCT_ID", selected_business.getDoct_id());
        intent.putExtra("status", "1");
        intent.putExtra("amount", selected_business.getConsult_from());
        intent.putExtra("is_ziffydoc", selected_business.getIs_ziffydoc());
        Log.e("AMOUNT", selected_business.getConsult_from());
        Log.e("is_ziffydoc", selected_business.getIs_ziffydoc());
        if (ActiveModels.RESCHEDULE_APP_ID != null && !ActiveModels.RESCHEDULE_APP_ID.equalsIgnoreCase("")) {

            intent.putExtra("appointment_id", ActiveModels.RESCHEDULE_APP_ID);

        }
*/

    /*    startActivity(intent);*/

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("bus_id",  selected_business.getBus_id());
        params.put("user_id", common.get_user_id());
        params.put("sub_user_id",userId);
        params.put("appointment_date",date);
        params.put("start_time",getIntent().getExtras().getString("timeslot"));
        params.put("doct_id",doct_id);
        params.put("time_token", getIntent().getStringExtra("time_token"));
        params.put("payment_status", "0");
        params.put("add_to_wallet", "");
        params.put("amount", selected_business.getConsult_from());
        params.put("is_use_wallet","0");
        params.put("promo_id", "");
        params.put("payment_mode", "cash");
        params.put("wallet_amt",common.getSession(ApiParams.ZIFFY_WALLET_AMT));

        Log.e("PARAMS", params.toString());

        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.BOOKING_URL, params, this.createRequestSuccessListenerBookAppointment(), this.createRequestErrorListenerBookAppointment());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);

        customRequestForString.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        startProgressDialog(ThanksActivity.this);


    }


    private com.android.volley.Response.Listener<String> createRequestSuccessListenerReschedule() {
        return new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgressBar();
                Log.e("##### " + "DOCTOR RESCHEDULE", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("responce");


                    if (result.equalsIgnoreCase("1")) {


                        dialog1 = new MaterialDialog.Builder(ThanksActivity.this)
                                .customView(R.layout.dilog_order_success, false)
                                .show();
                        dialog1.setCancelable(false);

                        View view = dialog1.getView();

                        TextView text_ok = view.findViewById(R.id.text_ok);
                        TextView txt_info = view.findViewById(R.id.txt_info);
                        txt_info.setText("Your Appointment has been rescheduled.");
                        text_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                                Intent intent = new Intent(ThanksActivity.this, MainActivity.class);
                                //intent.putExtra("new","new");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


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

    private com.android.volley.Response.ErrorListener createRequestErrorListenerReschedule() {
        return new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##", "##" + error.toString());
                hideProgressBar();

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





                            dialog1 = new MaterialDialog.Builder(ThanksActivity.this)
                                    .customView(R.layout.dilog_order_success, false)
                                    .show();
                            dialog1.setCancelable(false);

                            View view = dialog1.getView();
                            TextView text_ok = view.findViewById(R.id.text_ok);
                            text_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                    Intent intent = new Intent(ThanksActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }
                            });
                    } else {

                        MyUtility.showAlertMessage(ThanksActivity.this, "This time slot is recently booked.Please select other time slot.");
                        finish();
                        Intent intent = new Intent(ThanksActivity.this, MainActivity.class);
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


    private com.android.volley.Response.ErrorListener createRequestErrorListenerBookAppointment() {
        return new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialog();
                if (error instanceof TimeoutError) {
                    MyUtility.showAlertMessage(ThanksActivity.this, "Server is busy.Please try again");
                }
                Log.i("##", "##" + error.toString());


            }
        };


    }


}
