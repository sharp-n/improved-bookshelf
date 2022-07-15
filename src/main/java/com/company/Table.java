package com.company;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Table {

    List<? extends Item> items;
    PrintWriter out;

    private int idColumnWidth;
    private int titleColumnWidth;
    private int authorColumnWidth;
    private int publishingDateColumnWidth;
    private int pagesColumnWidth;
    private int borrowedColumnWidth;

    private static final String NEW_LINE = "\n\r";
    private static final String END_START_PATTERN = "s| %-";

    public Table(List<? extends Item> items, PrintWriter out) {
        this.items = items;
        this.out = out;
        if (items.get(0) instanceof Book||items.get(0) instanceof Journal) {
            this.idColumnWidth = countMaxSymbols(createStringListFromID(), 10);
            this.titleColumnWidth = countMaxSymbols(createStringListFromTitle(), 5);
            if (items.get(0) instanceof Book) {
                this.authorColumnWidth = countMaxSymbols(createStringListFromAuthor(), 6);
            }
            this.publishingDateColumnWidth = countMaxSymbols(createStringListFromPublishingDate(), 16);
            this.pagesColumnWidth = countMaxSymbols(createStringListFromPages(), 6);
            this.borrowedColumnWidth = 10;
        }
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
        for (Item item : items) {
            out.print(NEW_LINE);
            if (item instanceof Book|| item instanceof Journal) {
                printID(item);
                printTitle(item);
                if (item instanceof Book) {
                    printAuthor((Book) item);
                    printPublishingDate((Book) item);
                }
                printPages(item);
                printBorrowed(item);
            }
        }
    }

    private void printItemsOptionsHeader(){
        final String ITEM_ID = "ITEM ID";
        final String TITLE = "TITLE";
        final String AUTHOR = "AUTHOR";
        final String PUBLISHING_DATE = "PUBLISHING DATE";
        final String PAGES = "PAGES";
        final String BORROWED = "BORROWED";
        if(items.get(0) instanceof Book || items.get(0) instanceof Journal) {
            String itemIDAndTitleFormat = " %-" + idColumnWidth + END_START_PATTERN + titleColumnWidth + "s";
            String pagesAndBorrowedFormat = "| %-" + pagesColumnWidth + END_START_PATTERN + borrowedColumnWidth + "s";

            out.printf(itemIDAndTitleFormat, NEW_LINE + ITEM_ID, TITLE);
            if (items.get(0) instanceof Book) {
                String bookPartFormat = "| %-" + authorColumnWidth + END_START_PATTERN + publishingDateColumnWidth + "s";
                out.printf(bookPartFormat, AUTHOR, PUBLISHING_DATE);
            }
            out.printf(pagesAndBorrowedFormat, PAGES, BORROWED);
        }
    }

    public void printHeaderLines(){
        if(items.get(0) instanceof Book || items.get(0) instanceof Journal) {
            out.print(NEW_LINE);

            printLineForOption(idColumnWidth-4, true);
            printLineForOption(titleColumnWidth-1, true);

            if (items.get(0) instanceof Book) {
                printLineForOption(authorColumnWidth-1, true);
                printLineForOption(publishingDateColumnWidth-1, true);
            }

            printLineForOption(pagesColumnWidth-1, true);
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


    private void printID(Item item){
        String regex = "%-" + (idColumnWidth-2) + "s";
        out.printf(regex,(item.getItemID()));
    }

    private void printTitle(Item item){
        String regex = "| %-" + titleColumnWidth + "s";
        out.printf(regex,(item.getTitle()));
    }

    private void printAuthor(Book book){
        String regex = "| %-" + authorColumnWidth + "s";
        out.printf(regex,(book.getAuthor()));
    }

    private void printPublishingDate(Book book){
        String regex = "| %-" + publishingDateColumnWidth + "s";
        SimpleDateFormat df = new SimpleDateFormat("dd.M.y");
        out.printf(regex,(df.format(book.getPublishingDate().getTime())));
    }

    private void printPages(Item item){
        String regex = "| %-" + pagesColumnWidth + "s";
        out.printf(regex,(item.getPages()));
    }

    private void printBorrowed(Item item){
        String regex = "| %-" + borrowedColumnWidth + "s";
        out.printf(regex,(item.isBorrowed()));
    }


    private void printPlus(){
        out.print("+");
    }

}
