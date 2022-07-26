package com.company;

import com.company.enums.*;
import com.company.handlers.*;
import com.company.items.Item;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static com.company.ConstantsForItemsTable.NEW_LINE;
import static com.company.ConstantsForSorting.*;
import static com.company.enums.FilesMenu.*;
import static com.company.enums.FilesMenu.EXIT_VALUE;

public class ProjectHandler {

    public Scanner in;
    public PrintWriter out;

    ItemHandler<? extends Item> itemHandler;

    Librarian librarian;

    boolean mainProcValue;

    ConstantsForFiles pathForFileToWorkWith;

    public ProjectHandler(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.itemHandler = new ItemHandler<>(out,in);
        this.librarian = itemHandler.librarian;
        this.pathForFileToWorkWith = new ConstantsForFiles();
    }


    public void handle() {

        boolean filesValue = true;

        boolean validUserName = false;

        while (filesValue) {

            User user = createUser(itemHandler.userInput, validUserName);
            if (user.userName.equals("exit")) {
                filesValue = false;
            } else {
                Integer usersFilesMenuChoice = usersFilesMenuChoice(itemHandler.userInput);
                if (usersFilesMenuChoice == null) usersFilesMenuChoice = -1;

                FilesMenu filesMenuOption = FilesMenu.getByIndex(usersFilesMenuChoice);

                mainProcValue = true;

                filesValue = fileSwitch(filesMenuOption, user);
                initMainProcess();
            }
        }
    }

    public void initMainProcess(){
        while (mainProcValue) {

            out.println(itemHandler.initItemsMenuText());

            Integer itemsChoice = itemHandler.userInput.getItemMenuVar();

            boolean chosenItem = itemMenuSwitch(MainMenu.getByIndex(itemsChoice));

            if (chosenItem) {
                Integer usersChoice = getUsersMainMenuChoice(itemHandler.initItemsMenuText(), itemHandler.userInput);
                if (usersChoice == null) usersChoice = -1;
                ActionsWithItem actionsWithItem = ActionsWithItem.getByIndex(usersChoice);
                mainMenuVariants(actionsWithItem, itemHandler);
            } else {
                mainProcValue = false;
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
        pathForFileToWorkWith.workWithOneFile = new WorkWithFiles(user.userName);
        librarian = new Librarian(pathForFileToWorkWith.workWithOneFile, out);
        out.println("Your items will be saved in one file");
    }

    public void initFilePerItemWork(User user) {// todo 
        pathForFileToWorkWith.workWithBookFile = new WorkWithFiles("books_" + user.userName);
        pathForFileToWorkWith.workWithJournalFile = new WorkWithFiles("journals_" + user.userName);
        pathForFileToWorkWith.workWithNewspaperFile = new WorkWithFiles("newspaper_" + user.userName);
        out.println("Your items will be saved in different files");
    }

    public User createUser(UserInput dialogue, boolean validUserName) {
        String userName = "";
        while (!validUserName) {
            userName = itemHandler.validator.usernameValidation(dialogue.usernameInput());
            if (userName != null) validUserName = true;
        }
        return new User(userName);
    }

    public List<Item> getSortedItemsByComparator(WorkWithFiles workWithFiles, SortingMenu sortingParameter) throws IOException {
        String typeOfClass = ItemHandlerProvider.getClassByHandler(itemHandler).getSimpleName();
        ConstantsForSorting<Item> constant = new ConstantsForSorting<>();
        List<Item> items = workWithFiles.readToAnyItemList(typeOfClass);
        switch (sortingParameter) {
            case RETURN_VALUE:
                break;
            case ITEM_ID:
                return ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassByHandler(itemHandler)).getSortedItemsByComparator(items,constant.COMPARATOR_ITEM_BY_ID);
            case TITLE:
                return ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassByHandler(itemHandler)).getSortedItemsByComparator(items,constant.COMPARATOR_ITEM_BY_TITLE);
            case PAGES:
                return ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassByHandler(itemHandler)).getSortedItemsByComparator(items,constant.COMPARATOR_ITEM_BY_PAGES);
            case AUTHOR:
                return ItemHandlerProvider.getBookHandler().getSortedItemsByComparator(items, constant.COMPARATOR_ITEM_BY_AUTHOR);
            case PUBLISHING_DATE:
                return ItemHandlerProvider.getBookHandler().getSortedItemsByComparator(items, constant.COMPARATOR_ITEM_BY_DATE);
            default:
                itemHandler.userInput.printDefaultMessage();
                break;
        }
        return Collections.emptyList();
    }

    public void initSorting() throws IOException {
        WorkWithFiles workWithFiles = librarian.workWithFiles;
        Integer usersChoice = itemHandler.userInput.getSortingVar();
        if (usersChoice != null) {
            SortingMenu sortingParameter = SortingMenu.getByIndex(usersChoice);
            List<Item> sortedItemsByComparator = getSortedItemsByComparator(workWithFiles, sortingParameter);
            if(!sortedItemsByComparator.isEmpty()){
                librarian.printItems(sortedItemsByComparator);
            }
        } else{
            itemHandler.userInput.printDefaultMessage();
        }
    }

    private void mainMenuVariants(ActionsWithItem actionsWithItem, ItemHandler<? extends Item> handler) {
        try {
            switch (actionsWithItem) {

                case ADD:
                    Item item = handler.createItem(handler.getItem());
                    if (item == null) {
                        out.println("Try again");
                        break;
                    }
                    librarian.addItem(item);
                    itemHandler.userInput.printSuccessMessage("added");
                    break;

                case DELETE:
                    itemHandler.deleteItem();
                    break;

                case TAKE:
                    itemHandler.initItemBorrowing(true);
                    break;

                case RETURN:
                    itemHandler.initItemBorrowing(false);
                    break;

                case SHOW:
                    initSorting();
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
