package org.ironworkschurch.eli.churchapp.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.ironworkschurch.eli.churchapp.R;

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

        Intent notificationIntent = new Intent(context, PromptActivity.class);
        notificationIntent.putExtra("time", time);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, time.equals("morning")? 1 : (time.equals("afternoon") ? 2 : 3),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "org.ironworkschurch.eli.churchapp")
                .setSmallIcon(R.drawable.ic_ironworkslogo)
                .setLights(Color.BLUE, 500, 2000)
                .setAutoCancel(true)
                .setVibrate(vibrate)
                .setContentTitle(name)
                .setContentText(description);

        if(!time.equalsIgnoreCase("evening"))
            builder.setContentIntent(pi);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(manager == null){
            Log.d("test", "manager was null?");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence chanName = "Prayer Trainer";
            String chanDesc = "channelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("org.ironworkschurch.eli.churchapp", chanName, importance);
            channel.setDescription(chanDesc);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }

        manager.notify(time.equals("morning")? 1 : (time.equals("afternoon") ? 2 : 3), builder.build());

        if(!intent.getBooleanExtra("reset", false))
            return;

        //Reset the alarm for one day in the future
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent mIntent = new Intent(context, AlarmReceiver.class).putExtra("time", time);
        //Puts the correct identifier for morning, evening, and afternoon.
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, time.equals("morning") ? 111 : (time.equals("afternoon") ? 222 : 333), mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Plus one day
        Calendar c = new GregorianCalendar();
        c.add(Calendar.DATE, 1);

        if(alarmManager == null){
            Log.d("test", "alarmManager is null?");
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

}
