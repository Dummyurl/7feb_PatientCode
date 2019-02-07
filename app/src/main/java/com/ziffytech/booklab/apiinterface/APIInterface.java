package com.ziffytech.booklab.apiinterface;

import com.google.gson.JsonObject;
import com.ziffytech.booklab.apiinterface.models.LabDetailListApi;
import com.ziffytech.booklab.apiinterface.models.LabListApi;
import com.ziffytech.booklab.apiinterface.models.LabViewApi;
import com.ziffytech.booklab.apiinterface.models.TestApi;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {


    @POST("filter_lab")
    @FormUrlEncoded
    Call<LabDetailListApi> getlab(@Field("home_collection") String home_collection,
                                  @Field("lat") String lat,
                                  @Field("lng") String lng,
                                  @Field("test") String test);

    @POST("search_lab_by_name")
    @FormUrlEncoded
    Call<LabListApi> search_lab_by_name(@Field("search") String search);

    @POST("get_lab_by_id")
    @FormUrlEncoded
    Call<LabViewApi> get_lab_by_id(@Field("lab_id") String lab_id);


    @POST("get_recommend_test")
    @FormUrlEncoded
    Call<TestApi> get_recommend_test(@Field("user_id") String user_id, @Field("sub_user_id") String sub_user_id);


    @POST("add_appointment_for_lab")
    @FormUrlEncoded
    Call<JsonObject> bookLab(@Field("lab_id") String lab_id,
                             @Field("appointment_date") String date,
                             @Field("start_time") String time,
                             @Field("appointment_id") String user_id,
                             @Field("home_collection") String home_collection,
                             @Field("test") String test,
                             @Field("user_id") String userid,
                             @Field("txn_id") String txn_id,
                             @Field("amount") String amount,
                             @Field("payment_mode") String payment_mode,
                             @Field("payment_status") String payment_status
    );




    /*     params.put("lab_id", getIntent().getStringExtra("lab_id"));
            params.put("appointment_date", getIntent().getStringExtra("appointment_date"));
            params.put("start_time", getIntent().getStringExtra("start_time"));
            params.put("home_collection", getIntent().getStringExtra("home_collection"));
            params.put("user_id", common.get_user_id());
            params.put("test", getIntent().getStringExtra("test"));
            params.put("appointment_id", "965");
            params.put("payment_status", "0");


            params.put("amount", getIntent().getStringExtra("total_price"));
            if (isPayUmoney) {
                params.put("payment_mode", "online");
                params.put("txn_id", txn_id);
            } else if (isWallet) {
                params.put("payment_mode", "wallet");
                params.put("txn_id", txn_id);
            } else {
                params.put("payment_mode", "cash");
            }   */


    @Multipart
    @POST("Api/add_user_medicine")
    Call<JsonObject> addUserMedicine(@Part("user_id") RequestBody user_id,
                                     @Part("pincode") RequestBody pincode,
                                     @Part("promocode") RequestBody promocode,
                                     @Part("address") RequestBody address,
                                     @Part("time") RequestBody time,
                                     @Part MultipartBody.Part image);

}