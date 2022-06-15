package com.company;

public class Item {

    protected int itemID;
    protected String title;
    protected int pages;
    protected boolean borrowed;

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public int getItemID() {
        return itemID;
    }

    public String getTitle() {
        return title;
    }

    public int getPages() {
        return pages;
    }

    public boolean isBorrowed() {
        return borrowed;
    }
}