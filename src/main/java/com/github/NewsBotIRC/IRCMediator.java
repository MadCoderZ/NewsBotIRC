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
import org.pircbotx.cap.SASLCapHandler;

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
        Configuration.Builder confBuilder = new Configuration.Builder();

        // check if SSL is enabled, otherwise it won't use SSL connection.
        if (ConfReader.getInstance().isSSL())
        {
            confBuilder.setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates());
        }

        // It tries to authenticate using SASL (it doesn't work on all networks)
        if (ConfReader.getInstance().isIdentifyEnabled())
        {
            confBuilder.addCapHandler(new SASLCapHandler(ConfReader.getInstance().getNick(), ConfReader.getInstance().getNickserv_passwd()));
        }

        confBuilder.setAutoReconnect(ConfReader.getInstance().isAutoreconnect());
        confBuilder.setAutoReconnectAttempts(ConfReader.getInstance().getReconnectattempts());
        confBuilder.setAutoReconnectDelay(ConfReader.getInstance().getDelaybetweenretries());
        confBuilder.setRealName(ConfReader.getInstance().getRealname());
        confBuilder.setName(ConfReader.getInstance().getNick());
        confBuilder.setLogin(ConfReader.getInstance().getLogin());
        confBuilder.setAutoNickChange(true);
        confBuilder.addServer(ConfReader.getInstance().getIrcserver(), ConfReader.getInstance().getPort());
        confBuilder.addAutoJoinChannel("#" + ConfReader.getInstance().getChannel());
        confBuilder.setVersion(ConfReader.getInstance().getVersion());
        confBuilder.addListener(new IRCListener(this) );
        confBuilder.buildConfiguration();

        // Print on stdout the bot's current version.
        System.out.println(ConfReader.getInstance().getVersion());

        this.bot = new PircBotX(confBuilder.buildConfiguration());
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