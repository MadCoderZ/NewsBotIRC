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
        Iterator<Channel> channels = this.bot.getUserChannelDao().getAllChannels().iterator();
        while (channels.hasNext()) {
            Channel chan = channels.next();
            this.bot.send().message(chan.getName(), message);
        }
    }

    public void listFeeds() throws IOException, FeedException
    {
        List<SyndFeed> feeds = this.newsReader.getNewsFeeds();

        int i = 0;
        for (SyndFeed myFeed : feeds) {
            this.showMessage("[" + i + "] <> " + myFeed.getTitleEx().getValue());
            ++i;
        }
    }

    public void addFeed(String url) throws MalformedURLException
    {
        if ( !this.newsReader.addFeedUrl(url) ) {
            this.showMessage("ERROR: invalid or duplicate feed!");
        } else {
            this.showMessage("Success!");
        }
    }

    public boolean removeFeed(int index)
    {
        return this.newsReader.removeFeed(index);
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