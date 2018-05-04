package org.ironworkschurch.eli.churchapp;

/**
 * Created by elija on 4/16/2018.
 */

public class MorningVerse extends Verse {
    private String date;
    private String title;

    public MorningVerse(String date, String title, String txt, String ref) {
        super(ref, txt);
        this.date = date;
        this.title = title;
    }

    public String getDate(){
        return date;
    }

    public String getTitle(){
        return title;
    }
}
