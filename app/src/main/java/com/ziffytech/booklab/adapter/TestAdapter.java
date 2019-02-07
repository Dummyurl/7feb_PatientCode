package com.ziffytech.booklab.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ziffytech.R;
import com.ziffytech.booklab.models.TestModel;

import java.util.ArrayList;


public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolerModule> {


    private ArrayList<TestModel> arrayList;

    private Context context;


    public TestAdapter(ArrayList<TestModel> arrayList, Context context) {

        this.arrayList = arrayList;

        this.context = context;
        Log.e("Adapter","true");
    }


    @Override
    public TestAdapter.MyViewHolerModule onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext())

                .inflate(R.layout.row_test_item, parent, false);

        return new TestAdapter.MyViewHolerModule(layoutView, context, arrayList);
    }

    @Override
    public void onBindViewHolder(TestAdapter.MyViewHolerModule holder, final int position) {

        TestModel item = arrayList.get(position);
        holder.mTestcheck.setText(item.getTest_name());
        //   holder.tvPrice.setText(ConstValue.CURRENCY+item.getPrice());
        //  Log.e("PRICE",item.getPrice());

        holder.mTestcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {

                    arrayList.get(position).setChecked(true);
                    Log.e("##" + "Checked", position + "true");


                } else {

                    arrayList.get(position).setChecked(false);
                    Log.e("##" + "Checked", position + "false");


                }

            }
        });


    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static final class MyViewHolerModule extends RecyclerView.ViewHolder {

        CheckBox mTestcheck;
        TextView tvPrice;
        private ArrayList<TestModel> arrayList;
        private Context context;


        public MyViewHolerModule(View itemView, Context context, ArrayList<TestModel> arrayList) {

            super(itemView);
            this.context = context;
            mTestcheck = (CheckBox) itemView.findViewById(R.id.check_test);
            //   tvPrice = (TextView) itemView.findViewById(R.id.price);
        }

    }
}




