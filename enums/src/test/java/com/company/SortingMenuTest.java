package com.company;

import com.company.enums.SortingMenu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class SortingMenuTest {

    @ParameterizedTest
    @MethodSource("provideNumbers")
    void getByIndexTest(int provided, SortingMenu expected) {
        Assertions.assertEquals(expected,SortingMenu.getByIndex(provided));
    }

    private static Stream<Arguments> provideNumbers() {
        return Stream.of(
                Arguments.of(1, SortingMenu.ITEM_ID),
                Arguments.of(34, SortingMenu.DEFAULT),
                Arguments.of(-2, SortingMenu.DEFAULT),
                Arguments.of(0, SortingMenu.RETURN_VALUE),
                Arguments.of(3, SortingMenu.PAGES)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOptionAndString")
    void toStringTest(SortingMenu provided, String expected) {
        Assertions.assertEquals(expected,provided.toString());
    }

    private static Stream<Arguments> provideOptionAndString() {
        return Stream.of(
                Arguments.of(SortingMenu.ITEM_ID, "1 - Item ID"),
                Arguments.of(SortingMenu.DEFAULT, "-1 - Default"),
                Arguments.of(SortingMenu.RETURN_VALUE, "0 - Return"),
                Arguments.of(SortingMenu.PAGES, "3 - Pages")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStringOptions")
    void getByOptionTest(String provided, SortingMenu expected) {
        Assertions.assertEquals(expected,SortingMenu.getByOption(provided));
    }

    private static Stream<Arguments> provideStringOptions() {
        return Stream.of(
                Arguments.of("itemID", SortingMenu.ITEM_ID),
                Arguments.of("Default", SortingMenu.DEFAULT),
                Arguments.of("1234", SortingMenu.DEFAULT),
                Arguments.of("return", SortingMenu.RETURN_VALUE),
                Arguments.of("paGes", SortingMenu.PAGES)
        );
    }

}
