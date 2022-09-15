package com.company.springbootapp.constants;

import java.util.HashMap;
import java.util.Map;

public class MessagesAndTitlesConstants {

    public static final Map<Boolean,String> successFailMessageMap = new HashMap<>();
    public static final Map<Boolean,String> successFailTitleMap = new HashMap<>();

    static {
        successFailMessageMap.put(true,"Action is successful");
        successFailMessageMap.put(false,"Ooops, action failed. Check yor parameters and try again");

        successFailTitleMap.put(true,"Success");
        successFailTitleMap.put(false,"Fail");
    }
}
