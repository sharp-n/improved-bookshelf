package com.company;


public class Journal extends Item {

    private static final String filePath = "journals.txt";

    public Journal() {
    }

    public Journal(int itemID, String title, int pages) {
        super.itemID=itemID;
        super.title = title;
        super.pages = pages;
        super.borrowed = false;
    }

    public static String getFilePath() {
        return filePath;
    }

}