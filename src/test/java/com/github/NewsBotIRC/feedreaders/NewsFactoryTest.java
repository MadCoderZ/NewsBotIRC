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

import com.github.NewsBotIRC.ConfReader.Input;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Geronimo
 */
public class NewsFactoryTest
{
    String feedPath;

    public NewsFactoryTest()
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
     * Test of createFeed method, of class NewsFactory.
     */
    @Test
    public void testCreateFeed()
    {
        System.out.println("createFeed");
        NewsFeed feed = NewsFactory.getInstance().createFeed(Input.RSS, "");
        assertNotNull(feed);
        assertFalse(feed.isValid());

        feed = NewsFactory.getInstance().createFeed(Input.DB, "");
        assertNotNull(feed);
        assertTrue(feed.isValid());

        feed = NewsFactory.getInstance().createFeed(Input.RSS,
                "file://" + this.feedPath);
        assertNotNull(feed);
        assertTrue(feed.isValid());
    }
}
