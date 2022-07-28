package com.company.work_with_files;

import java.nio.file.Paths;

public class WorkWithFilePerType extends WorkWithFiles {

    String typeOfClass;

    public WorkWithFilePerType(String userName, String directoryName, String typeOfItem){
        super(userName, directoryName);
        this.typeOfClass = typeOfItem;
    }

    public void genFilePath(){
        filePath = Paths.get(pathToDirectoryAsString, (userName + "_" + typeOfClass.toLowerCase() + ".txt"));
    }

}
