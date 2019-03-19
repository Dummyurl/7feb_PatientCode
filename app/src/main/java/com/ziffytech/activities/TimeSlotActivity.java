package com.ziffytech.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.fragments.TimeSlotFragment;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.models.TimeSlotModel;
import com.ziffytech.util.GPSTracker;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class TimeSlotActivity extends CommonActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    public static TimeSlotModel timeSlotModel;
    DoctorModel selected_business;
    TextView textSalonName, totalTime6;
    ImageView imageLogo, imgv_bookcall, imgv_chat, imgv_direction, imgv_video;
    TabLayout tabLayout;
    Calendar calender;
    SimpleDateFormat df, df2;
    String currentdate;
    Button buttonChooseDate;
    TextView txt_docname;
    TextView adress;
    TextView text_address;
    TextView lbl_degree;
    ViewPager viewPager;
    TextView lbl_speciality;
    TextView txt_rating;
    TextView tvAddress;
    //HashMap<String,String> timeslotdata;
    SampleFragmentPagerAdapter sampleFragmentPagerAdapter;
    ArrayList<DoctorModel> mDoctorArray;
    LinearLayout lin_tablayout;
    ImageView doctImage;
    RelativeLayout clinic_details, toprating;
    LinearLayout layout_social;
    SimpleRatingBar rating;
    TextView txtTotalTime;
    TextView txtTotalAmount;
    ImageView tvDirection;
    String docName;
    GPSTracker gps;
    private int c_day;
    private int c_month;
    private int c_year;
    private String days = "";

    public static List<Calendar> saturdaysundaycount(Date d1, Date d2) {

        List<Calendar> list = new ArrayList<>();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        int sundays = 0;
        int saturday = 0;

        while (!c1.after(c2)) {

            if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

                saturday++;

                list.add(c1);
            }

            if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                sundays++;

                list.add(c1);

            }

            c1.add(Calendar.DATE, 1);
        }

        //System.out.println("Saturday Count = "+saturday);
        //System.out.println("Sunday Count = "+sundays);
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);

        allowBack();
        setHeaderTitle("Book Appointment");

        mDoctorArray = new ArrayList<>();
        selected_business = ActiveModels.DOCTOR_MODEL;

        //imageLogo = (ImageView)findViewById(R.id.salonImage);
        textSalonName = (TextView) findViewById(R.id.textSalonName);
        lbl_degree = (TextView) findViewById(R.id.degree);
        lbl_speciality = (TextView) findViewById(R.id.specilaity);
        rating = (SimpleRatingBar) findViewById(R.id.rating);
        txt_rating = (TextView) findViewById(R.id.txt_rating);
        doctImage = (ImageView) findViewById(R.id.doctImage);
        //adress = (TextView) findViewById(R.id.adress);
        text_address = (TextView) findViewById(R.id.text_address);
        totalTime6 = (TextView) findViewById(R.id.totalTime6);
        txt_docname = (TextView) findViewById(R.id.txt_docname);
       // tvAddress = (TextView) findViewById(R.id.adress);
        tvDirection = (ImageView) findViewById(R.id.imgv_direction);
        lin_tablayout = (LinearLayout) findViewById(R.id.lin_tablayout);
        toprating = (RelativeLayout) findViewById(R.id.toprating);
        clinic_details = (RelativeLayout) findViewById(R.id.clinic_details);
        layout_social = (LinearLayout) findViewById(R.id.layout_social);

        imgv_bookcall = (ImageView) findViewById(R.id.imgv_bookcall);
        imgv_bookcall.setOnClickListener(this);
        imgv_chat = (ImageView) findViewById(R.id.imgv_chat);
        imgv_chat.setOnClickListener(this);
        imgv_direction = (ImageView) findViewById(R.id.imgv_direction);
        imgv_direction.setOnClickListener(this);
        imgv_video = (ImageView) findViewById(R.id.imgv_video);
        imgv_video.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        txtTotalAmount = (TextView) findViewById(R.id.totalAmount);
        txtTotalTime = (TextView) findViewById(R.id.totalTime);

        //textSalonName.setTypeface(common.getCustomFont());

        //Picasso.with(this).load(ConstValue.BASE_URL + "/uploads/profile/" + selected_business.getBus_logo()).into(imageLogo);


        calender = Calendar.getInstance(TimeZone.getDefault());
        c_day = calender.get(Calendar.DAY_OF_MONTH);
        c_month = calender.get(Calendar.MONTH) + 1;
        c_year = calender.get(Calendar.YEAR);
        //df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        df2 = new SimpleDateFormat("HH:mm aa", Locale.ENGLISH);
        currentdate = c_year + "-" + c_month + "-" + c_day;
        //df.format(calender.getTime());
        buttonChooseDate = (Button) findViewById(R.id.buttonChooseDate);
        //buttonChooseDate.setText(currentdate);
        buttonChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });


        gps = new GPSTracker(TimeSlotActivity.this);

        String lat = String.valueOf(gps.getLatitude());
        String longi = String.valueOf(gps.getLongitude());


        if (getIntent().hasExtra("Activity")) {

            if (getIntent().getStringExtra("Activity").equals("5")) {

                // showPrgressBar();
                HashMap<String, String> params = new HashMap<>();
                params.put("doct_id", getIntent().getStringExtra("doct_id"));
                params.put("city", common.getSession(ApiParams.CURRENT_CITY));
                params.put("lat", lat);
                params.put("long", longi);
                Log.e("PARAMS", params.toString());
                CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_DOCTORS_BY_NAME, params, this.createRequestSuccessListenerDoctor(), this.createRequestErrorListenerDoctor());
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(customRequestForString);

              /*  if (!currentdate.equals("")){

                    loadWorkingDays();
                }else {

                    MyUtility.showToast("Please select date",TimeSlotActivity.this);
                }
*/

            }
        } else {
            try {

                if (!selected_business.getRating().equalsIgnoreCase("") && !selected_business.getRating().equalsIgnoreCase("null") && selected_business.getRating() != null) {
                    rating.setRating(Float.parseFloat(selected_business.getRating()));
                    txt_rating.setText("" + selected_business.getRating());
                } else {
                    rating.setRating((float) 1.5);
                    txt_rating.setText("" + selected_business.getRating());
                }


            } catch (Exception e) {
                rating.setRating((float) 1.5);
                txt_rating.setText("5");
                e.printStackTrace();
            }


            textSalonName.setText(Html.fromHtml(selected_business.getBus_title()));
            lbl_speciality.setText("(" + selected_business.getDoct_multi_speciality() + ")");
            lbl_degree.setText(selected_business.getDoct_degree() + ", Exp. : " + selected_business.getDoct_experience() + " year");

            totalTime6.setText(" Rs. " + selected_business.getConsult_fee());


            Picasso.with(this).load(ConstValue.BASE_URL + "/uploads/profile/" + selected_business.getDoct_photo()).into(doctImage);
            txt_docname.setText(selected_business.getDoct_name());
            //  adress.setText(selected_business.getBus_google_street());
            text_address.setText(selected_business.getBus_google_street());
            Log.e("CLINIC_ADDRESS", selected_business.getBus_google_street());


            txtTotalTime.setText(selected_business.getStart_con_time()+" - "+selected_business.getEnd_con_time());


            //TextView days = (TextView)findViewById(R.id.days);
            //days.setText(selected_business.getWorking_day());
            // TextView time = (TextView)findViewById(R.id.time);
            //time.setText(selected_business.getStart_con_time());
            loadWorkingDays();


        }


       /* tvDirection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(TimeSlotActivity.this,MapActivity.class);
                i.putExtra("lat",selected_business.getBus_latitude());
                i.putExtra("lng",selected_business.getBus_longitude());
                i.putExtra("title",selected_business.getBus_title());
                startActivity(i);

            }
        });*/


        // loadDoctorData();


    }

    public void loadWorkingDays() {

        Log.e("APICalled", currentdate);

        // showPrgressBar();

        HashMap<String, String> params = new HashMap<>();
        if (getIntent().hasExtra("Activity")) {
            if (getIntent().getStringExtra("Activity").equals("5")) {
                params.put("doct_id", getIntent().getStringExtra("doct_id"));
            }
        } else {
            params.put("doct_id", selected_business.getDoct_id());

        }


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.DOCTOR_DAYS_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {
                        //  hideProgressBar();

                        Log.e("DOCTOR DAYS", responce);

                        // loadSlotTask();


                        try {
                            JSONObject jsonObject = new JSONObject(responce);

                            if (jsonObject.getInt("responce") == 1) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    days = obj.getString("working_days");
                                }


                            } else {

                                MyUtility.showToast("No time slot available.", TimeSlotActivity.this);
                                finish();
                                //onBackPressed();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void VError(String responce) {
                        // hideProgressBar();
                        //progressBar1.setVisibility(View.GONE);

                        // loadSlotTask();
                    }
                });

    }

    public void loadSlotTask() {
        //Log.e("APICalled", currentdate);
        //showPrgressBar();
        HashMap<String, String> params = new HashMap<>();


        if (getIntent().hasExtra("Activity")) {

            if (getIntent().getStringExtra("Activity").equals("5")) {
                txt_docname.setText(getIntent().getStringExtra("doc_name"));

                params.put("bus_id", getIntent().getStringExtra("bus_id"));
                params.put("doct_id", getIntent().getStringExtra("doct_id"));
                params.put("date", currentdate);
            }
        } else {
            txt_docname.setText(selected_business.getDoct_name());

            //params.put("bus_id",selected_business.getBus_id());


            params.put("bus_id", selected_business.getBus_id());
            params.put("doct_id", selected_business.getDoct_id());
            params.put("date", currentdate);

        }


        showPrgressBar();

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.TIME_SLOT_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {
                        //hideProgressBar();

                        try {

                            JSONObject jsonObject = new JSONObject(responce);
                            JSONObject data = jsonObject.getJSONObject("data");

                            //extract string from data and token from given array

                            Object json = new JSONTokener(data.toString()).nextValue();

                            if (json instanceof JSONObject) {
                                // Convert JASON into JAVA object

                                Gson gson = new Gson();

                                // Serialization //byte stream of code in java object

                                Type listType = new TypeToken<TimeSlotModel>() {
                                }.getType();

                                // Putting data into out POJO
                                //Please check timeSlotModel in @TimeSlotModel and @SlotsModel

                                timeSlotModel = gson.fromJson(data.toString(), listType);

                                sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
                                viewPager.setAdapter(sampleFragmentPagerAdapter);
                                tabLayout.setupWithViewPager(viewPager);
                                sampleFragmentPagerAdapter.notifyDataSetChanged();

                                hideProgressBar();

                            } else {
                                hideProgressBar();
                                Toast.makeText(getApplicationContext(), getString(R.string.not_timetable_set), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            e.printStackTrace();
                            MyUtility.showAlertMessage(TimeSlotActivity.this, "Time slot not available.");

                        }

                    }

                    @Override
                    public void VError(String responce) {
                        //hideProgressBar();
                        //progressBar1.setVisibility(View.GONE);
                    }
                });

    }

    public void DateDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance
                (
                        TimeSlotActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

        dpd.setMinDate(now);
        now.add(Calendar.MONTH, +12);
        dpd.setMaxDate(now);

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

        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        currentdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        c_year = year;
        c_day = dayOfMonth;
        c_month = monthOfYear + 1;
        buttonChooseDate.setText("Date : " + currentdate);
        lin_tablayout.setVisibility(View.VISIBLE);
        tabLayout.requestFocus();
        tabLayout.setFocusable(true);
        clinic_details.setVisibility(View.GONE);
        layout_social.setVisibility(View.GONE);
        rating.setVisibility(View.GONE);
        doctImage.setVisibility(View.GONE);
        toprating.setVisibility(View.GONE);
        loadSlotTask();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgv_bookcall) {


            if(selected_business.getIs_ziffydoc().equals("1")){

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", selected_business.getDoct_phone(), null));
                startActivity(intent);
            }else {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "7066532000", null));
                startActivity(intent);
            }

        }
        if (v.getId() == R.id.imgv_chat) {
          /*  Intent intent = new Intent(TimeSlotActivity.this, ChatActivity.class);
            startActivity(intent);*/
            MyUtility.showToast("Coming soon...", TimeSlotActivity.this);
        }
        if (v.getId() == R.id.imgv_direction) {
            String map = "http://maps.google.co.in/maps?q=" + selected_business.getBus_google_street();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
            startActivity(intent);

        }
        if (v.getId() == R.id.imgv_video) {
            MyUtility.showToast("Coming soon..", TimeSlotActivity.this);
        }

    }



    private Response.Listener<String> createRequestSuccessListenerDoctor() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   hideProgressBar();
                Log.e("DOCTOR_RESPONSE", response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {

                        JSONObject object = data.getJSONObject(i);

                        txt_docname.setText(object.getString("doct_name"));

                        docName = object.getString("doct_name");

                        textSalonName.setText(object.getString("bus_title"));
                        text_address.setText(object.getString("bus_title"));
                        lbl_speciality.setText(object.getString("doct_speciality"));
                        lbl_degree.setText(object.getString("doct_degree"));
                        txtTotalTime.setText(object.getString("start_con_time")+(object.getString("end_con_time")));
                        totalTime6.setText(object.getString("consult_fee"));
                        if (!object.getString("doct_photo").equals("")) {

                            Picasso.with(TimeSlotActivity.this).load(ConstValue.BASE_URL + "/uploads/profile/" + object.getString("doct_photo")).into(doctImage);

                        }

                        try {

                            if (!object.getString("rating").equals("") && !object.getString("rating").equalsIgnoreCase("null") && object.getString("rating") != null) {
                                rating.setRating(Float.parseFloat(object.getString("rating")));
                                txt_rating.setText("" + object.getString("rating"));
                            } else {
                                rating.setRating((float) 1.5);
                                txt_rating.setText("" + object.getString("rating"));
                            }


                        } catch (Exception e) {
                            rating.setRating((float) 1.5);
                            txt_rating.setText("5");
                            e.printStackTrace();
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
    }


    private Response.ErrorListener createRequestErrorListenerDoctor() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##", "##" + error.toString());
                // hideProgressBar();
            }
        };
    }

    public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[]{getString(R.string.tab_morning), getString(R.string.tab_afternoon), getString(R.string.tab_evening)};

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TimeSlotFragment();
            Bundle args = new Bundle();
            args.putString("date", currentdate);
            args.putInt("position", position);
            if (getIntent().hasExtra("Activity")) {
                if (getIntent().getStringExtra("Activity").equals("5")) {

                    args.putString("doct_id", getIntent().getStringExtra("doct_id"));
                    args.putString("doctor_name", docName);
                    args.putString("appointment_id", getIntent().getStringExtra("appointment_id"));
                    Log.e("appointment_id", getIntent().getStringExtra("appointment_id"));
                    Log.e("R_doct_id", getIntent().getStringExtra("doct_id"));
                    args.putString("appointment_details", getIntent().getStringExtra("appointment_details"));
                }
            } else {
                args.putString("doct_id", selected_business.getDoct_id());
                args.putString("doctor_name", selected_business.getDoct_name());
                args.putString("app_id", "");
                args.putString("appointment_details", "");
            }


            fragment.setArguments(args);
            Log.e("Timeslot Frag", currentdate + "//" + position);


            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return tabTitles[position];
        }
    }

}
