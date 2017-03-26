package com.github.NewsBotIRC;

import java.util.TimerTask;

/**
 * Created by Geronimo Poppino on 11/6/16
 */
public class NewsTask extends TimerTask
{
    private final NewsReader newsReader;

    public NewsTask(NewsReader newsReader)
    {
        this.newsReader = newsReader;
    }

    @Override
    public void run()
    {
        this.newsReader.readNews();
    }
}
