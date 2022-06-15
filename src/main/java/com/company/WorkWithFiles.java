package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkWithFiles {

    public static void write(String text, String filePath, boolean append) {
        try (FileWriter fw = new FileWriter(filePath, append);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw))
        {
            out.println(text);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<String> readToList(String filePath) {
        try {
            BufferedReader reader = read(filePath);
            String line = " ";
            List<String> items = new ArrayList<>();
            while (line != null) {
                line = reader.readLine();
                if (line!=null&&!line.equals("")) {
                    items.add(line);
                }
            }
            return items;
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void clearFile(String filePath) {
        try
        {
            FileWriter fw = new FileWriter(filePath);
            BufferedWriter out = new BufferedWriter(fw);
            out.write("");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    private static BufferedReader read(String filePath){
        try{
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            return new BufferedReader(fr);
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}