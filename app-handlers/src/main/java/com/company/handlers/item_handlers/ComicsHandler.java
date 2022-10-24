package com.company.handlers.item_handlers;

import com.company.*;
import com.company.databases.queries.SQLQueries;
import com.company.db.repositories.ItemRepository;
import com.company.db.repositories.UserRepository;
import com.company.db.services.ItemService;
import com.company.db.services.UserService;
import com.company.enums.SortingMenu;
import com.company.table.TableUtil;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ComicsHandler extends ItemHandler<Comics> {

    private static final Logger log
            = Logger.getLogger(ComicsHandler.class);


    public List<String> columnTitles = new ArrayList<>(Arrays.asList("item id", "type of item", "title", "pages", "borrowed", "publishing"));

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
    public List<String> getItem(Integer itemID) {
        String publishing = "";
        List<String> itemOptions = new ArrayList<>(super.getItem(itemID));

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
        return super.genSortingMenuText() + SortingMenu.PUBLISHER + TableUtil.NEW_LINE;
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

    @Override
    public String genAddFormContent() {
        HTMLFormBuilder formBuild = new HTMLFormBuilder();
        String form = super.genAddFormContent();
        return form.substring(0,form.lastIndexOf("<"))
                + formBuild.genLabel("Publishing: ", SortingMenu.PUBLISHER.getDbColumn())
                + formBuild.genTextField(SortingMenu.PUBLISHER.getDbColumn(), SortingMenu.PUBLISHER.getDbColumn())
                + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
                + formBuild.genButton("Add comics");
    }

    @Override
    public String genSortFormContent() {
        HTMLFormBuilder formBuild = new HTMLFormBuilder();
        String form = super.genSortFormContent();
        return form.substring(0,form.lastIndexOf("<"))
                + formBuild.genRadioButton(SortingMenu.PUBLISHER.getDbColumn(), SortingMenu.PUBLISHER.getDbColumn(), SortingMenu.PUBLISHER.getOption())
                + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
                + formBuild.genButton("Sort");
    }

    @Override
    public List<List<String>> getItemsAsStringListFromResultSet(ResultSet resultSet) {
        try {
            List<List<String>> itemsStr = new ArrayList<>();
            while (resultSet.next()) {
                List<String> itemStr = new ArrayList<>();
                itemStr = getMainOptions(resultSet, itemStr);
                itemsStr.add(itemStr);
            }
            return itemsStr;
        } catch (SQLException sqlException){
            log.error(sqlException.getMessage() + " : " + ComicsHandler.class.getSimpleName() + " : getItemsAsStringListFromResultSet()");
            return Collections.emptyList();
        }
    }

    @Override
    List<String> getMainOptions(ResultSet resultSet, List<String> itemStr){
        try {
            itemStr = getMainOptions(resultSet, itemStr);
            itemStr.add(resultSet.getString(SortingMenu.PUBLISHER.getDbColumn()));
            return itemStr;
        } catch (SQLException sqlException){
            log.error(sqlException.getMessage()  + " : " + ComicsHandler.class.getSimpleName() + " : getMainOptions()");
            return itemStr;
        }
    }

    @Override
    public Comics getItem(int itemID, User user, SQLQueries sqlQueries) {

        ResultSet resultSet = sqlQueries.getItem(itemID, user);
        List<String> itemStr = new ArrayList<>();
        itemStr = getMainOptions(resultSet, itemStr);
        return new Comics(
                Integer.parseInt(itemStr.get(0)),
                itemStr.get(2),
                Integer.parseInt(itemStr.get(3)),
                itemStr.get(5),
                Boolean.parseBoolean(itemStr.get(4)));
    }

    public List<Item> convertToCoreDefinedTypeOfItems(List<com.company.db.entities.Item> items){
        List<Item> comics = new ArrayList<>();

        items.forEach(item -> comics.add(
                new Comics(
                        item.getId(),
                        item.getTitle(),
                        item.getPages(),
                        item.getPublisher(),
                        item.isBorrowed())));
        return comics;
    }

    public Comics convertToCoreDefinedItem(com.company.db.entities.Item item){
        return new Comics(
                item.getId(),
                item.getTitle(),
                item.getPages(),
                item.getPublisher(),
                item.isBorrowed());
    }

    public boolean addItemToDB(Comics item, String userName, ItemService itemService, UserService userService){
        try {
            itemService.addItem(item, userName,userService);
            return true;
        } catch (Exception e){
            log.error(e.getMessage() + " : " + ComicsHandler.class.getSimpleName() + " : addItemToDB()");
            return false;
        }
    }

}
