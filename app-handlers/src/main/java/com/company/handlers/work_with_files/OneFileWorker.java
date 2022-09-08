package com.company.handlers.work_with_files;

import com.company.Item;

import java.io.IOException;
import java.nio.file.Paths;

public class OneFileWorker extends FilesWorker {

    public OneFileWorker(String directoryName, String userName){
        super(directoryName, userName);
    }

    public void genFilePath(){
        filePath = Paths.get(pathToDirectoryAsString, ( userName +"_items.txt"));
    }

    @Override
    public synchronized void addItemToFile(Item itemToAdd) throws IOException {
        super.addItemToFile(itemToAdd);
    }
}
