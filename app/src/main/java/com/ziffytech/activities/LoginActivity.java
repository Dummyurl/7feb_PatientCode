package com.ziffytech.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.Preferences;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;


public class LoginActivity extends CommonActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, BaseSliderView.OnSliderClickListener {
    private static final int REQ_CODE = 9001;
    EditText editEmail, editPassword;
    SignInButton signInButton;
    Button fb, gp, btn_email;
    LoginButton loginButton;
    LinearLayout layout_register;
    GoogleApiClient googleApiClient;
    RelativeLayout layout;
    SliderLayout mDemoSlider;
    RelativeLayout rel_bottom_login;
    LinearLayout lin_email_login, lin_slider;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private SharedPreferences sharedPreferences;
    TextView text_register;
    String phoneNumberString;
    private String social_name,
            social_email,
            social_id,
            social_image,
            social_phone,
            social_type,
            login_by;
    private Boolean saveLogin;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    public static int APP_REQUEST_CODE = 99;
    boolean isLoginwithOTP=false;


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        printKeyHash();

        com.facebook.accountkit.AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            // if previously logged in, proceed to the account activity
            launchAccountActivity();
        }

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        layout = (RelativeLayout) findViewById(R.id.activity_login);
        layout_register = (LinearLayout) findViewById(R.id.layout_register);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.check_remember_me);
        hideKeyboard(layout, LoginActivity.this);

        setHeaderTitle(getString(R.string.login_now));
        editEmail = (EditText) findViewById(R.id.txtEmail);
        editPassword = (EditText) findViewById(R.id.txtPassword);
        text_register=findViewById(R.id.text_register);
        Button button = (Button) findViewById(R.id.button);
        Button forgot = (Button) findViewById(R.id.button4);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });


       /* editEmail.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        //Do something here
                        break;

                    default:
                        break;
                }
            }

        });
*/







        text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));


            }
        });



        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();


        sharedPreferences = getSharedPreferences(Preferences.MyPREFERENCES, Context.MODE_PRIVATE);


        callbackManager = CallbackManager.Factory.create();


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        setUpViews();



        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            editEmail.setText(loginPreferences.getString("username", ""));
            editPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                // Check for a valid email address.
               /* if (TextUtils.isEmpty(email)) {
                    // common.setToastMessage(getString(R.string.valid_required_email));
                    editEmail.setError(getString(R.string.valid_required_email));
                    editEmail.setFocusable(true);

                    return;

                    // cancel = true;
                }else if (!isValidEmail(email)) {
                    editEmail.setError(getString(R.string.valid_email));
                    editEmail.setFocusable(true);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editPassword.setError(getString(R.string.valid_required_password));
                    editPassword.setFocusable(true);
                    return;
                }

                if (!MyUtility.isConnected(LoginActivity.this)) {
                    MyUtility.showAlertMessage(LoginActivity.this, MyUtility.INTERNET_ERROR);
                    return;
                }




*/


                if (!email.equals("")) {

                    if (isValidEmail(email)) {
                        if (!password.equals("")) {

                            if (saveLoginCheckBox.isChecked()) {
                                loginPrefsEditor.putBoolean("saveLogin", true);
                                loginPrefsEditor.putString("username", email);
                                loginPrefsEditor.putString("password", password);
                                loginPrefsEditor.commit();
                            } else {
                                loginPrefsEditor.clear();
                                loginPrefsEditor.commit();
                            }
                            login(email, password);

                        } else {

                            editPassword.setError("Password Required");
                        }

                    } else {
                        editEmail.setError("Please enter valid email");
                    }

                } else {
                    editEmail.setError("Email Required");

                }


                //login();
            }
        });

        mDemoSlider = (SliderLayout) findViewById(R.id.ziffyslider1);
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.banner4);
        file_maps.put("2", R.drawable.banner2);
        file_maps.put("3", R.drawable.banner3);
        file_maps.put("4", R.drawable.banner);
        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }


    private void setUpViews() {


        loginButton = (LoginButton) findViewById(R.id.login_button);
        signInButton = (SignInButton) findViewById(R.id.g_login);
        signInButton.setOnClickListener(this);
        GoogleSignInOptions signinOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signinOption).build();


        loginButton.setReadPermissions("user_friends");
        //  loginButton.setFrag
        //loginButton.registerCallback(callbackManager, callback);

        fb = (Button) findViewById(R.id.fb);
        gp = (Button) findViewById(R.id.gp);

        fb.setOnClickListener(this);
        gp.setOnClickListener(this);


        facebook();

        rel_bottom_login = (RelativeLayout) findViewById(R.id.rel_bottom_login);
        rel_bottom_login.setOnClickListener(this);
        lin_email_login = (LinearLayout) findViewById(R.id.lin_email_login);
        lin_slider = (LinearLayout) findViewById(R.id.lin_slider);
        btn_email = (Button) findViewById(R.id.btn_email);
        btn_email.setOnClickListener(this);


    }

    private void facebook() {

        loginButton.setReadPermissions(Arrays.asList("public_profile, email"));
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse response) {

                                Log.e("FB", response.toString());

                                try {
                                    social_id = jsonObject.getString("id");
                                    social_email = jsonObject.getString("email");
                                    social_name = jsonObject.getString("name");
                                    social_type = "2";
                                    login_by = "Facebook";
                                    LoginManager.getInstance().logOut();

                                    SocialLogin(1, phoneNumberString);

                                } catch (Exception e) {

                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code

            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "error to Login Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("AT", "onActivityResult");


        if (requestCode == REQ_CODE) {
            Log.e("AT", "google");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }

        // Newly Added Code

        else if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
               // Toast.makeText(this, "Added Contact", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
               // Toast.makeText(this, "Cancelled Added Contact", Toast.LENGTH_SHORT).show();
            }

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        //Facebook account kit
        // confirm that this response matches your request
        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                // display login error
                String toastMessage = loginResult.getError().getErrorType().getMessage();
              //  Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();

            } else if (loginResult.getAccessToken() != null) {
                // on successful login, proceed to the account activity
                launchAccountActivity();
            }
        }else {

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.g_login:
                // signIn();
                break;

         /*   case R.id.logout:
                SignOut();
                break;*/

            case R.id.fb:
                loginButton.performClick();
                break;

            case R.id.gp:
                signIn();
                break;

            case R.id.rel_bottom_login:
             /*   rel_bottom_login.setVisibility(View.GONE);
                lin_email_login.setVisibility(View.VISIBLE);
                lin_slider.setVisibility(View.GONE);*/
                break;

            case R.id.btn_email:
                rel_bottom_login.setVisibility(View.GONE);
                lin_email_login.setVisibility(View.VISIBLE);
                lin_slider.setVisibility(View.GONE);
                break;

        }
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

/*
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            loginButton.setReadPermissions(Arrays.asList("email,user_birthday,phone,name"));

            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            //displayMessage(profile);

            Log.e("AT", "onSuccess");

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code

                            JSONObject c = response.getJSONObject();

                            Log.e("AT", c.toString());


                            try {

                                social_id = c.getString("id");
                                social_name = c.getString("name");
                                social_email = c.getString("email");
                                social_type = "2";
                                social_phone = c.getString("phone");

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                            login_by = "Facebook";


                             SocialLogin();


                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender");
            request.setParameters(parameters);
            request.executeAsync();


        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };*/

    private void SignOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                // UpdateUI(false);
            }
        });


    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount account = result.getSignInAccount();


       /*     String emaill = account.getEmail();
            String namee = account.getDisplayName();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Preferences.Email, emaill);
            editor.putString(Preferences.Name, namee);
            editor.commit();*/
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                }
            });

            if (result != null) {
                social_id = account.getId();
                social_email = account.getEmail();
                social_name = account.getDisplayName();
                social_type = "1";
                login_by = "Gmail";
                SocialLogin(1, phoneNumberString);

            } else {
                Toast.makeText(this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
            //  UpdateUI(true);
        } else {
            // UpdateUI(false);
            Toast.makeText(this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public void login(String email, String password) {

        showPrgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_email", email);
        params.put("user_password", password);

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.LOGIN_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Log.e("RESPONSE LOGIN", responce);

                        hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);


                            if (userdata.getInt("responce") == 1) {

                                JSONObject data = userdata.getJSONArray("data").getJSONObject(0);

                                common.setSession(ApiParams.COMMON_KEY, data.getString("user_id"));
                                common.setSession(ApiParams.USER_EMAIL, data.getString("user_email"));
                                common.setSession(ApiParams.USER_FULLNAME, data.getString("user_fullname"));
                                common.setSession(ApiParams.USER_LOCATION, data.getString("location"));
                                common.setSession(ApiParams.USER_PHONE, data.getString("user_phone"));
                                common.setSession(ApiParams.USER_SALUTATION, data.getString("salutations"));
                                common.setSession(ApiParams.USER_GENDER, data.getString("gender"));
                                common.setSession(ApiParams.USER_MARITAL_STATUS, data.getString("matrial_status"));
                                common.setSession(ApiParams.USER_BLOOD_GROUP, data.getString("blood_group"));
                                common.setSession(ApiParams.USER_DOB, data.getString("user_bdate"));
                                common.setSession(ApiParams.USER_HEIGHT, data.getString("height"));
                                common.setSession(ApiParams.USER_WEIGHT, data.getString("weight"));
                                common.setSession(ApiParams.USER_EMERGENCY_CONTACT, data.toString());

                                Log.e("data", data.toString());
                                Log.e("location", common.getSession(ApiParams.USER_LOCATION));

                                if (data.getString("salutations").equalsIgnoreCase("")) {

                                        /*finish();
                                        Intent intent = new Intent(LoginActivity.this, PersonalDetailsActivity.class);
                                        intent.putExtra("new","new");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);*/


                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    //intent.putExtra("new","new");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                  /*  Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                                    contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                                    contactIntent
                                            .putExtra(ContactsContract.Intents.Insert.NAME, "Ziffytech Healthcare")
                                            .putExtra(ContactsContract.Intents.Insert.PHONE, "8432229000");

                                    startActivityForResult(contactIntent, 1);
*/

                                } else {

                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } else {
                                MyUtility.showAlertMessage(LoginActivity.this, "Invalid Credentials");
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void VError(String responce) {

                        hideProgressBar();
                        //     Log.e("ERROR", responce);
                    }


                });

    }

    public void registerClick(View v) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);

    }

    private void SocialLogin(int number, String phoneNumberString2) {


//        Log.e("Data", social_name + "/" + social_email + "/" + social_id + "/" + social_type);


        HashMap<String, String> params = new HashMap<>();

//        params.put("user_fullname", social_name);

        if (number==1){
            params.put("user_email", social_email);
        }else if (number==2){
            params.put("user_phone", phoneNumberString2);
        }

//        params.put("social_id", social_id);
//        params.put("social_type", social_type);
//        params.put("user_phone", "");
//

        Log.e("PARAMS", params.toString());

        CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.FACEBOOK_LOGIN, params, this.createRequestSuccessListenerFB(), this.createRequestErrorListenerFB());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(customRequestForString);


    }


    private Response.Listener<String> createRequestSuccessListenerFB() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("FB_RESPONSE", response.toString());


                /*{"data":[{"id":"195","user_id":"12071","allergies":"","c_medicine":"","p_medicine":"","diseases":"","injuries":"","surgeries":"","occupation":"IT","smoking":"","alcohol":"","activity_level":"","food":"","status":"1","created":"0000-00-00 00:00:00","modified":"2018-10-23 15:54:19","user_email":"lochan17thorat@gmail.com","salutations":"Miss.","user_fullname":"lochan thorat","user_password":"e10adc3949ba59abbe56e057f20f883e","user_type_id":"2","user_bdate":"1994-11-18","user_phone":"9921120071","gender":"1","blood_group":"O+","matrial_status":"1","height":"5'10\"","weight":"62","user_age":"24","e_contact":"3214569870","location":"Dhankawadi, Pune, Maharashtra, India","adhar_number":"","phone_number":"9921120071","is_email_varified":"0","varified_token":"5bcf03615c4645bcf03615c467","user_gcm_code":"dpAF5QS8Bzk:APA91bHoCE1C9TpGpu1Z3wf5oQjtvDnnZ9y1V28MYebbG6KeWfdj8orE3_TNyfgxN5ogFmqIJBGoeTLK2o4-y3Nj92t9vdLunJCWvBOz1wJi3eF4lXAPvuJC4AcW-HZnS9BUK0VI9Ecj","fcm_id":"","user_ios_token":"","user_social_id":"","user_social_type":"","user_status":"1","user_image":"avatar.png","user_city":"0","user_country":"0","user_state":"0","created_at":"2018-10-23 15:21:28","lab_notification":"0","pharma_notification":"0","test_status_per":"0","ehr_test_history_permission":"0","ehr_test_dummy_token":"DUMMY","user_otp":"0"}],"responce":1,"message":"Successfully"}*/


                try {

                    JSONObject userData = new JSONObject(response);


                    if (userData.getString("message").equals("Successfully")){

                        Log.e("RESULT", "1");

                        JSONArray data = userData.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {

                            JSONObject jsonObject = data.getJSONObject(i);


                            common.setSession(ApiParams.COMMON_KEY, jsonObject.getString("user_id"));
                            Log.e("user_id", jsonObject.getString("user_id"));

                            common.setSession(ApiParams.USER_EMAIL, jsonObject.getString("user_email"));
                            Log.e("user_email", jsonObject.getString("user_email"));

                            common.setSession(ApiParams.USER_FULLNAME, jsonObject.getString("user_fullname"));
                            Log.e("user_fullname", jsonObject.getString("user_fullname"));

                            common.setSession(ApiParams.USER_PHONE, jsonObject.getString("user_phone"));
                            Log.e("user_phone", jsonObject.getString("user_phone"));

                            common.setSession(ApiParams.USER_JSON_DATA, jsonObject.toString());

                            common.setSession(ApiParams.USER_WEIGHT, jsonObject.getString("weight"));
                            Log.e("weight", jsonObject.getString("weight"));

                            common.setSession(ApiParams.USER_HEIGHT, jsonObject.getString("height"));
                            Log.e("height", jsonObject.getString("height"));

                            common.setSession(ApiParams.USER_BLOOD_GROUP, jsonObject.getString("blood_group"));
                            Log.e("blood_group", jsonObject.getString("blood_group"));

                            common.setSession(ApiParams.USER_MARITAL_STATUS, jsonObject.getString("matrial_status"));
                            Log.e("matrial_status", jsonObject.getString("matrial_status"));

                            common.setSession(ApiParams.USER_DOB, jsonObject.getString("user_bdate"));
                            Log.e("user_bdate", jsonObject.getString("user_bdate"));

                            common.setSession(ApiParams.USER_LOCATION, jsonObject.getString("location"));
                            Log.e("user_fullname", jsonObject.getString("user_fullname"));

                            common.setSession(ApiParams.USER_GENDER, jsonObject.getString("gender"));
                            Log.e("location", jsonObject.getString("location"));

                            common.setSession(ApiParams.USER_SALUTATION, jsonObject.getString("salutations"));
                            Log.e("salutations", jsonObject.getString("salutations"));


                            if (jsonObject.getString("user_phone").equals("")) {

                                Log.e("USER PHONE", "False");

                                Intent intent1 = new Intent(LoginActivity.this, UpdateProfileActivity.class);
                                startActivity(intent1);

                            } else {
                                Log.e("USER PHONE", "True");

                                finish();
                                Intent intent11 = new Intent(LoginActivity.this, MainActivity.class);
                                intent11.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent11);
                            }
                        }


                    } else if (userData.getString("message").equals("No any record")) {

                        Log.e("Result", "0");

                        String msg = userData.getString("message");

                        if (msg.equals("No any record")) {

                            if (isLoginwithOTP){

                                finish();
                                Log.e("RESPONSE", "False");
                                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                intent.putExtra("name", social_name);
                                intent.putExtra("phone", phoneNumberString);
                                intent.putExtra("Activity", "facebook");
                                startActivity(intent);
                                AccountKit.logOut();

                            }else {


                                finish();
                                Log.e("RESPONSE", "False");
                                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                intent.putExtra("name", social_name);
                                intent.putExtra("email", social_email);
                                intent.putExtra("Activity", "facebook");
                                startActivity(intent);

                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
    }


    private Response.ErrorListener createRequestErrorListenerFB() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##", "##" + error.toString());
                //   hideProgressBar();
            }
        };
    }


    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.ziffytech", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("#####" + "KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("####" + "KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("####" + "KeyHash:", e.toString());
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onBackPressed() {
        rel_bottom_login.setVisibility(View.VISIBLE);
        lin_email_login.setVisibility(View.GONE);
        lin_slider.setVisibility(View.VISIBLE);
    }




    private void onLogin(final LoginType loginType)
    {

        isLoginwithOTP=true;

        Log.e("Login button pressed","Yes");

        //UIManager uiManager;
        // Skin is CLASSIC, CONTEMPORARY, or TRANSLUCENT
        // Tint is WHITE or BLACK
        // TintIntensity is a value between 55-85%
        //uiManager = new SkinManager(Skin <CLASSIC>, @ColorInt int <#ff422a>,@DrawableRes int <backgroundImage>, Tint <BLACK>,double <55%>);
        // create intent for the Account Kit activity
        final Intent intent = new Intent(this, AccountKitActivity.class);
        // configure login type and response type
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );

        //configurationBuilder.setUIManager(uiManager);

        final AccountKitConfiguration configuration = configurationBuilder.build();
        // launch the Account Kit activity
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    public void onPhoneLogin(View view) {
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("onPhoneLogin");
        onLogin(LoginType.PHONE);

    }



    private void launchAccountActivity()
    {


        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (phoneNumber != null) {
                     phoneNumberString = phoneNumber.toString().replace("+91","");
                    // Toast.makeText(LoginActivity.this, ""+phoneNumberString, Toast.LENGTH_SHORT).show();
                    SocialLogin(2,phoneNumberString);
                }

                // Get email
                String email = account.getEmail();
            }

            @Override
            public void onError(final AccountKitError error) {
                // Handle Error
            }
        });


    }




}
