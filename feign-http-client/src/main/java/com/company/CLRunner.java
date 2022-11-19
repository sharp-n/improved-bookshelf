package com.company;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CLRunner implements CommandLineRunner {

    final ChoiceHandler choiceHandler;

    public CLRunner(ChoiceHandler choiceHandler) {
        this.choiceHandler = choiceHandler;
    }


    @Override
    public void run(String... args) throws Exception  {
        choiceHandler.getUsersChoice();
    }
}
