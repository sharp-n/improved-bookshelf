package com.company.items;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
public class Item {

    public int itemID;
    public String title;
    public int pages;
    public boolean borrowed;

    public Item(int itemID, String title, int pages) {
        this.itemID = itemID;
        this.title = title;
        this.pages = pages;
        this.borrowed = false;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

}