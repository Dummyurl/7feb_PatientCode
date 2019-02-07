package com.ziffytech.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.adapters.PackageLabListAdapter;
import com.ziffytech.util.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PackageLabSelectActivity extends CommonActivity {


    /*ArrayList<LabModel> labArrayList;*/
    ArrayList<LabModel> labArrayListLocation;

    EditText edit_lab_search;
    RecyclerView recyclerViewLabList;
    /*LabListAdapter labListAdapter;*/
    PackageLabListAdapter labListAdapterLocation;
    LinearLayoutManager layoutManager;
    ImageView image_filter;
    Dialog dialog_menus;
    TextView txt_location, txt_not_found;
    String testIDs, test_price, test_name;
    String address;
    GPSTracker gps;
    boolean isChangeLocation = false;
    int REQUEST_WRITE_PERMISSION = 12;
    String lat;
    String longi;
    String loc;
    boolean isNoLabs = false;
    LinearLayout layout_test_selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_lab_select);

        init();
        testIDs = getIntent().getStringExtra("test");
        setHeaderTitle("Lab List");
        allowBack();

        Log.e("TESTS", testIDs);












        if (getIntent().hasExtra("Address")) {

            Log.e("ADDRESS", "AVAILABLE");

            txt_location.setText(common.getSession("location"));

            if (getIntent().getStringExtra("Address").equals("1")) {
                getLocationFromAddress(common.getSession("location"));
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("lat", lat);
                params.put("long", longi);
                params.put("test", testIDs);

                Log.e("PARAMS", params.toString());
                CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.LAB_URL, params, this.createRequestSuccessListenerLabListLocation(), this.createRequestErrorListenerLabListLocation());
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(customRequestForString);
                showPrgressBar();


            } else if (getIntent().getStringExtra("Address").equals("0")) {
                Log.e("ADDRESS", "NOT AVAILABLE");
                if (checkPermission()) {
                    getlocation();
                } else {
                    requestPermission();
                }
                Log.e("LOC", loc);
                getLocationFromAddress(loc);
                Log.e("LAT", lat);
                Log.e("LONGI", longi);

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("lat", lat);
                params.put("long", longi);
                params.put("test", testIDs);
                Log.e("PARAMS", params.toString());

                CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.LAB_URL, params, this.createRequestSuccessListenerLabListLocation(), this.createRequestErrorListenerLabListLocation());
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(customRequestForString);
                showPrgressBar();
            }


            if (isChangeLocation) {
                Log.e("isChangeLocation", String.valueOf(isChangeLocation));
                isChangeLocation = false;

                Intent intent = getIntent();
                address = intent.getStringExtra("location");
                Log.e("location", address);
                txt_location.setText(address);
                isChangeLocation = false;
            }


        }

  /*      if (common.getSession("loaction").equals("")){
            getlocation();
            getLocationFromAddress(loc);
            HashMap<String, String> params = new HashMap<String,String>();
            params.put("lat",lat);
            params.put("long",longi);
            params.put("test",testIDs);
            Log.e("PARAMS",params.toString());
            CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.LAB_URL, params, this.createRequestSuccessListenerLabList(), this.createRequestErrorListenerLabList());
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(customRequestForString);
        }else{
            Log.e("#####","PROFILE LOCATION");
            txt_location.setText(common.getSession("location"));
            getLocationFromAddress(common.getSession("location"));
            HashMap<String, String> params = new HashMap<String,String>();
            params.put("lat",lat);
            params.put("long",longi);
            params.put("test",testIDs);
            Log.e("PARAMS",params.toString());
            CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.LAB_URL, params, this.createRequestSuccessListenerLabList(), this.createRequestErrorListenerLabList());
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(customRequestForString);
        }*/


        txt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChangeLocation = true;
                Intent intent = new Intent(PackageLabSelectActivity.this, AddressSelectcActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        edit_lab_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (isNoLabs) {

                } else {
                    filter(editable.toString());
                }
            }
        });


        image_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflator = (LayoutInflater) PackageLabSelectActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View show_menu_popup = inflator.inflate(R.layout.dialog_self_filter_popup, null);


                dialog_menus = new Dialog(PackageLabSelectActivity.this);

                dialog_menus.setContentView(show_menu_popup);

                dialog_menus.setTitle(" ");

                dialog_menus.show();


            }
        });


    }


 /*   @Override
    public boolean onCreateOptionsMenu (Menu menu) {

        getMenuInflater().inflate(R.menu.item_address, menu);
        MenuItem item = menu.findItem(R.id.txt_address);
        if (!ApiParams.USER_LOCATION.equals("")){
            item.setTitle(common.getSession("location"));


        }else{

        }
        return true;
    }*/


//    private Response.Listener<String> createRequestSuccessListenerLabList() {
//        return new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                hideProgressBar();
//
//                Log.e("LAB_RESPONSE", response.toString());
//                // App.stopProgressDialog();
//                labArrayList = new ArrayList<>();
//
//                if (labArrayList.isEmpty()) {
//                } else {
//                    labArrayList.clear();
//                }
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String result = jsonObject.getString("response");
//
//
//                    if (result.equals("1")) {
//                        isNoLabs=false;
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                        Log.e("RESPONSE", result);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            JSONObject obj = jsonArray.getJSONObject(i);
//
//                            LabModel labModel = new LabModel();
//
//                            if (!obj.getString("lab_ip").equals("")) {
//
//
//
//                                /*  "lab_name": "S.R. Diagnostics",
//            "lab_ip": "http://183.87.69.174:100/frm/frmLogin.aspx",
//            "lab_id": "22",
//            "lab_lat": "18.544862",
//            "lab_long": "73.93582200000003",
//            "category": "Pathology",
//            "test_name": "17 OH Progesterone (17 OH-PROG)",
//            "test_code": "Ziffy_Test_3",
//            "distance": "9.960138458265268"*/
//
//
//                                labModel.setLab_id(obj.getString("lab_id"));
//                                Log.e("LAb_ID", obj.getString("lab_id"));
//                                labModel.setLab_name(obj.getString("lab_name"));
//                                Log.e("LAB_NAME", obj.getString("lab_name"));
//                                labModel.setLab_address(obj.getString("lab_address"));
//                                Log.e("LAb_ADDRESS", obj.getString("lab_address"));
//                                labModel.setLab_ip(obj.getString("lab_ip"));
//                                Log.e("LAb_IP", obj.getString("lab_ip"));
//                                labModel.setLab_closing_time(obj.getString("lab_closing_time"));
//                                Log.e("LAb_CLOSING_TIME", obj.getString("lab_closing_time"));
//                                labModel.setLab_opening_time(obj.getString("lab_opening_time"));
//                                Log.e("LAb_OPENING_TIME", obj.getString("lab_opening_time"));
//                                labModel.setLab_photo(obj.getString("lab_image"));
//                                Log.e("LAb_ID", obj.getString("lab_image"));
//                                Log.e("LAB_MODEL", labModel.toString());
//
//
//                            } else {
//
//
//                                labModel.setLab_id(obj.getString("lab_id"));
//                                Log.e("LAb_ID", obj.getString("lab_id"));
//                                labModel.setLab_name(obj.getString("lab_name"));
//                                Log.e("LAB_NAME", obj.getString("lab_name"));
//                                labModel.setLab_address(obj.getString("lab_address"));
//                                Log.e("LAb_ADDRESS", obj.getString("lab_address"));
//                                labModel.setLab_ip("");
//                                // Log.e("LAb_IP",obj.getString("lab_ip"));
//                                labModel.setLab_closing_time(obj.getString("lab_closing_time"));
//                                Log.e("LAb_CLOSING_TIME", obj.getString("lab_closing_time"));
//                                labModel.setLab_opening_time(obj.getString("lab_opening_time"));
//                                Log.e("LAb_OPENING_TIME", obj.getString("lab_opening_time"));
//                                labModel.setLab_photo(obj.getString("lab_image"));
//                                Log.e("LAb_ID", obj.getString("lab_image"));
//                                Log.e("LAB_MODEL", labModel.toString());
//
//
//                            }
//                            labArrayList.add(labModel);
//
//                        }
//
//                        labListAdapter = new LabListAdapter(PackageLabSelectActivity.this, labArrayList, testIDs);
//                        recyclerViewLabList.setAdapter(labListAdapter);
//
//                    } else if (result.equals("0")) {
//                        Log.e("RESPONSE", "0");
//
//                        recyclerViewLabList.setVisibility(View.GONE);
//                        txt_not_found.setVisibility(View.VISIBLE);
//
//                    }
//
//                } catch (JSONException e) {
//                    hideProgressBar();
//                    e.printStackTrace();
//
//                }
//
//
//            }
//        };
//    }


//    private Response.ErrorListener createRequestErrorListenerLabList() {
//        return new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                hideProgressBar();
//                Log.i("##", "##" + error.toString());
//                //App.showAlert("Something Went Wrong, Please Try again",MultiTestSearchActivity.this);
//
//            }
//        };
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String location = data.getStringExtra("location");
                Log.e("location", location);
                txt_location.setText(location);
                getLocationFromAddress(location);

                HashMap<String, String> params = new HashMap<String, String>();

                params.put("lat", lat);
                params.put("long", longi);
                params.put("test", testIDs);
                Log.e("PARAMS", params.toString());

                CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.LAB_URL, params, this.createRequestSuccessListenerLabListLocation(), this.createRequestErrorListenerLabListLocation());
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(customRequestForString);
                showPrgressBar();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Select Address", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Response.Listener<String> createRequestSuccessListenerLabListLocation() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgressBar();

                Log.e("LAB_RESPONSE", response.toString());
                // App.stopProgressDialog();
                labArrayListLocation = new ArrayList<>();


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("response");


                    if (result.equals("1")) {
                        isNoLabs = false;
                        recyclerViewLabList.setVisibility(View.VISIBLE);
                        txt_not_found.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        Log.e("RESPONSE", result);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            LabModel labModel = new LabModel();

                            if (!obj.getString("lab_ip").equals("")) {



                                /*  "lab_name": "S.R. Diagnostics",
            "lab_ip": "http://183.87.69.174:100/frm/frmLogin.aspx",
            "lab_id": "22",
            "lab_lat": "18.544862",
            "lab_long": "73.93582200000003",
            "category": "Pathology",
            "test_name": "17 OH Progesterone (17 OH-PROG)",
            "test_code": "Ziffy_Test_3",
            "distance": "9.960138458265268"*/


                                labModel.setLab_id(obj.getString("lab_id"));
                                Log.e("LAb_ID", obj.getString("lab_id"));
                                labModel.setLab_name(obj.getString("lab_name"));
                                Log.e("LAB_NAME", obj.getString("lab_name"));
                                labModel.setLab_address(obj.getString("lab_address"));
                                Log.e("LAb_ADDRESS", obj.getString("lab_address"));
                                labModel.setLab_ip(obj.getString("lab_ip"));
                                Log.e("LAb_IP", obj.getString("lab_ip"));
                                labModel.setLab_closing_time(obj.getString("lab_closing_time"));
                                Log.e("LAb_CLOSING_TIME", obj.getString("lab_closing_time"));
                                labModel.setLab_opening_time(obj.getString("lab_opening_time"));
                                Log.e("LAb_OPENING_TIME", obj.getString("lab_opening_time"));
                                labModel.setLab_photo(obj.getString("lab_image"));
                                Log.e("LAb_ID", obj.getString("lab_image"));
                                Log.e("LAB_MODEL", labModel.toString());


                            } else {
                                labModel.setLab_id(obj.getString("lab_id"));
                                Log.e("LAb_ID", obj.getString("lab_id"));
                                labModel.setLab_name(obj.getString("lab_name"));
                                Log.e("LAB_NAME", obj.getString("lab_name"));
                                labModel.setLab_address(obj.getString("lab_address"));
                                Log.e("LAb_ADDRESS", obj.getString("lab_address"));
                                labModel.setLab_ip("");
                                // Log.e("LAb_IP",obj.getString("lab_ip"));
                                labModel.setLab_closing_time(obj.getString("lab_closing_time"));
                                Log.e("LAb_CLOSING_TIME", obj.getString("lab_closing_time"));
                                labModel.setLab_opening_time(obj.getString("lab_opening_time"));
                                Log.e("LAb_OPENING_TIME", obj.getString("lab_opening_time"));
                                labModel.setLab_photo(obj.getString("lab_image"));
                                Log.e("LAb_ID", obj.getString("lab_image"));
                                Log.e("LAB_MODEL", labModel.toString());


                            }
                            labArrayListLocation.add(labModel);

                        }

                        labListAdapterLocation = new PackageLabListAdapter (PackageLabSelectActivity.this, labArrayListLocation, testIDs);
                        recyclerViewLabList.setAdapter(labListAdapterLocation);

                    } else if (result.equals("0")) {
                        Log.e("RESPONSE", "0");
                        isNoLabs = true;

                        recyclerViewLabList.setVisibility(View.GONE);
                        txt_not_found.setVisibility(View.VISIBLE);

                    }

                } catch (JSONException e) {
                    hideProgressBar();
                    e.printStackTrace();

                }


            }
        };
    }


    private Response.ErrorListener createRequestErrorListenerLabListLocation() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                Log.i("##", "##" + error.toString());
                //App.showAlert("Something Went Wrong, Please Try again",MultiTestSearchActivity.this);

            }
        };
    }


    private void init() {


        edit_lab_search = (EditText) findViewById(R.id.edit_search_lab);
        recyclerViewLabList = (RecyclerView) findViewById(R.id.recyclerview_lablist);

        layoutManager = new LinearLayoutManager(PackageLabSelectActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewLabList.setHasFixedSize(true);
        recyclerViewLabList.setLayoutManager(layoutManager);
        image_filter = (ImageView) findViewById(R.id.image_filter);
        txt_location = (TextView) findViewById(R.id.txt_location);
        txt_not_found = (TextView) findViewById(R.id.txt_not_found);
        layout_test_selected = (LinearLayout) findViewById(R.id.layout_test_selected);


    }

    private void filter(String text) {
        ArrayList<LabModel> filterdNames = new ArrayList<>();

        if (!labArrayListLocation.isEmpty()) {

            for (LabModel l : labArrayListLocation) {
                if (l.getLab_name().toLowerCase().toUpperCase().contains(text.toLowerCase().toUpperCase())) {
                    filterdNames.add(l);
                } else {

                  /*  MyUtility.showAlertMessage(this,"Sorry, No Result Found!\n" +
                            "We were unable to find a single lab that provides the test you’ve selected");*/
                }
            }

        }

        //calling a method of the adapter class and passing the filtered list
        labListAdapterLocation.filterList(filterdNames);
    }


    @SuppressLint("RestrictedApi")
    public Barcode.GeoPoint getLocationFromAddress(String strAddress) {

        Log.e("ADDRESS", strAddress);

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        Barcode.GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            lat = String.valueOf(location.getLatitude());
            longi = String.valueOf(location.getLongitude());

            Log.e("lat", lat);
            Log.e("longi", longi);

        /*    p1 = new Barcode.GeoPoint((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));
*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getlocation();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_WRITE_PERMISSION);
        } else {
            getlocation();
        }
    }


    private boolean checkPermission() {
        int result = ActivityCompat.checkSelfPermission(PackageLabSelectActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void getlocation() {
        // create class object
        gps = new GPSTracker(PackageLabSelectActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();


                Log.e("ADDRESS", "" + address + city + state + country + postalCode);

                txt_location.setText(address  + city + " " + state + " ");

                loc = address;


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            gps.showSettingsAlert();
        }

    }


}