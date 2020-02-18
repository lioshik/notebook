package com.example.notebook;

import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class Homework {
    public String subj;
    public String txt;
    public DateData date;
    public ArrayList<String> photos;

    public Homework(String subj, String txt, DateData date, String[] photoss){
        this.date = date;
        this.txt = txt;
        this.subj = subj;
        this.photos = new ArrayList<String>();
        for (int i = 0; i < photoss.length; i++) {
            photos.add(photoss[i]);
        }
    }

    public String getString(){
        return subj + " " + txt + " " + date.getString();
    }
}
