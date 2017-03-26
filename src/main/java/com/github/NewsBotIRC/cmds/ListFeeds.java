package com.github.NewsBotIRC.cmds;

import com.github.NewsBotIRC.IRCMediator;
import java.io.IOException;

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
            m.showMessage("--> BEGIN");
            m.listFeeds();
            m.showMessage("--> END");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
