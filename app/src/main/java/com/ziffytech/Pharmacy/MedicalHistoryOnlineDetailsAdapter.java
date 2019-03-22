package com.ziffytech.Pharmacy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziffytech.R;
import com.ziffytech.activities.AdapterTestlistshowing;
import com.ziffytech.activities.AppointmentDetailsActivity;
import com.ziffytech.activities.BillingActivity;

import java.util.ArrayList;

public class MedicalHistoryOnlineDetailsAdapter extends RecyclerView.Adapter<MedicalHistoryOnlineDetailsAdapter.ViewHolder> {

    private ArrayList<OnlineModel> modelArrayList;
    Context context;
    AdapterTestlistshowing.OnItemClickListener clickListener;

    public MedicalHistoryOnlineDetailsAdapter(Context context, ArrayList<OnlineModel> names) {
        this.context = context;
        this.modelArrayList = names;
    }

    @Override
    public MedicalHistoryOnlineDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_med_order_details, parent, false);
        return new MedicalHistoryOnlineDetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MedicalHistoryOnlineDetailsAdapter.ViewHolder holder, int position) {

        final OnlineModel model = modelArrayList.get(position);


        holder.txt_amount.setText(model.getAmount());
        holder.txt_date_time.setText(model.getDate_time());

        if (model.getTxn_id().equals("0")){
            holder.layout_txn.setVisibility(View.GONE);
        }else{

            holder.txt_txn_id.setText(model.getTxn_id());
            holder.txtv_status.setText(model.getStatus());

            if (model.getStatus().equals("0")) {
                holder.txtv_status.setText("Initialized");
                holder.txtv_status.setTextColor(Color.parseColor("#fff29d13"));

            } else if (model.getStatus().equals("1")) {
                holder.txtv_status.setText("Successful");
                holder.txtv_status.setTextColor(Color.parseColor("#37b337"));
            } else if (model.getStatus().equals("2")){
                holder.txtv_status.setText("Failed");
                holder.txtv_status.setTextColor(Color.parseColor("#ff0000"));
            }else  if (model.getStatus().equals("3")){
                holder.txtv_status.setText("Cancelled");
                holder.txtv_status.setTextColor(Color.parseColor("#ff0000"));

            }


        }






        holder.text_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, AppointmentDetailsActivity.class);
                intent.putExtra("medicine",model.getOrder_details());

                Log.e("medicine",model.getOrder_details());

                intent.putExtra("start_time","");
                intent.putExtra("appointment_date",model.getDate_time());

                intent.putExtra("total_price", model.getAmount());
                intent.putExtra("final_total", model.getAmount());
                intent.putExtra("status", "4");
                context.startActivity(intent);

            }
        });











    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }


    public void SetOnItemClickListener(
            final AdapterTestlistshowing.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }



    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView txt_amount;
        TextView txt_date_time;
        TextView text_view_details;
        TextView txt_txn_id;
        TextView txtv_status;
        Button btn_cancel;
        LinearLayout layout_txn;

        ViewHolder(View itemView) {
            super(itemView);

            txt_amount = (TextView) itemView.findViewById(R.id.txt_amount);
            txt_date_time = (TextView) itemView.findViewById(R.id.txt_date_time);
            text_view_details= (TextView) itemView.findViewById(R.id.text_view_details);
            txt_txn_id= (TextView) itemView.findViewById(R.id.txt_txn_id);
            txtv_status= (TextView) itemView.findViewById(R.id.txtv_status);
            btn_cancel= (Button) itemView.findViewById(R.id.btn_cancel);
            layout_txn= (LinearLayout) itemView.findViewById(R.id.layout_txn);

        }



    }

    public void filterList(ArrayList<OnlineModel> filterdNames) {
        this.modelArrayList = filterdNames;
        notifyDataSetChanged();

        Log.e("FILTERED LIST", "" + modelArrayList);
    }
}