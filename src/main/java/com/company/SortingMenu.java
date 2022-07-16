package com.company;

public enum SortingMenu {

    RETURN_VALUE(0, "Return"),
    ITEM_ID(1, "Item ID"),
    TITLE(2, "Title"),
    PAGES(3, "Pages"),
    AUTHOR(4, "Author"),
    PUBLISHING_DATE(5, "Publishing date");

    private final int num;
    private final String option;

    SortingMenu(int num, String option) {
        this.num = num;
        this.option = option;
    }

    @Override
    public String toString() {
        return num + " - " + option;
    }
}
