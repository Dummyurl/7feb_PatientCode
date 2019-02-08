
package com.ziffytech.thyrocare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ziffytech.R;

import java.util.ArrayList;

public class TestDetailadpater extends RecyclerView.Adapter<TestDetailadpater.ViewHolder> implements AdapterView.OnItemClickListener{

    private ArrayList<PackageDetailModel> modelArrayList;
    Context context;
    OnItemClickListener clickListener;

    public TestDetailadpater(Context context, ArrayList<PackageDetailModel> names) {
        this.context=context;
        this.modelArrayList = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        PackageDetailModel model =modelArrayList.get(position);
        holder.txtv_detail_test_name.setText(model.getTest_name_detail());
//        Log.e("PACKAGE_NAME",model.getTest_name_detail());

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
        TextView txtv_detail_test_name;


        ViewHolder(View itemView) {
            super(itemView);

            txtv_detail_test_name = (TextView) itemView.findViewById(R.id.txtv_detail_test_name);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());

        }
    }

}
