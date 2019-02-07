package com.ziffytech.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.models.Testappointment;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TestAppointmentList extends CommonActivity
{

    ArrayList<Testappointment> appointmentArray;
    TestAppointmentAdapter adapter;
    AlertDialog alertDialog;
    TextView notfoundtv;
    Date dateObj;
    public static final String inputFormat = "HH:mm";
    String appointmentDetails;
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
    String app_id;
    Testappointment model,model1;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_appointment_list);
        appointmentArray = new ArrayList<>();

        notfoundtv = (TextView) findViewById(R.id.notfoundtv);
        notfoundtv.setVisibility(View.GONE);

        allowBack();
        setHeaderTitle(getString(R.string.my_appointments));

        listView = (ListView) findViewById(R.id.listview_test_appo);

        adapter = new TestAppointmentAdapter();
        listView.setAdapter(adapter);

        //listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //deleteConfirm(position);
            }
        });

        showPrgressBar();
        String status = "1";
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());
        params.put("status",status);

        VJsonRequest vJsonRequest = new VJsonRequest(TestAppointmentList.this, ApiParams.MYAPPOINTMENTS_URL, params,
                new VJsonRequest.VJsonResponce()
                {
                    @Override
                    public void VResponce(String responce) {
                        Log.e("MY TEST APPOINTMENTS", responce);

                        hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);

                            if(userdata.getInt("responce")==1)
                            {

                                JSONArray data = userdata.getJSONArray("data");

                                if (data.length() > 0)
                                {

                                    notfoundtv.setVisibility(View.GONE);
                                    //Gson gson = new Gson();
                                    //Type listType = new TypeToken<List<AppointmentModel>>() {
                                   // }.getType();
                                   // appointmentArray.clear();
                                   // appointmentArray.addAll((Collection<? extends Testappointment>) gson.fromJson(data.toString(), listType));


                                    /*model = new Testappointment();

                                    model.setLab_appointment_count("test_count");
                                    model.setLab_appointment_date("lab_appointment_date");
                                    model.setLab_appointment_id("appointment_id");
                                    model.setLab_appointment_name("lab_name");
                                    model.setLab_start_time("start_time");
                                    model.setLab_appointment_test("test");
                                    appointmentArray.add(model);*/


                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Testappointment>>() {
                                    }.getType();
                                    appointmentArray.clear();
                                    appointmentArray.addAll((Collection<? extends Testappointment>) gson.fromJson(data.toString(), listType));
                                    //adapter.notifyDataSetChanged();
                                    //adapter = new TestAppointmentAdapter(TestAppointmentList.this,appointmentArray);
                                    // listView.setAdapter(adapter);
                                     adapter.notifyDataSetChanged();


                                } else {

                                    notfoundtv.setVisibility(View.VISIBLE);

                                }


                            } else {
                                //MyUtility.showAlertMessage(TestAppointmentList.this, "Failed to retrieve appointment");
                                notfoundtv.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            notfoundtv.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                        // common.setToastMessage(responce);
                        notfoundtv.setVisibility(View.VISIBLE);

                    }
                });
    }

  /*  private void deleteConfirm(final int position)
    {
        Testappointment map = appointmentArray.get(position);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());
        params.put("app_id", map.getLab_appointment_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.TEST_APP_CANCEL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        appointmentArray.remove(position);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                    }
                });
    }*/



   /* private void deleteConfirm(final int position)
    {
        Testappointment map = appointmentArray.get(position);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());
        params.put("app_id", map.getLab_appointment_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.TEST_APP_CANCEL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        appointmentArray.remove(position);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                    }
                });
    }*/


    class TestAppointmentAdapter extends BaseAdapter {

        ArrayList<Testappointment> allreportlist2;
        Context context;
        String type;
        private ArrayList<Testappointment> mFilteredList;

       /* public TestAppointmentAdapter(Context context, ArrayList<Testappointment> appointmentArray2)
        {

            Log.e("ADAPTER","true");
            this.allreportlist2 = appointmentArray2;
            this.context = context;
           // mFilteredList = appointmentArray;

            Log.e("APPOINTMENTARRAY",appointmentArray2.toString());

        }*/

        @Override
        public int getCount() {
            if (appointmentArray == null)
                return 0;
            else
                return appointmentArray.size();
        }

        @Override
        public Testappointment getItem(int i) {
            return appointmentArray.get(i);

        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        getApplicationContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.row_appointment, null);
            }

            model1 = new Testappointment();
            model1 = appointmentArray.get(i);


            TextView txtDate = (TextView) view.findViewById(R.id.txtDate);
            TextView txtTime = (TextView) view.findViewById(R.id.txtTime);
            TextView txtName = (TextView) view.findViewById(R.id.txtName);
            TextView txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            TextView payment = (TextView) view.findViewById(R.id.payment);
            TextView txtClinic = (TextView) view.findViewById(R.id.txtClinic);
            TextView txtClinicAddress = (TextView) view.findViewById(R.id.txtClincAddress);
            TextView txtClinicPhone = (TextView) view.findViewById(R.id.txtClincPhone);
            Button btnRate = (Button) view.findViewById(R.id.rate);
            Button btnCancel = (Button) view.findViewById(R.id.cancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteConfirm(i);
                }
            });

            txtDate.setText("Date : " + model1.getLab_appointment_date());
            txtTime.setText(model1.getLab_start_time());
            txtName.setText("include " + model1.getLab_appointment_count() + " test");
            //txtPhone.setText(appointment.getApp_phone());
            txtClinic.setText(model1.getLab_appointment_name());
            //txtClinicAddress.setText();
            //txtClinicPhone.setText(appointment.getDoct_degree());

            return view;
        }

       /* private void deleteConfirm(int i)
        {
            Testappointment map = appointmentArray.get(i);
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", common.get_user_id());
            params.put("app_id", map.getLab_appointment_id());

            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.TEST_APP_CANCEL, params,
                    new VJsonRequest.VJsonResponce()
                    {
                        @Override
                        public void VResponce(String responce)
                        {

                            appointmentArray.remove(i);
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void VError(String responce) {
                            common.setToastMessage(responce);
                        }
                    });

        }*/


        public void deleteConfirm(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TestAppointmentList.this);
            builder.setMessage(R.string.cancel_confirmation)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            //selected_index = position;
                            deleteRow(position);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            alertDialog = builder.create();
            alertDialog.show();
        }

        public void deleteRow(final int position) {

            Log.e("isCancel", "true");
            Testappointment map = appointmentArray.get(position);
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", common.get_user_id());
            params.put("app_id", map.getLab_appointment_id());
            Log.e("params", params.toString());

            VJsonRequest vJsonRequest = new VJsonRequest(TestAppointmentList.this, ApiParams.TEST_APP_CANCEL, params,
                    new VJsonRequest.VJsonResponce() {
                        @Override
                        public void VResponce(String responce) {
                            Log.e("CANCEL_APP", responce);

                            try {
                                JSONObject jsonObject = new JSONObject(responce);

                                if (jsonObject.getInt("responce")==1)
                                {

                                    appointmentArray.remove(position);
                                    adapter.notifyDataSetChanged();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void VError(String responce) {
                            common.setToastMessage(responce);
                        }
                    });
        }
    }
 }
