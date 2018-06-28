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

import com.pubnub.api.PubNub;

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
        ((TextView) findViewById(R.id.feedbackText)).setTypeface(robotoslabReg);
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

    public void openFeedback(View v){
        //It would be kind of cool to have a form that sends me a message with their desired text, title, their name, and email if they want a response.

        //If internet, open dialog to get details (name, email if they want a response, message, include date and time automatically)
                //When they press OK, send a message to PubNub.
                //Going to need another app that can just be on my phone that checks every hour for a new PubNub message, downloads it if there is and shows it to me by notification.
                        //Make sure there is a log as well so that if I miss one, it will show when I enter the app
        //If no internet, tell them in dialog that because they aren't connected, they can't send me a direct message right now.
                //Give them my email and tell them they can talk to me there


    /*

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    */

        new AlertDialog.Builder(this)
                .setTitle("For What Time Of Daytime?")
                .setMessage("If you have found an issue with this app, would like to report your experience, or want to suggest something to add to this app, please email the developer at: wondrouspiffle@gmail.com")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
