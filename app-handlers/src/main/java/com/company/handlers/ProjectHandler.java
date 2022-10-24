package com.company.handlers;

import com.company.*;
import com.company.enums.ActionsWithItem;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.item_handlers.*;
import com.company.handlers.work_with_files.FilePerTypeWorker;
import com.company.handlers.work_with_files.FilesWorker;
import com.company.handlers.work_with_files.OneFileWorker;
import com.company.table.TableUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class ProjectHandler {

    String rootFolder = System.getProperty("user.home");

    //TODO fix input/output -> extract it to one class

    public Scanner in;
    public PrintWriter out;

    ItemHandler itemHandler;

    boolean mainProcValue;

    Librarian librarian;
    UserInput userInput;
    String typeOfItem;

    public Librarian getLibrarian() {
        return librarian;
    }

    public ItemHandler<? extends Item> getItemHandler() {
        return itemHandler;
    }

    public ProjectHandler(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.librarian = new DefaultLibrarian();
        this.itemHandler = new DefaultItemHandler(out,in);
        this.userInput = new UserInput(out,in);
    }

    public void handle() {

        boolean filesValue = true;
        boolean validUserName = false;

        while (filesValue) {

            User user = createUser(itemHandler.userInput, validUserName);
            if (user.userName.equals("exit")) {
                filesValue = false;
                validUserName = false;
            } else {
                mainProcValue = true;
                int usersFilesMenuChoice = usersFilesMenuChoice(itemHandler.userInput);
                filesValue = initProcessWithItemsChoosingAndActions(usersFilesMenuChoice, user);
            }
        }
    }

    public boolean initProcessWithItemsChoosingAndActions(Integer usersFilesMenuChoice, User user){
        boolean filesValue = false;
        while (mainProcValue) {
            out.println(itemHandler.initItemsMenuText());
            Integer itemsChoice = itemHandler.userInput.getItemMenuVar();
            boolean chosenItem = itemMenuSwitch(MainMenu.getByIndex(itemsChoice));
            FilesMenu filesMenuOption = FilesMenu.getByIndex(usersFilesMenuChoice);
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
        return filesValue;
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
            case DATABASE_MYSQL:
            case DATABASE_SQLITE:
                initWorkWithDB(user, DBServiceProvider.getDBServiceByOption(filesMenuOption.getServletParameter()));
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

    public void initWorkWithDB(User user, DBService dbService){
        dbService.open(DBServiceProvider.getDBNameByService(dbService));
        dbService.createTablesIfNotExist(dbService.getConnection());
        dbService.createUser(user,dbService.getConnection());
        librarian = new DBWorker(user,dbService,out);
    }

    public boolean itemMenuSwitch(MainMenu mainMenu){
        boolean chosenItem = true;
        switch(mainMenu) {
            case BOOK:
                itemHandler = new BookHandler(out,in);
                break;
            case NEWSPAPER:
                itemHandler = new NewspaperHandler(out,in);
                break;
            case COMICS:
                itemHandler = new ComicsHandler(out,in);
                break;
            case JOURNAL:
                itemHandler = new JournalHandler(out,in);
                break;
            case SHOW_ALL_THE_ITEMS:
                showSortedItems();
                itemHandler = new JournalHandler(out,in);
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

    private void showSortedItems() {
        SortingMenu sortingParam = SortingMenu.getByOption(SortingMenu.ITEM_ID.getDbColumn());
        List<Item> items = librarian.initSortingAllItemsByComparator();
        librarian.printItems(items, itemHandler);
    }

    public Integer getUsersMainMenuChoice(String message, UserInput dialogue) {
        out.println(message);
        dialogue.printWaitingForReplyMessage();
        return dialogue.getMainMenuVar();
    }

    public Integer usersFilesMenuChoice(UserInput dialogue) {
        out.println(TableUtil.NEW_LINE + FilesMenu.EXIT_VALUE + TableUtil.NEW_LINE + FilesMenu.ONE_FILE +
                TableUtil.NEW_LINE + FilesMenu.FILE_PER_ITEM + TableUtil.NEW_LINE +
                FilesMenu.DATABASE_SQLITE + TableUtil.NEW_LINE +
                FilesMenu.DATABASE_MYSQL + TableUtil.NEW_LINE +
                FilesMenu.CHANGE_USER);
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
        librarian = new DefaultLibrarian(filesWorker, out);
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
        switch (actionsWithItem) {

            case ADD:
                librarian.addItem(itemHandler, itemHandler.getItem(librarian.genItemID()));
                break;

            case DELETE:
                librarian.deleteItem(userInput.idUserInput(), false);
                break;

            case TAKE:
                librarian.borrowItem(userInput.idUserInput(), true);
                break;

            case RETURN:
                librarian.borrowItem(userInput.idUserInput(), false);
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
    }
}
