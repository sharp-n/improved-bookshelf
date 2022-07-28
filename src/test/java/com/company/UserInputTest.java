package com.company;

import com.company.handlers.BookHandler;
import com.company.work_with_files.WorkWithFiles;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserInputTest {

    Validator validator = new Validator(new PrintWriter(System.out,true));

/*
    public static Object[][] dataprovider(){
        return new Object[][]{
                {"a", "A"},
                {"b", "B"},
                {"c", "C"},
                {"d", "D"},
                {"e", "E"},
        };
    }

    UserInputTest(String character){

    }

    public void test(String character, String expected){

        Assert.assertEquals(doSomething(character));
    }
*/

    UserInput getDialogues(String x) {
        ByteArrayInputStream in = new ByteArrayInputStream(x.getBytes());
        Scanner scanner = new Scanner(in);
        return new UserInput(new Librarian(new WorkWithFiles("test"),new PrintWriter(System.out)),scanner);
    }

    // USERNAME

    @ParameterizedTest
    @MethodSource("provideNulls")
    void usernameNullInput(String input){
        UserInput dialogues = getDialogues(input);
        assertNull(validator.usernameValidation(dialogues.usernameInput()));
    }

    @ParameterizedTest
    @MethodSource("provideUsernameForGoodInput")
    void usernameGoodInput(String input, String expected){
        UserInput dialogues = getDialogues(input);
        assertEquals(expected,validator.usernameValidation(dialogues.usernameInput()));
    }

    private static Stream<Arguments> provideUsernameForGoodInput(){
        return Stream.of(
                Arguments.of("User", "User"),
                Arguments.of(" just_a_user ","just_a_user")
        );
    }

    @ParameterizedTest
    @MethodSource("provideUsernameForBadInput")
    void usernameBadInput(String input){
        UserInput dialogues = getDialogues(input);
        assertNull(validator.usernameValidation(dialogues.usernameInput()));
    }

    private static Stream<Arguments> provideUsernameForBadInput(){
        return Stream.of(
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

    //  PAGES

    @ParameterizedTest
    @ValueSource(strings = {"0", "-5", "6000", " 5 000 ", "2304.213"})
    void pagesBadInput(String input){
        UserInput dialogues = getDialogues(input);
        assertNull(validator.validatePages(dialogues.pagesUsersInput()));
    }

    @ParameterizedTest
    @MethodSource("provideNumbersForGoodInput")
    void pagesGoodInput(String input, Integer expected){
        UserInput dialogues = getDialogues(input);
        assertEquals(expected,validator.validatePages(dialogues.pagesUsersInput()));
    }

    private static Stream<Arguments> provideNumbersForGoodInput(){
        return Stream.of(
                Arguments.of("1",1),
                Arguments.of("25",25),
                Arguments.of("4000",4000)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNulls")
    void pagesNullInput(String input){
        UserInput dialogues = getDialogues(input);
        assertNull(validator.validatePages(dialogues.pagesUsersInput()));
    }

    private static Stream<Arguments> provideNulls(){
        return Stream.of(
                Arguments.of(" "),
                Arguments.of("  "),
                Arguments.of("\t"),
                Arguments.of("\n")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"pages", " string", " author ", "#"})
    void pagesStringInput(String input){
        UserInput dialogues = getDialogues(input);
        assertNull(validator.validatePages(dialogues.pagesUsersInput()));
    }

    //AUTHOR

    @ParameterizedTest
    @MethodSource("provideNulls")
    void authorNullInput(String input){
        UserInput dialogues = getDialogues(input);
        assertNull(validator.validateAuthorName(dialogues.authorUserInput()));
    }

    @ParameterizedTest
    @MethodSource("provideAuthorsForGoodInput")
    void authorGoodInput(String input, String expected){
        UserInput dialogues = getDialogues(input);
        assertEquals(expected,validator.validateAuthorName(dialogues.authorUserInput()));
    }

    private static Stream<Arguments> provideAuthorsForGoodInput(){
        return Stream.of(
                Arguments.of("Lee Bardugo", "Lee Bardugo"),
                Arguments.of(" Charlotte Bronte ","Charlotte Bronte"),
                Arguments.of(" NoName","NoName"),
                Arguments.of("ALexandre Dumas ","ALexandre Dumas"),
                Arguments.of("   Gaston Leroux  ","Gaston Leroux")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"author#", "@uthor","auth()r", "a:u;thor", "author%"})
    void authorBadInput(String input){
        UserInput dialogues = getDialogues(input);
        assertNull(validator.validateAuthorName(dialogues.authorUserInput()));
    }

    // ITEM ID

    @ParameterizedTest
    @ValueSource(strings = {"-20", "0", "25.5", "item ID", " ITEM "})
    void idBadInput(String input){
        UserInput dialogues = getDialogues(input);
        assertNull(validator.validateID(dialogues.idUserInput()));
    }

    @ParameterizedTest
    @MethodSource("provideNulls")
    void idNullInput(String input){
        UserInput dialogues = getDialogues(input);
        assertNull(validator.validateID(dialogues.idUserInput()));
    }

    @ParameterizedTest
    @MethodSource("provideNumbersForGoodInput")
    void idGoodInput(String input, Integer expected){
        UserInput dialogues = getDialogues(input);
        assertEquals(expected, validator.validateID(dialogues.idUserInput()));
    }

    // TITLE

    @ParameterizedTest
    @ValueSource(strings = {"title<>", "title/","tit\\le", "tit\tle", "title~"})
    void titleBadInput(String input){
        UserInput dialogues = getDialogues(input);
        assertNull(validator.validateTitle(dialogues.titleUserInput()));
    }

    @ParameterizedTest
    @MethodSource("provideNulls")
    void titleNullInput(String input){
        UserInput dialogues = getDialogues(input);
        assertNull(validator.validateTitle(dialogues.titleUserInput()));
    }

    @ParameterizedTest
    @MethodSource("provideTitlesForGoodInput")
    void titleGoodInput(String input, String expected){
        UserInput dialogues = getDialogues(input);
        assertEquals(expected,validator.validateTitle(dialogues.titleUserInput()));
    }

    private static Stream<Arguments> provideTitlesForGoodInput(){
        return Stream.of(
                Arguments.of("Jane Eyre", "Jane Eyre"),
                Arguments.of(" Jane Eyre ","Jane Eyre"),
                Arguments.of(" Jane Eyre","Jane Eyre"),
                Arguments.of("Jane Eyre ","Jane Eyre"),
                Arguments.of("   Jane Eyre  ","Jane Eyre"),
                Arguments.of("   Jane Eyre?  ","Jane Eyre?"),
                Arguments.of("   Jane Eyre!  ","Jane Eyre!"),
                Arguments.of("   Jane%Eyre  ","Jane%Eyre"),
                Arguments.of("   Jane:Eyre  ","Jane:Eyre")
        );
    }

    // PUBLISHING DATE

    @ParameterizedTest
    @MethodSource("provideDateForNullInput")
    void dateNullInput(String inputYear, String inputMonth, String inputDay){
        UserInput dialoguesYear = getDialogues(inputYear);
        Integer year = dialoguesYear.yearUserInput();
        UserInput dialoguesMonth = getDialogues(inputMonth);
        Integer month = dialoguesMonth.monthUserInput();
        UserInput dialoguesDay = getDialogues(inputDay);
        Integer day = dialoguesDay.dayUserInput();
        assertNull(new BookHandler().validateDate(year,month,day));
    }

    private static Stream<Arguments> provideDateForNullInput(){
        return Stream.of(
                Arguments.of(" ", " ", " "),
                Arguments.of(" ", "  ", "  "),
                Arguments.of("2000", "4", " "),
                Arguments.of("  ", "4", "5"),
                Arguments.of("2000", " ", "5")
        );
    }

    @ParameterizedTest
    @MethodSource("provideDateForBadInput")
    void dateBadInput(String inputYear, String inputMonth, String inputDay){
        UserInput dialoguesYear = getDialogues(inputYear);
        Integer year = dialoguesYear.yearUserInput();
        UserInput dialoguesMonth = getDialogues(inputMonth);
        Integer month = dialoguesMonth.monthUserInput();
        UserInput dialoguesDay = getDialogues(inputDay);
        Integer day = dialoguesDay.dayUserInput();
        assertNull(new BookHandler().validateDate(year,month,day));
    }

    private static Stream<Arguments> provideDateForBadInput(){
        return Stream.of(
                Arguments.of("-4", "2", "21"),
                Arguments.of("2021", "4", "-21"),
                Arguments.of("2000", "-3", "21"),
                Arguments.of("year", "4", "5"),
                Arguments.of("2000", "month", "5"),
                Arguments.of("2000", "4", "day"),
                Arguments.of("2000", "15", "5"),
                Arguments.of("2000", "12", "32")
        );
    }

    @ParameterizedTest
    @MethodSource("provideDateForGoodInput")
    void dateGoodInput(String inputYear, String inputMonth, String inputDay, String expected){
        UserInput dialoguesYear = getDialogues(inputYear);
        Integer year = dialoguesYear.yearUserInput();
        UserInput dialoguesMonth = getDialogues(inputMonth);
        Integer month = dialoguesMonth.monthUserInput();
        UserInput dialoguesDay = getDialogues(inputDay);
        Integer day = dialoguesDay.dayUserInput();
        DateFormat df = new SimpleDateFormat("dd.M.y");
        assertEquals(expected,df.format(new BookHandler().validateDate(year,month,day).getTime()));
    }

    private static Stream<Arguments> provideDateForGoodInput(){
        return Stream.of(
                Arguments.of("1991", "8", "24", "24.8.1991"),
                Arguments.of("988", "7", "21", "21.7.988"),
                Arguments.of("2000", "12", "21", "21.12.2000"),
                Arguments.of("1", "4", "5","05.4.1"),
                Arguments.of("1", "12", "5","05.12.1")
        );
    }

}