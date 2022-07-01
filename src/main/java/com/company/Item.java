package com.company;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Item {

    protected int itemID;
    protected String title;
    protected int pages;
    protected boolean borrowed;

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

}