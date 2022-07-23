package com.company;

import com.company.enums.SortingMenu;
import com.company.handlers.ItemHandlerProvider;
import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.items.Newspaper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.company.ConstantsForSorting.*;

public class Dialogues {

    Scanner scan;
    Item item;

    Librarian librarian;

    PrintWriter out;

    private static final String BAD_NUMBER_VALIDATION_MESSAGE = "ID. It should be a number (>0)"; // FIXME print twice

    private static final String NEW_LINE = System.lineSeparator();
    private static final String NEW_LINE_WITH_TAB = System.lineSeparator();

    public Dialogues(PrintWriter out, Scanner scan) {
        this.out = out;
        this.scan = scan;
    }

    public Dialogues(Item item, Librarian librarian, Scanner scan) {
        this.item = item;
        this.librarian = librarian;
        this.out = librarian.out;
        this.scan = scan;
    }

    public String titleUserInput() {
        out.println("Input title:");
        printWaitingForReplyMessage();
        return scan.nextLine().trim();
    }

    public String validateTitle(String title) {
        Pattern pattern = Pattern.compile("[\t|/<>~\\\\]");
        Matcher matcher = pattern.matcher(title);
        if (!Librarian.checkItemForValidity(title) || matcher.find()) {
            printBadValidationMessage("title. It should not contains such symbols as \\t, |, /, <, > ~ or \\");
            return null;
        } else return title;
    }

    public String usernameInput() {
        out.println(NEW_LINE + "Input your name. If you want to use default file(s) write \"default\". To exit input\"exit\"");
        printWaitingForReplyMessage();
        return scan.nextLine().trim();
    }

    public String usernameValidation(String userName) {
        if (userName.equals("exit")) {
            return userName;
        }
        boolean validUserName = User.checkUserNameForValidity(userName);
        if (validUserName) {
            return userName;
        }
        return null;
    }

    public Integer idUserInput() {
        try {
            out.println("Item ID:");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer validateID(Integer id) {
        if (id == null) {
            return null;
        }
        if (Librarian.checkItemForValidity(id)) {
            return id;
        }
        printBadValidationMessage(BAD_NUMBER_VALIDATION_MESSAGE);
        return null;
    }

    public String authorUserInput() {
        out.println("Author:");
        printWaitingForReplyMessage();
        return scan.nextLine().trim();
    }

    public String validateAuthorName(String author) {
        Pattern pattern = Pattern.compile("[@\t#$;:=+*&|/<>?!~()%']");
        Matcher matcher = pattern.matcher(author);
        if (!Librarian.checkItemForValidity(author) || matcher.find()) {
            printBadValidationMessage("author`s name. It should contains only letters, numbers or underscore and spaces");
            return null;
        }
        return author;
    }

    public Integer yearUserInput() {
        try {
            out.println();
            out.println("Date of publish:" + NEW_LINE_WITH_TAB + "Year: ");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer dayUserInput() {
        try {
            out.println("\tDay: ");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer monthUserInput() {
        try {
            out.println("\tMonth: ");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public GregorianCalendar validateDate(Integer year, Integer month, Integer day) {
        if (day == null || month == null || year == null) {
            printBadValidationMessage("date");
            return null;
        }
        if (day > 31) {
            printBadValidationMessage("day. It should be less then 31");
            return null;
        }
        if (month > 12) {
            printBadValidationMessage("day. It should be less then 12");
            return null;
        }
        if (Librarian.checkItemForValidity(year) && Librarian.checkItemForValidity(month)
                && Librarian.checkItemForValidity(day)) {
            return new GregorianCalendar(year, month - 1, day);
        }
        printBadValidationMessage("date");
        return null;
    }

    public Integer validatePages(Integer pages) {
        if (pages == null) {
            return null;
        }
        if (!Librarian.checkItemForValidity(pages)) {
            printBadValidationMessage("pages");
            return null;
        }
        if (pages > 5000) {
            printBadValidationMessage("pages. It should be less then 5000");
            return null;
        }
        return pages;
    }

    public Integer pagesUsersInput() {
        try {
            out.println("Pages: ");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean addingDialogue() throws IOException {
        String author = "";
        GregorianCalendar publishingDate = null;
        Integer itemID = validateID(idUserInput());
        if (itemID == null) {
            printBadValidationMessage(BAD_NUMBER_VALIDATION_MESSAGE);
            return false;
        }
        if (librarian.checkIDForExistence(itemID)) {
            out.println("Item with this ID exists. Please change ID and try again");
            return false;
        }

        String title = validateTitle(titleUserInput());

        if (!Librarian.checkItemForValidity(title)) return false;
        if (item instanceof Book) {
            author = validateAuthorName(authorUserInput());
            if (author == null) {
                return false;
            }
            publishingDate = validateDate(yearUserInput(), monthUserInput(), dayUserInput());
            if (publishingDate == null) {
                out.println("Try again");
                return false;
            }
        }

        Integer numOfPages = validatePages(pagesUsersInput());

        if (numOfPages != null) {

            if (item instanceof Book) {
                Book book = new Book(itemID, title, author, publishingDate, numOfPages);
                librarian.addItem(book);
            } else if (item instanceof Journal) {
                Journal journal = new Journal(itemID, title, numOfPages);
                librarian.addItem(journal);
            } else if (item instanceof Newspaper) {
                Newspaper newspaper = new Newspaper(itemID, title, numOfPages);
                librarian.addItem(newspaper);
            }
            return true;
        }
        return false;
    }

    private Integer validateIdToBorrow() throws IOException {

        Integer itemID = validateID(idUserInput());
        if (itemID == null) {
            return null;
        }
        if (!Librarian.checkItemForValidity(itemID)) {
            printBadValidationMessage(BAD_NUMBER_VALIDATION_MESSAGE);
            return null;
        }
        if (!librarian.checkIDForExistence(itemID)) {
            out.println("There`s no item with such ID");
            return null;
        }
        return itemID;
    }

    public void deletingDialogue() throws IOException {
        Integer itemID = validateIdToBorrow();
        if (itemID != null) {
            boolean deleted = librarian.deleteItem(itemID, false);
            if (deleted) {
                printSuccessMessage("deleted");
            }
        }
    }

    public void borrowingDialogue(boolean borrow) throws IOException {
        Integer itemID = validateIdToBorrow();
        if (itemID != null) {
            librarian.borrowItem(itemID, borrow);
        }
    }

    private Integer getSortingVar() { // TODO enum
        try {
            if (item instanceof Book || item instanceof Journal || item instanceof Newspaper) {
                out.println("Sort by:" + NEW_LINE_WITH_TAB
                        + SortingMenu.ITEM_ID + NEW_LINE_WITH_TAB
                        + SortingMenu.TITLE + NEW_LINE_WITH_TAB
                        + SortingMenu.PAGES);
                if (item instanceof Book) {
                    out.println(SortingMenu.AUTHOR + NEW_LINE_WITH_TAB + SortingMenu.PUBLISHING_DATE);
                }
                out.println(SortingMenu.RETURN_VALUE);
                printWaitingForReplyMessage();
            }
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            printDefaultMessage();
            return null;
        }
    }

    public Integer getMainMenuVar() {
        try {
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            printDefaultMessage();
            return null;
        }
    }

    public void sortingDialogue() throws IOException {
        WorkWithFiles workWithFiles = librarian.workWithFiles;
        Integer usersChoice = getSortingVar();
        if (usersChoice != null) {
            SortingMenu sortingParameter = SortingMenu.getByIndex(usersChoice);
            List<Item> sortedItemsByComparator = getSortedItemsByComparator(workWithFiles, sortingParameter);
            if(!sortedItemsByComparator.isEmpty()){
                librarian.printItems(sortedItemsByComparator);
            }
        } else{
            printDefaultMessage();
        }
    }

    private List<Item> getSortedItemsByComparator(WorkWithFiles workWithFiles, SortingMenu sortingParameter) throws IOException {
        List<Item> items = workWithFiles.readToAnyItemList(item.getClass().getSimpleName());
        switch (sortingParameter) {
            case RETURN_VALUE:
                break;
            case ITEM_ID:
                return ItemHandlerProvider.getHandlerByClass(item.getClass()).getSortedItemsByComparator(items,COMPARATOR_ITEM_BY_ID);
            case TITLE:
                return ItemHandlerProvider.getHandlerByClass(item.getClass()).getSortedItemsByComparator(items,COMPARATOR_ITEM_BY_TITLE);
            case PAGES:
                return ItemHandlerProvider.getHandlerByClass(item.getClass()).getSortedItemsByComparator(items,COMPARATOR_ITEM_BY_PAGES);
            case AUTHOR:
                return ItemHandlerProvider.getBookHandler().getSortedItemsByComparator(items, COMPARATOR_ITEM_BY_AUTHOR);//    librarian.sortingBooksByAuthor(items);
            case PUBLISHING_DATE:
                return ItemHandlerProvider.getBookHandler().getSortedItemsByComparator(items, COMPARATOR_ITEM_BY_DATE);
            default:
                printDefaultMessage();
                break;
        }
        return Collections.emptyList();
    }

    public void printBadValidationMessage(String item) {
        out.println("Please, input valid " + item);
    }

    public void printSuccessMessage(String item) {
        out.println("The item is successfully " + item);
    }

    public void printDefaultMessage() {
        out.println("Input the proposed option");
    }

    public void printWaitingForReplyMessage() {
        out.println("Waiting for reply...");
    }

}
