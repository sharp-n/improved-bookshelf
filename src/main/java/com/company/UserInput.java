package com.company;

import com.company.enums.SortingMenu;
import java.io.PrintWriter;
import java.util.*;


public class UserInput {

    Scanner scan;

    Librarian librarian;

    PrintWriter out;

    Validator validator;

    private static final String NEW_LINE = System.lineSeparator();
    private static final String NEW_LINE_WITH_TAB = System.lineSeparator();

    public UserInput(PrintWriter out, Scanner scan) {
        this.out = out;
        this.scan = scan;
        this.validator = new Validator(out);
    }

    public UserInput(Librarian librarian, Scanner scan) {
        this.librarian = librarian;
        this.out = librarian.out;
        this.scan = scan;
        this.validator = new Validator(out);
    }

    public String titleUserInput() {
        out.println("Input title:");
        printWaitingForReplyMessage();
        return scan.nextLine().trim();
    }

    public String usernameInput() {
        out.println(NEW_LINE + "Input your name. If you want to use default file(s) write \"default\". To exit input\"exit\"");
        printWaitingForReplyMessage();
        return scan.nextLine().trim();
    }

    public Integer idUserInput() {
        try {
            out.println("Item ID:");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String authorUserInput() {
        out.println("Author:");
        printWaitingForReplyMessage();
        return scan.nextLine().trim();
    }


    public Integer yearUserInput() {
        try {
            out.println();
            out.println("Date of publish:" + NEW_LINE_WITH_TAB + "Year: ");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer dayUserInput() {
        try {
            out.println("\tDay: ");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer monthUserInput() {
        try {
            out.println("\tMonth: ");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer pagesUsersInput() {
        try {
            out.println("Pages: ");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }


    public Integer getSortingVar() { // TODO enum
        try {

            //TODO create getMenuText method in handlers

            //if (item instanceof Book || item instanceof Journal || item instanceof Newspaper) {
                out.println("Sort by:" + NEW_LINE_WITH_TAB
                        + SortingMenu.ITEM_ID + NEW_LINE_WITH_TAB
                        + SortingMenu.TITLE + NEW_LINE_WITH_TAB
                        + SortingMenu.PAGES);
                //if (item instanceof Book) {
                    out.println(SortingMenu.AUTHOR + NEW_LINE_WITH_TAB + SortingMenu.PUBLISHING_DATE);
                //}
                out.println(SortingMenu.RETURN_VALUE);
                printWaitingForReplyMessage();
            //}
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            printDefaultMessage();
            return null;
        }
    }

    public Integer getMainMenuVar() {
        try {
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            printDefaultMessage();
            return null;
        }
    }

    public Integer getItemMenuVar() {
        try {
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            printDefaultMessage();
            return null;
        }
    }

    public void printSuccessMessage(String item) {
        out.println("The item is successfully " + item);
    }

    public void printDefaultMessage() {
        out.println("Input the proposed option");
    }

    public void printWaitingForReplyMessage() {
        out.println("Waiting for reply...");
    }

}
