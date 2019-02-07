package com.ziffytech.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ziffytech.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppointmentDetailsAdapter  extends RecyclerView.Adapter<AppointmentDetailsAdapter.ViewHolder> implements AdapterView.OnItemClickListener{



    private String names;
    Context context;
    AdapterTestlistshowing.OnItemClickListener clickListener;

    public AppointmentDetailsAdapter(Context context, String test) {
        this.context=context;
        this.names = test;
        Log.e("APPOINTMENTADAPTER",names);
    }

    @Override
    public AppointmentDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new AppointmentDetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        try {

            Log.e("#####","TRY ADAPTER");
            JSONArray jsonArray=new JSONArray(names);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                holder.textViewName.setText(jsonObject.getString("name"));
                holder.text_price.setText(jsonObject.getString("price"));



            }

            } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public void SetOnItemClickListener(
            final AdapterTestlistshowing.OnItemClickListener itemClickListener) {
        this.clickListener =  itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }



    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        TextView textViewName;
        TextView text_price;

        ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.text_test_name);
            text_price = (TextView) itemView.findViewById(R.id.text_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());

        }
    }


    public class JTEST{

        String tname;
        String tprice;

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }
    }


}