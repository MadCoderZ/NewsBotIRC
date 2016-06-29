package com.github.NewsBotIRC;

import com.rometools.rome.io.FeedException;
import java.util.TimerTask;

/**
 * Created by Geronimo Poppino on 11/6/16.
 */
public class NewsTask extends TimerTask
{
    private NewsReader newsReader;

    public NewsTask(NewsReader newsReader)
    {
        this.newsReader = newsReader;
    }

    public void run() {
        try {
            this.newsReader.readNews();
        } catch (FeedException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
