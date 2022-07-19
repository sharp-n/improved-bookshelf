package com.company;

import com.company.items.Book;
import com.company.server.ServerHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class WorkWithFilesTest {

    Librarian booksLibrarian = new Librarian(new WorkWithFiles("books_test"), new ServerHandler(new Scanner(System.in), new PrintWriter(System.out)));
    Librarian journalsLibrarian = new Librarian(new WorkWithFiles("journals_test"), new ServerHandler(new Scanner(System.in), new PrintWriter(System.out)));
    WorkWithFiles workWithFiles = new WorkWithFiles("test");

    Book secondBook = new Book(101, "Second Book", "Second Author", new GregorianCalendar(2021, Calendar.APRIL,21),924);

    @Test
    void readToBooksListTest() throws IOException {
        workWithFiles.readToBooksList().forEach(b->System.out.println(b.getItemID() + " - " + b.getTitle() + " - " + b.getAuthor()));
    }

    @Test
    void readToJournalsListTest() throws IOException {
        workWithFiles.readToJournalsList().forEach(b->System.out.println(b.getItemID() + " - " + b.getTitle()));
    }

    @Test
    void deleteItemFromFileTest() throws IOException {
        Assertions.assertTrue(booksLibrarian.deleteItem(101,false,"Book"));
        Assertions.assertFalse(booksLibrarian.deleteItem(105, false, "Book"));
    }

    @Test
    void addItemToFileTest() throws IOException {
        workWithFiles.addItemToFile(secondBook);
    }

}
