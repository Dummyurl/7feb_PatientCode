package com.ziffytech.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziffytech.R;

public class SelfCreateTextView extends AppCompatActivity {
    LinearLayout linearLayout;
    Button btn_add;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

         linearLayout=(LinearLayout)findViewById(R.id.layout_text);
        btn_add= (Button) findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(SelfCreateTextView.this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setGravity(Gravity.CENTER);
                textView.setText("TEXT ADDED");

                // Add TextView to LinearLayout

                    linearLayout.addView(textView);

            }
        });


    }

}









