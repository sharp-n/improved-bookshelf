package com.company;

import com.company.handlers.item_handlers.BookHandler;
import com.company.handlers.item_handlers.NewspaperHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemHandlersTest {

    static Book book1 = new Book(5,"Some title","Author",new GregorianCalendar(2002, Calendar.MAY,2),824);
    static Book book2 = new Book(666,"Any title","unknown",new GregorianCalendar(2002,Calendar.APRIL,2),500);
    static Book book3 = new Book(1005,"title","Some author",new GregorianCalendar(1991,Calendar.MAY,2),2736);
    static Book book4 = new Book(1,"Title","same author",new GregorianCalendar(2002,Calendar.MAY,25),666);


    static Newspaper newspaper1 = new Newspaper(5,"Some title",824);
    static Newspaper newspaper2 = new Newspaper(666,"Any title",500);
    static Newspaper newspaper3 = new Newspaper(1005,"title",2736);
    static Newspaper newspaper4 = new Newspaper(1,"Title",666);

    @ParameterizedTest
    @MethodSource("provideNewspapers")
    void newspaperAddingItemHandlerTest(List<String> provided, String expected){
        NewspaperHandler newspaperHandler = new NewspaperHandler();
        Newspaper newspaper = newspaperHandler.createItem(provided);
        Assertions.assertEquals(expected,
                newspaper.getItemID() + " - " + newspaper.getTitle() + " - " + newspaper.getPages() + " - " + newspaper.isBorrowed());
    }

    private static Stream<Arguments> provideNewspapers(){
        return Stream.of(
                Arguments.of(Arrays.asList("1","title","63"),"1 - title - 63 - false")
        );
    }

    @Test
    void bookAddingItemHandlerTest(){
        BookHandler bookHandler = new BookHandler();
        Book book  = bookHandler.createItem(Arrays.asList("1","title","63","author","2002.2.3"));
        String expected = "1 - title - author - 03.2.2002 - 63 - false";
        SimpleDateFormat sdf = new SimpleDateFormat("dd.M.y");
        Assertions.assertEquals(expected,book.getItemID() + " - " + book.getTitle() + " - "
                + book.getAuthor() + " - " + sdf.format(book.getPublishingDate().getTime())
                + " - " + book.getPages() + " - " + book.isBorrowed());
    }

    @ParameterizedTest
    @MethodSource("provideNullBooks")
    void nullBookAddingItemHandlerTest(List<String> provided){
        BookHandler bookHandler = new BookHandler();
        Book book  = bookHandler.createItem(provided);
        Assertions.assertNull(book);
    }

    private static Stream<Arguments> provideNullBooks(){
        return Stream.of(
                Arguments.of(Arrays.asList("1","title","63","author","2002.0.3")),
                Arguments.of(Arrays.asList("1","title","63","author","2002.12.54"))
        );
    }

    //@Test
    //void journalAddingItemHandlerTest(){
    //    JournalHandler journalHandler = new JournalHandler();
    //    Journal journal  = journalHandler.createItem(Arrays.asList("1","title","63"));
    //    String expected = "1 - title - 63 - false";
    //    Assertions.assertEquals(expected,
    //            journal.getItemID() + " - " + journal.getTitle() + " - " + journal.getPages() + " - " + journal.isBorrowed());
    //}

    @ParameterizedTest
    @MethodSource("provideBooksForSortingByID")
    void booksGetSortedItemsByIDTest(List<Item> provided, List<Book> expected){
        BookHandler bookHandler = new BookHandler();
        List<Book> providedBooks = bookHandler.getSortedItemsByComparator(provided, ComparatorsForSorting.COMPARATOR_ITEM_BY_ID);
        Assertions.assertEquals(expected,providedBooks);
    }

    private static Stream<Arguments> provideBooksForSortingByID(){
        return Stream.of(
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book1,book2,book3,book4)),
                        new ArrayList<>(Arrays.asList(book4,book1,book2,book3))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book2,book3,book4,book1)),
                        new ArrayList<>(Arrays.asList(book4,book1,book2,book3))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book4,book1,book3,book2)),
                        new ArrayList<>(Arrays.asList(book4,book1,book2,book3)))
        );
    }

    @ParameterizedTest
    @MethodSource("provideBooksForSortingByAuthor")
    void booksGetSortedItemsByAuthorTest(List<Item> provided, List<Book> expected){
        BookHandler bookHandler = new BookHandler();
        List<Book> providedBooks = bookHandler.getSortedItemsByComparator(provided, ComparatorsForSorting.COMPARATOR_ITEM_BY_AUTHOR);
        Assertions.assertEquals(expected,providedBooks);
    }

    private static Stream<Arguments> provideBooksForSortingByAuthor(){
        return Stream.of(
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book1,book2,book3,book4)),
                        new ArrayList<>(Arrays.asList(book1,book3,book4,book2))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book2,book3,book4,book1)),
                        new ArrayList<>(Arrays.asList(book1,book3,book4,book2))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book4,book1,book3,book2)),
                        new ArrayList<>(Arrays.asList(book1,book3,book4,book2)))
        );
    }

    @ParameterizedTest
    @MethodSource("provideBooksForSortingByTitle")
    void booksGetSortedItemsByTitleTest(List<Item> provided, List<Book> expected){
        BookHandler bookHandler = new BookHandler();
        List<Book> providedBooks = bookHandler.getSortedItemsByComparator(provided, ComparatorsForSorting.COMPARATOR_ITEM_BY_TITLE);
        Assertions.assertEquals(expected,providedBooks);
    }


    private static Stream<Arguments> provideBooksForSortingByTitle(){
        return Stream.of(
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book1,book2,book3,book4)),
                        new ArrayList<>(Arrays.asList(book2,book1,book4,book3))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book2,book3,book4,book1)),
                        new ArrayList<>(Arrays.asList(book2,book1,book4,book3))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book4,book1,book3,book2)),
                        new ArrayList<>(Arrays.asList(book2,book1,book4,book3)))
        );
    }

    @ParameterizedTest
    @MethodSource("provideBooksForSortingByPublishingDate")
    void booksGetSortedItemsByPublishingDateTest(List<Item> provided, List<Book> expected){
        BookHandler bookHandler = new BookHandler();
        List<Book> providedBooks = bookHandler.getSortedItemsByComparator(provided, ComparatorsForSorting.COMPARATOR_ITEM_BY_DATE);
        Assertions.assertEquals(expected,providedBooks);
    }


    private static Stream<Arguments> provideBooksForSortingByPublishingDate(){
        return Stream.of(
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book1,book2,book3,book4)),
                        new ArrayList<>(Arrays.asList(book3,book2,book1,book4))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book2,book3,book4,book1)),
                        new ArrayList<>(Arrays.asList(book3,book2,book1,book4))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book4,book1,book3,book2)),
                        new ArrayList<>(Arrays.asList(book3,book2,book1,book4)))
        );
    }

    @ParameterizedTest
    @MethodSource("provideBooksForSortingByPages")
    void booksGetSortedItemsByPagesTest(List<Item> provided, List<Book> expected){
        BookHandler bookHandler = new BookHandler();
        List<Book> providedBooks = bookHandler.getSortedItemsByComparator(provided, ComparatorsForSorting.COMPARATOR_ITEM_BY_PAGES);
        Assertions.assertEquals(expected,providedBooks);
    }


    private static Stream<Arguments> provideBooksForSortingByPages(){
        return Stream.of(
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book1,book2,book3,book4)),
                        new ArrayList<>(Arrays.asList(book2,book4,book1,book3))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book2,book3,book4,book1)),
                        new ArrayList<>(Arrays.asList(book2,book4,book1,book3))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(book4,book1,book3,book2)),
                        new ArrayList<>(Arrays.asList(book2,book4,book1,book3)))
        );
    }

    //@ParameterizedTest
    //@MethodSource("provideJournalsForSortingByPages")
    //void journalsGetSortedItemsByPagesTest(List<Item> provided, List<Journal> expected){
    //    JournalHandler journalHandler = new JournalHandler();
    //    List<Journal> providedJournals = journalHandler.getSortedItemsByComparator(provided, ComparatorsForSorting.COMPARATOR_ITEM_BY_PAGES);
    //    assertEquals(expected,providedJournals);
    //}


    //private static Stream<Arguments> provideJournalsForSortingByPages(){
    //    return Stream.of(
    //            Arguments.of(
    //                    new ArrayList<>(Arrays.asList(journal1,journal2,journal3,journal4)),
    //                    new ArrayList<>(Arrays.asList(journal2,journal4,journal1,journal3))),
    //            Arguments.of(
    //                    new ArrayList<>(Arrays.asList(journal2,journal3,journal4,journal1)),
    //                    new ArrayList<>(Arrays.asList(journal2,journal4,journal1,journal3))),
    //            Arguments.of(
    //                    new ArrayList<>(Arrays.asList(journal4,journal1,journal3,journal2)),
    //                    new ArrayList<>(Arrays.asList(journal2,journal4,journal1,journal3)))
    //    );
    //}

    //@ParameterizedTest
    //@MethodSource("provideJournalsForSortingByTitle")
    //void journalsGetSortedItemsByTitleTest(List<Item> provided, List<Journal> expected){
    //    JournalHandler journalHandler = new JournalHandler();
    //    List<Journal> providedJournals = journalHandler.getSortedItemsByComparator(provided, ComparatorsForSorting.COMPARATOR_ITEM_BY_TITLE);
    //    assertEquals(expected,providedJournals);
    //}


    //private static Stream<Arguments> provideJournalsForSortingByTitle(){
    //    return Stream.of(
    //            Arguments.of(
    //                    new ArrayList<>(Arrays.asList(journal1,journal2,journal3,journal4)),
    //                    new ArrayList<>(Arrays.asList(journal2,journal1,journal4,journal3))),
    //            Arguments.of(
    //                    new ArrayList<>(Arrays.asList(journal2,journal3,journal4,journal1)),
    //                    new ArrayList<>(Arrays.asList(journal2,journal1,journal4,journal3))),
    //            Arguments.of(
    //                    new ArrayList<>(Arrays.asList(journal4,journal1,journal3,journal2)),
    //                    new ArrayList<>(Arrays.asList(journal2,journal1,journal4,journal3)))
    //    );
    //}

    @ParameterizedTest
    @MethodSource("provideNewspapersForSortingByID")
    void newspapersGetSortedItemsByIDTest(List<Item> provided, List<Newspaper> expected){
        NewspaperHandler newspaperHandler = new NewspaperHandler();
        List<Newspaper> providedNewspapers = newspaperHandler.getSortedItemsByComparator(provided, ComparatorsForSorting.COMPARATOR_ITEM_BY_ID);
        Assertions.assertEquals(expected,providedNewspapers);
    }

    private static Stream<Arguments> provideNewspapersForSortingByID(){
        return Stream.of(
                Arguments.of(
                        new ArrayList<>(Arrays.asList(newspaper1,newspaper2,newspaper3,newspaper4)),
                        new ArrayList<>(Arrays.asList(newspaper4,newspaper1,newspaper2,newspaper3))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(newspaper2,newspaper3,newspaper4,newspaper1)),
                        new ArrayList<>(Arrays.asList(newspaper4,newspaper1,newspaper2,newspaper3))),
                Arguments.of(
                        new ArrayList<>(Arrays.asList(newspaper4,newspaper1,newspaper3,newspaper2)),
                        new ArrayList<>(Arrays.asList(newspaper4,newspaper1,newspaper2,newspaper3)))
        );
    }

}
