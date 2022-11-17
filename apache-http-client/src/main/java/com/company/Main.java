package com.company;

import com.company.methods_handlers.apache_http_client.ApacheHttpClientMethodsHandler;

public class Main {

    public static void main(String[] args)  {
        ChoiceHandler choiceHandler = new ChoiceHandler(new ApacheHttpClientMethodsHandler());
        choiceHandler.getUsersChoice();
    }
}