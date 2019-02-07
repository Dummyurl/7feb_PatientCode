package com.ziffytech.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UpdateProfileActivity extends CommonActivity {
    EditText editEmail, editFullname;
    EditText editPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        allowBack();
        setHeaderTitle(getString(R.string.update_profile));
        editEmail = (EditText)findViewById(R.id.txtEmail);
        editFullname = (EditText)findViewById(R.id.txtFirstname);
        editPhone = (EditText) findViewById(R.id.txtPhone);

        editEmail.setText(common.getSession(ApiParams.USER_EMAIL));
        editFullname.setText(common.getSession(ApiParams.USER_FULLNAME));
        editPhone.setText(common.getSession(ApiParams.USER_PHONE));

        editPhone.setEnabled(false);


        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    public void register(){

        final String fullname = editFullname.getText().toString();
        final String email = editEmail.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.

        if (TextUtils.isEmpty(fullname)) {
            editFullname.setError(getString(R.string.valid_required_fullname));
            focusView = editFullname;
            cancel = true;
        }
      /*  if (TextUtils.isEmpty(phone)) {
            editPhone.setError("Phone number required");
            focusView = editPhone;
            cancel = true;
        }

        if (phone.length() < 10) {
            editPhone.setError(getString(R.string.valid_required_phone));
            focusView = editPhone;
            focusView.requestFocus();
            return;
        }*/


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



        if (cancel) {
            if (focusView!=null)
                focusView.requestFocus();
        } else {
            HashMap<String,String> params = new HashMap<>();
            params.put("user_fullname",fullname);
            params.put("user_email",email);
            params.put("user_id",common.get_user_id());
            Log.e("PARAMS",params.toString());
            showPrgressBar();


            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.UPDATEPROFILE_URL,params,
                    new VJsonRequest.VJsonResponce(){
                        @Override
                        public void VResponce(String responce) {
                            hideProgressBar();

                            try {
                                Log.e("RESPONSE",responce);
                                JSONObject jsonObject = new JSONObject(responce);

                                if (jsonObject.getInt("responce") == 1) {

                                    common.setSession(ApiParams.USER_FULLNAME, fullname);
                                    common.setSession(ApiParams.USER_EMAIL, email);

                                    MyUtility.showToast("Profile Updated Successfully",UpdateProfileActivity.this);
                                    MyUtility.hideKeyboard(editFullname,UpdateProfileActivity.this);
                                    startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class));

                                   // Intent intent = new Intent(UpdateProfileActivity.this, Profi.class);
                                   // startActivity(intent);

                                } else {

                                    MyUtility.showToast("Failed to update.", UpdateProfileActivity.this);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        @Override
                        public void VError(String responce) {
                            hideProgressBar();
                            common.setToastMessage(responce);
                        }
                    });
        }

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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
