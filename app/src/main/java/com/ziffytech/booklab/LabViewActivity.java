package com.ziffytech.booklab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.BillingActivity;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.MapActivity;
import com.ziffytech.booklab.apiinterface.APIInterface;
import com.ziffytech.booklab.apiinterface.ApiClient;
import com.ziffytech.booklab.apiinterface.models.LabViewApi;
import com.ziffytech.booklab.models.LabModel;
import com.ziffytech.util.MyUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LabViewActivity extends CommonActivity implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener,
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener
   {

    ArrayList<LabModel> mList;
    Button buttonChooseDate,buttontimeChoose;
    String currentdate;
    LabModel selected_business;
    TextView mLabName,mClosingTime,mOpeningTime,mLabAdddress,mLabTitle,direction;
    ImageView mLabImage;
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<LabModel> mDoctorArray;
    Calendar calender;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String finalDate="",finalTime="";
    ImageView labBg;
    private String days,lat,lng;
    String finalTest;
    String appID="";

       @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_view);
        allowBack();
        setHeaderTitle("Lab Details");
        setUpViews();

        calender = Calendar.getInstance(TimeZone.getDefault());
        buttonChooseDate = (Button) findViewById(R.id.buttonChooseDate);
        buttontimeChoose = (Button) findViewById(R.id.DateChooseDate);
        buttonChooseDate.setOnClickListener(this);
        buttontimeChoose.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        direction=(TextView)findViewById(R.id.direction);
        direction.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(LabViewActivity.this,MapActivity.class);
                i.putExtra("lat",lat);
                i.putExtra("lng",lng);
                i.putExtra("title",mLabTitle.getText());
                startActivity(i);
            }
        });










        Button submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(finalDate.equalsIgnoreCase(""))
                {

                    MyUtility.showAlertMessage(LabViewActivity.this,"Select Date");

                }
                else
                if(finalTime.equalsIgnoreCase(""))
                {

                    MyUtility.showAlertMessage(LabViewActivity.this,"Select Time");

                }else
                {

                    bookLab();

                }


            }
        });


    }

    @Override
    public void onClick(View view) {

        if (view == buttonChooseDate) {

            DateDialog();
        }

        if (view == buttontimeChoose) {


            try{

                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        LabViewActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                String[] openingTime=mOpeningTime.getText().toString().split(":");
                String[] closingTime=mClosingTime.getText().toString().split(":");
                tpd.setMinTime(Integer.parseInt(openingTime[0]), Integer.parseInt(openingTime[1]),0);
                tpd.setMaxTime(Integer.parseInt(closingTime[0]), Integer.parseInt(closingTime[1]),0);
                tpd.setThemeDark(false);
                tpd.show(getFragmentManager(), "Timepickerdialog");

            }catch (Exception e){

                e.printStackTrace();
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        LabViewActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                tpd.setThemeDark(false);
                tpd.show(getFragmentManager(), "Timepickerdialog");


            }
        }

    }

    public void setUpViews()
    {
        mLabName=(TextView)findViewById(R.id.buttonChooseLab);
        mLabAdddress=(TextView)findViewById(R.id.address);
        mClosingTime=(TextView)findViewById(R.id.totalAmount);
        mOpeningTime=(TextView)findViewById(R.id.totalTime);




        mLabImage=(ImageView) findViewById(R.id.LabIcon);
        labBg=(ImageView)findViewById(R.id.LabImage);



        mLabTitle=(TextView)findViewById(R.id.labName);
        mList=new ArrayList<>();
        getdata();

    }

    private void getdata() {

        Log.e("intent Data", getIntent().toUri(0));

      if (!MyUtility.isConnected(this)) {

            MyUtility.showAlertMessage(this, MyUtility.INTERNET_ERROR);

            return;
        }

        showPrgressBar();

        APIInterface apiInterface = ApiClient.getClient().create(APIInterface.class);
        Call<LabViewApi> call3 = apiInterface.get_lab_by_id(getIntent().getStringExtra("lab_id"));
        call3.enqueue(new Callback<LabViewApi>() {
            @Override
            public void onResponse(Call<LabViewApi> call, Response<LabViewApi> response) {

                Log.e("Data:", new Gson().toJson(response.body()));

                hideProgressBar();

                if (response.body() != null) {

                    LabViewApi model = response.body();

                    if (model.response == 1) {


                        if (model.data.size() > 0) {


                            for (int i = 0; i < model.data.size(); i++) {



                                String labName=model.data.get(i).lab_name;
                                mLabName.setText(labName);
                                mLabTitle.setText(labName);
                                String labaddress=model.data.get(i).lab_address;
                                mLabAdddress.setText(labaddress);
                                String closingtime=model.data.get(i).lab_closing_time;
                                mClosingTime.setText(closingtime);
                                String opening_time=model.data.get(i).lab_opening_time;
                                mOpeningTime.setText(opening_time);

                                days=model.data.get(i).working_days;
                                lat=model.data.get(i).lab_lat;
                                lng=model.data.get(i).lab_long;


                                Picasso.with(LabViewActivity.this).load(ConstValue.BASE_URL+"uploads/lab/"+model.data.get(i).lab_photo).into(labBg);
                                Picasso.with(LabViewActivity.this).load(ConstValue.BASE_URL+"uploads/lab/"+model.data.get(i).lab_photo).into(mLabImage);


                            }

                        }

                    } else {
                        if (model.response==0){

                           /* String msg= String.valueOf(model.data);
                            MyUtility.showAlertMessage(getApplicationContext(),msg);*/
                        }

                    }

                } else {


                    // MyUtility.showAlertMessage(getApplicationContext(), MyUtility.FAILED_TO_GET_DATA);
                }


            }

            @Override
            public void onFailure(Call<LabViewApi> call, Throwable t) {
                call.cancel();
                hideProgressBar();
                //  MyUtility.showAlertMessage(getApplicationContext(), MyUtility.INTERNET_ERROR);

            }
        });

    }


    private void bookLab() {

        String lab_id = getIntent().getStringExtra("lab_id");
        String app_id = getIntent().getStringExtra("app_id");
        String ishome = getIntent().getStringExtra("ishome");
        String test = getIntent().getStringExtra("test");
        String price = getIntent().getStringExtra("test_price");
        String details = getIntent().getStringExtra("test_details");
        String lab_name = getIntent().getStringExtra("lab_name");


        if (!finalDate.equalsIgnoreCase("")) {

            if (!finalTime.equalsIgnoreCase("")) {

                Intent intent = new Intent(LabViewActivity.this, BillingActivity.class);
                intent.putExtra("lab_id", lab_id);
                intent.putExtra("app_id", app_id);
                intent.putExtra("ishome", ishome);
                intent.putExtra("test", test);
                intent.putExtra("price", price);
                intent.putExtra("status", "2");
                intent.putExtra("finalDate", finalDate);
                intent.putExtra("finalTime", finalTime);
                intent.putExtra("status", "2");
                intent.putExtra("test_details", details);
                intent.putExtra("lab_name", lab_name);

                startActivity(intent);
            }
        }

/*
        if (!MyUtility.isConnected(this)) {

            MyUtility.showAlertMessage(this, MyUtility.INTERNET_ERROR);

            return;
        }*/

     /*   showPrgressBar();

        APIInterface apiInterface = ApiClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.bookLab(getIntent().getStringExtra("lab_id"),finalDate,finalTime,getIntent().getStringExtra("app_id"),getIntent().getStringExtra("ishome"),getIntent().getStringExtra("test"),common.get_user_id());

        Log.e("PARAMS",getIntent().getStringExtra("lab_id")+"---"+finalDate+"----"+finalTime+"----"+getIntent().getStringExtra("app_id")+"---"+getIntent().getStringExtra("ishome")+"-----"+getIntent().getStringExtra("test")+common.get_user_id()+"-------");

        call3.enqueue(new Callback<JsonObject>()
        {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                hideProgressBar();

                Log.e("Data :", new Gson().toJson(response.body()));

                String result=new Gson().toJson(response.body());
                Log.e("RESULT :", result);

                hideProgressBar();


                if (response.body() != null) {



                   MaterialDialog dialog1 = new MaterialDialog.Builder(LabViewActivity.this)
                            .customView(R.layout.dilog_order_success, false)
                            .show();

                    View view = dialog1.getView();

                    TextView text_ok = view.findViewById(R.id.text_ok);
                    text_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            Intent intent = new Intent(LabViewActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    });
                }else {


                     MyUtility.showAlertMessage(LabViewActivity.this, "Failed to get data");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                hideProgressBar();
               MyUtility.showAlertMessage(getApplicationContext(), "Something went wrong");

            }
        });

    }
*/
    }
    public void DateDialog(){


        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                LabViewActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dpd.setMinDate(now);
        now.add(Calendar.MONTH,+1);
        dpd.setMaxDate(now);

        try{

        if(!days.equalsIgnoreCase("")){

            Calendar sunday,mon,tue,wed,thu,fri,sat;

            List<Calendar> weekends = new ArrayList<>();

            int weeks =6;

            for (int i = 0; i < (weeks * 7) ; i=i+7) {


                if(!days.contains("sun")){
                    sunday = Calendar.getInstance();
                    sunday.add(Calendar.DATE,-10);
                    sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK)+7+i));
                    weekends.add(sunday);
                }

                if(!days.contains("mon")){
                    mon = Calendar.getInstance();
                    mon.add(Calendar.DATE,-10);
                    mon.add(Calendar.DAY_OF_YEAR, (Calendar.MONDAY - mon.get(Calendar.DAY_OF_WEEK) +7+i));
                    weekends.add(mon);
                }

                if(!days.contains("tue")){
                    tue = Calendar.getInstance();
                    tue.add(Calendar.DATE,-10);
                    tue.add(Calendar.DAY_OF_YEAR, (Calendar.TUESDAY - tue.get(Calendar.DAY_OF_WEEK) +7+ i));
                    weekends.add(tue);
                }

                if(!days.contains("wed")){
                    wed = Calendar.getInstance();
                    wed.add(Calendar.DATE,-10);
                    wed.add(Calendar.DAY_OF_YEAR, (Calendar.WEDNESDAY - wed.get(Calendar.DAY_OF_WEEK) +7+ i));
                    weekends.add(wed);
                }

                if(!days.contains("thu")){
                    thu = Calendar.getInstance();
                    thu.add(Calendar.DATE,-10);
                    thu.add(Calendar.DAY_OF_YEAR, (Calendar.THURSDAY - thu.get(Calendar.DAY_OF_WEEK)  +7+ i));
                    weekends.add(thu);
                }

                if(!days.contains("fri")){
                    fri = Calendar.getInstance();
                    fri.add(Calendar.DATE,-10);
                    fri.add(Calendar.DAY_OF_YEAR, (Calendar.FRIDAY - fri.get(Calendar.DAY_OF_WEEK) + 7 + i));
                    weekends.add(fri);
                }

                if(!days.contains("sat")){
                    sat = Calendar.getInstance();
                    sat.add(Calendar.DATE,-10);
                    sat.add(Calendar.DAY_OF_YEAR, (Calendar.SATURDAY - sat.get(Calendar.DAY_OF_WEEK) + 7 + i));
                    weekends.add(sat);
                }



            }

            Calendar[] disabledDays = weekends.toArray(new Calendar[weekends.size()]);
            dpd.setDisabledDays(disabledDays);


        }

        }catch (Exception e){

            e.printStackTrace();
        }


        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
         String currentdate =year +"-"+ (monthOfYear+1)+"-"+dayOfMonth;
         buttonChooseDate.setText(currentdate);
         finalDate=currentdate;
    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        buttontimeChoose.setText(hourOfDay + ":" + minute);
        finalTime=hourOfDay + ":" + minute;
    }


    private class JTEST{
        public String getTest_code() {
            return test_code;
        }
        public void setTest_code(String test_code) {
            this.test_code = test_code;
        }
        private String test_code;
    }
}




