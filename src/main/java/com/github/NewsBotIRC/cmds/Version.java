package com.github.NewsBotIRC.cmds;

import com.github.NewsBotIRC.ConfReader;
import com.github.NewsBotIRC.IRCMediator;

public class Version implements Cmd {

    @Override
    public String get()
    {
        return "version";
    }

    @Override
    public void action(IRCMediator m, String params)
    {
        m.showMessage(ConfReader.getInstance().getVersion());
    }
}
