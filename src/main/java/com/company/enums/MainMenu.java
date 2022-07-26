package com.company.enums;

import java.util.Arrays;

public enum MainMenu {

    EXIT_VALUE(0, "Exit"),
    BOOK(1, "Book"),
    JOURNAL(2, "Journal"),
    NEWSPAPER(3, "Newspaper"),
    DEFAULT(-1,"Default");

    private final int num;
    private final String option;


    MainMenu(int num, String option) {
        this.num = num;
        this.option = option;
    }

    @Override
    public String toString() {
        return num + " - " + option;

    }

    public static MainMenu getByIndex(int index){
        return Arrays
                .stream(values())
                .filter(e -> e.num == index)
                .findFirst()
                .orElse(DEFAULT);
    }

}



