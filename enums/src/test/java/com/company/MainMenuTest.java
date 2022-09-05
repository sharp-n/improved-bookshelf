package com.company;

import com.company.enums.MainMenu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class MainMenuTest {

    @ParameterizedTest
    @MethodSource("provideNumbers")
    void getByIndexTest(int provided, MainMenu expected) {
        Assertions.assertEquals(expected,MainMenu.getByIndex(provided));
    }

    private static Stream<Arguments> provideNumbers() {
        return Stream.of(
                Arguments.of(1, MainMenu.BOOK),
                Arguments.of(34, MainMenu.DEFAULT),
                Arguments.of(-2, MainMenu.DEFAULT),
                Arguments.of(0, MainMenu.EXIT_VALUE),
                Arguments.of(3, MainMenu.COMICS)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOptionAndString")
    void toStringTest(MainMenu provided, String expected) {
        Assertions.assertEquals(expected,provided.toString());
    }

    private static Stream<Arguments> provideOptionAndString() {
        return Stream.of(
                Arguments.of(MainMenu.BOOK, "1 - Book"),
                Arguments.of(MainMenu.DEFAULT, "-1 - Default"),
                Arguments.of(MainMenu.EXIT_VALUE, "0 - Exit"),
                Arguments.of(MainMenu.COMICS, "3 - Comics")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStringOptions")
    void getByOptionTest(String provided, MainMenu expected) {
        Assertions.assertEquals(expected,MainMenu.getByOption(provided));
    }

    private static Stream<Arguments> provideStringOptions() {
        return Stream.of(
                Arguments.of("Book", MainMenu.BOOK),
                Arguments.of("Default", MainMenu.DEFAULT),
                Arguments.of("1234", MainMenu.DEFAULT),
                Arguments.of("Exit", MainMenu.EXIT_VALUE),
                Arguments.of("Comics", MainMenu.COMICS)
        );
    }

}
