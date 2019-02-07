package com.ziffytech.Wallet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.afollestad.materialdialogs.MaterialDialog;
import com.ziffytech.R;


import java.util.ArrayList;

public class OffersAdapter  extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {



    private ArrayList<PromocodeModel> modelArrayList;
    Context context;
    private OnOfferListener onOfferListener;




    public OffersAdapter(Context context, ArrayList<PromocodeModel> names,OnOfferListener onOfferListener) {
        this.context=context;
        this.modelArrayList = names;
        this.onOfferListener=onOfferListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo_code, parent, false);



        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {

        final PromocodeModel promocodeModel=modelArrayList.get(position);

        holder.text_promo_code_name.setText(promocodeModel.getPromo_code_name());
       final String code="Use code "+ "<b>"+promocodeModel.getPromo_code_name()+"</b>" +" to avail this offer.";

       holder.promo_code_title.setText((Html.fromHtml(code)));
        holder.view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final MaterialDialog dialog1 = new MaterialDialog.Builder(context)
                        .customView(R.layout.item_promo_details, false)
                        .show();
                dialog1.setCancelable(false);

                View view1 = dialog1.getView();


                TextView text_ok = view1.findViewById(R.id.text_ok);
                TextView text_info = view1.findViewById(R.id.txt_info);
                text_info.setText(promocodeModel.getPromo_code_details());
                text_info.setPadding(10, 10, 10, 10);
                text_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });


            }
        });



        holder.text_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String promo_id=promocodeModel.getPromo_code_id();
                String promo_name=promocodeModel.getPromo_code_name();
                String max_wallet_usage=promocodeModel.getMax_wallet_usage();
              //  double min_trans= Double.parseDouble(promocodeModel.getMin_trans_amt());







                onOfferListener.applyPromoCode(promo_id,promo_name,max_wallet_usage);

            }
        });


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }





    class ViewHolder extends RecyclerView.ViewHolder {

         TextView text_promo_code_name,promo_code_title,view_details,text_apply;


        ViewHolder(View itemView) {
            super(itemView);

            text_promo_code_name=itemView.findViewById(R.id.promocode);
            promo_code_title=itemView.findViewById(R.id.text_title);
            view_details=itemView.findViewById(R.id.view_details);
            text_apply=itemView.findViewById(R.id.text_apply);
        }


    }

    public interface OnOfferListener {
        void applyPromoCode(String promo_id, String promo_name, String max_wallet_usage);
    }

}



