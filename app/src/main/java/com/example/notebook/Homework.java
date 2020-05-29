package com.example.notebook;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.Arrays.asList;

public class Homework {
        private final static String serverUrl = "http://localhost:4567/";
        @SerializedName("subj")
        @Expose
        public String subj;
        @SerializedName("txt")
        @Expose
        public String txt;
        @SerializedName("date")
        @Expose
    public DateData date;
    @SerializedName("photos")
    @Expose
    public List<String> photos;

    public Homework(String subj, String txt, DateData date, String[] photoss){
        this.date = date;
        this.txt = txt;
        this.subj = subj;
        this.photos = new ArrayList<String>();
        for (int i = 0; i < photoss.length; i++) {
            photos.add(photoss[i]);
        }
    }

    public void send() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public String getString(){
        return subj + " " + txt + " " + date.getString();
    }
}
