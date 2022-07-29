package com.company.handlers;

import com.company.convertors.ItemConvertor;
import com.company.convertors.JournalConvertor;
import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@NoArgsConstructor
public class JournalHandler extends ItemHandler<Journal> {

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
            ItemConvertor journalConvertor = new JournalConvertor(journal);
            journalsAsStringList.add(journalConvertor.itemToString());
        }
        return journalsAsStringList;
    }

}
