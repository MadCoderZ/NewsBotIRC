package NewsBotIRC;
import com.rometools.rome.io.FeedException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.IOException;
import java.net.MalformedURLException;

/*
 *  Written by Geronimo Poppino
 *  Organization and code enhancement by Gerardo Canosa
 */
public class IRCListener extends ListenerAdapter
{
    private IRCMediator mediator;

    public IRCListener(IRCMediator mediator)
    {
        this.mediator = mediator;
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event)
    {
        if (event.getMessage().startsWith("?version")) {
            event.respond("NewsBot v0.1 by Gerardo Canosa and Geronimo Poppino!");
        } else if (event.getMessage().startsWith("?list")) {
            try {
                this.mediator.showMessage("--> BEGIN: My Feeds Are...");
                this.mediator.listFeeds();
                this.mediator.showMessage("--> END");
            } catch (IOException | FeedException e) {
                System.out.println(e.getMessage());
            }
        } else if (event.getMessage().startsWith("?add")) {
            try {
                this.mediator.addFeed( event.getMessage().substring( event.getMessage().indexOf("http") ) );
            } catch (MalformedURLException e) {
                System.out.println(e.getMessage());
            }
        } else if (event.getMessage().startsWith("?remove")) {
            System.out.println( event.getMessage().substring( event.getMessage().indexOf("http") ) );
            this.mediator.removeFeed( event.getMessage().substring( event.getMessage().indexOf("http") ) );
        }
    }
}