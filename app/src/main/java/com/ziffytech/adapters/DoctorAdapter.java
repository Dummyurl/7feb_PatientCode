package com.ziffytech.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.TimeSlotActivity;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.DoctorModel;

import java.util.ArrayList;

/**
 * Created by Light link on 04/07/2016.
 */
public class  DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ProductHolder>
 {

    ArrayList<DoctorModel> list;
    Activity activity;
    public DoctorAdapter(Activity activity, ArrayList<DoctorModel> list) {
        this.list = list;
        this.activity = activity;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_list_fragment,parent,false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final DoctorModel categoryModel = list.get(position);
        String path = categoryModel.getDoct_photo();

        Picasso.with(activity).load(ConstValue.BASE_URL + "/uploads/profile/" + path).into(holder.icon_image);

        Log.e("Image", ConstValue.BASE_URL + "/uploads/profile/" + path);

        holder.lbl_title.setText(categoryModel.getDoct_name());
        holder.lbl_degree.setText(categoryModel.getDoct_degree()+","+categoryModel.getDoct_speciality());
        holder.lbl_clinic_name.setText(categoryModel.getBus_title()+", "+categoryModel.getBus_google_street());
        //holder.lbl_speciality.setText(categoryModel.getDoct_speciality());
        holder.charges.setText(""+categoryModel.getConsult_fee());
        holder.txt_avail.setText(""+categoryModel.getStart_con_time()+" - "+categoryModel.getEnd_con_time());
        holder.distance.setText("Distance : "+categoryModel.getDistance()+" KM");



        if (categoryModel.getIs_ziffydoc().equals("0")){

            holder.book.setVisibility(View.GONE);
        }else
        if (categoryModel.getIs_ziffydoc().equals("1")){

            holder.book.setVisibility(View.VISIBLE);
        }


        holder.book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (categoryModel.getIs_ziffydoc().equals("0")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel","+917066532000", null));
                    activity.startActivity(intent);
                }else if (categoryModel.getIs_ziffydoc().equals("1")){

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",categoryModel.getDoct_phone(), null));
                    activity.startActivity(intent);
                }

            }
        });
/*

        try {
            if(!categoryModel.getRating().equalsIgnoreCase("") && !categoryModel.getRating().equalsIgnoreCase("null") && categoryModel.getRating()!=null){
                holder.txt_rating.setText(""+(categoryModel.getRating()));
            }else{
                holder.txt_rating.setText(""+(categoryModel.getRating()));
            }

        }catch (Exception e){
            holder.txt_rating.setText("1.0");
            e.printStackTrace();
        }
*/


      holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActiveModels.DOCTOR_MODEL = categoryModel;
                Intent intent = new Intent(activity, TimeSlotActivity.class);
                intent.putExtra("doct_name",categoryModel.getDoct_name());
                Log.e("DOCT_NAME"+""+"DOCT_ADAPTER",categoryModel.getDoct_name());
                activity.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView icon_image;
        TextView lbl_title;
        TextView lbl_degree;
        TextView lbl_speciality;
        TextView lbl_clinic_name;
        TextView charges,txt_rating,txt_avail,distance;
        TextView book,book1;
        SimpleRatingBar rating;

        public ProductHolder(View itemView)
        {
            super(itemView);
            icon_image = (ImageView) itemView.findViewById(R.id.imageView);
            lbl_title = (TextView) itemView.findViewById(R.id.title);
            lbl_degree = (TextView) itemView.findViewById(R.id.degree);
            lbl_clinic_name = (TextView) itemView.findViewById(R.id.clinic);
            lbl_speciality=(TextView)itemView.findViewById(R.id.specilaity);
            txt_rating=(TextView)itemView.findViewById(R.id.txt_rating);
            txt_avail =(TextView)itemView.findViewById(R.id.txt_avail);

            //rating=(SimpleRatingBar)itemView.findViewById(R.id.rating);

            charges=(TextView)itemView.findViewById(R.id.charges);
            distance = (TextView)itemView.findViewById(R.id.distance);

            book=(TextView)itemView.findViewById(R.id.book);
            book1=(TextView)itemView.findViewById(R.id.book1);
        }
    }

     public void filterList(ArrayList<DoctorModel> filterdNames) {
         this.list = filterdNames;
         notifyDataSetChanged();
     }

}
