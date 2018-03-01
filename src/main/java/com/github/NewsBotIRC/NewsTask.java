package com.github.NewsBotIRC;

import com.github.NewsBotIRC.output.Outputter;
import org.apache.logging.log4j.LogManager;

/**
 * Created by Geronimo Poppino on 11/6/16
 */
public class NewsTask implements Runnable
{
    private final NewsReader newsReader;

    public NewsTask(NewsReader newsReader)
    {
        this.newsReader = newsReader;
    }

    @Override
    public void run()
    {
        Outputter outputter;
        try {
            outputter = this.newsReader.readNews();
            outputter.save();
        } catch (Throwable t) {
            LogManager.getLogger(NewsTask.class).debug(t.getMessage());
            t.getStackTrace();
        }
    }
}
