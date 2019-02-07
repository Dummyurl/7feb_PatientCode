package com.ziffytech.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.AdapterTestlistshowing;
import com.ziffytech.activities.MasterWebView;
import com.ziffytech.activities.MedicalRecords;
import com.ziffytech.activities.Model;
import com.ziffytech.booklab.RecommmendedTest;
import com.ziffytech.models.TestResultModel;

import java.util.ArrayList;

public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.ViewHolder>{


    private ArrayList<TestResultModel> modelArrayList;
    Context context;
    String patient_id;
    String test_name;


    public TestResultAdapter(Context context, ArrayList<TestResultModel> names,String patient_id) {
        this.context=context;
        this.modelArrayList = names;
        this.patient_id = patient_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_result, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final TestResultModel testResultModel = modelArrayList.get(position);

        Log.e("Position :", String.valueOf(position));


        if (position == 0) {

            holder.row1.setVisibility(View.VISIBLE);
        } else {
            holder.row1.setVisibility(View.GONE);
        }


        Log.e("observation text", testResultModel.getObservation_Text());
        Log.e("observation value", testResultModel.getObservation_value());
        Log.e("Result_unit_reference_range value", testResultModel.getResult_unit_reference_range());

        if (testResultModel.getAbnormal_flags().equals("H")){

            holder.itemView.setBackgroundColor(Color.parseColor("#ffb3b3"));
        }else if (testResultModel.getAbnormal_flags().equals("N")){
            holder.itemView.setBackgroundColor(Color.parseColor("#b3ffb3"));

        }else if (testResultModel.getAbnormal_flags().equals("-")){
            holder.itemView.setBackgroundColor(Color.parseColor("#fff0b3"));

        }


        holder.parameter.setText(testResultModel.getObservation_Text());
        holder.observation.setText(testResultModel.getObservation_value());

        holder.range.setText(testResultModel.getResult_unit_reference_range());



    /*    if (testResultModel.getObservation_Text().contains(" ")){

           test_name= testResultModel.getObservation_Text().replaceAll(" ","%");
        }
*/


        holder.row2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, MasterWebView.class);
                i.putExtra("title", "Test Result");
                i.putExtra("link", ConstValue.BASE_URL + "doctor/report_grpah_api/"+patient_id+"/"+testResultModel.getObservation_Test_id()+"/"+testResultModel.getObservation_Text());
                Log.e("Link",ConstValue.BASE_URL + "doctor/report_grpah_api/"+patient_id+"/"+testResultModel.getObservation_Test_id()+"/"+testResultModel.getObservation_Text());
                context.startActivity(i);
            }
        });



      /*  final TestResultModel model=modelArrayList.get(position);


        if (position==0){

            holder.row1.setVisibility(View.VISIBLE);
            }else {
            holder.row1.setVisibility(View.GONE);
        }


        Log.e("###"+position,modelArrayList.get(position).toString());

        holder.parameter.setText(model.getObservation_Text());
        holder.observation.setText(model.getObservation_value());
        holder.range.setText(model.getEffective_date_of_last_normal_observation());


      Log.e("parameter",model.getObservation_Text());
      Log.e("observation",model.getObservation_value());
      Log.e("range",model.getEffective_date_of_last_normal_observation());


      if (model.getObservation_Text().contains(" ")){

         test_name= model.getObservation_Text().replaceAll(" ","%");
         Log.e("test_name",test_name);
      }



      holder.row2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              Intent i = new Intent(context, MasterWebView.class);
              i.putExtra("title", "Test Result");
              i.putExtra("link", ConstValue.BASE_URL + "doctor/report_grpah_api/"+patient_id+modelArrayList.get(position).getObservation_Test_id()+modelArrayList.get(position).getObservation_Text());
              context.startActivity(i);
          }
      });




       *//* holder.textViewName.setText(model.getTest_name());
        holder.text_price.setText(" Rs. "+model.getTest_price());

        modelArrayList.get(position).setIsClickable(true);
        Log.e("TESTNAME",model.getTest_name());*/
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }





    class ViewHolder extends RecyclerView.ViewHolder {
        TextView parameter;
        TextView observation;
        TextView range;
        LinearLayout row1,row2;
        WebView webView;


        ViewHolder(View itemView) {
            super(itemView);


            row1 = (LinearLayout) itemView.findViewById(R.id.row1);
            row2 = (LinearLayout) itemView.findViewById(R.id.row_2);
            parameter = (TextView) itemView.findViewById(R.id.parameter);
            observation = (TextView) itemView.findViewById(R.id.observation);
            range = (TextView) itemView.findViewById(R.id.range);
            webView=itemView.findViewById(R.id.webView);


        }


    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.endsWith(".pdf"))
            {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                // if want to download pdf manually create AsyncTask here
                // and download file
                return true;

            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }
    }


  /*  public  void filterList(ArrayList<TestResultModel> filterdNames) {
        this.modelArrayList = filterdNames;
        notifyDataSetChanged();

        Log.e("FILTERED LIST",""+modelArrayList);
    }*/
}