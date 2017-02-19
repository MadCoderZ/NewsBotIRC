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
            System.out.println("params: " + params);
            index = Integer.parseInt(params);
        } catch (NumberFormatException e) {
            m.showMessage("ERROR: could not remove feed!");
            return;
        }

        if (m.removeFeed(index)) {
            m.showMessage("Success!");
        } else {
            m.showMessage("ERROR: could not remove feed!");
        }
    }
}
