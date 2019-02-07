package com.ziffytech.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Mahesh on 17/10/17.
 */

public class MedicineOrderAdressActivity extends CommonActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText pincode, city, full_address, Pref_time;
    int mHour, mMinute;
    String user_id="";
    String aMpM = "AM";
    Button submit;
    String currentdate;

    private String pharamPath;

    Button showll,showll1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        setHeaderTitle("Add Address");
        allowBack();
        bindView();
        getPharamacy();

    }

    public void bindView() {

        pincode = (EditText) findViewById(R.id.pin);
        //city = (EditText) findViewById(R.id.mycity);
        full_address = (EditText) findViewById(R.id.address);
        Pref_time = (EditText) findViewById(R.id.time);

        showll=(Button) findViewById(R.id.showll);
       /* showll1=(Button) findViewById(R.id.showll1);

        showll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MedicineOrderAdressActivity.this,OrderPrescribedMedicine.class));
            }
        });
*/


        if (!common.getSession("location").equals("")){

            full_address.setText(common.getSession("location"));
        }



        showll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(MedicineOrderAdressActivity.this, MasterWebView.class);
                i.putExtra("title","Medication History");
                i.putExtra("link",ConstValue.BASE_PHARMACY+"onlineprescription/medication_history_list_api/"+common.getSession("user_id"));
                Log.e("link",pharamPath+common.getSession("user_id"));
                startActivity(i);

            }

        });


        Pref_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateDialog();
            }

        });


        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Order();
            }

            //  startActivity(new Intent(MedicineOrderAdressActivity.this,MedicineOrderActivity.class));


        });




    }

    public void DateDialog(){

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                MedicineOrderAdressActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        now.add(Calendar.MONTH,+1);
        dpd.setMaxDate(now);
        dpd.show(getFragmentManager(), "Datepickerdialog");



    }




    public void Order() {

        String mypin = pincode.getText().toString().trim();
      //  String mycity = city.getText().toString().trim();
        String myAddress = full_address.getText().toString().trim();
        String preftime = Pref_time.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if(TextUtils.isEmpty(mypin)) {
            pincode.setError(getString(R.string.enterpin));
            focusView = pincode;
            focusView.requestFocus();
            return;
        }

        if (mypin.length()<6) {
            pincode.setError("Enter Valid Pin");
            focusView = pincode;
            focusView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(myAddress)) {
            full_address.setError(getString(R.string.enteraddress));
            focusView = full_address;
            focusView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(preftime)) {
            Pref_time.setError(getString(R.string.enetertime));
            focusView = Pref_time;
            focusView.requestFocus();
            return;
        }

            Intent intent = new Intent(MedicineOrderAdressActivity.this, MedicineOrderActivity.class);
            intent.putExtra(ApiParams.PINCODE, mypin);
       //     intent.putExtra(ApiParams.CITY, mycity);
            intent.putExtra(ApiParams.ADDRESS, myAddress);
            intent.putExtra(ApiParams.Time, preftime);
            startActivity(intent);
      }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        currentdate =year +"-"+ (monthOfYear+1)+"-"+dayOfMonth;

        Calendar now = Calendar.getInstance();
        TimePickerDialog timepickerdialog = TimePickerDialog.newInstance(
                MedicineOrderAdressActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);
        timepickerdialog.setMinTime(now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),now.get(Calendar.SECOND));
        timepickerdialog.show(getFragmentManager(), "Timepickerdialog");


    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        if(hourOfDay >11)
        {
            aMpM = "PM";
        }

        if(hourOfDay>11)
        {
            mHour = hourOfDay - 12;
        }
        else
        {
            mHour = hourOfDay;
        }

        Pref_time.setText(currentdate +" "+ String.valueOf(mHour) + ":" + minute +" "+ aMpM);

    }

    public void getPharamacy() {



        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());
        Log.e("PARAMS",params.toString());
        showPrgressBar();

        VJsonRequest vJsonRequest = new VJsonRequest(this, ConstValue.GET_PHARMA, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {
                        hideProgressBar();

                        Log.e("Response:",responce);

                        JSONObject userdata=null;


                        try {
                            userdata = new JSONObject(responce);

                            if (userdata.getInt("response") ==1) {


                                pharamPath=userdata.getString("prescription_path");
                                Log.e("URL",pharamPath);

                                showll.setVisibility(View.VISIBLE);
                            }else{

                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } }

                    @Override
                    public void VError(String responce) {
                         hideProgressBar();
                    }
                });
    }
}



