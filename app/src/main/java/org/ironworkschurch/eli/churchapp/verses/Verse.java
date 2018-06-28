package org.ironworkschurch.eli.churchapp.verses;

/**
 * Created by elija on 4/16/2018.
 */

public abstract class Verse {

    private String reference;
    private String text;

    public Verse(String ref, String txt){
        reference = ref;
        text = txt;
    }

    public String getReference(){
        return reference;
    }

    public String getText() {
        return text;
    }


}