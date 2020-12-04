package com.sanju.constraintlayoutpracticessk;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Test {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) {

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);

        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);



        int day = localDate.getDayOfMonth();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();

        System.out.println("day :"+day);
        System.out.println("month :"+month);
        System.out.println("year :"+year);


        LocalTime localTime = LocalTime.now();

        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        int second = localTime.getSecond();

        System.out.println("HOUR :"+hour);
        System.out.println("Minute :"+minute);
        System.out.println("Second :"+second);
    }
}
