package com.example.notebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateData {
    @SerializedName("year")
    @Expose
    public int year;
    @SerializedName("month")
    @Expose
    public int month;
    @SerializedName("day")
    @Expose
    public int day;

    public DateData(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public boolean equals(DateData d2){
        return year == d2.year && month == d2.month && day == d2.day;
    }

    public String getString() {
        return Integer.toString(year) + "." + Integer.toString(month + 1) + "." + Integer.toString(day);
    }

    public boolean notGreater(DateData d){
        if (d.year > this.year) {
            return false;
        } else if (d.year == this.year) {
            if (d.month > this.month) {
                return false;
            } else if (d.month == this.month) {
                if (d.day > this.day) {
                    return false;
                }
            }
        }
        return true;
    }

}
