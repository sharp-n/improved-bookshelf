package com.company;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor
public class Dialogues {

    static Scanner scan;
    Item item;

    Librarian librarian;

    public static void setScan(Scanner scan) {
        Dialogues.scan = scan;
    }

    String titleUserInput(){
        System.out.println("Title:");
        return scan.nextLine().trim();
    }

    String validateTitle(String title){
        Pattern pattern= Pattern.compile("[\t|/<>~\\\\]");
        Matcher matcher = pattern.matcher(title);
        if (!Librarian.checkItemForValidity(title)||matcher.find()) {
            printBadValidationMessage("title. It should not contains such symbols as \\t, |, /, <, > ~ or \\");
            return null;
        }
        else return title;
    }

    String usernameInput() {
        System.out.println("\nInput your name. If you want to use default folders(s) write \"default\"");
        return scan.nextLine().trim();
    }

    String usernameValidation(String userName){
        boolean validUserName = User.checkUserNameForValidity(userName);
        if (validUserName) {
            return userName;
        }
        return null;
    }

    Integer idUserInput(){
        try {
            System.out.println("Item ID:");
            return Integer.parseInt(scan.nextLine().trim());
        }  catch (NumberFormatException e){
            return null;
        }
    }

    Integer validateID(Integer id) {
        if (id==null) {return null;}
        if (Librarian.checkItemForValidity(id)) {
            return id;
        }
        printBadValidationMessage("ID. It should be a number (>0)");
        return null;
    }

    String authorUserInput(){
        System.out.println("Author:");
        return scan.nextLine().trim();
    }

    String validateAuthorName(String author) {
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
            System.out.println("Date of publish:\n\tYear: ");
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e){
            return null;
        }
    }

    Integer dayUserInput(){
        try {
            System.out.println("\tDay: ");
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e){
            return null;
        }
    }

    Integer monthUserInput(){
        try {
            System.out.println("\tMonth: ");
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e){
            return null;
        }
    }

    GregorianCalendar validateDate(Integer year, Integer month, Integer day) {
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

    Integer validatePages(Integer pages) {
        if (pages == null) {return null;}
        if (!Librarian.checkItemForValidity(pages)) {
            printBadValidationMessage("pages");
            return null;
        }
        if (pages >5000){
            printBadValidationMessage("pages. It should be less then 5000");
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
            String typeOfItem;
            if(item instanceof Book) typeOfItem = "Book";
            else typeOfItem = "Journal";
            boolean add = true;
            String author = "";
            GregorianCalendar publishingDate = new GregorianCalendar();
            Integer itemID = validateID(idUserInput());
            if (itemID!=null) {
                if (!librarian.checkIDForExistence(itemID,typeOfItem)) {
                    String title = validateTitle(titleUserInput());
                    if (Librarian.checkItemForValidity(title)) {

                        if (item instanceof Book) {
                            author = validateAuthorName(authorUserInput());
                            if (author != null)
                            {
                                publishingDate = validateDate(yearUserInput(),monthUserInput(),dayUserInput());
                                if (publishingDate==null){
                                    System.out.println("Try again");
                                    add=false;
                                }
                            }
                        }
                        if (add) {
                            System.out.println("Pages:");
                            Integer numOfPages = validatePages(pagesUsersInput());
                            if (numOfPages != null) {

                                if (item instanceof Book) {
                                    Book book = new Book(itemID, title, author, publishingDate, numOfPages);
                                    librarian.addItem(book);
                                } else {
                                    Journal journal = new Journal(itemID, title, numOfPages);
                                    librarian.addItem(journal);
                                }
                                printSuccessMessage("added");
                            }
                        }
                    }
                } else System.out.println("Item with this ID exists. Please change ID and try again");
            } else printBadValidationMessage("ID. It should be a number (>0)");
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private Integer validateIdToBorrow() throws IOException{

        Integer itemID = validateID(idUserInput());
        if (itemID==null){
            return null;
        }
        if (!Librarian.checkItemForValidity(itemID)) {
            printBadValidationMessage("ID. It should be a number (>0)");
            return null;
        }
        String typeOfItem;
        if(item instanceof Book) typeOfItem = "Book";
        else typeOfItem = "Journal";
        if(!librarian.checkIDForExistence(itemID, typeOfItem)) {
            System.out.println("There`s no item with such ID");
            return null;
        }
        return itemID;
    }

    public void deletingDialogue() throws IOException{
        Integer itemID = validateIdToBorrow();
        if(itemID!=null) {
            boolean deleted;
            if(item instanceof Book)
            deleted = librarian.deleteItem(itemID, false, "Book");
            else deleted = librarian.deleteItem(itemID, false, "Journal");
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
            } else librarian.borrowItem(itemID, "Journal", borrow);
        }
    }

    public void printListOfItems(List<? extends Item> items){
        if (items.size()==0) System.out.println("There`s no items here");
        else {
            System.out.print(String.format("%-11s", "\n ITEM ID") + String.format("%-40s", "| TITLE"));
            if (item instanceof Book) System.out.print(String.format("%-32s", "| AUTHOR")+String.format("%-18s", "| PUBLISHING DATE"));
            System.out.print(String.format("%-8s", "| PAGES")+String.format("%-30s", "| " +
                    "BORROWED") );
            System.out.print("\n----------|---------------------------------------");
            if (item instanceof Book) System.out.print("|-------------------------------|-----------------");
            System.out.println("|-------|----------");
            items.forEach(i -> {
                System.out.print(String.format("%-10s", " " + i.getItemID()) + String.format("%-40s", "| " + i.getTitle()));
                if (item instanceof Book) {
                    DateFormat df = new SimpleDateFormat("dd.M.y");
                    System.out.print(String.format("%-32s", "| " + ((Book) i).getAuthor())
                            + String.format("%-18s", "| " + df.format(((Book) i).getPublishingDate().getTime())));
                }
                System.out.print(String.format("%-8s", "| " + i.getPages()) + "| " + i.isBorrowed() + "\n");
            });
        }
    }

    private int getSortingVar(){
        try {
            System.out.println("Sort by:\n\t1 - Item ID\n\t2 - Title\n\t3 - Pages");
            if (item instanceof Book) {
                System.out.println("\t4 - Author\n\t5 - Publishing date");
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
                    List<? extends Item > books1 = librarian.sortingItemsByID(WorkWithFiles.readToBooksList(librarian.filePath));
                    printListOfItems(books1);
                    break;
                case 2:
                    List<? extends Item > books2 = librarian.sortingItemsByTitle(WorkWithFiles.readToBooksList(librarian.filePath));
                    printListOfItems(books2);
                    break;
                case 3:
                    List<? extends Item > books3 = librarian.sortingItemsByPages(WorkWithFiles.readToBooksList(librarian.filePath));
                    printListOfItems(books3);
                    break;
                case 4:
                    List<? extends Item > books4 = librarian.sortingBooksByAuthor(WorkWithFiles.readToBooksList(librarian.filePath));
                    printListOfItems(books4);
                    break;
                case 5:
                    List<? extends Item > books5 = librarian.sortingBooksByPublishingDate(WorkWithFiles.readToBooksList(librarian.filePath));
                    printListOfItems(books5);
                    break;
                default: printDefaultMessage();
                    break;
            }
        else switch(var){
            case 0: break;
            case 1:
                List<? extends Item > journals1 = librarian.sortingItemsByID(WorkWithFiles.readToJournalsList(librarian.filePath));
                printListOfItems(journals1);
                break;
            case 2:
                List<? extends Item > journals2 = librarian.sortingItemsByTitle(WorkWithFiles.readToJournalsList(librarian.filePath));
                printListOfItems(journals2);
                break;
            case 3:
                List<? extends Item > journals3 = librarian.sortingItemsByPages(WorkWithFiles.readToJournalsList(librarian.filePath));
                printListOfItems(journals3);
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
