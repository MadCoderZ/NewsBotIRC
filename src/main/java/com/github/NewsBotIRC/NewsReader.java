package com.github.NewsBotIRC;

import com.github.NewsBotIRC.ConfReader.Input;
import com.github.NewsBotIRC.feedreaders.NewsEntry;
import com.github.NewsBotIRC.feedreaders.NewsFactory;
import com.github.NewsBotIRC.feedreaders.NewsFeed;
import com.github.NewsBotIRC.output.Outputter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;

public final class NewsReader
{
    private int pass = 0;
    private List<NewsFeed> feeds;
    private Set<NewsEntry> oldEntries;
    private Outputter outputter = null;

    NewsReader(Outputter outputter)
    {
        this.feeds = new ArrayList<>();
        this.oldEntries = new HashSet();
        try {
            this.outputter = outputter.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LogManager.getLogger(NewsReader.class).error(e.getMessage());
            return;
        }

        ConfReader config = ConfReader.getInstance();
        switch (config.getInput()) {
            case RSS:
                for (String myURL : config.getRssUrls()) {
                    this.addFeed(Input.RSS, myURL);
                }
                break;
            case DB:
                this.addFeed(Input.DB,
                        "https://github.com/MadCoderZ/NewsBotIRC/");
                break;
        }
    }

    public boolean addFeed(Input input, String url)
    {
        if (this.feeds.stream().anyMatch(f -> f.getURL().equals(url))) {
            return false;
        }

        URL nURL;
        try {
            nURL = new URL(url);
        } catch (MalformedURLException e) {
            LogManager.getLogger(NewsReader.class).error(e.getMessage());
            LogManager.getLogger(NewsReader.class).error("Invalid Feed URL -> "
                    + url);
            return false;
        }

        NewsFeed feed = NewsFactory.getInstance().createFeed(input, url);
        if (!feed.isValid()) {
            LogManager.getLogger(NewsReader.class).error("Invalid Feed -> "
                    + url);
            return false;
        }

        this.feeds.add(feed);
        this.oldEntries.addAll(feed.getEntries()); // mark all entries as read

        LogManager.getLogger(NewsReader.class).info("Added Feed -> " + url);

        return true;
    }

    public synchronized boolean removeFeed(int index)
    {
        if (index >= this.feeds.size() || index < 0 || this.feeds.isEmpty())
            return false;

        NewsFeed feedToRemove = this.feeds.get(index);
        if (feedToRemove != null) {
            this.feeds.remove(feedToRemove);
            return true;
        }
        return false;
    }

    public synchronized List<NewsFeed> getNewsFeeds() throws IOException
    {
        return this.feeds;
    }

    private List<NewsEntry> getAllEntries() throws IOException
    {
        List<NewsEntry> allEntries = new ArrayList<>();

        this.feeds.stream().forEachOrdered(feed ->
                                        allEntries.addAll(feed.getEntries()));
        return allEntries;
    }

    public synchronized Outputter readNews()
    {
        LogManager.getLogger(NewsReader.class)
                .debug("readNews(): checking for updates...");
        try {
            Set<String> oldLinks = this.oldEntries.stream()
                    .map(NewsEntry::getLink)
                    .collect(Collectors.toSet());

            List<NewsEntry> allEntries = this.getAllEntries();

            Set<NewsEntry> newEntries = allEntries.stream()
                    .filter(e -> !oldLinks.contains(e.getLink()))
                    .collect(Collectors.toSet());

            newEntries.forEach(e -> this.outputter.append(e));

            if (this.pass++ > 3) {
                this.oldEntries.clear(); // do not let it to grow too much
                this.oldEntries.addAll(allEntries);
                this.pass = 0;
            } else {
                this.oldEntries.addAll(newEntries);
            }
        } catch (IOException e) {
            LogManager.getLogger(NewsReader.class).error(e.getMessage());
        }

        return this.outputter;
    }
}
