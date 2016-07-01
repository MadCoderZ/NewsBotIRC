package com.github.NewsBotIRC;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.Colors;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.ConfigurationException;

/**
 * Created by Geronimo on 13/6/16.
 * Properties file support added by Gerardo Canosa
 */
public class IRCMediator {
    private final PircBotX bot;
    private final NewsReader newsReader;
    private ConfReader confReader;

    public IRCMediator()
    {
        try {
            this.confReader = new ConfReader();
        } catch (ConfigurationException ex) {
            Logger.getLogger(IRCMediator.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

        Configuration configuration = new Configuration.Builder()
            .setAutoReconnect(this.confReader.isAutoreconnect())
            .setAutoReconnectAttempts(this.confReader.getReconnectattempts())
            .setAutoReconnectDelay(this.confReader.getDelaybetweenentries())
            .setRealName(this.confReader.getRealname())
            .setName(this.confReader.getNick())
            .addServer(this.confReader.getIrcserver())
            .addAutoJoinChannel("#" + this.confReader.getChannel())
            .setVersion(this.confReader.getVersion())
            .addListener( new IRCListener(this) )
            .buildConfiguration();

        this.bot = new PircBotX(configuration);
        this.newsReader = new NewsReader(this);

        new TimerNews(180).addTask( new NewsTask(this.newsReader) );
    }

    public void showMessage(String message)
    {
        System.out.println("showMessage(): about to print an entry into the channels...");

        Iterator<Channel> channels = this.bot.getUserChannelDao().getAllChannels().iterator();
        while (channels.hasNext()) {
            Channel chan = channels.next();
            this.bot.send().message(chan.getName(), message);
        }
    }

    public void listFeeds() throws IOException, FeedException
    {
        List<SyndFeed> feeds = this.newsReader.getNewsFeeds();

        for (SyndFeed myFeed : feeds) {
            this.showMessage(myFeed.getTitleEx().getValue() + " : " + myFeed.getLink());
        }
    }

    public void addFeed(String url) throws MalformedURLException
    {
        if ( !this.newsReader.addFeedUrl(url) ) {
            this.showMessage("ERROR: invalid feed!");
        } else {
            this.showMessage("Success!");
        }
    }

    public void removeFeed(String url)
    {
        if ( !this.newsReader.removeFeed(url) ) {
            this.showMessage("ERROR: could not remove feed!");
        } else {
            this.showMessage("Success!");
        }
    }

    public void start()
    {
        try {
            this.bot.startBot();
        } catch (IOException | IrcException e) {
            System.out.println(e.getMessage());
        }
    }
}