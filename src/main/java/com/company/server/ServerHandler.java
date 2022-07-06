package com.company.server;

import com.company.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ServerHandler {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    final static int ADD_BOOK = 1;
    final static int DELETE_BOOK = 2;
    final static int TAKE_BOOK = 3;
    final static int RETURN_BOOK = 4;
    final static int SHOW_BOOKS = 5;
    final static int ADD_JOURNAL = 6;
    final static int DELETE_JOURNAL = 7;
    final static int TAKE_JOURNAL = 8;
    final static int RETURN_JOURNAL = 9;
    final static int SHOW_JOURNALS = 10;

    private static Dialogues bookDialogue;
    private static Dialogues journalDialogue;

    static void handle(Scanner in, PrintWriter out, Scanner send) throws IOException {

        String name = in.nextLine();
        User user = new User(name);
        int numOfFiles = Integer.parseInt(in.nextLine());

        Librarian librarian = createLibrarian(numOfFiles, user);

        manageDialogues(librarian);

        int var = Integer.parseInt(in.nextLine());

        switch (var) {

            case ADD_BOOK:
                Book bookToAdd = gson.fromJson(in.nextLine(),Book.class);
                librarian.addItem(bookToAdd);
                out.println("Added");
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
                Journal journalToAdd = gson.fromJson(in.nextLine(),Journal.class);
                librarian.addItem(journalToAdd);
                System.out.println("Added");
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
        }
    }

    static Librarian createLibrarian(int numOfFiles, User user){
        Librarian librarian = new ServerLibrarian();
        if (numOfFiles ==1){
            WorkWithFiles.setSingleFilePath(user.userName);
            librarian = new Librarian(WorkWithFiles.SINGLE_FILE_PATH);
        } else {
            WorkWithFiles.setBookFilePath(user.userName);
            WorkWithFiles.setJournalsFilePath(user.userName);
        }
        return librarian;
    }

    static void manageDialogues(Librarian librarian){
        if (librarian.filePath == null) {
            bookDialogue = new Dialogues(new Book(), new Librarian(WorkWithFiles.BOOK_FILE_PATH));
            journalDialogue = new Dialogues(new Journal(), new Librarian(WorkWithFiles.JOURNALS_FILE_PATH));
        } else {
            bookDialogue = new Dialogues(new Book(), librarian);
            journalDialogue = new Dialogues(new Journal(), librarian);
        }
    }
}
