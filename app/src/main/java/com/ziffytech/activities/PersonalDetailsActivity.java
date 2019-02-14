package com.ziffytech.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.Preferences;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by admn on 11/11/2017.
 */

public class PersonalDetailsActivity extends CommonActivity {

    EditText name,email,phone,locality,bloodGroup,dob,feet,inches,weight,emer_contact,aadhar,salutations;
    int year, month, day;
    String Mygender="0",mystatus="1";
    SharedPreferences sharedPreferences;
    Button button;
    ImageView img_address;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_details_activity);
        allowBack();
        setHeaderTitle(getString(R.string.PersonalDetails));
         Button skip=(Button)findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(PersonalDetailsActivity.this,MedicalDetails.class);
                intent1.putExtra("new","new");
                startActivity(intent1);
            }
        });

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        email = (EditText)findViewById(R.id.email);
        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.txtPhone);
        aadhar = (EditText)findViewById(R.id.aadhar_no);

        locality = (EditText)findViewById(R.id.location);




        dob = (EditText)findViewById(R.id.dob);
        feet = (EditText)findViewById(R.id.feet);
        inches = (EditText)findViewById(R.id.inches);
        weight = (EditText)findViewById(R.id.weight);
        emer_contact = (EditText)findViewById(R.id.emer_contact);
        bloodGroup = (EditText)findViewById(R.id.blood_group);
       /* img_address = (ImageView) findViewById(R.id.img_address);

        img_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent =
                            new PlaceAutocomplete
                                    .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(PersonalDetailsActivity.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }
        });
*/
        salutations = (EditText)findViewById(R.id.stype);


        bloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(PersonalDetailsActivity.this);
                builderSingle.setTitle("Select Blood Group");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PersonalDetailsActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("O+");
                arrayAdapter.add("O-");
                arrayAdapter.add("A+");
                arrayAdapter.add("B+");
                arrayAdapter.add("A-");
                arrayAdapter.add("B-");
                arrayAdapter.add("AB+");
                arrayAdapter.add("AB-");
                arrayAdapter.add("Not Known");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(PersonalDetailsActivity.this);
                        builderInner.setMessage(strName);
                        bloodGroup.setText(strName);
                    }
                });
                builderSingle.show();
            }
        });

        salutations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(PersonalDetailsActivity.this);
                builderSingle.setTitle("Select salutations");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PersonalDetailsActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Mr.");
                arrayAdapter.add("Mrs.");
                arrayAdapter.add("Miss.");
                arrayAdapter.add("Dr.");
                arrayAdapter.add("Baby of.");
                arrayAdapter.add("Mast.");
                arrayAdapter.add("SMT.");
                arrayAdapter.add("Hosp.");
                arrayAdapter.add("Ms.");


                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(PersonalDetailsActivity.this);
                        builderInner.setMessage(strName);
                        salutations.setText(strName);
                    }
                });
                builderSingle.show();
            }
        });



        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dd = new DatePickerDialog(PersonalDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                                    String dateInString = year +"-"+ (monthOfYear + 1) +"-"+ dayOfMonth;
                                    Date date = formatter.parse(dateInString);
                                    dob.setText(formatter.format(date).toString());

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }, year, month, day);

                Calendar c= Calendar.getInstance();
                c.add(Calendar.YEAR,-12);
                dd.getDatePicker().setMaxDate(c.getTimeInMillis());
                dd.show();

            }
        });


        sharedPreferences = getSharedPreferences(Preferences.MyPREFERENCES, Context.MODE_PRIVATE);

         button = (Button)findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personaldetails();
            }
        });



        //setup data

        email.setText(common.getSession(ApiParams.USER_EMAIL));
        name.setText(common.getSession(ApiParams.USER_FULLNAME));
        email.setEnabled(false);
        name.setEnabled(false);


        if(getIntent().hasExtra("new")){



            skip.setVisibility(View.GONE);
            button.setText("SUBMIT");


        }else{

            try {
                JSONObject data=new JSONObject(common.getSession(ApiParams.USER_JSON_DATA));


                if(data.getString("user_bdate").contains("0000")){
                    dob.setText("");
                }else{
                    dob.setText(data.getString("user_bdate"));
                }


             //   height.setText(data.getString("height"));

                if (data.getString("height").equals("")){

                    feet.setText("");
                    inches.setText("");
                }else {
                    String height = data.getString("height");

                    String s1=height;

                    String ft=s1.substring(0,1);
                    String in=s1.substring(2,3);

                    feet.setText(ft);
                    inches.setText(in);

                }




/*

                String str=height.substring(height.lastIndexOf("'") + 1);
                String str2= String.valueOf(height.charAt(0));


                Log.e("HEIGHT",height +" "+str+" "+str2);
                feet.setText(str2);
                inches.setText(str);
                if(height.contains(".")){

                    String[] split = height.split(".");
                    String ft = split[0];
                    String inch= split[1];

                    Log.e("HEIGHT","####"+split+"####"+ft+"###"+inch);

                    feet.setText(ft);
                    inches.setText(inch);

                }else if(height.contains("'")){

                    String[] split = height.split(".");
                    String ft = split[0];
                    String inch= split[1];

                    Log.e("HEIGHT",height+"####"+split+"####"+ft+"###"+inch);

                    feet.setText(ft);
                    inches.setText(inch);

                }else {

                    feet.setText(data.getString("height"));
                }
*/

                if (data.getString("weight").equals("")){
                    weight.setText("");
                }else {
                    weight.setText(data.getString("weight"));
                }

                if (data.getString("blood_group").equals("")){
                    bloodGroup.setText("");
                }else {
                    bloodGroup.setText(data.getString("blood_group"));
                }

                if (data.getString("adhar_number").equals("")){
                    aadhar.setText("");
                }else {
                    aadhar.setText(data.getString("adhar_number"));
                }




                if (data.getString("gender").equals("")){

                 Mygender="";
                    }else {

                    Mygender=data.getString("gender");

                    if(data.getString("gender").equalsIgnoreCase("1")){

                        // female

                        RadioButton female = (RadioButton) findViewById(R.id.radioFemale);
                        female.setChecked(true);

                    }else{

                        // male

                        RadioButton male = (RadioButton) findViewById(R.id.radioMale);
                        male.setChecked(true);
                    }


                }


                if (data.getString("location").equals("")){
                    locality.setText("");

                }else {

                    locality.setText(data.getString("location"));
                }


                emer_contact.setText(data.getString("e_contact"));
                phone.setText(data.getString("phone_number"));
                salutations.setText(data.getString("salutations"));


                if (data.getString("matrial_status").equals("")){
                    mystatus="";
                }else {
                    mystatus=data.getString("matrial_status");

                    if(data.getString("matrial_status").equalsIgnoreCase("1")){

                        //  Unmarried  SINGLE
                        RadioButton unmarr = (RadioButton) findViewById(R.id.single_radio);
                        unmarr.setChecked(true);

                    }else{

                        //Married....

                        RadioButton single = (RadioButton) findViewById(R.id.marride);
                        single.setChecked(true);

                    }



                }

                skip.setVisibility(View.GONE);
                button.setText("UPDATE");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        typeGenderSelection();
        typeMaritalSelection();

    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(PersonalDetailsActivity.this, data);
                Log.e("Tag", "Place: " + place.getAddress());

                locality.setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(PersonalDetailsActivity.this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED)
            {
                // The user canceled the operation.
            }
        }
    }

    private void typeGenderSelection()
    {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.radioMale) {
                    Mygender="0";
                } else if (checkedId == R.id.radioFemale) {
                    Mygender="1";
                }
            }
        });
    }

    private void typeMaritalSelection()
    {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.readio_status);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.single_radio) {
                    mystatus="1";
                } else if (checkedId == R.id.marride) {
                    mystatus="0";
                }
            }
        });
    }


    private void personaldetails()
    {
        String aadh=aadhar.getText().toString();
        String sal=salutations.getText().toString();
        //String myphone=phone.getText().toString();
        String local=locality.getText().toString();
        String mydob=dob.getText().toString();
        String myh=feet.getText().toString()+"'"+inches.getText().toString();
        String myw=weight.getText().toString();
        String emer=emer_contact.getText().toString();
        String blood=bloodGroup.getText().toString();
        boolean cancel = false;
        View focusView = null;



        if (TextUtils.isEmpty(sal)) {
            salutations.setError("Select Salutations");
            salutations.setFocusable(true);
            return;

        }
        if (TextUtils.isEmpty(mydob)) {

            dob.setError(getString(R.string.dobi));
            dob.setFocusable(true);

            return;

        }
        if (TextUtils.isEmpty(blood)) {

            bloodGroup.setError(getString(R.string.blood_grop));
            bloodGroup.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(myh)) {

            feet.setError("Enter Valid Height");
            inches.setError("Enter Valid Height");
            feet.setFocusable(true);
            inches.setFocusable(true);

            return;
        }

        if (TextUtils.isEmpty(myw)) {

            weight.setError("Enter Valid Weight");
            weight.setFocusable(true);
            return;
        }

        /*if (TextUtils.isEmpty(myphone) || myphone.length()<10) {
            phone.setError("Enter Valid Phone number");
            return;
        }*/


        if (TextUtils.isEmpty(emer) || emer.length()<10) {
            emer_contact.setError("Enter Valid Emergency Contact Number");
            emer_contact.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(local)) {

            locality.setError("Enter Valid Locality");
            return;
        }

        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this, MyUtility.INTERNET_ERROR);
            return;
        }


        showPrgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.getSession(ApiParams.COMMON_KEY));
        params.put("user_bdate", mydob);
        params.put("blood_group", blood);
        params.put("adhar_number",aadh);
        //params.put("user_phone",myphone);
        params.put("matrial_status", mystatus);
        params.put("height", myh);
        params.put("weight", myw);
        params.put("gender", Mygender);
        params.put("e_contact", emer);
        params.put("location", local);
        params.put("salutations", sal);

       // Log.e("phone",myphone);


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.PERSON_DETAIL_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {
                        hideProgressBar();
                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);

                            if(userdata.getInt("responce") == 1 )
                            {

                                JSONObject data=userdata.getJSONObject("data");

                                common.setSession(ApiParams.COMMON_KEY, data.getString("user_id"));
                                common.setSession(ApiParams.USER_EMAIL, data.getString("user_email"));
                                common.setSession(ApiParams.USER_FULLNAME, data.getString("user_fullname"));
                                common.setSession(ApiParams.USER_PHONE, data.getString("user_phone"));
                                common.setSession(ApiParams.USER_JSON_DATA, data.toString());
                                common.setSession(ApiParams.USER_WEIGHT, data.getString("weight"));
                                common.setSession(ApiParams.USER_HEIGHT, data.getString("height"));
                                common.setSession(ApiParams.USER_BLOOD_GROUP, data.getString("blood_group"));
                                common.setSession(ApiParams.USER_MARITAL_STATUS, data.getString("matrial_status"));
                                common.setSession(ApiParams.USER_DOB, data.getString("user_bdate"));
                                common.setSession(ApiParams.USER_LOCATION, data.getString("location"));
                                Log.e("location",data.getString("location"));
                                common.setSession(ApiParams.USER_GENDER, data.getString("gender"));
                                common.setSession(ApiParams.USER_SALUTATION, data.getString("salutations"));

                                if (getIntent().hasExtra("Activity")){
                                    if (getIntent().getStringExtra("Activity").equals("1")){

                                        common.setSession(ApiParams.COMMON_KEY, data.getString("user_id"));
                                        common.setSession(ApiParams.USER_EMAIL, data.getString("user_email"));
                                        common.setSession(ApiParams.USER_FULLNAME, data.getString("user_fullname"));
                                        common.setSession(ApiParams.USER_PHONE, data.getString("user_phone"));
                                        common.setSession(ApiParams.USER_JSON_DATA, data.toString());
                                        common.setSession(ApiParams.USER_WEIGHT, data.getString("weight"));
                                        common.setSession(ApiParams.USER_HEIGHT, data.getString("height"));
                                        common.setSession(ApiParams.USER_BLOOD_GROUP, data.getString("blood_group"));
                                        common.setSession(ApiParams.USER_MARITAL_STATUS, data.getString("matrial_status"));
                                        common.setSession(ApiParams.USER_DOB, data.getString("user_bdate"));
                                        common.setSession(ApiParams.USER_LOCATION, data.getString("location"));
                                        Log.e("location",data.getString("location"));
                                        common.setSession(ApiParams.USER_GENDER, data.getString("gender"));
                                        common.setSession(ApiParams.USER_SALUTATION, data.getString("salutations"));
                                        finish();


                                    }

                                }

                                if(getIntent().hasExtra("new")){

                                    Intent intent=new Intent(PersonalDetailsActivity.this,MedicalDetails.class);
                                    intent.putExtra("new","new");
                                    startActivity(intent);
                                }else{
                                    finish();
                                    MyUtility.showToast("Personal Detail Updated!",PersonalDetailsActivity.this);
                                }

                            }else{
                                MyUtility.showAlertMessage(PersonalDetailsActivity.this,"Failed to Update");
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            MyUtility.showAlertMessage(PersonalDetailsActivity.this,"Something went wrong.");
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void VError(String responce) {

                        hideProgressBar();
                        MyUtility.showAlertMessage(PersonalDetailsActivity.this,"Something went wrong.");
                       Log.e("ERROR",responce);
                    }
                });

    }

    public final static boolean isValidEmail(CharSequence target) {
        if(target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_skip, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

          /*  case R.id.action_search:
                    Intent intent1 = new Intent(PersonalDetailsActivity.this, MainActivity.class);
                    startActivity(intent1);
*/
            //    break;



            case android.R.id.home:

                onBackPressed();

                return true;


            /*case R.id.item_logout:
                business_Medical_session.logoutUser();
                finish();
                break;*/
            default:


        }
        return super.onOptionsItemSelected(item);
    }

}
