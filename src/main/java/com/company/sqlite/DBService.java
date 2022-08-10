package com.company.sqlite;

import com.company.work_with_files.FilesWorker;
import com.company.work_with_files.OneFileWorker;

import java.nio.file.Paths;

public class DBService {

    void createDBIfNotExist(){
        FilesWorker filesWorker = new OneFileWorker(System.getProperty("user.home"),"yana");
        filesWorker.createFileIfNotExists(Paths.get("bookshelf.db"));
    }

}
