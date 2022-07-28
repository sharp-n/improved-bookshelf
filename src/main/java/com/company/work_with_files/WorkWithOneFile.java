package com.company.work_with_files;

import com.company.items.Item;

import java.io.IOException;
import java.nio.file.Paths;

public class WorkWithOneFile extends WorkWithFiles {

    public WorkWithOneFile(String userName, String directoryName){
        super(userName, directoryName);
    }

    public void genFilePath(){
        filePath = Paths.get(pathToDirectoryAsString, ( userName +"_items.txt"));
    }

    @Override
    public synchronized void addItemToFile(Item itemToAdd) throws IOException {
        super.addItemToFile(itemToAdd);
    }
}
