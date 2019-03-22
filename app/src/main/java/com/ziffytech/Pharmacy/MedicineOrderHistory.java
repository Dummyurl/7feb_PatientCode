package com.ziffytech.Pharmacy;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Api;
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
import java.util.List;

public class MedicineOrderHistory extends CommonActivity {

    ArrayList<OnlineModel> onlineModelArrayList;
    ArrayList<OfflineModel> offlineModelArrayList;
    TextView text_date;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    OnlineFragment onlineFragment;
    OfflineFragment offlineFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_order_history);
        setHeaderTitle("Medicine Order History");
        allowBack();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id",common.get_user_id());
        Log.e("PARAMS",params.toString());
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.MEDICINE_HISTORY, params, this.createRequestSuccessListenerTimeSlot(), this.createRequestErrorListenerTimeSlot());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);
        startProgressDialog(MedicineOrderHistory.this);



    }




    private void setupViewPager(ViewPager viewPager) {



        onlineFragment = OnlineFragment.newInstance();
        offlineFragment = OfflineFragment.newInstance();
//        EveningFragment eveningFragment = EveningFragment.newInstance(extrasEvn);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFragment(onlineFragment, "Online");
        adapter.addFragment(offlineFragment, "Offline");
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
    }






    private Response.Listener<String> createRequestSuccessListenerTimeSlot() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                stopProgressDialog();

                Log.e("med_history_response", response.toString());


                onlineModelArrayList = new ArrayList<>();
                offlineModelArrayList = new ArrayList<>();

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt("response") == 1) {

                        JSONArray online = jsonObject.getJSONArray("online");
                        JSONArray offline = jsonObject.getJSONArray("offline");

                        if (!online.equals("")) {

                            for (int i = 0; i < online.length(); i++) {

                                JSONObject object = online.getJSONObject(i);

                                OnlineModel onlineModel = new OnlineModel();

                                if (!object.isNull("txn_id")){
                                    onlineModel.setTxn_id(object.getString("txn_id"));
                                }else {
                                    onlineModel.setTxn_id("");
                                }

                                onlineModel.setDate_time(object.getString("created"));
                                onlineModel.setOrder_details(object.getString("user_prescription"));
                                onlineModel.setAmount(object.getString("amount"));

                                if (object.has("txn_id") && (!object.getString("txn_id").equals(""))){
                                    Log.e("txn_id","true");
                                    onlineModel.setTxn_id(object.getString("txn_id"));
                                }else {
                                    onlineModel.setTxn_id("0");
                                }

                                onlineModel.setStatus(object.getString("payment_status"));

                                onlineModelArrayList.add(onlineModel);

                                Log.e("onlineModelArrayList",onlineModelArrayList.toString());
                            }


                        }
                        if (!offline.equals("")) {

                            for (int i = 0; i < offline.length(); i++) {

                                JSONObject object = offline.getJSONObject(i);

                                OfflineModel offlineModel = new OfflineModel();


                                offlineModel.setPres_img(object.getString("image"));
                                offlineModel.setDate_time(object.getString("created"));
                                offlineModel.setPrescriptionoff_id(object.getString("prescriptionoff_id"));

                                Log.e("image",offlineModel.getPres_img());
                                Log.e("created",offlineModel.getDate_time());


                                offlineModelArrayList.add(offlineModel);
                                Log.e("offlineModelArrayList",offlineModelArrayList.toString());
                            }


                        }

                        onlineFragment.updateDataOnline(onlineModelArrayList);
                        offlineFragment.updateDataOffline(offlineModelArrayList);


                    } else {

                        MyUtility.showAlertMessage(MedicineOrderHistory.this, "History Not Available.");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };


    }


    private Response.ErrorListener createRequestErrorListenerTimeSlot() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                stopProgressDialog();
                if (error instanceof TimeoutError)
                {
                    MyUtility.showAlertMessage(MedicineOrderHistory.this, "Server is busy.Please try again");
                }
                Log.i("##", "##" + error.toString());

            }
        };
    }
}
