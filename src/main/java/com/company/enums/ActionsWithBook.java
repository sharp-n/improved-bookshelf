package com.company.enums;

import java.util.Arrays;

public enum ActionsWithBook {

    EXIT_VALUE(0, "Exit"),
    ADD_BOOK(1, "Add book"),
    DELETE_BOOK(2, "Delete book"),
    TAKE_BOOK(3, "Take book"),
    RETURN_BOOK(4, "Return book"),
    SHOW_BOOKS(5, "Show books"),
    DEFAULT(-1,"Default");

    private final int num;
    private final String option;


    ActionsWithBook(int num, String option) {
        this.num = num;
        this.option = option;
    }

    @Override
    public String toString() {
        return num + " - " + option;

    }

    public static ActionsWithBook getByIndex(int index){
        return Arrays
                .stream(values())
                .filter(e -> e.num == index)
                .findFirst()
                .orElseGet(() -> DEFAULT);
    }
}
