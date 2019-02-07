package com.ziffytech.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.LabModel;
import com.ziffytech.activities.LabViewDetailActivity;

import java.util.ArrayList;

public class PackageLabListAdapter extends RecyclerView.Adapter<PackageLabListAdapter.ViewHolder> {

    Context context;
    OnItemClickListener clickListener;
    String testIDs;
    private ArrayList<LabModel> modelArrayList;

    public PackageLabListAdapter(Context context, ArrayList<LabModel> names, String tests) {
        this.context = context;
        this.modelArrayList = names;
        this.testIDs = tests;
        Log.e("LABLISTADAPTER", "###" + testIDs);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_lab_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final LabModel model = modelArrayList.get(position);
        Picasso.with(context).load(ConstValue.BASE_URL + "uploads/lab/" + model.getLab_photo()).into(holder.mLabImage);
        holder.mLabName.setText(model.getLab_name());
        holder.mLabAddress.setText("Address : " + model.getLab_address());
        holder.mOpeningTime.setText(model.getLab_opening_time() + " - " + model.getLab_closing_time());


        holder.mBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!model.getLab_ip().equals("")) {
                    Intent lIntent = new Intent(context, LabViewDetailActivity.class);
                    lIntent.putExtra("lab_id", model.getLab_id());
                    lIntent.putExtra("lab_name", model.getLab_name());
                    lIntent.putExtra("test", testIDs);
                    lIntent.putExtra("flag", "1");
                    lIntent.putExtra("status", "0");
                    Log.e("####", "TESTLIST ADAPTER" + testIDs);

                    context.startActivity(lIntent);
                } else {

                    Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
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
        this.clickListener = itemClickListener;
    }

    public void filterList(ArrayList<LabModel> filterdNames) {
        this.modelArrayList = filterdNames;
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mLabImage;
        Button mBookButton;

        TextView mLabAddress;
        TextView mOpeningTime;
        TextView mClosingTime;
        TextView mLabName;

        ViewHolder(View itemView) {
            super(itemView);

            mLabImage = (ImageView) itemView.findViewById(R.id.lab_image);
            mLabName = (TextView) itemView.findViewById(R.id.lab_name);
            mLabAddress = (TextView) itemView.findViewById(R.id.LabAddress);
            mOpeningTime = (TextView) itemView.findViewById(R.id.Opening_time);
            mClosingTime = (TextView) itemView.findViewById(R.id.Closing_time);
            mBookButton = (Button) itemView.findViewById(R.id.book);


        }


    }
}