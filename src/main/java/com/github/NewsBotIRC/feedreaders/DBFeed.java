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

import com.github.NewsBotIRC.ConfReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


public class DBFeed extends NewsFeed
{
    private int offset;
    private final int limit;
    private EntityManagerFactory entityManagerFactory = null;

    public DBFeed()
    {
        this.offset = 0;
        this.limit = ConfReader.getInstance().getDBLimit();
    }

    private EntityManager getEntityManager()
    {
        if (this.entityManagerFactory == null) {
            Map<String, Object> configOverrides = new HashMap<>();

            ConfReader config = ConfReader.getInstance();
            configOverrides.put("javax.persistence.jdbc.driver",
            config.getDBDriver());
            configOverrides.put("javax.persistence.jdbc.url",
                    config.getDBURL());
            configOverrides.put("javax.persistence.jdbc.user",
                    config.getDBUser());
            configOverrides.put("javax.persistence.jdbc.password",
                    config.getDBPassword());

            entityManagerFactory =
                    Persistence.createEntityManagerFactory("com.github.NewsBotIRC",
                            configOverrides);
        }

        return this.entityManagerFactory.createEntityManager();
    }

    @Override
    public boolean isValid()
    {
        return true;
    }

    @Override
    public String getTitle()
    {
        return "DB Feed";
    }

    @Override
    public Set<NewsEntry> getEntries()
    {
        EntityManager entityManager = this.getEntityManager();

	entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("from DBEntry");
        query.setFirstResult(this.offset);
        query.setMaxResults(this.limit);

        List<NewsEntry> result = query.getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();

        this.offset += result.size();

        return new TreeSet(result);
    }
}
