package com.ziffytech.booklab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.ziffytech.R;
import com.ziffytech.booklab.adapter.LabListAdapter;
import com.ziffytech.booklab.apiinterface.APIInterface;
import com.ziffytech.booklab.apiinterface.ApiClient;
import com.ziffytech.booklab.apiinterface.models.LabListApi;
import com.ziffytech.booklab.models.LabListModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LabListActivity extends AppCompatActivity {

    ArrayList<LabListModel> mData;
    LabListAdapter lablistAdapter;
    RecyclerView recyclerView;
    String search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_list);
        setUpViews();

    }

    public void setUpViews()
    {

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_lablist);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mData=new ArrayList<>();

        lablistAdapter=new LabListAdapter(mData,this);
        recyclerView.setAdapter(lablistAdapter);


        getdata();


    }

    private void getdata() {


        Log.d("msg","data");

        APIInterface apiInterface = ApiClient.getClient().create(APIInterface.class);
        Call<LabListApi> call3 = apiInterface.search_lab_by_name(search);
        call3.enqueue(new Callback<LabListApi>() {
            @Override
            public void onResponse(Call<LabListApi> call, Response<LabListApi> response) {

                Log.e("Data:", new Gson().toJson(response.body()));


                if (response.body() != null) {


                    LabListApi model = response.body();

                    if (model.response == 1) {


                        if (model.data.size() > 0) {


                            for (int i = 0; i < model.data.size(); i++) {
                                LabListModel model1=new LabListModel();
                                model1.setLab_name(model.data.get(i).lab_name);

                                mData.add(model1);


                            }

                            // lablistAdapter=new LabDetailListAdapter(mList,getApplicationContext());
                            // recyclerView.setAdapter(lablistAdapter);

                        }

                        lablistAdapter.notifyDataSetChanged();
                    } else {
                        // MyUtility.showAlertMessage(getApplicationContext(), MyUtility.FAILED_TO_GET_DATA);
                    }

                } else {


                    // MyUtility.showAlertMessage(getApplicationContext(), MyUtility.FAILED_TO_GET_DATA);
                }


            }

            @Override
            public void onFailure(Call<LabListApi> call, Throwable t) {
                call.cancel();
                //  MyUtility.showAlertMessage(getApplicationContext(), MyUtility.INTERNET_ERROR);

            }
        });

    }

}
