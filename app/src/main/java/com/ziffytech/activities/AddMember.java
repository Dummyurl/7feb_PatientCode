package com.ziffytech.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class AddMember extends CommonActivity {
    EditText dob,name,relation,age,enumber,feet,inches,weight,aadhar,salutations;
    int year, month, day;
    Button btn_add_member;
    String ageS;
    String height;
    String Mygender="0";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member_profile);
        setHeaderTitle("Add Member");
        allowBack();

        name=(EditText) findViewById(R.id.name);
        relation=(EditText) findViewById(R.id.relation);
        age=(EditText) findViewById(R.id.age);
        enumber=(EditText)findViewById(R.id.contact);

        btn_add_member=(Button)findViewById(R.id.add_member_button);









        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        dob=(EditText)findViewById(R.id.dob);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dd = new DatePickerDialog(AddMember.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);

                                    dob.setText(formatter.format(date).toString());
                                  getAge(year,month,day);
                                    age.setText(ageS);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }, year, month, day);

               // Calendar c=Calendar.getInstance();
               // c.add(Calendar.YEAR,-12);
                dd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dd.show();

            }
        });

        feet = (EditText)findViewById(R.id.feet);
        inches = (EditText)findViewById(R.id.inches);
        weight = (EditText)findViewById(R.id.weight);


        salutations = (EditText)findViewById(R.id.stype);
        aadhar = (EditText)findViewById(R.id.aadhar_no);



        salutations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(AddMember.this);
                builderSingle.setTitle("Select salutations");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddMember.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Mr.");
                arrayAdapter.add("Mrs.");
                arrayAdapter.add("Miss.");
                arrayAdapter.add("Dr.");
                arrayAdapter.add("Baby of.");
                arrayAdapter.add("Mast.");
                arrayAdapter.add("SMT.");
                arrayAdapter.add("Hosp.");
                arrayAdapter.add("Ms.");


                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(AddMember.this);
                        builderInner.setMessage(strName);
                        salutations.setText(strName);
                    }
                });
                builderSingle.show();
            }
        });




        typeGenderSelection();



        btn_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addMember();
            }
        });
    }

    private void addMember() {
        Log.e("ADD_MEMBER","CLICKED");
        if (TextUtils.isEmpty(salutations.getText().toString())) {
            salutations.setError("Select Salutations");
            return;
        }



        if (name.getText().toString().length() <= 0) {
            name.setError(getString(R.string.valid_required_fullname));
            return;}

        if (relation.getText().toString().length() <= 0) {
            relation.setError(getString(R.string.enter_relation));
            return;
        }
        if (age.getText().toString().length() <= 0) {
            age.setError(getString(R.string.enter_age));
            return;
        }
        if (enumber.getText().toString().length() != 10) {
            enumber.setError(getString(R.string.enter_phone));
            return;
        }

        if (dob.getText().toString().equalsIgnoreCase("")) {
            dob.setError(getString(R.string.enter_bdy));
            return;
        }



     /*   if (TextUtils.isEmpty(aadhar.getText().toString()) || aadhar.getText().toString().length()<12) {
            aadhar.setError("Enter Valid Aadhar number");
            return;
        }


        if(!MyUtility.isConnected(AddMember.this)) {
            MyUtility.showAlertMessage(AddMember.this,MyUtility.INTERNET_ERROR);
            return;
        }
*/


     height=feet+"'"+inches;

       startProgressDialog(AddMember.this);
                HashMap<String,String> params = new HashMap<>();
                params.put("m_name",name.getText().toString());
                params.put("relation",relation.getText().toString());
                params.put("age",age.getText().toString());
                params.put("height",height);
                params.put("weight", weight.getText().toString());
                params.put("birth_date",dob.getText().toString());
                params.put("alternate_number",enumber.getText().toString());
                params.put("m_gender",Mygender);
                params.put("user_id",common.get_user_id());
                params.put("adhar_number","adhar");
                params.put("salutations", salutations.getText().toString());

              CustomRequestForString customRequestForString=new CustomRequestForString(Request.Method.POST, ApiParams.ADD_MEMBER,params,this.createRequestSuccessListenerMemberAdd(),this.createRequestErrorListenerMemberAdd());
              RequestQueue requestQueue = Volley.newRequestQueue(AddMember.this);
              requestQueue.add(customRequestForString);


        }


    public String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);

        ageS = ageInt.toString();

        return ageS;
    }


    private Response.Listener<String> createRequestSuccessListenerMemberAdd() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                stopProgressDialog();
                Log.e("MEMBER_RESPONSE", response.toString());
                Toast.makeText(AddMember.this, "MEMBER ADDED", Toast.LENGTH_SHORT).show();
                finish();

            }
        };
    }

    private Response.ErrorListener createRequestErrorListenerMemberAdd() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("##", "##" + error.toString());
                stopProgressDialog();
                //App.showAlert("Something Went Wrong, Please Try again",MultiTestSearchActivity.this);

            }
        };
    }




    private void typeGenderSelection() {

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.radioMale) {
                    Mygender="0";
                } else if (checkedId == R.id.radioFemale) {
                    Mygender="1";
                }
            }
        });
    }



}
