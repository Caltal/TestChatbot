package com.example.asg02.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static String convertMinsToStringTime (int durationMins) {
        int hours = durationMins / 60;
        int mins = durationMins % 60;
        String hourString = (hours == 0) ? "" : hours + " giờ ";
        String minuteString = (mins == 0) ? "" : mins + " phút";
        return hourString + minuteString;
    }

    public static LocalDateTime now = LocalDateTime.now();

    public static LocalDateTime transferStringToDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public static boolean isAfterNow(String dateTime) {
        LocalDateTime date = transferStringToDateTime(dateTime);
        return date.isAfter(now);
    }

    public static boolean isAfterToday(String releaseDate) {
        if (releaseDate == null || releaseDate.isEmpty()) {
            return true;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(releaseDate, formatter);

        return date.isAfter(now.toLocalDate());
    }

    public static String getToday() {
        return now.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static boolean isExpiredTime(String time) {
        if (time == null || time.isEmpty()) {
            return true;
        }
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        return localTime.isAfter(now.toLocalTime());
    }

    public static String convertTimestampToDate(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);

        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return dtf.format(zdt);
    }
}
