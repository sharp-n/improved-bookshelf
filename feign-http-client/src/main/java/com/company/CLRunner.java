package com.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@SpringBootApplication
@EnableFeignClients
@EntityScan("com.company.db.entities")
@EnableJpaRepositories("com.company.db.repositories")
public class CLRunner implements CommandLineRunner {

    final ChoiceHandler choiceHandler;

    @Autowired
    public CLRunner(ChoiceHandler choiceHandler) {
        this.choiceHandler = choiceHandler;
    }


    @Override
    public void run(String... args) throws Exception  {
        choiceHandler.getUsersChoice();
    }

    public static void main(String [] args){
        SpringApplication.run(CLRunner.class,args);
    }
}
