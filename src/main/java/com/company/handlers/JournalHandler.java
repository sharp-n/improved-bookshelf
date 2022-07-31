package com.company.handlers;

import com.company.items.Item;
import com.company.items.Journal;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class JournalHandler extends ItemHandler<Journal> {

    public List<String> columnTitles = new ArrayList<>(Arrays.asList("item id","title","pages","borrowed"));

    public List<String> getColumnTitles() {
        return columnTitles;
    }

    public JournalHandler(PrintWriter out, Scanner in) {
        super(out,in);
    }

    @Override
    public List<Journal> getSortedItemsByComparator(List<Item> items, Comparator<Item> comparator) {
        return cast(items).stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Journal createItem(List<String> options){
        int itemID = Integer.parseInt(options.get(0));
        String title = options.get(1);
        int pages = Integer.parseInt(options.get(2));
        return new Journal(itemID,title,pages);
    }

    public List<Journal> cast(List<Item> items) {
        List<Journal> journals = new ArrayList<>();
        items.forEach(i->journals.add((Journal) i));
        return journals;
    }

    @Override
    public List<List<String>> anyItemsToString(List<Journal> journals) {
        List<List<String>> journalsAsStringList = new ArrayList<>();
        for (Journal journal: journals) {
            journalsAsStringList.add(itemToString(journal));
        }
        return journalsAsStringList;
    }

}
