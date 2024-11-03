package com.trainticketbooking.app.Utils;

import java.time.Duration;

public class DurationUtils {

    /**
     * Formats the duration in hours and minutes, or only in minutes if the duration is under one hour.
     *
     * @param duration the duration to format
     * @return a formatted string representing the duration
     */
    public static String formatDuration(Duration duration) {
        if (duration.toHours() > 0) {
            // Show hours and minutes if duration is 1 hour or more
            return String.format("%d giờ %d phút", duration.toHours(), duration.toMinutesPart());
        } else {
            // Show only minutes if duration is under 60 minutes
            return String.format("%d phút", duration.toMinutes());
        }
    }
}