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
import org.pircbotx.Colors;

public class NewsReader
{
	private IRCMediator mediator;
	private Set<String> oldLinks;
        private List<URL> feeds;

	NewsReader(IRCMediator mediator)
	{
            this.mediator = mediator;
            this.oldLinks = new HashSet<>();
            this.feeds = new ArrayList<>();

            try {
                this.addFeedUrl("http://www.clarin.com/rss/lo-ultimo/");
                this.addFeedUrl("http://www.nacion.com/rss/latest/?contentType=NWS");
                this.addFeedUrl("http://rss.cnn.com/rss/edition.rss");
                this.addFeedUrl("http://feeds.arstechnica.com/arstechnica/index");
                this.addFeedUrl("https://www.reddit.com/r/news/.rss");
                this.addFeedUrl("http://www.osnews.com/files/recent.xml");
                this.addFeedUrl("http://www.cnet.com/rss/news/");
                this.addFeedUrl("https://news.ycombinator.com/rss");
                this.addFeedUrl("http://rss.slashdot.org/Slashdot/slashdot");
                this.addFeedUrl("http://gizmodo.com/rss");
                this.addFeedUrl("http://feeds.bbci.co.uk/news/rss.xml");
                this.loadNews();
            } catch (IOException | FeedException e) {
                System.out.println("NewsReader() Exception: " + e.getMessage());
            }
	}

	public boolean addFeedUrl(String newUrl) throws MalformedURLException
	{
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed;

            try {
                feed = input.build(new XmlReader( new URL(newUrl) ) );
            } catch (FeedException | IOException e) {
                return false;
            }

            this.feeds.add( new URL(newUrl) );

            System.out.println("Added Feed -> " + feed.getLink());

            return true;
	}

    public boolean removeFeed(String url)
    {
        SyndFeedInput input = new SyndFeedInput();

        URL urlToRemove = null;
        SyndFeed currentFeed;
        for (URL myURL : this.feeds) {
            try {
                currentFeed = input.build(new XmlReader( myURL ));
            } catch (FeedException | IOException e) {
                System.out.println(e.getMessage());
                continue;
            }
            if (currentFeed.getLink().compareTo(url) == 0) {
                urlToRemove = myURL;
                break;
            }
        }
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
            SyndFeed feed = input.build(new XmlReader( myFeed ) );

            newsFeeds.add(feed);
        }
        return newsFeeds;
    }

    private List<SyndEntry> getNewEntries() throws IOException, FeedException
    {
        List<SyndEntry> newEntries = new ArrayList<>();

        for (URL myFeed : this.feeds) {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader( myFeed ) );

            newEntries.addAll( feed.getEntries() );
        }
        return newEntries;
    }

    private Set<String> getLinks(List<SyndEntry> entries)
    {
        Set<String> links = new HashSet<>();
        for (SyndEntry entry : entries) {
            links.add(entry.getLink());
        }
        return links;
    }

    private SyndEntry findEntryByLink(String myLink, List<SyndEntry> myEntries)
    {
        SyndEntry rEntry = null;
        for (SyndEntry mEntry : myEntries) {
            if (myLink.compareTo(mEntry.getLink()) == 0) {
                rEntry = mEntry;
                break;
            }
        }
        return rEntry;
    }

    private void loadNews() throws IOException, FeedException
    {
        System.out.println("loadNews(): Pre-loading news, please wait...");
        List<SyndEntry> newEntries = this.getNewEntries();
        Set<String> newLinks = this.getLinks(newEntries);

        this.oldLinks.addAll(newLinks);
    }

    private void showLinks(String msg, Set<String> s)
    {
        for (String link : s) {
            System.out.println(msg + " -> " + link + " | hash -> " + link.hashCode());
        }
    }

    public void readNews() throws FeedException, InterruptedException
    {
        System.out.println("readNews(): checking for updates...");
	try {
            List<SyndEntry> newEntries = this.getNewEntries();
            Set<String> newLinks = this.getLinks(newEntries);

            if (this.oldLinks.containsAll(newLinks)) return;

            Set<String> newLinksOriginal = new HashSet<>(newLinks);

            newLinks.removeAll(this.oldLinks);
            if (newLinks.isEmpty()) return;

            this.showLinks("New Links", newLinks);

            Iterator it = newLinks.iterator();

            while (it.hasNext()) {
                String myLink = (String) it.next();
                SyndEntry entry = this.findEntryByLink(myLink, newEntries);
                if (entry != null) {
                    this.showEntry(entry);
                    Thread.sleep(500);
                }
            }

            Set<String> discardedLinks = new HashSet<>(this.oldLinks);
            discardedLinks.removeAll(newLinksOriginal);
            this.showLinks("Discarded Links", discardedLinks);

            this.oldLinks.clear();
            this.oldLinks.addAll(newLinksOriginal);

        } catch(IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void showEntry(SyndEntry entry)
    {
        String domain = entry.getLink().replaceFirst(".*https?://([\\w.-]+)/.*", "<$1>");
        String link = UrlShortener.shortenUrl(entry.getLink());

        this.mediator.showMessage("\"" + Colors.DARK_GRAY + entry.getTitle() + Colors.NORMAL + "\" " + Colors.BOLD + domain + Colors.NORMAL + " <" + link + ">");
    }
}