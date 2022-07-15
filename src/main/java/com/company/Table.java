package com.company;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Table {

    List<? extends Item> items;
    PrintWriter out;

    private final int idColumnWidth ;
    private final int titleColumnWidth ;
    private final int authorColumnWidth;
    private final int publishingDateColumnWidth;
    private final int pagesColumnWidth;
    private final int borrowedColumnWidth;

    private final String NEW_LINE = "\n\r";
    private final String ITEM_ID = "ITEM ID";
    private final String TITLE = "TITLE";
    private final String AUTHOR = "AUTHOR";
    private final String PUBLISHING_DATE = "PUBLISHING DATE";
    private final String PAGES = "PAGES";
    private final String BORROWED = "BORROWED";

    public Table(List<? extends Item> items, PrintWriter out) {
        this.items = items;
        this.out = out;
        this.idColumnWidth = countMaxSymbols(createStringListFromID(),7);
        this.titleColumnWidth = countMaxSymbols(createStringListFromTitle(),5);
        this.authorColumnWidth = countMaxSymbols(createStringListFromAuthor(),6);
        this.publishingDateColumnWidth = countMaxSymbols(createStringListFromPublishingDate(),16);
        this.pagesColumnWidth = countMaxSymbols(createStringListFromPages(),5);
        this.borrowedColumnWidth = 10;
    }

    public void printTable(){

        printHeader();
        printBody();

    }

    public void printHeader(){
        printItemsOptionsHeader();
        printHeaderLines();
    }

    public void printBody(){


    }

    private void printItemsOptionsHeader(){
        if(items.get(0) instanceof Book || items.get(0) instanceof Journal) {
            String str = "%-" + idColumnWidth + "s| %-" + titleColumnWidth + "s";
            out.printf(str, NEW_LINE + ITEM_ID, TITLE);
            if (items.get(0) instanceof Book) {
                out.printf("| %-" + authorColumnWidth + "s| %-" + publishingDateColumnWidth + "s", AUTHOR, PUBLISHING_DATE);
            }
            out.printf("| %-" + pagesColumnWidth + "s| %-" + borrowedColumnWidth + "s", PAGES, BORROWED);
        }
    }

    public void printHeaderLines(){
        if(items.get(0) instanceof Book || items.get(0) instanceof Journal) {
            out.print(NEW_LINE);

            printLineForOption(countMaxSymbols(createStringListFromID(),7), true);
            printLineForOption(countMaxSymbols(createStringListFromTitle(),5), true);

            if (items.get(0) instanceof Book) {
                printLineForOption(countMaxSymbols(createStringListFromAuthor(),6), true);
                printLineForOption(countMaxSymbols(createStringListFromPublishingDate(),16), true);
            }

            printLineForOption(countMaxSymbols(createStringListFromPages(),5), true);
            printLineForOption(8, false);
        }
    }

    private void printLineForOption(int max, boolean printPlus){
        for (int i = 0; i<max+2;i++){
            out.print("-");
        }
        if (printPlus) {printPlus();}
    }

    private List<String> createStringListFromID(){
        List<String> options = new ArrayList<>();
        for (Item item: items){
            options.add(Integer.toString(item.getItemID()));
        }
        return options;
    }

    private List<String> createStringListFromTitle(){
        List<String> options = new ArrayList<>();
        for (Item item: items){
            options.add(item.getTitle());
        }
        return options;
    }

    private List<String> createStringListFromAuthor(){
        List<String> options = new ArrayList<>();
        for (Item item: items){
            options.add(((Book) item).getAuthor());
        }
        return options;
    }

    private List<String> createStringListFromPublishingDate(){
        List<String> options = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.M.y");
        for (Item item: items){
            options.add(sdf.format(((Book) item).getPublishingDate().getTime()));
        }
        return options;
    }

    private List<String> createStringListFromPages(){
        List<String> options = new ArrayList<>();
        for (Item item: items){
            options.add(Integer.toString(item.getPages()));
        }
        return options;
    }

    private int countMaxSymbols(List<String> stringOptions, int min){
        int max = min;
        int current ;
        for (String option: stringOptions) {
            current = option.length();
            if (current>max) {max=current;}
        }
        return max;
    }


    private void printPlus(){
        out.print("+");
    }


}
