package com.ziffytech.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.List;

/**
 * Created by swarajpal on 19-04-2016.
 */


public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("ON RECEIVE","TRUE");


        int result = ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.e("PERMISSION","true");
        if (isAppForground(context)) {

            Bundle data  = intent.getExtras();

                Object[] pdus = (Object[]) data.get("pdus");

            Log.e("pdus lenght", "" + pdus.length);
                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    String sender = smsMessage.getDisplayOriginatingAddress();
                    //You must check here if the sender is your provider and not another one with same text.
                    Log.e("sender", "" + sender.toString());

                    String messageBody = smsMessage.getMessageBody();

                    Log.e("messageBody", "" + messageBody.toString());


                    //Pass on the text to our listener.

                    if(sender.contains("ZIFFYT")) {
                        String code = messageBody.replaceAll("[^0-9]", "");
                        Log.e("CODE",code);
                        mListener.messageReceived(code, sender);
                    }

                }
            }
            else {

            ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);


            }
        }



    }


    public boolean isAppForground(Context mContext) {

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {

            ComponentName topActivity = tasks.get(0).topActivity;

            Log.e("topActivity",""+topActivity.getClassName());
            if (!topActivity.getClassName().equals("com.ziffytech.activities.Otppage")) {
                return false;
            }
        }

        return true;
    }


    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
