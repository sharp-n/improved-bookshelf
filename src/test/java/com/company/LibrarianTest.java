package com.company;

import com.company.convertors.BookConvertor;
import com.company.convertors.JournalConvertor;
import com.company.convertors.NewspaperConvertor;
import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.items.Newspaper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LibrarianTest {
    //TODO create tests

    static Book book1 = new Book(101, "First Book", "First Author", new GregorianCalendar(2020, Calendar.DECEMBER,10),510);
    static Book book2 = new Book(102, "Second Book", "Second Author", new GregorianCalendar(2021,Calendar.APRIL,21),924);
    static Book book3 = new Book(103,"Third Book", "Third Author", new GregorianCalendar(2002,Calendar.MAY,10),783);

    static Journal journal1 = new Journal(101,"First Journal",95);
    static Journal journal2 = new Journal(105, "Second Journal", 96);
    static Journal journal3 = new Journal(103, "Existing Journal", 96);

    static Newspaper newspaper1 = new Newspaper(101,"First Newspaper",95);
    static Newspaper newspaper2 = new Newspaper(105, "Second Newspaper", 96);
    static Newspaper newspaper3 = new Newspaper(103, "Existing Newspaper", 96);

    List<Item> books = new ArrayList<>();
    List<Item> journals = new ArrayList<>();
    List<Item> newspapers = new ArrayList<>();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    PrintWriter printWriter = new PrintWriter(printStream, true);


    {
        books.add(book1);
        books.add(book2);
        books.add(book3);

        journals.add(journal1);
        journals.add(journal2);

        newspapers.add(newspaper1);
        newspapers.add(newspaper2);
        newspapers.add(newspaper3);

    }

    @ParameterizedTest
    @MethodSource("provideTable")
    void defineNumberOfColumnsTest(List<List<String>> table, int expected){
        Librarian librarian = new Librarian();
        int provided = librarian.defineNumberOfColumns(table);
        assertEquals(expected,provided);
    }

    private static Stream<Arguments> provideTable(){

        List<List<String>> table1 = new ArrayList<>(Arrays.asList(
                new BookConvertor(book1).itemToString(),
                new BookConvertor(book2).itemToString(),
                new JournalConvertor(journal1).itemToString()));
        List<List<String>> table2 = new ArrayList<>(Arrays.asList(
                new NewspaperConvertor(newspaper2).itemToString(),
                new JournalConvertor(journal1).itemToString()));

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
        Librarian librarian = new Librarian(new WorkWithFiles("test"),printWriter);
        librarian.printItems(provided);
        assertEquals(expected,outputStream.toString());
    }

    private static Stream<Arguments> provideTablesForPrinting(){
        return Stream.of(
                Arguments.of(new ArrayList<>(Collections.emptyList()), "There`s no items here" + System.lineSeparator()),

                Arguments.of(new ArrayList<>(Arrays.asList(book1, book2, journal1)),
                        " =ITEM ID= | =TITLE=       | =AUTHOR=      | =PUBLISHING DATE= | =PAGES= | =BORROWED= " + System.lineSeparator() +
                        "-----------+---------------+---------------+-------------------+---------+------------" + System.lineSeparator() +
                        " 101       | First Book    | First Author  | 10.12.2020        | 510     | false      " + System.lineSeparator() +
                        " 102       | Second Book   | Second Author | 21.4.2021         | 924     | false      " + System.lineSeparator() +
                        " 101       | First Journal | 95            | false             | NULL    | NULL       " + System.lineSeparator())
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
                Arguments.of(101,book1),
                Arguments.of(105,newspaper2),
                Arguments.of(103,journal3),
                Arguments.of(21,null));
    }

}
