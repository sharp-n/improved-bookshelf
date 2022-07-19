package com.company;

import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Table {

    List<? extends Item> items;
    PrintWriter out;

    private int idColumnWidth;
    private int titleColumnWidth;
    private int authorColumnWidth;
    private int publishingDateColumnWidth;
    private int pagesColumnWidth;
    private int borrowedColumnWidth;

    private static final int NUMBER_OF_SPACES_FOR_PRETTY_COLUMN = 2;

    private static final String NEW_LINE = System.lineSeparator();
    private static final String END_PATTERN = "s";
    private static final String START_PATTERN = "| %-";

    private static final String ITEM_ID = "ITEM ID";
    private static final String TITLE = "TITLE";
    private static final String AUTHOR = "AUTHOR";
    private static final String PUBLISHING_DATE = "PUBLISHING DATE";
    private static final String PAGES = "PAGES";
    private static final String BORROWED = "BORROWED";

    public Table(List<? extends Item> items, PrintWriter out) {
        this.items = items;
        this.out = out;
        if (items.get(0) instanceof Book ||items.get(0) instanceof Journal) {
            this.idColumnWidth = countMaxSymbols(createStringListFromID(), ITEM_ID.length() + NUMBER_OF_SPACES_FOR_PRETTY_COLUMN);
            this.titleColumnWidth = countMaxSymbols(createStringListFromTitle(), TITLE.length() + NUMBER_OF_SPACES_FOR_PRETTY_COLUMN);
            if (items.get(0) instanceof Book) {
                this.authorColumnWidth = countMaxSymbols(createStringListFromAuthor(), AUTHOR.length() + NUMBER_OF_SPACES_FOR_PRETTY_COLUMN);
                this.publishingDateColumnWidth = countMaxSymbols(createStringListFromPublishingDate(), PUBLISHING_DATE.length() + NUMBER_OF_SPACES_FOR_PRETTY_COLUMN);
            }
            this.pagesColumnWidth = countMaxSymbols(createStringListFromPages(), PAGES.length() + NUMBER_OF_SPACES_FOR_PRETTY_COLUMN);
            this.borrowedColumnWidth = BORROWED.length() + NUMBER_OF_SPACES_FOR_PRETTY_COLUMN;
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
        if(items.get(0) instanceof Book || items.get(0) instanceof Journal) {
            String itemIDAndTitleFormat = START_PATTERN + idColumnWidth + END_PATTERN + START_PATTERN + titleColumnWidth + END_PATTERN;
            String pagesAndBorrowedFormat = START_PATTERN + pagesColumnWidth + END_PATTERN + START_PATTERN + borrowedColumnWidth + END_PATTERN;

            out.printf(itemIDAndTitleFormat, NEW_LINE + ITEM_ID, TITLE);
            if (items.get(0) instanceof Book) {
                String bookPartFormat = START_PATTERN + authorColumnWidth + END_PATTERN + START_PATTERN + publishingDateColumnWidth + END_PATTERN;
                out.printf(bookPartFormat, AUTHOR, PUBLISHING_DATE);
            }
            out.printf(pagesAndBorrowedFormat, PAGES, BORROWED);
        }
    }

    public void printHeaderLines(){
        if(items.get(0) instanceof Book || items.get(0) instanceof Journal) {
            out.print(NEW_LINE);

            printLineForOption(idColumnWidth-3, true);
            printLineForOption(titleColumnWidth, true);

            if (items.get(0) instanceof Book) {
                printLineForOption(authorColumnWidth, true);
                printLineForOption(publishingDateColumnWidth, true);
            }

            printLineForOption(pagesColumnWidth, true);
            printLineForOption(borrowedColumnWidth, false);
        }
    }

    private void printLineForOption(int max, boolean printPlus){
        for (int i = 0; i<max+1;i++){
            out.print("-");
        }
        if (printPlus) {printPlus();}
    }

    private List<String> createStringListFromID(){
        return items.stream()
                .map(item ->Integer.toString(item.getItemID()))
                .collect(Collectors.toList());
    }

    private List<String> createStringListFromTitle(){
        return items.stream()
                .map(Item::getTitle)
                .collect(Collectors.toList());
    }

    private List<String> createStringListFromAuthor(){
        return items.stream()
                .map(item->((Book)item).getAuthor())
                .collect(Collectors.toList());
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
        return items.stream()
                .map(item ->Integer.toString(item.getPages()))
                .collect(Collectors.toList());
    }

    private int countMaxSymbols(List<String> stringOptions, int min){
        int max = min;
        int current ;
        for (String option: stringOptions) {
            current = option.length();
            if (current>max) {
                max=current;
            }
        }
        return max + NUMBER_OF_SPACES_FOR_PRETTY_COLUMN;
    }


    private void printID(Item item){
        String regex = "%-" + (idColumnWidth-2) + END_PATTERN;
        out.printf(regex,(item.getItemID()));
    }

    private void printTitle(Item item){
        String regex = createPattern(titleColumnWidth);
        out.printf(regex,(item.getTitle()));
    }

    private void printAuthor(Book book){
        String regex = createPattern(authorColumnWidth);
        out.printf(regex,(book.getAuthor()));
    }

    private void printPublishingDate(Book book){
        String regex = createPattern(publishingDateColumnWidth);
        SimpleDateFormat df = new SimpleDateFormat("dd.M.y");
        out.printf(regex,(df.format(book.getPublishingDate().getTime())));
    }

    private void printPages(Item item){
        String regex = createPattern(pagesColumnWidth);
        out.printf(regex,(item.getPages()));
    }

    private void printBorrowed(Item item){
        String regex =createPattern(borrowedColumnWidth);
        out.printf(regex,(item.isBorrowed()));
    }

    private String createPattern(int width){
        return START_PATTERN + width+ END_PATTERN;
    }

    private void printPlus(){
        out.print("+");
    }

}
