package com.company;

import com.company.methods_handlers.ApacheHttpClientMethodsHandler;
import com.company.methods_handlers.RestTemplateHttpClientMethodsHandler;
import org.springframework.web.client.RestTemplate;

public class Main {
    public static void main(String[] args) {
        ChoiceHandler choiceHandler = new ChoiceHandler(new RestTemplateHttpClientMethodsHandler(new RestTemplate()));
        choiceHandler.getUsersChoice();
    }

}