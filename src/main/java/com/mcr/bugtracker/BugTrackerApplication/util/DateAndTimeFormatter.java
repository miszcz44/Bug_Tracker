package com.mcr.bugtracker.BugTrackerApplication.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateAndTimeFormatter {
    public static String convertDateAndTimeToCurrentUserZone(LocalDateTime serverDateTime) {
        ZoneId currentUserZone = ZoneId.systemDefault();
        ZoneId serverZone = ZoneId.of("Europe/Warsaw");
        LocalDateTime currentUserDateTime = serverDateTime.truncatedTo(ChronoUnit.SECONDS).atZone(serverZone)
                .withZoneSameInstant(currentUserZone).toLocalDateTime();
        return DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss").format(currentUserDateTime);
    }
}
