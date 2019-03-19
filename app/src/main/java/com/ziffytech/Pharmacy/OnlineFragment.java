package com.ziffytech.Pharmacy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziffytech.R;

import java.util.ArrayList;

public class OnlineFragment extends Fragment {

    RecyclerView recyclerView;
    TextView tv_no_data;
    LinearLayoutManager layoutManager;

    public OnlineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public static OnlineFragment newInstance() {
        return new OnlineFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.frag_onl, container, false);
/*

        recyclerView=rootView.findViewById(R.id.recyclerview_online);
        tv_no_data=rootView.findViewById(R.id.tv_no_data);
        tv_no_data.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        GridLayoutManager layout = new GridLayoutManager(getActivity(),2, GridLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layout);

*/

     /*   TimeSlotActivity timeSlotActivity = (TimeSlotActivity) getActivity();
        historyAdapter=new HistoryAdapter(getActivity(),arrayListAll,"0",timeSlotActivity);
        recyclerView.setAdapter(historyAdapter);

*/
        return rootView;

    }

    public void updateData(ArrayList<OnlineModel> arrayList){
        if (recyclerView!=null) {
            if (!arrayList.isEmpty()) {

                recyclerView.setVisibility(View.VISIBLE);
                tv_no_data.setVisibility(View.GONE);
               /* arrayListAll.clear();
                arrayListAll.addAll(arrayList);
                historyAdapter.notifyDataSetChanged();*/

            } else {
                Log.e("isEmpty", "true");
                recyclerView.setVisibility(View.GONE);
                tv_no_data.setVisibility(View.VISIBLE);
            }

        }
    }


}
