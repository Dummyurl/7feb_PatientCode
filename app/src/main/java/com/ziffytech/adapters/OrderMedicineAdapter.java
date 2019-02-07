package com.ziffytech.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.ziffytech.R;
import com.ziffytech.activities.AdapterTestlistshowing;
import com.ziffytech.models.MedicalHistoryModel;
import com.ziffytech.models.MedicineOrderModel;

import java.util.ArrayList;

public class OrderMedicineAdapter extends RecyclerView.Adapter<OrderMedicineAdapter.ViewHolder> implements AdapterView.OnItemClickListener{

    private ArrayList<MedicineOrderModel> modelArrayList;
    Context context;
    AdapterTestlistshowing.OnItemClickListener clickListener;

    public OrderMedicineAdapter(Context context, ArrayList<MedicineOrderModel> names) {
        this.context=context;
        this.modelArrayList = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescribed_medicine, parent, false);
        return new OrderMedicineAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        MedicineOrderModel model=modelArrayList.get(i);

        viewHolder.checkBoxMedicine.setText(model.getMedicine_name());

        viewHolder.checkBoxMedicine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    viewHolder.edit_qty.setVisibility(View.VISIBLE);
                    viewHolder.edit_qty.setText(viewHolder.edit_qty.getText().toString());
                    modelArrayList.get(i).setChecked(true);
                    Log.e("##" + "Checked", i + "true");


                } else {

                    modelArrayList.get(i).setChecked(false);
                    Log.e("##" + "Checked", i + "false");


                }

            }
        });



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
        CheckBox checkBoxMedicine;
        EditText edit_qty;

        ViewHolder(View itemView) {
            super(itemView);


            checkBoxMedicine=itemView.findViewById(R.id.check_medicine);
            edit_qty=itemView.findViewById(R.id.edit_qty);
            edit_qty.setVisibility(View.GONE);


            itemView.setOnClickListener(this);
        }




        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());

        }
    }

  /*  public  void filterList(ArrayList<MedicalHistoryModel> filterdNames) {
        this.modelArrayList = filterdNames;
        notifyDataSetChanged();

        Log.e("FILTERED LIST",""+modelArrayList);
    }*/
}
