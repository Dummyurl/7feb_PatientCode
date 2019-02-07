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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziffy_lab_booking);

        txtv_test_available = (Button) findViewById(R.id.txtv_test_available);
        txtv_test_available.setOnClickListener(this);

        recyclerViewPackage = (RecyclerView) findViewById(R.id.recyclerview_package);
        layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPackage.setHasFixedSize(true);
        recyclerViewPackage.setLayoutManager(layoutManager);


        HashMap<String, String> params1 = new HashMap<String, String>();
        CustomRequestForString customRequestForString1 = new CustomRequestForString(Request.Method.POST, ApiParams.GET_PACKAGES, params1, this.createRequestSuccessListenerPackage(), this.createRequestErrorListenerPackage());
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(customRequestForString1);
        showPrgressBar();


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

    /*private void getAlllabtest()
    {
        al_lab_id.clear();
        al_lab_category.clear();
        al_lab_test_name.clear();

        String base = "https://www.ziffytech.com/admin/Lab_api/get_tests_all";

        AsyncHttpClient client =  new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.post(base, params, new AsyncHttpResponseHandler()
        {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(String items)
            {
                try
                {
                    JSONObject root = new JSONObject(items);
                    //String message = root.optString("message");
                    String Status = root.optString("responce");
                    JSONArray arr = root.getJSONArray("data");
                    for (int i = 0; i < arr.length(); i++)
                    {
                        JSONObject obj = (JSONObject) arr.get(i);

                        al_lab_id.add("" + obj.optString("id"));
                        al_lab_category.add("" + obj.optString("category"));
                        al_lab_test_name.add("" + obj.optString("test_name"));

                        Toast.makeText(ZiffyLabBooking.this, ""+al_lab_test_name, Toast.LENGTH_SHORT).show();

                        SetAdapter();

                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                    //Toast.makeText(Productpage.this, " "+e, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content)
            {
                Toast.makeText(ZiffyLabBooking.this, "Please wait , Server Currently Busy...", Toast.LENGTH_LONG).show();
                //progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void SetAdapter()
    {
        testlistadapter = new Adaptertestlist(ZiffyLabBooking.this,al_lab_id,al_lab_category,al_lab_test_name);
        simpleGrid.setAdapter(testlistadapter);
        editSearchlab.addTextChangedListener(this);
        //pd.dismiss();
    }*/

    public void setrecomonded(int[] imgpath_orignal) {
        LinearLayout rec = (LinearLayout) findViewById(R.id.hori_recom);
        //ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(200, 200);
        // for(int g=0;g<5;g++)
        for (int g = 0; g < imgpath_orignal.length; g++) {
            ImageView recimg = new ImageView(ZiffyLabBooking.this);
            recimg.setId(g + 1);
            recimg.setPadding(25, 10, 10, 10);
            //Toast.makeText(New_Detailactivity.this,"Hi............", Toast.LENGTH_SHORT).show();
            recimg.setImageResource(imgpath_orignal[g]);
            // recimg.setImageResource(R.drawable.carwalelogo);
            recimg.setLayoutParams(params);
            rec.addView(recimg);
        }

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

    }
    /*@Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        String search = editSearchlab.getText().toString();
        testlistadapter.getFilter().filter(search);
        testlistadapter.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }*/


    private Response.Listener<String> createRequestSuccessListenerPackage() {
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
    }


    private Response.ErrorListener createRequestErrorListenerPackage() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##", "##" + error.toString());
                hideProgressBar();
                //App.showAlert("Something Went Wrong, Please Try again",MultiTestSearchActivity.this);
            }
        };
    }
}
