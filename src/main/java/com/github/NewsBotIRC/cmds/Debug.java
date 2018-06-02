package com.github.NewsBotIRC.cmds;

import com.github.NewsBotIRC.IRCMediator;

/**
 *
 * @author Gerardo
 */
public class Debug implements Cmd
{
    @Override
    public String get()
    {
        return "debug";
    }

    @Override
    public void action(IRCMediator m, String params)
    {
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();

        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        long usedMB = (memory / 1024) / 1024;
        m.sendMessage("Used memory: " + usedMB + " Megabytes");
    }

    @Override
    public String help()
    {
        return "Shows the bot's memory usage.";
    }
}

