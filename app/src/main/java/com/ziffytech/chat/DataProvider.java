package com.ziffytech.chat;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.ziffytech.remainder.Events;
import com.ziffytech.remainder.Reminder;

import java.util.ArrayList;
import java.util.List;


/**
 * @author appsrox.com
 *
 */

public class DataProvider extends ContentProvider
{
	
	public static final Uri CONTENT_URI_MESSAGES = Uri.parse("content://com.ziffytech.provider/messages");
	public static final Uri CONTENT_URI_PROFILE = Uri.parse("content://com.ziffytech.provider/profile");
	public static final Uri CONTENT_URI_GROUP= Uri.parse("content://com.ziffytech.provider/group");
	public static final Uri CONTENT_URI_GROUP_MESSAGES= Uri.parse("content://com.ziffytech.provider/gmessages");
	public static final String COL_ID = "_id";
	public static final String TABLE_MESSAGES = "messages";
	public static final String COL_MSG = "msg";
	public static final String COL_FROM = "user_id";
    public static final String COL_TO = "friend_id";
	public static final String COL_AT = "at";
	public static final String COL_MSG_ID = "msg_id";
	public static final String COL_IS_VIEW = "is_view";
	public static final String COL_IS_SENT = "is_send";
	public static final String COL_LAST_SEEN_TIME = "last_seen";
	public static final String COL_USER_ID = "user_id";
	public static final String COL_IS_IMAGE = "is_image";
	public static final String COL_IS_UPLOAD = "is_upload";
	public static final String COL_LAST_MSG_DATE = "last_msg_date";
	public static final String COL_IS_STREAM = "is_stream";
	public static final String COL_STREAM_ID = "stream_id";
	public static final String COL_STREAM_LINK = "stream_link";

	//notifications

	public static final String TABLE_NOTIFICATION_FRIEND = "friend_notification_chat";
	public static final String TABLE_NOTIFICATION_GROUP = "group_notification_chat";
	public static final String COL_FRIEND_ID = "friend_id";

	//table group messages

	public static final String TABLE_GROUP_MESSAGES = "group_messages";
	public static final String COL_PARTNER_NAME = "partner_name";
	public static final String COL_PARTNER_SOCIAL_ID = "partner_social_id";
	public static final String COL_PARTNER_TYPE = "partner_type";
	public static final String COL_PARTNER_IMAGE = "partner_image";
	public static final String COL_PARTNER_COLOR = "partner_color";

	
	public static final String TABLE_PROFILE = "profile";
	public static final String COL_NAME = "name";
	public static final String COL_EMAIL = "email";
	public static final String COL_COUNT = "count";
	public static final String COL_LAST_MSG = "last_msg";
	public static final String COL_IS_TYPING = "is_typing";
	public static final String COL_IS_ONLINE="is_online";
	public static final String COL_IMAGE="image";
	public static final String COL_SOCIAL_ID="social_id";
	public static final String COL_SOCIAL_TYPE="social_type";
	public static final String COL_LAST_SEEN="last_seen_time";
	public static final String COL_IS_MINE="is_mine";
	public static final String COL_IS_GROUP="is_group";
	public static final String COL_GROUP_ID="group_id";
	public static final String COL_IS_STREAM_PROFILE="is_stram";


	private DbHelper dbHelper;
	
	private static final int MESSAGES_ALLROWS = 1;
	private static final int MESSAGES_SINGLE_ROW = 2;
	private static final int PROFILE_ALLROWS = 3;
	private static final int PROFILE_SINGLE_ROW = 4;
	private static final int GROUP_ALLROWS = 5;
	private static final int GROUP_SINGLE_ROW = 6;
	private static final int GROUP_MESSAGES_ALLROWS = 7;
	private static final int GROUP_MESSAGES_SINGLE_ROW = 8;



	public static final String TABLE_CHAT_BG = "chat_bg";
	public static final String CHAT_BG_ID = "user_id";
	public static final String CHAT_BG_TYPE = "type";
	public static final String CHAT_BG_BG = "bg_id";


	// Table name
	private static final String TABLE_REMINDERS = "ReminderTable";

	// Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_DATE = "date";
	private static final String KEY_TIME = "time";
	private static final String KEY_REPEAT = "repeat";
	private static final String KEY_REPEAT_NO = "repeat_no";
	private static final String KEY_REPEAT_TYPE = "repeat_type";
	private static final String KEY_ACTIVE = "active";

	private static final String KEY_MEDICINE_NAMES ="medicine_name";
	private static final String KEY_DOSE_TYPE ="dose_type";
	private static final String KEY_DOSE_REPEAT_TYPE ="dose_repeat_type";
	private static final String KEY_END_DATE ="end_date";


	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("com.ziffytech.provider", "messages", MESSAGES_ALLROWS);
		uriMatcher.addURI("com.ziffytech.provider", "messages/#", MESSAGES_SINGLE_ROW);
		uriMatcher.addURI("com.ziffytech.provider", "profile", PROFILE_ALLROWS);
		uriMatcher.addURI("com.ziffytech.provider", "profile/#", PROFILE_SINGLE_ROW);
		uriMatcher.addURI("com.ziffytech.provider", "group", GROUP_ALLROWS);
		uriMatcher.addURI("com.ziffytech.provider", "group/#", GROUP_SINGLE_ROW);
		uriMatcher.addURI("com.ziffytech.provider", "gmessages", GROUP_MESSAGES_ALLROWS);
		uriMatcher.addURI("com.ziffytech.provider", "gmessages/#", GROUP_MESSAGES_SINGLE_ROW);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DbHelper(getContext());
		return true;
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch(uriMatcher.match(uri)) {
			case MESSAGES_ALLROWS:
				qb.setTables(TABLE_MESSAGES);
				break;

			case MESSAGES_SINGLE_ROW:
				qb.setTables(TABLE_MESSAGES);
				qb.appendWhere("msg_id = " + uri.getLastPathSegment());
				break;

			case PROFILE_ALLROWS:
				qb.setTables(TABLE_PROFILE);
				break;

			case PROFILE_SINGLE_ROW:
				qb.setTables(TABLE_PROFILE);
				qb.appendWhere("user_id = " + uri.getLastPathSegment());
				break;


			case GROUP_SINGLE_ROW:
				qb.setTables(TABLE_PROFILE);
				qb.appendWhere("group_id = " + uri.getLastPathSegment());
				break;

			case GROUP_MESSAGES_ALLROWS:
				qb.setTables(TABLE_GROUP_MESSAGES);
				break;

			case GROUP_MESSAGES_SINGLE_ROW:
				qb.setTables(TABLE_GROUP_MESSAGES);
				qb.appendWhere("msg_id = " + uri.getLastPathSegment());
				break;


			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		long id = 0;
		switch(uriMatcher.match(uri)) {
			case MESSAGES_ALLROWS:

				//	if(!values.get(COL_MSG).toString().equalsIgnoreCase("typing...")){

				id = db.insertOrThrow(TABLE_MESSAGES, null, values);
				//	}

				if (values.get(COL_TO) == null) {

					//	if(values.get(COL_MSG).toString().equalsIgnoreCase("typing...")){

					//		db.execSQL("update profile set is_typing = '"+values.get(COL_MSG)+"' where email = ?", new Object[]{values.get(COL_FROM)});
					//		getContext().getContentResolver().notifyChange(CONTENT_URI_PROFILE, null);

					//	}else{

					//	if(!values.get(COL_MSG).toString().equalsIgnoreCase("typing...")){

					db.execSQL("update profile set last_msg = '"+values.get(COL_MSG)+"' , is_mine ='no' , count = count+1 where user_id = ?", new Object[]{values.get(COL_FROM)});
					getContext().getContentResolver().notifyChange(CONTENT_URI_PROFILE, null);
					//	}else{

					//		db.execSQL("update profile set is_typing = '"+values.get(COL_MSG)+"' where email = ?", new Object[]{values.get(COL_FROM)});
					//		getContext().getContentResolver().notifyChange(CONTENT_URI_PROFILE, null);

					//	}

					//	}

				}



				break;

			case PROFILE_ALLROWS:
				id = db.insertOrThrow(TABLE_PROFILE, null, values);
				break;

			case GROUP_MESSAGES_ALLROWS:

				id = db.insertOrThrow(TABLE_GROUP_MESSAGES, null, values);

				if (values.get(COL_TO) != null) {


					db.execSQL("update profile set last_msg = '"+values.get(COL_PARTNER_NAME)+":"+values.get(COL_MSG)+"' , is_mine ='no' , count = count+1 where group_id = ?", new Object[]{values.get(COL_GROUP_ID)});
					getContext().getContentResolver().notifyChange(CONTENT_URI_PROFILE, null);

				}


				break;

			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		Uri insertUri = ContentUris.withAppendedId(uri, id);
		getContext().getContentResolver().notifyChange(insertUri, null);

		return insertUri;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		int count;
		switch(uriMatcher.match(uri)) {
			case MESSAGES_ALLROWS:
				count = db.update(TABLE_MESSAGES, values, selection, selectionArgs);
				break;

			case MESSAGES_SINGLE_ROW:
				count = db.update(TABLE_MESSAGES, values, "msg_id = ?", new String[]{uri.getLastPathSegment()});
				break;

			case PROFILE_ALLROWS:
				count = db.update(TABLE_PROFILE, values, selection, selectionArgs);
				break;

			case PROFILE_SINGLE_ROW:
				count = db.update(TABLE_PROFILE, values, "user_id = ?", new String[]{uri.getLastPathSegment()});

				break;

			case GROUP_SINGLE_ROW:
				count = db.update(TABLE_PROFILE, values, "group_id = ?", new String[]{uri.getLastPathSegment()});

				break;

			case GROUP_MESSAGES_ALLROWS:
				count = db.update(TABLE_GROUP_MESSAGES, values, selection, selectionArgs);
				break;

			case GROUP_MESSAGES_SINGLE_ROW:
				count = db.update(TABLE_GROUP_MESSAGES, values, "msg_id = ?", new String[]{uri.getLastPathSegment()});
				break;

			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		int count;
		switch(uriMatcher.match(uri)) {
			case MESSAGES_ALLROWS:
				count = db.delete(TABLE_MESSAGES, selection , selectionArgs);
				break;

			case MESSAGES_SINGLE_ROW:
				count = db.delete(TABLE_MESSAGES, "msg_id = ?", new String[]{uri.getLastPathSegment()});
				break;

			case PROFILE_ALLROWS:
				count = db.delete(TABLE_PROFILE, selection, selectionArgs);
				break;

			case PROFILE_SINGLE_ROW:
				count = db.delete(TABLE_PROFILE, "user_id = ?", new String[]{uri.getLastPathSegment()});
				break;

			case GROUP_SINGLE_ROW:
				count = db.delete(TABLE_PROFILE, "group_id = ?", new String[]{uri.getLastPathSegment()});
				break;

			case GROUP_MESSAGES_ALLROWS:
				count = db.delete(TABLE_GROUP_MESSAGES, selection, selectionArgs);
				break;

			case GROUP_MESSAGES_SINGLE_ROW:
				count = db.delete(TABLE_GROUP_MESSAGES, "msg_id = ?", new String[]{uri.getLastPathSegment()});
				break;


			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}


	@Override
	public String getType(Uri uri) {
		return null;
	}



	public Boolean isChatMsgPresent(Context c){

		dbHelper = new DbHelper(c);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String query="select _id from profile where count != 0";
		Cursor rs=db.rawQuery(query, null);
		if(rs.moveToNext()){

			String id1=rs.getString(rs.getColumnIndex(COL_ID));
			return true;

		}
		if(!rs.isClosed()){
			rs.close();
		}
		return false;
	}



	//--------------------------------------------------------------------------
	
	private static class DbHelper extends SQLiteOpenHelper {
		
		private static final String DATABASE_NAME = "chat.db";
		private static final int DATABASE_VERSION = 3;

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {


			db.execSQL("create table messages (_id integer primary key autoincrement," +
					"msg_id integer unique, " +
					" msg text, user_id text, " +
					" is_view text  default no, is_send text  default no, " +
					" last_seen text, " +
					" is_image text default no, " +
					" is_stream text default no, " +
					" stream_id text , " +
					" stream_link text , " +
					" is_upload text default no, " +
					"friend_id text, at datetime default current_timestamp);");



			db.execSQL("create table profile " +
					"(_id integer primary key autoincrement, " +
					"user_id integer , " +
					"group_id integer , " +   // my user_id (means current app user)
					"name text, " +
					"email text , " +
					"image text, " +
					"is_image text  default no , " +
					"is_stram text  default no, " +
					"social_id text, " +
					"social_type text, " +
					"is_view text default no, " +
					"is_send text default no, " +
					"is_mine text default no, " +
					"is_online text default no, " +
					"last_msg text, " +
					"last_msg_date datetime , " +
					"last_seen_time text, " +
					"is_typing text default no, " +
					"is_group text default no, " +
					"count integer default 0);");




			String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS +
					"("
					+ KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
					+ COL_GROUP_ID + " INTEGER ,"
					+ KEY_TITLE + " TEXT,"
					+ KEY_MEDICINE_NAMES + " TEXT,"
					+ KEY_DOSE_TYPE + " TEXT,"
					+ KEY_DOSE_REPEAT_TYPE + " TEXT,"
					+ KEY_DATE + " TEXT,"
					+ KEY_END_DATE + " TEXT,"
					+ KEY_TIME + " TEXT,"
					+ KEY_REPEAT + " BOOLEAN,"
					+ KEY_REPEAT_NO + " INTEGER,"
					+ KEY_REPEAT_TYPE + " TEXT,"
					+ KEY_ACTIVE + " BOOLEAN" + ")";
			db.execSQL(CREATE_REMINDERS_TABLE);



			String CREATE_EVENT_TABLE = "CREATE TABLE events " +
					"("
					+ KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
					+ "main_id INTEGER ,"
					+ "event_id TEXT "+ ")";
			db.execSQL(CREATE_EVENT_TABLE);






		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			// Drop older table if existed
			if (oldVersion >= newVersion)
				return;
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
			db.execSQL("DROP TABLE IF EXISTS messages ");
			db.execSQL("DROP TABLE IF EXISTS profile ");
			db.execSQL("DROP TABLE IF EXISTS events ");


			// Create tables again
			onCreate(db);

		}
	}

	/**** Remainder *****/

	// Adding new Reminder
	public int addReminder(Reminder reminder, Context c, String user_id, String id){
		dbHelper = new DbHelper(c);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(COL_GROUP_ID ,user_id);
		values.put(KEY_TITLE , reminder.getTitle());
		values.put(KEY_MEDICINE_NAMES , reminder.getmMedicine());
		values.put(KEY_DOSE_TYPE , reminder.getmDoseType());
		values.put(KEY_DOSE_REPEAT_TYPE , reminder.getmDoseRepeatType());
		values.put(KEY_DATE , reminder.getDate());
		values.put(KEY_END_DATE , reminder.getmEndDate());
		values.put(KEY_TIME , reminder.getTime());
		values.put(KEY_REPEAT , false);
		//values.put(KEY_REPEAT_NO , 0);
		values.put(KEY_REPEAT_NO, Integer.parseInt(id));
		values.put(KEY_REPEAT_TYPE, reminder.getRepeatType());
		values.put(KEY_ACTIVE, true);
		// Inserting Row
		long ID = db.insert(TABLE_REMINDERS, null, values);
		db.close();
		return (int) ID;
	}

	// Adding new Reminder
	public int addEvent(Context c, int mainid, long eventid)
	{
		dbHelper = new DbHelper(c);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("main_id" ,mainid);
		values.put("event_id" , String.valueOf(eventid));

		// Inserting Row
		long ID = db.insert("events", null, values);
		db.close();
		return (int) ID;
	}


	// Getting single Reminder
	public Reminder getReminder(int id, Context c){
		dbHelper = new DbHelper(c);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Cursor cursor = db.query(TABLE_REMINDERS, new String[]
						{
								KEY_ID,
								KEY_TITLE,
								KEY_MEDICINE_NAMES,
								KEY_DOSE_TYPE,
								KEY_DOSE_REPEAT_TYPE,
								KEY_DATE,
								KEY_END_DATE,
								KEY_TIME,
								KEY_REPEAT,
								KEY_REPEAT_NO,
								KEY_REPEAT_TYPE,
								KEY_ACTIVE
						}, KEY_ID + "=?",

				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Reminder reminder = new Reminder(
				Integer.parseInt(cursor.getString(0)),
				cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11));

		return reminder;
	}

	// Getting all Reminders
	public List<Reminder> getAllReminders(Context c, String user_id)
	{

		dbHelper = new DbHelper(c);
		List<Reminder> reminderList = new ArrayList<>();

		// Select all Query
		String selectQuery = "SELECT * FROM " + TABLE_REMINDERS +" where "+COL_GROUP_ID+" = "+user_id+" ORDER BY id DESC ";

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				Reminder reminder = new Reminder();
				reminder.setID(Integer.parseInt(cursor.getString(0)));
				reminder.setTitle(cursor.getString(2));
				reminder.setmMedicine(cursor.getString(3));
				reminder.setmDoseType(cursor.getString(4));
				reminder.setmDoseRepeatType(cursor.getString(5));
				reminder.setDate(cursor.getString(6));
				reminder.setmEndDate(cursor.getString(7));
				reminder.setTime(cursor.getString(8));
				reminder.setRepeat(cursor.getString(9));
				reminder.setRepeatNo(cursor.getString(10));
				reminder.setRepeatType(cursor.getString(11));
				reminder.setActive(cursor.getString(12));
				// Adding Reminders to list
				reminderList.add(reminder);
			} while (cursor.moveToNext());
		}
		return reminderList;
	}

	// Getting Reminders Count
	public int getRemindersCount(Context c){
		dbHelper = new DbHelper(c);
		String countQuery = "SELECT * FROM " + TABLE_REMINDERS;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery,null);
		cursor.close();

		return cursor.getCount();
	}

	// Updating single Reminder
	public int updateReminder(Reminder reminder, Context c, String user_id, String id, String rid){

		dbHelper = new DbHelper(c);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(COL_GROUP_ID ,user_id);
		values.put(KEY_TITLE , reminder.getTitle());
		values.put(KEY_MEDICINE_NAMES , reminder.getmMedicine());
		values.put(KEY_DOSE_TYPE , reminder.getmDoseType());
		values.put(KEY_DOSE_REPEAT_TYPE , reminder.getmDoseRepeatType());
		values.put(KEY_DATE , reminder.getDate());
		values.put(KEY_END_DATE , reminder.getmEndDate());
		values.put(KEY_TIME , reminder.getTime());
		values.put(KEY_REPEAT , false);
		//values.put(KEY_REPEAT_NO , 0);
		values.put(KEY_REPEAT_NO, Integer.parseInt(id));
		values.put(KEY_REPEAT_TYPE, reminder.getRepeatType());
		values.put(KEY_ACTIVE, true);

		int i=db.update(TABLE_REMINDERS, values, KEY_ID + "=?",
				new String[]{String.valueOf(rid)});

		db.close();

		return i;
	}

	// Deleting single Reminder
	public void deleteReminder(Reminder reminder, Context c){
		dbHelper = new DbHelper(c);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(TABLE_REMINDERS, KEY_ID + "=?",
				new String[]{String.valueOf(reminder.getID())});
		db.close();
	}


	public boolean isRemainderPresent(Context c, String id){


		dbHelper = new DbHelper(c);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String query="select * from ReminderTable where repeat_no="+id+"";

		Cursor rs=db.rawQuery(query, null);

		if(rs.moveToLast()){

			return true;

		}

		if(!rs.isClosed()){

			rs.close();
		}

		return false;

	}



	// Getting all Reminders
	public List<Events> getEvents(Context c, int mainid){

		dbHelper = new DbHelper(c);
		List<Events> reminderList = new ArrayList<>();

		// Select all Query
		String selectQuery = "SELECT * FROM events where main_id = "+mainid+" ";

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				Events reminder = new Events();
				reminder.setMainId(Integer.parseInt(cursor.getString(0)));
				reminder.setEventId(cursor.getString(2));

				// Adding Reminders to list
				reminderList.add(reminder);
			} while (cursor.moveToNext());
		}


		if(!cursor.isClosed()){

			cursor.close();
		}

		return reminderList;
	}



	// Deleting single Reminder
	public void deleteEvent(String id, Context c){
		dbHelper = new DbHelper(c);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("events", KEY_ID + "=?",
				new String[]{String.valueOf(id)});
		db.close();
	}



}
