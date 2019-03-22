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
import android.widget.Toast;


import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.util.MyUtility;


import java.util.ArrayList;

public class OrderMedicineAdapter extends RecyclerView.Adapter<OrderMedicineAdapter.ViewHolder> {

    private ArrayList<MedicineOrderModel> modelArrayList;
    private ArrayList<MedicineOrderModel> selectedList;
    Context context;



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

      //  holder.checkBoxMedicine.setText(model.getMedicine_name()+"("+ ConstValue.CURRENCY"+")");

        Log.e("name", model.getDrug_name());
        Log.e("qty", model.getQuantity());


        if (model.getAvailability().equals("0")) {

            holder.checkBoxMedicine.setText(model.getDrug_name());
            holder.text_qty.setText(model.getQuantity());


            holder.text_not_available.setVisibility(View.VISIBLE);

            Log.e("availabililty", model.getAvailability());
            holder.checkBoxMedicine.setEnabled(false);
            holder.checkBoxMedicine.setTextColor(Color.parseColor("#787878"));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "Medicine not available.", Toast.LENGTH_SHORT).show();
                }
            });

//          holder.checkBoxMedicine.setEnabled(false);
//          holder.checkBoxMedicine.setTextColor(Color.parseColor("#787878"));
        } else {


            holder.checkBoxMedicine.setText(model.getDrug_name()+"("+ConstValue.CURRENCY+model.getMedicine_price()+")");
            holder.text_qty.setText(model.getQuantity());

            holder.text_not_available.setVisibility(View.GONE);

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

                    int length=s.length();

                    if (length==0){

                        MyUtility.showAlertMessage(context,"Please enter quantity.");
                        holder.checkBoxMedicine.setChecked(false);
                        holder.edit_qty.setText(model.getQuantity());
                        holder.text_qty.setText(modelArrayList.get(position).getQuantity());
                    }else {

                        modelArrayList.get(position).setQuantity(String.valueOf(holder.edit_qty.getText()));
                        Log.e("##" + "Quantity", position + modelArrayList.get(position).getQuantity());
                    }




                 /*   if (!s.equals("")){

                        modelArrayList.get(position).setQuantity(String.valueOf(holder.edit_qty.getText()));
                        Log.e("##" + "Quantity", position + modelArrayList.get(position).getQuantity());
                    }else {

                        Log.e("EDit","true");
                        MyUtility.showAlertMessage(context,"###Please enter quantity.");

                        holder.checkBoxMedicine.setChecked(false);


                    }*/


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




    class ViewHolder extends RecyclerView.ViewHolder  {
        CheckBox checkBoxMedicine;
        EditText edit_qty;
        TextView text_qty;
        TextView text_not_available;

        ViewHolder(View itemView) {
            super(itemView);

            checkBoxMedicine = itemView.findViewById(R.id.check_medicine);
            edit_qty = itemView.findViewById(R.id.edit_qty);
            text_qty = itemView.findViewById(R.id.text_qty);
            text_not_available = itemView.findViewById(R.id.text_not_available);
            edit_qty.setVisibility(View.GONE);


        }


    }


}