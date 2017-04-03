package com.github.NewsBotIRC.cmds;

import com.github.NewsBotIRC.IRCMediator;
import java.net.MalformedURLException;
import org.apache.logging.log4j.LogManager;

public class AddFeed implements Cmd
{

    @Override
    public String get()
    {
        return "add";
    }

    @Override
    public void action(IRCMediator m, String params)
    {
        if (!params.startsWith("http")) {
            m.sendMessage("Invalid feed");
            LogManager.getLogger(AddFeed.class).error("Invalid feed");
            return;
        }

        try {
            m.addFeed(params);
        } catch (MalformedURLException e) {
            LogManager.getLogger(AddFeed.class).error(e.getMessage()
                    + " Invalid feed.");
            m.sendMessage("Invalid feed");
        }
    }

}
