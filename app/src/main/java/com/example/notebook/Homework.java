package com.example.notebook;

import java.text.DateFormat;

public class Homework {
    public String subj;
    public String txt;
    public DateData date;

    public Homework(String subj, String txt, DateData date){
        this.date = date;
        this.txt = txt;
        this.subj = subj;
    }

    public String getString(){
        return subj + " " + txt + " " + date.getString();
    }
}
