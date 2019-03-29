package com.ziffytech.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SelfTestserach extends CommonActivity implements View.OnClickListener {


    ArrayList<Model> testArraylist;
    ArrayList<TextSelectedModel> arrayText = new ArrayList<>();
    EditText edit_search;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    AdapterTestlistshowing adapterTestlistshowing;
    LinearLayout linearLayout;
    String test_name;
    Button btn_proceed;
    TextView txt_clear;
    ArrayList<Model> filterdNames;
    ArrayList<Model> testCodes;
    boolean isSearchTestClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_testsearch);
        setHeaderTitle("Book Test");
        allowBack();

        init();
        HashMap<String, String> params = new HashMap<String, String>();
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_ALL_TESTS, params, this.createRequestSuccessListenerTestList(), this.createRequestErrorListenerTestList());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);
        showPrgressBar();


        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isSearchTestClicked = true;

                filter(editable.toString());
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isSearchTestClicked = false;
                if (!arrayText.isEmpty()) {

                }
                Intent intent = new Intent(SelfTestserach.this, SelfLabSelectActivity.class);
                intent.putExtra("test", new Gson().toJson(arrayText).toString());
                if (!common.getSession("location").equals("")) {
                    intent.putExtra("Address", "1");
                } else {
                    intent.putExtra("Address", "0");
                }


                Log.e("SELECTED_TEST", new Gson().toJson(arrayText).toString());

                startActivity(intent);
            }
        });


    }

    private void init() {

        edit_search = (EditText) findViewById(R.id.edit_search_test);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_test);
        linearLayout = (LinearLayout) findViewById(R.id.layout_selected_test);
        layoutManager = new LinearLayoutManager(SelfTestserach.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        txt_clear = (TextView) findViewById(R.id.txt_clear);
        txt_clear.setOnClickListener(this);
    }


    private void filter(String text) {


        filterdNames = new ArrayList<>();

        for (Model m : testArraylist) {
            if (m.getTest_name().toLowerCase().toUpperCase().contains(text.toLowerCase().toUpperCase())) {
                filterdNames.add(m);


                Log.e("FILTERED LIST", "" + filterdNames);

            }else{

                //  Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapterTestlistshowing.filterList(filterdNames);
    }


    private Response.Listener<String> createRequestSuccessListenerTestList() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("TEST_RESPONSE", response.toString());
                // App.stopProgressDialog();


                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");

                    testArraylist = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);

                        Model model = new Model();

                        model.setId(obj.getString("lab_test_id"));
                        Log.e("ID", obj.getString("lab_test_id"));

                        model.setCategory(obj.getString("category"));
                        Log.e("Category", obj.getString("category"));

                        model.setTest_name(obj.getString("test_name"));
                        Log.e("TestName", obj.getString("test_name"));

                        model.setTest_price(obj.getString("test_price"));
                        Log.e("TestPrice", obj.getString("test_price"));

                        model.setCode(obj.getString("test_code"));
                        Log.e("Code", obj.getString("test_code"));

                        model.setStatus(obj.getString("status"));
                        Log.e("Status", obj.getString("status"));

                        model.setCreated_date(obj.getString("created"));
                        Log.e("Created_on", obj.getString("created"));

                        model.setModified_date(obj.getString("modified"));
                        Log.e("Modified", obj.getString("modified"));

                        testArraylist.add(model);
                        Log.e("ARRAYLIST", "" + testArraylist);

                    }
                    adapterTestlistshowing = new AdapterTestlistshowing(SelfTestserach.this, testArraylist);
                    recyclerView.setAdapter(adapterTestlistshowing);

                    hideProgressBar();

                    adapterTestlistshowing.SetOnItemClickListener(new AdapterTestlistshowing.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            btn_proceed.setVisibility(View.VISIBLE);
                            txt_clear.setVisibility(View.VISIBLE);

                            Log.e("####", "ITEM CLICKED");

                            linearLayout.setVisibility(View.VISIBLE);

                            if (arrayText.size() < 10) {

                                TextSelectedModel ts = new TextSelectedModel();

                                if (isSearchTestClicked) {

                                    Model m = filterdNames.get(position);
                                    String price = m.getTest_price();
                                    String test_id = m.getCode();
                                    String test_name = m.getTest_name();
                                    String lab_test_id = m.getId();
                                    String ISTestProfile = "T";


                                    ts.setTest_code(test_id);
                                    ts.setPrice(price);
                                    ts.setISTestProfile(ISTestProfile);
                                    ts.setNames(test_name);
                                    ts.setLab_test_id(lab_test_id);


                                    if (!contains(arrayText, m.getCode())) {
                                        Log.e("Already contains :", "true");
                                        arrayText.add(ts);


                                        TextView textView = new TextView(SelfTestserach.this);

                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                                        params.setMargins(1, 5, 1, 5);
                                        textView.setLayoutParams(params);

                                        //textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                                        String testName = filterdNames.get(position).getTest_name();
                                        textView.setTextColor(Color.DKGRAY);
                                        textView.setBackgroundResource(R.color.white);
                                        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(55)});
                                        textView.setPadding(10, 10, 10, 10);
                                        textView.setText(testName);
                                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                                        linearLayout.addView(textView);

                                    } else {
                                        Toast.makeText(SelfTestserach.this, "Test already selected", Toast.LENGTH_SHORT).show();

                                    }



                                    //testArraylist.get(position).setIsClickable(false);


                                } else {

                                    if (testArraylist.get(position).isClickable()) {
                                        //Perform your logic
                                        Model m = testArraylist.get(position);
                                        String price = m.getTest_price();
                                        String test_id = m.getCode();
                                        String test_name = m.getTest_name();
                                        String ISTestProfile = "T";
                                        String lab_test_id = m.getId();

                                        //ts.setTextView(test_name);
                                        ts.setTest_code(test_id);
                                        ts.setPrice(price);
                                        ts.setISTestProfile(ISTestProfile);
                                        ts.setNames(test_name);
                                        ts.setLab_test_id(lab_test_id);

                                        if (!contains(arrayText, m.getCode())) {
                                            arrayText.add(ts);
                                            testArraylist.get(position).setIsClickable(false);
                                            TextView textView = new TextView(SelfTestserach.this);

                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                                            params.setMargins(1, 5, 1, 5);
                                            textView.setLayoutParams(params);

                                            //textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                                            String testName = testArraylist.get(position).getTest_name();
                                            textView.setTextColor(Color.DKGRAY);
                                            textView.setBackgroundResource(R.color.white);
                                            textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(55)});
                                            textView.setPadding(10, 10, 10, 10);
                                            textView.setText(testName);
                                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                                            linearLayout.addView(textView);

                                            testArraylist.get(position).setIsClickable(false);
                                        } else {
                                            Toast.makeText(SelfTestserach.this, "Test already selected", Toast.LENGTH_SHORT).show();
                                        }


                                    } else {

                                        Toast.makeText(SelfTestserach.this, "Test already selected", Toast.LENGTH_SHORT).show();

                                    }


                                }
                            } else {
                                Toast.makeText(SelfTestserach.this, "You can add only 10", Toast.LENGTH_SHORT).show();
                            }
                        }


                    });

                } catch (JSONException e) {

                }


            }

        };


    }


    private Response.ErrorListener createRequestErrorListenerTestList() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError){
                    MyUtility.showAlertMessage(SelfTestserach.this,"Server is busy.Please try again");
                }
                // Log.i("##", "##" + error.toString());
                hideProgressBar();
            }
        };
    }


    boolean contains(ArrayList<TextSelectedModel> list, String name) {
        for (TextSelectedModel item : list) {
            if (item.getTest_code().equals(name)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_clear) {
            myUpdateOperation();
        }

    }

    private void myUpdateOperation() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_search:

                /*Intent i = new Intent(SelfTestserach.this, MasterWebView.class);
                i.putExtra("title", "Appointments");
                i.putExtra("link", ConstValue.BASE_URL + "pathology/get_user_appointment/" + common.get_user_id());
                startActivity(i);*/

                Intent i = new Intent(SelfTestserach.this, TestAppointmentList.class);
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
}






