package com.company;

import lombok.AllArgsConstructor;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Table {

    List<? extends Item> items;
    PrintWriter out;

    private final String NEW_LINE = "\n\r";
    private final String ITEM_ID = "ITEM ID";
    private final String TITLE = "TITLE";
    private final String AUTHOR = "AUTHOR";
    private final String PUBLISHING_DATE = "PUBLISHING DATE";
    private final String PAGES = "PAGES";
    private final String BORROWED = "BORROWED";

    public void printHeader(){
        printItemsOptionsHeader();
        printHeaderLines();
    }

    private void printItemsOptionsHeader(){
        if(items.get(0) instanceof Book || items.get(0) instanceof Journal) {
            String str = "% -12s| %-40s";
            out.printf(str, NEW_LINE + ITEM_ID, TITLE);
            if (items.get(0) instanceof Book) {
                out.printf("| %-32s| %-18s", AUTHOR, PUBLISHING_DATE);
            }
            out.printf("| %-8s| %-30s", PAGES, BORROWED);
        }
    }

    public void printHeaderLines(){
        if(items.get(0) instanceof Book || items.get(0) instanceof Journal) {
            out.print(NEW_LINE);

            printLineForOption(countMaxSymbols(createStringListFromID()), true);
            printLineForOption(countMaxSymbols(createStringListFromTitle()), true);

            if (items.get(0) instanceof Book) {
                printLineForOption(countMaxSymbols(createStringListFromAuthor()), true);
                printLineForOption(countMaxSymbols(createStringListFromPublishingDate()), true);
            }

            printLineForOption(countMaxSymbols(createStringListFromPages()), true);
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

    private int countMaxSymbols(List<String> stringOptions){
        int max = 0;
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
