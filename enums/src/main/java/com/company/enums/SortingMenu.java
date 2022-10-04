package com.company.enums;

import java.util.Arrays;

public enum SortingMenu {

    RETURN_VALUE(0, "Return",""),
    ITEM_ID(1, "Item ID","item_id"),
    TITLE(2, "Title","title"),
    PAGES(3, "Pages","pages"),
    AUTHOR(4, "Author","author"),
    PUBLISHING_DATE(5, "Publishing Date","publishing_date"),
    PUBLISHER(6, "Publisher","publisher"),
    DEFAULT(-1, "Default","");

    private final int num;
    private final String option;
    private final String parameter;

    SortingMenu(int num, String option, String parameter) {
        this.num = num;
        this.option = option;
        this.parameter = parameter;
    }

    public String getOption() {
        return option;
    }

    public String getDbColumn() {
        return parameter;
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
    public static SortingMenu getByParameter(String parameter){
        return Arrays
                .stream(values())
                .filter(e -> e.parameter.equalsIgnoreCase(parameter))
                .findFirst()
                .orElse(DEFAULT);
    }

}
