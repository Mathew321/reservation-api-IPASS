package org.hu.reservation.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Utils {

    public static LocalDateTime toMorningOrEvening(LocalDateTime l1) {
        // Get the day and year from l1
        int dayOfYear = l1.getDayOfYear();
        int year = l1.getYear();

        // Determine the time for l2 based on the condition
        LocalTime timeForL2 = (l1.toLocalTime().isAfter(LocalTime.of(16, 0)))
                ? LocalTime.of(16, 0)
                : LocalTime.of(10, 0);

        // Construct LocalDateTime l2 with the day, year, and adjusted time
        LocalDateTime l2 = LocalDateTime.of(year, l1.getMonth(), l1.getDayOfMonth(), timeForL2.getHour(), timeForL2.getMinute());

        return l2;
    }
}
