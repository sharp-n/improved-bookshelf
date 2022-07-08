package com.company;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @ParameterizedTest
    @MethodSource("provideUsernameForGoodInput")
    void authorGoodValidation(String input){
        assertTrue(User.checkUserNameForValidity(input));
    }

    private static Stream<Arguments> provideUsernameForGoodInput(){
        return Stream.of(
                Arguments.of("user"),
                Arguments.of("first_user"),
                Arguments.of("f1rst_user"),
                Arguments.of("f1rst_User")
        );
    }

    @ParameterizedTest
    @MethodSource("provideUsernameForBadInput")
    void authorBadValidation(String input){
        assertFalse(User.checkUserNameForValidity(input));
    }

    private static Stream<Arguments> provideUsernameForBadInput(){
        return Stream.of(
                Arguments.of("user "),
                Arguments.of("use"),
                Arguments.of("first_user%"),
                Arguments.of("f1rst_user@"),
                Arguments.of("f1rst_User\\"),
                Arguments.of("f1rst_User&"),
                Arguments.of("f1rst_User?"),
                Arguments.of("f1rst_User!"),
                Arguments.of("f1rst_User#"),
                Arguments.of("f1rst_User^"),
                Arguments.of("f1rst_User:")
        );
    }

}
