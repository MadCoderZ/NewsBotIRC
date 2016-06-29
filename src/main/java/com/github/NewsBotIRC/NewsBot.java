package com.github.NewsBotIRC;

public class NewsBot
{
	public static void main(String[] args)
	{
            System.out.println("NewsBot v0.1 by Gerardo Canosa and Geronimo Poppino");
            new IRCMediator().start();
	}
}