package com.company;

import com.company.table.HtmlTableBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class HTMLTableBuilderTest {

    @ParameterizedTest
    @MethodSource("provideHeader")
    void generateHeaderTest(List<String> providedHeader, String expected){
        HtmlTableBuilder tableBuilder = new HtmlTableBuilder(providedHeader,new ArrayList<>(new ArrayList<>()));
        Assertions.assertEquals(expected,tableBuilder.generateHeader().toString());
    }

    private static Stream<Arguments> provideHeader() {
        return Stream.of(
                Arguments.of(
                        new ArrayList<>(Collections.singletonList("single column")),
                        "<tr align=\"center\"><th>SINGLE COLUMN</th></tr>"),
                Arguments.of(
                        new ArrayList<>(Arrays.asList("first column","seconD Column","third cOlumn","fourth")),
                        "<tr align=\"center\"><th>FIRST COLUMN</th><th>SECOND COLUMN</th><th>THIRD COLUMN</th><th>FOURTH</th></tr>")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBody")
    void generateBodyTest(List<List<String>> providedRows, String expected){
        HtmlTableBuilder tableBuilder = new HtmlTableBuilder(new ArrayList<>(),providedRows);
        Assertions.assertEquals(expected,tableBuilder.generateBody().toString());
    }

    private static Stream<Arguments> provideBody() {
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList(
                                new ArrayList<>(Collections.singletonList("1.1")),
                                new ArrayList<>(Collections.singletonList("2.1")))),
                        "<tr align=\"center\"><td>1.1</td></tr>" +
                                "<tr align=\"center\"><td>2.1</td></tr>"),
                Arguments.of(new ArrayList<>(Arrays.asList(
                        new ArrayList<>(Arrays.asList("1.1","1.2","1.3","1.4")),
                        new ArrayList<>(Arrays.asList("2.1","2.2","2.3","2.4")))),
                        "<tr align=\"center\"><td>1.1</td><td>1.2</td><td>1.3</td><td>1.4</td></tr>" +
                                "<tr align=\"center\"><td>2.1</td><td>2.2</td><td>2.3</td><td>2.4</td></tr>")
        );
    }


    @ParameterizedTest
    @MethodSource("provideTable")
    void generateTableTest(List<String> header,List<List<String>> rows, String expected){
        HtmlTableBuilder tableBuilder = new HtmlTableBuilder(header,rows);
        Assertions.assertEquals(expected,tableBuilder.generateTable());

    }

    private static Stream<Arguments> provideTable() {
        return Stream.of(
                Arguments.of(new ArrayList<>(Collections.singletonList("single column")),
                        new ArrayList<>(Arrays.asList(
                                new ArrayList<>(Collections.singletonList("1.1")),
                                new ArrayList<>(Collections.singletonList("2.1")))),
                        "<table border=\"1\" cellpadding=\"5\" cellspacing=\"5\">" +
                                "<tr align=\"center\"><th>SINGLE COLUMN</th></tr>" +
                                "<tr align=\"center\"><td>1.1</td></tr>" +
                                "<tr align=\"center\"><td>2.1</td></tr>" +
                                "</table>"),
                Arguments.of(new ArrayList<>(Arrays.asList("first column","seconD Column","third cOlumn","fourth")),
                        new ArrayList<>(Arrays.asList(
                                new ArrayList<>(Arrays.asList(" 1.1","1.2","1.3","1.4")),
                                new ArrayList<>(Arrays.asList("2.1","2.2","2.3","2.4")))),
                        "<table border=\"1\" cellpadding=\"5\" cellspacing=\"5\">" +
                                "<tr align=\"center\"><th>FIRST COLUMN</th><th>SECOND COLUMN</th><th>THIRD COLUMN</th><th>FOURTH</th></tr>" +
                                "<tr align=\"center\"><td>1.1</td><td>1.2</td><td>1.3</td><td>1.4</td></tr>" +
                                "<tr align=\"center\"><td>2.1</td><td>2.2</td><td>2.3</td><td>2.4</td></tr>" +
                                "</table>"),
                Arguments.of(new ArrayList<>(Arrays.asList("first column","seconD Column","third cOlumn","fourth")),
                        new ArrayList<>(Arrays.asList(
                                new ArrayList<>(Arrays.asList("1.1","","1.3","1.4")),
                                new ArrayList<>(Arrays.asList("2.1","2.2","2.3"," ")))),
                        "<table border=\"1\" cellpadding=\"5\" cellspacing=\"5\">" +
                                "<tr align=\"center\"><th>FIRST COLUMN</th><th>SECOND COLUMN</th><th>THIRD COLUMN</th><th>FOURTH</th></tr>" +
                                "<tr align=\"center\"><td>1.1</td><td></td><td>1.3</td><td>1.4</td></tr>" +
                                "<tr align=\"center\"><td>2.1</td><td>2.2</td><td>2.3</td><td> </td></tr>" +
                                "</table>")
        );
    }

}
