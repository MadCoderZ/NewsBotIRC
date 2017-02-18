package com.github.NewsBotIRC;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 *
 * @author Gerardo
 */
public class Uptime
{

    String getUptime()
    {
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long uptime = rb.getUptime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = uptime / daysInMilli;
        uptime = uptime % daysInMilli;

        long elapsedHours = uptime / hoursInMilli;
        uptime = uptime % hoursInMilli;

        long elapsedMinutes = uptime / minutesInMilli;
        uptime = uptime % minutesInMilli;

        long elapsedSeconds = uptime / secondsInMilli;

        return elapsedDays + " days, " + elapsedHours + " hours, " +
                elapsedMinutes + " minutes, " + elapsedSeconds + " seconds";
    }
}
