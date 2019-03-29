package com.ziffytech.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.ziffytech.R;
import com.ziffytech.models.VersionModel;
import com.ziffytech.util.SaveSharedPreference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class SplashActivity extends CommonActivity implements BaseSliderView.OnSliderClickListener {
    public static final int  PERMISSION_REQUEST_CODE= 123;
    SliderLayout mDemoSlider;
    private static final int REQUEST_CODE = 0;
    String referralCode="";
    ArrayList<VersionModel> categoryArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        categoryArray = new ArrayList<>();
        FirebaseApp.initializeApp(SplashActivity.this);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
          //  Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
            //jump_to_nextActivity();
            permisson();
            getHash();

        }

        else
        {
            showGPSDisabledAlertToUser();
        }

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData != null) {
                            //init analytics if you want to get analytics from your dynamic links
                          //  analytics = FirebaseAnalytics.getInstance(MainActivity.this);

                            Uri deepLink = pendingDynamicLinkData.getLink();
                          Log.e("####"+"onSuccess called ",deepLink.toString());
                           // https://WWW.ziffytech.com/ZIFFY12044
                         String ref=deepLink.toString().replace("https://WWW.ziffytech.com/","");

                         Log.e("ref",ref);

                            SaveSharedPreference.setPrefRefCode(SplashActivity.this,ref);

                            //logic here, redeem code or whatever

                            FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(pendingDynamicLinkData);
                            if (invite != null) {
                                String invitationId = invite.getInvitationId();
                                if (!TextUtils.isEmpty(invitationId))
                                   Log.e("####"+"invitation Id ",invitationId);
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                     Log.e("####"+"onFailure",e.toString());
                    }
                });

    }

    private void showGPSDisabledAlertToUser()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device, You have to enable it to start the App !!")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(callGPSSettingIntent, REQUEST_CODE);

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void getHash()
    {

        Log.e("HASH","true");

    /*    MessageDigest md = null;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e)
        {
                 e.printStackTrace();
        }
        Log.e("SecretKey = ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
*/

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.ziffytech", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("#####"+"KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("####"+"KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("####"+"KeyHash:", e.toString());
        }


    }


    private void goTomain()
    {

        Thread background = new Thread()
        {
            public void run()
            {
                try {
                    sleep(3 * 1000);

                    Gotofunctinality();

                } catch (Exception e) {
                    //Toast.makeText(Splash.this,""+e,Toast.LENGTH_SHORT).show();

                }
            }
        };
        background.start();

    }

    private void Gotofunctinality()
    {
        Intent intent = null;
        if(super.common.is_user_login()){

            Log.e("is_user_login","true");
            intent = new Intent(SplashActivity.this,MainActivity.class);
        }else {
            Log.e("is_user_login","false");
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        if (intent!=null){
            startActivity(intent);
            finish();
        }


    }


    public  void permisson()
    {
        if (!checkPermission())
        {
            requestPermission();
        } else {
            goTomain();
        }
    }


    private boolean checkPermission()
    {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CALENDAR);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CALENDAR);


        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED &&
                result3 == PackageManager.PERMISSION_GRANTED &&
                result4== PackageManager.PERMISSION_GRANTED &&
                result5 == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]
                {
                ACCESS_FINE_LOCATION,
                CAMERA,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE ,
                READ_CALENDAR,
                WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0)
                {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean calRead = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean calWrite = grantResults[5] == PackageManager.PERMISSION_GRANTED;


                    if (locationAccepted && cameraAccepted && readAccepted && writeAccepted && calRead && calWrite)
                    {

                        goTomain();

                    } else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            showMessageOKCancel();
                        }
                    }
                }
                else
                {
                    myUpdateOperation();
                }


                break;
        }
    }

    private void myUpdateOperation()
    {

        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

    }

    private void showMessageOKCancel()
    {
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setMessage("You need to allow access to all the permissions. If permissions not showing go to your app setting and allow permission");
        ad.setCancelable(false);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
                permisson();
            }
        });

        AlertDialog alertDialog=ad.create();
        alertDialog.show();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }



}
