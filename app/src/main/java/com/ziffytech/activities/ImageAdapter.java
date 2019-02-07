package com.ziffytech.activities;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ziffytech.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;


    int [] mThumbIds = {R.drawable.package1, R.drawable.package2, R.drawable.package3, R.drawable.package4, R.drawable.package5,
            R.drawable.package6, R.drawable.package7};

    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(120, 120));
        imageView.setPadding(10,10,10,10);
        return imageView;
    }

}