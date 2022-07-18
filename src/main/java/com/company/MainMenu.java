package com.company;

import java.util.Arrays;

public enum MainMenu {

    EXIT_VALUE(0, "Exit"),
    ADD_BOOK(1, "Add book"),
    DELETE_BOOK(2, "Delete book"),
    TAKE_BOOK(3, "Take book"),
    RETURN_BOOK(4, "Return boo"),
    SHOW_BOOKS(5, "Show books"),
    ADD_JOURNAL(6, "Add journal"),
    DELETE_JOURNAL(7, "Delete journal"),
    TAKE_JOURNAL(8, "Take journal"),
    RETURN_JOURNAL(9, "Return journal"),
    SHOW_JOURNALS(10, "Show journals"),
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
                .orElseGet(() -> DEFAULT);
    }

}



