package com.ziffytech.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.models.Medication_History_Model;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AllMedicalReports extends CommonActivity {

    public static ArrayList<Medication_History_Model> allreportlist;
    MyReportsAdaptercall reportsAdapter;
    RecyclerView recyclerView;
    private TextView tvNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_medical_reports);
        setHeaderTitle("Medical Records");
        allowBack();
        allreportlist = new ArrayList<>();
        tvNotFound = (TextView) findViewById(R.id.tvnotfound);
        tvNotFound.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.test_report_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        loadData();
    }

    private void loadData()
    {
        HashMap<String, String> params = new HashMap<>();
        String user_id1 = common.get_user_id();
        params.put("user_id", user_id1);
        String url = ApiParams.GET_ALL_REPORTS;
      startProgressDialog(AllMedicalReports.this);
        VJsonRequest vJsonRequest = new VJsonRequest(this, url, params,
                new VJsonRequest.VJsonResponce() {

                    @Override
                    public void VResponce(String responce) {
                        Log.e("GET_DOC_RESPONSE", responce);
                       stopProgressDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(responce);
                            JSONArray data = jsonObject.getJSONArray("data");
                            allreportlist.clear();

                            Log.e("####", String.valueOf(jsonObject.getInt("responce")));

                            if(jsonObject.getInt("responce")==1) {


                                for (int i = 0; i < data.length(); i++) {


                                    JSONObject object = data.getJSONObject(i);
                                    {
                                        Medication_History_Model model = new Medication_History_Model();

                                        model.setPatient_id(object.getString("patient_id"));
                                        Log.e("patient_id", object.getString("patient_id"));

                                        model.setZiffy_code(object.getString("ziffy_code"));
                                        Log.e("ziffy_code", object.getString("ziffy_code"));

                                        model.setDoctor_name(object.getString("doctor_name"));
                                        Log.e("doctor_name", object.getString("doctor_name"));

                                        model.setToken(object.getString("token"));
                                        Log.e("token", object.getString("token"));

                                        model.setMobile_number(object.getString("mobile_number"));
                                        Log.e("mobile_number", object.getString("mobile_number"));

                                        model.setCreated_date(object.getString("created"));
                                        Log.e("created", object.getString("created"));

                                        model.setModified_date(object.getString("modified"));
                                        Log.e("modified", object.getString("modified"));

                                        model.setFinal_note(object.getString("final_note"));
                                        Log.e("final_note", object.getString("final_note"));

                                        model.setDoctor_note(object.getString("doctor_note"));
                                        Log.e("doctor_note", object.getString("doctor_note"));

                                        model.setTest_History_Id(object.getString("id"));
                                        Log.e("id", object.getString("id"));

                                        model.setTest_History_Category(object.getString("category"));
                                        Log.e("category", object.getString("category"));

                                        model.setTest_History_name(object.getString("test_name"));
                                        Log.e("test_name", object.getString("test_name"));


                                        allreportlist.add(model);
                                    }

                                    reportsAdapter = new MyReportsAdaptercall(AllMedicalReports.this, allreportlist);
                                    recyclerView.setAdapter(reportsAdapter);

                                }

                            }else {

                                tvNotFound.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }


                           /* if (allreportlist.isEmpty()) {
                                tvNotFound.setVisibility(View.VISIBLE);
                            } else {
                                tvNotFound.setVisibility(View.GONE);
                            }*/

                           // reportsAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            stopProgressDialog();
                            tvNotFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void VError(String responce) {
                        tvNotFound.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);

                     stopProgressDialog();
                    }
                });
    }




    public class MyReportsAdaptercall extends RecyclerView.Adapter<MyReportsAdaptercall.ProductHolder> {

        ArrayList<Medication_History_Model> allreportlist2;
        Activity activity;
        String type;
        private ArrayList<Medication_History_Model> mFilteredList;

        public MyReportsAdaptercall(Activity activity, ArrayList<Medication_History_Model> allreportlist2) {
            this.allreportlist2 = allreportlist2;
            this.activity = activity;
            mFilteredList = allreportlist2;
        }

        @Override
        public  MyReportsAdaptercall.ProductHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_report_list, parent, false);

            return new  MyReportsAdaptercall.ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(final  MyReportsAdaptercall.ProductHolder holder, final int position)
        {
            final Medication_History_Model categoryModel = mFilteredList.get(position);


            holder.date.setText(categoryModel.getCreated_date());
            holder.testname.setText(categoryModel.getTest_History_name());
            holder.type.setText(categoryModel.getTest_History_Category());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(AllMedicalReports.this, TestResult.class);
                    intent.putExtra("patient_id",categoryModel.getPatient_id());
                    intent.putExtra("test_code",categoryModel.getZiffy_code());
                    intent.putExtra("token",categoryModel.getToken());
                    intent.putExtra("testname",categoryModel.getTest_History_name());
                    intent.putExtra("date",categoryModel.getCreated_date());
                    intent.putExtra("givenby",categoryModel.getDoctor_name());
                    activity.startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount()
        {
            return mFilteredList.size();
        }

        public void filterList(ArrayList<Medication_History_Model> allreportlist2)
        {
            this.mFilteredList = allreportlist2;
            notifyDataSetChanged();
        }

        class ProductHolder extends RecyclerView.ViewHolder
        {

            TextView date,testname,type;


            public ProductHolder(View itemView)
            {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.date);
                testname = (TextView) itemView.findViewById(R.id.testname);
                type = (TextView) itemView.findViewById(R.id.type);

            }
        }


    }





}
