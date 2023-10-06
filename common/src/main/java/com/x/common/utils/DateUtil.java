package com.x.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.*;
import java.util.*;
import java.io.*;

public class DateUtil {
    
    public static int currentTimestamp() {
        return (int)(System.currentTimeMillis() / 1000);
    }

    public static int getTick15m(int timestamp) {
        return timestamp / (15 * 60); 
    }

    public static int fromTick15m(int tick15) {
        return tick15 * 15 * 60;
    }

    public static int getTickHour(int timestamp) {
        return timestamp / (60 * 60); 
    }

    public static int fromTickHour(int tickHour) {
        return tickHour * 60 * 60;
    }

    public static int getTickDay(int timestamp) {
        return timestamp / (24 * 60 * 60); 
    }

    public static int fromTickDay(int tickDay) {
        return tickDay * 24 * 60 * 60;
    }

    public static Date getUTCDate() {
        TimeZone zone = TimeZone.getTimeZone("GMT-0:00");
        Calendar cal = Calendar.getInstance(zone);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getYesterdayUTCDate() {
        TimeZone zone = TimeZone.getTimeZone("GMT-0:00");
        Calendar cal = Calendar.getInstance(zone);
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date fromString(String dateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            TimeZone zone = TimeZone.getTimeZone("GMT-0:00");
            simpleDateFormat.setTimeZone(zone);
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static long getBeginTime(Date date) {
        TimeZone zone = TimeZone.getTimeZone("GMT-0:00");
        Calendar cal = Calendar.getInstance(zone);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime() / 1000;
    }

    public static long getEndTime(Date date) {
        TimeZone zone = TimeZone.getTimeZone("GMT-0:00");
        Calendar cal = Calendar.getInstance(zone);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime().getTime() / 1000;
    }

    public static Date getYesterdayDate(Date date) {
        TimeZone zone = TimeZone.getTimeZone("GMT-0:00");
        Calendar cal = Calendar.getInstance(zone);
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    };
}
