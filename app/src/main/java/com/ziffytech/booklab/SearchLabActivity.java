package com.ziffytech.booklab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ziffytech.R;


public class SearchLabActivity extends AppCompatActivity {

    EditText mSearchLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lab);

        mSearchLab=(EditText)findViewById(R.id.lab_editText);

        mSearchLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("msg","data");

                Intent labIntent=new Intent(SearchLabActivity.this,LabListActivity.class);

                startActivity(labIntent);

            }
        });

    }
}
