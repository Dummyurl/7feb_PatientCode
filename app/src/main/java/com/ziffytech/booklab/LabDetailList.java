package com.ziffytech.booklab;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.ziffytech.booklab.adapter.LabDetailListAdapter;
import com.ziffytech.booklab.models.LabDetailListModel;
import com.ziffytech.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LabDetailList extends CommonActivity
{

     ArrayList<LabDetailListModel> mList;
     LabDetailListAdapter labdetaillistAdapter;
     RecyclerView recyclerView;

     TextView notfound;
     String ishome,lat,lng;
    String test;
    String finalTestApi,finalTestIntent,finalTestDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labdetail_list);
        allowBack();
        setHeaderTitle("Lab List");
        setUpViews();

    }

    public void setUpViews()
    {

         test=getIntent().getStringExtra("test");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_lab);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ishome=getIntent().getStringExtra("home");
        lat=getIntent().getStringExtra("lat");
        lng=getIntent().getStringExtra("lng");
        Log.e("LAT",lat);
        Log.e("LONG",lng);




        notfound=(TextView)findViewById(R.id.notfound);
        notfound.setVisibility(View.GONE);

        getdata();


    }

    private void getdata()
    {

        if (!MyUtility.isConnected(this))
        {
            MyUtility.showAlertMessage(this, MyUtility.INTERNET_ERROR);
            return;
        }

        List<JTEST> testList=new ArrayList<>();
        List<JTEST> testList2=new ArrayList<>();
        List<JTEST> testList3=new ArrayList<>();

        Log.e("intent Data", getIntent().toUri(0));



        try {
            JSONArray array= new JSONArray(test);

            for(int i=0;i<array.length();i++)
            {

                JSONObject jsonObject=array.getJSONObject(i);

                if(jsonObject.getBoolean("isChecked"))
                {
                    JTEST model=new JTEST();
                    JTEST model2=new JTEST();
                    JTEST model3=new JTEST();
                    model.setTest_code(jsonObject.getString("test"));
                    model.setApp_id(jsonObject.getString("app_id"));
                    model.setISTestProfile(jsonObject.getString("ISTestProfile"));
                    model.setPrice(jsonObject.getString("price"));
                    Log.e("ISTestProfile",jsonObject.getString("ISTestProfile"));

                    model2.setTest_code(jsonObject.getString("test"));
                    model2.setApp_id(jsonObject.getString("app_id"));


                    model3.setTest_name(jsonObject.getString("test_name"));
                    model3.setPrice(jsonObject.getString("price"));





                    testList.add(model);
                    testList2.add(model2);
                    testList3.add(model3);
                }
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        Log.e("FinalTest",new Gson().toJson(testList2).toString());
        Log.e("FinalTest2",new Gson().toJson(testList).toString());

         finalTestApi=new Gson().toJson(testList2).toString();
         finalTestIntent=new Gson().toJson(testList).toString();
         finalTestDetails=new Gson().toJson(testList3).toString();



        showPrgressBar();


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("home_collection",getIntent().getStringExtra("home"));
        params.put("lat",lat);
        params.put("lng",lng);
        params.put("test",finalTestApi);
        Log.e("PARAMSLAB",params.toString());
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_LABS, params, this.createRequestSuccessListenerLab(), this.createRequestErrorListenerLab());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);




/*

        APIInterface apiInterface = ApiClient.getClient().create(APIInterface.class);
        Call<LabDetailListApi> call3 = apiInterface.getlab(getIntent().getStringExtra("home"),
                getIntent().getStringExtra("lat"),
                getIntent().getStringExtra("lng"),finalTest);
        call3.enqueue(new Callback<LabDetailListApi>() {
            @Override
            public void onResponse(Call<LabDetailListApi> call, Response<LabDetailListApi> response) {

                Log.e("Data:", new Gson().toJson(response.body()));

                hideProgressBar();

                if (response.body() != null) {


                    LabDetailListApi model = response.body();

                    if (model.response == 1) {


                        if (model.data.size() > 0) {


                            for (int i = 0; i < model.data.size(); i++) {
                                LabDetailListModel model1=new LabDetailListModel();
                                model1.setLab_name(model.data.get(i).lab_name);
                                model1.setLab_address(model.data.get(i).lab_address);
                                model1.setLab_closing_time(model.data.get(i).lab_closing_time);
                                model1.setLab_opening_time(model.data.get(i).lab_opening_time);
                                model1.setLab_photo(model.data.get(i).lab_photo);
                                model1.setLab_id(model.data.get(i).lab_id);
                                mList.add(model1);


                            }
                            labdetaillistAdapter.notifyDataSetChanged();


                        }
                        else{

                            notfound.setVisibility(View.VISIBLE);

                        }

                    } else {
                        notfound.setVisibility(View.VISIBLE);

                        // MyUtility.showAlertMessage(getApplicationContext(), MyUtility.FAILED_TO_GET_DATA);
                    }

                } else {

                    notfound.setVisibility(View.VISIBLE);

                   // MyUtility.showAlertMessage(getApplicationContext(), MyUtility.FAILED_TO_GET_DATA);
                }


            }

            @Override
            public void onFailure(Call<LabDetailListApi> call, Throwable t) {
                call.cancel();
              //  MyUtility.showAlertMessage(getApplicationContext(), MyUtility.INTERNET_ERROR);
                hideProgressBar();

                notfound.setVisibility(View.VISIBLE);

            }
        });
*/

    }


    private com.android.volley.Response.Listener<String> createRequestSuccessListenerLab() {
        return new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("LAB_RESPONSE", response.toString());
                hideProgressBar();

               mList=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    String result=jsonObject.getString("response");


                    if (result.equalsIgnoreCase("1")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            LabDetailListModel model1 = new LabDetailListModel();

                            if (!object.getString("lab_ip").equals("")){

                            }
                            model1.setLab_name(object.getString("lab_name"));
                            model1.setLab_address(object.getString("lab_address"));
                            model1.setLab_closing_time(object.getString("lab_closing_time"));
                            model1.setLab_opening_time(object.getString("lab_opening_time"));
                            model1.setLab_photo(object.getString("lab_image"));
                            model1.setLab_id(object.getString("lab_id"));
                            mList.add(model1);
                            Log.e("LAB_LIST",mList.toString());


                        }

                            LabDetailListAdapter labDetailListAdapter=new LabDetailListAdapter(mList,LabDetailList.this,ishome,lat,lng,finalTestIntent,finalTestDetails);
                            recyclerView.setAdapter(labDetailListAdapter);


                    } else if (result.equalsIgnoreCase("0")){
                        hideProgressBar();
                        String msg=jsonObject.getString("message");

                        MyUtility.showAlertMessage(LabDetailList.this,msg);
                        notfound.setVisibility(View.VISIBLE);
                    }
                    // MyUtility.showAlertMessage(getApplicationContext(), MyUtility.FAILED_TO_GET_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private com.android.volley.Response.ErrorListener createRequestErrorListenerLab() {
        return new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##", "##" + error.toString());
                hideProgressBar();

                notfound.setVisibility(View.VISIBLE);
                }
        };
    }




    private class JTEST{

        public String getTest_code() {
            return test_code;
        }

        public void setTest_code(String test_code) {
            this.test_code = test_code;
        }

        private String test_code;

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getISTestProfile() {
            return ISTestProfile;
        }

        public void setISTestProfile(String ISTestProfile) {
            this.ISTestProfile = ISTestProfile;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTest_name() {
            return name;
        }

        public void setTest_name(String test_name) {
            this.name = test_name;
        }

        private String app_id;

        private String ISTestProfile;
        private String price;
        private String name;
    }


}