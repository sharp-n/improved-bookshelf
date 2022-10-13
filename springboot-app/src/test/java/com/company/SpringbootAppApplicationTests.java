package com.company;

import com.company.db.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@ActiveProfiles("test")
public class SpringbootAppApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        userRepository.addUser("yana");
        userRepository.addUser("sof");
        userRepository.getAllElements().forEach(System.out::println);
    }

}
