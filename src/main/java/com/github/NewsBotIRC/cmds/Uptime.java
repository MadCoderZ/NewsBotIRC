package com.github.NewsBotIRC.cmds;

import com.github.NewsBotIRC.IRCMediator;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Uptime implements Cmd
{
    @Override
    public String get()
    {
        return "uptime";
    }

    @Override
    public void action(IRCMediator m, String params)
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

        m.showMessage(elapsedDays + " days, " + elapsedHours + " hours, " +
                elapsedMinutes + " minutes, " + elapsedSeconds + " seconds");
    }
}
