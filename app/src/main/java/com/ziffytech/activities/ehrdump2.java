package com.ziffytech.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class ehrdump2 extends CommonActivity implements View.OnClickListener {

    ImageView gif;
    CheckBox chkehr;
    TextView txtv_restore,txtv_skipp;
    AlertDialog.Builder builder;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ehrdump2);
        chkehr = (CheckBox)findViewById(R.id.chkehr);
        txtv_restore = (TextView)findViewById(R.id.txtv_restore);
        txtv_skipp = (TextView)findViewById(R.id.txtv_skipp);
        txtv_restore.setOnClickListener(this);
        txtv_skipp.setOnClickListener(this);


    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()== R.id.txtv_restore)
        {
            if (chkehr.isChecked())
            {


                Log.e("###","check true");
                common.setSession(ApiParams.EHR,"true");
                 GotoApi();
            }
            else
                {
                    Toast.makeText(this, "Please check the condition", LENGTH_SHORT).show();
                }

        }
        else
            {
                builder = new AlertDialog.Builder(this);
                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to skip EHR backup?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                finish();
                                Intent intent = new Intent(ehrdump2.this, MainActivity.class);
                                intent.putExtra("new","new");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Ziffytech EHR Backup");
                alert.show();
            }
    }


    private void GotoApi()
    {

        String value = "1";

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.getSession(ApiParams.COMMON_KEY));
        params.put("test_status_per",value );
        String base = "https://www.ziffytech.com/admin/index.php/Api/update_test_resultt";
        startProgressDialog(ehrdump2.this);
        VJsonRequest vJsonRequest = new VJsonRequest(this, base, params,
                new VJsonRequest.VJsonResponce()
                {
                    @Override
                    public void VResponce(String responce) throws JSONException {
                        Log.e("EHR Res", responce);
                        stopProgressDialog();
                        try
                        {
                            Log.e("###","check true2");
                            JSONObject root = new JSONObject(responce);
                            String message = root.optString("message");
                            String Status = root.optString("responce");
                            //pd.dismiss();
                            //Toast.makeText(ehrdump2.this, ""+message+" "+Status, Toast.LENGTH_SHORT).show();
                            JumpToNextActivity();

                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                            //Toast.makeText(Productpage.this, " "+e, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void VError(String responce)
                    {
                        stopProgressDialog();
                    }

                });

    }

    private void JumpToNextActivity()
    {
        Log.e("###","check true3");
        finish();
        Intent intent = new Intent(ehrdump2.this, MainActivity.class);
        intent.putExtra("new","new");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
