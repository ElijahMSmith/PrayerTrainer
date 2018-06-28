package org.ironworkschurch.eli.churchapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import org.ironworkschurch.eli.churchapp.R;
import org.ironworkschurch.eli.churchapp.utilities.Constants;

import java.util.Date;

public class LoadupActivity extends AppCompatActivity {

    private Typeface robotoslabReg;
    private PubNub connection;

    @Override
    protected void onStop() {
        super.onStop();
        if(connection != null)
            connection.destroy();
    }

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
        robotoslabReg = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Regular.ttf");
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
        if(isNetworkAvailable()){
            PNConfiguration config = new PNConfiguration();
            config.setPublishKey(Constants.publishKey);
            config.setSubscribeKey(Constants.subscribeKey);
            connection = new PubNub(config);
            final Context context = getApplicationContext();

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            final TextView nameTitle = new TextView(context);
            nameTitle.setTypeface(robotoslabReg);
            nameTitle.setText(R.string.yourname);
            nameTitle.setGravity(Gravity.CENTER);
            layout.addView(nameTitle);

            final EditText name = new EditText(context);
            name.setHint("Optional");
            name.setGravity(Gravity.CENTER);
            name.setTypeface(robotoslabReg);
            name.setMaxLines(2);
            name.setInputType(InputType.TYPE_CLASS_TEXT);
            layout.addView(name);

            final TextView emailTitle = new TextView(context);
            emailTitle.setTypeface(robotoslabReg);
            emailTitle.setText(R.string.youremail);
            emailTitle.setGravity(Gravity.CENTER);
            layout.addView(emailTitle);

            final EditText email = new EditText(context);
            email.setHint("Optional");
            email.setGravity(Gravity.CENTER);
            email.setTypeface(robotoslabReg);
            email.setMaxLines(2);
            email.setInputType(InputType.TYPE_CLASS_TEXT);
            layout.addView(email);

            final TextView messageTitle = new TextView(context);
            messageTitle.setTypeface(robotoslabReg);
            messageTitle.setText(R.string.yourmessage);
            messageTitle.setGravity(Gravity.CENTER);
            layout.addView(messageTitle);

            final EditText message = new EditText(context);
            message.setHint("Required");
            message.setGravity(Gravity.CENTER);
            message.setTypeface(robotoslabReg);
            message.setMaxLines(5);
            message.setScroller(new Scroller(this));
            message.setVerticalScrollBarEnabled(true);
            message.setMovementMethod(new ScrollingMovementMethod());
            layout.addView(message);

            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Send Feedback")
                    .setView(layout)
                    .setMessage("Use this form to send feedback directly to the developer (criticism, issues, questions, reports, feature requests, etc.)\n")
                    .setPositiveButton("OK", null)
                    .setNegativeButton("Cancel", null)
                    .create();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Button ybutton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    ybutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(message.getText().toString().equals("")){
                                Toast.makeText(getApplicationContext(), "The message field is empty!", Toast.LENGTH_LONG).show();
                            } else if(isNetworkAvailable()){
                                Date calendar = new Date();
                                String mess = System.currentTimeMillis() + ";;;"
                                        + "Name: " + name.getText().toString().trim() + ";;;"
                                        + "Email: " + email.getText().toString().trim() + ";;;"
                                        + "Message: " + message.getText().toString().trim() + ";;;"
                                        + "Time Stamp: " + calendar.toString().trim();
                                connection.publish().channel("<<<<feedback>>>>").message(mess).async(new PNCallback<PNPublishResult>() {
                                    @Override
                                    public void onResponse(PNPublishResult pnPublishResult, PNStatus pnStatus) {
                                        if(!pnStatus.isError())
                                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                            } else {
                                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    Button cbutton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    cbutton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            //Dismiss once everything is OK.
                            dialog.cancel();
                        }
                    });
                }
            });
            dialog.show();
        } else {
            new AlertDialog.Builder(this)
                .setTitle("You Aren't Connected to the Internet")
                .setMessage("Because you don't have an internet connection, feedback directly through this app cannot be sent right now. \n\nPlease either find an internet connection or email your feedback later directly to elijah.matthew.smith@gmail.com")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null)
            return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
