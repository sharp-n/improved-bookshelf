package com.company;

import com.company.handlers.BookHandler;
import com.company.handlers.JournalHandler;
import com.company.handlers.NewspaperHandler;
import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
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
    static Book book3 = new Book(1005,"title","Some author",new GregorianCalendar(1991,Calendar.MAY,2),2736);

    static Journal journal1 = new Journal(5,"Some title",824);
    static Journal journal2 = new Journal(666,"Any title",500);
    static Journal journal3 = new Journal(1005,"title",2736);

    static Newspaper newspaper1 = new Newspaper(5,"Some title",824);
    static Newspaper newspaper2 = new Newspaper(666,"Any title",500);
    static Newspaper newspaper3 = new Newspaper(1005,"title",2736);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    PrintWriter printWriter = new PrintWriter(printStream, true);

    @ParameterizedTest
    @MethodSource("provideTable")
    void defineNumberOfColumnsTest(List<List<String>> table, int expected){
        Librarian librarian = new Librarian();
        int provided = librarian.defineNumberOfColumns(table);
        assertEquals(expected,provided);
    }

    private static Stream<Arguments> provideTable(){

        List<List<String>> table1 = new ArrayList<>(Arrays.asList(
                new BookHandler().itemToString(book1),
                new BookHandler().itemToString(book2),
                new JournalHandler().itemToString(journal1)));
        List<List<String>> table2 = new ArrayList<>();
        table2.add(new NewspaperHandler().itemToString(newspaper2));
        table2.add(new JournalHandler().itemToString(journal1));

        List<List<String>> table3 = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("1", "2","3","4")),
                new ArrayList<>(Arrays.asList("1", "2")),
                new ArrayList<>(Arrays.asList("1", "2","3","4","5")),
                new ArrayList<>(Arrays.asList("1", "2","3","4"))
        ));

        List<List<String>> table4 = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Collections.emptyList()),
                new ArrayList<>(Collections.singletonList("1"))
        ));

        return Stream.of(
                Arguments.of(table1,6),
                Arguments.of(table2,4),
                Arguments.of(table3,5),
                Arguments.of(table4,1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNumberOfColumns")
    void generateOptionsForTableTest(int provided, int expected){
        Librarian librarian = new Librarian();
        int size = librarian.generateOptionsForTable(provided).size();
        assertEquals(expected,size);
    }

    private static Stream<Arguments> provideNumberOfColumns(){
        return Stream.of(
                Arguments.of(6, new ConstantsForItemsTable().columnsForBooks.size()),
                Arguments.of(4, new ConstantsForItemsTable().columnsForJournals.size()),
                Arguments.of(6, new ConstantsForItemsTable().columnsForAllItems.size())
        );
    }

    @ParameterizedTest
    @MethodSource("provideTablesForPrinting")
    void printItemsTest(List<Item> provided, String expected){
        Librarian librarian = new Librarian(new OneFileWorker("test", "test"),printWriter);
        librarian.printItems(provided, new BookHandler());
        assertEquals(expected,outputStream.toString());
    }

    private static Stream<Arguments> provideTablesForPrinting(){
        return Stream.of(
                Arguments.of(new ArrayList<>(Collections.emptyList()), "There`s no items here" + System.lineSeparator()),

                Arguments.of(new ArrayList<>(Arrays.asList(book1, book2, journal1)),
                        " =ITEM ID= | =TITLE=    | =AUTHOR= | =PUBLISHING DATE= | =PAGES= | =BORROWED= " +
                                "-----------+------------+----------+-------------------+---------+------------" + System.lineSeparator() +
                                " 5         | Some title | Author   | 02.5.2002         | 824     | false      " +System.lineSeparator() +
                                " 666       | Any title  | unknown  | 02.4.2002         | 500     | false      " + System.lineSeparator() +
                                " 5         | Some title | 824      | false             | NULL    | NULL       " + System.lineSeparator())
        );
    }

    @ParameterizedTest
    @MethodSource("provideIDToFind")
    void findItemByIDTest(int providedID, Item expected){
        List<Item> items = new ArrayList<>();
        items.add(book1);
        items.add(newspaper2);
        items.add(journal3);
        Item item = Librarian.findItemByID(providedID,items);
        assertEquals(expected,item);
    }

    private static Stream<Arguments> provideIDToFind(){
        return Stream.of(
                Arguments.of(5,book1),
                Arguments.of(666,newspaper2),
                Arguments.of(1005,journal3),
                Arguments.of(21,null));
    }

    @ParameterizedTest
    @MethodSource("provideIDToDelete")
    void deleteItemTest(int providedID) throws IOException {
        Librarian librarian = new Librarian(new OneFileWorker("test_deleting", "test"),printWriter);
        librarian.workWithFiles.addItemToFile(book1);
        librarian.workWithFiles.addItemToFile(newspaper2);
        librarian.workWithFiles.addItemToFile(journal3);
        assertTrue(librarian.deleteItem(providedID,false));
    }

    private static Stream<Arguments> provideIDToDelete(){
        return Stream.of(
                Arguments.of(5,book1),
                Arguments.of(666,newspaper2),
                Arguments.of(1005,journal3));
    }

    @ParameterizedTest
    @MethodSource("provideIDToDelete")
    void borrowItemTest(int providedID,Item item) throws IOException {
        Librarian librarian = new Librarian(new OneFileWorker("test_borrowing","test"),printWriter);
        librarian.workWithFiles.addItemToFile(item);
        librarian.borrowItem(providedID,true,new JournalHandler());
        item = librarian.workWithFiles.readToItemsList().stream().filter(o->o.getItemID()==providedID).collect(Collectors.toList()).get(0);
        assertTrue(item.isBorrowed());
    }

}
