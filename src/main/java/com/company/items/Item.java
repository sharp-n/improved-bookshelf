package com.company.items;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Item {

    public int itemID;
    public String title;
    public int pages;
    public boolean borrowed;

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

}