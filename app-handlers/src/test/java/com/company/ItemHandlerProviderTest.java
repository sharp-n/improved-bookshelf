package com.company;

import com.company.handlers.item_handlers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ItemHandlerProviderTest {

    @ParameterizedTest
    @MethodSource("provideHandlers")
    void getClassByHandlerTest(ItemHandler<Item> providedItemHandler,String expected){
        Assertions.assertEquals(expected, ItemHandlerProvider.getClassByHandler(providedItemHandler).getSimpleName().toLowerCase());
    }

    private static Stream<Arguments> provideHandlers(){
        return Stream.of(
                Arguments.of(new BookHandler(),"book"),
                Arguments.of(new ComicsHandler(),"comics"),
                Arguments.of(new JournalHandler(),"journal"),
                Arguments.of(new NewspaperHandler(),"newspaper")
        );
    }

}
