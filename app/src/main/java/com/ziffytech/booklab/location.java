package com.ziffytech.booklab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;


public class location extends CommonActivity
{
    int PLACE_PICKER_REQUEST=1;
    private GoogleApiClient mClient;

    String latitude="";
    String longitude="";

    TextView tvLoc,selectedLocation;
    CheckBox homcollection;

    String isHomelocation="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_location);
        allowBack();
        setHeaderTitle("Filter Lab");


        setUpViews();


        mClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();



    }

    @Override
    protected void onStart() {
        super.onStart();
        mClient.connect();
    }
    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }

    public void setUpViews()
    {
        tvLoc=(TextView) findViewById(R.id.tvLocation);
        tvLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                place();


            }
        });


        selectedLocation=(TextView)findViewById(R.id.selectedlocation);
        homcollection=(CheckBox)findViewById(R.id.homecollection);

        homcollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isHomelocation="1";
                }else {
                    isHomelocation="0";
                }
            }
        });


        Button submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String test=getIntent().getStringExtra("test");
                Log.e("LOCATION_TEST",test);
                    Intent intent=new Intent(location.this,LabDetailList.class);
                    intent.putExtra("test",test);
                    Log.e("TEST_PERESCRIBED",test);
                     intent.putExtra("home",isHomelocation);
                     intent.putExtra("lat",latitude);
                     intent.putExtra("lng",longitude);
                     startActivity(intent);
            }
        });

    }

    private void place() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            Log.e("RQC","TRUE");
            if (resultCode == RESULT_OK) {
                Log.e("RO","TRUE");

                Place selectedPlace = PlacePicker.getPlace(data, this);

                String placename = String.format("%s",selectedPlace.getAddress());

                 selectedLocation.setText(placename);

                 latitude = String.valueOf(selectedPlace.getLatLng().latitude);
                 longitude = String.valueOf(selectedPlace.getLatLng().longitude);

                 Log.e("LAT_LONG",latitude+ ""+longitude);
                // Do something with the place


            }
        }
    }
}
