package com.company.enums;

import java.util.Arrays;

public enum FilesMenu {

    EXIT_VALUE(0,"Exit"),
    ONE_FILE(1,"Use one file"),
    TWO_FILES(2,"Use two files"),
    CHANGE_USER(3,"Change user"),
    DEFAULT(-1,"Default");

    private final int num;
    private final String option;

    FilesMenu(int num, String option) {
        this.num = num;
        this.option = option;
    }

    @Override
    public String toString(){
        return num + " - " + option;
    }

    public static FilesMenu getByIndex(int index){
        return Arrays
                .stream(values())
                .filter(e -> e.num == index)
                .findFirst()
                .orElseGet(() -> DEFAULT);
    }
}
