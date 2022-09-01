package com.company;

import com.company.table.TableUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Stream;

class TableUtilTest {

    // TODO create test for handlers and tableBuilder

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    PrintWriter printWriter = new PrintWriter(printStream, true);

    static final String NEW_LINE = System.lineSeparator();


    @ParameterizedTest
    @MethodSource("provideOption")
    void tableBodyWithOneOptionTest(List<List<String>> row, List<String> option, String expected) {
        TableUtil tableUtil = new TableUtil(option, row, printWriter);
        tableUtil.printBody();
        Assertions.assertEquals(expected, outputStream.toString());
    }

    private static Stream<Arguments> provideOption() {
        return Stream.of(
                Arguments.of(new ArrayList<>(Collections.singletonList(
                                new ArrayList<>(Collections.singletonList("option")))),
                        new ArrayList<>(Collections.singletonList("1")), " option " + NEW_LINE),
                Arguments.of(new ArrayList<>(Collections.singletonList(
                                new ArrayList<>(Arrays.asList("option", "secondOption")))),
                        new ArrayList<>(Arrays.asList("1", "2")), " option | secondOption " + NEW_LINE),
                Arguments.of(new ArrayList<>(Arrays.asList(
                                new ArrayList<>(Arrays.asList("option", "secondOption")),
                                new ArrayList<>(Collections.singletonList("2.1")))),
                        new ArrayList<>(Arrays.asList("1", "2")),
                        " option | secondOption " + NEW_LINE +
                                " 2.1    | NULL         " + NEW_LINE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOptions")
    void tableWithOneOptionTest(List<List<String>> row, List<String> option, String expected) {
        TableUtil tableUtil = new TableUtil(option, row, printWriter);
        tableUtil.printTable();
        Assertions.assertEquals(expected, outputStream.toString());
    }

    private static Stream<Arguments> provideOptions() {
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
                        " =ONE OPTION= " + NEW_LINE +
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
    void validateColumnsTest(List<String> option, String expected) {
        TableUtil tableUtil = new TableUtil(option, new ArrayList<>(), printWriter);
        tableUtil.validateRows();
        tableUtil.printHeader();
        Assertions.assertEquals(expected, outputStream.toString());
    }

    private static Stream<Arguments> provideOptionsForValidation() {
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
