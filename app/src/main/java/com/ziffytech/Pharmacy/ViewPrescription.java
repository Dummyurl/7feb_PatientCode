package com.ziffytech.Pharmacy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.util.Utility;


public class ViewPrescription extends CommonActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prescription);
      showPrgressBar();

      setHeaderTitle("Prescription");
      allowBack();

        imageView=findViewById(R.id.imageView);

        String img=getIntent().getStringExtra("presc_img");



        Picasso.with(getApplicationContext())
                .load(img )
                .into(imageView);

       hideProgressBar();
        Log.e("URL",img);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return false;
    }


}
