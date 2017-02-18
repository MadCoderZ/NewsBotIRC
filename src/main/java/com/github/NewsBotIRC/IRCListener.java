package com.github.NewsBotIRC;
import com.rometools.rome.io.FeedException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.IOException;
import java.net.MalformedURLException;

/*
 *  Written by Geronimo Poppino
 *  Organization and code enhancement by Gerardo Canosa.
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
        if (event.getMessage().startsWith("!version")) {
            event.respond(ConfReader.getInstance().getVersion());
        } else if (event.getMessage().startsWith("!list")) {
            try {
                this.mediator.showMessage("--> BEGIN: My Feeds Are...");
                this.mediator.listFeeds();
                this.mediator.showMessage("--> END");
            } catch (IOException | FeedException e) {
                System.out.println(e.getMessage());
            }
        } else if (event.getMessage().startsWith("!add")) {
            try {
                this.mediator.addFeed( event.getMessage().substring( event.getMessage().indexOf("http") ) );
            } catch (MalformedURLException e) {
                System.out.println(e.getMessage());
            }
        } else if (event.getMessage().startsWith("!remove")) {
            int index = -1;
            try {
                index = Integer.parseInt( event.getMessage().substring( event.getMessage().indexOf(" ") + 1) );
            } catch (NumberFormatException e) {
                event.respond("ERROR: could not remove feed!");
                return;
            }

            if (this.mediator.removeFeed(index)) {
                event.respond("Success!");
            } else {
                event.respond("ERROR: could not remove feed!");
            }
        } else if (event.getMessage().startsWith("!uptime")) {
            event.respond(new Uptime().getUptime());
        }
    }
}