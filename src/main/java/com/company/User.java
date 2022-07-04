package com.company;

import lombok.AllArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class User {

    String userName;

    static boolean checkUserNameForValidity(String userName){
        Pattern pattern= Pattern.compile("^[a-zA-Z0-9._-]{4,}$");
        Matcher matcher = pattern.matcher(userName);
        return matcher.find();
    }


}
