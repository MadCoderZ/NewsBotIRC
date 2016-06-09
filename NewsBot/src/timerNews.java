import java.util.Timer;
import java.util.TimerTask;

public class timerNews {
	Timer timer;
	ReadNews readnews = new ReadNews();
	
	public timerNews(int seconds) {
		timer = new Timer();
		timer.schedule(new timerNewsTask(), 0, seconds*1000);
	}

	 class timerNewsTask extends TimerTask {
		 public void run() {
			try {
				readnews.setfeedUrl("http://www.clarin.com/rss/lo-ultimo/");
				readnews.readNews();
				System.out.println("New NewsRead task ran.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
	 }
}