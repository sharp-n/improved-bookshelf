package com.company;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Dialogues {

    private static String getTitleDialogue(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Title:");
        String title = scanner.nextLine().trim();
        if (!Librarian.checkItemForValidity(title)) {
            printBadValidationMessage("title");
            return null;
        }
        else return title;
    }

    private static Integer getIDDialogue(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Item ID:");
            int id = scanner.nextInt();
            if (Librarian.checkItemForValidity(id))
                return id;
            printBadValidationMessage("ID");
            return null;
        } catch (InputMismatchException e){
            return null;
        }
    }

    private static String getAuthorDialogue(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Author:");
        String author = scanner.nextLine().trim();

        if (!Librarian.checkItemForValidity(author)){
            printBadValidationMessage("author name");
            return null;
        }
        return author;
    }

    private static GregorianCalendar getDateDialogue(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Date of publish:\n" + "\tYear: ");
            int publishingYear = scanner.nextInt();
            System.out.print("\tMonth: ");
            int publishingMonth = scanner.nextInt();
            System.out.print("\tDay: ");
            int publishingDay = scanner.nextInt();
            if (Librarian.checkItemForValidity(publishingYear) && Librarian.checkItemForValidity(publishingMonth)
                    && Librarian.checkItemForValidity(publishingDay))
                return new GregorianCalendar(publishingYear, publishingMonth - 1, publishingDay);
            printBadValidationMessage("date");
            return null;
        } catch (InputMismatchException e){
            printBadValidationMessage("date");
            return null;
        }
    }

    private static Integer getPagesDialogue(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Pages:");
            int pages = scanner.nextInt();
            if (!Librarian.checkItemForValidity(pages)) {
                printBadValidationMessage("pages");
                return null;
            }
            return pages;
        } catch(InputMismatchException e){
            printBadValidationMessage("pages");
            return null;
        }
    }

    public static boolean addingDialogue(String filePath){
        try {
            String author = "";
            GregorianCalendar publishingDate = new GregorianCalendar();
            Integer itemID = getIDDialogue();
            if (itemID==null) {
                printBadValidationMessage("ID");
                return true;
            }
            if(Librarian.checkIDForExistence((int)itemID,filePath)) {
                System.out.println("Item with this ID exists. Please change ID and try again");
                return true;
            }


            String title = getTitleDialogue();
            if (!Librarian.checkItemForValidity(title)) return true;

            if(filePath.equals(Book.getFilePath())){
                author = getAuthorDialogue();
                if (author==null) return true;
                publishingDate = getDateDialogue();
                if (publishingDate==null) return true;
            }

            Integer numOfPages = getPagesDialogue();
            if (numOfPages==null) return true;

            if(filePath.equals(Book.getFilePath())){
                Book book = new Book(itemID, title, author, publishingDate, numOfPages);
                Librarian.addItem(book,Book.getFilePath());
            } else {
                Journal journal = new Journal(itemID,title,numOfPages);
                Librarian.addItem(journal,Journal.getFilePath());
            }
            printSuccessMessage("added");
            return true;
        } catch (NullPointerException e){
            e.printStackTrace();
            return true;
        }
    }

    private static Integer getIDToBorrowDialogue(String filePath){
        Integer itemID = getIDDialogue();
        if (itemID==null){
            return null;
        }
        if (!Librarian.checkItemForValidity((int)itemID)) {
            printBadValidationMessage("ID");
            return null;
        }
        if(!Librarian.checkIDForExistence(itemID,filePath)) {
            System.out.println("There`s no item with such ID");
            return null;
        }
        return itemID;
    }

    public static boolean deletingDialogue(String filePath){
        Integer itemID = getIDToBorrowDialogue(filePath);
        if(itemID==null) return true;
        boolean deleted = Librarian.deleteItem(itemID,filePath, false);
        if (!deleted) return true;
        printSuccessMessage("deleted");
        return true;
    }

    public static boolean takingDialogue(String filePath){
        Integer itemID = getIDToBorrowDialogue(filePath);
        if(itemID==null) return true;
        if (filePath.equals(Book.getFilePath()))
            Librarian.borrowItem(itemID,filePath,new Book(),true);
        else Librarian.borrowItem(itemID,filePath,new Journal(),true);
        return true;
    }

    public static boolean returningDialogue(String filePath){
        Integer itemID = getIDToBorrowDialogue(filePath);
        if(itemID==null) return true;
        if (filePath.equals(Book.getFilePath()))
            Librarian.borrowItem(itemID,filePath,new Book(),false);
        else Librarian.borrowItem(itemID,filePath,new Journal(),false);
        return true;
    }

    public static void printListOfItems(List<? extends Item> items, Item item){
        if (items.size()==0) System.out.println("There`s no items here");
        else {
            items.forEach(i -> {
                System.out.print("Item ID: " + i.getItemID() + ", Title: " + i.getTitle());
                if (item instanceof Book) {
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                    System.out.print(", Author: " + ((Book) i).getAuthor()
                            + ", Date of publish: " + df.format(((Book) i).getPublishingDate().getTime()));
                }
                System.out.print(", Pages: " + i.getPages() + ", Borrowed: " + i.isBorrowed() + "\n");
            });
        }
    }

    private static int getSortingVar(String filePath){
        try {
            System.out.println("Sort by:\n\t1 - Item ID\n\t2 - Title\n\t3 - Pages");
            if (filePath.equals(Book.getFilePath()))
                System.out.println("\t4 - Author\n\t5 - Publishing date");
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

    public static boolean sortingDialogue(String filePath){
        int var = getSortingVar(filePath);
        if (filePath.equals(Book.getFilePath()))
            return switch(var){
                case 0: yield true;
                case 1:
                    List<? extends Item > books1 = Librarian.sortingItemsByID(Librarian.convertBookListFromGson(WorkWithFiles.readToList(filePath)));
                    printListOfItems(books1, new Book());
                    yield true;
                case 2:
                    List<? extends Item > books2 = Librarian.sortingItemsByTitle(Librarian.convertBookListFromGson(WorkWithFiles.readToList(filePath)));
                    printListOfItems(books2, new Book());
                    yield true;
                case 3:
                    List<? extends Item > books3 = Librarian.sortingItemsByPages(Librarian.convertBookListFromGson(WorkWithFiles.readToList(filePath)));
                    printListOfItems(books3, new Book());
                    yield true;
                case 4:
                    List<? extends Item > books4 = Librarian.sortingBooksByAuthor(Librarian.convertBookListFromGson(WorkWithFiles.readToList(filePath)));
                    printListOfItems(books4, new Book());
                    yield true;
                case 5:
                    List<? extends Item > books5 = Librarian.sortingBooksByPublishingDate(Librarian.convertBookListFromGson(WorkWithFiles.readToList(filePath)));
                    printListOfItems(books5, new Book());
                    yield true;
                default: yield  printDefaultMessage();
            };
        else return switch(var){
            case 0: yield true;
            case 1:
                List<? extends Item > journals1 = Librarian.sortingItemsByID(Librarian.convertJournalListFromGson(WorkWithFiles.readToList(filePath)));
                printListOfItems(journals1, new Journal());
                yield true;
            case 2:
                List<? extends Item > journals2 = Librarian.sortingItemsByTitle(Librarian.convertJournalListFromGson(WorkWithFiles.readToList(filePath)));
                printListOfItems(journals2, new Journal());
                yield true;
            case 3:
                List<? extends Item > journals3 = Librarian.sortingItemsByPages(Librarian.convertJournalListFromGson(WorkWithFiles.readToList(filePath)));
                printListOfItems(journals3, new Journal());
                yield true;
            default: yield printDefaultMessage();
        };
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

    public static boolean printDefaultMessage(){
        System.out.println("Input the proposed option");
        return true;
    }

}
