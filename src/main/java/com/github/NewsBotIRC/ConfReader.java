package com.github.NewsBotIRC;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author  Written by Gerardo Canosa
 */
public class ConfReader
{
    private static ConfReader instance = null;
    private static Configuration appProperties = null;

    private boolean autoreconnect = false;
    private int reconnectattempts;
    private int reconnectDelay;
    private String realname = null;
    private String nick = null;
    private String login = null;
    private String version = null;
    private String ircserver = null;
    private String channel = null;
    private final int pollfrequency;
    private int port = 0;
    private String[] rssUrls = null;
    private boolean ssl = false;
    private boolean identify = false;
    private String nickserv_passwd = null;
    private String DBURL = null;
    private String DBUser = null;
    private String DBPassword = null;
    private String DBDriver = null;
    private final int jsonMaxResults;
    private final int DBLimit;

    private CompositeConfiguration config = null;

    public enum Input {
        RSS, DB
    }

    public enum Output {
        IRC, JSON, CONSOLE, DB
    }

    private Output output = null;
    private Input input = null;

    protected ConfReader()
    {
        this.config = new CompositeConfiguration();

        String propFilename = "newsbot.properties";
        if (System.getenv().get("TESTING_PULL_REQUEST") != null) {
            propFilename = "newsbot.properties-for-pull-requests";
        }

        try {
            this.config.addConfiguration(
                    new PropertiesConfiguration(propFilename));
        } catch (ConfigurationException e) {
            LogManager.getLogger(ConfReader.class).error(e.getMessage());
            System.exit(1);
        }

        this.autoreconnect = config.getBoolean("bot.autoreconnect");
        this.reconnectattempts = config.getInt("bot.reconnectattempts");
        this.reconnectDelay = config.getInt("bot.reconnectdelay");
        this.realname = config.getString("bot.realname");
        this.nick = config.getString("bot.nick");
        this.login = config.getString("bot.login");
        this.version = config.getString("bot.version");
        this.port = config.getInt("bot.port");
        this.ircserver = config.getString("bot.ircserver");
        this.channel = config.getString("bot.channel");
        this.pollfrequency = config.getInt("rss.pollfrequency");
        this.ssl = config.getBoolean("bot.ssl");
        this.identify = config.getBoolean("bot.identify");
        this.nickserv_passwd = config.getString("bot.nickserv_passwd");
        this.input = Input.valueOf(config.getString("bot.input"));
        this.output = Output.valueOf(config.getString("bot.output"));
        this.rssUrls = config.getStringArray("rss.feed");
        this.DBURL = config.getString("bot.db.url");
        this.DBUser = config.getString("bot.db.user");
        this.DBPassword = config.getString("bot.db.password");
        this.DBDriver = config.getString("bot.db.driver");
        this.jsonMaxResults = config.getInt("bot.json.max_results", 30);
        this.DBLimit = config.getInt("bot.db.limit", 30);
    }

    public static ConfReader getInstance()
    {
        if (instance == null) instance = new ConfReader();
        return instance;
    }

    public static Configuration getAppProperties()
    {
        if (appProperties == null) {
            try {
                appProperties = new PropertiesConfiguration(
                    ConfReader.class.getClass().getResource("/application.properties"));
            } catch (ConfigurationException e) {
                System.out.println(e.getMessage());
            }
        }
        return appProperties;
    }

    public boolean isSSL()
    {
        return this.ssl;
    }

    public String[] getRssUrls()
    {
        return rssUrls;
    }

    public boolean isAutoreconnect()
    {
        return this.autoreconnect;
    }

    public void setAutoreconnect(boolean autoreconnect)
    {
        this.autoreconnect = autoreconnect;
    }

    public int getReconnectattempts()
    {
        return this.reconnectattempts;
    }

    public void setReconnectattempts(int reconnectattempts)
    {
        this.reconnectattempts = reconnectattempts;
    }

    public int getDelaybetweenretries()
    {
        return this.reconnectDelay;
    }

    public void setReconnectDelay(int reconnectDelay)
    {
        this.reconnectDelay = reconnectDelay;
    }

    public String getRealname()
    {
        return this.realname;
    }

    public void setRealname(String realname)
    {
        this.realname = realname;
    }

    public String getNick()
    {
        return this.nick;
    }

    public void setNick(String nick)
    {
        this.nick = nick;
    }

    public String getVersion()
    {
        return this.version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getIrcserver()
    {
        return this.ircserver;
    }

    public void setIrcserver(String ircserver)
    {
        this.ircserver = ircserver;
    }

    public String getChannel()
    {
        return this.channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public CompositeConfiguration getConfig()
    {
        return this.config;
    }

    public void setConfig(CompositeConfiguration config)
    {
        this.config = config;
    }

    public String getLogin()
    {
        return this.login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public int getPollFrequency()
    {
        return this.pollfrequency;
    }

    public int getPort()
    {
        return this.port;
    }

    public void setIdentify(boolean identify)
    {
        this.identify = identify;
    }

    public void setNickserv_passwd(String nickserv_passwd)
    {
        this.nickserv_passwd = nickserv_passwd;
    }

    public boolean isIdentifyEnabled()
    {
        return identify;
    }

    public String getNickserv_passwd()
    {
        return nickserv_passwd;
    }

    public Input getInput()
    {
        return this.input;
    }

    public Output getOutput()
    {
        return this.output;
    }

    public String getDBURL()
    {
        return this.DBURL;
    }

    public String getDBUser()
    {
        return this.DBUser;
    }

    public String getDBPassword()
    {
        return this.DBPassword;
    }

    public String getDBDriver()
    {
        return this.DBDriver;
    }

    public int getJsonMaxResults()
    {
        return this.jsonMaxResults;
    }

    public int getDBLimit()
    {
        return this.DBLimit;
    }
}
