package com.company;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private final static int ADD_BOOK = 1;
    private final static int DELETE_BOOK = 2;
    private final static int TAKE_BOOK = 3;
    private final static int RETURN_BOOK = 4;
    private final static int SHOW_BOOKS = 5;
    private final static int ADD_JOURNAL = 6;
    private final static int DELETE_JOURNAL = 7;
    private final static int TAKE_JOURNAL = 8;
    private final static int RETURN_JOURNAL = 9;
    private final static int SHOW_JOURNALS = 10;
    private final static int EXIT_VALUE = 11;

    public static void main(String[] args) throws IOException {

        System.out.println(Arrays.toString(args));
        boolean value = true;
        while (value) {

            System.out.println("""
                    
                    1 - Add book\t6 - Add journal
                    2 - Delete book\t7 - Delete journal
                    3 - Take book\t8 - Take journal
                    4 - Return book\t9 - Return journal
                    5 - Show books\t10 - Show journals
                    \t\t11 - Exit""");

            Dialogues.setScan(new Scanner(System.in));
            Dialogues bookDialogue = new Dialogues(new Book());
            Dialogues journalDialogue = new Dialogues(new Journal());
            int var = Dialogues.getMainMenuVar();
            value = switch (var) {

                case ADD_BOOK:
                    bookDialogue.addingDialogue();
                    yield true;

                case DELETE_BOOK:
                    bookDialogue.deletingDialogue();
                    yield true;

                case TAKE_BOOK:
                    bookDialogue.borrowingDialogue(true);
                    yield true;

                case RETURN_BOOK:
                    bookDialogue.borrowingDialogue(false);
                    yield true;

                case SHOW_BOOKS:
                    bookDialogue.sortingDialogue();
                    yield true;

                case ADD_JOURNAL:
                    journalDialogue.addingDialogue();
                    yield true;

                case DELETE_JOURNAL:
                    journalDialogue.deletingDialogue();
                    yield true;

                case TAKE_JOURNAL:
                    journalDialogue.borrowingDialogue(true);
                    yield true;

                case RETURN_JOURNAL:
                    journalDialogue.borrowingDialogue(false);
                    yield true;

                case SHOW_JOURNALS:
                    journalDialogue.sortingDialogue();
                    yield true;

                case EXIT_VALUE: yield false;

                default:
                    Dialogues.printDefaultMessage();
                    yield true;
            };
        }
    }
}