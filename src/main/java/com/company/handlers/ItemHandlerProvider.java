package com.company.handlers;

import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.items.Newspaper;


import java.util.HashMap;

import java.util.Map;

public class ItemHandlerProvider {

    static Map<Class<? extends Item>, ItemHandler<? extends Item>> classItemHandlerMap = new HashMap<>();
    static Map<Class<? extends Item>, String> classSimpleNameOfClassMap = new HashMap<>();

    private ItemHandlerProvider() {
    }

    static {
        classItemHandlerMap.put(Newspaper.class, new NewspaperHandler());
        classItemHandlerMap.put(Journal.class, new NewspaperHandler()); // TODO handlers
        classItemHandlerMap.put(Book.class, new BookHandler()); // TODO handlers

        classItemHandlerMap.forEach((k, v) -> classSimpleNameOfClassMap.put(k, k.getSimpleName()));
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
        return getNewspaperHandler();
    }
    public static ItemHandler getBookHandler() {
        return getBookHandler();
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
