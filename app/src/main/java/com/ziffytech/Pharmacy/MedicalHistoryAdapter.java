package com.ziffytech.Pharmacy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.activities.AdapterTestlistshowing;
import com.ziffytech.activities.MasterWebView;

import java.util.ArrayList;

public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.ViewHolder> implements AdapterView.OnItemClickListener {

    private ArrayList<MedicalHistoryModel> modelArrayList;
    Context context;
    AdapterTestlistshowing.OnItemClickListener clickListener;

    public MedicalHistoryAdapter(Context context, ArrayList<MedicalHistoryModel> names) {
        this.context = context;
        this.modelArrayList = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final MedicalHistoryModel model = modelArrayList.get(position);

        holder.clinicName.setText(model.getClinic_name());
        holder.doctorName.setText(model.getDoctor_name());
        holder.date_time.setText(model.getDate());

        Log.e("clinicName", model.getClinic_name());
        Log.e("doctorName", model.getDoctor_name());
        Log.e("date_time", model.getDate());




        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(context, MasterWebView.class);
                i.putExtra("link", ApiParams.GET_PRESCRIPTION_WEBVIEW+model.getPres_id());
                i.putExtra("title", "Prescription");
                Log.e("link", ApiParams.GET_PRESCRIPTION_WEBVIEW+model.getPres_id());
                Log.e("title", "Prescription");
                context.startActivity(i);
            }
        });

        holder.btnOrderMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent intent=new Intent(context, OrderPrescribedMedicine.class);
              intent.putExtra("presc_id", model.getPres_id());
              intent.putExtra("title","Prescription");
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        TextView clinicName;
        TextView doctorName;
        TextView date_time;
        Button btnView;
        Button btnOrderMedicine;

        ViewHolder(View itemView) {
            super(itemView);

            clinicName = (TextView) itemView.findViewById(R.id.clinic_name);
            doctorName = (TextView) itemView.findViewById(R.id.doctor_name);
            date_time = (TextView) itemView.findViewById(R.id.date);
            btnView = (Button) itemView.findViewById(R.id.btn_view);
            btnOrderMedicine = (Button) itemView.findViewById(R.id.btn_order);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());

        }
    }

    public void filterList(ArrayList<MedicalHistoryModel> filterdNames) {
        this.modelArrayList = filterdNames;
        notifyDataSetChanged();

        Log.e("FILTERED LIST", "" + modelArrayList);
    }
}