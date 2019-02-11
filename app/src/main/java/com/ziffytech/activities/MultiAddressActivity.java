package com.ziffytech.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziffytech.R;
import com.ziffytech.util.CommonClass;
import com.ziffytech.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MultiAddressActivity extends CommonActivity {

    ListView listAddress;
    ImageView image_add_address, image_arrow_slot;
    TextView text_next, text_no_address;
    // ArrayList<Address> addressArrayList;
    public static boolean isAddressSelected = false;
    Toolbar toolbar;
    // AddressAdapter addressAdapter;
    CommonClass common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_address);
        common = new CommonClass(MultiAddressActivity.this);

        listAddress = (ListView) findViewById(R.id.list_address);
        image_add_address = (ImageView) findViewById(R.id.image_add_address);
//        image_arrow_slot = (ImageView) findViewById(R.id.image_arrow_slot);
//        text_next = (TextView) findViewById(R.id.text_next);
        text_no_address = (TextView) findViewById(R.id.text_no_address);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  image_add_address.setColorFilter();


        HashMap<String, String> params = new HashMap<String, String>();

        Log.e("Params", params.toString());
/*
            CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, GET_ADDRESS, params, this.createRequestSuccessListenerAddress(), this.createRequestErrorListener());
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(customRequestForString);*/


        image_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(MultiAddressActivity.this, AddNewAddress.class);
                startActivity(intent);*/
            }
        });

        image_arrow_slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddressSelected) {
                    //isAddressSelected=false;
                    //addressAdapter.notifyDataSetChanged();
                    //Intent intent = new Intent(MultiAddressActivity.this, ServicesActivity.class);
                    //startActivity(intent);
                } else {

                    MyUtility.showAlertMessage(MultiAddressActivity.this, "Please select address");
                }
            }
        });

        text_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAddressSelected) {
                    // isAddressSelected=false;
                    //addressAdapter.notifyDataSetChanged();
                /*    Intent intent = new Intent(MultiAddressActivity.this, ServicesActivity.class);
                    startActivity(intent);*/
                    finish();
                } else {

                    MyUtility.showAlertMessage(MultiAddressActivity.this, "Please select address");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isAddressSelected = false;
    }

    private Response.Listener<String> createRequestSuccessListenerAddress() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // App.stopProgressDialog();
                Log.e("Response :", response.toString());
          /*      try {
                    JSONObject jsonObject=new JSONObject(response.toString());

                    boolean status=jsonObject.getBoolean("status");

                    if (status){
                        addressArrayList=new ArrayList<>();

                        JSONArray jsonArray=jsonObject.getJSONArray("details");

                        for (int i=0;i<jsonArray.length();i++){
                            Address.address=new Address();


                            JSONObject object=jsonArray.getJSONObject(i);

                            String id=object.getString("id");
                            Address.address.setAddress_id(id);

                            String address=object.getString("address");
                            Address.address.setAddress_line(address);

                            String landmark=object.getString("landmark");
                            Address.address.setLand_mark(landmark);

                            String area=object.getString("area");
                            Address.address.setArea(area);

                            String pincode=object.getString("pincode");
                            Address.address.setPincode(pincode);


                            addressArrayList.add(  Address.address);

                            addressAdapter=new AddressAdapter(MultiAddressActivity.this,addressArrayList);
                            listAddress.setAdapter(addressAdapter);

                        }





                    }else {

                        listAddress.setVisibility(View.GONE);
                        text_no_address.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
*/
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##", "##" + error.toString());


                MyUtility.showAlertMessage(MultiAddressActivity.this, "Something Went Wrong, Please Try again");


            }
        };
    }
}