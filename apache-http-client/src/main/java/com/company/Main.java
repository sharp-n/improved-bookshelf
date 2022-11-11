package com.company;

import com.company.methods_handlers.MethodsHandler;

import java.io.IOException;

public class Main {



    public static void main(String[] args) throws IOException {
//        MethodsHandler methodsHandler = new MethodsHandler();
//        methodsHandler.simpleGet();
//        methodsHandler.postForTake();
//        ParametersForWeb params = new ParametersForWeb("User","oneFile","journal");
//        simplePost(params);
        ChoiceHandler choiceHandler = new ChoiceHandler(new MethodsHandler());
        choiceHandler.getUsersChoice();
    }
}