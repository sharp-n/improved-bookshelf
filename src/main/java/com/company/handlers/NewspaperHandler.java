package com.company.handlers;

import com.company.items.Item;
import com.company.items.Newspaper;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@NoArgsConstructor
public class NewspaperHandler extends ItemHandler<Newspaper>{

    public NewspaperHandler(PrintWriter out, Scanner in) {
        super(out,in);
    }

    @Override
    public List<Newspaper> getSortedItemsByComparator(List<Item> items, Comparator<Item> comparator) {
        return cast(items).stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Newspaper createItem(List<String> options){
        int itemID = Integer.parseInt(options.get(0));
        String title = options.get(1);
        int pages = Integer.parseInt(options.get(2));
        return new Newspaper(itemID,title,pages);
    }

    public List<Newspaper> cast(List<Item> items) {
        List<Newspaper> newspapers = new ArrayList<>();
        items.forEach(i->newspapers.add((Newspaper) i));
        return newspapers;
    }

    @Override
    public List<List<String>> anyItemsToString(List<Newspaper> newspapers) {
        List<List<String>> newspapersAsStringList = new ArrayList<>();
        for (Newspaper newspaper: newspapers) {
            newspapersAsStringList.add(itemToString(newspaper));
        }
        return newspapersAsStringList;
    }

}
