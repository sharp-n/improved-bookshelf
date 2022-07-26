package com.company;

import com.company.enums.*;
import com.company.handlers.BookHandler;
import com.company.handlers.ItemHandler;
import com.company.handlers.ItemHandlerProvider;
import com.company.handlers.NewspaperHandler;
import com.company.items.Item;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static com.company.ConstantsForItemsTable.NEW_LINE;
import static com.company.ConstantsForSorting.*;
import static com.company.enums.ActionsWithItem.*;

public class ProjectHandler {

    public Scanner in;
    public PrintWriter out;

    ItemHandler itemHandler;


    Librarian librarian = new Librarian();

    boolean mainProcValue;

    ConstantsForFiles pathForFileToWorkWith;

    public ProjectHandler(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.itemHandler = new ItemHandler<>(out,in);
        this.pathForFileToWorkWith = new ConstantsForFiles();
    }


    public void handle() {

        boolean filesValue = true;

        boolean validUserName = false;

        while (filesValue) {

            UserInput dialogue = new UserInput(out, in);

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
                        dialogue.printDefaultMessage();
                        break;
                }

                while (mainProcValue) {

                    out.println(itemHandler.initItemsMenuText());

                    Integer itemsChoice = itemHandler.userInput.getItemMenuVar();
                    MainMenu mainMenu = MainMenu.getByIndex(itemsChoice);

                    switch(mainMenu) {
                        case BOOK:
                            itemHandler = new BookHandler(out,in);
                            break;
                        //case JOURNAL:
                        //    itemHandler = new JournalHandler(out,in); // TODO journalHandler
                        //    break;
                        case NEWSPAPER:
                            itemHandler = new NewspaperHandler(out,in);
                            break;
                    }

                    Integer usersChoice = getUsersMainMenuChoice(itemHandler.initItemsMenuText(),dialogue);
                    if (usersChoice == null) usersChoice = -1;
                    ActionsWithItem actionsWithItem = ActionsWithItem.getByIndex(usersChoice);
                    mainMenuVariants(actionsWithItem, itemHandler);
                }
            }
        }
    }

    public Integer getUsersMainMenuChoice(String message, UserInput dialogue) {
        out.println(message);
        dialogue.printWaitingForReplyMessage();
        return dialogue.getMainMenuVar();
    }

    public Integer usersFilesMenuChoice(UserInput dialogue) {
        out.println(NEW_LINE + FilesMenu.EXIT_VALUE + NEW_LINE + FilesMenu.ONE_FILE +
                NEW_LINE + FilesMenu.FILE_PER_ITEM + NEW_LINE + FilesMenu.CHANGE_USER);
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
        List<Item> items = workWithFiles.readToAnyItemList(typeOfClass);
        switch (sortingParameter) {
            case RETURN_VALUE:
                break;
            case ITEM_ID:
                return ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassByHandler(itemHandler)).getSortedItemsByComparator(items,COMPARATOR_ITEM_BY_ID);
            case TITLE:
                return ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassByHandler(itemHandler)).getSortedItemsByComparator(items,COMPARATOR_ITEM_BY_TITLE);
            case PAGES:
                return ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassByHandler(itemHandler)).getSortedItemsByComparator(items,COMPARATOR_ITEM_BY_PAGES);
            case AUTHOR:
                return ItemHandlerProvider.getBookHandler().getSortedItemsByComparator(items, COMPARATOR_ITEM_BY_AUTHOR);//    librarian.sortingBooksByAuthor(items);
            case PUBLISHING_DATE:
                return ItemHandlerProvider.getBookHandler().getSortedItemsByComparator(items, COMPARATOR_ITEM_BY_DATE);
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

    private void mainMenuVariants(ActionsWithItem actionsWithItem, ItemHandler handler) {
        try {
            switch (actionsWithItem) {

                case ADD:

                    Item item = handler.createItem(handler.getItem());
                    if (item == null) {
                        out.println("Try again");
                        break;
                    //    return false;
                    }

                    librarian.addItem(item);
                   // return true;

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
