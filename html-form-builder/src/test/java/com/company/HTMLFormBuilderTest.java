package com.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class HTMLFormBuilderTest {

    HTMLFormBuilder formBuilder = new HTMLFormBuilder();
    @ParameterizedTest
    @MethodSource("provideLabels")
    void genLabelTest(String providedLabelStr, String providedName, String expected){
        Assertions.assertEquals(expected,formBuilder.genLabel(providedLabelStr,providedName));
    }

    private static Stream<Arguments> provideLabels() {
        return Stream.of(
                Arguments.of("some label","test","<label for=\"test\">some label</label>"),
                Arguments.of("","test","<label for=\"test\"></label>"),
                Arguments.of(" ","test","<label for=\"test\"> </label>")
        );
    }

    @ParameterizedTest
    @MethodSource("provideButtons")
    void genButtonTest(String providedLabelStr, String expected){
        Assertions.assertEquals(expected, formBuilder.genButton(providedLabelStr));
    }

    private static Stream<Arguments> provideButtons() {
        return Stream.of(
                Arguments.of("test","<input type=\"submit\" value=\"test\"/>"),
                Arguments.of("","<input type=\"submit\" value=\"\"/>"),
                Arguments.of(" ","<input type=\"submit\" value=\" \"/>")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTextFields")
    void genTextFieldTest(String providedID, String providedName, String expected){
        Assertions.assertEquals(expected,formBuilder.genTextField(providedID,providedName));
    }

    private static Stream<Arguments> provideTextFields() {
        return Stream.of(
                Arguments.of("test","test","<input id=\"test\" type=\"text\" name=\"test\">"),
                Arguments.of("","","<input id=\"\" type=\"text\" name=\"\">"),
                Arguments.of(" "," ","<input id=\" \" type=\"text\" name=\" \">"),
                Arguments.of("test","","<input id=\"test\" type=\"text\" name=\"\">")
        );
    }


    @ParameterizedTest
    @MethodSource("provideRadioButtons")
    void genRadioButtonTest(String providedName, String providedValue, String providedLabel, String expected){
        Assertions.assertEquals(expected,formBuilder.genRadioButton(providedName,providedValue,providedLabel));
    }

    private static Stream<Arguments> provideRadioButtons() {
        return Stream.of(
                Arguments.of("test","test","for test",
                        "<input type=\"radio\" name=\"test\" value=\"test\"/>for test"),
                Arguments.of("test","","",
                        "<input type=\"radio\" name=\"test\" value=\"\"/>"),
                Arguments.of(" "," "," ",
                        "<input type=\"radio\" name=\" \" value=\" \"/> ")
        );
    }

    @ParameterizedTest
    @MethodSource("provideForms")
    void genFormTest(String providedFormContent,String expected){
        Assertions.assertEquals(expected,formBuilder.genForm(providedFormContent,"/test"));
    }

    private static Stream<Arguments> provideForms() {
        return Stream.of(
                Arguments.of("<label for=\"test\">some label</label>" +
                                "<input type=\"submit\" value=\"test\"/>" +
                                "<input id=\"test\" type=\"text\" name=\"test\">" +
                                "<input type=\"radio\" name=\"test\" value=\"test\"/>for test",
                        "<form method=\"post\" action=\"/test\">" +
                                "<label for=\"test\">some label</label>" +
                                "<input type=\"submit\" value=\"test\"/>" +
                                "<input id=\"test\" type=\"text\" name=\"test\">" +
                                "<input type=\"radio\" name=\"test\" value=\"test\"/>for test" +
                                "</form>"
                )
        );
    }

}
