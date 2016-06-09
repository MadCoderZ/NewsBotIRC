import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

// read news from RSS
public class ReadNews {
URL feedUrl;
	
	void setfeedUrl(String newUrl) throws MalformedURLException
	{
		this.feedUrl = new URL(newUrl);
	}
	
	URL getfeedUrl() {
		return this.feedUrl;
	}
	
	int readNews() throws Exception, FeedException {
		int tempHash, sum = 0;
		List<Integer> hashes = new ArrayList<Integer>();
		
		try {
			//URL feedUrl = new URL("http://contenidos.lanacion.com.ar/herramientas/rss/origen=2");
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(this.getfeedUrl()));
			
			// create iterator and List
			List entries = feed.getEntries();
			Iterator itEntries = entries.iterator();
		//	Iterator itHashes = hashes.iterator();
			
			while (itEntries.hasNext()) {
				SyndEntry entry = (SyndEntry) itEntries.next();
				tempHash = entry.getDescription().hashCode();
				hashes.add(tempHash);
				
				if (!hashes.contains(entry.getDescription().hashCode())) {
					System.out.println("NEWS: " + entry.getDescription().getValue().toString());
					System.out.println(hashes.get(sum));
					sum++;
				} else {
					System.out.println("NEWS2: " + entry.getDescription().getValue().toString());
					System.out.println(hashes.get(sum));
					sum++;
				}
				//System.out.println("Link: " + entry.getLink());
				//System.out.println("Author: " + entry.getAuthor());
				//System.out.println("Publish Date: " + entry.getUpdatedDate());
				//System.out.println("Description: " + entry.getDescription().getValue());
				//System.out.println();				
			}
			
			//System.out.println(feed);
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: " + e.getMessage());
		}
		return 0;
	}
}