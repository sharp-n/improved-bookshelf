package com.company;

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkWithFiles {

    static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static final String SINGLE_FILE_PATH = System.getProperty("user.home") + "\\items.txt";
    static final String BOOK_FILE_PATH = System.getProperty("user.home") + "\\books.txt";
    static final String JOURNALS_FILE_PATH = System.getProperty("user.home") + "\\journals.txt";

    public static void addItemToFile(Container<? extends Item> itemContainer, String filePath) throws IOException {
        List<Container<? extends Item>> containers = readToContainersList(filePath);
        containers.add(itemContainer);
        rewriteFile(containers, filePath);
    }

    static void rewriteFile(List<Container<? extends Item>> containers, String filePath){
        try {
            File file = new File(filePath);
            if (!file.exists()) file.createNewFile();
            FileWriter fw = new FileWriter(filePath, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(gson.toJson(containers));
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Journal> readToJournalsList(String filePath) throws IOException {
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
        JsonArray jsonArray = makeJsonArrayFromFile(filePath);
        List<Container<? extends Item>> containers = new ArrayList<>();
        if(jsonArray != null) {
            for (JsonElement element : jsonArray) {
                containers.add(gson.fromJson(element, Container.class));
            }
        }
        return containers;
    }

    static JsonArray makeJsonArrayFromFile(String filePath) throws FileNotFoundException {
        return  gson.fromJson(new FileReader(filePath), JsonArray.class);
    }

}