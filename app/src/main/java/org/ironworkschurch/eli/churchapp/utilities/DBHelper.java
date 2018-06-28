package org.ironworkschurch.eli.churchapp;

/**
 * Created by elija on 4/16/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.ironworkschurch.eli.churchapp.verses.AfternoonVerse;
import org.ironworkschurch.eli.churchapp.verses.MorningVerse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    //MAKE SURE TO UPDATE THIS IF I EVER PUBLISH AN UPDATE WITH NEW TABLE VALUES
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "prayerprompt.db";

    private static final String M_TABLE_NAME = "morning";
    private static final String A_TABLE_NAME = "afternoon";

    private static final String DATE_TEXT = "datetext";
    private static final String TITLE_TEXT = "prayertitle";
    private static final String M_TEXT = "prayertxt";
    private static final String M_REFERENCE = "prayerreferences";

    private static final String ID = "id";
    private static final String A_REFERENCE = "reference";
    private static final String A_TEXT = "msgtext";

    private Context context;

    public DBHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        context = c;
    }

    //Only called when accessed but not yet created, so only the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {

        //THIS IS GOING TO NEED TO CHANGE
        String queryM = "CREATE TABLE " + M_TABLE_NAME + "("
                + DATE_TEXT + " TEXT NOT NULL, "
                + TITLE_TEXT + " TEXT NOT NULL, "
                + M_TEXT + " TEXT NOT NULL, "
                + M_REFERENCE + " TEXT NOT NULL"
                + ");";
        db.execSQL(queryM);

        String queryA = "CREATE TABLE " + A_TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY, "
                + A_TEXT + " TEXT NOT NULL, "
                + A_REFERENCE + " TEXT NOT NULL"
                + ");";
        db.execSQL(queryA);
        setUpTables(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + A_TABLE_NAME + "," + M_TABLE_NAME + ";");
        onCreate(db);
    }

    public Verse getAVerse(String tableTime){
        //morning or afternoon
        //NEED TO GO AROUND AND DELETE ANYTHING THAT TREATS AFTERNOON AND MORNING THE SAME AND FIX IT FOR THE TWO WAYS

        SQLiteDatabase db = getReadableDatabase();
        if(tableTime.equals("afternoon")){
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            int nextID = sp.getInt("A_ID", 1);
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableTime + " WHERE id >= '" + nextID + "'", null);
            if(cursor.getCount() == 0){
                //We have gone too far in database, there is no next listing. Reset to the front.
                cursor = db.rawQuery("SELECT * FROM " + tableTime + " WHERE id >= '" + 1 + "'", null);
                if(cursor.getCount() == 0) {
                    //Nothing in the database, return nothing. Need to check for null wherever this goes in PromptActivity
                    return null;
                }
            }
            //All of these have index of -1 for cursor
            cursor.moveToNext();
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            String reference = cursor.getString(cursor.getColumnIndex(A_REFERENCE));
            String text = cursor.getString(cursor.getColumnIndex(A_TEXT));
            cursor.close();
            sp.edit().putInt("A_ID", id + 1).apply();
            return new AfternoonVerse(reference, text);
        } else {
            String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            Calendar today = Calendar.getInstance();
            String day = months[today.get(Calendar.MONTH)] + " " + Integer.parseInt(new Date().toString().split(" ")[2]);
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableTime + " WHERE " + DATE_TEXT + " = '" + day + "'", null);
            if(cursor.getCount() == 0){
                Toast.makeText(context, "We couldn't find a prayer for today in the database", Toast.LENGTH_LONG).show();
                return null;
            }
            cursor.moveToNext();
            String date = cursor.getString(cursor.getColumnIndex(DATE_TEXT));
            String title = cursor.getString(cursor.getColumnIndex(TITLE_TEXT));
            String prayertext = cursor.getString(cursor.getColumnIndex(M_TEXT));
            String prayerreference = cursor.getString(cursor.getColumnIndex(M_REFERENCE));
            cursor.close();
            return new MorningVerse(date, title, prayertext, prayerreference);
        }
    }

    public void setUpTables(SQLiteDatabase db){

        AssetManager am = context.getAssets();
        try {
            InputStream isa = am.open("afternoonFunctions.txt");
            InputStream ism = am.open("morningFunctions.txt");

            BufferedReader readerm = new BufferedReader(new InputStreamReader(ism));
            BufferedReader readera = new BufferedReader(new InputStreamReader(isa));
            String linea = readera.readLine();
            while (linea != null) {
                try {
                    db.execSQL(linea);
                } catch (SQLiteException e){
                    //If the system doesn't work, the databases won't fill and we will figure out something is wrong.
                    Log.d("test", "SQL LOADING FAILURE - AFTERNOON");
                    return;
                }
                linea = readera.readLine();
            }
            readera.close();
            String linem = readerm.readLine();
            while (linem != null) {
                try {
                    db.execSQL(linem);
                } catch (SQLiteException e){
                    Log.d("test", "SQL LOADING FAILURE - MORNING");
                    return;
                }
                linem = readerm.readLine();
            }
            readerm.close();
        } catch (IOException e) {
            Log.d("test", "FILE LOADING FAILURE");
            e.printStackTrace();
            //The system doesn't work, so we aren't going to fill the database so that we can figure it out later.
        }
    }
}