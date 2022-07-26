package com.company;

import com.company.handlers.BookHandler;
import com.company.handlers.JournalHandler;
import com.company.handlers.NewspaperHandler;
import com.company.items.Book;
import com.company.items.Journal;
import com.company.items.Newspaper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNull;

class HandlerTest {

    //TODO crate tests

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
        assertNull(book);
    }

    private static Stream<Arguments> provideNullBooks(){
        return Stream.of(
                Arguments.of(Arrays.asList("1","title","63","author","2002.0.3")),
                Arguments.of(Arrays.asList("1","title","63","author","2002.12.54"))
        );
    }

    @Test
    void journalAddingItemHandlerTest(){
        JournalHandler journalHandler = new JournalHandler();
        Journal journal  = journalHandler.createItem(Arrays.asList("1","title","63"));
        String expected = "1 - title - 63 - false";
        Assertions.assertEquals(expected,
                journal.getItemID() + " - " + journal.getTitle() + " - " + journal.getPages() + " - " + journal.isBorrowed());
    }
}
