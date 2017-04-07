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
import com.github.NewsBotIRC.feedreaders.NewsEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Geronimo
 */
public class DBOutputter implements Outputter
{
    private final EntityManagerFactory entityManagerFactory;
    private final List<NewsEntry> entries;

    public DBOutputter()
    {
        Map<String, Object> configOverrides = new HashMap<>();

        ConfReader config = ConfReader.getInstance();
        configOverrides.put("javax.persistence.jdbc.driver",
                config.getDBDriver());
        configOverrides.put("javax.persistence.jdbc.url", config.getDBURL());
        configOverrides.put("javax.persistence.jdbc.user", config.getDBUser());
        configOverrides.put("javax.persistence.jdbc.password",
                config.getDBPassword());

        entityManagerFactory =
                Persistence.createEntityManagerFactory("com.github.NewsBotIRC",
                        configOverrides);
        this.entries = new ArrayList<>();
    }

    @Override
    public void append(NewsEntry entry)
    {
        this.entries.add(entry);
    }

    @Override
    public String getOutput()
    {
        return "Only appends and saves data";
    }

    @Override
    public void save()
    {
        if (this.entries.isEmpty()) return;

        EntityManager entityManager =
                entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        this.entries.forEach(e ->
                entityManager.persist(
                        new DBEntry(e.getTitle(), e.getLink(),
                                e.getLocalDateTime())));
        entityManager.getTransaction().commit();
        entityManager.close();
        this.entries.clear();
    }
}
