package com.example.letsmeet;

import java.util.ArrayList;
import java.util.Collections;

public class MyDate {
    int year;
    int month;
    int dayOfMonth;
    int dayOfWeek;
    ArrayList<Integer> timingsOfTheDay;


    public MyDate(int year, int month, int dayOfMonth, int dayOfWeek) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.timingsOfTheDay = new ArrayList<>();
        Collections.addAll(timingsOfTheDay, 6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24);
    }

    public void removeTime(int start, int end) {
        int i = 0;
        while(i < timingsOfTheDay.size()) {
            if(timingsOfTheDay.get(i) >= start && timingsOfTheDay.get(i) < end) {
                timingsOfTheDay.remove(i);
            } else {
                i++;
            }
        }
    }

    public String printTime() {
        String temp = "";
        for(Integer i : timingsOfTheDay) {
            if(i == 6 || i == 7 || i == 8) {
                temp += ("0" + i + "00 - 0" + (i + 1) + "00" + "\n");
            } else if (i == 9) {
                temp += ("0" + i + "00 - " + (i + 1) + "00" + "\n");
            } else if(i == 23) {
                temp += ("" + i + "00 - " + "0000" + "\n");
            } else if(i == 24) {
                continue;
            }else {
                temp += ("" + i + "00 - " + (i + 1) + "00" + "\n");
            }
        }
        return temp;
    }

    public String printDay() {
        String whichDay = "";
        switch (dayOfWeek) {
            case 1:
                whichDay = "Sunday";
                break;
            case 2:
                whichDay = "Monday";
                break;
            case 3:
                whichDay = "Tuesday";
                break;
            case 4:
                whichDay = "Wednesday";
                break;
            case 5:
                whichDay = "Thursday";
                break;
            case 6:
                whichDay = "Friday";
                break;
            case 7:
                whichDay = "Saturday";
                break;
            default:
        }
        return "" + dayOfMonth + "/" + month + "/" + year + " " + whichDay;

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MyDate) {
            MyDate other = (MyDate) obj;
            return year == other.year && month == other.month && dayOfMonth == other.dayOfMonth && dayOfWeek == other.dayOfWeek;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String whichDay = "";
        switch (dayOfWeek) {
            case 1:
                whichDay = "Sunday";
                break;
            case 2:
                whichDay = "Monday";
                break;
            case 3:
                whichDay = "Tuesday";
                break;
            case 4:
                whichDay = "Wednesday";
                break;
            case 5:
                whichDay = "Thursday";
                break;
            case 6:
                whichDay = "Friday";
                break;
            case 7:
                whichDay = "Saturday";
                break;
            default:
        }
        return "" + dayOfMonth + "/" + month + "/" + year + " " + whichDay;

    }



}
