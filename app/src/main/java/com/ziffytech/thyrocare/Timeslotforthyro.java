package com.ziffytech.thyrocare;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
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
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.CustomRequestForString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static com.ziffytech.util.Preferences.MyPREFERENCES;

public class Timeslotforthyro extends CommonActivity implements View.OnClickListener {

    private HorizontalCalendar horizontalCalendar;
    TextView txtv_todays_date,txtv_gocart;
    String selectedDateStr;
    ArrayList<TimeslotModelThyro> timeslotArrayList;
    RecyclerView recyclerview_timeslot;
    GridLayoutManager layoutManager;
    String key = "Key";
    ArrayList<CartDetailModel> ModelArrayList=new ArrayList();
    TextView cartcount;
    SharedPreferences shref;
    SharedPreferences.Editor editor;
    RelativeLayout relative;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeslotforthyro);
        setHeaderTitle("Timeslot");
        allowBack();
        common.GetCartDetails(Timeslotforthyro.this);
        shref = Timeslotforthyro.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = shref.edit();
        relative = (RelativeLayout)findViewById(R.id.relative);
        txtv_gocart = (TextView)findViewById(R.id.txtv_gocart);
        txtv_gocart.setOnClickListener(this);
        recyclerview_timeslot = (RecyclerView) findViewById(R.id.recyclerview_timeslot);
        layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerview_timeslot.setHasFixedSize(true);
        recyclerview_timeslot.setLayoutManager(layoutManager);
        SetupUI();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                horizontalCalendar.goToday(false);

            }
        });

    }

    private void SetupUI()
    {

        Gson gson = new Gson();
        String response=shref.getString(key , "");

        if(response != null)
        {
            ArrayList<CartDetailModel> lstArrayList = gson.fromJson(response, new TypeToken<List<CartDetailModel>>(){}.getType());

            if(lstArrayList != null && !lstArrayList.isEmpty()) {
                CartDetailModel Model = lstArrayList.get(0);
                cartcount = (TextView) findViewById(R.id.cartcount);
                cartcount.setText("" + Model.getElementsincart().toString());
            }
            else {
                cartcount = (TextView) findViewById(R.id.cartcount);
                cartcount.setText("0");
            }
        }
        else
        {
            Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();
        }

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM d, yyyy");
        String dateToStr = format.format(today);

        txtv_todays_date = (TextView)findViewById(R.id.txtv_todays_date);
        txtv_todays_date.setText(""+dateToStr);

        selectedDateStr = DateFormat.format("yyyy-MM-dd", today).toString();
        GetTimeslot(selectedDateStr);

        /* start 2 months ago from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.WEEK_OF_MONTH, 0);

        /* end after 2 months from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.WEEK_OF_MONTH, 7);

        // Default Date set to Today.
        final Calendar defaultSelectedDate = Calendar.getInstance();

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(3)
                .configure()
                .formatTopText("EEE")
                .formatMiddleText("dd MMM")
                .formatBottomText("")
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.WHITE, Color.parseColor("#ffd54f"))
                .colorTextMiddle(Color.WHITE, Color.parseColor("#ffd54f"))
                .end()
                .defaultSelectedDate(defaultSelectedDate)
                .build();

        Log.i("Default Date", DateFormat.format("EEE, MMM d, yyyy", defaultSelectedDate).toString());

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener()
        {
            @Override
            public void onDateSelected(Calendar date, int position)
            {
                selectedDateStr = DateFormat.format("yyyy-MM-dd", date).toString();
                String format = DateFormat.format("EEEE, MMM d, yyyy", date).toString();
                txtv_todays_date.setText(""+format);
                Toast.makeText(Timeslotforthyro.this, selectedDateStr + " selected!", Toast.LENGTH_SHORT).show();
                Log.i("onDateSelected", selectedDateStr + " - Position = " + position);
                GetTimeslot(selectedDateStr);

            }

        });

    }

    private void GetTimeslot(String selectedDateStr2)
    {
        HashMap<String, String> params2 = new HashMap<String, String>();
        params2.put("date",selectedDateStr2);
        Snackbar.make(relative, "Selected Date : " + selectedDateStr2, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        CustomRequestForString customRequestForString2 = new CustomRequestForString(Request.Method.POST, ApiParams.GETTIMESLOT, params2, this.createRequestSuccessListenerThyrotimeslot(), this.createRequestErrorListenerThyrotimeslot());
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(customRequestForString2);
        showPrgressBar();
    }


    private Response.Listener<String> createRequestSuccessListenerThyrotimeslot()
    {
        return new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                Log.e("Time_Slot_Thyro", response.toString());
                hideProgressBar();
                timeslotArrayList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("status");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        TimeslotModelThyro timeslot = new TimeslotModelThyro();
                        timeslot.setSlot(object.getString("slot"));
                        timeslot.setIs_booked(object.getBoolean("is_booked"));
                        timeslotArrayList.add(timeslot);
                    }

                    ThrotimeslotAdapter packageAdapter = new ThrotimeslotAdapter(Timeslotforthyro.this,timeslotArrayList,selectedDateStr);
                    recyclerview_timeslot.setAdapter(packageAdapter);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };
    }


    private Response.ErrorListener createRequestErrorListenerThyrotimeslot()
    {
        return new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("##", "##" + error.toString());
                hideProgressBar();
                //App.showAlert("Something Went Wrong, Please Try again",MultiTestSearchActivity.this);
            }
        };
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.txtv_gocart)
        {
            Snackbar.make(v, "Please Select Your Preffered Time Slot..", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

}
