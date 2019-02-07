package com.ziffytech.Wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.AppointmentDetailsActivity;
import com.ziffytech.models.TransactionHistory;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>/* implements AdapterView.OnClickListener */{

    private ArrayList<TransactionHistory> modelArrayList;
    Context context;
    HistoryAdapter.OnItemClickListener clickListener;
    String payment_status;


    public HistoryAdapter(Context context, ArrayList<TransactionHistory> names) {
        this.context=context;
        this.modelArrayList = names;

        Log.e("ADAPTER LIST",modelArrayList .toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trans_history, parent, false);
        return new HistoryAdapter.ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        final TransactionHistory model=modelArrayList.get(position);

        holder.txn_id.setText(model.getTxn_id());
        holder.amt.setText(ConstValue.CURRENCY+model.getAmount());
        if (model.getPayment_status().equals("0")) {
            holder.status.setText("Initialized");
            holder.status.setTextColor(Color.parseColor("#fff29d13"));

        } else if (model.getPayment_status().equals("1")) {
            holder.status.setText("Successful");
            holder.status.setTextColor(Color.parseColor("#37b337"));
        } else if (model.getPayment_status().equals("2")){
            holder.status.setText("Failed");
            holder.status.setTextColor(Color.parseColor("#ff0000"));
        }else  if (model.getPayment_status().equals("3")){
            holder.status.setText("Cancelled");
            holder.status.setTextColor(Color.parseColor("#ff0000"));

        }

        if (model.getTrans_mode().equals("1")) {

            holder.text_view_details.setVisibility(View.GONE);
            holder.paid_for.setText("Lab Test Booking");


            holder.text_view_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,AppointmentDetailsActivity.class);
                    intent.putExtra("status","H");
                    intent.putExtra("test",model.getTest());
                    intent.putExtra("lab_name",model.getBookedFor());
                    context.startActivity(intent);





                }
            });


        } else {
            holder.paid_for.setText("Doctor Appointment");
        }

        holder.dateTime.setText(model.getDate_time());
        holder.paid_to.setText(model.getBookedFor());





 /*       if (model.getPayment_status().equals("0")){

            if (model.isAll()) {


                Log.e("####", "0");


                holder.txn_id.setText(model.getTxn_id());
                holder.amt.setText(model.getAmount());
                if (model.getPayment_status().equals("0")) {
                    holder.status.setText("Pending");
                    holder.status.setTextColor(Color.parseColor("#fff29d13"));

                } else if (model.getPayment_status().equals("1")) {
                    holder.status.setText("Successful");
                    holder.status.setTextColor(Color.parseColor("#37b337"));
                } else {
                    holder.status.setText("Failure");
                    holder.status.setTextColor(Color.parseColor("#ff0000"));

                }

                if (model.getTrans_mode().equals("1")) {

                    holder.paid_for.setText("Lab Test Booking");
                } else {
                    holder.paid_for.setText("Doctor_Appointment");
                }

                holder.dateTime.setText(model.getDate_time());
                holder.paid_to.setText(model.getPaidTo());

            }else {

                holder.layoutHistory.setVisibility(View.GONE);
                holder.tv_not_found.setVisibility(View.VISIBLE);

            }

        }else if (model.getPayment_status().equals("1")) {

            if (model.isSuccess()) {


                holder.dateTime.setText(model.getDate_time());
                holder.paid_to.setText(model.getPaidTo());

                if (model.getTrans_mode().equals("1")) {

                    holder.paid_for.setText("Lab Test Booking");
                } else {
                    holder.paid_for.setText("Doctor_Appointment");
                }

                holder.status.setText("Failure");
                holder.status.setTextColor(Color.parseColor("#ff0000"));
                holder.txn_id.setText(model.getTxn_id());
                holder.amt.setText(model.getAmount());
            }else {

                holder.layoutHistory.setVisibility(View.GONE);
                holder.tv_not_found.setVisibility(View.VISIBLE);
            }
        }
            else if (model.getPayment_status().equals("2")) {
            if (model.isFail()) {


                holder.dateTime.setText(model.getDate_time());
                holder.paid_to.setText(model.getPaidTo());

                if (model.getTrans_mode().equals("1")) {

                    holder.paid_for.setText("Lab Test Booking");
                } else {
                    holder.paid_for.setText("Doctor_Appointment");
                }

                holder.status.setText("Successful");
                holder.status.setTextColor(Color.parseColor("#37b337"));
                holder.txn_id.setText(model.getTxn_id());
                holder.amt.setText(model.getAmount());
            }else {

                holder.layoutHistory.setVisibility(View.GONE);
                holder.tv_not_found.setVisibility(View.VISIBLE);
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }


    public void SetOnItemClickListener(
            final HistoryAdapter.OnItemClickListener itemClickListener) {
        this.clickListener =  itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


/*

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
*/



    class ViewHolder extends RecyclerView.ViewHolder/* implements
            View.OnClickListener */{
        TextView txn_id;
        TextView amt;
        TextView status;
        TextView dateTime;
        TextView paid_for;
        TextView paid_to;
        LinearLayout layoutHistory;
        TextView tv_not_found;
        TextView text_view_details;


        ViewHolder(View itemView) {
            super(itemView);

            txn_id = (TextView) itemView.findViewById(R.id.txt_txn_id);
            amt = (TextView) itemView.findViewById(R.id.txt_amount);
            status = (TextView) itemView.findViewById(R.id.txtv_status);
            dateTime = (TextView) itemView.findViewById(R.id.txt_date_time);
            paid_for = (TextView) itemView.findViewById(R.id.txt_paid_for);
            paid_to = (TextView) itemView.findViewById(R.id.txt_paid_to);
            tv_not_found = (TextView) itemView.findViewById(R.id.tv_not_found);
            text_view_details = (TextView) itemView.findViewById(R.id.text_view_details);
            layoutHistory=itemView.findViewById(R.id.layout_item_history);

            /*itemView.setOnClickListener(this);*/
        }

      /*  @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());

        }*/
    }


}
