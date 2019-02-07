package com.ziffytech.activities;

import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.Button;
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


public class MyDoctorsActivity extends CommonActivity implements View.OnClickListener {

    public static ArrayList<DoctorModel> mDoctorArray;
    MyDoctorAdapter doctorAdapter;
    RecyclerView recyclerView;
    private TextView tvNotFound,txtv_filter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctors_askqu);
        setHeaderTitle("Select Doctor");
        allowBack();
        mDoctorArray = new ArrayList<>();

        setResult(Activity.RESULT_CANCELED);

        tvNotFound=(TextView)findViewById(R.id.tvnotfound);
        tvNotFound.setVisibility(View.GONE);


        recyclerView = (RecyclerView)findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        String type=null;
        if(getIntent().hasExtra("from"))
        {

            type ="ask";

        }else{

            type ="chat";
        }

        doctorAdapter = new MyDoctorAdapter(this,mDoctorArray,type);
        recyclerView.setAdapter(doctorAdapter);



        loadData();





    }
    public void loadData(){
        HashMap<String,String> params = new HashMap<>();



        String url=null;
        if(getIntent().hasExtra("from"))
        {
            url = ApiParams.GET_LOCALITY;
            common.setSession(ApiParams.CURRENT_CITY,"Pune");
            params.put("city",common.getSession(ApiParams.CURRENT_CITY));

        }else
        {

            url= ApiParams.GET_CHAT_USER_LIST;
            params.put("user_id",common.get_user_id());
        }

        showPrgressBar();
        VJsonRequest vJsonRequest = new VJsonRequest(this, url,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject=new JSONObject(responce);


                            JSONArray data = jsonObject.getJSONArray("data");
                            mDoctorArray.clear();


                            for (int i = 0; i < data.length(); i++) {


                                JSONObject jsonObject1 = data.getJSONObject(i);



                                if(getIntent().hasExtra("from")){

                                    DoctorModel model = new DoctorModel();
                                    model.setDoct_id(jsonObject1.getString("doct_id"));
                                    model.setDoct_photo(jsonObject1.getString("doct_photo"));
                                    model.setDoct_name(jsonObject1.getString("doct_name"));
                                    model.setBus_title(jsonObject1.getString("bus_title"));
                                    model.setDoct_speciality(jsonObject1.getString("doct_speciality"));
                                    model.setDoct_degree(jsonObject1.getString("doct_degree"));
                                    model.setDoct_experience(jsonObject1.getString("doct_experience"));
                                    model.setBus_google_street(jsonObject1.getString("city"));
                                    Log.e("city",jsonObject1.getString("city"));


                                    mDoctorArray.add(model);

                                }else{

                                    Cursor c = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, jsonObject1.getString("doct_id")), null, null, null, null);

                                    if (c != null && c.getCount() > 0) {
                                        //present
                                    }else{
                                        DoctorModel model = new DoctorModel();
                                        model.setDoct_id(jsonObject1.getString("doct_id"));
                                        model.setDoct_photo(jsonObject1.getString("doct_photo"));
                                        model.setDoct_name(jsonObject1.getString("doct_name"));
                                        model.setBus_title(jsonObject1.getString("bus_title"));
                                        model.setDoct_speciality(jsonObject1.getString("doct_speciality"));
                                        model.setDoct_degree(jsonObject1.getString("doct_degree"));
                                        model.setDoct_experience(jsonObject1.getString("doct_experience"));
                                        model.setBus_google_street(jsonObject1.getString("city"));
                                        mDoctorArray.add(model);
                                    }


                                }



                            }


                           /* Gson gson = new Gson();
                            Type listType = new TypeToken<List<DoctorModel>>() {
                            }.getType();
                            mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(data.toString(), listType));
                           */



                            if(mDoctorArray.isEmpty()){
                                tvNotFound.setVisibility(View.VISIBLE);
                            }else{
                                tvNotFound.setVisibility(View.GONE);
                            }



                            doctorAdapter.notifyDataSetChanged();

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
    public void addToFriend(final DoctorModel model){


        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",common.get_user_id());
        params.put("doct_id",model.getDoct_id());

        String url= ApiParams.ADD_FRIEND;

        showPrgressBar();
        VJsonRequest vJsonRequest = new VJsonRequest(this, url,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {
                        hideProgressBar();


                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            if(jsonObject.getInt("success")==1){

                                Cursor c = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, model.getDoct_id()), null, null, null, null);

                                if (c != null && c.getCount() > 0) {

                                    Log.e("AT", "Friend:Update");

                                    ContentValues values = new ContentValues(8);
                                    values.put(DataProvider.COL_NAME, model.getDoct_name());
                                    values.put(DataProvider.COL_EMAIL, model.getDoct_email());
                                    values.put(DataProvider.COL_IMAGE, model.getDoct_photo());
                                    values.put(DataProvider.COL_GROUP_ID, common.get_user_id());
                                    values.put("social_id", "");
                                    values.put("social_type", "");
                                    values.put("is_online", "");
                                    values.put("is_group", "no");
                                    values.put(DataProvider.COL_IS_TYPING, "no");
                                    values.put(DataProvider.COL_LAST_SEEN, "");
                                    getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, model.getDoct_id()), values, null, null);

                                } else {

                                    Log.e("AT", "Friend:New");

                                    ContentValues values = new ContentValues(11);
                                    values.put(DataProvider.COL_NAME, model.getDoct_name());
                                    values.put(DataProvider.COL_USER_ID, model.getDoct_id());
                                    values.put(DataProvider.COL_GROUP_ID, common.get_user_id());
                                    values.put(DataProvider.COL_EMAIL, model.getDoct_email());
                                    values.put(DataProvider.COL_IMAGE, model.getDoct_photo());
                                    values.put(DataProvider.COL_SOCIAL_ID, "");
                                    values.put(DataProvider.COL_SOCIAL_TYPE, "");
                                    values.put("is_view", "no");
                                    values.put("is_online", "");
                                    values.put("last_seen_time", "");
                                    values.put(DataProvider.COL_LAST_MSG, "");
                                    values.put(DataProvider.COL_IS_TYPING, "no");
                                    values.put("is_group", "no");
                                    values.put(DataProvider.COL_LAST_SEEN, "");
                                    getContentResolver().insert(DataProvider.CONTENT_URI_PROFILE, values);

                                }

                                if (!c.isClosed()) {

                                    c.close();
                                }

                                Intent intent = new Intent();
                                setResult(Activity.RESULT_OK, intent);
                                finish();


                            }else{
                                MyUtility.showToast("Failed to start Chat",MyDoctorsActivity.this);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void VError(String responce) {
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
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");
                //doctorAdapter.getFilter().filter(newText);
                //doctorAdapter.notifyDataSetChanged();
                filter(newText);
                return false;
            }
        });

        return true;
    }

    private void filter(String newText) {


        ArrayList<DoctorModel> filterdNames = new ArrayList<>();

        for (DoctorModel m :mDoctorArray) {
            if (m.getDoct_name().toLowerCase().contains(newText.toLowerCase())) {
                filterdNames.add(m);
            }else if (m.getDoct_speciality().toLowerCase().contains(newText.toLowerCase())) {
                filterdNames.add(m);
            }

           /* if (m.get().toLowerCase().contains(newText.toLowerCase())) {
                filterdNames.add(m);
            }*/

        }

        //calling a method of the adapter class and passing the filtered list
        doctorAdapter.filterList(filterdNames);
    }

    @Override
    public void onClick(View v)
    {

    }

    public class MyDoctorAdapter extends RecyclerView.Adapter<MyDoctorAdapter.ProductHolder>  {

        ArrayList<DoctorModel> list;
        Activity activity;
        String type;
        private ArrayList<DoctorModel> mFilteredList;

        public MyDoctorAdapter(Activity activity, ArrayList<DoctorModel> list, String type) {this.list = list;
            this.activity = activity;
            mFilteredList=list;
            this.type=type;

        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_doctors,parent,false);

            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(final ProductHolder holder, final int position) {
            final DoctorModel categoryModel = mFilteredList.get(position);
            String path = categoryModel.getDoct_photo();

            Picasso.with(activity).load(ConstValue.BASE_URL + "/uploads/profile/" + path).into(holder.icon_image);

            // Log.e("Image",ConstValue.BASE_URL + "/uploads/profile/" + path);


            holder.lbl_title.setText(categoryModel.getDoct_name());
            holder.lbl_clinic_name.setText("Clinic Name : "+categoryModel.getBus_title());
            holder.lbl_speciality.setText(categoryModel.getDoct_speciality());
            holder.lbl_degree.setText(categoryModel.getDoct_degree()+" , "+categoryModel.getDoct_experience()+" year");



            if(type.equalsIgnoreCase("ask")){

                holder.book.setText("Select");
            }

            holder.rel_startchat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(getIntent().hasExtra("from")){

                        Bundle b = new Bundle();
                        b.putString("id",categoryModel.getDoct_id());
                        b.putString("name",categoryModel.getDoct_name());
                        Intent intent = new Intent();
                        intent.putExtras(b);
                        setResult(Activity.RESULT_OK, intent);
                        finish();


                    }else {

                        addToFriend(categoryModel);

                    }
                }
            });

        }



      /*  @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    String charString = charSequence.toString();

                    Log.e("Text",charString);


                    if (charString.isEmpty()) {

                        mFilteredList = list;

                    } else {

                        ArrayList<DoctorModel> filteredList = new ArrayList<>();

                        for (DoctorModel model : list) {

                            if (model.getDoct_speciality().toLowerCase().contains(charString)) {

                                Log.e("At if",charString);

                                filteredList.add(model);
                            }
                        }

                        mFilteredList = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mFilteredList = (ArrayList<DoctorModel>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        } */


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
            Button book;

            public ProductHolder(View itemView) {
                super(itemView);
                icon_image = (ImageView) itemView.findViewById(R.id.imageView);
                lbl_title = (TextView) itemView.findViewById(R.id.title);
                lbl_clinic_name = (TextView) itemView.findViewById(R.id.clinic);
                lbl_degree = (TextView) itemView.findViewById(R.id.degree);
                lbl_speciality=(TextView)itemView.findViewById(R.id.specilaity);
                rel_startchat = (RelativeLayout) itemView.findViewById(R.id.rel_startchat);

                book=(Button)itemView.findViewById(R.id.book);
            }
        }


    }

}
