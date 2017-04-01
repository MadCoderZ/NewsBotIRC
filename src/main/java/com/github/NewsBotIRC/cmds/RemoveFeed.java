package com.github.NewsBotIRC.cmds;

import com.github.NewsBotIRC.IRCMediator;

public class RemoveFeed implements Cmd
{

    @Override
    public String get()
    {
        return "remove";
    }

    @Override
    public void action(IRCMediator m, String params)
    {
        int index = -1;
        try {
            index = Integer.parseInt(params);
        } catch (NumberFormatException e) {
            m.sendMessage("ERROR: could not remove feed!");
            return;
        }

        if (m.removeFeed(index)) {
            m.sendMessage("Success!");
        } else {
            m.sendMessage("ERROR: could not remove feed!");
        }
    }
}
