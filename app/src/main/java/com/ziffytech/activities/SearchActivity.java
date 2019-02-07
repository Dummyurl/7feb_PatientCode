package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ziffytech.R;


public class SearchActivity extends CommonActivity
 {
    SeekBar seekBar,seekBarFee,seekBarRating;
    TextView txtArea,txtAreaFee,txtAreaRating;
    Switch switch1;
     String fees;
    boolean isSeekBarDistance=false,isSeekBarRate=false,isSeekBarFee=false,isRadioChecked=false;
   // AutoCompleteTextView searchText;
    Bundle bundleloclaity;


    RadioGroup radioAvail,radioGender,radiosort,radiofee;

    private String gender="",availability="",sort="",fee="";
    boolean isRadioGender=false,isRadioSort=false,isRadioFee=false;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        allowBack();
        setHeaderTitle(getString(R.string.hint_search));

      //  searchText = (AutoCompleteTextView) findViewById(R.id.search_keyword);
      /*  searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // searchDoctor();
            }
        });
        */

    /*  String cat_id=getIntent().getStringExtra("cat_id");
        Log.e("cat_id",cat_id);*/

        txtArea = (TextView) findViewById(R.id.textArea);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        switch1 = (Switch) findViewById(R.id.switch1);

        seekBar.setMax(20);
        txtArea.setText(common.getSessionInt("radius") + getString(R.string.KM));
        seekBar.setProgress(common.getSessionInt("radius"));

        if (!common.containKeyInSession("nearby_enable")) {
            common.setSessionBool("nearby_enable", true);
        }
        switch1.setChecked(common.getSessionBool("nearby_enable"));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                common.setSessionBool("nearby_enable", b);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                isSeekBarDistance=true;
                common.setSessionInt("radius", i);
                txtArea.setText(common.getSessionInt("radius") + getString(R.string.KM));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //Fee

        txtAreaFee = (TextView) findViewById(R.id.textAreaFee);
        seekBarFee = (SeekBar) findViewById(R.id.seekBarFee);

        seekBarFee.setMax(999);
        //txtAreaFee.setText(common.getSessionInt("radius") + getString(R.string.KM));
        seekBarFee.setProgress(0);


        seekBarFee.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               // common.setSessionInt("radius", i);
                isSeekBarFee=true;
                txtAreaFee.setText("Rs. "+i);

                 fees= String.valueOf(i);

                Log.e("FEE", String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Rating

        txtAreaRating = (TextView) findViewById(R.id.textAreaRating);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);

        seekBarRating.setMax(5);
       // txtAreaRating.setText(common.getSessionInt("radius") + getString(R.string.KM));
         seekBarRating.setProgress(0);


        seekBarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                isSeekBarRate=true;
               // common.setSessionInt("radius", i);
                txtAreaRating.setText(i+"");
              //  txtAreaRating.setText(common.getSessionInt("radius") + getString(R.string.KM));


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Availbility

        availibiltiy();
        genderSelection();
        FeeSelection();
        SortSelection();
    }

     private void FeeSelection()
     {
         radiofee = (RadioGroup) findViewById(R.id.radiofee);
         radiofee.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {
                 isRadioFee=true;
                 // find which radio button is selected
                 if (checkedId == R.id.fee){
                     sort="1";
                 } else if (checkedId == R.id.min){
                     sort="2";
                 }else if (checkedId == R.id.mid){
                     sort ="3";
                 }else if (checkedId == R.id.high) {
                     sort ="4";
                 }
             }
         });

     }

     private void SortSelection()
     {
         radiosort = (RadioGroup) findViewById(R.id.radiosort);
         radiosort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {
                 isRadioSort=true;
                 // find which radio button is selected
                 if (checkedId == R.id.newest){
                     sort="1";
                 } else if (checkedId == R.id.popularity){
                     sort="0";
                 }else if (checkedId == R.id.price_low_to_high){
                     sort ="2";
                 }else if (checkedId == R.id.price_high_to_low) {
                     sort ="3";
                 }
             }
         });
     }

     private void genderSelection() {

        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isRadioGender=true;
                // find which radio button is selected
                if (checkedId == R.id.male) {
                    gender="male";
                } else if (checkedId == R.id.female) {
                    gender="female";
                }
            }
        });
    }

    private void availibiltiy() {
        radioAvail = (RadioGroup) findViewById(R.id.radioAvail);
        radioAvail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isRadioChecked=true;
                // find which radio button is selected
                if (checkedId == R.id.today) {
                    availability="today";
                } else if (checkedId == R.id.tomorrow) {
                    availability="tomorrow";
                } else if (checkedId == R.id.home) {
                    availability="home";
                }
            }
        });
    }



    public void SearchButtonClick1(View view) {
        if (isSeekBarDistance = false) {
            if (isSeekBarRate = false) {
                if (isSeekBarFee = false) {
                    if (isRadioGender = false) {
                        if (isRadioChecked = false)
                        {
                            Toast.makeText(this, "Select filter options", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        } else
        {


            Intent intent = new Intent(SearchActivity.this, BookActivity.class);
            intent.putExtra("search", "search");

            if (isSeekBarDistance) {
                intent.putExtra("distance", txtArea.getText().toString());
            }else{
                Log.e("######","falseD");
                txtArea.setText("");
                intent.putExtra("distance", "");
            }
            if (isSeekBarFee) {
                intent.putExtra("fee", fees);
                Log.e("FEE","#####"+fees);
            }else {

                Log.e("######","falseF");
                txtAreaFee.setText("");
                intent.putExtra("fee", "");
            }
            if (isSeekBarRate) {
                intent.putExtra("rating", txtAreaRating.getText().toString());

            }else {

                Log.e("######","falseR");
                intent.putExtra("rating","");

            }
            if (isRadioChecked) {
                intent.putExtra("availability", availability);
            }else {

                Log.e("######","falseA");

                intent.putExtra("availability", "");
            }
            if (isRadioGender) {
                intent.putExtra("gender", gender);
            }else {

                Log.e("######","false");
                intent.putExtra("gender", "");
            }

            if (isRadioFee) {
                intent.putExtra("fee2", fee);
            }else {

                Log.e("######","false");
                intent.putExtra("fee2", "");
            }

            if (isRadioSort) {
                intent.putExtra("sort2", sort);
            }else {

                Log.e("######","false");
                intent.putExtra("sort2", "");
            }


            intent.putExtra("Activity", "2");
            intent.putExtra("cat_id", getIntent().getStringExtra("cat_id"));
            Log.e("SEARCH_ACTIVITY", "TRUE");
            Log.e("SEARCH_INTENT", intent.toString());

            startActivity(intent);
            finish();

        }
    }
    public void LocalityViewClick(View view){
        Intent intent = new Intent(SearchActivity.this,LocationActivity.class);
        startActivityForResult(intent,1);
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            ((TextView)findViewById(R.id.chooseLocality)).setText(data.getExtras().getString("locality"));
            bundleloclaity = data.getExtras();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
