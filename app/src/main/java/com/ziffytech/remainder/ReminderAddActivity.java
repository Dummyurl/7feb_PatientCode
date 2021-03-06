

package com.ziffytech.remainder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.chat.DataProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ReminderAddActivity extends CommonActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private EditText mTitleText;
    private EditText mMedicinest;

    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText,set_end_date,set_dose_repeat_type;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
   private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;

    // Values for orientation change
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    ArrayList<Integer> selList=new ArrayList();
    String msg ="";

    String dateType="";
    String times="";


    TextView tvMorn,tvAfter,tvEve,tvNight;
    boolean isMorning=false,isAfternoon=false,isEvening=false,isNight=false;
    String morTime,afterTime,eveTime,nightTime;
    int timeType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        allowBack();
        setHeaderTitle("Add");

        // Initialize Views
        mTitleText = (EditText) findViewById(R.id.reminder_title);
        mMedicinest=(EditText)findViewById(R.id.medicine_name);

        mDateText = (TextView) findViewById(R.id.set_date);
        mTimeText = (TextView) findViewById(R.id.set_time);
        mRepeatText = (TextView) findViewById(R.id.set_repeat);
        mRepeatNoText = (TextView) findViewById(R.id.set_repeat_no);
        mRepeatTypeText = (TextView) findViewById(R.id.set_repeat_type);
        set_dose_repeat_type=(TextView)findViewById(R.id.set_dose_repeat_type);
        set_end_date=(TextView)findViewById(R.id.set_end_date);

        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);

        // Initialize default values
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";

        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;



        // Setup TextViews using reminder values
        mDateText.setText(mDate);
        mTimeText.setText(mTime);
        mRepeatNoText.setText(mRepeatNo);
        mRepeatTypeText.setText(mRepeatType);
        mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");

        // To save state on device rotation
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(saveRepeat);
            mRepeat = saveRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            mRepeatNoText.setText(savedRepeatNo);
            mRepeatNo = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            mRepeatTypeText.setText(savedRepeatType);
            mRepeatType = savedRepeatType;

            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }



        tvMorn=(TextView)findViewById(R.id.morningTime);
        tvAfter=(TextView)findViewById(R.id.afternnonTime);
        tvEve=(TextView)findViewById(R.id.eveningTime);
        tvNight=(TextView)findViewById(R.id.nightTime);

        tvMorn.setOnClickListener(this);
        tvAfter.setOnClickListener(this);
        tvEve.setOnClickListener(this);
        tvNight.setOnClickListener(this);

        // Setup up active buttons
        if (mActive.equals("false")) {
            mFAB1.setVisibility(View.GONE);
            mFAB2.setVisibility(View.GONE);

        } else if (mActive.equals("true")) {
            mFAB1.setVisibility(View.GONE);
            mFAB2.setVisibility(View.GONE);
        }

        Button btnSave=(Button)findViewById(R.id.btnSave);
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

    // To save state on device rotation
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTypeText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);
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

    // On clicking Date picker
    public void setDate(View v){
        dateType="start";
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        now.add(Calendar.MONTH,+1);
        dpd.setMaxDate(now);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    // On clicking Date picker
    public void setEndDate(View v){
        dateType="end";
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        now.add(Calendar.MONTH,+1);
        dpd.setMaxDate(now);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }



    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

       int mHour = hourOfDay;
       int mMinute = minute;
        String mTime= "";
        if (minute < 10) {
           mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }

        if(timeType==1){

            isMorning=true;
            morTime=mTime;
            tvMorn.setText(mTime);

        }else if(timeType==2){

            isAfternoon=true;
            afterTime=mTime;
            tvAfter.setText(mTime);

        }if(timeType==3){

            isEvening=true;
            eveTime=mTime;
            tvEve.setText(mTime);

        }if(timeType==4){

            isNight=true;
            nightTime=mTime;
            tvNight.setText(mTime);

        }

        times=times + " "+mTime;



    }

    // Obtain date from date picker
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        monthOfYear ++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;

        String mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        if(dateType.equalsIgnoreCase("start")){

            mDateText.setText(mDate);

        }else if(dateType.equalsIgnoreCase("end")){

            set_end_date.setText(mDate);
        }

    }

    // On clicking the active button
    public void selectFab1(View v) {
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.GONE);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.GONE);
        mActive = "true";
    }

    // On clicking the inactive button
    public void selectFab2(View v) {
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.GONE);
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.GONE);
        mActive = "false";
    }

    // On clicking the repeat switch
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat = "true";
            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
        } else {
            mRepeat = "false";
            mRepeatText.setText(R.string.repeat_off);
        }
    }

    // On clicking repeat type button
    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "Minute";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";

        // Create List Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mRepeatType = items[item];
                mRepeatTypeText.setText(mRepeatType);
                mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // On clicking dose type button
    public void selectDoseType(View v){

        if(!selList.isEmpty()){
            selList.clear();
        }

        final String[] items = new String[4];

        items[0] = "Morning";
        items[1] = "Afternoon";
        items[2] = "Evening";
        items[3] = "Night";

        // Create List Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Dose");
        boolean bl[] = new boolean[items.length];
        builder.setMultiChoiceItems(items, bl, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int arg1, boolean arg2) {

                if(arg2)
                {
                    // If user select a item then add it in selected items
                    selList.add(arg1);
                }
                else if (selList.contains(arg1))
                {
                    // if the item is already selected then remove it
                    selList.remove(Integer.valueOf(arg1));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                msg="";
                times="";
                for (int i = 0; i < selList.size(); i++) {

                    msg=msg+" "+items[selList.get(i)];

                    switch (items[selList.get(i)]){

                        case "Morning":
                            times=times + " "+"9:00";
                            break;

                        case "Afternoon":
                            times=times + " "+"14:00";
                            break;

                        case "Evening":
                            times=times + " "+"17:00";
                            break;

                        case "Night":
                            times=times + " "+"20:00";
                            break;
                    }

                }

                mTimeText.setText(times);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
              dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    // On clicking repeat interval button
    public void setRepeatNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");

        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mRepeatNo = Integer.toString(1);
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
                        }
                        else {
                            mRepeatNo = input.getText().toString().trim();
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    // On clicking the save button
    public void saveReminder(){



        String dose_type="";
        if(isMorning){

            dose_type = dose_type + "Morning";

        }

        if(isAfternoon){

            dose_type = dose_type + "Afternoon";

        }

        if(isEvening){

            dose_type = dose_type + "Evening";

        }

        if(isNight){

            dose_type = dose_type + "Night";

        }

        DataProvider rb = new DataProvider();
        mRepeat= "false";
        int ID = rb.addReminder(new Reminder(mTitleText.getText().toString(),
                                             mMedicinest.getText().toString(),
                                             dose_type,
                                             set_dose_repeat_type.getText().toString(),
                                             mDateText.getText().toString(),
                                             set_end_date.getText().toString(),
                                             times, mRepeat, mRepeatNo, mRepeatType, mActive),this,common.get_user_id(),"0");


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

                 long eventId=   addAppointmentsToCalender(ReminderAddActivity.this,mTitleText.getText().toString(),
                            mMedicinest.getText().toString(), "", 1, mCalendar.getTimeInMillis(),
                            true, false);


                    rb.addEvent(ReminderAddActivity.this,ID,eventId);


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

                   long eventId=   addAppointmentsToCalender(ReminderAddActivity.this,mTitleText.getText().toString(),
                            mMedicinest.getText().toString(), "", 1, mCalendar.getTimeInMillis(),
                            true, false);


                    rb.addEvent(ReminderAddActivity.this,ID,eventId);

                    //  new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);

                }

                if(isEvening)

                {
                    String mor[]=eveTime.split(":");
                    Calendar cal=toCalendar(date);
                    Calendar mCalendar= Calendar.getInstance();
                    mCalendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                    mCalendar.set(Calendar.YEAR,  cal.get(Calendar.YEAR));
                    mCalendar.set(Calendar.DAY_OF_MONTH,  cal.get(Calendar.DAY_OF_MONTH));
                    mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mor[0]));
                    mCalendar.set(Calendar.MINUTE, Integer.parseInt(mor[1]));
                    mCalendar.set(Calendar.SECOND, 0);

                  long eventId=  addAppointmentsToCalender(ReminderAddActivity.this,mTitleText.getText().toString(),
                            mMedicinest.getText().toString(), "", 1, mCalendar.getTimeInMillis(),
                            true, false);

                    rb.addEvent(ReminderAddActivity.this,ID,eventId);

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

                 long eventId=   addAppointmentsToCalender(ReminderAddActivity.this,mTitleText.getText().toString(),
                            mMedicinest.getText().toString(), "", 1, mCalendar.getTimeInMillis(),
                            true, false);


                    rb.addEvent(ReminderAddActivity.this,ID,eventId);


                    //new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);


            }
        }



        Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show();
        onBackPressed();

    }

    // On pressing the back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Creating the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    // On clicking menu buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save_reminder:


                return true;

            case R.id.discard_reminder:
                Toast.makeText(getApplicationContext(), "Discarded",
                        Toast.LENGTH_SHORT).show();

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // On clicking repeat type button
    public void selectDailyType(View v){
        final String[] items = new String[2];

        items[0] = "Daily";
        items[1] = "Weekly";

        // Create List Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                set_dose_repeat_type.setText(items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.morningTime:

                timeType=1;

                setTime(tvMorn);

                break;

            case R.id.afternnonTime:

                timeType=2;

                setTime(tvAfter);

                break;
            case R.id.eveningTime:

                timeType=3;

                setTime(tvEve);

                break;

            case R.id.nightTime:

                timeType=4;

                setTime(tvNight);

                break;
        }
    }


    /*****Ramainders*****/

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

           // eventValues.put(CalendarContract.Events.H, 1);

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

