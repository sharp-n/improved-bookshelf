package com.company;

import com.company.handlers.DefaultLibrarian;
import com.company.handlers.item_handlers.BookHandler;
import com.company.handlers.Librarian;
import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Newspaper;
import com.company.work_with_files.OneFileWorker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LibrarianTest {

    static Book book1 = new Book(5,"Some title","Author",new GregorianCalendar(2002, Calendar.MAY,2),824);
    static Book book2 = new Book(666,"Any title","unknown",new GregorianCalendar(2002,Calendar.APRIL,2),500);



     static Newspaper newspaper2 = new Newspaper(666,"Any title",500);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    PrintWriter printWriter = new PrintWriter(printStream, true);


    @ParameterizedTest
    @MethodSource("provideTablesForPrinting")
    void printItemsTest(List<Item> provided, String expected){
        Librarian librarian = new DefaultLibrarian(new OneFileWorker("test", "test"),printWriter);
        librarian.printItems(provided, new BookHandler());
        assertEquals(expected,outputStream.toString());
    }

    private static Stream<Arguments> provideTablesForPrinting(){
        return Stream.of(
                Arguments.of(new ArrayList<>(Collections.emptyList()), "There`s no items here" + System.lineSeparator()),

                Arguments.of(new ArrayList<>(Arrays.asList(book1, book2)),
                        " =ITEM ID= | =TITLE=    | =AUTHOR= | =PUBLISHING DATE= | =PAGES= | =BORROWED= " +
                                "-----------+------------+----------+-------------------+---------+------------" + System.lineSeparator() +
                                " 5         | Some title | Author   | 02.5.2002         | 824     | false      " +System.lineSeparator() +
                                " 666       | Any title  | unknown  | 02.4.2002         | 500     | false      " + System.lineSeparator())
        );
    }

    @ParameterizedTest
    @MethodSource("provideIDToFind")
    void findItemByIDTest(int providedID, Item expected){
        List<Item> items = new ArrayList<>();
        items.add(book1);
        items.add(newspaper2);
        //items.add(journal3);
        Item item = Librarian.findItemByID(providedID,items);
        assertEquals(expected,item);
    }

    private static Stream<Arguments> provideIDToFind(){
        return Stream.of(
                Arguments.of(5,book1),
                Arguments.of(666,newspaper2),
                //Arguments.of(1005,journal3),
                Arguments.of(21,null));
    }

    @ParameterizedTest
    @MethodSource("provideIDToDelete")
    void deleteItemTest(int providedID) throws IOException {
        Librarian librarian = new DefaultLibrarian(new OneFileWorker("test_deleting", "test"),printWriter);
        librarian.workWithFiles.addItemToFile(book1);
        librarian.workWithFiles.addItemToFile(newspaper2);
        //librarian.workWithFiles.addItemToFile(journal3);
        assertTrue(librarian.deleteItem(providedID,false));
    }

    private static Stream<Arguments> provideIDToDelete(){
        return Stream.of(
                Arguments.of(5,book1),
                Arguments.of(666,newspaper2));
                //Arguments.of(1005,journal3));
    }

    @ParameterizedTest
    @MethodSource("provideIDToDelete")
    void borrowItemTest(int providedID,Item item) throws IOException {
        Librarian librarian = new DefaultLibrarian(new OneFileWorker("test_borrowing","test"),printWriter);
        librarian.workWithFiles.addItemToFile(item);
        //librarian.borrowItem(providedID,true,new JournalHandler());
        item = librarian.workWithFiles.readToItemsList().stream().filter(o->o.getItemID()==providedID).collect(Collectors.toList()).get(0);
        assertTrue(item.isBorrowed());
    }

}
