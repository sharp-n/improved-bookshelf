package com.company;

import com.company.enums.FilesMenu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class FilesMenuTest {

    @ParameterizedTest
    @MethodSource("provideNumbers")
    void getByIndexTest(int provided, FilesMenu expected) {
        Assertions.assertEquals(expected,FilesMenu.getByIndex(provided));
    }

    private static Stream<Arguments> provideNumbers() {
        return Stream.of(
                Arguments.of(1, FilesMenu.ONE_FILE),
                Arguments.of(34, FilesMenu.DEFAULT),
                Arguments.of(-2, FilesMenu.DEFAULT),
                Arguments.of(0, FilesMenu.EXIT_VALUE),
                Arguments.of(3, FilesMenu.DATABASE_SQLITE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOptionAndString")
    void toStringTest(FilesMenu provided, String expected) {
        Assertions.assertEquals(expected,provided.toString());
    }

    private static Stream<Arguments> provideOptionAndString() {
        return Stream.of(
                Arguments.of(FilesMenu.ONE_FILE, "1 - One file"),
                Arguments.of(FilesMenu.DEFAULT, "-1 - Default"),
                Arguments.of(FilesMenu.EXIT_VALUE, "0 - Exit"),
                Arguments.of(FilesMenu.DATABASE_SQLITE, "3 - Database (SQLite)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStringOptions")
    void getByOptionTest(String provided, FilesMenu expected) {
        Assertions.assertEquals(expected,FilesMenu.getByOption(provided));
    }

    private static Stream<Arguments> provideStringOptions() {
        return Stream.of(
                Arguments.of("One file", FilesMenu.ONE_FILE),
                Arguments.of("Default", FilesMenu.DEFAULT),
                Arguments.of("1234", FilesMenu.DEFAULT),
                Arguments.of("Exit", FilesMenu.EXIT_VALUE),
                Arguments.of("Database (SQLite)", FilesMenu.DATABASE_SQLITE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDBNameOptions")
    void getByDBColumnName(String provided, FilesMenu expected) {
        Assertions.assertEquals(expected,FilesMenu.getByParameter(provided));
    }

    private static Stream<Arguments> provideDBNameOptions() {
        return Stream.of(
                Arguments.of("oneFile", FilesMenu.ONE_FILE),
                Arguments.of("    ", FilesMenu.DEFAULT),
                Arguments.of("", FilesMenu.DEFAULT),
                Arguments.of("exit", FilesMenu.EXIT_VALUE),
                Arguments.of("databaseSQLite", FilesMenu.DATABASE_SQLITE)
        );
    }

}
