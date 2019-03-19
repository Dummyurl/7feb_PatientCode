package com.ziffytech.Pharmacy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;


import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.AdapterTestlistshowing;
import com.ziffytech.activities.Model;
import com.ziffytech.util.MyUtility;

import java.util.ArrayList;

public class OrderMedicineAdapter extends RecyclerView.Adapter<OrderMedicineAdapter.ViewHolder> {

    private ArrayList<MedicineOrderModel> modelArrayList;
    private ArrayList<MedicineOrderModel> selectedList;
    Context context;
    OnItemClickListener clickListener;


    public OrderMedicineAdapter(Context context, ArrayList<MedicineOrderModel> names) {
        this.context = context;
        this.modelArrayList = names;
        Log.e("modelArrayList", modelArrayList.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescribed_medicine, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final MedicineOrderModel model = modelArrayList.get(position);

        Log.e("model", String.valueOf(model));

      //  holder.checkBoxMedicine.setText(model.getMedicine_name()+"("+ ConstValue.CURRENCY+model.getQty()+")");
        holder.text_qty.setText(model.getQuantity());
        holder.checkBoxMedicine.setText(model.getDrug_name());

        Log.e("name", model.getDrug_name());
        Log.e("qty", model.getQuantity());


        if (model.getAvailability().equals("0")) {

            Log.e("availabililty", model.getAvailability());
            holder.checkBoxMedicine.setEnabled(false);
            holder.checkBoxMedicine.setTextColor(Color.parseColor("#787878"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyUtility.showAlertMessage(context, "Medicine is not available for now");

                }
            });


//          holder.checkBoxMedicine.setEnabled(false);
//          holder.checkBoxMedicine.setTextColor(Color.parseColor("#787878"));
        } else {

            holder.checkBoxMedicine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b) {
                        holder.text_qty.setVisibility(View.GONE);
                        holder.edit_qty.setVisibility(View.VISIBLE);
                        holder.edit_qty.setText(model.getQuantity());
                        modelArrayList.get(position).setChecked(true);

                        Log.e("##" + "Quantity", position + modelArrayList.get(position).getQuantity());
                        Log.e("##" + "Checked", position + "true");


                    } else {

                        holder.text_qty.setVisibility(View.VISIBLE);
                        holder.text_qty.setText(model.getQuantity());
                        modelArrayList.get(position).setChecked(false);
                        holder.edit_qty.setVisibility(View.GONE);
                        Log.e("##" + "Checked", position + "false");


                    }
                }
            });


            holder.edit_qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    modelArrayList.get(position).setQuantity(String.valueOf(holder.edit_qty.getText()));
                    Log.e("##" + "Quantity", position + modelArrayList.get(position).getQuantity());

                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }



    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }




    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        CheckBox checkBoxMedicine;
        EditText edit_qty;
        TextView text_qty;

        ViewHolder(View itemView) {
            super(itemView);

            checkBoxMedicine = itemView.findViewById(R.id.check_medicine);
            edit_qty = itemView.findViewById(R.id.edit_qty);
            text_qty = itemView.findViewById(R.id.text_qty);
            edit_qty.setVisibility(View.GONE);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());

        }
    }


}