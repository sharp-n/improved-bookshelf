package com.company.handlers.item_handlers;

import com.company.User;
import com.company.enums.SortingMenu;
import com.company.items.Comics;
import com.company.items.Item;
import com.company.items.Newspaper;
import com.company.sqlite.queries.SQLQueries;
import com.company.tomcat_server.servlet_service.HTMLFormBuilder;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.table.TableUtil.NEW_LINE;
import static com.company.tomcat_server.constants.FormConstants.*;
import static com.company.tomcat_server.servlet_service.HTMLFormBuilder.NEW_LINE_TAG;

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

    @Override
    public String genAddFormContent() {
        HTMLFormBuilder formBuild = new HTMLFormBuilder();
        String form = super.genAddFormContent();
        return form.substring(0,form.lastIndexOf("<"))
                + formBuild.genLabel("Publishing: ",PUBLISHING_PARAM)
                + formBuild.genTextField(PUBLISHING_PARAM,PUBLISHING_PARAM)
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genButton("Add comics");
    }

    @Override
    public String genSortFormContent() {
        HTMLFormBuilder formBuild = new HTMLFormBuilder();
        String form = super.genSortFormContent();
        return form.substring(0,form.lastIndexOf("<"))
                + formBuild.genRadioButton(PUBLISHING_PARAM,PUBLISHING_PARAM,"Publisher")
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genButton("Sort");
    }

    @Override
    public List<List<String>> getItemsAsStringListFromResultSet(ResultSet resultSet) throws SQLException {
        List<List<String>> itemsStr = new ArrayList<>();
        List<String> itemStr = new ArrayList<>();
        while (resultSet.next()) {
            itemStr = getMainOptions(resultSet,itemStr);
            itemStr.add(Integer.toString(resultSet.getInt("publisher")));
            itemsStr.add(itemStr);
        }
        return itemsStr;
    }

    @Override
    List<String> getMainOptions(ResultSet resultSet, List<String> itemStr) throws SQLException {
        itemStr = getMainOptions(resultSet, itemStr);
        itemStr.add(resultSet.getString("publisher"));
        return itemStr;
    }

    @Override
    public Comics getItem(int itemID, User user, SQLQueries sqlQueries) {
        try {
            ResultSet resultSet = sqlQueries.getItem(itemID, user);
            List<String> itemStr = new ArrayList<>();
            itemStr = getMainOptions(resultSet, itemStr);
            return new Comics(
                    Integer.parseInt(itemStr.get(0)),
                    itemStr.get(2),
                    Integer.parseInt(itemStr.get(3)),
                    itemStr.get(5),
                    Boolean.parseBoolean(itemStr.get(4)));
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }
}
