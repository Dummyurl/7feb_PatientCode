package com.ziffytech.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.models.AppointmentModel;
import com.ziffytech.util.CommonClass;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.RoundedImageView;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/*public class Fragment_doc_appo extends Fragment//,CommonActivity
{

    ArrayList<AppointmentModel> appointmentArray;
    MyAppointmentsActivity.AppointmentAdapter adapter;
    AlertDialog alertDialog;
    TextView notfoundtv;
    Date dateObj;
    public static final String inputFormat = "HH:mm";
    String appointmentDetails;
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
    String app_id;
    CommonClass common;



    public Fragment_doc_appo()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Toast.makeText(getActivity(), "Photos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        // Toast.makeText(getActivity(), "In onCreate view", Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_doc_appointmnet, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            //Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
        }
        setUpUi();
        //Toast.makeText(getActivity(), "Not Null", Toast.LENGTH_SHORT).show();
    }

    private void setUpUi()
    {

        appointmentArray = new ArrayList<>();

        notfoundtv = (TextView)getActivity().findViewById(R.id.notfoundtv);
        notfoundtv.setVisibility(View.GONE);

        allowBack();
        setHeaderTitle(getActivity().getString(R.string.my_appointments));

        ListView listView = (ListView)getActivity().findViewById(R.id.listview);
        //adapter = new Fragment_doc_appo().AppointmentAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteConfirm(position);
            }
        });

        showPrgressBar();

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.MYAPPOINTMENTS_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {
                        Log.e("MY APPOINTMENTS", responce);

                        hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);


                            if (userdata.getBoolean("responce")) {

                                JSONArray data = userdata.getJSONArray("data");

                                if (data.length() > 0) {

                                    notfoundtv.setVisibility(View.GONE);
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<AppointmentModel>>() {
                                    }.getType();
                                    appointmentArray.clear();
                                    appointmentArray.addAll((Collection<? extends AppointmentModel>) gson.fromJson(data.toString(), listType));
                                    adapter.notifyDataSetChanged();




                                } else {

                                    notfoundtv.setVisibility(View.VISIBLE);

                                }


                            } else {
                                MyUtility.showAlertMessage(getActivity(), "Failed to retrieve appointment");
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



    public void deleteConfirm(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.cancel_confirmation)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        //selected_index = position;
                        //deleteRow(position);


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
        AppointmentModel map = appointmentArray.get(position);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());
        params.put("app_id", map.getId());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.CANCELAPPOINTMENTS_URL, params,
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
    }


    class AppointmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (appointmentArray == null)
                return 0;
            else
                return appointmentArray.size();
        }

        @Override
        public AppointmentModel getItem(int i) {
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
                        getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.row_appointment,null);
            }
            final AppointmentModel appointment = appointmentArray.get(i);

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
            Button btnSchedule = (Button) view.findViewById(R.id.reschedule);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    deleteConfirm(i);
                }
            });


            app_id=appointmentArray.get(i).getId();


            Log.e("###"+"app_id",app_id);

          btnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showRateDialog(i);

                }
            });


            //String  appointment_date="2018-09-05";

            //  String status = "0"; // asum status 0 for appointment place 1 for appointment completed

            // Get Current Date Time
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");

            String getCurrentTime = sdftime.format(c.getTime());  // current time
            Log.e("getCurrentTime", getCurrentTime);

            String getCurrentDateTime = sdf1.format(c.getTime());

            Log.e("getCurrentDateTime", getCurrentDateTime);
            Log.e("appointment_date", appointment.getAppointment_date());


            Date AppointmentStartTime = null; /// Appointment book time
            Date AppointmentStartDate = null; /// Appointment book time
            Date currentDate = null;
            Date CurrentTime = null;
            try {

                AppointmentStartDate=sdf1.parse(appointment.getAppointment_date());
                currentDate=sdf1.parse(getCurrentDateTime);
                AppointmentStartTime = sdftime.parse(appointment.getStart_time());
                CurrentTime = sdftime.parse(getCurrentTime);
                Log.e("AppointmentStartTime", "" + AppointmentStartTime);
                Log.e("strDatecrrnt", "" + CurrentTime);

                Log.e("AppointmentStartDate", "" + AppointmentStartDate);
                Log.e("currentDate", "" + currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (appointment.getApp_status().equals("1")) {

                Log.e("YES", "IF ");


                Log.e("Expire", "Yes");
                btnRate.setVisibility(View.VISIBLE);
                btnRate.setText("CHECKED");
                btnCancel.setVisibility(View.GONE);
                btnSchedule.setVisibility(View.GONE);


            } else if (appointment.getApp_status().equals("0")) {

                if (AppointmentStartDate.equals( currentDate)) {
                    Log.e("AppointmentStartDate","equals currentDate");// date is same

                    if (CurrentTime.before(AppointmentStartTime)) {
                        Log.e("CurrentTime","before AppointmentStartTime");

                        btnRate.setVisibility(View.GONE);
                        btnCancel.setVisibility(View.VISIBLE);
                        btnSchedule.setVisibility(View.VISIBLE);
                    } else   if (CurrentTime.after(AppointmentStartTime)) {
                        Log.e("CurrentTime","after AppointmentStartTime");

                        btnRate.setVisibility(View.VISIBLE);
                        btnRate.setText("CANCELLED");
                        btnCancel.setVisibility(View.GONE);
                        btnSchedule.setVisibility(View.GONE);

                    }
                }else if (AppointmentStartDate.before(currentDate)) {
                    Log.e("AppointmentStartDate","before currentDate");

                    btnRate.setVisibility(View.VISIBLE);
                    btnRate.setText("CANCELLED");
                    btnCancel.setVisibility(View.GONE);
                    btnSchedule.setVisibility(View.GONE);
                }else if (AppointmentStartDate.after(currentDate)){
                    Log.e("AppointmentStartDate","after currentDate");

                    btnRate.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnSchedule.setVisibility(View.VISIBLE);

                }





            }



            txtDate.setText(parseDateToddMM(String.valueOf(appointment.getAppointment_date())));
            txtTime.setText(parseTime(appointment.getStart_time()));
            txtName.setText(appointment.getDoct_name());
            txtPhone.setText(appointment.getApp_phone());

            txtClinic.setText(appointment.getBus_title());
            txtClinicAddress.setText(appointment.getDoct_name());
            txtClinicPhone.setText(appointment.getDoct_degree());

            return view;
        }


        private void showRateDialog(int i) {

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.dialog_rate_doctor, null);
            final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
            dialog.setView(alertLayout);

            TextView tvName = (TextView) alertLayout.findViewById(R.id.doctorName);
            final SimpleRatingBar rating = (SimpleRatingBar) alertLayout.findViewById(R.id.rating);


            Button btnRate = (Button) alertLayout.findViewById(R.id.btnRate);
            RoundedImageView image = (RoundedImageView) alertLayout.findViewById(R.id.doctorImage);
            final AppointmentModel map = appointmentArray.get(i);
            Picasso.with(getActivity()).load(ConstValue.BASE_URL + "/uploads/profile/" + map.getDoct_photo()).into(image);
            tvName.setText(map.getDoct_name());
            final android.support.v7.app.AlertDialog alert = dialog.create();

            btnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (rating.getRating() > 0) {

                        HashMap<String, String> params = new HashMap<>();
                        params.put("user_id", common.get_user_id());
                        params.put("doct_id", map.getDoct_id());
                        params.put("ratings", rating.getRating() + "");

                        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.RATE_DOCTOR, params,
                                new VJsonRequest.VJsonResponce() {
                                    @Override
                                    public void VResponce(String responce) {

                                        alert.dismiss();
                                        MyUtility.showToast("Rated Successfully!", getActivity());

                                    }

                                    @Override
                                    public void VError(String responce) {
                                        common.setToastMessage(responce);
                                    }
                                });

                    } else {

                        MyUtility.showToast("Give rating first", getActivity());
                    }
                }
            });


            alert.show();
        }
    }

    public String parseDateToddMM(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String parseTime(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }



    public static boolean CompareDates(String date1, String date2) {
        try {

            Log.e("DATE1",date1);
            Log.e("DATE2",date2);


            String pattern = "dd-MM-yyyy";
            String patter2 = " HH:mm:ss";
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm:ss");

            Date Date1 = null;
            Date time1 = null;
            try {
                Date1 = formatter.parse(date1);
                time1 = simpleDateFormat.parse(date1);

                Log.e("DATE1",Date1.toString());
                Log.e("time1",time1.toString());


            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date Date2 = null;
            Date time2 = null;
            try {
                Date2 = formatter.parse(date2);
                time2 = simpleDateFormat.parse(date1);
                Log.e("DATE1",Date2.toString());
                Log.e("time1",time2.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }


            Log.e("time1",time1.toString());
            Log.e("time2",time2.toString());





            if (Date1 .equals(Date2 ))
            {
                return false;
            }
            else{
                if (Date1.before(Date2)){
                    return true;
                }else if (Date1.after(Date2)){

                    if (time2.before(time1)){

                        return true;

                    }else if (time2.after(time1)){
                        return false;

                    }
                }





//both date are not equal("use after and before to check occurrence of dates")
            }
        }catch (Exception e1){
            e1.printStackTrace();
        }
        return false;
    }






    private boolean isExpire(String date) {

        Log.e("Date", date);

        if(date.isEmpty() || date.trim().equals("")){
            return false;
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // Jan-20-2015 1:30:55 PM
            Date d = null;
            Date d1 = null;
            String today = getToday("yyyy-MM-dd hh:mm:ss");
            try {
                //System.out.println("expdate>> "+date);
                //System.out.println("today>> "+today+"\n\n");
                d = sdf.parse(date);
                d1 = sdf.parse(today);
                if (d1.compareTo(d) < 0) {// not expired
                    return false;

                } else if (d.compareTo(d1) == 0) {// both date are same

                    if (d.getTime() < d1.getTime()) {// not expired
                        return false;
                    } else if (d.getTime() == d1.getTime()) {//expired
                        return true;
                    } else {//expired
                        return true;
                    }

                } else {//expired
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
    }










    public static String getToday(String format){
        Date date = new Date();
        Log.e("##Date",date.toString());
        return new SimpleDateFormat(format).format(date);
    }

    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }





}*/








