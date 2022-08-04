package com.company.enums;

import java.io.File;
import java.util.Arrays;

public enum FilesMenu {

    EXIT_VALUE(0,"Exit"),
    ONE_FILE(1,"oneFile"),
    FILE_PER_ITEM(2,"filePerType"),
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

    public static FilesMenu getByOption(String option){
        return Arrays
                .stream(values())
                .filter(e -> e.option.equals(option))
                .findFirst()
                .orElse(DEFAULT);
    }
}
