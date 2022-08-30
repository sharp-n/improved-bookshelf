package com.company.handlers.item_handlers;

import com.company.User;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.databases.queries.SQLQueries;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class JournalHandler extends ItemHandler<Journal> {

    public List<String> columnTitles = new ArrayList<>(Arrays.asList("item id","type of item","title","pages","borrowed"));

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
        List<Journal> newspapers = new ArrayList<>();
        items.forEach(i->newspapers.add((Journal) i));
        return newspapers;
    }

    @Override
    public List<List<String>> anyItemsToString(List<Journal> newspapers) {
        List<List<String>> newspapersAsStringList = new ArrayList<>();
        for (Journal newspaper: newspapers) {
            newspapersAsStringList.add(itemToString(newspaper));
        }
        return newspapersAsStringList;
    }

    @Override
    public Journal getItem(int itemID, User user, SQLQueries sqlQueries) {
        try {
            ResultSet resultSet = sqlQueries.getItem(itemID, user);
            List<String> itemStr = new ArrayList<>();
            itemStr = getMainOptions(resultSet, itemStr);
            return new Journal(Integer.parseInt(itemStr.get(0)), itemStr.get(2), Integer.parseInt(itemStr.get(3)), Boolean.parseBoolean(itemStr.get(4)));
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

}
