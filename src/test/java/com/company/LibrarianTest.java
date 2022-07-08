package com.company;

import com.company.server.ServerHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LibrarianTest {

    Librarian booksLibrarian = new Librarian(new WorkWithFiles("books_test"), new ServerHandler(new Scanner(System.in),new PrintWriter(System.out)));
    Librarian journalsLibrarian = new Librarian(new WorkWithFiles("journals_test"), new ServerHandler(new Scanner(System.in),new PrintWriter(System.out)));
    Librarian bothItemsLibrarian = new Librarian(new WorkWithFiles("test"), new ServerHandler(new Scanner(System.in),new PrintWriter(System.out)));

    Book firstBook = new Book(101, "First Book", "First Author", new GregorianCalendar(2020, Calendar.DECEMBER,10),510);
    Book secondBook = new Book(102, "Second Book", "Second Author", new GregorianCalendar(2021,Calendar.APRIL,21),924);
    Book thirdBook = new Book(103,"Third Book", "Third Author", new GregorianCalendar(2002,Calendar.MAY,10),783);

    Journal firstJournal = new Journal(101,"First Journal",95);
    Journal secondJournal = new Journal(102, "Second Journal", 96);
    Journal thirdJournal = new Journal(103, "Existing Journal", 96);

    List<Book> books = new ArrayList<>();
    List<Journal> journals = new ArrayList<>();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    {
        books.add(firstBook);
        books.add(secondBook);
        books.add(thirdBook);

        journals.add(firstJournal);
        journals.add(secondJournal);
        journals.add(thirdJournal);
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
    void readToList() throws IOException {
        ContainerTest containerTest = new ContainerTest();
        containerTest.readToList();
    }

    @Test
    void addItemsToAllFilesTest() throws IOException {

        booksLibrarian.addItem(firstBook);
        booksLibrarian.addItem(secondBook);

        journalsLibrarian.addItem(firstJournal);
        journalsLibrarian.addItem(secondJournal);
        bothItemsLibrarian.addItem(thirdBook);
        bothItemsLibrarian.addItem(thirdJournal);
    }

    @Test
    void deleteItemsTest() throws IOException {
        assertFalse(bothItemsLibrarian.deleteItem(105, false,"Book"));
        assertFalse(bothItemsLibrarian.deleteItem(103, false,"Journal"));
        assertFalse(booksLibrarian.deleteItem(102,false, "Book"));
        assertTrue(journalsLibrarian.deleteItem(101,false, "Journal"));
    }

    @Test
    void borrowItemTest() throws IOException {
        booksLibrarian.borrowItem(102,"Book",true);
        journalsLibrarian.borrowItem(102,"Journal",true);
    }


    @Test
    void sortingItemsTest(){
        booksLibrarian.sortingItemsByID(books).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        journalsLibrarian.sortingItemsByID(journals).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        System.out.println();
        booksLibrarian.sortingItemsByTitle(books).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        journalsLibrarian.sortingItemsByTitle(journals).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        System.out.println();
        booksLibrarian.sortingItemsByPages(books).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        journalsLibrarian.sortingItemsByPages(journals).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        System.out.println();
        booksLibrarian.sortingBooksByAuthor(books).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
        System.out.println();
        booksLibrarian.sortingBooksByPublishingDate(books).forEach(i->System.out.println(i.getItemID() + " - " + i.getTitle() + " - " + i.getPages()));
    }



}
