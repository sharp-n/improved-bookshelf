package com.company.handlers.item_handlers;

import com.company.enums.SortingMenu;
import com.company.items.Comics;
import com.company.items.Item;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.table.TableUtil.NEW_LINE;

@NoArgsConstructor
public class ComicsHandler extends ItemHandler<Comics> {

    public List<String> columnTitles = new ArrayList<>(Arrays.asList("item id", "title", "pages", "borrowed", "publishing"));

    public List<String> getColumnTitles() {
        return columnTitles;
    }

    public ComicsHandler(PrintWriter out, Scanner in) {
        super(out, in);
    }

    @Override
    public List<Comics> getSortedItemsByComparator(List<Item> items, Comparator<Item> comparator) {
        return cast(items).stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Comics createItem(List<String> options) {
        int itemID = Integer.parseInt(options.get(0));
        String title = options.get(1);
        int pages = Integer.parseInt(options.get(2));
        String publishing = options.get(3);
        return new Comics(itemID, title, pages, publishing);
    }

    @Override
    public List<String> getItem() {
        String publishing = "";
        List<String> itemOptions = new ArrayList<>(super.getItem());

        publishing = validator.validateAuthorName(userInput.publishingUserInput());
        if (publishing == null) {
            return Collections.emptyList();
        }
        itemOptions.add(publishing);

        return itemOptions;
    }

    public List<Comics> cast(List<Item> items) {
        List<Comics> comics = new ArrayList<>();
        items.forEach(i -> comics.add((Comics) i));
        return comics;
    }

    @Override
    public String genSortingMenuText() {
        return super.genSortingMenuText() + SortingMenu.PUBLISHING + NEW_LINE;
    }


    @Override
    public List<List<String>> anyItemsToString(List<Comics> someComics) {
        List<List<String>> someComicsAsStringList = new ArrayList<>();
        for (Comics comics : someComics) {
            someComicsAsStringList.add(itemToString(comics));
        }
        return someComicsAsStringList;
    }

    public String publishingToString(Comics comics){
        if(comics.getPublishing()==null) return "NULL";
        return comics.getPublishing();
    }

    @Override
    public List<String> itemToString(Item item){
        List<String> bookAsList = super.itemToString(item);
        bookAsList.add(publishingToString((Comics)item));
        return bookAsList;
    }

}

