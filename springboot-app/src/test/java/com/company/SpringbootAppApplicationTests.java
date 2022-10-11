package com.company;

import com.company.db.services.UserService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class SpringbootAppApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
//        userService.addUser("yana" + new Random().nextDouble());
//        userService.addUser("sof4"+ new Random().nextDouble());
        userService.getAllElements().forEach(System.out::println);
    }

}
