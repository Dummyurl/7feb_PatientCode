package com.ziffytech.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.Preferences;
import com.ziffytech.util.Utility;
import com.ziffytech.util.VolleyMultipartRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Mahesh on 17/10/17.
 */

public class MedicineOrderActivity extends CommonActivity
{

    ImageView imageView;
    TextView textView;
    Button proceed;
    private String userChoosenTask;
    String pincode, city, address, time;
    SharedPreferences sharedPreferences;
    File destination;
    Bitmap bitmap;
    LinearLayout llbtn;

    public static final int REQUEST_IMAGE = 0;
    public static final int REQUEST_PERMISSION = 200;
    private String imageFilePath = "";
    Uri photoUri;

    private int REQUEST_CAMERA = 100, SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        allowBack();

        Intent intent = getIntent();
        pincode = intent.getStringExtra(ApiParams.PINCODE);
        city = intent.getStringExtra(ApiParams.CITY);
        address = intent.getStringExtra(ApiParams.ADDRESS);
        time = intent.getStringExtra(ApiParams.Time);
        llbtn=findViewById(R.id.llbtn);
        setHeaderTitle("Order Medicine");
        sharedPreferences = getSharedPreferences(Preferences.MyPREFERENCES, Context.MODE_PRIVATE);
        bindView();

    }

    public void bindView()
    {
        imageView = (ImageView) findViewById(R.id.imageview);
        textView = (TextView) findViewById(R.id.text);

        llbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                selectImage();
            }
        });

        proceed = (Button) findViewById(R.id.submit);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  orderMedicine();
                uploadBitmap(bitmap);

            }


        });

    }



    private void selectImage()
    {

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MedicineOrderActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(MedicineOrderActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }


    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent()
    {

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null)
        {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            photoUri = FileProvider.getUriForFile(this, "com.ziffytech.util.GenericFileProvider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_CAMERA);
        }
    }


    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();

        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA){

                //onCaptureImageResult(data);
                try {



                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    //Toast.makeText(this, ""+bitmap1, Toast.LENGTH_SHORT).show();


                    final int maxSize = 960;
                    int outWidth;
                    int outHeight;
                    int inWidth = bitmap1.getWidth();
                    int inHeight = bitmap1.getHeight();
                    if(inWidth > inHeight){
                        outWidth = maxSize;
                        outHeight = (inHeight * maxSize) / inWidth;
                    } else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }

                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap1, outWidth, outHeight, false);

                    imageView.setImageBitmap(bitmap1);
                    textView.setVisibility(View.GONE);
                    textView.isClickable();
                    bitmap  = resizedBitmap;
                    //getFileDataFromDrawable(bitmap1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    private void onCaptureImageResult(Intent data)
    {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        getFileDataFromDrawable(thumbnail);

        /* ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try
        {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }*/

        bitmap=thumbnail;
        imageView.setImageBitmap(thumbnail);
        textView.setVisibility(View.GONE);
        textView.isClickable();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data)
    {
        Bitmap bm = null;
        if (data != null)
        {
            try
            {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Toast.makeText(this, ""+bm, Toast.LENGTH_SHORT).show();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        bitmap=bm;
        imageView.setImageBitmap(bm);
        textView.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed()
    {

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        Intent myIntent = new Intent(MedicineOrderActivity.this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
        return;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }

    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void uploadBitmap(final Bitmap bitmap)
    {

        if(bitmap==null)
        {
            MyUtility.showToast("Select image to upload",this);
            return;
        }

        //getting the tag from the edittext
        showPrgressBar();
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiParams.ORDER_MEDICINE,
                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        Log.e("RESPONSE_MEDICINE_ORDER", String.valueOf(response));
                        hideProgressBar();

                        showSuccessDialog();
                        /*try {

                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.e("Data",obj.toString());
                            showSuccessDialog();
                            //  Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {

                            Toast.makeText(getApplicationContext(),"Failed to Submit form.", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }*/
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", common.get_user_id());
                params.put("pincode", pincode);
                params.put("promocode", city);
                params.put("address", address);
                params.put("time", time);
                Log.e("PARAMS",params.toString());
                //params.put("user_image",getFileDataFromDrawable(bitmap));
                return params;
            }
            //Here we are passing image by renaming it with a unique name
            //
            @Override
            protected Map<String, DataPart> getByteData()
            {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                // params.put("user_image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                params.put("user_image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                //Toast.makeText(MedicineOrderActivity.this, ""+getFileDataFromDrawable(bitmap), Toast.LENGTH_SHORT).show();
                Log.e("mangesh",getFileDataFromDrawable(bitmap).toString());
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }


    private void showSuccessDialog()
    {
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setMessage("Your request has been taken.We will get back to you soon.");
        ad.setCancelable(false);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
                Intent intent = new Intent(MedicineOrderActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        ad.create().show();

    }




    /****************************************** Compression *********************************************/






}
