package com.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class WorkWithFilesTest {

    Librarian booksLibrarian = new Librarian(WorkWithFiles.BOOK_FILE_PATH);
    Librarian journalsLibrarian = new Librarian(WorkWithFiles.BOOK_FILE_PATH);

    Book secondBook = new Book(101, "Second Book", "Second Author", new GregorianCalendar(2021, Calendar.APRIL,21),924);

    @Test
    void readToBooksListTest() throws IOException {
        WorkWithFiles.readToBooksList(WorkWithFiles.SINGLE_FILE_PATH).forEach(b->System.out.println(b.getItemID() + " - " + b.getTitle() + " - " + b.getAuthor()));
    }

    @Test
    void readToJournalsListTest() throws IOException {
        WorkWithFiles.readToJournalsList(WorkWithFiles.SINGLE_FILE_PATH).forEach(b->System.out.println(b.getItemID() + " - " + b.getTitle()));
    }

    @Test
    void readToContainersListTest() throws IOException {
        WorkWithFiles.readToContainersList(WorkWithFiles.SINGLE_FILE_PATH).forEach(b->System.out.println(b.item + " - " + b.typeOfClass));
    }

    @Test
    void deleteItemFromFileTest() throws IOException {
        Assertions.assertTrue(booksLibrarian.deleteItem(101,false,"Book"));
        Assertions.assertFalse(booksLibrarian.deleteItem(105, false, "Book"));
    }

    @Test
    void addItemToFileTest() throws IOException {
        WorkWithFiles.addItemToFile(new Container<>(secondBook), WorkWithFiles.SINGLE_FILE_PATH);
    }

}
