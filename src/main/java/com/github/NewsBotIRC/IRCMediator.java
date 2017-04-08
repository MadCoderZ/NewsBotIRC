package com.github.NewsBotIRC;

import com.github.NewsBotIRC.ConfReader.Input;
import com.github.NewsBotIRC.feedreaders.NewsFeed;
import com.github.NewsBotIRC.output.IRCOutputter;
import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.SASLCapHandler;

/**
 * Created by Geronimo on 13/6/16.
 * Properties file support added by Gerardo Canosa.
 */
public class IRCMediator
{
    private static IRCMediator instance = null;

    private final PircBotX bot;
    private final NewsReader newsReader;

    protected IRCMediator()
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
            confBuilder.addCapHandler(new SASLCapHandler(ConfReader.getInstance().getNick(),
                    ConfReader.getInstance().getNickserv_passwd(), true));
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

        confBuilder.setVersion(
                ConfReader.getAppProperties().getString("application.name") +
                " v" + ConfReader.getAppProperties()
                        .getString("application.version") +
                " " + ConfReader.getAppProperties()
                        .getString("application.buildnumber")
        );
        confBuilder.addListener(new IRCListener(this) );
        confBuilder.buildConfiguration();

        this.bot = new PircBotX(confBuilder.buildConfiguration());
        this.newsReader = new NewsReader(new IRCOutputter());

        new NewsTimer(ConfReader.getInstance().getPollFrequency()).addTask(
                        new NewsTask(newsReader)
        );
    }

    public static IRCMediator getInstance()
    {
        if (instance == null) instance = new IRCMediator();
        return instance;
    }

    public void sendMessage(String message)
    {
        Iterator<Channel> channels = this.bot.getUserChannelDao().getAllChannels().iterator();
        while (channels.hasNext()) {
            Channel chan = channels.next();
            this.bot.send().message(chan.getName(), message);
        }
    }

    public void listFeeds() throws IOException
    {
        List<NewsFeed> feeds = this.newsReader.getNewsFeeds();

        int i = 0;
        for (NewsFeed myFeed : feeds)
        {
            this.sendMessage("[" + i + "] <> " + myFeed.getTitle());
            ++i;
        }
    }

    public void addFeed(String url) throws MalformedURLException
    {
        if (!this.newsReader.addFeed(Input.RSS, url)) {
            this.sendMessage("ERROR: invalid or duplicate feed!");
        } else {
            this.sendMessage("Success!");
        }
    }

    public boolean removeFeed(int index)
    {
        return this.newsReader.removeFeed(index);
    }

    public void startIRCClient()
    {
        try {
            this.bot.startBot();
        } catch (IOException | IrcException e) {
            LogManager.getLogger(IRCMediator.class).error(e.getMessage());
        }
    }
}