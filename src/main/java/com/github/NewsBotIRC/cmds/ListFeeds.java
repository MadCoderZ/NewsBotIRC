package com.github.NewsBotIRC.cmds;

import com.github.NewsBotIRC.IRCMediator;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;

public class ListFeeds implements Cmd
{
    @Override
    public String get()
    {
        return "list";
    }

    @Override
    public void action(IRCMediator m, String params)
    {
        try {
            m.sendMessage("--> BEGIN");
            m.listFeeds();
            m.sendMessage("--> END");
        } catch (IOException e) {
            LogManager.getLogger(ListFeeds.class).error(e.getMessage());
        }
    }

    @Override
    public String help()
    {
        return "Lists the available feeds.";
    }
}
