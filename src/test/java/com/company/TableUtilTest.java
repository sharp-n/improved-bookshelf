package com.company;

import com.company.convertors.ItemsConvertor;
import com.company.items.Book;
import com.company.items.Journal;
import com.company.items.Newspaper;
import com.company.table.TableUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TableUtilTest {

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    PrintWriter printWriter = new PrintWriter(printStream, true);

    static List<String> bookOptions = Arrays.asList("ID", "Title", "Author", "Publishing date", "Pages","borrowed");

    static final String NEW_LINE = System.lineSeparator();

    @Test
    void tesNewspapersInTable() {
        String expectedString ="" +
                " 1    | title1  | 1        | false             | NULL    | NULL       " + System.lineSeparator() +
                " 2    | title2  | 2        | false             | NULL    | NULL       " + System.lineSeparator() +
                " 3    | title3  | 3        | false             | NULL    | NULL       " + System.lineSeparator() +
                " 4    | title4  | 4        | false             | NULL    | NULL       " + System.lineSeparator();
        ItemsConvertor itemsConvertor = new ItemsConvertor();
        List<List<String>> items = itemsConvertor.newspapersToString(new ArrayList<>(
                Arrays.asList(
                        new Newspaper(1,"title1", 1),
                        new Newspaper(2,"title2", 2),
                        new Newspaper(3,"title3", 3),
                        new Newspaper(4,"title4", 4)
                        )
        ));
        TableUtil tableUtil = new TableUtil(bookOptions,items, printWriter);
        tableUtil.printBody();

        assertEquals(expectedString, outputStream.toString());
    }

    @Test
    void tesAllTypesOfItemsInTable() {
        String expectedString ="" +
                " 1    | title1  | 1           | false             | NULL    | NULL       " + System.lineSeparator() +
                " 2    | title2  | 2           | false             | NULL    | NULL       " + System.lineSeparator() +
                " 3    | title3  | some author | 02.11.2022        | 932     | false      " + System.lineSeparator();
        ItemsConvertor itemsConvertor = new ItemsConvertor();
        List<List<String>> items = itemsConvertor.itemsToString(new ArrayList<>(
                Arrays.asList(
                        new Newspaper(1,"title1", 1),
                        new Journal(2,"title2", 2),
                        new Book(3,"title3","some author", new GregorianCalendar(2022,10,2),932)
                )
        ));
        TableUtil tableUtil = new TableUtil(bookOptions,items, printWriter);
        tableUtil.printBody();

        assertEquals(expectedString, outputStream.toString());
    }

    @ParameterizedTest
    @MethodSource("provideBooks")
    void bodyBooksTest(List<Book> provided, String expected){
        ItemsConvertor itemsConvertor = new ItemsConvertor();
        List<List<String>> items = itemsConvertor.booksToString(provided);
        TableUtil tableUtil = new TableUtil(bookOptions,items, printWriter);
        tableUtil.printBody();
        Assertions.assertEquals(expected, outputStream.toString());
    }

    private static Stream<Arguments> provideBooks(){
        return Stream.of(
                Arguments.of(Arrays.asList(
                        new Book(1,"some title", "some author", new GregorianCalendar(2022,10,2),932),
                        new Book(534,"TITLE", null, new GregorianCalendar(2022,8,2),932)),
                        " 1    | some title | some author | 02.11.2022        | 932     | false      " + NEW_LINE +
                                " 534  | TITLE      | NULL        | 02.9.2022         | 932     | false      " + NEW_LINE),
                Arguments.of(Arrays.asList(
                        new Book(534,"TITLE", "author", null,932),
                        new Book(534,"TITLE", null, null,932)),
                        " 534  | TITLE   | author   | NULL              | 932     | false      " + NEW_LINE +
                                " 534  | TITLE   | NULL     | NULL              | 932     | false      " + NEW_LINE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBooksForTablePrint")
    void tableBooksTest(List<Book> provided,  String expected, List<String> options){
        ItemsConvertor itemsConvertor = new ItemsConvertor();
        List<List<String>> items = itemsConvertor.booksToString(provided);
        TableUtil tableUtil = new TableUtil(options,items, printWriter);
        tableUtil.printTable();
        Assertions.assertEquals(expected, outputStream.toString());
    }

    private static Stream<Arguments> provideBooksForTablePrint(){
        return Stream.of(
                Arguments.of(Arrays.asList(
                                new Book(1,"some title", "some author", new GregorianCalendar(2022,10,2),932),
                                new Book(534,"TITLE", null, new GregorianCalendar(2022,8,2),932)),
                        " =ID= | =TITLE=    | =AUTHOR=    | =PUBLISHING DATE= | =PAGES= | =BORROWED= " + NEW_LINE +
                                "------+------------+-------------+-------------------+---------+------------" + NEW_LINE +
                                " 1    | some title | some author | 02.11.2022        | 932     | false      " + NEW_LINE +
                                " 534  | TITLE      | NULL        | 02.9.2022         | 932     | false      " + NEW_LINE,
                        bookOptions),
                Arguments.of(Arrays.asList(
                                new Book(534,"TITLE", "author", null,932),
                                new Book(534,"TITLE", null, null,932)),
                        " =ID= | =TITLE= | =AUTHOR= | =PUBLISHING DATE= | =PAGES= | =BORROWED= " + NEW_LINE +
                                "------+---------+----------+-------------------+---------+------------" + NEW_LINE +
                                " 534  | TITLE   | author   | NULL              | 932     | false      " + NEW_LINE +
                                " 534  | TITLE   | NULL     | NULL              | 932     | false      " + NEW_LINE,
                        bookOptions)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOption")
    void tableBodyWithOneOptionTest(List<List<String>> row, List<String> option, String expected){
        TableUtil tableUtil = new TableUtil(option,row, printWriter);
        tableUtil.printBody();
        Assertions.assertEquals(expected, outputStream.toString());
    }

    private static Stream<Arguments> provideOption(){
        return Stream.of(
                Arguments.of(new ArrayList<>(Collections.singletonList(
                        new ArrayList<>(Collections.singletonList("option")))),
                        new ArrayList<>(Collections.singletonList("1")), " option " + NEW_LINE),
                Arguments.of(new ArrayList<>(Collections.singletonList(
                        new ArrayList<>(Arrays.asList("option", "secondOption")))),
                        new ArrayList<>(Arrays.asList("1","2"))," option | secondOption " + NEW_LINE),
                Arguments.of(new ArrayList<>(Arrays.asList(
                                new ArrayList<>(Arrays.asList("option","secondOption")),
                                new ArrayList<>(Collections.singletonList("2.1")))),
                        new ArrayList<>(Arrays.asList("1","2")),
                        " option | secondOption " + NEW_LINE +
                                " 2.1    | NULL         " + NEW_LINE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOptions")
    void tableWithOneOptionTest(List<List<String>> row, List<String> option, String expected){
        TableUtil tableUtil = new TableUtil(option,row, printWriter);
        tableUtil.printTable();
        Assertions.assertEquals(expected, outputStream.toString());
    }

    private static Stream<Arguments> provideOptions(){
        return Stream.of(
                Arguments.of(new ArrayList<>(Collections.singletonList(
                                new ArrayList<>(Collections.singletonList("option")))),
                        new ArrayList<>(Collections.singletonList("1")),
                        " =1=    " + System.lineSeparator() +
                                "--------" + NEW_LINE +
                                " option " + NEW_LINE),

                Arguments.of(new ArrayList<>(Arrays.asList(
                                new ArrayList<>(Collections.singletonList("first row")),
                                new ArrayList<>(Collections.singletonList("second row")))),
                        new ArrayList<>(Collections.singletonList("one option")),
                        " =ONE OPTION= " + NEW_LINE  +
                                "--------------" + NEW_LINE +
                                " first row    " + NEW_LINE +
                                " second row   " + NEW_LINE),

                Arguments.of(new ArrayList<>(Arrays.asList(
                                new ArrayList<>(Collections.singletonList("row")),
                                new ArrayList<>(Collections.singletonList("second row")),
                                new ArrayList<>(Collections.singletonList("another row")))),
                        new ArrayList<>(Collections.singletonList("option")),
                        " =OPTION=    " + System.lineSeparator() +
                                "-------------" + NEW_LINE +
                                " row         " + NEW_LINE +
                                " second row  " + NEW_LINE +
                                " another row " + NEW_LINE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOptionsForValidation")
    void validateColumnsTest(List<String> option, String expected){
        TableUtil tableUtil = new TableUtil(option, new ArrayList<>(), printWriter);
        tableUtil.validateRows();
        tableUtil.printHeader();
        Assertions.assertEquals(expected, outputStream.toString());
    }

    private static Stream<Arguments> provideOptionsForValidation(){
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList(
                        "first column",
                        "",
                        "last column")),
                        " =FIRST COLUMN= | =NULL= | =LAST COLUMN= " + NEW_LINE +
                                "----------------+--------+---------------" + NEW_LINE),
                Arguments.of(new ArrayList<>(Arrays.asList(
                                "",
                                null,
                                "last column")),
                        " =NULL= | =NULL= | =LAST COLUMN= " + NEW_LINE +
                                "--------+--------+---------------" + NEW_LINE),
                Arguments.of(new ArrayList<>(Collections.singletonList("")),
                        " =NULL= " + NEW_LINE +
                                "--------" + NEW_LINE),
                Arguments.of(new ArrayList<>(Collections.singletonList(null)),
                        " =NULL= " + NEW_LINE +
                                "--------" + NEW_LINE)
        );
    }


}
