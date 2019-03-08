package com.ziffytech.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.accountkit.AccountKit;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.BuildConfig;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.Wallet.WalletActivity;
import com.ziffytech.adapters.CategoryAdapter;
import com.ziffytech.adapters.ThyroPackageAdapter;
import com.ziffytech.chat.DataProvider;

import com.ziffytech.configfcm.MyFirebaseRegister;
import com.ziffytech.models.CategoryModel;
import com.ziffytech.models.TopfivepickModel;
import com.ziffytech.models.VersionModel;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.SaveSharedPreference;
import com.ziffytech.util.VJsonRequest;
import com.ziffytech.vaccination.VaccinationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class MainActivity extends CommonActivity implements NavigationView.OnNavigationItemSelectedListener,
             BaseSliderView.OnSliderClickListener, BottomNavigationView.OnNavigationItemSelectedListener
{

    ArrayList<CategoryModel> categoryArray;
    RecyclerView categoryRecyclerView;
    CategoryAdapter categoryAdapter;
    ProgressBar progressBar1;
    Toolbar toolbar;
    static String user_id;
    CardView findDoctor,medicalRecords,remainders,orderMedicine;
    boolean isLabBooked=false,isPharma=false,IsChat=false;
    private String bookingNumber="",emergencyNumber="";
    TextView labNotify,phrmaNotify,chatNotify;
    String status = "0";
    GridView simpleList;
    ArrayList<Item> birdList=new ArrayList<>();
    SliderLayout mDemoSlider;
    BottomNavigationView bottom_navigation;
    int mMenuId;
    boolean isEventConfirm=false;
    boolean isChecked = false;
    ArrayList<TopfivepickModel> tyro5modelArrayList;
    RecyclerView recyclerViewPackage,recyclerViewPackage2;
    LinearLayoutManager layoutManager,layoutManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerViewPackage = (RecyclerView) findViewById(R.id.recyclerview_package_thyro);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPackage.setHasFixedSize(true);
        recyclerViewPackage.setLayoutManager(layoutManager);

        HashMap<String, String> params2 = new HashMap<String, String>();
        CustomRequestForString customRequestForString2 = new CustomRequestForString(Request.Method.POST, ApiParams.GET_THYRO_5_PACKAGES, params2, this.createRequestSuccessListenerThyropack(), this.createRequestErrorListenerThyropack());
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(customRequestForString2);
        showPrgressBar();

        user_id=common.get_user_id();

        loadversioncode();

        // GiveAlertDialog();

        getHash();

        //To stop sms reciver
        /* PackageManager pm  = MainActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this, SmsReceiver.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);*/

        if(common.getSession(ApiParams.CURRENT_CITY)!=null && !common.getSession(ApiParams.CURRENT_CITY).equalsIgnoreCase(""))
        {
            setHeaderTitle("ZiffyTech",common.getSession(ApiParams.CURRENT_CITY));

        }else{
            common.setSession(ApiParams.CURRENT_CITY,"Pune");
            setHeaderTitle("ZiffyTech",common.getSession(ApiParams.CURRENT_CITY));

        }
        categoryArray = new ArrayList<>();
        allowBack();


       /*   String token= FirebaseInstanceId.getInstance().getToken();
        Log.e("TOKEN",token);*/


        if (!common.getSessionBool("fcm_registered") && common.is_user_login()){
            FirebaseApp.initializeApp(MainActivity.this);
            MyFirebaseRegister fireReg = new MyFirebaseRegister(MainActivity.this);
            fireReg.RegisterUser(common.get_user_id());
        }

        TextView textView1 = (TextView)findViewById(R.id.textView);
        textView1.setTypeface(getCustomFont());
        //loadData();

        labNotify=(TextView)findViewById(R.id.labNotify);
        phrmaNotify=(TextView)findViewById(R.id.pharmaNotify);
        chatNotify=(TextView)findViewById(R.id.chatNotify);


        bottom_navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottom_navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        labNotify.setText("");
        phrmaNotify.setText("");
        labNotify.setVisibility(View.GONE);
        phrmaNotify.setVisibility(View.GONE);
        chatNotify.setVisibility(View.GONE);

        bindView();


        Log.e("LoggedUserData",common.getSession(ApiParams.USER_JSON_DATA));

        try{

            JSONObject data=new JSONObject(common.getSession(ApiParams.USER_JSON_DATA));

          /*  if(data.getString("salutations").equalsIgnoreCase(""))
            {

                *//* //  MyUtility.showToast("Complete your profile!",MainActivity.this);
                Intent intent = new Intent(MainActivity.this, PersonalDetailsActivity.class);
                intent.putExtra("new","new");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                *//*

                finish();
                Intent intent = new Intent(MainActivity.this, Otppage.class);
                //intent.putExtra("new","new");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }*/

        }catch (Exception e){


        }

        mDemoSlider = (SliderLayout)findViewById(R.id.ziffyslider);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1",R.drawable.newbanners1);
        file_maps.put("2",R.drawable.newbanners2);
        file_maps.put("3",R.drawable.newbanners3);

        for(String name : file_maps.keySet())
        {
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
        bottom_navigation.setOnNavigationItemSelectedListener(this);
        bottom_navigation.setSelected(true);

    }


    public void bindView()
    {
        //Will see it later
        categoryRecyclerView = (RecyclerView) findViewById(R.id.rv_artist);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(this,categoryArray);
        categoryRecyclerView.setAdapter(categoryAdapter);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        setProgressBarAnimation(progressBar1);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        findDoctor=(CardView)findViewById(R.id.cv_find_a_doctor);
        medicalRecords=(CardView)findViewById(R.id.cv_records);
        remainders=(CardView)findViewById(R.id.cv_remainders);
        orderMedicine=(CardView)findViewById(R.id.cv_order_medicine);


        findDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,DoctorMainCategoriesActivity.class);
                startActivity(intent);

            }
        });

        medicalRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Intent intent = new Intent(MainActivity.this,MedicalRecords.class);
                //startActivity(intent);

                Intent intent = new Intent(MainActivity.this,AllMedicalReports.class);
                startActivity(intent);

            }
        });

        remainders.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, com.ziffytech.remainder.RemainderActivity.class);
                startActivity(intent);

            }
        });

        orderMedicine.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this,MedicineOrderAdressActivity.class);
                startActivity(intent);

            }
        });

        CardView cv_book=(CardView)findViewById(R.id.cv_book);
        cv_book.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*String phone = "+91"+bookingNumber;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);*/

                Intent intent = new Intent(MainActivity.this,Bookbycall.class);
                startActivity(intent);

            // MyUtility.showToast("Coming Soon",MainActivity.this);

            }
        });

        CardView cv_chat=(CardView)findViewById(R.id.cv_chat);
        cv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);*/

              MyUtility.showAlertMessage(MainActivity.this,"Thanks for showing interest for chat.Our customer care will get back to you shortly.");
            }
        });

        CardView cv_askquetions=(CardView)findViewById(R.id.cv_askquetions);
        cv_askquetions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AskQuestionActiivty.class);
                startActivity(intent);

            }
        });


        CardView btnEmergency=(CardView)findViewById(R.id.cv_emer);
        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = "+91"+emergencyNumber;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        CardView bookLab=(CardView)findViewById(R.id.cv_booklab);
        bookLab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ZiffyLabBooking.class);
                intent.putExtra("status",status);
                startActivity(intent);

                /*Intent intent = new Intent(MainActivity.this, TestingBooking.class);
                startActivity(intent);
                finish();*/

                /*Intent intent = new Intent(MainActivity.this, RecommmendedTest.class);
                startActivity(intent);*/

            }
        });
    }


    @Override
    protected void onResume()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();
        if (!common.is_user_login())
        {
            nav_Menu.findItem(R.id.nav_appointment).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            nav_Menu.findItem(R.id.nav_password).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
            nav_Menu.findItem(R.id.nav_login).setVisible(true);
            nav_Menu.findItem(R.id.nav_wallet).setVisible(true);
            nav_Menu.findItem(R.id.nav_vaccination).setVisible(false);
            nav_Menu.findItem(R.id.nav_history).setVisible(false);
            // nav_Menu.findItem(R.id.nav_refer).setVisible(false);
            // navHeader.findViewById(R.id.txtFullName).setVisibility(View.GONE);
            // navHeader.findViewById(R.id.textEmailId).setVisibility(View.GONE);

        }else{
            nav_Menu.findItem(R.id.nav_appointment).setVisible(true);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
            nav_Menu.findItem(R.id.nav_password).setVisible(true);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
            nav_Menu.findItem(R.id.nav_vaccination).setVisible(false);
            nav_Menu.findItem(R.id.nav_history).setVisible(false);
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
            nav_Menu.findItem(R.id.nav_wallet).setVisible(true);
            //nav_Menu.findItem(R.id.nav_refer).setVisible(true);
            //navHeader.findViewById(R.id.txtFullName).setVisibility(View.VISIBLE);
            //navHeader.findViewById(R.id.textEmailId).setVisibility(View.VISIBLE);
            //((TextView)navHeader.findViewById(R.id.txtFullName)).setText(common.getSession(ApiParams.USER_FULLNAME));
            //((TextView)navHeader.findViewById(R.id.textEmailId)).setText(common.getSession(ApiParams.USER_EMAIL));
        }
        super.onResume();
    }

    public void loadData()
    {
        VJsonRequest vJsonRequest = new VJsonRequest(MainActivity.this, ApiParams.CATEGORY_LIST, new VJsonRequest.VJsonResponce()
                {
                    @Override
                    public void VResponce(String responce)
                    {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<CategoryModel>>() {}.getType();
                        categoryArray.clear();
                        categoryArray.addAll((Collection<? extends CategoryModel>) gson.fromJson(responce, listType));
                        categoryAdapter.notifyDataSetChanged();
                        progressBar1.setVisibility(View.GONE);

                    }
                    @Override
                    public void VError(String responce) {
                        progressBar1.setVisibility(View.GONE);
                    }
                });
    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_password) {
            Intent intent = new Intent(MainActivity.this,ChangePasswordActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_appointment) {
            Intent intent = new Intent(MainActivity.this,MyAppointmentsActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_history) {
            Intent intent = new Intent(MainActivity.this,LabTestHistoryActivity.class);
            startActivity(intent);

        }else if(id == R.id.nav_logout){
            common.logOut();
            AccountKit.logOut();
            SaveSharedPreference.clearUserSession(MainActivity.this);

        }else if(id == R.id.nav_login){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_share){
            shareApp();
        }else if(id == R.id.nav_rating){
            reviewOnApp();
        }else if(id == R.id.nav_wallet){
            Intent intent=new Intent(MainActivity.this,WalletActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_vaccination){
            Intent intent=new Intent(MainActivity.this,VaccinationActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_refer){
            shareLongDynamicLink();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        Menu menu = bottom_navigation.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++)
        {
            MenuItem item1 = menu.getItem(i);
            boolean shouldBeChecked = item1.getItemId() == item.getItemId();
            if (shouldBeChecked) {
                item.setChecked(true);

            }else {
                item.setChecked(false);
            }
        }

        /*  for (int i = 0; i < bottom_navigation.getMenu().size(); i++)
        {
            MenuItem menuItem = bottom_navigation.getMenu().getItem(i);
            isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }*/

        switch (item.getItemId())
        {
            case R.id.navigation_home:
                {
                    recreateActivityCompat(MainActivity.this);
                }
            break;
            case R.id.navigation_aboutus:
            {
                String url = "https://www.ziffytech.com/About_Us";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
            break;
            case R.id.navigation_noti:
            {
                Toast.makeText(this, "No New Notifications...", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.navigation_profile:
             {
                Intent i = new Intent(this,ProfileActivity.class);
                startActivity(i);
                finish();
            }
            break;
        }
        return true;

    }

    private void shareLongDynamicLink()
    {
            Intent intent = new Intent();
            String msg = "Checkout Ziffytech App: " + buildDynamicLink();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, msg);
            intent.setType("text/plain");
            startActivity(intent);
    }



    private String buildDynamicLink(/*String link, String description, String titleSocial, String source*/)
    {
        //more info at https://firebase.google.com/docs/dynamic-links/create-manually

        String path = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setDynamicLinkDomain("ziffytechhealthcare.page.link")
                .setLink(Uri.parse("https://WWW.ziffytech.com/ZIFFY"+common.getSession("user_id")))
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build()) //com.melardev.tutorialsfirebase
                .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder().setTitle("Refer and Earn").setDescription("Get Rs.50 for first transaction").build())
                .setGoogleAnalyticsParameters(new DynamicLink.GoogleAnalyticsParameters.Builder().setSource("AndroidApp").build())
                .buildDynamicLink().getUri().toString();
        return path;
    }


    public void searchViewClick(View view)
    {
        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
        startActivity(intent);
    }

    public void shareApp()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @SuppressLint("WrongConstant")
    public void reviewOnApp()
    {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        login();
        try{

            DataProvider db=new DataProvider();
            if(db.isChatMsgPresent(this)){

                chatNotify.setText("1");
                chatNotify.setVisibility(View.VISIBLE);
            }else{
                chatNotify.setVisibility(View.GONE);

            }

        }catch (Exception e){
            chatNotify.setVisibility(View.GONE);

            e.printStackTrace();
        }
    }

    public  void getHash()
    {
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


    public void login()
    {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());
        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_CONTACTS, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce)
                    {

                        Log.e("MEDICINE NOTIFICATION",responce);
                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);
                             // Log.e("WALLET AMOUNT", String.valueOf(ApiParams.ZIFFY_WALLET_AMT));
                            common.setSession(ApiParams.ZIFFY_WALLET_AMT,userdata.getString("wallet_amt"));
                            Log.e("WALLET AMOUNT",common.getSession(ApiParams.ZIFFY_WALLET_AMT));
                            Log.e("WALLET AMOUNT###",userdata.getString("wallet_amt"));
                            if(userdata.getInt("responce")==1){
                            //    Log.e("WALLET AMOUNT", String.valueOf(ApiParams.ZIFFY_WALLET_AMT));
                                JSONArray arr=userdata.getJSONArray("data");
                                for(int i=0;i<arr.length();i++)
                                {
                                    JSONObject obj=arr.getJSONObject(i);
                                    bookingNumber=obj.getString("book_no");
                                    emergencyNumber=obj.getString("emergency_no");
                                }
                                //pharma_notification
                                //lab_notification
                                if(userdata.has("user")) {

                                    JSONObject data = userdata.getJSONObject("user");

                                    if (!data.getString("pharma_notification").equalsIgnoreCase("0")) {
                                        phrmaNotify.setText("1");
                                        phrmaNotify.setVisibility(View.VISIBLE);

                                    } else {
                                        phrmaNotify.setVisibility(View.GONE);
                                    }

                                    if (!data.getString("lab_notification").equalsIgnoreCase("0")) {

                                        labNotify.setText("1");
                                        labNotify.setVisibility(View.VISIBLE);
                                        status = "1";

                                    } else {
                                        labNotify.setVisibility(View.GONE);
                                    }
                                    common.setSession(ApiParams.COMMON_KEY, data.getString("user_id"));
                                    common.setSession(ApiParams.USER_EMAIL, data.getString("user_email"));
                                    common.setSession(ApiParams.USER_FULLNAME, data.getString("user_fullname"));
                                    common.setSession(ApiParams.USER_PHONE, data.getString("user_phone"));
                                    common.setSession(ApiParams.USER_JSON_DATA, data.toString());
                                }

                            }else{
                               // common.logOut();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void VError(String responce) {

                    }
                });

    }


/*
   private void GiveAlertDialog()
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.alert_dialog_with_imageview, null);
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton)
            {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_id", common.getSession("user_id"));
                params.put("event_id","0");
                CustomRequestForString customRequestForString = new CustomRequestForString(Request.Method.POST, ApiParams.GET_EVENT_HEALTH_CHECKUP, params, this.createRequestSuccessListenerEvent(), this.createRequestErrorListenerEvent());
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(customRequestForString);

            }

            private Response.Listener<String> createRequestSuccessListenerEvent()
            {
                return new Response.Listener<String>()
                 {
                    @Override
                    public void onResponse(String response)
                     {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String result=jsonObject.getString("response");
                            if (result.equals("1"))
                            {
                                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                LayoutInflater inflater = getLayoutInflater();
                                View dialogLayout = inflater.inflate(R.layout.dilog_order_success, null);
                                TextView textView=dialogLayout.findViewById(R.id.txt_info);
                                textView.setText("Thanks for the confirmation!!!!!!!");
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
            }

            private Response.ErrorListener createRequestErrorListenerEvent()
            {
                return new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("##", "##" + error.toString());
                    }
                };
            }

        });
        final AlertDialog alert = dialog.create();
        alert.setView(dialogLayout);
        alert.show();

        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 10000);
    }
*/

    private Response.Listener<String> createRequestSuccessListenerThyropack()
    {
        return new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                Log.e("PACKAGE_RESPONSE_THYRO", response.toString());
                hideProgressBar();
                tyro5modelArrayList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("status");


                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {

                        JSONObject object = jsonArray.getJSONObject(i);

                        TopfivepickModel topfivemodel = new TopfivepickModel();

                        topfivemodel.setThyro_profile_id(object.getString("thyro_profile_id"));
                        Log.e("profile_id", object.getString("thyro_profile_id"));

                        topfivemodel.setTest_code(object.getString("test_code"));
                        Log.e("Test_code", object.getString("test_code"));

                        topfivemodel.setName(object.getString("name"));
                        Log.e("Name", object.getString("name"));

                        topfivemodel.setActual_amount(object.getString("actual_amount"));
                        Log.e("Actual_amount", object.getString("actual_amount"));

                        topfivemodel.setTest_count(object.getInt("test_count"));
                        Log.e("Test_count", String.valueOf(object.getInt("test_count")));

                        topfivemodel.setZiffy_profile_price(object.getString("ziffy_profile_price"));
                        Log.e("Ziffy_profile_price", object.getString("ziffy_profile_price"));

                        tyro5modelArrayList.add(topfivemodel);
                        Log.e("Arraylist", String.valueOf(tyro5modelArrayList));
                    }

                    final ThyroPackageAdapter packageAdapter = new ThyroPackageAdapter(MainActivity.this, tyro5modelArrayList);
                    recyclerViewPackage.setAdapter(packageAdapter);
                    final int scrollSpeed = 150;   // Scroll Speed in Milliseconds
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        int x = 15;        // Pixels To Move/Scroll
                        boolean flag = true;
                        // Find Scroll Position By Accessing RecyclerView's LinearLayout's Visible Item Position
                        int scrollPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                        int arraySize = tyro5modelArrayList.size();  // Gets RecyclerView's Adapter's Array Size

                        @Override
                        public void run() {
                            if (scrollPosition < arraySize) {
                                if (scrollPosition == arraySize - 1) {
                                    flag = false;
                                } else if (scrollPosition <= 1) {
                                    flag = true;
                                }
                                if (!flag) {
                                    try {
                                        // Delay in Seconds So User Can Completely Read Till Last String
                                        TimeUnit.SECONDS.sleep(1);
                                        recyclerViewPackage.smoothScrollToPosition(0);  // Jumps Back Scroll To Start Point
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // Know The Last Visible Item
                                scrollPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                                recyclerViewPackage.smoothScrollBy(x, 1);
                                handler.postDelayed(this, scrollSpeed);
                            }
                        }
                    };
                    handler.postDelayed(runnable, scrollSpeed);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };
    }

    private Response.ErrorListener createRequestErrorListenerThyropack()
    {
        return new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("##", "##" + error.toString());
                hideProgressBar();
                //App.showAlert("Something Went Wrong, Please Try again",MultiTestSearchActivity.this);
            }
        };
    }

    @Override
    public void onSliderClick(BaseSliderView slider) { }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) { }

    public void loadversioncode()
    {

        VJsonRequest vJsonRequest = new VJsonRequest(MainActivity.this, ApiParams.VER_CODE,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce)
                    {

                        try {
                            JSONObject jsonObject=new JSONObject(responce);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++)
                            {

                                if (jsonObject.getInt("response")==1)
                                {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    VersionModel ver = new VersionModel();
                                    ver.setPatient_ver_code(obj.getString("patient_ver_code"));
                                    String app_ver = ver.getPatient_ver_code();
                                    common.setSession(ApiParams.VERSION, app_ver);
                                    CheckUPdate();
                                }
                            }

                        } catch (JSONException e) {

                        }
                    }
                    @Override
                    public void VError(String responce)
                    {
                        Toast.makeText(MainActivity.this, "Error to get version code", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void CheckUPdate()
    {

        VersionModel ver = new VersionModel();
        String appVersionName = BuildConfig.VERSION_NAME;
        String version = ver.getPatient_ver_code();
        Log.e("appVersionName",appVersionName);
        Log.e("VERSION",ApiParams.VERSION);
        //Toast.makeText(this, ""+appVersionName+"Mangesh ver : "+version, Toast.LENGTH_SHORT).show();
        if(!appVersionName.equals(common.getSession(ApiParams.VERSION)))
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Please update your app");
            alertDialog.setMessage("This app version is no longer supported. Please update your app from the Play Store.");
            alertDialog.setPositiveButton("UPDATE NOW", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which)
                {
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ziffytech&" + appPackageName)));
                    } catch (ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ziffytech&" + appPackageName)));
                    }
                }
            });
            alertDialog.show();
        }

    }

    @SuppressLint("NewApi")
    public static final void recreateActivityCompat(final Activity a)
         {
            final Intent intent = a.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            a.finish();
            a.overridePendingTransition(0, 0);
            a.startActivity(intent);
            a.overridePendingTransition(0, 0);
        }
}




