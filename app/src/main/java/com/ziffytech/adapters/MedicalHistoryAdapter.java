package com.ziffytech.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.TextView;

import com.ziffytech.R;
import com.ziffytech.activities.AdapterTestlistshowing;
import com.ziffytech.activities.Adaptertestlist;
import com.ziffytech.activities.Model;
import com.ziffytech.models.MedicalHistoryModel;

import java.util.ArrayList;

public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.ViewHolder> implements AdapterView.OnItemClickListener{

    private ArrayList<MedicalHistoryModel> modelArrayList;
    Context context;
    AdapterTestlistshowing.OnItemClickListener clickListener;

    public MedicalHistoryAdapter(Context context, ArrayList<MedicalHistoryModel> names) {
        this.context=context;
        this.modelArrayList = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_medicine, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        MedicalHistoryModel model=modelArrayList.get(position);

      /*  holder.textViewName.setText(model.getTest_name());
        // holder.text_price.setText(" Rs. "+model.getTest_price());

        modelArrayList.get(position).setIsClickable(true);
        Log.e("TESTNAME",model.getTest_name());
*/
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
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
            //   text_price = (TextView) itemView.findViewById(R.id.text_price);

            itemView.setOnClickListener(this);
        }




        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());

        }
    }

    public  void filterList(ArrayList<MedicalHistoryModel> filterdNames) {
        this.modelArrayList = filterdNames;
        notifyDataSetChanged();

        Log.e("FILTERED LIST",""+modelArrayList);
    }
}