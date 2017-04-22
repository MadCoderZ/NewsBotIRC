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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


/**
 *
 * @author Geronimo
 */
@Entity
@Table(name = "NEWS")
public class DBEntry implements NewsEntry, Serializable
{
    private Long id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime ldt;
    private List<String> categories;

    public DBEntry()
    {
    }

    public DBEntry(String title, String link, String description, List<String> categories, LocalDateTime ldt)
    {
        this.title = title;
        this.link = link;
        this.description = description;
        this.categories = categories;
        this.ldt = ldt;
    }

    public DBEntry(NewsEntry entry)
    {
        this.title = entry.getTitle();
        this.link = entry.getLink();
        this.description = entry.getDescription();
        this.ldt = entry.getLocalDateTime();
        this.categories = entry.getCategories();
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Override
    @Type(type="text")
    public String getTitle()
    {
        return this.title;
    }

    @Override
    @Type(type="text")
    @Column(unique=true)
    public String getLink()
    {
        return this.link;
    }

    @Override
    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public void setLink(String link)
    {
        this.link = link;
    }

    @Override
    public LocalDateTime getLocalDateTime()
    {
        return this.ldt;
    }

    @Override
    public void setLocalDateTime(LocalDateTime ldt)
    {
        this.ldt = ldt;
    }

    @Override
    @Type(type="text")
    public String getDescription()
    {
        return this.description;
    }

    @Override
    public void setDescription(String desc)
    {
        this.description = desc;
    }

    @Override
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="Categories")
    public List<String> getCategories()
    {
        return this.categories;
    }

    @Override
    public void setCategories(List<String> categories)
    {
        this.categories = categories;
    }
}
