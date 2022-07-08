package com.company.server;

import com.company.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ServerHandler {

    Scanner in;
    PrintWriter out;

    public ServerHandler(PrintWriter out) {
        this.out = out;
    }

    public ServerHandler(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    private final static int ONE_FILE = 1;
    private final static int TWO_FILES = 2;
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
    private final static int EXIT_VALUE = 0;

    public void handle() throws IOException {

        boolean filesValue = true;
        boolean value = true;
        boolean validUserName = false;
        while (filesValue) {

            String userName = "";
            Dialogues dialogue = new Dialogues(out,in);

            while (!validUserName) {
                userName = dialogue.usernameValidation(dialogue.usernameInput());
                if (userName != null) validUserName = true;
            }

            User user = new User(userName);
            writeLineMessage("\n0 - Exit\n1 - Use one file\n2 - Use two files");
            dialogue.printWaitingForReplyMessage();
            Integer filesVar = dialogue.getMainMenuVar();
            if (filesVar == null) filesVar = -1;

            WorkWithFiles workWithFirstFile = new WorkWithFiles();
            WorkWithFiles workWithSecondFile = new WorkWithFiles();
            Librarian librarian = new Librarian();

            value = true;

            switch (filesVar) {
                case ONE_FILE:
                    workWithFirstFile = new WorkWithFiles(user.userName);
                    librarian = new Librarian(workWithFirstFile, new ServerHandler(in,out));
                    writeLineMessage("Your items will be saved in one file");
                    break;
                case TWO_FILES:
                    workWithFirstFile = new WorkWithFiles("books_" + user.userName);
                    workWithSecondFile = new WorkWithFiles("journals_" + user.userName);
                    writeLineMessage("Your items will be saved in different files");
                    break;
                case EXIT_VALUE:
                    filesValue = false;
                    value = false;
                    break;
                default:
                    dialogue.printDefaultMessage();
                    break;
            }


            while (value) {
                Dialogues dialogues = new Dialogues(out,in);
                writeLineMessage("\n\t\t0 - Exit" +
                        "\n1 - Add book\t6 - Add journal" +
                        "\n2 - Delete book\t7 - Delete journal" +
                        "\n3 - Take book\t8 - Take journal" +
                        "\n4 - Return book\t9 - Return journal" +
                        "\n5 - Show books\t10 - Show journals");

                dialogues.printWaitingForReplyMessage();
                Dialogues bookDialogue;
                Dialogues journalDialogue;
                if (workWithSecondFile.filePath != null) {
                    bookDialogue = new Dialogues(new Book(), new Librarian(workWithFirstFile, new ServerHandler(in,out)),out,in);
                    journalDialogue = new Dialogues(new Journal(), new Librarian(workWithSecondFile, new ServerHandler(in,out)), out,in);
                } else {
                    bookDialogue = new Dialogues(new Book(), librarian, out,in);
                    journalDialogue = new Dialogues(new Journal(), librarian, out,in);
                }

                Integer var = dialogues.getMainMenuVar();

                if (var ==null) var =-1;

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
                        dialogue.printDefaultMessage();
                        break;
                }
            }
        }
    }

    public void writeLineMessage(String msg){
        out.println(msg);
        out.flush();
    }

    public void writeMessage(String msg){
        out.print(msg);
        out.flush();
    }

}
