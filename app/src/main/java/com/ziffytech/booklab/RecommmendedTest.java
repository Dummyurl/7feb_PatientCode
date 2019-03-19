package com.ziffytech.booklab;

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

import com.google.gson.Gson;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.MasterWebView;
import com.ziffytech.activities.SelfLabSelectActivity;
import com.ziffytech.activities.TestAppointmentList;
import com.ziffytech.booklab.adapter.TestAdapter;
import com.ziffytech.booklab.apiinterface.APIInterface;
import com.ziffytech.booklab.apiinterface.ApiClient;
import com.ziffytech.booklab.apiinterface.models.TestApi;
import com.ziffytech.booklab.models.TestModel;
import com.ziffytech.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommmendedTest extends CommonActivity {

    ArrayList<TestModel> mList;
    TestAdapter mTestadater;
    RecyclerView recyclerView;
    String user_id, sub_user_id;
    JSONArray array;
    TextView titleforlabtest;
    ArrayList<TestModel> mListArr = new ArrayList<>();
    ArrayList<TestModel> selectedList;
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


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedList = new ArrayList<>();
                status=true;
                boolean isSelected = false;

                for (TestModel model : mListArr) {
                    if (model.isChecked()) {
                        isSelected = true;
                        Log.e("Test selected", model.getTest_name());
                        selectedList.add(model);
                        }
                }

                if (isSelected) {


                    Log.e("isSelected", String.valueOf(isSelected));
                    Log.e("R Selected:", new Gson().toJson(selectedList).toString());
                    Intent intent = new Intent(RecommmendedTest.this, SelfLabSelectActivity.class);
                    intent.putExtra("test", new Gson().toJson(selectedList).toString());
                    intent.putExtra("Activity", "R");
                    if (!common.getSession("location").equals("")){
                        intent.putExtra("Address","1");
                    }else {
                        intent.putExtra("Address","0");
                    }
                    Log.e("R test", new Gson().toJson(mListArr).toString());
                    startActivity(intent);

                } else {

                    MyUtility.showAlertMessage(RecommmendedTest.this, "Please select test");
                }
            }
        });

    }

    public void setUpViews() {

        //  user_id = App.getUserId();
        //    sub_user_id = App.getSubUserId();

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

        if (!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this, MyUtility.INTERNET_ERROR);
            return;
        }

        showPrgressBar();


        Log.d("msg", "data");

        APIInterface apiInterface = ApiClient.getClient().create(APIInterface.class);
        //   Call<TestApi> call3 = apiInterface.get_recommend_test("89","0");

        Log.e("USER_ID", common.get_user_id());

        Call<TestApi> call3 = apiInterface.get_recommend_test(common.get_user_id(), "0");

        call3.enqueue(new Callback<TestApi>() {
            @Override
            public void onResponse(Call<TestApi> call, Response<TestApi> response) {

                Log.e("DATA_TEST :", new Gson().toJson(response.body()));

                hideProgressBar();

                if (response.body() != null) {

                    TestApi model = response.body();
                    if (model.response == 1) {


                        if (model.data.size() > 0) {

                            Log.e("data","true");

                            for (int i = 0; i < model.data.size(); i++) {

                                TestModel model1 = new TestModel();
                                model1.setId(model.data.get(i).id);
                                model1.setUser_id(model.data.get(i).user_id);
                                model1.setTest(model.data.get(i).test);
                                model1.setApp_id(model.data.get(i).app_id);
                                model1.setPrice(model.data.get(i).price);
                                model1.setISTestProfile("T");
                                Log.e("APP_ID", model.data.get(i).app_id);
                                model1.setActivity("R");

                                mList.add(model1);

                            }

                            String test = mList.get(0).getTest();

                            Log.e("test",test);
                            try {
                                Log.e("test","true");


                                array = new JSONArray(test);

                                for (int i = 0; i < array.length(); i++) {

                                    Log.e("array","true");
                                    JSONObject object = array.getJSONObject(i);
                                    TestModel model1 = new TestModel();
                                    model1.setTest(object.getString("test_code"));
                                    model1.setApp_id(mList.get(0).getApp_id());
                                   //model1.setPrice(object.getString("test_price"));
                                    //Log.e("PRICE", object.getString("test_price"));
                                    model1.setISTestProfile("T");
                                    Log.e("APP_ID", "R####   " + mList.get(0).getApp_id());
                                    model1.setTest_name(object.getString("test_name"));
                                    model1.setChecked(false);
                                    model1.setActivity("R");
                                    mListArr.add(model1);
                                }


                            }catch (JSONException e) {
                                e.printStackTrace();
                            }



                            mTestadater = new TestAdapter(mListArr, RecommmendedTest.this);
                            recyclerView.setAdapter(mTestadater);

                            titleforlabtest = (TextView) findViewById(R.id.titleforlabtest);
                            titleforlabtest.setVisibility(View.VISIBLE);

                        } else {

                            notfound.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.GONE);
                        }

                    } else {
                        notfound.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);

                        // MyUtility.showAlertMessage(getApplicationContext(), MyUtility.FAILED_TO_GET_DATA);
                    }

                } else {
                    notfound.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    // MyUtility.showAlertMessage(getApplicationContext(), MyUtility.FAILED_TO_GET_DATA);
                }


            }

            @Override
            public void onFailure(Call<TestApi> call, Throwable t) {
                call.cancel();
                hideProgressBar();
                notfound.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                //  MyUtility.showAlertMessage(getApplicationContext(), MyUtility.INTERNET_ERROR);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_search:

                /*Intent i = new Intent(RecommmendedTest.this, MasterWebView.class);
                i.putExtra("title", "History");
                i.putExtra("link", ConstValue.BASE_URL + "pathology/get_user_appointment/" + common.get_user_id());
                startActivity(i);*/

                Intent i = new Intent(RecommmendedTest.this, TestAppointmentList.class);
                startActivity(i);



                return true;


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lab, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
