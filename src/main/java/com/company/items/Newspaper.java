package com.company.items;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Newspaper extends Item {

    public Newspaper(int itemID, String title, int pages) {
        super.itemID=itemID;
        super.title = title;
        super.pages = pages;
        super.borrowed = false;
    }

}