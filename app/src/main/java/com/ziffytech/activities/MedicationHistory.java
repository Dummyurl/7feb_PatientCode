package com.ziffytech.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.models.MedicalHistoryModel;
import com.ziffytech.util.MyUtility;

import java.util.ArrayList;
import java.util.HashMap;

public class MedicationHistory extends CommonActivity {
    RecyclerView recyclerview_med_history;
    TextView tvnotfound;
    ArrayList<MedicalHistoryModel> historyModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_history);

        setHeaderTitle("Medication History");
        allowBack();
        setUpViews();


        HashMap<String, String> params = new HashMap<String, String>();
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_MEDICAL_HISTORY, params, this.createRequestSuccessListenerMedicalHistory(), this.createRequestErrorListenerTestList());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);
        startProgressDialog(MedicationHistory.this);


    }


    private Response.Listener<String> createRequestSuccessListenerMedicalHistory() {
        return new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {

                Log.e("MEDICAL_HISTORY", response.toString());

                stopProgressDialog();
            }
        };
    }


    private Response.ErrorListener createRequestErrorListenerTestList() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialog();

                if (error instanceof TimeoutError) {
                    MyUtility.showAlertMessage(MedicationHistory.this, "Server is busy.Please try again");
                }
                Log.i("##", "##" + error.toString());
                hideProgressBar();
            }
        };
    }


    private void setUpViews() {

        recyclerview_med_history = findViewById(R.id.recyclerview_med_history);
        tvnotfound = findViewById(R.id.recyclerview_med_history);

    }
}