package com.github.NewsBotIRC.cmds;

import com.github.NewsBotIRC.ConfReader;
import com.github.NewsBotIRC.IRCMediator;
import org.apache.commons.configuration.Configuration;

public class Version implements Cmd {

    @Override
    public String get()
    {
        return "version";
    }

    @Override
    public void action(IRCMediator m, String params)
    {
        Configuration config = ConfReader.getAppProperties();
        m.sendMessage(config.getString("application.name") + " v" +
                config.getString("application.version") + " by " +
                config.getString("application.developers") + " (" +
                config.getString("application.inceptionYear") + ")"
        );
    }
}
