package com.ziffytech.booklab.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.booklab.LabViewActivity;
import com.ziffytech.booklab.models.LabDetailListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LabDetailListAdapter extends RecyclerView.Adapter<LabDetailListAdapter.MyViewHolerModule> {


    private ArrayList<LabDetailListModel> arrayList;

    private Context context;

     String isHome,lat,lng,test_adapter,test_details;
     String appID,price;
     String finalTest;
     double amt=0;
    String Amt;


    public LabDetailListAdapter(ArrayList<LabDetailListModel> arrayList, Context context, String isHome, String lat, String lng, String test, String details) {
        this.arrayList = arrayList;
        this.context = context;
        this.isHome=isHome;
        this.lat=lat;
        this.lng=lng;
        this.test_adapter=test;
        this.test_details=details;
        Log.e("ADAPTER_test",test_adapter);
    }


    @Override
    public LabDetailListAdapter.MyViewHolerModule onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext())

                .inflate(R.layout.lab_listdetail_item, parent, false);

        return new LabDetailListAdapter.MyViewHolerModule(layoutView, context, arrayList);
    }

    @Override
    public void onBindViewHolder(LabDetailListAdapter.MyViewHolerModule holder, int position) {


        final LabDetailListModel item = arrayList.get(position);

        Picasso.with(context).load(ConstValue.BASE_URL+"uploads/lab/"+item.getLab_photo()).into(holder.mLabImage);


        holder.mLabName.setText(item.getLab_name());
        holder.mLabAddress.setText(item.getLab_address());
        holder.mOpeningTime.setText(item.getLab_opening_time()+" - " +item.getLab_closing_time());


        List<JTEST> testList=new ArrayList<>();


        try {
            Log.e("######,","ADAPTER TRY");

            JSONArray array= new JSONArray(test_adapter);
            for(int i=0;i<array.length();i++)
            {

                Log.e("######,","ADAPTER TRY");
                JSONObject jsonObject=array.getJSONObject(i);

                    LabDetailListAdapter.JTEST model=new LabDetailListAdapter.JTEST();
                    model.setTest_code(jsonObject.getString("test_code"));
                    model.setISTestProfile(jsonObject.getString("ISTestProfile"));
                    appID=jsonObject.getString("app_id");
                    price=jsonObject.getString("price");
                    Log.e("#####"+"APP ID",appID);
                    Log.e("#####"+"PRICE",price);
                    testList.add(model);

                finalTest=new Gson().toJson(testList).toString();
                Log.e("####"+"FINAL TEST",finalTest);

                double p= Double.parseDouble(price);
                Log.e("PPPPPP", String.valueOf(p));

                amt=amt+p;
                Log.e("AMOUNT", String.valueOf(amt));

                 Amt= String.valueOf(amt);
                Log.e("#######AMOUNT", String.valueOf(amt));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.mBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lIntent=new Intent(context, LabViewActivity.class);
                lIntent.putExtra("lab_id",item.getLab_id());
                lIntent.putExtra("lab_name",item.getLab_name());
                Log.e("###","LAB ID ---"+item.getLab_id());
                lIntent.putExtra("lat",lat);
                Log.e("###","LAT ---"+lat);
                lIntent.putExtra("lng",lng);
                Log.e("###","LONG ---"+lng);
                lIntent.putExtra("ishome",isHome);
               Log.e("ishome",isHome);
                lIntent.putExtra("test",finalTest);
                Log.e("test",finalTest);
                lIntent.putExtra("app_id",appID);
                Log.e("app_id",appID);
                lIntent.putExtra("test_price",Amt);
                lIntent.putExtra("test_details",test_details);
                Log.e("test_details",test_details);


                context.startActivity(lIntent);
            }
        });


    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static final class MyViewHolerModule extends RecyclerView.ViewHolder
    {
        ImageView mLabImage;
        Button mBookButton;

        TextView mLabAddress;
        TextView mOpeningTime;
        TextView mClosingTime;
        TextView mLabName;


        public MyViewHolerModule(View itemView, Context context, ArrayList<LabDetailListModel> arrayList)
        {
            super(itemView);
            mLabImage = (ImageView) itemView.findViewById(R.id.lab_image);
            mLabName = (TextView) itemView.findViewById(R.id.lab_name);
            mLabAddress = (TextView) itemView.findViewById(R.id.LabAddress);
            mOpeningTime = (TextView) itemView.findViewById(R.id.Opening_time);
            mClosingTime = (TextView) itemView.findViewById(R.id.Closing_time);
            mBookButton = (Button) itemView.findViewById(R.id.book);
        }

    }


    private class JTEST{
        public String getTest_code() {
            return test_code;
        }
        public void setTest_code(String test_code) {
            this.test_code = test_code;
        }
        private String test_code;

        public String getISTestProfile() {
            return ISTestProfile;
        }

        public void setISTestProfile(String ISTestProfile) {
            this.ISTestProfile = ISTestProfile;
        }

        private String ISTestProfile;
        private String price;
    }



}




