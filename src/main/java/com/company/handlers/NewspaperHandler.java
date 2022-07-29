package com.company.handlers;

import com.company.items.Newspaper;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
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
    public List<Newspaper> getSortedItemsByComparator(List<Newspaper> items, Comparator<Newspaper> comparator) {
        return items.stream()
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

}
