/*
 * The MIT License
 *
 * Copyright 2017 Geronimo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.NewsBotIRC.feedreaders;

import java.time.Instant;
import java.util.Set;
import java.util.TreeSet;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Geronimo
 */
public abstract class NewsFeed
{
    private String feedURL;
    private TreeSet<NewsEntry> publishedEntries;

    public NewsFeed()
    {
        this.publishedEntries = new TreeSet<>(
                (NewsEntry a, NewsEntry b) -> {
                    if (a.getDownloadedTimeStamp() == b.getDownloadedTimeStamp()) {
                        if (a.getLink() != null) {
                            return a.getLink().compareTo(b.getLink());
                        } else if (a.getTitle() != null) {
                            return a.getTitle().compareTo(b.getTitle());
                        } else return 0;
                    }
                    if (a.getDownloadedTimeStamp() > b.getDownloadedTimeStamp()) return 1;
                    return -1;
                });
    }

    public String getURL()
    {
        return this.feedURL;
    }

    public void setURL(String feedURL)
    {
        this.feedURL = feedURL;
    }

    public TreeSet<NewsEntry> getPublishedEntries()
    {
        return this.publishedEntries;
    }

    public void addPublishedEntries(Set<NewsEntry> entries)
    {
        LogManager.getLogger(NewsFeed.class).info("Number of Added Entries -> " + entries.size());
        entries.forEach(e -> e.setDownloadedTimeStamp(Instant.now().toEpochMilli()));
        this.publishedEntries.addAll(entries);
        LogManager.getLogger(NewsFeed.class).info("Number of Published Entries -> " +
                this.publishedEntries.size());
    }

    public void truncatePublishedEntries(int numberOfEntries)
    {
        int numberOfRemovedEntries = 0;
        if (numberOfEntries > 0 && !this.publishedEntries.isEmpty()) {
            while (numberOfEntries > 0) {
                NewsEntry entry = this.publishedEntries.pollFirst();
                if (entry == null) {
                    LogManager.getLogger(NewsFeed.class).info("Entry is null!");
                    return;
                }
                LogManager.getLogger(NewsFeed.class).info("Removing entry: " + entry.getLink() +
                        " - timestamp: " + entry.getDownloadedTimeStamp());
                numberOfEntries--;
                numberOfRemovedEntries++;
            }
        }
        LogManager.getLogger(NewsFeed.class).info("Number of Purged Entries: " + numberOfRemovedEntries);
    }

    public abstract boolean isValid();
    public abstract String getTitle();
    public abstract Set<NewsEntry> getEntries();
}
