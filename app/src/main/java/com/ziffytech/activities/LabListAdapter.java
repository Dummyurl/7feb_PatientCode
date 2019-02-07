package com.ziffytech.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LabListAdapter extends RecyclerView.Adapter<LabListAdapter.ViewHolder> implements AdapterView.OnItemClickListener{

    private ArrayList<LabModel> modelArrayList;
    Context context;
    OnItemClickListener clickListener;
    String testIDs;
    TextView text_test_number,text_total_price,btn_ok;
    LinearLayout layout_selected_test;
    String totalP,priceTotal;
    String location;


    public LabListAdapter(Context context, ArrayList<LabModel> names, String tests,String address)  {
        this.context=context;
        this.modelArrayList = names;
        this.testIDs=tests;
        this.location=address;
        Log.e("LABLISTADAPTER","###"+testIDs);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_listdetail_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {



        final LabModel model=modelArrayList.get(position);
        Picasso.with(context).load(ConstValue.BASE_URL+"uploads/lab/"+model.getLab_photo()).into(holder.mLabImage);
        holder.mLabName.setText(model.getLab_name());
        holder.mLabAddress.setText("Address : "+model.getLab_address());
        holder.mOpeningTime.setText(model.getLab_opening_time()+" - " +model.getLab_closing_time());

        double total=0;
        double price=0;

        try {
            JSONArray jsonArray=new JSONArray(model.getTest_details());

            for (int i = 0; i <jsonArray.length() ; i++) {
                Log.e("Adp details","true");
                Log.e("Test details",model.getTest_details());

                JSONObject object=jsonArray.getJSONObject(i);
                price= Double.parseDouble(object.getString("test_price"));
                total=total+price;
            }

            priceTotal= String.valueOf(total);
            holder.mPrice.setText(ConstValue.CURRENCY+ " "+priceTotal);
            Log.e("###ADP_TOTAL", String.valueOf(priceTotal));







        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.mPricebreakup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final MaterialDialog dialog1 = new MaterialDialog.Builder(context)
                        .customView(R.layout.item_price_breakup, false)
                        .show();

                View view1 = dialog1.getView();

                text_test_number=view1.findViewById(R.id.text_test_number);
                layout_selected_test=view1.findViewById(R.id.layout_test);
                text_total_price=view1.findViewById(R.id.text_total_price);
                btn_ok=view1.findViewById(R.id.btn_ok);

                double total=0;
                try {
                    JSONArray jsonArray=new JSONArray(model.getTest_details());

                    Log.e("SELECTED TEST LAB",model.getTest_details());

                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject object=jsonArray.getJSONObject(i);


                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        params.setMargins(1, 5, 1, 5);
                        params.weight = 1f;
                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        params2.setMargins(10, 5, 1, 5);
                        params2.weight = 0.6f;

                        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        params3.setMargins(1, 5, 1, 5);
                        params2.weight = 0.4f;

                        params3.gravity = View.TEXT_ALIGNMENT_VIEW_END;

                        LinearLayout linearLayout1 = new LinearLayout(context);
                        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout1.setLayoutParams(params);

                        TextView name = new TextView(context);
                        TextView price = new TextView(context);
                        name.setLayoutParams(params2);
                        price.setLayoutParams(params3);


                        name.setTextColor(Color.DKGRAY);
                        //name.setBackgroundResource(R.color.white);
                        name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(55)});
                        name.setPadding(10, 5, 5, 5);
                        name.setText(object.getString("test_name"));
                        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

                        price.setTextColor(Color.DKGRAY);
                        //  price.setBackgroundResource(R.color.white);
                        price.setFilters(new InputFilter[]{new InputFilter.LengthFilter(55)});
                        price.setPadding(10, 5, 10, 5);
                        price.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
                        price.setText(ConstValue.CURRENCY +" "+ object.getString("test_price"));
                        price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

                        linearLayout1.addView(name);
                        linearLayout1.addView(price);
                        layout_selected_test.addView(linearLayout1, params);
                        if (jsonArray.length()==1){

                            text_test_number.setText(String.valueOf(jsonArray.length()+" "+"test selected"));
                        }else {

                            text_test_number.setText(String.valueOf(jsonArray.length()+" "+"tests selected"));
                        }


                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });
                        double price1= Double.parseDouble(object.getString("test_price"));
                        total=total+price1;
                    }

                    totalP=String.valueOf(total);
                    text_total_price.setText(ConstValue.CURRENCY+""+totalP);
                    Log.e("TOTAL", String.valueOf(totalP));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

        holder.mBookButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                if (!model.getLab_ip().equals(""))
                {
                    Intent lIntent=new Intent(context,LabViewDetailActivity.class);
                    lIntent.putExtra("lab_id",model.getLab_id());
                    lIntent.putExtra("lab_name",model.getLab_name());
                    lIntent.putExtra("test",testIDs);
                    lIntent.putExtra("flag", "0");

                    lIntent.putExtra("address",location);
                    Log.e("####", "TESTLIST ADAPTER"+testIDs);
                    Log.e("####", "ADDRESS"+location);
                    Log.e("####", "lab_id"+model.getLab_id());
                    Log.e("####", "lab_name"+model.getLab_name());


                    if (testIDs.contains("Activity")){
                        lIntent.putExtra("Activity","R");
                        lIntent.putExtra("total",holder.mPrice.getText().toString());
                        Log.e("####", "total"+holder.mPrice.getText().toString());
                    }else {
                        lIntent.putExtra("total",holder.mPrice.getText().toString());
                        Log.e("####", "total"+holder.mPrice.getText().toString());
                    }
                    context.startActivity(lIntent);
                }else
                {

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
        this.clickListener =  itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }



    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mLabImage;
        Button mBookButton;

        TextView mLabAddress;
        TextView mOpeningTime;
        TextView mClosingTime;
        TextView mLabName;
        TextView mPrice;
        TextView mPricebreakup;

        ViewHolder(View itemView) {
            super(itemView);

            mLabImage = (ImageView) itemView.findViewById(R.id.lab_image);
            mLabName = (TextView) itemView.findViewById(R.id.lab_name);
            mLabAddress = (TextView) itemView.findViewById(R.id.LabAddress);
            mOpeningTime = (TextView) itemView.findViewById(R.id.Opening_time);
            mClosingTime = (TextView) itemView.findViewById(R.id.Closing_time);
            mBookButton = (Button) itemView.findViewById(R.id.book);
            mPrice = (TextView) itemView.findViewById(R.id.text_total);
            mPricebreakup = (TextView) itemView.findViewById(R.id.text_price_breakup);



        }


    }

    public  void filterList(ArrayList<LabModel> filterdNames) {
        this.modelArrayList = filterdNames;
        notifyDataSetChanged();
    }
}