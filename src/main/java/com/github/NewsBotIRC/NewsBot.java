package com.github.NewsBotIRC;

import com.github.NewsBotIRC.output.ConsoleOutputter;
import com.github.NewsBotIRC.output.DBOutputter;
import com.github.NewsBotIRC.output.JSONOutputter;
import com.github.NewsBotIRC.output.Outputter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static spark.Spark.get;

/*
 * Written by Gerardo Canosa and Geronimo Poppino.
 */
public class NewsBot
{
    public static void main(String[] args)
    {
        NewsReader newsReader;
        ConfReader config = ConfReader.getInstance();
        switch(config.getOutput()) {
            case IRC:
                IRCMediator.getInstance().startIRCClient();
                break;
            case JSON:
                newsReader = new NewsReader(new JSONOutputter());

                get("/news", (req, res) -> {
                    res.type("text/json");

                    Outputter jsonOutputter = newsReader.readNews();

                    return jsonOutputter.getOutput();
                });
                break;
            case CONSOLE:
                newsReader = new NewsReader(new ConsoleOutputter());
                new NewsExecutor(config.getPollFrequency()).addTask(
                        new NewsTask(newsReader)
                );
                break;
            case DB:
                newsReader = new NewsReader(new DBOutputter());
                new NewsExecutor(config.getPollFrequency()).addTask(
                        new NewsTask(newsReader)
                );
                break;
            default:
                Logger logger = LogManager.getLogger();
                logger.info("Invalid 'bot.preset' option selected!");
                break;
        }
    }
}