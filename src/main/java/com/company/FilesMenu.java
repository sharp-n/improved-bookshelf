package com.company;

public enum FilesMenu {


    EXIT_VALUE(0,"Exit"),
    ONE_FILE(1,"Use one file"),
    TWO_FILES(2,"Use two files"),
    CHANGE_USER(3,"Change user");

    private int num;
    private String option;


    FilesMenu(int num, String option) {
        this.num = num;
        this.option = option;
    }

    @Override
    public String toString(){
        return num + " - " + option;
    }


}
