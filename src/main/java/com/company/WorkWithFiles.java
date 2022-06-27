package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkWithFiles {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_PATH = "items.txt";

    public static void writeList(List<? extends Item> items) {
        try {
            List<List<? extends Item>> lists = new ArrayList<>();
            if(items.get(0) instanceof Book){
                List<Journal> journals = readToJournalsList();
                lists.add(items);
                lists.add(journals);
            } else {
                List<Book> journals = readToBooksList();
                lists.add(journals);
                lists.add(items);
            }
            FileWriter fw = new FileWriter(FILE_PATH, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(gson.toJson(lists));
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Journal> readToJournalsList() throws IOException {
        Type type = new TypeToken<List<List<Journal>>>() {}.getType();
        List<List<Journal>> lists = gson.fromJson(new FileReader(FILE_PATH), type);
        if(lists==null) {
            return new ArrayList<>();
        }
        return lists.get(1);
    }

    public static List<Book> readToBooksList() throws IOException {
        Type type = new TypeToken<List<List<Book>>>() {}.getType();
        List<List<Book>> lists = gson.fromJson(new FileReader(FILE_PATH), type);
        if(lists==null) {
            return new ArrayList<>();
        }
        return lists.get(0);
    }

}