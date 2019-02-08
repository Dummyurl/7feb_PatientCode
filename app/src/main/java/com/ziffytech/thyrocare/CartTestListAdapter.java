
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.activities.CustomRequestForString;
import com.ziffytech.util.CommonClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CartTestListAdapter extends RecyclerView.Adapter<CartTestListAdapter.ViewHolder> implements AdapterView.OnItemClickListener
{

    private ArrayList<BillingDetailModel> modelArrayList;
    Context context;
    OnItemClickListener clickListener;
    private CommonClass common;
    BillingDetailModel model;

    public CartTestListAdapter(Activity context, ArrayList<BillingDetailModel> names)
    {
        this.context=context;
        this.modelArrayList = names;
        common = new CommonClass(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        model = modelArrayList.get(position);
        holder.carttestname.setText(model.getName());
        holder.carttestprice.setText(model.getZiffy_profile_price());

        holder.remove_cart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String testid = model.getThyro_profile_id() ;
                String testcode = model.getTest_code() ;
                String userid =  common.getSession("user_id");
                Toast.makeText(context, ""+testid, Toast.LENGTH_SHORT).show();
                Removepack(testid,testcode,userid,position);
            }
        });
    }

    private void Removepack(String testid, String testcode, String userid, int position)
    {
        HashMap<String, String> params2 = new HashMap<String, String>();
        params2.put("thyro_profile_id",testid);
        params2.put("user_id",userid);
        CustomRequestForString customRequestForString2 = new CustomRequestForString(Request.Method.POST, ApiParams.REMOVETOCART, params2, this.createRequestSuccessListenerThyropackadd(), this.createRequestErrorListenerThyropackadd());
        RequestQueue requestQueue2 = Volley.newRequestQueue(context);
        requestQueue2.add(customRequestForString2);
    }

    @Override
    public int getItemCount()
    {
        return modelArrayList.size();
    }


    public void SetOnItemClickListener( final OnItemClickListener itemClickListener)
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView carttestname,carttestprice,remove_cart;

        ViewHolder(View itemView)
        {
            super(itemView);

            carttestname = (TextView) itemView.findViewById(R.id.carttestname);
            carttestprice = (TextView) itemView.findViewById(R.id.carttestprice);
            remove_cart = (TextView)itemView.findViewById(R.id.remove_cart);

            //itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            clickListener.onItemClick(v, getPosition());
        }
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
                        Toast.makeText(context, "Test Removed From Cart", Toast.LENGTH_SHORT).show();

                        //UpdateActivity();

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
            Intent intent = new Intent(context, Ordersummarythyro.class);
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

