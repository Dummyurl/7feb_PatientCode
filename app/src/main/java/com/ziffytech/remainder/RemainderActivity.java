
package com.ziffytech.remainder;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.MyApplication;
import com.ziffytech.adapters.RemainderAdapter;
import com.ziffytech.chat.DataProvider;
import com.ziffytech.util.RecyclerItemClickListener;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class RemainderActivity extends CommonActivity {

    private RecyclerView mList;
    private RemainderAdapter mAdapter;
    private TextView mNoReminderView;
    private List<Reminder> mTest;
    private DataProvider rb ;

    private ArrayList<RemainderModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_remainder);

        allowBack();
        setHeaderTitle("Reminders");

        list=new ArrayList<>();



        mTest=new ArrayList<>();
        rb = new DataProvider();

        mNoReminderView = (TextView) findViewById(R.id.no_reminder_text);
        mNoReminderView.setVisibility(View.GONE);


        mList = (RecyclerView) findViewById(R.id.reminder_list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(layoutManager);
        mAdapter = new RemainderAdapter(this,mTest);
        mList.setAdapter(mAdapter);


        final AlarmReceiver mAlarmReceiver = new AlarmReceiver();

        mList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, final int position)
            {
                confirmDialog(position);
            }
        }));

        getRecords();

        FloatingActionButton  mAddReminderButton = (FloatingActionButton) findViewById(R.id.add_reminder);
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReminderAddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData()
    {
        if(!mTest.isEmpty())
        {
            mTest.clear();
        }

        mTest = rb.getAllReminders(this,common.get_user_id());

        if (mTest.isEmpty())
        {
            mNoReminderView.setVisibility(View.VISIBLE);
        }else
        {
            mNoReminderView.setVisibility(View.GONE);
            mAdapter = new RemainderAdapter(this,mTest);
            mList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }

    }


    private void confirmDialog(final int position)
    {

        String[] arr={"Edit","Delete","Cancel"};

        AlertDialog.Builder ad=  new AlertDialog.Builder(RemainderActivity.this);
        ad.setTitle("Choose");
        ad.setItems(arr, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                switch (i)
                {
                    case 0:
                        dialogInterface.dismiss();
                        MyApplication.remainder=mTest.get(position);
                        startActivity(new Intent(RemainderActivity.this,EditRemainder.class));
                        break;

                    case 1:
                        dialogInterface.dismiss();
                        Log.e("AT","ITEM CLICK");
                        AlertDialog.Builder ad=  new AlertDialog.Builder(RemainderActivity.this);
                        ad.setMessage("Are you sure you want to delete remainder?");
                        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.dismiss();

                                rb.deleteReminder(mTest.get(position),RemainderActivity.this);
                                mAdapter.notifyItemRemoved(position);
                                mAdapter.notifyDataSetChanged();


                                DataProvider db=new DataProvider();

                                for(Events events:db.getEvents(RemainderActivity.this,mTest.get(position).getID()))
                                {

                                    Log.e("EventId",events.getEventId()+"");
                                    Uri deleteUri = null;
                                    deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(String.valueOf(events.getEventId())));
                                    int rows = getContentResolver().delete(deleteUri, null, null);
                                    rb.deleteEvent(events.getMainId()+"",RemainderActivity.this);

                                }

                                onStart();
                            }
                        });
                        ad.setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.dismiss();
                            }
                        });
                        ad.create().show();

                        break;


                    case 2:

                        dialogInterface.dismiss();

                        break;
                }
            }
        });

        ad.create().show();


    }

    public void getRecords()
    {

        if(!list.isEmpty())
        {
            list.clear();
        }

        showPrgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_REMAINDERS, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        Log.e("####"+"Prescription:",responce);

                        try
                        {
                            JSONObject jsonObject=new JSONObject(responce);
                            JSONArray meds=jsonObject.getJSONArray("data");

                            for(int i=0;i<meds.length();i++)
                            {
                                JSONObject obj=meds.getJSONObject(i);
                                String medication=obj.getString("medication");

                                JSONArray array=new JSONArray(medication);

                                for(int j=0;j<array.length();j++)
                                {
                                    JSONObject objm=array.getJSONObject(j);

                                    RemainderModel model=new RemainderModel();

                                    model.setId(obj.getString("id"));
                                    model.setDrug_name(objm.getString("drug_name"));
                                    model.setStrength(objm.getString("strength"));
                                    model.setTake(objm.getString("take"));
                                    model.setDuration(objm.getString("duration"));
                                    String str1=objm.getString("slot");
                                    model.setSlot(String.valueOf(Html.fromHtml(str1)));

                                    Log.e("##","##"+objm.getString("slot")+str1);
                                    model.setSlot(objm.getString("slot"));
                                    model.setDose_type(objm.getString("dose_type"));

                                    list.add(model);

                                }

                            }


                            if(!list.isEmpty())
                            {
                                addRemainders();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void VError(String responce) {
                         hideProgressBar();
                    }
                });

    }



    private void addRemainders()
    {

        if (!rb.isRemainderPresent(RemainderActivity.this, list.get(0).getId()))
        {

        for(RemainderModel model:list)
        {

            Log.e("ITem:",new Gson().toJson(model).toString());


            DataProvider rb = new DataProvider();


                long date1 = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String current_date = sdf.format(date1);


                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(current_date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.add(Calendar.DATE, Integer.parseInt(model.getDuration()));  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                String endDate = sdf.format(c.getTime());

                int ID = rb.addReminder(new Reminder(
                        model.getDrug_name(),
                        model.getDrug_name(),
                        model.getDose_type(),
                        model.getDose_type(),
                        current_date,
                        endDate,
                        model.getSlot(), "false", "", "", ""), this, common.get_user_id(),model.getId());




                List<Date> dates = getDates(current_date, endDate);

                for (Date date : dates) {


                    if (model.getSlot().contains("Mor"))
                    {
                        Calendar cal = toCalendar(date);
                        Calendar mCalendar = Calendar.getInstance();
                        mCalendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                        mCalendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
                        mCalendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
                        mCalendar.set(Calendar.HOUR_OF_DAY, 8);
                        mCalendar.set(Calendar.MINUTE, 0);
                        mCalendar.set(Calendar.SECOND, 0);

                     long eventId = addAppointmentsToCalender(RemainderActivity.this, model.getDrug_name(), model.getDrug_name(), "", 1, mCalendar.getTimeInMillis(), true, false);

                        rb.addEvent(RemainderActivity.this,ID,eventId);

                        //  new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);

                    }

                    if (model.getSlot().contains("Noon")) {


                        Calendar cal = toCalendar(date);
                        Calendar mCalendar = Calendar.getInstance();
                        mCalendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                        mCalendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
                        mCalendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
                        mCalendar.set(Calendar.HOUR_OF_DAY, 13);
                        mCalendar.set(Calendar.MINUTE, 0);
                        mCalendar.set(Calendar.SECOND, 0);

                       long eventId= addAppointmentsToCalender(RemainderActivity.this, model.getDrug_name(),
                                model.getDrug_name(), "", 1, mCalendar.getTimeInMillis(),
                                true, false);


                        rb.addEvent(RemainderActivity.this,ID,eventId);

                        //  new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);

                    }

                    if (model.getSlot().contains("Eve")) {


                        Calendar cal = toCalendar(date);
                        Calendar mCalendar = Calendar.getInstance();
                        mCalendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                        mCalendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
                        mCalendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
                        mCalendar.set(Calendar.HOUR_OF_DAY, 17);
                        mCalendar.set(Calendar.MINUTE, 0);
                        mCalendar.set(Calendar.SECOND, 0);

                     long eventId=   addAppointmentsToCalender(RemainderActivity.this, model.getDrug_name(),
                                model.getDrug_name(), "", 1, mCalendar.getTimeInMillis(),
                                true, false);


                        rb.addEvent(RemainderActivity.this,ID,eventId);


                        // new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);


                    }

                    if (model.getSlot().contains("Nigh")) {


                        Calendar cal = toCalendar(date);
                        Calendar mCalendar = Calendar.getInstance();
                        mCalendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                        mCalendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
                        mCalendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
                        mCalendar.set(Calendar.HOUR_OF_DAY, 20);
                        mCalendar.set(Calendar.MINUTE, 0);
                        mCalendar.set(Calendar.SECOND, 0);

                       long eventId=   addAppointmentsToCalender(RemainderActivity.this, model.getDrug_name(),
                                model.getDrug_name(), "", 1, mCalendar.getTimeInMillis(),
                                true, false);


                        rb.addEvent(RemainderActivity.this,ID,eventId);


                        //new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);


                    }
                }

              //  Toast.makeText(getApplicationContext(), "Saved",
                //        Toast.LENGTH_SHORT).show();

                getData();

            }

        }

    }



    public long addAppointmentsToCalender(Activity curActivity, String title,
                                          String desc, String place, int status, long startDate,
                                          boolean needReminder, boolean needMailService)
    {

            /***************** Event: add event *******************/

            long eventID = -1;

             try {
                     String eventUriString = "content://com.android.calendar/events";
                     ContentValues eventValues = new ContentValues();
                     eventValues.put("calendar_id", 1); // id, We need to choose from // our mobile for primary its 1
                     eventValues.put("title", title);
                     eventValues.put("description", desc);
                     eventValues.put("eventLocation", place);

                       long endDate = startDate + 1000 * 10 * 10; // For next 10min
                       eventValues.put("dtstart", startDate);
                       eventValues.put("dtend", endDate);

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

            if (needReminder)

            {
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

            if (needMailService)

            {
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
           // Log.e("Error r" , ex.getMessage());
        }

        return eventID;

    }



    private static List<Date> getDates(String dateString1, String dateString2)
    {

        Log.e("StartDate:",dateString1);
        Log.e("EndDate:",dateString2);


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

}
