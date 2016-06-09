import com.sun.syndication.io.FeedException;

// main class
public class newsBot {
	//public final String NEWSBOTVERSION = "v0.1";
	
	public static void main(String[] args) throws FeedException, Exception {
		// set proxy socks
		System.setProperty("socksProxyHost", "127.0.0.1");
		System.setProperty("socksProxyPort", "9999");
		
		ListsUrls listsUrls = new ListsUrls();
		Irc irc = new Irc();
		
		// poll news every X seconds.
		new timerNews(60);
		
		listsUrls.addFeedsToList("http://www.clarin.com/rss/lo-ultimo/");
		listsUrls.addFeedsToList("http://contenidos.lanacion.com.ar/herramientas/rss/origen=2");
		
		System.out.println("NewsBot v0.1 by Gerardo Canosa");
		System.out.println(listsUrls.getFeeds());
		
		// connect to the IRC
		irc.connect();
		
	}
}