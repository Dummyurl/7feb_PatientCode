package com.ziffytech.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.ZiffyLabBooking;
import com.ziffytech.models.TopfivepickModel;

import java.util.ArrayList;

public class ThyroPackageAdapter extends RecyclerView.Adapter<ThyroPackageAdapter.ViewHolder> implements AdapterView.OnItemClickListener{

    private ArrayList<TopfivepickModel> modelArrayList;
    Context context;
    OnItemClickListener clickListener;

    public ThyroPackageAdapter(Context context, ArrayList<TopfivepickModel> names) {
        this.context=context;
        this.modelArrayList = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thyro, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        final TopfivepickModel model =modelArrayList.get(position);

        Picasso.with(context).load(R.drawable.tyrocare).into(holder.imgv_pack_lab);

        holder.textViewName.setText(model.getName());
        Log.e("PACKAGE_NAME",model.getName());

        holder.textPrice.setText(ConstValue.CURRENCY+" "+model.getZiffy_profile_price());
        Log.e("PACKAGE_PRICE",model.getZiffy_profile_price());

        holder.package_test_count.setText("Includes "+model.getTest_count()+" tests.");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(context, ZiffyLabBooking.class);
                myIntent.putExtra("PROFILEID",model.getThyro_profile_id());
                context.startActivity(myIntent);

            }
        });

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
        TextView textPrice,package_test_count;
        ImageView imgv_pack_lab;

        ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.text_test_name);
            textPrice = (TextView) itemView.findViewById(R.id.text_price);
            imgv_pack_lab = (ImageView)itemView.findViewById(R.id.imgv_pack_lab);
            package_test_count = (TextView)itemView.findViewById(R.id.package_test_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());

        }
    }

}