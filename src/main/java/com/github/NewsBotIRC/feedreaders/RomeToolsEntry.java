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

import com.github.NewsBotIRC.util.HTMLParser;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Geronimo
 */
public class RomeToolsEntry implements NewsEntry
{
    private final SyndEntry entry;
    private long timestamp;

    public RomeToolsEntry(SyndEntry entry)
    {
        this.entry = entry;
        this.timestamp = 0;
    }

    @Override
    public String getTitle()
    {
        String title = this.entry.getTitle();
        if (title == null) return "";
        return title.replaceAll("\n", "").trim();
    }

    @Override
    public String getLink()
    {
        return this.entry.getLink();
    }

    @Override
    public void setTitle(String title)
    {

    }

    @Override
    public void setLink(String link)
    {
    }

    @Override
    public LocalDateTime getLocalDateTime()
    {
        LocalDateTime ldt;
        Date tmpDate = this.entry.getPublishedDate() != null ?
                this.entry.getPublishedDate() : this.entry.getUpdatedDate();
        if (tmpDate != null)  {
            ldt = LocalDateTime.ofInstant(tmpDate.toInstant(),
                    ZoneId.systemDefault());
            return ldt;
        }
        return LocalDateTime.now();
    }

    @Override
    public void setLocalDateTime(LocalDateTime ldt)
    {
        this.entry.setPublishedDate(Date.from(ldt.toInstant(ZoneOffset.UTC)));
    }

    @Override
    public String getDescription()
    {
        if (this.entry.getDescription() != null) {
            return HTMLParser.stripHTML( this.entry.getDescription().getValue() );
        }
        return "";
    }

    @Override
    public void setDescription(String desc)
    {
    }

    @Override
    public List<String> getCategories()
    {
        return this.entry.getCategories()
                .stream()
                .map(SyndCategory::getName)
                .collect(toList());
    }

    @Override
    public void setCategories(List<String> categories)
    {
    }

    @Override
    public long getDownloadedTimeStamp()
    {
        return this.timestamp;
    }

    @Override
    public void setDownloadedTimeStamp(long timestamp)
    {
        this.timestamp = timestamp;
    }
}
