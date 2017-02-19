package com.github.NewsBotIRC.cmds;

import com.github.NewsBotIRC.IRCMediator;

/**
 *
 * @author Geronimo
 */
public interface Cmd {

    public String get();
    public void action(IRCMediator m, String params);

}
