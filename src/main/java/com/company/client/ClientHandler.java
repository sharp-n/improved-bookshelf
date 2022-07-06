package com.company.client;

import com.company.Book;
import com.company.Dialogues;
import com.company.Journal;
import com.company.Librarian;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;

public class ClientHandler {
/*
    static final Gson gson = new Gson();


    static Dialogues booksDialogues = new Dialogues(new Book(),new Librarian());
    static Dialogues journalsDialogues = new Dialogues(new Journal(),new Librarian());

    static String makeJsonObjectFromUsersBookInput(){
        Book book = new Book(booksDialogues.validateID(booksDialogues.idUserInput()),
                booksDialogues.validateTitle(booksDialogues.titleUserInput()),
                booksDialogues.validateAuthorName(booksDialogues.authorUserInput()),
                booksDialogues.validateDate(booksDialogues.yearUserInput(),booksDialogues.monthUserInput(),booksDialogues.dayUserInput()),
                booksDialogues.validatePages(booksDialogues.pagesUsersInput()));
        return gson.toJson(book);
    }

    static String makeJsonObjectFromUsersJournalInput(){
        Journal journal = new Journal(journalsDialogues.validateID(journalsDialogues.idUserInput()),
                journalsDialogues.validateTitle(journalsDialogues.titleUserInput()),
                journalsDialogues.validatePages(journalsDialogues.pagesUsersInput()));
        return gson.toJson(journal);
    }

    static String enterIdToDoSmth(){
        return Integer.toString(booksDialogues.validateID(booksDialogues.idUserInput()));
    }

    static void showBooks() throws IOException {
        booksDialogues.sortingDialogue();
    }

    static void showJournals() throws IOException {
        journalsDialogues.sortingDialogue();
    }

    static void sendMessageToServer(PrintWriter out, String message){
        out.println(message);
        out.flush();
    }
*/
}
