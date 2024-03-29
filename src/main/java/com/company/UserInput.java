package com.company;

import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.LogManager;


public class UserInput {

    static Logger LOGGER;
    Scanner scan;
    PrintWriter out;

    Validator validator;

    private static final String NEW_LINE = System.lineSeparator();
    private static final String NEW_LINE_WITH_TAB = System.lineSeparator();

    public UserInput(PrintWriter out, Scanner scan) {
        this.out = out;
        this.scan = scan;
        this.validator = new Validator(out);
    }

    public String titleUserInput() {
        out.println("Input title:");
        printWaitingForReplyMessage();
        return scan.nextLine().trim();
    }

    public String publishingUserInput() {
        out.println("Input publishing:");
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
            LOGGER.info(e.getMessage());
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


    public Integer getSortingVar(String sortingMenuText) {
        try {
            out.print(sortingMenuText);
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            printDefaultMessage();
            return null;
        }
    }

    public int getMainMenuVar() {
        try {
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            printDefaultMessage();
            return -1;
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
