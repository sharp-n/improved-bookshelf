package com.company.enums;

import java.util.Arrays;

public enum ActionsWithItem {
    EXIT_VALUE(0, "Exit"),
    ADD(1, "Add"),
    DELETE(2, "Delete"),
    TAKE(3, "Take"),
    RETURN(4, "Return"),
    SHOW(5, "Show"),
    DEFAULT(-1,"Default");

    private final int num;
    private final String option;


    ActionsWithItem(int num, String option) {
        this.num = num;
        this.option = option;
    }

    @Override
    public String toString() {
        return num + " - " + option;

    }

    public static ActionsWithItem getByIndex(int index){
        return Arrays
                .stream(values())
                .filter(e -> e.num == index)
                .findFirst()
                .orElseGet(() -> DEFAULT);
    }
}
