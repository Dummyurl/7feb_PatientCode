package com.ziffytech.thyrocare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;

public class PackageDetail extends CommonActivity implements View.OnClickListener
{

    Button bookpack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);
        setHeaderTitle("Package Detail");
        allowBack();

        bookpack = (Button)findViewById(R.id.bookpack);
        bookpack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.bookpack)
        {
            /*Intent i = new Intent(PackageDetail.this,Timeslotforthyro.class);
            startActivity(i);*/
        }

    }
}

