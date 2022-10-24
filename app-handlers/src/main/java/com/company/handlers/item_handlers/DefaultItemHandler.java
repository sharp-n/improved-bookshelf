package com.company.handlers.item_handlers;

import com.company.Item;
import com.company.Newspaper;
import com.company.User;
import com.company.databases.queries.SQLQueries;
import com.company.table.TableUtil;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@NoArgsConstructor
public class DefaultItemHandler extends ItemHandler<Item> {

    private static final Logger log
            = Logger.getLogger(DefaultItemHandler.class);

    public DefaultItemHandler(PrintWriter out, Scanner in) {
        super(out, in);
    }

    @Override
    public List<Item> getSortedItemsByComparator(List<Item> items, Comparator<Item> comparator) {
        return items.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(List<String> options) {
        return null;
    }

    @Override
    public List<List<String>> anyItemsToString(List<Item> items) { // todo fix converting
        List<List<String>> itemsAsStringList = new ArrayList<>();
        for (Item item: items) {
            ItemHandler itemHandler = ItemHandlerProvider.getHandlerByClass(item.getClass());
            itemsAsStringList.add(itemHandler.itemToString(item));
        }
        TableUtil tableUtil = new TableUtil(columnTitles,itemsAsStringList, out);
        tableUtil.validateRows();
        return tableUtil.getRows();
    }

    @Override
    public Item getItem(int itemID, User user, SQLQueries sqlQueries){
        try {
            ResultSet resultSet = sqlQueries.getItem(itemID, user);
            List<String> itemStr = new ArrayList<>();
            itemStr = getMainOptions(resultSet, itemStr);
            return new Item(Integer.parseInt(itemStr.get(0)), itemStr.get(2), Integer.parseInt(itemStr.get(3)), Boolean.parseBoolean(itemStr.get(4)));
        } catch (SQLException sqlException){
            log.error(sqlException.getMessage() + " : " + DefaultItemHandler.class.getSimpleName() + " : getItem()");
            return null;
        }
    }

    public List<Item> convertToCoreDefinedTypeOfItems(List<com.company.db.entities.Item> items){
        List<Item> itemsCore = new ArrayList<>();

        items.forEach(item -> itemsCore.add(
                new Item(
                        item.getId(),
                        item.getTitle(),
                        item.getPages(),
                        item.isBorrowed())));
        return itemsCore;
    }

    public Item convertToCoreDefinedItem(com.company.db.entities.Item item){
        return new Item(
                item.getId(),
                item.getTitle(),
                item.getPages(),
                item.isBorrowed());
    }
}
