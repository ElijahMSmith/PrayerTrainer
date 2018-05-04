package org.ironworkschurch.eli.churchapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Eli on 1/18/2018.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String name;
        String description;
        String time = intent.getStringExtra("time");
        switch (time) {
            case "morning":
                name = "Morning: Devote the day to God";
                description = "Commit your day to Him, look to glorify and enjoy Him, lay the activities of the day before Him, enlist His help to honor Him throughout.\n\n\nSelect this notification to view a unique prayer for this day.";
                break;
            case "afternoon":
                name = "Mid-day: Delight yourself in the Lord";
                description = "Pray for others, yourself, and your church.\n\nSelect this notification to view a portion of the Psalms with which to reflect on and respond to God.";
                break;
            default:
                name = "Evening: Debrief your day with God";
                description = "Express your thanks, trusts, and needs.";
                break;
        }

        long[] vibrate = new long[2];
        vibrate[0] = 500;
        vibrate[1] = 1000;

        //LIST OF THINGS TO DO



        //REVIEW ALL TEXT THE USER MIGHT READ AND MAKE SURE IT ALL MAKES SENSE
        //STYLE THINGS TO MAKE IT LOOK NOT AWFUL AND ASK PEOPLE WHAT THEY THINK
        //TALK TO STAN



        Intent notificationIntent = new Intent(context, PromptActivity.class);
        notificationIntent.putExtra("time", time);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 99999999,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                //Need to be able to send back into the app, preferably onto the PromptActivity with enough data to realize what time of day we are referencing.
                .setSmallIcon(R.drawable.ic_ironworkslogo)
                .setLights(Color.BLUE, 500, 2000)
                .setAutoCancel(true)
                .setVibrate(vibrate)
                .setContentTitle(name)
                .setStyle(new NotificationCompat.BigTextStyle()
                        //May need to change this formatting when morning prayers are added, depending on if they are verses or not
                        //Trim removes possible extra linebreak if no reference for the verse
                        .bigText((description)));

        if(!time.equalsIgnoreCase("evening"))
            builder.setContentIntent(pi);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

        if(!intent.getBooleanExtra("reset", false))
            return;

        //Reset the alarm for one day in the future
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent mIntent = new Intent(context, AlarmReceiver.class).putExtra("time", time);
        //Puts the correct identifier for morning, evening, and afternoon.
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, time.equals("morning") ? 11111111 : (time.equals("afternoon") ? 22222222 : 33333333), mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Plus one day
        Calendar c = new GregorianCalendar();
        c.add(Calendar.DATE, 1);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

}
