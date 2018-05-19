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
    private List<NewsFeed> feeds;
    private Outputter outputter = null;

    NewsReader(Outputter outputter)
    {
        this.feeds = new ArrayList<>();
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

        feed.addPublishedEntries(feed.getEntries()); // mark all entries as read
        this.feeds.add(feed);

        LogManager.getLogger(NewsReader.class).info("Added Feed -> " + feed.getURL());

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

    private Set<NewsEntry> getNewEntries(Set<NewsEntry> allEntries, Set<String> oldLinks)
    {
        Set<NewsEntry> newEntries = allEntries.stream()
                .filter(e -> !oldLinks.contains(e.getLink()) && !e.getTitle().isEmpty())
                .collect(Collectors.toSet());

        return newEntries;
    }

    public synchronized Outputter readNews()
    {
        LogManager.getLogger(NewsReader.class)
                .debug("readNews(): checking for updates...");

        this.feeds.forEach((feed) -> {
            Set<String> oldLinks = feed.getPublishedEntries().stream()
                .map(NewsEntry::getLink)
                .collect(Collectors.toSet());

            LogManager.getLogger(NewsReader.class).info("Nro Published Entries: " + feed.getPublishedEntries().size());
            Set<NewsEntry> entries = feed.getEntries();
            Set<NewsEntry> newEntries = this.getNewEntries(entries, oldLinks);

            LogManager.getLogger(NewsReader.class).info("Title: " + feed.getTitle() +
                    " URL " + feed.getURL() + " Number of oldLinks -> " + oldLinks.size() + " " +
                     "New Entries -> " + newEntries.size() + " Current Entries -> " + entries.size());

            newEntries.forEach(e -> this.outputter.append(e));

            feed.addPublishedEntries(newEntries);

            feed.truncatePublishedEntries(feed.getPublishedEntries().size() - entries.size()*5);
        });

        return this.outputter;
    }
}
