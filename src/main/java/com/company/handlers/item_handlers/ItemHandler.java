package com.company.handlers.item_handlers;

import com.company.*;
import com.company.enums.MainMenu;
import com.company.handlers.Librarian;
import com.company.items.Item;

import com.company.items.Newspaper;
import com.company.sqlite.queries.SQLQueries;
import com.company.tomcat_server.constants.ParametersConstants;
import com.company.tomcat_server.constants.URLConstants;
import com.company.tomcat_server.servlet_service.HTMLFormBuilder;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.company.Validator.BAD_NUMBER_VALIDATION_MESSAGE;
import static com.company.enums.ActionsWithItem.*;
import static com.company.enums.SortingMenu.*;
import static com.company.table.TableUtil.NEW_LINE;
import static com.company.tomcat_server.constants.FormConstants.*;
import static com.company.tomcat_server.servlet_service.HTMLFormBuilder.NEW_LINE_TAG;

@NoArgsConstructor
public abstract class ItemHandler<T extends Item> {

    //
    // journals deleting - 3.40 - with tests, 1.4- without
    // adding comics - 15.0
    // adding journals - 3.31
    //

    Scanner in; // TODO fix input/output
    PrintWriter out;
    public Validator validator;
    public UserInput userInput;

    public List<String> columnTitles = new ArrayList<>(Arrays.asList("item id","title","pages","borrowed","author", "publishing date"));

    public List<String> getColumnTitles() {
        return columnTitles;
    }

    protected ItemHandler(PrintWriter out, Scanner in) {
        this.out = out;
        this.in = in;
        this.validator = new Validator(out);
        this.userInput = new UserInput(out,in);
    }

    public abstract List<T> getSortedItemsByComparator(List<Item> items, Comparator<Item> comparator);

    public abstract T createItem(List<String> options);

    public List<String> getItem() {
        Integer itemID = validator.validateID(userInput.idUserInput());

        if (itemID == null) {
            validator.printBadValidationMessage(BAD_NUMBER_VALIDATION_MESSAGE);
            return Collections.emptyList();
        }

        String title = validator.validateTitle(userInput.titleUserInput());
        if (!Librarian.checkItemForValidity(title)) return Collections.emptyList();

        Integer numOfPages = validator.validatePages(userInput.pagesUsersInput());
        if (numOfPages == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(itemID.toString(), title, numOfPages.toString());
    }

    public String initItemsMenuText(){
        return NEW_LINE + MainMenu.BOOK + NEW_LINE
                + MainMenu.NEWSPAPER + NEW_LINE
                + MainMenu.COMICS + NEW_LINE
                + MainMenu.JOURNAL + NEW_LINE;
    }

    public String initActionsWithItemsMenuText(){
        return NEW_LINE + ADD + NEW_LINE + DELETE +
                NEW_LINE + TAKE + NEW_LINE + RETURN +
                NEW_LINE + SHOW + NEW_LINE;
    }

    public String genSortingMenuText(){
        return NEW_LINE + EXIT_VALUE
                + NEW_LINE + ITEM_ID
                + NEW_LINE + TITLE
                + NEW_LINE + PAGES + NEW_LINE;
    }


    public List<List<String>> itemsToString(List<? extends Item> items, ItemHandler<? extends Item> itemHandler){
        List<List<String>> containersAsStringList = new ArrayList<>();
        items.forEach(i->containersAsStringList.add(itemHandler.itemToString(i)));
        return containersAsStringList;
    }

    public abstract List<List<String>> anyItemsToString(List<T> items);


    public String idToString(Item item){
        return Integer.toString(item.getItemID());
    }

    public String titleToString(Item item){
        return item.getTitle().trim();
    }

    public String pagesToString(Item item){
        return Integer.toString(item.getPages());
    }

    public String borrowedToString(Item item){
        if(item.isBorrowed()){
            return "yes";
        } else return "no";
    }

    public List<String> itemToString(Item item){
        List<String> itemAsList = new ArrayList<>();
        itemAsList.add(idToString(item));
        itemAsList.add(titleToString(item));
        itemAsList.add(pagesToString(item));
        itemAsList.add(borrowedToString(item));
        return itemAsList;
    }

    public String genAddFormContent(){
        HTMLFormBuilder formBuild = new HTMLFormBuilder();
        return formBuild.genLabel("Item ID: ", ITEM_ID_PARAM)
                        + formBuild.genTextField(ITEM_ID_PARAM, ITEM_ID_PARAM)
                        + NEW_LINE_TAG + NEW_LINE_TAG
                        + formBuild.genLabel("Title: ",TITLE_PARAM)
                        + formBuild.genTextField(TITLE_PARAM, TITLE_PARAM)
                        + NEW_LINE_TAG + NEW_LINE_TAG
                        + formBuild.genLabel("Pages: ",PAGES_PARAM)
                        + formBuild.genTextField(PAGES_PARAM, PAGES_PARAM)
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genButton("Add item");
    }

    public String genFormForGettingID(String action){
        HTMLFormBuilder formBuild = new HTMLFormBuilder();
        return formBuild.genForm(
                formBuild.genLabel("Item ID: ", ITEM_ID_PARAM)
                        + formBuild.genTextField(ITEM_ID_PARAM, ITEM_ID_PARAM)
                        + NEW_LINE_TAG + NEW_LINE_TAG
                        + formBuild.genButton(action.toUpperCase()),action);
    }

    public List<String> convertItemParametersMapToList(Map<String, String[]> params){
        List<String> paramsStr = new ArrayList<>();
        for(Map.Entry<String, String[]> param : params.entrySet()){
            paramsStr.addAll(Arrays.asList(param.getValue()));
        }
        return paramsStr;
    }

    public String genSortFormContent() {
        HTMLFormBuilder formBuild = new HTMLFormBuilder();
        return formBuild.genLabel("Choose parameter for sorting", COMPARATOR_PARAM)
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genRadioButton(COMPARATOR_PARAM, ITEM_ID_PARAM,"Item ID")
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genRadioButton(COMPARATOR_PARAM, "title","Title")
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genRadioButton(COMPARATOR_PARAM, "pages","Pages")
                + NEW_LINE_TAG + NEW_LINE_TAG
                + formBuild.genButton("Sort");
    }

    public String genItemChoosingForm(){
        HTMLFormBuilder formBuild = new HTMLFormBuilder();

        StringBuilder radioButtons = new StringBuilder();

        for (Map.Entry<Class<? extends Item>, String> classStringEntry : ItemHandlerProvider.classSimpleNameOfClassMap.entrySet()){
            radioButtons.append(formBuild.genRadioButton(ParametersConstants.TYPE_OF_ITEM, classStringEntry.getValue(), classStringEntry.getValue())).append(NEW_LINE_TAG).append(NEW_LINE_TAG);
        }
        return formBuild.genForm(
                formBuild.genLabel("Choose item:", ParametersConstants.TYPE_OF_ITEM)
                        + NEW_LINE_TAG + NEW_LINE_TAG
                        + radioButtons
                        + NEW_LINE_TAG + NEW_LINE_TAG
                        + formBuild.genButton("Choose"),
                URLConstants.CHOOSE_ITEM_PAGE
        );
    }

    public List<List<String>> getItemsAsStringListFromResultSet(ResultSet resultSet) throws SQLException {
        List<List<String>> itemsStr = new ArrayList<>();
        while (resultSet.next()) {
            List<String> itemStr = new ArrayList<>();
            itemStr = getMainOptions(resultSet,itemStr);
            itemsStr.add(itemStr);
        }
        return itemsStr;
    }

    List<String> getMainOptions(ResultSet resultSet, List<String> itemStr) throws SQLException {
        itemStr.add(Integer.toString(resultSet.getInt("item_id")));
        itemStr.add(resultSet.getString("type_of_item"));
        itemStr.add(resultSet.getString("title"));
        itemStr.add(Integer.toString(resultSet.getInt("pages")));
        String borrowedStr ;
        int borrowedInt = resultSet.getInt("borrowed");
        if (borrowedInt==0) {
            borrowedStr = "true";
        } else borrowedStr = "false";
        itemStr.add(borrowedStr);
        return itemStr;
    }

    public abstract T getItem(int itemID, User user, SQLQueries sqlQueries);



}
