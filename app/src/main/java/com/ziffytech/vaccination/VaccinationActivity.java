package com.ziffytech.vaccination;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.CustomRequestForString;
import com.ziffytech.vaccination.models.AgeVaccinationData;
import com.ziffytech.vaccination.models.VaccinationData;
import com.ziffytech.vaccination.models.VaccinationRequestResponseBaseMode;
import com.ziffytech.vaccination.models.VaccineDataDB;

import java.util.ArrayList;
import java.util.HashMap;

public class VaccinationActivity extends CommonActivity {


    DBHelper dbHelper;
    ExpandableListView expandableListView;
    TextView v_userDob, v_username;
    ArrayList<String> arrayListAges;

    HashMap<String, ArrayList<AgeVaccinationData>> hashMapVaccineData;

    private int lastExpandedPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination);
        setHeaderTitle("Vaccination");
        dbHelper = new DBHelper(VaccinationActivity.this);
        setUpViews();
        allowBack();

        if (dbHelper.numberOfRows()!=0){
            Log.e( "VACCINE DETAILSFromDB", "data is there in DB");
            VaccineDataDB vaccineDataDB =dbHelper.getVaccineDetails("1");
            String data = vaccineDataDB.getVaccineData();
            Log.e( "VACCINE DETAILSFromDB", data);
            VaccinationRequestResponseBaseMode vaccinationRequestResponseBaseMode = new Gson().fromJson(data, VaccinationRequestResponseBaseMode.class);
            Log.e("#####  " + "VACCINE username", vaccinationRequestResponseBaseMode.getData().getVcDetails().getMName());
            v_username.setText(vaccinationRequestResponseBaseMode.getData().getVcDetails().getMName());
            v_userDob.setText(vaccinationRequestResponseBaseMode.getData().getVcDetails().getBirthDate());
            arrayListAges = new ArrayList<>();
            hashMapVaccineData = new HashMap<>();
            for (int i = 0; i < vaccinationRequestResponseBaseMode.getData().getResult().size(); i++) {

                VaccinationData vaccinationData = vaccinationRequestResponseBaseMode.getData().getResult().get(i);
                Log.e("#####  " + "VACCINE Age name", vaccinationData.getAge());
                Log.e("#####  " + "VACCINE Age data", vaccinationData.getAgeVaccinationDataList().toString());

                arrayListAges.add(vaccinationData.getAge());
                hashMapVaccineData.put(vaccinationData.getAge(), vaccinationData.getAgeVaccinationDataList());

            }
            ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(VaccinationActivity.this, arrayListAges, hashMapVaccineData);
            expandableListView.setAdapter(expandableListAdapter);


        }else {

            Log.e( "VACCINE DETAILSFromDB", "data is not there in DB");

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("user_id", common.get_user_id());
            Log.e("params", params.toString());
            CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.VACCINATION_DETAILS, params, this.createRequestSuccessListenerVaccination(), this.createRequestErrorListenerVaccination());
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(customRequestForString);
            showPrgressBar();
        }


        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });


    }


    private com.android.volley.Response.Listener<String> createRequestSuccessListenerVaccination() {
        return new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgressBar();
                Log.e("#####  " + "VACCINE DETAILS", response);

                VaccinationRequestResponseBaseMode vaccinationRequestResponseBaseMode = new Gson().fromJson(response, VaccinationRequestResponseBaseMode.class);

                if (vaccinationRequestResponseBaseMode.getResponse() == 1) {

                    if (dbHelper.numberOfRows()!=0){
                        Log.e("#####  " + "VACCINE DETAILS","update");
                        dbHelper.updateVaccineData("1",response);
                    }else {
                        Log.e("#####  " + "VACCINE DETAILS","insert");

                        dbHelper.insertVaccineData(response);
                    }

                    Log.e("#####  " + "VACCINE username", vaccinationRequestResponseBaseMode.getData().getVcDetails().getMName());
                    v_username.setText(vaccinationRequestResponseBaseMode.getData().getVcDetails().getMName());
                    v_userDob.setText(vaccinationRequestResponseBaseMode.getData().getVcDetails().getBirthDate());
                    arrayListAges = new ArrayList<>();
                    hashMapVaccineData = new HashMap<>();
                    for (int i = 0; i < vaccinationRequestResponseBaseMode.getData().getResult().size(); i++) {

                        VaccinationData vaccinationData = vaccinationRequestResponseBaseMode.getData().getResult().get(i);
                        Log.e("#####  " + "VACCINE Age name", vaccinationData.getAge());
                        Log.e("#####  " + "VACCINE Age data", vaccinationData.getAgeVaccinationDataList().toString());

                        arrayListAges.add(vaccinationData.getAge());
                        hashMapVaccineData.put(vaccinationData.getAge(), vaccinationData.getAgeVaccinationDataList());

                    }
                    ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(VaccinationActivity.this, arrayListAges, hashMapVaccineData);
                    expandableListView.setAdapter(expandableListAdapter);


                }

            }
        };
    }


    private com.android.volley.Response.ErrorListener createRequestErrorListenerVaccination() {
        return new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##", "##" + error.toString());
                hideProgressBar();

            }
        };
    }


    private void setUpViews() {
        v_username = findViewById(R.id.v_username);
        v_userDob = findViewById(R.id.v_userDob);

        expandableListView = (ExpandableListView) findViewById(R.id.lvExp);

//      //  recyclerviewVaccination = findViewById(R.id.recyclerviewVaccination);
//        layoutManager = new LinearLayoutManager(VaccinationActivity.this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerviewVaccination.setHasFixedSize(true);
//        recyclerviewVaccination.setLayoutManager(layoutManager);


    }
}
