package com.ziffytech.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.util.CommonClass;


/**
 * Created by Mahesh on 29/11/17.
 */

public class ProfileActivity extends CommonActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottom_navigation;
    int mMenuId;
    boolean isEventConfirm=false;
    boolean isChecked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_activity);



        setHeaderTitle("");

            String letter;
            letter =common.getSession(ApiParams.USER_FULLNAME).substring(0, 1);
            ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
            int color = mColorGenerator.getRandomColor();
            TextDrawable mDrawableBuilder = TextDrawable.builder()
                    .buildRound(letter, color);
            ImageView thumbnail_image1=(ImageView)findViewById(R.id.thumbnail_image1);
            thumbnail_image1.setImageDrawable(mDrawableBuilder);

        bottom_navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(this);
        bottom_navigation.getMenu().findItem(R.id.navigation_profile).setChecked(true);




    }

    @Override
    protected void onStart() {
        super.onStart();

        setUpViews();

    }

    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.

        mMenuId = item.getItemId();
        for (int i = 0; i < bottom_navigation.getMenu().size(); i++)
        {
            MenuItem menuItem = bottom_navigation.getMenu().getItem(i);
            isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }

        switch (item.getItemId())
        {
            case R.id.navigation_home:
            {
                //finish();
                //Intent i = new Intent(this,MainActivity.class);
                //startActivity(i);


                MenuItem menuItem = bottom_navigation.getMenu().findItem(R.id.navigation_profile);
                isChecked = menuItem.getItemId() == item.getItemId();
                menuItem.setChecked(isChecked);


                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);

            }
            break;
            case R.id.navigation_aboutus:
            {
                String url = "https://www.ziffytech.com/About_Us";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
            break;
            case R.id.navigation_noti:
            {
                Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.navigation_profile:
            {
               recreateActivityCompat(ProfileActivity.this);
            }
            break;
        }
        return true;

    }

    private void setUpViews() {

        TextView tvName=(TextView)findViewById(R.id.tvName);
        tvName.setText(common.getSession(ApiParams.USER_FULLNAME));

        TextView tvEmail=(TextView)findViewById(R.id.tvEmail);
        tvEmail.setText(common.getSession(ApiParams.USER_EMAIL));

        TextView btnPersondetails=(TextView)findViewById(R.id.btnpersonalDetails);
        btnPersondetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,PersonalDetailsActivity.class));

            }
        });

        TextView btnMedicaldetails=(TextView)findViewById(R.id.btnMedicalDetails);
        btnMedicaldetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,MedicalDetails.class));

            }
        });


        TextView btnLifeStyle=(TextView)findViewById(R.id.btnLifeStyleDetails);
        btnLifeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,LifeStyleActivity.class));

            }
        });

        TextView btnFamilyMebers=(TextView)findViewById(R.id.btnFamilyMebers);
        btnFamilyMebers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,RelativeProfile.class));

            }
        });


        TextView btnUpdateProfile=(TextView)findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,UpdateProfileActivity.class));

            }
        });

        TextView btnMedication=(TextView)findViewById(R.id.btnMedication);
        btnMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,MedicationActivity.class));

            }
        });

    }

    @SuppressLint("NewApi")
    public static final void recreateActivityCompat(final Activity a)
    {
        final Intent intent = a.getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        a.finish();
        a.overridePendingTransition(0, 0);
        a.startActivity(intent);
        a.overridePendingTransition(0, 0);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        finish();
        bottom_navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

    }

   /* @Override
    public void allowBack() {

        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        bottom_navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }*/
}
