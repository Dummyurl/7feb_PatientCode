package com.ziffytech.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.booklab.RecommmendedTest;
import com.ziffytech.booklab.models.SelectedPackageModel;
import com.ziffytech.models.PackageModel;
import com.ziffytech.thyrocare.Allthyropackageslisting;
import com.ziffytech.thyrocare.ThyroPackageAdapter;
import com.ziffytech.thyrocare.TopfivepickModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ZiffyLabBooking extends CommonActivity implements View.OnClickListener {
    int[] imgpath_orignal = {R.drawable.package1, R.drawable.package2, R.drawable.package3, R.drawable.package4, R.drawable.package5,
            R.drawable.package6, R.drawable.package7};
    Button txtv_test_available;
    EditText editSearchlab;
    //ProgressDialog pd;
    ArrayList<String> al_lab_id, al_lab_category, al_lab_test_name;
    Adaptertestlist testlistadapter;
    ArrayList<PackageModel> packageModelArrayList;
    RecyclerView recyclerViewPackage;
    GridLayoutManager layoutManager;
    ArrayList<SelectedPackageModel> arrayPAckage = new ArrayList<>();

    ArrayList<TopfivepickModel> tyro5modelArrayList;
    RecyclerView recyclerViewPackage2;
    LinearLayoutManager layoutManager2;
    TextView seeall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziffy_lab_booking);

        txtv_test_available = (Button) findViewById(R.id.txtv_test_available);
        txtv_test_available.setOnClickListener(this);

        seeall = (TextView)findViewById(R.id.seeall);
        seeall.setOnClickListener(this);

        recyclerViewPackage = (RecyclerView) findViewById(R.id.recyclerview_package);
        layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPackage.setHasFixedSize(true);
        recyclerViewPackage.setLayoutManager(layoutManager);


        HashMap<String, String> params1 = new HashMap<String, String>();
        CustomRequestForString customRequestForString1 = new CustomRequestForString(Request.Method.POST, ApiParams.GET_PACKAGES, params1, this.createRequestSuccessListenerPackage(), this.createRequestErrorListenerPackage());
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(customRequestForString1);
        showPrgressBar();


        recyclerViewPackage2 = (RecyclerView) findViewById(R.id.recyclerview_package_thyro);

        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPackage2.setHasFixedSize(true);
        recyclerViewPackage2.setLayoutManager(layoutManager2);

     /*   HashMap<String, String> params2 = new HashMap<String, String>();
        CustomRequestForString customRequestForString2 = new CustomRequestForString(Request.Method.POST, ApiParams.GET_THYRO_5_PACKAGES, params2, this.createRequestSuccessListenerThyropack(), this.createRequestErrorListenerThyropack());
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(customRequestForString2);
*/

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            String j = (String) b.get("status");
            txtv_test_available.setVisibility(View.VISIBLE);
        }

        // setrecomonded(imgpath_orignal);
        setHeaderTitle("Book Lab");
        allowBack();
        //simpleGrid = (GridView) findViewById(R.id.homegrid);
        editSearchlab = (EditText) findViewById(R.id.editSearchlab);
        editSearchlab.setOnClickListener(this);
        al_lab_id = new ArrayList<String>();
        al_lab_category = new ArrayList<String>();
        al_lab_test_name = new ArrayList<String>();
        //getAlllabtest();
        // Instance of ImageAdapter Class

    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtv_test_available) {
            Intent i = new Intent(ZiffyLabBooking.this, RecommmendedTest.class);
            startActivity(i);
        }
        if (v.getId() == R.id.editSearchlab) {
            Intent i = new Intent(ZiffyLabBooking.this, SelfTestserach.class);
            startActivity(i);
        }

        if(v.getId() == R.id.seeall)
        {
            Intent i = new Intent(ZiffyLabBooking.this, Allthyropackageslisting.class);
            startActivity(i);
        }

    }


    private Response.Listener<String> createRequestSuccessListenerPackage()
    {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("PACKAGE_RESPONSE", response.toString());
                hideProgressBar();
                packageModelArrayList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("responce");

                    if (result.equalsIgnoreCase("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            PackageModel packageModel = new PackageModel();

                            packageModel.setProf_id(object.getString("profile_id"));
                            Log.e("profile_id", object.getString("profile_id"));

                            packageModel.setProf_name(object.getString("profile_name"));
                            Log.e("profile_name", object.getString("profile_name"));

                            packageModel.setTest_code(object.getString("test_code"));
                            Log.e("test_code", object.getString("test_code"));

                            packageModel.setTest_price(object.getString("ziffy_profile_price"));
                            Log.e("ziffy_profile_price", object.getString("ziffy_profile_price"));

                            packageModel.setTest_name(object.getString("test_name"));
                            Log.e("test_name", object.getString("test_name"));

                            packageModelArrayList.add(packageModel);
                        }

                        PackageAdapter packageAdapter = new PackageAdapter(ZiffyLabBooking.this, packageModelArrayList);
                        recyclerViewPackage.setAdapter(packageAdapter);


                        packageAdapter.SetOnItemClickListener(new PackageAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                arrayPAckage.clear();

                                SelectedPackageModel selectedModel = new SelectedPackageModel();

                                PackageModel packageModel = packageModelArrayList.get(position);

                                Log.e("MODEL_PACKAGE", String.valueOf(packageModel));
                                selectedModel.setTest_code(packageModel.getTest_code());
                                Log.e("SELCTED_PCKG_ID", packageModel.getTest_code());
                                selectedModel.setPrice(packageModel.getTest_price());
                                Log.e("SELCTED_PCKG_PRICE", packageModel.getTest_price());
                                selectedModel.setNames(packageModel.getProf_name());
                                Log.e("SELCTED_PCKG_NAME", packageModel.getProf_name());
                                selectedModel.setTest_names(packageModel.getTest_name());
                                Log.e("SELCTED_PCKG_NAME", packageModel.getTest_name());
                                selectedModel.setLab_test_id(packageModel.getProf_id());
                                Log.e("SELECTEDLAB_TEST_ID", packageModel.getProf_id());
                                selectedModel.setISTestProfile("P");

                                arrayPAckage.add(selectedModel);

                                Log.e("SELECTED PACKAGE", "" + arrayPAckage);

                                Intent intent = new Intent(ZiffyLabBooking.this, PackageLabSelectActivity.class);
                                intent.putExtra("test", new Gson().toJson(arrayPAckage).toString());
                                intent.putExtra("package", "P");
                                if (!common.getSession("location").equals("")) {
                                    intent.putExtra("Address", "1");
                                } else {
                                    intent.putExtra("Address", "0");
                                }
                                Log.e("PCKG", new Gson().toJson(arrayPAckage).toString());
                                startActivity(intent);

                            }
                        });

                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };
    }

    private Response.ErrorListener createRequestErrorListenerPackage()
    {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##", "##" + error.toString());
                hideProgressBar();
                //App.showAlert("Something Went Wrong, Please Try again",MultiTestSearchActivity.this);
            }
        };
    }


    private Response.Listener<String> createRequestSuccessListenerThyropack()
    {
        return new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                Log.e("PACKAGE_RESPONSE_THYRO", response.toString());
                tyro5modelArrayList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("status");


                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {

                        JSONObject object = jsonArray.getJSONObject(i);

                        TopfivepickModel topfivemodel = new TopfivepickModel();

                        topfivemodel.setThyro_profile_id(object.getString("thyro_profile_id"));
                        Log.e("profile_id", object.getString("thyro_profile_id"));

                        topfivemodel.setTest_code(object.getString("test_code"));
                        Log.e("Test_code", object.getString("test_code"));

                        topfivemodel.setName(object.getString("name"));
                        Log.e("Name", object.getString("name"));

                        topfivemodel.setActual_amount(object.getString("actual_amount"));
                        Log.e("Actual_amount", object.getString("actual_amount"));

                        topfivemodel.setTest_count(object.getInt("test_count"));
                        Log.e("Test_count", String.valueOf(object.getInt("test_count")));

                        topfivemodel.setZiffy_profile_price(object.getString("ziffy_profile_price"));
                        Log.e("Ziffy_profile_price", object.getString("ziffy_profile_price"));

                        tyro5modelArrayList.add(topfivemodel);
                        Log.e("Arraylist", String.valueOf(tyro5modelArrayList));
                    }

                    ThyroPackageAdapter packageAdapter = new ThyroPackageAdapter(ZiffyLabBooking.this, tyro5modelArrayList);
                    recyclerViewPackage2.setAdapter(packageAdapter);


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };
    }

    private Response.ErrorListener createRequestErrorListenerThyropack()
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


}
