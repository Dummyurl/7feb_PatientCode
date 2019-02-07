package com.ziffytech.booklab.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziffytech.R;
import com.ziffytech.booklab.models.LabListModel;

import java.util.ArrayList;


public class LabListAdapter extends RecyclerView.Adapter<LabListAdapter.MyViewHolerModule> {


        private ArrayList<LabListModel> arrayList;

        private Context context;


        public LabListAdapter(ArrayList<LabListModel> arrayList, Context context) {

            this.arrayList = arrayList;

            this.context = context;
        }


        @Override
        public MyViewHolerModule onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext())

                    .inflate(R.layout.row_lablist_item, parent, false);

            return new MyViewHolerModule(layoutView, context, arrayList);
        }

        @Override
        public void onBindViewHolder(MyViewHolerModule holder, int position) {

            LabListModel item = arrayList.get(position);
            holder.mLabName.setText(item.getLab_name());

        }

        @Override
        public int getItemCount() {

            return arrayList.size();
        }

        public static final class MyViewHolerModule extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ArrayList<LabListModel> arrayList;

            private Context context;

            TextView mLabName;


            public MyViewHolerModule(View itemView, Context context, ArrayList<LabListModel> arrayList) {

                super(itemView);

                this.arrayList = arrayList;

                this.context = context;



                mLabName = (TextView) itemView.findViewById(R.id.lab_nametext);




                itemView.setOnClickListener(this);


            }

            @Override
            public void onClick(View v) {

                int position = getAdapterPosition();

                LabListModel labModel = this.arrayList.get(position);


            }
        }
    }

