package com.ziffytech.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.ziffytech.adapters.DoctorAdapter;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.Address;
import com.ziffytech.models.BusinessModel;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.util.GPSTracker;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BookActivity extends CommonActivity implements View.OnClickListener
{
    public static ArrayList<DoctorModel> mDoctorArray;
    BusinessModel selected_business;
    DoctorAdapter doctorAdapter;
    RecyclerView recyclerView;
    TextView  text_category;
    String cat_id;
    GPSTracker gps;
    FloatingActionButton txtv_filter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctors);
        allowBack();
        setHeaderTitle("Doctor List",common.getSession(ApiParams.CURRENT_CITY));

        gps = new GPSTracker(BookActivity.this);

        if (getIntent().hasExtra("cat_id")) {
            cat_id = getIntent().getStringExtra("cat_id");
            Log.e("CATEGORY_ID", cat_id);
        }

        txtv_filter = (FloatingActionButton) findViewById(R.id.txtv_filter);
        text_category = (TextView) findViewById(R.id.text_category);

        txtv_filter.setOnClickListener(this);

        if (getIntent().getStringExtra("Activity").equalsIgnoreCase("1"))
        {
            txtv_filter.setVisibility(View.GONE);
        }else
        {
            txtv_filter.setVisibility(View.VISIBLE);
        }

        selected_business = ActiveModels.BUSINESS_MODEL;
        mDoctorArray = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        doctorAdapter = new DoctorAdapter(this, mDoctorArray);
        recyclerView.setAdapter(doctorAdapter);

        if (getIntent().hasExtra("search"))
        {
            Log.e("INTENT", "SEARCH");
            Log.e("intent Data", getIntent().toUri(0));
            ActiveModels.RESCHEDULE_APP_ID = "";
            if (getIntent().getStringExtra("Activity").equals("2"))
                loadDataSearch();

        } else if (getIntent().hasExtra("cat_id"))
        {
            text_category.setVisibility(View.VISIBLE);
            text_category.setText(getIntent().getStringExtra("cat_name"));
            Log.e("INTENT", "CATEGORY");
            ActiveModels.RESCHEDULE_APP_ID = "";
            loadData();

        } else if (getIntent().hasExtra("name"))
        {
            Log.e("INTENT", "NAME");
            ActiveModels.RESCHEDULE_APP_ID = "";
            loadDataName();

        }

        if (getIntent().hasExtra("name"))
        {
            Log.e("INTENT", "NAME");
            ActiveModels.RESCHEDULE_APP_ID = "";
            loadDataName();

        }
        if (getIntent().hasExtra("search"))
        {
            Log.e("INTENT", "SEARCH");
            Log.e("intent Data", getIntent().toUri(0));
            ActiveModels.RESCHEDULE_APP_ID = "";
            loadDataSearch();


        } else if (getIntent().hasExtra("cat_id"))
        {
            Log.e("INTENT", "CAT_ID");

            ActiveModels.RESCHEDULE_APP_ID = "";
            loadData();

        } else if (getIntent().hasExtra("appointment_id"))
        {

            ActiveModels.RESCHEDULE_APP_ID = getIntent().getStringExtra("appointment_id");
            loadDataName();
        } else if (getIntent().hasExtra("search"))
        {
            if (getIntent().hasExtra("cat_id"))
            {
                loadDataSearch();

            }

        }


        /*FloatingActionButton btnFilter=(FloatingActionButton)findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(BookActivity.this,SearchActivity.class));
            }
        });*/


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.doctor_search_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        item.setTitle("Search ...");

        if (getIntent().getStringExtra("Activity").equalsIgnoreCase("1")) {
            item.setVisible(false);
        }

        SearchView sv = new SearchView(getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");

                filter(newText);

                return false;
            }
        });

        return true;
    }


    private void filter(String newText) {


        ArrayList<DoctorModel> filterdNames = new ArrayList<>();

        for (DoctorModel m : mDoctorArray) {
            if (m.getDoct_name().toLowerCase().toUpperCase().contains(newText.toLowerCase().toUpperCase())) {
                filterdNames.add(m);
            } else if (m.getDoct_speciality().toLowerCase().toUpperCase().contains(newText.toLowerCase().toLowerCase())) {
                filterdNames.add(m);
            }

           /* if (m.get().toLowerCase().contains(newText.toLowerCase())) {
                filterdNames.add(m);
            }*/

        }

        //calling a method of the adapter class and passing the filtered list
        doctorAdapter.filterList(filterdNames);
    }


    private void loadDataName() {


        HashMap<String, String> params = new HashMap<>();
        params.put("doct_id", getIntent().getStringExtra("id"));
        params.put("city", common.getSession(ApiParams.CURRENT_CITY));
        /*  String l1="18.518961";*/
        /*  String l2="73.943062";*/
        /*  params.put("lat", l1);*/
        /*  params.put("long",l2);*/
        double lat=gps.getLatitude();
        String latitude= String.valueOf(gps.getLatitude());
        double longi=gps.getLatitude();
        String longitude= String.valueOf(gps.getLongitude());
        params.put("lat", latitude);
        params.put("long",longitude);
        Log.e("PARAMS",params.toString());
        // Log.e("IDDD",selected_business.getBus_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_DOCTORS_BY_NAME, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {


                        try {
                            JSONObject jsonObject = new JSONObject(responce);

                            JSONArray data = jsonObject.getJSONArray("data");

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<DoctorModel>>() {
                            }.getType();
                            mDoctorArray.clear();
                            mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(data.toString(), listType));
                            doctorAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void VError(String responce) {
                        Log.e("ERROR", responce);
                    }
                });

    }


    public void loadData() {

        HashMap<String, String> params = new HashMap<>();
        params.put("cat_id", getIntent().getStringExtra("cat_id"));
        params.put("city", common.getSession(ApiParams.CURRENT_CITY));
        /*String l1="18.518961";*/
        /*String l2="73.943062";*/
        /*params.put("lat", l1);*/
        /*params.put("long",l2);*/

        double lat=gps.getLatitude();
        String latitude= String.valueOf(gps.getLatitude());
        double longi=gps.getLatitude();
        String longitude= String.valueOf(gps.getLongitude());
        params.put("lat", latitude);
        params.put("long",longitude);
        Log.e("CATEGORY_PARAMS", String.valueOf(params));

        // Log.e("IDDD",selected_business.getBus_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_DOCTORS, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {


                        Log.e("CATEGORY_DOCTORS", responce);

                        try {
                            JSONObject jsonObject = new JSONObject(responce);

                            if (jsonObject.getInt("response") == 1)
                            {


                                JSONArray data = jsonObject.getJSONArray("data");

                                if (data.length() > 0)
                                {

                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<DoctorModel>>()
                                    {
                                    }.getType();
                                    mDoctorArray.clear();
                                    mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(data.toString(), listType));
                                    doctorAdapter.notifyDataSetChanged();

                                } else
                                {
                                    //finish();
                                    MyUtility.showAlertMessage(BookActivity.this, "Data Not Available");
                                }
                            } else {
                                //finish();
                                MyUtility.showAlertMessage(BookActivity.this, "Data Not Available");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            MyUtility.showAlertMessage(BookActivity.this, "Data Not Available");
                        }
                    }
                    @Override
                    public void VError(String responce) { }
                });

    }


    private void loadDataSearch() {
        Log.e("LOAD_SEARCH", "TRUE");


        HashMap<String, String> params = new HashMap<>();
        params.put("distance", "20");
        params.put("fee", getIntent().getStringExtra("fee2"));
        params.put("rating", getIntent().getStringExtra("rating"));
        params.put("availability", getIntent().getStringExtra("availability"));
        params.put("gender", getIntent().getStringExtra("gender"));
        params.put("categories", getIntent().getStringExtra("cat_id"));
/*        String l1="18.518961";
        String l2="73.943062";
        params.put("lat", );
        params.put("long",l2);*/

        double lat=gps.getLatitude();
        String latitude= String.valueOf(gps.getLatitude());
        double longi=gps.getLatitude();
        String longitude= String.valueOf(gps.getLongitude());
        params.put("lat", latitude);
        params.put("long",longitude);

        params.put("sort_by",getIntent().getStringExtra("sort2"));

        Log.e("####", "PARAMS" + params);

        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_DOCTORS_BY_FILTER, params, this.createRequestSuccessListenerFilter(), this.createRequestErrorListenerFilter());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);



    }


    private Response.Listener<String> createRequestSuccessListenerFilter()
    {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                Log.e("FILTER_RESPONSE", response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray data = jsonObject.getJSONArray("data");

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<DoctorModel>>() {
                    }.getType();

                    if (mDoctorArray.isEmpty()) {
                        mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(data.toString(), listType));
                        doctorAdapter.notifyDataSetChanged();
                    } else {
                        mDoctorArray.clear();
                        mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(data.toString(), listType));
                        doctorAdapter.notifyDataSetChanged();

                    }

                    Log.e("FILTER_DOTOR_LIST", "" + mDoctorArray);


                } catch (JSONException e) {


                    e.printStackTrace();
                }


            }
        };
    }


    private Response.ErrorListener createRequestErrorListenerFilter() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("##", "##" + error.toString());
                //App.showAlert("Something Went Wrong, Please Try again",MultiTestSearchActivity.this);

            }
        };
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtv_filter) {
            finish();
            //  startActivity(new Intent(BookActivity.this,SearchActivity.class));
            Intent intent = new Intent(BookActivity.this, SearchActivity.class);
            intent.putExtra("cat_id", cat_id);
            startActivity(intent);

        }

    }
}