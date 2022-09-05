package com.company;

import com.company.enums.ActionsWithItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ActionsWithItemTest {

    @ParameterizedTest
    @MethodSource("provideOption")
    void getByIndexTest(int provided, ActionsWithItem expected) {
        Assertions.assertEquals(expected,ActionsWithItem.getByIndex(provided));
    }

    private static Stream<Arguments> provideOption() {
        return Stream.of(
                Arguments.of(1, ActionsWithItem.ADD),
                Arguments.of(34, ActionsWithItem.DEFAULT),
                Arguments.of(-2, ActionsWithItem.DEFAULT),
                Arguments.of(0, ActionsWithItem.EXIT_VALUE),
                Arguments.of(3, ActionsWithItem.TAKE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOptionAndString")
    void toStringTest(ActionsWithItem provided, String expected) {
        Assertions.assertEquals(expected,provided.toString());
    }

    private static Stream<Arguments> provideOptionAndString() {
        return Stream.of(
                Arguments.of(ActionsWithItem.ADD,"1 - Add"),
                Arguments.of(ActionsWithItem.DEFAULT,"-1 - Default"),
                Arguments.of(ActionsWithItem.EXIT_VALUE,"0 - Exit"),
                Arguments.of(ActionsWithItem.TAKE,"3 - Take")
        );
    }

}
