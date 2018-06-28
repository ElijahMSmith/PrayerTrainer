package org.ironworkschurch.eli.churchapp.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.ironworkschurch.eli.churchapp.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Typeface robotoslabBold = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Bold.ttf");
        Typeface robotoslabReg = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Regular.ttf");

        ((TextView)findViewById(R.id.howToUseTitle)).setTypeface(robotoslabBold);
        ((TextView)findViewById(R.id.howToParagraph)).setTypeface(robotoslabReg);

    }
}
