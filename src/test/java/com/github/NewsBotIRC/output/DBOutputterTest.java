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

import com.github.NewsBotIRC.ConfReader;
import com.github.NewsBotIRC.feedreaders.DBEntry;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Geronimo
 */
public class DBOutputterTest
{
    private EntityManagerFactory emFactory = null;
    private final String duplicateLink = "http://www.getall.news";

    public DBOutputterTest()
    {
        Map<String, Object> configOverrides = new HashMap<>();

        ConfReader config = ConfReader.getInstance();
        configOverrides.put("javax.persistence.jdbc.driver",
                config.getDBDriver());
        configOverrides.put("javax.persistence.jdbc.url", config.getDBURL());
        configOverrides.put("javax.persistence.jdbc.user", config.getDBUser());
        configOverrides.put("javax.persistence.jdbc.password",
                config.getDBPassword());

        this.emFactory =
                Persistence.createEntityManagerFactory("com.github.NewsBotIRC",
                        configOverrides);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of save method, of class DBOutputter.
     */
    @Test
    public void testSave()
    {
        System.out.println("save");
        DBOutputter instance = new DBOutputter();

        List<String> categories = Arrays.asList("weird", "science");
        instance.append(new DBEntry("News Title01", this.duplicateLink, "My desc", categories,
                LocalDateTime.of(2017, Month.APRIL, 20, 21, 15, 20)));
        instance.append(new DBEntry("News Title01", this.duplicateLink, "My desc", categories,
                LocalDateTime.of(2017, Month.APRIL, 20, 21, 15, 20)));
        instance.append(new DBEntry("News Title01", this.duplicateLink, "My desc", categories,
                LocalDateTime.of(2017, Month.APRIL, 20, 21, 15, 20)));
        instance.append(new DBEntry("News Title02", "http://www.geronimo.guru", "My little site",
                categories, LocalDateTime.of(2017, Month.APRIL, 20, 21, 20, 30)));

        instance.save();

        assertFalse(this.hasDuplicates());
        assertEquals(2, this.numberOfEntries());
    }

    private boolean hasDuplicates()
    {
        EntityManager em =
                emFactory.createEntityManager();

        Query query = em.createQuery("SELECT COUNT(e) FROM DBEntry e WHERE e.link = '" +
                this.duplicateLink + "'");

        Long duplicates = (Long)query.getSingleResult();

        em.close();

        return duplicates > 1;
    }

    private int numberOfEntries()
    {
        EntityManager em =
                emFactory.createEntityManager();

        Query query = em.createQuery("SELECT COUNT(e) FROM DBEntry e");

        Long n = (Long)query.getSingleResult();

        em.close();

        return n.intValue();
    }
}
