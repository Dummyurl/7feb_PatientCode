package com.ziffytech.thyrocare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziffytech.R;
import com.ziffytech.activities.LoginActivity;
import com.ziffytech.util.CommonClass;

import java.util.ArrayList;

public class ThrotimeslotAdapter extends RecyclerView.Adapter<ThrotimeslotAdapter.ViewHolder> implements AdapterView.OnItemClickListener{

    private ArrayList<TimeslotModelThyro> modelArrayList;
    Context context;
    OnItemClickListener clickListener;
    CommonClass common;
    String date;


    public ThrotimeslotAdapter(Context context, ArrayList<TimeslotModelThyro> names, String selectedDateStr) {
        this.context=context;
        this.modelArrayList = names;
        this.date = selectedDateStr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeslot_list, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        common = new CommonClass((Activity) context);

        final TimeslotModelThyro slotsModel = modelArrayList.get(position);

        if (slotsModel.getIs_booked())
        {
            // holder.itemView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.gray_stroke));
            holder.txtv_timeslot.setTextColor(context.getResources().getColor(R.color.grey));
            holder.clockimage.setImageResource(R.drawable.redclock);
        }else
        {
            //holder.itemView.setBackgroundColor(getResources().getColor(R.color.green));
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.gray_stroke1));
            holder.txtv_timeslot.setTextColor(context.getResources().getColor(R.color.black));
            holder.clockimage.setImageResource(R.drawable.greenclock);
        }

        holder.txtv_timeslot.setText(slotsModel.getSlot());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeslotModelThyro chooseMap = modelArrayList.get(position);
                if (!chooseMap.getIs_booked()) {
                    if (common.is_user_login())
                    {
                        Intent intent = new Intent(context, Ordersummarythyro.class);
                        intent.putExtra("timeslot",slotsModel.getSlot());
                        intent.putExtra("date",date);
                        context.startActivity(intent);


                    }else{
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                }else{
                    common.setToastMessage(context.getString(R.string.already_booked));
                }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

    }



    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        TextView txtv_timeslot;
        ImageView clockimage;


        ViewHolder(View itemView) {
            super(itemView);

            txtv_timeslot = (TextView) itemView.findViewById(R.id.txtv_timeslot);
            clockimage = (ImageView) itemView.findViewById(R.id.clockimage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());

        }
    }

}

