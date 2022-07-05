package com.company;

import com.google.gson.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WorkWithFiles {

    static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static String SINGLE_FILE_PATH;
    public static String BOOK_FILE_PATH;
    public static String JOURNALS_FILE_PATH;

    public static void setSingleFilePath(String userName) {
        SINGLE_FILE_PATH = System.getProperty("user.home") + "/items_" + userName + ".txt";
    }

    public static void setBookFilePath(String userName) {
        BOOK_FILE_PATH = System.getProperty("user.home") + "/books_" + userName + ".txt";
    }

    public static void setJournalsFilePath(String userName) {
        JOURNALS_FILE_PATH = System.getProperty("user.home") + "/journals_" + userName + ".txt";
    }

    public static void addItemToFile(Container<? extends Item> itemContainer, String filePath) throws IOException {
        List<Container<? extends Item>> containers = readToContainersList(filePath);
        containers.add(itemContainer);
        rewriteFile(containers, filePath);
    }

    static void rewriteFile(List<Container<? extends Item>> containers, String filePath){
        try {
            File file = createFileIfNotExists(filePath);
            FileWriter fw = new FileWriter(filePath, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(gson.toJson(containers));
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Journal> readToJournalsList(String filePath) throws IOException {
        createFileIfNotExists(filePath);
        JsonArray jsonArray = makeJsonArrayFromFile(filePath);
        if(jsonArray==null) return new ArrayList<>();
        List<Journal> journals = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            JsonObject itemObject = element.getAsJsonObject().getAsJsonObject("item");
            String typeOfClass = element.getAsJsonObject().get("typeOfClass").getAsString();
            if (typeOfClass.equals("Journal")) {
                journals.add(gson.fromJson(itemObject, Journal.class));
            }
        }
        return journals;
    }

    public static List<Book> readToBooksList( String filePath) throws IOException {
        createFileIfNotExists(filePath);
        JsonArray jsonArray = makeJsonArrayFromFile(filePath);
        if(jsonArray==null) return new ArrayList<>();
        List<Book> books = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            JsonObject itemObject = element.getAsJsonObject().getAsJsonObject("item");
            String typeOfClass = element.getAsJsonObject().get("typeOfClass").getAsString();
            if (typeOfClass.equals("Book")) {
                books.add(gson.fromJson(itemObject, Book.class));
            }
        }
        return books;
    }

    public static List<Container<? extends Item>> readToContainersList(String filePath) throws IOException {
        createFileIfNotExists(filePath);
        JsonArray jsonArray = makeJsonArrayFromFile(filePath);
        List<Container<? extends Item>> containers = new ArrayList<>();
        if(jsonArray != null) {
            for (JsonElement element : jsonArray) {
                containers.add(gson.fromJson(element, Container.class));
            }
        }
        return containers;
    }

    static JsonArray makeJsonArrayFromFile(String filePath) throws IOException {
        createFileIfNotExists(filePath);
        return  gson.fromJson(new FileReader(filePath), JsonArray.class);
    }

    static File createFileIfNotExists(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) file.createNewFile();
        return file;
    }
}