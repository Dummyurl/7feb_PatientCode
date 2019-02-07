package com.ziffytech.Wallet;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;

import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.CustomRequestForString;
import com.ziffytech.activities.SelfTestserach;
import com.ziffytech.models.TransactionHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WalletActivity extends CommonActivity {

    static ArrayList<TransactionHistory> arrayListAll;
   // static ArrayList<TransactionHistory> arrayListSuccess;
   // static ArrayList<TransactionHistory> arrayListFailure;
    TextView text_wallet_amt;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerview_history;
    TextView tv_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        setHeaderTitle("Ziffy Wallet History");
        allowBack();

        text_wallet_amt = findViewById(R.id.text_wallet_amt);
        recyclerview_history=findViewById(R.id.recyclerview_history);
        layoutManager = new LinearLayoutManager(WalletActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_history.setHasFixedSize(true);
        recyclerview_history.setLayoutManager(layoutManager);
        tv_no_data=findViewById(R.id.tv_no_data);

        text_wallet_amt.setText(ConstValue.CURRENCY + " " +common.getSession(ApiParams.ZIFFY_WALLET_AMT));





        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id",common.getSession("user_id"));
       // params.put("payment_status", "0");
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_TRANS_HISTORY, params, this.createRequestSuccessListenerTransHostory(), this.createRequestErrorListenerTransHistory());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);



    }


    private Response.Listener<String> createRequestSuccessListenerTransHostory() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("TRANS_HISTORY_RESPONSE", response.toString());


                arrayListAll = new ArrayList<>();
               // arrayListSuccess = new ArrayList<>();
               // arrayListFailure = new ArrayList<>();
//
                try {
                    Log.e("###", "TRY");
                    JSONObject jsonObject = new JSONObject(response);

                    int result = jsonObject.getInt("response");

                    if (result == 1) {


                      //  common.setSession(ApiParams.ZIFFY_WALLET_AMT, jsonObject.getString("wallet_amt"));

                        JSONArray data = jsonObject.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {

                            JSONObject object = data.getJSONObject(i);

                            TransactionHistory transactionHistory = new TransactionHistory();



                                Log.e("###","SUCCESS");

                                String trans_for = object.getString("transaction_mode");
                                Log.e("trans_for", trans_for);


                                transactionHistory.setTrans_mode(object.getString("transaction_mode"));
                                Log.e("transaction_mode", object.getString("transaction_mode"));

                                transactionHistory.setPayment_status(object.getString("payment_status"));
                                Log.e("payment_status", object.getString("payment_status"));


                                transactionHistory.setTxn_id(object.getString("txn_id"));
                                Log.e("txn_id", object.getString("txn_id"));

                                transactionHistory.setAmount(object.getString("amount"));
                                Log.e("amount", object.getString("amount"));

                                transactionHistory.setDate_time(object.getString("date_time"));
                                Log.e("date_time", object.getString("date_time"));


                                transactionHistory.setTrans_mode(object.getString("transaction_mode"));
                                Log.e("transaction_mode", object.getString("transaction_mode"));


                                transactionHistory.setBookedFor(object.getString("varient_name"));
                                Log.e("varient_name", object.getString("varient_name"));

                                transactionHistory.setTest(object.getString("test_decode"));
                                Log.e("test_decode", object.getString("test_decode"));

/*

                              //  arrayListSuccess.add(transactionHistory);


                                Log.e("###","FAILURE");

                               // String trans_for = object.getString("transaction_mode");
                               // Log.e("trans_for", trans_for);


                                transactionHistory.setTrans_mode(object.getString("transaction_mode"));
                                Log.e("transaction_mode", object.getString("transaction_mode"));

                                transactionHistory.setPayment_status(object.getString("payment_status"));
                                Log.e("payment_status", object.getString("payment_status"));


                                transactionHistory.setTxn_id(object.getString("txn_id"));
                                Log.e("txn_id", object.getString("txn_id"));

                                transactionHistory.setAmount(object.getString("amount"));
                                Log.e("amount", object.getString("amount"));

                                transactionHistory.setDate_time(object.getString("date_time"));
                                Log.e("date_time", object.getString("date_time"));


                                transactionHistory.setTrans_mode(object.getString("transaction_mode"));
                                Log.e("transaction_mode", object.getString("transaction_mode"));


                                transactionHistory.setBookedFor(object.getString("varient_name"));
                                Log.e("varient_name", object.getString("varient_name"));

                                transactionHistory.setTest(object.getString("test_decode"));
                                Log.e("test_decode", object.getString("test_decode"));



                                Log.e("###","OTHERS");

                                String trans_for = object.getString("transaction_mode");
                                Log.e("trans_for", trans_for);


                                transactionHistory.setTrans_mode(object.getString("transaction_mode"));
                                Log.e("transaction_mode", object.getString("transaction_mode"));

                                transactionHistory.setPayment_status(object.getString("payment_status"));
                                Log.e("payment_status", object.getString("payment_status"));

                                transactionHistory.setTxn_id(object.getString("txn_id"));
                                Log.e("txn_id", object.getString("txn_id"));

                                transactionHistory.setAmount(object.getString("amount"));
                                Log.e("amount", object.getString("amount"));

                                transactionHistory.setDate_time(object.getString("date_time"));
                                Log.e("date_time", object.getString("date_time"));


                                transactionHistory.setTrans_mode(object.getString("transaction_mode"));
                                Log.e("transaction_mode", object.getString("transaction_mode"));


                                transactionHistory.setBookedFor(object.getString("varient_name"));
                                Log.e("varient_name", object.getString("varient_name"));

                                transactionHistory.setTest(object.getString("test_decode"));
                                Log.e("test_decode", object.getString("test_decode"));
*/

                                arrayListAll.add(transactionHistory);










                           // setupViewPager(viewPager);

                        }

                        HistoryAdapter historyAdapter=new HistoryAdapter(WalletActivity.this,arrayListAll);
                        recyclerview_history.setAdapter(historyAdapter);




                    }else {

                        recyclerview_history.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
    }

    private Response.ErrorListener createRequestErrorListenerTransHistory() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                Log.i("##", "##" + error.toString());


            }
        };
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

