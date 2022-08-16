package com.company.handlers.item_handlers;

import com.company.User;
import com.company.items.Item;
import com.company.items.Newspaper;
import com.company.sqlite.queries.SQLQueries;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class NewspaperHandler extends ItemHandler<Newspaper> {

    public List<String> columnTitles = new ArrayList<>(Arrays.asList("item id","title","pages","borrowed"));

    public List<String> getColumnTitles() {
        return columnTitles;
    }

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

    public Newspaper getItem(int itemID, User user, SQLQueries sqlQueries){
        try {
            ResultSet resultSet = sqlQueries.getItem(itemID, user);
            List<String> itemStr = new ArrayList<>();
            itemStr = getMainOptions(resultSet, itemStr);
            return new Newspaper(Integer.parseInt(itemStr.get(0)), itemStr.get(2), Integer.parseInt(itemStr.get(3)), Boolean.parseBoolean(itemStr.get(4)));
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

}
