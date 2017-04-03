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
package com.github.NewsBotIRC;

import com.github.NewsBotIRC.ConfReader.Input;
import com.github.NewsBotIRC.output.ConsoleOutputter;
import java.io.File;
import java.net.MalformedURLException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Geronimo
 */
public class NewsReaderTest
{

    String feedPath;

    public NewsReaderTest()
    {
    }

    @Before
    public void setUp()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("slashdotMain").getFile());
        this.feedPath = file.getAbsolutePath();
    }

    /**
     * Test of addFeedUrl method, of class NewsReader.
     * @throws java.lang.Exception
     */
    @Test
    public void testAddFeed() throws Exception
    {
        System.out.println("addFeedUrl");
        NewsReader newsReader = new NewsReader(new ConsoleOutputter());

        assertTrue(newsReader.addFeed(Input.RSS, "file:" + this.feedPath));
        assertFalse(newsReader.addFeed(Input.RSS, "file:/not_valid_feed.xml"));
        assertFalse(newsReader.addFeed(Input.RSS, "file:" + this.feedPath));
    }

    /**
     * Test of removeFeed method, of class NewsReader.
     * @throws java.net.MalformedURLException
     */
    @Test
    public void testRemoveFeed() throws MalformedURLException
    {
        System.out.println("removeFeed");
        NewsReader newsReader = new NewsReader(new ConsoleOutputter());

        newsReader.addFeed(Input.RSS, "file:" + this.feedPath);

        assertTrue(newsReader.removeFeed(0));
        assertFalse(newsReader.removeFeed(0));
    }

    /**
     * Test of getNewsFeeds method, of class NewsReader.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetNewsFeeds() throws Exception
    {
        System.out.println("getNewsFeeds");
        NewsReader newsReader = new NewsReader(new ConsoleOutputter());

        newsReader.addFeed(Input.RSS, "file:" + this.feedPath);
        assertEquals(1, newsReader.getNewsFeeds().size());

        newsReader.removeFeed(0);
        assertTrue(newsReader.getNewsFeeds().isEmpty());
    }
}
