package com.company;

import com.company.methods_handlers.rest_template_http_client.RestTemplateHttpClientMethodsHandler;
import org.springframework.web.client.RestTemplate;

public class Main {
    public static void main(String[] args) {
        ChoiceHandler choiceHandler = new ChoiceHandler(new RestTemplateHttpClientMethodsHandler(new RestTemplate()));
        choiceHandler.getUsersChoice();
    }

}