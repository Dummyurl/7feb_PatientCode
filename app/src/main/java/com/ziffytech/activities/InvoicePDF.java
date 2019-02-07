package com.ziffytech.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ziffytech.R;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.util.CommonClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InvoicePDF extends AppCompatActivity {

    TextView txtPatientName, txtAge, txtGender;
    TextView txtBusinessName, txtDoctorName, txtDate, txtTimeSlot;
    DoctorModel selected_business;
    CommonClass common;
     String userId;
     Button btnPDF;
    TextView tax, total_price, final_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_pdf);




        if (ContextCompat.checkSelfPermission(InvoicePDF.this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted


            ActivityCompat.requestPermissions(InvoicePDF.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }


        selected_business = ActiveModels.DOCTOR_MODEL;


        Log.e("SELCTED BUSINESS",selected_business.toString());

        txtDoctorName = (TextView) findViewById(R.id.doctorName);
        txtDate = (TextView) findViewById(R.id.bookedDate);
        txtTimeSlot = (TextView) findViewById(R.id.bookedTime);
        txtBusinessName = (TextView) findViewById(R.id.clinicName);

        tax = (TextView) findViewById(R.id.text_tax);
     //   payment_mode=findViewById(R.id.payment_mode);
        total_price = (TextView) findViewById(R.id.total_price);
        final_total = (TextView) findViewById(R.id.final_total);



        btnPDF= (Button) findViewById(R.id.btnInvoice);

        txtDoctorName.setText(selected_business.getDoct_name()+" , "+selected_business.getBus_title());
        txtBusinessName.setText(selected_business.getBus_title());
       // txtDate.setText("Booking Date & Time :" +getIntent().getExtras().getString("date")+" , "+getIntent().getExtras().getString("timeslot"));
       // txtTimeSlot.setText("Booking Time"+" : "+getIntent().getExtras().getString("timeslot"));


        txtPatientName = (TextView) findViewById(R.id.patientName);
        txtAge = (TextView) findViewById(R.id.age);
        txtGender = (TextView) findViewById(R.id.gender);



        String amount=getIntent().getStringExtra("amount");
        String doct_name=getIntent().getStringExtra("doctor_name");
        String date=getIntent().getStringExtra("appointment_date");
        String time=getIntent().getStringExtra("start_time");

        double amt= Double.parseDouble(amount);
        final String tAmt= String.valueOf(amt);

        double tax_amt=(amt*10)/100;
        final String taxAmt= String.valueOf(tax_amt);

        double final_amt=tax_amt+amt;
        final String fAmt= String.valueOf(final_amt);


        tax.setText(" Rs. "+taxAmt);
        total_price.setText(" Rs. "+tAmt);
        final_total.setText(" Rs. "+fAmt);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK){
            txtPatientName.setText(data.getExtras().getString("name"));
            txtAge.setText("Age:"+data.getExtras().getString("age"));

            if(data.getExtras().getString("gender").equalsIgnoreCase("0")){

                txtGender.setText("Gender:"+" Male");

            }else if(data.getExtras().getString("gender").equalsIgnoreCase("1")){

                txtGender.setText("Gender:"+" Female");

            }


            userId=data.getExtras().getString("user_id");


        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void printPDF(View view){
        Log.e("####","print pdf");
        if(isExternalStorageWritable()) {
            Log.e("####","print pdf");

            String filename = getFileName();
            File file = new File(getAlbumStorageDir("PDF"), filename);
            try {
                Log.e("####","print pdf");

                FileOutputStream outputStream = new FileOutputStream(file);
                createPDF(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Checks if external storage is available for read and write
     * @return boolean
     */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Returns a name for the file that will be created
     * @return String
     */
    private String getFileName() {
        //TODO: 06/10/2015
        return "file2" + ".pdf";
    }

    /**
     * Creates a PDF document and writes it to external storage using the
     * received FileOutputStream object
     * @param outputStream a FileOutputStream object
     * @throws IOException
     */
    private void createPDF(FileOutputStream outputStream) throws IOException {
        PrintedPdfDocument document = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document = new PrintedPdfDocument(this,
                    getPrintAttributes());
        }

        // start a page
        PdfDocument.Page page = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            page = document.startPage(1);
        }

        // draw something on the page
        View content = getContentView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            content.draw(page.getCanvas());
        }

        // finish the page
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.finishPage(page);
        }
        //. . .
        // add more pages
        //. . .
        // write the document content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.writeTo(outputStream);
        }

        //close the document
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.close();
        }
    }


    private View getContentView() {
        return findViewById(R.id.layout_invoice);
    }

    private PrintAttributes getPrintAttributes() {
        PrintAttributes.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            builder = new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.NA_LETTER.asLandscape())
                    .setResolution(new PrintAttributes.Resolution("res1","Resolution",300,300)).setMinMargins(new PrintAttributes.Margins(0, 0,0,0));
        }
        PrintAttributes printAttributes = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            printAttributes = builder.build();
        }
        return printAttributes;
    }


    private File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.

        String pdfpath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/Ziffytech";



        File folder= new File(pdfpath);
        folder.mkdir();

        //  File pdfdir = new File(String.valueOf(pdfpath));

       /* File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), albumName);*/
        if (!folder.mkdirs()) {
            Log.e("Error", "Directory not created");
        }
        return folder;
    }




}
