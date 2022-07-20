package com.company;

import com.company.convertors.*;
import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.items.Newspaper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Stream;

class ConvertorsTest {

    @ParameterizedTest
    @MethodSource("provideBooks")
    void bookConvertorTest(Book provided, List<String> expected){
        ItemConvertor bookConvertor = new BookConvertor(provided);
        Assertions.assertEquals(expected,bookConvertor.itemToString());
    }

    @ParameterizedTest
    @MethodSource("provideBooks")
    void itemsForBookConvertorTest(Book provided, List<String> expected){
        ItemConvertor bookConvertor = new BookConvertor(provided);
        Assertions.assertEquals(expected,bookConvertor.itemToString());
    }

    private static Stream<Arguments> provideBooks(){
        return Stream.of(
                Arguments.of(new Book(1,"some title", "some author", new GregorianCalendar(2022,10,2),932),
                        Arrays.asList("1","some title","some author","02.11.2022","932","false")),
                Arguments.of(new Book(534,"TITLE", null, new GregorianCalendar(2022,8,2),932),
                        Arrays.asList("534","TITLE","NULL","02.9.2022","932","false")),
                Arguments.of(new Book(534,"TITLE", "author", null,932),
                        Arrays.asList("534","TITLE","author","NULL","932","false")),
                Arguments.of(new Book(534,"TITLE", null, null,932),
                        Arrays.asList("534","TITLE","NULL","NULL","932","false"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideJournals")
    void itemForJournalsConvertorTest(Journal provided, List<String> expected){
        ItemConvertor itemConvertor = new ItemConvertor(provided);
        Assertions.assertEquals(expected,itemConvertor.itemToString());
    }

    @ParameterizedTest
    @MethodSource("provideJournals")
    void journalsConvertorTest(Journal provided, List<String> expected){
        ItemConvertor itemConvertor = new JournalConvertor(provided);
        Assertions.assertEquals(expected,itemConvertor.itemToString());
    }

    private static Stream<Arguments> provideJournals(){
        return Stream.of(
                Arguments.of(new Journal(1,"some title", 932),
                        Arrays.asList("1","some title","932","false"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideItems")
    void itemsConverterTest(List<Item> provided, List<List<String>> expected) throws IOException {
        ItemsConvertor itemsConvertor = new ItemsConvertor();
        Assertions.assertEquals(expected,itemsConvertor.itemsToString(provided));
    }

    private static Stream<Arguments> provideItems(){
        return Stream.of(
                Arguments.of(Arrays.asList(new Journal(1,"some title", 932), new Book(1,"some title", "some author", new GregorianCalendar(2022,10,2),932)),
                        Arrays.asList(Arrays.asList("1","some title","932","false"),Arrays.asList("1","some title","some author","02.11.2022","932","false")))
        );
    }

    @ParameterizedTest
    @MethodSource("provideNewspapers")
    void newspaperConvertorTest(Newspaper provided, List<String> expected){
        ItemConvertor itemConvertor = new NewspaperConvertor(provided);
        Assertions.assertEquals(expected,itemConvertor.itemToString());
    }

    private static Stream<Arguments> provideNewspapers(){
        return Stream.of(
                Arguments.of(new Newspaper(1,"some title", 932),
                        Arrays.asList("1","some title","932","false"))
        );
    }

}
