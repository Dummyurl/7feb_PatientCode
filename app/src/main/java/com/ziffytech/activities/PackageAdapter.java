package com.ziffytech.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.models.PackageModel;

import java.util.ArrayList;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> implements AdapterView.OnItemClickListener{

    private ArrayList<PackageModel> modelArrayList;
    Context context;
    OnItemClickListener clickListener;

    public PackageAdapter(Context context, ArrayList<PackageModel> names) {
        this.context=context;
        this.modelArrayList = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        PackageModel model=modelArrayList.get(position);

        holder.textViewName.setText(model.getProf_name());
        Log.e("PACKAGE_NAME",model.getProf_name());

        holder.textPrice.setText(ConstValue.CURRENCY+" "+model.getTest_price());
        Log.e("PACKAGE_PRICE",model.getTest_price());

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }


    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
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
        TextView textPrice;

        ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.text_test_name);
            textPrice = (TextView) itemView.findViewById(R.id.text_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());

        }
    }

}