package com.github.NewsBotIRC;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.ConfigurationException;
import org.pircbotx.UtilSSLSocketFactory;

/**
 * Created by Geronimo on 13/6/16.
 * Properties file support added by Gerardo Canosa.
 */
public class IRCMediator
{
    private final PircBotX bot;
    private final NewsReader newsReader;

    public IRCMediator()
    {
        Configuration configuration = null;

        if (ConfReader.getInstance().isSSL()) {
            configuration = new Configuration.Builder()
            .setAutoReconnect(ConfReader.getInstance().isAutoreconnect())
            .setAutoReconnectAttempts(ConfReader.getInstance().getReconnectattempts())
            .setAutoReconnectDelay(ConfReader.getInstance().getDelaybetweenretries())
            .setRealName(ConfReader.getInstance().getRealname())
            .setName(ConfReader.getInstance().getNick())
            .setLogin(ConfReader.getInstance().getLogin())
            .setAutoNickChange(true)
            .addServer(ConfReader.getInstance().getIrcserver(), ConfReader.getInstance().getPort())
            .setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
            .addAutoJoinChannel("#" + ConfReader.getInstance().getChannel())
            .setVersion(ConfReader.getInstance().getVersion())
            .addListener( new IRCListener(this) )
            .buildConfiguration();
        } else {
            configuration = new Configuration.Builder()
            .setAutoReconnect(ConfReader.getInstance().isAutoreconnect())
            .setAutoReconnectAttempts(ConfReader.getInstance().getReconnectattempts())
            .setAutoReconnectDelay(ConfReader.getInstance().getDelaybetweenretries())
            .setRealName(ConfReader.getInstance().getRealname())
            .setName(ConfReader.getInstance().getNick())
            .setLogin(ConfReader.getInstance().getLogin())
            .setAutoNickChange(true)
            .addServer(ConfReader.getInstance().getIrcserver(), ConfReader.getInstance().getPort())
            .addAutoJoinChannel("#" + ConfReader.getInstance().getChannel())
            .setVersion(ConfReader.getInstance().getVersion())
            .addListener( new IRCListener(this) )
            .buildConfiguration();
        }

        System.out.println(ConfReader.getInstance().getVersion());

        this.bot = new PircBotX(configuration);
        this.newsReader = new NewsReader(this);

        new TimerNews(ConfReader.getInstance().getPollFrequency()).addTask( new NewsTask(this.newsReader) );
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

        feeds.forEach((myFeed) -> {
            this.showMessage(myFeed.getTitleEx().getValue() + " : " + myFeed.getLink());
        });
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