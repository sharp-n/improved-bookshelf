package com.company.handlers.item_handlers;

import com.company.*;
import com.company.databases.queries.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ItemHandlerProvider {

    static Map<Class<? extends Item>, ItemHandler<? extends Item>> classItemHandlerMap = new HashMap<>();
    static Map<Class<? extends Item>, String> classSimpleNameOfClassMap = new HashMap<>();


    private ItemHandlerProvider() {
    }

    static {
        classItemHandlerMap.put(Newspaper.class, new NewspaperHandler());
        classItemHandlerMap.put(Book.class, new BookHandler());
        classItemHandlerMap.put(Comics.class, new ComicsHandler());
        classItemHandlerMap.put(Journal.class, new JournalHandler());

        classItemHandlerMap.forEach((k, v) -> classSimpleNameOfClassMap.put(k, k.getSimpleName()));
    }

    public static SQLQueries<? extends Item> getSQLQueryClassByHandler(ItemHandler<? extends Item> itemHandler, Connection connection) {
        Map<Class<? extends Item>, SQLQueries<? extends Item>> itemHandlerQueriesMap = initClassSQLQueriesMap(connection);
        if (itemHandler.getClass().isAssignableFrom(DefaultItemHandler.class)) {
            return new SQLDefaultQueries(connection);
        }
        Class<? extends Item> classOfItem = getClassByHandler(itemHandler);
        for (Map.Entry<Class<? extends Item>, SQLQueries<? extends Item>> itemHandlerSQLQueryEntry : itemHandlerQueriesMap.entrySet()) {
            if (itemHandlerSQLQueryEntry.getKey().isAssignableFrom(classOfItem)){
                return itemHandlerSQLQueryEntry.getValue();
            }

        }
        return null;// TODO fix null
    }

    public static Map<Class<? extends Item>, SQLQueries<? extends Item>> initClassSQLQueriesMap(Connection connection){
        Map<Class<? extends Item>, SQLQueries<? extends Item>> itemHandlerQueriesMap = new HashMap<>();
        itemHandlerQueriesMap.put(Book.class, new SQLBooksQueries(connection));
        itemHandlerQueriesMap.put(Journal.class, new SQLJournalQueries(connection));
        itemHandlerQueriesMap.put(Comics.class, new SQLComicsQueries(connection));
        itemHandlerQueriesMap.put(Newspaper.class, new SQLNewspaperQueries(connection));
        itemHandlerQueriesMap.put(Item.class, new SQLDefaultQueries(connection));
        return itemHandlerQueriesMap;
    }

    public static Class<? extends Item> getClassByHandler(ItemHandler<? extends Item> itemHandler) {
        for (Map.Entry<Class<? extends Item>, ItemHandler<? extends Item>> classItemHandlerEntry : classItemHandlerMap.entrySet()) {
            if (classItemHandlerEntry.getValue().getClass().isAssignableFrom(itemHandler.getClass())){
                    //classItemHandlerEntry.getValue().getClass().getSimpleName().equals(itemHandler.getClass().getSimpleName()))
                return classItemHandlerEntry.getKey();
            }
        }
        return null;// TODO fix null
    }

    public static ItemHandler getHandlerByClass(Class<? extends Item> classOfItem) {
        for (Map.Entry<Class<? extends Item>, ItemHandler<? extends Item>> classItemHandlerEntry : classItemHandlerMap.entrySet()) {
            if (classItemHandlerEntry.getKey().equals(classOfItem)) {
                return classItemHandlerEntry.getValue();
            }
        }
        return null;// TODO fix null
    }

    public static ItemHandler getNewspaperHandler() {
        return new NewspaperHandler();
    }
    public static ItemHandler getBookHandler() {
        return new BookHandler();
    }
    public static ItemHandler getComicsHandler() {
        return new ComicsHandler();
    }
    public static ItemHandler getJournalHandler() {
        return new JournalHandler();
    }

    public static Class<? extends Item> getClassBySimpleNameOfClass(String simpleClassName) {
        for (Map.Entry<Class<? extends Item>, String> classStringEntry : classSimpleNameOfClassMap.entrySet()) {
            if (classStringEntry.getValue().equals(simpleClassName)) {
                return classStringEntry.getKey();
            }
        }
        return null; // TODO fix null
    }

}
