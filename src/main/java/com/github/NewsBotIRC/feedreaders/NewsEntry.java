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

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Geronimo
 */
public interface NewsEntry
{
    public String getTitle();
    public String getLink();
    public String getDescription();
    public List<String> getCategories();
    public LocalDateTime getLocalDateTime();
    public long getDownloadedTimeStamp();
    public void setTitle(String title);
    public void setLink(String link);
    public void setDescription(String desc);
    public void setCategories(List<String> categories);
    public void setLocalDateTime(LocalDateTime ldt);
    public void setDownloadedTimeStamp(long timestamp);
}
