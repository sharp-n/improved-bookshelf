package com.company;

import java.util.*;

public class ConstantsForItemsTable {

    Map<Integer,List<String>> allTheColumnsForItems = new HashMap<>();

    List<String> columnsForAllItems = new ArrayList<>(Arrays.asList("item id", "title", "author", "publishing date", "pages", "borrowed"));

    List<String> columnsForBooks = columnsForAllItems;

    List<String> columnsForJournals = new ArrayList<>(Arrays.asList("item id", "title", "pages", "borrowed"));

    List<String> columnsForNewspapers = columnsForJournals;

    {
        allTheColumnsForItems.put(4,columnsForJournals);
        allTheColumnsForItems.put(6,columnsForAllItems);
    }

}