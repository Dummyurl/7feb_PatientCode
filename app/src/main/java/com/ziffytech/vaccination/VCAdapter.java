package com.ziffytech.vaccination;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ziffytech.R;
import com.ziffytech.activities.LabListAdapter;
import com.ziffytech.vaccination.models.AgeVaccinationData;

import java.util.ArrayList;

public class VCAdapter extends RecyclerView.Adapter<VCAdapter.ViewHolder> implements AdapterView.OnItemClickListener {


    private ArrayList<AgeVaccinationData> ageVaccinationDataList;
    Context context;
    LabListAdapter.OnItemClickListener clickListener;
    String testIDs;

    public VCAdapter(Context context, ArrayList<AgeVaccinationData> ageVaccinationDataList) {
        this.context = context;
        this.ageVaccinationDataList = ageVaccinationDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vacci_details, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AgeVaccinationData ageVaccinationData = ageVaccinationDataList.get(position);
        holder.vcName.setText(ageVaccinationData.getVaccinationName());

        if (ageVaccinationData.getVaccinactionDetails().getDueOn() != null)
            holder.vcDueon.setText(ageVaccinationData.getVaccinactionDetails().getDueOn());
        else
            holder.vcDueon.setText("-");

        if (ageVaccinationData.getVaccinactionDetails().getGivenOn() != null)
            holder.vcGivenon.setText(ageVaccinationData.getVaccinactionDetails().getGivenOn());
        else
            holder.vcGivenon.setText("-");

        if (ageVaccinationData.getVaccinactionDetails().getWeight() != null)
            holder.vcWeight.setText(ageVaccinationData.getVaccinactionDetails().getWeight());
        else
            holder.vcWeight.setText("-");

        if (ageVaccinationData.getVaccinactionDetails().getBatch() != null)
            holder.vcBatch.setText(ageVaccinationData.getVaccinactionDetails().getBatch());
        else
            holder.vcBatch.setText("-");
        }

    @Override
    public int getItemCount() {
        return ageVaccinationDataList.size();
    }


    public void SetOnItemClickListener(
            final LabListAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView vcName;
        TextView vcDueon;
        TextView vcGivenon;
        TextView vcWeight;
        TextView vcBatch;

        ViewHolder(View itemView) {
            super(itemView);

            vcName = (TextView) itemView.findViewById(R.id.vc_name);
            vcDueon = (TextView) itemView.findViewById(R.id.vc_due_on_date);
            vcGivenon = (TextView) itemView.findViewById(R.id.vc_given_on_date);
            vcWeight = (TextView) itemView.findViewById(R.id.vc_weight);
            vcBatch = (TextView) itemView.findViewById(R.id.vc_batch);


        }


    }


}