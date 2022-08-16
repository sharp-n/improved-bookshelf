package com.company.enums;

import java.util.Arrays;

public enum FilesMenu {

    EXIT_VALUE(0,"Exit", ""),
    ONE_FILE(1,"One file", "oneFile"),
    FILE_PER_ITEM(2,"File per type", "filePerType"),
    DATABASE(3,"Database", "database"),
    CHANGE_USER(4,"Change user", ""),
    DEFAULT(-1,"Default", "");

    private final int num;
    private final String option;
    private final String servletParameter;

    FilesMenu(int num, String option, String servletParameter) {
        this.num = num;
        this.option = option;
        this.servletParameter = servletParameter;
    }

    public String getServletParameter() {
        return servletParameter;
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
