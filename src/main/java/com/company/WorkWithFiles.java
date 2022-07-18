package com.company;

import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import com.google.gson.*;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor

public class WorkWithFiles {

    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Path filePath;

    public WorkWithFiles(String filePath) {
        this.filePath = Paths.get(System.getProperty("user.home") + "/items_" + filePath + ".txt");
    }

    {
        if (filePath ==null){
            filePath=Paths.get(System.getProperty("user.home") + "/items_default.txt");
        }
    }

    public synchronized void addItemToFile(Container<? extends Item> itemContainer) throws IOException {
        List<Container<? extends Item>> containers = readToContainersList();
        containers.add(itemContainer);
        rewriteFile(containers);
    }

    void rewriteFile(List<Container<? extends Item>> containers){
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

    public List<Journal> readToJournalsList() throws IOException {
        createFileIfNotExists();
        JsonArray jsonArray = makeJsonArrayFromFile();
        if(jsonArray==null) {return new ArrayList<>();}
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
        if(jsonArray==null) {return new ArrayList<>();}
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

    public List<Container<? extends Item>> readToContainersList() throws IOException {
        createFileIfNotExists();
        JsonArray jsonArray = makeJsonArrayFromFile();
        List<Container<? extends Item>> containers = new ArrayList<>();
        if(jsonArray != null) {
            for (JsonElement element : jsonArray) {
                containers.add(gson.fromJson(element, Container.class));
            }
        }
        return containers;
    }

    JsonArray makeJsonArrayFromFile() throws IOException {
        createFileIfNotExists();
        return  gson.fromJson(new FileReader(filePath.toString()), JsonArray.class);
    }

    File createFileIfNotExists() throws IOException {
        File file = filePath.toFile();
        if (!file.exists()) {file.createNewFile();}
        return file;
    }
}