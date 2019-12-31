package com.example.letsmeet;

public class Unavailable {
    int dayOfMonth;
    int month;
    int year;
    int startTime;
    int endTime;
    boolean hasTiming;

    public Unavailable(int dayOfMonth, int month, int year, int startTime, int endTime) {
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hasTiming = true;
    }
    public Unavailable(int dayOfMonth, int month, int year) {
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
        this.hasTiming = false;
    }

    public boolean same(MyDate md) {
        if(dayOfMonth == md.dayOfMonth && month == md.month && year == md.year) {
            return true;
        } else {
            return false;
        }
    }

}
