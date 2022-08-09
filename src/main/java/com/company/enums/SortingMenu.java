package com.company.enums;

import java.util.Arrays;

public enum SortingMenu {

    RETURN_VALUE(0, "Return"),
    ITEM_ID(1, "Item ID"),
    TITLE(2, "Title"),
    PAGES(3, "Pages"),
    AUTHOR(4, "Author"),
    PUBLISHING_DATE(5, "Publishing Date"),
    PUBLISHING(6, "Publishing"),
    DEFAULT(-1, "DEFAULT");

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

    public static SortingMenu getByIndex(int index){
        return Arrays
                .stream(values())
                .filter(e -> e.num == index)
                .findFirst()
                .orElse(DEFAULT);
    }


    public static SortingMenu getByOption(String option){
        return Arrays
                .stream(values())
                .filter(e -> e.option.replace(" ", "").equalsIgnoreCase(option))
                .findFirst()
                .orElse(DEFAULT);
    }
}
