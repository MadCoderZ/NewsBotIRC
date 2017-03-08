# NewsBot
### Java IRC Bot with RSS feed reader

This project has been entirely written in Java language, so basically it might run on computers that are able to run Java code.

We focus on handling memory in the best possible way, keep a clean code, and stability/reliability.

Its purpose is to fetch from different sources (RSS and Atom) and publish them on an IRC channel on a specific IRC server.

Everybody is welcome to cooperate, add new functions by creating a branch in GitHub, and also fix issues or propose features or modifications. We listen to the community and we want to keep working on it as a hobby.

## Configuration file

### Enable/disable SSL connection to the IRC Server.
bot.ssl = false

### True or false depending - Do you want the bot to be auto reconnected when connection is lost?
bot.autoreconnect = true

### How many times NewsBot will try to connect.
bot.reconnectattempts = 999

### How much time between RSS poll between each retry in milliseconds.
bot.reconnectdelay = 5000

### Realname
bot.realname = BOT_NAME

### Username
bot.login = BOT_USERNAME

### Nickname
bot.nick = NewsBot

### Do not modify this line - Last NewsBot version.
bot.version = NewsBot v0.1.2 by Gerardo Canosa and Geronimo Poppino (2016-2017)

### IRC server hostname / IP address.
bot.ircserver = IRC_HOST

### IRC server port where we should connect.
bot.port = PORT_NUMBER

### Do not add the COMMENT # character prefix, just the channel name. The java application will auto add it
bot.channel = CHANNEL_NAME_TO_JOIN

### How often fetch RSS feeds list ? 180s as minimum. recommended to avoid throttling rss feeds.
rss.pollfrequency = 180

### List of RSS feeds you want to fetch.
rss.feed = RSS_URL_FEED_1
rss.feed = RSS_URL_FEED_2
rss.feed = RSS_URL_AS_MUCH_AS_WANTED

## Authors

Gerardo Canosa [gera.canosa@gmail.com](mailto:gera.canosa@gmail.com) EtherNet@irc.mundochat.com.ar

Geronimo Poppino [<>](mailto:blah@bleh.com) Geronimo@irc.mundochat.com.ar