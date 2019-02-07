package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;

import com.ziffytech.booklab.adapter.TestAdapter;

import com.ziffytech.models.MedicineOrderModel;
import com.ziffytech.util.MyUtility;

import org.json.JSONArray;


import java.util.ArrayList;
import java.util.HashMap;


public class OrderPrescribedMedicine extends CommonActivity {

    ArrayList<MedicineOrderModel> mList;

    RecyclerView recyclerView;
    String user_id, sub_user_id;

    ArrayList<MedicineOrderModel> mListArr;
    ArrayList<MedicineOrderModel> selectedList;
    TextView notfound;
    Button submit;
    boolean status=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommmended_test);
        setHeaderTitle("Recommended Test");
        allowBack();
        setUpViews();



    }

    public void setUpViews() {



        notfound = (TextView) findViewById(R.id.notfound);
        notfound.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_test);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mList = new ArrayList<>();
        submit = (Button) findViewById(R.id.submit);

       getdata();
    }

    private void getdata() {


        HashMap<String, String> params = new HashMap<String, String>();
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_PRESCRIBED_MEDICINE, params, this.createRequestSuccessListenerTestList(), this.createRequestErrorListenerTestList());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);
        showPrgressBar();

    }

    private Response.Listener<String> createRequestSuccessListenerTestList() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("TEST_RESPONSE", response.toString());
                // App.stopProgressDialog();
            }
        };
    }

    private Response.ErrorListener createRequestErrorListenerTestList() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError){
                    MyUtility.showAlertMessage(OrderPrescribedMedicine.this,"Server is busy.Please try again");
                }
                Log.i("##", "##" + error.toString());
                hideProgressBar();
            }
        };
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
