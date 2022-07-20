package com.company;

import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.items.Newspaper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.NoArgsConstructor;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor

public class WorkWithFiles {

    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Path filePath;

    public WorkWithFiles(String filePath) {
        this.filePath = Paths.get(System.getProperty("user.home") + "/items_" + filePath + ".txt");
    }

    {
        if (filePath == null) {
            filePath = Paths.get(System.getProperty("user.home") + "/items_default.txt");
        }
    }

    public synchronized void addItemToFile(Item itemToAdd) throws IOException {
        List<Item> items = readToItemsList();
        items.add(itemToAdd);
        List<Container<? extends Item>> containers = convertToContainer(items);
        rewriteFile(containers);
    }

    void rewriteFile(List<Container<? extends Item>> containers) {
        try {
            File file = createFileIfNotExists();
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(gson.toJson(containers));
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    boolean removeItemFromFile(int itemID, boolean forBorrow, String typeOfItem) throws IOException {
        List<Item> items = readToItemsList();
        for (Item item : items) {
            if (item.getClass().getSimpleName().equals(typeOfItem) && item.getItemID() == itemID && !forBorrow) {

                if (item.isBorrowed()) {
                    return false;
                }
                items.remove(item);
                rewriteFile(convertToContainer(items));
                return true;
            }
        }
        return false;
    }

    public List<Journal> readToJournalsList() throws IOException {
        createFileIfNotExists();
        JsonArray jsonArray = makeJsonArrayFromFile();
        if (jsonArray == null) {
            return new ArrayList<>();
        }
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

    public List<Book> readToBooksList() throws IOException {
        createFileIfNotExists();
        JsonArray jsonArray = makeJsonArrayFromFile();
        if (jsonArray == null) {
            return new ArrayList<>();
        }
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

    public List<Newspaper> readToNewspapersList() throws IOException {
        createFileIfNotExists();
        JsonArray jsonArray = makeJsonArrayFromFile();
        if (jsonArray == null) {
            return new ArrayList<>();
        }
        List<Newspaper> newspapers = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            JsonObject itemObject = element.getAsJsonObject().getAsJsonObject("item");
            String typeOfClass = element.getAsJsonObject().get("typeOfClass").getAsString();
            if (typeOfClass.equals("Newspaper")) {
                newspapers.add(gson.fromJson(itemObject, Newspaper.class));
            }
        }
        return newspapers;
    }

    public List<Item> readToItemsList() throws IOException {
        createFileIfNotExists();
        JsonArray jsonArray = makeJsonArrayFromFile();
        List<Item> containers = new ArrayList<>();
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                JsonObject itemObject = element.getAsJsonObject().getAsJsonObject("item");
                String typeOfClass = element.getAsJsonObject().get("typeOfClass").getAsString();
                if (typeOfClass.equals("Book")) {
                    containers.add(gson.fromJson(itemObject, Book.class));
                } else if (typeOfClass.equals("Journal")) {
                    containers.add(gson.fromJson(itemObject, Journal.class));
                } else if (typeOfClass.equals("Newspaper")) {
                    containers.add(gson.fromJson(itemObject, Newspaper.class));
                }
            }
        }
        return containers;
    }

    JsonArray makeJsonArrayFromFile() throws IOException {
        createFileIfNotExists();
        return gson.fromJson(new FileReader(filePath.toString()), JsonArray.class);
    }

    File createFileIfNotExists() throws IOException {
        File file = filePath.toFile();
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    List<Container<? extends Item>> convertToContainer(List<Item> items) {
        List<Container<? extends Item>> containers = new ArrayList<>();
        for (Item item : items) {
            containers.add(new Container<>(item));
        }
        return containers;
    }


}