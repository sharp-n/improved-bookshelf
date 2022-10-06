package com.company.db.entities;

import com.company.db.entities.User;
import jdk.nashorn.internal.objects.annotations.Property;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "items")
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false)
    @Basic(optional = false)
    private Long id;

    @Column(name = "type_of_item",unique = true,nullable = false)
    @Basic(optional = false)
    private String typeOfItem;

    @Column(name = "title",unique = true,nullable = false)
    @Basic(optional = false)
    private String title;

    @Property(name = "author")
    private String author;

    @Property(name = "publishing_date")
    private Date publishingDate;

    @Property(name = "publisher")
    private String publisher;

    @Column(name = "pages",unique = true,nullable = false)
    @Basic(optional = false)
    private int pages;

    @Property(name = "borrowed")
    private boolean borrowed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Item(String typeOfItem, String title, String author, java.util.Date publishingDate, int pages, boolean borrowed, User user) {
        this.typeOfItem = typeOfItem;
        this.title = title;
        this.author = author;
        this.publishingDate = new Date(publishingDate.getTime());
        this.pages = pages;
        this.borrowed = borrowed;
        this.user = user;
    }

    public Item(String typeOfItem, String title, int pages, boolean borrowed, User user) {
        this.typeOfItem = typeOfItem;
        this.title = title;
        this.pages = pages;
        this.borrowed = borrowed;
        this.user = user;
    }

    public Item(String typeOfItem, String title, String publisher, int pages, boolean borrowed, User user) {
        this.typeOfItem = typeOfItem;
        this.title = title;
        this.publisher = publisher;
        this.pages = pages;
        this.borrowed = borrowed;
        this.user = user;
    }

}
