package com.company;

import com.company.handlers.ChoiceHandler;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class Main {



    public static void main(String[] args) throws IOException, ParseException {
//        MethodsHandler methodsHandler = new MethodsHandler();
//        methodsHandler.simpleGet();
//        methodsHandler.postForTake();
//        ParametersForWeb params = new ParametersForWeb("User","oneFile","journal");
//        simplePost(params);
        ChoiceHandler choiceHandler = new ChoiceHandler();
        choiceHandler.getUsersChoice();
    }
}