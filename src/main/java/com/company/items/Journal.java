package com.company.items;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Journal extends Item {

    public Journal(int itemID, String title, int pages) {
        super.itemID=itemID;
        super.title = title;
        super.pages = pages;
        super.borrowed = false;
    }

}