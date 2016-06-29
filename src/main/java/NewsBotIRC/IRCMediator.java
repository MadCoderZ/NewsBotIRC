package NewsBotIRC;

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

/**
 * Created by Geronimo on 13/6/16.
 * Properties file support added by Gerardo Canosa
 */
public class IRCMediator {
    private final PircBotX bot;
    private final NewsReader newsReader;
    ConfReader confReader;

    public IRCMediator()
    {
        Configuration configuration = new Configuration.Builder()
            .setAutoReconnect(confReader.isAutoreconnect())
            .setAutoReconnectAttempts(confReader.getReconnectattempts())
            .setAutoReconnectDelay(confReader.getDelaybetweenentries())
            .setRealName(confReader.getRealname())
            .setName(confReader.getNick())
            .addServer(confReader.getIrcserver())
            .addAutoJoinChannel("#" + confReader.getChannel())
            .setVersion(confReader.getVersion())
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