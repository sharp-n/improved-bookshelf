package com.company;

import com.company.enums.*;
import com.company.handlers.*;
import com.company.items.Item;
import com.company.work_with_files.WorkWithFilePerType;
import com.company.work_with_files.WorkWithFiles;
import com.company.work_with_files.WorkWithOneFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static com.company.ConstantsForItemsTable.NEW_LINE;
import static com.company.enums.FilesMenu.*;
import static com.company.enums.FilesMenu.EXIT_VALUE;

public class ProjectHandler {

    //TODO fix input/output -> extract it to one class

    public Scanner in;
    public PrintWriter out;

    ItemHandler<? extends Item> itemHandler;

    boolean mainProcValue;

    Librarian librarian;
    String typeOfItem;

    public ProjectHandler(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.librarian = new Librarian();
        this.itemHandler = new ItemHandler<>(out,in);
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
                initOneFileWork(user);
                break;
            case FILE_PER_ITEM:
                initFilePerItemWork(user);
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

    public void initOneFileWork(User user) {
        WorkWithFiles workWithOneFile = new WorkWithOneFile(user.userName,user.userName);
        workWithOneFile.genFilePath();
        librarian = new Librarian(workWithOneFile, out);
    }

    public void initFilePerItemWork(User user) {
        WorkWithFiles workWithFilePerType = new WorkWithFilePerType(user.userName,user.userName,typeOfItem);
        workWithFilePerType.genFilePath();
        librarian = new Librarian(workWithFilePerType,out);
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
                    itemHandler.addItem(librarian);
                    break;

                case DELETE:
                    itemHandler.deleteItem(librarian);
                    break;

                case TAKE:
                    itemHandler.initItemBorrowing(true,librarian);
                    break;

                case RETURN:
                    itemHandler.initItemBorrowing(false,librarian);
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
