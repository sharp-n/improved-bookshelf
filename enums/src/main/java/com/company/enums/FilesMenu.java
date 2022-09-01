package com.company.enums;

import java.util.Arrays;

public enum FilesMenu {

    EXIT_VALUE(0,"Exit", "exit"),
    ONE_FILE(1,"One file", "oneFile"),
    FILE_PER_ITEM(2,"File per type", "filePerType"),
    DATABASE_SQLITE(3,"Database (SQLite)", "databaseSQLite"),
    DATABASE_MYSQL(4,"Database (MySQL)", "databaseMySQL"),
    CHANGE_USER(5,"Change user", "changeUser"),
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
                .orElse(DEFAULT);
    }

    public static FilesMenu getByOption(String option){
        return Arrays
                .stream(values())
                .filter(e -> e.option.equals(option))
                .findFirst()
                .orElse(DEFAULT);
    }

    public static FilesMenu getByDBColumnName(String dbColumn){
        return Arrays
                .stream(values())
                .filter(e -> e.servletParameter.equals(dbColumn))
                .findFirst()
                .orElse(DEFAULT);
    }
}
