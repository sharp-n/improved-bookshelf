package com.company;

import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.items.Newspaper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

class LibrarianTest {
    //TODO create tests
    Librarian booksLibrarian = new Librarian(new WorkWithFiles("books_test"),new PrintWriter(System.out, true));
    Librarian journalsLibrarian = new Librarian(new WorkWithFiles("journals_test"),new PrintWriter(System.out, true));
    Librarian newspapersLibrarian = new Librarian(new WorkWithFiles("newspapers_test"),new PrintWriter(System.out, true));
    Librarian allItemsLibrarian = new Librarian(new WorkWithFiles("test"),new PrintWriter(System.out, true));

    Book firstBook = new Book(101, "First Book", "First Author", new GregorianCalendar(2020, Calendar.DECEMBER,10),510);
    Book secondBook = new Book(102, "Second Book", "Second Author", new GregorianCalendar(2021,Calendar.APRIL,21),924);
    Book thirdBook = new Book(103,"Third Book", "Third Author", new GregorianCalendar(2002,Calendar.MAY,10),783);

    Journal firstJournal = new Journal(101,"First Journal",95);
    Journal secondJournal = new Journal(105, "Second Journal", 96);
    Journal thirdJournal = new Journal(103, "Existing Journal", 96);

    Newspaper firstNewspaper = new Newspaper(101,"First Newspaper",95);
    Newspaper secondNewspaper = new Newspaper(105, "Second Newspaper", 96);
    Newspaper thirdNewspaper = new Newspaper(103, "Existing Newspaper", 96);

    List<Item> books = new ArrayList<>();
    List<Item> journals = new ArrayList<>();
    List<Item> newspapers = new ArrayList<>();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    {
        books.add(firstBook);
        books.add(secondBook);
        books.add(thirdBook);

        journals.add(firstJournal);
        journals.add(secondJournal);

        newspapers.add(firstNewspaper);
        newspapers.add(secondNewspaper);
        newspapers.add(thirdNewspaper);

    }

    @Test
    void writeToList() {
        try {
            List<Container> containers = new ArrayList<>();
            containers.add(new Container<>(firstBook));
            containers.add(new Container<>(secondBook));
            containers.add(new Container<>(firstJournal));
            FileWriter fw = new FileWriter(System.getProperty("user.home") + "\\testItems.txt", false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(gson.toJson(containers));
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void addItemsToAllFilesTest() throws IOException {

        booksLibrarian.addItem(firstBook);
        booksLibrarian.addItem(secondBook);
        allItemsLibrarian.addItem(thirdBook);

        journalsLibrarian.addItem(firstJournal);
        journalsLibrarian.addItem(secondJournal);
        allItemsLibrarian.addItem(thirdJournal);

        newspapersLibrarian.addItem(firstNewspaper);
        newspapersLibrarian.addItem(secondNewspaper);
        allItemsLibrarian.addItem(thirdNewspaper);
    }

//    @Test
//    void deleteItemsTest() throws IOException {
//        assertFalse(bothItemsLibrarian.deleteItem(105, false,"Book"));
//        assertFalse(bothItemsLibrarian.deleteItem(103, false,"Journal"));
//        assertFalse(booksLibrarian.deleteItem(102,false, "Book"));
//        assertFalse(journalsLibrarian.deleteItem(101,false, "Journal"));
//    }

    @Test
    void borrowItemTest() throws IOException {
        booksLibrarian.borrowItem(102, true);
        journalsLibrarian.borrowItem(102, true);
        newspapersLibrarian.borrowItem(102, true);
    }

    @Test
    void sortingItemsTest(){
        booksLibrarian.sortingItemsByID(books).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        journalsLibrarian.sortingItemsByID(journals).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        newspapersLibrarian.sortingItemsByID(journals).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        System.out.println();
        //booksLibrarian.sortingItemsByTitle(books).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        //journalsLibrarian.sortingItemsByTitle(journals).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        //newspapersLibrarian.sortingItemsByTitle(journals).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        System.out.println();
        booksLibrarian.sortingItemsByPages(books).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        journalsLibrarian.sortingItemsByPages(journals).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        newspapersLibrarian.sortingItemsByPages(journals).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        System.out.println();
        //booksLibrarian.sortingBooksByAuthor(books).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        System.out.println();
        //booksLibrarian.sortingBooksByPublishingDate(books).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
    }



}
