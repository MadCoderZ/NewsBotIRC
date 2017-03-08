package com.github.NewsBotIRC.cmds;

import java.util.ArrayList;
import java.util.List;

public class CmdFactory
{
    private List<Cmd> cmds = null;
    private static CmdFactory instance = null;

    protected CmdFactory()
    {
        this.cmds = new ArrayList<>();

        this.cmds.add(new Version());
        this.cmds.add(new Uptime());
        this.cmds.add(new ListFeeds());
        this.cmds.add(new AddFeed());
        this.cmds.add(new RemoveFeed());
    }

    public static CmdFactory getInstance()
    {
        if (instance == null) instance = new CmdFactory();
        return instance;
    }

    public List<Cmd> getCmds()
    {
        return this.cmds;
    }
}
