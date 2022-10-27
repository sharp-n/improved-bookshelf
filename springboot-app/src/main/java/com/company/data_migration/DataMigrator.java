package com.company.data_migration;

import com.company.ParametersForWeb;
import com.company.enums.FilesMenu;
import com.company.handlers.ControllersHandler;
import com.company.handlers.ProjectHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DataMigrator {

    ParametersForWeb params;
    ControllersHandler controllersHandler;

    public void updateStorages(){
        FilesMenu option = FilesMenu.getByParameter(params.getTypeOfWork());
        switch(option){
            case ONE_FILE:
                updateFilePerItem();
                updateSQLiteDb();
                updateMySqlDb();
                break;
            case FILE_PER_ITEM:
                updateOneFile();
                updateMySqlDb();
                updateSQLiteDb();
                break;
            case DATABASE_MYSQL:
                updateOneFile();
                updateFilePerItem();
                updateSQLiteDb();
                break;
            case DATABASE_SQLITE:
                updateOneFile();
                updateFilePerItem();
                updateMySqlDb();
                break;
        }
    }

    // 1. Get all components from storage
    // 2. Get all components from other storages
    // 3. Compare
    // 4. Make changes

    void updateOneFile(){
        ProjectHandler projectHandler = controllersHandler.initProjectHandler(params);
    }

    void updateFilePerItem(){

    }

    void updateMySqlDb(){

    }

    void updateSQLiteDb(){

    }

}
