package com.ziffytech.activities;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ziffytech.R;

import java.util.ArrayList;


public class Adaptertestlist extends BaseAdapter implements Filterable
{
    public Context context;
    public LayoutInflater inflater;
    public ArrayList<String> al_lab_id,al_lab_category,al_lab_test_name;
    ValueFilter valueFilter;


    public Adaptertestlist(Context context, ArrayList<String> al_lab_id, ArrayList<String> al_lab_category, ArrayList<String> al_lab_test_name)
    {
        this.context = context;
        this.al_lab_id=al_lab_id;
        this.al_lab_category=al_lab_category;
        this.al_lab_test_name=al_lab_test_name;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount()
    {
        return al_lab_id.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflater.inflate(R.layout.item_product_info, null);
        TextView testtitle = (TextView) convertView.findViewById(R.id.testtitle);
        TextView testcategory = (TextView) convertView.findViewById(R.id.testcategory);
        testtitle.setText(""+al_lab_test_name.get(position));
        testcategory.setText(""+al_lab_category.get(position));

        return convertView;
    }

    @Override
    public Filter getFilter()
    {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<String> filterList = new ArrayList<>();
                for (int i = 0; i < al_lab_test_name.size(); i++)
                {
                    if (    ((al_lab_test_name.get(i).toString().toUpperCase()).contains(constraint.toString().toUpperCase())) ||
                            ((al_lab_category.get(i).toString().toUpperCase()).contains(constraint.toString().toUpperCase()))    )
                    {
                        filterList.add(al_lab_test_name.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else
            {
                results.count = al_lab_test_name.size();
                results.values = al_lab_test_name;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            al_lab_test_name = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
