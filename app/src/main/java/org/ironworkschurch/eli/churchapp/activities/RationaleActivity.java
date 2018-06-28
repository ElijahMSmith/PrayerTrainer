package org.ironworkschurch.eli.churchapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class RationaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rationale);
        Typeface robotoslabBold = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Bold.ttf");
        Typeface robotoslabReg = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Regular.ttf");

        ((TextView)findViewById(R.id.rationaleTitle)).setTypeface(robotoslabBold);
        ((TextView)findViewById(R.id.rationale1)).setTypeface(robotoslabReg);
        ((TextView)findViewById(R.id.rationale2)).setTypeface(robotoslabReg);
        ((TextView)findViewById(R.id.rationale3)).setTypeface(robotoslabReg);
        ((TextView)findViewById(R.id.rationale4)).setTypeface(robotoslabReg);
        ((TextView)findViewById(R.id.rationale5)).setTypeface(robotoslabReg);
    }
}