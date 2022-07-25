package com.company.enums;

import java.util.Arrays;

public enum ActionsWithNewspapers {
    ADD_NEWSPAPER(1, "Add newspaper"),
    DELETE_NEWSPAPER(2, "Delete newspaper"),
    TAKE_NEWSPAPER(3, "Take newspaper"),
    RETURN_NEWSPAPER(4, "Return newspaper"),
    SHOW_NEWSPAPERS(5, "Show newspapers"),
    DEFAULT(-1,"Default");

    private final int num;
    private final String option;


    ActionsWithNewspapers(int num, String option) {
        this.num = num;
        this.option = option;
    }

    @Override
    public String toString() {
        return num + " - " + option;

    }

    public static ActionsWithNewspapers getByIndex(int index){
        return Arrays
                .stream(values())
                .filter(e -> e.num == index)
                .findFirst()
                .orElseGet(() -> DEFAULT);
    }
}
