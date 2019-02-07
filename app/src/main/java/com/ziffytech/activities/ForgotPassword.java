package com.ziffytech.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.util.MyUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by admn on 15/11/2017.
 */

public class ForgotPassword extends CommonActivity
{
    EditText editText;
    private static final int MY_SOCKET_TIMEOUT_MS = 2000;


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        setHeaderTitle("Forgot Password");
        allowBack();
        editText = (EditText) findViewById(R.id.txtEmail);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent=new Intent(ForgotPassword.this,LoginActivity.class);
                startActivity(intent);*/
                forgetPassword();
            }
        });
    }

    private void forgetPassword() {
        String email = editText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editText.setError(getString(R.string.valid_required_email));
            return;

        }
        if (!isValidEmail(email)) {
            editText.setError(getString(R.string.valid_email));
            return;
        }


        if (!MyUtility.isConnected(this)) {

            MyUtility.showAlertMessage(this, MyUtility.INTERNET_ERROR);
            return;
        }


        showPrgressBar();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);

        Log.e("PARAMS", params.toString());
        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.FORGOT_PASSWORD_URL, params, this.createRequestSuccessListenerPassword(), this.createRequestErrorListenerPassword());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);



        customRequestForString.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


/*

            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);

            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.FORGOT_PASSWORD_URL, params,
                    new VJsonRequest.VJsonResponce()
                    {
                        @Override
                        public void VResponce(String responce)
                         {
                            hideProgressBar();
                            JSONObject userdata = null;
                            try {
                                userdata = new JSONObject(responce);

                                if(userdata.getInt("responce")==1)
                                {
                                    Log.e("Response",userdata.toString());
                                    MyUtility.showAlertMessage(ForgotPassword.this,userdata.getString("success"));

                                }
                                else if(userdata.getInt("responce")==2)
                                {
                                    Toast.makeText(ForgotPassword.this, "Error In Sending Mail..Try Again Later", Toast.LENGTH_SHORT).show();
                                }
                                else{

                                    MyUtility.showAlertMessage(ForgotPassword.this,"Provided email id not registered with us.");

                                }
                                } catch (JSONException e) {
                                e.printStackTrace();
                                hideProgressBar();
                                MyUtility.showAlertMessage(ForgotPassword.this,MyUtility.DATA_ERROR);

                            }
                        }
                        @Override
                        public void VError(String responce) {
                            hideProgressBar();
                            MyUtility.showAlertMessage(ForgotPassword.this,MyUtility.DATA_ERROR);

                        }
                    });
*/

    }

    private Response.Listener<String> createRequestSuccessListenerPassword() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgressBar();

                Log.e("RESPONSE",response);
                JSONObject userdata = null;
                try {
                    userdata = new JSONObject(response);

                    if(userdata.getInt("responce")==1) {
                        MyUtility.showAlertMessage(ForgotPassword.this,userdata.getString("success"));
                    }
                    else if(userdata.getInt("responce")==2){
                        Toast.makeText(ForgotPassword.this, "Error In Sending Mail..Try Again Later", Toast.LENGTH_SHORT).show();
                    }else{
                        MyUtility.showAlertMessage(ForgotPassword.this,"Provided email id not registered with us.");
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                    hideProgressBar();
                    MyUtility.showAlertMessage(ForgotPassword.this, MyUtility.DATA_ERROR);
                }

            }
        };
    }

    private Response.ErrorListener createRequestErrorListenerPassword() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                Log.i("##", "##" + error.toString());
                //App.showAlert("Something Went Wrong, Please Try again",MultiTestSearchActivity.this);

            }
        };
    }


}
