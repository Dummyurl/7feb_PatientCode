package com.ziffytech.configfcm;

/**
 * Created by subhashsanghani on 12/21/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ziffytech.activities.MainActivity;
import com.ziffytech.chat.DataProvider;
import com.ziffytech.util.NotificationUtils;
import com.ziffytech.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static final String broadCastReceiver = "ChatBroadCast";
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]

    private NotificationUtils notificationUtils;



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("NOTIFICATION",remoteMessage.toString());

       Log.e("notification",remoteMessage.getNotification().getBody());


        if (remoteMessage.getData().size() > 0) {

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }else{

            Log.e("notificationElse",remoteMessage.getNotification().getBody().toString());

            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("message", remoteMessage.getNotification().getBody());
            showNotificationMessage(getApplicationContext(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), resultIntent);
        }

    }


    private void handleDataMessage(JSONObject data) {
        Log.e(TAG, "push json: " + data.toString());

        try {
            // JSONObject data = json.getJSONObject("data");

            String msg = data.getString("msg");
            String msgId = data.getString("msg_id");
            String doct_id = data.getString("doct_id");
            String title = data.getString("title");


            ContentValues values = new ContentValues(8);
            values.put(DataProvider.COL_MSG, msg);
            values.put(DataProvider.COL_FROM, doct_id);
            values.put(DataProvider.COL_MSG_ID, msgId);
            values.put(DataProvider.COL_IS_IMAGE, "no");
            values.put(DataProvider.COL_IS_STREAM, "no");
            values.put(DataProvider.COL_AT, Utils.getCurrentDate());
            getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);

            ContentValues values_p = new ContentValues(4);
            values_p.put(DataProvider.COL_IS_TYPING, "no");
            values_p.put(DataProvider.COL_IS_IMAGE, "no");
            values_p.put(DataProvider.COL_IS_STREAM_PROFILE, "no");
            values_p.put(DataProvider.COL_LAST_MSG_DATE, Utils.getCurrentDate());
            getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, doct_id), values_p, null, null);


            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("message", msg);
            showNotificationMessage(getApplicationContext(), title, msg, resultIntent);

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }



    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }

}