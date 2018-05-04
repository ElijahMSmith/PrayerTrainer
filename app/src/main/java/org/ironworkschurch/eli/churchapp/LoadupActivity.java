package org.ironworkschurch.eli.churchapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoadupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadup);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.getString("morningTime", "").equals("")){
            //Never entered the app before
            sp.edit().putString("morningTime", "07:00 AM").apply();
            sp.edit().putString("afternoonTime", "12:00 PM").apply();
            sp.edit().putString("eveningTime", "07:00 PM").apply();
        }

        Typeface robotoslabBold = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Bold.ttf");
        Typeface robotoslabReg = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Regular.ttf");
        ((TextView) findViewById(R.id.ppTitle)).setTypeface(robotoslabBold);
        ((Button) findViewById(R.id.rationaleButton)).setTypeface(robotoslabReg);
        ((Button) findViewById(R.id.howToUseButton)).setTypeface(robotoslabReg);
        ((Button) findViewById(R.id.setNotificationsButton)).setTypeface(robotoslabReg);
        ((Button) findViewById(R.id.scriptureForReflectionButton)).setTypeface(robotoslabReg);
        ((Button) findViewById(R.id.aboutButton)).setTypeface(robotoslabReg);
    }

    public void goToNotifications(View view) {
        Intent intent = new Intent(getApplicationContext(), TimeSetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    public void goToRationale(View view) {
        Intent intent = new Intent(getApplicationContext(), RationaleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    public void goToAboutApp(View view) {
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    public void goToScripture(View view) {
        new AlertDialog.Builder(this)
                .setTitle("For What Time Of Daytime?")
                .setPositiveButton(R.string.morningOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String time = "morning";

                        Intent intent = new Intent(getApplicationContext(), PromptActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("time", time);
                        getApplicationContext().startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.middayOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String time = "afternoon";

                        Intent intent = new Intent(getApplicationContext(), PromptActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("time", time);
                        getApplicationContext().startActivity(intent);
                    }
                })
                .show();
    }

    public void goToCredits(View view) {
        Intent intent = new Intent(getApplicationContext(), CreditsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
}
