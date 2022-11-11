package com.company;

import com.company.methods_handlers.ApacheHttpClientMethodsHandler;

public class Main {

    public static void main(String[] args)  {
        ChoiceHandler choiceHandler = new ChoiceHandler(new ApacheHttpClientMethodsHandler());
        choiceHandler.getUsersChoice();
    }
}