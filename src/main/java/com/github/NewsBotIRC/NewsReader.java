package com.github.NewsBotIRC;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.util.stream.Collectors;
import org.pircbotx.Colors;

public class NewsReader
{
    private int pass = 0;
    private List<URL> feeds;
    private IRCMediator mediator;
    private Set<SyndEntry> oldEntries;

    NewsReader(IRCMediator mediator)
    {
        this.mediator = mediator;
        this.feeds = new ArrayList<>();
        this.oldEntries = new HashSet();

        try {

            for (String myUrl : ConfReader.getInstance().getRssUrls()) {
                this.addFeedUrl(myUrl);
            }

            this.loadNews();

        } catch (IOException | FeedException e) {
            System.out.println("NewsReader() Exception: " + e.getMessage());
        }
    }

    public boolean addFeedUrl(String newUrl)
            throws MalformedURLException
    {
        URL nURL = new URL(newUrl);
        if (!this.feeds.stream().noneMatch((url) -> (url.equals(nURL)))) {
            return false;
        }

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed;

        try {
            feed = input.build(new XmlReader(new URL(newUrl)));
        } catch (FeedException | IOException e) {
            System.out.println("ERROR:" + e.getMessage());
            return false;
        }

        this.feeds.add(nURL);
        this.oldEntries.addAll(feed.getEntries()); // mark all entries as read

        System.out.println("Added Feed -> " + newUrl);

        return true;
    }

    public boolean removeFeed(int index)
    {
        if (index >= this.feeds.size() || index < 0 || this.feeds.isEmpty())
            return false;

        URL urlToRemove = this.feeds.get(index);
        if (urlToRemove != null) {
            this.feeds.remove(urlToRemove);
            return true;
        }
        return false;
    }

    public List<SyndFeed> getNewsFeeds() throws IOException, FeedException
    {
        List<SyndFeed> newsFeeds = new ArrayList<>();

        for (URL myFeed : this.feeds) {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(myFeed));

            newsFeeds.add(feed);
        }
        return newsFeeds;
    }

    private List<SyndEntry> getAllEntries() throws IOException, FeedException
    {
        List<SyndEntry> newEntries = new ArrayList<>();

        for (URL myFeed : this.feeds) {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(myFeed));

            newEntries.addAll(feed.getEntries());
        }
        return newEntries;
    }

    private void loadNews() throws IOException, FeedException
    {
        System.out.println("loadNews(): Pre-loading news, please wait...");
        this.oldEntries.addAll(this.getAllEntries());
    }

    public void readNews() throws FeedException
    {
        System.out.println("readNews(): checking for updates...");
        try {
            Set<String> oldLinks = this.oldEntries.stream()
                    .map(SyndEntry::getLink)
                    .collect(Collectors.toSet());

            List<SyndEntry> allEntries = this.getAllEntries();

            Set<SyndEntry> newEntries = allEntries.stream()
                    .filter(e -> !oldLinks.contains(e.getLink()))
                    .collect(Collectors.toSet());

            newEntries.forEach(e -> this.showEntry(e));

            if (this.pass++ > 3) {
                this.oldEntries.clear(); // do not let it to grow too much
                this.oldEntries.addAll(allEntries);
                this.pass = 0;
            } else {
                this.oldEntries.addAll(newEntries);
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void showEntry(SyndEntry entry)
    {
        String domain = entry.getLink().replaceFirst(".*https?://([\\w.-]+)/.*", "<$1>");
        String link = UrlShortener.shortenUrl(entry.getLink());

        this.mediator.showMessage("\"" + Colors.DARK_GRAY + entry.getTitle() + Colors.NORMAL + "\" " + Colors.ITALICS + domain + Colors.NORMAL + " <" + link + ">");
    }
}
