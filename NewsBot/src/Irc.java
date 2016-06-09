import org.pircbotx.Configuration;
import org.pircbotx.IdentServer;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class Irc extends ListenerAdapter {	
	@Override
	public void onGenericMessage(GenericMessageEvent event) {
        //When someone says ?helloworld respond with "Hello World"
        if (event.getMessage().startsWith("?version")) {
                event.respond("NewsBot v0.1 by Gerardo Canosa!");
        } else if (event.getMessage().startsWith("?news")) {
        	event.respond("News goes here");
        } else if (event.getMessage().startsWith("?quit")) {
        	// nothing yet
        
        }
	}

	public int connect() throws Exception {
        IdentServer.startServer();
        //Configure what we want our bot to do
        Configuration configuration = new Configuration.Builder()
        				.setAutoReconnect(true)
        				.setAutoReconnectAttempts(999)
        				.setAutoReconnectDelay(5)
        				.setRealName("NewsBot")
        				.setIdentServerEnabled(true)
        				.setVersion("NewsBot v0.1 by Gerardo Canosa")
                        .setName("NewsBot") //Set the nick of the bot. CHANGE IN YOUR CODE
                        .addServer("irc.mundochat.com.ar") //Join the freenode network
                        .addAutoJoinChannel("#news") //Join the official #pircbotx channel
                        .addListener(new Irc()) //Add our listener that will be called on Events
                        .buildConfiguration();

        //Create our bot with the configuration
        PircBotX bot = new PircBotX(configuration);
        
        //Connect to the server
        bot.startBot();

        return 0;
	}
}
