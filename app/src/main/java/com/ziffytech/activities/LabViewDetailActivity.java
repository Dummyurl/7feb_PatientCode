package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.booklab.apiinterface.APIInterface;
import com.ziffytech.booklab.apiinterface.ApiClient;
import com.ziffytech.booklab.apiinterface.models.LabViewApi;
import com.ziffytech.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LabViewDetailActivity extends CommonActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    ArrayList<com.ziffytech.booklab.models.LabModel> mList;
    Button buttonChooseDate, buttontimeChoose;
    String currentdate;
    com.ziffytech.booklab.models.LabModel selected_business;
    TextView mLabName, mClosingTime, mOpeningTime, mLabAdddress, mLabTitle, direction, text_lab_name;
    ImageView mLabImage;
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<com.ziffytech.booklab.models.LabModel> mDoctorArray;
    Calendar calender;
    ImageView labBg;
    CheckBox checkHomeCollection;
    TextView text_details, text_total_price;
    /*ImageView image_next;*/
    String isHomeChecked = "0";
    String lab_id, testIDs, lab_name;
    String price;
    ImageView image_next;
    LinearLayout lin_book_lab;
    double total_price = 0, final_total, tax_value;
    String finalTest, testName;
    String tax_amt, total_amt, final_amt;
    String app_id;
    String ishome;

    String test_name;
    ArrayList<TEST> arrayList;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String finalDate = "", finalTime = "";
    private String days, lat, lng;
    List<JTEST> testList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_detail_view);
        allowBack();
        setHeaderTitle("Lab Details");
        setUpViews();


        calender = Calendar.getInstance(TimeZone.getDefault());
        buttonChooseDate = (Button) findViewById(R.id.buttonChooseDate);
        buttontimeChoose = (Button) findViewById(R.id.DateChooseDate);


        bookLab();

        buttonChooseDate.setOnClickListener(this);
        buttontimeChoose.setOnClickListener(this);

        checkHomeCollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isHomeChecked = "1";
                } else {
                    isHomeChecked = "0";
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


            try {

                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        LabViewDetailActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                String[] openingTime = mOpeningTime.getText().toString().split(":");
                String[] closingTime = mClosingTime.getText().toString().split(":");
                tpd.setMinTime(Integer.parseInt(openingTime[0]), Integer.parseInt(openingTime[1]), 0);
                tpd.setMaxTime(Integer.parseInt(closingTime[0]), Integer.parseInt(closingTime[1]), 0);
                tpd.setThemeDark(false);
                tpd.show(getFragmentManager(), "Timepickerdialog");

            } catch (Exception e) {

                e.printStackTrace();
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        LabViewDetailActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                tpd.setThemeDark(false);
                tpd.show(getFragmentManager(), "Timepickerdialog");


            }
        }


    }

    public void setUpViews() {
        mLabName = (TextView) findViewById(R.id.text_lab_name);
        mLabAdddress = (TextView) findViewById(R.id.address);
        mClosingTime = (TextView) findViewById(R.id.totalAmount);
        mOpeningTime = (TextView) findViewById(R.id.totalTime);

        checkHomeCollection = (CheckBox) findViewById(R.id.homecollection);

        text_details = (TextView) findViewById(R.id.text_details);

        text_total_price = (TextView) findViewById(R.id.text_total_price);
        image_next = (ImageView) findViewById(R.id.image_next);

        lin_book_lab = (LinearLayout) findViewById(R.id.lin_book_lab);


        lab_id = getIntent().getStringExtra("lab_id");
        testIDs = getIntent().getStringExtra("test");
        Log.e("testIDs", testIDs);
        lab_name = getIntent().getStringExtra("lab_name");


        mLabImage = (ImageView) findViewById(R.id.LabIcon);
        labBg = (ImageView) findViewById(R.id.LabImage);


        mLabTitle = (TextView) findViewById(R.id.labName);
        mList = new ArrayList<>();


        //   text_total_price.setText(getIntent().getStringExtra("total"));

        text_total_price.setText("("+getIntent().getStringExtra("total")+")");

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

                                LabModel model1 = new LabModel();

                                String labName = model.data.get(i).lab_name;
                                mLabName.setText(labName);
                                //     mLabTitle.setText(labName);
                                String labaddress = model.data.get(i).lab_address;
                                mLabAdddress.setText(labaddress);
                                String closingtime = model.data.get(i).lab_closing_time;
                                mClosingTime.setText(closingtime);
                                String opening_time = model.data.get(i).lab_opening_time;
                                mOpeningTime.setText(opening_time);

                                String lab_details = model.data.get(i).lab_details;
                                text_details.setText(lab_details);

                                days = model.data.get(i).working_days;
                                lat = model.data.get(i).lab_lat;
                                lng = model.data.get(i).lab_long;


                                Picasso.with(LabViewDetailActivity.this).load(ConstValue.BASE_URL + "uploads/lab/" + model.data.get(i).lab_photo).into(labBg);
                                //       Picasso.with(LabViewDetailActivity.this).load(ConstValue.BASE_URL+"uploads/lab/"+model.data.get(i).lab_photo).into(mLabImage);


                            }

                        }

                    } else {
                        // MyUtility.showAlertMessage(getApplicationContext(), MyUtility.FAILED_TO_GET_DATA);
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

        lin_book_lab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (!finalDate.equalsIgnoreCase("")) {

                    if (!finalTime.equalsIgnoreCase("")) {


                        Intent intent = new Intent(LabViewDetailActivity.this, BillingActivity.class);

                        if (getIntent().hasExtra("Activity")) {
                            intent.putExtra("app_id", app_id);
                            Log.e("APP_ID_Intent", app_id);
                            intent.putExtra("lab_id", lab_id);
                            intent.putExtra("ishome", isHomeChecked);
                            intent.putExtra("test", finalTest);
                            Log.e("**TEST ID", finalTest);
                            Log.e("***TEST Name", testName);
                            intent.putExtra("address",getIntent().getStringExtra("address"));
                            intent.putExtra("test_names", testName);
                            intent.putExtra("total", getIntent().getStringExtra("total").replace(ConstValue.CURRENCY,"" +
                                    ""));
                            // Log.e("**TEST PRICE",total_amt);
                            intent.putExtra("status", "2");
                            intent.putExtra("finalDate", finalDate);
                            intent.putExtra("finalTime", finalTime);
                            intent.putExtra("lab_name", lab_name);

                        } else {


                            intent.putExtra("lab_id", lab_id);
                            intent.putExtra("lab_name", lab_name);
                            intent.putExtra("appointment_date", finalDate);
                            intent.putExtra("start_time", finalTime);
                            intent.putExtra("home_collection", isHomeChecked);
                            intent.putExtra("user_id", common.get_user_id());
                            intent.putExtra("test", finalTest);
                            intent.putExtra("test_names", testName);

                            intent.putExtra("tax", tax_amt);

                            if (getIntent().getStringExtra("flag").equals("0")){

                                Log.e("flag","0");

                                intent.putExtra("total_price",getIntent().getStringExtra("total"));
                                Log.e("***"+"total_price",total_amt);
                            }else  if (getIntent().getStringExtra("flag").equals("1")){

                                Log.e("flag","0");
                                intent.putExtra("total_price",getIntent().getStringExtra("total"));
                                Log.e("***"+"total_price",price);
                            }



                            intent.putExtra("final_total", final_amt);
                            intent.putExtra("appointment_id", "965");
                            Log.e("APP_ID_Intent", "SELT TEST");
                            intent.putExtra("status", "0");
                        }
                        startActivity(intent);

                    } else {

                        MyUtility.showAlertMessage(LabViewDetailActivity.this, "select time");

                    }


                } else {
                    MyUtility.showAlertMessage(LabViewDetailActivity.this, "select date");
                }


            }

        });

    }


    private void bookLab() {


        testIDs = getIntent().getStringExtra("test");
        Log.e("testIDs", testIDs);

        /*[{"Activity":"R","ISTestProfile":"T","app_id":"1324","isChecked":true,"price":"550","test":"Ziffy_Test_38","test_name":"Beta Human Chorionic Gonadotropin Hormone (BHCG)"}]*/

        if (testIDs.contains("Activity")) {
            Log.e("####", "TRUE");
             testList = new ArrayList<>();
            try {
                Log.e("#####", "TRY" + "LABVIEW DETAIL");
                arrayList=new ArrayList<>();
                JSONArray array = new JSONArray(testIDs);
                for (int i = 0; i < array.length(); i++) {
                    Log.e("#####", "TRY" + "LABVIEW DETAIL");

                    JSONObject jsonObject = array.getJSONObject(i);


                    LabViewDetailActivity.JTEST model = new LabViewDetailActivity.JTEST();
                    LabViewDetailActivity.TEST test = new LabViewDetailActivity.TEST();

                    if (jsonObject.getBoolean("isChecked")) {
                        model.setTest_code(jsonObject.getString("test_code"));
                        model.setISTestProfile(jsonObject.getString("ISTestProfile"));
                        test_name = jsonObject.getString("test_name");


                        Log.e("TESTNAME", test_name);

                        test.setName(test_name);
                        app_id = jsonObject.getString("app_id");
                        Log.e("APP ID", app_id);


                        testList.add(model);

                        arrayList.add(test);

/*
                        price = jsonObject.getString("price");
                        Log.e("PRICE", price);
                        test.setPrice(price);

                        arrayList.add(test);

                        Log.e("TEST_PRICE", jsonObject.getString("price"));

                        double test_price = Double.parseDouble(price);

                        total_price = total_price + test_price;

                        Log.e("####", "TOTAL_PRICE" + "  " + total_price);*/
                        //    text_total_price.setText("("+ConstValue.CURRENCY+total_price);



                    }


                }


              /*  total_amt = String.valueOf(total_price);

                tax_value = (total_price * 10) / 100;

                tax_amt = String.valueOf(tax_value);
                Log.e("TAX_VALUE", String.valueOf(tax_value));
                final_total = total_price + tax_value;

                final_amt = String.valueOf(final_total);
                Log.e("FINAL_TOTAL", "###" + final_amt);*/

                //text_total_price.setText(ConstValue.CURRENCY+ "( " + total_price + " )");
                // text_total_price.setText("("+ConstValue.CURRENCY+getIntent().getStringExtra("total")+")");

                finalTest = new Gson().toJson(testList).toString();
                testName = new Gson().toJson(arrayList).toString();
                Log.e("test_ids", "####" + finalTest);
                Log.e("test_name", "####" + test_name);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {

            Log.e("ELSE","TRUE");
           testList = new ArrayList<>();
           arrayList = new ArrayList<>();
            try {

                JSONArray array = new JSONArray(testIDs);
                for (int i = 0; i < array.length(); i++) {


                    JSONObject jsonObject = array.getJSONObject(i);


                    LabViewDetailActivity.JTEST model = new LabViewDetailActivity.JTEST();
                    LabViewDetailActivity.TEST test = new LabViewDetailActivity.TEST();
                    model.setTest_code(jsonObject.getString("test_code"));
                    model.setISTestProfile(jsonObject.getString("ISTestProfile"));
                    model.setLab_test_id(jsonObject.getString("lab_test_id"));



                    if (jsonObject.getString("ISTestProfile").equals("P")) {
                        test_name = jsonObject.getString("test_names");
                        Log.e("TESTNAME", test_name);
                        test.setName(test_name);
                        test.setPackage_name(jsonObject.getString("names"));

                        Log.e("TEST_ID", jsonObject.getString("test_code"));


                        price = jsonObject.getString("price");
                        Log.e("PRICE", price);
                        test.setPrice(price);


                        Log.e("TEST_PRICE", jsonObject.getString("price"));

                        double test_price = Double.parseDouble(price);


                        Log.e("####", "TOTAL_PRICE" + "  " + total_price);

                        text_total_price.setText(ConstValue.CURRENCY+String.valueOf(price));



                        lin_book_lab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!finalDate.equals("")){

                                    if (!finalTime.equals("")){



                                        Intent intent = new Intent(LabViewDetailActivity.this, BillingActivity.class);

                                        intent.putExtra("lab_id", lab_id);
                                        intent.putExtra("lab_name", lab_name);
                                        intent.putExtra("appointment_date", finalDate);
                                        intent.putExtra("start_time", finalTime);
                                        intent.putExtra("home_collection", isHomeChecked);
                                        intent.putExtra("user_id", common.get_user_id());
                                        intent.putExtra("test", finalTest);
                                        intent.putExtra("test_names", testName);

                                        intent.putExtra("tax", tax_amt);
                                        intent.putExtra("total_price",ConstValue.CURRENCY+price);
                                        intent.putExtra("final_total", final_amt);
                                        intent.putExtra("appointment_id", "965");
                                        Log.e("APP_ID_Intent", "SELT TEST");
                                        intent.putExtra("status", "0");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);



                                    }else {

                                        MyUtility.showAlertMessage(LabViewDetailActivity.this,"select time");
                                    }


                                }else {

                                    MyUtility.showAlertMessage(LabViewDetailActivity.this,"select date");
                                }
                            }

                        });







                    } else {
                        test_name = jsonObject.getString("names");
                        Log.e("TESTNAME", test_name);
                        test.setName(test_name);
                    }





                    Log.e("TEST_ID", jsonObject.getString("test_code"));

                    arrayList.add(test);

                    testList.add(model);


                    price  = jsonObject.getString("price");
                    Log.e("PRICE", price);
                    test.setPrice(price);



                    Log.e("TEST_PRICE", jsonObject.getString("price"));

                    double test_price = Double.parseDouble(price);

                    total_price = total_price + test_price;

                    Log.e("####", "TOTAL_PRICE" + "  " + total_price);



                }


                total_amt = String.valueOf(total_price);

                tax_value = (total_price * 10) / 100;

                tax_amt = String.valueOf(tax_value);
                Log.e("TAX_VALUE", String.valueOf(tax_value));
                final_total = total_price + tax_value;

                final_amt = String.valueOf(final_total);
                Log.e("FINAL_TOTAL", "###" + final_amt);

                //     text_total_price.setText(ConstValue.CURRENCY + "( " + total_price + " )");

                finalTest = new Gson().toJson(testList).toString();
                testName = new Gson().toJson(arrayList).toString();
                Log.e("test_ids", "####" + finalTest);
                Log.e("test_name", "####" + test_name);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    public void DateDialog() {


        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                LabViewDetailActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dpd.setMinDate(now);
        now.add(Calendar.MONTH, +1);
        dpd.setMaxDate(now);

        try {

            if (!days.equalsIgnoreCase("")) {

                Calendar sunday, mon, tue, wed, thu, fri, sat;

                List<Calendar> weekends = new ArrayList<>();

                int weeks = 6;

                for (int i = 0; i < (weeks * 7); i = i + 7) {


                    if (!days.contains("sun")) {
                        sunday = Calendar.getInstance();
                        sunday.add(Calendar.DATE, -10);
                        sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(sunday);
                    }

                    if (!days.contains("mon")) {
                        mon = Calendar.getInstance();
                        mon.add(Calendar.DATE, -10);
                        mon.add(Calendar.DAY_OF_YEAR, (Calendar.MONDAY - mon.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(mon);
                    }

                    if (!days.contains("tue")) {
                        tue = Calendar.getInstance();
                        tue.add(Calendar.DATE, -10);
                        tue.add(Calendar.DAY_OF_YEAR, (Calendar.TUESDAY - tue.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(tue);
                    }

                    if (!days.contains("wed")) {
                        wed = Calendar.getInstance();
                        wed.add(Calendar.DATE, -10);
                        wed.add(Calendar.DAY_OF_YEAR, (Calendar.WEDNESDAY - wed.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(wed);
                    }

                    if (!days.contains("thu")) {
                        thu = Calendar.getInstance();
                        thu.add(Calendar.DATE, -10);
                        thu.add(Calendar.DAY_OF_YEAR, (Calendar.THURSDAY - thu.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(thu);
                    }

                    if (!days.contains("fri")) {
                        fri = Calendar.getInstance();
                        fri.add(Calendar.DATE, -10);
                        fri.add(Calendar.DAY_OF_YEAR, (Calendar.FRIDAY - fri.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(fri);
                    }

                    if (!days.contains("sat")) {
                        sat = Calendar.getInstance();
                        sat.add(Calendar.DATE, -10);
                        sat.add(Calendar.DAY_OF_YEAR, (Calendar.SATURDAY - sat.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(sat);
                    }


                }

                Calendar[] disabledDays = weekends.toArray(new Calendar[weekends.size()]);
                dpd.setDisabledDays(disabledDays);


            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String currentdate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        buttonChooseDate.setText(currentdate);
        finalDate = currentdate;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        buttontimeChoose.setText(hourOfDay + ":" + minute);
        finalTime = hourOfDay + ":" + minute;
    }


    private class JTEST {
        private String test_code;
        private String ISTestProfile;
        private String lab_test_id;

        public String getTest_code() {
            return test_code;
        }

        public void setTest_code(String test_code) {
            this.test_code = test_code;
        }

        public String getISTestProfile() {
            return ISTestProfile;
        }

        public void setISTestProfile(String ISTestProfile) {
            this.ISTestProfile = ISTestProfile;
        }

        public String getLab_test_id() {
            return lab_test_id;
        }

        public void setLab_test_id(String lab_test_id) {
            this.lab_test_id = lab_test_id;
        }
    }


    private class TEST {

        String name;
        String price;
        String package_name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPackage_name() {
            return package_name;
        }

        public void setPackage_name(String package_name) {
            this.package_name = package_name;
        }
    }

}





