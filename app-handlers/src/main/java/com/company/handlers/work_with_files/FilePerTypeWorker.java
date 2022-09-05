package com.company.handlers.work_with_files;

import java.nio.file.Paths;

public class FilePerTypeWorker extends FilesWorker {

    String typeOfClass;

    public FilePerTypeWorker(String userName, String directoryName, String typeOfItem){
        super(userName, directoryName);
        this.typeOfClass = typeOfItem;
    }

    public void genFilePath(){
        filePath = Paths.get(pathToDirectoryAsString, (userName + "_" + typeOfClass.toLowerCase() + ".txt"));
    }

}
