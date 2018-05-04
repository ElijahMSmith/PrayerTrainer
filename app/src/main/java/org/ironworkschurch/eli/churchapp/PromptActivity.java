package org.ironworkschurch.eli.churchapp;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class PromptActivity extends AppCompatActivity {

    private DBHelper db;
    private String time;
    private TextView dateDisplay;
    private TextView titleDisplay;
    private TextView textDisplay;
    private TextView refDisplay;
    private ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        Typeface robotoslabBold = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Bold.ttf");
        Typeface robotoslab = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Regular.ttf");

        db = new DBHelper(getApplicationContext());
        dateDisplay = findViewById(R.id.prayerDate);
        titleDisplay = findViewById(R.id.prayerTitle);
        textDisplay = findViewById(R.id.textDisplay);
        refDisplay = findViewById(R.id.refDisplay);
        Button refresh = findViewById(R.id.refresh);
        scroll = findViewById(R.id.textScroll);
        time = getIntent().getStringExtra("time");

        dateDisplay.setTypeface(robotoslabBold);
        titleDisplay.setTypeface(robotoslabBold);
        refDisplay.setTypeface(robotoslab);
        textDisplay.setTypeface(robotoslab);

        if(time.equals("morning")) {
            refresh.setVisibility(View.GONE);
        }

        refresh(null);
    }

    @SuppressLint("SetTextI18n")
    public void refresh(View v){
        Verse verse = db.getAVerse(time);
        if(verse == null){
            Toast.makeText(getApplicationContext(), "Cannot retrieve due to a database error", Toast.LENGTH_LONG).show();
            return;
        }

        if(time.equals("afternoon")){
            dateDisplay.setVisibility(View.GONE);
            titleDisplay.setVisibility(View.GONE);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            scroll.setLayoutParams(lp);

            RelativeLayout.LayoutParams reflp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            reflp.setMargins(0, 30, 0, 0);
            reflp.addRule(RelativeLayout.BELOW, R.id.textScroll);
            refDisplay.setLayoutParams(reflp);
        } else {
            dateDisplay.setText(((MorningVerse) verse).getDate());
            titleDisplay.setText(((MorningVerse) verse).getTitle());

            RelativeLayout.LayoutParams scrolllp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            scrolllp.addRule(RelativeLayout.BELOW, R.id.prayerTitle);
            scrolllp.addRule(RelativeLayout.ABOVE, R.id.refDisplay);
            scrolllp.setMargins(0, 15, 0, 10);
            scroll.setLayoutParams(scrolllp);

            RelativeLayout.LayoutParams reflp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            reflp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            refDisplay.setLayoutParams(reflp);
        }
        textDisplay.setText(("        " + verse.getText().trim()).replace("\\n", "\n"));
        refDisplay.setText("\n" + verse.getReference());
    }
}
