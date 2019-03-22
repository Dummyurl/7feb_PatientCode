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

import com.ziffytech.R;
import com.ziffytech.activities.AdapterTestlistshowing;

import java.util.ArrayList;

public class MedicalHistoryfflineDetailsAdapter extends RecyclerView.Adapter<MedicalHistoryfflineDetailsAdapter.ViewHolder> {

    private ArrayList<OfflineModel> modelArrayList;
    Context context;
    OnItemClickListener clickListener;

    public MedicalHistoryfflineDetailsAdapter(Context context, ArrayList<OfflineModel> names) {
        this.context = context;
        this.modelArrayList = names;
    }

    @Override
    public MedicalHistoryfflineDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_med_history_offline, parent, false);
        return new MedicalHistoryfflineDetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MedicalHistoryfflineDetailsAdapter.ViewHolder holder, int position) {

        final OfflineModel model = modelArrayList.get(position);


        holder.date_time.setText(model.getDate_time());

        holder.text_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,ViewPrescription.class);
                intent.putExtra("presc_img",model.getPres_img());
                context.startActivity(intent);
            }
        });

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });





    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }


    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }




    class ViewHolder extends RecyclerView.ViewHolder {

        TextView date_time;
        TextView text_view_details;
        Button btn_cancel;


        ViewHolder(View itemView) {
            super(itemView);


            date_time = (TextView) itemView.findViewById(R.id.txt_date_time);
            text_view_details = (TextView) itemView.findViewById(R.id.text_view_details);
            btn_cancel = (Button) itemView.findViewById(R.id.btn_cancel);



        }


    }

    public void filterList(ArrayList<OfflineModel> filterdNames) {
        this.modelArrayList = filterdNames;
        notifyDataSetChanged();

        Log.e("FILTERED LIST", "" + modelArrayList);
    }
}
