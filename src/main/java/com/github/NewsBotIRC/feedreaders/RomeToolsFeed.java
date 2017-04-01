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

package com.github.NewsBotIRC.feedreaders;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Geronimo
 */
public class RomeToolsFeed extends NewsFeed
{
    private SyndFeed feed = null;

    public RomeToolsFeed()
    {
    }

    private boolean read()
    {
        SyndFeedInput input = new SyndFeedInput();
        try {
            this.feed = input.build(new XmlReader(new URL( this.getURL() )));
        } catch (MalformedURLException ex) {
            LogManager.getLogger().error(ex.getMessage());
            return false;
        } catch (IOException | IllegalArgumentException | FeedException ex) {
            LogManager.getLogger().error(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean isValid()
    {
        if (this.feed != null) return true;
        return this.read();
    }

    @Override
    public String getTitle()
    {
        if (this.feed == null) this.read();
        return this.feed.getTitleEx().getValue();
    }

    @Override
    public List<NewsEntry> getEntries()
    {
        if (this.feed == null) this.read();
        return this.feed.getEntries().stream()
                .map(e -> new RomeToolsEntry(e))
                .collect(toList());
    }
}
