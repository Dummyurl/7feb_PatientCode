package com.ziffytech.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.Preferences;
import com.ziffytech.util.SaveSharedPreference;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends CommonActivity {
    EditText editEmail, editPassword, editFullname, passwrd, confirm_pass, phone, edit_referral;
    int user_id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button btnCountry;
    LinearLayout terms;
    CheckBox checkReferral;

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        allowBack();
        setHeaderTitle(getString(R.string.register_new));

        editEmail = (EditText) findViewById(R.id.txtEmail);
        //editPassword = (EditText)findViewById(R.id.txtPassword);
        editFullname = (EditText) findViewById(R.id.txtFirstname);
       // passwrd = (EditText) findViewById(R.id.c_pass);
        phone = (EditText) findViewById(R.id.txtPhone);
       // confirm_pass = (EditText) findViewById(R.id.confirm_pass);
        edit_referral = (EditText) findViewById(R.id.edit_referral);
        //checkReferral = (CheckBox) findViewById(R.id.check_referral);


       /* checkReferral.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit_referral.setVisibility(View.VISIBLE);
                } else {
                    edit_referral.setVisibility(View.GONE);
                }
            }
        });*/


        if (getIntent().hasExtra("Activity")) {

            if (getIntent().getStringExtra("Activity").equals("facebook")) {

                editFullname.setText(getIntent().getStringExtra("name"));

                if (getIntent().hasExtra("phone")){

                    phone.setText(getIntent().getStringExtra("phone"));

                }else if (getIntent().hasExtra("email")){

                    editEmail.setText(getIntent().getStringExtra("email"));
                }


            }
        }


        terms = (LinearLayout) findViewById(R.id.terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(ConstValue.BASE_URL + "index.php/admin/policy"));
                startActivity(i);
            }
        });


        sharedPreferences = getSharedPreferences(Preferences.MyPREFERENCES, Context.MODE_PRIVATE);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


     /*btnCountry=(Button)findViewById(R.id.btnCountry);
        btnCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String [] arr=getResources().getStringArray(R.array.country_arrays);
                final String [] code=getResources().getStringArray(R.array.country_code);


                AlertDialog.Builder ad=new AlertDialog.Builder(RegisterActivity.this);
                ad.setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        if(arr[i].equalsIgnoreCase("India")){

                            btnCountry.setText("+91");

                        }else{

                            btnCountry.setText(code[i]);

                        }

                    }
                });

                ad.create().show();

            }
        });
        */
    }

    public void register() {

        String email = editEmail.getText().toString();
        //String password = editPassword.getText().toString();
        String fullname = editFullname.getText().toString();
//        String c_pass = passwrd.getText().toString();
     //   String confirm_passwrd = confirm_pass.getText().toString();
        String myphone = phone.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(fullname)) {
            editFullname.setError(getString(R.string.valid_required_fullname));
            focusView = editFullname;
            focusView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT);
            return;
        }

        if (!TextUtils.isEmpty(email)){
            if (!isEmailValid(email)) {
                editEmail.setError(getString(R.string.valid_email));
                focusView = editEmail;
                focusView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT);
                return;
            }
        }




        // Check for a valid email address.
       /* if (TextUtils.isEmpty(email)) {
            //editEmail.setError(getString(R.string.valid_required_email));
            focusView = editEmail;
            focusView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT);
            return;
        }*/
      /*  if (!isEmailValid(email)) {
            editEmail.setError(getString(R.string.valid_email));
            focusView = editEmail;
            focusView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT);
            return;
        }*/


      /*  if (!passwrd.getText().toString().equalsIgnoreCase("")) {

            if (passwrd.getText().toString().length() < 6) {
                passwrd.setError(getString(R.string.password_short));
                focusView = passwrd;
                focusView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT);
                return;
            } else if (c_pass.length() > 15) {
                passwrd.setError("Password Length too large");
                focusView = passwrd;
                focusView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT);
                return;

            }


            if (!confirm_passwrd.equals("")) {
                if (!c_pass.equals(confirm_passwrd)) {
                    confirm_pass.setError(getString(R.string.password_not_match));
                    focusView = confirm_pass;
                    focusView.requestFocus();
                    return;
                }
            } else {
                confirm_pass.setError("Please confirm password");
            }


        } else {

            passwrd.setError("Password is required");
        }
*/





/*

        if (!c_pass.equalsIgnoreCase(c_pass)) {
            c_passwrd.setError(getString(R.string.password_not_match));
            focusView = c_passwrd;
            focusView.requestFocus();
            return;
        }
*/

        if (TextUtils.isEmpty(myphone) || myphone.length() < 10) {
            phone.setError("Enter Valid Phone number");
            return;
        }


        if (!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this, MyUtility.INTERNET_ERROR);
            return;
        }




        Intent intent=new Intent(RegisterActivity.this,Otppage.class);
        intent.putExtra("user_fullname",fullname);
        intent.putExtra("user_phone",myphone);
        if (!email.equals("")){
            intent.putExtra("user_email",email);
        }else {
            intent.putExtra("user_email","");
        }

        if (!SaveSharedPreference.getPrefRefCode(RegisterActivity.this).equals("")){

        intent.putExtra("referral_code", SaveSharedPreference.getPrefRefCode(RegisterActivity.this));
        }else {
            intent.putExtra("referral_code","");
        }

        startActivity(intent);


      /*  HashMap<String, String> params = new HashMap<>();
        params.put("user_fullname", fullname);
        params.put("user_phone", myphone);
        params.put("user_email", email);
     *//*   params.put("user_email", email);
        params.put("user_password", c_pass);*//*
        if (!SaveSharedPreference.getPrefRefCode(RegisterActivity.this).equals("")){

            params.put("referral_code", SaveSharedPreference.getPrefRefCode(RegisterActivity.this));
        }else {
            params.put("referral_code","");
        }

        Log.e("PARAMS",params.toString());*/


      /*  if (checkReferral.isChecked()) {
            params.put("referral_code", edit_referral.getText().toString());
        }*/




//
//        userRegister(params);
       // showPrgressBar();
        // params.put("country_code",btnCountry.getText().toString());


    }

    private void userRegister(HashMap<String, String> params) {


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.REGISTER_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);


                            if (userdata.getInt("responce") == 1) {
                                JSONObject data = userdata.getJSONObject("data");
                                common.setSession(ApiParams.COMMON_KEY, data.getString("user_id"));
                               // common.setSession(ApiParams.USER_EMAIL, data.getString("user_email"));
                                common.setSession(ApiParams.USER_FULLNAME, data.getString("user_fullname"));
                                common.setSession(ApiParams.USER_PHONE, data.getString("user_phone"));
                                common.setSession(ApiParams.USER_JSON_DATA, data.toString());

                                finish();

                                if (getIntent().hasExtra("phone")){

                                    Intent intent = new Intent(RegisterActivity.this, ehrdump2.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(RegisterActivity.this, Otppage.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                                //intent.putExtra("new","new");


                            } else {
                                MyUtility.showAlertMessage(RegisterActivity.this, "Provided email or mobile number is already Registered with us,If you forgotten your password then reset your password.");
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                    }
                });

    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
