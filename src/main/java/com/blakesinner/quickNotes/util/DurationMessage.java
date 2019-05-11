package com.blakesinner.quickNotes.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * Get a string showing the time elapsed between now and a given time.
 *
 * @author bsinner
 */
public class DurationMessage {

    private static final long MIN = 1000 * 60;
    private static final long HOUR = MIN * 60;
    private static final long DAY = HOUR * 24;
    private static final long WEEK = DAY * 7;
    private static final long MONTH = WEEK * 4;

    /**
     * Get the duration string.
     *
     * @param date the LocalDateTime
     * @return     the duration string, or the date time formatted to show
     *             only the date if the duration is over one month
     */
    public static String elapsed(LocalDateTime date) {
        long duration = MILLIS.between(date, LocalDateTime.now());

        if (duration < MIN ) {
            return "seconds ago";
        } else if (duration < HOUR) {
            long result = duration / MIN;
            return result + (result == 1 ? " minute ago" : " minutes ago");
        } else if (duration < DAY) {
            long result = duration / HOUR;
            return result + (result == 1 ? " hour ago" : " hours ago");
        } else if (duration < WEEK) {
            long result = duration / DAY;
            return result + (result == 1 ? " day ago" : " days ago");
        } else if (duration < MONTH) {
            long result = duration / WEEK;
            return result + (result == 1 ? " week ago" : " weeks ago");
        }

        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
