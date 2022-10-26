package com.company.data_migration;

import com.company.ParametersForWeb;
import com.company.enums.FilesMenu;
import com.company.utils.Params;
import lombok.AllArgsConstructor;

import java.io.File;

@AllArgsConstructor
public class DataMigrator {

    ParametersForWeb params;

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

    void updateOneFile(){

    }

    void updateFilePerItem(){

    }

    void updateMySqlDb(){

    }

    void updateSQLiteDb(){

    }


}
