package com.ziffytech.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.ziffytech.R;
import com.ziffytech.util.CommonClass;
import com.ziffytech.util.GPSTracker;
import com.ziffytech.util.MyUtility;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressSelectcActivity extends CommonActivity {

    ImageView imageAdd;
    CheckBox checkHome, checkCurrent, checkOther;
    TextView text_address,text_proceed;
    EditText edit_address;
    CommonClass common;
    GPSTracker gps;
    RadioGroup radioGroup;
    RadioButton home;
    RadioButton current;
    RadioButton other;
    int REQUEST_WRITE_PERMISSION = 12;
    boolean isHome = false;
    boolean isCurrent = false;
    boolean isOther = false;
    String locationAddress;
    boolean isRadioChecked = false;
    boolean isNothing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        common = new CommonClass(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select);
        setHeaderTitle("Select Address");
        String addrress;
        allowBack();


        text_address = (TextView) findViewById(R.id.text_address);
        text_proceed = (TextView) findViewById(R.id.text_proceed);

        edit_address = (EditText) findViewById(R.id.edit_address);

        text_address.setText(common.getSession("location"));

        if (common.getSession("location").equals("")) {
            text_address.setText("");
            Toast.makeText(AddressSelectcActivity.this, "Update your Profile", Toast.LENGTH_SHORT).show();
        } else {
            text_address.setText(common.getSession("location"));
        }


        radioGroup = (RadioGroup) findViewById(R.id.radioLocation);

        home= (RadioButton) findViewById(R.id.checkHome);
        current= (RadioButton) findViewById(R.id.checkCurrent);
        other= (RadioButton) findViewById(R.id.checkOther);

        if (!common.getSession("location").equals("")){
            home.setChecked(true);
        }else {
            current.setChecked(true);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isRadioChecked = true;

                //find which radio button is selected
                if (checkedId == R.id.checkHome) {
                    isHome = true;
                    isCurrent = false;
                    isOther = false;
                    edit_address.setVisibility(View.GONE);
                    text_address.setVisibility(View.VISIBLE);

                    if (!common.getSession("location").equals("")) {
                        Log.e("IsAddress","true");
                        text_address.setText(common.getSession("location"));

                        RadioButton rbHome=findViewById(R.id.checkHome);
                        rbHome.setChecked(true);

                    } else {
                        Log.e("IsAddress","false");
                       /* RadioButton rbCurrent=radioGroup.findViewById(R.id.checkCurrent);
                        rbCurrent.setChecked(true);*/
                       Intent intent=new Intent(AddressSelectcActivity.this,PersonalDetailsActivity.class);
                       startActivity(intent);

                    }
                } else if (checkedId == R.id.checkCurrent) {
                    edit_address.setVisibility(View.GONE);
                    text_address.setVisibility(View.VISIBLE);
                    isCurrent = true;
                    isHome=false;
                    isOther=false;
                    if (checkPermission()) {
                        getlocation();
                    } else {
                        requestPermission();
                    }

                } else if (checkedId == R.id.checkOther) {
                    isOther = true;
                    isHome=false;
                    isCurrent=false;
                    edit_address.setVisibility(View.VISIBLE);
                    text_address.setVisibility(View.GONE);

                    try {
                        Intent intent =
                                new PlaceAutocomplete
                                        .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                        .build(AddressSelectcActivity.this);
                        startActivityForResult(intent, 1);
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                    }


                } else {
                    isNothing = true;
                    MyUtility.showToast("Please Select Address", AddressSelectcActivity.this);
                }
            }
        });


        text_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (isRadioChecked) {
                    Log.e("isRadioChecked", "true");

                    if (isHome) {
                        locationAddress = common.getSession("location");
                    } else if (isCurrent) {
                        locationAddress = text_address.getText().toString();
                    } else if (isOther) {
                        Log.e("isOther", "true");

                        if (edit_address.getText().equals(" ")){
                            MyUtility.showToast("Please enter Address", AddressSelectcActivity.this);
                        }else {
                            locationAddress = edit_address.getText().toString();
                        }

                    } else {
                        MyUtility.showToast("Please Select Address", AddressSelectcActivity.this);
                    }

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("location", locationAddress);
                    Log.e("locationAddress", locationAddress);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Log.e("isRadioChecked", "false");
                    text_address.setText(common.getSession("location"));
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("location",text_address.getText().toString());
                    Log.e("locationAddress",text_address.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                    //MyUtility.showToast("Please Select Address", AddressSelectcActivity.this);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(AddressSelectcActivity.this, data);
                Log.e("Tag", "Place: " + place.getAddress());

                edit_address.setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(AddressSelectcActivity.this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED)
            {
                // The user canceled the operation.
            }
        }
    }



    private void getlocation() {
        // create class object
        gps = new GPSTracker(AddressSelectcActivity.this);

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
                String knownName = addresses.get(0).getFeatureName();
                String city = addresses.get(0).getLocality();

               /* String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();*/


                Log.e("ADDRESS", "" + knownName + city /*+ city + state + country + postalCode*/);

                text_address.setText(knownName + "," + city/*+ " " + city + " " + state  + " "*/);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            gps.showSettingsAlert();
        }

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
        int result = ContextCompat.checkSelfPermission(AddressSelectcActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


}
