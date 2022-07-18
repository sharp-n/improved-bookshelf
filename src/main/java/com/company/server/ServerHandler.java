package com.company.server;

import com.company.*;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.items.Book;
import com.company.items.Journal;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ServerHandler {

    public Scanner in;
    public PrintWriter out;

    public WorkWithFiles workWithFirstFile = new WorkWithFiles();
    public WorkWithFiles workWithSecondFile = new WorkWithFiles();

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

            // TODO fix user`s name input

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
                    case TWO_FILES:
                        twoFilesChoice(user);
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
                    if (workWithSecondFile.filePath != null) {
                        bookDialogue = new Dialogues(new Book(), new Librarian(workWithFirstFile, new ServerHandler(in, out)), out, in);
                        journalDialogue = new Dialogues(new Journal(), new Librarian(workWithSecondFile, new ServerHandler(in, out)), out, in);
                    } else {
                        bookDialogue = new Dialogues(new Book(), librarian, out, in);
                        journalDialogue = new Dialogues(new Journal(), librarian, out, in);
                    }

                    Integer usersChoice = getUsersMainMenuChoice(dialogue);
                    if (usersChoice == null) usersChoice = -1;
                    MainMenu mainMenuOption = MainMenu.getByIndex(usersChoice);
                    mainMenuVariants(mainMenuOption, bookDialogue, journalDialogue);

                }
            }
        }
    }

    public Integer getUsersMainMenuChoice(Dialogues dialogue){
        writeLineMessage(NEW_LINE + MainMenu.EXIT_VALUE +"\t\t" +
                NEW_LINE + MainMenu.ADD_BOOK + NEW_LINE + "\t" + MainMenu.ADD_JOURNAL +
                NEW_LINE + MainMenu.DELETE_BOOK + "\t" + MainMenu.DELETE_JOURNAL +
                NEW_LINE + MainMenu.TAKE_BOOK + "\t" + MainMenu.TAKE_JOURNAL +
                NEW_LINE + MainMenu.RETURN_BOOK + "\t" + MainMenu.RETURN_JOURNAL +
                NEW_LINE + MainMenu.SHOW_BOOKS + "\t" + MainMenu.SHOW_JOURNALS);
        dialogue.printWaitingForReplyMessage();
        return dialogue.getMainMenuVar();
    }

    public Integer usersFilesMenuChoice(Dialogues dialogue){
        writeLineMessage(NEW_LINE + FilesMenu.EXIT_VALUE + NEW_LINE  + FilesMenu.ONE_FILE +
                NEW_LINE + FilesMenu.TWO_FILES + NEW_LINE + FilesMenu.CHANGE_USER);
        dialogue.printWaitingForReplyMessage();
        return dialogue.getMainMenuVar();
    }

    public void oneFileChoice(User user){
        workWithFirstFile = new WorkWithFiles(user.userName);
        librarian = new Librarian(workWithFirstFile, new ServerHandler(in,out));
        writeLineMessage("Your items will be saved in one file");
    }

    public void twoFilesChoice(User user){
        workWithFirstFile = new WorkWithFiles("books_" + user.userName);
        workWithSecondFile = new WorkWithFiles("journals_" + user.userName);
        writeLineMessage("Your items will be saved in different files");
    }

    public User createUser(Dialogues dialogue, boolean validUserName){
        String userName = "";
        while (!validUserName) {
            userName = dialogue.usernameValidation(dialogue.usernameInput());
            if (userName != null) validUserName = true;
        }
        return new User(userName);
    }

    public void writeLineMessage(String msg){
        out.println(msg);
        out.flush();
    }

    public void writeMessage(String msg){
        out.print(msg);
        out.flush();
    }

    private void mainMenuVariants(MainMenu mainMenuOption, Dialogues bookDialogue, Dialogues journalDialogue){
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

                case EXIT_VALUE:
                    mainProcValue = false;
                    break;

                default:
                    if (bookDialogue!=null) {
                        bookDialogue.printDefaultMessage();
                    } else if (journalDialogue!=null){
                        journalDialogue.printDefaultMessage();
                    }
                    break;
            }
        } catch (IOException e){
            out.println(e);
        }
    }


}
