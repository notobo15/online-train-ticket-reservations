package com.trainticketbooking.app.Utils;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date calExpiryDate(int expiryTimeInMinutes) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime expiryDateTime = now.plus(expiryTimeInMinutes, ChronoUnit.MINUTES);
        return Date.from(expiryDateTime.toInstant());
    }

    private static Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }
}