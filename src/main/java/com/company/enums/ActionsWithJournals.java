package com.company.enums;

import java.util.Arrays;

public enum ActionsWithJournals {
    ADD_JOURNAL(1, "Add journal"),
    DELETE_JOURNAL(2, "Delete journal"),
    TAKE_JOURNAL(3, "Take journal"),
    RETURN_JOURNAL(4, "Return journal"),
    SHOW_JOURNALS(5, "Show journals"),
    DEFAULT(-1,"Default");

    private final int num;
    private final String option;


    ActionsWithJournals(int num, String option) {
        this.num = num;
        this.option = option;
    }

    @Override
    public String toString() {
        return num + " - " + option;

    }

    public static ActionsWithJournals getByIndex(int index){
        return Arrays
                .stream(values())
                .filter(e -> e.num == index)
                .findFirst()
                .orElseGet(() -> DEFAULT);
    }
}
