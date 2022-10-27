package com.company.handlers.work_with_files;

import com.company.Book;
import com.company.Container;
import com.company.Item;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.google.gson.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class FilesWorker {

//    private static final Logger log
//            = Logger.getLogger(FilesWorker.class);

    public static final String PROGRAM_DIR_NAME_FOR_ITEMS = "book_shelf";
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Path filePath;
    public String userName;
    protected String pathToDirectoryAsString;

    public abstract void genFilePath();

    protected FilesWorker(String root, String userName) {
        this.userName = userName;
        createDirectoryIfNotExists(Paths.get(root, PROGRAM_DIR_NAME_FOR_ITEMS));
        pathToDirectoryAsString = String.valueOf(Paths.get(root, PROGRAM_DIR_NAME_FOR_ITEMS, generateDirectoryForUser()));
        createDirectoryIfNotExists(Paths.get(pathToDirectoryAsString));
    }

    private String generateDirectoryForUser() {
        return userName + "_directory";
    }

    public synchronized void addItemToFile(Item itemToAdd) {
        List<Item> items = readToItemsList();
        items.add(itemToAdd);
        List<Container<? extends Item>> containers = convertToContainer(items);
        rewriteFile(containers);
    }

    void rewriteFile(List<Container<? extends Item>> containers) {
        try {
            File file = createFileIfNotExists(filePath);
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(gson.toJson(containers));
            bw.close();
        } catch (IOException ioException) {
//            log.error(ioException.getMessage() + " : " + FilesWorker.class.getSimpleName() + " : rewriteFile()");
        }
    }

    public boolean removeItemFromFile(int itemID, boolean forBorrow) {
        List<Item> items = readToItemsList();
        for (Item item : items) {
            if (item.getItemID() == itemID) {
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

    public <T extends Item> List<T> readToAnyItemList(String typeOfCLass) {
        createFileIfNotExists(filePath);
        List<? extends Item> items = readToItemsList();
        List<T> filteredItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getClass().getSimpleName().equals(typeOfCLass)) {
                filteredItems.add((T) item);
            }
        }
        return filteredItems;
    }

    public List<Item> readToItemsList() {
        createFileIfNotExists(filePath);
        JsonArray jsonArray = readJsonArrayFromFile();
        List<Item> items = new ArrayList<>();
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                JsonObject itemObject = element.getAsJsonObject().getAsJsonObject("item");
                // TODO use options to check for null
                String typeOfClass = element.getAsJsonObject().get("typeOfClass").getAsString();

                items.add(gson.fromJson(itemObject, ItemHandlerProvider.getClassBySimpleNameOfClass(typeOfClass)));
            }
        }
        return items;
    }

    JsonArray readJsonArrayFromFile() {
        try {
            createFileIfNotExists(filePath);
            return gson.fromJson(new FileReader(filePath.toString()), JsonArray.class);
        } catch (IOException ioException){
//            log.error(ioException.getMessage()  + " : " + FilesWorker.class.getSimpleName() + " : readJsonArrayFromFile()");
            return new JsonArray();
        }
    }

    public File createFileIfNotExists(Path filePath) {
        try {
            File file = filePath.toFile();

            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (IOException ioException) {
//            log.error(ioException.getMessage() + " : " + FilesWorker.class.getSimpleName() + " : createFileIfNotExists()");
            return null;
        }
    }

    void createDirectoryIfNotExists(Path path) {
        try {
            File folder = new File(path.toString());
            if (!folder.exists()) {
                Files.createDirectories(path);
            }
        } catch (IOException ioException){
//            log.error(ioException.getMessage() + " : " + FilesWorker.class.getSimpleName() + " : createFileIfNotExists()");
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