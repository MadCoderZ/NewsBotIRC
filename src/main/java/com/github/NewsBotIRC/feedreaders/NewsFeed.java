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

import java.util.List;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Geronimo
 */
public abstract class NewsFeed implements Cloneable
{
    private String feedURL;
    private boolean breakingNews = false;

    public String getURL()
    {
        return this.feedURL;
    }

    public void setURL(String feedURL)
    {
        this.feedURL = feedURL;
    }

    public boolean isBreakingNews()
    {
        return this.breakingNews;
    }

    public void setBreakingNews(boolean breakingNews)
    {
        this.breakingNews = breakingNews;
    }

    @Override
    public Object clone()
    {
      Object clone = null;

      try {
         clone = super.clone();
      } catch (CloneNotSupportedException e) {
          LogManager.getLogger().error(e.getMessage());
      }

      return clone;
   }

    public abstract boolean isValid();
    public abstract String getTitle();
    public abstract List<NewsEntry> getEntries();
}
