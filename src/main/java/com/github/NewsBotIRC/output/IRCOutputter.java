/*
 * The MIT License
 *
 * Copyright 2017 Geronimo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.NewsBotIRC.output;

import com.github.NewsBotIRC.IRCMediator;
import com.github.NewsBotIRC.util.URLShortener;
import com.github.NewsBotIRC.feedreaders.NewsEntry;
import com.github.NewsBotIRC.util.LocalDateTimeAdjuster;
import org.pircbotx.Colors;

/**
 *
 * @author Geronimo
 */
public class IRCOutputter implements Outputter
{

    public IRCOutputter()
    {
    }

    @Override
    public void append(NewsEntry entry)
    {
        String timeAgo = LocalDateTimeAdjuster.timeAgo(entry.getLocalDateTime());
        if (entry.getLink() != null) {
            String domain = entry.getLink().replaceFirst(".*https?://([\\w.-]+)/.*",
                                                                        "<$1>");
            String link = URLShortener.shortenUrl(entry.getLink());

            IRCMediator.getInstance().sendMessage("\"" + Colors.DARK_GRAY
                 + entry.getTitle() + Colors.NORMAL + "\" " + timeAgo + " "
                 + Colors.ITALICS + domain + Colors.NORMAL + " <" + link + ">");
        } else {
            IRCMediator.getInstance().sendMessage("\"" + Colors.DARK_GRAY
                 + entry.getTitle() + Colors.NORMAL + "\" " + timeAgo);
        }
    }

    @Override
    public String getOutput()
    {
        return "Only appends data";
    }

    @Override
    public void save()
    {
    }
}
