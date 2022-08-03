package com.company.tomcat_server.servlet_service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ServletService {

    public final Path pathToHTMLFilesDir = Paths.get("src/main/webapp");

    public String getTextFromFile(Path filePath) { // TODO refactor usage
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


}
