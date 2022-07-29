package com.company;

import com.company.enums.*;
import com.company.handlers.*;
import com.company.items.Item;
import com.company.work_with_files.FilePerTypeWorker;
import com.company.work_with_files.FilesWorker;
import com.company.work_with_files.OneFileWorker;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static com.company.ConstantsForItemsTable.NEW_LINE;
import static com.company.enums.FilesMenu.*;
import static com.company.enums.FilesMenu.EXIT_VALUE;

public class ProjectHandler {

    String rootFolder = System.getProperty("user.home");

    //TODO fix input/output -> extract it to one class

    public Scanner in;
    public PrintWriter out;

    ItemHandler<? extends Item> itemHandler;

    boolean mainProcValue;

    Librarian librarian;
    UserInput userInput;
    String typeOfItem;

    public ProjectHandler(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.librarian = new Librarian();
        this.itemHandler = new JournalHandler(out,in);
        this.userInput = new UserInput(out,in);
    }

    public void handle() {

        boolean filesValue = true;
        boolean validUserName = false;

        while (filesValue) {

            User user = createUser(itemHandler.userInput, validUserName);
            if (user.userName.equals("exit")) {
                filesValue = false;
            } else {

                mainProcValue = true;

                Integer usersFilesMenuChoice = usersFilesMenuChoice(itemHandler.userInput);
                if (usersFilesMenuChoice == null) usersFilesMenuChoice = -1;
                FilesMenu filesMenuOption = FilesMenu.getByIndex(usersFilesMenuChoice);

                while (mainProcValue) {

                    out.println(itemHandler.initItemsMenuText());
                    Integer itemsChoice = itemHandler.userInput.getItemMenuVar();
                    boolean chosenItem = itemMenuSwitch(MainMenu.getByIndex(itemsChoice));

                    filesValue = fileSwitch(filesMenuOption, user);

                    if (chosenItem) {

                        Integer usersChoice = getUsersMainMenuChoice(itemHandler.initActionsWithItemsMenuText(), itemHandler.userInput);
                        if (usersChoice == null) usersChoice = -1;
                        ActionsWithItem actionsWithItem = ActionsWithItem.getByIndex(usersChoice);
                        mainMenuVariants(actionsWithItem);
                    } else {
                        mainProcValue = false;
                    }
                }
            }
        }
    }


    public boolean fileSwitch(FilesMenu filesMenuOption,User user){
        boolean filesValue = true;
        switch (filesMenuOption) {

            case ONE_FILE:
                initFileWork(genOneFileWorker(user));
                break;
            case FILE_PER_ITEM:
                initFileWork(genFilePerTypeWorker(user));
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
                itemHandler.userInput.printDefaultMessage();
                break;
        }
        return filesValue;
    }

    public boolean itemMenuSwitch(MainMenu mainMenu){
        boolean chosenItem = true;
        switch(mainMenu) {
            case BOOK:
                itemHandler = new BookHandler(out,in);
                break;
            case JOURNAL:
                itemHandler = new JournalHandler(out,in);
                break;
            case NEWSPAPER:
                itemHandler = new NewspaperHandler(out,in);
                break;
            default:
                itemHandler.userInput.printDefaultMessage();
                chosenItem = false;
                break;
        }
        if (chosenItem) {
            typeOfItem = ItemHandlerProvider.getClassByHandler(itemHandler).getSimpleName();
        }
        return chosenItem;
    }

    public Integer getUsersMainMenuChoice(String message, UserInput dialogue) {
        out.println(message);
        dialogue.printWaitingForReplyMessage();
        return dialogue.getMainMenuVar();
    }

    public Integer usersFilesMenuChoice(UserInput dialogue) {
        out.println(NEW_LINE + EXIT_VALUE + NEW_LINE + ONE_FILE +
                NEW_LINE + FILE_PER_ITEM + NEW_LINE + FilesMenu.CHANGE_USER);
        dialogue.printWaitingForReplyMessage();
        return dialogue.getMainMenuVar();
    }

    public FilePerTypeWorker genFilePerTypeWorker(User user){
        return new FilePerTypeWorker(rootFolder,user.userName,typeOfItem);
    }

    public OneFileWorker genOneFileWorker(User user){
        return new OneFileWorker(rootFolder,user.userName);
    }

    public void initFileWork(FilesWorker filesWorker) {
        filesWorker.genFilePath();
        librarian = new Librarian(filesWorker, out);
    }

    public User createUser(UserInput dialogue, boolean validUserName) {
        String userName = "";
        while (!validUserName) {
            userName = itemHandler.validator.usernameValidation(dialogue.usernameInput());
            if (userName != null) validUserName = true;
        }
        return new User(userName);
    }

    private void mainMenuVariants(ActionsWithItem actionsWithItem) {
        try {
            switch (actionsWithItem) {

                case ADD:
                    librarian.addItem(itemHandler);
                    break;

                case DELETE:
                    librarian.deleteItem(userInput.idUserInput(),false);
                    break;

                case TAKE:
                    librarian.borrowItem(userInput.idUserInput(),true,itemHandler);
                    break;

                case RETURN:
                    librarian.borrowItem(userInput.idUserInput(),false,itemHandler);
                    break;

                case SHOW:
                    librarian.initSorting(itemHandler);
                    break;

                case EXIT_VALUE:
                    mainProcValue = false;
                    break;

                default:
                    itemHandler.userInput.printDefaultMessage();
                    break;
            }
        } catch (IOException e) {
            out.println(e);
        }
    }
}
