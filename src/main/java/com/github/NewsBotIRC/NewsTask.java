package com.github.NewsBotIRC;

import com.github.NewsBotIRC.output.Outputter;
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
        Outputter outputter = this.newsReader.readNews();
        outputter.save();
    }
}
