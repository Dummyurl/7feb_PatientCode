package com.ziffytech.thyrocare;

import android.content.Intent;
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
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.BillingActivity;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.CustomRequestForString;
import com.ziffytech.booklab.models.SelectedPackageModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ordersummarythyro extends CommonActivity implements View.OnClickListener {
    TextView bookedTime,txtv_patientName,txtv_mob,txtv_add,total_price,text_tax,txtv_total_discount,final_total,txtv_carttotal;
    CommonActivity comman;
    ArrayList<BillingDetailModel> billdetailArrayList1,billdetailArrayList2,billdetailArrayList3;
    RecyclerView recyclerview_thyro_carttest;
    GridLayoutManager  layoutManager;
    Gson gson;
    TextView txtv_checkout;
    ArrayList<SelectedPackageModel> arrayPAckage = new ArrayList<>();
    ArrayList<SelectedPackageModel>  arrayPAckageTesname = new ArrayList<>();
    String carttotal;

    //SharedPreferences pref = getApplicationContext().getSharedPreferences("MyList", 0); // 0 - for private mode
    //SharedPreferences.Editor editor = pref.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersummarythyro);

        setHeaderTitle("Order Summary");
        allowBack();

        txtv_checkout = (TextView)findViewById(R.id.txtv_checkout);
        txtv_checkout.setOnClickListener(this);

        SetupUI();

        recyclerview_thyro_carttest = (RecyclerView) findViewById(R.id.recyclerview_thyro_carttest);
        layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerview_thyro_carttest.setHasFixedSize(true);
        recyclerview_thyro_carttest.setLayoutManager(layoutManager);

        HashMap<String, String> params2 = new HashMap<String, String>();
        params2.put("user_id",common.getSession("user_id"));

        CustomRequestForString customRequestForString2 = new CustomRequestForString(Request.Method.POST, ApiParams.BILLINGDETAIL, params2, this.createRequestSuccessListenerThyropackBilldetail(), this.createRequestErrorListenerThyropackBilldetail());
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(customRequestForString2);
        showPrgressBar();

    }

    private void GetDetail()
    {
    }

    private void SetupUI()
    {

        Toast.makeText(this, ""+(common.getSession("user_id")), Toast.LENGTH_SHORT).show();

        txtv_patientName = (TextView)findViewById(R.id.txtv_patientName);
        txtv_mob = (TextView)findViewById(R.id.txtv_mob);
        txtv_add = (TextView)findViewById(R.id.txtv_add );
        total_price = (TextView)findViewById(R.id.total_price);
        text_tax = (TextView)findViewById(R.id.text_tax);
        txtv_total_discount = (TextView)findViewById(R.id.txtv_total_discount);
        final_total = (TextView)findViewById(R.id.final_total);
        txtv_carttotal = (TextView)findViewById(R.id.txtv_carttotal);
        bookedTime = (TextView)findViewById(R.id.bookedTime);
        bookedTime.setText(""+getIntent().getExtras().getString("date")+" , "+getIntent().getExtras().getString("timeslot"));

    }


    private Response.Listener<String> createRequestSuccessListenerThyropackBilldetail()
    {
        return new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                Log.e("PACKAGE_BILL_DETAIL", response.toString());
                hideProgressBar();
                billdetailArrayList1 = new ArrayList<>();
                billdetailArrayList2 = new ArrayList<>();
                billdetailArrayList3 = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("status");

                    JSONArray jsonArray = jsonObject.getJSONArray("info");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {

                        JSONObject object = jsonArray.getJSONObject(i);
                        BillingDetailModel model = new BillingDetailModel();
                        model.setUser_id(object.getString("user_id"));
                        model.setUser_name(object.getString("user_name"));
                        model.setUser_phone(object.getString("user_phone"));
                        model.setLocation(object.getString("location"));
                        billdetailArrayList1.add(model);
                        txtv_patientName.setText(object.getString("user_name"));
                        txtv_mob.setText(object.getString("user_phone"));
                        txtv_add.setText(object.getString("location"));

                    }

                    JSONArray jsonArray1 = jsonObject.getJSONArray("choosen_product");

                    for (int i = 0; i < jsonArray1.length(); i++)
                    {

                        JSONObject object1 = jsonArray1.getJSONObject(i);
                        BillingDetailModel model1 = new BillingDetailModel();
                        model1.setThyro_profile_id(object1.getString("thyro_profile_id"));
                        model1.setTest_code(object1.getString("test_code"));
                        model1.setZiffy_profile_price(object1.getString("ziffy_profile_price"));
                        model1.setName(object1.getString("profile_name"));
                        billdetailArrayList2.add(model1);

                    }



                    JSONArray jsonArray2 = jsonObject.getJSONArray("bill_details");

                    for (int i = 0; i < jsonArray2.length(); i++)
                    {
                        JSONObject object2 = jsonArray2.getJSONObject(i);
                        BillingDetailModel model2 = new BillingDetailModel();
                        model2.setActual_total(object2.getString("actual_total"));
                        model2.setCard_total(object2.getString("card_total"));
                        model2.setDiscount(object2.getString("discount"));
                        billdetailArrayList3.add(model2);

                        text_tax.setText(ConstValue.CURRENCY+". 0");
                        txtv_total_discount.setText(ConstValue.CURRENCY+". "+object2.getString("discount"));
                        final_total.setText(ConstValue.CURRENCY+". "+object2.getString("card_total"));
                        txtv_carttotal.setText(ConstValue.CURRENCY+". "+object2.getString("card_total"));
                        carttotal = object2.getString("card_total").toString();

                    }

                    CartTestListAdapter packageAdapter = new CartTestListAdapter(Ordersummarythyro.this, billdetailArrayList2);
                    recyclerview_thyro_carttest.setAdapter(packageAdapter);
                    recyclerview_thyro_carttest.invalidate();

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createRequestErrorListenerThyropackBilldetail()
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

        if(v.getId()==R.id.txtv_checkout)
        {

            GetData();

            String isHomeChecked = "1";
            String tax_amt = "0";

             Intent intent = new Intent(Ordersummarythyro.this, BillingActivity.class);
             intent.putExtra("lab_id","1001");
             intent.putExtra("lab_name","Thyrocare");
             intent.putExtra("appointment_date", getIntent().getExtras().getString("date"));
             intent.putExtra("start_time", getIntent().getExtras().getString("timeslot"));
             intent.putExtra("home_collection", isHomeChecked);
             intent.putExtra("user_id", common.get_user_id());
             intent.putExtra("test",new Gson().toJson(arrayPAckage).toString());
             intent.putExtra("test_names", new Gson().toJson(arrayPAckageTesname).toString());
             intent.putExtra("tax", tax_amt);
             intent.putExtra("total_price", carttotal);
             intent.putExtra("final_total", carttotal);
             intent.putExtra("appointment_id", "965");
             intent.putExtra("status", "3");
             Log.e("appointment_date",getIntent().getExtras().getString("date"));
             Log.e("start_time",getIntent().getExtras().getString("timeslot"));
             Log.e("test",new Gson().toJson(arrayPAckage).toString());
             Log.e("total_price",carttotal);
             Log.e("test_names",new Gson().toJson(arrayPAckageTesname).toString());
             startActivity(intent);

        }
    }

    private void GetData()
    {
        for (int i=0;i<billdetailArrayList2.size();i++)

        {
            SelectedPackageModel selectedModel = new SelectedPackageModel();
            BillingDetailModel packageModel = billdetailArrayList2.get(i);

            Log.e("MODEL_PACKAGE", String.valueOf(packageModel));
            Log.e("SELCTED_PCKG_ID", packageModel.getTest_code());
            Log.e("SELCTED_PCKG_PRICE", packageModel.getZiffy_profile_price());
            Log.e("SELCTED_PCKG_NAME",packageModel.getName());
            Log.e("SELCTED_PCKG_NAME", packageModel.getTest_code());
            Log.e("SELECTEDLAB_TEST_ID", packageModel.getThyro_profile_id());

            selectedModel.setTest_code(packageModel.getTest_code());
            selectedModel.setPrice(packageModel.getZiffy_profile_price());
            selectedModel.setNames(packageModel.getName());
            selectedModel.setTest_names(packageModel.getTest_code());
            selectedModel.setLab_test_id(packageModel.getThyro_profile_id());
            selectedModel.setISTestProfile("THYP");
            arrayPAckage.add(selectedModel);

        }

        for (int i=0;i<billdetailArrayList2.size();i++)
        {

            SelectedPackageModel selectedModel = new SelectedPackageModel();
            BillingDetailModel packageModel = billdetailArrayList2.get(i);
            selectedModel.setNames(packageModel.getName());
            arrayPAckageTesname.add(selectedModel);
        }

    }

}
