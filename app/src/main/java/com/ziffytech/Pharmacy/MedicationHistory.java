package com.ziffytech.Pharmacy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.CustomRequestForString;
import com.ziffytech.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MedicationHistory extends CommonActivity {
    RecyclerView recyclerview_med_history;
    TextView tvnotfound;
    ArrayList<MedicalHistoryModel> historyModelArrayList;
    LinearLayoutManager layoutManager;
    FloatingActionButton fab_call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_history);

        setHeaderTitle("Medication History");
        allowBack();
        setUpViews();



        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "7499913961", null));
                startActivity(intent);

            }
        });


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id",common.get_user_id());
        Log.e("PARAMS",params.toString());
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_MEDICAL_HISTORY, params, this.createRequestSuccessListenerMedicalHistory(), this.createRequestErrorListenerTestList());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);
        startProgressDialog(MedicationHistory.this);


    }


    private Response.Listener<String> createRequestSuccessListenerMedicalHistory() {
        return new Response.Listener<String>() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(String response) {

                Log.e("MEDICAL_HISTORY", response.toString());
                stopProgressDialog();


                historyModelArrayList=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.getInt("response")==1){

                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                        for (int i = 0; i <jsonArray.length() ; i++) {

                            JSONObject object=jsonArray.getJSONObject(i);


                            MedicalHistoryModel medicalHistoryModel=new MedicalHistoryModel();

                            medicalHistoryModel.setClinic_name(object.getString("clinic_name"));
                            Log.e("clinic_name", object.getString("clinic_name"));
                            medicalHistoryModel.setDoctor_name(object.getString("ref_doctor"));
                            Log.e("ref_doctor",object.getString("ref_doctor"));
                            medicalHistoryModel.setDate(object.getString("created"));
                            Log.e("created", object.getString("created"));
                            medicalHistoryModel.setPres_id(object.getString("id"));
                            Log.e("id",object.getString("id"));

                            historyModelArrayList.add(medicalHistoryModel);

                        }

                        MedicalHistoryAdapter medicalHistoryAdapter=new MedicalHistoryAdapter(MedicationHistory.this,historyModelArrayList);
                        recyclerview_med_history.setAdapter(medicalHistoryAdapter);

                    }else {

                        recyclerview_med_history.setVisibility(View.GONE);
                        tvnotfound.setVisibility(View.VISIBLE);
                        fab_call.setVisibility(View.GONE);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
        layoutManager = new LinearLayoutManager(MedicationHistory.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_med_history.setHasFixedSize(true);
        recyclerview_med_history.setLayoutManager(layoutManager);
        tvnotfound = findViewById(R.id.text_notfound);
        fab_call = findViewById(R.id.fab_call);

    }
}