package com.company.handlers.item_handlers;

import com.company.User;
import com.company.enums.SortingMenu;
import com.company.handlers.Librarian;
import com.company.items.Book;
import com.company.items.Item;
import com.company.databases.queries.SQLQueries;
import com.company.tomcat_server.servlet_service.HTMLFormBuilder;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.table.TableUtil.NEW_LINE;
import static com.company.tomcat_server.constants.ParametersConstants.COMPARATOR_PARAM;
import static com.company.tomcat_server.servlet_service.HTMLFormBuilder.NEW_LINE_TAG;
import static com.company.enums.SortingMenu.*;

@NoArgsConstructor // TODO
public class BookHandler extends ItemHandler<Book> {

    public List<String> columnTitles = new ArrayList<>(Arrays.asList("item id", "type of item","title","pages","borrowed","author", "publishing date"));


    public BookHandler(PrintWriter out, Scanner in) {
        super(out,in);
    }

    public List<String> getColumnTitles() {
        return columnTitles;
    }

    @Override
    public List<Book> getSortedItemsByComparator(List<Item> items, Comparator<Item> comparator) {
        return cast(items).stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Book createItem(List<String> options){
        int itemID = Integer.parseInt(options.get(0));
        String title = options.get(1);
        int pages = Integer.parseInt(options.get(2));
        String author = options.get(3);
        String date = options.get(4);

        GregorianCalendar publishingDate = getDateFromString(date);
        if (publishingDate == null) {return null;}
        return new Book(itemID,title, author, publishingDate,pages);
    }

    public GregorianCalendar validateDate(Integer year, Integer month, Integer day) {
        if (day == null || month == null || year == null) {
            return null;
        }
        if (day > 31) {
            return null;
        }
        if (month > 12) {
            return null;
        }
        if (Librarian.checkItemForValidity(year) && Librarian.checkItemForValidity(month)
                && Librarian.checkItemForValidity(day)) {
            return new GregorianCalendar(year, month - 1, day);
        }
        return null;
    }

    @Override
    public List<String> getItem(Integer itemID) {
        String author = "";
        List<String> itemOptions = new ArrayList<>(super.getItem(itemID));

        author = validator.validateAuthorName(userInput.authorUserInput());
        if (author == null) {
            return Collections.emptyList();
        }

        Integer publishingYear = userInput.yearUserInput();
        Integer publishingMonth = userInput.monthUserInput();
        Integer publishingDay = userInput.dayUserInput();

        itemOptions.add(author);
        itemOptions.add(publishingDay + " . " + publishingMonth + " . " + publishingYear);

        return itemOptions;

    }

    @Override
    public List<List<String>> anyItemsToString(List<Book> books) {
        List<List<String>> booksAsStringList = new ArrayList<>();
        for (Book book: books) {
            booksAsStringList.add(itemToString(book));
        }
        return booksAsStringList;
    }

    public List<Book> cast(List<Item> items) {
        List<Book> books = new ArrayList<>();
        items.forEach(i->books.add((Book)i));
        return books;
    }

    @Override
    public String genSortingMenuText() {
        return super.genSortingMenuText() + SortingMenu.AUTHOR
                + NEW_LINE + SortingMenu.PUBLISHING_DATE + NEW_LINE;
    }

    public String authorToString(Book book){
        if(book.getAuthor()==null) return "NULL";
        return book.getAuthor();
    }

    public String publishingDateToString(Book book){
        SimpleDateFormat df = new SimpleDateFormat("dd.M.y");
        if(book.getPublishingDate()==null) return "NULL";
        return df.format(book.getPublishingDate().getTime());
    }

    @Override
    public List<String> itemToString(Item item){
        List<String> bookAsList = super.itemToString(item);
        bookAsList.add(authorToString((Book)item));
        bookAsList.add(publishingDateToString((Book)item));
        return bookAsList;
    }

    @Override
    public String genAddFormContent() {
        HTMLFormBuilder formBuild = new HTMLFormBuilder();
        String form = super.genAddFormContent();
        return form.substring(0,form.lastIndexOf("<"))
                + formBuild.genLabel("Author: ",AUTHOR.getDbColumn())
                + formBuild.genTextField(AUTHOR.getDbColumn(),AUTHOR.getDbColumn())
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genLabel("Publishing date: ",PUBLISHING_DATE.getDbColumn())
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genLabel("Day: ","day")
                + formBuild.genTextField("day", "day")
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genLabel("Month: ","month")
                + formBuild.genTextField("month", "month")
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genLabel("Year: ","year")
                + formBuild.genTextField("year", "year")
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genButton("Add book");
    }

    @Override
    public List<String> convertItemParametersMapToList(Map<String, String[]> params){
        List<String> paramsList = super.convertItemParametersMapToList(params);
        String date = paramsList.get(4) + "." + paramsList.get(5) + "." + paramsList.get(6);
        for (int i = 3; i>0; i--){
            paramsList.remove(paramsList.size()-1);
        }
        paramsList.add(date);
        return paramsList;
    }

    @Override
    public String genSortFormContent() {
        HTMLFormBuilder formBuild = new HTMLFormBuilder();
        String form = super.genSortFormContent();
        return form.substring(0,form.lastIndexOf("<"))
                + formBuild.genRadioButton(COMPARATOR_PARAM,AUTHOR.getDbColumn(),AUTHOR.getOption())
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genRadioButton(COMPARATOR_PARAM,PUBLISHING_DATE.getDbColumn(),PUBLISHING_DATE.getOption())
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genButton("Sort");
    }

    @Override
    public List<List<String>> getItemsAsStringListFromResultSet(ResultSet resultSet) throws SQLException {
        List<List<String>> itemsStr = new ArrayList<>();
        while (resultSet.next()) {
            List<String> itemStr = new ArrayList<>();
            itemStr = getMainOptions(resultSet,itemStr);
            itemsStr.add(itemStr);
        }
        return itemsStr;
    }

    @Override
    List<String> getMainOptions(ResultSet resultSet, List<String> itemStr) throws SQLException {
        itemStr = super.getMainOptions(resultSet, itemStr);
        itemStr.add(Integer.toString(resultSet.getInt(AUTHOR.getDbColumn())));
        itemStr.add(Integer.toString(resultSet.getInt(PUBLISHING_DATE.getDbColumn())));
        return itemStr;
    }

    @Override
    public Book getItem(int itemID, User user, SQLQueries sqlQueries) {
        try {
            ResultSet resultSet = sqlQueries.getItem(itemID, user);
            List<String> itemStr = new ArrayList<>();
            itemStr = getMainOptions(resultSet, itemStr);
            String dateStr = itemStr.get(6);
            System.out.println(itemStr.get(4));
            GregorianCalendar publishingDate = getDateFromString(dateStr);
            return new Book(
                    Integer.parseInt(itemStr.get(0)),
                    itemStr.get(2),
                    itemStr.get(5),
                    publishingDate,
                    Integer.parseInt(itemStr.get(3)),
                    Boolean.parseBoolean(itemStr.get(4)));
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

    public GregorianCalendar getDateFromString(String dateStr){
        String [] date = dateStr.split("\\.");
        return validateDate(Integer.parseInt(date[2].trim()),Integer.parseInt(date[1].trim()),Integer.parseInt(date[0].trim()));
    }
}
