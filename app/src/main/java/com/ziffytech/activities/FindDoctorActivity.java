package com.ziffytech.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.adapters.CategoryAdapter;
import com.ziffytech.models.CategoryModel;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Mahesh on 16/10/17.
 */

public class     FindDoctorActivity extends CommonActivity {

    ArrayList<CategoryModel> categoryArray;
    RecyclerView categoryRecyclerView;
    CategoryAdapter categoryAdapter;
    ProgressBar progressBar1;
    Toolbar toolbar;
    SeekBar seekBar;
    TextView txtArea;
    Switch switch1;
    AutoCompleteTextView searchText;
    Bundle bundleloclaity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_layout);
        allowBack();
        setHeaderTitle("Select Category");
        categoryArray = new ArrayList<>();
        TextView textView1 = (TextView)findViewById(R.id.textView);
        //textView1.setTypeface(getCustomFont());
        bindView();
        loadData();
    }
    public void bindView(){

        txtArea = (TextView) findViewById(R.id.textArea);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        switch1 = (Switch) findViewById(R.id.switch1);
        searchText = (AutoCompleteTextView) findViewById(R.id.search_keyword);



        categoryRecyclerView = (RecyclerView) findViewById(R.id.rv_artist);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        categoryRecyclerView.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this,categoryArray);
        categoryRecyclerView.setAdapter(categoryAdapter);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        setProgressBarAnimation(progressBar1);

        seekBar.setMax(100);
        txtArea.setText(common.getSessionInt("radius") + getString(R.string.KM));
        seekBar.setProgress(common.getSessionInt("radius"));

        if (!common.containKeyInSession("nearby_enable")) {
            common.setSessionBool("nearby_enable", true);
        }
        switch1.setChecked(common.getSessionBool("nearby_enable"));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                common.setSessionBool("nearby_enable", b);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                common.setSessionInt("radius", i);
                txtArea.setText(common.getSessionInt("radius") + getString(R.string.KM));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }

    public void SearchButtonClick(View view){


        Intent intent = new Intent(FindDoctorActivity.this,BookActivity.class);
        Bundle b = new Bundle();
        if (!TextUtils.isEmpty(searchText.getText()))
        {
            b.putString("search", searchText.getText().toString());
        }
        if (bundleloclaity!=null)
        {
            b.putString("lat",bundleloclaity.getString("latitude"));
            b.putString("lon",bundleloclaity.getString("longitude"));
            b.putString("locality",bundleloclaity.getString("locality"));
            b.putString("locality_id",bundleloclaity.getString("locality_id"));
            b.putString("Activity","3");
        }
        intent.putExtras(b);
        startActivity(intent);

    }
    public void LocalityViewClick(View view)
    {
        Log.e("######","LOCALITY CLICKED");
        Intent intent = new Intent(FindDoctorActivity.this,LocationActivity.class);
        startActivity(intent);
    }




    public void loadData()
    {

        Log.e("LOAD DATA","True");

        HashMap<String,String> params = new HashMap<>();
        params.put("cat_id",getIntent().getStringExtra("id"));
        params.put("city",common.getSession("city"));
        Log.e("PARAMS", String.valueOf(params));

        VJsonRequest vJsonRequest = new VJsonRequest(FindDoctorActivity.this, ApiParams.CATEGORY_LIST,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {
                        Log.e("CATEGORY_DOCTOR_LIST",responce);

                        try {

                            Log.e("TRY","true");
                            JSONObject jsonObject=new JSONObject(responce);

                        if(jsonObject.getInt("responce")==1){


                            JSONArray data=jsonObject.getJSONArray("data");

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<CategoryModel>>()
                            {
                            }.getType();
                            categoryArray.clear();
                            //Collections.sort(categoryArray, String.CASE_INSENSITIVE_ORDER);
                            categoryArray.addAll((Collection<? extends CategoryModel>) gson.fromJson(data.toString(), listType));
                            categoryAdapter.notifyDataSetChanged();
                            progressBar1.setVisibility(View.GONE);
                        }else {

                            AlertDialog.Builder ad=new AlertDialog.Builder(FindDoctorActivity.this);
                            ad.setMessage("Doctors not available");

                            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                  Intent intent=new Intent(FindDoctorActivity.this,DoctorMainCategoriesActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    startActivity(intent);
                                }
                            });
                            AlertDialog dialog=ad.create();
                            dialog.show();



                        }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                        progressBar1.setVisibility(View.GONE);
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_location:

                return true;
                default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void searchViewClick(View view){
        Intent intent = new Intent(FindDoctorActivity.this,SearchActivity.class);
        Log.e("FILTER","true");
        startActivity(intent);
    }
}
