package com.ziffytech.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppointmentDetailsActivity extends CommonActivity {
    RecyclerView recyclerViewDetails;
    String test_names, lab_name, date, time;
    TextView labName, text_date, text_time, text_test;
    Button btnOk;
    TextView tax, total_price, final_total, clinic_name;
    ArrayList<String> arrayList;
    LinearLayoutManager layoutManager;
    LinearLayout linearLayout, layout, layout_payment_mode, layout_time, layout_date, layout_test_history, layout_all, layout_details;
    LinearLayout layoutTest, layout_payment;
    View view;
    boolean isRadioPayment, isChecked = false;
    TextView payment_mode;
    Button btnInvoice;
    LinearLayout layout_package;
    TextView text_package;
    TextView package_price;
    TextView tv_test;
    LinearLayout layout_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appointment_details);
        setHeaderTitle("Details");
        allowBack();
        setupViews();

        final_total.setText(ConstValue.CURRENCY+ getIntent().getStringExtra("final_total"));



        if (getIntent().getStringExtra("status").equals("H")) {

            Log.e("test History", getIntent().getStringExtra("test"));
            test_names = getIntent().getStringExtra("test");


            layout_time.setVisibility(View.GONE);
            layout_date.setVisibility(View.GONE);
            layout_payment.setVisibility(View.GONE);
            layoutTest.setVisibility(View.GONE);
            labName.setVisibility(View.GONE);
            layout_details.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);

            layout_history.setVisibility(View.VISIBLE);


            try {
                JSONArray jsonArray = new JSONArray(test_names);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Log.e("object", jsonObject.getString("testName"));


                    TextView textView = new TextView(AppointmentDetailsActivity.this);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                    params.setMargins(1, 5, 1, 5);
                    textView.setLayoutParams(params);

                    //textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                    textView.setTextColor(Color.DKGRAY);
                    textView.setBackgroundResource(R.color.white);
                    textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(55)});
                    textView.setPadding(10, 10, 10, 10);
                    textView.setText(jsonObject.getString("testName"));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                    layout_test_history.addView(textView);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (getIntent().getStringExtra("status").equals("1")) {
            Log.e("####", "TRUE");
            //    layout_payment_mode.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);

            layoutTest.setVisibility(View.GONE);
            clinic_name.setVisibility(View.VISIBLE);


            labName.setText(getIntent().getStringExtra("doct_name"));
            clinic_name.setText(getIntent().getStringExtra("clinic_name"));
            time = getIntent().getStringExtra("start_time");
            date = getIntent().getStringExtra("appointment_date");

            text_date.setText(getIntent().getStringExtra("appointment_date"));
            text_time.setText(getIntent().getStringExtra("start_time"));


            Log.e("####", getIntent().getStringExtra("start_time") + getIntent().getStringExtra("appointment_date"));

            //tax.setText(" Rs. " + getIntent().getStringExtra("tax"));
            total_price.setText(getIntent().getStringExtra("final_total"));
            final_total.setText(getIntent().getStringExtra("final_total"));

        } else if (getIntent().getStringExtra("status").equals("3")){


            layoutTest.setVisibility(View.VISIBLE);

            clinic_name.setVisibility(View.GONE);

            test_names = getIntent().getStringExtra("test_names");
            lab_name = getIntent().getStringExtra("lab_name");
            time = getIntent().getStringExtra("start_time");
            date = getIntent().getStringExtra("appointment_date");



            try {
                test_names = getIntent().getStringExtra("test_names");
                Log.e("#####", "TRY ADAPTER_Test1");
                JSONArray jsonArray = new JSONArray(test_names);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

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

                    LinearLayout linearLayout1 = new LinearLayout(AppointmentDetailsActivity.this);
                    linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout1.setLayoutParams(params);

                    TextView name = new TextView(AppointmentDetailsActivity.this);
//                    TextView price = new TextView(AppointmentDetailsActivity.this);
                    name.setLayoutParams(params2);
//                    price.setLayoutParams(params3);


                    name.setTextColor(Color.DKGRAY);
                    //name.setBackgroundResource(R.color.white);
                    name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(55)});
                    name.setPadding(10, 5, 5, 5);

                    name.setText(jsonObject.getString("name"));
                    name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

                /*    price.setTextColor(Color.DKGRAY);
                    //  price.setBackgroundResource(R.color.white);
                    price.setFilters(new InputFilter[]{new InputFilter.LengthFilter(55)});
                    price.setPadding(10, 5, 10, 5);
                    price.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
                    price.setText(ConstValue.CURRENCY + jsonObject.getString("price"));
                    price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);*/
                    linearLayout1.addView(name);
//                    linearLayout1.addView(price);

                    layout.addView(linearLayout1, params);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }




        }else {


            layoutTest.setVisibility(View.VISIBLE);

            clinic_name.setVisibility(View.GONE);

            test_names = getIntent().getStringExtra("test_names");

            Log.e("TEST NAMES", test_names);
            lab_name = getIntent().getStringExtra("lab_name");
            time = getIntent().getStringExtra("start_time");
            date = getIntent().getStringExtra("appointment_date");
            Log.e("FINAL TOTAL", getIntent().getStringExtra("final_total"));


            if (test_names.contains("package_name")) {
                text_test.setText("Package Details");

                layout_package.setVisibility(View.VISIBLE);
                tv_test.setVisibility(View.VISIBLE);

                try {

                    Log.e("#####PACKAGE", "TRY ADAPTER");
                    JSONArray jsonArray = new JSONArray(test_names);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        text_package.setText(jsonObject.getString("package_name"));
                        package_price.setText(ConstValue.CURRENCY + " " + jsonObject.getString("price"));


                        String tests = jsonObject.getString("name");
                        Log.e("tests", tests);

                        JSONArray jsonArray1 = new JSONArray(tests);

                        for (int j = 0; j < jsonArray1.length(); j++) {

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

                            LinearLayout linearLayout1 = new LinearLayout(AppointmentDetailsActivity.this);
                            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                            linearLayout1.setLayoutParams(params);

                            TextView name = new TextView(AppointmentDetailsActivity.this);
                            name.setLayoutParams(params2);


                            name.setTextColor(Color.DKGRAY);
                            //name.setBackgroundResource(R.color.white);
                            name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(55)});
                            name.setPadding(10, 5, 5, 5);
//                            name.setText(Integer.toString());

                            String testname = String.valueOf(jsonArray1.get(j));
                            name.setText(testname);
                            Log.e("test", "##" + testname);
                            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

                            linearLayout1.addView(name);


                            layout.addView(linearLayout1, params);

                            //textViewName.setText(jsonObject.getString("name"));
                            //text_price.setText(jsonObject.getString("price"));
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    test_names = getIntent().getStringExtra("test_names");

                    Log.e("#####", "TRY ADAPTER_Test");
                    JSONArray jsonArray = new JSONArray(test_names);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

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

                        LinearLayout linearLayout1 = new LinearLayout(AppointmentDetailsActivity.this);
                        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout1.setLayoutParams(params);

                        TextView name = new TextView(AppointmentDetailsActivity.this);
                        TextView price = new TextView(AppointmentDetailsActivity.this);
                        name.setLayoutParams(params2);
                        price.setLayoutParams(params3);


                        name.setTextColor(Color.DKGRAY);
                        //name.setBackgroundResource(R.color.white);
                        name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(55)});
                        name.setPadding(10, 5, 5, 5);

                        name.setText(jsonObject.getString("name"));
                        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

                        // price.setTextColor(Color.DKGRAY);
                        //  price.setBackgroundResource(R.color.white);
                        // price.setFilters(new InputFilter[]{new InputFilter.LengthFilter(55)});
                        // price.setPadding(10, 5, 10, 5);
                        // price.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
                        // price.setText(ConstValue.CURRENCY + jsonObject.getString("price"));
                        // price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                        linearLayout1.addView(name);
                        linearLayout1.addView(price);

                        layout.addView(linearLayout1, params);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }







   /*    AppointmentDetailsAdapter appointmentDetailsAdapter=new AppointmentDetailsAdapter(AppointmentDetailsActivity.this,test_names);
       recyclerViewDetails.setAdapter(appointmentDetailsAdapter);

*/


            labName.setText(lab_name);
            text_date.setText(date);
            text_time.setText(time);


            // tax.setText(" Rs. " + getIntent().getStringExtra("tax"));
            if (getIntent().getStringExtra("total_price").contains(ConstValue.CURRENCY)){
                total_price.setText( getIntent().getStringExtra("total_price"));
            }else {
                total_price.setText( ConstValue.CURRENCY+getIntent().getStringExtra("total_price"));
            }

            final_total.setText(ConstValue.CURRENCY + getIntent().getStringExtra("final_total"));



        }

    }


    private void setupViews() {

        labName = findViewById(R.id.labName);
        text_date = findViewById(R.id.Tvdate);
        text_time = (TextView) findViewById(R.id.Tvtime);
        labName = findViewById(R.id.labName);
        clinic_name = findViewById(R.id.clinic_name);

        layout = findViewById(R.id.layout_test);
        layoutTest = findViewById(R.id.layoutTest);
        layout_payment = findViewById(R.id.layout_payment);
        layout_details = findViewById(R.id.layout_details);
        layout_payment_mode = findViewById(R.id.layout_payment_mode);
        layout_date = findViewById(R.id.layout_date);
        layout_time = findViewById(R.id.layout_time);
        layout_test_history = findViewById(R.id.layout_test_history);
        layout_history = findViewById(R.id.layout_history);
        tax = findViewById(R.id.text_tax);
        payment_mode = findViewById(R.id.payment_mode);
        total_price = findViewById(R.id.total_price);
        final_total = findViewById(R.id.final_total);
        btnInvoice = findViewById(R.id.btnInvoice);
        layout_package = findViewById(R.id.layout_package);
        layout_all = findViewById(R.id.layout_all);
        text_package = findViewById(R.id.text_package);
        package_price = findViewById(R.id.package_price);
        tv_test = findViewById(R.id.tv_test);
        text_test = findViewById(R.id.text_test);

        view = findViewById(R.id.view1);
    }
}
