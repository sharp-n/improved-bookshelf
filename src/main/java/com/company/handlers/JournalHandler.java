package com.company.handlers;

import com.company.items.Journal;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.company.ConstantsForItemsTable.NEW_LINE;
import static com.company.enums.ActionsWithJournals.*;

@NoArgsConstructor
public class JournalHandler extends ItemHandler<Journal> {

    public JournalHandler(PrintWriter out, Scanner in) {
        super(out, in);
    }

    @Override
    public List<Journal> getSortedItemsByComparator(List<Journal> items, Comparator<Journal> comparator) {
        return items.stream()
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

    @Override
    public String initItemsMenuText(){
        return NEW_LINE + ADD_JOURNAL + NEW_LINE + DELETE_JOURNAL +
                NEW_LINE + TAKE_JOURNAL + NEW_LINE + RETURN_JOURNAL +
                NEW_LINE + SHOW_JOURNAL + NEW_LINE;
    }
}
