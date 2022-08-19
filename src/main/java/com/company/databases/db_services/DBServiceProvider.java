package com.company.databases.db_services;

import com.company.enums.FilesMenu;

import java.util.HashMap;
import java.util.Map;

public class DBServiceProvider {

    public static final Map<String,DBService> optionDbServiceMap = new HashMap<>();

    static{
        optionDbServiceMap.put(FilesMenu.DATABASE_SQLITE.getServletParameter(),new SQLiteDBService());
        optionDbServiceMap.put(FilesMenu.DATABASE_MYSQL.getServletParameter(),new MySQLDBService());
    }

    public static DBService getDBServiceByOption(String option) {
        for (Map.Entry<String, DBService> optionDPServiceEntry : optionDbServiceMap.entrySet()) {
            if (optionDPServiceEntry.getKey().equals(option)) {
                return optionDPServiceEntry.getValue();
            }
        }
        return null;// TODO fix null
    }

    public static String getOptionByDBService(DBService dbService) {
        for (Map.Entry<String, DBService> optionDPServiceEntry : optionDbServiceMap.entrySet()) {
            if (optionDPServiceEntry.getValue().getClass().isAssignableFrom(dbService.getClass())) {
                return optionDPServiceEntry.getKey();
            }
        }
        return null;// TODO fix null
    }

}
