package com.ziffytech.thyrocare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.CustomRequestForString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ziffytech.util.Preferences.MyPREFERENCES;

public class PackageDetail extends CommonActivity implements View.OnClickListener
{

    TextView bookpack;
    ArrayList<PackageDetailModel> ThyropackDetailArrayList,ThyropackDetailArrayList2;
    TextView text_test_name,package_test_count,text_price,package_discount,txtv_precau,test_detail;
    PackageDetailModel detailmodel,detailmodel1;
    String discription,preparation,profileid;
    RecyclerView recyclerview_thyro_test;
    GridLayoutManager layoutManager;
    SharedPreferences shref;
    SharedPreferences.Editor editor;
    String key = "Key";
    TextView cartcount;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);
        setHeaderTitle("Package Detail");
        allowBack();

        common.GetCartDetails(PackageDetail.this);

        shref = PackageDetail.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        profileid = getIntent().getStringExtra("PROFILEID");

        ThyropackDetailArrayList = new ArrayList<>();
        ThyropackDetailArrayList2 = new ArrayList<>();

        bookpack = (TextView)findViewById(R.id.bookpack);
        bookpack.setOnClickListener(this);

        detailmodel = new PackageDetailModel();

        recyclerview_thyro_test = (RecyclerView) findViewById(R.id.recyclerview_thyro_test);

        layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerview_thyro_test.setHasFixedSize(true);
        recyclerview_thyro_test.setLayoutManager(layoutManager);

        //String pro_id = "2019";
        HashMap<String, String> params2 = new HashMap<String, String>();
        params2.put("thyro_profile_id",profileid);
        CustomRequestForString customRequestForString2 = new CustomRequestForString(Request.Method.POST, ApiParams.PACK_DETAIL, params2, this.createRequestSuccessListenerThyropackDetail(), this.createRequestErrorListenerThyropackDetail());
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(customRequestForString2);
        showPrgressBar();



        Gson gson = new Gson();
        String response=shref.getString(key , "");

        if(response != null)
        {
            ArrayList<CartDetailModel> lstArrayList = gson.fromJson(response, new TypeToken<List<CartDetailModel>>(){}.getType());

            if(lstArrayList != null && !lstArrayList.isEmpty()) {
                CartDetailModel Model = lstArrayList.get(0);
                cartcount = (TextView) findViewById(R.id.cartcount);
                cartcount.setText("" + Model.getElementsincart().toString());

            }
            else{

                cartcount = (TextView) findViewById(R.id.cartcount);
                cartcount.setText("0");
            }
        }
        else
        {
            Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.bookpack)
        {
            AddPack();
        }

    }

    private void AddPack()
    {
        //Intent i = new Intent(PackageDetail.this,Timeslotforthyro.class);
        //startActivity(i);
        HashMap<String, String> params2 = new HashMap<String, String>();
        params2.put("thyro_profile_id",detailmodel.getThyro_profile_id());
        params2.put("test_code",detailmodel.getTest_code_Detail());
        params2.put("user_id",common.getSession("user_id"));
        CustomRequestForString customRequestForString2 = new CustomRequestForString(Request.Method.POST, ApiParams.ADDTOCART, params2, this.createRequestSuccessListenerThyropackadd(), this.createRequestErrorListenerThyropackadd());
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(customRequestForString2);

    }


    /***************************************************************************************************************/

     private Response.Listener<String> createRequestSuccessListenerThyropackadd()
     {
     return new Response.Listener<String>()
     {
     @Override
     public void onResponse(String response)
     {

     Log.e("PACKAGE_RESPONSE_THYRO", response.toString());

     try {
     JSONObject jsonObject = new JSONObject(response);
     String result = jsonObject.getString("message");

     if(result.equals("success"))
     {
     Toast.makeText(PackageDetail.this, "Test Added To Cart", Toast.LENGTH_SHORT).show();
     UpdateActivity();
     }
     else
     {
     Toast.makeText(PackageDetail.this, "Please Try Again...", Toast.LENGTH_SHORT).show();
     }

     } catch (JSONException e)
     {
     e.printStackTrace();
     }

     }
     };
     }

     private void UpdateActivity()
     {
         Intent i = new Intent(PackageDetail.this,Timeslotforthyro.class);
         startActivity(i);
         finish();
     }

     private Response.ErrorListener createRequestErrorListenerThyropackadd()
     {
     return new Response.ErrorListener()
     {
     @Override
     public void onErrorResponse(VolleyError error)
     {
     Log.i("##", "##" + error.toString());
     }
     };
     }

     /****************************************************************************************/

    private Response.Listener<String> createRequestSuccessListenerThyropackDetail()
    {
        return new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                Log.e("PACKAGE_Detail_THYRO", response.toString());
                hideProgressBar();


                ThyropackDetailArrayList = new ArrayList<>();
                ThyropackDetailArrayList2 = new ArrayList<>();

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("status");
                    discription = jsonObject.getString("description");
                    preparation =  jsonObject.getString("preparation");

                    JSONArray jsonArray = jsonObject.getJSONArray("info");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {


                        JSONObject object = jsonArray.getJSONObject(i);
                        detailmodel.setThyro_profile_id(object.getString("thyro_profile_id"));
                        detailmodel.setTest_code_Detail(object.getString("test_code"));
                        detailmodel.setProfile_name(object.getString("profile_name"));
                        detailmodel.setZiffy_profile_price(object.getString("ziffy_profile_price"));
                        detailmodel.setTest_count(object.getString("test_count"));
                        ThyropackDetailArrayList.add(detailmodel);

                    }

                    JSONArray jsonArray1 = jsonObject.getJSONArray("test");

                    for (int i = 0; i < jsonArray1.length(); i++)
                    {
                        detailmodel1 = new PackageDetailModel();
                        JSONObject object2 = jsonArray1.getJSONObject(i);
                        detailmodel1.setTest_name_detail(object2.getString("test_name"));
                        ThyropackDetailArrayList2.add(detailmodel1);
                    }

                    SetUpView();
                    TestDetailadpater packageAdapter = new TestDetailadpater(PackageDetail.this, ThyropackDetailArrayList2);
                    recyclerview_thyro_test.setAdapter(packageAdapter);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };
    }

    private void SetUpView()
    {

        text_test_name = (TextView)findViewById(R.id.text_test_name);
        package_test_count = (TextView)findViewById(R.id.package_test_count);
        text_price = (TextView)findViewById(R.id.text_price);
        package_discount = (TextView)findViewById(R.id.package_discount);
        txtv_precau = (TextView)findViewById(R.id.txtv_precau);
        test_detail = (TextView)findViewById(R.id.test_detail);

       for (int i=0;i<=ThyropackDetailArrayList.size();i++)
       {
           text_test_name.setText(detailmodel.getProfile_name());
           package_test_count.setText("Includes "+detailmodel.getTest_count()+" test");
           text_price.setText(ConstValue.CURRENCY+" "+detailmodel.getZiffy_profile_price());
           if(preparation.equals("Null")&& discription.equals("Null"))
           {
               txtv_precau.setText("Details not available");
               test_detail.setText("Details not available");
           }
           else
           {
               txtv_precau.setText(preparation);
               test_detail.setText(discription);
           }

       }


    }

    private Response.ErrorListener createRequestErrorListenerThyropackDetail()
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
