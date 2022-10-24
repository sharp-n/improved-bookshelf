package com.company.handlers.item_handlers;

import com.company.*;
import com.company.databases.queries.SQLQueries;
import com.company.db.repositories.ItemRepository;
import com.company.db.repositories.UserRepository;
import com.company.db.services.ItemService;
import com.company.db.services.UserService;
import com.company.enums.ActionsWithItem;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.Librarian;
import com.company.table.TableUtil;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@NoArgsConstructor
public abstract class ItemHandler<T extends Item> {

    //
    // journals deleting - 3.40 - with tests, 1.4- without
    // adding comics - 15.0
    // adding journals - 3.31
    //

    private static final Logger log
            = Logger.getLogger(ItemHandler.class);

    Scanner in; // TODO fix input/output
    PrintWriter out;
    public Validator validator;
    public UserInput userInput;

    public List<String> columnTitles = new ArrayList<>(Arrays.asList("item id", "type of item","title","pages","borrowed","author", "publishing date", "publisher"));

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

    public List<String> getItem(Integer itemID) {

        String title = validator.validateTitle(userInput.titleUserInput());
        if (!Librarian.checkItemForValidity(title)) return Collections.emptyList();

        Integer numOfPages = validator.validatePages(userInput.pagesUsersInput());
        if (numOfPages == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(itemID.toString(), title, numOfPages.toString());
    }

    public String initItemsMenuText(){
        return TableUtil.NEW_LINE + MainMenu.BOOK + TableUtil.NEW_LINE
                + MainMenu.NEWSPAPER + TableUtil.NEW_LINE
                + MainMenu.COMICS + TableUtil.NEW_LINE
                + MainMenu.JOURNAL + TableUtil.NEW_LINE
                + MainMenu.SHOW_ALL_THE_ITEMS + TableUtil.NEW_LINE;
    }

    public String initActionsWithItemsMenuText(){
        return TableUtil.NEW_LINE + ActionsWithItem.ADD + TableUtil.NEW_LINE + ActionsWithItem.DELETE +
                TableUtil.NEW_LINE + ActionsWithItem.TAKE + TableUtil.NEW_LINE + ActionsWithItem.RETURN +
                TableUtil.NEW_LINE + ActionsWithItem.SHOW + TableUtil.NEW_LINE;
    }

    public String genSortingMenuText(){
        return TableUtil.NEW_LINE + ActionsWithItem.EXIT_VALUE
                + TableUtil.NEW_LINE + SortingMenu.ITEM_ID
                + TableUtil.NEW_LINE + SortingMenu.TITLE
                + TableUtil.NEW_LINE + SortingMenu.PAGES + TableUtil.NEW_LINE;
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
        itemAsList.add(item.getClass().getSimpleName());
        itemAsList.add(titleToString(item));
        itemAsList.add(pagesToString(item));
        itemAsList.add(borrowedToString(item));
        return itemAsList;
    }

    public String genAddFormContent(){
        HTMLFormBuilder formBuild = new HTMLFormBuilder();
        return formBuild.genLabel("Item ID: ", SortingMenu.ITEM_ID.getDbColumn())
                        + formBuild.genTextField(SortingMenu.ITEM_ID.getDbColumn(), SortingMenu.ITEM_ID.getDbColumn())
                        + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
                        + formBuild.genLabel("Title: ", SortingMenu.TITLE.getDbColumn())
                        + formBuild.genTextField( SortingMenu.TITLE.getDbColumn(),  SortingMenu.TITLE.getDbColumn())
                        + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
                        + formBuild.genLabel("Pages: ", SortingMenu.PAGES.getDbColumn())
                        + formBuild.genTextField(SortingMenu.PAGES.getDbColumn(), SortingMenu.PAGES.getDbColumn())
                + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
                + formBuild.genButton("Add item");
    }

    public String genFormForGettingID(String action){
        HTMLFormBuilder formBuild = new HTMLFormBuilder();
        return formBuild.genForm(
                formBuild.genLabel("Item ID: ", SortingMenu.ITEM_ID.getDbColumn())
                        + formBuild.genTextField(SortingMenu.ITEM_ID.getDbColumn(), SortingMenu.ITEM_ID.getDbColumn())
                        + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
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
        return formBuild.genLabel("Choose parameter for sorting", ParametersConstants.COMPARATOR_PARAM)
                + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
                + formBuild.genRadioButton(ParametersConstants.COMPARATOR_PARAM, SortingMenu.ITEM_ID.getDbColumn(), SortingMenu.ITEM_ID.getOption())
                + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
                + formBuild.genRadioButton(ParametersConstants.COMPARATOR_PARAM, SortingMenu.TITLE.getDbColumn(), SortingMenu.TITLE.getOption())
                + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
                + formBuild.genRadioButton(ParametersConstants.COMPARATOR_PARAM, SortingMenu.PAGES.getDbColumn(), SortingMenu.PAGES.getOption())
                + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
                + formBuild.genButton("Sort");
    }

    public String genItemChoosingForm(){
        HTMLFormBuilder formBuild = new HTMLFormBuilder();

        StringBuilder radioButtons = new StringBuilder();

        for (Map.Entry<Class<? extends Item>, String> classStringEntry : ItemHandlerProvider.classSimpleNameOfClassMap.entrySet()){
            radioButtons.append(formBuild.genRadioButton(ParametersConstants.TYPE_OF_ITEM, classStringEntry.getValue(), classStringEntry.getValue())).append(HTMLFormBuilder.NEW_LINE_TAG).append(HTMLFormBuilder.NEW_LINE_TAG);
        }
        return formBuild.genForm(
                formBuild.genLabel("Choose item:", ParametersConstants.TYPE_OF_ITEM)
                        + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
                        + radioButtons
                        + HTMLFormBuilder.NEW_LINE_TAG + HTMLFormBuilder.NEW_LINE_TAG
                        + formBuild.genButton("Choose"),
                URLConstants.CHOOSE_ITEM_PAGE
        );
    }

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
            log.error(sqlException.getMessage() + " : " + ItemHandler.class.getSimpleName() + " : getItemsAsStringListFromResultSet()");
            return Collections.emptyList();
        }
    }

    List<String> getMainOptions(ResultSet resultSet, List<String> itemStr) {
        try {
            itemStr.add(Integer.toString(resultSet.getInt("item_id")));
            itemStr.add(resultSet.getString("type_of_item"));
            itemStr.add(resultSet.getString("title"));
            itemStr.add(Integer.toString(resultSet.getInt("pages")));
            String borrowedStr;
            int borrowedInt = resultSet.getInt("borrowed");
            if (borrowedInt == 1) {
                borrowedStr = "true";
            } else borrowedStr = "false";
            itemStr.add(borrowedStr);
            return itemStr;
        } catch (SQLException sqlException){
            log.error(sqlException.getMessage() + " : " + ItemHandler.class.getSimpleName() + " : getItemsAsStringListFromResultSet()");
            return itemStr;
        }
    }

    public abstract T getItem(int itemID, User user, SQLQueries sqlQueries);


    public abstract List<Item> convertToCoreDefinedTypeOfItems(List<com.company.db.entities.Item> items);

    public abstract Item convertToCoreDefinedItem(com.company.db.entities.Item item);

    public List<Item> convertToCoreItems(List<com.company.db.entities.Item> items){
        List<Item> itemsCore = new ArrayList<>();

        items.forEach(item -> itemsCore
                .add(Objects.requireNonNull(
                        ItemHandlerProvider.getHandlerByClass(
                                ItemHandlerProvider.getClassBySimpleNameOfClass(item.getTypeOfItem())))
                        .convertToCoreDefinedItem(item)));
        return itemsCore;
    }

    public boolean addItemToDB(Item item, String userName, ItemService itemService, UserService userService){
        try {
            itemService.addItem(item, userName,userService);
            return true;
        } catch (Exception e){
            log.error(e.getMessage() + " : " + ItemHandler.class.getSimpleName() + " : addItemToDB()");
            return false;
        }
    }

}
