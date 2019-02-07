package com.ziffytech.remainder;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.MyApplication;
import com.ziffytech.chat.DataProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Mahesh on 22/04/18.
 */

public class EditRemainder extends CommonActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private EditText mTitleText;
    private EditText mMedicinest;

    private TextView mDateText,set_end_date,set_dose_repeat_type;

    private LinearLayout llmor,llafter,lleve,llnight;


    TextView tvMorn,tvAfter,tvEve,tvNight;
    boolean isMorning=false,isAfternoon=false,isEvening=false,isNight=false;
    boolean isMorningEdit=false,isAfternoonEdit=false,isEveningEdit=false,isNightEdit=false;
    String morTime,afterTime,eveTime,nightTime;
    int timeType=0;
    Button btnSave;

    String times="";

    String updatedTimes="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remainder);
        allowBack();
        setHeaderTitle("Edit");

        Log.e("Data:",new Gson().toJson(MyApplication.remainder).toString());

        setUpViews();
        setUpData();

    }



    private void setUpViews() {

        mTitleText = (EditText) findViewById(R.id.reminder_title);
        mMedicinest=(EditText)findViewById(R.id.medicine_name);
        mDateText = (TextView) findViewById(R.id.set_date);
        set_end_date=(TextView)findViewById(R.id.set_end_date);

        set_dose_repeat_type=(TextView)findViewById(R.id.set_dose_repeat_type);

        llmor=(LinearLayout)findViewById(R.id.llmorning);
        llafter=(LinearLayout)findViewById(R.id.llafter);
        lleve=(LinearLayout)findViewById(R.id.llevening);
        llnight=(LinearLayout)findViewById(R.id.llnight);

        llmor.setVisibility(View.GONE);
        llafter.setVisibility(View.GONE);
        lleve.setVisibility(View.GONE);
        llnight.setVisibility(View.GONE);




        tvMorn=(TextView)findViewById(R.id.morningTime);
        tvAfter=(TextView)findViewById(R.id.afternnonTime);
        tvEve=(TextView)findViewById(R.id.eveningTime);
        tvNight=(TextView)findViewById(R.id.nightTime);

        tvMorn.setOnClickListener(this);
        tvAfter.setOnClickListener(this);
        tvEve.setOnClickListener(this);
        tvNight.setOnClickListener(this);

         btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTitleText.getText().toString().length() == 0){

                    mTitleText.setError("Reminder Title cannot be blank!");

                }else
                if (mMedicinest.getText().toString().length() == 0){

                    mMedicinest.setError("Medicine cannot be blank!");

                }else
                if (set_dose_repeat_type.getText().toString().length() == 0){

                    set_dose_repeat_type.setError("Doses Type cannot be blank!");

                }else
                if (mDateText.getText().toString().length() == 0){

                    mDateText.setError("Starting date cannot be blank!");

                }else
                if (set_end_date.getText().toString().length() == 0){

                    set_end_date.setError("End date cannot be blank!");

                }
                else {

                    mTitleText.setError(null);
                    mMedicinest.setError(null);
                    set_dose_repeat_type.setError(null);
                    mDateText.setError(null);
                    set_end_date.setError(null);
                    saveReminder();



                }
            }
        });



    }

    private void setUpData() {

        mTitleText.setText(MyApplication.remainder.getTitle());
        mMedicinest.setText(MyApplication.remainder.getmMedicine());

        mDateText.setText(MyApplication.remainder.getDate());
        set_end_date.setText(MyApplication.remainder.getmEndDate());

        set_dose_repeat_type.setText(MyApplication.remainder.getmDoseType());


        String times= MyApplication.remainder.getTime();
        Log.e("**TIME**",times);

        if(times.contains("Mor")){

            llmor.setVisibility(View.VISIBLE);
            isMorning=true;
            morTime="0:00";




        }

        if(times.contains("Noon") || times.contains("noon")){

            llafter.setVisibility(View.VISIBLE);
            isAfternoon=true;
            afterTime="0:00";


        }

        if(times.contains("Eve")){

            lleve.setVisibility(View.VISIBLE);

            isEvening=true;
            eveTime="0:00";
        }


        if(times.contains("Nigh")){

            llnight.setVisibility(View.VISIBLE);

            isNight=true;

            nightTime="0:00";
        }


        String t= MyApplication.remainder.getRepeatType();
        Log.e("###"+"REPEAT TYPE",t);



            String[] arr=t.split(",");

            for(int i=0;i<arr.length;i++){

                if(arr[i].contains("Mor")){
                    String[] m=arr[i].split("#");
                    Log.e("###ARRAY",m.toString());
                    tvMorn.setText(m[1]);
                    morTime=m[1];

                }

                if(arr[i].contains("noon") || arr[i].contains("Noon")){
                    String[] m=arr[i].split("#");
                    tvAfter.setText(m[1]);
                    Log.e("###ARRAY",m.toString());
                    afterTime=m[1];
                }

                if(arr[i].contains("eve") || arr[i].contains("eve")){
                    String[] m=arr[i].split("#");
                    Log.e("###ARRAY",m.toString());
                    tvEve.setText(m[1]);
                    eveTime=m[1];
                }

                if(arr[i].contains("Nigh") || arr[i].contains("Nigh")){
                    String[] m=arr[i].split("#");
                    Log.e("###ARRAY",m.toString());
                    tvNight.setText(m[1]);
                    nightTime=m[1];
                }


            }




    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.morningTime:
                isMorningEdit=true;

                timeType=1;

                setTime(tvMorn);
                Log.e("TvMorng",tvMorn.getText().toString());

                break;

            case R.id.afternnonTime:
                isAfternoonEdit=true;

                timeType=2;
                Log.e("Tvnoon",tvAfter.getText().toString());


                setTime(tvAfter);


                break;
            case R.id.eveningTime:

                isEveningEdit=true;

                timeType=3;
                Log.e("TvEve",tvEve.getText().toString());

                setTime(tvEve);

                break;

            case R.id.nightTime:
                isNightEdit=true;

                timeType=4;
                Log.e("TvNigh",tvNight.getText().toString());
                setTime(tvNight);

                break;
        }
    }

    // On clicking Time picker
    public void setTime(View v){


        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");



    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        int mHour = hourOfDay;
        int mMinute = minute;
        String mTime= "";
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
            Log.e("ON TIME SET",mTime);
        } else {
            mTime = hourOfDay + ":" + minute;
            Log.e("ON TIME SET",mTime);
        }

        if(timeType==1){

            Log.e("MORNING","TRUE");

            isMorning=true;
            morTime=mTime;
            tvMorn.setText(mTime);

            times=times + " Morning ";
        /*    if (isAfternoon){
                times=times + " Afternoon ";
            }
            if (isEvening){
                times=times + " Evening ";
            }
            if (isNight){
                times=times + " Night ";
            }*/

            updatedTimes=updatedTimes +",Morning#"+mTime;

        }else if(timeType==2){
            Log.e("AFTERNOON","TRUE");

            isAfternoon=true;
            afterTime=mTime;
            tvAfter.setText(mTime);


/*

            if (isMorning){
                times=times + " Morning ";
            }
*/

            times=times + " Afternoon ";

          /*  if (isEvening){
                times=times + " Evening ";
            }
            if (isNight){
                times=times + " Night ";
            }
*/
            updatedTimes=updatedTimes +",Afternoon#"+mTime;


        }if(timeType==3){
            Log.e("EVENING","TRUE");

            isEvening=true;
            eveTime=mTime;
            tvEve.setText(mTime);

      /*      if (isMorning){
                times=times + " Morning ";
            }

            if (isAfternoon){
                times=times + " Afternoon ";
            }
*/
            times=times + " Evening ";
/*

            if (isNight){
                times=times + " Night ";
            }
*/

            updatedTimes=updatedTimes +",Evening#"+mTime;



        }if(timeType==4){

            Log.e("NIGHT","TRUE");


            isNight=true;
            nightTime=mTime;
            tvNight.setText(mTime);
           /* if (isMorning){
                times=times + " Morning ";
            }

            if (isAfternoon){
                times=times + " Afternoon ";
            }

            if (isEvening){
                times=times + " Evening ";
            }*/

            times=times + " Night ";

            updatedTimes=updatedTimes +",Night#"+mTime;



        }

Log.e("######",mTime);



    }

    /***Edit Remainder ***/

    // On clicking the save button
    public void saveReminder(){





        String dose_type= MyApplication.remainder.getmDoseType();
   /*     if(isMorning){

            if (isMorningEdit){

            }

            dose_type = dose_type + "Morning";

        }

     if(isAfternoon){
            if (isAfternoonEdit){

                dose_type = dose_type + "Afternoon";

            }


        }


        if(isEvening){
            if (isEveningEdit){

                dose_type = dose_type + "Evening";

            }


        }


        if(isNight){

            if (isNightEdit){
                dose_type = dose_type + "Night";
            }

        }else {
            dose_type="Night";
        }




        if(isMorning){

            if (isMorningEdit){
                dose_type = dose_type + "Morning";
            }

            dose_type = dose_type + "Morning";

        }else {
        }

        if(isAfternoon){
            if (isAfternoonEdit){
                dose_type = dose_type + "Afternoon";
            }

            dose_type = dose_type + "Afternoon";
        }else {

        }


        if(isEvening){
            if (isEveningEdit){

                dose_type = dose_type + "Evening";

            }
            dose_type = dose_type + "Evening";

        }else {
            dose_type = dose_type + "Evening";
        }


        if(isNight){

            if (isNightEdit){
                dose_type = dose_type + "Night";
            }

            dose_type = dose_type + "Night";


        }else {

        }

*/







        Log.e("DOSE_TYPE",dose_type);
        DataProvider rb = new DataProvider();


        for(Events events:rb.getEvents(EditRemainder.this, MyApplication.remainder.getID())){

            Log.e("EventId",events.getEventId()+"");

            Uri deleteUri = null;
            deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(String.valueOf(events.getEventId())));
            int rows = getContentResolver().delete(deleteUri, null, null);

            rb.deleteEvent(events.getMainId()+"",EditRemainder.this);

        }

        String mRepeat= "false";
        String mActive = "true";
        String mRepeatNo = Integer.toString(1);
        long i= rb.updateReminder(new Reminder(mTitleText.getText().toString(),
                mMedicinest.getText().toString(),
                dose_type,
                set_dose_repeat_type.getText().toString(),
                mDateText.getText().toString(),
                set_end_date.getText().toString(),
                times, mRepeat, mRepeatNo, updatedTimes, mActive),this,common.get_user_id(),"0", MyApplication.remainder.getID()+"");

            Log.e("UPDATED TIME",i+updatedTimes);


        List<Date> dates=getDates(mDateText.getText().toString(),set_end_date.getText().toString());

        for(Date date:dates){

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Log.e("Date:",sdf.format(date));


            if(isMorning){

                String mor[]=morTime.split(":");

                Calendar cal=toCalendar(date);
                Calendar mCalendar= Calendar.getInstance();
                mCalendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                mCalendar.set(Calendar.YEAR,  cal.get(Calendar.YEAR));
                mCalendar.set(Calendar.DAY_OF_MONTH,  cal.get(Calendar.DAY_OF_MONTH));
                mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mor[0]));
                mCalendar.set(Calendar.MINUTE, Integer.parseInt(mor[1]));
                mCalendar.set(Calendar.SECOND, 0);

              long eventId=   addAppointmentsToCalender(EditRemainder.this,mTitleText.getText().toString(),
                        mMedicinest.getText().toString(), "", 1, mCalendar.getTimeInMillis(),
                        true, false);

              Log.e("###TIME", String.valueOf(mCalendar.getTimeInMillis()));


                rb.addEvent(EditRemainder.this, MyApplication.remainder.getID(),eventId);


                //  new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);

            }

            if(isAfternoon){

                String mor[]=afterTime.split(":");


                Calendar cal=toCalendar(date);
                Calendar mCalendar= Calendar.getInstance();
                mCalendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                mCalendar.set(Calendar.YEAR,  cal.get(Calendar.YEAR));
                mCalendar.set(Calendar.DAY_OF_MONTH,  cal.get(Calendar.DAY_OF_MONTH));
                mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mor[0]));
                mCalendar.set(Calendar.MINUTE, Integer.parseInt(mor[1]));
                mCalendar.set(Calendar.SECOND, 0);

             long eventId=   addAppointmentsToCalender(EditRemainder.this,mTitleText.getText().toString(),
                        mMedicinest.getText().toString(), "", 1, mCalendar.getTimeInMillis(),
                        true, false);

                rb.addEvent(EditRemainder.this, MyApplication.remainder.getID(),eventId);


                //  new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);

            }

            if(isEvening){

                String mor[]=eveTime.split(":");


                Calendar cal=toCalendar(date);
                Calendar mCalendar= Calendar.getInstance();
                mCalendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                mCalendar.set(Calendar.YEAR,  cal.get(Calendar.YEAR));
                mCalendar.set(Calendar.DAY_OF_MONTH,  cal.get(Calendar.DAY_OF_MONTH));
                mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mor[0]));
                mCalendar.set(Calendar.MINUTE, Integer.parseInt(mor[1]));
                mCalendar.set(Calendar.SECOND, 0);

               long eventId=  addAppointmentsToCalender(EditRemainder.this,mTitleText.getText().toString(),
                        mMedicinest.getText().toString(), "", 1, mCalendar.getTimeInMillis(),
                        true, false);

                rb.addEvent(EditRemainder.this, MyApplication.remainder.getID(),eventId);


                // new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);



            }

            if(isNight){

                String mor[]=nightTime.split(":");


                Calendar cal=toCalendar(date);
                Calendar mCalendar= Calendar.getInstance();
                mCalendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                mCalendar.set(Calendar.YEAR,  cal.get(Calendar.YEAR));
                mCalendar.set(Calendar.DAY_OF_MONTH,  cal.get(Calendar.DAY_OF_MONTH));
                mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mor[0]));
                mCalendar.set(Calendar.MINUTE, Integer.parseInt(mor[1]));
                mCalendar.set(Calendar.SECOND, 0);

               long eventId=  addAppointmentsToCalender(EditRemainder.this,mTitleText.getText().toString(),
                        mMedicinest.getText().toString(), "", 1, mCalendar.getTimeInMillis(),
                        true, false);


                rb.addEvent(EditRemainder.this, MyApplication.remainder.getID(),eventId);

                Log.e("###TIME", String.valueOf(mCalendar.getTimeInMillis()));



                //new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);


            }
        }


        Toast.makeText(getApplicationContext(), "Updated Successfully",
                Toast.LENGTH_SHORT).show();
        onBackPressed();

    }

    private static List<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /*****Raminders*****/

    public long addAppointmentsToCalender(Activity curActivity, String title,
                                          String desc, String place, int status, long startDate,
                                          boolean needReminder, boolean needMailService) {
/***************** Event: add event *******************/
        long eventID = -1;
        try {
            String eventUriString = "content://com.android.calendar/events";
            ContentValues eventValues = new ContentValues();
            eventValues.put("calendar_id", 1); // id, We need to choose from
            // our mobile for primary its 1
            eventValues.put("title", title);
            eventValues.put("description", desc);
            eventValues.put("eventLocation", place);

            long endDate = startDate + 1000 * 10 * 10; // For next 10min
            eventValues.put("dtstart", startDate);
            eventValues.put("dtend", endDate);

            // values.put("allDay", 1); //If it is bithday alarm or such
            // kind (which should remind me for whole day) 0 for false, 1
            // for true
            eventValues.put("eventStatus", status); // This information is
            // sufficient for most
            // entries tentative (0),
            // confirmed (1) or canceled
            // (2):
            eventValues.put("eventTimezone", "UTC/GMT +5:30");
 /*
  * Comment below visibility and transparency column to avoid
  * java.lang.IllegalArgumentException column visibility is invalid
  * error
  */
            // eventValues.put("allDay", 1);
            // eventValues.put("visibility", 0); // visibility to default (0),
            // confidential (1), private
            // (2), or public (3):
            // eventValues.put("transparency", 0); // You can control whether
            // an event consumes time
            // opaque (0) or transparent (1).

            eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

            Uri eventUri = curActivity.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(eventUriString), eventValues);
            eventID = Long.parseLong(eventUri.getLastPathSegment());

            Log.e("eventID",eventID+"");

            if (needReminder) {
                /***************** Event: Reminder(with alert) Adding reminder to event ***********        ********/

                String reminderUriString = "content://com.android.calendar/reminders";
                ContentValues reminderValues = new ContentValues();
                reminderValues.put("event_id", eventID);
                reminderValues.put("minutes", 5); // Default value of the
                // system. Minutes is a integer
                reminderValues.put("method", 1); // Alert Methods: Default(0),
                // Alert(1), Email(2),SMS(3)

                Uri reminderUri = curActivity.getApplicationContext()
                        .getContentResolver()
                        .insert(Uri.parse(reminderUriString), reminderValues);

                Log.e("reminderUri",reminderUri.getPath()+"");


            }

/***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

            if (needMailService) {
                String attendeuesesUriString = "content://com.android.calendar/attendees";
                /********
                 * To add multiple attendees need to insert ContentValues
                 * multiple times
                 ***********/
                ContentValues attendeesValues = new ContentValues();
                attendeesValues.put("event_id", eventID);
                attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
                attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee Email
                attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
                // Relationship_None(0),
                // Organizer(2),
                // Performer(3),
                // Speaker(4)
                attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
                // Required(2),
                // Resource(3)
                attendeesValues.put("attendeeStatus", 0); // NOne(0),
                // Accepted(1),
                // Decline(2),
                // Invited(3),
                // Tentative(4)

                Uri eventsUri = Uri.parse("content://calendar/events");
                Uri url = curActivity.getApplicationContext()
                        .getContentResolver()
                        .insert(eventsUri, attendeesValues);

                // Uri attendeuesesUri = curActivity.getApplicationContext()
                // .getContentResolver()
                // .insert(Uri.parse(attendeuesesUriString), attendeesValues);
            }
        } catch (Exception ex) {
        }

        return eventID;

    }




}
