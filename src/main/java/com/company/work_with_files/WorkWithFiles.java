package com.company.work_with_files;

import com.company.Container;
import com.company.handlers.ItemHandler;
import com.company.handlers.ItemHandlerProvider;
import com.company.items.Item;
import com.google.gson.*;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor

public class WorkWithFiles { // FIXME WORK WITH FILES


    public static final String PROGRAM_DIR_NAME_FOR_ITEMS = "book_shelf";
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Path filePath;
    private final String pathToDirectoryAsString = String.valueOf(Paths.get(System.getProperty("user.home"), PROGRAM_DIR_NAME_FOR_ITEMS));

    public WorkWithFiles(String filePath) {
        createDirectoryIfNotExists(Paths.get(pathToDirectoryAsString));
        this.filePath = Paths.get(pathToDirectoryAsString, ("items_" + filePath + ".txt"));
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

    boolean removeItemFromFile(int itemID, boolean forBorrow) throws IOException {
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

    public <T extends Item> List<T> readToAnyItemList(String typeOfCLass) throws IOException {
        createFileIfNotExists();
        List<? extends Item> items = readToItemsList();
        List<T> filteredItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getClass().getSimpleName().equals(typeOfCLass)) {
                filteredItems.add((T) item);
            }
        }
        return filteredItems;
    }

    public List<Item> readToItemsList() throws IOException {
        createFileIfNotExists();
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

    JsonArray readJsonArrayFromFile() throws IOException {
        createFileIfNotExists();
        return gson.fromJson(new FileReader(filePath.toString()), JsonArray.class);
    }

    File createFileIfNotExists() {
        try {
            File file = filePath.toFile();

            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (IOException e){
            return null;
        }
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