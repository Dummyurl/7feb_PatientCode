package com.ziffytech.Wallet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.CustomRequestForString;
import com.ziffytech.util.CommonClass;
import com.ziffytech.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OfferScreen extends CommonActivity implements OffersAdapter.OnOfferListener {

    RecyclerView recyclerViewOffers;
    TextView text_not_found;
    EditText edit_offer;
    CommonClass common;
    String trans_amt;
    LinearLayoutManager layoutManager;
    ArrayList<PromocodeModel> promocodeModelArrayList;
    boolean isPaymentOptionChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_screen);
        common = new CommonClass(OfferScreen.this);

        setHeaderTitle("Offers");
        allowBack();

        edit_offer = (EditText) findViewById(R.id.edit_offer);
        text_not_found = (TextView) findViewById(R.id.txt_not_found);


        recyclerViewOffers = (RecyclerView) findViewById(R.id.recyclerview_offers);
        layoutManager = new LinearLayoutManager(OfferScreen.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewOffers.setHasFixedSize(true);
        recyclerViewOffers.setLayoutManager(layoutManager);

        trans_amt=getIntent().getStringExtra("trans_amt");

        Log.e("trans amt",trans_amt);



/*
0 self test    0 doc
1 doc           1 lab
2 recommend
3 medicine     2 med
*/
        isPaymentOptionChecked = getIntent().getBooleanExtra("isPaymentOptionChecked", false);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("user_id", common.getSession("user_id"));
        params.put("transaction_amt",trans_amt);
        if (getIntent().getStringExtra("Activity").equalsIgnoreCase("0")) {
            params.put("code_use_for", "1");

        } else if (getIntent().getStringExtra("Activity").equalsIgnoreCase("1")) {
            params.put("code_use_for", "0");

        } else if (getIntent().getStringExtra("Activity").equalsIgnoreCase("2")) {
            params.put("code_use_for", "1");
        }


        Log.e("params_offers", params.toString());
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_PROMOCODE, params, this.createRequestSuccessListenerTestList(), this.createRequestErrorListenerTestList());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);
        showPrgressBar();
        Log.e("Service GET_PROMOCODE", ApiParams.GET_PROMOCODE);
    }


    private Response.Listener<String> createRequestSuccessListenerTestList() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgressBar();
                Log.e("OFFERS_RESPONSE", response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt("status") == 1) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        promocodeModelArrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            PromocodeModel promocodeModel = new PromocodeModel();

                            promocodeModel.setPromo_code_id(object.getString("id"));
                            Log.e("id", object.getString("id"));

                            promocodeModel.setPromo_code_name(object.getString("code_name"));
                            Log.e("code_name", object.getString("code_name"));

                            promocodeModel.setPromo_code_details(object.getString("description"));
                            Log.e("description", object.getString("description"));

                            promocodeModel.setMax_wallet_usage(object.getString("max_wallet_usage"));
                            Log.e("max_wallet_usage", object.getString("max_wallet_usage"));


                            promocodeModel.setCashback(object.getString("cashback"));
                            Log.e("cashback", object.getString("cashback"));

                            promocodeModelArrayList.add(promocodeModel);
                        }

                        OffersAdapter offersAdapter = new OffersAdapter(OfferScreen.this, promocodeModelArrayList, OfferScreen.this);

                        recyclerViewOffers.setAdapter(offersAdapter);


                    } else {

                        text_not_found.setVisibility(View.VISIBLE);
                        recyclerViewOffers.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private Response.ErrorListener createRequestErrorListenerTestList() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    MyUtility.showAlertMessage(OfferScreen.this, "Server is busy.Please try again");
                }
                Log.i("##", "##" + error.toString());
                hideProgressBar();
            }
        };
    }

    @Override
    public void applyPromoCode(String promo_id, String promo_name, String max_wallet_usage) {
        if (isPaymentOptionChecked) {
            Intent intent = new Intent();
            intent.putExtra("promo_id", promo_id);
            intent.putExtra("promo_name", promo_name);
            intent.putExtra("max_wallet_usage", max_wallet_usage);
            setResult(Activity.RESULT_OK, intent);
            onBackPressed();
        } else {


            AlertDialog.Builder ad = new AlertDialog.Builder(OfferScreen.this);
            ad.setMessage("You Can Not Apply Promo Code without selecting payment method.");

            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    onBackPressed();

                }
            });
            AlertDialog dialog = ad.create();
            dialog.show();
            dialog.setCancelable(false);
        }

    }
}
