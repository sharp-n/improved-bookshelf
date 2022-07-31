package com.company;

import com.company.handlers.Librarian;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class CheckingValidityTest {

    @ParameterizedTest
    @ValueSource(strings = {"0", "-25"})
    void checkIntItemForValidityBadTest(int input){
        assertFalse(Librarian.checkItemForValidity(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "25", "255"})
    void checkIntItemForValidityGoodTest(int input){
        assertTrue(Librarian.checkItemForValidity(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", " "})
    void checkStringItemForValidityBadTest(String input){
        assertFalse(Librarian.checkItemForValidity(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"  string", " item ", "not null string ", "just a normal test string"})
    void checkStringItemForValidityGoodTest(String input){
        assertTrue(Librarian.checkItemForValidity(input));
    }

}
