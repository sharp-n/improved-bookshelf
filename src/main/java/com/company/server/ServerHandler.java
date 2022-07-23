package com.company.server;

import com.company.*;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.items.Book;
import com.company.items.Journal;
import com.company.items.Newspaper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ServerHandler {

    public Scanner in;
    public PrintWriter out;

    public WorkWithFiles workWithBookFile = new WorkWithFiles();
    public WorkWithFiles workWithJournalFile = new WorkWithFiles();
    public WorkWithFiles  workWithNewspaperFile = new WorkWithFiles();

    Librarian librarian = new Librarian();

    boolean mainProcValue;

    public ServerHandler(PrintWriter out) {
        this.out = out;
    }

    public ServerHandler(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    private static final String NEW_LINE = System.lineSeparator();

    public void handle() {

        boolean filesValue = true;

        boolean validUserName = false;

        while (filesValue) {

            Dialogues dialogue = new Dialogues(out, in);

            User user = createUser(dialogue, validUserName);
            if (user.userName.equals("exit")) {
                filesValue = false;
            } else {

                Integer usersFilesMenuChoice = usersFilesMenuChoice(dialogue);
                if (usersFilesMenuChoice == null) usersFilesMenuChoice = -1;

                FilesMenu filesMenuOption = FilesMenu.getByIndex(usersFilesMenuChoice);

                mainProcValue = true;

                switch (filesMenuOption) {
                    case ONE_FILE:
                        oneFileChoice(user);
                        break;
                    case FEW_FILES:
                        fewFilesChoice(user);
                        break;
                    case CHANGE_USER:
                        mainProcValue = false;
                        break;
                    case EXIT_VALUE:
                        filesValue = false;
                        mainProcValue = false;
                        break;
                    default:
                        mainProcValue = false;
                        dialogue.printDefaultMessage();
                        break;
                }

                while (mainProcValue) {

                    Dialogues bookDialogue;
                    Dialogues journalDialogue;
                    Dialogues newspaperDialogue;
                    if (workWithJournalFile.filePath != null) { // TODO ???
                        bookDialogue = new Dialogues(new Book(), new Librarian(workWithBookFile, new ServerHandler(in, out)), out, in);
                        journalDialogue = new Dialogues(new Journal(), new Librarian(workWithJournalFile, new ServerHandler(in, out)), out, in);
                        newspaperDialogue = new Dialogues(new Newspaper(), new Librarian(workWithNewspaperFile, new ServerHandler(in, out)), out, in);
                    } else {
                        bookDialogue = new Dialogues(new Book(), librarian, out, in);
                        journalDialogue = new Dialogues(new Journal(), librarian, out, in);
                        newspaperDialogue = new Dialogues(new Newspaper(), librarian, out, in);
                    }

                    Integer usersChoice = getUsersMainMenuChoice(dialogue);
                    if (usersChoice == null) usersChoice = -1;
                    MainMenu mainMenuOption = MainMenu.getByIndex(usersChoice);
                    mainMenuVariants(mainMenuOption, bookDialogue, journalDialogue, newspaperDialogue);
                }
            }
        }
    }

    public Integer getUsersMainMenuChoice(Dialogues dialogue) {
        writeLineMessage(NEW_LINE + "\t\t" + MainMenu.EXIT_VALUE +
                NEW_LINE + MainMenu.ADD_BOOK + "\t" + MainMenu.ADD_JOURNAL + "\t\t" + MainMenu.ADD_NEWSPAPER +
                NEW_LINE + MainMenu.DELETE_BOOK + "\t" + MainMenu.DELETE_JOURNAL + "\t" + MainMenu.DELETE_NEWSPAPER +
                NEW_LINE + MainMenu.TAKE_BOOK + "\t" + MainMenu.TAKE_JOURNAL + "\t" + MainMenu.TAKE_NEWSPAPER +
                NEW_LINE + MainMenu.RETURN_BOOK + "\t" + MainMenu.RETURN_JOURNAL + "\t" + MainMenu.RETURN_NEWSPAPER +
                NEW_LINE + MainMenu.SHOW_BOOKS + "\t" + MainMenu.SHOW_JOURNALS + "\t" + MainMenu.SHOW_NEWSPAPERS
        );
        dialogue.printWaitingForReplyMessage();
        return dialogue.getMainMenuVar();
    }

    public Integer usersFilesMenuChoice(Dialogues dialogue) {
        writeLineMessage(NEW_LINE + FilesMenu.EXIT_VALUE + NEW_LINE + FilesMenu.ONE_FILE +
                NEW_LINE + FilesMenu.FEW_FILES + NEW_LINE + FilesMenu.CHANGE_USER);
        dialogue.printWaitingForReplyMessage();
        return dialogue.getMainMenuVar();
    }

    public void oneFileChoice(User user) {
        workWithBookFile = new WorkWithFiles(user.userName);
        librarian = new Librarian(workWithBookFile, new ServerHandler(in, out));
        writeLineMessage("Your items will be saved in one file");
    }

    public void fewFilesChoice(User user) {
        workWithBookFile = new WorkWithFiles("books_" + user.userName);
        workWithJournalFile = new WorkWithFiles("journals_" + user.userName);
        workWithNewspaperFile = new WorkWithFiles("newspaper_" + user.userName);
        writeLineMessage("Your items will be saved in different files");
    }

    public User createUser(Dialogues dialogue, boolean validUserName) {
        String userName = "";
        while (!validUserName) {
            userName = dialogue.usernameValidation(dialogue.usernameInput());
            if (userName != null) validUserName = true;
        }
        return new User(userName);
    }

    public void writeLineMessage(String msg) {
        out.println(msg);
        out.flush();
    }

    public void writeMessage(String msg) {
        out.print(msg);
        out.flush();
    }

    private void mainMenuVariants(MainMenu mainMenuOption, Dialogues bookDialogue, Dialogues journalDialogue, Dialogues newspaperDialogue) {
        try {
            switch (mainMenuOption) {

                case ADD_BOOK:
                    boolean bookSuccess = bookDialogue.addingDialogue();
                    if (bookSuccess) bookDialogue.printSuccessMessage("added");
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
                    boolean journalSuccess = journalDialogue.addingDialogue();
                    if (journalSuccess) bookDialogue.printSuccessMessage("added");
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

                case ADD_NEWSPAPER:
                    boolean newspaperSuccess = newspaperDialogue.addingDialogue();
                    if (newspaperSuccess) newspaperDialogue.printSuccessMessage("added");
                    break;

                case DELETE_NEWSPAPER:
                    newspaperDialogue.deletingDialogue();
                    break;

                case TAKE_NEWSPAPER:
                    newspaperDialogue.borrowingDialogue(true);
                    break;

                case RETURN_NEWSPAPER:
                    newspaperDialogue.borrowingDialogue(false);
                    break;

                case SHOW_NEWSPAPERS:
                    newspaperDialogue.sortingDialogue();
                    break;

                case EXIT_VALUE:
                    mainProcValue = false;
                    break;

                default:
                    if (bookDialogue != null) {
                        bookDialogue.printDefaultMessage();
                    } else if (journalDialogue != null) {
                        journalDialogue.printDefaultMessage();
                    }
                    else if (newspaperDialogue != null) {
                        newspaperDialogue.printDefaultMessage();
                    }
                    break;
            }
        } catch (IOException e) {
            out.println(e);
        }
    }
}
