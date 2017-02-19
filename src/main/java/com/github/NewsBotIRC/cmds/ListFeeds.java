/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.NewsBotIRC.cmds;

import com.github.NewsBotIRC.IRCMediator;
import com.rometools.rome.io.FeedException;
import java.io.IOException;

/**
 *
 * @author Geronimo
 */
public class ListFeeds implements Cmd
{
    @Override
    public String get()
    {
        return "list";
    }

    @Override
    public void action(IRCMediator m, String params)
    {
        try {
            m.showMessage("--> BEGIN");
            m.listFeeds();
            m.showMessage("--> END");
        } catch (IOException | FeedException e) {
            System.out.println(e.getMessage());
        }
    }
}
