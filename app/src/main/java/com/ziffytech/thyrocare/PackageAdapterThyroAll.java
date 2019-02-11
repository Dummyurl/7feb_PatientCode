package com.ziffytech.thyrocare;

import android.app.Activity;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.CustomRequestForString;
import com.ziffytech.util.CommonClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PackageAdapterThyroAll extends RecyclerView.Adapter<PackageAdapterThyroAll.ViewHolder> implements AdapterView.OnItemClickListener{

    private ArrayList<TopfivepickModel> modelArrayList;
    Context context;
    OnItemClickListener clickListener;
    private CommonClass common;

    public PackageAdapterThyroAll(Activity context, ArrayList<TopfivepickModel> names) {
        this.context=context;
        this.modelArrayList = names;
        Log.e("Adapter",names.toString());
        common = new CommonClass(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_list2, parent, false);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder holder, int position)
    {

        final TopfivepickModel model=modelArrayList.get(position);

        Picasso.with(context).load(R.drawable.tyrocare).into(holder.imgv_pack_lab);

        holder.textViewName.setText(model.getName());
        Log.e("PACKAGE_NAME",model.getName());

        holder.textPrice.setText(ConstValue.CURRENCY+" "+model.getZiffy_profile_price());
        Log.e("PACKAGE_PRICE",model.getZiffy_profile_price());

        holder.package_test_count.setText("Includes "+model.getTest_count()+" tests.");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent myIntent = new Intent(context, PackageDetail.class);
                myIntent.putExtra("PROFILEID",model.getThyro_profile_id());
                context.startActivity(myIntent);
            }
        });

        holder.txtv_add_package.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String testid = model.getThyro_profile_id() ;
                String testcode = model.getTest_code() ;
                String userid =  common.getSession("user_id");

                Toast.makeText(context, ""+testid, Toast.LENGTH_SHORT).show();

                AddPacakge(testid,testcode,userid);

            }
        });


    }

    private void AddPacakge(String testid, String testcode, String userid)
    {
        HashMap<String, String> params2 = new HashMap<String, String>();
        params2.put("thyro_profile_id",testid);
        params2.put("test_code",testcode);
        params2.put("user_id",userid);
        CustomRequestForString customRequestForString2 = new CustomRequestForString(Request.Method.POST, ApiParams.ADDTOCART, params2, this.createRequestSuccessListenerThyropackadd(), this.createRequestErrorListenerThyropackadd());
        RequestQueue requestQueue2 = Volley.newRequestQueue(context);
        requestQueue2.add(customRequestForString2);

    }

    @Override
    public int getItemCount()
    {
        return modelArrayList.size();
    }


    public void SetOnItemClickListener
    (
        final OnItemClickListener itemClickListener)
    {
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
        TextView textViewName;
        TextView textPrice,package_test_count,txtv_add_package;
        ImageView imgv_pack_lab;

        ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.text_test_name);
            textPrice = (TextView) itemView.findViewById(R.id.text_price);
            imgv_pack_lab = (ImageView)itemView.findViewById(R.id.imgv_pack_lab);
            package_test_count = (TextView)itemView.findViewById(R.id.package_test_count);

            txtv_add_package = (TextView)itemView.findViewById(R.id.txtv_add_package);

           // itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            //clickListener.onItemClick(v, getPosition());



        }
    }

    public  void filterList(ArrayList<TopfivepickModel> modelArrayList) {
        this.modelArrayList = modelArrayList;
        notifyDataSetChanged();

        Log.e("FILTERED LIST",""+modelArrayList);
    }




    private Response.Listener<String> createRequestSuccessListenerThyropackadd()
    {
        return new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                Log.e("PACKAGE_RESPONSE_THYRO", response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("message");

                    if(result.equals("success"))
                    {
                        Toast.makeText(context, "Test Added To Cart", Toast.LENGTH_SHORT).show();
                        UpdateActivity();
                    }
                    else
                    {
                        Toast.makeText(context, "Please Try Again...", Toast.LENGTH_SHORT).show();
                    }

                    } catch (JSONException e)
                    {
                       e.printStackTrace();
                    }

            }
        };
    }

    private void UpdateActivity()
    {
        //context.finish();
        //context.startActivity(context.getIntent());

        ((Activity)context).finish();
        Intent intent = new Intent(context, Allthyropackageslisting.class);
        context.startActivity(intent);


    }

    private Response.ErrorListener createRequestErrorListenerThyropackadd()
    {
        return new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("##", "##" + error.toString());
            }
        };
    }

}