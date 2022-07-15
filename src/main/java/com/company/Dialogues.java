package com.company;

import com.company.server.ServerHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dialogues {

    Scanner scan;
    Item item;

    Librarian librarian;

    PrintWriter out;

    ServerHandler serverHandler;

    private static final String BAD_NUMBER_VALIDATION_MESSAGE = "ID. It should be a number (>0)";

    public Dialogues(PrintWriter out, Scanner scan) {
        this.out = out;
        this.scan = scan;
        this.serverHandler = new ServerHandler(out);
    }

    public Dialogues(Item item, Librarian librarian, PrintWriter out, Scanner scan) {
        this.item = item;
        this.librarian = librarian;
        this.out = out;
        this.scan = scan;
        this.serverHandler = new ServerHandler(out);
    }

    public String titleUserInput() {
        serverHandler.writeLineMessage("Input title:");
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
        serverHandler.writeLineMessage("\nInput your name. If you want to use default file(s) write \"default\". To exit input\"exit\"");
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
            serverHandler.writeLineMessage("Item ID:");
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
        serverHandler.writeLineMessage("Author:");
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
            serverHandler.writeLineMessage("");
            serverHandler.writeLineMessage("Date of publish:\n\r\tYear: ");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer dayUserInput() {
        try {
            serverHandler.writeLineMessage("\tDay: ");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer monthUserInput() {
        try {
            serverHandler.writeLineMessage("\tMonth: ");
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
            serverHandler.writeLineMessage("Pages: ");
            printWaitingForReplyMessage();
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean addingDialogue() throws IOException {
        String typeOfItem = item.getClass().getSimpleName();
        String author = "";
        GregorianCalendar publishingDate = new GregorianCalendar();
        Integer itemID = validateID(idUserInput());
        if (itemID == null) {
            printBadValidationMessage(BAD_NUMBER_VALIDATION_MESSAGE);
            return false;
        }
        if (librarian.checkIDForExistence(itemID, typeOfItem)) {
            out.println("Item with this ID exists. Please change ID and try again");
            return false;
        }

        String title = validateTitle(titleUserInput());

        if (!Librarian.checkItemForValidity(title)) return false;
        if (item instanceof Book) {
            author = validateAuthorName(authorUserInput());
            if (author != null) {
                publishingDate = validateDate(yearUserInput(), monthUserInput(), dayUserInput());
                if (publishingDate == null) {
                    serverHandler.writeLineMessage("Try again");
                    return false;
                }
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
            }
            printSuccessMessage("added");
            return true;
        }
        return false;
    }

    private Integer validateIdToBorrow() throws IOException{

        Integer itemID = validateID(idUserInput());
        if (itemID==null){
            return null;
        }
        if (!Librarian.checkItemForValidity(itemID)) {
            printBadValidationMessage(BAD_NUMBER_VALIDATION_MESSAGE);
            return null;
        }
        String typeOfItem = "";
        if(item instanceof Book) {
            typeOfItem = Librarian.TYPE_OF_ITEM_BOOK;
        }
        else if (item instanceof Journal) {
            typeOfItem = Librarian.TYPE_OF_ITEM_JOURNAL;
        }
        if(!librarian.checkIDForExistence(itemID, typeOfItem)) {
            serverHandler.writeLineMessage("There`s no item with such ID");
            return null;
        }
        return itemID;
    }

    public void deletingDialogue() throws IOException{
        Integer itemID = validateIdToBorrow();
        if(itemID!=null) {
            boolean deleted = false;
            if(item instanceof Book) {
                deleted = librarian.deleteItem(itemID, false, "Book");
            }
            else  if (item instanceof Journal) {
                deleted = librarian.deleteItem(itemID, false, "Journal");
            }
            if (deleted) {
                printSuccessMessage("deleted");
            }
        }
    }

    public void borrowingDialogue(boolean borrow) throws IOException{
        Integer itemID = validateIdToBorrow();
        if(itemID!=null) {
            if (item instanceof Book) {
                librarian.borrowItem(itemID, "Book", borrow);
            } else if (item instanceof Journal) librarian.borrowItem(itemID, "Journal", borrow);
        }
    }

    // TODO refactor printListOfItems() -> if-else & table

    public void printListOfItems(List<? extends Item> items){
        if (items.isEmpty()) serverHandler.writeLineMessage("There`s no items here");
        else {
            String str = "%-12s%-40s";
            serverHandler.writeMessage(String.format(str, "\n\r ITEM ID", "| TITLE"));
            if (item instanceof Book) {
                serverHandler.writeMessage(String.format("%-32s%-18s", "| AUTHOR", "| PUBLISHING DATE"));
            }
            serverHandler.writeMessage(String.format("%-8s%-30s", "| PAGES", "| " + "BORROWED"));
            serverHandler.writeMessage("\n\r----------+---------------------------------------");
            if (item instanceof Book) {
                serverHandler.writeMessage("+-------------------------------+-----------------");
            }
            serverHandler.writeLineMessage("+-------+----------");
            for (Item i : items) {
                serverHandler.writeMessage(String.format("%-10s%-40s", " " + i.getItemID(), "| " + i.getTitle()));
                if (item instanceof Book) {
                    DateFormat df = new SimpleDateFormat("dd.M.y");
                    serverHandler.writeMessage(String.format("%-32s%-18s", "| " + ((Book) i).getAuthor(), "| " + df.format(((Book) i).getPublishingDate().getTime())));
                }
                serverHandler.writeMessage(String.format("%-8s", "| " + i.getPages()) + "| " + i.isBorrowed() + "\n\r");
            }
        }
    }

    private Integer getSortingVar(){
        try {
            if (item instanceof Book || item instanceof Journal) {
                serverHandler.writeLineMessage("Sort by:\n\r\t1 - Item ID\n\r\t2 - Title\n\r\t3 - Pages");
                if (item instanceof Book) {
                    serverHandler.writeLineMessage("\t4 - Author\n\r\t5 - Publishing date");
                }
                serverHandler.writeLineMessage("0 - Return");
                printWaitingForReplyMessage();
            }
            return Integer.parseInt(scan.nextLine().trim());
        }catch(NumberFormatException e){
            printDefaultMessage();
            return null;
        }
    }

    public Integer getMainMenuVar(){
        try {
            return Integer.parseInt(scan.nextLine().trim());
        }catch(NumberFormatException e){
            printDefaultMessage();
            return null;
        }
    }

    public void sortingDialogue() throws IOException {
        WorkWithFiles workWithFiles = librarian.workWithFiles;
        Integer sortingParameter = getSortingVar();
        if (sortingParameter != null) {
            List<? extends Item> items = new ArrayList<>();
            if (item instanceof Book)
                switch (sortingParameter) {
                    case 0:
                        break;
                    case 1:
                        items = librarian.sortingItemsByID(workWithFiles.readToBooksList());
                        break;
                    case 2:
                        items = librarian.sortingItemsByTitle(workWithFiles.readToBooksList());
                        break;
                    case 3:
                        items = librarian.sortingItemsByPages(workWithFiles.readToBooksList());
                        break;
                    case 4:
                        items = librarian.sortingBooksByAuthor(workWithFiles.readToBooksList());
                        break;
                    case 5:
                        items = librarian.sortingBooksByPublishingDate(workWithFiles.readToBooksList());
                        break;
                    default:
                        printDefaultMessage();
                        break;

                }
            else if (item instanceof Journal) switch (sortingParameter) {
                case 0:
                    break;
                case 1:
                    items = librarian.sortingItemsByID(workWithFiles.readToJournalsList());
                    break;
                case 2:
                    items = librarian.sortingItemsByTitle(workWithFiles.readToJournalsList());
                    break;
                case 3:
                    items = librarian.sortingItemsByPages(workWithFiles.readToJournalsList());
                    break;
                default:
                    printDefaultMessage();
                    break;
            }
            printListOfItems(items);
        } else printDefaultMessage();
    }

    public void printBadValidationMessage(String item){
        serverHandler.writeLineMessage("Please, input valid " + item);
    }

    public void printSuccessMessage(String item){
        serverHandler.writeLineMessage("The item is successfully " + item);
    }

    public void printDefaultMessage(){
        serverHandler.writeLineMessage("Input the proposed option");
    }

    public void printWaitingForReplyMessage(){
        serverHandler.writeLineMessage("Waiting for reply...");
    }

}
