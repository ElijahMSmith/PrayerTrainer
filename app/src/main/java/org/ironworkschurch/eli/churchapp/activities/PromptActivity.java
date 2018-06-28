package org.ironworkschurch.eli.churchapp.activities;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.ironworkschurch.eli.churchapp.utilities.DBHelper;
import org.ironworkschurch.eli.churchapp.verses.MorningVerse;
import org.ironworkschurch.eli.churchapp.R;
import org.ironworkschurch.eli.churchapp.verses.Verse;

public class PromptActivity extends AppCompatActivity {

    private DBHelper db;
    private String time;
    private TextView dateDisplay;
    private TextView titleDisplay;
    private TextView textDisplay;
    private TextView refDisplay;
    private ScrollView scroll;

    /*

    Planning for Journaling

    - Separate activity for journal which, on entering, keeps a list (reddit like feed) of journal entries.
        - Keep in a internal database with the passage and all the extra details (depending on morning or afternoon)
        - Additionally have a comment section (optional) for if this was meaningful to you.
        - Eventually, directly from this page, you should be able to share this to a church, but that feature comes later.

    - Needs a button on the prompt activity (that would need to be put into each layout specifically to fit)
    - Button press should bring up an option that lets you leave a comment about the passage (if you want)
    - Submit takes you to the journal activity, where your new entry has been saved.



    !!!!!!!! All of the activities need to have a back button that leads to the homepage.
     */


    /*

    Planning for Sharing

    - I would really love to use PubNub for this, but I don't know it will work unless saving is persistent, rather than 7 days.
    - Might instead need to do it in a database somehow. Maybe a relational database?
        - I can imagine a SQL database being sufficient, but not good.
        - Iron Works has a server that could probably hold the database. I could also use dad's for a while if need be.
            - Definitely ask Dad what is the better option and how to do it.

    - Button on the side of the journal entry that lets you share.
    - Brings up a dialog that lets you search for a church (suggestion drop down)
    - Needs to have some way of looking at all registered Churches, and if the input doesn't match, register a new one.
        - Churches should probably have a full name (that ends in church), denomination (?), and a city/state/country.
        - All registered churches probably need to be sent to me to make sure they are real churches/correctly formatted.

    - Will again probably need a separate activity that can take all the data from the input church and display the most x recents posts.
    - After a while, if a post is too old or won't be shown on the feed, delete it from the database for storage purposes.
        - People will probably want an "older posts" feature, but I think I can probably just put them all in the display
                and have them scroll up.

    - Having sharing straight from journal might be fine, but also some people might want to say two different things.
        - Option to share straight from the prayer too.
            - What if they want to do both?
                - Probably have journaling not take them directly to activity, rather ask if they would like to go off.
                    - That way they can say no, return to passage, then share to Church.

    - Also going to need to make sure that people can't just jump onto any old church and post something, prevent spam.
        - Each church might need a password.

    - The other option is to have every church who would like to be registered email me to be added to the database with name/pass

     */

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
