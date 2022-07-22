package com.company;

import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.items.Newspaper;
import com.google.gson.*;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor

public class WorkWithFiles {

    public static final String PROGRAM_DIR_NAME_FOR_ITEMS = "book_shelf";
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Path filePath;
    private final String pathToDirectoryAsString = String.valueOf(Paths.get(System.getProperty("user.home"), PROGRAM_DIR_NAME_FOR_ITEMS));
            //= System.getProperty("user.home") + "/book_shelf"; //TODO fix files

    public WorkWithFiles(String filePath) {
        createDirectoryIfNotExists(Paths.get(pathToDirectoryAsString));
        this.filePath = Paths.get(pathToDirectoryAsString, ("items_" + filePath + ".txt"));
                //.get(pathToDirectoryAsString + "/items_" + filePath + ".txt");
    }

    {
        if (filePath == null) {
            createDirectoryIfNotExists(Paths.get(pathToDirectoryAsString));
            filePath = Paths.get(pathToDirectoryAsString, "items_default.txt");

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
            if (item.getClass().getSimpleName().equals(typeOfItem) && item.getItemID() == itemID) {
                if (!forBorrow && item.isBorrowed()) {
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
        List<? extends Item> items = readToItemsList();
        List<Journal> journals = new ArrayList<>();
        for(Item item:items){
            if(item instanceof Journal){
                journals.add((Journal) item);
            }
        }
        return journals;
    }

    public List<Book> readToBooksList() throws IOException {
        createFileIfNotExists();
        List<? extends Item> items = readToItemsList();
        List<Book> books = new ArrayList<>();
        for(Item item:items){
            if(item instanceof Book){
                books.add((Book) item);
            }
        }
        return books;
    }

    public List<Newspaper> readToNewspapersList() throws IOException {
        List<? extends Item> items = readToItemsList();
        List<Newspaper> newspapers = new ArrayList<>();
        for(Item item:items){
            if(item instanceof Newspaper){
                newspapers.add((Newspaper) item);
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

    void createDirectoryIfNotExists(Path path) {
            if (!Files.exists(path)) {
                new File(pathToDirectoryAsString).mkdir();
            }
    }

    List<Container<? extends Item>> convertToContainer(List<Item> items) {
        List<Container<? extends Item>> containers = new ArrayList<>();
        for (Item item : items) {
            containers.add(new Container<>(item));
        }
        return containers;
    }


}