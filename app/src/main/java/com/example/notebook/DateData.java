package com.example.notebook;

public class DateData {
    public int year, month, day;

    public DateData(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
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
