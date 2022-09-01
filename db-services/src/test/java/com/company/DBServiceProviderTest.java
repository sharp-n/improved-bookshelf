package com.company;

import com.company.databases.db_services.DBService;
import com.company.databases.db_services.DBServiceProvider;
import com.company.databases.db_services.MySQLDBService;
import com.company.databases.db_services.SQLiteDBService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class DBServiceProviderTest {


    @ParameterizedTest
    @MethodSource("provideOptionNull")
    void getDBServiceByOptionNullTest(String provided){
        Assertions.assertNull(DBServiceProvider.getDBServiceByOption(provided));
    }

    private static Stream<Arguments> provideOptionNull() {
        return Stream.of(
                Arguments.of("null"),
                Arguments.of(""),
                Arguments.of("     "),
                Arguments.of("qwerty"),
                Arguments.of("mysq"),
                Arguments.of("sqlit")
        );
    }

    @ParameterizedTest
    @MethodSource("provideOption")
    void getDBServiceByOptionTest(String provided, DBService expected){
        Assertions.assertEquals(expected.getClass(),DBServiceProvider.getDBServiceByOption(provided).getClass());
    }

    private static Stream<Arguments> provideOption() {
        return Stream.of(
                Arguments.of("databaseMySQL", new MySQLDBService()),
                Arguments.of("databaseSQLite", new SQLiteDBService())
        );
    }

    @ParameterizedTest
    @MethodSource("provideOption")
    void getOptionByDBServiceTest(String expected, DBService provided){
        Assertions.assertEquals(expected,DBServiceProvider.getOptionByDBService(provided));
    }

    @ParameterizedTest
    @MethodSource("provideName")
    void getDBNameByDBServiceTest(String expected, DBService provided){
        Assertions.assertEquals(expected,DBServiceProvider.getDBNameByService(provided));
    }

    private static Stream<Arguments> provideName() {
        return Stream.of(
                Arguments.of("bookshelf", new MySQLDBService()),
                Arguments.of("bookshelf.db", new SQLiteDBService())
        );
    }

}
