package com.ziffytech.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.ziffytech.R;
import com.ziffytech.util.CommonClass;

import java.util.ArrayList;
import java.util.List;

public class Multiappointview extends CommonActivity {
    Toolbar toolbar;
    int value;
    CommonClass common;
    int page = 1;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiappointview);
        setHeaderTitle("My Appointments");
        allowBack();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //int page = getIntent().getIntExtra("Currentpage", value);
        viewPager.setCurrentItem(page);

    }


    private void setupViewPager(ViewPager viewPager) {
        AdapterViewPager adapter = new AdapterViewPager(getSupportFragmentManager());
        //adapter.addFragment(new Fragment_doc_appo(), "Doctors Appointments");
        adapter.addFragment(new Fragment_lab_appo(), "Lab Appointments");
        viewPager.setAdapter(adapter);
    }


    public class AdapterViewPager extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public AdapterViewPager(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
