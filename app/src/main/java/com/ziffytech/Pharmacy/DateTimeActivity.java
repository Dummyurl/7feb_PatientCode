package com.ziffytech.Pharmacy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.BillingActivity;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.alarm.DateTime;
import com.ziffytech.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class DateTimeActivity extends CommonActivity {

    EditText date_time, address;
    String medicine,pres_id;
    double total_price = 0;
    double total = 0;
    Button btn_proceed;
    TextView buttonChooseDate, buttonChooseTime;
    private int mDay,mMonth,mYear;
    private int mHour ;
    private int mMinute;
    private int  msec ;
    String appointment_date="",start_time="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);
        setHeaderTitle("Delivery Details");
        allowBack();
        setupView();

        medicine = getIntent().getStringExtra("medicine");
        pres_id = getIntent().getStringExtra("pres_id");
        Log.e("#medicine", medicine);
        Log.e("#pres_id", pres_id);


        if (!common.getSession("location").equals("")){

            address.setText(common.getSession("location"));
        }




        buttonChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();

            }
        });


        buttonChooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectTime();

            }
        });




        try {
            JSONArray jsonArray = new JSONArray(medicine);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);

                double price = Double.parseDouble(object.getString("medicine_price"));
                double qty = Double.parseDouble(object.getString("quantity"));

                total = price * qty;
                total_price = total_price + total;
            }


            btn_proceed.setText("( PROCEED " + ConstValue.CURRENCY + total_price + " )");

            Log.e("total_price", String.valueOf(total_price));
            Log.e("total", String.valueOf(total));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!address.getText().toString().equals("")) {

                    if (!appointment_date.equals("")) {

                        if (!start_time.equals("")){


                            Intent intent = new Intent(DateTimeActivity.this, BillingActivity.class);

                            intent.putExtra("total", String.valueOf(total_price));
                            intent.putExtra("medicine", medicine);
                            intent.putExtra("pres_id", pres_id);
                            intent.putExtra("address", address.getText().toString());
                            intent.putExtra("appointment_date",appointment_date);
                            intent.putExtra("start_time",start_time);
                            intent.putExtra("status", "4");
                            startActivity(intent);

                        }else {

                            MyUtility.showAlertMessage(DateTimeActivity.this, "Please select time");
                        }



                    } else {

                        MyUtility.showAlertMessage(DateTimeActivity.this, "Please select date");
                    }
                } else {

                    MyUtility.showAlertMessage(DateTimeActivity.this, "Please enter address");
                }


            }
        });



    }



    private void selectTime() {



        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        msec = c.get(Calendar.SECOND);


        TimePickerDialog timePickerDialog = new TimePickerDialog(DateTimeActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        buttonChooseTime.setText(hourOfDay + ":" + minute+":"+msec);
                        start_time=hourOfDay + ":" + minute+":"+msec;
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();





    }


    private void selectDate() {


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(DateTimeActivity.this,R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        buttonChooseDate.setText((dayOfMonth)+ "-" + (monthOfYear+1) + "-" + year);

                        appointment_date=(dayOfMonth)+ "-" + (monthOfYear+1) + "-" + year;


                    }







                }, mYear, mMonth, mDay);



        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                finish();
            }
        });
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();






    }


    private void setupView() {


        address = findViewById(R.id.address);
        buttonChooseTime = findViewById(R.id.buttonChooseTime);
        buttonChooseDate = findViewById(R.id.buttonChooseDate);
        btn_proceed = findViewById(R.id.proceed);
        medicine = getIntent().getStringExtra("medicine");
    }
}
