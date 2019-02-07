package com.ziffytech.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.chat.DataProvider;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Bookbycall extends CommonActivity {

    public static ArrayList<DoctorModel> mDoctorArray;
    MyDoctorAdaptercall doctorAdapter;
    RecyclerView recyclerView;
    private TextView tvNotFound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookbycall);
        setHeaderTitle("Book By Call");
        allowBack();
        mDoctorArray = new ArrayList<>();

        setResult(Activity.RESULT_CANCELED);

        tvNotFound = (TextView) findViewById(R.id.tvnotfound);
        tvNotFound.setVisibility(View.GONE);


        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        loadData();

    }

    public void loadData() {
        HashMap<String, String> params = new HashMap<>();
        String city1 = common.getSession(ApiParams.CURRENT_CITY).toString();
        params.put("city", city1);
        String url = ApiParams.GET_LOCALITY;
        showPrgressBar();
        VJsonRequest vJsonRequest = new VJsonRequest(this, url, params,
                new VJsonRequest.VJsonResponce() {

                    @Override
                    public void VResponce(String responce) {
                        Log.e(" GET_DOC_RESPONSE", responce);
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(responce);
                            if (jsonObject.getInt("responce")==1){



                                JSONArray data=jsonObject.optJSONArray("data");

                                for (int i = 0; i <data.length() ; i++) {


                                    JSONObject object=data.getJSONObject(i);


                                    DoctorModel model = new DoctorModel();

                                    model.setDoct_id(object.getString("doct_id"));
                                    Log.e("doct_id", object.getString("doct_id"));

                                    model.setDoct_photo(object.getString("doct_photo"));
                                    Log.e("doct_photo", object.getString("doct_photo"));


                                    model.setDoct_name(object.getString("doct_name"));
                                    Log.e("doct_name", object.getString("doct_name"));

                                    model.setBus_title(object.getString("bus_title"));
                                    Log.e("bus_title", object.getString("bus_title"));

                                    model.setDoct_speciality(object.getString("doct_speciality"));
                                    Log.e("doct_speciality", object.getString("doct_speciality"));

                                    model.setDoct_degree(object.getString("doct_degree"));
                                    Log.e("doct_degree", object.getString("doct_degree"));

                                    model.setDoct_experience(object.getString("doct_experience"));
                                    Log.e("doct_experience", object.getString("doct_experience"));

                                    // model.setDoct_phone(object.getString("doct_phone"));
                                    // Log.e("doct_phone", object.getString("doct_phone"));


                                    model.setDoct_phone(object.getString("doct_phone_help"));
                                    Log.e("doct_phone", object.getString("doct_phone_help"));

                                    model.setBus_google_street(object.getString("city"));
                                    Log.e("city", object.getString("city"));

                                    mDoctorArray.add(model);


                                }

                                doctorAdapter = new MyDoctorAdaptercall(Bookbycall.this, mDoctorArray);
                                recyclerView.setAdapter(doctorAdapter);



                            }else {
                                recyclerView.setVisibility(View.GONE);
                                tvNotFound.setVisibility(View.VISIBLE);
                                tvNotFound.setText("No Data Found");

                            }













                        } catch (JSONException e) {
                            tvNotFound.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void VError(String responce) {
                        tvNotFound.setVisibility(View.GONE);
                        hideProgressBar();
                    }
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.doctor_search_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        item.setTitle("Search ...");
        SearchView sv = new SearchView(getSupportActionBar().getThemedContext());


        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");

                if (!mDoctorArray.isEmpty()) {
                    filter(newText);

                } else {


                }


                return false;
            }
        });


        return true;
    }

    private void filter(String newText) {


        ArrayList<DoctorModel> filterdNames = new ArrayList<>();

        if (!mDoctorArray.isEmpty()) {

            for (DoctorModel m : mDoctorArray) {
                if (m.getDoct_name().toLowerCase().toUpperCase().contains(newText.toLowerCase().toUpperCase())) {
                    filterdNames.add(m);
                } else if (m.getDoct_speciality().toLowerCase().toUpperCase().contains(newText.toLowerCase().toUpperCase())) {
                    filterdNames.add(m);
                }

            }



           /* if (m.get().toLowerCase().contains(newText.toLowerCase())) {
                filterdNames.add(m);
            }*/

        } else {

            MyUtility.showAlertMessage(this, "");
        }

        //calling a method of the adapter class and passing the filtered list
        doctorAdapter.filterList(filterdNames);
    }

    public class MyDoctorAdaptercall extends RecyclerView.Adapter<MyDoctorAdaptercall.ProductHolder> {

        ArrayList<DoctorModel> list;
        Activity activity;
        String type;
        private ArrayList<DoctorModel> mFilteredList;

        public MyDoctorAdaptercall(Activity activity, ArrayList<DoctorModel> list) {
            this.list = list;
            this.activity = activity;
            mFilteredList = list;
        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_doctors_call, parent, false);

            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(final ProductHolder holder, final int position) {
            final DoctorModel categoryModel = mFilteredList.get(position);
            String path = categoryModel.getDoct_photo();

            Picasso.with(activity).load(ConstValue.BASE_URL + "/uploads/profile/" + path).into(holder.icon_image);

            Log.e("Image", ConstValue.BASE_URL + "/uploads/profile/" + path);


            holder.lbl_title.setText(categoryModel.getDoct_name());
            Log.e("lbl_title", categoryModel.getDoct_name());

            holder.lbl_clinic_name.setText("Clinic Name : " + categoryModel.getBus_title());
            Log.e("lbl_clinic_name", categoryModel.getBus_title());

            holder.lbl_speciality.setText(categoryModel.getDoct_speciality());
            Log.e("lbl_speciality", categoryModel.getDoct_speciality());

            holder.lbl_degree.setText(categoryModel.getDoct_degree());
            Log.e("lbl_degree", categoryModel.getDoct_degree());


            holder.book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toast.makeText(activity, "Book by call", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", categoryModel.getDoct_phone(), null));
                    startActivity(intent);

                }

            });


        }

        @Override
        public int getItemCount() {
            return mFilteredList.size();
        }

        public void filterList(ArrayList<DoctorModel> filterdNames) {
            this.mFilteredList = filterdNames;
            notifyDataSetChanged();
        }

        class ProductHolder extends RecyclerView.ViewHolder {
            ImageView icon_image;
            TextView lbl_title;
            TextView lbl_degree;
            TextView lbl_clinic_name;
            TextView lbl_speciality;
            RelativeLayout rel_startchat;
            TextView book;

            public ProductHolder(View itemView) {
                super(itemView);
                icon_image = (ImageView) itemView.findViewById(R.id.imageView);
                lbl_title = (TextView) itemView.findViewById(R.id.title);
                lbl_clinic_name = (TextView) itemView.findViewById(R.id.clinic);
                lbl_degree = (TextView) itemView.findViewById(R.id.degree);
                lbl_speciality = (TextView) itemView.findViewById(R.id.specilaity);
                rel_startchat = (RelativeLayout) itemView.findViewById(R.id.rel_startchat);
                book = (TextView) itemView.findViewById(R.id.book);
            }
        }


    }

}
