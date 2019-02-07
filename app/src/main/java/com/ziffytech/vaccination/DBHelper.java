

package com.ziffytech.vaccination;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ziffytech.vaccination.models.VaccineDataDB;

import java.util.ArrayList;


/*  Class Name : DBHelper
     Purpose:All methods related to database are written in this class
*/
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_vaccination.db";
    private static final int DATABASE_VERSION = 1;
    public static final String VACCINE_TABLE_NAME = "vaccination";

    //USER Table
    public static final String VACCINE_ID = "VaccineId";
    public static final String VACCINE_DATA = "VaccineData";




    public static final String TableVaccine = "create table " + VACCINE_TABLE_NAME +
            "(" + VACCINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + VACCINE_DATA + " TEXT)";

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.e("tbl user", "" + TableVaccine);
            db.execSQL(TableVaccine);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        Log.e("oldVersion", "" + oldVersion);
        Log.e("newVersion", "" + newVersion);
    }

    /* Function Name :func insertUser()
              Purpose:To insert data into User table
            Parameter: User
             return :long
                           */
    public Long insertVaccineData(String vaccineData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VACCINE_DATA, vaccineData);

        Long id = db.insert(VACCINE_TABLE_NAME, null, contentValues);
        Log.e("id...insert", "" + id);
        return id;
    }








 /* Function Name :func IsUserExist()
               Purpose:To check user exist or not using the email
               Parameter: userEmail
                return :  int
            */


    public int IsVaccineDataExist(String vaccineID) {

        ArrayList<VaccineDataDB> userArrayList = new ArrayList<VaccineDataDB>();
        if (numberOfRows() != 0) {
            userArrayList = getAllVaccineDetails();
            String checkInUserTable;
            Boolean flag = false;

            String vaccineid;
            Cursor cursor = null;
            for (int i = 0; i < userArrayList.size(); i++) {
                cursor = null;

                vaccineid = vaccineID;

                SQLiteDatabase db = this.getReadableDatabase();

                checkInUserTable = ("Select * from " + VACCINE_TABLE_NAME + " where " + VACCINE_ID + " = \"" + vaccineid + "\"");
                Log.e("id...checkInUserTable", "" + checkInUserTable);
                cursor = db.rawQuery(checkInUserTable, null);

                if (cursor.getCount() <= 0) {

                    flag = false;
                } else {
                    flag = true;
                    break;

                }

            }
            if (flag == true) {
                cursor.moveToFirst();
                return cursor.getCount();
            } else
                return 0;

        } else
            return 0;

    }







/* Function Name :func numberOfRows()
                   Purpose:To get number of user
                   Parameter: none
                    return :int
                */

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, VACCINE_TABLE_NAME);
        return numRows;
    }


    /* Function Name :func getAllUser()
                  Purpose:To get all User information
                  Parameter: none
                   return :ArrayList
               */
    public ArrayList<VaccineDataDB> getAllVaccineDetails() {
        ArrayList<VaccineDataDB> vaccineList = new ArrayList<VaccineDataDB>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + VACCINE_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                VaccineDataDB vaccineDataDB = new VaccineDataDB();

                vaccineDataDB.setVaccineId(cursor.getInt(0));
                vaccineDataDB.setVaccineData(cursor.getString(1));

                Log.e("setid...", "" + cursor.getInt(0));
                Log.e("setUserEmail...", "" + cursor.getString(1));

                vaccineList.add(vaccineDataDB);
            } while (cursor.moveToNext());
        }


        return vaccineList;
    }


    /* Function Name :func getUserDetails()
                  Purpose:To get  User information
                  Parameter: Userid
                   return :User
               */
    public VaccineDataDB getVaccineDetails(String id) {
        String selectQuery = ("Select * from " + VACCINE_TABLE_NAME + " where " + VACCINE_ID + " = \"" + id + "\"");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        VaccineDataDB vaccineDataDB = null;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            vaccineDataDB = new VaccineDataDB();
            vaccineDataDB.setVaccineId(cursor.getInt(0));
            vaccineDataDB.setVaccineData(cursor.getString(1));

            Log.e("setid...", "" + cursor.getInt(0));
            Log.e("setUserEmail...", "" + cursor.getString(1));

        }

        return vaccineDataDB;
    }




     /* Function Name :func updateUser()
               Purpose:To update user name  and  using user id
               Parameter: id,name
                return : void
            */


    public void updateVaccineData(String id, String vData) {
        Log.e("id update", "" + id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues data = new ContentValues();
        data.put(VACCINE_DATA, vData);
        int iid = db.update(VACCINE_TABLE_NAME, data, VACCINE_ID + "=" + id, null);
        Log.e("id", "" + iid);
        db.close();
    }


}