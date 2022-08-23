package com.company;

import com.company.handlers.DefaultLibrarian;
import com.company.handlers.Librarian;
import com.company.items.Book;
import com.company.work_with_files.FilesWorker;
import com.company.work_with_files.OneFileWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

class WorkWithFilesTest {

    //TODO create tests

    Librarian booksLibrarian = new DefaultLibrarian(new OneFileWorker("books_test", "books_test"), new PrintWriter(System.out));
    FilesWorker workWithFiles = new OneFileWorker("test", "workWithFilesTest");

    Book secondBook = new Book(101, "Second Book", "Second Author", new GregorianCalendar(2021, Calendar.APRIL,21),924);

    @Test
    void readToBooksListTest() throws IOException {
        //workWithFiles.readToBooksList().forEach(b->System.out.println(b.getItemID() + " - " + b.getTitle() + " - " + b.getAuthor()));
    }

    @Test
    void readToJournalsListTest() throws IOException {
        //workWithFiles.readToJournalsList().forEach(b->System.out.println(b.getItemID() + " - " + b.getTitle()));
    }

    @Test
    void deleteItemFromFileTest() throws IOException {
        Assertions.assertTrue(booksLibrarian.deleteItem(101,false));
        Assertions.assertFalse(booksLibrarian.deleteItem(105, false));
    }

    @Test
    void addItemToFileTest() throws IOException {
        workWithFiles.addItemToFile(secondBook);
    }

}
