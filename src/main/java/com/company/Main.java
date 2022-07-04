package com.company;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private final static int ONE_FILE = 1;
    private final static int TWO_FILES = 2;

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
    private final static int EXIT_VALUE = 0;

    public static void main(String[] args) throws IOException {

        Dialogues.setScan(new Scanner(System.in));
        boolean filesValue = true;
        boolean value = true;
        boolean validUserName = false;
        while (filesValue) {

            String userName = "";
            Dialogues dialogue = new Dialogues();

            while(!validUserName) {
                userName = dialogue.usernameValidation(dialogue.usernameInput());
                if (userName != null) validUserName = true;
            }

            User user = new User(userName);
            System.out.println("\n0 - Exit\n1 - Use one file\n2 - Use two files");
            int filesVar = Dialogues.getMainMenuVar();

            Librarian librarian = new Librarian();
            switch (filesVar) {
                case ONE_FILE:
                    WorkWithFiles.setSingleFilePath(user.userName);
                    librarian = new Librarian(WorkWithFiles.SINGLE_FILE_PATH);
                    System.out.println("Your items will be saved in one file");
                    break;
                case TWO_FILES:
                    WorkWithFiles.setBookFilePath(user.userName);
                    WorkWithFiles.setJournalsFilePath(user.userName);
                    System.out.println("Your items will be saved in different files");
                    break;
                case EXIT_VALUE:
                    filesValue = false;
                    value = false;
                    break;
                default:
                    Dialogues.printDefaultMessage();
                    break;
            }

            while (value) {

                System.out.println("\n\t\t0 - Exit" +
                        "\n1 - Add book\t6 - Add journal" +
                        "\n2 - Delete book\t7 - Delete journal" +
                        "\n3 - Take book\t8 - Take journal" +
                        "\n4 - Return book\t9 - Return journal" +
                        "\n5 - Show books\t10 - Show journals");

                Dialogues bookDialogue;
                Dialogues journalDialogue;
                if (librarian.filePath == null) {
                    bookDialogue = new Dialogues(new Book(), new Librarian(WorkWithFiles.BOOK_FILE_PATH));
                    journalDialogue = new Dialogues(new Journal(), new Librarian(WorkWithFiles.JOURNALS_FILE_PATH));
                } else {
                    bookDialogue = new Dialogues(new Book(), librarian);
                    journalDialogue = new Dialogues(new Journal(), librarian);
                }

                int var = Dialogues.getMainMenuVar();

                switch (var) {

                    case ADD_BOOK:
                        bookDialogue.addingDialogue();
                        break;

                    case DELETE_BOOK:
                        bookDialogue.deletingDialogue();
                        break;

                    case TAKE_BOOK:
                        bookDialogue.borrowingDialogue(true);
                        break;

                    case RETURN_BOOK:
                        bookDialogue.borrowingDialogue(false);
                        break;

                    case SHOW_BOOKS:
                        bookDialogue.sortingDialogue();
                        break;

                    case ADD_JOURNAL:
                        journalDialogue.addingDialogue();
                        break;

                    case DELETE_JOURNAL:
                        journalDialogue.deletingDialogue();
                        break;

                    case TAKE_JOURNAL:
                        journalDialogue.borrowingDialogue(true);
                        break;

                    case RETURN_JOURNAL:
                        journalDialogue.borrowingDialogue(false);
                        break;

                    case SHOW_JOURNALS:
                        journalDialogue.sortingDialogue();
                        break;

                    case EXIT_VALUE:
                        value = false;
                        break;

                    default:
                        Dialogues.printDefaultMessage();
                        break;
                }
            }
        }
    }
}