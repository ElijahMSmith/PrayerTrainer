package org.ironworkschurch.eli.churchapp.activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.ironworkschurch.eli.churchapp.R;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        Typeface robotoslabReg = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Regular.ttf");
        Typeface robotoslabBold = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Bold.ttf");

        ((TextView)findViewById(R.id.aboutApp)).setTypeface(robotoslabBold);
        ((TextView)findViewById(R.id.creditsTextView)).setTypeface(robotoslabReg);
    }
}
