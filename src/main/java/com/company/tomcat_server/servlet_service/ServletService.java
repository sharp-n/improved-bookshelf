package com.company.tomcat_server.servlet_service;

import com.company.User;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.Librarian;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.items.Item;
import com.company.table.HtmlTableBuilder;
import com.company.tomcat_server.constants.FileNameConstants;
import com.company.tomcat_server.constants.ParametersConstants;
import com.company.tomcat_server.constants.TemplatesConstants;
import com.company.tomcat_server.constants.URLConstants;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ServletService {

    public final Path pathToHTMLFilesDir = Paths.get("src/main/webapp");

    public String getTextFromFile(Path filePath) {
        try {
            File file = filePath.toFile();
            StringBuilder htmlCode = new StringBuilder();
            Scanner in = new Scanner(file);
            while (in.hasNext()) {
                htmlCode.append(in.nextLine()).append(" ").append("\n");
            }
            in.close();
            return htmlCode.toString();
        } catch (FileNotFoundException fileNotFoundException){
            return null; // TODO FIX null
        }

    }

    public URIBuilder addParams(String name, String typeOfFileWork, String typeOfItem){
        return new URIBuilder()
                .addParameter(ParametersConstants.NAME, name)
                .addParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE, typeOfFileWork)
                .addParameter(ParametersConstants.TYPE_OF_ITEM,typeOfItem);
    }

    public Map<String,String> initURLTemplatesMap(){
        Map<String, String> paths = new HashMap<>();
        paths.put(TemplatesConstants.URL_ADD_TEMPLATE,"add");
        paths.put(TemplatesConstants.URL_DELETE_TEMPLATE,"delete");
        paths.put(TemplatesConstants.URL_TAKE_TEMPLATE,"borrow");
        paths.put(TemplatesConstants.URL_RETURN_TEMPLATE,"return");
        paths.put(TemplatesConstants.URL_SHOW_TEMPLATE,"show");
        return paths;
    }

    public String replaceURLInTemplate(String htmlCode, Map<String, String> paths){
        for (Map.Entry<String, String> path : paths.entrySet()) {
            htmlCode = htmlCode.replace(path.getKey(),path.getValue());
        }
        return htmlCode;
    }

    public Map<String, String> addParamsToParametersMapValues(Map<String,String> paths, ParametersFromURL param){
        for(Map.Entry<String,String> path : paths.entrySet()){
            path.setValue(new ServletService().addParams(param.name,param.typeOfFileWork,param.typeOfItem).setPathSegments(path.getValue()).toString());
        }
        return paths;
    }

    public String replaceURLTemplatesInActionsPage(String htmlCode, ParametersFromURL param){
        htmlCode = replaceTemplateByURL(htmlCode,TemplatesConstants.URL_ITEMS_MENU_TEMPLATE,URLConstants.CHOOSE_ITEM_PAGE,param);
        htmlCode = replaceTemplateByURL(htmlCode,TemplatesConstants.URL_ACTIONS_TEMPLATE,URLConstants.CHOOSE_ACTION,param);
        return htmlCode;
    }

    public String replaceTemplateByURL(String htmlCode, String template, String path, ParametersFromURL param){
        return htmlCode.replace(
                template,
                new URIBuilder()
                        .setPathSegments(path)
                        .addParameter(ParametersConstants.NAME,param.name)
                        .addParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE,param.typeOfFileWork)
                        .addParameter(ParametersConstants.TYPE_OF_ITEM,param.typeOfItem)
                        .toString());
    }

    public Integer parseParamToInt(String itemIDParam){
        try{
            return Integer.parseInt(itemIDParam);
        } catch (NumberFormatException nfe){
            return -1;
        }

    }

    public void generateAndPrintHTMLCode(HttpServletResponse resp, String message, ParametersFromURL param, String fileName) {
        String htmlCode = getTextFromFile(Paths.get(pathToHTMLFilesDir.toString(), fileName));
        htmlCode = replaceURLTemplatesInActionsPage(htmlCode,param).replace(TemplatesConstants.MESSAGE_TEMPLATE, message);
        printHtmlCode(resp,htmlCode);
    }

    public void printHtmlCode(HttpServletResponse resp,String htmlCode) {
        try {
            ServletOutputStream out = resp.getOutputStream();
            out.write(htmlCode.getBytes());
            out.flush();
            out.close();
        } catch (IOException ioException){
            printErrorPage(resp);
        }
    }

    public void printErrorPage(HttpServletResponse resp){
        String htmlCode = getTextFromFile(Paths.get(pathToHTMLFilesDir.toString(), FileNameConstants.ERROR_PAGE_FILE));
        printHtmlCode(resp, htmlCode);
    }


    public String genTableOfSortedItems(String comparator, ParametersFromURL param) throws IOException {
        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
        projectHandler.itemMenuSwitch(MainMenu.getByOption(param.typeOfItem));
        projectHandler.fileSwitch(FilesMenu.getByOption(param.typeOfFileWork), new User(param.name));
        SortingMenu sortingParam = SortingMenu.getByOption(comparator);
        ItemHandler itemHandler = projectHandler.getItemHandler();
        Librarian librarian = projectHandler.getLibrarian();
        List<Item> items = librarian.initSortingItemsByComparator(librarian.workWithFiles, sortingParam, itemHandler);
        List<List<String>> itemsAsStr = itemHandler.anyItemsToString(items);
        HtmlTableBuilder tableBuilder = new HtmlTableBuilder(itemHandler.getColumnTitles(), itemsAsStr);
        return tableBuilder.generateTable();
    }

    public String buildURLWithParameters(String url, String name, String typeOfFileWork, String typeOfItem){
        URIBuilder uri = new URIBuilder().setPathSegments(url);
        if (name!=null&&!name.trim().equals("")){
            uri.addParameter(ParametersConstants.NAME, name);
        }
        if (typeOfFileWork!=null&&!typeOfFileWork.trim().equals("")){
            uri.addParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE, typeOfFileWork);
        }
        if (typeOfItem!=null&&!typeOfItem.trim().equals("")){
            uri.addParameter(ParametersConstants.TYPE_OF_ITEM, typeOfItem);
        }
        return uri.toString();
    }

}
