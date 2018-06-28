package org.ironworkschurch.eli.churchapp.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ironworkschurch.eli.churchapp.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeSetActivity extends AppCompatActivity {

    private EditText morningField;
    private EditText afternoonField;
    private EditText eveningField;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_set);

        Typeface robotoslabBold = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Bold.ttf");

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        TextView morningTitle = findViewById(R.id.morningTitle);
        TextView afternoonTitle = findViewById(R.id.afternoonTitle);
        TextView eveningTitle = findViewById(R.id.eveningTitle);
        morningField = findViewById(R.id.morningTimeField);
        afternoonField = findViewById(R.id.afternoonTimeField);
        eveningField = findViewById(R.id.eveningTimeField);
        Button update = findViewById(R.id.updateButton);

        morningTitle.setTypeface(robotoslabBold);
        afternoonTitle.setTypeface(robotoslabBold);
        eveningTitle.setTypeface(robotoslabBold);
        morningField.setTypeface(robotoslabBold);
        afternoonField.setTypeface(robotoslabBold);
        eveningField.setTypeface(robotoslabBold);
        update.setTypeface(robotoslabBold);

        //Take, if available, notification times from SharedPreferences and load them into blanks.
        morningField.setText(sp.getString("morningTime", ""));
        afternoonField.setText(sp.getString("afternoonTime", ""));
        eveningField.setText(sp.getString("eveningTime", ""));

    }

    public void updateTimes(View view) {
        //Called by button: Should take times in each blank and save them formatted to SharedPreferences
        String morningTime = morningField.getText().toString().toLowerCase();
        String afternoonTime = afternoonField.getText().toString().toLowerCase();
        String eveningTime = eveningField.getText().toString().toLowerCase();

        if (!validStrings(new String[]{morningTime, afternoonTime, eveningTime})){
            new AlertDialog.Builder(this)
                    .setTitle("It looks like one or more of your times are invalid.")
                    .setMessage("Please make sure each time fits the required format (military time is not allowed) -> HH:MM AM/PM")
                    .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
            return;
        }

        sp.edit().putString("morningTime", morningTime).apply();
        sp.edit().putString("afternoonTime", afternoonTime).apply();
        sp.edit().putString("eveningTime", eveningTime).apply();

        setThreeAlarms(getApplicationContext(), morningTime, afternoonTime, eveningTime);

        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
    }

    public boolean validStrings(String[] s) {
        //If there isn't enough to work with, then it obviously doesn't fit the criterea
        if(s.length != 3 || s[0].equals("") || s[1].equals("") || s[2].equals(""))
            return false;
        for (String st : s) {
            String[] split = st.split(" ");
            if(split.length != 2)
                return false;
            String[] firstSplit = split[0].split(":");
            if(firstSplit.length != 2)
                return false;
            //Checks to make sure that the end of the time has am/pm, and not anything else/nothing
            if(!(split[1].equalsIgnoreCase("pm") || split[1].equalsIgnoreCase("am")))
                return false;
            //Checks if something is an inappropriate length
            if(!(firstSplit[0].length() <= 2 && firstSplit[0].length() > 0 && firstSplit[1].length() == 2))
                return false;
            //If there is a non-number in the time part
            if(!(("" + firstSplit[0] + firstSplit[1]).matches("[0-9]+")))
                return false;
            //If one of the values doesn't make sense
            if(!(Integer.parseInt(firstSplit[0]) <= 12 && Integer.parseInt(firstSplit[0]) >= 0 && Integer.parseInt(firstSplit[1]) < 60 && Integer.parseInt(firstSplit[1]) >= 0))
                return false;
            //If the hour is 0, obviously something is amiss
            if(Integer.parseInt(firstSplit[0]) == 0)
                return false;
        }
        return true;
    }

    protected static void setThreeAlarms(Context context, String morningTime, String afternoonTime, String eveningTime){
        //Set the alarms with new details, they should override the other alarms that were set

        //On the off chance that the bootup receiver doesn't find the correct values, nothing is going to happen and nothing breaks.
        //The only time this might happen is if there is nothing yet in the sharedpreferences, which means they haven't used the app yet, and as such we don't want to offend them.
        if(morningTime.equals("") || afternoonTime.equals("") || eveningTime.equals(""))
            return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        Intent mIntent = new Intent(context, AlarmReceiver.class).putExtra("time", "morning").putExtra("reset", true);
        Intent aIntent = new Intent(context, AlarmReceiver.class).putExtra("time", "afternoon").putExtra("reset", true);
        Intent eIntent = new Intent(context, AlarmReceiver.class).putExtra("time", "evening").putExtra("reset", true);

        PendingIntent mPendingIntent = PendingIntent.getBroadcast(context, 11111111, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent aPendingIntent = PendingIntent.getBroadcast(context, 22222222, aIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent ePendingIntent = PendingIntent.getBroadcast(context, 33333333, eIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar morningCalendar = new GregorianCalendar();
        Calendar afternoonCalendar = new GregorianCalendar();
        Calendar eveningCalendar = new GregorianCalendar();

        String[] morningVals = morningTime.split(" ")[0].split(":");
        String[] afternoonVals = afternoonTime.split(" ")[0].split(":");
        String[] eveningVals = eveningTime.split(" ")[0].split(":");

        morningCalendar.set(Calendar.AM_PM, morningTime.contains("am") ? Calendar.AM : Calendar.PM);
        morningCalendar.set(Calendar.HOUR, Integer.parseInt(morningVals[0]));
        morningCalendar.set(Calendar.MINUTE, Integer.parseInt(morningVals[1]));
        morningCalendar.set(Calendar.SECOND, 0);

        afternoonCalendar.set(Calendar.AM_PM, afternoonTime.contains("am") ? Calendar.AM : Calendar.PM);
        afternoonCalendar.set(Calendar.HOUR, Integer.parseInt(afternoonVals[0]));
        afternoonCalendar.set(Calendar.MINUTE, Integer.parseInt(afternoonVals[1]));
        afternoonCalendar.set(Calendar.SECOND, 0);

        eveningCalendar.set(Calendar.AM_PM, eveningTime.contains("am") ? Calendar.AM : Calendar.PM);
        eveningCalendar.set(Calendar.HOUR, Integer.parseInt(eveningVals[0]));
        eveningCalendar.set(Calendar.MINUTE, Integer.parseInt(eveningVals[1]));
        eveningCalendar.set(Calendar.SECOND, 0);

        //Some fancy math for if the time has already passed that day, in which case it schedules for the next
        long morningMillis = morningCalendar.getTimeInMillis() + (System.currentTimeMillis() >= morningCalendar.getTimeInMillis() ? 1000 * 60 * 60 * 24 : 0);
        long afternoonMillis = afternoonCalendar.getTimeInMillis() + (System.currentTimeMillis() >= afternoonCalendar.getTimeInMillis() ? 1000 * 60 * 60 * 24 : 0);
        long eveningMillis = eveningCalendar.getTimeInMillis() + (System.currentTimeMillis() >= eveningCalendar.getTimeInMillis() ? 1000 * 60 * 60 * 24 : 0);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //If it already passed, set it one day in advance from that time
            alarmManager.set(AlarmManager.RTC_WAKEUP, morningMillis, mPendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, afternoonMillis, aPendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, eveningMillis, ePendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, morningMillis, mPendingIntent);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, afternoonMillis, aPendingIntent);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, eveningMillis, ePendingIntent);
        }
    }
}
