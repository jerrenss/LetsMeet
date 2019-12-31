package com.example.letsmeet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Organiser {
    String[] startAndEnd;
    ArrayList<MyDate> allDates = new ArrayList<>();

    public Organiser(String[] startAndEnd) {
        this.startAndEnd = startAndEnd;
        String[] start = startAndEnd[0].split("/");
        String[] end = startAndEnd[1].split("/");
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(start[2]), Integer.parseInt(start[1]) - 1, Integer.parseInt(start[0]), 0,0,0);
        Date dStart = cal.getTime();
        cal.set(Integer.parseInt(end[2]), Integer.parseInt(end[1]) - 1, Integer.parseInt(end[0]), 0,0,0);
        cal.add(Calendar.DATE, 1);
        Date dEnd = cal.getTime();
        cal.setTime(dStart);
        while(cal.getTime().before(dEnd)) {
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DATE, 1);
            allDates.add(new MyDate(year, month, dayOfMonth, dayOfWeek));
        }

    }




}

