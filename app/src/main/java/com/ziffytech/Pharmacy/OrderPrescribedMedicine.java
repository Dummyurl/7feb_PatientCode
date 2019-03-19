package com.ziffytech.Pharmacy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.CustomRequestForString;

import com.ziffytech.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class OrderPrescribedMedicine extends CommonActivity {

    ArrayList<MedicineOrderModel> mList;

    RecyclerView recyclerview_medicine;


    ArrayList<MedicineOrderModel> medicineOrderModelArrayList;

    TextView notfound;
    Button submit;
    boolean status = false;
    LinearLayoutManager layoutManager;
    String prescription_id;
    ArrayList<MedicineOrderModel> selectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_prescribed_medicine);
        setHeaderTitle("Recommended Medicine");
        allowBack();
        setUpViews();



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedList = new ArrayList<>();

                boolean isSelected = false;

                for (MedicineOrderModel model : medicineOrderModelArrayList) {
                    if (model.isChecked()) {
                        isSelected = true;
                        Log.e("Test selected", model.getMedicine_price());
                        selectedList.add(model);
                    }
                }

                if (isSelected) {


                    Log.e("isSelected", String.valueOf(isSelected));
                    Log.e("M Selected:", new Gson().toJson(selectedList).toString());
                    Intent intent = new Intent(OrderPrescribedMedicine.this, DateTimeActivity.class);
                    intent.putExtra("medicine", new Gson().toJson(selectedList).toString());
                    intent.putExtra("pres_id", prescription_id);


                    Log.e("medicine", new Gson().toJson(medicineOrderModelArrayList).toString());
                    startActivity(intent);

                } else {

                    MyUtility.showAlertMessage(OrderPrescribedMedicine.this, "Please select medicine");
                }
            }


        });




    }








    public void setUpViews() {


        notfound = (TextView) findViewById(R.id.notfound);
        notfound.setVisibility(View.GONE);
        recyclerview_medicine = (RecyclerView) findViewById(R.id.recyclerview_medicine);
        recyclerview_medicine.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_medicine.setLayoutManager(layoutManager);
        submit = (Button) findViewById(R.id.submit);
        prescription_id=getIntent().getStringExtra("presc_id");
        getdata();
    }






    private void getdata() {


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("prescription_id", getIntent().getStringExtra("presc_id"));
        Log.e("PARAMS", params.toString());
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_PRESCRIBED_MEDICINE, params, this.createRequestSuccessListenerPrescription(), this.createRequestErrorListenerPrescription());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);
        showPrgressBar();

    }

    private Response.Listener<String> createRequestSuccessListenerPrescription() {
        return new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {

                Log.e("PRESCRIPTION_RESPONSE", response.toString());
                hideProgressBar();


                medicineOrderModelArrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt("response") == 1) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            MedicineOrderModel medicineOrderModel = new MedicineOrderModel();

                            medicineOrderModel.setDrug_name(object.getString("drug_name"));
                            Log.e("drug_name", medicineOrderModel.getDrug_name());
                            medicineOrderModel.setQuantity(object.getString("quantity"));
                            Log.e("quantity", medicineOrderModel.getQuantity());
                            medicineOrderModel.setMedicine_price(object.getString("selling_price"));
                            Log.e("selling_price", medicineOrderModel.getMedicine_price());

                            medicineOrderModel.setAvailability(object.getString("availability"));

                            Log.e("availability", medicineOrderModel.getAvailability());


                            medicineOrderModelArrayList.add(medicineOrderModel);

                            Log.e("medicineOrderModelArrayList", "" + medicineOrderModelArrayList);


                        }

                        OrderMedicineAdapter orderMedicineAdapter = new OrderMedicineAdapter(OrderPrescribedMedicine.this, medicineOrderModelArrayList);
                        recyclerview_medicine.setAdapter(orderMedicineAdapter);

                    } else {

                        recyclerview_medicine.setVisibility(View.GONE);
                        notfound.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        };
    }

    private Response.ErrorListener createRequestErrorListenerPrescription() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    MyUtility.showAlertMessage(OrderPrescribedMedicine.this, "Server is busy.Please try again");
                }
                Log.i("##", "##" + error.toString());
                hideProgressBar();
            }
        };
    }
}
