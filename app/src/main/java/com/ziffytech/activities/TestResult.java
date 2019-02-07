package com.ziffytech.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.adapters.TestResultAdapter;
import com.ziffytech.models.TestResultModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TestResult extends CommonActivity {


    ArrayList<TestResultModel> testResultModelArrayList;
    RecyclerView recyclerviewTestResult;
    LinearLayoutManager layoutManager;
    TestResultAdapter testResultAdapter;
    TextView testname, date, givenby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        //setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setHeaderTitle("Test Result");
        allowBack();


        recyclerviewTestResult = findViewById(R.id.recyclerviewTestResult);
        layoutManager = new LinearLayoutManager(TestResult.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerviewTestResult.setHasFixedSize(true);
        recyclerviewTestResult.setLayoutManager(layoutManager);

        testname = findViewById(R.id.testname);
        date = findViewById(R.id.date);
        givenby = findViewById(R.id.givenby);


        testname.setText(getIntent().getStringExtra("testname"));
        date.setText(getIntent().getStringExtra("date"));
        givenby.setText(getIntent().getStringExtra("gievenby"));


        HashMap<String, String> params = new HashMap<String, String>();
        params.put(" ziffy_code", getIntent().getStringExtra("test_code"));
        params.put("token", getIntent().getStringExtra("token"));
        params.put("patient_id", getIntent().getStringExtra("patient_id"));
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_TEST_REPORTS, params, this.createRequestSuccessListenerTestReports(), this.createRequestErrorListenerTestReports());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);
        showPrgressBar();
    }


    private Response.Listener<String> createRequestSuccessListenerTestReports() {
        return new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {

                Log.e("TEST_RESULT_RESPONSE", response.toString());
                hideProgressBar();


                testResultModelArrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    //String result= String.valueOf(jsonObject.getInt("response"));

                    if (jsonObject.getInt("responce") == 1) {


                        JSONArray data = jsonObject.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {

                            TestResultModel testResultModel = new TestResultModel();

                            JSONObject object = data.getJSONObject(i);

                            if (object.has("token")) {
                                testResultModel.setToken(object.getString("token"));
                                Log.e("token", object.getString("token"));
                            } else {

                                testResultModel.setToken("-");
                                Log.e("token", testResultModel.getToken());

                            }


                            JSONObject observationDetails = object.getJSONObject("observationDetails");

                            if (observationDetails.has("Value_type")) {
                                testResultModel.setValue_type(observationDetails.getString("Value_type"));
                                Log.e("Value_type", observationDetails.getString("Value_type"));
                            } else {

                                testResultModel.setValue_type("-");
                                Log.e("Value_type", testResultModel.getValue_type());

                            }

                            if (observationDetails.has("Sequence_No")) {
                                testResultModel.setSequence_No(observationDetails.getString("Sequence_No"));
                                Log.e("Sequence_No", observationDetails.getString("Sequence_No"));
                            } else {

                                testResultModel.setToken("-");
                                Log.e("Sequence_No", testResultModel.getSequence_No());

                            }


                            if (observationDetails.has("Observation_value")) {
                                testResultModel.setObservation_value(observationDetails.getString("Observation_value"));
                                Log.e("Observation_value", observationDetails.getString("Observation_value"));
                            } else {

                                testResultModel.setObservation_value("-");
                                Log.e("Observation_value", testResultModel.getObservation_value());

                            }
                            if (observationDetails.has("Abnormal_flags")) {
                                testResultModel.setAbnormal_flags(observationDetails.getString("Abnormal_flags"));
                                Log.e("Abnormal_flags", observationDetails.getString("Abnormal_flags"));
                            } else {

                                testResultModel.setObservation_value("-");
                                Log.e("Abnormal_flags", testResultModel.getAbnormal_flags());

                            }
                            if (observationDetails.has("Observation_result_status")) {
                                testResultModel.setObservation_result_status(observationDetails.getString("Observation_result_status"));
                                Log.e("Observation_result_status", observationDetails.getString("Observation_result_status"));
                            } else {

                                testResultModel.setObservation_result_status("-");
                                Log.e("Observation_result_status", testResultModel.getObservation_result_status());

                            }


                            if (observationDetails.has("Effective_date_of_last_normal_observation")) {
                                testResultModel.setEffective_date_of_last_normal_observation(observationDetails.getString("Effective_date_of_last_normal_observation"));
                                Log.e("Effective_date_of_last_normal_observation", observationDetails.getString("Effective_date_of_last_normal_observation"));
                            } else {

                                testResultModel.setEffective_date_of_last_normal_observation("-");
                                Log.e("Effective_date_of_last_normal_observation", testResultModel.getEffective_date_of_last_normal_observation());

                            }








                            if (observationDetails.has("Result_unit_reference_range")) {
                                testResultModel.setResult_unit_reference_range(observationDetails.getString("Result_unit_reference_range"));
                                Log.e("Result_unit_reference_range", observationDetails.getString("Result_unit_reference_range"));

                            } else {

                                testResultModel.setResult_unit_reference_range("-");
                                Log.e("Result_unit_reference_range", testResultModel.getResult_unit_reference_range());

                            }


                            JSONObject Observation_identifier = observationDetails.getJSONObject("Observation_identifier");

                            if (Observation_identifier.has("Observation_Coding_System")) {
                                testResultModel.setValue_type(Observation_identifier.getString("Observation_Coding_System"));
                                Log.e("Observation_Coding_System", Observation_identifier.getString("Observation_Coding_System"));


                            } else {

                                testResultModel.setObservation_Coding_System("-");
                                Log.e("Observation_Coding_System", testResultModel.getObservation_Coding_System());

                            }


                            if (Observation_identifier.has("Observation_Text")) {
                                Log.e("Observation_Text", "true");
                                testResultModel.setObservation_Text(Observation_identifier.getString("Observation_Text"));
                                Log.e("Observation_Text", Observation_identifier.getString("Observation_Text"));

                            } else {

                                testResultModel.setObservation_Text("-");
                                Log.e("Observation_Text", testResultModel.getObservation_Text());

                            }

                            if (Observation_identifier.has("Observation_Test_id")) {
                                testResultModel.setObservation_Test_id(Observation_identifier.getString("Observation_Test_id"));
                                Log.e("Observation_Test_id", Observation_identifier.getString("Observation_Test_id"));
                            } else {

                                testResultModel.setObservation_Test_id("-");
                                Log.e("Observation_Test_id", testResultModel.getObservation_Test_id());

                            }
                            testResultModelArrayList.add(testResultModel);
                        }


                    }
                    testResultAdapter = new TestResultAdapter(TestResult.this, testResultModelArrayList, getIntent().getStringExtra("patient_id"));
                    recyclerviewTestResult.setAdapter(testResultAdapter);
                    Log.e("testResultModelArrayList", new Gson().toJson(testResultModelArrayList.toString()));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
    }

    private Response.ErrorListener createRequestErrorListenerTestReports() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##", "##" + error.toString());
                hideProgressBar();
            }
        };
    }
}
