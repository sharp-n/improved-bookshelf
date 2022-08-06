package com.company.tomcat_server.servlet_service;

import org.apache.http.client.utils.URIBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ServletService {

    public final Path pathToHTMLFilesDir = Paths.get("src/main/webapp");

    public String getTextFromFile(Path filePath) {
        try {
            File file = filePath.toFile();
            String htmlCode = "";
            Scanner in = new Scanner(file);
            while (in.hasNext()) {
                htmlCode += in.nextLine() + " " + "\n"; // TODO fix concatenation
            }
            in.close();
            return htmlCode;
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
        paths.put("{{URL-ADD}}","add");
        paths.put("{{URL-DELETE}}","delete");
        paths.put("{{URL-TAKE}}","borrow");
        paths.put("{{URL-RETURN}}","return");
        paths.put("{{URL-SHOW}}","show");
        return paths;
    }

    public String replaceURLInTemplate(String htmlCode, Map<String, String> paths){
        for (Map.Entry<String, String> path : paths.entrySet()) {
            htmlCode = htmlCode.replace(path.getKey(),path.getValue());
        }
        return htmlCode;
    }

    public Map<String, String> addParamsToParametersMapValues(Map<String,String> paths, String nameParam, String typeOfFileWorkParam, String typeOfItemParam){
        for(Map.Entry<String,String> path : paths.entrySet()){
            path.setValue(new ServletService().addParams(nameParam,typeOfFileWorkParam,typeOfItemParam).setPathSegments(path.getValue()).toString());
        }
        return paths;
    }

    public String replaceURLTemplatesInInformPage(String htmlCode, String name, String typeOfFileWork, String typeOfItem){
        return htmlCode.replace(
                        "{{URL-ITEMS-MENU}}",
                        new URIBuilder()
                                .setPath(URLConstants.CHOOSE_ITEM_PAGE)
                                .addParameter(ParametersConstants.NAME,name)
                                .addParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE,typeOfFileWork)
                                .toString())
                .replace(
                        "{{URL-ACTIONS}}",
                        new URIBuilder()
                                .setPath(URLConstants.CHOOSE_ACTION)
                                .addParameter(ParametersConstants.NAME,name)
                                .addParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE,typeOfFileWork)
                                .addParameter(ParametersConstants.TYPE_OF_ITEM,typeOfItem)
                                .toString()
                );
    }

}
