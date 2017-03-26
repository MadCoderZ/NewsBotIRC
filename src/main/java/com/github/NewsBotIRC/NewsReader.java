package com.github.NewsBotIRC;

import com.github.NewsBotIRC.feedreaders.NewsEntry;
import com.github.NewsBotIRC.feedreaders.NewsFactory;
import com.github.NewsBotIRC.feedreaders.NewsFeed;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import java.util.stream.Collectors;
import org.pircbotx.Colors;

public final class NewsReader
{
    private int pass = 0;
    private List<URL> feeds;
    private IRCMediator mediator;
    private Set<NewsEntry> oldEntries;

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

        } catch (IOException e) {
            System.out.println("NewsReader() Exception: " + e.getMessage());
        }
    }

    public synchronized boolean addFeedUrl(String url)
            throws MalformedURLException
    {
        URL nURL = new URL(url);
        if (this.feeds.stream().anyMatch(u -> u.equals(nURL))) {
            return false;
        }

        String feedReader = ConfReader.getInstance().getFeedReader();
        NewsFeed feed = NewsFactory.getInstance().createFeed(feedReader, url);
        if (!feed.isValid()) return false;

        this.feeds.add(nURL);
        this.oldEntries.addAll(feed.getEntries()); // mark all entries as read

        System.out.println("Added Feed -> " + url);

        return true;
    }

    public synchronized boolean removeFeed(int index)
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

    public synchronized List<NewsFeed> getNewsFeeds() throws IOException
    {
        List<NewsFeed> newsFeeds = new ArrayList<>();

        String feedReader = ConfReader.getInstance().getFeedReader();
        this.feeds.stream()
                .map(url -> NewsFactory.getInstance().createFeed(feedReader,
                                                                url.toString()))
                .forEachOrdered((feed) -> {
                    if (feed.isValid()) newsFeeds.add(feed);
                });

        return newsFeeds;
    }

    private List<NewsEntry> getAllEntries() throws IOException
    {
        List<NewsEntry> newEntries = new ArrayList<>();

        String feedReader = ConfReader.getInstance().getFeedReader();
        this.feeds.stream()
                .map(url -> NewsFactory.getInstance().createFeed(feedReader,
                        url.toString())).forEachOrdered(
                                feed -> {
                                    if (feed.isValid()) {
                                        newEntries.addAll(feed.getEntries());
                                    }
                                });
        return newEntries;
    }

    private void loadNews() throws IOException
    {
        System.out.println("loadNews(): Pre-loading news, please wait...");
        this.oldEntries.addAll(this.getAllEntries());
    }

    public synchronized void readNews()
    {
        System.out.println("readNews(): checking for updates...");
        try {
            Set<String> oldLinks = this.oldEntries.stream()
                    .map(NewsEntry::getLink)
                    .collect(Collectors.toSet());

            List<NewsEntry> allEntries = this.getAllEntries();

            Set<NewsEntry> newEntries = allEntries.stream()
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

    private void showEntry(NewsEntry entry)
    {
        String domain = entry.getLink().replaceFirst(".*https?://([\\w.-]+)/.*",
                                                                        "<$1>");
        String link = UrlShortener.shortenUrl(entry.getLink());

        this.mediator.showMessage("\"" + Colors.DARK_GRAY + entry.getTitle()
                + Colors.NORMAL + "\" " + Colors.ITALICS + domain
                + Colors.NORMAL + " <" + link + ">");
    }
}
