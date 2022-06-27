package com.company;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dialogues {

    static Scanner scan;
    Item item;

    public Dialogues(Item item) {
       this.item = item;
    }

    public static void setScan(Scanner scan) {
        Dialogues.scan = scan;
    }

    String titleUserInput(){

        System.out.println("Title:");
        return scan.nextLine().trim();
    }

    String getTitleDialogue(String title){
        Pattern pattern= Pattern.compile("[\t|/<>~\\\\]");
        Matcher matcher = pattern.matcher(title);
        if (!Librarian.checkItemForValidity(title)||matcher.find()) {
            printBadValidationMessage("title");
            return null;
        }
        else return title;
    }

    Integer idUserInput(){
        try {
            System.out.println("Item ID:");
            return Integer.parseInt(scan.nextLine().trim());
        }  catch (NumberFormatException e){
            return null;
        }
    }
    Integer getIDDialogue(Integer id) {
        if (id==null) {return null;}
        if (Librarian.checkItemForValidity(id)) {
            return id;
        }
        printBadValidationMessage("ID");
        return null;
    }

    String authorUserInput(){
        System.out.println("Author:");
        return scan.nextLine().trim();
    }
    String getAuthorDialogue(String author) {
        Pattern pattern= Pattern.compile("[@\t#$;:=+*&|/<>?!~()%']");
        Matcher matcher = pattern.matcher(author);
        if (!Librarian.checkItemForValidity(author) || matcher.find()) {
            printBadValidationMessage("author`s name. It should contains only letters, numbers or underscore and spaces");
            return null;
        }
        return author;
    }

    Integer yearUserInput(){
        try {
            System.out.println("Date of publish:\tYear: ");
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e){
            return null;
        }
    }

    Integer dayUserInput(){
        try {
            System.out.println("Day: ");
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e){
            return null;
        }
    }

    Integer monthUserInput(){
        try {
            System.out.println("Month: ");
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e){
            return null;
        }
    }

    GregorianCalendar getDateDialogue(Integer year, Integer month, Integer day) {
        if (day==null||month==null||year==null) {
            printBadValidationMessage("date");
            return null;
        }
        if (day>31){
            printBadValidationMessage("day. It should be less then 31");
            return null;
        }
        if (month>12){
            printBadValidationMessage("day. It should be less then 12");
            return null;
        }
        if (Librarian.checkItemForValidity(year) && Librarian.checkItemForValidity(month)
                && Librarian.checkItemForValidity(day))
            return new GregorianCalendar(year, month - 1, day);
        printBadValidationMessage("date");
        return null;
    }

    Integer getPagesDialogue(Integer pages) {
        if (pages == null) return null;
        if (!Librarian.checkItemForValidity(pages)) {
            printBadValidationMessage("pages");
            return null;
        }
        return pages;
    }

    Integer pagesUsersInput(){
        try {
            return Integer.parseInt(scan.nextLine().trim());
        } catch(NumberFormatException e){
            return null;
        }
    }

    public void addingDialogue() throws IOException{
        try {
            String author = "";
            GregorianCalendar publishingDate = new GregorianCalendar();
            Integer itemID = getIDDialogue(idUserInput());
            if (itemID!=null) {
                if (!Librarian.checkIDForExistence(itemID,item)) {
                    String title = getTitleDialogue(titleUserInput());
                    if (Librarian.checkItemForValidity(title)) {

                        if (item instanceof Book) {
                            author = getAuthorDialogue(authorUserInput());
                            if (author != null)
                            {
                                publishingDate = getDateDialogue(yearUserInput(),monthUserInput(),dayUserInput());
                            }
                        }
                        System.out.println("Pages:");
                        Integer numOfPages = getPagesDialogue(pagesUsersInput());
                        if (numOfPages != null) {

                            if (item instanceof Book) {
                                Book book = new Book(itemID, title, author, publishingDate, numOfPages);
                                Librarian.addItem(book);
                            } else {
                                Journal journal = new Journal(itemID, title, numOfPages);
                                Librarian.addItem(journal);
                            }
                            printSuccessMessage("added");
                        }
                    }
                } else System.out.println("Item with this ID exists. Please change ID and try again");
            } else printBadValidationMessage("ID");
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private Integer getIDToBorrowDialogue() throws IOException{

        Integer itemID = getIDDialogue(idUserInput());
        if (itemID==null){
            return null;
        }
        if (!Librarian.checkItemForValidity(itemID)) {
            printBadValidationMessage("ID");
            return null;
        }
        if(!Librarian.checkIDForExistence(itemID, item)) {
            System.out.println("There`s no item with such ID");
            return null;
        }
        return itemID;
    }

    public void deletingDialogue() throws IOException{
        Integer itemID = getIDToBorrowDialogue();
        if(itemID!=null) {
            boolean deleted;
            if(item instanceof Book)
            deleted = Librarian.deleteBook(itemID, false);
            else deleted = Librarian.deleteJournal(itemID, false);
            if (deleted) {
                printSuccessMessage("deleted");
            }
        }
    }

    public void borrowingDialogue(boolean borrow) throws IOException{
        Integer itemID = getIDToBorrowDialogue();
        if(itemID!=null) {
            if (item instanceof Book) {
                Librarian.borrowItem(itemID, new Book(), borrow);
            } else Librarian.borrowItem(itemID, new Journal(), borrow);
        }
    }

    public void printListOfItems(List<? extends Item> items, Item item){
        if (items.size()==0) System.out.println("There`s no items here");
        else {
            items.forEach(i -> {
                System.out.print(String.format("%-20s", "Item ID: " + i.getItemID()) + String.format("%-40s", "Title: " + i.getTitle()));
                if (item instanceof Book) {
                    DateFormat df = new SimpleDateFormat("dd.M.y");
                    System.out.print(String.format("%-30s", "Author: " + ((Book) i).getAuthor())
                            + String.format("%-30s", "Date of publish: " + df.format(((Book) i).getPublishingDate().getTime())));
                }
                System.out.print(String.format("%-13s", "Pages: " + i.getPages()) + " Borrowed: " + i.isBorrowed() + "\n");
            });
        }
    }

    private int getSortingVar(){
        try {
            System.out.println("""
            
            Sort by:
            \t1 - Item ID
            \t2 - Title
            \t3 - Pages""");

            if (item instanceof Book) {
                System.out.println("""
                        \t4 - Author
                        \t5 - Publishing date""");
            }

            System.out.println("0 - Return");
            return new Scanner(System.in).nextInt();
        }catch(InputMismatchException e){
            printDefaultMessage();
            return 0;
        }
    }

    public static int getMainMenuVar(){
        try {
            return new Scanner(System.in).nextInt();
        }catch(InputMismatchException e){
            return 0;
        }
    }

    public void sortingDialogue() throws IOException {
        int var = getSortingVar();
        if (item instanceof Book)
            switch(var){
                case 0: break;
                case 1:
                    List<? extends Item > books1 = Librarian.sortingItemsByID(WorkWithFiles.readToBooksList());
                    printListOfItems(books1, new Book());
                    break;
                case 2:
                    List<? extends Item > books2 = Librarian.sortingItemsByTitle(WorkWithFiles.readToBooksList());
                    printListOfItems(books2, new Book());
                    break;
                case 3:
                    List<? extends Item > books3 = Librarian.sortingItemsByPages(WorkWithFiles.readToBooksList());
                    printListOfItems(books3, new Book());
                    break;
                case 4:
                    List<? extends Item > books4 = Librarian.sortingBooksByAuthor(WorkWithFiles.readToBooksList());
                    printListOfItems(books4, new Book());
                    break;
                case 5:
                    List<? extends Item > books5 = Librarian.sortingBooksByPublishingDate(WorkWithFiles.readToBooksList());
                    printListOfItems(books5, new Book());
                    break;
                default: printDefaultMessage();
                    break;
            }
        else switch(var){
            case 0: break;
            case 1:
                List<? extends Item > journals1 = Librarian.sortingItemsByID(WorkWithFiles.readToJournalsList());
                printListOfItems(journals1, new Journal());
                break;
            case 2:
                List<? extends Item > journals2 = Librarian.sortingItemsByTitle(WorkWithFiles.readToJournalsList());
                printListOfItems(journals2, new Journal());
                break;
            case 3:
                List<? extends Item > journals3 = Librarian.sortingItemsByPages(WorkWithFiles.readToJournalsList());
                printListOfItems(journals3, new Journal());
                break;
            default: printDefaultMessage();
                break;
        }
    }

    public static void printItemNotExistsMessage(){
        System.out.println("There`s no such item");
    }

    public static void printBadValidationMessage(String item){
        System.out.println("Please, input valid " + item);
    }

    public static void printSuccessMessage(String item){
        System.out.println("The item is successfully " + item);
    }

    public static void printDefaultMessage(){
        System.out.println("Input the proposed option");
    }

}
