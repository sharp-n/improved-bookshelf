package com.company;

import com.company.handlers.BookHandler;
import com.company.handlers.ItemHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProjectHandlerTest {

    ProjectHandler getServerHandler(String x) {
        ByteArrayInputStream in = new ByteArrayInputStream(x.getBytes());
        Scanner scanner = new Scanner(in);
        return new ProjectHandler(scanner, new PrintWriter(System.out));
    }

    // NUMBER OF FILES INPUT

    @ParameterizedTest
    @MethodSource("provideNulls")
    void numberOfFilesNullInput(String input){
        ProjectHandler serverHandler = getServerHandler(input);
        assertNull(serverHandler.usersFilesMenuChoice(new UserInput(serverHandler.out,serverHandler.in)));
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
    @MethodSource("provideNumberOfFilesForGoodInput")
    void numberOfFilesGoodInput(String input, Integer expected){
        ProjectHandler serverHandler = getServerHandler(input);
        assertEquals(expected,serverHandler.usersFilesMenuChoice(new UserInput(serverHandler.out,serverHandler.in)));
    }

    private static Stream<Arguments> provideNumberOfFilesForGoodInput(){
        return Stream.of(
                Arguments.of("1", 1),
                Arguments.of(" 2 ","2")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNumberOfFilesForBadInput")
    void numberOfFilesBadInput(String input){
        ProjectHandler serverHandler = getServerHandler(input);
        assertNull(serverHandler.usersFilesMenuChoice(new UserInput(serverHandler.out,serverHandler.in)));
    }

    private static Stream<Arguments> provideNumberOfFilesForBadInput(){
        return Stream.of(
                Arguments.of("sdvczxv"),
                Arguments.of("r"),
                Arguments.of("first")
        );
    }

    // USER CREATION

    @ParameterizedTest
    @MethodSource("provideUsersForGoodInput")
    void userCreationGoodInput(String input, User expected){
        ProjectHandler serverHandler = getServerHandler(input);
        boolean validUserName = false;
        assertEquals(expected.userName,serverHandler.createUser(new UserInput(serverHandler.out,serverHandler.in),validUserName).userName);
    }

    private static Stream<Arguments> provideUsersForGoodInput(){
        return Stream.of(
                Arguments.of("user", new User("user")),
                Arguments.of("UserName ", new User("UserName")),
                Arguments.of("  user_name", new User("user_name"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideUsersForExitInput")
    void userCreationBadInput(String input, User expected){
        ProjectHandler serverHandler = getServerHandler(input);
        boolean validUserName = false;
        assertEquals(expected.userName,serverHandler.createUser(new UserInput(serverHandler.out,serverHandler.in),validUserName).userName);
    }

    private static Stream<Arguments> provideUsersForExitInput(){
        return Stream.of(
                Arguments.of("exit", new User("exit")),
                Arguments.of(" exit", new User("exit"))
        );
    }

    // MAIN MENU CHOICE

    @ParameterizedTest
    @MethodSource("provideVariantsForMainMenuBadInput")
    void mainMenuBadInput(String input){
        ProjectHandler serverHandler = getServerHandler(input);
        assertNull(serverHandler.getUsersMainMenuChoice(
                new BookHandler(new PrintWriter(System.out,true),new Scanner(System.in)).initItemsMenuText(),
                new UserInput(serverHandler.out,serverHandler.in)));
    }

    private static Stream<Arguments> provideVariantsForMainMenuBadInput(){
        return Stream.of(
                Arguments.of("1.1"),
                Arguments.of("-35.6")
        );
    }


    @ParameterizedTest
    @MethodSource("provideVariantsForMainMenuGoodInput")
    void mainMenuGoodInput(String input, Integer expected){
        ProjectHandler serverHandler = getServerHandler(input);
        assertEquals(expected, serverHandler.getUsersMainMenuChoice(
                new BookHandler(new PrintWriter(System.out,true),new Scanner(System.in)).initItemsMenuText(),
                new UserInput(serverHandler.out,serverHandler.in)));
    }

    private static Stream<Arguments> provideVariantsForMainMenuGoodInput(){
        return Stream.of(
                Arguments.of("25", 25),
                Arguments.of("-25", -25),
                Arguments.of("0", 0),
                Arguments.of("11", 11)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNulls")
    void mainMenuNullInput(String input){
        ProjectHandler serverHandler = getServerHandler(input);
        assertNull(serverHandler.getUsersMainMenuChoice(
                new BookHandler(new PrintWriter(System.out,true),new Scanner(System.in)).initItemsMenuText(),
                new UserInput(serverHandler.out,serverHandler.in)));
    }

    // ONE FILE CHOICE

    //@ParameterizedTest
    //@MethodSource("provideFilesForOneFileChoice")
    //void oneFileChoice(String input, String expected){
    //    ProjectHandler projectHandler = getServerHandler(input);
    //    projectHandler.initOneFileWork(new User(input));
    //    assertEquals(expected,projectHandler.pathForFileToWorkWith.workWithOneFile.filePath.toString());
    //    assertEquals(System.getProperty("user.home") + "\\book_shelf\\items_default.txt",projectHandler.pathForFileToWorkWith.workWithJournalFile.filePath.toString());
    //    assertEquals(System.getProperty("user.home") + "\\book_shelf\\items_default.txt",projectHandler.pathForFileToWorkWith.workWithNewspaperFile.filePath.toString());
    //}

    private static Stream<Arguments> provideFilesForOneFileChoice(){
        return Stream.of(
                Arguments.of("user", System.getProperty("user.home") + "\\book_shelf\\items_user.txt"),
                Arguments.of("user_name", System.getProperty("user.home") + "\\book_shelf\\items_user_name.txt"),
                Arguments.of("default", System.getProperty("user.home") + "\\book_shelf\\items_default.txt")
        );
    }

    // FEW FILES CHOICE

    //@ParameterizedTest
    //@MethodSource("provideFilesForFewFilesChoice")
    //void fewFilesChoice(String input){
    //    ProjectHandler serverHandler = getServerHandler(input);
    //    serverHandler.initFilePerItemWork(new User(input));
    //    assertEquals(System.getProperty("user.home") + "\\book_shelf\\items_books_" + input + ".txt",serverHandler.pathForFileToWorkWith.workWithBookFile.filePath.toString());
    //    assertEquals(System.getProperty("user.home") + "\\book_shelf\\items_journals_" + input + ".txt",serverHandler.pathForFileToWorkWith.workWithJournalFile.filePath.toString());
    //    assertEquals(System.getProperty("user.home") + "\\book_shelf\\items_newspaper_" + input + ".txt",serverHandler.pathForFileToWorkWith.workWithNewspaperFile.filePath.toString());
    //}

    private static Stream<Arguments> provideFilesForFewFilesChoice(){
        return Stream.of(
                Arguments.of("user"),
                Arguments.of("user_name"),
                Arguments.of("default")
        );
    }

}