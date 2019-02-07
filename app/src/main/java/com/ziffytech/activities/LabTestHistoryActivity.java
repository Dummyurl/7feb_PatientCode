package com.ziffytech.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.Wallet.HistoryAdapter;
import com.ziffytech.models.TransactionHistory;
import com.ziffytech.util.CommonClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LabTestHistoryActivity extends CommonActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    CommonClass common;
    static ArrayList<TransactionHistory> arrayListAll;
    RecyclerView recyclerView;
    TextView tv_no_data;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        common=new CommonClass(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_history);

        setHeaderTitle("Appointment History");
        allowBack();

//        viewPager = (ViewPager) findViewById(R.id.viewpager);



        recyclerView=findViewById(R.id.recyclerview_history);
        tv_no_data=findViewById(R.id.tv_no_data);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);



        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", common.getSession("user_id"));
        params.put("payment_status","0");
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_TRANS_HISTORY, params, this.createRequestSuccessListenerTransHostory(), this.createRequestErrorListenerTransHistory());
        RequestQueue requestQueue =Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);



    }






   /* private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new AllFragment(), "");
        //adapter.addFragment(new SuccessFragment(), "Success");
        //adapter.addFragment(new FailFragment(), "Failed");
        viewPager.setAdapter(adapter);
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
    }*/


    private Response.Listener<String> createRequestSuccessListenerTransHostory() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("TRANS_HISTORY_RESPONSE", response.toString());


                try {
                    Log.e("###","TRY");
                    JSONObject jsonObject=new JSONObject(response);

                    String result=jsonObject.getString("response");

                    if (result.equals("1")){


                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                        arrayListAll=new ArrayList<>();
                        // arrayListSuccess=new ArrayList<>();
                        // arrayListFailure=new ArrayList<>();


                        for (int i=0;i<jsonArray.length();i++) {
                            Log.e("###","FOR");

                            JSONObject object = jsonArray.getJSONObject(i);

                            TransactionHistory transactionHistory = new TransactionHistory();

                            String trans_for = object.getString("transaction_mode");
                            Log.e("trans_for", trans_for);


                            transactionHistory.setTrans_mode(object.getString("transaction_mode"));
                            Log.e("transaction_mode", object.getString("transaction_mode"));

                            transactionHistory.setPayment_status(object.getString("payment_status"));
                            Log.e("payment_status", object.getString("payment_status"));


                            if (object.getString("payment_status").contains("1")) {
                                Log.e("####","success");
                                transactionHistory.isSuccess();
                            }else if(object.getString("payment_status").contains("2")){
                                transactionHistory.isFail();
                                Log.e("####","fail");
                            }else if (object.getString("payment_status").equals("0")){
                                transactionHistory.isAll();
                            }



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

                              /* if (object.getString("transaction_mode").equals("1")){

                                   transactionHistory.setBookedFor(object.getString("lab_name"));
                               }else {
                                   transactionHistory.setBookedFor(object.getString("doct_name"));
                               }*/

                            arrayListAll.add(transactionHistory);
                            Log.e("ALL",arrayListAll.toString());




                          /*  if (object.getString("payment_status").equals("1")) {
                                Log.e("####","Success payment");
                                transactionHistory.setTrans_mode(object.getString("Lab booking"));
                                Log.e("transaction_mode", object.getString("transaction_mode"));

                                transactionHistory.setPayment_status("Succeessful");
                                Log.e("payment_status", object.getString("payment_status"));

                                transactionHistory.setTxn_id(object.getString("txn_id"));
                                Log.e("txn_id", object.getString("txn_id"));

                                transactionHistory.setAmount(object.getString("amount"));
                                Log.e("amount", object.getString("amount"));

                                transactionHistory.setDate_time(object.getString("date_time"));
                                Log.e("date_time", object.getString("date_time"));

                                arrayListSuccess.add(transactionHistory);

                            }else  if (object.getString("payment_status").equals("2")) {
                                Log.e("####","Failed payment");
                                transactionHistory.setTrans_mode(object.getString("Doctor Appointment booking"));
                                Log.e("transaction_mode", object.getString("transaction_mode"));

                                transactionHistory.setPayment_status("Failed");
                                Log.e("payment_status", object.getString("payment_status"));

                                transactionHistory.setTxn_id(object.getString("txn_id"));
                                Log.e("txn_id", object.getString("txn_id"));

                                transactionHistory.setAmount(object.getString("amount"));
                                Log.e("amount", object.getString("amount"));

                                transactionHistory.setDate_time(object.getString("date_time"));
                                Log.e("date_time", object.getString("date_time"));

                                arrayListFailure.add(transactionHistory);
                            }
*/

                        }
                        HistoryAdapter historyAdapter=new HistoryAdapter(LabTestHistoryActivity.this, LabTestHistoryActivity.arrayListAll);
                        recyclerView.setAdapter(historyAdapter);


                    }else {

                        Toast.makeText(LabTestHistoryActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
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
}

