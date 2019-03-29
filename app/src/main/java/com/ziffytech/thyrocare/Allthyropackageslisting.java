package com.ziffytech.thyrocare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.activities.Adaptertestlist;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.CustomRequestForString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ziffytech.util.Preferences.MyPREFERENCES;

public class  Allthyropackageslisting extends CommonActivity implements View.OnClickListener
{
    Button txtv_test_available;
    EditText editSearchpak;
    //ProgressDialog pd;
    ArrayList<String> al_lab_id, al_lab_category, al_lab_test_name;
    Adaptertestlist testlistadapter;
    ArrayList<TopfivepickModel> tyro5modelArrayList;
    ArrayList<CartDetailModel> cartmodelArraylist;
    RecyclerView recyclerViewPackage,recyclerViewPackage2;
    LinearLayoutManager layoutManager,layoutManager2;
    ArrayList<TopfivepickModel> arrayPAckage = new ArrayList<>();
    ArrayList<TopfivepickModel> filterdNames;
    PackageAdapterThyroAll packageAdapter2;
    PackageAdapterThyroAll packageAdapter;
    TextView cartcount,txtv_schedule;
    CartDetailModel model;

    String key = "Key";
    ArrayList<CartDetailModel> ModelArrayList=new ArrayList();
    SharedPreferences shref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allpackageslisting);

        setHeaderTitle("All Packages");
        allowBack();

        shref = Allthyropackageslisting.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //editor = shref.edit();

        common.GetCartDetails(Allthyropackageslisting.this);

        //model = new CartDetailModel();
        txtv_test_available = (Button) findViewById(R.id.txtv_test_available);
        recyclerViewPackage = (RecyclerView) findViewById(R.id.recyclerview_package);
        txtv_schedule = (TextView)findViewById(R.id.txtv_schedule);
        txtv_schedule.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPackage.setHasFixedSize(true);
        recyclerViewPackage.setLayoutManager(layoutManager);
        HashMap<String, String> params1 = new HashMap<String, String>();
        CustomRequestForString customRequestForString1 = new CustomRequestForString(Request.Method.POST, ApiParams.GETALLTHYROPACK, params1, this.createRequestSuccessListenerPackage(), this.createRequestErrorListenerPackage());
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(customRequestForString1);
        showPrgressBar();

       /* HashMap<String, String> params3 = new HashMap<String, String>();
        String userid =  common.getSession("user_id");
        params3.put("user_id",userid);
        CustomRequestForString customRequestForString3 = new CustomRequestForString(Request.Method.POST, ApiParams.CARTDETAIL, params3, this.createRequestSuccessListenerCartDetail(), this.createRequestErrorListenerCartDetail());
        RequestQueue requestQueue3 = Volley.newRequestQueue(this);
        requestQueue3.add(customRequestForString3);*/

        al_lab_id = new ArrayList<String>();
        al_lab_category = new ArrayList<String>();
        al_lab_test_name = new ArrayList<String>();

        editSearchpak = (EditText) findViewById(R.id.editSearchpak);
        editSearchpak.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                filter(editable.toString());
            }
        });


        Gson gson = new Gson();
        String response=shref.getString(key , "");

        if(response != null)
        {
            ArrayList<CartDetailModel> lstArrayList = gson.fromJson(response, new TypeToken<List<CartDetailModel>>(){}.getType());
            if(lstArrayList != null && !lstArrayList.isEmpty())
            {
                CartDetailModel Model=lstArrayList.get(0);
                cartcount = (TextView)findViewById(R.id.cartcount);
                cartcount.setText(""+Model.getElementsincart().toString());
            }
            else
            {
                    cartcount = (TextView)findViewById(R.id.cartcount);
                    cartcount.setText("0");
            }
        }
        else
        {
            Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();
        }

    }

    private Response.Listener<String> createRequestSuccessListenerPackage()
    {
        return new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                Log.e("PACKAGE_RESPONSE_THYRO", response.toString());
                hideProgressBar();
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

                    packageAdapter = new PackageAdapterThyroAll(Allthyropackageslisting.this, tyro5modelArrayList);
                    recyclerViewPackage.setAdapter(packageAdapter);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

    }


    private Response.ErrorListener createRequestErrorListenerPackage()
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

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.txtv_schedule)
        {

            Intent i = new Intent(Allthyropackageslisting.this,Timeslotforthyro.class);
            startActivity(i);

        }

    }

    /********************************** Cart Detail *******************************************************************


    private Response.Listener<String> createRequestSuccessListenerCartDetail()
    {
        return new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                Log.e("Billing_Detail_THYRO", response.toString());

                cartmodelArraylist = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int result = jsonObject.getInt("status");

                    JSONArray jsonArray = jsonObject.getJSONArray("card_total");
                    JSONArray jsonArray2 = jsonObject.getJSONArray("info");
                    int cartelementcart = jsonArray2.length();

                    model.setElementsincart(cartelementcart);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {

                        JSONObject object = jsonArray.getJSONObject(i);
                        model.setTotal(object.getInt("total"));
                        model.setSubtotal(object.getInt("subtotal"));
                        model.setDiscount(object.getInt("discount"));

                        cartmodelArraylist.add(model);

                    }

                    Gson gson = new Gson();
                    String json = gson.toJson(cartmodelArraylist);
                    editor.putString(key, json);
                    editor.commit();

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };
    }

    private Response.ErrorListener createRequestErrorListenerCartDetail()
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
    /***********************************************************************************************************************************/
    private void filter(String text)
    {

        filterdNames = new ArrayList<>();

        for (TopfivepickModel m : tyro5modelArrayList)
        {
            if (m.getName().toLowerCase().toUpperCase().contains(text.toLowerCase().toUpperCase()))
            {
                filterdNames.add(m);
                Log.e("FILTERED LIST", "" + filterdNames);
            }else
            {
                //  Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
            }
        }
        //calling a method of the adapter class and passing the filtered list
        packageAdapter.filterList(filterdNames);
    }


}

