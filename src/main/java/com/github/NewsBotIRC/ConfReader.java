package com.github.NewsBotIRC;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author  Written by Gerardo Canosa
 */
public class ConfReader {
        private boolean autoreconnect = false;
        private int reconnectattempts;
        private int delaybetweenentries;
        private String realname = null;
        private String nick = null;
        private String version = null;
        private String ircserver = null;
        private String channel = null;

        private CompositeConfiguration config = null;

    ConfReader() throws ConfigurationException
    {
        this.config = new CompositeConfiguration();
        this.config.addConfiguration(
            new PropertiesConfiguration("newsbot.properties"));

        this.autoreconnect = config.getBoolean("bot.autoreconnect");
        this.reconnectattempts = config.getInt("bot.reconnectattempts");
        this.delaybetweenentries = config.getInt("bot.delaybetweenentries");
        this.realname = config.getString("bot.realname");
        this.nick = config.getString("bot.nick");
        this.version = config.getString("bot.version");
        this.ircserver = config.getString("bot.ircserver");
        this.channel = config.getString("bot.channel");
    }

    public boolean isAutoreconnect()
    {
        return this.autoreconnect;
    }

    public void setAutoreconnect(boolean autoreconnect)
    {
        this.autoreconnect = autoreconnect;
    }

    public int getReconnectattempts()
    {
        return this.reconnectattempts;
    }

    public void setReconnectattempts(int reconnectattempts)
    {
        this.reconnectattempts = reconnectattempts;
    }

    public int getDelaybetweenentries()
    {
        return this.delaybetweenentries;
    }

    public void setDelaybetweenentries(int delaybetweenentries)
    {
        this.delaybetweenentries = delaybetweenentries;
    }

    public String getRealname()
    {
        return this.realname;
    }

    public void setRealname(String realname)
    {
        this.realname = realname;
    }

    public String getNick()
    {
        return this.nick;
    }

    public void setNick(String nick)
    {
        this.nick = nick;
    }

    public String getVersion()
    {
        return this.version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getIrcserver()
    {
        return this.ircserver;
    }

    public void setIrcserver(String ircserver)
    {
        this.ircserver = ircserver;
    }

    public String getChannel()
    {
        return this.channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public CompositeConfiguration getConfig()
    {
        return this.config;
    }

    public void setConfig(CompositeConfiguration config)
    {
        this.config = config;
    }
}